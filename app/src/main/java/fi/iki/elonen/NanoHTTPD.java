package fi.iki.elonen;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public abstract class NanoHTTPD {
  private static final Pattern CONTENT_DISPOSITION_ATTRIBUTE_PATTERN;
  
  private static final String CONTENT_DISPOSITION_ATTRIBUTE_REGEX = "[ |\t]*([a-zA-Z]*)[ |\t]*=[ |\t]*['|\"]([^\"^']*)['|\"]";
  
  private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile("([ |\t]*Content-Disposition[ |\t]*:)(.*)", 2);
  
  private static final String CONTENT_DISPOSITION_REGEX = "([ |\t]*Content-Disposition[ |\t]*:)(.*)";
  
  private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile("([ |\t]*content-type[ |\t]*:)(.*)", 2);
  
  private static final String CONTENT_TYPE_REGEX = "([ |\t]*content-type[ |\t]*:)(.*)";
  
  private static final Logger LOG;
  
  public static final String MIME_HTML = "text/html";
  
  public static final String MIME_PLAINTEXT = "text/plain";
  
  protected static Map<String, String> MIME_TYPES;
  
  private static final String QUERY_STRING_PARAMETER = "NanoHttpd.QUERY_STRING";
  
  public static final int SOCKET_READ_TIMEOUT = 5000;
  
  protected AsyncRunner asyncRunner;
  
  private final String hostname;
  
  private final int myPort;
  
  private volatile ServerSocket myServerSocket;
  
  private Thread myThread;
  
  private ServerSocketFactory serverSocketFactory = new DefaultServerSocketFactory();
  
  private TempFileManagerFactory tempFileManagerFactory;
  
  static {
    CONTENT_DISPOSITION_ATTRIBUTE_PATTERN = Pattern.compile("[ |\t]*([a-zA-Z]*)[ |\t]*=[ |\t]*['|\"]([^\"^']*)['|\"]");
    LOG = Logger.getLogger(NanoHTTPD.class.getName());
  }
  
  public NanoHTTPD(int paramInt) {
    this(null, paramInt);
  }
  
  public NanoHTTPD(String paramString, int paramInt) {
    this.hostname = paramString;
    this.myPort = paramInt;
    setTempFileManagerFactory(new DefaultTempFileManagerFactory());
    setAsyncRunner(new DefaultAsyncRunner());
  }
  
  protected static Map<String, List<String>> decodeParameters(String paramString) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    if (paramString != null) {
      StringTokenizer stringTokenizer = new StringTokenizer(paramString, "&");
      while (stringTokenizer.hasMoreTokens()) {
        String str1 = stringTokenizer.nextToken();
        int i = str1.indexOf('=');
        if (i >= 0) {
          paramString = decodePercent(str1.substring(0, i));
        } else {
          paramString = decodePercent(str1);
        } 
        String str2 = paramString.trim();
        if (!hashMap.containsKey(str2))
          hashMap.put(str2, new ArrayList()); 
        if (i >= 0) {
          paramString = decodePercent(str1.substring(i + 1));
        } else {
          paramString = null;
        } 
        if (paramString != null)
          ((List<String>)hashMap.get(str2)).add(paramString); 
      } 
    } 
    return (Map)hashMap;
  }
  
  protected static Map<String, List<String>> decodeParameters(Map<String, String> paramMap) {
    return decodeParameters(paramMap.get("NanoHttpd.QUERY_STRING"));
  }
  
  protected static String decodePercent(String paramString) {
    try {
      return URLDecoder.decode(paramString, "UTF8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      LOG.log(Level.WARNING, "Encoding not supported, ignored", unsupportedEncodingException);
      return null;
    } 
  }
  
  public static String getMimeTypeForFile(String paramString) {
    int i = paramString.lastIndexOf('.');
    if (i >= 0) {
      paramString = mimeTypes().get(paramString.substring(i + 1).toLowerCase());
    } else {
      paramString = null;
    } 
    String str = paramString;
    if (paramString == null)
      str = "application/octet-stream"; 
    return str;
  }
  
  private static void loadMimeTypes(Map<String, String> paramMap, String paramString) {
    try {
      Enumeration<URL> enumeration = NanoHTTPD.class.getClassLoader().getResources(paramString);
      while (enumeration.hasMoreElements()) {
        URL uRL = enumeration.nextElement();
        Properties properties = new Properties();
        InputStream inputStream1 = null;
        InputStream inputStream2 = null;
        try {
          InputStream inputStream = uRL.openStream();
          inputStream2 = inputStream;
          inputStream1 = inputStream;
          properties.load(inputStream);
          inputStream1 = inputStream;
          safeClose(inputStream1);
        } catch (IOException iOException) {
          inputStream2 = inputStream1;
          Logger logger = LOG;
          inputStream2 = inputStream1;
          Level level = Level.SEVERE;
          inputStream2 = inputStream1;
          StringBuilder stringBuilder = new StringBuilder();
          inputStream2 = inputStream1;
          stringBuilder.append("could not load mimetypes from ");
          inputStream2 = inputStream1;
          stringBuilder.append(uRL);
          inputStream2 = inputStream1;
          logger.log(level, stringBuilder.toString(), iOException);
          safeClose(inputStream1);
        } finally {}
        paramMap.putAll(properties);
      } 
    } catch (IOException iOException) {
      Logger logger = LOG;
      Level level = Level.INFO;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("no mime types available at ");
      stringBuilder.append(paramString);
      logger.log(level, stringBuilder.toString());
    } 
  }
  
  public static SSLServerSocketFactory makeSSLSocketFactory(String paramString, char[] paramArrayOfchar) throws IOException {
    try {
      KeyManagerFactory keyManagerFactory;
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream inputStream = NanoHTTPD.class.getResourceAsStream(paramString);
      if (inputStream != null) {
        keyStore.load(inputStream, paramArrayOfchar);
        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, paramArrayOfchar);
        return makeSSLSocketFactory(keyStore, keyManagerFactory);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to load keystore from classpath: ");
      stringBuilder.append((String)keyManagerFactory);
      throw new IOException(stringBuilder.toString());
    } catch (Exception exception) {
      throw new IOException(exception.getMessage());
    } 
  }
  
  public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore paramKeyStore, KeyManagerFactory paramKeyManagerFactory) throws IOException {
    try {
      return makeSSLSocketFactory(paramKeyStore, paramKeyManagerFactory.getKeyManagers());
    } catch (Exception exception) {
      throw new IOException(exception.getMessage());
    } 
  }
  
  public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore paramKeyStore, KeyManager[] paramArrayOfKeyManager) throws IOException {
    try {
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(paramKeyStore);
      SSLContext sSLContext = SSLContext.getInstance("TLS");
      sSLContext.init(paramArrayOfKeyManager, trustManagerFactory.getTrustManagers(), null);
      return sSLContext.getServerSocketFactory();
    } catch (Exception exception) {
      throw new IOException(exception.getMessage());
    } 
  }
  
  public static Map<String, String> mimeTypes() {
    if (MIME_TYPES == null) {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      MIME_TYPES = (Map)hashMap;
      loadMimeTypes((Map)hashMap, "META-INF/nanohttpd/default-mimetypes.properties");
      loadMimeTypes(MIME_TYPES, "META-INF/nanohttpd/mimetypes.properties");
      if (MIME_TYPES.isEmpty())
        LOG.log(Level.WARNING, "no mime types found in the classpath! please provide mimetypes.properties"); 
    } 
    return MIME_TYPES;
  }
  
  public static Response newChunkedResponse(Response.IStatus paramIStatus, String paramString, InputStream paramInputStream) {
    return new Response(paramIStatus, paramString, paramInputStream, -1L);
  }
  
  public static Response newFixedLengthResponse(Response.IStatus paramIStatus, String paramString, InputStream paramInputStream, long paramLong) {
    return new Response(paramIStatus, paramString, paramInputStream, paramLong);
  }
  
  public static Response newFixedLengthResponse(Response.IStatus paramIStatus, String paramString1, String paramString2) {
    byte[] arrayOfByte;
    ContentType contentType = new ContentType(paramString1);
    if (paramString2 == null)
      return newFixedLengthResponse(paramIStatus, paramString1, new ByteArrayInputStream(new byte[0]), 0L); 
    try {
      arrayOfByte = paramString2.getBytes(contentType.getEncoding());
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      LOG.log(Level.SEVERE, "UTF-8 encoding problem, responding nothing", unsupportedEncodingException);
      arrayOfByte = new byte[0];
    } 
    return newFixedLengthResponse(paramIStatus, contentType.getContentTypeHeader(), new ByteArrayInputStream(arrayOfByte), arrayOfByte.length);
  }
  
  public static Response newFixedLengthResponse(String paramString) {
    return newFixedLengthResponse(Response.Status.OK, "text/html", paramString);
  }
  
  private static final void safeClose(Object paramObject) {
    if (paramObject != null)
      try {
        if (paramObject instanceof Closeable) {
          ((Closeable)paramObject).close();
          return;
        } 
        if (paramObject instanceof Socket) {
          ((Socket)paramObject).close();
          return;
        } 
        if (paramObject instanceof ServerSocket) {
          ((ServerSocket)paramObject).close();
          return;
        } 
        throw new IllegalArgumentException("Unknown object to close");
      } catch (IOException iOException) {
        LOG.log(Level.SEVERE, "Could not close", iOException);
      }  
  }
  
  public void closeAllConnections() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual stop : ()V
    //   6: aload_0
    //   7: monitorexit
    //   8: return
    //   9: astore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: aload_1
    //   13: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	9	finally
  }
  
  protected ClientHandler createClientHandler(Socket paramSocket, InputStream paramInputStream) {
    return new ClientHandler(paramInputStream, paramSocket);
  }
  
  protected ServerRunnable createServerRunnable(int paramInt) {
    return new ServerRunnable(paramInt);
  }
  
  public String getHostname() {
    return this.hostname;
  }
  
  public final int getListeningPort() {
    return (this.myServerSocket == null) ? -1 : this.myServerSocket.getLocalPort();
  }
  
  public ServerSocketFactory getServerSocketFactory() {
    return this.serverSocketFactory;
  }
  
  public TempFileManagerFactory getTempFileManagerFactory() {
    return this.tempFileManagerFactory;
  }
  
  public final boolean isAlive() {
    return (wasStarted() && !this.myServerSocket.isClosed() && this.myThread.isAlive());
  }
  
  public void makeSecure(SSLServerSocketFactory paramSSLServerSocketFactory, String[] paramArrayOfString) {
    this.serverSocketFactory = new SecureServerSocketFactory(paramSSLServerSocketFactory, paramArrayOfString);
  }
  
  public Response serve(IHTTPSession paramIHTTPSession) {
    Response.Status status;
    StringBuilder stringBuilder;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    Method method = paramIHTTPSession.getMethod();
    if (Method.PUT.equals(method) || Method.POST.equals(method))
      try {
        paramIHTTPSession.parseBody((Map)hashMap);
        Map<String, String> map1 = paramIHTTPSession.getParms();
        map1.put("NanoHttpd.QUERY_STRING", paramIHTTPSession.getQueryParameterString());
        return serve(paramIHTTPSession.getUri(), method, paramIHTTPSession.getHeaders(), map1, (Map)hashMap);
      } catch (IOException iOException) {
        status = Response.Status.INTERNAL_ERROR;
        stringBuilder = new StringBuilder();
        stringBuilder.append("SERVER INTERNAL ERROR: IOException: ");
        stringBuilder.append(iOException.getMessage());
        return newFixedLengthResponse(status, "text/plain", stringBuilder.toString());
      } catch (ResponseException responseException) {
        return newFixedLengthResponse(responseException.getStatus(), "text/plain", responseException.getMessage());
      }  
    Map<String, String> map = responseException.getParms();
    map.put("NanoHttpd.QUERY_STRING", responseException.getQueryParameterString());
    return serve(responseException.getUri(), (Method)stringBuilder, responseException.getHeaders(), map, (Map<String, String>)status);
  }
  
  @Deprecated
  public Response serve(String paramString, Method paramMethod, Map<String, String> paramMap1, Map<String, String> paramMap2, Map<String, String> paramMap3) {
    return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
  }
  
  public void setAsyncRunner(AsyncRunner paramAsyncRunner) {
    this.asyncRunner = paramAsyncRunner;
  }
  
  public void setServerSocketFactory(ServerSocketFactory paramServerSocketFactory) {
    this.serverSocketFactory = paramServerSocketFactory;
  }
  
  public void setTempFileManagerFactory(TempFileManagerFactory paramTempFileManagerFactory) {
    this.tempFileManagerFactory = paramTempFileManagerFactory;
  }
  
  public void start() throws IOException {
    start(5000);
  }
  
  public void start(int paramInt) throws IOException {
    start(paramInt, true);
  }
  
  public void start(int paramInt, boolean paramBoolean) throws IOException {
    this.myServerSocket = getServerSocketFactory().create();
    this.myServerSocket.setReuseAddress(true);
    ServerRunnable serverRunnable = createServerRunnable(paramInt);
    Thread thread = new Thread(serverRunnable);
    this.myThread = thread;
    thread.setDaemon(paramBoolean);
    this.myThread.setName("NanoHttpd Main Listener");
    this.myThread.start();
    while (true) {
      if (!serverRunnable.hasBinded && serverRunnable.bindException == null) {
        try {
          Thread.sleep(10L);
        } finally {}
        continue;
      } 
      if (serverRunnable.bindException == null)
        return; 
      throw serverRunnable.bindException;
    } 
  }
  
  public void stop() {
    try {
      safeClose(this.myServerSocket);
      this.asyncRunner.closeAll();
      if (this.myThread != null) {
        this.myThread.join();
        return;
      } 
    } catch (Exception exception) {
      LOG.log(Level.SEVERE, "Could not stop all connections", exception);
    } 
  }
  
  protected boolean useGzipWhenAccepted(Response paramResponse) {
    return (paramResponse.getMimeType() != null && (paramResponse.getMimeType().toLowerCase().contains("text/") || paramResponse.getMimeType().toLowerCase().contains("/json")));
  }
  
  public final boolean wasStarted() {
    return (this.myServerSocket != null && this.myThread != null);
  }
  
  public static interface AsyncRunner {
    void closeAll();
    
    void closed(NanoHTTPD.ClientHandler param1ClientHandler);
    
    void exec(NanoHTTPD.ClientHandler param1ClientHandler);
  }
  
  public class ClientHandler implements Runnable {
    private final Socket acceptSocket;
    
    private final InputStream inputStream;
    
    public ClientHandler(InputStream param1InputStream, Socket param1Socket) {
      this.inputStream = param1InputStream;
      this.acceptSocket = param1Socket;
    }
    
    public void close() {
      NanoHTTPD.safeClose(this.inputStream);
      NanoHTTPD.safeClose(this.acceptSocket);
    }
    
    public void run() {
      // Byte code:
      //   0: aconst_null
      //   1: astore_1
      //   2: aconst_null
      //   3: astore_2
      //   4: aload_0
      //   5: getfield acceptSocket : Ljava/net/Socket;
      //   8: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
      //   11: astore_3
      //   12: aload_3
      //   13: astore_2
      //   14: aload_3
      //   15: astore_1
      //   16: aload_0
      //   17: getfield this$0 : Lfi/iki/elonen/NanoHTTPD;
      //   20: invokestatic access$100 : (Lfi/iki/elonen/NanoHTTPD;)Lfi/iki/elonen/NanoHTTPD$TempFileManagerFactory;
      //   23: invokeinterface create : ()Lfi/iki/elonen/NanoHTTPD$TempFileManager;
      //   28: astore #4
      //   30: aload_3
      //   31: astore_2
      //   32: aload_3
      //   33: astore_1
      //   34: new fi/iki/elonen/NanoHTTPD$HTTPSession
      //   37: dup
      //   38: aload_0
      //   39: getfield this$0 : Lfi/iki/elonen/NanoHTTPD;
      //   42: aload #4
      //   44: aload_0
      //   45: getfield inputStream : Ljava/io/InputStream;
      //   48: aload_3
      //   49: aload_0
      //   50: getfield acceptSocket : Ljava/net/Socket;
      //   53: invokevirtual getInetAddress : ()Ljava/net/InetAddress;
      //   56: invokespecial <init> : (Lfi/iki/elonen/NanoHTTPD;Lfi/iki/elonen/NanoHTTPD$TempFileManager;Ljava/io/InputStream;Ljava/io/OutputStream;Ljava/net/InetAddress;)V
      //   59: astore #5
      //   61: aload_3
      //   62: astore_2
      //   63: aload_3
      //   64: astore_1
      //   65: aload_3
      //   66: astore #4
      //   68: aload_0
      //   69: getfield acceptSocket : Ljava/net/Socket;
      //   72: invokevirtual isClosed : ()Z
      //   75: ifne -> 150
      //   78: aload_3
      //   79: astore_2
      //   80: aload_3
      //   81: astore_1
      //   82: aload #5
      //   84: invokevirtual execute : ()V
      //   87: goto -> 61
      //   90: astore_1
      //   91: goto -> 183
      //   94: astore_3
      //   95: aload_1
      //   96: astore_2
      //   97: aload_3
      //   98: instanceof java/net/SocketException
      //   101: ifeq -> 121
      //   104: aload_1
      //   105: astore_2
      //   106: aload_1
      //   107: astore #4
      //   109: ldc 'NanoHttpd Shutdown'
      //   111: aload_3
      //   112: invokevirtual getMessage : ()Ljava/lang/String;
      //   115: invokevirtual equals : (Ljava/lang/Object;)Z
      //   118: ifne -> 150
      //   121: aload_1
      //   122: astore_2
      //   123: aload_1
      //   124: astore #4
      //   126: aload_3
      //   127: instanceof java/net/SocketTimeoutException
      //   130: ifne -> 150
      //   133: aload_1
      //   134: astore_2
      //   135: invokestatic access$200 : ()Ljava/util/logging/Logger;
      //   138: getstatic java/util/logging/Level.SEVERE : Ljava/util/logging/Level;
      //   141: ldc 'Communication with the client broken, or an bug in the handler code'
      //   143: aload_3
      //   144: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
      //   147: aload_1
      //   148: astore #4
      //   150: aload #4
      //   152: invokestatic access$000 : (Ljava/lang/Object;)V
      //   155: aload_0
      //   156: getfield inputStream : Ljava/io/InputStream;
      //   159: invokestatic access$000 : (Ljava/lang/Object;)V
      //   162: aload_0
      //   163: getfield acceptSocket : Ljava/net/Socket;
      //   166: invokestatic access$000 : (Ljava/lang/Object;)V
      //   169: aload_0
      //   170: getfield this$0 : Lfi/iki/elonen/NanoHTTPD;
      //   173: getfield asyncRunner : Lfi/iki/elonen/NanoHTTPD$AsyncRunner;
      //   176: aload_0
      //   177: invokeinterface closed : (Lfi/iki/elonen/NanoHTTPD$ClientHandler;)V
      //   182: return
      //   183: aload_2
      //   184: invokestatic access$000 : (Ljava/lang/Object;)V
      //   187: aload_0
      //   188: getfield inputStream : Ljava/io/InputStream;
      //   191: invokestatic access$000 : (Ljava/lang/Object;)V
      //   194: aload_0
      //   195: getfield acceptSocket : Ljava/net/Socket;
      //   198: invokestatic access$000 : (Ljava/lang/Object;)V
      //   201: aload_0
      //   202: getfield this$0 : Lfi/iki/elonen/NanoHTTPD;
      //   205: getfield asyncRunner : Lfi/iki/elonen/NanoHTTPD$AsyncRunner;
      //   208: aload_0
      //   209: invokeinterface closed : (Lfi/iki/elonen/NanoHTTPD$ClientHandler;)V
      //   214: aload_1
      //   215: athrow
      // Exception table:
      //   from	to	target	type
      //   4	12	94	java/lang/Exception
      //   4	12	90	finally
      //   16	30	94	java/lang/Exception
      //   16	30	90	finally
      //   34	61	94	java/lang/Exception
      //   34	61	90	finally
      //   68	78	94	java/lang/Exception
      //   68	78	90	finally
      //   82	87	94	java/lang/Exception
      //   82	87	90	finally
      //   97	104	90	finally
      //   109	121	90	finally
      //   126	133	90	finally
      //   135	147	90	finally
    }
  }
  
  protected static class ContentType {
    private static final String ASCII_ENCODING = "US-ASCII";
    
    private static final Pattern BOUNDARY_PATTERN;
    
    private static final String BOUNDARY_REGEX = "[ |\t]*(boundary)[ |\t]*=[ |\t]*['|\"]?([^\"^'^;^,]*)['|\"]?";
    
    private static final Pattern CHARSET_PATTERN = Pattern.compile("[ |\t]*(charset)[ |\t]*=[ |\t]*['|\"]?([^\"^'^;^,]*)['|\"]?", 2);
    
    private static final String CHARSET_REGEX = "[ |\t]*(charset)[ |\t]*=[ |\t]*['|\"]?([^\"^'^;^,]*)['|\"]?";
    
    private static final String CONTENT_REGEX = "[ |\t]*([^/^ ^;^,]+/[^ ^;^,]+)";
    
    private static final Pattern MIME_PATTERN = Pattern.compile("[ |\t]*([^/^ ^;^,]+/[^ ^;^,]+)", 2);
    
    private static final String MULTIPART_FORM_DATA_HEADER = "multipart/form-data";
    
    private static final String UTF8_ENCODING = "UTF-8";
    
    private final String boundary;
    
    private final String contentType;
    
    private final String contentTypeHeader;
    
    private final String encoding;
    
    static {
      BOUNDARY_PATTERN = Pattern.compile("[ |\t]*(boundary)[ |\t]*=[ |\t]*['|\"]?([^\"^'^;^,]*)['|\"]?", 2);
    }
    
    public ContentType(String param1String) {
      this.contentTypeHeader = param1String;
      if (param1String != null) {
        this.contentType = getDetailFromContentHeader(param1String, MIME_PATTERN, "", 1);
        String str = getDetailFromContentHeader(param1String, CHARSET_PATTERN, null, 2);
        if (str != null && !str.equals("UTF-8"))
          NanoHTTPD.LOG.log(Level.WARNING, "A charset other than UTF-8 was specified in the server response, however the server will respond with UTF-8"); 
        this.encoding = "UTF-8";
      } else {
        this.contentType = "";
        this.encoding = "UTF-8";
      } 
      if ("multipart/form-data".equalsIgnoreCase(this.contentType)) {
        this.boundary = getDetailFromContentHeader(param1String, BOUNDARY_PATTERN, null, 2);
        return;
      } 
      this.boundary = null;
    }
    
    private String getDetailFromContentHeader(String param1String1, Pattern param1Pattern, String param1String2, int param1Int) {
      Matcher matcher = param1Pattern.matcher(param1String1);
      if (matcher.find())
        param1String2 = matcher.group(param1Int); 
      return param1String2;
    }
    
    public String getBoundary() {
      return this.boundary;
    }
    
    public String getContentType() {
      return this.contentType;
    }
    
    public String getContentTypeHeader() {
      return this.contentTypeHeader;
    }
    
    public String getEncoding() {
      String str2 = this.encoding;
      String str1 = str2;
      if (str2 == null)
        str1 = "UTF-8"; 
      return str1;
    }
    
    public boolean isMultipart() {
      return "multipart/form-data".equalsIgnoreCase(this.contentType);
    }
    
    public ContentType tryUTF8() {
      if (this.encoding == null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.contentTypeHeader);
        stringBuilder.append("; charset=UTF-8");
        return new ContentType(stringBuilder.toString());
      } 
      return this;
    }
  }
  
  public static class Cookie {
    protected final String e;
    
    protected final String n;
    
    protected final String v;
    
    public Cookie(String param1String1, String param1String2) {
      this(param1String1, param1String2, 30);
    }
    
    public Cookie(String param1String1, String param1String2, int param1Int) {
      this.n = param1String1;
      this.v = param1String2;
      this.e = getHTTPTime(param1Int);
    }
    
    public Cookie(String param1String1, String param1String2, String param1String3) {
      this.n = param1String1;
      this.v = param1String2;
      this.e = param1String3;
    }
    
    public static String getHTTPTime(int param1Int) {
      Calendar calendar = Calendar.getInstance();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
      simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      calendar.add(5, param1Int);
      return simpleDateFormat.format(calendar.getTime());
    }
    
    public String getHTTPHeader() {
      return String.format("%s=%s; expires=%s", new Object[] { this.n, this.v, this.e });
    }
  }
  
  public class CookieHandler implements Iterable<String> {
    private final HashMap<String, String> cookies = new HashMap<String, String>();
    
    private final ArrayList<NanoHTTPD.Cookie> queue = new ArrayList<NanoHTTPD.Cookie>();
    
    public CookieHandler(Map<String, String> param1Map) {
      String str = param1Map.get("cookie");
      if (str != null) {
        String[] arrayOfString = str.split(";");
        int j = arrayOfString.length;
        for (int i = 0; i < j; i++) {
          String[] arrayOfString1 = arrayOfString[i].trim().split("=");
          if (arrayOfString1.length == 2)
            this.cookies.put(arrayOfString1[0], arrayOfString1[1]); 
        } 
      } 
    }
    
    public void delete(String param1String) {
      set(param1String, "-delete-", -30);
    }
    
    public Iterator<String> iterator() {
      return this.cookies.keySet().iterator();
    }
    
    public String read(String param1String) {
      return this.cookies.get(param1String);
    }
    
    public void set(NanoHTTPD.Cookie param1Cookie) {
      this.queue.add(param1Cookie);
    }
    
    public void set(String param1String1, String param1String2, int param1Int) {
      this.queue.add(new NanoHTTPD.Cookie(param1String1, param1String2, NanoHTTPD.Cookie.getHTTPTime(param1Int)));
    }
    
    public void unloadQueue(NanoHTTPD.Response param1Response) {
      Iterator<NanoHTTPD.Cookie> iterator = this.queue.iterator();
      while (iterator.hasNext())
        param1Response.addHeader("Set-Cookie", ((NanoHTTPD.Cookie)iterator.next()).getHTTPHeader()); 
    }
  }
  
  public static class DefaultAsyncRunner implements AsyncRunner {
    private long requestCount;
    
    private final List<NanoHTTPD.ClientHandler> running = Collections.synchronizedList(new ArrayList<NanoHTTPD.ClientHandler>());
    
    public void closeAll() {
      Iterator<?> iterator = (new ArrayList(this.running)).iterator();
      while (iterator.hasNext())
        ((NanoHTTPD.ClientHandler)iterator.next()).close(); 
    }
    
    public void closed(NanoHTTPD.ClientHandler param1ClientHandler) {
      this.running.remove(param1ClientHandler);
    }
    
    public void exec(NanoHTTPD.ClientHandler param1ClientHandler) {
      this.requestCount++;
      Thread thread = new Thread(param1ClientHandler);
      thread.setDaemon(true);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("NanoHttpd Request Processor (#");
      stringBuilder.append(this.requestCount);
      stringBuilder.append(")");
      thread.setName(stringBuilder.toString());
      this.running.add(param1ClientHandler);
      thread.start();
    }
    
    public List<NanoHTTPD.ClientHandler> getRunning() {
      return this.running;
    }
  }
  
  public static class DefaultServerSocketFactory implements ServerSocketFactory {
    public ServerSocket create() throws IOException {
      return new ServerSocket();
    }
  }
  
  public static class DefaultTempFile implements TempFile {
    private final File file;
    
    private final OutputStream fstream;
    
    public DefaultTempFile(File param1File) throws IOException {
      this.file = File.createTempFile("NanoHTTPD-", "", param1File);
      this.fstream = new FileOutputStream(this.file);
    }
    
    public void delete() throws Exception {
      NanoHTTPD.safeClose(this.fstream);
      if (this.file.delete())
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("could not delete temporary file: ");
      stringBuilder.append(this.file.getAbsolutePath());
      throw new Exception(stringBuilder.toString());
    }
    
    public String getName() {
      return this.file.getAbsolutePath();
    }
    
    public OutputStream open() throws Exception {
      return this.fstream;
    }
  }
  
  public static class DefaultTempFileManager implements TempFileManager {
    private final List<NanoHTTPD.TempFile> tempFiles;
    
    private final File tmpdir;
    
    public DefaultTempFileManager() {
      File file = new File(System.getProperty("java.io.tmpdir"));
      this.tmpdir = file;
      if (!file.exists())
        this.tmpdir.mkdirs(); 
      this.tempFiles = new ArrayList<NanoHTTPD.TempFile>();
    }
    
    public void clear() {
      for (NanoHTTPD.TempFile tempFile : this.tempFiles) {
        try {
          tempFile.delete();
        } catch (Exception exception) {
          NanoHTTPD.LOG.log(Level.WARNING, "could not delete file ", exception);
        } 
      } 
      this.tempFiles.clear();
    }
    
    public NanoHTTPD.TempFile createTempFile(String param1String) throws Exception {
      NanoHTTPD.DefaultTempFile defaultTempFile = new NanoHTTPD.DefaultTempFile(this.tmpdir);
      this.tempFiles.add(defaultTempFile);
      return defaultTempFile;
    }
  }
  
  private class DefaultTempFileManagerFactory implements TempFileManagerFactory {
    private DefaultTempFileManagerFactory() {}
    
    public NanoHTTPD.TempFileManager create() {
      return new NanoHTTPD.DefaultTempFileManager();
    }
  }
  
  protected class HTTPSession implements IHTTPSession {
    public static final int BUFSIZE = 8192;
    
    public static final int MAX_HEADER_SIZE = 1024;
    
    private static final int MEMORY_STORE_LIMIT = 1024;
    
    private static final int REQUEST_BUFFER_LEN = 512;
    
    private NanoHTTPD.CookieHandler cookies;
    
    private Map<String, String> headers;
    
    private final BufferedInputStream inputStream;
    
    private NanoHTTPD.Method method;
    
    private final OutputStream outputStream;
    
    private Map<String, List<String>> parms;
    
    private String protocolVersion;
    
    private String queryParameterString;
    
    private String remoteHostname;
    
    private String remoteIp;
    
    private int rlen;
    
    private int splitbyte;
    
    private final NanoHTTPD.TempFileManager tempFileManager;
    
    private String uri;
    
    public HTTPSession(NanoHTTPD.TempFileManager param1TempFileManager, InputStream param1InputStream, OutputStream param1OutputStream) {
      this.tempFileManager = param1TempFileManager;
      this.inputStream = new BufferedInputStream(param1InputStream, 8192);
      this.outputStream = param1OutputStream;
    }
    
    public HTTPSession(NanoHTTPD.TempFileManager param1TempFileManager, InputStream param1InputStream, OutputStream param1OutputStream, InetAddress param1InetAddress) {
      String str;
      this.tempFileManager = param1TempFileManager;
      this.inputStream = new BufferedInputStream(param1InputStream, 8192);
      this.outputStream = param1OutputStream;
      if (param1InetAddress.isLoopbackAddress() || param1InetAddress.isAnyLocalAddress()) {
        str = "127.0.0.1";
      } else {
        str = param1InetAddress.getHostAddress().toString();
      } 
      this.remoteIp = str;
      if (param1InetAddress.isLoopbackAddress() || param1InetAddress.isAnyLocalAddress()) {
        str = "localhost";
      } else {
        str = param1InetAddress.getHostName().toString();
      } 
      this.remoteHostname = str;
      this.headers = new HashMap<String, String>();
    }
    
    private void decodeHeader(BufferedReader param1BufferedReader, Map<String, String> param1Map1, Map<String, List<String>> param1Map, Map<String, String> param1Map2) throws NanoHTTPD.ResponseException {
      try {
        String str = param1BufferedReader.readLine();
        if (str == null)
          return; 
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        if (stringTokenizer.hasMoreTokens()) {
          param1Map1.put("method", stringTokenizer.nextToken());
          if (stringTokenizer.hasMoreTokens()) {
            String str1;
            String str3 = stringTokenizer.nextToken();
            int i = str3.indexOf('?');
            if (i >= 0) {
              decodeParms(str3.substring(i + 1), param1Map);
              str1 = NanoHTTPD.decodePercent(str3.substring(0, i));
            } else {
              str1 = NanoHTTPD.decodePercent(str3);
            } 
            if (stringTokenizer.hasMoreTokens()) {
              this.protocolVersion = stringTokenizer.nextToken();
            } else {
              this.protocolVersion = "HTTP/1.1";
              NanoHTTPD.LOG.log(Level.FINE, "no protocol version specified, strange. Assuming HTTP/1.1.");
            } 
            for (String str2 = param1BufferedReader.readLine(); str2 != null && !str2.trim().isEmpty(); str2 = param1BufferedReader.readLine()) {
              i = str2.indexOf(':');
              if (i >= 0)
                param1Map2.put(str2.substring(0, i).trim().toLowerCase(Locale.US), str2.substring(i + 1).trim()); 
            } 
            param1Map1.put("uri", str1);
            return;
          } 
          throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");
        } 
        throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");
      } catch (IOException iOException) {
        NanoHTTPD.Response.Status status = NanoHTTPD.Response.Status.INTERNAL_ERROR;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SERVER INTERNAL ERROR: IOException: ");
        stringBuilder.append(iOException.getMessage());
        throw new NanoHTTPD.ResponseException(status, stringBuilder.toString(), iOException);
      } 
    }
    
    private void decodeMultipartFormData(NanoHTTPD.ContentType param1ContentType, ByteBuffer param1ByteBuffer, Map<String, List<String>> param1Map, Map<String, String> param1Map1) throws NanoHTTPD.ResponseException {
      try {
        int[] arrayOfInt = getBoundaryPositions(param1ByteBuffer, param1ContentType.getBoundary().getBytes());
        int j = arrayOfInt.length;
        int i = 2;
        if (j >= 2) {
          byte[] arrayOfByte = new byte[1024];
          int k = 0;
          j = k;
          while (k < arrayOfInt.length - 1) {
            int m;
            int n;
            String str2;
            String str3;
            param1ByteBuffer.position(arrayOfInt[k]);
            if (param1ByteBuffer.remaining() < 1024) {
              n = param1ByteBuffer.remaining();
            } else {
              n = 1024;
            } 
            param1ByteBuffer.get(arrayOfByte, 0, n);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(arrayOfByte, 0, n), Charset.forName(param1ContentType.getEncoding())), n);
            String str1 = bufferedReader.readLine();
            if (str1 != null && str1.contains(param1ContentType.getBoundary())) {
              String str = bufferedReader.readLine();
              str2 = null;
              m = i;
              str1 = null;
              str3 = str1;
              while (str != null && str.trim().length() > 0) {
                Matcher matcher = NanoHTTPD.CONTENT_DISPOSITION_PATTERN.matcher(str);
                int i2 = j;
                String str4 = str2;
                String str5 = str1;
                if (matcher.matches()) {
                  str4 = matcher.group(i);
                  matcher = NanoHTTPD.CONTENT_DISPOSITION_ATTRIBUTE_PATTERN.matcher(str4);
                  i = j;
                  while (true) {
                    i2 = i;
                    str4 = str2;
                    str5 = str1;
                    i = j;
                    str2 = str4;
                  } 
                } 
                continue;
              } 
            } else {
              throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but chunk does not start with boundary.");
            } 
            int i1 = 0;
            while (m > 0) {
              i1 = scipOverNewLine(arrayOfByte, i1);
              m--;
            } 
            if (i1 < n - 4) {
              byte[] arrayOfByte1;
              m = arrayOfInt[k] + i1;
              n = arrayOfInt[++k] - 4;
              param1ByteBuffer.position(m);
              List<String> list2 = param1Map.get(str2);
              List<String> list1 = list2;
              if (list2 == null) {
                list1 = new ArrayList();
                param1Map.put(str2, list1);
              } 
              if (str3 == null) {
                arrayOfByte1 = new byte[n - m];
                param1ByteBuffer.get(arrayOfByte1);
                list1.add(new String(arrayOfByte1, param1ContentType.getEncoding()));
                continue;
              } 
              str3 = saveTmpFile(param1ByteBuffer, m, n - m, (String)arrayOfByte1);
              if (!param1Map1.containsKey(str2)) {
                param1Map1.put(str2, str3);
              } else {
                m = i;
                while (true) {
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append(str2);
                  stringBuilder.append(m);
                  if (param1Map1.containsKey(stringBuilder.toString())) {
                    m++;
                    continue;
                  } 
                  stringBuilder = new StringBuilder();
                  stringBuilder.append(str2);
                  stringBuilder.append(m);
                  param1Map1.put(stringBuilder.toString(), str3);
                  break;
                } 
              } 
              list1.add(arrayOfByte1);
              continue;
            } 
            throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.INTERNAL_ERROR, "Multipart header size exceeds MAX_HEADER_SIZE.");
          } 
        } else {
          throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but contains less than two boundary strings.");
        } 
      } catch (ResponseException responseException) {
        throw responseException;
      } catch (Exception exception) {
        throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.INTERNAL_ERROR, exception.toString());
      } 
    }
    
    private void decodeParms(String param1String, Map<String, List<String>> param1Map) {
      if (param1String == null) {
        this.queryParameterString = "";
        return;
      } 
      this.queryParameterString = param1String;
      StringTokenizer stringTokenizer = new StringTokenizer(param1String, "&");
      while (stringTokenizer.hasMoreTokens()) {
        String str = stringTokenizer.nextToken();
        int i = str.indexOf('=');
        if (i >= 0) {
          param1String = NanoHTTPD.decodePercent(str.substring(0, i)).trim();
          str = NanoHTTPD.decodePercent(str.substring(i + 1));
        } else {
          param1String = NanoHTTPD.decodePercent(str).trim();
          str = "";
        } 
        List<String> list2 = param1Map.get(param1String);
        List<String> list1 = list2;
        if (list2 == null) {
          list1 = new ArrayList();
          param1Map.put(param1String, list1);
        } 
        list1.add(str);
      } 
    }
    
    private int findHeaderEnd(byte[] param1ArrayOfbyte, int param1Int) {
      int i = 0;
      while (true) {
        int j = i + 1;
        if (j < param1Int) {
          if (param1ArrayOfbyte[i] == 13 && param1ArrayOfbyte[j] == 10) {
            int k = i + 3;
            if (k < param1Int && param1ArrayOfbyte[i + 2] == 13 && param1ArrayOfbyte[k] == 10)
              return i + 4; 
          } 
          if (param1ArrayOfbyte[i] == 10 && param1ArrayOfbyte[j] == 10)
            return i + 2; 
          i = j;
          continue;
        } 
        return 0;
      } 
    }
    
    private int[] getBoundaryPositions(ByteBuffer param1ByteBuffer, byte[] param1ArrayOfbyte) {
      int i;
      int[] arrayOfInt = new int[0];
      if (param1ByteBuffer.remaining() < param1ArrayOfbyte.length)
        return arrayOfInt; 
      int m = param1ArrayOfbyte.length + 4096;
      byte[] arrayOfByte = new byte[m];
      if (param1ByteBuffer.remaining() < m) {
        i = param1ByteBuffer.remaining();
      } else {
        i = m;
      } 
      param1ByteBuffer.get(arrayOfByte, 0, i);
      int j = i - param1ArrayOfbyte.length;
      int k = 0;
      while (true) {
        i = 0;
        int[] arrayOfInt1;
        for (arrayOfInt1 = arrayOfInt; i < j; arrayOfInt1 = arrayOfInt) {
          int n = 0;
          for (arrayOfInt = arrayOfInt1; n < param1ArrayOfbyte.length && arrayOfByte[i + n] == param1ArrayOfbyte[n]; arrayOfInt = arrayOfInt1) {
            arrayOfInt1 = arrayOfInt;
            if (n == param1ArrayOfbyte.length - 1) {
              arrayOfInt1 = new int[arrayOfInt.length + 1];
              System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, arrayOfInt.length);
              arrayOfInt1[arrayOfInt.length] = k + i;
            } 
            n++;
          } 
          i++;
        } 
        k += j;
        System.arraycopy(arrayOfByte, m - param1ArrayOfbyte.length, arrayOfByte, 0, param1ArrayOfbyte.length);
        j = m - param1ArrayOfbyte.length;
        i = j;
        if (param1ByteBuffer.remaining() < j)
          i = param1ByteBuffer.remaining(); 
        param1ByteBuffer.get(arrayOfByte, param1ArrayOfbyte.length, i);
        arrayOfInt = arrayOfInt1;
        j = i;
        if (i <= 0)
          return arrayOfInt1; 
      } 
    }
    
    private RandomAccessFile getTmpBucket() {
      try {
        return new RandomAccessFile(this.tempFileManager.createTempFile(null).getName(), "rw");
      } catch (Exception exception) {
        throw new Error(exception);
      } 
    }
    
    private String saveTmpFile(ByteBuffer param1ByteBuffer, int param1Int1, int param1Int2, String param1String) {
      if (param1Int2 > 0) {
        FileChannel fileChannel2 = null;
        FileChannel fileChannel3 = null;
        FileChannel fileChannel1 = fileChannel3;
        try {
          Exception exception;
          NanoHTTPD.TempFile tempFile = this.tempFileManager.createTempFile(param1String);
          fileChannel1 = fileChannel3;
          ByteBuffer byteBuffer = param1ByteBuffer.duplicate();
          fileChannel1 = fileChannel3;
          FileOutputStream fileOutputStream = new FileOutputStream(tempFile.getName());
          try {
            fileChannel1 = fileOutputStream.getChannel();
            byteBuffer.position(param1Int1).limit(param1Int1 + param1Int2);
            fileChannel1.write(byteBuffer.slice());
            return str;
          } catch (Exception exception1) {
          
          } finally {
            NanoHTTPD.TempFile tempFile1;
            tempFile = null;
            Exception exception1 = exception;
          } 
        } catch (Exception exception) {
          fileChannel1 = fileChannel2;
        } finally {}
        throw new Error(param1ByteBuffer);
      } 
      return "";
    }
    
    private int scipOverNewLine(byte[] param1ArrayOfbyte, int param1Int) {
      while (param1ArrayOfbyte[param1Int] != 10)
        param1Int++; 
      return param1Int + 1;
    }
    
    public void execute() throws IOException {
      // Byte code:
      //   0: aconst_null
      //   1: astore #12
      //   3: aconst_null
      //   4: astore #13
      //   6: aconst_null
      //   7: astore #14
      //   9: aconst_null
      //   10: astore #15
      //   12: aconst_null
      //   13: astore #16
      //   15: aconst_null
      //   16: astore #6
      //   18: aload #6
      //   20: astore #5
      //   22: aload #12
      //   24: astore #9
      //   26: aload #13
      //   28: astore #7
      //   30: aload #14
      //   32: astore #8
      //   34: aload #15
      //   36: astore #10
      //   38: aload #16
      //   40: astore #11
      //   42: sipush #8192
      //   45: newarray byte
      //   47: astore #17
      //   49: iconst_0
      //   50: istore #4
      //   52: aload #6
      //   54: astore #5
      //   56: aload #12
      //   58: astore #9
      //   60: aload #13
      //   62: astore #7
      //   64: aload #14
      //   66: astore #8
      //   68: aload #15
      //   70: astore #10
      //   72: aload #16
      //   74: astore #11
      //   76: aload_0
      //   77: iconst_0
      //   78: putfield splitbyte : I
      //   81: aload #6
      //   83: astore #5
      //   85: aload #12
      //   87: astore #9
      //   89: aload #13
      //   91: astore #7
      //   93: aload #14
      //   95: astore #8
      //   97: aload #15
      //   99: astore #10
      //   101: aload #16
      //   103: astore #11
      //   105: aload_0
      //   106: iconst_0
      //   107: putfield rlen : I
      //   110: aload #6
      //   112: astore #5
      //   114: aload #12
      //   116: astore #9
      //   118: aload #13
      //   120: astore #7
      //   122: aload #14
      //   124: astore #8
      //   126: aload #15
      //   128: astore #10
      //   130: aload #16
      //   132: astore #11
      //   134: aload_0
      //   135: getfield inputStream : Ljava/io/BufferedInputStream;
      //   138: sipush #8192
      //   141: invokevirtual mark : (I)V
      //   144: aload #6
      //   146: astore #5
      //   148: aload #12
      //   150: astore #9
      //   152: aload #15
      //   154: astore #10
      //   156: aload #16
      //   158: astore #11
      //   160: aload_0
      //   161: getfield inputStream : Ljava/io/BufferedInputStream;
      //   164: aload #17
      //   166: iconst_0
      //   167: sipush #8192
      //   170: invokevirtual read : ([BII)I
      //   173: istore_1
      //   174: iload_1
      //   175: iconst_m1
      //   176: if_icmpeq -> 1795
      //   179: iload_1
      //   180: ifle -> 360
      //   183: aload #6
      //   185: astore #5
      //   187: aload #12
      //   189: astore #9
      //   191: aload #13
      //   193: astore #7
      //   195: aload #14
      //   197: astore #8
      //   199: aload #15
      //   201: astore #10
      //   203: aload #16
      //   205: astore #11
      //   207: aload_0
      //   208: getfield rlen : I
      //   211: iload_1
      //   212: iadd
      //   213: istore_1
      //   214: aload #6
      //   216: astore #5
      //   218: aload #12
      //   220: astore #9
      //   222: aload #13
      //   224: astore #7
      //   226: aload #14
      //   228: astore #8
      //   230: aload #15
      //   232: astore #10
      //   234: aload #16
      //   236: astore #11
      //   238: aload_0
      //   239: iload_1
      //   240: putfield rlen : I
      //   243: aload #6
      //   245: astore #5
      //   247: aload #12
      //   249: astore #9
      //   251: aload #13
      //   253: astore #7
      //   255: aload #14
      //   257: astore #8
      //   259: aload #15
      //   261: astore #10
      //   263: aload #16
      //   265: astore #11
      //   267: aload_0
      //   268: aload #17
      //   270: iload_1
      //   271: invokespecial findHeaderEnd : ([BI)I
      //   274: istore_1
      //   275: aload #6
      //   277: astore #5
      //   279: aload #12
      //   281: astore #9
      //   283: aload #13
      //   285: astore #7
      //   287: aload #14
      //   289: astore #8
      //   291: aload #15
      //   293: astore #10
      //   295: aload #16
      //   297: astore #11
      //   299: aload_0
      //   300: iload_1
      //   301: putfield splitbyte : I
      //   304: iload_1
      //   305: ifle -> 311
      //   308: goto -> 360
      //   311: aload #6
      //   313: astore #5
      //   315: aload #12
      //   317: astore #9
      //   319: aload #13
      //   321: astore #7
      //   323: aload #14
      //   325: astore #8
      //   327: aload #15
      //   329: astore #10
      //   331: aload #16
      //   333: astore #11
      //   335: aload_0
      //   336: getfield inputStream : Ljava/io/BufferedInputStream;
      //   339: aload #17
      //   341: aload_0
      //   342: getfield rlen : I
      //   345: sipush #8192
      //   348: aload_0
      //   349: getfield rlen : I
      //   352: isub
      //   353: invokevirtual read : ([BII)I
      //   356: istore_1
      //   357: goto -> 179
      //   360: aload #6
      //   362: astore #5
      //   364: aload #12
      //   366: astore #9
      //   368: aload #13
      //   370: astore #7
      //   372: aload #14
      //   374: astore #8
      //   376: aload #15
      //   378: astore #10
      //   380: aload #16
      //   382: astore #11
      //   384: aload_0
      //   385: getfield splitbyte : I
      //   388: aload_0
      //   389: getfield rlen : I
      //   392: if_icmpge -> 463
      //   395: aload #6
      //   397: astore #5
      //   399: aload #12
      //   401: astore #9
      //   403: aload #13
      //   405: astore #7
      //   407: aload #14
      //   409: astore #8
      //   411: aload #15
      //   413: astore #10
      //   415: aload #16
      //   417: astore #11
      //   419: aload_0
      //   420: getfield inputStream : Ljava/io/BufferedInputStream;
      //   423: invokevirtual reset : ()V
      //   426: aload #6
      //   428: astore #5
      //   430: aload #12
      //   432: astore #9
      //   434: aload #13
      //   436: astore #7
      //   438: aload #14
      //   440: astore #8
      //   442: aload #15
      //   444: astore #10
      //   446: aload #16
      //   448: astore #11
      //   450: aload_0
      //   451: getfield inputStream : Ljava/io/BufferedInputStream;
      //   454: aload_0
      //   455: getfield splitbyte : I
      //   458: i2l
      //   459: invokevirtual skip : (J)J
      //   462: pop2
      //   463: aload #6
      //   465: astore #5
      //   467: aload #12
      //   469: astore #9
      //   471: aload #13
      //   473: astore #7
      //   475: aload #14
      //   477: astore #8
      //   479: aload #15
      //   481: astore #10
      //   483: aload #16
      //   485: astore #11
      //   487: aload_0
      //   488: new java/util/HashMap
      //   491: dup
      //   492: invokespecial <init> : ()V
      //   495: putfield parms : Ljava/util/Map;
      //   498: aload #6
      //   500: astore #5
      //   502: aload #12
      //   504: astore #9
      //   506: aload #13
      //   508: astore #7
      //   510: aload #14
      //   512: astore #8
      //   514: aload #15
      //   516: astore #10
      //   518: aload #16
      //   520: astore #11
      //   522: aload_0
      //   523: getfield headers : Ljava/util/Map;
      //   526: ifnonnull -> 567
      //   529: aload #6
      //   531: astore #5
      //   533: aload #12
      //   535: astore #9
      //   537: aload #13
      //   539: astore #7
      //   541: aload #14
      //   543: astore #8
      //   545: aload #15
      //   547: astore #10
      //   549: aload #16
      //   551: astore #11
      //   553: aload_0
      //   554: new java/util/HashMap
      //   557: dup
      //   558: invokespecial <init> : ()V
      //   561: putfield headers : Ljava/util/Map;
      //   564: goto -> 600
      //   567: aload #6
      //   569: astore #5
      //   571: aload #12
      //   573: astore #9
      //   575: aload #13
      //   577: astore #7
      //   579: aload #14
      //   581: astore #8
      //   583: aload #15
      //   585: astore #10
      //   587: aload #16
      //   589: astore #11
      //   591: aload_0
      //   592: getfield headers : Ljava/util/Map;
      //   595: invokeinterface clear : ()V
      //   600: aload #6
      //   602: astore #5
      //   604: aload #12
      //   606: astore #9
      //   608: aload #13
      //   610: astore #7
      //   612: aload #14
      //   614: astore #8
      //   616: aload #15
      //   618: astore #10
      //   620: aload #16
      //   622: astore #11
      //   624: aload_0
      //   625: aconst_null
      //   626: putfield queryParameterString : Ljava/lang/String;
      //   629: aload #6
      //   631: astore #5
      //   633: aload #12
      //   635: astore #9
      //   637: aload #13
      //   639: astore #7
      //   641: aload #14
      //   643: astore #8
      //   645: aload #15
      //   647: astore #10
      //   649: aload #16
      //   651: astore #11
      //   653: new java/io/BufferedReader
      //   656: dup
      //   657: new java/io/InputStreamReader
      //   660: dup
      //   661: new java/io/ByteArrayInputStream
      //   664: dup
      //   665: aload #17
      //   667: iconst_0
      //   668: aload_0
      //   669: getfield rlen : I
      //   672: invokespecial <init> : ([BII)V
      //   675: invokespecial <init> : (Ljava/io/InputStream;)V
      //   678: invokespecial <init> : (Ljava/io/Reader;)V
      //   681: astore #18
      //   683: aload #6
      //   685: astore #5
      //   687: aload #12
      //   689: astore #9
      //   691: aload #13
      //   693: astore #7
      //   695: aload #14
      //   697: astore #8
      //   699: aload #15
      //   701: astore #10
      //   703: aload #16
      //   705: astore #11
      //   707: new java/util/HashMap
      //   710: dup
      //   711: invokespecial <init> : ()V
      //   714: astore #17
      //   716: aload #6
      //   718: astore #5
      //   720: aload #12
      //   722: astore #9
      //   724: aload #13
      //   726: astore #7
      //   728: aload #14
      //   730: astore #8
      //   732: aload #15
      //   734: astore #10
      //   736: aload #16
      //   738: astore #11
      //   740: aload_0
      //   741: aload #18
      //   743: aload #17
      //   745: aload_0
      //   746: getfield parms : Ljava/util/Map;
      //   749: aload_0
      //   750: getfield headers : Ljava/util/Map;
      //   753: invokespecial decodeHeader : (Ljava/io/BufferedReader;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;)V
      //   756: aload #6
      //   758: astore #5
      //   760: aload #12
      //   762: astore #9
      //   764: aload #13
      //   766: astore #7
      //   768: aload #14
      //   770: astore #8
      //   772: aload #15
      //   774: astore #10
      //   776: aload #16
      //   778: astore #11
      //   780: aload_0
      //   781: getfield remoteIp : Ljava/lang/String;
      //   784: ifnull -> 869
      //   787: aload #6
      //   789: astore #5
      //   791: aload #12
      //   793: astore #9
      //   795: aload #13
      //   797: astore #7
      //   799: aload #14
      //   801: astore #8
      //   803: aload #15
      //   805: astore #10
      //   807: aload #16
      //   809: astore #11
      //   811: aload_0
      //   812: getfield headers : Ljava/util/Map;
      //   815: ldc_w 'remote-addr'
      //   818: aload_0
      //   819: getfield remoteIp : Ljava/lang/String;
      //   822: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   827: pop
      //   828: aload #6
      //   830: astore #5
      //   832: aload #12
      //   834: astore #9
      //   836: aload #13
      //   838: astore #7
      //   840: aload #14
      //   842: astore #8
      //   844: aload #15
      //   846: astore #10
      //   848: aload #16
      //   850: astore #11
      //   852: aload_0
      //   853: getfield headers : Ljava/util/Map;
      //   856: ldc_w 'http-client-ip'
      //   859: aload_0
      //   860: getfield remoteIp : Ljava/lang/String;
      //   863: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   868: pop
      //   869: aload #6
      //   871: astore #5
      //   873: aload #12
      //   875: astore #9
      //   877: aload #13
      //   879: astore #7
      //   881: aload #14
      //   883: astore #8
      //   885: aload #15
      //   887: astore #10
      //   889: aload #16
      //   891: astore #11
      //   893: aload #17
      //   895: ldc 'method'
      //   897: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   902: checkcast java/lang/String
      //   905: invokestatic lookup : (Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Method;
      //   908: astore #18
      //   910: aload #6
      //   912: astore #5
      //   914: aload #12
      //   916: astore #9
      //   918: aload #13
      //   920: astore #7
      //   922: aload #14
      //   924: astore #8
      //   926: aload #15
      //   928: astore #10
      //   930: aload #16
      //   932: astore #11
      //   934: aload_0
      //   935: aload #18
      //   937: putfield method : Lfi/iki/elonen/NanoHTTPD$Method;
      //   940: aload #18
      //   942: ifnull -> 1586
      //   945: aload #6
      //   947: astore #5
      //   949: aload #12
      //   951: astore #9
      //   953: aload #13
      //   955: astore #7
      //   957: aload #14
      //   959: astore #8
      //   961: aload #15
      //   963: astore #10
      //   965: aload #16
      //   967: astore #11
      //   969: aload_0
      //   970: aload #17
      //   972: ldc 'uri'
      //   974: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   979: checkcast java/lang/String
      //   982: putfield uri : Ljava/lang/String;
      //   985: aload #6
      //   987: astore #5
      //   989: aload #12
      //   991: astore #9
      //   993: aload #13
      //   995: astore #7
      //   997: aload #14
      //   999: astore #8
      //   1001: aload #15
      //   1003: astore #10
      //   1005: aload #16
      //   1007: astore #11
      //   1009: aload_0
      //   1010: new fi/iki/elonen/NanoHTTPD$CookieHandler
      //   1013: dup
      //   1014: aload_0
      //   1015: getfield this$0 : Lfi/iki/elonen/NanoHTTPD;
      //   1018: aload_0
      //   1019: getfield headers : Ljava/util/Map;
      //   1022: invokespecial <init> : (Lfi/iki/elonen/NanoHTTPD;Ljava/util/Map;)V
      //   1025: putfield cookies : Lfi/iki/elonen/NanoHTTPD$CookieHandler;
      //   1028: aload #6
      //   1030: astore #5
      //   1032: aload #12
      //   1034: astore #9
      //   1036: aload #13
      //   1038: astore #7
      //   1040: aload #14
      //   1042: astore #8
      //   1044: aload #15
      //   1046: astore #10
      //   1048: aload #16
      //   1050: astore #11
      //   1052: aload_0
      //   1053: getfield headers : Ljava/util/Map;
      //   1056: ldc_w 'connection'
      //   1059: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   1064: checkcast java/lang/String
      //   1067: astore #17
      //   1069: aload #6
      //   1071: astore #5
      //   1073: aload #12
      //   1075: astore #9
      //   1077: aload #13
      //   1079: astore #7
      //   1081: aload #14
      //   1083: astore #8
      //   1085: aload #15
      //   1087: astore #10
      //   1089: aload #16
      //   1091: astore #11
      //   1093: ldc 'HTTP/1.1'
      //   1095: aload_0
      //   1096: getfield protocolVersion : Ljava/lang/String;
      //   1099: invokevirtual equals : (Ljava/lang/Object;)Z
      //   1102: ifeq -> 2314
      //   1105: aload #17
      //   1107: ifnull -> 2309
      //   1110: aload #6
      //   1112: astore #5
      //   1114: aload #12
      //   1116: astore #9
      //   1118: aload #13
      //   1120: astore #7
      //   1122: aload #14
      //   1124: astore #8
      //   1126: aload #15
      //   1128: astore #10
      //   1130: aload #16
      //   1132: astore #11
      //   1134: aload #17
      //   1136: ldc_w '(?i).*close.*'
      //   1139: invokevirtual matches : (Ljava/lang/String;)Z
      //   1142: ifne -> 2314
      //   1145: goto -> 2309
      //   1148: aload #6
      //   1150: astore #5
      //   1152: aload #12
      //   1154: astore #9
      //   1156: aload #13
      //   1158: astore #7
      //   1160: aload #14
      //   1162: astore #8
      //   1164: aload #15
      //   1166: astore #10
      //   1168: aload #16
      //   1170: astore #11
      //   1172: aload_0
      //   1173: getfield this$0 : Lfi/iki/elonen/NanoHTTPD;
      //   1176: aload_0
      //   1177: invokevirtual serve : (Lfi/iki/elonen/NanoHTTPD$IHTTPSession;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   1180: astore #6
      //   1182: aload #6
      //   1184: ifnull -> 1548
      //   1187: aload #6
      //   1189: astore #5
      //   1191: aload #6
      //   1193: astore #9
      //   1195: aload #6
      //   1197: astore #7
      //   1199: aload #6
      //   1201: astore #8
      //   1203: aload #6
      //   1205: astore #10
      //   1207: aload #6
      //   1209: astore #11
      //   1211: aload_0
      //   1212: getfield headers : Ljava/util/Map;
      //   1215: ldc_w 'accept-encoding'
      //   1218: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   1223: checkcast java/lang/String
      //   1226: astore #12
      //   1228: aload #6
      //   1230: astore #5
      //   1232: aload #6
      //   1234: astore #9
      //   1236: aload #6
      //   1238: astore #7
      //   1240: aload #6
      //   1242: astore #8
      //   1244: aload #6
      //   1246: astore #10
      //   1248: aload #6
      //   1250: astore #11
      //   1252: aload_0
      //   1253: getfield cookies : Lfi/iki/elonen/NanoHTTPD$CookieHandler;
      //   1256: aload #6
      //   1258: invokevirtual unloadQueue : (Lfi/iki/elonen/NanoHTTPD$Response;)V
      //   1261: aload #6
      //   1263: astore #5
      //   1265: aload #6
      //   1267: astore #9
      //   1269: aload #6
      //   1271: astore #7
      //   1273: aload #6
      //   1275: astore #8
      //   1277: aload #6
      //   1279: astore #10
      //   1281: aload #6
      //   1283: astore #11
      //   1285: aload #6
      //   1287: aload_0
      //   1288: getfield method : Lfi/iki/elonen/NanoHTTPD$Method;
      //   1291: invokevirtual setRequestMethod : (Lfi/iki/elonen/NanoHTTPD$Method;)V
      //   1294: iload #4
      //   1296: istore_3
      //   1297: aload #6
      //   1299: astore #5
      //   1301: aload #6
      //   1303: astore #9
      //   1305: aload #6
      //   1307: astore #7
      //   1309: aload #6
      //   1311: astore #8
      //   1313: aload #6
      //   1315: astore #10
      //   1317: aload #6
      //   1319: astore #11
      //   1321: aload_0
      //   1322: getfield this$0 : Lfi/iki/elonen/NanoHTTPD;
      //   1325: aload #6
      //   1327: invokevirtual useGzipWhenAccepted : (Lfi/iki/elonen/NanoHTTPD$Response;)Z
      //   1330: ifeq -> 1381
      //   1333: iload #4
      //   1335: istore_3
      //   1336: aload #12
      //   1338: ifnull -> 1381
      //   1341: iload #4
      //   1343: istore_3
      //   1344: aload #6
      //   1346: astore #5
      //   1348: aload #6
      //   1350: astore #9
      //   1352: aload #6
      //   1354: astore #7
      //   1356: aload #6
      //   1358: astore #8
      //   1360: aload #6
      //   1362: astore #10
      //   1364: aload #6
      //   1366: astore #11
      //   1368: aload #12
      //   1370: ldc_w 'gzip'
      //   1373: invokevirtual contains : (Ljava/lang/CharSequence;)Z
      //   1376: ifeq -> 1381
      //   1379: iconst_1
      //   1380: istore_3
      //   1381: aload #6
      //   1383: astore #5
      //   1385: aload #6
      //   1387: astore #9
      //   1389: aload #6
      //   1391: astore #7
      //   1393: aload #6
      //   1395: astore #8
      //   1397: aload #6
      //   1399: astore #10
      //   1401: aload #6
      //   1403: astore #11
      //   1405: aload #6
      //   1407: iload_3
      //   1408: invokevirtual setGzipEncoding : (Z)V
      //   1411: aload #6
      //   1413: astore #5
      //   1415: aload #6
      //   1417: astore #9
      //   1419: aload #6
      //   1421: astore #7
      //   1423: aload #6
      //   1425: astore #8
      //   1427: aload #6
      //   1429: astore #10
      //   1431: aload #6
      //   1433: astore #11
      //   1435: aload #6
      //   1437: iload_2
      //   1438: invokevirtual setKeepAlive : (Z)V
      //   1441: aload #6
      //   1443: astore #5
      //   1445: aload #6
      //   1447: astore #9
      //   1449: aload #6
      //   1451: astore #7
      //   1453: aload #6
      //   1455: astore #8
      //   1457: aload #6
      //   1459: astore #10
      //   1461: aload #6
      //   1463: astore #11
      //   1465: aload #6
      //   1467: aload_0
      //   1468: getfield outputStream : Ljava/io/OutputStream;
      //   1471: invokevirtual send : (Ljava/io/OutputStream;)V
      //   1474: iload_2
      //   1475: ifeq -> 1513
      //   1478: aload #6
      //   1480: astore #5
      //   1482: aload #6
      //   1484: astore #9
      //   1486: aload #6
      //   1488: astore #7
      //   1490: aload #6
      //   1492: astore #8
      //   1494: aload #6
      //   1496: astore #10
      //   1498: aload #6
      //   1500: astore #11
      //   1502: aload #6
      //   1504: invokevirtual isCloseConnection : ()Z
      //   1507: ifne -> 1513
      //   1510: goto -> 2067
      //   1513: aload #6
      //   1515: astore #5
      //   1517: aload #6
      //   1519: astore #9
      //   1521: aload #6
      //   1523: astore #7
      //   1525: aload #6
      //   1527: astore #8
      //   1529: aload #6
      //   1531: astore #10
      //   1533: aload #6
      //   1535: astore #11
      //   1537: new java/net/SocketException
      //   1540: dup
      //   1541: ldc_w 'NanoHttpd Shutdown'
      //   1544: invokespecial <init> : (Ljava/lang/String;)V
      //   1547: athrow
      //   1548: aload #6
      //   1550: astore #5
      //   1552: aload #6
      //   1554: astore #9
      //   1556: aload #6
      //   1558: astore #7
      //   1560: aload #6
      //   1562: astore #8
      //   1564: aload #6
      //   1566: astore #10
      //   1568: aload #6
      //   1570: astore #11
      //   1572: new fi/iki/elonen/NanoHTTPD$ResponseException
      //   1575: dup
      //   1576: getstatic fi/iki/elonen/NanoHTTPD$Response$Status.INTERNAL_ERROR : Lfi/iki/elonen/NanoHTTPD$Response$Status;
      //   1579: ldc_w 'SERVER INTERNAL ERROR: Serve() returned a null response.'
      //   1582: invokespecial <init> : (Lfi/iki/elonen/NanoHTTPD$Response$Status;Ljava/lang/String;)V
      //   1585: athrow
      //   1586: aload #6
      //   1588: astore #5
      //   1590: aload #12
      //   1592: astore #9
      //   1594: aload #13
      //   1596: astore #7
      //   1598: aload #14
      //   1600: astore #8
      //   1602: aload #15
      //   1604: astore #10
      //   1606: aload #16
      //   1608: astore #11
      //   1610: getstatic fi/iki/elonen/NanoHTTPD$Response$Status.BAD_REQUEST : Lfi/iki/elonen/NanoHTTPD$Response$Status;
      //   1613: astore #18
      //   1615: aload #6
      //   1617: astore #5
      //   1619: aload #12
      //   1621: astore #9
      //   1623: aload #13
      //   1625: astore #7
      //   1627: aload #14
      //   1629: astore #8
      //   1631: aload #15
      //   1633: astore #10
      //   1635: aload #16
      //   1637: astore #11
      //   1639: new java/lang/StringBuilder
      //   1642: dup
      //   1643: invokespecial <init> : ()V
      //   1646: astore #19
      //   1648: aload #6
      //   1650: astore #5
      //   1652: aload #12
      //   1654: astore #9
      //   1656: aload #13
      //   1658: astore #7
      //   1660: aload #14
      //   1662: astore #8
      //   1664: aload #15
      //   1666: astore #10
      //   1668: aload #16
      //   1670: astore #11
      //   1672: aload #19
      //   1674: ldc_w 'BAD REQUEST: Syntax error. HTTP verb '
      //   1677: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1680: pop
      //   1681: aload #6
      //   1683: astore #5
      //   1685: aload #12
      //   1687: astore #9
      //   1689: aload #13
      //   1691: astore #7
      //   1693: aload #14
      //   1695: astore #8
      //   1697: aload #15
      //   1699: astore #10
      //   1701: aload #16
      //   1703: astore #11
      //   1705: aload #19
      //   1707: aload #17
      //   1709: ldc 'method'
      //   1711: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   1716: checkcast java/lang/String
      //   1719: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1722: pop
      //   1723: aload #6
      //   1725: astore #5
      //   1727: aload #12
      //   1729: astore #9
      //   1731: aload #13
      //   1733: astore #7
      //   1735: aload #14
      //   1737: astore #8
      //   1739: aload #15
      //   1741: astore #10
      //   1743: aload #16
      //   1745: astore #11
      //   1747: aload #19
      //   1749: ldc_w ' unhandled.'
      //   1752: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1755: pop
      //   1756: aload #6
      //   1758: astore #5
      //   1760: aload #12
      //   1762: astore #9
      //   1764: aload #13
      //   1766: astore #7
      //   1768: aload #14
      //   1770: astore #8
      //   1772: aload #15
      //   1774: astore #10
      //   1776: aload #16
      //   1778: astore #11
      //   1780: new fi/iki/elonen/NanoHTTPD$ResponseException
      //   1783: dup
      //   1784: aload #18
      //   1786: aload #19
      //   1788: invokevirtual toString : ()Ljava/lang/String;
      //   1791: invokespecial <init> : (Lfi/iki/elonen/NanoHTTPD$Response$Status;Ljava/lang/String;)V
      //   1794: athrow
      //   1795: aload #6
      //   1797: astore #5
      //   1799: aload #12
      //   1801: astore #9
      //   1803: aload #13
      //   1805: astore #7
      //   1807: aload #14
      //   1809: astore #8
      //   1811: aload #15
      //   1813: astore #10
      //   1815: aload #16
      //   1817: astore #11
      //   1819: aload_0
      //   1820: getfield inputStream : Ljava/io/BufferedInputStream;
      //   1823: invokestatic access$000 : (Ljava/lang/Object;)V
      //   1826: aload #6
      //   1828: astore #5
      //   1830: aload #12
      //   1832: astore #9
      //   1834: aload #13
      //   1836: astore #7
      //   1838: aload #14
      //   1840: astore #8
      //   1842: aload #15
      //   1844: astore #10
      //   1846: aload #16
      //   1848: astore #11
      //   1850: aload_0
      //   1851: getfield outputStream : Ljava/io/OutputStream;
      //   1854: invokestatic access$000 : (Ljava/lang/Object;)V
      //   1857: aload #6
      //   1859: astore #5
      //   1861: aload #12
      //   1863: astore #9
      //   1865: aload #13
      //   1867: astore #7
      //   1869: aload #14
      //   1871: astore #8
      //   1873: aload #15
      //   1875: astore #10
      //   1877: aload #16
      //   1879: astore #11
      //   1881: new java/net/SocketException
      //   1884: dup
      //   1885: ldc_w 'NanoHttpd Shutdown'
      //   1888: invokespecial <init> : (Ljava/lang/String;)V
      //   1891: athrow
      //   1892: aload #6
      //   1894: astore #5
      //   1896: aload #12
      //   1898: astore #9
      //   1900: aload #13
      //   1902: astore #7
      //   1904: aload #14
      //   1906: astore #8
      //   1908: aload #15
      //   1910: astore #10
      //   1912: aload #16
      //   1914: astore #11
      //   1916: aload_0
      //   1917: getfield inputStream : Ljava/io/BufferedInputStream;
      //   1920: invokestatic access$000 : (Ljava/lang/Object;)V
      //   1923: aload #6
      //   1925: astore #5
      //   1927: aload #12
      //   1929: astore #9
      //   1931: aload #13
      //   1933: astore #7
      //   1935: aload #14
      //   1937: astore #8
      //   1939: aload #15
      //   1941: astore #10
      //   1943: aload #16
      //   1945: astore #11
      //   1947: aload_0
      //   1948: getfield outputStream : Ljava/io/OutputStream;
      //   1951: invokestatic access$000 : (Ljava/lang/Object;)V
      //   1954: aload #6
      //   1956: astore #5
      //   1958: aload #12
      //   1960: astore #9
      //   1962: aload #13
      //   1964: astore #7
      //   1966: aload #14
      //   1968: astore #8
      //   1970: aload #15
      //   1972: astore #10
      //   1974: aload #16
      //   1976: astore #11
      //   1978: new java/net/SocketException
      //   1981: dup
      //   1982: ldc_w 'NanoHttpd Shutdown'
      //   1985: invokespecial <init> : (Ljava/lang/String;)V
      //   1988: athrow
      //   1989: astore #17
      //   1991: aload #6
      //   1993: astore #5
      //   1995: aload #12
      //   1997: astore #9
      //   1999: aload #13
      //   2001: astore #7
      //   2003: aload #14
      //   2005: astore #8
      //   2007: aload #15
      //   2009: astore #10
      //   2011: aload #16
      //   2013: astore #11
      //   2015: aload #17
      //   2017: athrow
      //   2018: astore #6
      //   2020: goto -> 2287
      //   2023: astore #6
      //   2025: aload #9
      //   2027: astore #5
      //   2029: aload #6
      //   2031: invokevirtual getStatus : ()Lfi/iki/elonen/NanoHTTPD$Response$Status;
      //   2034: ldc_w 'text/plain'
      //   2037: aload #6
      //   2039: invokevirtual getMessage : ()Ljava/lang/String;
      //   2042: invokestatic newFixedLengthResponse : (Lfi/iki/elonen/NanoHTTPD$Response$IStatus;Ljava/lang/String;Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   2045: aload_0
      //   2046: getfield outputStream : Ljava/io/OutputStream;
      //   2049: invokevirtual send : (Ljava/io/OutputStream;)V
      //   2052: aload #9
      //   2054: astore #5
      //   2056: aload_0
      //   2057: getfield outputStream : Ljava/io/OutputStream;
      //   2060: invokestatic access$000 : (Ljava/lang/Object;)V
      //   2063: aload #9
      //   2065: astore #6
      //   2067: aload #6
      //   2069: invokestatic access$000 : (Ljava/lang/Object;)V
      //   2072: aload_0
      //   2073: getfield tempFileManager : Lfi/iki/elonen/NanoHTTPD$TempFileManager;
      //   2076: invokeinterface clear : ()V
      //   2081: return
      //   2082: astore #6
      //   2084: aload #7
      //   2086: astore #5
      //   2088: getstatic fi/iki/elonen/NanoHTTPD$Response$Status.INTERNAL_ERROR : Lfi/iki/elonen/NanoHTTPD$Response$Status;
      //   2091: astore #8
      //   2093: aload #7
      //   2095: astore #5
      //   2097: new java/lang/StringBuilder
      //   2100: dup
      //   2101: invokespecial <init> : ()V
      //   2104: astore #9
      //   2106: aload #7
      //   2108: astore #5
      //   2110: aload #9
      //   2112: ldc 'SERVER INTERNAL ERROR: IOException: '
      //   2114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2117: pop
      //   2118: aload #7
      //   2120: astore #5
      //   2122: aload #9
      //   2124: aload #6
      //   2126: invokevirtual getMessage : ()Ljava/lang/String;
      //   2129: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2132: pop
      //   2133: aload #7
      //   2135: astore #5
      //   2137: aload #8
      //   2139: ldc_w 'text/plain'
      //   2142: aload #9
      //   2144: invokevirtual toString : ()Ljava/lang/String;
      //   2147: invokestatic newFixedLengthResponse : (Lfi/iki/elonen/NanoHTTPD$Response$IStatus;Ljava/lang/String;Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   2150: aload_0
      //   2151: getfield outputStream : Ljava/io/OutputStream;
      //   2154: invokevirtual send : (Ljava/io/OutputStream;)V
      //   2157: aload #7
      //   2159: astore #5
      //   2161: aload_0
      //   2162: getfield outputStream : Ljava/io/OutputStream;
      //   2165: invokestatic access$000 : (Ljava/lang/Object;)V
      //   2168: aload #7
      //   2170: astore #6
      //   2172: goto -> 2067
      //   2175: astore #6
      //   2177: aload #8
      //   2179: astore #5
      //   2181: getstatic fi/iki/elonen/NanoHTTPD$Response$Status.INTERNAL_ERROR : Lfi/iki/elonen/NanoHTTPD$Response$Status;
      //   2184: astore #7
      //   2186: aload #8
      //   2188: astore #5
      //   2190: new java/lang/StringBuilder
      //   2193: dup
      //   2194: invokespecial <init> : ()V
      //   2197: astore #9
      //   2199: aload #8
      //   2201: astore #5
      //   2203: aload #9
      //   2205: ldc_w 'SSL PROTOCOL FAILURE: '
      //   2208: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2211: pop
      //   2212: aload #8
      //   2214: astore #5
      //   2216: aload #9
      //   2218: aload #6
      //   2220: invokevirtual getMessage : ()Ljava/lang/String;
      //   2223: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2226: pop
      //   2227: aload #8
      //   2229: astore #5
      //   2231: aload #7
      //   2233: ldc_w 'text/plain'
      //   2236: aload #9
      //   2238: invokevirtual toString : ()Ljava/lang/String;
      //   2241: invokestatic newFixedLengthResponse : (Lfi/iki/elonen/NanoHTTPD$Response$IStatus;Ljava/lang/String;Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   2244: aload_0
      //   2245: getfield outputStream : Ljava/io/OutputStream;
      //   2248: invokevirtual send : (Ljava/io/OutputStream;)V
      //   2251: aload #8
      //   2253: astore #5
      //   2255: aload_0
      //   2256: getfield outputStream : Ljava/io/OutputStream;
      //   2259: invokestatic access$000 : (Ljava/lang/Object;)V
      //   2262: aload #8
      //   2264: astore #6
      //   2266: goto -> 2067
      //   2269: astore #6
      //   2271: aload #10
      //   2273: astore #5
      //   2275: aload #6
      //   2277: athrow
      //   2278: astore #6
      //   2280: aload #11
      //   2282: astore #5
      //   2284: aload #6
      //   2286: athrow
      //   2287: aload #5
      //   2289: invokestatic access$000 : (Ljava/lang/Object;)V
      //   2292: aload_0
      //   2293: getfield tempFileManager : Lfi/iki/elonen/NanoHTTPD$TempFileManager;
      //   2296: invokeinterface clear : ()V
      //   2301: aload #6
      //   2303: athrow
      //   2304: astore #5
      //   2306: goto -> 1892
      //   2309: iconst_1
      //   2310: istore_2
      //   2311: goto -> 1148
      //   2314: iconst_0
      //   2315: istore_2
      //   2316: goto -> 1148
      // Exception table:
      //   from	to	target	type
      //   42	49	2278	java/net/SocketException
      //   42	49	2269	java/net/SocketTimeoutException
      //   42	49	2175	javax/net/ssl/SSLException
      //   42	49	2082	java/io/IOException
      //   42	49	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   42	49	2018	finally
      //   76	81	2278	java/net/SocketException
      //   76	81	2269	java/net/SocketTimeoutException
      //   76	81	2175	javax/net/ssl/SSLException
      //   76	81	2082	java/io/IOException
      //   76	81	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   76	81	2018	finally
      //   105	110	2278	java/net/SocketException
      //   105	110	2269	java/net/SocketTimeoutException
      //   105	110	2175	javax/net/ssl/SSLException
      //   105	110	2082	java/io/IOException
      //   105	110	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   105	110	2018	finally
      //   134	144	2278	java/net/SocketException
      //   134	144	2269	java/net/SocketTimeoutException
      //   134	144	2175	javax/net/ssl/SSLException
      //   134	144	2082	java/io/IOException
      //   134	144	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   134	144	2018	finally
      //   160	174	1989	javax/net/ssl/SSLException
      //   160	174	2304	java/io/IOException
      //   160	174	2278	java/net/SocketException
      //   160	174	2269	java/net/SocketTimeoutException
      //   160	174	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   160	174	2018	finally
      //   207	214	2278	java/net/SocketException
      //   207	214	2269	java/net/SocketTimeoutException
      //   207	214	2175	javax/net/ssl/SSLException
      //   207	214	2082	java/io/IOException
      //   207	214	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   207	214	2018	finally
      //   238	243	2278	java/net/SocketException
      //   238	243	2269	java/net/SocketTimeoutException
      //   238	243	2175	javax/net/ssl/SSLException
      //   238	243	2082	java/io/IOException
      //   238	243	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   238	243	2018	finally
      //   267	275	2278	java/net/SocketException
      //   267	275	2269	java/net/SocketTimeoutException
      //   267	275	2175	javax/net/ssl/SSLException
      //   267	275	2082	java/io/IOException
      //   267	275	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   267	275	2018	finally
      //   299	304	2278	java/net/SocketException
      //   299	304	2269	java/net/SocketTimeoutException
      //   299	304	2175	javax/net/ssl/SSLException
      //   299	304	2082	java/io/IOException
      //   299	304	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   299	304	2018	finally
      //   335	357	2278	java/net/SocketException
      //   335	357	2269	java/net/SocketTimeoutException
      //   335	357	2175	javax/net/ssl/SSLException
      //   335	357	2082	java/io/IOException
      //   335	357	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   335	357	2018	finally
      //   384	395	2278	java/net/SocketException
      //   384	395	2269	java/net/SocketTimeoutException
      //   384	395	2175	javax/net/ssl/SSLException
      //   384	395	2082	java/io/IOException
      //   384	395	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   384	395	2018	finally
      //   419	426	2278	java/net/SocketException
      //   419	426	2269	java/net/SocketTimeoutException
      //   419	426	2175	javax/net/ssl/SSLException
      //   419	426	2082	java/io/IOException
      //   419	426	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   419	426	2018	finally
      //   450	463	2278	java/net/SocketException
      //   450	463	2269	java/net/SocketTimeoutException
      //   450	463	2175	javax/net/ssl/SSLException
      //   450	463	2082	java/io/IOException
      //   450	463	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   450	463	2018	finally
      //   487	498	2278	java/net/SocketException
      //   487	498	2269	java/net/SocketTimeoutException
      //   487	498	2175	javax/net/ssl/SSLException
      //   487	498	2082	java/io/IOException
      //   487	498	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   487	498	2018	finally
      //   522	529	2278	java/net/SocketException
      //   522	529	2269	java/net/SocketTimeoutException
      //   522	529	2175	javax/net/ssl/SSLException
      //   522	529	2082	java/io/IOException
      //   522	529	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   522	529	2018	finally
      //   553	564	2278	java/net/SocketException
      //   553	564	2269	java/net/SocketTimeoutException
      //   553	564	2175	javax/net/ssl/SSLException
      //   553	564	2082	java/io/IOException
      //   553	564	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   553	564	2018	finally
      //   591	600	2278	java/net/SocketException
      //   591	600	2269	java/net/SocketTimeoutException
      //   591	600	2175	javax/net/ssl/SSLException
      //   591	600	2082	java/io/IOException
      //   591	600	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   591	600	2018	finally
      //   624	629	2278	java/net/SocketException
      //   624	629	2269	java/net/SocketTimeoutException
      //   624	629	2175	javax/net/ssl/SSLException
      //   624	629	2082	java/io/IOException
      //   624	629	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   624	629	2018	finally
      //   653	683	2278	java/net/SocketException
      //   653	683	2269	java/net/SocketTimeoutException
      //   653	683	2175	javax/net/ssl/SSLException
      //   653	683	2082	java/io/IOException
      //   653	683	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   653	683	2018	finally
      //   707	716	2278	java/net/SocketException
      //   707	716	2269	java/net/SocketTimeoutException
      //   707	716	2175	javax/net/ssl/SSLException
      //   707	716	2082	java/io/IOException
      //   707	716	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   707	716	2018	finally
      //   740	756	2278	java/net/SocketException
      //   740	756	2269	java/net/SocketTimeoutException
      //   740	756	2175	javax/net/ssl/SSLException
      //   740	756	2082	java/io/IOException
      //   740	756	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   740	756	2018	finally
      //   780	787	2278	java/net/SocketException
      //   780	787	2269	java/net/SocketTimeoutException
      //   780	787	2175	javax/net/ssl/SSLException
      //   780	787	2082	java/io/IOException
      //   780	787	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   780	787	2018	finally
      //   811	828	2278	java/net/SocketException
      //   811	828	2269	java/net/SocketTimeoutException
      //   811	828	2175	javax/net/ssl/SSLException
      //   811	828	2082	java/io/IOException
      //   811	828	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   811	828	2018	finally
      //   852	869	2278	java/net/SocketException
      //   852	869	2269	java/net/SocketTimeoutException
      //   852	869	2175	javax/net/ssl/SSLException
      //   852	869	2082	java/io/IOException
      //   852	869	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   852	869	2018	finally
      //   893	910	2278	java/net/SocketException
      //   893	910	2269	java/net/SocketTimeoutException
      //   893	910	2175	javax/net/ssl/SSLException
      //   893	910	2082	java/io/IOException
      //   893	910	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   893	910	2018	finally
      //   934	940	2278	java/net/SocketException
      //   934	940	2269	java/net/SocketTimeoutException
      //   934	940	2175	javax/net/ssl/SSLException
      //   934	940	2082	java/io/IOException
      //   934	940	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   934	940	2018	finally
      //   969	985	2278	java/net/SocketException
      //   969	985	2269	java/net/SocketTimeoutException
      //   969	985	2175	javax/net/ssl/SSLException
      //   969	985	2082	java/io/IOException
      //   969	985	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   969	985	2018	finally
      //   1009	1028	2278	java/net/SocketException
      //   1009	1028	2269	java/net/SocketTimeoutException
      //   1009	1028	2175	javax/net/ssl/SSLException
      //   1009	1028	2082	java/io/IOException
      //   1009	1028	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1009	1028	2018	finally
      //   1052	1069	2278	java/net/SocketException
      //   1052	1069	2269	java/net/SocketTimeoutException
      //   1052	1069	2175	javax/net/ssl/SSLException
      //   1052	1069	2082	java/io/IOException
      //   1052	1069	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1052	1069	2018	finally
      //   1093	1105	2278	java/net/SocketException
      //   1093	1105	2269	java/net/SocketTimeoutException
      //   1093	1105	2175	javax/net/ssl/SSLException
      //   1093	1105	2082	java/io/IOException
      //   1093	1105	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1093	1105	2018	finally
      //   1134	1145	2278	java/net/SocketException
      //   1134	1145	2269	java/net/SocketTimeoutException
      //   1134	1145	2175	javax/net/ssl/SSLException
      //   1134	1145	2082	java/io/IOException
      //   1134	1145	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1134	1145	2018	finally
      //   1172	1182	2278	java/net/SocketException
      //   1172	1182	2269	java/net/SocketTimeoutException
      //   1172	1182	2175	javax/net/ssl/SSLException
      //   1172	1182	2082	java/io/IOException
      //   1172	1182	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1172	1182	2018	finally
      //   1211	1228	2278	java/net/SocketException
      //   1211	1228	2269	java/net/SocketTimeoutException
      //   1211	1228	2175	javax/net/ssl/SSLException
      //   1211	1228	2082	java/io/IOException
      //   1211	1228	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1211	1228	2018	finally
      //   1252	1261	2278	java/net/SocketException
      //   1252	1261	2269	java/net/SocketTimeoutException
      //   1252	1261	2175	javax/net/ssl/SSLException
      //   1252	1261	2082	java/io/IOException
      //   1252	1261	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1252	1261	2018	finally
      //   1285	1294	2278	java/net/SocketException
      //   1285	1294	2269	java/net/SocketTimeoutException
      //   1285	1294	2175	javax/net/ssl/SSLException
      //   1285	1294	2082	java/io/IOException
      //   1285	1294	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1285	1294	2018	finally
      //   1321	1333	2278	java/net/SocketException
      //   1321	1333	2269	java/net/SocketTimeoutException
      //   1321	1333	2175	javax/net/ssl/SSLException
      //   1321	1333	2082	java/io/IOException
      //   1321	1333	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1321	1333	2018	finally
      //   1368	1379	2278	java/net/SocketException
      //   1368	1379	2269	java/net/SocketTimeoutException
      //   1368	1379	2175	javax/net/ssl/SSLException
      //   1368	1379	2082	java/io/IOException
      //   1368	1379	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1368	1379	2018	finally
      //   1405	1411	2278	java/net/SocketException
      //   1405	1411	2269	java/net/SocketTimeoutException
      //   1405	1411	2175	javax/net/ssl/SSLException
      //   1405	1411	2082	java/io/IOException
      //   1405	1411	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1405	1411	2018	finally
      //   1435	1441	2278	java/net/SocketException
      //   1435	1441	2269	java/net/SocketTimeoutException
      //   1435	1441	2175	javax/net/ssl/SSLException
      //   1435	1441	2082	java/io/IOException
      //   1435	1441	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1435	1441	2018	finally
      //   1465	1474	2278	java/net/SocketException
      //   1465	1474	2269	java/net/SocketTimeoutException
      //   1465	1474	2175	javax/net/ssl/SSLException
      //   1465	1474	2082	java/io/IOException
      //   1465	1474	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1465	1474	2018	finally
      //   1502	1510	2278	java/net/SocketException
      //   1502	1510	2269	java/net/SocketTimeoutException
      //   1502	1510	2175	javax/net/ssl/SSLException
      //   1502	1510	2082	java/io/IOException
      //   1502	1510	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1502	1510	2018	finally
      //   1537	1548	2278	java/net/SocketException
      //   1537	1548	2269	java/net/SocketTimeoutException
      //   1537	1548	2175	javax/net/ssl/SSLException
      //   1537	1548	2082	java/io/IOException
      //   1537	1548	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1537	1548	2018	finally
      //   1572	1586	2278	java/net/SocketException
      //   1572	1586	2269	java/net/SocketTimeoutException
      //   1572	1586	2175	javax/net/ssl/SSLException
      //   1572	1586	2082	java/io/IOException
      //   1572	1586	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1572	1586	2018	finally
      //   1610	1615	2278	java/net/SocketException
      //   1610	1615	2269	java/net/SocketTimeoutException
      //   1610	1615	2175	javax/net/ssl/SSLException
      //   1610	1615	2082	java/io/IOException
      //   1610	1615	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1610	1615	2018	finally
      //   1639	1648	2278	java/net/SocketException
      //   1639	1648	2269	java/net/SocketTimeoutException
      //   1639	1648	2175	javax/net/ssl/SSLException
      //   1639	1648	2082	java/io/IOException
      //   1639	1648	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1639	1648	2018	finally
      //   1672	1681	2278	java/net/SocketException
      //   1672	1681	2269	java/net/SocketTimeoutException
      //   1672	1681	2175	javax/net/ssl/SSLException
      //   1672	1681	2082	java/io/IOException
      //   1672	1681	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1672	1681	2018	finally
      //   1705	1723	2278	java/net/SocketException
      //   1705	1723	2269	java/net/SocketTimeoutException
      //   1705	1723	2175	javax/net/ssl/SSLException
      //   1705	1723	2082	java/io/IOException
      //   1705	1723	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1705	1723	2018	finally
      //   1747	1756	2278	java/net/SocketException
      //   1747	1756	2269	java/net/SocketTimeoutException
      //   1747	1756	2175	javax/net/ssl/SSLException
      //   1747	1756	2082	java/io/IOException
      //   1747	1756	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1747	1756	2018	finally
      //   1780	1795	2278	java/net/SocketException
      //   1780	1795	2269	java/net/SocketTimeoutException
      //   1780	1795	2175	javax/net/ssl/SSLException
      //   1780	1795	2082	java/io/IOException
      //   1780	1795	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1780	1795	2018	finally
      //   1819	1826	2278	java/net/SocketException
      //   1819	1826	2269	java/net/SocketTimeoutException
      //   1819	1826	2175	javax/net/ssl/SSLException
      //   1819	1826	2082	java/io/IOException
      //   1819	1826	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1819	1826	2018	finally
      //   1850	1857	2278	java/net/SocketException
      //   1850	1857	2269	java/net/SocketTimeoutException
      //   1850	1857	2175	javax/net/ssl/SSLException
      //   1850	1857	2082	java/io/IOException
      //   1850	1857	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1850	1857	2018	finally
      //   1881	1892	2278	java/net/SocketException
      //   1881	1892	2269	java/net/SocketTimeoutException
      //   1881	1892	2175	javax/net/ssl/SSLException
      //   1881	1892	2082	java/io/IOException
      //   1881	1892	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1881	1892	2018	finally
      //   1916	1923	2278	java/net/SocketException
      //   1916	1923	2269	java/net/SocketTimeoutException
      //   1916	1923	2175	javax/net/ssl/SSLException
      //   1916	1923	2082	java/io/IOException
      //   1916	1923	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1916	1923	2018	finally
      //   1947	1954	2278	java/net/SocketException
      //   1947	1954	2269	java/net/SocketTimeoutException
      //   1947	1954	2175	javax/net/ssl/SSLException
      //   1947	1954	2082	java/io/IOException
      //   1947	1954	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1947	1954	2018	finally
      //   1978	1989	2278	java/net/SocketException
      //   1978	1989	2269	java/net/SocketTimeoutException
      //   1978	1989	2175	javax/net/ssl/SSLException
      //   1978	1989	2082	java/io/IOException
      //   1978	1989	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   1978	1989	2018	finally
      //   2015	2018	2278	java/net/SocketException
      //   2015	2018	2269	java/net/SocketTimeoutException
      //   2015	2018	2175	javax/net/ssl/SSLException
      //   2015	2018	2082	java/io/IOException
      //   2015	2018	2023	fi/iki/elonen/NanoHTTPD$ResponseException
      //   2015	2018	2018	finally
      //   2029	2052	2018	finally
      //   2056	2063	2018	finally
      //   2088	2093	2018	finally
      //   2097	2106	2018	finally
      //   2110	2118	2018	finally
      //   2122	2133	2018	finally
      //   2137	2157	2018	finally
      //   2161	2168	2018	finally
      //   2181	2186	2018	finally
      //   2190	2199	2018	finally
      //   2203	2212	2018	finally
      //   2216	2227	2018	finally
      //   2231	2251	2018	finally
      //   2255	2262	2018	finally
      //   2275	2278	2018	finally
      //   2284	2287	2018	finally
    }
    
    public long getBodySize() {
      if (this.headers.containsKey("content-length"))
        return Long.parseLong(this.headers.get("content-length")); 
      int i = this.splitbyte;
      int j = this.rlen;
      return (i < j) ? (j - i) : 0L;
    }
    
    public NanoHTTPD.CookieHandler getCookies() {
      return this.cookies;
    }
    
    public final Map<String, String> getHeaders() {
      return this.headers;
    }
    
    public final InputStream getInputStream() {
      return this.inputStream;
    }
    
    public final NanoHTTPD.Method getMethod() {
      return this.method;
    }
    
    public final Map<String, List<String>> getParameters() {
      return this.parms;
    }
    
    @Deprecated
    public final Map<String, String> getParms() {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      for (String str : this.parms.keySet())
        hashMap.put(str, ((List)this.parms.get(str)).get(0)); 
      return (Map)hashMap;
    }
    
    public String getQueryParameterString() {
      return this.queryParameterString;
    }
    
    public String getRemoteHostName() {
      return this.remoteHostname;
    }
    
    public String getRemoteIpAddress() {
      return this.remoteIp;
    }
    
    public final String getUri() {
      return this.uri;
    }
    
    public void parseBody(Map<String, String> param1Map) throws IOException, NanoHTTPD.ResponseException {
      String str1;
      String str2;
      ByteArrayOutputStream byteArrayOutputStream = null;
      try {
        RandomAccessFile randomAccessFile1;
        RandomAccessFile randomAccessFile2;
        long l = getBodySize();
        if (l < 1024L) {
          ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
          DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream1);
          ByteArrayOutputStream byteArrayOutputStream2 = null;
          byteArrayOutputStream = byteArrayOutputStream1;
          byteArrayOutputStream1 = byteArrayOutputStream2;
        } else {
          randomAccessFile1 = getTmpBucket();
          byteArrayOutputStream = null;
          randomAccessFile2 = randomAccessFile1;
        } 
      } finally {
        param1Map = null;
      } 
      NanoHTTPD.safeClose(str1);
      throw param1Map;
    }
  }
  
  public static interface IHTTPSession {
    void execute() throws IOException;
    
    NanoHTTPD.CookieHandler getCookies();
    
    Map<String, String> getHeaders();
    
    InputStream getInputStream();
    
    NanoHTTPD.Method getMethod();
    
    Map<String, List<String>> getParameters();
    
    @Deprecated
    Map<String, String> getParms();
    
    String getQueryParameterString();
    
    String getRemoteHostName();
    
    String getRemoteIpAddress();
    
    String getUri();
    
    void parseBody(Map<String, String> param1Map) throws IOException, NanoHTTPD.ResponseException;
  }
  
  public enum Method {
    CONNECT, COPY, DELETE, GET, HEAD, LOCK, MKCOL, MOVE, OPTIONS, PATCH, POST, PROPFIND, PROPPATCH, PUT, TRACE, UNLOCK;
    
    static {
      DELETE = new Method("DELETE", 3);
      HEAD = new Method("HEAD", 4);
      OPTIONS = new Method("OPTIONS", 5);
      TRACE = new Method("TRACE", 6);
      CONNECT = new Method("CONNECT", 7);
      PATCH = new Method("PATCH", 8);
      PROPFIND = new Method("PROPFIND", 9);
      PROPPATCH = new Method("PROPPATCH", 10);
      MKCOL = new Method("MKCOL", 11);
      MOVE = new Method("MOVE", 12);
      COPY = new Method("COPY", 13);
      LOCK = new Method("LOCK", 14);
      Method method = new Method("UNLOCK", 15);
      UNLOCK = method;
      $VALUES = new Method[] { 
          GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, 
          PROPPATCH, MKCOL, MOVE, COPY, LOCK, method };
    }
    
    static Method lookup(String param1String) {
      if (param1String == null)
        return null; 
      try {
        return valueOf(param1String);
      } catch (IllegalArgumentException illegalArgumentException) {
        return null;
      } 
    }
  }
  
  public static class Response implements Closeable {
    private boolean chunkedTransfer;
    
    private long contentLength;
    
    private InputStream data;
    
    private boolean encodeAsGzip;
    
    private final Map<String, String> header = new HashMap<String, String>() {
        public String put(String param2String1, String param2String2) {
          String str;
          Map<String, String> map = NanoHTTPD.Response.this.lowerCaseHeader;
          if (param2String1 == null) {
            str = param2String1;
          } else {
            str = param2String1.toLowerCase();
          } 
          map.put(str, param2String2);
          return super.put(param2String1, param2String2);
        }
      };
    
    private boolean keepAlive;
    
    private final Map<String, String> lowerCaseHeader = new HashMap<String, String>();
    
    private String mimeType;
    
    private NanoHTTPD.Method requestMethod;
    
    private IStatus status;
    
    protected Response(IStatus param1IStatus, String param1String, InputStream param1InputStream, long param1Long) {
      this.status = param1IStatus;
      this.mimeType = param1String;
      boolean bool = false;
      if (param1InputStream == null) {
        this.data = new ByteArrayInputStream(new byte[0]);
        this.contentLength = 0L;
      } else {
        this.data = param1InputStream;
        this.contentLength = param1Long;
      } 
      if (this.contentLength < 0L)
        bool = true; 
      this.chunkedTransfer = bool;
      this.keepAlive = true;
    }
    
    private void sendBody(OutputStream param1OutputStream, long param1Long) throws IOException {
      boolean bool;
      byte[] arrayOfByte = new byte[(int)16384L];
      if (param1Long == -1L) {
        bool = true;
      } else {
        bool = false;
      } 
      while (true) {
        if (param1Long > 0L || bool) {
          long l;
          if (bool) {
            l = 16384L;
          } else {
            l = Math.min(param1Long, 16384L);
          } 
          int i = this.data.read(arrayOfByte, 0, (int)l);
          if (i > 0) {
            param1OutputStream.write(arrayOfByte, 0, i);
            if (!bool)
              param1Long -= i; 
            continue;
          } 
        } 
        return;
      } 
    }
    
    private void sendBodyWithCorrectEncoding(OutputStream param1OutputStream, long param1Long) throws IOException {
      if (this.encodeAsGzip) {
        param1OutputStream = new GZIPOutputStream(param1OutputStream);
        sendBody(param1OutputStream, -1L);
        param1OutputStream.finish();
        return;
      } 
      sendBody(param1OutputStream, param1Long);
    }
    
    private void sendBodyWithCorrectTransferAndEncoding(OutputStream param1OutputStream, long param1Long) throws IOException {
      if (this.requestMethod != NanoHTTPD.Method.HEAD && this.chunkedTransfer) {
        param1OutputStream = new ChunkedOutputStream(param1OutputStream);
        sendBodyWithCorrectEncoding(param1OutputStream, -1L);
        param1OutputStream.finish();
        return;
      } 
      sendBodyWithCorrectEncoding(param1OutputStream, param1Long);
    }
    
    public void addHeader(String param1String1, String param1String2) {
      this.header.put(param1String1, param1String2);
    }
    
    public void close() throws IOException {
      InputStream inputStream = this.data;
      if (inputStream != null)
        inputStream.close(); 
    }
    
    public void closeConnection(boolean param1Boolean) {
      if (param1Boolean) {
        this.header.put("connection", "close");
        return;
      } 
      this.header.remove("connection");
    }
    
    public InputStream getData() {
      return this.data;
    }
    
    public String getHeader(String param1String) {
      return this.lowerCaseHeader.get(param1String.toLowerCase());
    }
    
    public String getMimeType() {
      return this.mimeType;
    }
    
    public NanoHTTPD.Method getRequestMethod() {
      return this.requestMethod;
    }
    
    public IStatus getStatus() {
      return this.status;
    }
    
    public boolean isCloseConnection() {
      return "close".equals(getHeader("connection"));
    }
    
    protected void printHeader(PrintWriter param1PrintWriter, String param1String1, String param1String2) {
      param1PrintWriter.append(param1String1).append(": ").append(param1String2).append("\r\n");
    }
    
    protected void send(OutputStream param1OutputStream) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
      simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      try {
        if (this.status != null) {
          long l1;
          long l2;
          PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(param1OutputStream, (new NanoHTTPD.ContentType(this.mimeType)).getEncoding())), false);
          printWriter.append("HTTP/1.1 ").append(this.status.getDescription()).append(" \r\n");
          if (this.mimeType != null)
            printHeader(printWriter, "Content-Type", this.mimeType); 
          if (getHeader("date") == null)
            printHeader(printWriter, "Date", simpleDateFormat.format(new Date())); 
          for (Map.Entry<String, String> entry : this.header.entrySet())
            printHeader(printWriter, (String)entry.getKey(), (String)entry.getValue()); 
          if (getHeader("connection") == null) {
            String str;
            if (this.keepAlive) {
              str = "keep-alive";
            } else {
              str = "close";
            } 
            printHeader(printWriter, "Connection", str);
          } 
          if (getHeader("content-length") != null)
            this.encodeAsGzip = false; 
          if (this.encodeAsGzip) {
            printHeader(printWriter, "Content-Encoding", "gzip");
            setChunkedTransfer(true);
          } 
          if (this.data != null) {
            l1 = this.contentLength;
          } else {
            l1 = 0L;
          } 
          if (this.requestMethod != NanoHTTPD.Method.HEAD && this.chunkedTransfer) {
            printHeader(printWriter, "Transfer-Encoding", "chunked");
            l2 = l1;
          } else {
            l2 = l1;
            if (!this.encodeAsGzip)
              l2 = sendContentLengthHeaderIfNotAlreadyPresent(printWriter, l1); 
          } 
          printWriter.append("\r\n");
          printWriter.flush();
          sendBodyWithCorrectTransferAndEncoding(param1OutputStream, l2);
          param1OutputStream.flush();
          NanoHTTPD.safeClose(this.data);
          return;
        } 
        throw new Error("sendResponse(): Status can't be null.");
      } catch (IOException iOException) {
        NanoHTTPD.LOG.log(Level.SEVERE, "Could not send response to the client", iOException);
        return;
      } 
    }
    
    protected long sendContentLengthHeaderIfNotAlreadyPresent(PrintWriter param1PrintWriter, long param1Long) {
      String str = getHeader("content-length");
      long l = param1Long;
      if (str != null)
        try {
          l = Long.parseLong(str);
        } catch (NumberFormatException numberFormatException) {
          Logger logger = NanoHTTPD.LOG;
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("content-length was no number ");
          stringBuilder1.append(str);
          logger.severe(stringBuilder1.toString());
          l = param1Long;
        }  
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Content-Length: ");
      stringBuilder.append(l);
      stringBuilder.append("\r\n");
      param1PrintWriter.print(stringBuilder.toString());
      return l;
    }
    
    public void setChunkedTransfer(boolean param1Boolean) {
      this.chunkedTransfer = param1Boolean;
    }
    
    public void setData(InputStream param1InputStream) {
      this.data = param1InputStream;
    }
    
    public void setGzipEncoding(boolean param1Boolean) {
      this.encodeAsGzip = param1Boolean;
    }
    
    public void setKeepAlive(boolean param1Boolean) {
      this.keepAlive = param1Boolean;
    }
    
    public void setMimeType(String param1String) {
      this.mimeType = param1String;
    }
    
    public void setRequestMethod(NanoHTTPD.Method param1Method) {
      this.requestMethod = param1Method;
    }
    
    public void setStatus(IStatus param1IStatus) {
      this.status = param1IStatus;
    }
    
    private static class ChunkedOutputStream extends FilterOutputStream {
      public ChunkedOutputStream(OutputStream param2OutputStream) {
        super(param2OutputStream);
      }
      
      public void finish() throws IOException {
        this.out.write("0\r\n\r\n".getBytes());
      }
      
      public void write(int param2Int) throws IOException {
        write(new byte[] { (byte)param2Int }, 0, 1);
      }
      
      public void write(byte[] param2ArrayOfbyte) throws IOException {
        write(param2ArrayOfbyte, 0, param2ArrayOfbyte.length);
      }
      
      public void write(byte[] param2ArrayOfbyte, int param2Int1, int param2Int2) throws IOException {
        if (param2Int2 == 0)
          return; 
        this.out.write(String.format("%x\r\n", new Object[] { Integer.valueOf(param2Int2) }).getBytes());
        this.out.write(param2ArrayOfbyte, param2Int1, param2Int2);
        this.out.write("\r\n".getBytes());
      }
    }
    
    public static interface IStatus {
      String getDescription();
      
      int getRequestStatus();
    }
    
    public enum Status implements IStatus {
      SWITCH_PROTOCOL(101, "Switching Protocols"),
      TEMPORARY_REDIRECT(101, "Switching Protocols"),
      TOO_MANY_REQUESTS(101, "Switching Protocols"),
      UNAUTHORIZED(101, "Switching Protocols"),
      UNSUPPORTED_HTTP_VERSION(101, "Switching Protocols"),
      UNSUPPORTED_MEDIA_TYPE(101, "Switching Protocols"),
      ACCEPTED,
      BAD_REQUEST,
      CONFLICT,
      CREATED,
      EXPECTATION_FAILED,
      FORBIDDEN,
      FOUND,
      GONE,
      INTERNAL_ERROR,
      LENGTH_REQUIRED,
      METHOD_NOT_ALLOWED,
      MULTI_STATUS,
      NOT_ACCEPTABLE,
      NOT_FOUND,
      NOT_IMPLEMENTED,
      NOT_MODIFIED,
      NO_CONTENT,
      OK,
      PARTIAL_CONTENT,
      PAYLOAD_TOO_LARGE,
      PRECONDITION_FAILED,
      RANGE_NOT_SATISFIABLE,
      REDIRECT,
      REDIRECT_SEE_OTHER,
      REQUEST_TIMEOUT,
      SERVICE_UNAVAILABLE;
      
      private final String description;
      
      private final int requestStatus;
      
      static {
        ACCEPTED = new Status("ACCEPTED", 3, 202, "Accepted");
        NO_CONTENT = new Status("NO_CONTENT", 4, 204, "No Content");
        PARTIAL_CONTENT = new Status("PARTIAL_CONTENT", 5, 206, "Partial Content");
        MULTI_STATUS = new Status("MULTI_STATUS", 6, 207, "Multi-Status");
        REDIRECT = new Status("REDIRECT", 7, 301, "Moved Permanently");
        FOUND = new Status("FOUND", 8, 302, "Found");
        REDIRECT_SEE_OTHER = new Status("REDIRECT_SEE_OTHER", 9, 303, "See Other");
        NOT_MODIFIED = new Status("NOT_MODIFIED", 10, 304, "Not Modified");
        TEMPORARY_REDIRECT = new Status("TEMPORARY_REDIRECT", 11, 307, "Temporary Redirect");
        BAD_REQUEST = new Status("BAD_REQUEST", 12, 400, "Bad Request");
        UNAUTHORIZED = new Status("UNAUTHORIZED", 13, 401, "Unauthorized");
        FORBIDDEN = new Status("FORBIDDEN", 14, 403, "Forbidden");
        NOT_FOUND = new Status("NOT_FOUND", 15, 404, "Not Found");
        METHOD_NOT_ALLOWED = new Status("METHOD_NOT_ALLOWED", 16, 405, "Method Not Allowed");
        NOT_ACCEPTABLE = new Status("NOT_ACCEPTABLE", 17, 406, "Not Acceptable");
        REQUEST_TIMEOUT = new Status("REQUEST_TIMEOUT", 18, 408, "Request Timeout");
        CONFLICT = new Status("CONFLICT", 19, 409, "Conflict");
        GONE = new Status("GONE", 20, 410, "Gone");
        LENGTH_REQUIRED = new Status("LENGTH_REQUIRED", 21, 411, "Length Required");
        PRECONDITION_FAILED = new Status("PRECONDITION_FAILED", 22, 412, "Precondition Failed");
        PAYLOAD_TOO_LARGE = new Status("PAYLOAD_TOO_LARGE", 23, 413, "Payload Too Large");
        UNSUPPORTED_MEDIA_TYPE = new Status("UNSUPPORTED_MEDIA_TYPE", 24, 415, "Unsupported Media Type");
        RANGE_NOT_SATISFIABLE = new Status("RANGE_NOT_SATISFIABLE", 25, 416, "Requested Range Not Satisfiable");
        EXPECTATION_FAILED = new Status("EXPECTATION_FAILED", 26, 417, "Expectation Failed");
        TOO_MANY_REQUESTS = new Status("TOO_MANY_REQUESTS", 27, 429, "Too Many Requests");
        INTERNAL_ERROR = new Status("INTERNAL_ERROR", 28, 500, "Internal Server Error");
        NOT_IMPLEMENTED = new Status("NOT_IMPLEMENTED", 29, 501, "Not Implemented");
        SERVICE_UNAVAILABLE = new Status("SERVICE_UNAVAILABLE", 30, 503, "Service Unavailable");
        Status status = new Status("UNSUPPORTED_HTTP_VERSION", 31, 505, "HTTP Version Not Supported");
        UNSUPPORTED_HTTP_VERSION = status;
        $VALUES = new Status[] { 
            SWITCH_PROTOCOL, OK, CREATED, ACCEPTED, NO_CONTENT, PARTIAL_CONTENT, MULTI_STATUS, REDIRECT, FOUND, REDIRECT_SEE_OTHER, 
            NOT_MODIFIED, TEMPORARY_REDIRECT, BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, METHOD_NOT_ALLOWED, NOT_ACCEPTABLE, REQUEST_TIMEOUT, CONFLICT, 
            GONE, LENGTH_REQUIRED, PRECONDITION_FAILED, PAYLOAD_TOO_LARGE, UNSUPPORTED_MEDIA_TYPE, RANGE_NOT_SATISFIABLE, EXPECTATION_FAILED, TOO_MANY_REQUESTS, INTERNAL_ERROR, NOT_IMPLEMENTED, 
            SERVICE_UNAVAILABLE, status };
      }
      
      Status(int param2Int1, String param2String1) {
        this.requestStatus = param2Int1;
        this.description = param2String1;
      }
      
      public static Status lookup(int param2Int) {
        for (Status status : values()) {
          if (status.getRequestStatus() == param2Int)
            return status; 
        } 
        return null;
      }
      
      public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(this.requestStatus);
        stringBuilder.append(" ");
        stringBuilder.append(this.description);
        return stringBuilder.toString();
      }
      
      public int getRequestStatus() {
        return this.requestStatus;
      }
    }
  }
  
  class null extends HashMap<String, String> {
    public String put(String param1String1, String param1String2) {
      String str;
      Map<String, String> map = this.this$0.lowerCaseHeader;
      if (param1String1 == null) {
        str = param1String1;
      } else {
        str = param1String1.toLowerCase();
      } 
      map.put(str, param1String2);
      return super.put(param1String1, param1String2);
    }
  }
  
  private static class ChunkedOutputStream extends FilterOutputStream {
    public ChunkedOutputStream(OutputStream param1OutputStream) {
      super(param1OutputStream);
    }
    
    public void finish() throws IOException {
      this.out.write("0\r\n\r\n".getBytes());
    }
    
    public void write(int param1Int) throws IOException {
      write(new byte[] { (byte)param1Int }, 0, 1);
    }
    
    public void write(byte[] param1ArrayOfbyte) throws IOException {
      write(param1ArrayOfbyte, 0, param1ArrayOfbyte.length);
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      if (param1Int2 == 0)
        return; 
      this.out.write(String.format("%x\r\n", new Object[] { Integer.valueOf(param1Int2) }).getBytes());
      this.out.write(param1ArrayOfbyte, param1Int1, param1Int2);
      this.out.write("\r\n".getBytes());
    }
  }
  
  public static interface IStatus {
    String getDescription();
    
    int getRequestStatus();
  }
  
  public enum Status implements Response.IStatus {
    ACCEPTED(101, "Switching Protocols"),
    BAD_REQUEST(101, "Switching Protocols"),
    CONFLICT(101, "Switching Protocols"),
    CREATED(101, "Switching Protocols"),
    EXPECTATION_FAILED(101, "Switching Protocols"),
    FORBIDDEN(101, "Switching Protocols"),
    FOUND(101, "Switching Protocols"),
    GONE(101, "Switching Protocols"),
    INTERNAL_ERROR(101, "Switching Protocols"),
    LENGTH_REQUIRED(101, "Switching Protocols"),
    METHOD_NOT_ALLOWED(101, "Switching Protocols"),
    MULTI_STATUS(101, "Switching Protocols"),
    NOT_ACCEPTABLE(101, "Switching Protocols"),
    NOT_FOUND(101, "Switching Protocols"),
    NOT_IMPLEMENTED(101, "Switching Protocols"),
    NOT_MODIFIED(101, "Switching Protocols"),
    NO_CONTENT(101, "Switching Protocols"),
    OK(101, "Switching Protocols"),
    PARTIAL_CONTENT(101, "Switching Protocols"),
    PAYLOAD_TOO_LARGE(101, "Switching Protocols"),
    PRECONDITION_FAILED(101, "Switching Protocols"),
    RANGE_NOT_SATISFIABLE(101, "Switching Protocols"),
    REDIRECT(101, "Switching Protocols"),
    REDIRECT_SEE_OTHER(101, "Switching Protocols"),
    REQUEST_TIMEOUT(101, "Switching Protocols"),
    SERVICE_UNAVAILABLE(101, "Switching Protocols"),
    SWITCH_PROTOCOL(101, "Switching Protocols"),
    TEMPORARY_REDIRECT(101, "Switching Protocols"),
    TOO_MANY_REQUESTS(101, "Switching Protocols"),
    UNAUTHORIZED(101, "Switching Protocols"),
    UNSUPPORTED_HTTP_VERSION(101, "Switching Protocols"),
    UNSUPPORTED_MEDIA_TYPE(101, "Switching Protocols");
    
    private final String description;
    
    private final int requestStatus;
    
    static {
      CREATED = new Status("CREATED", 2, 201, "Created");
      ACCEPTED = new Status("ACCEPTED", 3, 202, "Accepted");
      NO_CONTENT = new Status("NO_CONTENT", 4, 204, "No Content");
      PARTIAL_CONTENT = new Status("PARTIAL_CONTENT", 5, 206, "Partial Content");
      MULTI_STATUS = new Status("MULTI_STATUS", 6, 207, "Multi-Status");
      REDIRECT = new Status("REDIRECT", 7, 301, "Moved Permanently");
      FOUND = new Status("FOUND", 8, 302, "Found");
      REDIRECT_SEE_OTHER = new Status("REDIRECT_SEE_OTHER", 9, 303, "See Other");
      NOT_MODIFIED = new Status("NOT_MODIFIED", 10, 304, "Not Modified");
      TEMPORARY_REDIRECT = new Status("TEMPORARY_REDIRECT", 11, 307, "Temporary Redirect");
      BAD_REQUEST = new Status("BAD_REQUEST", 12, 400, "Bad Request");
      UNAUTHORIZED = new Status("UNAUTHORIZED", 13, 401, "Unauthorized");
      FORBIDDEN = new Status("FORBIDDEN", 14, 403, "Forbidden");
      NOT_FOUND = new Status("NOT_FOUND", 15, 404, "Not Found");
      METHOD_NOT_ALLOWED = new Status("METHOD_NOT_ALLOWED", 16, 405, "Method Not Allowed");
      NOT_ACCEPTABLE = new Status("NOT_ACCEPTABLE", 17, 406, "Not Acceptable");
      REQUEST_TIMEOUT = new Status("REQUEST_TIMEOUT", 18, 408, "Request Timeout");
      CONFLICT = new Status("CONFLICT", 19, 409, "Conflict");
      GONE = new Status("GONE", 20, 410, "Gone");
      LENGTH_REQUIRED = new Status("LENGTH_REQUIRED", 21, 411, "Length Required");
      PRECONDITION_FAILED = new Status("PRECONDITION_FAILED", 22, 412, "Precondition Failed");
      PAYLOAD_TOO_LARGE = new Status("PAYLOAD_TOO_LARGE", 23, 413, "Payload Too Large");
      UNSUPPORTED_MEDIA_TYPE = new Status("UNSUPPORTED_MEDIA_TYPE", 24, 415, "Unsupported Media Type");
      RANGE_NOT_SATISFIABLE = new Status("RANGE_NOT_SATISFIABLE", 25, 416, "Requested Range Not Satisfiable");
      EXPECTATION_FAILED = new Status("EXPECTATION_FAILED", 26, 417, "Expectation Failed");
      TOO_MANY_REQUESTS = new Status("TOO_MANY_REQUESTS", 27, 429, "Too Many Requests");
      INTERNAL_ERROR = new Status("INTERNAL_ERROR", 28, 500, "Internal Server Error");
      NOT_IMPLEMENTED = new Status("NOT_IMPLEMENTED", 29, 501, "Not Implemented");
      SERVICE_UNAVAILABLE = new Status("SERVICE_UNAVAILABLE", 30, 503, "Service Unavailable");
      Status status = new Status("UNSUPPORTED_HTTP_VERSION", 31, 505, "HTTP Version Not Supported");
      UNSUPPORTED_HTTP_VERSION = status;
      $VALUES = new Status[] { 
          SWITCH_PROTOCOL, OK, CREATED, ACCEPTED, NO_CONTENT, PARTIAL_CONTENT, MULTI_STATUS, REDIRECT, FOUND, REDIRECT_SEE_OTHER, 
          NOT_MODIFIED, TEMPORARY_REDIRECT, BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, METHOD_NOT_ALLOWED, NOT_ACCEPTABLE, REQUEST_TIMEOUT, CONFLICT, 
          GONE, LENGTH_REQUIRED, PRECONDITION_FAILED, PAYLOAD_TOO_LARGE, UNSUPPORTED_MEDIA_TYPE, RANGE_NOT_SATISFIABLE, EXPECTATION_FAILED, TOO_MANY_REQUESTS, INTERNAL_ERROR, NOT_IMPLEMENTED, 
          SERVICE_UNAVAILABLE, status };
    }
    
    Status(int param1Int1, String param1String1) {
      this.requestStatus = param1Int1;
      this.description = param1String1;
    }
    
    public static Status lookup(int param1Int) {
      for (Status status : values()) {
        if (status.getRequestStatus() == param1Int)
          return status; 
      } 
      return null;
    }
    
    public String getDescription() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(this.requestStatus);
      stringBuilder.append(" ");
      stringBuilder.append(this.description);
      return stringBuilder.toString();
    }
    
    public int getRequestStatus() {
      return this.requestStatus;
    }
  }
  
  public static final class ResponseException extends Exception {
    private static final long serialVersionUID = 6569838532917408380L;
    
    private final NanoHTTPD.Response.Status status;
    
    public ResponseException(NanoHTTPD.Response.Status param1Status, String param1String) {
      super(param1String);
      this.status = param1Status;
    }
    
    public ResponseException(NanoHTTPD.Response.Status param1Status, String param1String, Exception param1Exception) {
      super(param1String, param1Exception);
      this.status = param1Status;
    }
    
    public NanoHTTPD.Response.Status getStatus() {
      return this.status;
    }
  }
  
  public static class SecureServerSocketFactory implements ServerSocketFactory {
    private String[] sslProtocols;
    
    private SSLServerSocketFactory sslServerSocketFactory;
    
    public SecureServerSocketFactory(SSLServerSocketFactory param1SSLServerSocketFactory, String[] param1ArrayOfString) {
      this.sslServerSocketFactory = param1SSLServerSocketFactory;
      this.sslProtocols = param1ArrayOfString;
    }
    
    public ServerSocket create() throws IOException {
      SSLServerSocket sSLServerSocket = (SSLServerSocket)this.sslServerSocketFactory.createServerSocket();
      String[] arrayOfString = this.sslProtocols;
      if (arrayOfString != null) {
        sSLServerSocket.setEnabledProtocols(arrayOfString);
      } else {
        sSLServerSocket.setEnabledProtocols(sSLServerSocket.getSupportedProtocols());
      } 
      sSLServerSocket.setUseClientMode(false);
      sSLServerSocket.setWantClientAuth(false);
      sSLServerSocket.setNeedClientAuth(false);
      return sSLServerSocket;
    }
  }
  
  public class ServerRunnable implements Runnable {
    private IOException bindException;
    
    private boolean hasBinded = false;
    
    private final int timeout;
    
    public ServerRunnable(int param1Int) {
      this.timeout = param1Int;
    }
    
    public void run() {
      try {
        InetSocketAddress inetSocketAddress;
        ServerSocket serverSocket = NanoHTTPD.this.myServerSocket;
        if (NanoHTTPD.this.hostname != null) {
          inetSocketAddress = new InetSocketAddress(NanoHTTPD.this.hostname, NanoHTTPD.this.myPort);
        } else {
          inetSocketAddress = new InetSocketAddress(NanoHTTPD.this.myPort);
        } 
        serverSocket.bind(inetSocketAddress);
        this.hasBinded = true;
        do {
          try {
            Socket socket = NanoHTTPD.this.myServerSocket.accept();
            if (this.timeout > 0)
              socket.setSoTimeout(this.timeout); 
            InputStream inputStream = socket.getInputStream();
            NanoHTTPD.this.asyncRunner.exec(NanoHTTPD.this.createClientHandler(socket, inputStream));
          } catch (IOException iOException) {
            NanoHTTPD.LOG.log(Level.FINE, "Communication with the client broken", iOException);
          } 
        } while (!NanoHTTPD.this.myServerSocket.isClosed());
        return;
      } catch (IOException iOException) {
        this.bindException = iOException;
        return;
      } 
    }
  }
  
  public static interface ServerSocketFactory {
    ServerSocket create() throws IOException;
  }
  
  public static interface TempFile {
    void delete() throws Exception;
    
    String getName();
    
    OutputStream open() throws Exception;
  }
  
  public static interface TempFileManager {
    void clear();
    
    NanoHTTPD.TempFile createTempFile(String param1String) throws Exception;
  }
  
  public static interface TempFileManagerFactory {
    NanoHTTPD.TempFileManager create();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\fi\iki\elonen\NanoHTTPD.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
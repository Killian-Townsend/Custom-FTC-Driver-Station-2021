package com.qualcomm.ftccommon;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.internal.android.SoundPoolIntf;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.system.LockingRunner;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class SoundPlayer implements SoundPool.OnLoadCompleteListener, SoundPoolIntf {
  public static final String TAG = "SoundPlayer";
  
  public static boolean TRACE = true;
  
  public static final int msSoundTransmissionFreshness = 400;
  
  protected final LockingRunner cacheLock = new LockingRunner();
  
  protected SoundInfo currentlyLoadingInfo = null;
  
  protected CountDownLatch currentlyLoadingLatch = null;
  
  protected Set<CurrentlyPlaying> currentlyPlayingSounds;
  
  protected final boolean isRobotController = AppUtil.getInstance().isRobotController();
  
  protected LoadedSoundCache loadedSounds;
  
  protected final Object lock = new Object();
  
  protected float masterVolume = 1.0F;
  
  protected MediaPlayer mediaSizer = new MediaPlayer();
  
  protected ScheduledExecutorService scheduledThreadPool;
  
  protected SharedPreferences sharedPreferences;
  
  protected float soundOffVolume = 0.0F;
  
  protected float soundOnVolume = 1.0F;
  
  protected SoundPool soundPool;
  
  protected ExecutorService threadPool;
  
  protected Tracer tracer = Tracer.create("SoundPlayer", TRACE);
  
  public SoundPlayer(int paramInt1, int paramInt2) {
    AudioAttributes.Builder builder = new AudioAttributes.Builder();
    builder.setContentType(4);
    AudioAttributes audioAttributes = builder.build();
    SoundPool.Builder builder1 = new SoundPool.Builder();
    builder1.setAudioAttributes(audioAttributes);
    builder1.setMaxStreams(paramInt1);
    this.soundPool = builder1.build();
    this.mediaSizer.setAudioAttributes(audioAttributes);
    paramInt1 = ((AudioManager)AppUtil.getDefContext().getSystemService("audio")).generateAudioSessionId();
    this.mediaSizer.setAudioSessionId(paramInt1);
    this.loadedSounds = new LoadedSoundCache(paramInt2);
    this.currentlyPlayingSounds = new HashSet<CurrentlyPlaying>();
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AppUtil.getDefContext());
    final CountDownLatch interlock = new CountDownLatch(1);
    this.threadPool = ThreadPool.newFixedThreadPool(1, "SoundPlayer");
    this.scheduledThreadPool = (ScheduledExecutorService)ThreadPool.newScheduledExecutor(1, "SoundPlayerScheduler");
    CallbackLooper.getDefault().post(new Runnable() {
          public void run() {
            SoundPlayer.this.soundPool.setOnLoadCompleteListener(SoundPlayer.this);
            interlock.countDown();
          }
        });
    try {
      countDownLatch.await();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public static SoundInfo addRef(SoundInfo paramSoundInfo) {
    if (paramSoundInfo != null)
      paramSoundInfo.addRef(); 
    return paramSoundInfo;
  }
  
  protected static void copy(InputStream paramInputStream, OutputStream paramOutputStream, int paramInt) throws IOException {
    if (paramInt > 0) {
      byte[] arrayOfByte = new byte[256];
      while (true) {
        int i = paramInputStream.read(arrayOfByte);
        if (i >= 0) {
          paramOutputStream.write(arrayOfByte, 0, i);
          i = paramInt - i;
          paramInt = i;
          if (i <= 0)
            return; 
          continue;
        } 
        throw new IOException("insufficient data");
      } 
    } 
  }
  
  public static SoundPlayer getInstance() {
    return InstanceHolder.theInstance;
  }
  
  public static void releaseRef(SoundInfo paramSoundInfo) {
    if (paramSoundInfo != null)
      paramSoundInfo.releaseRef(); 
  }
  
  void checkForFinishedSounds() {
    synchronized (this.lock) {
      long l = getMsNow();
      for (CurrentlyPlaying currentlyPlaying : new ArrayList(this.currentlyPlayingSounds)) {
        if (currentlyPlaying.msFinish <= l) {
          this.soundPool.stop(currentlyPlaying.streamId);
          if (currentlyPlaying.runWhenFinished != null)
            this.threadPool.execute(currentlyPlaying.runWhenFinished); 
          this.currentlyPlayingSounds.remove(currentlyPlaying);
        } 
      } 
      return;
    } 
  }
  
  public void close() {
    ExecutorService executorService = this.threadPool;
    if (executorService != null) {
      executorService.shutdownNow();
      ThreadPool.awaitTerminationOrExitApplication(this.threadPool, 5L, TimeUnit.SECONDS, "SoundPool", "internal error");
      this.threadPool = null;
    } 
    executorService = this.scheduledThreadPool;
    if (executorService != null) {
      executorService.shutdownNow();
      ThreadPool.awaitTerminationOrExitApplication(this.scheduledThreadPool, 3L, TimeUnit.SECONDS, "SoundPool", "internal error");
    } 
    MediaPlayer mediaPlayer = this.mediaSizer;
    if (mediaPlayer != null)
      mediaPlayer.release(); 
  }
  
  protected SoundInfo ensureCached(final String hashString, final SoundFromFile ifAbsent) {
    final MutableReference result = new MutableReference(null);
    AppUtil.getInstance().ensureDirectoryExists(AppUtil.SOUNDS_CACHE, false);
    try {
      this.cacheLock.lockWhile(new Runnable() {
            public void run() {
              // Byte code:
              //   0: getstatic org/firstinspires/ftc/robotcore/internal/system/AppUtil.SOUNDS_CACHE : Ljava/io/File;
              //   3: astore_2
              //   4: new java/lang/StringBuilder
              //   7: dup
              //   8: invokespecial <init> : ()V
              //   11: astore_3
              //   12: aload_3
              //   13: aload_0
              //   14: getfield val$hashString : Ljava/lang/String;
              //   17: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   20: pop
              //   21: aload_3
              //   22: ldc '.sound'
              //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
              //   27: pop
              //   28: new java/io/File
              //   31: dup
              //   32: aload_2
              //   33: aload_3
              //   34: invokevirtual toString : ()Ljava/lang/String;
              //   37: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
              //   40: astore_2
              //   41: aload_2
              //   42: invokevirtual exists : ()Z
              //   45: ifeq -> 77
              //   48: aload_0
              //   49: getfield this$0 : Lcom/qualcomm/ftccommon/SoundPlayer;
              //   52: invokestatic getDefContext : ()Landroid/content/Context;
              //   55: aload_2
              //   56: invokevirtual ensureLoaded : (Landroid/content/Context;Ljava/io/File;)Lcom/qualcomm/ftccommon/SoundPlayer$SoundInfo;
              //   59: astore_3
              //   60: aload_3
              //   61: ifnull -> 77
              //   64: aload_0
              //   65: getfield val$result : Lorg/firstinspires/ftc/robotcore/internal/collections/MutableReference;
              //   68: aload_3
              //   69: invokevirtual setValue : (Ljava/lang/Object;)V
              //   72: iconst_1
              //   73: istore_1
              //   74: goto -> 79
              //   77: iconst_0
              //   78: istore_1
              //   79: iload_1
              //   80: ifne -> 100
              //   83: aload_0
              //   84: getfield val$result : Lorg/firstinspires/ftc/robotcore/internal/collections/MutableReference;
              //   87: aload_0
              //   88: getfield val$ifAbsent : Lcom/qualcomm/ftccommon/SoundPlayer$SoundFromFile;
              //   91: aload_2
              //   92: invokeinterface apply : (Ljava/io/File;)Lcom/qualcomm/ftccommon/SoundPlayer$SoundInfo;
              //   97: invokevirtual setValue : (Ljava/lang/Object;)V
              //   100: return
            }
          });
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    return (SoundInfo)mutableReference.getValue();
  }
  
  protected void ensureCached(Context paramContext, int paramInt) {
    final SoundInfo soundInfo = ensureLoaded(paramContext, paramInt);
    if (soundInfo != null) {
      soundInfo = ensureCached(soundInfo.hashString, new SoundFromFile() {
            public SoundPlayer.SoundInfo apply(File param1File) {
              // Byte code:
              //   0: aload_0
              //   1: getfield val$soundInfo : Lcom/qualcomm/ftccommon/SoundPlayer$SoundInfo;
              //   4: invokevirtual getInputStream : ()Ljava/io/InputStream;
              //   7: astore #5
              //   9: aconst_null
              //   10: astore_3
              //   11: new java/io/FileOutputStream
              //   14: dup
              //   15: aload_1
              //   16: invokespecial <init> : (Ljava/io/File;)V
              //   19: astore_2
              //   20: aload_2
              //   21: astore_3
              //   22: aload #5
              //   24: aload_2
              //   25: aload_0
              //   26: getfield val$soundInfo : Lcom/qualcomm/ftccommon/SoundPlayer$SoundInfo;
              //   29: getfield cbSize : I
              //   32: invokestatic copy : (Ljava/io/InputStream;Ljava/io/OutputStream;I)V
              //   35: aload_0
              //   36: getfield this$0 : Lcom/qualcomm/ftccommon/SoundPlayer;
              //   39: aload_2
              //   40: invokevirtual safeClose : (Ljava/lang/Object;)V
              //   43: aload_0
              //   44: getfield this$0 : Lcom/qualcomm/ftccommon/SoundPlayer;
              //   47: aload #5
              //   49: invokevirtual safeClose : (Ljava/lang/Object;)V
              //   52: aconst_null
              //   53: areturn
              //   54: astore #4
              //   56: goto -> 67
              //   59: astore_1
              //   60: goto -> 95
              //   63: astore #4
              //   65: aconst_null
              //   66: astore_2
              //   67: aload_2
              //   68: astore_3
              //   69: aload_0
              //   70: getfield this$0 : Lcom/qualcomm/ftccommon/SoundPlayer;
              //   73: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
              //   76: aload #4
              //   78: ldc 'exception caching file: %s'
              //   80: iconst_1
              //   81: anewarray java/lang/Object
              //   84: dup
              //   85: iconst_0
              //   86: aload_1
              //   87: aastore
              //   88: invokevirtual traceError : (Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
              //   91: goto -> 35
              //   94: astore_1
              //   95: aload_0
              //   96: getfield this$0 : Lcom/qualcomm/ftccommon/SoundPlayer;
              //   99: aload_3
              //   100: invokevirtual safeClose : (Ljava/lang/Object;)V
              //   103: aload_0
              //   104: getfield this$0 : Lcom/qualcomm/ftccommon/SoundPlayer;
              //   107: aload #5
              //   109: invokevirtual safeClose : (Ljava/lang/Object;)V
              //   112: aload_1
              //   113: athrow
              // Exception table:
              //   from	to	target	type
              //   11	20	63	java/io/IOException
              //   11	20	59	finally
              //   22	35	54	java/io/IOException
              //   22	35	94	finally
              //   69	91	94	finally
            }
          });
      if (soundInfo != null)
        releaseRef(soundInfo); 
    } 
  }
  
  protected SoundInfo ensureLoaded(Context paramContext, int paramInt) {
    synchronized (this.lock) {
      SoundInfo soundInfo2 = this.loadedSounds.getResource(paramInt);
      SoundInfo soundInfo1 = soundInfo2;
      if (soundInfo2 == null) {
        int i = getMsDuration(paramContext, paramInt);
        this.currentlyLoadingLatch = new CountDownLatch(1);
        soundInfo1 = new SoundInfo(paramContext, paramInt, i);
        this.currentlyLoadingInfo = soundInfo1;
        i = this.soundPool.load(paramContext, paramInt, 1);
        if (i != 0) {
          soundInfo1.initialize(i);
          this.loadedSounds.putResource(paramInt, soundInfo1);
          waitForLoadCompletion();
        } else {
          this.tracer.traceError("unable to load sound resource 0x%08x", new Object[] { Integer.valueOf(paramInt) });
        } 
      } 
      return soundInfo1;
    } 
  }
  
  protected SoundInfo ensureLoaded(Context paramContext, File paramFile) {
    synchronized (this.lock) {
      SoundInfo soundInfo2 = this.loadedSounds.getFile(paramFile);
      SoundInfo soundInfo1 = soundInfo2;
      if (soundInfo2 == null) {
        int i = getMsDuration(paramContext, paramFile);
        this.currentlyLoadingLatch = new CountDownLatch(1);
        soundInfo1 = new SoundInfo(paramFile, i);
        this.currentlyLoadingInfo = soundInfo1;
        i = this.soundPool.load(paramFile.getAbsolutePath(), 1);
        if (i != 0) {
          soundInfo1.initialize(i);
          this.loadedSounds.putFile(paramFile, soundInfo1);
          waitForLoadCompletion();
        } else {
          this.tracer.traceError("unable to load sound %s", new Object[] { paramFile });
        } 
      } 
      return soundInfo1;
    } 
  }
  
  protected SoundInfo ensureLoaded(String paramString, SoundFromFile paramSoundFromFile) {
    SoundInfo soundInfo = this.loadedSounds.getHash(paramString);
    return (soundInfo != null) ? soundInfo : ensureCached(paramString, paramSoundFromFile);
  }
  
  public float getMasterVolume() {
    return this.masterVolume;
  }
  
  protected int getMsDuration(Context paramContext, int paramInt) {
    Object object = this.lock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      this.mediaSizer.reset();
      AssetFileDescriptor assetFileDescriptor = paramContext.getResources().openRawResourceFd(paramInt);
      this.mediaSizer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
      assetFileDescriptor.close();
      this.mediaSizer.prepare();
      paramInt = this.mediaSizer.getDuration();
    } catch (IOException iOException) {
      this.tracer.traceError(iOException, "exception preparing media sizer; media duration taken to be zero", new Object[0]);
      paramInt = 0;
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    return (paramInt < 0) ? 0 : paramInt;
  }
  
  protected int getMsDuration(Context paramContext, File paramFile) {
    boolean bool;
    Uri uri = Uri.fromFile(paramFile);
    Object object = this.lock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      this.mediaSizer.reset();
      this.mediaSizer.setDataSource(paramContext, uri);
      this.mediaSizer.prepare();
      bool = this.mediaSizer.getDuration();
    } catch (IOException iOException) {
      this.tracer.traceError(iOException, "exception preparing media sizer; media duration taken to be zero", new Object[0]);
      bool = false;
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    return bool ? 0 : bool;
  }
  
  protected long getMsNow() {
    return AppUtil.getInstance().getWallClockTime();
  }
  
  public CallbackResult handleCommandPlaySound(String paramString) {
    CallbackResult callbackResult = CallbackResult.HANDLED;
    final CommandList.CmdPlaySound cmdPlaySound = CommandList.CmdPlaySound.deserialize(paramString);
    SoundInfo soundInfo = ensureLoaded(cmdPlaySound.hashString, new SoundFromFile() {
          public SoundPlayer.SoundInfo apply(File param1File) {
            return SoundPlayer.this.requestRemoteSound(param1File, cmdPlaySound.hashString);
          }
        });
    if (soundInfo != null) {
      RobotLog.getLocalTime(cmdPlaySound.msPresentationTime);
      getMsNow();
      startPlayingLoadedSound(soundInfo, cmdPlaySound.getParams(), null, null);
      releaseRef(soundInfo);
    } 
    return callbackResult;
  }
  
  public CallbackResult handleCommandRequestSound(Command paramCommand) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getExtra : ()Ljava/lang/String;
    //   4: astore #4
    //   6: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   9: astore #15
    //   11: aload #4
    //   13: invokestatic deserialize : (Ljava/lang/String;)Lcom/qualcomm/ftccommon/CommandList$CmdRequestSound;
    //   16: astore #16
    //   18: aload_0
    //   19: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   22: ldc_w 'handleCommandRequestSound(): hash=%s'
    //   25: iconst_1
    //   26: anewarray java/lang/Object
    //   29: dup
    //   30: iconst_0
    //   31: aload #16
    //   33: getfield hashString : Ljava/lang/String;
    //   36: aastore
    //   37: invokevirtual trace : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   40: aconst_null
    //   41: astore #12
    //   43: aconst_null
    //   44: astore #13
    //   46: aconst_null
    //   47: astore #5
    //   49: aconst_null
    //   50: astore #14
    //   52: aconst_null
    //   53: astore #7
    //   55: aconst_null
    //   56: astore #6
    //   58: new java/net/Socket
    //   61: dup
    //   62: aload_1
    //   63: invokevirtual getSender : ()Ljava/net/InetSocketAddress;
    //   66: invokevirtual getAddress : ()Ljava/net/InetAddress;
    //   69: aload #16
    //   71: getfield port : I
    //   74: invokespecial <init> : (Ljava/net/InetAddress;I)V
    //   77: astore #4
    //   79: aload #4
    //   81: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   84: astore #8
    //   86: aload #12
    //   88: astore #7
    //   90: aload #13
    //   92: astore #11
    //   94: aload #14
    //   96: astore #5
    //   98: aload #4
    //   100: astore #9
    //   102: aload #8
    //   104: astore #10
    //   106: aload_0
    //   107: getfield loadedSounds : Lcom/qualcomm/ftccommon/SoundPlayer$LoadedSoundCache;
    //   110: aload #16
    //   112: getfield hashString : Ljava/lang/String;
    //   115: invokevirtual getHash : (Ljava/lang/String;)Lcom/qualcomm/ftccommon/SoundPlayer$SoundInfo;
    //   118: astore #17
    //   120: aload #17
    //   122: ifnull -> 154
    //   125: aload #12
    //   127: astore #7
    //   129: aload #13
    //   131: astore #11
    //   133: aload #14
    //   135: astore #5
    //   137: aload #4
    //   139: astore #9
    //   141: aload #8
    //   143: astore #10
    //   145: aload #17
    //   147: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   150: astore_1
    //   151: goto -> 199
    //   154: aload #12
    //   156: astore #7
    //   158: aload #13
    //   160: astore #11
    //   162: aload #14
    //   164: astore #5
    //   166: aload #4
    //   168: astore #9
    //   170: aload #8
    //   172: astore #10
    //   174: aload_0
    //   175: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   178: ldc_w 'handleCommandRequestSound(): can't find hash=%s'
    //   181: iconst_1
    //   182: anewarray java/lang/Object
    //   185: dup
    //   186: iconst_0
    //   187: aload #16
    //   189: getfield hashString : Ljava/lang/String;
    //   192: aastore
    //   193: invokevirtual traceError : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   196: aload #6
    //   198: astore_1
    //   199: aload_1
    //   200: ifnull -> 377
    //   203: aload_1
    //   204: astore #7
    //   206: aload_1
    //   207: astore #11
    //   209: aload_1
    //   210: astore #5
    //   212: aload #4
    //   214: astore #9
    //   216: aload #8
    //   218: astore #10
    //   220: aload #8
    //   222: aload #17
    //   224: getfield cbSize : I
    //   227: invokestatic intToByteArray : (I)[B
    //   230: invokevirtual write : ([B)V
    //   233: aload_1
    //   234: astore #7
    //   236: aload_1
    //   237: astore #11
    //   239: aload_1
    //   240: astore #5
    //   242: aload #4
    //   244: astore #9
    //   246: aload #8
    //   248: astore #10
    //   250: sipush #256
    //   253: newarray byte
    //   255: astore #6
    //   257: iconst_0
    //   258: istore_2
    //   259: aload_1
    //   260: astore #7
    //   262: aload_1
    //   263: astore #11
    //   265: aload_1
    //   266: astore #5
    //   268: aload #4
    //   270: astore #9
    //   272: aload #8
    //   274: astore #10
    //   276: aload_1
    //   277: aload #6
    //   279: invokevirtual read : ([B)I
    //   282: istore_3
    //   283: iload_3
    //   284: ifge -> 344
    //   287: aload_1
    //   288: astore #7
    //   290: aload_1
    //   291: astore #11
    //   293: aload_1
    //   294: astore #5
    //   296: aload #4
    //   298: astore #9
    //   300: aload #8
    //   302: astore #10
    //   304: aload_0
    //   305: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   308: ldc_w 'handleCommandRequestSound(): finished: %s cbSize=%d cbWritten=%d'
    //   311: iconst_3
    //   312: anewarray java/lang/Object
    //   315: dup
    //   316: iconst_0
    //   317: aload #17
    //   319: aastore
    //   320: dup
    //   321: iconst_1
    //   322: aload #17
    //   324: getfield cbSize : I
    //   327: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   330: aastore
    //   331: dup
    //   332: iconst_2
    //   333: iload_2
    //   334: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   337: aastore
    //   338: invokevirtual trace : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   341: goto -> 403
    //   344: aload_1
    //   345: astore #7
    //   347: aload_1
    //   348: astore #11
    //   350: aload_1
    //   351: astore #5
    //   353: aload #4
    //   355: astore #9
    //   357: aload #8
    //   359: astore #10
    //   361: aload #8
    //   363: aload #6
    //   365: iconst_0
    //   366: iload_3
    //   367: invokevirtual write : ([BII)V
    //   370: iload_2
    //   371: iload_3
    //   372: iadd
    //   373: istore_2
    //   374: goto -> 259
    //   377: aload_1
    //   378: astore #7
    //   380: aload_1
    //   381: astore #11
    //   383: aload_1
    //   384: astore #5
    //   386: aload #4
    //   388: astore #9
    //   390: aload #8
    //   392: astore #10
    //   394: aload #8
    //   396: iconst_0
    //   397: invokestatic intToByteArray : (I)[B
    //   400: invokevirtual write : ([B)V
    //   403: aload_1
    //   404: astore #7
    //   406: aload_1
    //   407: astore #11
    //   409: aload_1
    //   410: astore #5
    //   412: aload #4
    //   414: astore #9
    //   416: aload #8
    //   418: astore #10
    //   420: aload #17
    //   422: invokestatic releaseRef : (Lcom/qualcomm/ftccommon/SoundPlayer$SoundInfo;)V
    //   425: goto -> 562
    //   428: astore #6
    //   430: aload #7
    //   432: astore_1
    //   433: aload #4
    //   435: astore #7
    //   437: aload #8
    //   439: astore #4
    //   441: goto -> 527
    //   444: astore #6
    //   446: aload #11
    //   448: astore_1
    //   449: aload #4
    //   451: astore #7
    //   453: aload #8
    //   455: astore #4
    //   457: goto -> 527
    //   460: astore_1
    //   461: aconst_null
    //   462: astore #10
    //   464: aload #7
    //   466: astore #5
    //   468: goto -> 587
    //   471: astore #6
    //   473: goto -> 478
    //   476: astore #6
    //   478: aconst_null
    //   479: astore #8
    //   481: aload #5
    //   483: astore_1
    //   484: aload #4
    //   486: astore #7
    //   488: aload #8
    //   490: astore #4
    //   492: goto -> 527
    //   495: astore_1
    //   496: aconst_null
    //   497: astore #10
    //   499: aload #10
    //   501: astore #4
    //   503: aload #7
    //   505: astore #5
    //   507: goto -> 587
    //   510: astore #6
    //   512: goto -> 517
    //   515: astore #6
    //   517: aconst_null
    //   518: astore #4
    //   520: aload #4
    //   522: astore #7
    //   524: aload #5
    //   526: astore_1
    //   527: aload_1
    //   528: astore #5
    //   530: aload #7
    //   532: astore #9
    //   534: aload #4
    //   536: astore #10
    //   538: aload_0
    //   539: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   542: aload #6
    //   544: ldc_w 'handleCommandRequestSound(): exception thrown'
    //   547: iconst_0
    //   548: anewarray java/lang/Object
    //   551: invokevirtual traceError : (Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
    //   554: aload #4
    //   556: astore #8
    //   558: aload #7
    //   560: astore #4
    //   562: aload_0
    //   563: aload_1
    //   564: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   567: aload_0
    //   568: aload #8
    //   570: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   573: aload_0
    //   574: aload #4
    //   576: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   579: aload #15
    //   581: areturn
    //   582: astore_1
    //   583: aload #9
    //   585: astore #4
    //   587: aload_0
    //   588: aload #5
    //   590: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   593: aload_0
    //   594: aload #10
    //   596: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   599: aload_0
    //   600: aload #4
    //   602: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   605: aload_1
    //   606: athrow
    // Exception table:
    //   from	to	target	type
    //   58	79	515	java/io/IOException
    //   58	79	510	java/lang/RuntimeException
    //   58	79	495	finally
    //   79	86	476	java/io/IOException
    //   79	86	471	java/lang/RuntimeException
    //   79	86	460	finally
    //   106	120	444	java/io/IOException
    //   106	120	428	java/lang/RuntimeException
    //   106	120	582	finally
    //   145	151	444	java/io/IOException
    //   145	151	428	java/lang/RuntimeException
    //   145	151	582	finally
    //   174	196	444	java/io/IOException
    //   174	196	428	java/lang/RuntimeException
    //   174	196	582	finally
    //   220	233	444	java/io/IOException
    //   220	233	428	java/lang/RuntimeException
    //   220	233	582	finally
    //   250	257	444	java/io/IOException
    //   250	257	428	java/lang/RuntimeException
    //   250	257	582	finally
    //   276	283	444	java/io/IOException
    //   276	283	428	java/lang/RuntimeException
    //   276	283	582	finally
    //   304	341	444	java/io/IOException
    //   304	341	428	java/lang/RuntimeException
    //   304	341	582	finally
    //   361	370	444	java/io/IOException
    //   361	370	428	java/lang/RuntimeException
    //   361	370	582	finally
    //   394	403	444	java/io/IOException
    //   394	403	428	java/lang/RuntimeException
    //   394	403	582	finally
    //   420	425	444	java/io/IOException
    //   420	425	428	java/lang/RuntimeException
    //   420	425	582	finally
    //   538	554	582	finally
  }
  
  public CallbackResult handleCommandStopPlayingSounds(Command paramCommand) {
    String str = paramCommand.getExtra();
    CallbackResult callbackResult = CallbackResult.HANDLED;
    CommandList.CmdStopPlayingSounds cmdStopPlayingSounds = CommandList.CmdStopPlayingSounds.deserialize(str);
    this.tracer.trace("handleCommandStopPlayingSounds(): what=%s", new Object[] { cmdStopPlayingSounds.stopWhat });
    internalStopPlaying(cmdStopPlayingSounds.stopWhat);
    return callbackResult;
  }
  
  protected void internalStopPlaying(StopWhat paramStopWhat) {
    synchronized (this.lock) {
      for (CurrentlyPlaying currentlyPlaying : this.currentlyPlayingSounds) {
        if (paramStopWhat == StopWhat.All || (currentlyPlaying.isLooping() && paramStopWhat == StopWhat.Loops))
          currentlyPlaying.msFinish = Long.MIN_VALUE; 
      } 
      checkForFinishedSounds();
      if (this.isRobotController) {
        Command command = new Command("CMD_PLAY_SOUND", (new CommandList.CmdStopPlayingSounds(paramStopWhat)).serialize());
        NetworkConnectionHandler.getInstance().sendCommand(command);
      } 
      return;
    } 
  }
  
  public boolean isLocalSoundOn() {
    return (this.sharedPreferences.getBoolean(AppUtil.getDefContext().getString(R.string.pref_sound_on_off), true) && this.sharedPreferences.getBoolean(AppUtil.getDefContext().getString(R.string.pref_has_speaker), true));
  }
  
  protected void loadAndStartPlaying(Context paramContext, int paramInt, PlaySoundParams paramPlaySoundParams, Consumer<Integer> paramConsumer, Runnable paramRunnable) {
    synchronized (this.lock) {
      SoundInfo soundInfo = ensureLoaded(paramContext, paramInt);
      if (soundInfo != null) {
        startPlayingLoadedSound(soundInfo, paramPlaySoundParams, paramConsumer, paramRunnable);
        releaseRef(soundInfo);
      } 
      return;
    } 
  }
  
  protected void loadAndStartPlaying(Context paramContext, File paramFile, PlaySoundParams paramPlaySoundParams, Consumer<Integer> paramConsumer, Runnable paramRunnable) {
    synchronized (this.lock) {
      SoundInfo soundInfo = ensureLoaded(paramContext, paramFile);
      if (soundInfo != null) {
        startPlayingLoadedSound(soundInfo, paramPlaySoundParams, paramConsumer, paramRunnable);
        releaseRef(soundInfo);
      } 
      return;
    } 
  }
  
  public void onLoadComplete(SoundPool paramSoundPool, int paramInt1, int paramInt2) {
    this.tracer.trace("onLoadComplete(%s, samp=%d)=%d", new Object[] { this.currentlyLoadingInfo, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    this.currentlyLoadingLatch.countDown();
  }
  
  @Deprecated
  public void play(Context paramContext, int paramInt) {
    startPlaying(paramContext, paramInt);
  }
  
  public void play(Context paramContext, int paramInt1, float paramFloat1, int paramInt2, float paramFloat2) {
    PlaySoundParams playSoundParams = new PlaySoundParams(false);
    playSoundParams.volume = paramFloat1;
    playSoundParams.loopControl = paramInt2;
    playSoundParams.rate = paramFloat2;
    startPlaying(paramContext, paramInt1, playSoundParams, (Consumer<Integer>)null, (Runnable)null);
  }
  
  @Deprecated
  public void play(Context paramContext, int paramInt, boolean paramBoolean) {
    startPlaying(paramContext, paramInt, new PlaySoundParams(paramBoolean), (Consumer<Integer>)null, (Runnable)null);
  }
  
  public void play(Context paramContext, File paramFile, float paramFloat1, int paramInt, float paramFloat2) {
    PlaySoundParams playSoundParams = new PlaySoundParams(false);
    playSoundParams.volume = paramFloat1;
    playSoundParams.loopControl = paramInt;
    playSoundParams.rate = paramFloat2;
    startPlaying(paramContext, paramFile, playSoundParams, (Consumer<Integer>)null, (Runnable)null);
  }
  
  public void prefillSoundCache(int... paramVarArgs) {
    int j = paramVarArgs.length;
    for (int i = 0; i < j; i++) {
      final int resId = paramVarArgs[i];
      this.threadPool.submit(new Runnable() {
            public void run() {
              SoundPlayer.this.ensureCached(AppUtil.getDefContext(), resId);
            }
          });
    } 
  }
  
  public boolean preload(Context paramContext, int paramInt) {
    synchronized (this.lock) {
      SoundInfo soundInfo = ensureLoaded(paramContext, paramInt);
      if (soundInfo != null) {
        boolean bool1 = true;
        releaseRef(soundInfo);
        return bool1;
      } 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_4} */
    return bool;
  }
  
  public boolean preload(Context paramContext, File paramFile) {
    synchronized (this.lock) {
      SoundInfo soundInfo = ensureLoaded(paramContext, paramFile);
      if (soundInfo != null) {
        boolean bool1 = true;
        releaseRef(soundInfo);
        return bool1;
      } 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_4} */
    return bool;
  }
  
  SoundInfo requestRemoteSound(File paramFile, String paramString) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #7
    //   3: iconst_1
    //   4: istore #8
    //   6: iconst_1
    //   7: istore #9
    //   9: iconst_1
    //   10: istore #4
    //   12: iconst_1
    //   13: istore #10
    //   15: iconst_1
    //   16: istore #5
    //   18: iconst_1
    //   19: istore_3
    //   20: aconst_null
    //   21: astore #26
    //   23: aconst_null
    //   24: astore #27
    //   26: aconst_null
    //   27: astore #16
    //   29: aconst_null
    //   30: astore #28
    //   32: new java/io/FileOutputStream
    //   35: dup
    //   36: aload_1
    //   37: invokespecial <init> : (Ljava/io/File;)V
    //   40: astore #12
    //   42: new java/net/ServerSocket
    //   45: dup
    //   46: iconst_0
    //   47: invokespecial <init> : (I)V
    //   50: astore #15
    //   52: new com/qualcomm/ftccommon/CommandList$CmdRequestSound
    //   55: dup
    //   56: aload_2
    //   57: aload #15
    //   59: invokevirtual getLocalPort : ()I
    //   62: invokespecial <init> : (Ljava/lang/String;I)V
    //   65: astore #13
    //   67: aload_0
    //   68: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   71: ldc_w 'handleCommandPlaySound(): requesting: port=%d hash=%s'
    //   74: iconst_2
    //   75: anewarray java/lang/Object
    //   78: dup
    //   79: iconst_0
    //   80: aload #13
    //   82: getfield port : I
    //   85: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   88: aastore
    //   89: dup
    //   90: iconst_1
    //   91: aload #13
    //   93: getfield hashString : Ljava/lang/String;
    //   96: aastore
    //   97: invokevirtual trace : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   100: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
    //   103: new com/qualcomm/robotcore/robocol/Command
    //   106: dup
    //   107: ldc_w 'CMD_REQUEST_SOUND'
    //   110: aload #13
    //   112: invokevirtual serialize : ()Ljava/lang/String;
    //   115: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   118: invokevirtual sendCommand : (Lcom/qualcomm/robotcore/robocol/Command;)V
    //   121: aload #15
    //   123: sipush #1000
    //   126: invokevirtual setSoTimeout : (I)V
    //   129: aload #15
    //   131: invokevirtual accept : ()Ljava/net/Socket;
    //   134: astore #13
    //   136: aload #13
    //   138: sipush #1000
    //   141: invokevirtual setSoTimeout : (I)V
    //   144: aload #13
    //   146: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   149: astore #14
    //   151: iload #8
    //   153: istore #5
    //   155: aload #12
    //   157: astore #24
    //   159: aload #14
    //   161: astore #22
    //   163: aload #13
    //   165: astore #20
    //   167: iload #9
    //   169: istore #6
    //   171: aload #12
    //   173: astore #25
    //   175: aload #14
    //   177: astore #23
    //   179: aload #13
    //   181: astore #21
    //   183: iload #10
    //   185: istore #4
    //   187: aload #12
    //   189: astore #16
    //   191: aload #15
    //   193: astore #19
    //   195: aload #14
    //   197: astore #17
    //   199: aload #13
    //   201: astore #18
    //   203: iconst_4
    //   204: newarray byte
    //   206: astore #29
    //   208: iload #8
    //   210: istore #5
    //   212: aload #12
    //   214: astore #24
    //   216: aload #14
    //   218: astore #22
    //   220: aload #13
    //   222: astore #20
    //   224: iload #9
    //   226: istore #6
    //   228: aload #12
    //   230: astore #25
    //   232: aload #14
    //   234: astore #23
    //   236: aload #13
    //   238: astore #21
    //   240: iload #10
    //   242: istore #4
    //   244: aload #12
    //   246: astore #16
    //   248: aload #15
    //   250: astore #19
    //   252: aload #14
    //   254: astore #17
    //   256: aload #13
    //   258: astore #18
    //   260: iconst_4
    //   261: aload #14
    //   263: aload #29
    //   265: invokevirtual read : ([B)I
    //   268: if_icmpne -> 595
    //   271: iload #8
    //   273: istore #5
    //   275: aload #12
    //   277: astore #24
    //   279: aload #14
    //   281: astore #22
    //   283: aload #13
    //   285: astore #20
    //   287: iload #9
    //   289: istore #6
    //   291: aload #12
    //   293: astore #25
    //   295: aload #14
    //   297: astore #23
    //   299: aload #13
    //   301: astore #21
    //   303: iload #10
    //   305: istore #4
    //   307: aload #12
    //   309: astore #16
    //   311: aload #15
    //   313: astore #19
    //   315: aload #14
    //   317: astore #17
    //   319: aload #13
    //   321: astore #18
    //   323: aload #29
    //   325: invokestatic byteArrayToInt : ([B)I
    //   328: istore #11
    //   330: iload #11
    //   332: ifle -> 520
    //   335: iload #8
    //   337: istore #5
    //   339: aload #12
    //   341: astore #24
    //   343: aload #14
    //   345: astore #22
    //   347: aload #13
    //   349: astore #20
    //   351: iload #9
    //   353: istore #6
    //   355: aload #12
    //   357: astore #25
    //   359: aload #14
    //   361: astore #23
    //   363: aload #13
    //   365: astore #21
    //   367: iload #10
    //   369: istore #4
    //   371: aload #12
    //   373: astore #16
    //   375: aload #15
    //   377: astore #19
    //   379: aload #14
    //   381: astore #17
    //   383: aload #13
    //   385: astore #18
    //   387: aload #14
    //   389: aload #12
    //   391: iload #11
    //   393: invokestatic copy : (Ljava/io/InputStream;Ljava/io/OutputStream;I)V
    //   396: iload #8
    //   398: istore #5
    //   400: aload #12
    //   402: astore #24
    //   404: aload #14
    //   406: astore #22
    //   408: aload #13
    //   410: astore #20
    //   412: iload #9
    //   414: istore #6
    //   416: aload #12
    //   418: astore #25
    //   420: aload #14
    //   422: astore #23
    //   424: aload #13
    //   426: astore #21
    //   428: iload #10
    //   430: istore #4
    //   432: aload #12
    //   434: astore #16
    //   436: aload #15
    //   438: astore #19
    //   440: aload #14
    //   442: astore #17
    //   444: aload #13
    //   446: astore #18
    //   448: aload_0
    //   449: aload #12
    //   451: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   454: aload_0
    //   455: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   458: ldc_w 'handleCommandPlaySound(): received: hash=%s'
    //   461: iconst_1
    //   462: anewarray java/lang/Object
    //   465: dup
    //   466: iconst_0
    //   467: aload_2
    //   468: aastore
    //   469: invokevirtual trace : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   472: aload_0
    //   473: invokestatic getDefContext : ()Landroid/content/Context;
    //   476: aload_1
    //   477: invokevirtual ensureLoaded : (Landroid/content/Context;Ljava/io/File;)Lcom/qualcomm/ftccommon/SoundPlayer$SoundInfo;
    //   480: astore_2
    //   481: aconst_null
    //   482: astore #12
    //   484: iconst_0
    //   485: istore_3
    //   486: goto -> 763
    //   489: astore_2
    //   490: aconst_null
    //   491: astore #12
    //   493: iconst_0
    //   494: istore #4
    //   496: goto -> 1011
    //   499: astore_2
    //   500: goto -> 504
    //   503: astore_2
    //   504: aconst_null
    //   505: astore #12
    //   507: iconst_0
    //   508: istore_3
    //   509: goto -> 919
    //   512: aconst_null
    //   513: astore #12
    //   515: iconst_0
    //   516: istore_3
    //   517: goto -> 697
    //   520: iload #8
    //   522: istore #5
    //   524: aload #12
    //   526: astore #24
    //   528: aload #14
    //   530: astore #22
    //   532: aload #13
    //   534: astore #20
    //   536: iload #9
    //   538: istore #6
    //   540: aload #12
    //   542: astore #25
    //   544: aload #14
    //   546: astore #23
    //   548: aload #13
    //   550: astore #21
    //   552: iload #10
    //   554: istore #4
    //   556: aload #12
    //   558: astore #16
    //   560: aload #15
    //   562: astore #19
    //   564: aload #14
    //   566: astore #17
    //   568: aload #13
    //   570: astore #18
    //   572: aload_0
    //   573: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   576: ldc_w 'handleCommandPlaySound(): client couldn't send sound'
    //   579: iconst_0
    //   580: anewarray java/lang/Object
    //   583: invokevirtual traceError : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   586: iload #7
    //   588: istore_3
    //   589: aload #28
    //   591: astore_2
    //   592: goto -> 763
    //   595: iload #8
    //   597: istore #5
    //   599: aload #12
    //   601: astore #24
    //   603: aload #14
    //   605: astore #22
    //   607: aload #13
    //   609: astore #20
    //   611: iload #9
    //   613: istore #6
    //   615: aload #12
    //   617: astore #25
    //   619: aload #14
    //   621: astore #23
    //   623: aload #13
    //   625: astore #21
    //   627: iload #10
    //   629: istore #4
    //   631: aload #12
    //   633: astore #16
    //   635: aload #15
    //   637: astore #19
    //   639: aload #14
    //   641: astore #17
    //   643: aload #13
    //   645: astore #18
    //   647: new java/io/IOException
    //   650: dup
    //   651: ldc_w 'framing error'
    //   654: invokespecial <init> : (Ljava/lang/String;)V
    //   657: athrow
    //   658: astore_2
    //   659: iload #5
    //   661: istore #4
    //   663: aload #16
    //   665: astore #14
    //   667: goto -> 1011
    //   670: astore_2
    //   671: goto -> 675
    //   674: astore_2
    //   675: aconst_null
    //   676: astore #14
    //   678: iload #4
    //   680: istore_3
    //   681: goto -> 919
    //   684: aconst_null
    //   685: astore #14
    //   687: goto -> 697
    //   690: aconst_null
    //   691: astore #14
    //   693: aload #14
    //   695: astore #13
    //   697: iload_3
    //   698: istore #5
    //   700: aload #12
    //   702: astore #24
    //   704: aload #14
    //   706: astore #22
    //   708: aload #13
    //   710: astore #20
    //   712: iload_3
    //   713: istore #6
    //   715: aload #12
    //   717: astore #25
    //   719: aload #14
    //   721: astore #23
    //   723: aload #13
    //   725: astore #21
    //   727: iload_3
    //   728: istore #4
    //   730: aload #12
    //   732: astore #16
    //   734: aload #15
    //   736: astore #19
    //   738: aload #14
    //   740: astore #17
    //   742: aload #13
    //   744: astore #18
    //   746: aload_0
    //   747: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   750: ldc_w 'timed out awaiting sound file'
    //   753: iconst_0
    //   754: anewarray java/lang/Object
    //   757: invokevirtual traceError : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   760: aload #28
    //   762: astore_2
    //   763: aload_0
    //   764: aload #14
    //   766: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   769: aload_0
    //   770: aload #13
    //   772: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   775: aload_0
    //   776: aload #15
    //   778: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   781: aload_0
    //   782: aload #12
    //   784: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   787: aload_2
    //   788: astore #12
    //   790: iload_3
    //   791: ifeq -> 991
    //   794: aload_1
    //   795: invokevirtual delete : ()Z
    //   798: pop
    //   799: aload_2
    //   800: areturn
    //   801: astore_2
    //   802: iload #5
    //   804: istore_3
    //   805: aload #24
    //   807: astore #12
    //   809: aload #22
    //   811: astore #14
    //   813: aload #20
    //   815: astore #13
    //   817: goto -> 919
    //   820: astore_2
    //   821: iload #6
    //   823: istore_3
    //   824: aload #25
    //   826: astore #12
    //   828: aload #23
    //   830: astore #14
    //   832: aload #21
    //   834: astore #13
    //   836: goto -> 919
    //   839: astore_2
    //   840: aconst_null
    //   841: astore #13
    //   843: iload #5
    //   845: istore #4
    //   847: aload #16
    //   849: astore #14
    //   851: goto -> 1011
    //   854: astore_2
    //   855: goto -> 859
    //   858: astore_2
    //   859: aconst_null
    //   860: astore #14
    //   862: goto -> 912
    //   865: astore_2
    //   866: goto -> 881
    //   869: astore_2
    //   870: goto -> 874
    //   873: astore_2
    //   874: goto -> 906
    //   877: astore_2
    //   878: aconst_null
    //   879: astore #12
    //   881: aconst_null
    //   882: astore #15
    //   884: aconst_null
    //   885: astore #13
    //   887: iload #5
    //   889: istore #4
    //   891: aload #16
    //   893: astore #14
    //   895: goto -> 1011
    //   898: astore_2
    //   899: goto -> 903
    //   902: astore_2
    //   903: aconst_null
    //   904: astore #12
    //   906: aconst_null
    //   907: astore #15
    //   909: aconst_null
    //   910: astore #14
    //   912: aload #14
    //   914: astore #13
    //   916: iload #4
    //   918: istore_3
    //   919: iload_3
    //   920: istore #4
    //   922: aload #12
    //   924: astore #16
    //   926: aload #15
    //   928: astore #19
    //   930: aload #14
    //   932: astore #17
    //   934: aload #13
    //   936: astore #18
    //   938: aload_0
    //   939: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   942: aload_2
    //   943: ldc_w 'handleCommandPlaySound(): exception thrown'
    //   946: iconst_0
    //   947: anewarray java/lang/Object
    //   950: invokevirtual traceError : (Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
    //   953: aload_0
    //   954: aload #14
    //   956: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   959: aload_0
    //   960: aload #13
    //   962: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   965: aload_0
    //   966: aload #15
    //   968: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   971: aload_0
    //   972: aload #12
    //   974: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   977: aload #27
    //   979: astore #12
    //   981: iload_3
    //   982: ifeq -> 991
    //   985: aload #26
    //   987: astore_2
    //   988: goto -> 794
    //   991: aload #12
    //   993: areturn
    //   994: astore_2
    //   995: aload #18
    //   997: astore #13
    //   999: aload #17
    //   1001: astore #14
    //   1003: aload #19
    //   1005: astore #15
    //   1007: aload #16
    //   1009: astore #12
    //   1011: aload_0
    //   1012: aload #14
    //   1014: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   1017: aload_0
    //   1018: aload #13
    //   1020: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   1023: aload_0
    //   1024: aload #15
    //   1026: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   1029: aload_0
    //   1030: aload #12
    //   1032: invokevirtual safeClose : (Ljava/lang/Object;)V
    //   1035: iload #4
    //   1037: ifeq -> 1045
    //   1040: aload_1
    //   1041: invokevirtual delete : ()Z
    //   1044: pop
    //   1045: aload_2
    //   1046: athrow
    //   1047: astore_2
    //   1048: goto -> 690
    //   1051: astore_2
    //   1052: goto -> 684
    //   1055: astore_2
    //   1056: goto -> 697
    //   1059: astore_2
    //   1060: goto -> 512
    // Exception table:
    //   from	to	target	type
    //   32	42	902	java/io/IOException
    //   32	42	898	java/lang/RuntimeException
    //   32	42	877	finally
    //   42	52	873	java/io/IOException
    //   42	52	869	java/lang/RuntimeException
    //   42	52	865	finally
    //   52	129	858	java/io/IOException
    //   52	129	854	java/lang/RuntimeException
    //   52	129	839	finally
    //   129	136	1047	java/net/SocketTimeoutException
    //   129	136	858	java/io/IOException
    //   129	136	854	java/lang/RuntimeException
    //   129	136	839	finally
    //   136	151	1051	java/net/SocketTimeoutException
    //   136	151	674	java/io/IOException
    //   136	151	670	java/lang/RuntimeException
    //   136	151	658	finally
    //   203	208	1055	java/net/SocketTimeoutException
    //   203	208	820	java/io/IOException
    //   203	208	801	java/lang/RuntimeException
    //   203	208	994	finally
    //   260	271	1055	java/net/SocketTimeoutException
    //   260	271	820	java/io/IOException
    //   260	271	801	java/lang/RuntimeException
    //   260	271	994	finally
    //   323	330	1055	java/net/SocketTimeoutException
    //   323	330	820	java/io/IOException
    //   323	330	801	java/lang/RuntimeException
    //   323	330	994	finally
    //   387	396	1055	java/net/SocketTimeoutException
    //   387	396	820	java/io/IOException
    //   387	396	801	java/lang/RuntimeException
    //   387	396	994	finally
    //   448	454	1055	java/net/SocketTimeoutException
    //   448	454	820	java/io/IOException
    //   448	454	801	java/lang/RuntimeException
    //   448	454	994	finally
    //   454	481	1059	java/net/SocketTimeoutException
    //   454	481	503	java/io/IOException
    //   454	481	499	java/lang/RuntimeException
    //   454	481	489	finally
    //   572	586	1055	java/net/SocketTimeoutException
    //   572	586	820	java/io/IOException
    //   572	586	801	java/lang/RuntimeException
    //   572	586	994	finally
    //   647	658	1055	java/net/SocketTimeoutException
    //   647	658	820	java/io/IOException
    //   647	658	801	java/lang/RuntimeException
    //   647	658	994	finally
    //   746	760	820	java/io/IOException
    //   746	760	801	java/lang/RuntimeException
    //   746	760	994	finally
    //   938	953	994	finally
  }
  
  protected void safeClose(Object paramObject) {
    if (paramObject != null)
      try {
        boolean bool = paramObject instanceof Flushable;
        if (bool)
          try {
            ((Flushable)paramObject).flush();
          } catch (IOException iOException) {
            this.tracer.traceError(iOException, "exception while flushing", new Object[0]);
          }  
        if (paramObject instanceof Closeable) {
          ((Closeable)paramObject).close();
          return;
        } 
        throw new IllegalArgumentException("Unknown object to close");
      } catch (IOException iOException) {
        this.tracer.traceError(iOException, "exception while closing", new Object[0]);
      }  
  }
  
  public void setMasterVolume(float paramFloat) {
    synchronized (this.lock) {
      this.masterVolume = paramFloat;
      return;
    } 
  }
  
  public void startPlaying(Context paramContext, int paramInt) {
    startPlaying(paramContext, paramInt, new PlaySoundParams(true), (Consumer<Integer>)null, (Runnable)null);
  }
  
  public void startPlaying(final Context context, final int resId, final PlaySoundParams params, final Consumer<Integer> runWhenStarted, final Runnable runWhenFinished) {
    this.threadPool.execute(new Runnable() {
          public void run() {
            SoundPlayer.this.loadAndStartPlaying(context, resId, params, runWhenStarted, runWhenFinished);
          }
        });
  }
  
  public void startPlaying(Context paramContext, File paramFile) {
    startPlaying(paramContext, paramFile, new PlaySoundParams(true), (Consumer<Integer>)null, (Runnable)null);
  }
  
  public void startPlaying(final Context context, final File file, final PlaySoundParams params, final Consumer<Integer> runWhenStarted, final Runnable runWhenFinished) {
    if (file == null)
      return; 
    this.threadPool.execute(new Runnable() {
          public void run() {
            SoundPlayer.this.loadAndStartPlaying(context, file, params, runWhenStarted, runWhenFinished);
          }
        });
  }
  
  protected void startPlayingLoadedSound(final SoundInfo soundInfo, final PlaySoundParams params, final Consumer<Integer> runWhenStarted, final Runnable runWhenFinished) {
    if (params == null) {
      params = new PlaySoundParams();
    } else {
      params = new PlaySoundParams(params);
    } 
    params.volume *= this.masterVolume;
    if (soundInfo != null)
      synchronized (this.lock) {
        float f1;
        addRef(soundInfo);
        this.loadedSounds.noteSoundUsage(soundInfo);
        if (isLocalSoundOn()) {
          f1 = this.soundOnVolume;
        } else {
          f1 = this.soundOffVolume;
        } 
        float f2 = params.volume;
        checkForFinishedSounds();
        long l2 = getMsNow();
        final long msPresentation = Long.MIN_VALUE;
        for (CurrentlyPlaying currentlyPlaying : this.currentlyPlayingSounds) {
          if (!currentlyPlaying.isLooping())
            l1 = Math.max(l1, currentlyPlaying.msFinish); 
        } 
        if (params.waitForNonLoopingSoundsToFinish) {
          l1 = Math.max(l2, l1);
        } else {
          l1 = l2;
        } 
        l2 = l1 - l2;
        Runnable runnable = new Runnable() {
            public void run() {
              synchronized (SoundPlayer.this.lock) {
                int i;
                long l = SoundPlayer.this.getMsNow();
                final int streamId = SoundPlayer.this.soundPool.play(soundInfo.sampleId, volume, volume, 1, params.loopControl, params.rate);
                if (j != 0) {
                  i = 1;
                } else {
                  i = 0;
                } 
                if (i) {
                  long l1 = soundInfo.msDuration;
                  if (params.isLooping()) {
                    i = 1;
                  } else {
                    i = params.loopControl + 1;
                  } 
                  long l2 = l1 * i;
                  SoundPlayer.CurrentlyPlaying currentlyPlaying = new SoundPlayer.CurrentlyPlaying();
                  currentlyPlaying.streamId = j;
                  currentlyPlaying.loopControl = params.loopControl;
                  if (params.isLooping()) {
                    l1 = Long.MAX_VALUE;
                  } else {
                    l1 = l + l2;
                  } 
                  currentlyPlaying.msFinish = l1;
                  currentlyPlaying.runWhenFinished = runWhenFinished;
                  SoundPlayer.this.currentlyPlayingSounds.add(currentlyPlaying);
                  if (runWhenFinished != null && !params.isLooping())
                    SoundPlayer.this.scheduledThreadPool.schedule(new Runnable() {
                          public void run() {
                            SoundPlayer.this.checkForFinishedSounds();
                          }
                        },  l2 + ((params.loopControl + 1) * 5), TimeUnit.MILLISECONDS); 
                  SoundPlayer.this.tracer.trace("playing volume=%f %s", new Object[] { Float.valueOf(this.val$volume), this.val$soundInfo });
                  soundInfo.msLastPlay = l;
                } else {
                  SoundPlayer.this.tracer.traceError("unable to play %s", new Object[] { this.val$soundInfo });
                } 
                SoundPlayer.releaseRef(soundInfo);
                if (runWhenStarted != null)
                  SoundPlayer.this.threadPool.execute(new Runnable() {
                        public void run() {
                          runWhenStarted.accept(Integer.valueOf(streamId));
                        }
                      }); 
                if (SoundPlayer.this.isRobotController) {
                  null = new Command("CMD_PLAY_SOUND", (new CommandList.CmdPlaySound(msPresentation, soundInfo.hashString, params)).serialize());
                  null.setTransmissionDeadline(new Deadline(400L, TimeUnit.MILLISECONDS));
                  NetworkConnectionHandler.getInstance().sendCommand((Command)null);
                } 
                return;
              } 
            }
          };
        if (l2 > 0L) {
          this.scheduledThreadPool.schedule(runnable, l2, TimeUnit.MILLISECONDS);
        } else {
          runnable.run();
        } 
        return;
      }  
  }
  
  public void stopPlayingAll() {
    internalStopPlaying(StopWhat.All);
  }
  
  public void stopPlayingLoops() {
    internalStopPlaying(StopWhat.Loops);
  }
  
  protected void waitForLoadCompletion() {
    try {
      this.currentlyLoadingLatch.await();
      this.currentlyLoadingLatch = null;
      this.currentlyLoadingInfo = null;
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  protected static class CurrentlyPlaying {
    protected int loopControl = 0;
    
    protected long msFinish = Long.MAX_VALUE;
    
    protected Runnable runWhenFinished = null;
    
    protected int streamId = 0;
    
    protected boolean isLooping() {
      return (this.loopControl == -1);
    }
  }
  
  protected static class InstanceHolder {
    public static SoundPlayer theInstance = new SoundPlayer(3, 8);
  }
  
  protected class LoadedSoundCache {
    private final int capacity;
    
    private final Map<String, SoundPlayer.SoundInfo> hashMap;
    
    private final Map<Object, SoundPlayer.SoundInfo> keyMap;
    
    private final Object lock = new Object();
    
    private boolean unloadOnRemove;
    
    LoadedSoundCache(int param1Int) {
      this.keyMap = new SoundInfoMap(param1Int);
      this.hashMap = new SoundInfoMap<String>(param1Int);
      this.capacity = param1Int;
      this.unloadOnRemove = true;
    }
    
    public SoundPlayer.SoundInfo getFile(File param1File) {
      synchronized (this.lock) {
        return SoundPlayer.addRef(this.keyMap.get(param1File.getAbsoluteFile()));
      } 
    }
    
    public SoundPlayer.SoundInfo getHash(String param1String) {
      synchronized (this.lock) {
        return SoundPlayer.addRef(this.hashMap.get(param1String));
      } 
    }
    
    public SoundPlayer.SoundInfo getResource(int param1Int) {
      synchronized (this.lock) {
        return SoundPlayer.addRef(this.keyMap.get(Integer.valueOf(param1Int)));
      } 
    }
    
    public void noteSoundUsage(SoundPlayer.SoundInfo param1SoundInfo) {
      synchronized (this.lock) {
        this.unloadOnRemove = false;
        try {
          Object object = param1SoundInfo.getKey();
          this.keyMap.remove(object);
          this.keyMap.put(object, param1SoundInfo);
          this.hashMap.remove(param1SoundInfo.hashString);
          this.hashMap.put(param1SoundInfo.hashString, param1SoundInfo);
          return;
        } finally {
          this.unloadOnRemove = true;
        } 
      } 
    }
    
    public void putFile(File param1File, SoundPlayer.SoundInfo param1SoundInfo) {
      synchronized (this.lock) {
        this.keyMap.put(param1File.getAbsoluteFile(), SoundPlayer.addRef(param1SoundInfo));
        this.hashMap.put(param1SoundInfo.hashString, SoundPlayer.addRef(param1SoundInfo));
        return;
      } 
    }
    
    public void putResource(int param1Int, SoundPlayer.SoundInfo param1SoundInfo) {
      synchronized (this.lock) {
        this.keyMap.put(Integer.valueOf(param1Int), SoundPlayer.addRef(param1SoundInfo));
        this.hashMap.put(param1SoundInfo.hashString, SoundPlayer.addRef(param1SoundInfo));
        return;
      } 
    }
    
    class SoundInfoMap<K> extends LinkedHashMap<K, SoundPlayer.SoundInfo> {
      private static final float loadFactor = 0.75F;
      
      public SoundInfoMap(int param2Int) {
        super((int)Math.ceil((param2Int / 0.75F)) + 1, 0.75F, true);
      }
      
      public SoundPlayer.SoundInfo remove(Object param2Object) {
        param2Object = super.remove(param2Object);
        if (SoundPlayer.LoadedSoundCache.this.unloadOnRemove && param2Object != null)
          SoundPlayer.releaseRef((SoundPlayer.SoundInfo)param2Object); 
        return (SoundPlayer.SoundInfo)param2Object;
      }
      
      protected boolean removeEldestEntry(Map.Entry<K, SoundPlayer.SoundInfo> param2Entry) {
        return (size() > SoundPlayer.LoadedSoundCache.this.capacity);
      }
    }
  }
  
  class SoundInfoMap<K> extends LinkedHashMap<K, SoundInfo> {
    private static final float loadFactor = 0.75F;
    
    public SoundInfoMap(int param1Int) {
      super((int)Math.ceil((param1Int / 0.75F)) + 1, 0.75F, true);
    }
    
    public SoundPlayer.SoundInfo remove(Object param1Object) {
      param1Object = super.remove(param1Object);
      if (this.this$1.unloadOnRemove && param1Object != null)
        SoundPlayer.releaseRef((SoundPlayer.SoundInfo)param1Object); 
      return (SoundPlayer.SoundInfo)param1Object;
    }
    
    protected boolean removeEldestEntry(Map.Entry<K, SoundPlayer.SoundInfo> param1Entry) {
      return (size() > this.this$1.capacity);
    }
  }
  
  public static class PlaySoundParams {
    public int loopControl = 0;
    
    public float rate = 1.0F;
    
    public float volume = 1.0F;
    
    public boolean waitForNonLoopingSoundsToFinish = true;
    
    public PlaySoundParams() {}
    
    public PlaySoundParams(PlaySoundParams param1PlaySoundParams) {
      this.volume = param1PlaySoundParams.volume;
      this.waitForNonLoopingSoundsToFinish = param1PlaySoundParams.waitForNonLoopingSoundsToFinish;
      this.loopControl = param1PlaySoundParams.loopControl;
      this.rate = param1PlaySoundParams.rate;
    }
    
    public PlaySoundParams(boolean param1Boolean) {
      this.waitForNonLoopingSoundsToFinish = param1Boolean;
    }
    
    public boolean isLooping() {
      return (this.loopControl == -1);
    }
  }
  
  protected static interface SoundFromFile {
    SoundPlayer.SoundInfo apply(File param1File);
  }
  
  protected class SoundInfo extends RefCounted {
    public int cbSize;
    
    public final Context context;
    
    public final File file;
    
    public String hashString;
    
    public final long msDuration;
    
    public long msLastPlay = 0L;
    
    public final int resourceId;
    
    public int sampleId;
    
    public SoundInfo(Context param1Context, int param1Int1, int param1Int2) {
      this.context = param1Context;
      this.resourceId = param1Int1;
      this.file = null;
      this.msDuration = param1Int2;
      this.hashString = computeHash();
    }
    
    public SoundInfo(File param1File, int param1Int) {
      this.context = null;
      this.resourceId = 0;
      this.file = param1File;
      this.msDuration = param1Int;
      this.hashString = computeHash();
    }
    
    protected String computeHash() {
      InputStream inputStream = getInputStream();
      if (inputStream != null) {
        Exception exception;
        try {
          MessageDigest messageDigest = MessageDigest.getInstance("MD5");
          byte[] arrayOfByte = new byte[256];
          this.cbSize = 0;
          while (true) {
            String str;
            StringBuilder stringBuilder;
            int i = inputStream.read(arrayOfByte);
            if (i < 0) {
              byte[] arrayOfByte1 = messageDigest.digest();
              stringBuilder = new StringBuilder();
              for (i = 0; i < arrayOfByte1.length; i++) {
                stringBuilder.append(String.format(Locale.ROOT, "%02x", new Object[] { Byte.valueOf(arrayOfByte1[i]) }));
              } 
              str = stringBuilder.toString();
              SoundPlayer.this.safeClose(inputStream);
              return str;
            } 
            this.cbSize += i;
            str.update((byte[])stringBuilder, 0, i);
          } 
        } catch (NoSuchAlgorithmException null) {
        
        } catch (IOException null) {
        
        } finally {}
        SoundPlayer.this.tracer.traceError(exception, "exception computing hash", new Object[0]);
        SoundPlayer.this.safeClose(inputStream);
      } 
      throw Misc.illegalStateException("internal error: unable to compute hash of %s", new Object[] { this });
    }
    
    protected void destructor() {
      SoundPlayer.this.tracer.trace("unloading sound %s", new Object[] { this });
      SoundPlayer.this.soundPool.unload(this.sampleId);
      super.destructor();
    }
    
    public InputStream getInputStream() {
      try {
        return (this.resourceId != 0) ? this.context.getResources().openRawResource(this.resourceId) : new FileInputStream(this.file);
      } catch (IOException iOException) {
        return null;
      } 
    }
    
    public Object getKey() {
      int i = this.resourceId;
      return (i == 0) ? this.file : Integer.valueOf(i);
    }
    
    public void initialize(int param1Int) {
      this.sampleId = param1Int;
      this.hashString = computeHash();
    }
    
    public String toString() {
      return Misc.formatInvariant("samp=%d|ms=%d", new Object[] { Integer.valueOf(this.sampleId), Long.valueOf(this.msDuration) });
    }
  }
  
  protected enum StopWhat {
    All, Loops;
    
    static {
      StopWhat stopWhat = new StopWhat("Loops", 1);
      Loops = stopWhat;
      $VALUES = new StopWhat[] { All, stopWhat };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\SoundPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
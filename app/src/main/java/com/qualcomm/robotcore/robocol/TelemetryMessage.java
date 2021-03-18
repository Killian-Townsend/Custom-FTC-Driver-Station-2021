package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TelemetryMessage extends RobocolParsableBase {
  private static final Charset CHARSET = Charset.forName("UTF-8");
  
  public static final String DEFAULT_TAG = "TELEMETRY_DATA";
  
  public static final int cCountMax = 255;
  
  static final int cbCountLen = 1;
  
  static final int cbFloat = 4;
  
  static final int cbKeyLen = 2;
  
  public static final int cbKeyMax = 65535;
  
  static final int cbRobotState = 1;
  
  static final int cbSorted = 1;
  
  static final int cbTagLen = 1;
  
  public static final int cbTagMax = 255;
  
  static final int cbTimestamp = 8;
  
  static final int cbValueLen = 2;
  
  public static final int cbValueMax = 65535;
  
  private final Map<String, Float> dataNumbers = new LinkedHashMap<String, Float>();
  
  private final Map<String, String> dataStrings = new LinkedHashMap<String, String>();
  
  private boolean isSorted = true;
  
  private RobotState robotState = RobotState.UNKNOWN;
  
  private String tag = "";
  
  private long timestamp = 0L;
  
  public TelemetryMessage() {}
  
  public TelemetryMessage(byte[] paramArrayOfbyte) throws RobotCoreException {
    fromByteArray(paramArrayOfbyte);
  }
  
  private int countMessageBytes() {
    int i = (this.tag.getBytes(CHARSET)).length + 1 + 10 + 1;
    for (Map.Entry<String, String> entry : this.dataStrings.entrySet())
      i = i + (((String)entry.getKey()).getBytes(CHARSET)).length + 2 + (((String)entry.getValue()).getBytes(CHARSET)).length + 2; 
    i++;
    Iterator iterator = this.dataNumbers.entrySet().iterator();
    while (iterator.hasNext())
      i = i + (((String)((Map.Entry)iterator.next()).getKey()).getBytes(CHARSET)).length + 2 + 4; 
    return i;
  }
  
  static int getCount(ByteBuffer paramByteBuffer) {
    return TypeConversion.unsignedByteToInt(paramByteBuffer.get());
  }
  
  static int getKeyLen(ByteBuffer paramByteBuffer) {
    return TypeConversion.unsignedShortToInt(paramByteBuffer.getShort());
  }
  
  static int getTagLen(ByteBuffer paramByteBuffer) {
    return TypeConversion.unsignedByteToInt(paramByteBuffer.get());
  }
  
  static int getValueLen(ByteBuffer paramByteBuffer) {
    return getKeyLen(paramByteBuffer);
  }
  
  static void putCount(ByteBuffer paramByteBuffer, int paramInt) {
    paramByteBuffer.put((byte)paramInt);
  }
  
  static void putKeyLen(ByteBuffer paramByteBuffer, int paramInt) {
    paramByteBuffer.putShort((short)paramInt);
  }
  
  static void putTagLen(ByteBuffer paramByteBuffer, int paramInt) {
    paramByteBuffer.put((byte)paramInt);
  }
  
  static void putValueLen(ByteBuffer paramByteBuffer, int paramInt) {
    putKeyLen(paramByteBuffer, paramInt);
  }
  
  public void addData(String paramString, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield dataNumbers : Ljava/util/Map;
    //   6: aload_1
    //   7: dload_2
    //   8: d2f
    //   9: invokestatic valueOf : (F)Ljava/lang/Float;
    //   12: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   17: pop
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  public void addData(String paramString, float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield dataNumbers : Ljava/util/Map;
    //   6: aload_1
    //   7: fload_2
    //   8: invokestatic valueOf : (F)Ljava/lang/Float;
    //   11: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   16: pop
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public void addData(String paramString, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield dataStrings : Ljava/util/Map;
    //   6: aload_1
    //   7: aload_2
    //   8: invokevirtual toString : ()Ljava/lang/String;
    //   11: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   16: pop
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public void addData(String paramString1, String paramString2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield dataStrings : Ljava/util/Map;
    //   6: aload_1
    //   7: aload_2
    //   8: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	17	finally
  }
  
  public void clearData() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lconst_0
    //   4: putfield timestamp : J
    //   7: aload_0
    //   8: getfield dataStrings : Ljava/util/Map;
    //   11: invokeinterface clear : ()V
    //   16: aload_0
    //   17: getfield dataNumbers : Ljava/util/Map;
    //   20: invokeinterface clear : ()V
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	28	finally
  }
  
  public void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual clearData : ()V
    //   6: aload_0
    //   7: aload_1
    //   8: invokevirtual getReadBuffer : ([B)Ljava/nio/ByteBuffer;
    //   11: astore_1
    //   12: aload_0
    //   13: aload_1
    //   14: invokevirtual getLong : ()J
    //   17: putfield timestamp : J
    //   20: aload_1
    //   21: invokevirtual get : ()B
    //   24: istore_3
    //   25: iconst_0
    //   26: istore #4
    //   28: iload_3
    //   29: ifeq -> 274
    //   32: iconst_1
    //   33: istore #6
    //   35: goto -> 38
    //   38: aload_0
    //   39: iload #6
    //   41: putfield isSorted : Z
    //   44: aload_0
    //   45: aload_1
    //   46: invokevirtual get : ()B
    //   49: invokestatic fromByte : (I)Lcom/qualcomm/robotcore/robot/RobotState;
    //   52: putfield robotState : Lcom/qualcomm/robotcore/robot/RobotState;
    //   55: aload_1
    //   56: invokestatic getTagLen : (Ljava/nio/ByteBuffer;)I
    //   59: istore_3
    //   60: iload_3
    //   61: ifne -> 73
    //   64: aload_0
    //   65: ldc ''
    //   67: putfield tag : Ljava/lang/String;
    //   70: goto -> 101
    //   73: iload_3
    //   74: newarray byte
    //   76: astore #7
    //   78: aload_1
    //   79: aload #7
    //   81: invokevirtual get : ([B)Ljava/nio/ByteBuffer;
    //   84: pop
    //   85: aload_0
    //   86: new java/lang/String
    //   89: dup
    //   90: aload #7
    //   92: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   95: invokespecial <init> : ([BLjava/nio/charset/Charset;)V
    //   98: putfield tag : Ljava/lang/String;
    //   101: aload_1
    //   102: invokestatic getCount : (Ljava/nio/ByteBuffer;)I
    //   105: istore #5
    //   107: iconst_0
    //   108: istore_3
    //   109: iload_3
    //   110: iload #5
    //   112: if_icmpge -> 194
    //   115: aload_1
    //   116: invokestatic getKeyLen : (Ljava/nio/ByteBuffer;)I
    //   119: newarray byte
    //   121: astore #8
    //   123: aload_1
    //   124: aload #8
    //   126: invokevirtual get : ([B)Ljava/nio/ByteBuffer;
    //   129: pop
    //   130: aload_1
    //   131: invokestatic getValueLen : (Ljava/nio/ByteBuffer;)I
    //   134: newarray byte
    //   136: astore #7
    //   138: aload_1
    //   139: aload #7
    //   141: invokevirtual get : ([B)Ljava/nio/ByteBuffer;
    //   144: pop
    //   145: new java/lang/String
    //   148: dup
    //   149: aload #8
    //   151: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   154: invokespecial <init> : ([BLjava/nio/charset/Charset;)V
    //   157: astore #8
    //   159: new java/lang/String
    //   162: dup
    //   163: aload #7
    //   165: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   168: invokespecial <init> : ([BLjava/nio/charset/Charset;)V
    //   171: astore #7
    //   173: aload_0
    //   174: getfield dataStrings : Ljava/util/Map;
    //   177: aload #8
    //   179: aload #7
    //   181: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   186: pop
    //   187: iload_3
    //   188: iconst_1
    //   189: iadd
    //   190: istore_3
    //   191: goto -> 109
    //   194: aload_1
    //   195: invokestatic getCount : (Ljava/nio/ByteBuffer;)I
    //   198: istore #5
    //   200: iload #4
    //   202: istore_3
    //   203: iload_3
    //   204: iload #5
    //   206: if_icmpge -> 266
    //   209: aload_1
    //   210: invokestatic getKeyLen : (Ljava/nio/ByteBuffer;)I
    //   213: newarray byte
    //   215: astore #7
    //   217: aload_1
    //   218: aload #7
    //   220: invokevirtual get : ([B)Ljava/nio/ByteBuffer;
    //   223: pop
    //   224: new java/lang/String
    //   227: dup
    //   228: aload #7
    //   230: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   233: invokespecial <init> : ([BLjava/nio/charset/Charset;)V
    //   236: astore #7
    //   238: aload_1
    //   239: invokevirtual getFloat : ()F
    //   242: fstore_2
    //   243: aload_0
    //   244: getfield dataNumbers : Ljava/util/Map;
    //   247: aload #7
    //   249: fload_2
    //   250: invokestatic valueOf : (F)Ljava/lang/Float;
    //   253: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   258: pop
    //   259: iload_3
    //   260: iconst_1
    //   261: iadd
    //   262: istore_3
    //   263: goto -> 203
    //   266: aload_0
    //   267: monitorexit
    //   268: return
    //   269: astore_1
    //   270: aload_0
    //   271: monitorexit
    //   272: aload_1
    //   273: athrow
    //   274: iconst_0
    //   275: istore #6
    //   277: goto -> 38
    // Exception table:
    //   from	to	target	type
    //   2	25	269	finally
    //   38	60	269	finally
    //   64	70	269	finally
    //   73	101	269	finally
    //   101	107	269	finally
    //   115	187	269	finally
    //   194	200	269	finally
    //   209	259	269	finally
  }
  
  public Map<String, Float> getDataNumbers() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield dataNumbers : Ljava/util/Map;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public Map<String, String> getDataStrings() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield dataStrings : Ljava/util/Map;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public RobocolParsable.MsgType getRobocolMsgType() {
    return RobocolParsable.MsgType.TELEMETRY;
  }
  
  public RobotState getRobotState() {
    return this.robotState;
  }
  
  public String getTag() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield tag : Ljava/lang/String;
    //   6: invokevirtual length : ()I
    //   9: ifne -> 17
    //   12: aload_0
    //   13: monitorexit
    //   14: ldc 'TELEMETRY_DATA'
    //   16: areturn
    //   17: aload_0
    //   18: getfield tag : Ljava/lang/String;
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: areturn
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	26	finally
    //   17	22	26	finally
  }
  
  public long getTimestamp() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield timestamp : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean hasData() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield dataStrings : Ljava/util/Map;
    //   6: invokeinterface isEmpty : ()Z
    //   11: ifeq -> 36
    //   14: aload_0
    //   15: getfield dataNumbers : Ljava/util/Map;
    //   18: invokeinterface isEmpty : ()Z
    //   23: istore_1
    //   24: iload_1
    //   25: ifne -> 31
    //   28: goto -> 36
    //   31: iconst_0
    //   32: istore_1
    //   33: goto -> 38
    //   36: iconst_1
    //   37: istore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: iload_1
    //   41: ireturn
    //   42: astore_2
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_2
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	42	finally
  }
  
  public boolean isSorted() {
    return this.isSorted;
  }
  
  public void setRobotState(RobotState paramRobotState) {
    this.robotState = paramRobotState;
  }
  
  public void setSorted(boolean paramBoolean) {
    this.isSorted = paramBoolean;
  }
  
  public void setTag(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield tag : Ljava/lang/String;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public byte[] toByteArray() throws RobotCoreException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokestatic currentTimeMillis : ()J
    //   6: putfield timestamp : J
    //   9: aload_0
    //   10: getfield dataStrings : Ljava/util/Map;
    //   13: invokeinterface size : ()I
    //   18: sipush #255
    //   21: if_icmpgt -> 582
    //   24: aload_0
    //   25: getfield dataNumbers : Ljava/util/Map;
    //   28: invokeinterface size : ()I
    //   33: sipush #255
    //   36: if_icmpgt -> 558
    //   39: aload_0
    //   40: aload_0
    //   41: invokespecial countMessageBytes : ()I
    //   44: invokevirtual getWriteBuffer : (I)Ljava/nio/ByteBuffer;
    //   47: astore_3
    //   48: aload_3
    //   49: aload_0
    //   50: getfield timestamp : J
    //   53: invokevirtual putLong : (J)Ljava/nio/ByteBuffer;
    //   56: pop
    //   57: aload_0
    //   58: getfield isSorted : Z
    //   61: ifeq -> 611
    //   64: iconst_1
    //   65: istore_2
    //   66: goto -> 69
    //   69: aload_3
    //   70: iload_2
    //   71: i2b
    //   72: invokevirtual put : (B)Ljava/nio/ByteBuffer;
    //   75: pop
    //   76: aload_3
    //   77: aload_0
    //   78: getfield robotState : Lcom/qualcomm/robotcore/robot/RobotState;
    //   81: invokevirtual asByte : ()B
    //   84: invokevirtual put : (B)Ljava/nio/ByteBuffer;
    //   87: pop
    //   88: aload_0
    //   89: getfield tag : Ljava/lang/String;
    //   92: invokevirtual length : ()I
    //   95: ifne -> 106
    //   98: aload_3
    //   99: iconst_0
    //   100: invokestatic putTagLen : (Ljava/nio/ByteBuffer;I)V
    //   103: goto -> 141
    //   106: aload_0
    //   107: getfield tag : Ljava/lang/String;
    //   110: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   113: invokevirtual getBytes : (Ljava/nio/charset/Charset;)[B
    //   116: astore #4
    //   118: aload #4
    //   120: arraylength
    //   121: sipush #255
    //   124: if_icmpgt -> 524
    //   127: aload_3
    //   128: aload #4
    //   130: arraylength
    //   131: invokestatic putTagLen : (Ljava/nio/ByteBuffer;I)V
    //   134: aload_3
    //   135: aload #4
    //   137: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   140: pop
    //   141: aload_3
    //   142: aload_0
    //   143: getfield dataStrings : Ljava/util/Map;
    //   146: invokeinterface size : ()I
    //   151: invokestatic putCount : (Ljava/nio/ByteBuffer;I)V
    //   154: aload_0
    //   155: getfield dataStrings : Ljava/util/Map;
    //   158: invokeinterface entrySet : ()Ljava/util/Set;
    //   163: invokeinterface iterator : ()Ljava/util/Iterator;
    //   168: astore #4
    //   170: aload #4
    //   172: invokeinterface hasNext : ()Z
    //   177: ifeq -> 359
    //   180: aload #4
    //   182: invokeinterface next : ()Ljava/lang/Object;
    //   187: checkcast java/util/Map$Entry
    //   190: astore #5
    //   192: aload #5
    //   194: invokeinterface getKey : ()Ljava/lang/Object;
    //   199: checkcast java/lang/String
    //   202: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   205: invokevirtual getBytes : (Ljava/nio/charset/Charset;)[B
    //   208: astore #6
    //   210: aload #5
    //   212: invokeinterface getValue : ()Ljava/lang/Object;
    //   217: checkcast java/lang/String
    //   220: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   223: invokevirtual getBytes : (Ljava/nio/charset/Charset;)[B
    //   226: astore #7
    //   228: aload #6
    //   230: arraylength
    //   231: ldc 65535
    //   233: if_icmpgt -> 317
    //   236: aload #7
    //   238: arraylength
    //   239: ldc 65535
    //   241: if_icmpgt -> 275
    //   244: aload_3
    //   245: aload #6
    //   247: arraylength
    //   248: invokestatic putKeyLen : (Ljava/nio/ByteBuffer;I)V
    //   251: aload_3
    //   252: aload #6
    //   254: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   257: pop
    //   258: aload_3
    //   259: aload #7
    //   261: arraylength
    //   262: invokestatic putValueLen : (Ljava/nio/ByteBuffer;I)V
    //   265: aload_3
    //   266: aload #7
    //   268: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   271: pop
    //   272: goto -> 170
    //   275: new com/qualcomm/robotcore/exception/RobotCoreException
    //   278: dup
    //   279: ldc_w 'telemetry value '%s' too long: %d bytes; max %d bytes'
    //   282: iconst_3
    //   283: anewarray java/lang/Object
    //   286: dup
    //   287: iconst_0
    //   288: aload #5
    //   290: invokeinterface getValue : ()Ljava/lang/Object;
    //   295: aastore
    //   296: dup
    //   297: iconst_1
    //   298: aload #7
    //   300: arraylength
    //   301: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   304: aastore
    //   305: dup
    //   306: iconst_2
    //   307: ldc 65535
    //   309: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   312: aastore
    //   313: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   316: athrow
    //   317: new com/qualcomm/robotcore/exception/RobotCoreException
    //   320: dup
    //   321: ldc_w 'telemetry key '%s' too long: %d bytes; max %d bytes'
    //   324: iconst_3
    //   325: anewarray java/lang/Object
    //   328: dup
    //   329: iconst_0
    //   330: aload #5
    //   332: invokeinterface getKey : ()Ljava/lang/Object;
    //   337: aastore
    //   338: dup
    //   339: iconst_1
    //   340: aload #6
    //   342: arraylength
    //   343: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   346: aastore
    //   347: dup
    //   348: iconst_2
    //   349: ldc 65535
    //   351: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   354: aastore
    //   355: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   358: athrow
    //   359: aload_3
    //   360: aload_0
    //   361: getfield dataNumbers : Ljava/util/Map;
    //   364: invokeinterface size : ()I
    //   369: invokestatic putCount : (Ljava/nio/ByteBuffer;I)V
    //   372: aload_0
    //   373: getfield dataNumbers : Ljava/util/Map;
    //   376: invokeinterface entrySet : ()Ljava/util/Set;
    //   381: invokeinterface iterator : ()Ljava/util/Iterator;
    //   386: astore #4
    //   388: aload #4
    //   390: invokeinterface hasNext : ()Z
    //   395: ifeq -> 515
    //   398: aload #4
    //   400: invokeinterface next : ()Ljava/lang/Object;
    //   405: checkcast java/util/Map$Entry
    //   408: astore #5
    //   410: aload #5
    //   412: invokeinterface getKey : ()Ljava/lang/Object;
    //   417: checkcast java/lang/String
    //   420: getstatic com/qualcomm/robotcore/robocol/TelemetryMessage.CHARSET : Ljava/nio/charset/Charset;
    //   423: invokevirtual getBytes : (Ljava/nio/charset/Charset;)[B
    //   426: astore #6
    //   428: aload #5
    //   430: invokeinterface getValue : ()Ljava/lang/Object;
    //   435: checkcast java/lang/Float
    //   438: invokevirtual floatValue : ()F
    //   441: fstore_1
    //   442: aload #6
    //   444: arraylength
    //   445: ldc 65535
    //   447: if_icmpgt -> 473
    //   450: aload_3
    //   451: aload #6
    //   453: arraylength
    //   454: invokestatic putKeyLen : (Ljava/nio/ByteBuffer;I)V
    //   457: aload_3
    //   458: aload #6
    //   460: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   463: pop
    //   464: aload_3
    //   465: fload_1
    //   466: invokevirtual putFloat : (F)Ljava/nio/ByteBuffer;
    //   469: pop
    //   470: goto -> 388
    //   473: new com/qualcomm/robotcore/exception/RobotCoreException
    //   476: dup
    //   477: ldc_w 'telemetry key '%s' too long: %d bytes; max %d bytes'
    //   480: iconst_3
    //   481: anewarray java/lang/Object
    //   484: dup
    //   485: iconst_0
    //   486: aload #5
    //   488: invokeinterface getKey : ()Ljava/lang/Object;
    //   493: aastore
    //   494: dup
    //   495: iconst_1
    //   496: aload #6
    //   498: arraylength
    //   499: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   502: aastore
    //   503: dup
    //   504: iconst_2
    //   505: ldc 65535
    //   507: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   510: aastore
    //   511: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   514: athrow
    //   515: aload_3
    //   516: invokevirtual array : ()[B
    //   519: astore_3
    //   520: aload_0
    //   521: monitorexit
    //   522: aload_3
    //   523: areturn
    //   524: new com/qualcomm/robotcore/exception/RobotCoreException
    //   527: dup
    //   528: ldc_w 'Telemetry tag cannot exceed %d bytes [%s]'
    //   531: iconst_2
    //   532: anewarray java/lang/Object
    //   535: dup
    //   536: iconst_0
    //   537: sipush #255
    //   540: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   543: aastore
    //   544: dup
    //   545: iconst_1
    //   546: aload_0
    //   547: getfield tag : Ljava/lang/String;
    //   550: aastore
    //   551: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   554: invokespecial <init> : (Ljava/lang/String;)V
    //   557: athrow
    //   558: new com/qualcomm/robotcore/exception/RobotCoreException
    //   561: dup
    //   562: ldc_w 'Cannot have more than %d number data points'
    //   565: iconst_1
    //   566: anewarray java/lang/Object
    //   569: dup
    //   570: iconst_0
    //   571: sipush #255
    //   574: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   577: aastore
    //   578: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   581: athrow
    //   582: new com/qualcomm/robotcore/exception/RobotCoreException
    //   585: dup
    //   586: ldc_w 'Cannot have more than %d string data points'
    //   589: iconst_1
    //   590: anewarray java/lang/Object
    //   593: dup
    //   594: iconst_0
    //   595: sipush #255
    //   598: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   601: aastore
    //   602: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   605: athrow
    //   606: astore_3
    //   607: aload_0
    //   608: monitorexit
    //   609: aload_3
    //   610: athrow
    //   611: iconst_0
    //   612: istore_2
    //   613: goto -> 69
    // Exception table:
    //   from	to	target	type
    //   2	64	606	finally
    //   69	103	606	finally
    //   106	141	606	finally
    //   141	170	606	finally
    //   170	272	606	finally
    //   275	317	606	finally
    //   317	359	606	finally
    //   359	388	606	finally
    //   388	470	606	finally
    //   473	515	606	finally
    //   515	520	606	finally
    //   524	558	606	finally
    //   558	582	606	finally
    //   582	606	606	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\TelemetryMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
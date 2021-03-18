package com.qualcomm.hardware.matrix;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MatrixMasterController implements I2cController.I2cPortReadyCallback {
  private static final byte BATTERY_OFFSET = 67;
  
  private static final int DEFAULT_TIMEOUT = 3;
  
  private static final byte MATRIX_CONTROLLER_I2C_ADDR = 16;
  
  private static final double MIN_TRANSACTION_RATE = 2.0D;
  
  private static final byte SERVO_ENABLE_OFFSET = 69;
  
  private static final byte START_FLAG_OFFSET = 68;
  
  private static final byte TIMEOUT_OFFSET = 66;
  
  private static final byte WASTED_BYTE = 0;
  
  private static final boolean debug = false;
  
  private static final byte[] motorModeOffset;
  
  private static final byte[] motorPositionOffset;
  
  private static final byte[] motorSpeedOffset;
  
  private static final byte[] motorTargetOffset;
  
  private static final byte[] servoSpeedOffset = new byte[] { 0, 70, 72, 74, 76 };
  
  private final ElapsedTime lastTransaction = new ElapsedTime(0L);
  
  protected ModernRoboticsUsbLegacyModule legacyModule;
  
  protected MatrixDcMotorController motorController;
  
  protected int physicalPort;
  
  protected MatrixServoController servoController;
  
  protected ConcurrentLinkedQueue<MatrixI2cTransaction> transactionQueue;
  
  private volatile boolean waitingForGodot = false;
  
  static {
    motorPositionOffset = new byte[] { 0, 78, 88, 98, 108 };
    motorTargetOffset = new byte[] { 0, 82, 92, 102, 112 };
    motorSpeedOffset = new byte[] { 0, 86, 96, 106, 116 };
    motorModeOffset = new byte[] { 0, 87, 97, 107, 117 };
  }
  
  public MatrixMasterController(ModernRoboticsUsbLegacyModule paramModernRoboticsUsbLegacyModule, int paramInt) {
    this.legacyModule = paramModernRoboticsUsbLegacyModule;
    this.physicalPort = paramInt;
    this.transactionQueue = new ConcurrentLinkedQueue<MatrixI2cTransaction>();
    paramModernRoboticsUsbLegacyModule.registerForI2cPortReadyCallback(this, paramInt);
  }
  
  protected void buginf(String paramString) {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.legacyModule.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public int getPort() {
    return this.physicalPort;
  }
  
  protected void handleReadDone(MatrixI2cTransaction paramMatrixI2cTransaction) {
    // Byte code:
    //   0: aload_0
    //   1: getfield legacyModule : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbLegacyModule;
    //   4: aload_0
    //   5: getfield physicalPort : I
    //   8: invokevirtual getI2cReadCache : (I)[B
    //   11: astore_3
    //   12: getstatic com/qualcomm/hardware/matrix/MatrixMasterController$1.$SwitchMap$com$qualcomm$hardware$matrix$MatrixI2cTransaction$I2cTransactionProperty : [I
    //   15: aload_1
    //   16: getfield property : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionProperty;
    //   19: invokevirtual ordinal : ()I
    //   22: iaload
    //   23: istore_2
    //   24: iload_2
    //   25: iconst_1
    //   26: if_icmpeq -> 131
    //   29: iload_2
    //   30: iconst_2
    //   31: if_icmpeq -> 119
    //   34: iload_2
    //   35: iconst_3
    //   36: if_icmpeq -> 107
    //   39: iload_2
    //   40: iconst_4
    //   41: if_icmpeq -> 95
    //   44: iload_2
    //   45: iconst_5
    //   46: if_icmpeq -> 83
    //   49: new java/lang/StringBuilder
    //   52: dup
    //   53: invokespecial <init> : ()V
    //   56: astore_3
    //   57: aload_3
    //   58: ldc 'Transaction not a read '
    //   60: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: aload_3
    //   65: aload_1
    //   66: getfield property : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionProperty;
    //   69: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_3
    //   74: invokevirtual toString : ()Ljava/lang/String;
    //   77: invokestatic e : (Ljava/lang/String;)V
    //   80: goto -> 139
    //   83: aload_0
    //   84: getfield servoController : Lcom/qualcomm/hardware/matrix/MatrixServoController;
    //   87: aload_1
    //   88: aload_3
    //   89: invokevirtual handleReadServo : (Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction;[B)V
    //   92: goto -> 139
    //   95: aload_0
    //   96: getfield motorController : Lcom/qualcomm/hardware/matrix/MatrixDcMotorController;
    //   99: aload_1
    //   100: aload_3
    //   101: invokevirtual handleReadMode : (Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction;[B)V
    //   104: goto -> 139
    //   107: aload_0
    //   108: getfield motorController : Lcom/qualcomm/hardware/matrix/MatrixDcMotorController;
    //   111: aload_1
    //   112: aload_3
    //   113: invokevirtual handleReadPosition : (Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction;[B)V
    //   116: goto -> 139
    //   119: aload_0
    //   120: getfield motorController : Lcom/qualcomm/hardware/matrix/MatrixDcMotorController;
    //   123: aload_1
    //   124: aload_3
    //   125: invokevirtual handleReadPosition : (Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction;[B)V
    //   128: goto -> 139
    //   131: aload_0
    //   132: getfield motorController : Lcom/qualcomm/hardware/matrix/MatrixDcMotorController;
    //   135: aload_3
    //   136: invokevirtual handleReadBattery : ([B)V
    //   139: aload_0
    //   140: monitorenter
    //   141: aload_0
    //   142: getfield waitingForGodot : Z
    //   145: ifeq -> 157
    //   148: aload_0
    //   149: iconst_0
    //   150: putfield waitingForGodot : Z
    //   153: aload_0
    //   154: invokevirtual notify : ()V
    //   157: aload_0
    //   158: monitorexit
    //   159: return
    //   160: astore_1
    //   161: aload_0
    //   162: monitorexit
    //   163: aload_1
    //   164: athrow
    // Exception table:
    //   from	to	target	type
    //   141	157	160	finally
    //   157	159	160	finally
    //   161	163	160	finally
  }
  
  public void portIsReady(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   4: invokevirtual isEmpty : ()Z
    //   7: ifeq -> 36
    //   10: aload_0
    //   11: getfield lastTransaction : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   14: invokevirtual time : ()D
    //   17: ldc2_w 2.0
    //   20: dcmpl
    //   21: ifle -> 35
    //   24: aload_0
    //   25: invokevirtual sendHeartbeat : ()V
    //   28: aload_0
    //   29: getfield lastTransaction : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   32: invokevirtual reset : ()V
    //   35: return
    //   36: aload_0
    //   37: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   40: invokevirtual peek : ()Ljava/lang/Object;
    //   43: checkcast com/qualcomm/hardware/matrix/MatrixI2cTransaction
    //   46: astore #6
    //   48: aload #6
    //   50: getfield state : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   53: getstatic com/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState.PENDING_I2C_READ : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   56: if_acmpne -> 79
    //   59: aload_0
    //   60: getfield legacyModule : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbLegacyModule;
    //   63: aload_0
    //   64: getfield physicalPort : I
    //   67: invokevirtual readI2cCacheFromModule : (I)V
    //   70: aload #6
    //   72: getstatic com/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState.PENDING_READ_DONE : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   75: putfield state : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   78: return
    //   79: aload #6
    //   81: getfield state : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   84: getstatic com/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState.PENDING_I2C_WRITE : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   87: if_acmpne -> 128
    //   90: aload_0
    //   91: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   94: invokevirtual poll : ()Ljava/lang/Object;
    //   97: checkcast com/qualcomm/hardware/matrix/MatrixI2cTransaction
    //   100: astore #6
    //   102: aload_0
    //   103: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   106: invokevirtual isEmpty : ()Z
    //   109: ifeq -> 113
    //   112: return
    //   113: aload_0
    //   114: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   117: invokevirtual peek : ()Ljava/lang/Object;
    //   120: checkcast com/qualcomm/hardware/matrix/MatrixI2cTransaction
    //   123: astore #7
    //   125: goto -> 184
    //   128: aload #6
    //   130: astore #7
    //   132: aload #6
    //   134: getfield state : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   137: getstatic com/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState.PENDING_READ_DONE : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   140: if_acmpne -> 184
    //   143: aload_0
    //   144: aload #6
    //   146: invokevirtual handleReadDone : (Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction;)V
    //   149: aload_0
    //   150: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   153: invokevirtual poll : ()Ljava/lang/Object;
    //   156: checkcast com/qualcomm/hardware/matrix/MatrixI2cTransaction
    //   159: astore #6
    //   161: aload_0
    //   162: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   165: invokevirtual isEmpty : ()Z
    //   168: ifeq -> 172
    //   171: return
    //   172: aload_0
    //   173: getfield transactionQueue : Ljava/util/concurrent/ConcurrentLinkedQueue;
    //   176: invokevirtual peek : ()Ljava/lang/Object;
    //   179: checkcast com/qualcomm/hardware/matrix/MatrixI2cTransaction
    //   182: astore #7
    //   184: getstatic com/qualcomm/hardware/matrix/MatrixMasterController$1.$SwitchMap$com$qualcomm$hardware$matrix$MatrixI2cTransaction$I2cTransactionProperty : [I
    //   187: aload #7
    //   189: getfield property : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionProperty;
    //   192: invokevirtual ordinal : ()I
    //   195: iaload
    //   196: istore #5
    //   198: bipush #10
    //   200: istore_1
    //   201: iconst_0
    //   202: istore #4
    //   204: iload #5
    //   206: tableswitch default -> 260, 1 -> 563, 2 -> 537, 3 -> 513, 4 -> 485, 5 -> 443, 6 -> 422, 7 -> 401, 8 -> 373, 9 -> 299, 10 -> 278
    //   260: iconst_1
    //   261: newarray byte
    //   263: astore #6
    //   265: aload #6
    //   267: iconst_0
    //   268: aload #7
    //   270: getfield value : I
    //   273: i2b
    //   274: bastore
    //   275: goto -> 579
    //   278: bipush #69
    //   280: istore_1
    //   281: iconst_1
    //   282: newarray byte
    //   284: astore #6
    //   286: aload #6
    //   288: iconst_0
    //   289: aload #7
    //   291: getfield value : I
    //   294: i2b
    //   295: bastore
    //   296: goto -> 576
    //   299: getstatic com/qualcomm/hardware/matrix/MatrixMasterController.motorPositionOffset : [B
    //   302: aload #7
    //   304: getfield motor : B
    //   307: baload
    //   308: istore #4
    //   310: bipush #10
    //   312: invokestatic allocate : (I)Ljava/nio/ByteBuffer;
    //   315: astore #6
    //   317: aload #6
    //   319: iconst_0
    //   320: invokestatic intToByteArray : (I)[B
    //   323: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   326: pop
    //   327: aload #6
    //   329: aload #7
    //   331: getfield target : I
    //   334: invokestatic intToByteArray : (I)[B
    //   337: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   340: pop
    //   341: aload #6
    //   343: aload #7
    //   345: getfield speed : B
    //   348: invokevirtual put : (B)Ljava/nio/ByteBuffer;
    //   351: pop
    //   352: aload #6
    //   354: aload #7
    //   356: getfield mode : B
    //   359: invokevirtual put : (B)Ljava/nio/ByteBuffer;
    //   362: pop
    //   363: aload #6
    //   365: invokevirtual array : ()[B
    //   368: astore #6
    //   370: goto -> 581
    //   373: getstatic com/qualcomm/hardware/matrix/MatrixMasterController.motorSpeedOffset : [B
    //   376: aload #7
    //   378: getfield motor : B
    //   381: baload
    //   382: istore_1
    //   383: iconst_1
    //   384: newarray byte
    //   386: astore #6
    //   388: aload #6
    //   390: iconst_0
    //   391: aload #7
    //   393: getfield value : I
    //   396: i2b
    //   397: bastore
    //   398: goto -> 576
    //   401: bipush #68
    //   403: istore_1
    //   404: iconst_1
    //   405: newarray byte
    //   407: astore #6
    //   409: aload #6
    //   411: iconst_0
    //   412: aload #7
    //   414: getfield value : I
    //   417: i2b
    //   418: bastore
    //   419: goto -> 576
    //   422: bipush #66
    //   424: istore_1
    //   425: iconst_1
    //   426: newarray byte
    //   428: astore #6
    //   430: aload #6
    //   432: iconst_0
    //   433: aload #7
    //   435: getfield value : I
    //   438: i2b
    //   439: bastore
    //   440: goto -> 576
    //   443: getstatic com/qualcomm/hardware/matrix/MatrixMasterController.servoSpeedOffset : [B
    //   446: aload #7
    //   448: getfield servo : B
    //   451: baload
    //   452: istore #4
    //   454: aload #7
    //   456: getfield speed : B
    //   459: istore_2
    //   460: aload #7
    //   462: getfield target : I
    //   465: i2b
    //   466: istore_3
    //   467: iconst_2
    //   468: newarray byte
    //   470: dup
    //   471: iconst_0
    //   472: iload_2
    //   473: bastore
    //   474: dup
    //   475: iconst_1
    //   476: iload_3
    //   477: bastore
    //   478: astore #6
    //   480: iconst_2
    //   481: istore_1
    //   482: goto -> 581
    //   485: getstatic com/qualcomm/hardware/matrix/MatrixMasterController.motorModeOffset : [B
    //   488: aload #7
    //   490: getfield motor : B
    //   493: baload
    //   494: istore_1
    //   495: iconst_1
    //   496: newarray byte
    //   498: astore #6
    //   500: aload #6
    //   502: iconst_0
    //   503: aload #7
    //   505: getfield value : I
    //   508: i2b
    //   509: bastore
    //   510: goto -> 576
    //   513: getstatic com/qualcomm/hardware/matrix/MatrixMasterController.motorTargetOffset : [B
    //   516: aload #7
    //   518: getfield motor : B
    //   521: baload
    //   522: istore #4
    //   524: aload #7
    //   526: getfield value : I
    //   529: invokestatic intToByteArray : (I)[B
    //   532: astore #6
    //   534: goto -> 558
    //   537: getstatic com/qualcomm/hardware/matrix/MatrixMasterController.motorPositionOffset : [B
    //   540: aload #7
    //   542: getfield motor : B
    //   545: baload
    //   546: istore #4
    //   548: iconst_1
    //   549: newarray byte
    //   551: astore #6
    //   553: aload #6
    //   555: iconst_0
    //   556: iconst_0
    //   557: bastore
    //   558: iconst_4
    //   559: istore_1
    //   560: goto -> 581
    //   563: bipush #67
    //   565: istore_1
    //   566: iconst_1
    //   567: newarray byte
    //   569: astore #6
    //   571: aload #6
    //   573: iconst_0
    //   574: iconst_0
    //   575: bastore
    //   576: iload_1
    //   577: istore #4
    //   579: iconst_1
    //   580: istore_1
    //   581: aload #7
    //   583: getfield write : Z
    //   586: ifeq -> 629
    //   589: aload_0
    //   590: getfield legacyModule : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbLegacyModule;
    //   593: aload_0
    //   594: getfield physicalPort : I
    //   597: bipush #16
    //   599: iload #4
    //   601: invokevirtual setWriteMode : (III)V
    //   604: aload_0
    //   605: getfield legacyModule : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbLegacyModule;
    //   608: aload_0
    //   609: getfield physicalPort : I
    //   612: aload #6
    //   614: iload_1
    //   615: invokevirtual setData : (I[BI)V
    //   618: aload #7
    //   620: getstatic com/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState.PENDING_I2C_WRITE : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   623: putfield state : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   626: goto -> 653
    //   629: aload_0
    //   630: getfield legacyModule : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbLegacyModule;
    //   633: aload_0
    //   634: getfield physicalPort : I
    //   637: bipush #16
    //   639: iload #4
    //   641: iload_1
    //   642: invokevirtual setReadMode : (IIII)V
    //   645: aload #7
    //   647: getstatic com/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState.PENDING_I2C_READ : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   650: putfield state : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionState;
    //   653: aload_0
    //   654: getfield legacyModule : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbLegacyModule;
    //   657: aload_0
    //   658: getfield physicalPort : I
    //   661: invokevirtual setI2cPortActionFlag : (I)V
    //   664: aload_0
    //   665: getfield legacyModule : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbLegacyModule;
    //   668: aload_0
    //   669: getfield physicalPort : I
    //   672: invokevirtual writeI2cCacheToModule : (I)V
    //   675: goto -> 688
    //   678: astore #6
    //   680: aload #6
    //   682: invokevirtual getMessage : ()Ljava/lang/String;
    //   685: invokestatic e : (Ljava/lang/String;)V
    //   688: aload_0
    //   689: aload #7
    //   691: invokevirtual toString : ()Ljava/lang/String;
    //   694: invokevirtual buginf : (Ljava/lang/String;)V
    //   697: return
    // Exception table:
    //   from	to	target	type
    //   581	626	678	java/lang/IllegalArgumentException
    //   629	653	678	java/lang/IllegalArgumentException
    //   653	675	678	java/lang/IllegalArgumentException
  }
  
  public boolean queueTransaction(MatrixI2cTransaction paramMatrixI2cTransaction) {
    return queueTransaction(paramMatrixI2cTransaction, false);
  }
  
  public boolean queueTransaction(MatrixI2cTransaction paramMatrixI2cTransaction, boolean paramBoolean) {
    if (!paramBoolean) {
      Iterator<MatrixI2cTransaction> iterator = this.transactionQueue.iterator();
      while (iterator.hasNext()) {
        if (((MatrixI2cTransaction)iterator.next()).isEqual(paramMatrixI2cTransaction)) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("NO Queue transaction ");
          stringBuilder1.append(paramMatrixI2cTransaction.toString());
          buginf(stringBuilder1.toString());
          return false;
        } 
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("YES Queue transaction ");
    stringBuilder.append(paramMatrixI2cTransaction.toString());
    buginf(stringBuilder.toString());
    this.transactionQueue.add(paramMatrixI2cTransaction);
    return true;
  }
  
  public void registerMotorController(MatrixDcMotorController paramMatrixDcMotorController) {
    this.motorController = paramMatrixDcMotorController;
  }
  
  public void registerServoController(MatrixServoController paramMatrixServoController) {
    this.servoController = paramMatrixServoController;
  }
  
  protected void sendHeartbeat() {
    queueTransaction(new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_TIMEOUT, 3));
  }
  
  public void waitOnRead() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield waitingForGodot : Z
    //   7: aload_0
    //   8: getfield waitingForGodot : Z
    //   11: ifeq -> 27
    //   14: aload_0
    //   15: lconst_0
    //   16: invokevirtual wait : (J)V
    //   19: goto -> 7
    //   22: astore_1
    //   23: aload_1
    //   24: invokevirtual printStackTrace : ()V
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	30	finally
    //   7	19	22	java/lang/InterruptedException
    //   7	19	30	finally
    //   23	27	30	finally
    //   27	29	30	finally
    //   31	33	30	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\matrix\MatrixMasterController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
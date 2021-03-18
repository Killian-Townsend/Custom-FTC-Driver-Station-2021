package org.firstinspires.ftc.robotcore.internal.ftdi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.internal.collections.ArrayRunQueueLong;
import org.firstinspires.ftc.robotcore.internal.collections.CircularByteBuffer;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.collections.MarkedItemQueue;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public class ReadBufferManager extends FtConstants {
  public static final String TAG = "ReadBufferManager";
  
  private final ArrayList<BulkPacketBufferIn> mAvailableInBuffers;
  
  private final int mAvailableInBuffersCapacity;
  
  private final ArrayList<BulkPacketBufferOut> mAvailableOutBuffers;
  
  private final int mAvailableOutBuffersCapacity;
  
  private final CircularByteBuffer mCircularBuffer;
  
  private boolean mDebugRetainBuffers;
  
  private final FtDevice mDevice;
  
  private final int mEndpointMaxPacketSize;
  
  private boolean mIsOpen = true;
  
  private final MarkedItemQueue mMarkedItemQueue;
  
  private final FtDeviceManagerParams mParams;
  
  private volatile boolean mProcessBulkInDataCallInFlight;
  
  private boolean mReadBulkInDataInterruptRequested;
  
  private volatile Thread mReadBulkInDataThread;
  
  private Deadline mReadDeadline;
  
  private final ArrayList<BulkPacketBufferIn> mReadableBuffers;
  
  private final EvictingBlockingQueue<BulkPacketBuffer> mRetainedBuffers;
  
  private final ArrayRunQueueLong mTimestamps;
  
  public ReadBufferManager(FtDevice paramFtDevice, boolean paramBoolean) throws IOException, InterruptedException {
    this.mDevice = paramFtDevice;
    this.mParams = paramFtDevice.getDriverParameters();
    this.mReadDeadline = new Deadline(this.mParams.getBulkInReadTimeout(), TimeUnit.MILLISECONDS);
    this.mEndpointMaxPacketSize = this.mDevice.getEndpointMaxPacketSize();
    this.mCircularBuffer = new CircularByteBuffer(this.mEndpointMaxPacketSize * 5, this.mParams.getMaxReadBufferSize());
    this.mMarkedItemQueue = new MarkedItemQueue();
    this.mTimestamps = new ArrayRunQueueLong();
    int i = this.mParams.getPacketBufferCacheSize();
    this.mAvailableInBuffersCapacity = i;
    this.mAvailableOutBuffersCapacity = Math.min(i, this.mParams.getRetainedBufferCapacity());
    this.mAvailableInBuffers = new ArrayList<BulkPacketBufferIn>();
    this.mAvailableOutBuffers = new ArrayList<BulkPacketBufferOut>();
    this.mReadableBuffers = new ArrayList<BulkPacketBufferIn>();
    EvictingBlockingQueue<BulkPacketBuffer> evictingBlockingQueue = new EvictingBlockingQueue(new ArrayBlockingQueue(this.mParams.getRetainedBufferCapacity()));
    this.mRetainedBuffers = evictingBlockingQueue;
    evictingBlockingQueue.setEvictAction(new RecentPacketEvicted());
    this.mReadBulkInDataThread = null;
    this.mReadBulkInDataInterruptRequested = false;
    this.mProcessBulkInDataCallInFlight = false;
    this.mDebugRetainBuffers = paramBoolean;
    verifyInvariants("ctor");
  }
  
  private boolean extantProcessBulkInData() {
    return this.mProcessBulkInDataCallInFlight;
  }
  
  private boolean extantReadBulkInDataCall() {
    return (this.mReadBulkInDataThread != null);
  }
  
  private void extractReadData(BulkPacketBufferIn paramBulkPacketBufferIn) throws InterruptedException {
    int i = paramBulkPacketBufferIn.getCurrentLength();
    if (i > 0)
      verifyInvariants("->extractReadData"); 
  }
  
  private boolean isOpen() {
    return (this.mIsOpen && FtDevice.isOpen(this.mDevice));
  }
  
  private void offerAvailableBufferIn(BulkPacketBufferIn paramBulkPacketBufferIn) {
    synchronized (this.mAvailableInBuffers) {
      if (this.mAvailableInBuffers.size() < this.mAvailableInBuffersCapacity)
        this.mAvailableInBuffers.add(paramBulkPacketBufferIn); 
      return;
    } 
  }
  
  private void offerAvailableBufferOut(BulkPacketBufferOut paramBulkPacketBufferOut) {
    if (paramBulkPacketBufferOut.capacity() == this.mEndpointMaxPacketSize)
      synchronized (this.mAvailableOutBuffers) {
        if (this.mAvailableOutBuffers.size() < this.mAvailableOutBuffersCapacity)
          this.mAvailableOutBuffers.add(paramBulkPacketBufferOut); 
        return;
      }  
  }
  
  private void setBufferBounds(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    paramByteBuffer.clear();
    paramByteBuffer.position(paramInt1);
    paramByteBuffer.limit(paramInt2);
  }
  
  private void spinWaitNoReadBulkInData() {
    while (true) {
      if (!extantReadBulkInDataCall())
        return; 
      Thread.yield();
    } 
  }
  
  private void verifyInvariants(String paramString) {}
  
  private void wakeReadBulkInData() {
    synchronized (this.mCircularBuffer) {
      this.mCircularBuffer.notifyAll();
      return;
    } 
  }
  
  public BulkPacketBufferIn acquireReadableInputBuffer() throws InterruptedException {
    while (true) {
      synchronized (this.mReadableBuffers) {
        if (!this.mReadableBuffers.isEmpty())
          return this.mReadableBuffers.remove(0); 
        this.mReadableBuffers.wait();
      } 
    } 
  }
  
  public BulkPacketBufferIn acquireWritableInputBuffer() {
    ArrayList<BulkPacketBufferIn> arrayList;
    BulkPacketBufferIn bulkPacketBufferIn;
    synchronized (this.mAvailableInBuffers) {
      ArrayList arrayList1;
      if (!this.mAvailableInBuffers.isEmpty()) {
        arrayList1 = (ArrayList)this.mAvailableInBuffers.remove(this.mAvailableInBuffers.size() - 1);
      } else {
        arrayList1 = null;
      } 
      arrayList = arrayList1;
      if (arrayList1 == null)
        bulkPacketBufferIn = new BulkPacketBufferIn(this.mEndpointMaxPacketSize); 
      return bulkPacketBufferIn;
    } 
  }
  
  void close() {
    if (this.mIsOpen) {
      this.mIsOpen = false;
      Assert.assertFalse(extantProcessBulkInData());
      wakeReadBulkInData();
      spinWaitNoReadBulkInData();
    } 
  }
  
  public boolean getDebugRetainBuffers() {
    return this.mDebugRetainBuffers;
  }
  
  public FtDeviceManagerParams getParams() {
    return this.mParams;
  }
  
  public int getReadBufferSize() {
    synchronized (this.mCircularBuffer) {
      return this.mCircularBuffer.size();
    } 
  }
  
  protected Deadline getReadDeadline(long paramLong) {
    long l = paramLong;
    if (paramLong == 0L)
      l = this.mParams.getBulkInReadTimeout(); 
    if (this.mReadDeadline.getDuration(TimeUnit.MILLISECONDS) == l) {
      this.mReadDeadline.reset();
    } else {
      this.mReadDeadline = new Deadline(l, TimeUnit.MILLISECONDS);
    } 
    return this.mReadDeadline;
  }
  
  public boolean isReadBufferFull() {
    synchronized (this.mCircularBuffer) {
      if (this.mCircularBuffer.remainingCapacity() == 0)
        return true; 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    return bool;
  }
  
  public void logRetainedBuffers(long paramLong1, long paramLong2, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mRetainedBuffers : Lorg/firstinspires/ftc/robotcore/internal/collections/EvictingBlockingQueue;
    //   4: astore #13
    //   6: aload #13
    //   8: monitorenter
    //   9: aload #5
    //   11: aload #6
    //   13: aload #7
    //   15: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   18: aconst_null
    //   19: astore #7
    //   21: getstatic java/util/concurrent/TimeUnit.NANOSECONDS : Ljava/util/concurrent/TimeUnit;
    //   24: astore #14
    //   26: aload_0
    //   27: getfield mRetainedBuffers : Lorg/firstinspires/ftc/robotcore/internal/collections/EvictingBlockingQueue;
    //   30: invokevirtual poll : ()Ljava/lang/Object;
    //   33: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/BulkPacketBuffer
    //   36: astore #12
    //   38: aload #12
    //   40: ifnonnull -> 87
    //   43: lload_3
    //   44: lconst_0
    //   45: lcmp
    //   46: ifle -> 83
    //   49: aload #5
    //   51: ldc_w 'timer expired (ts=%.3f)'
    //   54: iconst_1
    //   55: anewarray java/lang/Object
    //   58: dup
    //   59: iconst_0
    //   60: aload #14
    //   62: lload_3
    //   63: lload_1
    //   64: lsub
    //   65: getstatic java/util/concurrent/TimeUnit.NANOSECONDS : Ljava/util/concurrent/TimeUnit;
    //   68: invokevirtual convert : (JLjava/util/concurrent/TimeUnit;)J
    //   71: l2d
    //   72: ldc2_w 1.0E-6
    //   75: dmul
    //   76: invokestatic valueOf : (D)Ljava/lang/Double;
    //   79: aastore
    //   80: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   83: aload #13
    //   85: monitorexit
    //   86: return
    //   87: aload #7
    //   89: astore #6
    //   91: lload_1
    //   92: lstore #10
    //   94: aload #7
    //   96: ifnonnull -> 121
    //   99: lload_1
    //   100: lstore #10
    //   102: lload_1
    //   103: lconst_0
    //   104: lcmp
    //   105: ifne -> 223
    //   108: aload #12
    //   110: getstatic java/util/concurrent/TimeUnit.NANOSECONDS : Ljava/util/concurrent/TimeUnit;
    //   113: invokevirtual getTimestamp : (Ljava/util/concurrent/TimeUnit;)J
    //   116: lstore #10
    //   118: goto -> 223
    //   121: aload #12
    //   123: aload #14
    //   125: invokevirtual getTimestamp : (Ljava/util/concurrent/TimeUnit;)J
    //   128: aload #14
    //   130: lload #10
    //   132: getstatic java/util/concurrent/TimeUnit.NANOSECONDS : Ljava/util/concurrent/TimeUnit;
    //   135: invokevirtual convert : (JLjava/util/concurrent/TimeUnit;)J
    //   138: lsub
    //   139: l2d
    //   140: dstore #8
    //   142: aload #12
    //   144: instanceof org/firstinspires/ftc/robotcore/internal/ftdi/BulkPacketBufferIn
    //   147: ifeq -> 230
    //   150: ldc_w 'read '
    //   153: astore #7
    //   155: goto -> 158
    //   158: aload #5
    //   160: ldc_w '%s (ts=%.3f)'
    //   163: iconst_2
    //   164: anewarray java/lang/Object
    //   167: dup
    //   168: iconst_0
    //   169: aload #7
    //   171: aastore
    //   172: dup
    //   173: iconst_1
    //   174: dload #8
    //   176: ldc2_w 1.0E-6
    //   179: dmul
    //   180: invokestatic valueOf : (D)Ljava/lang/Double;
    //   183: aastore
    //   184: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   187: aload #12
    //   189: invokevirtual array : ()[B
    //   192: aload #12
    //   194: invokevirtual arrayOffset : ()I
    //   197: aload #12
    //   199: invokevirtual getCurrentLength : ()I
    //   202: invokestatic logBytes : (Ljava/lang/String;Ljava/lang/String;[BII)V
    //   205: aload #6
    //   207: astore #7
    //   209: lload #10
    //   211: lstore_1
    //   212: goto -> 26
    //   215: astore #5
    //   217: aload #13
    //   219: monitorexit
    //   220: aload #5
    //   222: athrow
    //   223: aload #12
    //   225: astore #6
    //   227: goto -> 121
    //   230: ldc_w 'write'
    //   233: astore #7
    //   235: goto -> 158
    // Exception table:
    //   from	to	target	type
    //   9	18	215	finally
    //   21	26	215	finally
    //   26	38	215	finally
    //   49	83	215	finally
    //   83	86	215	finally
    //   108	118	215	finally
    //   121	150	215	finally
    //   158	205	215	finally
    //   217	220	215	finally
  }
  
  public boolean mightBeAtUsbPacketStart() {
    return (this.mMarkedItemQueue.isAtMarkedItem() || this.mMarkedItemQueue.isEmpty());
  }
  
  public BulkPacketBufferOut newOutputBuffer(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    BulkPacketBufferOut bulkPacketBufferOut;
    if (paramInt2 <= this.mEndpointMaxPacketSize) {
      ArrayList<BulkPacketBufferOut> arrayList;
      synchronized (this.mAvailableOutBuffers) {
        ArrayList arrayList1;
        if (!this.mAvailableOutBuffers.isEmpty()) {
          arrayList1 = (ArrayList)this.mAvailableOutBuffers.remove(this.mAvailableOutBuffers.size() - 1);
        } else {
          arrayList1 = null;
        } 
        arrayList = arrayList1;
        if (arrayList1 == null)
          bulkPacketBufferOut = new BulkPacketBufferOut(this.mEndpointMaxPacketSize); 
      } 
    } else {
      bulkPacketBufferOut = new BulkPacketBufferOut(paramInt2);
    } 
    bulkPacketBufferOut.copyFrom(paramArrayOfbyte, paramInt1, paramInt2);
    return bulkPacketBufferOut;
  }
  
  public void processBulkInData(BulkPacketBufferIn paramBulkPacketBufferIn) throws FtDeviceIOException, InterruptedException {
    if (isOpen() && paramBulkPacketBufferIn.getCurrentLength() > 0) {
      this.mProcessBulkInDataCallInFlight = true;
      try {
        int i = paramBulkPacketBufferIn.getCurrentLength();
        if (i < 2)
          return; 
      } finally {
        this.mProcessBulkInDataCallInFlight = false;
      } 
    } 
  }
  
  public int processEventChars(boolean paramBoolean, short paramShort1, short paramShort2) throws InterruptedException {
    return 0;
  }
  
  public void purgeInputData() {
    synchronized (this.mCircularBuffer) {
      synchronized (this.mReadableBuffers) {
        this.mReadableBuffers.clear();
        this.mCircularBuffer.clear();
        this.mMarkedItemQueue.clear();
        this.mTimestamps.clear();
        return;
      } 
    } 
  }
  
  public int readBulkInData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, long paramLong, TimeWindow paramTimeWindow) throws InterruptedException {
    // Byte code:
    //   0: aload_0
    //   1: getfield mReadBulkInDataInterruptRequested : Z
    //   4: ifne -> 265
    //   7: iload_3
    //   8: ifle -> 263
    //   11: aload_0
    //   12: invokespecial isOpen : ()Z
    //   15: ifeq -> 263
    //   18: aload_0
    //   19: invokestatic currentThread : ()Ljava/lang/Thread;
    //   22: putfield mReadBulkInDataThread : Ljava/lang/Thread;
    //   25: aload_0
    //   26: ldc_w '->readBulkInData'
    //   29: invokespecial verifyInvariants : (Ljava/lang/String;)V
    //   32: aload_0
    //   33: lload #4
    //   35: invokevirtual getReadDeadline : (J)Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   38: astore #9
    //   40: aload_0
    //   41: invokespecial isOpen : ()Z
    //   44: ifeq -> 243
    //   47: aload #9
    //   49: invokevirtual hasExpired : ()Z
    //   52: istore #7
    //   54: iload #7
    //   56: ifeq -> 73
    //   59: aload_0
    //   60: ldc_w '<-readBulkInData'
    //   63: invokespecial verifyInvariants : (Ljava/lang/String;)V
    //   66: aload_0
    //   67: aconst_null
    //   68: putfield mReadBulkInDataThread : Ljava/lang/Thread;
    //   71: iconst_0
    //   72: ireturn
    //   73: invokestatic interrupted : ()Z
    //   76: ifne -> 232
    //   79: aload_0
    //   80: getfield mCircularBuffer : Lorg/firstinspires/ftc/robotcore/internal/collections/CircularByteBuffer;
    //   83: astore #8
    //   85: aload #8
    //   87: monitorenter
    //   88: aload_0
    //   89: getfield mCircularBuffer : Lorg/firstinspires/ftc/robotcore/internal/collections/CircularByteBuffer;
    //   92: invokevirtual size : ()I
    //   95: iload_3
    //   96: if_icmplt -> 188
    //   99: aload_0
    //   100: getfield mCircularBuffer : Lorg/firstinspires/ftc/robotcore/internal/collections/CircularByteBuffer;
    //   103: aload_1
    //   104: iload_2
    //   105: iload_3
    //   106: invokevirtual read : ([BII)I
    //   109: istore_2
    //   110: iload_2
    //   111: ifle -> 171
    //   114: aload_0
    //   115: getfield mMarkedItemQueue : Lorg/firstinspires/ftc/robotcore/internal/collections/MarkedItemQueue;
    //   118: iload_2
    //   119: invokevirtual removeItems : (I)V
    //   122: aload #6
    //   124: ifnull -> 155
    //   127: aload #6
    //   129: aload_0
    //   130: getfield mTimestamps : Lorg/firstinspires/ftc/robotcore/internal/collections/ArrayRunQueueLong;
    //   133: invokevirtual getFirst : ()J
    //   136: invokevirtual setNanosecondsFirst : (J)V
    //   139: aload #6
    //   141: aload_0
    //   142: getfield mTimestamps : Lorg/firstinspires/ftc/robotcore/internal/collections/ArrayRunQueueLong;
    //   145: iload_2
    //   146: invokevirtual removeFirstCount : (I)J
    //   149: invokevirtual setNanosecondsLast : (J)V
    //   152: goto -> 164
    //   155: aload_0
    //   156: getfield mTimestamps : Lorg/firstinspires/ftc/robotcore/internal/collections/ArrayRunQueueLong;
    //   159: iload_2
    //   160: invokevirtual removeFirstCount : (I)J
    //   163: pop2
    //   164: aload_0
    //   165: getfield mCircularBuffer : Lorg/firstinspires/ftc/robotcore/internal/collections/CircularByteBuffer;
    //   168: invokevirtual notifyAll : ()V
    //   171: aload #8
    //   173: monitorexit
    //   174: aload_0
    //   175: ldc_w '<-readBulkInData'
    //   178: invokespecial verifyInvariants : (Ljava/lang/String;)V
    //   181: aload_0
    //   182: aconst_null
    //   183: putfield mReadBulkInDataThread : Ljava/lang/Thread;
    //   186: iload_2
    //   187: ireturn
    //   188: aload #9
    //   190: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   193: invokevirtual timeRemaining : (Ljava/util/concurrent/TimeUnit;)J
    //   196: ldc2_w 2147483647
    //   199: invokestatic min : (JJ)J
    //   202: lstore #4
    //   204: lload #4
    //   206: lconst_0
    //   207: lcmp
    //   208: ifle -> 220
    //   211: aload_0
    //   212: getfield mCircularBuffer : Lorg/firstinspires/ftc/robotcore/internal/collections/CircularByteBuffer;
    //   215: lload #4
    //   217: invokevirtual wait : (J)V
    //   220: aload #8
    //   222: monitorexit
    //   223: goto -> 40
    //   226: astore_1
    //   227: aload #8
    //   229: monitorexit
    //   230: aload_1
    //   231: athrow
    //   232: new java/lang/InterruptedException
    //   235: dup
    //   236: ldc_w 'interrupted reading USB data'
    //   239: invokespecial <init> : (Ljava/lang/String;)V
    //   242: athrow
    //   243: iconst_m1
    //   244: istore_2
    //   245: goto -> 174
    //   248: astore_1
    //   249: aload_0
    //   250: ldc_w '<-readBulkInData'
    //   253: invokespecial verifyInvariants : (Ljava/lang/String;)V
    //   256: aload_0
    //   257: aconst_null
    //   258: putfield mReadBulkInDataThread : Ljava/lang/Thread;
    //   261: aload_1
    //   262: athrow
    //   263: iconst_0
    //   264: ireturn
    //   265: new java/lang/InterruptedException
    //   268: dup
    //   269: ldc_w 'interrupted in readBulkInData()'
    //   272: invokespecial <init> : (Ljava/lang/String;)V
    //   275: athrow
    // Exception table:
    //   from	to	target	type
    //   25	40	248	finally
    //   40	54	248	finally
    //   73	88	248	finally
    //   88	110	226	finally
    //   114	122	226	finally
    //   127	152	226	finally
    //   155	164	226	finally
    //   164	171	226	finally
    //   171	174	226	finally
    //   188	204	226	finally
    //   211	220	226	finally
    //   220	223	226	finally
    //   227	230	226	finally
    //   230	232	248	finally
    //   232	243	248	finally
  }
  
  public void releaseReadableBuffer(BulkPacketBufferIn paramBulkPacketBufferIn) {
    synchronized (this.mReadableBuffers) {
      this.mReadableBuffers.add(paramBulkPacketBufferIn);
      this.mReadableBuffers.notifyAll();
      return;
    } 
  }
  
  public void releaseWritableInputBuffer(BulkPacketBufferIn paramBulkPacketBufferIn) {
    if (paramBulkPacketBufferIn.getCurrentLength() <= 2 || !retainRecentBuffer(paramBulkPacketBufferIn))
      offerAvailableBufferIn(paramBulkPacketBufferIn); 
  }
  
  public void requestReadInterrupt(boolean paramBoolean) {
    if (paramBoolean) {
      this.mReadBulkInDataInterruptRequested = true;
      Thread thread = this.mReadBulkInDataThread;
      if (thread != null) {
        thread.interrupt();
        return;
      } 
    } else {
      this.mReadBulkInDataInterruptRequested = false;
    } 
  }
  
  public boolean retainRecentBuffer(BulkPacketBuffer paramBulkPacketBuffer) {
    synchronized (this.mRetainedBuffers) {
      if (this.mDebugRetainBuffers) {
        this.mRetainedBuffers.add(paramBulkPacketBuffer);
        return true;
      } 
      return false;
    } 
  }
  
  public void setDebugRetainBuffers(boolean paramBoolean) {
    synchronized (this.mRetainedBuffers) {
      this.mDebugRetainBuffers = paramBoolean;
      if (!paramBoolean)
        this.mRetainedBuffers.clear(); 
      return;
    } 
  }
  
  public void skipToLikelyUsbPacketStart() {
    synchronized (this.mCircularBuffer) {
      int i = this.mMarkedItemQueue.removeUpToNextMarkedItemOrEnd();
      if (i > 0) {
        this.mTimestamps.removeFirstCount(i);
        this.mCircularBuffer.skip(i);
        this.mCircularBuffer.notifyAll();
      } 
      return;
    } 
  }
  
  private class RecentPacketEvicted implements Consumer<BulkPacketBuffer> {
    private RecentPacketEvicted() {}
    
    public void accept(BulkPacketBuffer param1BulkPacketBuffer) {
      if (param1BulkPacketBuffer instanceof BulkPacketBufferIn) {
        ReadBufferManager.this.offerAvailableBufferIn((BulkPacketBufferIn)param1BulkPacketBuffer);
        return;
      } 
      ReadBufferManager.this.offerAvailableBufferOut((BulkPacketBufferOut)param1BulkPacketBuffer);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\ReadBufferManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
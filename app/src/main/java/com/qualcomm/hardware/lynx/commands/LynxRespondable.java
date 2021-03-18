package com.qualcomm.hardware.lynx.commands;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.LynxModuleWarningManager;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.LynxUnsupportedCommandException;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.hardware.lynx.commands.standard.LynxNack;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;

public abstract class LynxRespondable<RESPONSE extends LynxMessage> extends LynxMessage {
  protected CountDownLatch ackOrNackReceived = new CountDownLatch(1);
  
  protected boolean isAckOrResponseReceived = false;
  
  protected LynxNack nackReceived = null;
  
  protected RESPONSE response = null;
  
  protected CountDownLatch responseOrNackReceived = new CountDownLatch(1);
  
  protected int retransmissionsRemaining = 5;
  
  public LynxRespondable(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  private void onResponseReceived() {
    if (isResponseExpected()) {
      this.isAckOrResponseReceived = true;
      this.responseOrNackReceived.countDown();
      return;
    } 
    RobotLog.e("internal error: unexpected response received for msg#=%d", new Object[] { Integer.valueOf(getMessageNumber()) });
  }
  
  protected void awaitAckResponseOrNack() throws InterruptedException {
    if (isResponseExpected()) {
      awaitAndRetransmit(this.responseOrNackReceived, (LynxNack.ReasonCode)LynxNack.StandardReasonCode.ABANDONED_WAITING_FOR_RESPONSE, "response");
      return;
    } 
    awaitAndRetransmit(this.ackOrNackReceived, (LynxNack.ReasonCode)LynxNack.StandardReasonCode.ABANDONED_WAITING_FOR_ACK, "ack");
  }
  
  protected void awaitAndRetransmit(CountDownLatch paramCountDownLatch, LynxNack.ReasonCode paramReasonCode, String paramString) throws InterruptedException {
    long l1 = System.nanoTime();
    long l2 = getMsAwaitInterval();
    int i = getMsAwaitInterval();
    int j = getMsRetransmissionInterval();
    if (this.module.isNotResponding()) {
      if (this.module instanceof LynxModule)
        LynxModuleWarningManager.getInstance().reportModuleUnresponsive((LynxModule)this.module); 
      onNackReceived(new LynxNack(this.module, paramReasonCode));
      this.module.finishedWithMessage(this);
      return;
    } 
    while (true) {
      long l = l1 + l2 * 1000000L - System.nanoTime();
      if (l <= 0L) {
        RobotLog.e("timeout: abandoning waiting %dms for %s: cmd=%s mod=%d msg#=%d", new Object[] { Integer.valueOf(i), paramString, getClass().getSimpleName(), Integer.valueOf(getModuleAddress()), Integer.valueOf(getMessageNumber()) });
        RobotLog.e("Marking module #%d as unresponsive until we receive some data back", new Object[] { Integer.valueOf(getModuleAddress()) });
        onNackReceived(new LynxNack(this.module, paramReasonCode));
        if (this.module instanceof LynxModule)
          LynxModuleWarningManager.getInstance().reportModuleUnresponsive((LynxModule)this.module); 
        this.module.noteNotResponding();
        this.module.finishedWithMessage(this);
        return;
      } 
      if (paramCountDownLatch.await(Math.min((int)(l / 1000000L), j), TimeUnit.MILLISECONDS))
        return; 
      this.module.retransmit(this);
    } 
  }
  
  protected int getMsAwaitInterval() {
    return 250;
  }
  
  protected int getMsRetransmissionInterval() {
    return 100;
  }
  
  public LynxNack getNackReceived() {
    return this.nackReceived;
  }
  
  public boolean hasBeenAcknowledged() {
    return (isAckOrResponseReceived() || isNackReceived());
  }
  
  public boolean isAckOrResponseReceived() {
    return this.isAckOrResponseReceived;
  }
  
  public boolean isAckable() {
    return true;
  }
  
  public boolean isNackReceived() {
    return (this.nackReceived != null);
  }
  
  public boolean isRetransmittable() {
    return (this.retransmissionsRemaining > 0);
  }
  
  protected void noteAttentionRequired() {
    this.module.noteAttentionRequired();
  }
  
  public void noteRetransmission() {
    int i = this.retransmissionsRemaining - 1;
    this.retransmissionsRemaining = i;
    if (i < 0)
      this.retransmissionsRemaining = 0; 
  }
  
  public void onAckReceived(LynxAck paramLynxAck) {
    if (!this.isAckOrResponseReceived) {
      this.isAckOrResponseReceived = true;
      if (paramLynxAck.isAttentionRequired())
        noteAttentionRequired(); 
      this.ackOrNackReceived.countDown();
    } 
  }
  
  public void onNackReceived(LynxNack paramLynxNack) {
    switch (paramLynxNack.getNackReasonCodeAsEnum()) {
      default:
        RobotLog.v("nack rec'd mod=%d msg#=%d ref#=%d reason=%s:%d", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(getMessageNumber()), Integer.valueOf(getReferenceNumber()), paramLynxNack.getNackReasonCode().toString(), Integer.valueOf(paramLynxNack.getNackReasonCode().getValue()) });
        break;
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    this.nackReceived = paramLynxNack;
    this.ackOrNackReceived.countDown();
    this.responseOrNackReceived.countDown();
  }
  
  public void onPretendTransmit() throws InterruptedException {
    super.onPretendTransmit();
    pretendFinish();
  }
  
  public void onResponseReceived(LynxMessage paramLynxMessage) {
    this.response = (RESPONSE)paramLynxMessage;
    onResponseReceived();
  }
  
  public void pretendFinish() throws InterruptedException {
    this.isAckOrResponseReceived = true;
    if (isResponseExpected()) {
      this.response.setPayloadTimeWindow(new TimeWindow());
      onResponseReceived();
    } 
    if (this.module != null)
      this.module.finishedWithMessage(this); 
    this.ackOrNackReceived.countDown();
  }
  
  protected RESPONSE responseOrThrow() throws LynxNackException {
    if (!isNackReceived())
      return this.response; 
    throw new LynxNackException(this, "%s: nack received: %s:%d", new Object[] { getClass().getSimpleName(), this.nackReceived.getNackReasonCode().toString(), Integer.valueOf(this.nackReceived.getNackReasonCode().getValue()) });
  }
  
  public void send() throws InterruptedException, LynxNackException {
    acquireNetworkLock();
    try {
      this.module.sendCommand(this);
    } catch (LynxUnsupportedCommandException lynxUnsupportedCommandException) {
      throwNackForUnsupportedCommand(lynxUnsupportedCommandException);
    } finally {
      Exception exception;
    } 
    awaitAckResponseOrNack();
    throwIfNack();
    releaseNetworkLock();
  }
  
  public RESPONSE sendReceive() throws InterruptedException, LynxNackException {
    Exception exception;
    acquireNetworkLock();
    try {
      this.module.sendCommand(this);
      awaitAckResponseOrNack();
      RESPONSE rESPONSE = responseOrThrow();
      releaseNetworkLock();
      return rESPONSE;
    } catch (LynxNackException lynxNackException) {
      RESPONSE rESPONSE;
      if (lynxNackException.getNack().getNackReasonCode().isUnsupportedReason() && usePretendResponseIfRealModuleDoesntSupport() && this.response != null) {
        rESPONSE = this.response;
        releaseNetworkLock();
        return rESPONSE;
      } 
      throw rESPONSE;
    } catch (LynxUnsupportedCommandException lynxUnsupportedCommandException) {
      RESPONSE rESPONSE;
      if (usePretendResponseIfRealModuleDoesntSupport() && this.response != null) {
        rESPONSE = this.response;
        releaseNetworkLock();
        return rESPONSE;
      } 
      throwNackForUnsupportedCommand((LynxUnsupportedCommandException)rESPONSE);
      releaseNetworkLock();
      return null;
    } finally {}
    releaseNetworkLock();
    throw exception;
  }
  
  public void setUnretransmittable() {
    this.retransmissionsRemaining = 0;
  }
  
  protected void throwIfNack() throws LynxNackException {
    if (!isNackReceived())
      return; 
    throw new LynxNackException(this, "%s: nack received: %s:%d", new Object[] { getClass().getSimpleName(), this.nackReceived.getNackReasonCode().toString(), Integer.valueOf(this.nackReceived.getNackReasonCode().getValue()) });
  }
  
  protected void throwNackForUnsupportedCommand(LynxUnsupportedCommandException paramLynxUnsupportedCommandException) throws LynxNackException {
    this.nackReceived = new LynxNack(getModule(), (LynxNack.ReasonCode)LynxNack.StandardReasonCode.PACKET_TYPE_ID_UNKNOWN);
    throw new LynxNackException(this, "%s: command %s(#0x%04x) not supported by mod#=%d", new Object[] { getClass().getSimpleName(), paramLynxUnsupportedCommandException.getClazz().getSimpleName(), Integer.valueOf(paramLynxUnsupportedCommandException.getCommandNumber()), Integer.valueOf(getModuleAddress()) });
  }
  
  protected boolean usePretendResponseIfRealModuleDoesntSupport() {
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\LynxRespondable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
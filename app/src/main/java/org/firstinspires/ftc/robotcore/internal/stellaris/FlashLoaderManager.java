package org.firstinspires.ftc.robotcore.internal.stellaris;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.TypeConversion;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.ui.ProgressParameters;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FlashLoaderManager {
  public static boolean DEBUG = false;
  
  public static final String TAG = "FlashLoaderManager";
  
  public static int secondsFirmwareUpdateTimeout = 120;
  
  protected byte[] firmwareImage;
  
  protected int msReadTimeout = 1000;
  
  protected int msRetryPause = 40;
  
  protected int retryAutobaudCount = 5;
  
  protected int retrySendWithRetriesAndVerifyCount = 2;
  
  protected int retrySendWithRetriesCount = 3;
  
  protected int retryVerifyStatusCount = 3;
  
  protected RobotUsbDevice robotUsbDevice;
  
  protected Tracer tracer = Tracer.create("FlashLoaderManager", true);
  
  protected Tracer verboseTracer = Tracer.create("FlashLoaderManager", DEBUG);
  
  public FlashLoaderManager(RobotUsbDevice paramRobotUsbDevice, byte[] paramArrayOfbyte) {
    this.robotUsbDevice = paramRobotUsbDevice;
    this.firmwareImage = paramArrayOfbyte;
  }
  
  protected void doAutobaud() throws InterruptedException, FlashLoaderProtocolException {
    for (int i = 0; i < this.retryAutobaudCount; i++) {
      if (i > 0)
        pauseBetweenRetryWrites(); 
      try {
        this.verboseTracer.trace("sending autobaud sync bytes", new Object[0]);
        write(new byte[] { 85, 85 });
        boolean bool = readAckOrNack();
        if (bool)
          return; 
      } catch (IOException iOException) {
        this.tracer.traceError(iOException, "doAutobaud exception: might retry", new Object[0]);
      } 
    } 
    throw new FlashLoaderProtocolException(makeExceptionMessage("unable to successfully autobaud", new Object[0]));
  }
  
  protected String makeExceptionMessage(String paramString, Object... paramVarArgs) {
    paramString = String.format(paramString, paramVarArgs);
    return String.format("flash loader(serial=%s) : %s", new Object[] { this.robotUsbDevice.getSerialNumber(), paramString });
  }
  
  protected void pauseBetweenRetryWrites() throws InterruptedException {
    Thread.sleep(this.msRetryPause);
  }
  
  protected void read(byte[] paramArrayOfbyte) throws IOException, TimeoutException, InterruptedException {
    read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  protected void read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException, TimeoutException, InterruptedException {
    if (paramInt2 > 0)
      try {
        paramInt1 = this.robotUsbDevice.read(paramArrayOfbyte, paramInt1, paramInt2, this.msReadTimeout, null);
        this.verboseTracer.trace("received %d bytes", new Object[] { Integer.valueOf(paramInt1) });
        if (paramInt1 != 0)
          return; 
        throw new TimeoutException(makeExceptionMessage("unable to read %d bytes from flash loader", new Object[] { Integer.valueOf(paramInt2) }));
      } catch (RobotUsbException robotUsbException) {
        throw new IOException(makeExceptionMessage("unable to read %d bytes from flash loader", new Object[] { Integer.valueOf(paramInt2) }), robotUsbException);
      }  
  }
  
  protected boolean readAckOrNack() {
    try {
      return readAckOrNackOrException();
    } catch (TimeoutException timeoutException) {
      this.tracer.traceError(timeoutException, "readAckOrNack exception", new Object[0]);
      return false;
    } catch (IOException iOException) {
      this.tracer.traceError(iOException, "readAckOrNack exception", new Object[0]);
      return false;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return false;
    } 
  }
  
  protected boolean readAckOrNackOrException() throws IOException, TimeoutException, InterruptedException {
    while (true) {
      byte[] arrayOfByte = new byte[1];
      read(arrayOfByte);
      byte b = arrayOfByte[0];
      if (b != -52) {
        if (b != 0) {
          if (b != 51) {
            this.tracer.traceError("readAckOrNackOrException: unexpected: 0x%02x: treat as nak", new Object[] { Byte.valueOf(arrayOfByte[0]) });
            return false;
          } 
          this.tracer.traceError("nak received", new Object[0]);
          return false;
        } 
        continue;
      } 
      return true;
    } 
  }
  
  protected void sendAckOrException() throws IOException, InterruptedException {
    write(new byte[] { -52 });
  }
  
  protected void sendAckOrIgnore() throws InterruptedException {
    try {
      sendAckOrException();
      return;
    } catch (IOException iOException) {
      this.tracer.traceError(iOException, "sendAckOrIgnore exception: ignored", new Object[0]);
      return;
    } 
  }
  
  protected void sendNakOrException() throws IOException, InterruptedException {
    write(new byte[] { 51 });
  }
  
  protected void sendNakOrIgnore() throws InterruptedException {
    try {
      this.tracer.traceError("sending nak", new Object[0]);
      sendNakOrException();
      return;
    } catch (IOException iOException) {
      this.tracer.traceError(iOException, "sendNakOrIgnore exception: ignored", new Object[0]);
      return;
    } 
  }
  
  protected void sendWithRetries(FlashLoaderCommand paramFlashLoaderCommand, boolean paramBoolean) throws FlashLoaderProtocolException, InterruptedException {
    paramFlashLoaderCommand.updateChecksum();
    for (int i = 0; i < this.retrySendWithRetriesCount; i++) {
      if (i > 0)
        pauseBetweenRetryWrites(); 
      try {
        write(paramFlashLoaderCommand.data);
        if (paramBoolean) {
          boolean bool = readAckOrNack();
          if (bool)
            return; 
        } else {
          return;
        } 
      } catch (IOException iOException) {
        this.tracer.traceError(iOException, "sendWithRetries exception: i=%d might retry", new Object[] { Integer.valueOf(i) });
      } 
    } 
    throw new FlashLoaderProtocolException(makeExceptionMessage("unable to send command", new Object[0]), paramFlashLoaderCommand);
  }
  
  protected void sendWithRetriesAndVerify(FlashLoaderCommand paramFlashLoaderCommand) throws InterruptedException, FlashLoaderProtocolException {
    int i = 0;
    while (i < this.retrySendWithRetriesAndVerifyCount) {
      sendWithRetries(paramFlashLoaderCommand, true);
      try {
        verifyStatus();
        return;
      } catch (FlashLoaderProtocolException flashLoaderProtocolException) {
        this.tracer.traceError(flashLoaderProtocolException, "exception in sendWithRetriesAndVerify(): might retry", new Object[0]);
        i++;
      } 
    } 
    throw new FlashLoaderProtocolException("sendWithRetriesAndVerify() failed: ", paramFlashLoaderCommand);
  }
  
  public void updateFirmware(Consumer<ProgressParameters> paramConsumer) throws InterruptedException, FlashLoaderProtocolException {
    doAutobaud();
    sendWithRetriesAndVerify(new FlashLoaderPingCommand());
    sendWithRetriesAndVerify(new FlashLoaderDownloadCommand(0, this.firmwareImage.length));
    for (int i = 0; i < this.firmwareImage.length; i = j) {
      Tracer tracer = this.tracer;
      int j = i + 8;
      tracer.trace("flashing [%d,%d) of %d", new Object[] { Integer.valueOf(i), Integer.valueOf(Math.min(j, this.firmwareImage.length)), Integer.valueOf(this.firmwareImage.length) });
      paramConsumer.accept(new ProgressParameters(i, this.firmwareImage.length));
      sendWithRetriesAndVerify(new FlashLoaderSendDataCommand(this.firmwareImage, i));
    } 
    byte[] arrayOfByte = this.firmwareImage;
    paramConsumer.accept(new ProgressParameters(arrayOfByte.length, arrayOfByte.length));
    sendWithRetries(new FlashLoaderResetCommand(), false);
  }
  
  protected void verifyStatus() throws FlashLoaderProtocolException, InterruptedException {
    for (int i = 0; i < this.retryVerifyStatusCount; i++) {
      this.verboseTracer.trace("sending getStatus", new Object[0]);
      sendWithRetries(new FlashLoaderGetStatusCommand(), true);
      FlashLoaderGetStatusResponse flashLoaderGetStatusResponse = new FlashLoaderGetStatusResponse();
      try {
        while (true) {
          read(flashLoaderGetStatusResponse.data, 0, 1);
          if (flashLoaderGetStatusResponse.data[0] != 0) {
            if (TypeConversion.unsignedByteToInt(flashLoaderGetStatusResponse.data[0]) == flashLoaderGetStatusResponse.data.length) {
              read(flashLoaderGetStatusResponse.data, 1, flashLoaderGetStatusResponse.data.length - 1);
              if (flashLoaderGetStatusResponse.isChecksumValid()) {
                sendAckOrIgnore();
                byte b = flashLoaderGetStatusResponse.data[2];
                if (b == 64)
                  return; 
                throw new FlashLoaderProtocolException(makeExceptionMessage("invalid status: 0x%02x", new Object[] { Byte.valueOf(b) }));
              } 
              sendNakOrIgnore();
              break;
            } 
            throw new FlashLoaderProtocolException(makeExceptionMessage("invalid length: expected=%d found=%d", new Object[] { Integer.valueOf(flashLoaderGetStatusResponse.data.length), Integer.valueOf(TypeConversion.unsignedByteToInt(flashLoaderGetStatusResponse.data[0])) }));
          } 
        } 
      } catch (IOException iOException) {
        this.tracer.traceError(iOException, "verifyStatus() exception: i=%d might retry", new Object[] { Integer.valueOf(i) });
        sendNakOrIgnore();
      } catch (TimeoutException timeoutException) {}
    } 
    throw new FlashLoaderProtocolException(makeExceptionMessage("unable to verify status", new Object[0]));
  }
  
  protected void write(byte[] paramArrayOfbyte) throws IOException, InterruptedException {
    this.verboseTracer.trace("writing %d bytes", new Object[] { Integer.valueOf(paramArrayOfbyte.length) });
    try {
      this.robotUsbDevice.write(paramArrayOfbyte);
      return;
    } catch (RobotUsbException robotUsbException) {
      throw new IOException(makeExceptionMessage("unable to write to flash loader", new Object[0]), robotUsbException);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\stellaris\FlashLoaderManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
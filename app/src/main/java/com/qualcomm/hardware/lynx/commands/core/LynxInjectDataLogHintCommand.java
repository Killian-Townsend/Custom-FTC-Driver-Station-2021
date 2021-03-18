package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class LynxInjectDataLogHintCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public static final int cbFixed = 1;
  
  public static final int cbMaxText = 100;
  
  public static final Charset charset = Charset.forName("UTF-8");
  
  private byte[] payload;
  
  public LynxInjectDataLogHintCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxInjectDataLogHintCommand(LynxModuleIntf paramLynxModuleIntf, String paramString) {
    this(paramLynxModuleIntf);
    setHintText(paramString);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    byte[] arrayOfByte = new byte[TypeConversion.unsignedByteToInt(byteBuffer.get())];
    this.payload = arrayOfByte;
    byteBuffer.get(arrayOfByte);
  }
  
  public String getHintText() {
    return (this.payload != null) ? new String(this.payload, charset) : "";
  }
  
  public void setHintText(String paramString) {
    String str = paramString;
    if (paramString.length() > 100)
      str = paramString.substring(0, 100); 
    while (true) {
      byte[] arrayOfByte = str.getBytes(charset);
      this.payload = arrayOfByte;
      if (arrayOfByte.length <= 100)
        return; 
      str = str.substring(0, str.length() - 1);
    } 
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(this.payload.length + 1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put((byte)this.payload.length);
    byteBuffer.put(this.payload);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxInjectDataLogHintCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
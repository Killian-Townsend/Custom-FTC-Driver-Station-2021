package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Util;
import java.nio.ByteBuffer;

public class LynxQueryInterfaceResponse extends LynxStandardResponse {
  short commandNumberFirst = 0;
  
  short numberOfCommands = 0;
  
  public LynxQueryInterfaceResponse(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public static int getStandardCommandNumber() {
    return LynxQueryInterfaceCommand.getStandardCommandNumber() | 0x8000;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte);
    byteBuffer.order(LynxDatagram.LYNX_ENDIAN);
    this.commandNumberFirst = byteBuffer.getShort();
    this.numberOfCommands = byteBuffer.getShort();
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public int getCommandNumberFirst() {
    return TypeConversion.unsignedShortToInt(this.commandNumberFirst);
  }
  
  public int getNumberOfCommands() {
    return TypeConversion.unsignedShortToInt(this.numberOfCommands);
  }
  
  public byte[] toPayloadByteArray() {
    return Util.concatenateByteArrays(TypeConversion.shortToByteArray(this.commandNumberFirst, LynxDatagram.LYNX_ENDIAN), TypeConversion.shortToByteArray(this.numberOfCommands, LynxDatagram.LYNX_ENDIAN));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxQueryInterfaceResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
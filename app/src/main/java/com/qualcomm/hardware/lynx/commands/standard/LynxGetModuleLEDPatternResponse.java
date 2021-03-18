package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.hardware.Blinker;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class LynxGetModuleLEDPatternResponse extends LynxStandardResponse {
  LynxSetModuleLEDPatternCommand.Steps steps = new LynxSetModuleLEDPatternCommand.Steps();
  
  public LynxGetModuleLEDPatternResponse(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public static int getStandardCommandNumber() {
    return LynxGetModuleLEDPatternCommand.getStandardCommandNumber() | 0x8000;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.steps = new LynxSetModuleLEDPatternCommand.Steps();
    while (byteBuffer.remaining() >= LynxSetModuleLEDPatternCommand.cbSerializeStep()) {
      Blinker.Step step = new Blinker.Step();
      LynxSetModuleLEDPatternCommand.deserializeStep(step, byteBuffer);
      this.steps.add(step);
    } 
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(this.steps.cbSerialize()).order(LynxDatagram.LYNX_ENDIAN);
    Iterator<Blinker.Step> iterator = this.steps.iterator();
    while (iterator.hasNext())
      LynxSetModuleLEDPatternCommand.serializeStep(iterator.next(), byteBuffer); 
    if (this.steps.size() < 16)
      LynxSetModuleLEDPatternCommand.serializeStep(Blinker.Step.nullStep(), byteBuffer); 
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxGetModuleLEDPatternResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
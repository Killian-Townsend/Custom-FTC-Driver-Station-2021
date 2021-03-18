package com.qualcomm.hardware.lynx.commands.standard;

import android.graphics.Color;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.hardware.Blinker;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class LynxSetModuleLEDPatternCommand extends LynxStandardCommand<LynxAck> {
  public static final int maxStepCount = 16;
  
  Steps steps = new Steps();
  
  public LynxSetModuleLEDPatternCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public LynxSetModuleLEDPatternCommand(LynxModule paramLynxModule, Steps paramSteps) {
    this(paramLynxModule);
    this.steps = paramSteps;
  }
  
  public static int cbSerializeStep() {
    return 4;
  }
  
  public static void deserializeStep(Blinker.Step paramStep, ByteBuffer paramByteBuffer) {
    paramStep.setDuration((paramByteBuffer.get() * 100), TimeUnit.MILLISECONDS);
    byte b1 = paramByteBuffer.get();
    byte b2 = paramByteBuffer.get();
    paramStep.setColor(Color.rgb(paramByteBuffer.get(), b2, b1));
  }
  
  public static int getStandardCommandNumber() {
    return 32524;
  }
  
  public static void serializeStep(Blinker.Step paramStep, ByteBuffer paramByteBuffer) {
    int i = (int)Math.round(paramStep.getDurationMs() / 100.0D);
    int j = paramStep.getColor();
    paramByteBuffer.put((byte)Math.min(255, i));
    paramByteBuffer.put((byte)Color.blue(j));
    paramByteBuffer.put((byte)Color.green(j));
    paramByteBuffer.put((byte)Color.red(j));
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.steps = new Steps();
    while (byteBuffer.remaining() >= cbSerializeStep()) {
      Blinker.Step step = new Blinker.Step();
      deserializeStep(step, byteBuffer);
      this.steps.add(step);
    } 
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(this.steps.cbSerialize()).order(LynxDatagram.LYNX_ENDIAN);
    Iterator<Blinker.Step> iterator = this.steps.iterator();
    while (iterator.hasNext())
      serializeStep(iterator.next(), byteBuffer); 
    if (this.steps.size() < 16)
      serializeStep(Blinker.Step.nullStep(), byteBuffer); 
    return byteBuffer.array();
  }
  
  public static class Steps implements Iterable<Blinker.Step> {
    ArrayList<Blinker.Step> steps = new ArrayList<Blinker.Step>(16);
    
    public void add(int param1Int, Blinker.Step param1Step) {
      if (param1Int < 16)
        this.steps.add(param1Int, param1Step); 
    }
    
    public void add(Blinker.Step param1Step) {
      if (this.steps.size() < 16)
        this.steps.add(param1Step); 
    }
    
    public int cbSerialize() {
      if (size() == 16) {
        int k = this.steps.size();
        int m = LynxSetModuleLEDPatternCommand.cbSerializeStep();
        return k * m;
      } 
      int i = this.steps.size() + 1;
      int j = LynxSetModuleLEDPatternCommand.cbSerializeStep();
      return i * j;
    }
    
    public Iterator<Blinker.Step> iterator() {
      return this.steps.iterator();
    }
    
    public int size() {
      return this.steps.size();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxSetModuleLEDPatternCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
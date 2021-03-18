package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.hardware.lynx.commands.LynxResponse;
import java.nio.charset.Charset;

public class LynxQueryInterfaceCommand extends LynxStandardCommand<LynxQueryInterfaceResponse> {
  private String interfaceName;
  
  public LynxQueryInterfaceCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
    this.response = (LynxMessage)new LynxQueryInterfaceResponse(paramLynxModule);
  }
  
  public LynxQueryInterfaceCommand(LynxModule paramLynxModule, String paramString) {
    this(paramLynxModule);
    this.interfaceName = paramString;
  }
  
  public static Class<? extends LynxResponse> getResponseClass() {
    return (Class)LynxQueryInterfaceResponse.class;
  }
  
  public static int getStandardCommandNumber() {
    return 32519;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    setInterfaceName(new String(paramArrayOfbyte, Charset.forName("UTF-8")));
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  String getInterfaceName() {
    return this.interfaceName;
  }
  
  String getNullTerminatedInterfaceName() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getInterfaceName());
    stringBuilder.append("\000");
    return stringBuilder.toString();
  }
  
  public LynxQueryInterfaceResponse getResponse() {
    return (LynxQueryInterfaceResponse)this.response;
  }
  
  public boolean isResponseExpected() {
    return true;
  }
  
  void setInterfaceName(String paramString) {
    this.interfaceName = paramString;
    if (paramString != null && paramString.length() > 0) {
      paramString = this.interfaceName;
      if (paramString.charAt(paramString.length() - 1) == '\000') {
        paramString = this.interfaceName;
        this.interfaceName = paramString.substring(0, paramString.length() - 1);
      } 
    } 
  }
  
  public byte[] toPayloadByteArray() {
    return getNullTerminatedInterfaceName().getBytes(Charset.forName("UTF-8"));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxQueryInterfaceCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
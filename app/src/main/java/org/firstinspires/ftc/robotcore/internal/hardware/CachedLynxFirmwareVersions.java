package org.firstinspires.ftc.robotcore.internal.hardware;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class CachedLynxFirmwareVersions {
  private static volatile List<LynxModuleInfo> cachedVersions;
  
  public static String formatFirmwareVersion(String paramString) {
    return paramString.substring(paramString.indexOf(',') + 1).replaceAll("[a-zA-Z: ]*", "").replaceAll(",", ".");
  }
  
  public static List<LynxModuleInfo> getFormattedVersions() {
    return cachedVersions;
  }
  
  public static void update(HardwareMap paramHardwareMap) {
    if (paramHardwareMap == null)
      return; 
    List list = paramHardwareMap.getAll(RobotCoreLynxModule.class);
    ArrayList<LynxModuleInfo> arrayList = new ArrayList();
    Iterator<RobotCoreLynxModule> iterator = list.iterator();
    while (true) {
      if (iterator.hasNext()) {
        String str;
        RobotCoreLynxModule robotCoreLynxModule = iterator.next();
        try {
          str = paramHardwareMap.getNamesOf((HardwareDevice)robotCoreLynxModule).iterator().next();
        } catch (RuntimeException runtimeException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Expansion Hub ");
          stringBuilder.append(robotCoreLynxModule.getModuleAddress());
          str = stringBuilder.toString();
        } 
        arrayList.add(new LynxModuleInfo(str, robotCoreLynxModule.getNullableFirmwareVersionString(), robotCoreLynxModule.getSerialNumber().toString(), robotCoreLynxModule.getModuleAddress()));
        continue;
      } 
      cachedVersions = arrayList;
      return;
    } 
  }
  
  public static class LynxModuleInfo {
    public final String firmwareVersion;
    
    public final int moduleAddress;
    
    public final String name;
    
    public final String parentSerial;
    
    private LynxModuleInfo(String param1String1, String param1String2, String param1String3, int param1Int) {
      this.name = param1String1;
      this.moduleAddress = param1Int;
      this.parentSerial = param1String3;
      if (param1String2 == null) {
        this.firmwareVersion = AppUtil.getDefContext().getString(R.string.lynxUnavailableFWVersionString);
        return;
      } 
      this.firmwareVersion = CachedLynxFirmwareVersions.formatFirmwareVersion(param1String2);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\CachedLynxFirmwareVersions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
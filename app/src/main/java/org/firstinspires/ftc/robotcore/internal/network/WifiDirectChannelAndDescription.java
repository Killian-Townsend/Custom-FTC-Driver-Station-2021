package org.firstinspires.ftc.robotcore.internal.network;

import com.qualcomm.robotcore.R;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class WifiDirectChannelAndDescription implements Comparable<WifiDirectChannelAndDescription> {
  protected int channel;
  
  protected String description;
  
  public WifiDirectChannelAndDescription(String paramString) {
    String[] arrayOfString = paramString.split("\\|");
    this.description = arrayOfString[0];
    this.channel = Integer.parseInt(arrayOfString[1]);
  }
  
  public static String getDescription(int paramInt) {
    for (WifiDirectChannelAndDescription wifiDirectChannelAndDescription : load()) {
      if (wifiDirectChannelAndDescription.getChannel() == paramInt)
        return wifiDirectChannelAndDescription.getDescription(); 
    } 
    return AppUtil.getDefContext().getString(R.string.unknown_wifi_direct_channel);
  }
  
  public static List<WifiDirectChannelAndDescription> load() {
    String[] arrayOfString = AppUtil.getDefContext().getResources().getStringArray(R.array.wifi_direct_channels);
    ArrayList<WifiDirectChannelAndDescription> arrayList = new ArrayList();
    int j = arrayOfString.length;
    for (int i = 0; i < j; i++)
      arrayList.add(new WifiDirectChannelAndDescription(arrayOfString[i])); 
    return arrayList;
  }
  
  public int compareTo(WifiDirectChannelAndDescription paramWifiDirectChannelAndDescription) {
    return this.channel - paramWifiDirectChannelAndDescription.channel;
  }
  
  public int getChannel() {
    return this.channel;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public String toString() {
    return getDescription();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiDirectChannelAndDescription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
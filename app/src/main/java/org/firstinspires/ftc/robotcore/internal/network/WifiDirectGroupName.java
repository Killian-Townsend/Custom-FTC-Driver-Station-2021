package org.firstinspires.ftc.robotcore.internal.network;

import android.net.wifi.p2p.WifiP2pGroup;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public class WifiDirectGroupName implements Comparable<WifiDirectGroupName> {
  protected String name;
  
  public WifiDirectGroupName(WifiP2pGroup paramWifiP2pGroup) {
    this.name = paramWifiP2pGroup.getNetworkName();
  }
  
  public WifiDirectGroupName(String paramString) {
    this.name = paramString;
  }
  
  public static List<WifiDirectGroupName> deserializeNames(String paramString) {
    Type type = (new TypeToken<ArrayList<WifiDirectGroupName>>() {
      
      }).getType();
    return (List<WifiDirectGroupName>)SimpleGson.getInstance().fromJson(paramString, type);
  }
  
  public static List<WifiDirectGroupName> namesFromGroups(Collection<WifiP2pGroup> paramCollection) {
    ArrayList<WifiDirectGroupName> arrayList = new ArrayList();
    Iterator<WifiP2pGroup> iterator = paramCollection.iterator();
    while (iterator.hasNext())
      arrayList.add(new WifiDirectGroupName(iterator.next())); 
    return arrayList;
  }
  
  public static String serializeNames(Collection<WifiP2pGroup> paramCollection) {
    return serializeNames(namesFromGroups(paramCollection));
  }
  
  public static String serializeNames(List<WifiDirectGroupName> paramList) {
    return SimpleGson.getInstance().toJson(paramList);
  }
  
  public int compareTo(WifiDirectGroupName paramWifiDirectGroupName) {
    return toString().compareTo(paramWifiDirectGroupName.toString());
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiDirectGroupName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
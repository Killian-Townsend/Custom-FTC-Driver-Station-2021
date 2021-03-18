package com.qualcomm.robotcore.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Network {
  public static ArrayList<String> getHostAddresses(Collection<InetAddress> paramCollection) {
    ArrayList<String> arrayList = new ArrayList();
    Iterator<InetAddress> iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      String str2 = ((InetAddress)iterator.next()).getHostAddress();
      String str1 = str2;
      if (str2.contains("%"))
        str1 = str2.substring(0, str2.indexOf('%')); 
      arrayList.add(str1);
    } 
    return arrayList;
  }
  
  public static ArrayList<InetAddress> getLocalIpAddress(String paramString) {
    ArrayList<InetAddress> arrayList = new ArrayList();
    try {
      for (NetworkInterface networkInterface : Collections.<NetworkInterface>list(NetworkInterface.getNetworkInterfaces())) {
        if (networkInterface.getName() == paramString)
          arrayList.addAll(Collections.list(networkInterface.getInetAddresses())); 
      } 
      return arrayList;
    } catch (SocketException socketException) {
      return arrayList;
    } 
  }
  
  public static ArrayList<InetAddress> getLocalIpAddresses() {
    ArrayList<InetAddress> arrayList = new ArrayList();
    try {
      Iterator<NetworkInterface> iterator = Collections.<NetworkInterface>list(NetworkInterface.getNetworkInterfaces()).iterator();
      while (iterator.hasNext())
        arrayList.addAll(Collections.list(((NetworkInterface)iterator.next()).getInetAddresses())); 
      return arrayList;
    } catch (SocketException socketException) {
      return arrayList;
    } 
  }
  
  public static InetAddress getLoopbackAddress() {
    try {
      return InetAddress.getByAddress(new byte[] { Byte.MAX_VALUE, 0, 0, 1 });
    } catch (UnknownHostException unknownHostException) {
      return null;
    } 
  }
  
  public static ArrayList<InetAddress> removeIPv4Addresses(Collection<InetAddress> paramCollection) {
    ArrayList<InetAddress> arrayList = new ArrayList();
    for (InetAddress inetAddress : paramCollection) {
      if (inetAddress instanceof java.net.Inet6Address)
        arrayList.add(inetAddress); 
    } 
    return arrayList;
  }
  
  public static ArrayList<InetAddress> removeIPv6Addresses(Collection<InetAddress> paramCollection) {
    ArrayList<InetAddress> arrayList = new ArrayList();
    for (InetAddress inetAddress : paramCollection) {
      if (inetAddress instanceof java.net.Inet4Address)
        arrayList.add(inetAddress); 
    } 
    return arrayList;
  }
  
  public static ArrayList<InetAddress> removeLoopbackAddresses(Collection<InetAddress> paramCollection) {
    ArrayList<InetAddress> arrayList = new ArrayList();
    for (InetAddress inetAddress : paramCollection) {
      if (!inetAddress.isLoopbackAddress())
        arrayList.add(inetAddress); 
    } 
    return arrayList;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\Network.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
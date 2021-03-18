package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.Network;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class RobocolConfig {
  public static final int MAX_MAX_PACKET_SIZE = 65520;
  
  public static final int MS_RECEIVE_TIMEOUT = 300;
  
  public static final int PORT_NUMBER = 20884;
  
  public static final byte ROBOCOL_VERSION = 121;
  
  public static final int TIMEOUT = 1000;
  
  public static final int TTL = 3;
  
  public static final int WIFI_P2P_SUBNET_MASK = -256;
  
  public static InetAddress determineBindAddress(InetAddress paramInetAddress) {
    ArrayList<InetAddress> arrayList = Network.removeIPv6Addresses(Network.removeLoopbackAddresses(Network.getLocalIpAddresses()));
    Iterator<InetAddress> iterator = arrayList.iterator();
    while (true) {
      if (iterator.hasNext()) {
        InetAddress inetAddress = iterator.next();
        try {
          Enumeration<InetAddress> enumeration = NetworkInterface.getByInetAddress(inetAddress).getInetAddresses();
          while (enumeration.hasMoreElements()) {
            InetAddress inetAddress1 = enumeration.nextElement();
            boolean bool = inetAddress1.equals(paramInetAddress);
            if (bool)
              return inetAddress1; 
          } 
        } catch (SocketException socketException) {
          RobotLog.v(String.format("socket exception while trying to get network interface of %s", new Object[] { inetAddress.getHostAddress() }));
        } 
        continue;
      } 
      return determineBindAddressBasedOnWifiP2pSubnet(arrayList, paramInetAddress);
    } 
  }
  
  public static InetAddress determineBindAddressBasedOnIsReachable(ArrayList<InetAddress> paramArrayList, InetAddress paramInetAddress) {
    Iterator<InetAddress> iterator = paramArrayList.iterator();
    while (true) {
      if (iterator.hasNext()) {
        InetAddress inetAddress = iterator.next();
        try {
          boolean bool = inetAddress.isReachable(NetworkInterface.getByInetAddress(inetAddress), 3, 1000);
          if (bool)
            return inetAddress; 
        } catch (SocketException socketException) {
          RobotLog.v(String.format("socket exception while trying to get network interface of %s", new Object[] { inetAddress.getHostAddress() }));
        } catch (IOException iOException) {
          RobotLog.v(String.format("IO exception while trying to determine if %s is reachable via %s", new Object[] { paramInetAddress.getHostAddress(), inetAddress.getHostAddress() }));
        } 
        continue;
      } 
      return Network.getLoopbackAddress();
    } 
  }
  
  public static InetAddress determineBindAddressBasedOnWifiP2pSubnet(ArrayList<InetAddress> paramArrayList, InetAddress paramInetAddress) {
    int i = TypeConversion.byteArrayToInt(paramInetAddress.getAddress());
    for (InetAddress paramInetAddress : paramArrayList) {
      if ((TypeConversion.byteArrayToInt(paramInetAddress.getAddress()) & 0xFFFFFF00) == (i & 0xFFFFFF00))
        return paramInetAddress; 
    } 
    return Network.getLoopbackAddress();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\RobocolConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
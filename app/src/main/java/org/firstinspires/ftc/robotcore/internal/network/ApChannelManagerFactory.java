package org.firstinspires.ftc.robotcore.internal.network;

public class ApChannelManagerFactory {
  protected static ApChannelManager apChannelManager;
  
  public static ApChannelManager getInstance() {
    // Byte code:
    //   0: ldc org/firstinspires/ftc/robotcore/internal/network/ApChannelManagerFactory
    //   2: monitorenter
    //   3: getstatic org/firstinspires/ftc/robotcore/internal/network/ApChannelManagerFactory.apChannelManager : Lorg/firstinspires/ftc/robotcore/internal/network/ApChannelManager;
    //   6: ifnonnull -> 38
    //   9: invokestatic isRevControlHub : ()Z
    //   12: ifeq -> 28
    //   15: new org/firstinspires/ftc/robotcore/internal/network/ControlHubApChannelManager
    //   18: dup
    //   19: invokespecial <init> : ()V
    //   22: putstatic org/firstinspires/ftc/robotcore/internal/network/ApChannelManagerFactory.apChannelManager : Lorg/firstinspires/ftc/robotcore/internal/network/ApChannelManager;
    //   25: goto -> 38
    //   28: new org/firstinspires/ftc/robotcore/internal/network/WifiDirectChannelManager
    //   31: dup
    //   32: invokespecial <init> : ()V
    //   35: putstatic org/firstinspires/ftc/robotcore/internal/network/ApChannelManagerFactory.apChannelManager : Lorg/firstinspires/ftc/robotcore/internal/network/ApChannelManager;
    //   38: getstatic org/firstinspires/ftc/robotcore/internal/network/ApChannelManagerFactory.apChannelManager : Lorg/firstinspires/ftc/robotcore/internal/network/ApChannelManager;
    //   41: astore_0
    //   42: ldc org/firstinspires/ftc/robotcore/internal/network/ApChannelManagerFactory
    //   44: monitorexit
    //   45: aload_0
    //   46: areturn
    //   47: astore_0
    //   48: ldc org/firstinspires/ftc/robotcore/internal/network/ApChannelManagerFactory
    //   50: monitorexit
    //   51: aload_0
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   3	25	47	finally
    //   28	38	47	finally
    //   38	42	47	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\ApChannelManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
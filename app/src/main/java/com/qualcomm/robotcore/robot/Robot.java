package com.qualcomm.robotcore.robot;

import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;

public class Robot {
  public final EventLoopManager eventLoopManager;
  
  public Robot(EventLoopManager paramEventLoopManager) {
    this.eventLoopManager = paramEventLoopManager;
  }
  
  public void shutdown() {
    EventLoopManager eventLoopManager = this.eventLoopManager;
    if (eventLoopManager != null)
      eventLoopManager.shutdown(); 
  }
  
  public void start(EventLoop paramEventLoop) throws RobotCoreException {
    this.eventLoopManager.start(paramEventLoop);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robot\Robot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.robotcore.factory;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robot.Robot;

public class RobotFactory {
  public static Robot createRobot(EventLoopManager paramEventLoopManager) throws RobotCoreException {
    return new Robot(paramEventLoopManager);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\factory\RobotFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
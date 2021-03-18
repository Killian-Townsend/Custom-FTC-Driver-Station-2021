package com.qualcomm.robotcore.hardware;

import java.util.concurrent.BlockingQueue;

public interface I2cDeviceSynchReadHistory {
  BlockingQueue<TimestampedI2cData> getHistoryQueue();
  
  int getHistoryQueueCapacity();
  
  void setHistoryQueueCapacity(int paramInt);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchReadHistory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
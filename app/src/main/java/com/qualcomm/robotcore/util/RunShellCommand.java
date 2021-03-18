package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;

public class RunShellCommand {
  private static int BUFFER_SIZE = 524288;
  
  private boolean logging = false;
  
  private Process process = null;
  
  public static int getSpawnedProcessPid(String paramString1, String paramString2) {
    RunShellCommand runShellCommand = new RunShellCommand();
    boolean bool = false;
    String str = (runShellCommand.runCommand("ps", false)).output;
    String[] arrayOfString2 = str.split("\n");
    int j = arrayOfString2.length;
    int i = 0;
    while (true) {
      if (i < j) {
        String str1 = arrayOfString2[i];
        if (str1.contains(paramString2)) {
          paramString2 = str1.split("\\s+")[0];
          break;
        } 
        i++;
        continue;
      } 
      paramString2 = "invalid";
      break;
    } 
    String[] arrayOfString1 = str.split("\n");
    j = arrayOfString1.length;
    for (i = bool; i < j; i++) {
      String str1 = arrayOfString1[i];
      if (str1.contains(paramString1) && str1.contains(paramString2))
        return Integer.parseInt(str1.split("\\s+")[1]); 
    } 
    return -1;
  }
  
  public static void killSpawnedProcess(String paramString1, String paramString2) throws RobotCoreException {
    try {
      for (int i = getSpawnedProcessPid(paramString1, paramString2); i != -1; i = getSpawnedProcessPid(paramString1, paramString2)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Killing PID ");
        stringBuilder.append(i);
        RobotLog.v(stringBuilder.toString());
        (new RunShellCommand()).run(String.format("kill %d", new Object[] { Integer.valueOf(i) }));
      } 
      return;
    } catch (Exception exception) {
      throw new RobotCoreException(String.format("Failed to kill %s instances started by this app", new Object[] { paramString1 }));
    } 
  }
  
  private ProcessResult runCommand(String paramString, boolean paramBoolean) {
    byte b1;
    Process process1;
    Process process2;
    ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
    byte[] arrayOfByte = new byte[BUFFER_SIZE];
    String str1 = "";
    byte b2 = -1;
    if (paramBoolean) {
      byte b;
      b1 = b2;
    } else {
      b1 = b2;
      byte b = b2;
      process2.command(new String[] { "sh", "-c", paramString }).redirectErrorStream(true);
      b1 = b2;
      b = b2;
    } 
    process1.destroy();
    String str2 = paramString;
    byte b3 = b1;
    return new ProcessResult(b3, str2);
  }
  
  public void commitSeppuku() {
    Process process = this.process;
    if (process != null) {
      process.destroy();
      try {
        this.process.waitFor();
        return;
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      } 
    } 
  }
  
  public void enableLogging(boolean paramBoolean) {
    this.logging = paramBoolean;
  }
  
  public ProcessResult run(String paramString) {
    return runCommand(paramString, false);
  }
  
  public ProcessResult runAsRoot(String paramString) {
    return runCommand(paramString, true);
  }
  
  public static final class ProcessResult {
    private final String output;
    
    private final int returnCode;
    
    private ProcessResult(int param1Int, String param1String) {
      this.returnCode = param1Int;
      this.output = param1String;
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object != null) {
        if (getClass() != param1Object.getClass())
          return false; 
        ProcessResult processResult = (ProcessResult)param1Object;
        if (this.returnCode != processResult.returnCode)
          return false; 
        param1Object = this.output;
        String str = processResult.output;
        return (param1Object != null) ? param1Object.equals(str) : ((str == null));
      } 
      return false;
    }
    
    public String getOutput() {
      return this.output;
    }
    
    public int getReturnCode() {
      return this.returnCode;
    }
    
    public int hashCode() {
      byte b;
      int i = this.returnCode;
      String str = this.output;
      if (str != null) {
        b = str.hashCode();
      } else {
        b = 0;
      } 
      return i * 31 + b;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\RunShellCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
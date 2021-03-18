package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;

public class CommandList extends RobotCoreCommandList {
  public static final String CMD_ACTIVATE_CONFIGURATION = "CMD_ACTIVATE_CONFIGURATION";
  
  public static final String CMD_DELETE_CONFIGURATION = "CMD_DELETE_CONFIGURATION";
  
  public static final String CMD_DISCOVER_LYNX_MODULES = "CMD_DISCOVER_LYNX_MODULES";
  
  public static final String CMD_DISCOVER_LYNX_MODULES_RESP = "CMD_DISCOVER_LYNX_MODULES_RESP";
  
  public static final String CMD_INIT_OP_MODE = "CMD_INIT_OP_MODE";
  
  public static final String CMD_LYNX_ADDRESS_CHANGE = "CMD_LYNX_ADDRESS_CHANGE";
  
  public static final String CMD_REQUEST_CONFIGURATIONS = "CMD_REQUEST_CONFIGURATIONS";
  
  public static final String CMD_REQUEST_CONFIGURATIONS_RESP = "CMD_REQUEST_CONFIGURATIONS_RESP";
  
  public static final String CMD_REQUEST_CONFIGURATION_TEMPLATES = "CMD_REQUEST_CONFIGURATION_TEMPLATES";
  
  public static final String CMD_REQUEST_CONFIGURATION_TEMPLATES_RESP = "CMD_REQUEST_CONFIGURATION_TEMPLATES_RESP";
  
  public static final String CMD_REQUEST_PARTICULAR_CONFIGURATION = "CMD_REQUEST_PARTICULAR_CONFIGURATION";
  
  public static final String CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP = "CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP";
  
  public static final String CMD_REQUEST_REMEMBERED_GROUPS = "CMD_REQUEST_REMEMBERED_GROUPS";
  
  public static final String CMD_REQUEST_REMEMBERED_GROUPS_RESP = "CMD_REQUEST_REMEMBERED_GROUPS_RESP";
  
  public static final String CMD_RESTART_ROBOT = "CMD_RESTART_ROBOT";
  
  public static final String CMD_RUN_OP_MODE = "CMD_RUN_OP_MODE";
  
  public static final String CMD_SAVE_CONFIGURATION = "CMD_SAVE_CONFIGURATION";
  
  public static final String CMD_SCAN = "CMD_SCAN";
  
  public static final String CMD_SCAN_RESP = "CMD_SCAN_RESP";
  
  public static final String CMD_SET_MATCH_NUMBER = "CMD_SET_MATCH_NUMBER";
  
  public static final String CMD_START_DS_PROGRAM_AND_MANAGE = "CMD_START_DS_PROGRAM_AND_MANAGE";
  
  public static final String CMD_START_DS_PROGRAM_AND_MANAGE_RESP = "CMD_START_DS_PROGRAM_AND_MANAGE_RESP";
  
  public static class CmdPlaySound {
    public static final String Command = "CMD_PLAY_SOUND";
    
    public final String hashString;
    
    public final int loopControl;
    
    public final long msPresentationTime;
    
    public final float rate;
    
    public final float volume;
    
    public final boolean waitForNonLoopingSoundsToFinish;
    
    public CmdPlaySound(long param1Long, String param1String, SoundPlayer.PlaySoundParams param1PlaySoundParams) {
      this.msPresentationTime = param1Long;
      this.hashString = param1String;
      this.waitForNonLoopingSoundsToFinish = param1PlaySoundParams.waitForNonLoopingSoundsToFinish;
      this.volume = param1PlaySoundParams.volume;
      this.loopControl = param1PlaySoundParams.loopControl;
      this.rate = param1PlaySoundParams.rate;
    }
    
    public static CmdPlaySound deserialize(String param1String) {
      return (CmdPlaySound)SimpleGson.getInstance().fromJson(param1String, CmdPlaySound.class);
    }
    
    public SoundPlayer.PlaySoundParams getParams() {
      SoundPlayer.PlaySoundParams playSoundParams = new SoundPlayer.PlaySoundParams();
      playSoundParams.waitForNonLoopingSoundsToFinish = this.waitForNonLoopingSoundsToFinish;
      playSoundParams.volume = this.volume;
      playSoundParams.loopControl = this.loopControl;
      playSoundParams.rate = this.rate;
      return playSoundParams;
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class CmdRequestSound {
    public static final String Command = "CMD_REQUEST_SOUND";
    
    public final String hashString;
    
    public final int port;
    
    public CmdRequestSound(String param1String, int param1Int) {
      this.hashString = param1String;
      this.port = param1Int;
    }
    
    public static CmdRequestSound deserialize(String param1String) {
      return (CmdRequestSound)SimpleGson.getInstance().fromJson(param1String, CmdRequestSound.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class CmdStopPlayingSounds {
    public static final String Command = "CMD_STOP_PLAYING_SOUNDS";
    
    public final SoundPlayer.StopWhat stopWhat;
    
    public CmdStopPlayingSounds(SoundPlayer.StopWhat param1StopWhat) {
      this.stopWhat = param1StopWhat;
    }
    
    public static CmdStopPlayingSounds deserialize(String param1String) {
      return (CmdStopPlayingSounds)SimpleGson.getInstance().fromJson(param1String, CmdStopPlayingSounds.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class CmdVisuallyIdentify {
    public static final String Command = "CMD_VISUALLY_IDENTIFY";
    
    public final SerialNumber serialNumber;
    
    public final boolean shouldIdentify;
    
    public CmdVisuallyIdentify(SerialNumber param1SerialNumber, boolean param1Boolean) {
      this.serialNumber = param1SerialNumber;
      this.shouldIdentify = param1Boolean;
    }
    
    public static CmdVisuallyIdentify deserialize(String param1String) {
      return (CmdVisuallyIdentify)SimpleGson.getInstance().fromJson(param1String, CmdVisuallyIdentify.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class LynxAddressChangeRequest {
    ArrayList<AddressChange> modulesToChange;
    
    public static LynxAddressChangeRequest deserialize(String param1String) {
      return (LynxAddressChangeRequest)SimpleGson.getInstance().fromJson(param1String, LynxAddressChangeRequest.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
    
    public static class AddressChange {
      int newAddress;
      
      int oldAddress;
      
      SerialNumber serialNumber;
    }
  }
  
  public static class AddressChange {
    int newAddress;
    
    int oldAddress;
    
    SerialNumber serialNumber;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\CommandList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
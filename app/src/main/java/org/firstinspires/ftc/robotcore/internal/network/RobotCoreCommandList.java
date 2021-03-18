package org.firstinspires.ftc.robotcore.internal.network;

import android.util.Base64;
import com.qualcomm.robotcore.hardware.USBAccessibleLynxModule;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.File;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.ProgressParameters;

public class RobotCoreCommandList {
  public static final String CMD_CLEAR_REMEMBERED_GROUPS = "CMD_CLEAR_REMEMBERED_GROUPS";
  
  public static final String CMD_DISCONNECT_FROM_WIFI_DIRECT = "CMD_DISCONNECT_FROM_WIFI_DIRECT";
  
  public static final String CMD_DISMISS_ALL_DIALOGS = "CMD_DISMISS_ALL_DIALOGS";
  
  public static final String CMD_DISMISS_DIALOG = "CMD_DISMISS_DIALOG";
  
  public static final String CMD_DISMISS_PROGRESS = "CMD_DISMISS_PROGRESS";
  
  public static final String CMD_GET_CANDIDATE_LYNX_FIRMWARE_IMAGES = "CMD_GET_CANDIDATE_LYNX_FIRMWARE_IMAGES";
  
  public static final String CMD_GET_CANDIDATE_LYNX_FIRMWARE_IMAGES_RESP = "CMD_GET_CANDIDATE_LYNX_FIRMWARE_IMAGES_RESP";
  
  public static final String CMD_GET_USB_ACCESSIBLE_LYNX_MODULES = "CMD_GET_USB_ACCESSIBLE_LYNX_MODULES";
  
  public static final String CMD_GET_USB_ACCESSIBLE_LYNX_MODULES_RESP = "CMD_GET_USB_ACCESSIBLE_LYNX_MODULES_RESP";
  
  public static final String CMD_LYNX_FIRMWARE_UPDATE = "CMD_LYNX_FIRMWARE_UPDATE";
  
  public static final String CMD_LYNX_FIRMWARE_UPDATE_RESP = "CMD_LYNX_FIRMWARE_UPDATE_RESP";
  
  public static final String CMD_NOTIFY_ACTIVE_CONFIGURATION = "CMD_NOTIFY_ACTIVE_CONFIGURATION";
  
  public static final String CMD_NOTIFY_INIT_OP_MODE = "CMD_NOTIFY_INIT_OP_MODE";
  
  public static final String CMD_NOTIFY_OP_MODE_LIST = "CMD_NOTIFY_OP_MODE_LIST";
  
  public static final String CMD_NOTIFY_ROBOT_STATE = "CMD_NOTIFY_ROBOT_STATE";
  
  public static final String CMD_NOTIFY_RUN_OP_MODE = "CMD_NOTIFY_RUN_OP_MODE";
  
  public static final String CMD_NOTIFY_USER_DEVICE_LIST = "CMD_NOTIFY_USER_DEVICE_LIST";
  
  public static final String CMD_NOTIFY_WIFI_DIRECT_REMEMBERED_GROUPS_CHANGED = "CMD_NOTIFY_WIFI_DIRECT_REMEMBERED_GROUPS_CHANGED";
  
  public static final String CMD_RECEIVE_FRAME_BEGIN = "CMD_RECEIVE_FRAME_BEGIN";
  
  public static final String CMD_RECEIVE_FRAME_CHUNK = "CMD_RECEIVE_FRAME_CHUNK";
  
  public static final String CMD_REQUEST_ABOUT_INFO = "CMD_REQUEST_ABOUT_INFO";
  
  public static final String CMD_REQUEST_ABOUT_INFO_RESP = "CMD_REQUEST_ABOUT_INFO_RESP";
  
  public static final String CMD_REQUEST_FRAME = "CMD_REQUEST_FRAME";
  
  public static final String CMD_REQUEST_INSPECTION_REPORT = "CMD_REQUEST_INSPECTION_REPORT";
  
  public static final String CMD_REQUEST_INSPECTION_REPORT_RESP = "CMD_REQUEST_INSPECTION_REPORT_RESP";
  
  public static final String CMD_REQUEST_UI_STATE = "CMD_REQUEST_UI_STATE";
  
  public static final String CMD_ROBOT_CONTROLLER_PREFERENCE = "CMD_ROBOT_CONTROLLER_PREFERENCE";
  
  public static final String CMD_SET_TELEMETRY_DISPLAY_FORMAT = "CMD_SET_TELEM_DISPL_FORMAT";
  
  public static final String CMD_SHOW_DIALOG = "CMD_SHOW_DIALOG";
  
  public static final String CMD_SHOW_PROGRESS = "CMD_SHOW_PROGRESS";
  
  public static final String CMD_SHOW_TOAST = "CMD_SHOW_TOAST";
  
  public static final String CMD_STREAM_CHANGE = "CMD_STREAM_CHANGE";
  
  public static final String CMD_TEXT_TO_SPEECH = "CMD_TEXT_TO_SPEECH";
  
  public static final String CMD_VISUALLY_CONFIRM_WIFI_BAND_SWITCH = "CMD_VISUALLY_CONFIRM_WIFI_BAND_SWITCH";
  
  public static final String CMD_VISUALLY_CONFIRM_WIFI_RESET = "CMD_VISUALLY_CONFIRM_WIFI_RESET";
  
  public static class AboutInfo {
    public String appVersion;
    
    public String buildTime;
    
    public String libVersion;
    
    public String networkConnectionInfo;
    
    public String networkProtocolVersion;
    
    public String osVersion;
    
    public static AboutInfo deserialize(String param1String) {
      return (AboutInfo)SimpleGson.getInstance().fromJson(param1String, AboutInfo.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class CmdReceiveFrameBegin {
    private int frameNum;
    
    private int length;
    
    public CmdReceiveFrameBegin(int param1Int1, int param1Int2) {
      this.frameNum = param1Int1;
      this.length = param1Int2;
    }
    
    public static CmdReceiveFrameBegin deserialize(String param1String) {
      return (CmdReceiveFrameBegin)SimpleGson.getInstance().fromJson(param1String, CmdReceiveFrameBegin.class);
    }
    
    public int getFrameNum() {
      return this.frameNum;
    }
    
    public int getLength() {
      return this.length;
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class CmdReceiveFrameChunk {
    private int chunkNum;
    
    private transient byte[] data;
    
    private String encodedData;
    
    private int frameNum;
    
    public CmdReceiveFrameChunk(int param1Int1, int param1Int2, byte[] param1ArrayOfbyte, int param1Int3, int param1Int4) {
      this.frameNum = param1Int1;
      this.chunkNum = param1Int2;
      this.data = param1ArrayOfbyte;
      this.encodedData = Base64.encodeToString(param1ArrayOfbyte, param1Int3, param1Int4, 0);
    }
    
    public static CmdReceiveFrameChunk deserialize(String param1String) {
      CmdReceiveFrameChunk cmdReceiveFrameChunk = (CmdReceiveFrameChunk)SimpleGson.getInstance().fromJson(param1String, CmdReceiveFrameChunk.class);
      cmdReceiveFrameChunk.data = Base64.decode(cmdReceiveFrameChunk.encodedData, 0);
      return cmdReceiveFrameChunk;
    }
    
    public int getChunkNum() {
      return this.chunkNum;
    }
    
    public byte[] getData() {
      return this.data;
    }
    
    public int getFrameNum() {
      return this.frameNum;
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class CmdStreamChange {
    public boolean available;
    
    public static CmdStreamChange deserialize(String param1String) {
      CmdStreamChange cmdStreamChange = new CmdStreamChange();
      cmdStreamChange.available = Boolean.parseBoolean(param1String);
      return cmdStreamChange;
    }
    
    public String serialize() {
      return String.valueOf(this.available);
    }
  }
  
  public static class DismissDialog {
    public String uuidString;
    
    public DismissDialog(String param1String) {
      this.uuidString = param1String;
    }
    
    public static DismissDialog deserialize(String param1String) {
      return (DismissDialog)SimpleGson.getInstance().fromJson(param1String, DismissDialog.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class FWImage {
    public File file;
    
    public boolean isAsset;
    
    public FWImage(File param1File, boolean param1Boolean) {
      this.file = param1File;
      this.isAsset = param1Boolean;
    }
    
    public String getName() {
      return this.file.getName();
    }
  }
  
  public static class LynxFirmwareImagesResp {
    public ArrayList<RobotCoreCommandList.FWImage> firmwareImages = new ArrayList<RobotCoreCommandList.FWImage>();
    
    public File firstFolder = AppUtil.FIRST_FOLDER;
    
    public static LynxFirmwareImagesResp deserialize(String param1String) {
      return (LynxFirmwareImagesResp)SimpleGson.getInstance().fromJson(param1String, LynxFirmwareImagesResp.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class LynxFirmwareUpdate {
    public RobotCoreCommandList.FWImage firmwareImageFile;
    
    public String originatorId;
    
    public SerialNumber serialNumber;
    
    public static LynxFirmwareUpdate deserialize(String param1String) {
      return (LynxFirmwareUpdate)SimpleGson.getInstance().fromJson(param1String, LynxFirmwareUpdate.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class LynxFirmwareUpdateResp {
    public String errorMessage;
    
    public String originatorId;
    
    public boolean success;
    
    public static LynxFirmwareUpdateResp deserialize(String param1String) {
      return (LynxFirmwareUpdateResp)SimpleGson.getInstance().fromJson(param1String, LynxFirmwareUpdateResp.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class ShowDialog {
    public String message;
    
    public String title;
    
    public String uuidString;
    
    public static ShowDialog deserialize(String param1String) {
      return (ShowDialog)SimpleGson.getInstance().fromJson(param1String, ShowDialog.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class ShowProgress extends ProgressParameters {
    public String message;
    
    public static ShowProgress deserialize(String param1String) {
      return (ShowProgress)SimpleGson.getInstance().fromJson(param1String, ShowProgress.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class ShowToast {
    public int duration;
    
    public String message;
    
    public static ShowToast deserialize(String param1String) {
      return (ShowToast)SimpleGson.getInstance().fromJson(param1String, ShowToast.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class TextToSpeech {
    private String countryCode;
    
    private String languageCode;
    
    private String text;
    
    public TextToSpeech(String param1String1, String param1String2, String param1String3) {
      this.text = param1String1;
      if (param1String2 == null)
        param1String2 = ""; 
      this.languageCode = param1String2;
      if (param1String3 == null)
        param1String3 = ""; 
      this.countryCode = param1String3;
    }
    
    public static TextToSpeech deserialize(String param1String) {
      return (TextToSpeech)SimpleGson.getInstance().fromJson(param1String, TextToSpeech.class);
    }
    
    public String getCountryCode() {
      return this.countryCode;
    }
    
    public String getLanguageCode() {
      return this.languageCode;
    }
    
    public String getText() {
      return this.text;
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class USBAccessibleLynxModulesRequest {
    public boolean forFirmwareUpdate = false;
    
    public static USBAccessibleLynxModulesRequest deserialize(String param1String) {
      return (USBAccessibleLynxModulesRequest)SimpleGson.getInstance().fromJson(param1String, USBAccessibleLynxModulesRequest.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class USBAccessibleLynxModulesResp {
    public ArrayList<USBAccessibleLynxModule> modules = new ArrayList<USBAccessibleLynxModule>();
    
    public static USBAccessibleLynxModulesResp deserialize(String param1String) {
      return (USBAccessibleLynxModulesResp)SimpleGson.getInstance().fromJson(param1String, USBAccessibleLynxModulesResp.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\RobotCoreCommandList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
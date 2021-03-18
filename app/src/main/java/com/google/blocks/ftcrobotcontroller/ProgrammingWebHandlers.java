package com.google.blocks.ftcrobotcontroller;

import com.google.blocks.ftcrobotcontroller.hardware.HardwareUtil;
import com.google.blocks.ftcrobotcontroller.util.ClipboardUtil;
import com.google.blocks.ftcrobotcontroller.util.OfflineBlocksUtil;
import com.google.blocks.ftcrobotcontroller.util.ProjectsUtil;
import com.google.blocks.ftcrobotcontroller.util.SoundsUtil;
import com.qualcomm.robotcore.robocol.Command;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotserver.internal.programmingmode.ProgrammingMode;
import org.firstinspires.ftc.robotserver.internal.programmingmode.ProgrammingModeManager;
import org.firstinspires.ftc.robotserver.internal.webserver.MimeTypesUtil;
import org.firstinspires.ftc.robotserver.internal.webserver.NoCachingWebHandler;
import org.firstinspires.ftc.robotserver.internal.webserver.RobotControllerWebHandlers;
import org.firstinspires.ftc.robotserver.internal.webserver.RobotWebHandlerManager;
import org.firstinspires.ftc.robotserver.internal.webserver.SessionParametersGenerator;

public class ProgrammingWebHandlers implements ProgrammingMode {
  private static final String PARAM_BLK = "blk";
  
  private static final String PARAM_CLIPBOARD = "cb";
  
  private static final String PARAM_CONTENT = "content";
  
  private static final String PARAM_ENABLE = "enable";
  
  private static final String PARAM_JAVA = "java";
  
  private static final String PARAM_JS = "js";
  
  private static final String PARAM_NAME = "name";
  
  private static final String PARAM_NEW_NAME = "new_name";
  
  private static final String PARAM_SAMPLE_NAME = "sample";
  
  public static final String TAG = ProgrammingWebHandlers.class.getSimpleName();
  
  private static final String URI_COLORS = "/css/colors.less";
  
  private static final String URI_COPY_PROJECT = "/copy";
  
  private static final String URI_COPY_SOUND = "/copy_sound";
  
  private static final String URI_DELETE_PROJECTS = "/delete";
  
  private static final String URI_DELETE_SOUNDS = "/delete_sounds";
  
  private static final String URI_ENABLE_PROJECT = "/enable";
  
  private static final String URI_FETCH_BLK = "/fetch_blk";
  
  private static final String URI_FETCH_CLIPBOARD = "/fetch_cb";
  
  private static final String URI_FETCH_OFFLINE_BLOCKS_EDITOR = "/offline_blocks_editor";
  
  private static final String URI_FETCH_SOUND = "/fetch_sound";
  
  private static final String URI_FETCH_SOUND_TYPE = "/fetch_sound_type";
  
  private static final String URI_GET_BLOCKS_JAVA_CLASS_NAME = "/get_blocks_java_class_name";
  
  private static final String URI_GET_CONFIGURATION_NAME = "/get_config_name";
  
  private static final String URI_HARDWARE = "/hardware";
  
  private static final String URI_LIST_PROJECTS = "/list";
  
  private static final String URI_LIST_SAMPLES = "/samples";
  
  private static final String URI_LIST_SOUNDS = "/list_sounds";
  
  private static String URI_NAV_BLOCKS;
  
  private static String URI_NAV_BLOCKS_OLD = "/FtcProjects.html";
  
  private static String URI_NAV_ONBOTJAVA;
  
  private static final String URI_NEW_PROJECT = "/new";
  
  private static final String URI_RENAME_PROJECT = "/rename";
  
  private static final String URI_RENAME_SOUND = "/rename_sound";
  
  private static final String URI_RESTART_ROBOT = "/restart_robot";
  
  private static final String URI_SAVE_BLOCKS_JAVA = "/save_blocks_java";
  
  private static final String URI_SAVE_CLIPBOARD = "/savecb";
  
  private static final String URI_SAVE_PROJECT = "/save";
  
  private static final String URI_SAVE_SOUND = "/save_sound";
  
  private static final String URI_SERVER = "/server";
  
  private volatile ProgrammingModeManager programmingModeManager;
  
  static {
    URI_NAV_BLOCKS = "/FtcBlocksProjects.html";
    URI_NAV_ONBOTJAVA = "/java/editor.html";
  }
  
  private WebHandler decorateWithLogging(WebHandler paramWebHandler) {
    return this.programmingModeManager.decorate(false, paramWebHandler);
  }
  
  private WebHandler decorateWithParms(WebHandler paramWebHandler) {
    return (WebHandler)new SessionParametersGenerator(paramWebHandler);
  }
  
  static String getFirstNamedParameter(NanoHTTPD.IHTTPSession paramIHTTPSession, String paramString) {
    Map map = paramIHTTPSession.getParameters();
    return !map.containsKey(paramString) ? null : ((List<String>)map.get(paramString)).get(0);
  }
  
  public void register(ProgrammingModeManager paramProgrammingModeManager) {
    this.programmingModeManager = paramProgrammingModeManager;
    paramProgrammingModeManager.register(URI_NAV_BLOCKS_OLD, (WebHandler)new RobotControllerWebHandlers.Redirection("/"));
    paramProgrammingModeManager.register("/server", decorateWithLogging(new Server()));
    paramProgrammingModeManager.register("/hardware", decorateWithLogging(new Hardware()));
    paramProgrammingModeManager.register("/get_config_name", decorateWithLogging(decorateWithParms(new GetConfigurationName())));
    paramProgrammingModeManager.register("/offline_blocks_editor", decorateWithLogging(new FetchOfflineBlocksEditor()));
    paramProgrammingModeManager.register("/list", decorateWithLogging(new ListProjects()));
    paramProgrammingModeManager.register("/samples", decorateWithLogging(new ListSamples()));
    paramProgrammingModeManager.register("/fetch_blk", decorateWithLogging(decorateWithParms(new FetchBlockFile())));
    paramProgrammingModeManager.register("/new", decorateWithLogging(decorateWithParms(new NewProject())));
    paramProgrammingModeManager.register("/save", decorateWithLogging(decorateWithParms(new SaveProject())));
    paramProgrammingModeManager.register("/rename", decorateWithLogging(decorateWithParms(new RenameProject())));
    paramProgrammingModeManager.register("/copy", decorateWithLogging(decorateWithParms(new CopyProject())));
    paramProgrammingModeManager.register("/enable", decorateWithLogging(decorateWithParms(new EnableProject())));
    paramProgrammingModeManager.register("/delete", decorateWithLogging(decorateWithParms(new DeleteProjects())));
    paramProgrammingModeManager.register("/get_blocks_java_class_name", decorateWithLogging(decorateWithParms(new GetBlocksJavaClassName())));
    paramProgrammingModeManager.register("/save_blocks_java", decorateWithLogging(decorateWithParms(new SaveBlocksJava())));
    paramProgrammingModeManager.register("/savecb", decorateWithLogging(decorateWithParms(new SaveClipboard())));
    paramProgrammingModeManager.register("/fetch_cb", decorateWithLogging(new FetchClipboard()));
    paramProgrammingModeManager.register("/list_sounds", decorateWithLogging(new ListSounds()));
    paramProgrammingModeManager.register("/save_sound", decorateWithLogging(decorateWithParms(new SaveSound())));
    paramProgrammingModeManager.register("/fetch_sound", decorateWithLogging(decorateWithParms(new FetchSound())));
    paramProgrammingModeManager.register("/fetch_sound_type", decorateWithLogging(decorateWithParms(new FetchSoundType())));
    paramProgrammingModeManager.register("/rename_sound", decorateWithLogging(decorateWithParms(new RenameSound())));
    paramProgrammingModeManager.register("/copy_sound", decorateWithLogging(decorateWithParms(new CopySound())));
    paramProgrammingModeManager.register("/delete_sounds", decorateWithLogging(decorateWithParms(new DeleteSounds())));
    paramProgrammingModeManager.register("/restart_robot", decorateWithLogging(new RestartRobot()));
    paramProgrammingModeManager.register("/css/colors.less", decorateWithLogging(paramProgrammingModeManager.getRegisteredHandler("/css/colors.less")));
    paramProgrammingModeManager.register("/js/rc_config.js", (WebHandler)new RobotControllerConfiguration());
  }
  
  private static class CopyProject implements WebHandler {
    private CopyProject() {}
    
    private NanoHTTPD.Response copyProject(String param1String1, String param1String2) throws IOException {
      try {
        ProjectsUtil.copyProject(param1String1, param1String2);
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
      } catch (IOExceptionWithUserVisibleMessage iOExceptionWithUserVisibleMessage) {
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", iOExceptionWithUserVisibleMessage.getMessage());
      } 
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "new_name");
      return (str2 != null && str1 != null) ? copyProject(str2, str1) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name and new_name parameters are required");
    }
  }
  
  private static class CopySound implements WebHandler {
    private CopySound() {}
    
    private NanoHTTPD.Response copySound(String param1String1, String param1String2) throws IOException {
      SoundsUtil.copySound(param1String1, param1String2);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "new_name");
      return (str2 != null && str1 != null) ? copySound(str2, str1) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name and new_name parameters are required");
    }
  }
  
  private static class DeleteProjects implements WebHandler {
    private DeleteProjects() {}
    
    private NanoHTTPD.Response deleteProjects(String param1String) throws IOException {
      ProjectsUtil.deleteProjects(param1String.split("\\*"));
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      return (str != null) ? deleteProjects(str) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required");
    }
  }
  
  private static class DeleteSounds implements WebHandler {
    private DeleteSounds() {}
    
    private NanoHTTPD.Response deleteSounds(String param1String) {
      SoundsUtil.deleteSounds(param1String.split("\\*"));
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      return (str != null) ? deleteSounds(str) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required");
    }
  }
  
  private static class EnableProject implements WebHandler {
    private EnableProject() {}
    
    private NanoHTTPD.Response enableProject(String param1String, boolean param1Boolean) throws IOException {
      ProjectsUtil.enableProject(param1String, param1Boolean);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "enable");
      return (str2 != null && str1 != null) ? enableProject(str2, Boolean.parseBoolean(str1)) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name and new_name parameters are required");
    }
  }
  
  private static class FetchBlockFile implements WebHandler {
    private FetchBlockFile() {}
    
    private NanoHTTPD.Response fetchBlkFileContent(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String) throws IOException {
      param1String = ProjectsUtil.fetchBlkFileContent(param1String);
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", param1String));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      return (str != null) ? fetchBlkFileContent(param1IHTTPSession, str) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required");
    }
  }
  
  private static class FetchClipboard implements WebHandler {
    private FetchClipboard() {}
    
    private NanoHTTPD.Response fetchClipboardContent() throws IOException {
      String str = ClipboardUtil.fetchClipboardContent();
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", str);
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return fetchClipboardContent();
    }
  }
  
  private static class FetchOfflineBlocksEditor implements WebHandler {
    private FetchOfflineBlocksEditor() {}
    
    private NanoHTTPD.Response fetchOfflineBlocksEditor(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newChunkedResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/zip", OfflineBlocksUtil.fetchOfflineBlocksEditor()));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return fetchOfflineBlocksEditor(param1IHTTPSession);
    }
  }
  
  private static class FetchSound implements WebHandler {
    private FetchSound() {}
    
    private NanoHTTPD.Response fetchSoundFileContent(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String) throws IOException {
      param1String = SoundsUtil.fetchSoundFileContent(param1String);
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", param1String));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      return (str != null) ? fetchSoundFileContent(param1IHTTPSession, str) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required");
    }
  }
  
  private static class FetchSoundType implements WebHandler {
    private FetchSoundType() {}
    
    private NanoHTTPD.Response fetchSoundFileMimeType(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String) throws IOException {
      String str = MimeTypesUtil.determineMimeType(param1String);
      param1String = str;
      if (str == null)
        param1String = ""; 
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", param1String));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      return (str != null) ? fetchSoundFileMimeType(param1IHTTPSession, str) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required");
    }
  }
  
  private static class GetBlocksJavaClassName implements WebHandler {
    private GetBlocksJavaClassName() {}
    
    private NanoHTTPD.Response getBlocksJavaClassName(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String) throws IOException {
      param1String = ProjectsUtil.getBlocksJavaClassName(param1String);
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", param1String));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      return (str != null) ? getBlocksJavaClassName(param1IHTTPSession, str) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required");
    }
  }
  
  private static class GetConfigurationName implements WebHandler {
    private GetConfigurationName() {}
    
    private NanoHTTPD.Response getConfigurationName(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      String str = HardwareUtil.getConfigurationName();
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", str));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return getConfigurationName(param1IHTTPSession);
    }
  }
  
  private static class Hardware implements WebHandler {
    private Hardware() {}
    
    private NanoHTTPD.Response fetchJavaScriptForHardware(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      String str = HardwareUtil.fetchJavaScriptForHardware();
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/javascript", str));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return fetchJavaScriptForHardware(param1IHTTPSession);
    }
  }
  
  private static class ListProjects implements WebHandler {
    private ListProjects() {}
    
    private NanoHTTPD.Response fetchProjects(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      String str = ProjectsUtil.fetchProjectsWithBlocks();
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", str));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return fetchProjects(param1IHTTPSession);
    }
  }
  
  private static class ListSamples implements WebHandler {
    private ListSamples() {}
    
    private NanoHTTPD.Response fetchSamples(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      String str = ProjectsUtil.fetchSampleNames();
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", str));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return fetchSamples(param1IHTTPSession);
    }
  }
  
  private static class ListSounds implements WebHandler {
    private ListSounds() {}
    
    private NanoHTTPD.Response fetchSounds(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      String str = SoundsUtil.fetchSounds();
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", str));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return fetchSounds(param1IHTTPSession);
    }
  }
  
  private static class NewProject implements WebHandler {
    private NewProject() {}
    
    private NanoHTTPD.Response newProject(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String1, String param1String2) throws IOException {
      param1String1 = ProjectsUtil.newProject(param1String1, param1String2);
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", param1String1));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      return (str != null) ? newProject(param1IHTTPSession, str, ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "sample")) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required");
    }
  }
  
  private static class RenameProject implements WebHandler {
    private RenameProject() {}
    
    private NanoHTTPD.Response renameProject(String param1String1, String param1String2) throws IOException {
      ProjectsUtil.renameProject(param1String1, param1String2);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "new_name");
      return (str2 != null && str1 != null) ? renameProject(str2, str1) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name and new_name parameters are required");
    }
  }
  
  private static class RenameSound implements WebHandler {
    private RenameSound() {}
    
    private NanoHTTPD.Response renameSound(String param1String1, String param1String2) throws IOException {
      SoundsUtil.renameSound(param1String1, param1String2);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "new_name");
      return (str2 != null && str1 != null) ? renameSound(str2, str1) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name and new_name parameters are required");
    }
  }
  
  private static class RestartRobot implements WebHandler {
    private RestartRobot() {}
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      NetworkConnectionHandler.getInstance().injectReceivedCommand(new Command("CMD_RESTART_ROBOT"));
      return RobotWebHandlerManager.OK_RESPONSE;
    }
  }
  
  private static class RobotControllerConfiguration extends RobotControllerWebHandlers.RobotControllerConfiguration {
    private RobotControllerConfiguration() {}
    
    protected void appendVariables(StringBuilder param1StringBuilder) {
      super.appendVariables(param1StringBuilder);
      appendVariable(param1StringBuilder, "URI_NAV_BLOCKS", ProgrammingWebHandlers.URI_NAV_BLOCKS);
      appendVariable(param1StringBuilder, "URI_NAV_ONBOTJAVA", ProgrammingWebHandlers.URI_NAV_ONBOTJAVA);
    }
  }
  
  private static class SaveBlocksJava implements WebHandler {
    private SaveBlocksJava() {}
    
    private NanoHTTPD.Response saveBlocksJava(String param1String1, String param1String2) throws IOException {
      ProjectsUtil.saveBlocksJava(param1String1, param1String2);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "java");
      return (str2 != null && str1 != null) ? saveBlocksJava(str2, str1) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name and java parameters are required");
    }
  }
  
  private static class SaveClipboard implements WebHandler {
    private SaveClipboard() {}
    
    private NanoHTTPD.Response saveClipboardContent(String param1String) throws IOException {
      ClipboardUtil.saveClipboardContent(param1String);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "cb");
      return (str != null) ? saveClipboardContent(str) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: cb parameter is required");
    }
  }
  
  private static class SaveProject implements WebHandler {
    private SaveProject() {}
    
    private NanoHTTPD.Response saveProject(String param1String1, String param1String2, String param1String3) throws IOException {
      ProjectsUtil.saveProject(param1String1, param1String2, param1String3);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str3 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "blk");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "js");
      return (str2 != null && str3 != null && str1 != null) ? saveProject(str2, str3, str1) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name, blk, and js parameters are required");
    }
  }
  
  private static class SaveSound implements WebHandler {
    private SaveSound() {}
    
    private NanoHTTPD.Response saveSoundFile(String param1String1, String param1String2) throws IOException {
      SoundsUtil.saveSoundFile(param1String1, param1String2);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str2 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "name");
      String str1 = ProgrammingWebHandlers.getFirstNamedParameter(param1IHTTPSession, "content");
      return (str2 != null && str1 != null) ? saveSoundFile(str2, str1) : NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name and content parameters are required");
    }
  }
  
  private static class Server implements WebHandler {
    private Server() {}
    
    private NanoHTTPD.Response fetchJavaScriptForServer(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("var URI_HARDWARE = '");
      stringBuilder.append("/hardware");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_GET_CONFIGURATION_NAME = '");
      stringBuilder.append("/get_config_name");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_FETCH_OFFLINE_BLOCKS_EDITOR = '");
      stringBuilder.append("/offline_blocks_editor");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_LIST_PROJECTS = '");
      stringBuilder.append("/list");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_LIST_SAMPLES = '");
      stringBuilder.append("/samples");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_FETCH_BLK = '");
      stringBuilder.append("/fetch_blk");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_NEW_PROJECT = '");
      stringBuilder.append("/new");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_SAVE_PROJECT = '");
      stringBuilder.append("/save");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_RENAME_PROJECT = '");
      stringBuilder.append("/rename");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_COPY_PROJECT = '");
      stringBuilder.append("/copy");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_ENABLE_PROJECT = '");
      stringBuilder.append("/enable");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_DELETE_PROJECTS = '");
      stringBuilder.append("/delete");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_GET_BLOCKS_JAVA_CLASS_NAME = '");
      stringBuilder.append("/get_blocks_java_class_name");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_SAVE_BLOCKS_JAVA = '");
      stringBuilder.append("/save_blocks_java");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_SAVE_CLIPBOARD = '");
      stringBuilder.append("/savecb");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_FETCH_CLIPBOARD = '");
      stringBuilder.append("/fetch_cb");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_LIST_SOUNDS = '");
      stringBuilder.append("/list_sounds");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_SAVE_SOUND = '");
      stringBuilder.append("/save_sound");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_FETCH_SOUND = '");
      stringBuilder.append("/fetch_sound");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_FETCH_SOUND_TYPE = '");
      stringBuilder.append("/fetch_sound_type");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_RENAME_SOUND = '");
      stringBuilder.append("/rename_sound");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_COPY_SOUND = '");
      stringBuilder.append("/copy_sound");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_DELETE_SOUNDS = '");
      stringBuilder.append("/delete_sounds");
      stringBuilder.append("';\n");
      stringBuilder.append("var URI_RESTART_ROBOT = '");
      stringBuilder.append("/restart_robot");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_NAME = '");
      stringBuilder.append("name");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_NEW_NAME = '");
      stringBuilder.append("new_name");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_SAMPLE_NAME = '");
      stringBuilder.append("sample");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_BLK = '");
      stringBuilder.append("blk");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_JS = '");
      stringBuilder.append("js");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_JAVA = '");
      stringBuilder.append("java");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_ENABLE = '");
      stringBuilder.append("enable");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_CLIPBOARD = '");
      stringBuilder.append("cb");
      stringBuilder.append("';\n");
      stringBuilder.append("var PARAM_CONTENT = '");
      stringBuilder.append("content");
      stringBuilder.append("';\n");
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/javascript", stringBuilder.toString()));
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      return fetchJavaScriptForServer(param1IHTTPSession);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\ProgrammingWebHandlers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
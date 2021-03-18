package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.exception.DuplicateNameException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.WriteXMLFileHandler;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Dom2XmlPullBuilder;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class RobotConfigFileManager {
  public static final boolean DEBUG = false;
  
  public static final String FILE_EXT = ".xml";
  
  public static final String FILE_LIST_COMMAND_DELIMITER = ";";
  
  public static final String ROBOT_CONFIG_DESCRIPTION_GENERATE_XSLT = "RobotConfigDescriptionGenerate.xslt";
  
  public static final String ROBOT_CONFIG_TAXONOMY_XML = "RobotConfigTaxonomy.xml";
  
  public static final String TAG = "RobotConfigFileManager";
  
  private static Supplier<Collection<Integer>> xmlResourceIdSupplier;
  
  private static Supplier<Collection<Integer>> xmlResourceTemplateIdsSupplier;
  
  private Activity activity;
  
  private AppUtil appUtil;
  
  private Context context;
  
  private final int idActiveConfigHeader = R.id.idActiveConfigHeader;
  
  private final int idActiveConfigName = R.id.idActiveConfigName;
  
  private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  public final String noConfig;
  
  private SharedPreferences preferences;
  
  private Resources resources;
  
  private WriteXMLFileHandler writer;
  
  public RobotConfigFileManager() {
    this(null);
  }
  
  public RobotConfigFileManager(Activity paramActivity) {
    AppUtil appUtil = AppUtil.getInstance();
    this.appUtil = appUtil;
    this.activity = paramActivity;
    Application application = appUtil.getApplication();
    this.context = (Context)application;
    this.resources = application.getResources();
    this.writer = new WriteXMLFileHandler();
    this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    this.noConfig = this.context.getString(R.string.noCurrentConfigFile);
  }
  
  public static RobotConfigFile deserializeConfig(String paramString) {
    return (RobotConfigFile)SimpleGson.getInstance().fromJson(paramString, RobotConfigFile.class);
  }
  
  public static List<RobotConfigFile> deserializeXMLConfigList(String paramString) {
    Type type = (new TypeToken<Collection<RobotConfigFile>>() {
      
      }).getType();
    return (List<RobotConfigFile>)SimpleGson.getInstance().fromJson(paramString, type);
  }
  
  public static File getFullPath(String paramString) {
    paramString = withExtension(paramString);
    return new File(AppUtil.CONFIG_FILES_DIR, paramString);
  }
  
  public static String getRobotConfigTemplateAttribute() {
    return "FirstInspires-FTC-template";
  }
  
  public static String getRobotConfigTypeAttribute() {
    return "FirstInspires-FTC";
  }
  
  private Collection<Integer> getXmlResourceIds() {
    Supplier<Collection<Integer>> supplier = xmlResourceIdSupplier;
    return (supplier != null) ? (Collection<Integer>)supplier.get() : new ArrayList<Integer>();
  }
  
  private Collection<Integer> getXmlResourceTemplateIds() {
    Supplier<Collection<Integer>> supplier = xmlResourceTemplateIdsSupplier;
    return (supplier != null) ? (Collection<Integer>)supplier.get() : new ArrayList<Integer>();
  }
  
  public static String serializeConfig(RobotConfigFile paramRobotConfigFile) {
    return SimpleGson.getInstance().toJson(paramRobotConfigFile);
  }
  
  public static String serializeXMLConfigList(List<RobotConfigFile> paramList) {
    return SimpleGson.getInstance().toJson(paramList);
  }
  
  public static void setXmlResourceIdSupplier(Supplier<Collection<Integer>> paramSupplier) {
    xmlResourceIdSupplier = paramSupplier;
  }
  
  public static void setXmlResourceTemplateIdSupplier(Supplier<Collection<Integer>> paramSupplier) {
    xmlResourceTemplateIdsSupplier = paramSupplier;
  }
  
  public static File stripFileNameExtension(File paramFile) {
    return new File(paramFile.getParentFile(), stripFileNameExtension(paramFile.getName()));
  }
  
  public static String stripFileNameExtension(String paramString) {
    return paramString.replaceFirst("[.][^.]+$", "");
  }
  
  public static String withExtension(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(stripFileNameExtension(paramString));
    stringBuilder.append(".xml");
    return stringBuilder.toString();
  }
  
  protected void addChild(Document paramDocument, Element paramElement, String paramString1, String paramString2) {
    Element element = paramDocument.createElement(paramString1);
    element.setTextContent(paramString2);
    paramElement.appendChild(element);
  }
  
  public void changeHeaderBackground(int paramInt) {
    Activity activity = this.activity;
    if (activity != null) {
      View view1 = activity.findViewById(paramInt);
      View view2 = this.activity.findViewById(R.id.idActiveConfigHeader);
      if (view1 != null && view2 != null) {
        view2.setBackground(view1.getBackground());
        return;
      } 
    } else {
      RobotLog.ee("RobotConfigFileManager", "changeHeaderBackground called with null activity");
    } 
  }
  
  public void createConfigFolder() {
    boolean bool;
    File file = AppUtil.CONFIG_FILES_DIR;
    if (!file.exists()) {
      bool = file.mkdir();
    } else {
      bool = true;
    } 
    if (!bool) {
      RobotLog.ee("RobotConfigFileManager", "Can't create the Robot Config Files directory!");
      this.appUtil.showToast(UILocation.BOTH, this.context.getString(R.string.toastCantCreateRobotConfigFilesDir));
    } 
  }
  
  public RobotConfigFile getActiveConfig() {
    String str = this.context.getString(R.string.pref_hardware_config_filename);
    str = this.preferences.getString(str, null);
    return (str == null) ? RobotConfigFile.noConfig(this) : getConfigFromString(str);
  }
  
  public RobotConfigFile getActiveConfigAndUpdateUI() {
    RobotConfigFile robotConfigFile = getActiveConfig();
    updateActiveConfigHeader(robotConfigFile);
    return robotConfigFile;
  }
  
  public RobotConfigFile getConfigFromString(String paramString) {
    return RobotConfigFile.fromString(this, paramString);
  }
  
  public String getRobotConfigDescription(XmlPullParser paramXmlPullParser) {
    try {
      Source source1 = getSourceFromPullParser(paramXmlPullParser);
      Source source2 = getRobotConfigDescriptionTransform();
      StringWriter stringWriter = new StringWriter();
      StreamResult streamResult = new StreamResult(stringWriter);
      TransformerFactory.newInstance().newTransformer(source2).transform(source1, streamResult);
      return stringWriter.toString().trim();
    } catch (IOException iOException) {
    
    } catch (TransformerException transformerException) {
    
    } catch (XmlPullParserException xmlPullParserException) {}
    RobotLog.logStackTrace((Throwable)xmlPullParserException);
    return this.context.getString(R.string.templateConfigureNoDescriptionAvailable);
  }
  
  protected Source getRobotConfigDescriptionTransform() throws XmlPullParserException, IOException, TransformerConfigurationException, TransformerException {
    XmlPullParser xmlPullParser = ReadXMLFileHandler.xmlPullParserFromReader(new InputStreamReader(this.context.getAssets().open("RobotConfigTaxonomy.xml")));
    Element element = (new Dom2XmlPullBuilder()).parseSubTree(xmlPullParser);
    element.getOwnerDocument();
    DOMSource dOMSource = new DOMSource(element);
    StreamSource streamSource = new StreamSource(this.context.getAssets().open("RobotConfigDescriptionGenerate.xslt"));
    StringWriter stringWriter = new StringWriter();
    StreamResult streamResult = new StreamResult(stringWriter);
    TransformerFactory.newInstance().newTransformer(streamSource).transform(dOMSource, streamResult);
    return new StreamSource(new StringReader(stringWriter.toString().trim()));
  }
  
  protected Source getSourceFromPullParser(XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    return new DOMSource((new Dom2XmlPullBuilder()).parseSubTree(paramXmlPullParser));
  }
  
  public ArrayList<RobotConfigFile> getXMLFiles() {
    File[] arrayOfFile = AppUtil.CONFIG_FILES_DIR.listFiles();
    ArrayList<RobotConfigFile> arrayList = new ArrayList();
    int j = arrayOfFile.length;
    int i;
    for (i = 0; i < j; i++) {
      File file = arrayOfFile[i];
      if (file.isFile()) {
        String str = file.getName();
        if (Pattern.compile("(?i).xml").matcher(str).find())
          arrayList.add(new RobotConfigFile(this, stripFileNameExtension(str))); 
      } 
    } 
    Iterator<Integer> iterator = getXmlResourceIds().iterator();
    while (iterator.hasNext()) {
      i = ((Integer)iterator.next()).intValue();
      RobotConfigFile robotConfigFile = new RobotConfigFile(RobotConfigResFilter.getRootAttribute(this.resources.getXml(i), "Robot", "name", this.resources.getResourceEntryName(i)), i);
      if (!robotConfigFile.containedIn(arrayList))
        arrayList.add(robotConfigFile); 
    } 
    return arrayList;
  }
  
  public ArrayList<RobotConfigFile> getXMLTemplates() {
    ArrayList<RobotConfigFile> arrayList = new ArrayList();
    Iterator<Integer> iterator = getXmlResourceTemplateIds().iterator();
    while (iterator.hasNext()) {
      int i = ((Integer)iterator.next()).intValue();
      RobotConfigFile robotConfigFile = new RobotConfigFile(RobotConfigResFilter.getRootAttribute(this.resources.getXml(i), "Robot", "name", this.resources.getResourceEntryName(i)), i);
      if (!robotConfigFile.containedIn(arrayList))
        arrayList.add(robotConfigFile); 
    } 
    return arrayList;
  }
  
  public ConfigNameCheckResult isPlausibleConfigName(RobotConfigFile paramRobotConfigFile, String paramString, List<RobotConfigFile> paramList) {
    if (!paramString.equals(paramString.trim()))
      return new ConfigNameCheckResult(this.context.getString(R.string.configNameWhitespace)); 
    if (paramString.length() == 0)
      return new ConfigNameCheckResult(this.context.getString(R.string.configNameEmpty)); 
    if (!(new File(paramString)).getName().equals(paramString))
      return new ConfigNameCheckResult(this.context.getString(R.string.configNameIllegalCharacters)); 
    char[] arrayOfChar = paramString.toCharArray();
    int j = arrayOfChar.length;
    int i;
    for (i = 0; i < j; i++) {
      if ("?:\"*|/\\<>".indexOf(arrayOfChar[i]) != -1)
        return new ConfigNameCheckResult(this.context.getString(R.string.configNameIllegalCharacters)); 
    } 
    if (paramString.equalsIgnoreCase(this.noConfig))
      return new ConfigNameCheckResult(this.context.getString(R.string.configNameReserved)); 
    if (paramString.equalsIgnoreCase(paramRobotConfigFile.getName()))
      return paramRobotConfigFile.isReadOnly() ? new ConfigNameCheckResult(this.context.getString(R.string.configNameReadOnly)) : new ConfigNameCheckResult(true); 
    Iterator<RobotConfigFile> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      if (paramString.equalsIgnoreCase(((RobotConfigFile)iterator.next()).getName()))
        return new ConfigNameCheckResult(this.context.getString(R.string.configNameExists)); 
    } 
    return new ConfigNameCheckResult(true);
  }
  
  public void sendActiveConfigToDriverStation() {
    String str = getActiveConfig().toString();
    this.networkConnectionHandler.sendCommand(new Command("CMD_NOTIFY_ACTIVE_CONFIGURATION", str));
  }
  
  public void sendRobotControllerActiveConfigAndUpdateUI(RobotConfigFile paramRobotConfigFile) {
    this.networkConnectionHandler.sendCommand(new Command("CMD_ACTIVATE_CONFIGURATION", paramRobotConfigFile.toString()));
  }
  
  public void setActiveConfig(RobotConfigFile paramRobotConfigFile) {
    String str = SimpleGson.getInstance().toJson(paramRobotConfigFile);
    SharedPreferences.Editor editor = this.preferences.edit();
    editor.putString(this.context.getString(R.string.pref_hardware_config_filename), str);
    editor.apply();
  }
  
  public void setActiveConfig(boolean paramBoolean, RobotConfigFile paramRobotConfigFile) {
    if (paramBoolean) {
      sendRobotControllerActiveConfigAndUpdateUI(paramRobotConfigFile);
      return;
    } 
    setActiveConfig(paramRobotConfigFile);
    sendActiveConfigToDriverStation();
  }
  
  public void setActiveConfigAndUpdateUI(RobotConfigFile paramRobotConfigFile) {
    setActiveConfig(paramRobotConfigFile);
    updateActiveConfigHeader(paramRobotConfigFile);
  }
  
  public void setActiveConfigAndUpdateUI(boolean paramBoolean, RobotConfigFile paramRobotConfigFile) {
    setActiveConfig(paramBoolean, paramRobotConfigFile);
    updateActiveConfigHeader(paramRobotConfigFile);
  }
  
  public String toXml(RobotConfigMap paramRobotConfigMap) {
    return toXml(paramRobotConfigMap.map);
  }
  
  public String toXml(Map<SerialNumber, ControllerConfiguration> paramMap) {
    ArrayList arrayList = new ArrayList();
    arrayList.addAll(paramMap.values());
    try {
      return this.writer.toXml(arrayList, "type", getRobotConfigTypeAttribute());
    } catch (DuplicateNameException duplicateNameException) {
      this.appUtil.showToast(UILocation.BOTH, String.format(this.context.getString(R.string.toastDuplicateName), new Object[] { duplicateNameException.getMessage() }));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Found ");
      stringBuilder.append(duplicateNameException.getMessage());
      RobotLog.ee("RobotConfigFileManager", stringBuilder.toString());
      return null;
    } catch (RuntimeException runtimeException) {
      RobotLog.ee("RobotConfigFileManager", runtimeException, "exception while writing XML");
      return null;
    } 
  }
  
  public void updateActiveConfigHeader(RobotConfigFile paramRobotConfigFile) {
    updateActiveConfigHeader(paramRobotConfigFile.getName(), paramRobotConfigFile.isDirty());
  }
  
  public void updateActiveConfigHeader(final String fileNameIn, final boolean dirty) {
    if (this.activity != null) {
      this.appUtil.runOnUiThread(new Runnable() {
            public void run() {
              String str2 = RobotConfigFileManager.stripFileNameExtension(fileNameIn).trim();
              String str1 = str2;
              if (str2.isEmpty())
                str1 = RobotConfigFileManager.this.noConfig; 
              str2 = str1;
              if (dirty)
                str2 = String.format(RobotConfigFileManager.this.context.getString(R.string.configDirtyLabel), new Object[] { str1 }); 
              TextView textView = (TextView)RobotConfigFileManager.this.activity.findViewById(RobotConfigFileManager.this.idActiveConfigName);
              if (textView != null) {
                textView.setText(str2);
              } else {
                RobotLog.ee("RobotConfigFileManager", "unable to find header text 0x%08x", new Object[] { Integer.valueOf(RobotConfigFileManager.access$100(this.this$0)) });
              } 
              if (!dirty && str2.equalsIgnoreCase(RobotConfigFileManager.this.noConfig)) {
                RobotConfigFileManager.this.changeHeaderBackground(R.id.backgroundLightHolder);
                return;
              } 
              if (dirty) {
                RobotConfigFileManager.this.changeHeaderBackground(R.id.backgroundDarkGrayHolder);
                return;
              } 
              RobotConfigFileManager.this.changeHeaderBackground(R.id.backgroundMediumHolder);
            }
          });
      return;
    } 
    RobotLog.ee("RobotConfigFileManager", "updateActiveConfigHeader called with null activity");
  }
  
  public void writeToFile(RobotConfigFile paramRobotConfigFile, boolean paramBoolean, String paramString) throws RobotCoreException, IOException {
    boolean bool = paramRobotConfigFile.isDirty();
    paramRobotConfigFile.markClean();
    if (paramBoolean) {
      try {
        writeToRobotController(paramRobotConfigFile, paramString);
        return;
      } catch (RobotCoreException robotCoreException) {
      
      } catch (IOException iOException) {
      
      } catch (RuntimeException runtimeException) {}
    } else {
      writeXMLToFile(withExtension(paramRobotConfigFile.getName()), (String)runtimeException);
      return;
    } 
    if (bool)
      paramRobotConfigFile.markDirty(); 
    throw runtimeException;
  }
  
  void writeToRobotController(RobotConfigFile paramRobotConfigFile, String paramString) {
    NetworkConnectionHandler networkConnectionHandler = this.networkConnectionHandler;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramRobotConfigFile.toString());
    stringBuilder.append(";");
    stringBuilder.append(paramString);
    networkConnectionHandler.sendCommand(new Command("CMD_SAVE_CONFIGURATION", stringBuilder.toString()));
  }
  
  void writeXMLToFile(String paramString1, String paramString2) throws RobotCoreException, IOException {
    this.writer.writeToFile(paramString2, AppUtil.CONFIG_FILES_DIR, paramString1);
  }
  
  public static class ConfigNameCheckResult {
    public String errorFormat = null;
    
    public boolean success = true;
    
    public ConfigNameCheckResult(String param1String) {
      this.success = false;
      this.errorFormat = param1String;
    }
    
    public ConfigNameCheckResult(boolean param1Boolean) {
      this.success = param1Boolean;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\RobotConfigFileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
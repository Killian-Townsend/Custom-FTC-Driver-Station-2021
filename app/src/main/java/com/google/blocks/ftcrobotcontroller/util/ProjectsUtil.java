package com.google.blocks.ftcrobotcontroller.util;

import android.content.res.AssetManager;
import android.text.Html;
import android.util.Xml;
import com.google.blocks.ftcrobotcontroller.IOExceptionWithUserVisibleMessage;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItemMap;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareType;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareUtil;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.ThrowingCallable;
import org.firstinspires.ftc.robotcore.internal.opmode.OnBotJavaHelper;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class ProjectsUtil {
  private static final String BLOCKS_SAMPLES_PATH = "blocks/samples";
  
  private static final String DEFAULT_BLOCKS_SAMPLE_NAME = "default";
  
  private static final OpModeMeta.Flavor DEFAULT_FLAVOR = OpModeMeta.Flavor.TELEOP;
  
  public static final String TAG = "ProjectsUtil";
  
  public static final String VALID_PROJECT_REGEX = "^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$";
  
  private static final String XML_ATTRIBUTE_FLAVOR = "flavor";
  
  private static final String XML_ATTRIBUTE_GROUP = "group";
  
  private static final String XML_ATTRIBUTE_VALUE = "value";
  
  private static final String XML_END_TAG = "</xml>";
  
  private static final String XML_TAG_ENABLED = "Enabled";
  
  private static final String XML_TAG_EXTRA = "Extra";
  
  private static final String XML_TAG_OP_MODE_META = "OpModeMeta";
  
  public static void copyProject(final String oldProjectName, final String newProjectName) throws IOException {
    if (isValidProjectName(oldProjectName) && isValidProjectName(newProjectName)) {
      ProjectsLockManager.lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
            public Void call() throws IOException {
              AppUtil.getInstance().ensureDirectoryExists(AppUtil.BLOCK_OPMODES_DIR, false);
              File file1 = AppUtil.BLOCK_OPMODES_DIR;
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append(oldProjectName);
              stringBuilder1.append(".blk");
              file1 = new File(file1, stringBuilder1.toString());
              File file2 = AppUtil.BLOCK_OPMODES_DIR;
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append(newProjectName);
              stringBuilder2.append(".blk");
              FileUtil.copyFile(file1, new File(file2, stringBuilder2.toString()));
              try {
                file1 = AppUtil.BLOCK_OPMODES_DIR;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(oldProjectName);
                stringBuilder.append(".js");
                file1 = new File(file1, stringBuilder.toString());
                File file = AppUtil.BLOCK_OPMODES_DIR;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(newProjectName);
                stringBuilder2.append(".js");
                FileUtil.copyFile(file1, new File(file, stringBuilder2.toString()));
                return null;
              } catch (IOException iOException) {
                throw new IOExceptionWithUserVisibleMessage("The blocks project was successfully copied, but the new op mode cannot be run until it is saved in the blocks editor.");
              } 
            }
          });
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  private static OpModeMeta createOpModeMeta(String paramString1, String paramString2) {
    OpModeMeta.Flavor flavor3 = DEFAULT_FLAVOR;
    String str1 = "";
    OpModeMeta.Flavor flavor2 = flavor3;
    String str2 = str1;
    OpModeMeta.Flavor flavor1 = flavor3;
    String str3 = str1;
    try {
      XmlPullParser xmlPullParser = Xml.newPullParser();
      flavor2 = flavor3;
      str2 = str1;
      flavor1 = flavor3;
      str3 = str1;
      xmlPullParser.setInput(new StringReader(removeNewLines(paramString2)));
      flavor2 = flavor3;
      str2 = str1;
      flavor1 = flavor3;
      str3 = str1;
      int i = xmlPullParser.getEventType();
      OpModeMeta.Flavor flavor = flavor3;
      label42: while (true) {
        flavor1 = flavor;
        str2 = str1;
        if (i != 1) {
          flavor3 = flavor;
          String str = str1;
          if (i == 2) {
            flavor3 = flavor;
            str = str1;
            flavor2 = flavor;
            str2 = str1;
            flavor1 = flavor;
            str3 = str1;
            if (xmlPullParser.getName().equals("OpModeMeta")) {
              i = 0;
              while (true) {
                flavor3 = flavor;
                str = str1;
                flavor2 = flavor;
                str2 = str1;
                flavor1 = flavor;
                str3 = str1;
                if (i < xmlPullParser.getAttributeCount()) {
                  flavor2 = flavor;
                  str2 = str1;
                  flavor1 = flavor;
                  str3 = str1;
                  String str5 = xmlPullParser.getAttributeName(i);
                  flavor2 = flavor;
                  str2 = str1;
                  flavor1 = flavor;
                  str3 = str1;
                  String str4 = xmlPullParser.getAttributeValue(i);
                  flavor2 = flavor;
                  str2 = str1;
                  flavor1 = flavor;
                  str3 = str1;
                  if (str5.equals("flavor")) {
                    flavor2 = flavor;
                    str2 = str1;
                    flavor1 = flavor;
                    str3 = str1;
                    flavor3 = OpModeMeta.Flavor.valueOf(str4.toUpperCase(Locale.ENGLISH));
                    str = str1;
                  } else {
                    flavor2 = flavor;
                    str2 = str1;
                    flavor1 = flavor;
                    str3 = str1;
                    flavor3 = flavor;
                    str = str1;
                    if (str5.equals("group")) {
                      flavor2 = flavor;
                      str2 = str1;
                      flavor1 = flavor;
                      str3 = str1;
                      flavor3 = flavor;
                      str = str1;
                      if (!str4.isEmpty()) {
                        flavor2 = flavor;
                        str2 = str1;
                        flavor1 = flavor;
                        str3 = str1;
                        flavor3 = flavor;
                        str = str1;
                        if (!str4.equals("$$$$$$$")) {
                          str = str4;
                          flavor3 = flavor;
                        } 
                      } 
                    } 
                  } 
                } else {
                  flavor2 = flavor3;
                  str2 = str;
                  flavor1 = flavor3;
                  str3 = str;
                  i = xmlPullParser.next();
                  flavor = flavor3;
                  str1 = str;
                  continue label42;
                } 
                i++;
                flavor = flavor3;
                str1 = str;
              } 
              break;
            } 
            continue;
          } 
          continue;
        } 
        return new OpModeMeta(paramString1, flavor1, str2);
      } 
    } catch (IOException iOException) {
      str2 = str3;
    } catch (XmlPullParserException xmlPullParserException) {
      flavor1 = flavor2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ProjectsUtil.createOpmodeMeta(\"");
    stringBuilder.append(paramString1);
    stringBuilder.append("\", ...) - failed to parse xml.");
    RobotLog.e(stringBuilder.toString());
    RobotLog.logStackTrace((Throwable)XmlPullParserException);
    return new OpModeMeta(paramString1, flavor1, str2);
  }
  
  public static Boolean deleteProjects(final String[] projectNames) {
    return ProjectsLockManager.<Boolean>lockProjectsWhile(new Supplier<Boolean>() {
          public Boolean get() {
            String[] arrayOfString = projectNames;
            int j = arrayOfString.length;
            int i = 0;
            while (i < j) {
              if (ProjectsUtil.isValidProjectName(arrayOfString[i])) {
                i++;
                continue;
              } 
              throw new IllegalArgumentException();
            } 
            arrayOfString = projectNames;
            j = arrayOfString.length;
            boolean bool = true;
            for (i = 0; i < j; i++) {
              String str = arrayOfString[i];
              File file = AppUtil.BLOCK_OPMODES_DIR;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(str);
              stringBuilder.append(".js");
              file = new File(file, stringBuilder.toString());
              boolean bool1 = bool;
              if (file.exists()) {
                bool1 = bool;
                if (!file.delete())
                  bool1 = false; 
              } 
              bool = bool1;
              if (bool1) {
                file = AppUtil.BLOCK_OPMODES_DIR;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(".blk");
                File file1 = new File(file, stringBuilder.toString());
                bool = bool1;
                if (file1.exists()) {
                  bool = bool1;
                  if (!file1.delete())
                    bool = false; 
                } 
              } 
            } 
            return Boolean.valueOf(bool);
          }
        });
  }
  
  public static void enableProject(final String projectName, final boolean enable) throws IOException {
    if (isValidProjectName(projectName)) {
      ProjectsLockManager.lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
            public Void call() throws IOException {
              File file1 = AppUtil.BLOCK_OPMODES_DIR;
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append(projectName);
              stringBuilder2.append(".blk");
              File file2 = new File(file1, stringBuilder2.toString());
              String str = FileUtil.readFile(file2);
              int i = str.indexOf("</xml>");
              if (i != -1) {
                i += 6;
                String str1 = str.substring(0, i);
                str = str.substring(i);
                OpModeMeta opModeMeta = ProjectsUtil.createOpModeMeta(projectName, str);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str1);
                stringBuilder.append(ProjectsUtil.formatExtraXml(opModeMeta.flavor, opModeMeta.group, enable));
                String str2 = stringBuilder.toString();
                if (file2.exists()) {
                  long l = System.currentTimeMillis();
                  File file = AppUtil.BLOCK_OPMODES_DIR;
                  stringBuilder = new StringBuilder();
                  stringBuilder.append("backup_");
                  stringBuilder.append(l);
                  stringBuilder.append("_");
                  stringBuilder.append(projectName);
                  stringBuilder.append(".blk");
                  file = new File(file, stringBuilder.toString());
                  FileUtil.copyFile(file2, file);
                } else {
                  str1 = null;
                } 
                FileUtil.writeFile(file2, str2);
                if (str1 != null)
                  str1.delete(); 
                return null;
              } 
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("File ");
              stringBuilder1.append(file2.getName());
              stringBuilder1.append(" is empty or corrupt.");
              throw new CorruptFileException(stringBuilder1.toString());
            }
          });
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public static String escapeDoubleQuotes(String paramString) {
    return paramString.replace("\"", "\\\"");
  }
  
  public static String escapeSingleQuotes(String paramString) {
    return paramString.replace("'", "\\'");
  }
  
  public static String fetchBlkFileContent(String paramString) throws IOException {
    if (isValidProjectName(paramString)) {
      String str;
      File file = AppUtil.BLOCK_OPMODES_DIR;
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(paramString);
      stringBuilder2.append(".blk");
      file = new File(file, stringBuilder2.toString());
      paramString = FileUtil.readFile(file);
      int i = paramString.indexOf("</xml>");
      if (i != -1) {
        String str1;
        i += 6;
        String str3 = paramString.substring(0, i);
        str = paramString.substring(i);
        String str2 = upgradeBlocks(str3, HardwareItemMap.newHardwareItemMap());
        if (!str2.equals(str3)) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str2);
          stringBuilder.append(str);
          str1 = stringBuilder.toString();
        } 
        return str1;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("File ");
      stringBuilder1.append(str.getName());
      stringBuilder1.append(" is empty or corrupt.");
      throw new CorruptFileException(stringBuilder1.toString());
    } 
    throw new IllegalArgumentException();
  }
  
  public static List<OpModeMeta> fetchEnabledProjectsWithJavaScript() {
    return ProjectsLockManager.<List<OpModeMeta>>lockProjectsWhile(new Supplier<List<OpModeMeta>>() {
          public List<OpModeMeta> get() {
            String[] arrayOfString = AppUtil.BLOCK_OPMODES_DIR.list(new FilenameFilter() {
                  public boolean accept(File param2File, String param2String) {
                    return param2String.endsWith(".js") ? ProjectsUtil.isValidProjectName(param2String.substring(0, param2String.length() - 3)) : false;
                  }
                });
            ArrayList<OpModeMeta> arrayList = new ArrayList();
            if (arrayOfString != null) {
              int j = arrayOfString.length;
              for (int i = 0; i < j; i++) {
                String str = arrayOfString[i];
                OpModeMeta opModeMeta = ProjectsUtil.fetchOpModeMeta(str.substring(0, str.length() - 3));
                if (opModeMeta != null)
                  arrayList.add(opModeMeta); 
              } 
            } 
            return arrayList;
          }
        });
  }
  
  public static String fetchJsFileContent(String paramString) throws IOException {
    if (isValidProjectName(paramString)) {
      File file = AppUtil.BLOCK_OPMODES_DIR;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".js");
      return FileUtil.readFile(new File(file, stringBuilder.toString()));
    } 
    throw new IllegalArgumentException();
  }
  
  private static OpModeMeta fetchOpModeMeta(String paramString) {
    if (isValidProjectName(paramString))
      try {
        String str1;
        File file = AppUtil.BLOCK_OPMODES_DIR;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(paramString);
        stringBuilder2.append(".blk");
        file = new File(file, stringBuilder2.toString());
        String str2 = FileUtil.readFile(file);
        int i = str2.indexOf("</xml>");
        if (i != -1) {
          str1 = str2.substring(i + 6);
          return !isProjectEnabled(paramString, str1) ? null : createOpModeMeta(paramString, str1);
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("File ");
        stringBuilder1.append(str1.getName());
        stringBuilder1.append(" is empty or corrupt.");
        throw new CorruptFileException(stringBuilder1.toString());
      } catch (IOException iOException) {
        if (!paramString.startsWith("backup_")) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("ProjectsUtil.fetchOpModeMeta(\"");
          stringBuilder.append(paramString);
          stringBuilder.append("\") - failed.");
          RobotLog.e(stringBuilder.toString());
          RobotLog.logStackTrace(iOException);
        } 
        return null;
      }  
    throw new IllegalArgumentException();
  }
  
  public static void fetchProjectsForOfflineBlocksEditor(final List<OfflineBlocksProject> offlineBlocksProjects) throws IOException {
    ProjectsLockManager.lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
          public Void call() throws IOException {
            File[] arrayOfFile = AppUtil.BLOCK_OPMODES_DIR.listFiles(new FilenameFilter() {
                  public boolean accept(File param2File, String param2String) {
                    return param2String.endsWith(".blk") ? ProjectsUtil.isValidProjectName(param2String.substring(0, param2String.length() - 4)) : false;
                  }
                });
            if (arrayOfFile != null) {
              int j = arrayOfFile.length;
              for (int i = 0; i < j; i++) {
                File file = arrayOfFile[i];
                String str1 = file.getName();
                String str2 = str1.substring(0, str1.length() - 4);
                String str3 = ProjectsUtil.fetchBlkFileContent(str2);
                int k = str3.indexOf("</xml>");
                if (k != -1) {
                  String str = str3.substring(k + 6);
                  offlineBlocksProjects.add(new OfflineBlocksProject(str1, str3, str2, file.lastModified(), ProjectsUtil.isProjectEnabled(str2, str)));
                } 
              } 
            } 
            return null;
          }
        });
  }
  
  public static String fetchProjectsWithBlocks() {
    return ProjectsLockManager.<String>lockProjectsWhile(new Supplier<String>() {
          public String get() {
            File[] arrayOfFile = AppUtil.BLOCK_OPMODES_DIR.listFiles(new FilenameFilter() {
                  public boolean accept(File param2File, String param2String) {
                    return param2String.endsWith(".blk") ? ProjectsUtil.isValidProjectName(param2String.substring(0, param2String.length() - 4)) : false;
                  }
                });
            if (arrayOfFile != null) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("[");
              int j = arrayOfFile.length;
              String str = "";
              for (int i = 0; i < j; i++) {
                File file = arrayOfFile[i];
                String str1 = file.getName();
                str1 = str1.substring(0, str1.length() - 4);
                try {
                  boolean bool = ProjectsUtil.isProjectEnabled(str1);
                  stringBuilder.append(str);
                  stringBuilder.append("{");
                  stringBuilder.append("\"name\":\"");
                  stringBuilder.append(ProjectsUtil.escapeDoubleQuotes(str1));
                  stringBuilder.append("\", ");
                  stringBuilder.append("\"escapedName\":\"");
                  stringBuilder.append(ProjectsUtil.escapeDoubleQuotes(Html.escapeHtml(str1)));
                  stringBuilder.append("\", ");
                  stringBuilder.append("\"dateModifiedMillis\":");
                  stringBuilder.append(file.lastModified());
                  stringBuilder.append(", ");
                  stringBuilder.append("\"enabled\":");
                  stringBuilder.append(bool);
                  stringBuilder.append("}");
                  str = ",";
                } catch (IOException iOException) {
                  StringBuilder stringBuilder1 = new StringBuilder();
                  stringBuilder1.append("ProjectsUtil.fetchProjectsWithBlocks() - problem with project ");
                  stringBuilder1.append(str1);
                  RobotLog.e(stringBuilder1.toString());
                  RobotLog.logStackTrace(iOException);
                } 
              } 
              stringBuilder.append("]");
              return stringBuilder.toString();
            } 
            return "[]";
          }
        });
  }
  
  public static String fetchSampleNames() throws IOException {
    HardwareItemMap hardwareItemMap = HardwareItemMap.newHardwareItemMap();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    List<String> list = Arrays.asList(AppUtil.getDefContext().getAssets().list("blocks/samples"));
    Collections.sort(list);
    if (list != null) {
      Iterator<String> iterator = list.iterator();
      String str = "";
      while (iterator.hasNext()) {
        String str1 = iterator.next();
        if (str1.endsWith(".blk")) {
          str1 = str1.substring(0, str1.length() - 4);
          if (!str1.equals("default")) {
            Set<HardwareUtil.Capability> set = getRequestedCapabilities(readSample(str1, hardwareItemMap));
            stringBuilder.append(str);
            stringBuilder.append("{");
            stringBuilder.append("\"name\":\"");
            stringBuilder.append(escapeDoubleQuotes(str1));
            stringBuilder.append("\", ");
            stringBuilder.append("\"escapedName\":\"");
            stringBuilder.append(escapeDoubleQuotes(Html.escapeHtml(str1)));
            stringBuilder.append("\", ");
            stringBuilder.append("\"requestedCapabilities\":[");
            Iterator<HardwareUtil.Capability> iterator1 = set.iterator();
            for (str = ""; iterator1.hasNext(); str = ",") {
              HardwareUtil.Capability capability = iterator1.next();
              stringBuilder.append(str);
              stringBuilder.append("\"");
              stringBuilder.append(capability);
              stringBuilder.append("\"");
            } 
            stringBuilder.append("]");
            stringBuilder.append("}");
            str = ",";
          } 
        } 
      } 
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  private static String formatExtraXml(OpModeMeta.Flavor paramFlavor, String paramString, boolean paramBoolean) throws IOException {
    XmlSerializer xmlSerializer = Xml.newSerializer();
    StringWriter stringWriter = new StringWriter();
    xmlSerializer.setOutput(stringWriter);
    xmlSerializer.startDocument("UTF-8", Boolean.valueOf(true));
    xmlSerializer.startTag("", "Extra");
    xmlSerializer.startTag("", "OpModeMeta");
    xmlSerializer.attribute("", "flavor", paramFlavor.toString());
    xmlSerializer.attribute("", "group", paramString);
    xmlSerializer.endTag("", "OpModeMeta");
    xmlSerializer.startTag("", "Enabled");
    xmlSerializer.attribute("", "value", Boolean.toString(paramBoolean));
    xmlSerializer.endTag("", "Enabled");
    xmlSerializer.endTag("", "Extra");
    xmlSerializer.endDocument();
    return stringWriter.toString();
  }
  
  public static String getBlocksJavaClassName(String paramString) {
    StringBuilder stringBuilder1 = new StringBuilder();
    char c = paramString.charAt(0);
    if (Character.isJavaIdentifierStart(c)) {
      stringBuilder1.append(c);
    } else if (Character.isJavaIdentifierPart(c)) {
      stringBuilder1.append('_');
      stringBuilder1.append(c);
    } 
    int j = paramString.length();
    int i;
    for (i = 1; i < j; i++) {
      c = paramString.charAt(i);
      if (Character.isJavaIdentifierPart(c))
        stringBuilder1.append(c); 
    } 
    File file = new File(OnBotJavaHelper.srcDir, "org/firstinspires/ftc/teamcode");
    String str = stringBuilder1.toString();
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(str);
    stringBuilder2.append(".java");
    if ((new File(file, stringBuilder2.toString())).exists()) {
      i = 1;
      do {
        j = i + 1;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(j);
        stringBuilder2.append(".java");
        i = j;
      } while ((new File(file, stringBuilder2.toString())).exists());
      stringBuilder1.append(j);
    } 
    return stringBuilder1.toString();
  }
  
  private static Set<HardwareUtil.Capability> getRequestedCapabilities(String paramString) {
    LinkedHashSet<HardwareUtil.Capability> linkedHashSet = new LinkedHashSet();
    if (paramString.contains("<block type=\"tfod"))
      linkedHashSet.add(HardwareUtil.Capability.TFOD); 
    if (paramString.contains("<block type=\"vuforia"))
      linkedHashSet.add(HardwareUtil.Capability.VUFORIA); 
    if (paramString.contains("<block type=\"navigation_switchableCamera"))
      linkedHashSet.add(HardwareUtil.Capability.SWITCHABLE_CAMERA); 
    if (paramString.contains("_initialize_withWebcam"))
      linkedHashSet.add(HardwareUtil.Capability.WEBCAM); 
    if (paramString.contains("_initialize_withCameraDirection"))
      linkedHashSet.add(HardwareUtil.Capability.CAMERA); 
    return linkedHashSet;
  }
  
  static Map<String, String> getSamples(HardwareItemMap paramHardwareItemMap) throws IOException {
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    for (String str : Arrays.<String>asList(AppUtil.getDefContext().getAssets().list("blocks/samples"))) {
      if (str.endsWith(".blk")) {
        String str1 = str.substring(0, str.length() - 4);
        String str2 = readSample(str1, paramHardwareItemMap);
        str = str1;
        if (str1.equals("default"))
          str = ""; 
        treeMap.put(str, str2);
      } 
    } 
    return (Map)treeMap;
  }
  
  private static boolean isProjectEnabled(String paramString) throws IOException {
    if (isValidProjectName(paramString)) {
      File file = AppUtil.BLOCK_OPMODES_DIR;
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(paramString);
      stringBuilder2.append(".blk");
      file = new File(file, stringBuilder2.toString());
      String str = FileUtil.readFile(file);
      int i = str.indexOf("</xml>");
      if (i != -1)
        return isProjectEnabled(paramString, str.substring(i + 6)); 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("File ");
      stringBuilder1.append(file.getName());
      stringBuilder1.append(" is empty or corrupt.");
      throw new CorruptFileException(stringBuilder1.toString());
    } 
    throw new IllegalArgumentException();
  }
  
  private static boolean isProjectEnabled(String paramString1, String paramString2) {
    Boolean bool = true;
    try {
      XmlPullParser xmlPullParser = Xml.newPullParser();
      xmlPullParser.setInput(new StringReader(removeNewLines(paramString2)));
      int i = xmlPullParser.getEventType();
      bool = true;
      label38: while (true) {
        bool = null;
        if (i != 1) {
          Boolean bool1 = bool;
          if (i == 2) {
            bool1 = bool;
            bool = null;
            Boolean bool2 = bool;
            try {
              if (xmlPullParser.getName().equals("Enabled")) {
                i = 0;
                while (true) {
                  bool1 = bool;
                  bool = null;
                  bool2 = bool;
                  if (i < xmlPullParser.getAttributeCount()) {
                    bool = null;
                    bool2 = bool;
                    paramString2 = xmlPullParser.getAttributeName(i);
                    bool = null;
                    bool2 = bool;
                    String str = xmlPullParser.getAttributeValue(i);
                    bool = null;
                    bool2 = bool;
                    bool1 = bool;
                    if (paramString2.equals("value")) {
                      bool = null;
                      bool2 = bool;
                      bool1 = Boolean.parseBoolean(str);
                    } 
                  } else {
                    bool = null;
                    bool2 = bool1;
                    i = xmlPullParser.next();
                    bool = bool1;
                    continue label38;
                  } 
                  i++;
                  bool = bool1;
                } 
                break;
              } 
              continue;
            } catch (IOException iOException) {
              bool2 = null;
            }
            bool = null;
          } else {
            continue;
          } 
        } else {
          return false;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("ProjectsUtil.isProjectEnabled(\"");
        stringBuilder1.append(paramString1);
        stringBuilder1.append("\", ...) - failed to parse xml.");
        RobotLog.e(stringBuilder1.toString());
        RobotLog.logStackTrace((Throwable)XmlPullParserException);
        return bool;
      } 
    } catch (IOException iOException) {
    
    } catch (XmlPullParserException xmlPullParserException) {}
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ProjectsUtil.isProjectEnabled(\"");
    stringBuilder.append(paramString1);
    stringBuilder.append("\", ...) - failed to parse xml.");
    RobotLog.e(stringBuilder.toString());
    RobotLog.logStackTrace((Throwable)XmlPullParserException);
    return bool;
  }
  
  public static boolean isValidProjectName(String paramString) {
    return (paramString != null) ? paramString.matches("^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$") : false;
  }
  
  public static String newProject(String paramString1, String paramString2) throws IOException {
    if (isValidProjectName(paramString1))
      return readSample(paramString2, HardwareItemMap.newHardwareItemMap()); 
    throw new IllegalArgumentException();
  }
  
  private static String readSample(String paramString, HardwareItemMap paramHardwareItemMap) throws IOException {
    if (paramString != null) {
      String str1 = paramString;
      if (paramString.isEmpty()) {
        str1 = "default";
        StringBuilder stringBuilder5 = new StringBuilder();
        AssetManager assetManager2 = AppUtil.getDefContext().getAssets();
        StringBuilder stringBuilder6 = new StringBuilder();
        stringBuilder6.append("blocks/samples/");
        stringBuilder6.append(str1);
        stringBuilder6.append(".blk");
        FileUtil.readAsset(stringBuilder5, assetManager2, stringBuilder6.toString());
        return replaceHardwareIdentifiers(stringBuilder5.toString(), paramHardwareItemMap);
      } 
      StringBuilder stringBuilder3 = new StringBuilder();
      AssetManager assetManager1 = AppUtil.getDefContext().getAssets();
      StringBuilder stringBuilder4 = new StringBuilder();
      stringBuilder4.append("blocks/samples/");
      stringBuilder4.append(str1);
      stringBuilder4.append(".blk");
      FileUtil.readAsset(stringBuilder3, assetManager1, stringBuilder4.toString());
      return replaceHardwareIdentifiers(stringBuilder3.toString(), paramHardwareItemMap);
    } 
    String str = "default";
    StringBuilder stringBuilder1 = new StringBuilder();
    AssetManager assetManager = AppUtil.getDefContext().getAssets();
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("blocks/samples/");
    stringBuilder2.append(str);
    stringBuilder2.append(".blk");
    FileUtil.readAsset(stringBuilder1, assetManager, stringBuilder2.toString());
    return replaceHardwareIdentifiers(stringBuilder1.toString(), paramHardwareItemMap);
  }
  
  private static String removeNewLines(String paramString) {
    return paramString.replace("\n", "");
  }
  
  public static void renameProject(final String oldProjectName, final String newProjectName) throws IOException {
    if (isValidProjectName(oldProjectName) && isValidProjectName(newProjectName)) {
      ProjectsLockManager.lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
            public Void call() throws IOException {
              AppUtil.getInstance().ensureDirectoryExists(AppUtil.BLOCK_OPMODES_DIR, false);
              File file1 = AppUtil.BLOCK_OPMODES_DIR;
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append(oldProjectName);
              stringBuilder1.append(".blk");
              file1 = new File(file1, stringBuilder1.toString());
              File file2 = AppUtil.BLOCK_OPMODES_DIR;
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append(newProjectName);
              stringBuilder2.append(".blk");
              if (file1.renameTo(new File(file2, stringBuilder2.toString()))) {
                file1 = AppUtil.BLOCK_OPMODES_DIR;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(oldProjectName);
                stringBuilder.append(".js");
                file1 = new File(file1, stringBuilder.toString());
                File file = AppUtil.BLOCK_OPMODES_DIR;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(newProjectName);
                stringBuilder2.append(".js");
                file1.renameTo(new File(file, stringBuilder2.toString()));
              } 
              return null;
            }
          });
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  private static String replaceHardwareIdentifiers(String paramString, HardwareItemMap paramHardwareItemMap) {
    if (paramHardwareItemMap.contains(HardwareType.DC_MOTOR)) {
      List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(HardwareType.DC_MOTOR);
      if (!list.isEmpty()) {
        String str1;
        String str4 = ((HardwareItem)list.get(0)).identifier;
        if (list.size() > 1) {
          str1 = ((HardwareItem)list.get(1)).identifier;
        } else {
          str1 = str4;
        } 
        Iterator<HardwareItem> iterator = list.iterator();
        String str3 = null;
        String str2 = null;
        while (iterator.hasNext()) {
          HardwareItem hardwareItem = iterator.next();
          String str6 = hardwareItem.deviceName.toLowerCase(Locale.ENGLISH);
          String str5 = str3;
          if (str3 == null) {
            str5 = str3;
            if (str6.contains("left"))
              str5 = hardwareItem.identifier; 
          } 
          str3 = str5;
          if (str2 == null) {
            str3 = str5;
            if (str6.contains("right")) {
              str2 = hardwareItem.identifier;
              str3 = str5;
            } 
          } 
        } 
        if (str3 == null)
          str3 = str4; 
        if (str2 != null)
          str1 = str2; 
        paramString = replaceIdentifierInBlocks("right_driveAsDcMotor", str1, replaceIdentifierInBlocks("left_driveAsDcMotor", str3, replaceIdentifierInBlocks("motorTestAsDcMotor", str4, paramString, false, list, new String[] { "IDENTIFIER" }), false, list, new String[] { "IDENTIFIER", "IDENTIFIER1" }), false, list, new String[] { "IDENTIFIER", "IDENTIFIER2" });
      } 
    } 
    String str = paramString;
    if (paramHardwareItemMap.contains(HardwareType.DIGITAL_CHANNEL)) {
      List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(HardwareType.DIGITAL_CHANNEL);
      str = paramString;
      if (!list.isEmpty())
        str = replaceIdentifierInBlocks("digitalTouchAsDigitalChannel", ((HardwareItem)list.get(0)).identifier, paramString, false, list, new String[] { "IDENTIFIER" }); 
    } 
    paramString = str;
    if (paramHardwareItemMap.contains(HardwareType.BNO055IMU)) {
      List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(HardwareType.BNO055IMU);
      paramString = str;
      if (!list.isEmpty())
        paramString = replaceIdentifierInBlocks("imuAsBNO055IMU", ((HardwareItem)list.get(0)).identifier, str, false, list, new String[] { "IDENTIFIER" }); 
    } 
    str = paramString;
    paramString = str;
    if (paramHardwareItemMap.contains(HardwareType.SERVO)) {
      List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(HardwareType.SERVO);
      paramString = str;
      if (!list.isEmpty()) {
        paramString = ((HardwareItem)list.get(0)).identifier;
        paramString = replaceIdentifierInBlocks("servoTestAsServo", paramString, replaceIdentifierInBlocks("left_handAsServo", paramString, str, false, list, new String[] { "IDENTIFIER" }), false, list, new String[] { "IDENTIFIER" });
      } 
    } 
    str = paramString;
    paramString = str;
    if (paramHardwareItemMap.contains(HardwareType.COLOR_RANGE_SENSOR)) {
      List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(HardwareType.COLOR_RANGE_SENSOR);
      paramString = str;
      if (!list.isEmpty())
        paramString = replaceIdentifierInBlocks("sensorColorRangeAsREVColorRangeSensor", ((HardwareItem)list.get(0)).identifier, str, false, list, new String[] { "IDENTIFIER" }); 
    } 
    str = paramString;
    paramString = str;
    if (paramHardwareItemMap.contains(HardwareType.REV_BLINKIN_LED_DRIVER)) {
      List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(HardwareType.REV_BLINKIN_LED_DRIVER);
      paramString = str;
      if (!list.isEmpty())
        paramString = replaceIdentifierInBlocks("blinkinAsRevBlinkinLedDriver", ((HardwareItem)list.get(0)).identifier, str, false, list, new String[] { "IDENTIFIER" }); 
    } 
    str = paramString;
    if (paramHardwareItemMap.contains(HardwareType.WEBCAM_NAME)) {
      List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(HardwareType.WEBCAM_NAME);
      str = paramString;
      if (!list.isEmpty())
        str = replaceIdentifierInBlocks("Webcam 1", ((HardwareItem)list.get(0)).deviceName, paramString, true, list, new String[] { "WEBCAM_NAME" }); 
    } 
    return str;
  }
  
  private static String replaceIdentifierInBlocks(String paramString1, String paramString2, String paramString3, boolean paramBoolean, List<HardwareItem> paramList, String... paramVarArgs) {
    for (HardwareItem hardwareItem : paramList) {
      String str;
      if (paramBoolean) {
        str = hardwareItem.deviceName;
      } else {
        str = ((HardwareItem)str).identifier;
      } 
      if (str.equals(paramString1))
        return paramString3; 
    } 
    int j = paramVarArgs.length;
    int i;
    for (i = 0; i < j; i++) {
      String str1 = paramVarArgs[i];
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("<field name=\"");
      stringBuilder1.append(str1);
      stringBuilder1.append("\">");
      stringBuilder1.append(paramString1);
      stringBuilder1.append("</field>");
      String str2 = stringBuilder1.toString();
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("<field name=\"");
      stringBuilder2.append(str1);
      stringBuilder2.append("\">");
      stringBuilder2.append(paramString2);
      stringBuilder2.append("</field>");
      paramString3 = paramString3.replace(str2, stringBuilder2.toString());
    } 
    return paramString3;
  }
  
  private static String replaceIdentifierSuffixInBlocks(String paramString1, List<HardwareItem> paramList, String paramString2, String paramString3) {
    String str = paramString1;
    if (paramList != null) {
      Iterator<HardwareItem> iterator = paramList.iterator();
      label16: while (true) {
        str = paramString1;
        if (iterator.hasNext()) {
          str = ((HardwareItem)iterator.next()).identifier;
          if (str.endsWith(paramString3)) {
            StringBuilder stringBuilder = new StringBuilder();
            int j = str.length();
            int k = paramString3.length();
            int i = 0;
            stringBuilder.append(str.substring(0, j - k));
            stringBuilder.append(paramString2);
            String str2 = stringBuilder.toString();
            String str1 = paramString1;
            while (true) {
              paramString1 = str1;
              if (i < 3) {
                (new String[3])[0] = "IDENTIFIER";
                (new String[3])[1] = "IDENTIFIER1";
                (new String[3])[2] = "IDENTIFIER2";
                paramString1 = (new String[3])[i];
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("<field name=\"");
                stringBuilder1.append(paramString1);
                stringBuilder1.append("\">");
                stringBuilder1.append(str2);
                stringBuilder1.append("</field>");
                String str3 = stringBuilder1.toString();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("<field name=\"");
                stringBuilder2.append(paramString1);
                stringBuilder2.append("\">");
                stringBuilder2.append(str);
                stringBuilder2.append("</field>");
                str1 = str1.replace(str3, stringBuilder2.toString());
                i++;
                continue;
              } 
              continue label16;
            } 
          } 
          continue;
        } 
        break;
      } 
    } 
    return str;
  }
  
  public static void saveBlocksJava(final String relativeFileName, final String javaContent) throws IOException {
    ProjectsLockManager.lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
          public Void call() throws IOException {
            AppUtil.getInstance().ensureDirectoryExists(AppUtil.BLOCK_OPMODES_DIR, false);
            int i = relativeFileName.lastIndexOf("/");
            String str1 = relativeFileName;
            String str2 = str1.substring(0, ++i);
            str1 = relativeFileName.substring(i);
            File file2 = AppUtil.BLOCK_OPMODES_DIR;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("../java/src/");
            stringBuilder.append(str2);
            file2 = new File(file2, stringBuilder.toString());
            file2.mkdirs();
            File file1 = new File(file2, str1);
            long l = System.currentTimeMillis();
            if (file1.exists()) {
              stringBuilder = new StringBuilder();
              stringBuilder.append("backup_");
              stringBuilder.append(l);
              stringBuilder.append("_");
              stringBuilder.append(str1);
              File file = new File(file2, stringBuilder.toString());
              FileUtil.copyFile(file1, file);
            } else {
              str1 = null;
            } 
            FileUtil.writeFile(file1, javaContent);
            if (str1 != null)
              str1.delete(); 
            return null;
          }
        });
  }
  
  public static void saveProject(final String projectName, final String blkFileContent, final String jsFileContent) throws IOException {
    if (isValidProjectName(projectName)) {
      ProjectsLockManager.lockProjectsWhile(new ThrowingCallable<Void, IOException>() {
            public Void call() throws IOException {
              AppUtil.getInstance().ensureDirectoryExists(AppUtil.BLOCK_OPMODES_DIR, false);
              File file1 = AppUtil.BLOCK_OPMODES_DIR;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(projectName);
              stringBuilder.append(".blk");
              File file2 = new File(file1, stringBuilder.toString());
              file1 = AppUtil.BLOCK_OPMODES_DIR;
              stringBuilder = new StringBuilder();
              stringBuilder.append(projectName);
              stringBuilder.append(".js");
              File file3 = new File(file1, stringBuilder.toString());
              long l = System.currentTimeMillis();
              if (file2.exists()) {
                file1 = AppUtil.BLOCK_OPMODES_DIR;
                stringBuilder = new StringBuilder();
                stringBuilder.append("backup_");
                stringBuilder.append(l);
                stringBuilder.append("_");
                stringBuilder.append(projectName);
                stringBuilder.append(".blk");
                file1 = new File(file1, stringBuilder.toString());
                FileUtil.copyFile(file2, file1);
              } else {
                file1 = null;
              } 
              if (file3.exists()) {
                File file = AppUtil.BLOCK_OPMODES_DIR;
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("backup_");
                stringBuilder1.append(l);
                stringBuilder1.append("_");
                stringBuilder1.append(projectName);
                stringBuilder1.append(".js");
                file = new File(file, stringBuilder1.toString());
                FileUtil.copyFile(file3, file);
              } else {
                stringBuilder = null;
              } 
              String str2 = blkFileContent.replace("><", ">\n<").replace(">\n</field>", "></field>").replace("</Extra> ", "</Extra>");
              String str1 = str2;
              if (!str2.endsWith("\n")) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append(str2);
                stringBuilder1.append("\n");
                str1 = stringBuilder1.toString();
              } 
              FileUtil.writeFile(file2, str1);
              FileUtil.writeFile(file3, jsFileContent);
              if (file1 != null)
                file1.delete(); 
              if (stringBuilder != null)
                stringBuilder.delete(); 
              return null;
            }
          });
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  private static String upgradeBlocks(String paramString, HardwareItemMap paramHardwareItemMap) {
    paramString = replaceIdentifierSuffixInBlocks(replaceIdentifierSuffixInBlocks(replaceIdentifierSuffixInBlocks(paramString.replace("<block type=\"adafruitBNO055IMU_", "<block type=\"bno055imu_"), paramHardwareItemMap.getHardwareItems(HardwareType.BNO055IMU), "AsAdafruitBNO055IMU", "AsBNO055IMU").replace("<block type=\"adafruitBNO055IMUParameters_", "<block type=\"bno055imuParameters_").replace("<shadow type=\"adafruitBNO055IMUParameters_", "<shadow type=\"bno055imuParameters_").replace("<value name=\"ADAFRUIT_BNO055IMU_PARAMETERS\">", "<value name=\"BNO055IMU_PARAMETERS\">"), paramHardwareItemMap.getHardwareItems(HardwareType.LYNX_MODULE), "asLynxModule", "AsREVModule"), paramHardwareItemMap.getHardwareItems(HardwareType.COLOR_RANGE_SENSOR), "asLynxI2cColorRangeSensor", "AsREVColorRangeSensor");
    HardwareType hardwareType1 = HardwareType.ACCELERATION_SENSOR;
    int i = 0;
    HardwareType hardwareType2 = HardwareType.COLOR_SENSOR;
    HardwareType hardwareType3 = HardwareType.COMPASS_SENSOR;
    HardwareType hardwareType4 = HardwareType.CR_SERVO;
    HardwareType hardwareType5 = HardwareType.DC_MOTOR;
    HardwareType hardwareType6 = HardwareType.DISTANCE_SENSOR;
    HardwareType hardwareType7 = HardwareType.GYRO_SENSOR;
    HardwareType hardwareType8 = HardwareType.IR_SEEKER_SENSOR;
    HardwareType hardwareType9 = HardwareType.LED;
    HardwareType hardwareType10 = HardwareType.LIGHT_SENSOR;
    HardwareType hardwareType11 = HardwareType.SERVO;
    HardwareType hardwareType12 = HardwareType.TOUCH_SENSOR;
    HardwareType hardwareType13 = HardwareType.ULTRASONIC_SENSOR;
    while (i < 13) {
      (new HardwareType[13])[0] = hardwareType1;
      (new HardwareType[13])[1] = hardwareType2;
      (new HardwareType[13])[2] = hardwareType3;
      (new HardwareType[13])[3] = hardwareType4;
      (new HardwareType[13])[4] = hardwareType5;
      (new HardwareType[13])[5] = hardwareType6;
      (new HardwareType[13])[6] = hardwareType7;
      (new HardwareType[13])[7] = hardwareType8;
      (new HardwareType[13])[8] = hardwareType9;
      (new HardwareType[13])[9] = hardwareType10;
      (new HardwareType[13])[10] = hardwareType11;
      (new HardwareType[13])[11] = hardwareType12;
      (new HardwareType[13])[12] = hardwareType13;
      HardwareType hardwareType = (new HardwareType[13])[i];
      paramString = replaceIdentifierSuffixInBlocks(paramString, paramHardwareItemMap.getHardwareItems(hardwareType), "", hardwareType.identifierSuffixForJavaScript);
      i++;
    } 
    return paramString;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\ProjectsUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
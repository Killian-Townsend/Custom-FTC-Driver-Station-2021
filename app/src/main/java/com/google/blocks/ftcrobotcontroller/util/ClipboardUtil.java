package com.google.blocks.ftcrobotcontroller.util;

import java.io.File;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class ClipboardUtil {
  private static final File CLIPBOARD_FILE = new File(AppUtil.BLOCK_OPMODES_DIR, "clipboard.xml");
  
  public static String fetchClipboardContent() throws IOException {
    return FileUtil.readFile(CLIPBOARD_FILE);
  }
  
  public static void saveClipboardContent(String paramString) throws IOException {
    AppUtil.getInstance().ensureDirectoryExists(AppUtil.BLOCK_OPMODES_DIR);
    FileUtil.writeFile(CLIPBOARD_FILE, paramString);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\ClipboardUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
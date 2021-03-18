package com.google.blocks.ftcrobotcontroller.util;

class OfflineBlocksProject {
  final String content;
  
  final long dateModifiedMillis;
  
  final boolean enabled;
  
  final String fileName;
  
  final String name;
  
  OfflineBlocksProject(String paramString1, String paramString2, String paramString3, long paramLong, boolean paramBoolean) {
    this.fileName = paramString1;
    this.content = paramString2.replace("\n", " ").replaceAll("\\> +\\<", "><");
    this.name = paramString3;
    this.dateModifiedMillis = paramLong;
    this.enabled = paramBoolean;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof OfflineBlocksProject;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.fileName.equals(((OfflineBlocksProject)paramObject).fileName)) {
        bool1 = bool2;
        if (this.content.equals(((OfflineBlocksProject)paramObject).content)) {
          bool1 = bool2;
          if (this.name.equals(((OfflineBlocksProject)paramObject).name)) {
            bool1 = bool2;
            if (this.dateModifiedMillis == ((OfflineBlocksProject)paramObject).dateModifiedMillis) {
              bool1 = bool2;
              if (this.enabled == ((OfflineBlocksProject)paramObject).enabled)
                bool1 = true; 
            } 
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  public int hashCode() {
    return this.fileName.hashCode() + this.content.hashCode() + this.name.hashCode();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\OfflineBlocksProject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
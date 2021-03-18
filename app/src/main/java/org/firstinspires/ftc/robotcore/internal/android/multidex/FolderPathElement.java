package org.firstinspires.ftc.robotcore.internal.android.multidex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

class FolderPathElement implements ClassPathElement {
  private File baseFolder;
  
  public FolderPathElement(File paramFile) {
    this.baseFolder = paramFile;
  }
  
  private void collect(File paramFile, String paramString, ArrayList<String> paramArrayList) {
    for (File file : paramFile.listFiles()) {
      if (file.isDirectory()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append('/');
        stringBuilder.append(file.getName());
        collect(file, stringBuilder.toString(), paramArrayList);
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append('/');
        stringBuilder.append(file.getName());
        paramArrayList.add(stringBuilder.toString());
      } 
    } 
  }
  
  public void close() {}
  
  public Iterable<String> list() {
    ArrayList<String> arrayList = new ArrayList();
    collect(this.baseFolder, "", arrayList);
    return arrayList;
  }
  
  public InputStream open(String paramString) throws FileNotFoundException {
    return new FileInputStream(new File(this.baseFolder, paramString.replace('/', File.separatorChar)));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\multidex\FolderPathElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
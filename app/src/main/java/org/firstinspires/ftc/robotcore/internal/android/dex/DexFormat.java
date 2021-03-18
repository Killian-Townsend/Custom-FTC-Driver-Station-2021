package org.firstinspires.ftc.robotcore.internal.android.dex;

public final class DexFormat {
  public static final int API_CURRENT = 24;
  
  public static final int API_NO_EXTENDED_OPCODES = 13;
  
  public static final String DEX_IN_JAR_NAME = "classes.dex";
  
  public static final int ENDIAN_TAG = 305419896;
  
  public static final String MAGIC_PREFIX = "dex\n";
  
  public static final String MAGIC_SUFFIX = "\000";
  
  public static final int MAX_MEMBER_IDX = 65535;
  
  public static final int MAX_TYPE_IDX = 65535;
  
  public static final String VERSION_CURRENT = "037";
  
  public static final String VERSION_FOR_API_13 = "035";
  
  public static String apiToMagic(int paramInt) {
    String str;
    if (paramInt >= 24) {
      str = "037";
    } else {
      str = "035";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("dex\n");
    stringBuilder.append(str);
    stringBuilder.append("\000");
    return stringBuilder.toString();
  }
  
  public static boolean isSupportedDexMagic(byte[] paramArrayOfbyte) {
    int i = magicToApi(paramArrayOfbyte);
    return (i == 13 || i == 24);
  }
  
  public static int magicToApi(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length != 8)
      return -1; 
    if (paramArrayOfbyte[0] == 100 && paramArrayOfbyte[1] == 101 && paramArrayOfbyte[2] == 120 && paramArrayOfbyte[3] == 10) {
      if (paramArrayOfbyte[7] != 0)
        return -1; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append((char)paramArrayOfbyte[4]);
      stringBuilder.append((char)paramArrayOfbyte[5]);
      stringBuilder.append((char)paramArrayOfbyte[6]);
      String str = stringBuilder.toString();
      if (str.equals("037"))
        return 24; 
      if (str.equals("035"))
        return 13; 
    } 
    return -1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\DexFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
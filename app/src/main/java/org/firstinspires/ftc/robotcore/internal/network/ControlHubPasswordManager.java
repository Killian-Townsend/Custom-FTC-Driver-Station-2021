package org.firstinspires.ftc.robotcore.internal.network;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class ControlHubPasswordManager implements PasswordManager {
  private static final String FACTORY_DEFAULT_PASSWORD = "password";
  
  private static final String TAG = "ControlHubPasswordManager";
  
  private Context context;
  
  private String password;
  
  private PreferencesHelper preferencesHelper;
  
  private SharedPreferences sharedPreferences;
  
  public ControlHubPasswordManager() {
    Application application = AppUtil.getInstance().getApplication();
    this.context = (Context)application;
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)application);
    this.preferencesHelper = new PreferencesHelper("ControlHubPasswordManager", this.sharedPreferences);
  }
  
  private StringBuffer stringify(byte[] paramArrayOfbyte) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < paramArrayOfbyte.length; i++) {
      String str2 = Integer.toHexString(paramArrayOfbyte[i] & 0xFF);
      String str1 = str2;
      if (str2.length() == 1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('0');
        stringBuilder.append(str2);
        str1 = stringBuilder.toString();
      } 
      stringBuffer.append(str1);
    } 
    return stringBuffer;
  }
  
  private String toSha256(String paramString) {
    try {
      return stringify(MessageDigest.getInstance("SHA-256").digest(paramString.getBytes(StandardCharsets.UTF_8))).toString();
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      noSuchAlgorithmException.printStackTrace();
      return null;
    } 
  }
  
  public String getPassword() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initializePasswordIfNecessary : ()V
    //   6: aload_0
    //   7: getfield preferencesHelper : Lorg/firstinspires/ftc/robotcore/internal/system/PreferencesHelper;
    //   10: aload_0
    //   11: getfield context : Landroid/content/Context;
    //   14: getstatic com/qualcomm/robotcore/R$string.pref_connection_owner_password : I
    //   17: invokevirtual getString : (I)Ljava/lang/String;
    //   20: ldc 'password'
    //   22: invokevirtual readString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   25: astore_1
    //   26: aload_1
    //   27: invokestatic i : (Ljava/lang/String;)V
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: areturn
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	34	finally
  }
  
  protected void initializePasswordIfNecessary() {
    String str = this.preferencesHelper.readString(this.context.getString(R.string.pref_connection_owner_password), "");
    this.password = str;
    if (str.isEmpty())
      this.preferencesHelper.writeStringPrefIfDifferent(this.context.getString(R.string.pref_connection_owner_password), "password"); 
    str = this.preferencesHelper.readString(this.context.getString(R.string.pref_connection_owner_password), "");
    this.password = str;
    if (!str.isEmpty())
      return; 
    throw new IllegalStateException("Password not set");
  }
  
  protected void internalSetDevicePassword(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Robot controller password: ");
    stringBuilder.append(paramString);
    RobotLog.ii("ControlHubPasswordManager", stringBuilder.toString());
    this.preferencesHelper.writeStringPrefIfDifferent(this.context.getString(R.string.pref_connection_owner_password), paramString);
  }
  
  public boolean isDefault() {
    return getPassword().equals("password");
  }
  
  public String resetPassword(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: ldc 'password'
    //   5: iload_1
    //   6: invokevirtual setPassword : (Ljava/lang/String;Z)V
    //   9: goto -> 45
    //   12: astore_2
    //   13: new java/lang/StringBuilder
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: astore_3
    //   21: aload_3
    //   22: ldc 'Unable to reset password to '
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload_3
    //   29: ldc 'password'
    //   31: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: pop
    //   35: ldc 'ControlHubPasswordManager'
    //   37: aload_2
    //   38: aload_3
    //   39: invokevirtual toString : ()Ljava/lang/String;
    //   42: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   45: aload_0
    //   46: monitorexit
    //   47: ldc 'password'
    //   49: areturn
    //   50: astore_2
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_2
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	org/firstinspires/ftc/robotcore/internal/network/InvalidNetworkSettingException
    //   2	9	50	finally
    //   13	45	50	finally
  }
  
  public void setPassword(String paramString, boolean paramBoolean) throws InvalidNetworkSettingException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual validatePassword : (Ljava/lang/String;)V
    //   7: aload_0
    //   8: aload_1
    //   9: invokevirtual internalSetDevicePassword : (Ljava/lang/String;)V
    //   12: iload_2
    //   13: ifeq -> 49
    //   16: ldc 'ControlHubPasswordManager'
    //   18: ldc 'Sending password change intent'
    //   20: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   23: new android/content/Intent
    //   26: dup
    //   27: ldc 'org.firstinspires.ftc.intent.action.FTC_AP_PASSWORD_CHANGE'
    //   29: invokespecial <init> : (Ljava/lang/String;)V
    //   32: astore_3
    //   33: aload_3
    //   34: ldc 'org.firstinspires.ftc.intent.extra.EXTRA_AP_PREF'
    //   36: aload_1
    //   37: invokevirtual putExtra : (Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   40: pop
    //   41: aload_0
    //   42: getfield context : Landroid/content/Context;
    //   45: aload_3
    //   46: invokevirtual sendBroadcast : (Landroid/content/Intent;)V
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	52	finally
    //   16	49	52	finally
  }
  
  public void validatePassword(String paramString) throws InvalidNetworkSettingException {
    if (paramString.length() >= 8 && paramString.length() <= 63)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid password length of ");
    stringBuilder.append(paramString.length());
    stringBuilder.append(" chars.");
    throw new InvalidNetworkSettingException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\ControlHubPasswordManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package androidx.core.view;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import androidx.core.util.Preconditions;
import java.util.ArrayList;

public final class DisplayCompat {
  private static final int DISPLAY_SIZE_4K_HEIGHT = 2160;
  
  private static final int DISPLAY_SIZE_4K_WIDTH = 3840;
  
  private static Point getPhysicalDisplaySize(Context paramContext, Display paramDisplay) {
    Display.Mode mode;
    Point point2;
    if (Build.VERSION.SDK_INT < 28) {
      point2 = parsePhysicalDisplaySizeFromSystemProperties("sys.display-size", paramDisplay);
    } else {
      point2 = parsePhysicalDisplaySizeFromSystemProperties("vendor.display-size", paramDisplay);
    } 
    if (point2 != null)
      return point2; 
    if (isSonyBravia4kTv(paramContext))
      return new Point(3840, 2160); 
    Point point1 = new Point();
    if (Build.VERSION.SDK_INT >= 23) {
      mode = paramDisplay.getMode();
      point1.x = mode.getPhysicalWidth();
      point1.y = mode.getPhysicalHeight();
      return point1;
    } 
    if (Build.VERSION.SDK_INT >= 17) {
      mode.getRealSize(point1);
      return point1;
    } 
    mode.getSize(point1);
    return point1;
  }
  
  public static ModeCompat[] getSupportedModes(Context paramContext, Display paramDisplay) {
    Point point = getPhysicalDisplaySize(paramContext, paramDisplay);
    if (Build.VERSION.SDK_INT >= 23) {
      Display.Mode[] arrayOfMode = paramDisplay.getSupportedModes();
      ArrayList<ModeCompat> arrayList = new ArrayList(arrayOfMode.length);
      int i = 0;
      int j = i;
      while (i < arrayOfMode.length) {
        if (physicalSizeEquals(arrayOfMode[i], point)) {
          arrayList.add(i, new ModeCompat(arrayOfMode[i], true));
          j = 1;
        } else {
          arrayList.add(i, new ModeCompat(arrayOfMode[i], false));
        } 
        i++;
      } 
      if (j == 0)
        arrayList.add(new ModeCompat(point)); 
      return arrayList.<ModeCompat>toArray(new ModeCompat[0]);
    } 
    return new ModeCompat[] { new ModeCompat(point) };
  }
  
  private static String getSystemProperty(String paramString) {
    try {
      Class<?> clazz = Class.forName("android.os.SystemProperties");
      return (String)clazz.getMethod("get", new Class[] { String.class }).invoke(clazz, new Object[] { paramString });
    } catch (Exception exception) {
      return null;
    } 
  }
  
  private static boolean isSonyBravia4kTv(Context paramContext) {
    return (isTv(paramContext) && "Sony".equals(Build.MANUFACTURER) && Build.MODEL.startsWith("BRAVIA") && paramContext.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd"));
  }
  
  private static boolean isTv(Context paramContext) {
    UiModeManager uiModeManager = (UiModeManager)paramContext.getSystemService("uimode");
    return (uiModeManager != null && uiModeManager.getCurrentModeType() == 4);
  }
  
  private static Point parseDisplaySize(String paramString) throws NumberFormatException {
    String[] arrayOfString = paramString.trim().split("x", -1);
    if (arrayOfString.length == 2) {
      int i = Integer.parseInt(arrayOfString[0]);
      int j = Integer.parseInt(arrayOfString[1]);
      if (i > 0 && j > 0)
        return new Point(i, j); 
    } 
    throw new NumberFormatException();
  }
  
  private static Point parsePhysicalDisplaySizeFromSystemProperties(String paramString, Display paramDisplay) {
    if (paramDisplay.getDisplayId() == 0) {
      paramString = getSystemProperty(paramString);
      if (!TextUtils.isEmpty(paramString))
        try {
          return parseDisplaySize(paramString);
        } catch (NumberFormatException numberFormatException) {} 
    } 
    return null;
  }
  
  private static boolean physicalSizeEquals(Display.Mode paramMode, Point paramPoint) {
    return ((paramMode.getPhysicalWidth() == paramPoint.x && paramMode.getPhysicalHeight() == paramPoint.y) || (paramMode.getPhysicalWidth() == paramPoint.y && paramMode.getPhysicalHeight() == paramPoint.x));
  }
  
  public static final class ModeCompat {
    private final boolean mIsNative;
    
    private final Display.Mode mMode;
    
    private final Point mPhysicalDisplaySize;
    
    ModeCompat(Point param1Point) {
      Preconditions.checkNotNull(param1Point, "physicalDisplaySize == null");
      this.mIsNative = true;
      this.mPhysicalDisplaySize = param1Point;
      this.mMode = null;
    }
    
    ModeCompat(Display.Mode param1Mode, boolean param1Boolean) {
      Preconditions.checkNotNull(param1Mode, "Display.Mode == null, can't wrap a null reference");
      this.mIsNative = param1Boolean;
      this.mPhysicalDisplaySize = new Point(param1Mode.getPhysicalWidth(), param1Mode.getPhysicalHeight());
      this.mMode = param1Mode;
    }
    
    public int getPhysicalHeight() {
      return this.mPhysicalDisplaySize.y;
    }
    
    public int getPhysicalWidth() {
      return this.mPhysicalDisplaySize.x;
    }
    
    public boolean isNative() {
      return this.mIsNative;
    }
    
    public Display.Mode toMode() {
      return this.mMode;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\view\DisplayCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
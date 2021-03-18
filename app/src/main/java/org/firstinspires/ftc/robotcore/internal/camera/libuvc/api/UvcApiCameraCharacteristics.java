package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import android.util.Pair;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class UvcApiCameraCharacteristics implements CameraCharacteristics {
  public static final String TAG = UvcApiCameraCharacteristics.class.getSimpleName();
  
  protected SparseArray<Size> defaultOutputSizeMap = new SparseArray();
  
  protected Map<Pair<Integer, Size>, FormatSizeInfo> formatSizeInfoMap = new HashMap<Pair<Integer, Size>, FormatSizeInfo>();
  
  protected int[] outputAndroidFormats = new int[0];
  
  protected String[] outputFormatNames = new String[0];
  
  protected SparseArray<Size[]> outputSizesMap = new SparseArray();
  
  public boolean equals(Object<CameraCharacteristics.CameraMode> paramObject) {
    if (paramObject instanceof CameraCharacteristics) {
      CameraCharacteristics cameraCharacteristics = (CameraCharacteristics)paramObject;
      paramObject = (Object<CameraCharacteristics.CameraMode>)getAllCameraModes();
      List list = cameraCharacteristics.getAllCameraModes();
      if (paramObject.size() == list.size()) {
        paramObject = (Object<CameraCharacteristics.CameraMode>)paramObject.iterator();
        while (paramObject.hasNext()) {
          if (!list.contains(paramObject.next()))
            return false; 
        } 
        return true;
      } 
      return false;
    } 
    return super.equals(paramObject);
  }
  
  public List<CameraCharacteristics.CameraMode> getAllCameraModes() {
    ArrayList<CameraCharacteristics.CameraMode> arrayList = new ArrayList();
    for (Pair<Integer, Size> pair : this.formatSizeInfoMap.keySet()) {
      FormatSizeInfo formatSizeInfo = this.formatSizeInfoMap.get(pair);
      boolean bool = getDefaultSize(((Integer)pair.first).intValue()).equals(pair.second);
      arrayList.add(new CameraCharacteristics.CameraMode(((Integer)pair.first).intValue(), (Size)pair.second, formatSizeInfo.nsMinFrameDuration, bool));
    } 
    return arrayList;
  }
  
  public int[] getAndroidFormats() {
    return this.outputAndroidFormats;
  }
  
  public Size getDefaultSize(int paramInt) {
    Size size2 = (Size)this.defaultOutputSizeMap.get(paramInt, null);
    Size size1 = size2;
    if (size2 == null) {
      Size[] arrayOfSize = getSizes(paramInt);
      size1 = size2;
      if (arrayOfSize.length > 0)
        size1 = arrayOfSize[0]; 
    } 
    return size1;
  }
  
  public int getMaxFramesPerSecond(int paramInt, Size paramSize) {
    long l = getMinFrameDuration(paramInt, paramSize);
    return (l == 0L) ? 0 : (int)Math.round(1.0E9D / l);
  }
  
  public long getMinFrameDuration(int paramInt, Size paramSize) {
    FormatSizeInfo formatSizeInfo = this.formatSizeInfoMap.get(Pair.create(Integer.valueOf(paramInt), paramSize));
    return (formatSizeInfo != null) ? formatSizeInfo.nsMinFrameDuration : 0L;
  }
  
  public Size[] getSizes(int paramInt) {
    return (Size[])this.outputSizesMap.get(paramInt, new Size[0]);
  }
  
  public int hashCode() {
    Iterator<Pair> iterator = this.formatSizeInfoMap.keySet().iterator();
    int i;
    for (i = 9007512; iterator.hasNext(); i ^= ((Pair)iterator.next()).hashCode());
    return i;
  }
  
  public void setMaps(SparseArray<Size[]> paramSparseArray, SparseArray<Size> paramSparseArray1, Map<Pair<Integer, Size>, FormatSizeInfo> paramMap) {
    this.outputSizesMap = paramSparseArray;
    this.defaultOutputSizeMap = paramSparseArray1;
    this.formatSizeInfoMap = paramMap;
  }
  
  public void setOutputFormats(int[] paramArrayOfint, String[] paramArrayOfString) {
    this.outputAndroidFormats = paramArrayOfint;
    this.outputFormatNames = paramArrayOfString;
  }
  
  public String toString() {
    return toString(TAG);
  }
  
  public String toString(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    List<CameraCharacteristics.CameraMode> list = getAllCameraModes();
    stringBuilder.append(Misc.formatInvariant("%s size=%d\n", new Object[] { paramString, Integer.valueOf(list.size()) }));
    for (CameraCharacteristics.CameraMode cameraMode : list) {
      stringBuilder.append("    ");
      stringBuilder.append(cameraMode.toString());
      stringBuilder.append("\n");
    } 
    return stringBuilder.toString();
  }
  
  public static class FormatSizeInfo {
    public final long nsMinFrameDuration;
    
    public FormatSizeInfo(long param1Long) {
      this.nsMinFrameDuration = param1Long;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiCameraCharacteristics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import android.util.Pair;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.internal.camera.ImageFormatMapper;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcFormatDesc;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcFrameDesc;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcStreamingInterface;
import org.firstinspires.ftc.robotcore.internal.system.DestructOnFinalize;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class UvcApiCameraCharacteristicsBuilder extends DestructOnFinalize {
  UvcApiCameraCharacteristics built = null;
  
  protected List<UvcStreamingInterface> streamingInterfaces = new ArrayList<UvcStreamingInterface>();
  
  public static UvcApiCameraCharacteristics buildFromModes(Collection<CameraCharacteristics.CameraMode> paramCollection) {
    Collection<CameraCharacteristics.CameraMode> collection = paramCollection;
    if (paramCollection == null)
      collection = new ArrayList<CameraCharacteristics.CameraMode>(); 
    SparseArray sparseArray3 = new SparseArray();
    SparseArray sparseArray2 = new SparseArray();
    SparseArray<Size> sparseArray1 = new SparseArray();
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    for (CameraCharacteristics.CameraMode cameraMode : collection) {
      hashMap.put(new Pair(Integer.valueOf(cameraMode.androidFormat), cameraMode.size), new UvcApiCameraCharacteristics.FormatSizeInfo(cameraMode.nsFrameDuration));
      collection = (List)sparseArray3.get(cameraMode.androidFormat, null);
      paramCollection = collection;
      if (collection == null)
        paramCollection = new ArrayList<CameraCharacteristics.CameraMode>(); 
      paramCollection.add(cameraMode.size);
      sparseArray3.put(cameraMode.androidFormat, paramCollection);
      if (cameraMode.isDefaultSize)
        sparseArray1.put(cameraMode.androidFormat, cameraMode.size); 
      for (ImageFormatMapper.Format format : ImageFormatMapper.allFromAndroid(cameraMode.androidFormat)) {
        if (format.android != 0 && sparseArray2.indexOfKey(format.android) < 0)
          sparseArray2.put(format.android, format.name); 
      } 
    } 
    SparseArray<Size[]> sparseArray = new SparseArray();
    boolean bool = false;
    int i;
    for (i = 0; i < sparseArray3.size(); i++) {
      int j = sparseArray3.keyAt(i);
      collection = (List)sparseArray3.get(j);
      sparseArray.put(j, collection.<Size>toArray(new Size[collection.size()]));
    } 
    int[] arrayOfInt = new int[sparseArray2.size()];
    String[] arrayOfString = new String[sparseArray2.size()];
    for (i = bool; i < sparseArray2.size(); i++) {
      arrayOfInt[i] = sparseArray2.keyAt(i);
      arrayOfString[i] = (String)sparseArray2.get(arrayOfInt[i]);
    } 
    UvcApiCameraCharacteristics uvcApiCameraCharacteristics = new UvcApiCameraCharacteristics();
    uvcApiCameraCharacteristics.setOutputFormats(arrayOfInt, arrayOfString);
    uvcApiCameraCharacteristics.setMaps(sparseArray, sparseArray1, (Map)hashMap);
    return uvcApiCameraCharacteristics;
  }
  
  public void addStream(UvcStreamingInterface paramUvcStreamingInterface) {
    synchronized (this.lock) {
      this.streamingInterfaces.add(paramUvcStreamingInterface);
      paramUvcStreamingInterface.addRef();
      return;
    } 
  }
  
  public UvcApiCameraCharacteristics build() {
    synchronized (this.lock) {
      if (this.built == null) {
        this.built = internalBuild();
        releaseStreams();
      } 
      return this.built;
    } 
  }
  
  protected void destructor() {
    releaseStreams();
    super.destructor();
  }
  
  protected Size getDefaultSizeOfAndroidFormat(int paramInt) {
    Size size;
    Iterator<UvcStreamingInterface> iterator = this.streamingInterfaces.iterator();
    UvcStreamingInterface uvcStreamingInterface = null;
    label19: while (iterator.hasNext()) {
      UvcStreamingInterface uvcStreamingInterface1 = iterator.next();
      boolean bool = false;
      Iterator<UvcFormatDesc> iterator1 = uvcStreamingInterface1.getFormatDescriptors().iterator();
      uvcStreamingInterface1 = uvcStreamingInterface;
      while (true) {
        uvcStreamingInterface = uvcStreamingInterface1;
        if (iterator1.hasNext()) {
          UvcFormatDesc uvcFormatDesc = iterator1.next();
          uvcStreamingInterface = uvcStreamingInterface1;
          boolean bool1 = bool;
          if (!bool) {
            uvcStreamingInterface = uvcStreamingInterface1;
            bool1 = bool;
            try {
              if (uvcFormatDesc.isAndroidFormat(paramInt)) {
                UvcFrameDesc uvcFrameDesc = uvcFormatDesc.getDefaultFrameDesc();
                size = uvcFrameDesc.getSize();
                uvcFrameDesc.releaseRef();
                bool1 = true;
              } 
            } finally {
              uvcFormatDesc.releaseRef();
            } 
          } 
          uvcFormatDesc.releaseRef();
          Size size1 = size;
          bool = bool1;
          continue;
        } 
        continue label19;
      } 
    } 
    return size;
  }
  
  protected long getNsMinFrameDurationOfAndroidFormat(int paramInt, Size paramSize) {
    Iterator<UvcStreamingInterface> iterator = this.streamingInterfaces.iterator();
    long l1 = Long.MAX_VALUE;
    label30: while (iterator.hasNext()) {
      UvcStreamingInterface uvcStreamingInterface = iterator.next();
      boolean bool = false;
      Iterator<UvcFormatDesc> iterator1 = uvcStreamingInterface.getFormatDescriptors().iterator();
      long l = l1;
      while (true) {
        l1 = l;
        if (iterator1.hasNext()) {
          UvcFormatDesc uvcFormatDesc = iterator1.next();
          l1 = l;
          boolean bool1 = bool;
          if (!bool) {
            l1 = l;
            bool1 = bool;
            try {
              if (uvcFormatDesc.isAndroidFormat(paramInt)) {
                for (UvcFrameDesc uvcFrameDesc : uvcFormatDesc.getFrameDescriptors()) {
                  l1 = l;
                  if (uvcFrameDesc.getSize().equals(paramSize))
                    l1 = Math.min(l, uvcFrameDesc.getMinFrameInterval() * 100L); 
                  uvcFrameDesc.releaseRef();
                  l = l1;
                } 
                bool1 = true;
                l1 = l;
              } 
            } finally {
              uvcFormatDesc.releaseRef();
            } 
          } 
          uvcFormatDesc.releaseRef();
          l = l1;
          bool = bool1;
          continue;
        } 
        continue label30;
      } 
    } 
    long l2 = l1;
    if (l1 == Long.MAX_VALUE)
      l2 = 0L; 
    return l2;
  }
  
  protected Size[] getSizesOfAndroidFormat(int paramInt) {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    for (UvcStreamingInterface uvcStreamingInterface : this.streamingInterfaces) {
      boolean bool = false;
      for (UvcFormatDesc uvcFormatDesc : uvcStreamingInterface.getFormatDescriptors()) {
        boolean bool1 = bool;
        if (!bool)
          bool1 = bool; 
        uvcFormatDesc.releaseRef();
        bool = bool1;
      } 
    } 
    return (Size[])Misc.toArray((Object[])new Size[linkedHashMap.keySet().size()], linkedHashMap.keySet());
  }
  
  protected UvcApiCameraCharacteristics internalBuild() {
    UvcApiCameraCharacteristics uvcApiCameraCharacteristics2 = this.built;
    UvcApiCameraCharacteristics uvcApiCameraCharacteristics1 = uvcApiCameraCharacteristics2;
    if (uvcApiCameraCharacteristics2 == null) {
      uvcApiCameraCharacteristics1 = new UvcApiCameraCharacteristics();
      SparseArray<Size[]> sparseArray = new SparseArray();
      Iterator<UvcStreamingInterface> iterator = this.streamingInterfaces.iterator();
      while (iterator.hasNext()) {
        for (UvcFormatDesc uvcFormatDesc : ((UvcStreamingInterface)iterator.next()).getFormatDescriptors()) {
          try {
            for (ImageFormatMapper.Format format : ImageFormatMapper.allFromGuid(uvcFormatDesc.getGuidFormat())) {
              if (format.android != 0) {
                if (sparseArray.indexOfKey(format.android) < 0)
                  sparseArray.put(format.android, format.name); 
                break;
              } 
            } 
          } finally {
            uvcFormatDesc.releaseRef();
          } 
        } 
      } 
      int j = sparseArray.size();
      int[] arrayOfInt = new int[j];
      String[] arrayOfString = new String[sparseArray.size()];
      int i;
      for (i = 0; i < sparseArray.size(); i++) {
        arrayOfInt[i] = sparseArray.keyAt(i);
        arrayOfString[i] = (String)sparseArray.get(arrayOfInt[i]);
      } 
      uvcApiCameraCharacteristics1.setOutputFormats(arrayOfInt, arrayOfString);
      sparseArray = new SparseArray();
      SparseArray<Size> sparseArray1 = new SparseArray();
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      for (i = 0; i < j; i++) {
        int m = arrayOfInt[i];
        Size[] arrayOfSize = getSizesOfAndroidFormat(m);
        sparseArray.put(m, arrayOfSize);
        sparseArray1.put(m, getDefaultSizeOfAndroidFormat(m));
        int n = arrayOfSize.length;
        for (int k = 0; k < n; k++) {
          Size size = arrayOfSize[k];
          long l = getNsMinFrameDurationOfAndroidFormat(m, size);
          if (l != 0L)
            hashMap.put(Pair.create(Integer.valueOf(m), size), new UvcApiCameraCharacteristics.FormatSizeInfo(l)); 
        } 
      } 
      uvcApiCameraCharacteristics1.setMaps(sparseArray, sparseArray1, (Map)hashMap);
    } 
    return uvcApiCameraCharacteristics1;
  }
  
  public void releaseStreams() {
    Iterator<UvcStreamingInterface> iterator = this.streamingInterfaces.iterator();
    while (iterator.hasNext())
      ((UvcStreamingInterface)iterator.next()).releaseRef(); 
    this.streamingInterfaces.clear();
  }
  
  public String toString() {
    synchronized (this.lock) {
      return internalBuild().toString(getTag());
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiCameraCharacteristicsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
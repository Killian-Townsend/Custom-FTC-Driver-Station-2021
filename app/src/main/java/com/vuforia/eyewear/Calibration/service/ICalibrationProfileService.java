package com.vuforia.eyewear.Calibration.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICalibrationProfileService extends IInterface {
  boolean clearProfile(int paramInt) throws RemoteException;
  
  int getActiveProfile() throws RemoteException;
  
  float[] getCameraToEyePose(int paramInt1, int paramInt2) throws RemoteException;
  
  float[] getEyeProjection(int paramInt1, int paramInt2) throws RemoteException;
  
  int getMaxProfileCount() throws RemoteException;
  
  String getProfileName(int paramInt) throws RemoteException;
  
  int getUsedProfileCount() throws RemoteException;
  
  boolean isProfileUsed(int paramInt) throws RemoteException;
  
  boolean setActiveProfile(int paramInt) throws RemoteException;
  
  boolean setCameraToEyePose(int paramInt1, int paramInt2, float[] paramArrayOffloat) throws RemoteException;
  
  boolean setEyeProjection(int paramInt1, int paramInt2, float[] paramArrayOffloat) throws RemoteException;
  
  boolean setProfileName(int paramInt, String paramString) throws RemoteException;
  
  public static abstract class Stub extends Binder implements ICalibrationProfileService {
    private static final String DESCRIPTOR = "com.vuforia.eyewear.Calibration.service.ICalibrationProfileService";
    
    static final int TRANSACTION_clearProfile = 12;
    
    static final int TRANSACTION_getActiveProfile = 4;
    
    static final int TRANSACTION_getCameraToEyePose = 8;
    
    static final int TRANSACTION_getEyeProjection = 9;
    
    static final int TRANSACTION_getMaxProfileCount = 1;
    
    static final int TRANSACTION_getProfileName = 6;
    
    static final int TRANSACTION_getUsedProfileCount = 2;
    
    static final int TRANSACTION_isProfileUsed = 3;
    
    static final int TRANSACTION_setActiveProfile = 5;
    
    static final int TRANSACTION_setCameraToEyePose = 10;
    
    static final int TRANSACTION_setEyeProjection = 11;
    
    static final int TRANSACTION_setProfileName = 7;
    
    public Stub() {
      attachInterface(this, "com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
    }
    
    public static ICalibrationProfileService asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
      return (iInterface != null && iInterface instanceof ICalibrationProfileService) ? (ICalibrationProfileService)iInterface : new Proxy(param1IBinder);
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    private static class Proxy implements ICalibrationProfileService {
      private IBinder mRemote;
      
      Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public boolean clearProfile(int param2Int) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int);
          IBinder iBinder = this.mRemote;
          boolean bool = false;
          iBinder.transact(12, parcel1, parcel2, 0);
          parcel2.readException();
          param2Int = parcel2.readInt();
          if (param2Int != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public int getActiveProfile() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          this.mRemote.transact(4, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readInt();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public float[] getCameraToEyePose(int param2Int1, int param2Int2) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          this.mRemote.transact(8, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.createFloatArray();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public float[] getEyeProjection(int param2Int1, int param2Int2) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          this.mRemote.transact(9, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.createFloatArray();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getInterfaceDescriptor() {
        return "com.vuforia.eyewear.Calibration.service.ICalibrationProfileService";
      }
      
      public int getMaxProfileCount() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          this.mRemote.transact(1, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readInt();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getProfileName(int param2Int) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int);
          this.mRemote.transact(6, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public int getUsedProfileCount() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          this.mRemote.transact(2, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readInt();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean isProfileUsed(int param2Int) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int);
          IBinder iBinder = this.mRemote;
          boolean bool = false;
          iBinder.transact(3, parcel1, parcel2, 0);
          parcel2.readException();
          param2Int = parcel2.readInt();
          if (param2Int != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean setActiveProfile(int param2Int) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int);
          IBinder iBinder = this.mRemote;
          boolean bool = false;
          iBinder.transact(5, parcel1, parcel2, 0);
          parcel2.readException();
          param2Int = parcel2.readInt();
          if (param2Int != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean setCameraToEyePose(int param2Int1, int param2Int2, float[] param2ArrayOffloat) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          parcel1.writeFloatArray(param2ArrayOffloat);
          IBinder iBinder = this.mRemote;
          boolean bool = false;
          iBinder.transact(10, parcel1, parcel2, 0);
          parcel2.readException();
          param2Int1 = parcel2.readInt();
          if (param2Int1 != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean setEyeProjection(int param2Int1, int param2Int2, float[] param2ArrayOffloat) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          parcel1.writeFloatArray(param2ArrayOffloat);
          IBinder iBinder = this.mRemote;
          boolean bool = false;
          iBinder.transact(11, parcel1, parcel2, 0);
          parcel2.readException();
          param2Int1 = parcel2.readInt();
          if (param2Int1 != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean setProfileName(int param2Int, String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
          parcel1.writeInt(param2Int);
          parcel1.writeString(param2String);
          IBinder iBinder = this.mRemote;
          boolean bool = false;
          iBinder.transact(7, parcel1, parcel2, 0);
          parcel2.readException();
          param2Int = parcel2.readInt();
          if (param2Int != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
    }
  }
  
  private static class Proxy implements ICalibrationProfileService {
    private IBinder mRemote;
    
    Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public boolean clearProfile(int param1Int) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int);
        IBinder iBinder = this.mRemote;
        boolean bool = false;
        iBinder.transact(12, parcel1, parcel2, 0);
        parcel2.readException();
        param1Int = parcel2.readInt();
        if (param1Int != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public int getActiveProfile() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        this.mRemote.transact(4, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readInt();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public float[] getCameraToEyePose(int param1Int1, int param1Int2) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        this.mRemote.transact(8, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.createFloatArray();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public float[] getEyeProjection(int param1Int1, int param1Int2) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        this.mRemote.transact(9, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.createFloatArray();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getInterfaceDescriptor() {
      return "com.vuforia.eyewear.Calibration.service.ICalibrationProfileService";
    }
    
    public int getMaxProfileCount() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        this.mRemote.transact(1, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readInt();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getProfileName(int param1Int) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int);
        this.mRemote.transact(6, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public int getUsedProfileCount() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        this.mRemote.transact(2, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readInt();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean isProfileUsed(int param1Int) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int);
        IBinder iBinder = this.mRemote;
        boolean bool = false;
        iBinder.transact(3, parcel1, parcel2, 0);
        parcel2.readException();
        param1Int = parcel2.readInt();
        if (param1Int != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean setActiveProfile(int param1Int) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int);
        IBinder iBinder = this.mRemote;
        boolean bool = false;
        iBinder.transact(5, parcel1, parcel2, 0);
        parcel2.readException();
        param1Int = parcel2.readInt();
        if (param1Int != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean setCameraToEyePose(int param1Int1, int param1Int2, float[] param1ArrayOffloat) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        parcel1.writeFloatArray(param1ArrayOffloat);
        IBinder iBinder = this.mRemote;
        boolean bool = false;
        iBinder.transact(10, parcel1, parcel2, 0);
        parcel2.readException();
        param1Int1 = parcel2.readInt();
        if (param1Int1 != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean setEyeProjection(int param1Int1, int param1Int2, float[] param1ArrayOffloat) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        parcel1.writeFloatArray(param1ArrayOffloat);
        IBinder iBinder = this.mRemote;
        boolean bool = false;
        iBinder.transact(11, parcel1, parcel2, 0);
        parcel2.readException();
        param1Int1 = parcel2.readInt();
        if (param1Int1 != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean setProfileName(int param1Int, String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.vuforia.eyewear.Calibration.service.ICalibrationProfileService");
        parcel1.writeInt(param1Int);
        parcel1.writeString(param1String);
        IBinder iBinder = this.mRemote;
        boolean bool = false;
        iBinder.transact(7, parcel1, parcel2, 0);
        parcel2.readException();
        param1Int = parcel2.readInt();
        if (param1Int != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\eyewear\Calibration\service\ICalibrationProfileService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
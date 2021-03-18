package org.firstinspires.ftc.robotcore.internal.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.qualcomm.robotcore.util.RobotLog;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class AutoConfigGLSurfaceView extends GLSurfaceView {
  private static final String LOGTAG = "LocalizationGLView";
  
  public AutoConfigGLSurfaceView(Context paramContext) {
    super(paramContext);
  }
  
  private static void checkEglError(String paramString, EGL10 paramEGL10) {
    while (true) {
      int i = paramEGL10.eglGetError();
      if (i != 12288) {
        RobotLog.ee("LocalizationGLView", "%s: EGL error: 0x%x", new Object[] { paramString, Integer.valueOf(i) });
        continue;
      } 
      break;
    } 
  }
  
  public void init(boolean paramBoolean, int paramInt1, int paramInt2) {
    ConfigChooser configChooser;
    if (paramBoolean)
      getHolder().setFormat(-3); 
    setEGLContextFactory(new ContextFactory());
    if (paramBoolean) {
      configChooser = new ConfigChooser(8, 8, 8, 8, paramInt1, paramInt2);
    } else {
      configChooser = new ConfigChooser(5, 6, 5, 0, paramInt1, paramInt2);
    } 
    setEGLConfigChooser(configChooser);
  }
  
  private static class ConfigChooser implements GLSurfaceView.EGLConfigChooser {
    protected int mAlphaSize;
    
    protected int mBlueSize;
    
    protected int mDepthSize;
    
    protected int mGreenSize;
    
    protected int mRedSize;
    
    protected int mStencilSize;
    
    private int[] mValue = new int[1];
    
    public ConfigChooser(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
      this.mRedSize = param1Int1;
      this.mGreenSize = param1Int2;
      this.mBlueSize = param1Int3;
      this.mAlphaSize = param1Int4;
      this.mDepthSize = param1Int5;
      this.mStencilSize = param1Int6;
    }
    
    private int findConfigAttrib(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig, int param1Int1, int param1Int2) {
      return param1EGL10.eglGetConfigAttrib(param1EGLDisplay, param1EGLConfig, param1Int1, this.mValue) ? this.mValue[0] : param1Int2;
    }
    
    private EGLConfig getMatchingConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, int[] param1ArrayOfint) {
      int[] arrayOfInt = new int[1];
      param1EGL10.eglChooseConfig(param1EGLDisplay, param1ArrayOfint, null, 0, arrayOfInt);
      int i = arrayOfInt[0];
      if (i > 0) {
        EGLConfig[] arrayOfEGLConfig = new EGLConfig[i];
        param1EGL10.eglChooseConfig(param1EGLDisplay, param1ArrayOfint, arrayOfEGLConfig, i, arrayOfInt);
        return chooseConfig(param1EGL10, param1EGLDisplay, arrayOfEGLConfig);
      } 
      throw new IllegalArgumentException("No matching EGL configs");
    }
    
    public EGLConfig chooseConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay) {
      return getMatchingConfig(param1EGL10, param1EGLDisplay, new int[] { 12324, 4, 12323, 4, 12322, 4, 12352, 4, 12344 });
    }
    
    public EGLConfig chooseConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig[] param1ArrayOfEGLConfig) {
      int j = param1ArrayOfEGLConfig.length;
      int i;
      for (i = 0; i < j; i++) {
        EGLConfig eGLConfig = param1ArrayOfEGLConfig[i];
        int k = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12325, 0);
        int m = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12326, 0);
        if (k >= this.mDepthSize && m >= this.mStencilSize) {
          k = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12324, 0);
          m = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12323, 0);
          int n = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12322, 0);
          int i1 = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12321, 0);
          if (k == this.mRedSize && m == this.mGreenSize && n == this.mBlueSize && i1 == this.mAlphaSize)
            return eGLConfig; 
        } 
      } 
      return null;
    }
  }
  
  private static class ContextFactory implements GLSurfaceView.EGLContextFactory {
    private static int EGL_CONTEXT_CLIENT_VERSION = 12440;
    
    private ContextFactory() {}
    
    public EGLContext createContext(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig) {
      AutoConfigGLSurfaceView.checkEglError("Before eglCreateContext", param1EGL10);
      int i = EGL_CONTEXT_CLIENT_VERSION;
      EGLContext eGLContext = param1EGL10.eglCreateContext(param1EGLDisplay, param1EGLConfig, EGL10.EGL_NO_CONTEXT, new int[] { i, 2, 12344 });
      AutoConfigGLSurfaceView.checkEglError("After eglCreateContext", param1EGL10);
      return eGLContext;
    }
    
    public void destroyContext(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLContext param1EGLContext) {
      param1EGL10.eglDestroyContext(param1EGLDisplay, param1EGLContext);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\AutoConfigGLSurfaceView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
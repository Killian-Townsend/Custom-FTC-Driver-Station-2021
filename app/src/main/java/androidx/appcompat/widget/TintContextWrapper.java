package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TintContextWrapper extends ContextWrapper {
  private static final Object CACHE_LOCK = new Object();
  
  private static ArrayList<WeakReference<TintContextWrapper>> sCache;
  
  private final Resources mResources;
  
  private final Resources.Theme mTheme;
  
  private TintContextWrapper(Context paramContext) {
    super(paramContext);
    if (VectorEnabledTintResources.shouldBeUsed()) {
      VectorEnabledTintResources vectorEnabledTintResources = new VectorEnabledTintResources((Context)this, paramContext.getResources());
      this.mResources = vectorEnabledTintResources;
      Resources.Theme theme = vectorEnabledTintResources.newTheme();
      this.mTheme = theme;
      theme.setTo(paramContext.getTheme());
      return;
    } 
    this.mResources = new TintResources((Context)this, paramContext.getResources());
    this.mTheme = null;
  }
  
  private static boolean shouldWrap(Context paramContext) {
    boolean bool1 = paramContext instanceof TintContextWrapper;
    boolean bool = false;
    null = bool;
    if (!bool1) {
      null = bool;
      if (!(paramContext.getResources() instanceof TintResources)) {
        if (paramContext.getResources() instanceof VectorEnabledTintResources)
          return false; 
        if (Build.VERSION.SDK_INT >= 21) {
          null = bool;
          return VectorEnabledTintResources.shouldBeUsed() ? true : null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public static Context wrap(Context paramContext) {
    if (shouldWrap(paramContext))
      synchronized (CACHE_LOCK) {
        if (sCache == null) {
          sCache = new ArrayList<WeakReference<TintContextWrapper>>();
        } else {
          for (int i = sCache.size() - 1;; i--) {
            if (i >= 0) {
              WeakReference weakReference = sCache.get(i);
              if (weakReference == null || weakReference.get() == null)
                sCache.remove(i); 
            } else {
              for (i = sCache.size() - 1;; i--) {
                if (i >= 0) {
                  WeakReference<TintContextWrapper> weakReference = sCache.get(i);
                  if (weakReference != null) {
                    TintContextWrapper tintContextWrapper1 = weakReference.get();
                  } else {
                    weakReference = null;
                  } 
                  if (weakReference != null && weakReference.getBaseContext() == paramContext)
                    return (Context)weakReference; 
                } else {
                  tintContextWrapper = new TintContextWrapper(paramContext);
                  sCache.add(new WeakReference<TintContextWrapper>(tintContextWrapper));
                  return (Context)tintContextWrapper;
                } 
              } 
            } 
          } 
          i--;
        } 
        TintContextWrapper tintContextWrapper = new TintContextWrapper((Context)tintContextWrapper);
        sCache.add(new WeakReference<TintContextWrapper>(tintContextWrapper));
        return (Context)tintContextWrapper;
      }  
    return paramContext;
  }
  
  public AssetManager getAssets() {
    return this.mResources.getAssets();
  }
  
  public Resources getResources() {
    return this.mResources;
  }
  
  public Resources.Theme getTheme() {
    Resources.Theme theme2 = this.mTheme;
    Resources.Theme theme1 = theme2;
    if (theme2 == null)
      theme1 = super.getTheme(); 
    return theme1;
  }
  
  public void setTheme(int paramInt) {
    Resources.Theme theme = this.mTheme;
    if (theme == null) {
      super.setTheme(paramInt);
      return;
    } 
    theme.applyStyle(paramInt, true);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\appcompat\widget\TintContextWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
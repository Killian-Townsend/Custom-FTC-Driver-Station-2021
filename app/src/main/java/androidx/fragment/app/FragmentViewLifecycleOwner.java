package androidx.fragment.app;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

class FragmentViewLifecycleOwner implements LifecycleOwner {
  private LifecycleRegistry mLifecycleRegistry = null;
  
  public Lifecycle getLifecycle() {
    initialize();
    return (Lifecycle)this.mLifecycleRegistry;
  }
  
  void handleLifecycleEvent(Lifecycle.Event paramEvent) {
    this.mLifecycleRegistry.handleLifecycleEvent(paramEvent);
  }
  
  void initialize() {
    if (this.mLifecycleRegistry == null)
      this.mLifecycleRegistry = new LifecycleRegistry(this); 
  }
  
  boolean isInitialized() {
    return (this.mLifecycleRegistry != null);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\fragment\app\FragmentViewLifecycleOwner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
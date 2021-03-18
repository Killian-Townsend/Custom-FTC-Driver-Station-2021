package org.firstinspires.ftc.robotcore.internal.ui;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LocalByRefIntentExtraHolder implements Parcelable {
  public static final Parcelable.Creator<LocalByRefIntentExtraHolder> CREATOR;
  
  private static final Map<UUID, Object> map = new ConcurrentHashMap<UUID, Object>();
  
  protected UUID uuid;
  
  static {
    CREATOR = new Parcelable.Creator<LocalByRefIntentExtraHolder>() {
        public LocalByRefIntentExtraHolder createFromParcel(Parcel param1Parcel) {
          return new LocalByRefIntentExtraHolder(param1Parcel);
        }
        
        public LocalByRefIntentExtraHolder[] newArray(int param1Int) {
          return new LocalByRefIntentExtraHolder[param1Int];
        }
      };
  }
  
  private LocalByRefIntentExtraHolder(Parcel paramParcel) {
    this.uuid = UUID.fromString(paramParcel.readString());
  }
  
  public LocalByRefIntentExtraHolder(Object paramObject) {
    UUID uUID = UUID.randomUUID();
    this.uuid = uUID;
    map.put(uUID, paramObject);
  }
  
  public int describeContents() {
    return 0;
  }
  
  public Object getTargetAndForget() {
    Object object = map.get(this.uuid);
    map.remove(this.uuid);
    return object;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeString(this.uuid.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\LocalByRefIntentExtraHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
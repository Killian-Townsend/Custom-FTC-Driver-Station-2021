package com.google.gson;

public enum LongSerializationPolicy {
  DEFAULT {
    public JsonElement serialize(Long param1Long) {
      return new JsonPrimitive(param1Long);
    }
  },
  STRING;
  
  static {
    null  = new null("STRING", 1);
    STRING = ;
    $VALUES = new LongSerializationPolicy[] { DEFAULT,  };
  }
  
  public abstract JsonElement serialize(Long paramLong);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\LongSerializationPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
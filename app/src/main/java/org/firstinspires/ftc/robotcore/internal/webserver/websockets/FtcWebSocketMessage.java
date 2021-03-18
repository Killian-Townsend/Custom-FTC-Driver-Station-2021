package org.firstinspires.ftc.robotcore.internal.webserver.websockets;

import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public final class FtcWebSocketMessage {
  private static final String TAG = "FtcWebSocketMessage";
  
  private String namespace;
  
  private String payload = "";
  
  private String type;
  
  public FtcWebSocketMessage(String paramString1, String paramString2) {
    this(paramString1, paramString2, "");
  }
  
  public FtcWebSocketMessage(String paramString1, String paramString2, String paramString3) {
    this.namespace = paramString1;
    this.type = paramString2;
    this.payload = paramString3;
  }
  
  public static FtcWebSocketMessage fromJson(String paramString) {
    return (FtcWebSocketMessage)SimpleGson.getInstance().fromJson(paramString, FtcWebSocketMessage.class);
  }
  
  public String getNamespace() {
    return this.namespace;
  }
  
  public String getPayload() {
    return this.payload;
  }
  
  public String getType() {
    return this.type;
  }
  
  public boolean hasPayload() {
    return this.payload.isEmpty() ^ true;
  }
  
  public String toJson() {
    return SimpleGson.getInstance().toJson(this);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FtcWebSocketMessage{namespace='");
    stringBuilder.append(getNamespace());
    stringBuilder.append('\'');
    stringBuilder.append(", type='");
    stringBuilder.append(getType());
    stringBuilder.append('\'');
    stringBuilder.append(", payload='");
    stringBuilder.append(getPayload());
    stringBuilder.append('\'');
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\websockets\FtcWebSocketMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
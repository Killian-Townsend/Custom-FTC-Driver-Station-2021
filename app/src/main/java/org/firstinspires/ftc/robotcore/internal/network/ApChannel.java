package org.firstinspires.ftc.robotcore.internal.network;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.EnumSet;

public enum ApChannel {
  AUTO_2_4_GHZ(0),
  AUTO_5_GHZ(0),
  CHAN_1(0),
  CHAN_10(0),
  CHAN_100(0),
  CHAN_104(0),
  CHAN_108(0),
  CHAN_11(0),
  CHAN_112(0),
  CHAN_116(0),
  CHAN_120(0),
  CHAN_124(0),
  CHAN_128(0),
  CHAN_132(0),
  CHAN_136(0),
  CHAN_140(0),
  CHAN_149(0),
  CHAN_153(0),
  CHAN_157(0),
  CHAN_161(0),
  CHAN_165(0),
  CHAN_2(0),
  CHAN_3(0),
  CHAN_36(0),
  CHAN_4(0),
  CHAN_40(0),
  CHAN_44(0),
  CHAN_48(0),
  CHAN_5(0),
  CHAN_52(0),
  CHAN_56(0),
  CHAN_6(0),
  CHAN_60(0),
  CHAN_64(0),
  CHAN_7(0),
  CHAN_8(0),
  CHAN_9(0),
  UNKNOWN(-1, Band.BAND_2_4_GHZ, false);
  
  public static final EnumSet<ApChannel> ALL_2_4_GHZ_CHANNELS;
  
  public static final int AP_BAND_2GHZ = 0;
  
  public static final int AP_BAND_5GHZ = 1;
  
  private static final int CHANNEL_AUTO_SELECT = 0;
  
  private static final int LOWEST_5GHZ_CHANNEL = 36;
  
  public static final EnumSet<ApChannel> NON_DFS_5_GHZ_CHANNELS;
  
  private static final String UNKNOWN_DISPLAY_NAME = "unknown";
  
  public final Band band;
  
  public final int channelNum;
  
  public final boolean overlapsWithOtherChannels;
  
  static {
    AUTO_2_4_GHZ = new ApChannel("AUTO_2_4_GHZ", 1, 0, Band.BAND_2_4_GHZ, false);
    AUTO_5_GHZ = new ApChannel("AUTO_5_GHZ", 2, 0, Band.BAND_5_GHZ, false);
    CHAN_1 = new ApChannel("CHAN_1", 3, 1);
    CHAN_2 = new ApChannel("CHAN_2", 4, 2, true);
    CHAN_3 = new ApChannel("CHAN_3", 5, 3, true);
    CHAN_4 = new ApChannel("CHAN_4", 6, 4, true);
    CHAN_5 = new ApChannel("CHAN_5", 7, 5, true);
    CHAN_6 = new ApChannel("CHAN_6", 8, 6);
    CHAN_7 = new ApChannel("CHAN_7", 9, 7, true);
    CHAN_8 = new ApChannel("CHAN_8", 10, 8, true);
    CHAN_9 = new ApChannel("CHAN_9", 11, 9, true);
    CHAN_10 = new ApChannel("CHAN_10", 12, 10, true);
    CHAN_11 = new ApChannel("CHAN_11", 13, 11);
    CHAN_36 = new ApChannel("CHAN_36", 14, 36);
    CHAN_40 = new ApChannel("CHAN_40", 15, 40);
    CHAN_44 = new ApChannel("CHAN_44", 16, 44);
    CHAN_48 = new ApChannel("CHAN_48", 17, 48);
    CHAN_149 = new ApChannel("CHAN_149", 18, 149);
    CHAN_153 = new ApChannel("CHAN_153", 19, 153);
    CHAN_157 = new ApChannel("CHAN_157", 20, 157);
    CHAN_161 = new ApChannel("CHAN_161", 21, 161);
    CHAN_165 = new ApChannel("CHAN_165", 22, 165);
    CHAN_52 = new ApChannel("CHAN_52", 23, 52);
    CHAN_56 = new ApChannel("CHAN_56", 24, 56);
    CHAN_60 = new ApChannel("CHAN_60", 25, 60);
    CHAN_64 = new ApChannel("CHAN_64", 26, 64);
    CHAN_100 = new ApChannel("CHAN_100", 27, 100);
    CHAN_104 = new ApChannel("CHAN_104", 28, 104);
    CHAN_108 = new ApChannel("CHAN_108", 29, 108);
    CHAN_112 = new ApChannel("CHAN_112", 30, 112);
    CHAN_116 = new ApChannel("CHAN_116", 31, 116);
    CHAN_120 = new ApChannel("CHAN_120", 32, 120);
    CHAN_124 = new ApChannel("CHAN_124", 33, 124);
    CHAN_128 = new ApChannel("CHAN_128", 34, 128);
    CHAN_132 = new ApChannel("CHAN_132", 35, 132);
    CHAN_136 = new ApChannel("CHAN_136", 36, 136);
    ApChannel apChannel1 = new ApChannel("CHAN_140", 37, 140);
    CHAN_140 = apChannel1;
    ApChannel apChannel2 = UNKNOWN;
    ApChannel apChannel3 = AUTO_2_4_GHZ;
    ApChannel apChannel4 = AUTO_5_GHZ;
    ApChannel apChannel5 = CHAN_1;
    ApChannel apChannel6 = CHAN_2;
    ApChannel apChannel7 = CHAN_3;
    ApChannel apChannel8 = CHAN_4;
    ApChannel apChannel9 = CHAN_5;
    ApChannel apChannel10 = CHAN_6;
    ApChannel apChannel11 = CHAN_7;
    ApChannel apChannel12 = CHAN_8;
    ApChannel apChannel13 = CHAN_9;
    ApChannel apChannel14 = CHAN_10;
    ApChannel apChannel15 = CHAN_11;
    $VALUES = new ApChannel[] { 
        apChannel2, apChannel3, apChannel4, apChannel5, apChannel6, apChannel7, apChannel8, apChannel9, apChannel10, apChannel11, 
        apChannel12, apChannel13, apChannel14, apChannel15, CHAN_36, CHAN_40, CHAN_44, CHAN_48, CHAN_149, CHAN_153, 
        CHAN_157, CHAN_161, CHAN_165, CHAN_52, CHAN_56, CHAN_60, CHAN_64, CHAN_100, CHAN_104, CHAN_108, 
        CHAN_112, CHAN_116, CHAN_120, CHAN_124, CHAN_128, CHAN_132, CHAN_136, apChannel1 };
    ALL_2_4_GHZ_CHANNELS = EnumSet.range(apChannel5, apChannel15);
    NON_DFS_5_GHZ_CHANNELS = EnumSet.range(CHAN_36, CHAN_165);
  }
  
  ApChannel(int paramInt1, Band paramBand, boolean paramBoolean) {
    this.channelNum = paramInt1;
    this.band = paramBand;
    this.overlapsWithOtherChannels = paramBoolean;
  }
  
  public static ApChannel fromBandAndChannel(int paramInt1, int paramInt2) {
    for (ApChannel apChannel : values()) {
      if (apChannel.band.androidInternalValue == paramInt1 && apChannel.channelNum == paramInt2)
        return apChannel; 
    } 
    return UNKNOWN;
  }
  
  public static ApChannel fromName(String paramString) {
    ApChannel apChannel2 = Enum.<ApChannel>valueOf(ApChannel.class, paramString);
    ApChannel apChannel1 = apChannel2;
    if (apChannel2 == null)
      apChannel1 = UNKNOWN; 
    return apChannel1;
  }
  
  public String getDisplayName() {
    if (this == UNKNOWN)
      return "unknown"; 
    int i = this.channelNum;
    return (i == 0) ? ((this.band == Band.BAND_2_4_GHZ) ? "auto (2.4 GHz)" : "auto (5 GHz)") : String.valueOf(i);
  }
  
  public enum Band {
    BAND_2_4_GHZ(0),
    BAND_5_GHZ(0);
    
    public final int androidInternalValue;
    
    static {
      Band band = new Band("BAND_5_GHZ", 1, 1);
      BAND_5_GHZ = band;
      $VALUES = new Band[] { BAND_2_4_GHZ, band };
    }
    
    Band(int param1Int1) {
      this.androidInternalValue = param1Int1;
    }
  }
  
  public static class GsonTypeAdapter extends TypeAdapter<ApChannel> {
    public ApChannel read(JsonReader param1JsonReader) throws IOException {
      param1JsonReader.beginObject();
      ApChannel apChannel2 = null;
      while (param1JsonReader.hasNext()) {
        if ("name".equals(param1JsonReader.nextName())) {
          apChannel2 = ApChannel.fromName(param1JsonReader.nextString());
          continue;
        } 
        param1JsonReader.skipValue();
      } 
      param1JsonReader.endObject();
      ApChannel apChannel1 = apChannel2;
      if (apChannel2 == null)
        apChannel1 = ApChannel.UNKNOWN; 
      return apChannel1;
    }
    
    public void write(JsonWriter param1JsonWriter, ApChannel param1ApChannel) throws IOException {
      if (param1ApChannel == null) {
        param1JsonWriter.nullValue();
        return;
      } 
      param1JsonWriter.beginObject();
      param1JsonWriter.name("name");
      param1JsonWriter.value(param1ApChannel.name());
      param1JsonWriter.name("displayName");
      param1JsonWriter.value(param1ApChannel.getDisplayName());
      param1JsonWriter.name("band");
      param1JsonWriter.value(param1ApChannel.band.name());
      param1JsonWriter.name("overlapsWithOtherChannels");
      param1JsonWriter.value(param1ApChannel.overlapsWithOtherChannels);
      param1JsonWriter.endObject();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\ApChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.java_websocket.extensions;

import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.framing.Framedata;

public abstract class CompressionExtension extends DefaultExtension {
  public void isFrameValid(Framedata paramFramedata) throws InvalidDataException {
    if (!(paramFramedata instanceof org.java_websocket.framing.DataFrame) || (!paramFramedata.isRSV2() && !paramFramedata.isRSV3())) {
      if (paramFramedata instanceof org.java_websocket.framing.ControlFrame) {
        if (!paramFramedata.isRSV1() && !paramFramedata.isRSV2() && !paramFramedata.isRSV3())
          return; 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("bad rsv RSV1: ");
        stringBuilder1.append(paramFramedata.isRSV1());
        stringBuilder1.append(" RSV2: ");
        stringBuilder1.append(paramFramedata.isRSV2());
        stringBuilder1.append(" RSV3: ");
        stringBuilder1.append(paramFramedata.isRSV3());
        throw new InvalidFrameException(stringBuilder1.toString());
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bad rsv RSV1: ");
    stringBuilder.append(paramFramedata.isRSV1());
    stringBuilder.append(" RSV2: ");
    stringBuilder.append(paramFramedata.isRSV2());
    stringBuilder.append(" RSV3: ");
    stringBuilder.append(paramFramedata.isRSV3());
    throw new InvalidFrameException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\extensions\CompressionExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
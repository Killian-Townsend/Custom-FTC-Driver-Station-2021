package org.java_websocket.framing;

import java.nio.ByteBuffer;
import org.java_websocket.enums.Opcode;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.util.ByteBufferUtils;

public abstract class FramedataImpl1 implements Framedata {
  private boolean fin;
  
  private Opcode optcode;
  
  private boolean rsv1;
  
  private boolean rsv2;
  
  private boolean rsv3;
  
  private boolean transferemasked;
  
  private ByteBuffer unmaskedpayload;
  
  public FramedataImpl1(Opcode paramOpcode) {
    this.optcode = paramOpcode;
    this.unmaskedpayload = ByteBufferUtils.getEmptyByteBuffer();
    this.fin = true;
    this.transferemasked = false;
    this.rsv1 = false;
    this.rsv2 = false;
    this.rsv3 = false;
  }
  
  public static FramedataImpl1 get(Opcode paramOpcode) {
    if (paramOpcode != null) {
      switch (paramOpcode) {
        default:
          throw new IllegalArgumentException("Supplied opcode is invalid");
        case null:
          return new ContinuousFrame();
        case null:
          return new CloseFrame();
        case null:
          return new BinaryFrame();
        case null:
          return new TextFrame();
        case null:
          return new PongFrame();
        case null:
          break;
      } 
      return new PingFrame();
    } 
    throw new IllegalArgumentException("Supplied opcode cannot be null");
  }
  
  public void append(Framedata paramFramedata) {
    ByteBuffer byteBuffer = paramFramedata.getPayloadData();
    if (this.unmaskedpayload == null) {
      this.unmaskedpayload = ByteBuffer.allocate(byteBuffer.remaining());
      byteBuffer.mark();
      this.unmaskedpayload.put(byteBuffer);
      byteBuffer.reset();
    } else {
      byteBuffer.mark();
      ByteBuffer byteBuffer1 = this.unmaskedpayload;
      byteBuffer1.position(byteBuffer1.limit());
      byteBuffer1 = this.unmaskedpayload;
      byteBuffer1.limit(byteBuffer1.capacity());
      if (byteBuffer.remaining() > this.unmaskedpayload.remaining()) {
        byteBuffer1 = ByteBuffer.allocate(byteBuffer.remaining() + this.unmaskedpayload.capacity());
        this.unmaskedpayload.flip();
        byteBuffer1.put(this.unmaskedpayload);
        byteBuffer1.put(byteBuffer);
        this.unmaskedpayload = byteBuffer1;
      } else {
        this.unmaskedpayload.put(byteBuffer);
      } 
      this.unmaskedpayload.rewind();
      byteBuffer.reset();
    } 
    this.fin = paramFramedata.isFin();
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject != null) {
      if (getClass() != paramObject.getClass())
        return false; 
      FramedataImpl1 framedataImpl1 = (FramedataImpl1)paramObject;
      if (this.fin != framedataImpl1.fin)
        return false; 
      if (this.transferemasked != framedataImpl1.transferemasked)
        return false; 
      if (this.rsv1 != framedataImpl1.rsv1)
        return false; 
      if (this.rsv2 != framedataImpl1.rsv2)
        return false; 
      if (this.rsv3 != framedataImpl1.rsv3)
        return false; 
      if (this.optcode != framedataImpl1.optcode)
        return false; 
      paramObject = this.unmaskedpayload;
      ByteBuffer byteBuffer = framedataImpl1.unmaskedpayload;
      return (paramObject != null) ? paramObject.equals(byteBuffer) : ((byteBuffer == null));
    } 
    return false;
  }
  
  public Opcode getOpcode() {
    return this.optcode;
  }
  
  public ByteBuffer getPayloadData() {
    return this.unmaskedpayload;
  }
  
  public boolean getTransfereMasked() {
    return this.transferemasked;
  }
  
  public int hashCode() {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:632)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  public boolean isFin() {
    return this.fin;
  }
  
  public boolean isRSV1() {
    return this.rsv1;
  }
  
  public boolean isRSV2() {
    return this.rsv2;
  }
  
  public boolean isRSV3() {
    return this.rsv3;
  }
  
  public abstract void isValid() throws InvalidDataException;
  
  public void setFin(boolean paramBoolean) {
    this.fin = paramBoolean;
  }
  
  public void setPayload(ByteBuffer paramByteBuffer) {
    this.unmaskedpayload = paramByteBuffer;
  }
  
  public void setRSV1(boolean paramBoolean) {
    this.rsv1 = paramBoolean;
  }
  
  public void setRSV2(boolean paramBoolean) {
    this.rsv2 = paramBoolean;
  }
  
  public void setRSV3(boolean paramBoolean) {
    this.rsv3 = paramBoolean;
  }
  
  public void setTransferemasked(boolean paramBoolean) {
    this.transferemasked = paramBoolean;
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Framedata{ optcode:");
    stringBuilder.append(getOpcode());
    stringBuilder.append(", fin:");
    stringBuilder.append(isFin());
    stringBuilder.append(", rsv1:");
    stringBuilder.append(isRSV1());
    stringBuilder.append(", rsv2:");
    stringBuilder.append(isRSV2());
    stringBuilder.append(", rsv3:");
    stringBuilder.append(isRSV3());
    stringBuilder.append(", payloadlength:[pos:");
    stringBuilder.append(this.unmaskedpayload.position());
    stringBuilder.append(", len:");
    stringBuilder.append(this.unmaskedpayload.remaining());
    stringBuilder.append("], payload:");
    if (this.unmaskedpayload.remaining() > 1000) {
      str = "(too big to display)";
    } else {
      str = new String(this.unmaskedpayload.array());
    } 
    stringBuilder.append(str);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\FramedataImpl1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxSetMotorPIDFControlLoopCoefficientsCommand extends LynxDekaInterfaceCommand<LynxAck> {
  private static final int cbPayload = 19;
  
  private int d;
  
  private int f;
  
  private int i;
  
  private byte mode;
  
  private byte motor;
  
  private byte motorControlAlgorithm;
  
  private int p;
  
  public LynxSetMotorPIDFControlLoopCoefficientsCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetMotorPIDFControlLoopCoefficientsCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, DcMotor.RunMode paramRunMode, int paramInt2, int paramInt3, int paramInt4, int paramInt5, InternalMotorControlAlgorithm paramInternalMotorControlAlgorithm) {
    this(paramLynxModuleIntf);
    LynxConstants.validateMotorZ(paramInt1);
    this.motor = (byte)paramInt1;
    paramInt1 = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[paramRunMode.ordinal()];
    if (paramInt1 != 1) {
      if (paramInt1 == 2) {
        this.mode = 2;
      } else {
        throw new IllegalArgumentException(String.format("illegal mode: %s", new Object[] { paramRunMode.toString() }));
      } 
    } else {
      this.mode = 1;
    } 
    this.p = paramInt2;
    this.i = paramInt3;
    this.d = paramInt4;
    this.f = paramInt5;
    this.motorControlAlgorithm = paramInternalMotorControlAlgorithm.getValue();
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.motor = byteBuffer.get();
    this.mode = byteBuffer.get();
    this.p = byteBuffer.getInt();
    this.i = byteBuffer.getInt();
    this.d = byteBuffer.getInt();
    this.f = byteBuffer.getInt();
    this.motorControlAlgorithm = this.motorControlAlgorithm;
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(19).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.motor);
    byteBuffer.put(this.mode);
    byteBuffer.putInt(this.p);
    byteBuffer.putInt(this.i);
    byteBuffer.putInt(this.d);
    byteBuffer.putInt(this.f);
    byteBuffer.put(this.motorControlAlgorithm);
    return byteBuffer.array();
  }
  
  public enum InternalMotorControlAlgorithm {
    First(0),
    LegacyPID(0),
    Max(0),
    NotSet(0),
    PIDF(1);
    
    private byte value;
    
    static {
      InternalMotorControlAlgorithm internalMotorControlAlgorithm = new InternalMotorControlAlgorithm("NotSet", 4, 255);
      NotSet = internalMotorControlAlgorithm;
      $VALUES = new InternalMotorControlAlgorithm[] { First, LegacyPID, PIDF, Max, internalMotorControlAlgorithm };
    }
    
    InternalMotorControlAlgorithm(int param1Int1) {
      this.value = (byte)param1Int1;
    }
    
    public static InternalMotorControlAlgorithm fromByte(byte param1Byte) {
      return (param1Byte == LegacyPID.getValue()) ? LegacyPID : ((param1Byte == PIDF.getValue()) ? PIDF : NotSet);
    }
    
    public static InternalMotorControlAlgorithm fromExternal(MotorControlAlgorithm param1MotorControlAlgorithm) {
      int i = LynxSetMotorPIDFControlLoopCoefficientsCommand.null.$SwitchMap$com$qualcomm$robotcore$hardware$MotorControlAlgorithm[param1MotorControlAlgorithm.ordinal()];
      return (i != 1) ? ((i != 2) ? NotSet : PIDF) : LegacyPID;
    }
    
    public byte getValue() {
      return this.value;
    }
    
    public MotorControlAlgorithm toExternal() {
      int i = LynxSetMotorPIDFControlLoopCoefficientsCommand.null.$SwitchMap$com$qualcomm$hardware$lynx$commands$core$LynxSetMotorPIDFControlLoopCoefficientsCommand$InternalMotorControlAlgorithm[ordinal()];
      return (i != 1) ? ((i != 2) ? MotorControlAlgorithm.Unknown : MotorControlAlgorithm.PIDF) : MotorControlAlgorithm.LegacyPID;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetMotorPIDFControlLoopCoefficientsCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
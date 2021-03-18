package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public interface Machine {
  void auxCstArg(Constant paramConstant);
  
  void auxInitValues(ArrayList<Constant> paramArrayList);
  
  void auxIntArg(int paramInt);
  
  void auxSwitchArg(SwitchList paramSwitchList);
  
  void auxTargetArg(int paramInt);
  
  void auxType(Type paramType);
  
  void clearArgs();
  
  Prototype getPrototype();
  
  void localArg(Frame paramFrame, int paramInt);
  
  void localInfo(boolean paramBoolean);
  
  void localTarget(int paramInt, Type paramType, LocalItem paramLocalItem);
  
  void popArgs(Frame paramFrame, int paramInt);
  
  void popArgs(Frame paramFrame, Prototype paramPrototype);
  
  void popArgs(Frame paramFrame, Type paramType);
  
  void popArgs(Frame paramFrame, Type paramType1, Type paramType2);
  
  void popArgs(Frame paramFrame, Type paramType1, Type paramType2, Type paramType3);
  
  void run(Frame paramFrame, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\Machine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;

public abstract class RegisterMapper {
  public abstract int getNewRegisterCount();
  
  public abstract RegisterSpec map(RegisterSpec paramRegisterSpec);
  
  public final RegisterSpecList map(RegisterSpecList paramRegisterSpecList) {
    int j = paramRegisterSpecList.size();
    RegisterSpecList registerSpecList = new RegisterSpecList(j);
    for (int i = 0; i < j; i++)
      registerSpecList.set(i, map(paramRegisterSpecList.get(i))); 
    registerSpecList.setImmutable();
    return registerSpecList.equals(paramRegisterSpecList) ? paramRegisterSpecList : registerSpecList;
  }
  
  public final RegisterSpecSet map(RegisterSpecSet paramRegisterSpecSet) {
    int j = paramRegisterSpecSet.getMaxSize();
    RegisterSpecSet registerSpecSet = new RegisterSpecSet(getNewRegisterCount());
    for (int i = 0; i < j; i++) {
      RegisterSpec registerSpec = paramRegisterSpecSet.get(i);
      if (registerSpec != null)
        registerSpecSet.put(map(registerSpec)); 
    } 
    registerSpecSet.setImmutable();
    return registerSpecSet.equals(paramRegisterSpecSet) ? paramRegisterSpecSet : registerSpecSet;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\RegisterMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.BitIntSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ListIntSet;

public final class SetFactory {
  private static final int DOMFRONT_SET_THRESHOLD_SIZE = 3072;
  
  private static final int INTERFERENCE_SET_THRESHOLD_SIZE = 3072;
  
  private static final int LIVENESS_SET_THRESHOLD_SIZE = 3072;
  
  static IntSet makeDomFrontSet(int paramInt) {
    return (IntSet)((paramInt <= 3072) ? new BitIntSet(paramInt) : new ListIntSet());
  }
  
  public static IntSet makeInterferenceSet(int paramInt) {
    return (IntSet)((paramInt <= 3072) ? new BitIntSet(paramInt) : new ListIntSet());
  }
  
  static IntSet makeLivenessSet(int paramInt) {
    return (IntSet)((paramInt <= 3072) ? new BitIntSet(paramInt) : new ListIntSet());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\SetFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
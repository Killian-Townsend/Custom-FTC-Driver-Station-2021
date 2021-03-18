package org.firstinspires.ftc.robotcore.internal.camera;

import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.FieldPacker;
import androidx.renderscript.RSRuntimeException;
import androidx.renderscript.RenderScript;
import androidx.renderscript.Script;
import androidx.renderscript.ScriptC;

public class ScriptC_format_convert extends ScriptC {
  private static final String __rs_resource_name = "format_convert";
  
  private static final int mExportForEachIdx_yuv2_to_argb8888 = 1;
  
  private static final int mExportVarIdx_inputAllocation = 0;
  
  private static final int mExportVarIdx_outputHeight = 2;
  
  private static final int mExportVarIdx_outputWidth = 1;
  
  private Element __ALLOCATION;
  
  private Element __I32;
  
  private Element __U8_4;
  
  private FieldPacker __rs_fp_ALLOCATION;
  
  private FieldPacker __rs_fp_I32;
  
  private Allocation mExportVar_inputAllocation;
  
  private int mExportVar_outputHeight;
  
  private int mExportVar_outputWidth;
  
  public ScriptC_format_convert(RenderScript paramRenderScript) {
    super(paramRenderScript, "format_convert", format_convertBitCode.getBitCode32(), format_convertBitCode.getBitCode64());
    this.__ALLOCATION = Element.ALLOCATION(paramRenderScript);
    this.__I32 = Element.I32(paramRenderScript);
    this.__U8_4 = Element.U8_4(paramRenderScript);
  }
  
  public void forEach_yuv2_to_argb8888(Allocation paramAllocation) {
    forEach_yuv2_to_argb8888(paramAllocation, (Script.LaunchOptions)null);
  }
  
  public void forEach_yuv2_to_argb8888(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions) {
    if (paramAllocation.getType().getElement().isCompatible(this.__U8_4)) {
      forEach(1, (Allocation)null, paramAllocation, null, paramLaunchOptions);
      return;
    } 
    throw new RSRuntimeException("Type mismatch with U8_4!");
  }
  
  public Script.FieldID getFieldID_inputAllocation() {
    return createFieldID(0, null);
  }
  
  public Script.FieldID getFieldID_outputHeight() {
    return createFieldID(2, null);
  }
  
  public Script.FieldID getFieldID_outputWidth() {
    return createFieldID(1, null);
  }
  
  public Script.KernelID getKernelID_yuv2_to_argb8888() {
    return createKernelID(1, 58, null, null);
  }
  
  public Allocation get_inputAllocation() {
    return this.mExportVar_inputAllocation;
  }
  
  public int get_outputHeight() {
    return this.mExportVar_outputHeight;
  }
  
  public int get_outputWidth() {
    return this.mExportVar_outputWidth;
  }
  
  public void set_inputAllocation(Allocation paramAllocation) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: aload_1
    //   5: invokevirtual setVar : (ILandroidx/renderscript/BaseObj;)V
    //   8: aload_0
    //   9: aload_1
    //   10: putfield mExportVar_inputAllocation : Landroidx/renderscript/Allocation;
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  public void set_outputHeight(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_2
    //   4: iload_1
    //   5: invokevirtual setVar : (II)V
    //   8: aload_0
    //   9: iload_1
    //   10: putfield mExportVar_outputHeight : I
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  public void set_outputWidth(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: iload_1
    //   5: invokevirtual setVar : (II)V
    //   8: aload_0
    //   9: iload_1
    //   10: putfield mExportVar_outputWidth : I
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\ScriptC_format_convert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
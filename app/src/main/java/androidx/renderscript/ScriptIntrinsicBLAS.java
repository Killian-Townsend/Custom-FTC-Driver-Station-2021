package androidx.renderscript;

import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ScriptIntrinsicBLAS extends ScriptIntrinsic {
  public static final int CONJ_TRANSPOSE = 113;
  
  private static final int INTRINSIC_API_LEVEL = 23;
  
  public static final int LEFT = 141;
  
  public static final int LOWER = 122;
  
  public static final int NON_UNIT = 131;
  
  public static final int NO_TRANSPOSE = 111;
  
  public static final int RIGHT = 142;
  
  private static final int RsBlas_bnnm = 1000;
  
  private static final int RsBlas_caxpy = 29;
  
  private static final int RsBlas_ccopy = 28;
  
  private static final int RsBlas_cdotc_sub = 6;
  
  private static final int RsBlas_cdotu_sub = 5;
  
  private static final int RsBlas_cgbmv = 64;
  
  private static final int RsBlas_cgemm = 125;
  
  private static final int RsBlas_cgemv = 63;
  
  private static final int RsBlas_cgerc = 99;
  
  private static final int RsBlas_cgeru = 98;
  
  private static final int RsBlas_chbmv = 96;
  
  private static final int RsBlas_chemm = 137;
  
  private static final int RsBlas_chemv = 95;
  
  private static final int RsBlas_cher = 100;
  
  private static final int RsBlas_cher2 = 102;
  
  private static final int RsBlas_cher2k = 139;
  
  private static final int RsBlas_cherk = 138;
  
  private static final int RsBlas_chpmv = 97;
  
  private static final int RsBlas_chpr = 101;
  
  private static final int RsBlas_chpr2 = 103;
  
  private static final int RsBlas_cscal = 43;
  
  private static final int RsBlas_csscal = 45;
  
  private static final int RsBlas_cswap = 27;
  
  private static final int RsBlas_csymm = 126;
  
  private static final int RsBlas_csyr2k = 128;
  
  private static final int RsBlas_csyrk = 127;
  
  private static final int RsBlas_ctbmv = 66;
  
  private static final int RsBlas_ctbsv = 69;
  
  private static final int RsBlas_ctpmv = 67;
  
  private static final int RsBlas_ctpsv = 70;
  
  private static final int RsBlas_ctrmm = 129;
  
  private static final int RsBlas_ctrmv = 65;
  
  private static final int RsBlas_ctrsm = 130;
  
  private static final int RsBlas_ctrsv = 68;
  
  private static final int RsBlas_dasum = 12;
  
  private static final int RsBlas_daxpy = 26;
  
  private static final int RsBlas_dcopy = 25;
  
  private static final int RsBlas_ddot = 4;
  
  private static final int RsBlas_dgbmv = 56;
  
  private static final int RsBlas_dgemm = 119;
  
  private static final int RsBlas_dgemv = 55;
  
  private static final int RsBlas_dger = 90;
  
  private static final int RsBlas_dnrm2 = 11;
  
  private static final int RsBlas_drot = 39;
  
  private static final int RsBlas_drotg = 37;
  
  private static final int RsBlas_drotm = 40;
  
  private static final int RsBlas_drotmg = 38;
  
  private static final int RsBlas_dsbmv = 88;
  
  private static final int RsBlas_dscal = 42;
  
  private static final int RsBlas_dsdot = 2;
  
  private static final int RsBlas_dspmv = 89;
  
  private static final int RsBlas_dspr = 92;
  
  private static final int RsBlas_dspr2 = 94;
  
  private static final int RsBlas_dswap = 24;
  
  private static final int RsBlas_dsymm = 120;
  
  private static final int RsBlas_dsymv = 87;
  
  private static final int RsBlas_dsyr = 91;
  
  private static final int RsBlas_dsyr2 = 93;
  
  private static final int RsBlas_dsyr2k = 122;
  
  private static final int RsBlas_dsyrk = 121;
  
  private static final int RsBlas_dtbmv = 58;
  
  private static final int RsBlas_dtbsv = 61;
  
  private static final int RsBlas_dtpmv = 59;
  
  private static final int RsBlas_dtpsv = 62;
  
  private static final int RsBlas_dtrmm = 123;
  
  private static final int RsBlas_dtrmv = 57;
  
  private static final int RsBlas_dtrsm = 124;
  
  private static final int RsBlas_dtrsv = 60;
  
  private static final int RsBlas_dzasum = 16;
  
  private static final int RsBlas_dznrm2 = 15;
  
  private static final int RsBlas_icamax = 19;
  
  private static final int RsBlas_idamax = 18;
  
  private static final int RsBlas_isamax = 17;
  
  private static final int RsBlas_izamax = 20;
  
  private static final int RsBlas_sasum = 10;
  
  private static final int RsBlas_saxpy = 23;
  
  private static final int RsBlas_scasum = 14;
  
  private static final int RsBlas_scnrm2 = 13;
  
  private static final int RsBlas_scopy = 22;
  
  private static final int RsBlas_sdot = 3;
  
  private static final int RsBlas_sdsdot = 1;
  
  private static final int RsBlas_sgbmv = 48;
  
  private static final int RsBlas_sgemm = 113;
  
  private static final int RsBlas_sgemv = 47;
  
  private static final int RsBlas_sger = 82;
  
  private static final int RsBlas_snrm2 = 9;
  
  private static final int RsBlas_srot = 35;
  
  private static final int RsBlas_srotg = 33;
  
  private static final int RsBlas_srotm = 36;
  
  private static final int RsBlas_srotmg = 34;
  
  private static final int RsBlas_ssbmv = 80;
  
  private static final int RsBlas_sscal = 41;
  
  private static final int RsBlas_sspmv = 81;
  
  private static final int RsBlas_sspr = 84;
  
  private static final int RsBlas_sspr2 = 86;
  
  private static final int RsBlas_sswap = 21;
  
  private static final int RsBlas_ssymm = 114;
  
  private static final int RsBlas_ssymv = 79;
  
  private static final int RsBlas_ssyr = 83;
  
  private static final int RsBlas_ssyr2 = 85;
  
  private static final int RsBlas_ssyr2k = 116;
  
  private static final int RsBlas_ssyrk = 115;
  
  private static final int RsBlas_stbmv = 50;
  
  private static final int RsBlas_stbsv = 53;
  
  private static final int RsBlas_stpmv = 51;
  
  private static final int RsBlas_stpsv = 54;
  
  private static final int RsBlas_strmm = 117;
  
  private static final int RsBlas_strmv = 49;
  
  private static final int RsBlas_strsm = 118;
  
  private static final int RsBlas_strsv = 52;
  
  private static final int RsBlas_zaxpy = 32;
  
  private static final int RsBlas_zcopy = 31;
  
  private static final int RsBlas_zdotc_sub = 8;
  
  private static final int RsBlas_zdotu_sub = 7;
  
  private static final int RsBlas_zdscal = 46;
  
  private static final int RsBlas_zgbmv = 72;
  
  private static final int RsBlas_zgemm = 131;
  
  private static final int RsBlas_zgemv = 71;
  
  private static final int RsBlas_zgerc = 108;
  
  private static final int RsBlas_zgeru = 107;
  
  private static final int RsBlas_zhbmv = 105;
  
  private static final int RsBlas_zhemm = 140;
  
  private static final int RsBlas_zhemv = 104;
  
  private static final int RsBlas_zher = 109;
  
  private static final int RsBlas_zher2 = 111;
  
  private static final int RsBlas_zher2k = 142;
  
  private static final int RsBlas_zherk = 141;
  
  private static final int RsBlas_zhpmv = 106;
  
  private static final int RsBlas_zhpr = 110;
  
  private static final int RsBlas_zhpr2 = 112;
  
  private static final int RsBlas_zscal = 44;
  
  private static final int RsBlas_zswap = 30;
  
  private static final int RsBlas_zsymm = 132;
  
  private static final int RsBlas_zsyr2k = 134;
  
  private static final int RsBlas_zsyrk = 133;
  
  private static final int RsBlas_ztbmv = 74;
  
  private static final int RsBlas_ztbsv = 77;
  
  private static final int RsBlas_ztpmv = 75;
  
  private static final int RsBlas_ztpsv = 78;
  
  private static final int RsBlas_ztrmm = 135;
  
  private static final int RsBlas_ztrmv = 73;
  
  private static final int RsBlas_ztrsm = 136;
  
  private static final int RsBlas_ztrsv = 76;
  
  public static final int TRANSPOSE = 112;
  
  public static final int UNIT = 132;
  
  public static final int UPPER = 121;
  
  private Allocation mLUT;
  
  private ScriptIntrinsicBLAS(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicBLAS create(RenderScript paramRenderScript) {
    boolean bool;
    if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 23) {
      bool = true;
    } else {
      bool = false;
    } 
    ScriptIntrinsicBLAS scriptIntrinsicBLAS = new ScriptIntrinsicBLAS(paramRenderScript.nScriptIntrinsicCreate(13, Element.U32(paramRenderScript).getID(paramRenderScript), bool), paramRenderScript);
    scriptIntrinsicBLAS.setIncSupp(bool);
    return scriptIntrinsicBLAS;
  }
  
  static void validateConjTranspose(int paramInt) {
    if (paramInt != 111) {
      if (paramInt == 113)
        return; 
      throw new RSRuntimeException("Invalid transpose passed to BLAS");
    } 
  }
  
  static void validateDiag(int paramInt) {
    if (paramInt != 131) {
      if (paramInt == 132)
        return; 
      throw new RSRuntimeException("Invalid diag passed to BLAS");
    } 
  }
  
  static void validateGEMV(Element paramElement, int paramInt1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3, int paramInt3) {
    validateTranspose(paramInt1);
    int i = paramAllocation1.getType().getY();
    int j = paramAllocation1.getType().getX();
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation3.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation2.getType().getY() <= 1 && paramAllocation3.getType().getY() <= 1) {
        if (paramInt2 > 0 && paramInt3 > 0) {
          if (paramInt1 == 111) {
            paramInt2 = (j - 1) * paramInt2 + 1;
            paramInt1 = (i - 1) * paramInt3 + 1;
          } else {
            paramInt1 = (j - 1) * paramInt3 + 1;
            paramInt2 = (i - 1) * paramInt2 + 1;
          } 
          if (paramAllocation2.getType().getX() == paramInt2 && paramAllocation3.getType().getX() == paramInt1)
            return; 
          throw new RSRuntimeException("Incorrect vector dimensions for GEMV");
        } 
        throw new RSRuntimeException("Vector increments must be greater than 0");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateGER(Element paramElement, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    if (paramAllocation3.getType().getElement().isCompatible(paramElement) && paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation1.getType().getY() <= 1 && paramAllocation2.getType().getY() <= 1) {
        int i = paramAllocation3.getType().getY();
        int j = paramAllocation3.getType().getX();
        if (j >= 1 && i >= 1) {
          if (paramInt1 > 0 && paramInt2 > 0) {
            if (paramAllocation1.getType().getX() == (i - 1) * paramInt1 + 1) {
              if (paramAllocation2.getType().getX() == (j - 1) * paramInt2 + 1)
                return; 
              throw new RSRuntimeException("Incorrect vector dimensions for GER");
            } 
            throw new RSRuntimeException("Incorrect vector dimensions for GER");
          } 
          throw new RSRuntimeException("Vector increments must be greater than 0");
        } 
        throw new RSRuntimeException("M and N must be 1 or greater for GER");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateGERU(Element paramElement, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    if (paramAllocation3.getType().getElement().isCompatible(paramElement) && paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation1.getType().getY() <= 1 && paramAllocation2.getType().getY() <= 1) {
        int i = paramAllocation3.getType().getY();
        int j = paramAllocation3.getType().getX();
        if (paramInt1 > 0 && paramInt2 > 0) {
          if (paramAllocation1.getType().getX() == (i - 1) * paramInt1 + 1) {
            if (paramAllocation2.getType().getX() == (j - 1) * paramInt2 + 1)
              return; 
            throw new RSRuntimeException("Incorrect vector dimensions for GERU");
          } 
          throw new RSRuntimeException("Incorrect vector dimensions for GERU");
        } 
        throw new RSRuntimeException("Vector increments must be greater than 0");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateHEMM(Element paramElement, int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, Allocation paramAllocation3) {
    validateSide(paramInt);
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation3.getType().getElement().isCompatible(paramElement)) {
      int i = paramAllocation1.getType().getX();
      if (i == paramAllocation1.getType().getY()) {
        if ((paramInt != 141 || i == paramAllocation2.getType().getY()) && (paramInt != 142 || i == paramAllocation2.getType().getX())) {
          if (paramAllocation2.getType().getX() == paramAllocation3.getType().getX() && paramAllocation2.getType().getY() == paramAllocation3.getType().getY())
            return; 
          throw new RSRuntimeException("Called HEMM with mismatched B and C");
        } 
        throw new RSRuntimeException("Called HEMM with invalid B");
      } 
      throw new RSRuntimeException("Called HEMM with non-square A");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateHER2K(Element paramElement, int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, Allocation paramAllocation3) {
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation3.getType().getElement().isCompatible(paramElement)) {
      validateConjTranspose(paramInt);
      int i = paramAllocation3.getType().getX();
      if (i == paramAllocation3.getType().getY()) {
        if (paramInt == 111) {
          if (paramAllocation1.getType().getY() != i)
            throw new RSRuntimeException("Called HER2K with invalid matrices"); 
        } else if (paramAllocation1.getType().getX() != i) {
          throw new RSRuntimeException("Called HER2K with invalid matrices");
        } 
        if (paramAllocation1.getType().getX() == paramAllocation2.getType().getX() && paramAllocation1.getType().getY() == paramAllocation2.getType().getY())
          return; 
        throw new RSRuntimeException("Called HER2K with invalid A and B matrices");
      } 
      throw new RSRuntimeException("Called HER2K with non-square C");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateHERK(Element paramElement, int paramInt, Allocation paramAllocation1, Allocation paramAllocation2) {
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      validateConjTranspose(paramInt);
      int i = paramAllocation2.getType().getX();
      if (i == paramAllocation2.getType().getY()) {
        if (paramInt == 111) {
          if (i == paramAllocation1.getType().getY())
            return; 
          throw new RSRuntimeException("Called HERK with invalid A");
        } 
        if (i == paramAllocation1.getType().getX())
          return; 
        throw new RSRuntimeException("Called HERK with invalid A");
      } 
      throw new RSRuntimeException("Called HERK with non-square C");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateL3(Element paramElement, int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, Allocation paramAllocation3) {
    if ((paramAllocation1 == null || paramAllocation1.getType().getElement().isCompatible(paramElement)) && (paramAllocation2 == null || paramAllocation2.getType().getElement().isCompatible(paramElement)) && (paramAllocation3 == null || paramAllocation3.getType().getElement().isCompatible(paramElement))) {
      if (paramAllocation3 != null) {
        int j = paramAllocation3.getType().getY();
        int k = paramAllocation3.getType().getX();
        int i = -1;
        if (paramInt3 == 142) {
          if ((paramAllocation1 != null || paramAllocation2 == null) && (paramAllocation1 == null || paramAllocation2 != null)) {
            if (paramAllocation2 != null) {
              paramInt2 = paramAllocation1.getType().getY();
              paramInt1 = paramAllocation1.getType().getX();
            } else {
              paramInt2 = -1;
              paramInt1 = paramInt2;
            } 
            if (paramAllocation1 != null) {
              int m = paramAllocation2.getType().getY();
              i = paramAllocation2.getType().getX();
              paramInt3 = paramInt1;
              paramInt1 = i;
              i = paramInt2;
              paramInt2 = m;
            } else {
              paramInt3 = paramInt1;
              byte b = -1;
              paramInt1 = i;
              i = paramInt2;
              paramInt2 = b;
            } 
          } else {
            throw new RSRuntimeException("Provided Matrix A without Matrix B, or vice versa");
          } 
        } else {
          if (paramAllocation1 != null) {
            if (paramInt1 == 112 || paramInt1 == 113) {
              paramInt1 = paramAllocation1.getType().getY();
              paramInt3 = paramAllocation1.getType().getX();
            } else {
              paramInt3 = paramAllocation1.getType().getY();
              paramInt1 = paramAllocation1.getType().getX();
            } 
          } else {
            paramInt3 = -1;
            paramInt1 = paramInt3;
          } 
          if (paramAllocation2 != null) {
            if (paramInt2 == 112 || paramInt2 == 113) {
              int m = paramAllocation2.getType().getY();
              i = paramAllocation2.getType().getX();
              paramInt2 = paramInt3;
              paramInt3 = m;
            } else {
              i = paramAllocation2.getType().getY();
              paramInt2 = paramAllocation2.getType().getX();
              int m = paramInt3;
              paramInt3 = paramInt2;
              paramInt2 = m;
            } 
          } else {
            i = -1;
            paramInt2 = i;
            int m = paramInt3;
            paramInt3 = paramInt2;
            paramInt2 = m;
          } 
        } 
        if (paramAllocation1 != null && paramAllocation2 != null && paramAllocation3 != null) {
          if (paramInt1 == i && paramInt2 == j && paramInt3 == k)
            return; 
          throw new RSRuntimeException("Called BLAS with invalid dimensions");
        } 
        if (paramAllocation1 != null && paramAllocation3 != null) {
          if (j == k) {
            if (paramInt2 == j)
              return; 
            throw new RSRuntimeException("Called BLAS with invalid dimensions");
          } 
          throw new RSRuntimeException("Matrix C is not symmetric");
        } 
        if (paramAllocation1 != null && paramAllocation2 != null) {
          if (paramInt1 == i)
            return; 
          throw new RSRuntimeException("Called BLAS with invalid dimensions");
        } 
        return;
      } 
      throw new RSRuntimeException("Allocation C cannot be null");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static int validateSPMV(Element paramElement, int paramInt1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3, int paramInt3) {
    validateUplo(paramInt1);
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation3.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation2.getType().getY() <= 1 && paramAllocation3.getType().getY() <= 1) {
        if (paramAllocation1.getType().getY() <= 1) {
          paramInt1 = (int)Math.sqrt(paramAllocation1.getType().getX() * 2.0D);
          if (paramAllocation1.getType().getX() == (paramInt1 + 1) * paramInt1 / 2) {
            if (paramInt2 > 0 && paramInt3 > 0) {
              int i = paramInt1 - 1;
              if (paramAllocation2.getType().getX() == paramInt2 * i + 1) {
                if (paramAllocation3.getType().getX() == i * paramInt3 + 1)
                  return paramInt1; 
                throw new RSRuntimeException("Incorrect vector dimensions for SPMV");
              } 
              throw new RSRuntimeException("Incorrect vector dimensions for SPMV");
            } 
            throw new RSRuntimeException("Vector increments must be greater than 0");
          } 
          throw new RSRuntimeException("Invalid dimension for Ap");
        } 
        throw new RSRuntimeException("Ap must have a Y dimension of 0 or 1");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static int validateSPR(Element paramElement, int paramInt1, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    validateUplo(paramInt1);
    if (paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation1.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation1.getType().getY() <= 1) {
        if (paramAllocation2.getType().getY() <= 1) {
          paramInt1 = (int)Math.sqrt(paramAllocation2.getType().getX() * 2.0D);
          if (paramAllocation2.getType().getX() == (paramInt1 + 1) * paramInt1 / 2) {
            if (paramInt2 > 0) {
              if (paramAllocation1.getType().getX() == (paramInt1 - 1) * paramInt2 + 1)
                return paramInt1; 
              throw new RSRuntimeException("Incorrect vector dimensions for SPR");
            } 
            throw new RSRuntimeException("Vector increments must be greater than 0");
          } 
          throw new RSRuntimeException("Invalid dimension for Ap");
        } 
        throw new RSRuntimeException("Ap must have a Y dimension of 0 or 1");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static int validateSPR2(Element paramElement, int paramInt1, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    validateUplo(paramInt1);
    if (paramAllocation3.getType().getElement().isCompatible(paramElement) && paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation1.getType().getY() <= 1 && paramAllocation2.getType().getY() <= 1) {
        if (paramAllocation3.getType().getY() <= 1) {
          paramInt1 = (int)Math.sqrt(paramAllocation3.getType().getX() * 2.0D);
          if (paramAllocation3.getType().getX() == (paramInt1 + 1) * paramInt1 / 2) {
            if (paramInt2 > 0 && paramInt3 > 0) {
              int i = paramInt1 - 1;
              if (paramAllocation1.getType().getX() == paramInt2 * i + 1 && paramAllocation2.getType().getX() == i * paramInt3 + 1)
                return paramInt1; 
              throw new RSRuntimeException("Incorrect vector dimensions for SPR2");
            } 
            throw new RSRuntimeException("Vector increments must be greater than 0");
          } 
          throw new RSRuntimeException("Invalid dimension for Ap");
        } 
        throw new RSRuntimeException("Ap must have a Y dimension of 0 or 1");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static int validateSYMV(Element paramElement, int paramInt1, Allocation paramAllocation1, Allocation paramAllocation2, Allocation paramAllocation3, int paramInt2, int paramInt3) {
    validateUplo(paramInt1);
    paramInt1 = paramAllocation1.getType().getY();
    if (paramAllocation1.getType().getX() == paramInt1) {
      if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation3.getType().getElement().isCompatible(paramElement)) {
        if (paramAllocation2.getType().getY() <= 1 && paramAllocation3.getType().getY() <= 1) {
          if (paramInt2 > 0 && paramInt3 > 0) {
            int i = paramInt1 - 1;
            if (paramAllocation2.getType().getX() == paramInt2 * i + 1) {
              if (paramAllocation3.getType().getX() == i * paramInt3 + 1)
                return paramInt1; 
              throw new RSRuntimeException("Incorrect vector dimensions for SYMV");
            } 
            throw new RSRuntimeException("Incorrect vector dimensions for SYMV");
          } 
          throw new RSRuntimeException("Vector increments must be greater than 0");
        } 
        throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
      } 
      throw new RSRuntimeException("Called BLAS with wrong Element type");
    } 
    throw new RSRuntimeException("A must be a square matrix for SYMV");
  }
  
  static int validateSYR(Element paramElement, int paramInt1, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    validateUplo(paramInt1);
    if (paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation1.getType().getElement().isCompatible(paramElement)) {
      paramInt1 = paramAllocation2.getType().getX();
      if (paramAllocation1.getType().getY() <= 1) {
        if (paramInt1 == paramAllocation2.getType().getY()) {
          if (paramInt2 > 0) {
            if (paramAllocation1.getType().getX() == (paramInt1 - 1) * paramInt2 + 1)
              return paramInt1; 
            throw new RSRuntimeException("Incorrect vector dimensions for SYR");
          } 
          throw new RSRuntimeException("Vector increments must be greater than 0");
        } 
        throw new RSRuntimeException("A must be a symmetric matrix");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static int validateSYR2(Element paramElement, int paramInt1, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    validateUplo(paramInt1);
    if (paramAllocation3.getType().getElement().isCompatible(paramElement) && paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation1.getType().getY() <= 1 && paramAllocation2.getType().getY() <= 1) {
        paramInt1 = paramAllocation3.getType().getX();
        if (paramInt1 == paramAllocation3.getType().getY()) {
          if (paramInt2 > 0 && paramInt3 > 0) {
            int i = paramInt1 - 1;
            if (paramAllocation1.getType().getX() == paramInt2 * i + 1 && paramAllocation2.getType().getX() == i * paramInt3 + 1)
              return paramInt1; 
            throw new RSRuntimeException("Incorrect vector dimensions for SYR");
          } 
          throw new RSRuntimeException("Vector increments must be greater than 0");
        } 
        throw new RSRuntimeException("A must be a symmetric matrix");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateSYR2K(Element paramElement, int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, Allocation paramAllocation3) {
    validateTranspose(paramInt);
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement) && paramAllocation3.getType().getElement().isCompatible(paramElement)) {
      if (paramInt == 112) {
        paramInt = paramAllocation1.getType().getX();
      } else {
        paramInt = paramAllocation1.getType().getY();
      } 
      if (paramAllocation3.getType().getX() == paramInt && paramAllocation3.getType().getY() == paramInt) {
        if (paramAllocation1.getType().getX() == paramAllocation2.getType().getX() && paramAllocation1.getType().getY() == paramAllocation2.getType().getY())
          return; 
        throw new RSRuntimeException("Invalid A and B in SYR2K");
      } 
      throw new RSRuntimeException("Invalid symmetric matrix in SYR2K");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateSide(int paramInt) {
    if (paramInt != 141) {
      if (paramInt == 142)
        return; 
      throw new RSRuntimeException("Invalid side passed to BLAS");
    } 
  }
  
  static int validateTPMV(Element paramElement, int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTranspose(paramInt2);
    validateUplo(paramInt1);
    validateDiag(paramInt3);
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      if (paramAllocation2.getType().getY() <= 1) {
        if (paramAllocation1.getType().getY() <= 1) {
          paramInt1 = (int)Math.sqrt(paramAllocation1.getType().getX() * 2.0D);
          if (paramAllocation1.getType().getX() == (paramInt1 + 1) * paramInt1 / 2) {
            if (paramInt4 > 0) {
              if (paramAllocation2.getType().getX() == (paramInt1 - 1) * paramInt4 + 1)
                return paramInt1; 
              throw new RSRuntimeException("Incorrect vector dimensions for TPMV");
            } 
            throw new RSRuntimeException("Vector increments must be greater than 0");
          } 
          throw new RSRuntimeException("Invalid dimension for Ap");
        } 
        throw new RSRuntimeException("Ap must have a Y dimension of 0 or 1");
      } 
      throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateTRMM(Element paramElement, int paramInt1, int paramInt2, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateSide(paramInt1);
    validateTranspose(paramInt2);
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      paramInt2 = paramAllocation1.getType().getY();
      int i = paramAllocation1.getType().getX();
      if (paramInt2 == i) {
        int j = paramAllocation2.getType().getY();
        int k = paramAllocation2.getType().getX();
        if (paramInt1 == 141) {
          if (i == j)
            return; 
          throw new RSRuntimeException("Called TRMM with invalid matrices");
        } 
        if (k == paramInt2)
          return; 
        throw new RSRuntimeException("Called TRMM with invalid matrices");
      } 
      throw new RSRuntimeException("Called TRMM with a non-symmetric matrix A");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateTRMV(Element paramElement, int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTranspose(paramInt2);
    validateUplo(paramInt1);
    validateDiag(paramInt3);
    paramInt1 = paramAllocation1.getType().getY();
    if (paramAllocation1.getType().getX() == paramInt1) {
      if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
        if (paramAllocation2.getType().getY() <= 1) {
          if (paramInt4 > 0) {
            if (paramAllocation2.getType().getX() == (paramInt1 - 1) * paramInt4 + 1)
              return; 
            throw new RSRuntimeException("Incorrect vector dimensions for TRMV");
          } 
          throw new RSRuntimeException("Vector increments must be greater than 0");
        } 
        throw new RSRuntimeException("BLAS vectors must have Y dimension of 0 or 1");
      } 
      throw new RSRuntimeException("Called BLAS with wrong Element type");
    } 
    throw new RSRuntimeException("A must be a square matrix for TRMV");
  }
  
  static void validateTRSM(Element paramElement, int paramInt1, int paramInt2, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateSide(paramInt1);
    validateTranspose(paramInt2);
    if (paramAllocation1.getType().getElement().isCompatible(paramElement) && paramAllocation2.getType().getElement().isCompatible(paramElement)) {
      paramInt2 = paramAllocation1.getType().getX();
      if (paramInt2 == paramAllocation1.getType().getY()) {
        int i = paramAllocation2.getType().getY();
        int j = paramAllocation2.getType().getX();
        if (paramInt1 == 141) {
          if (paramInt2 == i)
            return; 
          throw new RSRuntimeException("Called TRSM with invalid matrix dimensions");
        } 
        if (paramInt2 == j)
          return; 
        throw new RSRuntimeException("Called TRSM with invalid matrix dimensions");
      } 
      throw new RSRuntimeException("Called TRSM with a non-symmetric matrix A");
    } 
    throw new RSRuntimeException("Called BLAS with wrong Element type");
  }
  
  static void validateTranspose(int paramInt) {
    if (paramInt != 111 && paramInt != 112) {
      if (paramInt == 113)
        return; 
      throw new RSRuntimeException("Invalid transpose passed to BLAS");
    } 
  }
  
  static void validateUplo(int paramInt) {
    if (paramInt != 121) {
      if (paramInt == 122)
        return; 
      throw new RSRuntimeException("Invalid uplo passed to BLAS");
    } 
  }
  
  public void BNNM(Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3, int paramInt3, int paramInt4) {
    validateL3(Element.U8(this.mRS), 111, 112, 0, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt1 >= 0 && paramInt1 <= 255) {
      if (paramInt2 >= 0 && paramInt2 <= 255) {
        long l3;
        int i = paramAllocation1.getType().getY();
        int j = paramAllocation2.getType().getY();
        int k = paramAllocation1.getType().getX();
        boolean bool = isIncSupp();
        long l1 = paramAllocation1.getID(this.mRS);
        long l2 = paramAllocation2.getID(this.mRS);
        long l4 = paramAllocation3.getID(this.mRS);
        if (bool) {
          l2 = getDummyAlloc(paramAllocation1);
          l1 = getDummyAlloc(paramAllocation2);
          l4 = getDummyAlloc(paramAllocation3);
          l3 = l1;
        } else {
          l3 = l2;
          l2 = l1;
        } 
        this.mRS.nScriptIntrinsicBLAS_BNNM(getID(this.mRS), i, j, k, l2, paramInt1, l3, paramInt2, l4, paramInt3, paramInt4, bool);
        return;
      } 
      throw new RSRuntimeException("Invalid b_offset passed to BNNM");
    } 
    throw new RSRuntimeException("Invalid a_offset passed to BNNM");
  }
  
  public void CGBMV(int paramInt1, int paramInt2, int paramInt3, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4, Float2 paramFloat22, Allocation paramAllocation3, int paramInt5) {
    validateGEMV(Element.F32_2(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt4, paramAllocation3, paramInt5);
    if (paramInt2 >= 0 && paramInt3 >= 0) {
      int i = paramAllocation1.getType().getY();
      int j = paramAllocation1.getType().getX();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 64, paramInt1, 0, 0, 0, 0, i, j, 0, paramFloat21.x, paramFloat21.y, l1, l2, paramFloat22.x, paramFloat22.y, l3, paramInt4, paramInt5, paramInt2, paramInt3, bool);
      return;
    } 
    throw new RSRuntimeException("KL and KU must be greater than or equal to 0");
  }
  
  public void CGEMM(int paramInt1, int paramInt2, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, Float2 paramFloat22, Allocation paramAllocation3) {
    int i;
    int j;
    int k;
    validateTranspose(paramInt1);
    validateTranspose(paramInt2);
    validateL3(Element.F32_2(this.mRS), paramInt1, paramInt2, 0, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt1 != 111) {
      i = paramAllocation1.getType().getX();
      j = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getY();
      j = paramAllocation1.getType().getX();
    } 
    if (paramInt2 != 111) {
      k = paramAllocation2.getType().getY();
    } else {
      k = paramAllocation2.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 125, paramInt1, paramInt2, 0, 0, 0, i, k, j, paramFloat21.x, paramFloat21.y, l1, l2, paramFloat22.x, paramFloat22.y, l3, 0, 0, 0, 0, bool);
  }
  
  public void CGEMV(int paramInt1, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Float2 paramFloat22, Allocation paramAllocation3, int paramInt3) {
    validateGEMV(Element.F32_2(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt2, paramAllocation3, paramInt3);
    int i = paramAllocation1.getType().getY();
    int j = paramAllocation1.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 63, paramInt1, 0, 0, 0, 0, i, j, 0, paramFloat21.x, paramFloat21.y, l1, l2, paramFloat22.x, paramFloat22.y, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void CGERC(Float2 paramFloat2, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    validateGERU(Element.F32_2(this.mRS), paramAllocation1, paramInt1, paramAllocation2, paramInt2, paramAllocation3);
    int i = paramAllocation3.getType().getY();
    int j = paramAllocation3.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 99, 0, 0, 0, 0, 0, i, j, 0, paramFloat2.x, paramFloat2.y, l2, l3, 0.0F, 0.0F, l1, paramInt1, paramInt2, 0, 0, bool);
  }
  
  public void CGERU(Float2 paramFloat2, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    validateGERU(Element.F32_2(this.mRS), paramAllocation1, paramInt1, paramAllocation2, paramInt2, paramAllocation3);
    int i = paramAllocation3.getType().getY();
    int j = paramAllocation3.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 98, 0, 0, 0, 0, 0, i, j, 0, paramFloat2.x, paramFloat2.y, l2, l3, 0.0F, 0.0F, l1, paramInt1, paramInt2, 0, 0, bool);
  }
  
  public void CHBMV(int paramInt1, int paramInt2, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt3, Float2 paramFloat22, Allocation paramAllocation3, int paramInt4) {
    int i = validateSYR2(Element.F32_2(this.mRS), paramInt1, paramAllocation2, paramInt3, paramAllocation3, paramInt4, paramAllocation1);
    if (paramInt2 >= 0) {
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 96, 0, 0, 0, paramInt1, 0, 0, i, paramInt2, paramFloat21.x, paramFloat21.y, l1, l2, paramFloat22.x, paramFloat22.y, l3, paramInt3, paramInt4, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be 0 or greater for HBMV");
  }
  
  public void CHEMM(int paramInt1, int paramInt2, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, Float2 paramFloat22, Allocation paramAllocation3) {
    long l3;
    long l4;
    validateUplo(paramInt2);
    validateHEMM(Element.F32_2(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l5 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l3 = getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
      l4 = l1;
      l5 = l2;
    } else {
      l4 = l2;
      l3 = l1;
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 137, 0, 0, paramInt1, paramInt2, 0, paramAllocation3.getType().getY(), paramAllocation3.getType().getX(), 0, paramFloat21.x, paramFloat21.y, l3, l4, paramFloat22.x, paramFloat22.y, l5, 0, 0, 0, 0, bool);
  }
  
  public void CHEMV(int paramInt1, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Float2 paramFloat22, Allocation paramAllocation3, int paramInt3) {
    int i = validateSYR2(Element.F32_2(this.mRS), paramInt1, paramAllocation2, paramInt2, paramAllocation3, paramInt3, paramAllocation1);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 95, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat21.x, paramFloat21.y, l1, l2, paramFloat22.x, paramFloat22.y, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void CHER(int paramInt1, float paramFloat, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSYR(Element.F32_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 100, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat, 0.0F, l2, 0L, 0.0F, 0.0F, l1, paramInt2, 0, 0, 0, bool);
  }
  
  public void CHER2(int paramInt1, Float2 paramFloat2, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSYR2(Element.F32_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 102, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat2.x, paramFloat2.y, l2, l3, 0.0F, 0.0F, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void CHER2K(int paramInt1, int paramInt2, Float2 paramFloat2, Allocation paramAllocation1, Allocation paramAllocation2, float paramFloat, Allocation paramAllocation3) {
    int i;
    validateUplo(paramInt1);
    validateHER2K(Element.F32_2(this.mRS), paramInt2, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt2 == 111) {
      i = paramAllocation1.getType().getX();
    } else {
      i = paramAllocation1.getType().getY();
    } 
    boolean bool = isIncSupp();
    paramAllocation1.getID(this.mRS);
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation3.getID(this.mRS);
    if (bool) {
      getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 139, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation3.getType().getX(), i, paramFloat2.x, paramFloat2.y, paramAllocation1.getID(this.mRS), l1, paramFloat, 0.0F, l2, 0, 0, 0, 0, bool);
  }
  
  public void CHERK(int paramInt1, int paramInt2, float paramFloat1, Allocation paramAllocation1, float paramFloat2, Allocation paramAllocation2) {
    int i;
    validateUplo(paramInt1);
    validateHERK(Element.F32_2(this.mRS), paramInt2, paramAllocation1, paramAllocation2);
    if (paramInt2 == 113) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 138, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation2.getType().getX(), i, paramFloat1, 0.0F, l1, 0L, paramFloat2, 0.0F, l2, 0, 0, 0, 0, bool);
  }
  
  public void CHPMV(int paramInt1, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Float2 paramFloat22, Allocation paramAllocation3, int paramInt3) {
    int i = validateSPR2(Element.F32_2(this.mRS), paramInt1, paramAllocation2, paramInt2, paramAllocation3, paramInt3, paramAllocation1);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 97, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat21.x, paramFloat21.y, l1, l2, paramFloat22.x, paramFloat22.y, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void CHPR(int paramInt1, float paramFloat, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSPR(Element.F32_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 101, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat, 0.0F, l2, 0L, 0.0F, 0.0F, l1, paramInt2, 0, 0, 0, bool);
  }
  
  public void CHPR2(int paramInt1, Float2 paramFloat2, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSPR2(Element.F32_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 103, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat2.x, paramFloat2.y, l2, l3, 0.0F, 0.0F, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void CSYMM(int paramInt1, int paramInt2, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, Float2 paramFloat22, Allocation paramAllocation3) {
    validateSide(paramInt1);
    validateUplo(paramInt2);
    if (paramAllocation1.getType().getX() == paramAllocation1.getType().getY()) {
      validateL3(Element.F32_2(this.mRS), 0, 0, paramInt1, paramAllocation1, paramAllocation2, paramAllocation3);
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 126, 0, 0, paramInt1, paramInt2, 0, paramAllocation3.getType().getY(), paramAllocation3.getType().getX(), 0, paramFloat21.x, paramFloat21.y, l1, l2, paramFloat22.x, paramFloat22.y, l3, 0, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Matrix A is not symmetric");
  }
  
  public void CSYR2K(int paramInt1, int paramInt2, Float2 paramFloat21, Allocation paramAllocation1, Allocation paramAllocation2, Float2 paramFloat22, Allocation paramAllocation3) {
    int i;
    long l3;
    long l4;
    validateUplo(paramInt1);
    validateSYR2K(Element.F32_2(this.mRS), paramInt2, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l5 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l3 = getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
      l4 = l1;
      l5 = l2;
    } else {
      l4 = l2;
      l3 = l1;
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 128, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation3.getType().getX(), i, paramFloat21.x, paramFloat21.y, l3, l4, paramFloat22.x, paramFloat22.y, l5, 0, 0, 0, 0, bool);
  }
  
  public void CSYRK(int paramInt1, int paramInt2, Float2 paramFloat21, Allocation paramAllocation1, Float2 paramFloat22, Allocation paramAllocation2) {
    int i;
    validateTranspose(paramInt2);
    validateUplo(paramInt1);
    validateL3(Element.F32_2(this.mRS), paramInt2, 0, 0, paramAllocation1, (Allocation)null, paramAllocation2);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l = paramAllocation1.getID(this.mRS);
    paramAllocation2.getID(this.mRS);
    if (bool) {
      l = getDummyAlloc(paramAllocation1);
      getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 127, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation2.getType().getX(), i, paramFloat21.x, paramFloat21.y, l, 0L, paramFloat22.x, paramFloat22.y, paramAllocation2.getID(this.mRS), 0, 0, 0, 0, bool);
  }
  
  public void CTBMV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    if (paramInt4 >= 0) {
      validateTRMV(Element.F32_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
      int i = paramAllocation1.getType().getY();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 66, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0F, 0.0F, l1, l2, 0.0F, 0.0F, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be greater than or equal to 0");
  }
  
  public void CTBSV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    validateTRMV(Element.F32_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
    int i = paramAllocation1.getType().getY();
    if (paramInt4 >= 0) {
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 69, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0F, 0.0F, l1, l2, 0.0F, 0.0F, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Number of diagonals must be positive");
  }
  
  public void CTPMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F32_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 67, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, 0.0F, l1, l2, 0.0F, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void CTPSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F32_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 70, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, 0.0F, l1, l2, 0.0F, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void CTRMM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Float2 paramFloat2, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRMM(Element.F32_2(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 129, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramFloat2.x, paramFloat2.y, l1, l2, 0.0F, 0.0F, 0L, 0, 0, 0, 0, bool);
  }
  
  public void CTRMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F32_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 65, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, 0.0F, l1, l2, 0.0F, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void CTRSM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Float2 paramFloat2, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRSM(Element.F32_2(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 130, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramFloat2.x, paramFloat2.y, l1, l2, 0.0F, 0.0F, 0L, 0, 0, 0, 0, bool);
  }
  
  public void CTRSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F32_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Complex(getID(this.mRS), 68, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, 0.0F, l1, l2, 0.0F, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void DGBMV(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4, double paramDouble2, Allocation paramAllocation3, int paramInt5) {
    validateGEMV(Element.F64(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt4, paramAllocation3, paramInt5);
    if (paramInt2 >= 0 && paramInt3 >= 0) {
      int i = paramAllocation1.getType().getY();
      int j = paramAllocation1.getType().getX();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 56, paramInt1, 0, 0, 0, 0, i, j, 0, paramDouble1, l1, l2, paramDouble2, l3, paramInt4, paramInt5, paramInt2, paramInt3, bool);
      return;
    } 
    throw new RSRuntimeException("KL and KU must be greater than or equal to 0");
  }
  
  public void DGEMM(int paramInt1, int paramInt2, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, double paramDouble2, Allocation paramAllocation3) {
    int i;
    int j;
    int k;
    validateTranspose(paramInt1);
    validateTranspose(paramInt2);
    validateL3(Element.F64(this.mRS), paramInt1, paramInt2, 0, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt1 != 111) {
      i = paramAllocation1.getType().getX();
      j = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getY();
      j = paramAllocation1.getType().getX();
    } 
    if (paramInt2 != 111) {
      k = paramAllocation2.getType().getY();
    } else {
      k = paramAllocation2.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 119, paramInt1, paramInt2, 0, 0, 0, i, k, j, paramDouble1, l1, l2, paramDouble2, l3, 0, 0, 0, 0, bool);
  }
  
  public void DGEMV(int paramInt1, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, double paramDouble2, Allocation paramAllocation3, int paramInt3) {
    validateGEMV(Element.F64(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt2, paramAllocation3, paramInt3);
    int i = paramAllocation1.getType().getY();
    int j = paramAllocation1.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 55, paramInt1, 0, 0, 0, 0, i, j, 0, paramDouble1, l1, l2, paramDouble2, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void DGER(double paramDouble, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    int i = paramAllocation3.getType().getY();
    int j = paramAllocation3.getType().getX();
    validateGER(Element.F64(this.mRS), paramAllocation1, paramInt1, paramAllocation2, paramInt2, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 90, 0, 0, 0, 0, 0, i, j, 0, paramDouble, l2, l3, 0.0D, l1, paramInt1, paramInt2, 0, 0, bool);
  }
  
  public void DSBMV(int paramInt1, int paramInt2, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt3, double paramDouble2, Allocation paramAllocation3, int paramInt4) {
    if (paramInt2 >= 0) {
      int i = validateSYMV(Element.F64(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramAllocation3, paramInt3, paramInt4);
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 88, 0, 0, 0, paramInt1, 0, 0, i, paramInt2, paramDouble1, l1, l2, paramDouble2, l3, paramInt3, paramInt4, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be greater than or equal to 0");
  }
  
  public void DSPMV(int paramInt1, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, double paramDouble2, Allocation paramAllocation3, int paramInt3) {
    int i = validateSPMV(Element.F64(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt2, paramAllocation3, paramInt3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 89, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble1, l1, l2, paramDouble2, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void DSPR(int paramInt1, double paramDouble, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSPR(Element.F64(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 92, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble, l2, l1, 0.0D, 0L, paramInt2, 0, 0, 0, bool);
  }
  
  public void DSPR2(int paramInt1, double paramDouble, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSPR2(Element.F64(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 94, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble, l2, l3, 0.0D, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void DSYMM(int paramInt1, int paramInt2, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, double paramDouble2, Allocation paramAllocation3) {
    validateSide(paramInt1);
    validateUplo(paramInt2);
    if (paramAllocation1.getType().getX() == paramAllocation1.getType().getY()) {
      validateL3(Element.F64(this.mRS), 0, 0, paramInt1, paramAllocation1, paramAllocation2, paramAllocation3);
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 120, 0, 0, paramInt1, paramInt2, 0, paramAllocation3.getType().getY(), paramAllocation3.getType().getX(), 0, paramDouble1, l1, l2, paramDouble2, l3, 0, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Matrix A is not symmetric");
  }
  
  public void DSYMV(int paramInt1, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, double paramDouble2, Allocation paramAllocation3, int paramInt3) {
    int i = validateSYMV(Element.F64(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramAllocation3, paramInt2, paramInt3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 87, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble1, l1, l2, paramDouble2, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void DSYR(int paramInt1, double paramDouble, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSYR(Element.F64(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 91, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble, l2, l1, 0.0D, 0L, paramInt2, 0, 0, 0, bool);
  }
  
  public void DSYR2(int paramInt1, double paramDouble, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSYR2(Element.F64(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 93, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble, l2, l3, 0.0D, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void DSYR2K(int paramInt1, int paramInt2, double paramDouble1, Allocation paramAllocation1, Allocation paramAllocation2, double paramDouble2, Allocation paramAllocation3) {
    int i;
    long l3;
    long l4;
    validateUplo(paramInt1);
    validateSYR2K(Element.F64(this.mRS), paramInt2, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l5 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l3 = getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
      l4 = l1;
      l5 = l2;
    } else {
      l4 = l2;
      l3 = l1;
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 122, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation3.getType().getX(), i, paramDouble1, l3, l4, paramDouble2, l5, 0, 0, 0, 0, bool);
  }
  
  public void DSYRK(int paramInt1, int paramInt2, double paramDouble1, Allocation paramAllocation1, double paramDouble2, Allocation paramAllocation2) {
    int i;
    validateTranspose(paramInt2);
    validateUplo(paramInt1);
    validateL3(Element.F64(this.mRS), paramInt2, 0, 0, paramAllocation1, (Allocation)null, paramAllocation2);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 121, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation2.getType().getX(), i, paramDouble1, l1, 0L, paramDouble2, l2, 0, 0, 0, 0, bool);
  }
  
  public void DTBMV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    if (paramInt4 >= 0) {
      validateTRMV(Element.F64(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
      int i = paramAllocation1.getType().getY();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 58, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0D, l1, l2, 0.0D, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be greater than or equal to 0");
  }
  
  public void DTBSV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    validateTRMV(Element.F64(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
    int i = paramAllocation1.getType().getY();
    if (paramInt4 >= 0) {
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 61, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0D, l1, l2, 0.0D, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Number of diagonals must be positive");
  }
  
  public void DTPMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F64(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 59, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, l1, l2, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void DTPSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F64(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 62, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, l1, l2, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void DTRMM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRMM(Element.F64(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 123, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramDouble, l1, l2, 0.0D, 0L, 0, 0, 0, 0, bool);
  }
  
  public void DTRMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F64(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 57, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, l1, l2, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void DTRSM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRSM(Element.F64(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 124, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramDouble, l1, l2, 0.0D, 0L, 0, 0, 0, 0, bool);
  }
  
  public void DTRSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F64(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Double(getID(this.mRS), 60, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, l1, l2, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void SGBMV(int paramInt1, int paramInt2, int paramInt3, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4, float paramFloat2, Allocation paramAllocation3, int paramInt5) {
    validateGEMV(Element.F32(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt4, paramAllocation3, paramInt5);
    if (paramInt2 >= 0 && paramInt3 >= 0) {
      int i = paramAllocation1.getType().getY();
      int j = paramAllocation1.getType().getX();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 48, paramInt1, 0, 0, 0, 0, i, j, 0, paramFloat1, l1, l2, paramFloat2, l3, paramInt4, paramInt5, paramInt2, paramInt3, bool);
      return;
    } 
    throw new RSRuntimeException("KL and KU must be greater than or equal to 0");
  }
  
  public void SGEMM(int paramInt1, int paramInt2, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, float paramFloat2, Allocation paramAllocation3) {
    int i;
    int j;
    int k;
    long l4;
    validateTranspose(paramInt1);
    validateTranspose(paramInt2);
    validateL3(Element.F32(this.mRS), paramInt1, paramInt2, 0, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt1 != 111) {
      i = paramAllocation1.getType().getX();
      j = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getY();
      j = paramAllocation1.getType().getX();
    } 
    if (paramInt2 != 111) {
      k = paramAllocation2.getType().getY();
    } else {
      k = paramAllocation2.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l2 = paramAllocation1.getID(this.mRS);
    long l1 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
      l4 = l2;
    } else {
      l4 = l1;
      l1 = l2;
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 113, paramInt1, paramInt2, 0, 0, 0, i, k, j, paramFloat1, l1, l4, paramFloat2, l3, 0, 0, 0, 0, bool);
  }
  
  public void SGEMV(int paramInt1, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, float paramFloat2, Allocation paramAllocation3, int paramInt3) {
    validateGEMV(Element.F32(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt2, paramAllocation3, paramInt3);
    int i = paramAllocation1.getType().getY();
    int j = paramAllocation1.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 47, paramInt1, 0, 0, 0, 0, i, j, 0, paramFloat1, l1, l2, paramFloat2, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void SGER(float paramFloat, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    int i = paramAllocation3.getType().getY();
    int j = paramAllocation3.getType().getX();
    validateGER(Element.F32(this.mRS), paramAllocation1, paramInt1, paramAllocation2, paramInt2, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 82, 0, 0, 0, 0, 0, i, j, 0, paramFloat, l2, l3, 0.0F, l1, paramInt1, paramInt2, 0, 0, bool);
  }
  
  public void SSBMV(int paramInt1, int paramInt2, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt3, float paramFloat2, Allocation paramAllocation3, int paramInt4) {
    if (paramInt2 >= 0) {
      int i = validateSYMV(Element.F32(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramAllocation3, paramInt3, paramInt4);
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 80, 0, 0, 0, paramInt1, 0, 0, i, paramInt2, paramFloat1, l1, l2, paramFloat2, l3, paramInt3, paramInt4, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be greater than or equal to 0");
  }
  
  public void SSPMV(int paramInt1, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, float paramFloat2, Allocation paramAllocation3, int paramInt3) {
    int i = validateSPMV(Element.F32(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt2, paramAllocation3, paramInt3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 81, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat1, l1, l2, paramFloat2, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void SSPR(int paramInt1, float paramFloat, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSPR(Element.F32(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 84, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat, l2, l1, 0.0F, 0L, paramInt2, 0, 0, 0, bool);
  }
  
  public void SSPR2(int paramInt1, float paramFloat, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSPR2(Element.F32(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 86, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat, l2, l3, 0.0F, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void SSYMM(int paramInt1, int paramInt2, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, float paramFloat2, Allocation paramAllocation3) {
    validateSide(paramInt1);
    validateUplo(paramInt2);
    if (paramAllocation1.getType().getX() == paramAllocation1.getType().getY()) {
      validateL3(Element.F32(this.mRS), 0, 0, paramInt1, paramAllocation1, paramAllocation2, paramAllocation3);
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 114, 0, 0, paramInt1, paramInt2, 0, paramAllocation3.getType().getY(), paramAllocation3.getType().getX(), 0, paramFloat1, l1, l2, paramFloat2, l3, 0, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Matrix A is not symmetric");
  }
  
  public void SSYMV(int paramInt1, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, float paramFloat2, Allocation paramAllocation3, int paramInt3) {
    int i = validateSYMV(Element.F32(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramAllocation3, paramInt2, paramInt3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 79, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat1, l1, l2, paramFloat2, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void SSYR(int paramInt1, float paramFloat, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSYR(Element.F32(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 83, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat, l2, l1, 0.0F, 0L, paramInt2, 0, 0, 0, bool);
  }
  
  public void SSYR2(int paramInt1, float paramFloat, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSYR2(Element.F32(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 85, 0, 0, 0, paramInt1, 0, 0, i, 0, paramFloat, l2, l3, 0.0F, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void SSYR2K(int paramInt1, int paramInt2, float paramFloat1, Allocation paramAllocation1, Allocation paramAllocation2, float paramFloat2, Allocation paramAllocation3) {
    int i;
    long l3;
    long l4;
    validateUplo(paramInt1);
    validateSYR2K(Element.F32(this.mRS), paramInt2, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l5 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l3 = getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
      l4 = l1;
      l5 = l2;
    } else {
      l4 = l2;
      l3 = l1;
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 116, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation3.getType().getX(), i, paramFloat1, l3, l4, paramFloat2, l5, 0, 0, 0, 0, bool);
  }
  
  public void SSYRK(int paramInt1, int paramInt2, float paramFloat1, Allocation paramAllocation1, float paramFloat2, Allocation paramAllocation2) {
    int i;
    validateTranspose(paramInt2);
    validateUplo(paramInt1);
    validateL3(Element.F32(this.mRS), paramInt2, 0, 0, paramAllocation1, (Allocation)null, paramAllocation2);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 115, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation2.getType().getX(), i, paramFloat1, l1, 0L, paramFloat2, l2, 0, 0, 0, 0, bool);
  }
  
  public void STBMV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    if (paramInt4 >= 0) {
      validateTRMV(Element.F32(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
      int i = paramAllocation1.getType().getY();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 50, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0F, l1, l2, 0.0F, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be greater than or equal to 0");
  }
  
  public void STBSV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    validateTRMV(Element.F32(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
    int i = paramAllocation1.getType().getY();
    if (paramInt4 >= 0) {
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 53, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0F, l1, l2, 0.0F, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Number of diagonals must be positive");
  }
  
  public void STPMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F32(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 51, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, l1, l2, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void STPSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F32(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 54, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, l1, l2, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void STRMM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRMM(Element.F32(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 117, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramFloat, l1, l2, 0.0F, 0L, 0, 0, 0, 0, bool);
  }
  
  public void STRMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F32(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 49, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, l1, l2, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void STRSM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRSM(Element.F32(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 118, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramFloat, l1, l2, 0.0F, 0L, 0, 0, 0, 0, bool);
  }
  
  public void STRSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F32(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Single(getID(this.mRS), 52, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0F, l1, l2, 0.0F, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void ZGBMV(int paramInt1, int paramInt2, int paramInt3, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4, Double2 paramDouble22, Allocation paramAllocation3, int paramInt5) {
    validateGEMV(Element.F64_2(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt4, paramAllocation3, paramInt5);
    if (paramInt2 >= 0 && paramInt3 >= 0) {
      int i = paramAllocation1.getType().getY();
      int j = paramAllocation1.getType().getX();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 72, paramInt1, 0, 0, 0, 0, i, j, 0, paramDouble21.x, paramDouble21.y, l1, l2, paramDouble22.x, paramDouble22.y, l3, paramInt4, paramInt5, paramInt2, paramInt3, bool);
      return;
    } 
    throw new RSRuntimeException("KL and KU must be greater than or equal to 0");
  }
  
  public void ZGEMM(int paramInt1, int paramInt2, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, Double2 paramDouble22, Allocation paramAllocation3) {
    int i;
    int j;
    int k;
    validateTranspose(paramInt1);
    validateTranspose(paramInt2);
    validateL3(Element.F64_2(this.mRS), paramInt1, paramInt2, 0, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt1 != 111) {
      i = paramAllocation1.getType().getX();
      j = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getY();
      j = paramAllocation1.getType().getX();
    } 
    if (paramInt2 != 111) {
      k = paramAllocation2.getType().getY();
    } else {
      k = paramAllocation2.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 131, paramInt1, paramInt2, 0, 0, 0, i, k, j, paramDouble21.x, paramDouble21.y, l1, l2, paramDouble22.x, paramDouble22.y, l3, 0, 0, 0, 0, bool);
  }
  
  public void ZGEMV(int paramInt1, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Double2 paramDouble22, Allocation paramAllocation3, int paramInt3) {
    validateGEMV(Element.F64_2(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramInt2, paramAllocation3, paramInt3);
    int i = paramAllocation1.getType().getY();
    int j = paramAllocation1.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 71, paramInt1, 0, 0, 0, 0, i, j, 0, paramDouble21.x, paramDouble21.y, l1, l2, paramDouble22.x, paramDouble22.y, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void ZGERC(Double2 paramDouble2, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    validateGERU(Element.F64_2(this.mRS), paramAllocation1, paramInt1, paramAllocation2, paramInt2, paramAllocation3);
    int i = paramAllocation3.getType().getY();
    int j = paramAllocation3.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 108, 0, 0, 0, 0, 0, i, j, 0, paramDouble2.x, paramDouble2.y, l2, l3, 0.0D, 0.0D, l1, paramInt1, paramInt2, 0, 0, bool);
  }
  
  public void ZGERU(Double2 paramDouble2, Allocation paramAllocation1, int paramInt1, Allocation paramAllocation2, int paramInt2, Allocation paramAllocation3) {
    validateGERU(Element.F64_2(this.mRS), paramAllocation1, paramInt1, paramAllocation2, paramInt2, paramAllocation3);
    int i = paramAllocation3.getType().getY();
    int j = paramAllocation3.getType().getX();
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 107, 0, 0, 0, 0, 0, i, j, 0, paramDouble2.x, paramDouble2.y, l2, l3, 0.0D, 0.0D, l1, paramInt1, paramInt2, 0, 0, bool);
  }
  
  public void ZHBMV(int paramInt1, int paramInt2, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt3, Double2 paramDouble22, Allocation paramAllocation3, int paramInt4) {
    int i = validateSYR2(Element.F64_2(this.mRS), paramInt1, paramAllocation2, paramInt3, paramAllocation3, paramInt4, paramAllocation1);
    if (paramInt2 >= 0) {
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 105, 0, 0, 0, paramInt1, 0, 0, i, paramInt2, paramDouble21.x, paramDouble21.y, l1, l2, paramDouble22.x, paramDouble22.y, l3, paramInt3, paramInt4, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be 0 or greater for HBMV");
  }
  
  public void ZHEMM(int paramInt1, int paramInt2, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, Double2 paramDouble22, Allocation paramAllocation3) {
    long l3;
    long l4;
    validateUplo(paramInt2);
    validateHEMM(Element.F64_2(this.mRS), paramInt1, paramAllocation1, paramAllocation2, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l5 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l3 = getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
      l4 = l1;
      l5 = l2;
    } else {
      l4 = l2;
      l3 = l1;
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 140, 0, 0, paramInt1, paramInt2, 0, paramAllocation3.getType().getY(), paramAllocation3.getType().getX(), 0, paramDouble21.x, paramDouble21.y, l3, l4, paramDouble22.x, paramDouble22.y, l5, 0, 0, 0, 0, bool);
  }
  
  public void ZHEMV(int paramInt1, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Double2 paramDouble22, Allocation paramAllocation3, int paramInt3) {
    int i = validateSYR2(Element.F64_2(this.mRS), paramInt1, paramAllocation2, paramInt2, paramAllocation3, paramInt3, paramAllocation1);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 104, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble21.x, paramDouble21.y, l1, l2, paramDouble22.x, paramDouble22.y, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void ZHER(int paramInt1, double paramDouble, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSYR(Element.F64_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 109, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble, 0.0D, l2, 0L, 0.0D, 0.0D, l1, paramInt2, 0, 0, 0, bool);
  }
  
  public void ZHER2(int paramInt1, Double2 paramDouble2, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSYR2(Element.F64_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 111, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble2.x, paramDouble2.y, l2, l3, 0.0D, 0.0D, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void ZHER2K(int paramInt1, int paramInt2, Double2 paramDouble2, Allocation paramAllocation1, Allocation paramAllocation2, double paramDouble, Allocation paramAllocation3) {
    int i;
    validateUplo(paramInt1);
    validateHER2K(Element.F64_2(this.mRS), paramInt2, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt2 == 111) {
      i = paramAllocation1.getType().getX();
    } else {
      i = paramAllocation1.getType().getY();
    } 
    boolean bool = isIncSupp();
    paramAllocation1.getID(this.mRS);
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation3.getID(this.mRS);
    if (bool) {
      getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 142, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation3.getType().getX(), i, paramDouble2.x, paramDouble2.y, paramAllocation1.getID(this.mRS), l1, paramDouble, 0.0D, l2, 0, 0, 0, 0, bool);
  }
  
  public void ZHERK(int paramInt1, int paramInt2, double paramDouble1, Allocation paramAllocation1, double paramDouble2, Allocation paramAllocation2) {
    int i;
    validateUplo(paramInt1);
    validateHERK(Element.F64_2(this.mRS), paramInt2, paramAllocation1, paramAllocation2);
    if (paramInt2 == 113) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 141, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation2.getType().getX(), i, paramDouble1, 0.0D, l1, 0L, paramDouble2, 0.0D, l2, 0, 0, 0, 0, bool);
  }
  
  public void ZHPMV(int paramInt1, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt2, Double2 paramDouble22, Allocation paramAllocation3, int paramInt3) {
    int i = validateSPR2(Element.F64_2(this.mRS), paramInt1, paramAllocation2, paramInt2, paramAllocation3, paramInt3, paramAllocation1);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l3 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
      l3 = getDummyAlloc(paramAllocation3);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 106, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble21.x, paramDouble21.y, l1, l2, paramDouble22.x, paramDouble22.y, l3, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void ZHPR(int paramInt1, double paramDouble, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2) {
    int i = validateSPR(Element.F64_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation2.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation1);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 110, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble, 0.0D, l2, 0L, 0.0D, 0.0D, l1, paramInt2, 0, 0, 0, bool);
  }
  
  public void ZHPR2(int paramInt1, Double2 paramDouble2, Allocation paramAllocation1, int paramInt2, Allocation paramAllocation2, int paramInt3, Allocation paramAllocation3) {
    int i = validateSPR2(Element.F64_2(this.mRS), paramInt1, paramAllocation1, paramInt2, paramAllocation2, paramInt3, paramAllocation3);
    boolean bool = isIncSupp();
    long l1 = paramAllocation3.getID(this.mRS);
    long l2 = paramAllocation1.getID(this.mRS);
    long l3 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation3);
      l2 = getDummyAlloc(paramAllocation1);
      l3 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 112, 0, 0, 0, paramInt1, 0, 0, i, 0, paramDouble2.x, paramDouble2.y, l2, l3, 0.0D, 0.0D, l1, paramInt2, paramInt3, 0, 0, bool);
  }
  
  public void ZSYMM(int paramInt1, int paramInt2, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, Double2 paramDouble22, Allocation paramAllocation3) {
    validateSide(paramInt1);
    validateUplo(paramInt2);
    if (paramAllocation1.getType().getX() == paramAllocation1.getType().getY()) {
      validateL3(Element.F64_2(this.mRS), 0, 0, paramInt1, paramAllocation1, paramAllocation2, paramAllocation3);
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      long l3 = paramAllocation3.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        l3 = getDummyAlloc(paramAllocation3);
      } 
      this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 132, 0, 0, paramInt1, paramInt2, 0, paramAllocation3.getType().getY(), paramAllocation3.getType().getX(), 0, paramDouble21.x, paramDouble21.y, l1, l2, paramDouble22.x, paramDouble22.y, l3, 0, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Matrix A is not symmetric");
  }
  
  public void ZSYR2K(int paramInt1, int paramInt2, Double2 paramDouble21, Allocation paramAllocation1, Allocation paramAllocation2, Double2 paramDouble22, Allocation paramAllocation3) {
    int i;
    long l3;
    long l4;
    validateUplo(paramInt1);
    validateSYR2K(Element.F64_2(this.mRS), paramInt2, paramAllocation1, paramAllocation2, paramAllocation3);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    long l5 = paramAllocation3.getID(this.mRS);
    if (bool) {
      l3 = getDummyAlloc(paramAllocation1);
      l1 = getDummyAlloc(paramAllocation2);
      l2 = getDummyAlloc(paramAllocation3);
      l4 = l1;
      l5 = l2;
    } else {
      l4 = l2;
      l3 = l1;
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 134, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation3.getType().getX(), i, paramDouble21.x, paramDouble21.y, l3, l4, paramDouble22.x, paramDouble22.y, l5, 0, 0, 0, 0, bool);
  }
  
  public void ZSYRK(int paramInt1, int paramInt2, Double2 paramDouble21, Allocation paramAllocation1, Double2 paramDouble22, Allocation paramAllocation2) {
    int i;
    validateTranspose(paramInt2);
    validateUplo(paramInt1);
    validateL3(Element.F64_2(this.mRS), paramInt2, 0, 0, paramAllocation1, (Allocation)null, paramAllocation2);
    if (paramInt2 != 111) {
      i = paramAllocation1.getType().getY();
    } else {
      i = paramAllocation1.getType().getX();
    } 
    boolean bool = isIncSupp();
    long l = paramAllocation1.getID(this.mRS);
    paramAllocation2.getID(this.mRS);
    if (bool) {
      l = getDummyAlloc(paramAllocation1);
      getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 133, paramInt2, 0, 0, paramInt1, 0, 0, paramAllocation2.getType().getX(), i, paramDouble21.x, paramDouble21.y, l, 0L, paramDouble22.x, paramDouble22.y, paramAllocation2.getID(this.mRS), 0, 0, 0, 0, bool);
  }
  
  public void ZTBMV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    if (paramInt4 >= 0) {
      validateTRMV(Element.F64_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
      int i = paramAllocation1.getType().getY();
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 74, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0D, 0.0D, l1, l2, 0.0D, 0.0D, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("K must be greater than or equal to 0");
  }
  
  public void ZTBSV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt5) {
    validateTRMV(Element.F64_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt5);
    int i = paramAllocation1.getType().getY();
    if (paramInt4 >= 0) {
      boolean bool = isIncSupp();
      long l1 = paramAllocation1.getID(this.mRS);
      long l2 = paramAllocation2.getID(this.mRS);
      if (bool) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
      } 
      this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 77, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, paramInt4, 0.0D, 0.0D, l1, l2, 0.0D, 0.0D, 0L, paramInt5, 0, 0, 0, bool);
      return;
    } 
    throw new RSRuntimeException("Number of diagonals must be positive");
  }
  
  public void ZTPMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F64_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 75, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, 0.0D, l1, l2, 0.0D, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void ZTPSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    int i = validateTPMV(Element.F64_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 78, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, 0.0D, l1, l2, 0.0D, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void ZTRMM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Double2 paramDouble2, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRMM(Element.F64_2(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 135, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramDouble2.x, paramDouble2.y, l1, l2, 0.0D, 0.0D, 0L, 0, 0, 0, 0, bool);
  }
  
  public void ZTRMV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F64_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 73, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, 0.0D, l1, l2, 0.0D, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  public void ZTRSM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Double2 paramDouble2, Allocation paramAllocation1, Allocation paramAllocation2) {
    validateUplo(paramInt2);
    validateDiag(paramInt4);
    validateTRSM(Element.F64_2(this.mRS), paramInt1, paramInt3, paramAllocation1, paramAllocation2);
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 136, paramInt3, 0, paramInt1, paramInt2, paramInt4, paramAllocation2.getType().getY(), paramAllocation2.getType().getX(), 0, paramDouble2.x, paramDouble2.y, l1, l2, 0.0D, 0.0D, 0L, 0, 0, 0, 0, bool);
  }
  
  public void ZTRSV(int paramInt1, int paramInt2, int paramInt3, Allocation paramAllocation1, Allocation paramAllocation2, int paramInt4) {
    validateTRMV(Element.F64_2(this.mRS), paramInt1, paramInt2, paramInt3, paramAllocation1, paramAllocation2, paramInt4);
    int i = paramAllocation1.getType().getY();
    boolean bool = isIncSupp();
    long l1 = paramAllocation1.getID(this.mRS);
    long l2 = paramAllocation2.getID(this.mRS);
    if (bool) {
      l1 = getDummyAlloc(paramAllocation1);
      l2 = getDummyAlloc(paramAllocation2);
    } 
    this.mRS.nScriptIntrinsicBLAS_Z(getID(this.mRS), 76, paramInt2, 0, 0, paramInt1, paramInt3, 0, i, 0, 0.0D, 0.0D, l1, l2, 0.0D, 0.0D, 0L, paramInt4, 0, 0, 0, bool);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Diag {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Side {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Transpose {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Uplo {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicBLAS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
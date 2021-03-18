package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.io.InputStream;

public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
  public Typeface createFromFontFamilyFilesResourceEntry(Context paramContext, FontResourcesParserCompat.FontFamilyFilesResourceEntry paramFontFamilyFilesResourceEntry, Resources paramResources, int paramInt) {
    FontResourcesParserCompat.FontFileResourceEntry[] arrayOfFontFileResourceEntry = paramFontFamilyFilesResourceEntry.getEntries();
    int j = arrayOfFontFileResourceEntry.length;
    boolean bool = false;
    paramContext = null;
    int i = 0;
    while (true) {
      FontFamily.Builder builder;
      boolean bool1 = true;
      if (i < j) {
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = arrayOfFontFileResourceEntry[i];
        try {
          FontFamily.Builder builder1;
          Font.Builder builder2 = (new Font.Builder(paramResources, fontFileResourceEntry.getResourceId())).setWeight(fontFileResourceEntry.getWeight());
          if (!fontFileResourceEntry.isItalic())
            bool1 = false; 
          Font font = builder2.setSlant(bool1).setTtcIndex(fontFileResourceEntry.getTtcIndex()).setFontVariationSettings(fontFileResourceEntry.getVariationSettings()).build();
          if (paramContext == null) {
            builder1 = new FontFamily.Builder(font);
            builder = builder1;
          } else {
            builder.addFont((Font)builder1);
          } 
        } catch (IOException iOException) {}
        i++;
        continue;
      } 
      if (builder == null)
        return null; 
      if ((paramInt & 0x1) != 0) {
        i = 700;
      } else {
        i = 400;
      } 
      bool1 = bool;
      if ((paramInt & 0x2) != 0)
        bool1 = true; 
      FontStyle fontStyle = new FontStyle(i, bool1);
      return (new Typeface.CustomFallbackBuilder(builder.build())).setStyle(fontStyle).build();
    } 
  }
  
  public Typeface createFromFontInfo(Context paramContext, CancellationSignal paramCancellationSignal, FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   4: astore #10
    //   6: aload_3
    //   7: arraylength
    //   8: istore #8
    //   10: iconst_0
    //   11: istore #7
    //   13: aconst_null
    //   14: astore_1
    //   15: iconst_0
    //   16: istore #5
    //   18: iconst_1
    //   19: istore #6
    //   21: iload #5
    //   23: iload #8
    //   25: if_icmpge -> 213
    //   28: aload_3
    //   29: iload #5
    //   31: aaload
    //   32: astore #12
    //   34: aload_1
    //   35: astore #9
    //   37: aload #10
    //   39: aload #12
    //   41: invokevirtual getUri : ()Landroid/net/Uri;
    //   44: ldc 'r'
    //   46: aload_2
    //   47: invokevirtual openFileDescriptor : (Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
    //   50: astore #11
    //   52: aload #11
    //   54: ifnonnull -> 79
    //   57: aload_1
    //   58: astore #9
    //   60: aload #11
    //   62: ifnull -> 201
    //   65: aload_1
    //   66: astore #9
    //   68: aload #11
    //   70: invokevirtual close : ()V
    //   73: aload_1
    //   74: astore #9
    //   76: goto -> 201
    //   79: new android/graphics/fonts/Font$Builder
    //   82: dup
    //   83: aload #11
    //   85: invokespecial <init> : (Landroid/os/ParcelFileDescriptor;)V
    //   88: aload #12
    //   90: invokevirtual getWeight : ()I
    //   93: invokevirtual setWeight : (I)Landroid/graphics/fonts/Font$Builder;
    //   96: astore #9
    //   98: aload #12
    //   100: invokevirtual isItalic : ()Z
    //   103: ifeq -> 288
    //   106: goto -> 109
    //   109: aload #9
    //   111: iload #6
    //   113: invokevirtual setSlant : (I)Landroid/graphics/fonts/Font$Builder;
    //   116: aload #12
    //   118: invokevirtual getTtcIndex : ()I
    //   121: invokevirtual setTtcIndex : (I)Landroid/graphics/fonts/Font$Builder;
    //   124: invokevirtual build : ()Landroid/graphics/fonts/Font;
    //   127: astore #9
    //   129: aload_1
    //   130: ifnonnull -> 150
    //   133: new android/graphics/fonts/FontFamily$Builder
    //   136: dup
    //   137: aload #9
    //   139: invokespecial <init> : (Landroid/graphics/fonts/Font;)V
    //   142: astore #9
    //   144: aload #9
    //   146: astore_1
    //   147: goto -> 157
    //   150: aload_1
    //   151: aload #9
    //   153: invokevirtual addFont : (Landroid/graphics/fonts/Font;)Landroid/graphics/fonts/FontFamily$Builder;
    //   156: pop
    //   157: aload_1
    //   158: astore #9
    //   160: aload #11
    //   162: ifnull -> 201
    //   165: goto -> 65
    //   168: astore #12
    //   170: aload #11
    //   172: ifnull -> 195
    //   175: aload #11
    //   177: invokevirtual close : ()V
    //   180: goto -> 195
    //   183: astore #11
    //   185: aload_1
    //   186: astore #9
    //   188: aload #12
    //   190: aload #11
    //   192: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
    //   195: aload_1
    //   196: astore #9
    //   198: aload #12
    //   200: athrow
    //   201: iload #5
    //   203: iconst_1
    //   204: iadd
    //   205: istore #5
    //   207: aload #9
    //   209: astore_1
    //   210: goto -> 18
    //   213: aload_1
    //   214: ifnonnull -> 219
    //   217: aconst_null
    //   218: areturn
    //   219: iload #4
    //   221: iconst_1
    //   222: iand
    //   223: ifeq -> 234
    //   226: sipush #700
    //   229: istore #5
    //   231: goto -> 239
    //   234: sipush #400
    //   237: istore #5
    //   239: iload #7
    //   241: istore #6
    //   243: iload #4
    //   245: iconst_2
    //   246: iand
    //   247: ifeq -> 253
    //   250: iconst_1
    //   251: istore #6
    //   253: new android/graphics/fonts/FontStyle
    //   256: dup
    //   257: iload #5
    //   259: iload #6
    //   261: invokespecial <init> : (II)V
    //   264: astore_2
    //   265: new android/graphics/Typeface$CustomFallbackBuilder
    //   268: dup
    //   269: aload_1
    //   270: invokevirtual build : ()Landroid/graphics/fonts/FontFamily;
    //   273: invokespecial <init> : (Landroid/graphics/fonts/FontFamily;)V
    //   276: aload_2
    //   277: invokevirtual setStyle : (Landroid/graphics/fonts/FontStyle;)Landroid/graphics/Typeface$CustomFallbackBuilder;
    //   280: invokevirtual build : ()Landroid/graphics/Typeface;
    //   283: areturn
    //   284: astore_1
    //   285: goto -> 201
    //   288: iconst_0
    //   289: istore #6
    //   291: goto -> 109
    // Exception table:
    //   from	to	target	type
    //   37	52	284	java/io/IOException
    //   68	73	284	java/io/IOException
    //   79	106	168	finally
    //   109	129	168	finally
    //   133	144	168	finally
    //   150	157	168	finally
    //   175	180	183	finally
    //   188	195	284	java/io/IOException
    //   198	201	284	java/io/IOException
  }
  
  protected Typeface createFromInputStream(Context paramContext, InputStream paramInputStream) {
    throw new RuntimeException("Do not use this function in API 29 or later.");
  }
  
  public Typeface createFromResourcesFontFile(Context paramContext, Resources paramResources, int paramInt1, String paramString, int paramInt2) {
    try {
      FontFamily fontFamily = (new FontFamily.Builder((new Font.Builder(paramResources, paramInt1)).build())).build();
      if ((paramInt2 & 0x1) != 0) {
        paramInt1 = 700;
      } else {
        paramInt1 = 400;
      } 
      if ((paramInt2 & 0x2) != 0) {
        paramInt2 = 1;
      } else {
        paramInt2 = 0;
      } 
      FontStyle fontStyle = new FontStyle(paramInt1, paramInt2);
      return (new Typeface.CustomFallbackBuilder(fontFamily)).setStyle(fontStyle).build();
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    throw new RuntimeException("Do not use this function in API 29 or later.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\graphics\TypefaceCompatApi29Impl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
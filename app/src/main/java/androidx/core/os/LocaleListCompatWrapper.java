package androidx.core.os;

import android.os.Build;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

final class LocaleListCompatWrapper implements LocaleListInterface {
  private static final Locale EN_LATN;
  
  private static final Locale LOCALE_AR_XB;
  
  private static final Locale LOCALE_EN_XA;
  
  private static final Locale[] sEmptyList = new Locale[0];
  
  private final Locale[] mList;
  
  private final String mStringRepresentation;
  
  static {
    LOCALE_EN_XA = new Locale("en", "XA");
    LOCALE_AR_XB = new Locale("ar", "XB");
    EN_LATN = LocaleListCompat.forLanguageTagCompat("en-Latn");
  }
  
  LocaleListCompatWrapper(Locale... paramVarArgs) {
    if (paramVarArgs.length == 0) {
      this.mList = sEmptyList;
      this.mStringRepresentation = "";
      return;
    } 
    Locale[] arrayOfLocale = new Locale[paramVarArgs.length];
    HashSet<Locale> hashSet = new HashSet();
    StringBuilder stringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramVarArgs.length) {
      Locale locale = paramVarArgs[i];
      if (locale != null) {
        if (!hashSet.contains(locale)) {
          locale = (Locale)locale.clone();
          arrayOfLocale[i] = locale;
          toLanguageTag(stringBuilder, locale);
          if (i < paramVarArgs.length - 1)
            stringBuilder.append(','); 
          hashSet.add(locale);
          i++;
          continue;
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("list[");
        stringBuilder2.append(i);
        stringBuilder2.append("] is a repetition");
        throw new IllegalArgumentException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("list[");
      stringBuilder1.append(i);
      stringBuilder1.append("] is null");
      throw new NullPointerException(stringBuilder1.toString());
    } 
    this.mList = arrayOfLocale;
    this.mStringRepresentation = stringBuilder.toString();
  }
  
  private Locale computeFirstMatch(Collection<String> paramCollection, boolean paramBoolean) {
    int i = computeFirstMatchIndex(paramCollection, paramBoolean);
    return (i == -1) ? null : this.mList[i];
  }
  
  private int computeFirstMatchIndex(Collection<String> paramCollection, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mList : [Ljava/util/Locale;
    //   4: astore #5
    //   6: aload #5
    //   8: arraylength
    //   9: iconst_1
    //   10: if_icmpne -> 15
    //   13: iconst_0
    //   14: ireturn
    //   15: aload #5
    //   17: arraylength
    //   18: ifne -> 23
    //   21: iconst_m1
    //   22: ireturn
    //   23: iload_2
    //   24: ifeq -> 50
    //   27: aload_0
    //   28: getstatic androidx/core/os/LocaleListCompatWrapper.EN_LATN : Ljava/util/Locale;
    //   31: invokespecial findFirstMatchIndex : (Ljava/util/Locale;)I
    //   34: istore_3
    //   35: iload_3
    //   36: ifne -> 41
    //   39: iconst_0
    //   40: ireturn
    //   41: iload_3
    //   42: ldc 2147483647
    //   44: if_icmpge -> 50
    //   47: goto -> 53
    //   50: ldc 2147483647
    //   52: istore_3
    //   53: aload_1
    //   54: invokeinterface iterator : ()Ljava/util/Iterator;
    //   59: astore_1
    //   60: aload_1
    //   61: invokeinterface hasNext : ()Z
    //   66: ifeq -> 106
    //   69: aload_0
    //   70: aload_1
    //   71: invokeinterface next : ()Ljava/lang/Object;
    //   76: checkcast java/lang/String
    //   79: invokestatic forLanguageTagCompat : (Ljava/lang/String;)Ljava/util/Locale;
    //   82: invokespecial findFirstMatchIndex : (Ljava/util/Locale;)I
    //   85: istore #4
    //   87: iload #4
    //   89: ifne -> 94
    //   92: iconst_0
    //   93: ireturn
    //   94: iload #4
    //   96: iload_3
    //   97: if_icmpge -> 60
    //   100: iload #4
    //   102: istore_3
    //   103: goto -> 60
    //   106: iload_3
    //   107: ldc 2147483647
    //   109: if_icmpne -> 114
    //   112: iconst_0
    //   113: ireturn
    //   114: iload_3
    //   115: ireturn
  }
  
  private int findFirstMatchIndex(Locale paramLocale) {
    int i = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (i < arrayOfLocale.length) {
        if (matchScore(paramLocale, arrayOfLocale[i]) > 0)
          return i; 
        i++;
        continue;
      } 
      return Integer.MAX_VALUE;
    } 
  }
  
  private static String getLikelyScript(Locale paramLocale) {
    if (Build.VERSION.SDK_INT >= 21) {
      String str = paramLocale.getScript();
      if (!str.isEmpty())
        return str; 
    } 
    return "";
  }
  
  private static boolean isPseudoLocale(Locale paramLocale) {
    return (LOCALE_EN_XA.equals(paramLocale) || LOCALE_AR_XB.equals(paramLocale));
  }
  
  private static int matchScore(Locale paramLocale1, Locale paramLocale2) {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  static void toLanguageTag(StringBuilder paramStringBuilder, Locale paramLocale) {
    paramStringBuilder.append(paramLocale.getLanguage());
    String str = paramLocale.getCountry();
    if (str != null && !str.isEmpty()) {
      paramStringBuilder.append('-');
      paramStringBuilder.append(paramLocale.getCountry());
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof LocaleListCompatWrapper))
      return false; 
    paramObject = ((LocaleListCompatWrapper)paramObject).mList;
    if (this.mList.length != paramObject.length)
      return false; 
    int i = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (i < arrayOfLocale.length) {
        if (!arrayOfLocale[i].equals(paramObject[i]))
          return false; 
        i++;
        continue;
      } 
      return true;
    } 
  }
  
  public Locale get(int paramInt) {
    if (paramInt >= 0) {
      Locale[] arrayOfLocale = this.mList;
      if (paramInt < arrayOfLocale.length)
        return arrayOfLocale[paramInt]; 
    } 
    return null;
  }
  
  public Locale getFirstMatch(String[] paramArrayOfString) {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), false);
  }
  
  public Object getLocaleList() {
    return null;
  }
  
  public int hashCode() {
    int j = 1;
    int i = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (i < arrayOfLocale.length) {
        j = j * 31 + arrayOfLocale[i].hashCode();
        i++;
        continue;
      } 
      return j;
    } 
  }
  
  public int indexOf(Locale paramLocale) {
    int i = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (i < arrayOfLocale.length) {
        if (arrayOfLocale[i].equals(paramLocale))
          return i; 
        i++;
        continue;
      } 
      return -1;
    } 
  }
  
  public boolean isEmpty() {
    return (this.mList.length == 0);
  }
  
  public int size() {
    return this.mList.length;
  }
  
  public String toLanguageTags() {
    return this.mStringRepresentation;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    int i = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (i < arrayOfLocale.length) {
        stringBuilder.append(arrayOfLocale[i]);
        if (i < this.mList.length - 1)
          stringBuilder.append(','); 
        i++;
        continue;
      } 
      stringBuilder.append("]");
      return stringBuilder.toString();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\os\LocaleListCompatWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
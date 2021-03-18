package androidx.core.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class TextViewCompat {
  public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
  
  public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
  
  private static final int LINES = 1;
  
  private static final String LOG_TAG = "TextViewCompat";
  
  private static Field sMaxModeField;
  
  private static boolean sMaxModeFieldFetched;
  
  private static Field sMaximumField;
  
  private static boolean sMaximumFieldFetched;
  
  private static Field sMinModeField;
  
  private static boolean sMinModeFieldFetched;
  
  private static Field sMinimumField;
  
  private static boolean sMinimumFieldFetched;
  
  public static int getAutoSizeMaxTextSize(TextView paramTextView) {
    return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeMaxTextSize() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeMaxTextSize() : -1);
  }
  
  public static int getAutoSizeMinTextSize(TextView paramTextView) {
    return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeMinTextSize() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeMinTextSize() : -1);
  }
  
  public static int getAutoSizeStepGranularity(TextView paramTextView) {
    return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeStepGranularity() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeStepGranularity() : -1);
  }
  
  public static int[] getAutoSizeTextAvailableSizes(TextView paramTextView) {
    return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeTextAvailableSizes() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeTextAvailableSizes() : new int[0]);
  }
  
  public static int getAutoSizeTextType(TextView paramTextView) {
    return (Build.VERSION.SDK_INT >= 27) ? paramTextView.getAutoSizeTextType() : ((paramTextView instanceof AutoSizeableTextView) ? ((AutoSizeableTextView)paramTextView).getAutoSizeTextType() : 0);
  }
  
  public static ColorStateList getCompoundDrawableTintList(TextView paramTextView) {
    Preconditions.checkNotNull(paramTextView);
    return (Build.VERSION.SDK_INT >= 24) ? paramTextView.getCompoundDrawableTintList() : ((paramTextView instanceof TintableCompoundDrawablesView) ? ((TintableCompoundDrawablesView)paramTextView).getSupportCompoundDrawablesTintList() : null);
  }
  
  public static PorterDuff.Mode getCompoundDrawableTintMode(TextView paramTextView) {
    Preconditions.checkNotNull(paramTextView);
    return (Build.VERSION.SDK_INT >= 24) ? paramTextView.getCompoundDrawableTintMode() : ((paramTextView instanceof TintableCompoundDrawablesView) ? ((TintableCompoundDrawablesView)paramTextView).getSupportCompoundDrawablesTintMode() : null);
  }
  
  public static Drawable[] getCompoundDrawablesRelative(TextView paramTextView) {
    Drawable[] arrayOfDrawable;
    if (Build.VERSION.SDK_INT >= 18)
      return paramTextView.getCompoundDrawablesRelative(); 
    if (Build.VERSION.SDK_INT >= 17) {
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      arrayOfDrawable = paramTextView.getCompoundDrawables();
      if (bool) {
        Drawable drawable1 = arrayOfDrawable[2];
        Drawable drawable2 = arrayOfDrawable[0];
        arrayOfDrawable[0] = drawable1;
        arrayOfDrawable[2] = drawable2;
      } 
      return arrayOfDrawable;
    } 
    return arrayOfDrawable.getCompoundDrawables();
  }
  
  public static int getFirstBaselineToTopHeight(TextView paramTextView) {
    return paramTextView.getPaddingTop() - (paramTextView.getPaint().getFontMetricsInt()).top;
  }
  
  public static int getLastBaselineToBottomHeight(TextView paramTextView) {
    return paramTextView.getPaddingBottom() + (paramTextView.getPaint().getFontMetricsInt()).bottom;
  }
  
  public static int getMaxLines(TextView paramTextView) {
    if (Build.VERSION.SDK_INT >= 16)
      return paramTextView.getMaxLines(); 
    if (!sMaxModeFieldFetched) {
      sMaxModeField = retrieveField("mMaxMode");
      sMaxModeFieldFetched = true;
    } 
    Field field = sMaxModeField;
    if (field != null && retrieveIntFromField(field, paramTextView) == 1) {
      if (!sMaximumFieldFetched) {
        sMaximumField = retrieveField("mMaximum");
        sMaximumFieldFetched = true;
      } 
      field = sMaximumField;
      if (field != null)
        return retrieveIntFromField(field, paramTextView); 
    } 
    return -1;
  }
  
  public static int getMinLines(TextView paramTextView) {
    if (Build.VERSION.SDK_INT >= 16)
      return paramTextView.getMinLines(); 
    if (!sMinModeFieldFetched) {
      sMinModeField = retrieveField("mMinMode");
      sMinModeFieldFetched = true;
    } 
    Field field = sMinModeField;
    if (field != null && retrieveIntFromField(field, paramTextView) == 1) {
      if (!sMinimumFieldFetched) {
        sMinimumField = retrieveField("mMinimum");
        sMinimumFieldFetched = true;
      } 
      field = sMinimumField;
      if (field != null)
        return retrieveIntFromField(field, paramTextView); 
    } 
    return -1;
  }
  
  private static int getTextDirection(TextDirectionHeuristic paramTextDirectionHeuristic) {
    return (paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) ? 1 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) ? 1 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.ANYRTL_LTR) ? 2 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.LTR) ? 3 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.RTL) ? 4 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.LOCALE) ? 5 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) ? 6 : ((paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) ? 7 : 1)))))));
  }
  
  private static TextDirectionHeuristic getTextDirectionHeuristic(TextView paramTextView) {
    if (paramTextView.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod)
      return TextDirectionHeuristics.LTR; 
    int i = Build.VERSION.SDK_INT;
    byte b = 0;
    if (i >= 28 && (paramTextView.getInputType() & 0xF) == 3) {
      b = Character.getDirectionality(DecimalFormatSymbols.getInstance(paramTextView.getTextLocale()).getDigitStrings()[0].codePointAt(0));
      return (b == 1 || b == 2) ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR;
    } 
    if (paramTextView.getLayoutDirection() == 1)
      b = 1; 
    switch (paramTextView.getTextDirection()) {
      default:
        return (b != 0) ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
      case 7:
        return TextDirectionHeuristics.FIRSTSTRONG_RTL;
      case 6:
        return TextDirectionHeuristics.FIRSTSTRONG_LTR;
      case 5:
        return TextDirectionHeuristics.LOCALE;
      case 4:
        return TextDirectionHeuristics.RTL;
      case 3:
        return TextDirectionHeuristics.LTR;
      case 2:
        break;
    } 
    return TextDirectionHeuristics.ANYRTL_LTR;
  }
  
  public static PrecomputedTextCompat.Params getTextMetricsParams(TextView paramTextView) {
    if (Build.VERSION.SDK_INT >= 28)
      return new PrecomputedTextCompat.Params(paramTextView.getTextMetricsParams()); 
    PrecomputedTextCompat.Params.Builder builder = new PrecomputedTextCompat.Params.Builder(new TextPaint((Paint)paramTextView.getPaint()));
    if (Build.VERSION.SDK_INT >= 23) {
      builder.setBreakStrategy(paramTextView.getBreakStrategy());
      builder.setHyphenationFrequency(paramTextView.getHyphenationFrequency());
    } 
    if (Build.VERSION.SDK_INT >= 18)
      builder.setTextDirection(getTextDirectionHeuristic(paramTextView)); 
    return builder.build();
  }
  
  private static Field retrieveField(String paramString) {
    Field field = null;
    try {
      Field field1 = TextView.class.getDeclaredField(paramString);
      field = field1;
      field1.setAccessible(true);
      return field1;
    } catch (NoSuchFieldException noSuchFieldException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not retrieve ");
      stringBuilder.append(paramString);
      stringBuilder.append(" field.");
      Log.e("TextViewCompat", stringBuilder.toString());
      return field;
    } 
  }
  
  private static int retrieveIntFromField(Field paramField, TextView paramTextView) {
    try {
      return paramField.getInt(paramTextView);
    } catch (IllegalAccessException illegalAccessException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not retrieve value of ");
      stringBuilder.append(paramField.getName());
      stringBuilder.append(" field.");
      Log.d("TextViewCompat", stringBuilder.toString());
      return -1;
    } 
  }
  
  public static void setAutoSizeTextTypeUniformWithConfiguration(TextView paramTextView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws IllegalArgumentException {
    if (Build.VERSION.SDK_INT >= 27) {
      paramTextView.setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    if (paramTextView instanceof AutoSizeableTextView)
      ((AutoSizeableTextView)paramTextView).setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  public static void setAutoSizeTextTypeUniformWithPresetSizes(TextView paramTextView, int[] paramArrayOfint, int paramInt) throws IllegalArgumentException {
    if (Build.VERSION.SDK_INT >= 27) {
      paramTextView.setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfint, paramInt);
      return;
    } 
    if (paramTextView instanceof AutoSizeableTextView)
      ((AutoSizeableTextView)paramTextView).setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfint, paramInt); 
  }
  
  public static void setAutoSizeTextTypeWithDefaults(TextView paramTextView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 27) {
      paramTextView.setAutoSizeTextTypeWithDefaults(paramInt);
      return;
    } 
    if (paramTextView instanceof AutoSizeableTextView)
      ((AutoSizeableTextView)paramTextView).setAutoSizeTextTypeWithDefaults(paramInt); 
  }
  
  public static void setCompoundDrawableTintList(TextView paramTextView, ColorStateList paramColorStateList) {
    Preconditions.checkNotNull(paramTextView);
    if (Build.VERSION.SDK_INT >= 24) {
      paramTextView.setCompoundDrawableTintList(paramColorStateList);
      return;
    } 
    if (paramTextView instanceof TintableCompoundDrawablesView)
      ((TintableCompoundDrawablesView)paramTextView).setSupportCompoundDrawablesTintList(paramColorStateList); 
  }
  
  public static void setCompoundDrawableTintMode(TextView paramTextView, PorterDuff.Mode paramMode) {
    Preconditions.checkNotNull(paramTextView);
    if (Build.VERSION.SDK_INT >= 24) {
      paramTextView.setCompoundDrawableTintMode(paramMode);
      return;
    } 
    if (paramTextView instanceof TintableCompoundDrawablesView)
      ((TintableCompoundDrawablesView)paramTextView).setSupportCompoundDrawablesTintMode(paramMode); 
  }
  
  public static void setCompoundDrawablesRelative(TextView paramTextView, Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramTextView.setCompoundDrawablesRelative(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 17) {
      Drawable drawable;
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      if (bool) {
        drawable = paramDrawable3;
      } else {
        drawable = paramDrawable1;
      } 
      if (!bool)
        paramDrawable1 = paramDrawable3; 
      paramTextView.setCompoundDrawables(drawable, paramDrawable2, paramDrawable1, paramDrawable4);
      return;
    } 
    paramTextView.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
  }
  
  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView paramTextView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 17) {
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      if (bool) {
        i = paramInt3;
      } else {
        i = paramInt1;
      } 
      if (!bool)
        paramInt1 = paramInt3; 
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(i, paramInt2, paramInt1, paramInt4);
      return;
    } 
    paramTextView.setCompoundDrawablesWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView paramTextView, Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
    if (Build.VERSION.SDK_INT >= 18) {
      paramTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
      return;
    } 
    if (Build.VERSION.SDK_INT >= 17) {
      Drawable drawable;
      int i = paramTextView.getLayoutDirection();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      if (bool) {
        drawable = paramDrawable3;
      } else {
        drawable = paramDrawable1;
      } 
      if (!bool)
        paramDrawable1 = paramDrawable3; 
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, paramDrawable2, paramDrawable1, paramDrawable4);
      return;
    } 
    paramTextView.setCompoundDrawablesWithIntrinsicBounds(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
  }
  
  public static void setCustomSelectionActionModeCallback(TextView paramTextView, ActionMode.Callback paramCallback) {
    paramTextView.setCustomSelectionActionModeCallback(wrapCustomSelectionActionModeCallback(paramTextView, paramCallback));
  }
  
  public static void setFirstBaselineToTopHeight(TextView paramTextView, int paramInt) {
    int i;
    Preconditions.checkArgumentNonnegative(paramInt);
    if (Build.VERSION.SDK_INT >= 28) {
      paramTextView.setFirstBaselineToTopHeight(paramInt);
      return;
    } 
    Paint.FontMetricsInt fontMetricsInt = paramTextView.getPaint().getFontMetricsInt();
    if (Build.VERSION.SDK_INT < 16 || paramTextView.getIncludeFontPadding()) {
      i = fontMetricsInt.top;
    } else {
      i = fontMetricsInt.ascent;
    } 
    if (paramInt > Math.abs(i))
      paramTextView.setPadding(paramTextView.getPaddingLeft(), paramInt + i, paramTextView.getPaddingRight(), paramTextView.getPaddingBottom()); 
  }
  
  public static void setLastBaselineToBottomHeight(TextView paramTextView, int paramInt) {
    int i;
    Preconditions.checkArgumentNonnegative(paramInt);
    Paint.FontMetricsInt fontMetricsInt = paramTextView.getPaint().getFontMetricsInt();
    if (Build.VERSION.SDK_INT < 16 || paramTextView.getIncludeFontPadding()) {
      i = fontMetricsInt.bottom;
    } else {
      i = fontMetricsInt.descent;
    } 
    if (paramInt > Math.abs(i))
      paramTextView.setPadding(paramTextView.getPaddingLeft(), paramTextView.getPaddingTop(), paramTextView.getPaddingRight(), paramInt - i); 
  }
  
  public static void setLineHeight(TextView paramTextView, int paramInt) {
    Preconditions.checkArgumentNonnegative(paramInt);
    int i = paramTextView.getPaint().getFontMetricsInt(null);
    if (paramInt != i)
      paramTextView.setLineSpacing((paramInt - i), 1.0F); 
  }
  
  public static void setPrecomputedText(TextView paramTextView, PrecomputedTextCompat paramPrecomputedTextCompat) {
    if (Build.VERSION.SDK_INT >= 29) {
      paramTextView.setText((CharSequence)paramPrecomputedTextCompat.getPrecomputedText());
      return;
    } 
    if (getTextMetricsParams(paramTextView).equalsWithoutTextDirection(paramPrecomputedTextCompat.getParams())) {
      paramTextView.setText((CharSequence)paramPrecomputedTextCompat);
      return;
    } 
    throw new IllegalArgumentException("Given text can not be applied to TextView.");
  }
  
  public static void setTextAppearance(TextView paramTextView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramTextView.setTextAppearance(paramInt);
      return;
    } 
    paramTextView.setTextAppearance(paramTextView.getContext(), paramInt);
  }
  
  public static void setTextMetricsParams(TextView paramTextView, PrecomputedTextCompat.Params paramParams) {
    if (Build.VERSION.SDK_INT >= 18)
      paramTextView.setTextDirection(getTextDirection(paramParams.getTextDirection())); 
    if (Build.VERSION.SDK_INT < 23) {
      float f = paramParams.getTextPaint().getTextScaleX();
      paramTextView.getPaint().set(paramParams.getTextPaint());
      if (f == paramTextView.getTextScaleX())
        paramTextView.setTextScaleX(f / 2.0F + 1.0F); 
      paramTextView.setTextScaleX(f);
      return;
    } 
    paramTextView.getPaint().set(paramParams.getTextPaint());
    paramTextView.setBreakStrategy(paramParams.getBreakStrategy());
    paramTextView.setHyphenationFrequency(paramParams.getHyphenationFrequency());
  }
  
  public static ActionMode.Callback wrapCustomSelectionActionModeCallback(TextView paramTextView, ActionMode.Callback paramCallback) {
    return (Build.VERSION.SDK_INT >= 26 && Build.VERSION.SDK_INT <= 27) ? ((paramCallback instanceof OreoCallback) ? paramCallback : new OreoCallback(paramCallback, paramTextView)) : paramCallback;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AutoSizeTextType {}
  
  private static class OreoCallback implements ActionMode.Callback {
    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
    
    private final ActionMode.Callback mCallback;
    
    private boolean mCanUseMenuBuilderReferences;
    
    private boolean mInitializedMenuBuilderReferences;
    
    private Class<?> mMenuBuilderClass;
    
    private Method mMenuBuilderRemoveItemAtMethod;
    
    private final TextView mTextView;
    
    OreoCallback(ActionMode.Callback param1Callback, TextView param1TextView) {
      this.mCallback = param1Callback;
      this.mTextView = param1TextView;
      this.mInitializedMenuBuilderReferences = false;
    }
    
    private Intent createProcessTextIntent() {
      return (new Intent()).setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
    }
    
    private Intent createProcessTextIntentForResolveInfo(ResolveInfo param1ResolveInfo, TextView param1TextView) {
      return createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", isEditable(param1TextView) ^ true).setClassName(param1ResolveInfo.activityInfo.packageName, param1ResolveInfo.activityInfo.name);
    }
    
    private List<ResolveInfo> getSupportedActivities(Context param1Context, PackageManager param1PackageManager) {
      ArrayList<ResolveInfo> arrayList = new ArrayList();
      if (!(param1Context instanceof android.app.Activity))
        return arrayList; 
      for (ResolveInfo resolveInfo : param1PackageManager.queryIntentActivities(createProcessTextIntent(), 0)) {
        if (isSupportedActivity(resolveInfo, param1Context))
          arrayList.add(resolveInfo); 
      } 
      return arrayList;
    }
    
    private boolean isEditable(TextView param1TextView) {
      return (param1TextView instanceof android.text.Editable && param1TextView.onCheckIsTextEditor() && param1TextView.isEnabled());
    }
    
    private boolean isSupportedActivity(ResolveInfo param1ResolveInfo, Context param1Context) {
      boolean bool1 = param1Context.getPackageName().equals(param1ResolveInfo.activityInfo.packageName);
      boolean bool = true;
      if (bool1)
        return true; 
      if (!param1ResolveInfo.activityInfo.exported)
        return false; 
      if (param1ResolveInfo.activityInfo.permission != null) {
        if (param1Context.checkSelfPermission(param1ResolveInfo.activityInfo.permission) == 0)
          return true; 
        bool = false;
      } 
      return bool;
    }
    
    private void recomputeProcessTextMenuItems(Menu param1Menu) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mTextView : Landroid/widget/TextView;
      //   4: invokevirtual getContext : ()Landroid/content/Context;
      //   7: astore #5
      //   9: aload #5
      //   11: invokevirtual getPackageManager : ()Landroid/content/pm/PackageManager;
      //   14: astore #4
      //   16: aload_0
      //   17: getfield mInitializedMenuBuilderReferences : Z
      //   20: ifne -> 82
      //   23: aload_0
      //   24: iconst_1
      //   25: putfield mInitializedMenuBuilderReferences : Z
      //   28: ldc 'com.android.internal.view.menu.MenuBuilder'
      //   30: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
      //   33: astore_3
      //   34: aload_0
      //   35: aload_3
      //   36: putfield mMenuBuilderClass : Ljava/lang/Class;
      //   39: aload_0
      //   40: aload_3
      //   41: ldc 'removeItemAt'
      //   43: iconst_1
      //   44: anewarray java/lang/Class
      //   47: dup
      //   48: iconst_0
      //   49: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
      //   52: aastore
      //   53: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
      //   56: putfield mMenuBuilderRemoveItemAtMethod : Ljava/lang/reflect/Method;
      //   59: aload_0
      //   60: iconst_1
      //   61: putfield mCanUseMenuBuilderReferences : Z
      //   64: goto -> 82
      //   67: aload_0
      //   68: aconst_null
      //   69: putfield mMenuBuilderClass : Ljava/lang/Class;
      //   72: aload_0
      //   73: aconst_null
      //   74: putfield mMenuBuilderRemoveItemAtMethod : Ljava/lang/reflect/Method;
      //   77: aload_0
      //   78: iconst_0
      //   79: putfield mCanUseMenuBuilderReferences : Z
      //   82: aload_0
      //   83: getfield mCanUseMenuBuilderReferences : Z
      //   86: ifeq -> 108
      //   89: aload_0
      //   90: getfield mMenuBuilderClass : Ljava/lang/Class;
      //   93: aload_1
      //   94: invokevirtual isInstance : (Ljava/lang/Object;)Z
      //   97: ifeq -> 108
      //   100: aload_0
      //   101: getfield mMenuBuilderRemoveItemAtMethod : Ljava/lang/reflect/Method;
      //   104: astore_3
      //   105: goto -> 128
      //   108: aload_1
      //   109: invokevirtual getClass : ()Ljava/lang/Class;
      //   112: ldc 'removeItemAt'
      //   114: iconst_1
      //   115: anewarray java/lang/Class
      //   118: dup
      //   119: iconst_0
      //   120: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
      //   123: aastore
      //   124: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
      //   127: astore_3
      //   128: aload_1
      //   129: invokeinterface size : ()I
      //   134: iconst_1
      //   135: isub
      //   136: istore_2
      //   137: iload_2
      //   138: iflt -> 202
      //   141: aload_1
      //   142: iload_2
      //   143: invokeinterface getItem : (I)Landroid/view/MenuItem;
      //   148: astore #6
      //   150: aload #6
      //   152: invokeinterface getIntent : ()Landroid/content/Intent;
      //   157: ifnull -> 195
      //   160: ldc 'android.intent.action.PROCESS_TEXT'
      //   162: aload #6
      //   164: invokeinterface getIntent : ()Landroid/content/Intent;
      //   169: invokevirtual getAction : ()Ljava/lang/String;
      //   172: invokevirtual equals : (Ljava/lang/Object;)Z
      //   175: ifeq -> 195
      //   178: aload_3
      //   179: aload_1
      //   180: iconst_1
      //   181: anewarray java/lang/Object
      //   184: dup
      //   185: iconst_0
      //   186: iload_2
      //   187: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   190: aastore
      //   191: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
      //   194: pop
      //   195: iload_2
      //   196: iconst_1
      //   197: isub
      //   198: istore_2
      //   199: goto -> 137
      //   202: aload_0
      //   203: aload #5
      //   205: aload #4
      //   207: invokespecial getSupportedActivities : (Landroid/content/Context;Landroid/content/pm/PackageManager;)Ljava/util/List;
      //   210: astore_3
      //   211: iconst_0
      //   212: istore_2
      //   213: iload_2
      //   214: aload_3
      //   215: invokeinterface size : ()I
      //   220: if_icmpge -> 282
      //   223: aload_3
      //   224: iload_2
      //   225: invokeinterface get : (I)Ljava/lang/Object;
      //   230: checkcast android/content/pm/ResolveInfo
      //   233: astore #5
      //   235: aload_1
      //   236: iconst_0
      //   237: iconst_0
      //   238: iload_2
      //   239: bipush #100
      //   241: iadd
      //   242: aload #5
      //   244: aload #4
      //   246: invokevirtual loadLabel : (Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;
      //   249: invokeinterface add : (IIILjava/lang/CharSequence;)Landroid/view/MenuItem;
      //   254: aload_0
      //   255: aload #5
      //   257: aload_0
      //   258: getfield mTextView : Landroid/widget/TextView;
      //   261: invokespecial createProcessTextIntentForResolveInfo : (Landroid/content/pm/ResolveInfo;Landroid/widget/TextView;)Landroid/content/Intent;
      //   264: invokeinterface setIntent : (Landroid/content/Intent;)Landroid/view/MenuItem;
      //   269: iconst_1
      //   270: invokeinterface setShowAsAction : (I)V
      //   275: iload_2
      //   276: iconst_1
      //   277: iadd
      //   278: istore_2
      //   279: goto -> 213
      //   282: return
      //   283: astore_3
      //   284: goto -> 67
      //   287: astore_1
      //   288: return
      // Exception table:
      //   from	to	target	type
      //   28	64	283	java/lang/ClassNotFoundException
      //   28	64	283	java/lang/NoSuchMethodException
      //   82	105	287	java/lang/NoSuchMethodException
      //   82	105	287	java/lang/IllegalAccessException
      //   82	105	287	java/lang/reflect/InvocationTargetException
      //   108	128	287	java/lang/NoSuchMethodException
      //   108	128	287	java/lang/IllegalAccessException
      //   108	128	287	java/lang/reflect/InvocationTargetException
      //   128	137	287	java/lang/NoSuchMethodException
      //   128	137	287	java/lang/IllegalAccessException
      //   128	137	287	java/lang/reflect/InvocationTargetException
      //   141	195	287	java/lang/NoSuchMethodException
      //   141	195	287	java/lang/IllegalAccessException
      //   141	195	287	java/lang/reflect/InvocationTargetException
    }
    
    public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) {
      return this.mCallback.onActionItemClicked(param1ActionMode, param1MenuItem);
    }
    
    public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mCallback.onCreateActionMode(param1ActionMode, param1Menu);
    }
    
    public void onDestroyActionMode(ActionMode param1ActionMode) {
      this.mCallback.onDestroyActionMode(param1ActionMode);
    }
    
    public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      recomputeProcessTextMenuItems(param1Menu);
      return this.mCallback.onPrepareActionMode(param1ActionMode, param1Menu);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\widget\TextViewCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
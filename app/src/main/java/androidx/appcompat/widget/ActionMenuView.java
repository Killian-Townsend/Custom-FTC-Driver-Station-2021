package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
  static final int GENERATED_ITEM_PADDING = 4;
  
  static final int MIN_CELL_SIZE = 56;
  
  private static final String TAG = "ActionMenuView";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  private boolean mFormatItems;
  
  private int mFormatItemsWidth;
  
  private int mGeneratedItemPadding;
  
  private MenuBuilder mMenu;
  
  MenuBuilder.Callback mMenuBuilderCallback;
  
  private int mMinCellSize;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private ActionMenuPresenter mPresenter;
  
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mMinCellSize = (int)(56.0F * f);
    this.mGeneratedItemPadding = (int)(f * 4.0F);
    this.mPopupContext = paramContext;
    this.mPopupTheme = 0;
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ActionMenuItemView actionMenuItemView;
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt3) - paramInt4, View.MeasureSpec.getMode(paramInt3));
    if (paramView instanceof ActionMenuItemView) {
      actionMenuItemView = (ActionMenuItemView)paramView;
    } else {
      actionMenuItemView = null;
    } 
    boolean bool = true;
    if (actionMenuItemView != null && actionMenuItemView.hasText()) {
      paramInt3 = 1;
    } else {
      paramInt3 = 0;
    } 
    paramInt4 = 2;
    if (paramInt2 > 0 && (paramInt3 == 0 || paramInt2 >= 2)) {
      paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt2 * paramInt1, -2147483648), i);
      int k = paramView.getMeasuredWidth();
      int j = k / paramInt1;
      paramInt2 = j;
      if (k % paramInt1 != 0)
        paramInt2 = j + 1; 
      if (paramInt3 != 0 && paramInt2 < 2)
        paramInt2 = paramInt4; 
    } else {
      paramInt2 = 0;
    } 
    if (layoutParams.isOverflowButton || paramInt3 == 0)
      bool = false; 
    layoutParams.expandable = bool;
    layoutParams.cellsUsed = paramInt2;
    paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1 * paramInt2, 1073741824), i);
    return paramInt2;
  }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_2
    //   1: invokestatic getMode : (I)I
    //   4: istore #12
    //   6: iload_1
    //   7: invokestatic getSize : (I)I
    //   10: istore_1
    //   11: iload_2
    //   12: invokestatic getSize : (I)I
    //   15: istore #13
    //   17: aload_0
    //   18: invokevirtual getPaddingLeft : ()I
    //   21: istore #5
    //   23: aload_0
    //   24: invokevirtual getPaddingRight : ()I
    //   27: istore #6
    //   29: aload_0
    //   30: invokevirtual getPaddingTop : ()I
    //   33: aload_0
    //   34: invokevirtual getPaddingBottom : ()I
    //   37: iadd
    //   38: istore #16
    //   40: iload_2
    //   41: iload #16
    //   43: bipush #-2
    //   45: invokestatic getChildMeasureSpec : (III)I
    //   48: istore #19
    //   50: iload_1
    //   51: iload #5
    //   53: iload #6
    //   55: iadd
    //   56: isub
    //   57: istore #14
    //   59: aload_0
    //   60: getfield mMinCellSize : I
    //   63: istore_1
    //   64: iload #14
    //   66: iload_1
    //   67: idiv
    //   68: istore #15
    //   70: iload #15
    //   72: ifne -> 83
    //   75: aload_0
    //   76: iload #14
    //   78: iconst_0
    //   79: invokevirtual setMeasuredDimension : (II)V
    //   82: return
    //   83: iload_1
    //   84: iload #14
    //   86: iload_1
    //   87: irem
    //   88: iload #15
    //   90: idiv
    //   91: iadd
    //   92: istore #20
    //   94: aload_0
    //   95: invokevirtual getChildCount : ()I
    //   98: istore #21
    //   100: iconst_0
    //   101: istore #6
    //   103: iload #6
    //   105: istore_1
    //   106: iload_1
    //   107: istore #7
    //   109: iload #7
    //   111: istore_2
    //   112: iload_2
    //   113: istore #5
    //   115: iload #5
    //   117: istore #8
    //   119: lconst_0
    //   120: lstore #22
    //   122: iload #5
    //   124: istore #10
    //   126: iload_2
    //   127: istore #11
    //   129: iload_1
    //   130: istore #9
    //   132: iload #6
    //   134: istore #5
    //   136: iload #15
    //   138: istore_1
    //   139: iload #13
    //   141: istore #6
    //   143: iload #9
    //   145: iload #21
    //   147: if_icmpge -> 400
    //   150: aload_0
    //   151: iload #9
    //   153: invokevirtual getChildAt : (I)Landroid/view/View;
    //   156: astore #31
    //   158: aload #31
    //   160: invokevirtual getVisibility : ()I
    //   163: bipush #8
    //   165: if_icmpne -> 174
    //   168: iload #8
    //   170: istore_2
    //   171: goto -> 388
    //   174: aload #31
    //   176: instanceof androidx/appcompat/view/menu/ActionMenuItemView
    //   179: istore #30
    //   181: iload #11
    //   183: iconst_1
    //   184: iadd
    //   185: istore #11
    //   187: iload #30
    //   189: ifeq -> 209
    //   192: aload_0
    //   193: getfield mGeneratedItemPadding : I
    //   196: istore_2
    //   197: aload #31
    //   199: iload_2
    //   200: iconst_0
    //   201: iload_2
    //   202: iconst_0
    //   203: invokevirtual setPadding : (IIII)V
    //   206: goto -> 209
    //   209: aload #31
    //   211: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   214: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   217: astore #32
    //   219: aload #32
    //   221: iconst_0
    //   222: putfield expanded : Z
    //   225: aload #32
    //   227: iconst_0
    //   228: putfield extraPixels : I
    //   231: aload #32
    //   233: iconst_0
    //   234: putfield cellsUsed : I
    //   237: aload #32
    //   239: iconst_0
    //   240: putfield expandable : Z
    //   243: aload #32
    //   245: iconst_0
    //   246: putfield leftMargin : I
    //   249: aload #32
    //   251: iconst_0
    //   252: putfield rightMargin : I
    //   255: iload #30
    //   257: ifeq -> 277
    //   260: aload #31
    //   262: checkcast androidx/appcompat/view/menu/ActionMenuItemView
    //   265: invokevirtual hasText : ()Z
    //   268: ifeq -> 277
    //   271: iconst_1
    //   272: istore #30
    //   274: goto -> 280
    //   277: iconst_0
    //   278: istore #30
    //   280: aload #32
    //   282: iload #30
    //   284: putfield preventEdgeOffset : Z
    //   287: aload #32
    //   289: getfield isOverflowButton : Z
    //   292: ifeq -> 300
    //   295: iconst_1
    //   296: istore_2
    //   297: goto -> 302
    //   300: iload_1
    //   301: istore_2
    //   302: aload #31
    //   304: iload #20
    //   306: iload_2
    //   307: iload #19
    //   309: iload #16
    //   311: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   314: istore #13
    //   316: iload #10
    //   318: iload #13
    //   320: invokestatic max : (II)I
    //   323: istore #10
    //   325: iload #8
    //   327: istore_2
    //   328: aload #32
    //   330: getfield expandable : Z
    //   333: ifeq -> 341
    //   336: iload #8
    //   338: iconst_1
    //   339: iadd
    //   340: istore_2
    //   341: aload #32
    //   343: getfield isOverflowButton : Z
    //   346: ifeq -> 352
    //   349: iconst_1
    //   350: istore #7
    //   352: iload_1
    //   353: iload #13
    //   355: isub
    //   356: istore_1
    //   357: iload #5
    //   359: aload #31
    //   361: invokevirtual getMeasuredHeight : ()I
    //   364: invokestatic max : (II)I
    //   367: istore #5
    //   369: iload #13
    //   371: iconst_1
    //   372: if_icmpne -> 388
    //   375: lload #22
    //   377: iconst_1
    //   378: iload #9
    //   380: ishl
    //   381: i2l
    //   382: lor
    //   383: lstore #22
    //   385: goto -> 388
    //   388: iload #9
    //   390: iconst_1
    //   391: iadd
    //   392: istore #9
    //   394: iload_2
    //   395: istore #8
    //   397: goto -> 143
    //   400: iload #7
    //   402: ifeq -> 417
    //   405: iload #11
    //   407: iconst_2
    //   408: if_icmpne -> 417
    //   411: iconst_1
    //   412: istore #9
    //   414: goto -> 420
    //   417: iconst_0
    //   418: istore #9
    //   420: iconst_0
    //   421: istore_2
    //   422: iload_1
    //   423: istore #13
    //   425: iload #9
    //   427: istore #15
    //   429: iload #14
    //   431: istore #9
    //   433: iload #8
    //   435: ifle -> 760
    //   438: iload #13
    //   440: ifle -> 760
    //   443: ldc 2147483647
    //   445: istore #14
    //   447: iconst_0
    //   448: istore #17
    //   450: iconst_0
    //   451: istore #16
    //   453: lconst_0
    //   454: lstore #26
    //   456: iload #16
    //   458: iload #21
    //   460: if_icmpge -> 586
    //   463: aload_0
    //   464: iload #16
    //   466: invokevirtual getChildAt : (I)Landroid/view/View;
    //   469: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   472: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   475: astore #31
    //   477: aload #31
    //   479: getfield expandable : Z
    //   482: ifne -> 499
    //   485: iload #17
    //   487: istore_1
    //   488: iload #14
    //   490: istore #18
    //   492: lload #26
    //   494: lstore #24
    //   496: goto -> 566
    //   499: aload #31
    //   501: getfield cellsUsed : I
    //   504: iload #14
    //   506: if_icmpge -> 527
    //   509: aload #31
    //   511: getfield cellsUsed : I
    //   514: istore #18
    //   516: lconst_1
    //   517: iload #16
    //   519: lshl
    //   520: lstore #24
    //   522: iconst_1
    //   523: istore_1
    //   524: goto -> 566
    //   527: iload #17
    //   529: istore_1
    //   530: iload #14
    //   532: istore #18
    //   534: lload #26
    //   536: lstore #24
    //   538: aload #31
    //   540: getfield cellsUsed : I
    //   543: iload #14
    //   545: if_icmpne -> 566
    //   548: iload #17
    //   550: iconst_1
    //   551: iadd
    //   552: istore_1
    //   553: lload #26
    //   555: lconst_1
    //   556: iload #16
    //   558: lshl
    //   559: lor
    //   560: lstore #24
    //   562: iload #14
    //   564: istore #18
    //   566: iload #16
    //   568: iconst_1
    //   569: iadd
    //   570: istore #16
    //   572: iload_1
    //   573: istore #17
    //   575: iload #18
    //   577: istore #14
    //   579: lload #24
    //   581: lstore #26
    //   583: goto -> 456
    //   586: iload_2
    //   587: istore_1
    //   588: iload #5
    //   590: istore_2
    //   591: lload #22
    //   593: lload #26
    //   595: lor
    //   596: lstore #22
    //   598: iload #17
    //   600: iload #13
    //   602: if_icmple -> 608
    //   605: goto -> 765
    //   608: iconst_0
    //   609: istore_1
    //   610: iload_1
    //   611: iload #21
    //   613: if_icmpge -> 752
    //   616: aload_0
    //   617: iload_1
    //   618: invokevirtual getChildAt : (I)Landroid/view/View;
    //   621: astore #31
    //   623: aload #31
    //   625: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   628: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   631: astore #32
    //   633: iconst_1
    //   634: iload_1
    //   635: ishl
    //   636: i2l
    //   637: lstore #28
    //   639: lload #26
    //   641: lload #28
    //   643: land
    //   644: lconst_0
    //   645: lcmp
    //   646: ifne -> 679
    //   649: lload #22
    //   651: lstore #24
    //   653: aload #32
    //   655: getfield cellsUsed : I
    //   658: iload #14
    //   660: iconst_1
    //   661: iadd
    //   662: if_icmpne -> 672
    //   665: lload #22
    //   667: lload #28
    //   669: lor
    //   670: lstore #24
    //   672: lload #24
    //   674: lstore #22
    //   676: goto -> 745
    //   679: iload #15
    //   681: ifeq -> 721
    //   684: aload #32
    //   686: getfield preventEdgeOffset : Z
    //   689: ifeq -> 721
    //   692: iload #13
    //   694: iconst_1
    //   695: if_icmpne -> 721
    //   698: aload_0
    //   699: getfield mGeneratedItemPadding : I
    //   702: istore #5
    //   704: aload #31
    //   706: iload #5
    //   708: iload #20
    //   710: iadd
    //   711: iconst_0
    //   712: iload #5
    //   714: iconst_0
    //   715: invokevirtual setPadding : (IIII)V
    //   718: goto -> 721
    //   721: aload #32
    //   723: aload #32
    //   725: getfield cellsUsed : I
    //   728: iconst_1
    //   729: iadd
    //   730: putfield cellsUsed : I
    //   733: aload #32
    //   735: iconst_1
    //   736: putfield expanded : Z
    //   739: iload #13
    //   741: iconst_1
    //   742: isub
    //   743: istore #13
    //   745: iload_1
    //   746: iconst_1
    //   747: iadd
    //   748: istore_1
    //   749: goto -> 610
    //   752: iload_2
    //   753: istore #5
    //   755: iconst_1
    //   756: istore_2
    //   757: goto -> 433
    //   760: iload_2
    //   761: istore_1
    //   762: iload #5
    //   764: istore_2
    //   765: iload #7
    //   767: ifne -> 782
    //   770: iload #11
    //   772: iconst_1
    //   773: if_icmpne -> 782
    //   776: iconst_1
    //   777: istore #5
    //   779: goto -> 785
    //   782: iconst_0
    //   783: istore #5
    //   785: iload #13
    //   787: ifle -> 1135
    //   790: lload #22
    //   792: lconst_0
    //   793: lcmp
    //   794: ifeq -> 1135
    //   797: iload #13
    //   799: iload #11
    //   801: iconst_1
    //   802: isub
    //   803: if_icmplt -> 817
    //   806: iload #5
    //   808: ifne -> 817
    //   811: iload #10
    //   813: iconst_1
    //   814: if_icmple -> 1135
    //   817: lload #22
    //   819: invokestatic bitCount : (J)I
    //   822: i2f
    //   823: fstore #4
    //   825: iload #5
    //   827: ifne -> 920
    //   830: fload #4
    //   832: fstore_3
    //   833: lload #22
    //   835: lconst_1
    //   836: land
    //   837: lconst_0
    //   838: lcmp
    //   839: ifeq -> 868
    //   842: fload #4
    //   844: fstore_3
    //   845: aload_0
    //   846: iconst_0
    //   847: invokevirtual getChildAt : (I)Landroid/view/View;
    //   850: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   853: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   856: getfield preventEdgeOffset : Z
    //   859: ifne -> 868
    //   862: fload #4
    //   864: ldc 0.5
    //   866: fsub
    //   867: fstore_3
    //   868: iload #21
    //   870: iconst_1
    //   871: isub
    //   872: istore #5
    //   874: fload_3
    //   875: fstore #4
    //   877: lload #22
    //   879: iconst_1
    //   880: iload #5
    //   882: ishl
    //   883: i2l
    //   884: land
    //   885: lconst_0
    //   886: lcmp
    //   887: ifeq -> 920
    //   890: fload_3
    //   891: fstore #4
    //   893: aload_0
    //   894: iload #5
    //   896: invokevirtual getChildAt : (I)Landroid/view/View;
    //   899: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   902: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   905: getfield preventEdgeOffset : Z
    //   908: ifne -> 920
    //   911: fload_3
    //   912: ldc 0.5
    //   914: fsub
    //   915: fstore #4
    //   917: goto -> 920
    //   920: fload #4
    //   922: fconst_0
    //   923: fcmpl
    //   924: ifle -> 942
    //   927: iload #13
    //   929: iload #20
    //   931: imul
    //   932: i2f
    //   933: fload #4
    //   935: fdiv
    //   936: f2i
    //   937: istore #7
    //   939: goto -> 945
    //   942: iconst_0
    //   943: istore #7
    //   945: iconst_0
    //   946: istore #8
    //   948: iload_1
    //   949: istore #5
    //   951: iload #8
    //   953: iload #21
    //   955: if_icmpge -> 1138
    //   958: lload #22
    //   960: iconst_1
    //   961: iload #8
    //   963: ishl
    //   964: i2l
    //   965: land
    //   966: lconst_0
    //   967: lcmp
    //   968: ifne -> 977
    //   971: iload_1
    //   972: istore #5
    //   974: goto -> 1123
    //   977: aload_0
    //   978: iload #8
    //   980: invokevirtual getChildAt : (I)Landroid/view/View;
    //   983: astore #31
    //   985: aload #31
    //   987: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   990: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   993: astore #32
    //   995: aload #31
    //   997: instanceof androidx/appcompat/view/menu/ActionMenuItemView
    //   1000: ifeq -> 1048
    //   1003: aload #32
    //   1005: iload #7
    //   1007: putfield extraPixels : I
    //   1010: aload #32
    //   1012: iconst_1
    //   1013: putfield expanded : Z
    //   1016: iload #8
    //   1018: ifne -> 1042
    //   1021: aload #32
    //   1023: getfield preventEdgeOffset : Z
    //   1026: ifne -> 1042
    //   1029: aload #32
    //   1031: iload #7
    //   1033: ineg
    //   1034: iconst_2
    //   1035: idiv
    //   1036: putfield leftMargin : I
    //   1039: goto -> 1042
    //   1042: iconst_1
    //   1043: istore #5
    //   1045: goto -> 1123
    //   1048: aload #32
    //   1050: getfield isOverflowButton : Z
    //   1053: ifeq -> 1085
    //   1056: aload #32
    //   1058: iload #7
    //   1060: putfield extraPixels : I
    //   1063: aload #32
    //   1065: iconst_1
    //   1066: putfield expanded : Z
    //   1069: aload #32
    //   1071: iload #7
    //   1073: ineg
    //   1074: iconst_2
    //   1075: idiv
    //   1076: putfield rightMargin : I
    //   1079: iconst_1
    //   1080: istore #5
    //   1082: goto -> 1123
    //   1085: iload #8
    //   1087: ifeq -> 1099
    //   1090: aload #32
    //   1092: iload #7
    //   1094: iconst_2
    //   1095: idiv
    //   1096: putfield leftMargin : I
    //   1099: iload_1
    //   1100: istore #5
    //   1102: iload #8
    //   1104: iload #21
    //   1106: iconst_1
    //   1107: isub
    //   1108: if_icmpeq -> 1123
    //   1111: aload #32
    //   1113: iload #7
    //   1115: iconst_2
    //   1116: idiv
    //   1117: putfield rightMargin : I
    //   1120: iload_1
    //   1121: istore #5
    //   1123: iload #8
    //   1125: iconst_1
    //   1126: iadd
    //   1127: istore #8
    //   1129: iload #5
    //   1131: istore_1
    //   1132: goto -> 948
    //   1135: iload_1
    //   1136: istore #5
    //   1138: iload #5
    //   1140: ifeq -> 1212
    //   1143: iconst_0
    //   1144: istore_1
    //   1145: iload_1
    //   1146: iload #21
    //   1148: if_icmpge -> 1212
    //   1151: aload_0
    //   1152: iload_1
    //   1153: invokevirtual getChildAt : (I)Landroid/view/View;
    //   1156: astore #31
    //   1158: aload #31
    //   1160: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1163: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   1166: astore #32
    //   1168: aload #32
    //   1170: getfield expanded : Z
    //   1173: ifne -> 1179
    //   1176: goto -> 1205
    //   1179: aload #31
    //   1181: aload #32
    //   1183: getfield cellsUsed : I
    //   1186: iload #20
    //   1188: imul
    //   1189: aload #32
    //   1191: getfield extraPixels : I
    //   1194: iadd
    //   1195: ldc 1073741824
    //   1197: invokestatic makeMeasureSpec : (II)I
    //   1200: iload #19
    //   1202: invokevirtual measure : (II)V
    //   1205: iload_1
    //   1206: iconst_1
    //   1207: iadd
    //   1208: istore_1
    //   1209: goto -> 1145
    //   1212: iload #12
    //   1214: ldc 1073741824
    //   1216: if_icmpeq -> 1222
    //   1219: goto -> 1225
    //   1222: iload #6
    //   1224: istore_2
    //   1225: aload_0
    //   1226: iload #9
    //   1228: iload_2
    //   1229: invokevirtual setMeasuredDimension : (II)V
    //   1232: return
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dismissPopupMenus() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null)
      actionMenuPresenter.dismissPopupMenus(); 
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    return false;
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    LayoutParams layoutParams = new LayoutParams(-2, -2);
    layoutParams.gravity = 16;
    return layoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    if (paramLayoutParams != null) {
      LayoutParams layoutParams;
      if (paramLayoutParams instanceof LayoutParams) {
        layoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
      } else {
        layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
      } 
      if (layoutParams.gravity <= 0)
        layoutParams.gravity = 16; 
      return layoutParams;
    } 
    return generateDefaultLayoutParams();
  }
  
  public LayoutParams generateOverflowButtonLayoutParams() {
    LayoutParams layoutParams = generateDefaultLayoutParams();
    layoutParams.isOverflowButton = true;
    return layoutParams;
  }
  
  public Menu getMenu() {
    if (this.mMenu == null) {
      Context context = getContext();
      MenuBuilder menuBuilder = new MenuBuilder(context);
      this.mMenu = menuBuilder;
      menuBuilder.setCallback(new MenuBuilderCallback());
      ActionMenuPresenter actionMenuPresenter1 = new ActionMenuPresenter(context);
      this.mPresenter = actionMenuPresenter1;
      actionMenuPresenter1.setReserveOverflow(true);
      ActionMenuPresenter actionMenuPresenter2 = this.mPresenter;
      MenuPresenter.Callback callback = this.mActionMenuPresenterCallback;
      if (callback == null)
        callback = new ActionMenuPresenterCallback(); 
      actionMenuPresenter2.setCallback(callback);
      this.mMenu.addMenuPresenter((MenuPresenter)this.mPresenter, this.mPopupContext);
      this.mPresenter.setMenuView(this);
    } 
    return (Menu)this.mMenu;
  }
  
  public Drawable getOverflowIcon() {
    getMenu();
    return this.mPresenter.getOverflowIcon();
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  protected boolean hasSupportDividerBeforeChildAt(int paramInt) {
    boolean bool;
    int j = 0;
    if (paramInt == 0)
      return false; 
    View view1 = getChildAt(paramInt - 1);
    View view2 = getChildAt(paramInt);
    int i = j;
    if (paramInt < getChildCount()) {
      i = j;
      if (view1 instanceof ActionMenuChildView)
        i = false | ((ActionMenuChildView)view1).needsDividerAfter(); 
    } 
    j = i;
    if (paramInt > 0) {
      j = i;
      if (view2 instanceof ActionMenuChildView)
        bool = i | ((ActionMenuChildView)view2).needsDividerBefore(); 
    } 
    return bool;
  }
  
  public boolean hideOverflowMenu() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu());
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.mMenu = paramMenuBuilder;
  }
  
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl) {
    return this.mMenu.performItemAction((MenuItem)paramMenuItemImpl, 0);
  }
  
  public boolean isOverflowMenuShowPending() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending());
  }
  
  public boolean isOverflowMenuShowing() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing());
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null) {
      actionMenuPresenter.updateMenuView(false);
      if (this.mPresenter.isOverflowMenuShowing()) {
        this.mPresenter.hideOverflowMenu();
        this.mPresenter.showOverflowMenu();
      } 
    } 
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!this.mFormatItems) {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    int j = getChildCount();
    int i = (paramInt4 - paramInt2) / 2;
    int k = getDividerWidth();
    int m = paramInt3 - paramInt1;
    paramInt1 = m - getPaddingRight() - getPaddingLeft();
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    paramInt2 = 0;
    paramInt4 = 0;
    paramInt3 = 0;
    while (paramInt2 < j) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isOverflowButton) {
          int i1;
          int n = view.getMeasuredWidth();
          paramInt4 = n;
          if (hasSupportDividerBeforeChildAt(paramInt2))
            paramInt4 = n + k; 
          int i2 = view.getMeasuredHeight();
          if (paramBoolean) {
            i1 = getPaddingLeft() + layoutParams.leftMargin;
            n = i1 + paramInt4;
          } else {
            n = getWidth() - getPaddingRight() - layoutParams.rightMargin;
            i1 = n - paramInt4;
          } 
          int i3 = i - i2 / 2;
          view.layout(i1, i3, n, i2 + i3);
          paramInt1 -= paramInt4;
          paramInt4 = 1;
        } else {
          paramInt1 -= view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
          hasSupportDividerBeforeChildAt(paramInt2);
          paramInt3++;
        } 
      } 
      paramInt2++;
    } 
    if (j == 1 && paramInt4 == 0) {
      View view = getChildAt(0);
      paramInt1 = view.getMeasuredWidth();
      paramInt2 = view.getMeasuredHeight();
      paramInt3 = m / 2 - paramInt1 / 2;
      paramInt4 = i - paramInt2 / 2;
      view.layout(paramInt3, paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4);
      return;
    } 
    paramInt2 = paramInt3 - (paramInt4 ^ 0x1);
    if (paramInt2 > 0) {
      paramInt1 /= paramInt2;
    } else {
      paramInt1 = 0;
    } 
    paramInt4 = Math.max(0, paramInt1);
    if (paramBoolean) {
      paramInt2 = getWidth() - getPaddingRight();
      paramInt1 = 0;
      while (paramInt1 < j) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        paramInt3 = paramInt2;
        if (view.getVisibility() != 8)
          if (layoutParams.isOverflowButton) {
            paramInt3 = paramInt2;
          } else {
            paramInt2 -= layoutParams.rightMargin;
            paramInt3 = view.getMeasuredWidth();
            int n = view.getMeasuredHeight();
            int i1 = i - n / 2;
            view.layout(paramInt2 - paramInt3, i1, paramInt2, n + i1);
            paramInt3 = paramInt2 - paramInt3 + layoutParams.leftMargin + paramInt4;
          }  
        paramInt1++;
        paramInt2 = paramInt3;
      } 
    } else {
      paramInt2 = getPaddingLeft();
      paramInt1 = 0;
      while (paramInt1 < j) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        paramInt3 = paramInt2;
        if (view.getVisibility() != 8)
          if (layoutParams.isOverflowButton) {
            paramInt3 = paramInt2;
          } else {
            paramInt2 += layoutParams.leftMargin;
            paramInt3 = view.getMeasuredWidth();
            int n = view.getMeasuredHeight();
            int i1 = i - n / 2;
            view.layout(paramInt2, i1, paramInt2 + paramInt3, n + i1);
            paramInt3 = paramInt2 + paramInt3 + layoutParams.rightMargin + paramInt4;
          }  
        paramInt1++;
        paramInt2 = paramInt3;
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool2 = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mFormatItems = bool1;
    if (bool2 != bool1)
      this.mFormatItemsWidth = 0; 
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mFormatItems) {
      MenuBuilder menuBuilder = this.mMenu;
      if (menuBuilder != null && i != this.mFormatItemsWidth) {
        this.mFormatItemsWidth = i;
        menuBuilder.onItemsChanged(true);
      } 
    } 
    int j = getChildCount();
    if (this.mFormatItems && j > 0) {
      onMeasureExactFormat(paramInt1, paramInt2);
      return;
    } 
    for (i = 0; i < j; i++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(i).getLayoutParams();
      layoutParams.rightMargin = 0;
      layoutParams.leftMargin = 0;
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public MenuBuilder peekMenu() {
    return this.mMenu;
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mPresenter.setExpandedActionViewsExclusive(paramBoolean);
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    getMenu();
    this.mPresenter.setOverflowIcon(paramDrawable);
  }
  
  public void setOverflowReserved(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
  }
  
  public void setPopupTheme(int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
        return;
      } 
      this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
    } 
  }
  
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter) {
    this.mPresenter = paramActionMenuPresenter;
    paramActionMenuPresenter.setMenuView(this);
  }
  
  public boolean showOverflowMenu() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    return (actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu());
  }
  
  public static interface ActionMenuChildView {
    boolean needsDividerAfter();
    
    boolean needsDividerBefore();
  }
  
  private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      return false;
    }
  }
  
  public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
    @ExportedProperty
    public int cellsUsed;
    
    @ExportedProperty
    public boolean expandable;
    
    boolean expanded;
    
    @ExportedProperty
    public int extraPixels;
    
    @ExportedProperty
    public boolean isOverflowButton;
    
    @ExportedProperty
    public boolean preventEdgeOffset;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = false;
    }
    
    LayoutParams(int param1Int1, int param1Int2, boolean param1Boolean) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = param1Boolean;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
      this.isOverflowButton = param1LayoutParams.isOverflowButton;
    }
  }
  
  private class MenuBuilderCallback implements MenuBuilder.Callback {
    public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
      return (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem));
    }
    
    public void onMenuModeChange(MenuBuilder param1MenuBuilder) {
      if (ActionMenuView.this.mMenuBuilderCallback != null)
        ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(param1MenuBuilder); 
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\appcompat\widget\ActionMenuView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
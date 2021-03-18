package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
  
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    ViewCompat.saveAttributeDataForStyleable((View)this, paramContext, R.styleable.LinearLayoutCompat, paramAttributeSet, tintTypedArray.getWrappedTypeArray(), paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int j = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (int i = 0; i < paramInt1; i++) {
      View view = getVirtualChildAt(i);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int k = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, j, 0);
          layoutParams.width = k;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int j = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (int i = 0; i < paramInt1; i++) {
      View view = getVirtualChildAt(i);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int k = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, j, 0, paramInt2, 0);
          layoutParams.height = k;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt3 + paramInt1, paramInt4 + paramInt2);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        if (bool) {
          i = getPaddingLeft();
        } else {
          i = getWidth() - getPaddingRight();
          int k = this.mDividerWidth;
          i -= k;
        } 
      } else {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          i = view.getLeft() - layoutParams.leftMargin;
          k = this.mDividerWidth;
        } else {
          i = view.getRight() + layoutParams.rightMargin;
          drawVerticalDivider(paramCanvas, i);
        } 
        i -= k;
      } 
    } else {
      return;
    } 
    drawVerticalDivider(paramCanvas, i);
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        i = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        i = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, i);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    int i = this.mOrientation;
    return (i == 0) ? new LayoutParams(-2, -2) : ((i == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getBaseline() {
    if (this.mBaselineAlignedChildIndex < 0)
      return super.getBaseline(); 
    int i = getChildCount();
    int j = this.mBaselineAlignedChildIndex;
    if (i > j) {
      View view = getChildAt(j);
      int k = view.getBaseline();
      if (k == -1) {
        if (this.mBaselineAlignedChildIndex == 0)
          return -1; 
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      } 
      j = this.mBaselineChildTop;
      i = j;
      if (this.mOrientation == 1) {
        int m = this.mGravity & 0x70;
        i = j;
        if (m != 48)
          if (m != 16) {
            if (m != 80) {
              i = j;
            } else {
              i = getBottom() - getTop() - getPaddingBottom() - this.mTotalLength;
            } 
          } else {
            i = j + (getBottom() - getTop() - getPaddingTop() - getPaddingBottom() - this.mTotalLength) / 2;
          }  
      } 
      return i + ((LayoutParams)view.getLayoutParams()).topMargin + k;
    } 
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex() {
    return this.mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt) {
    return 0;
  }
  
  public Drawable getDividerDrawable() {
    return this.mDivider;
  }
  
  public int getDividerPadding() {
    return this.mDividerPadding;
  }
  
  public int getDividerWidth() {
    return this.mDividerWidth;
  }
  
  public int getGravity() {
    return this.mGravity;
  }
  
  int getLocationOffset(View paramView) {
    return 0;
  }
  
  int getNextLocationOffset(View paramView) {
    return 0;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getShowDividers() {
    return this.mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt) {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount() {
    return getChildCount();
  }
  
  public float getWeightSum() {
    return this.mWeightSum;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    boolean bool2 = false;
    boolean bool1 = false;
    if (paramInt == 0) {
      if ((this.mShowDividers & 0x1) != 0)
        bool1 = true; 
      return bool1;
    } 
    if (paramInt == getChildCount()) {
      bool1 = bool2;
      if ((this.mShowDividers & 0x4) != 0)
        bool1 = true; 
      return bool1;
    } 
    if ((this.mShowDividers & 0x2) != 0)
      while (--paramInt >= 0) {
        if (getChildAt(paramInt).getVisibility() != 8)
          return true; 
        paramInt--;
      }  
    return false;
  }
  
  public boolean isBaselineAligned() {
    return this.mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled() {
    return this.mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    byte b1;
    byte b2;
    boolean bool2 = ViewUtils.isLayoutRtl((View)this);
    int k = getPaddingTop();
    int m = paramInt4 - paramInt2;
    int n = getPaddingBottom();
    int i1 = getPaddingBottom();
    int j = getVirtualChildCount();
    paramInt2 = this.mGravity;
    paramInt4 = paramInt2 & 0x70;
    boolean bool1 = this.mBaselineAligned;
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    paramInt2 = GravityCompat.getAbsoluteGravity(0x800007 & paramInt2, ViewCompat.getLayoutDirection((View)this));
    boolean bool = true;
    if (paramInt2 != 1) {
      if (paramInt2 != 5) {
        paramInt2 = getPaddingLeft();
      } else {
        paramInt2 = getPaddingLeft() + paramInt3 - paramInt1 - this.mTotalLength;
      } 
    } else {
      paramInt2 = getPaddingLeft() + (paramInt3 - paramInt1 - this.mTotalLength) / 2;
    } 
    if (bool2) {
      b1 = j - 1;
      b2 = -1;
    } else {
      b1 = 0;
      b2 = 1;
    } 
    int i = 0;
    paramInt3 = paramInt4;
    paramInt4 = k;
    while (i < j) {
      int i2 = b1 + b2 * i;
      View view = getVirtualChildAt(i2);
      if (view == null) {
        paramInt2 += measureNullChild(i2);
      } else if (view.getVisibility() != 8) {
        int i5 = view.getMeasuredWidth();
        int i6 = view.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool1 && layoutParams.height != -1) {
          i3 = view.getBaseline();
        } else {
          i3 = -1;
        } 
        int i4 = layoutParams.gravity;
        paramInt1 = i4;
        if (i4 < 0)
          paramInt1 = paramInt3; 
        paramInt1 &= 0x70;
        if (paramInt1 != 16) {
          if (paramInt1 != 48) {
            if (paramInt1 != 80) {
              paramInt1 = paramInt4;
            } else {
              i4 = m - n - i6 - layoutParams.bottomMargin;
              paramInt1 = i4;
              if (i3 != -1) {
                paramInt1 = view.getMeasuredHeight();
                paramInt1 = i4 - arrayOfInt2[2] - paramInt1 - i3;
              } 
            } 
          } else {
            i4 = layoutParams.topMargin + paramInt4;
            paramInt1 = i4;
            if (i3 != -1)
              paramInt1 = i4 + arrayOfInt1[1] - i3; 
          } 
        } else {
          paramInt1 = (m - k - i1 - i6) / 2 + paramInt4 + layoutParams.topMargin - layoutParams.bottomMargin;
        } 
        bool = true;
        int i3 = paramInt2;
        if (hasDividerBeforeChildAt(i2))
          i3 = paramInt2 + this.mDividerWidth; 
        paramInt2 = layoutParams.leftMargin + i3;
        setChildFrame(view, paramInt2 + getLocationOffset(view), paramInt1, i5, i6);
        paramInt1 = layoutParams.rightMargin;
        i3 = getNextLocationOffset(view);
        i += getChildrenSkipCount(view, i2);
        paramInt2 += i5 + paramInt1 + i3;
      } else {
        bool = true;
      } 
      i++;
    } 
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    int n = getVirtualChildCount();
    int i1 = this.mGravity;
    paramInt1 = i1 & 0x70;
    if (paramInt1 != 16) {
      if (paramInt1 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - this.mTotalLength) / 2;
    } 
    for (paramInt2 = 0; paramInt2 < n; paramInt2++) {
      View view = getVirtualChildAt(paramInt2);
      if (view == null) {
        paramInt3 = paramInt1 + measureNullChild(paramInt2);
      } else {
        paramInt3 = paramInt1;
        if (view.getVisibility() != 8) {
          int i3 = view.getMeasuredWidth();
          int i2 = view.getMeasuredHeight();
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt4 = layoutParams.gravity;
          paramInt3 = paramInt4;
          if (paramInt4 < 0)
            paramInt3 = i1 & 0x800007; 
          paramInt3 = GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection((View)this)) & 0x7;
          if (paramInt3 != 1) {
            if (paramInt3 != 5) {
              paramInt3 = layoutParams.leftMargin + i;
            } else {
              paramInt3 = j - k - i3;
              paramInt4 = layoutParams.rightMargin;
              paramInt3 -= paramInt4;
            } 
          } else {
            paramInt3 = (j - i - m - i3) / 2 + i + layoutParams.leftMargin;
            paramInt4 = layoutParams.rightMargin;
            paramInt3 -= paramInt4;
          } 
          paramInt4 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt2))
            paramInt4 = paramInt1 + this.mDividerHeight; 
          paramInt1 = paramInt4 + layoutParams.topMargin;
          setChildFrame(view, paramInt3, paramInt1 + getLocationOffset(view), i3, i2);
          paramInt3 = layoutParams.bottomMargin;
          paramInt4 = getNextLocationOffset(view);
          paramInt2 += getChildrenSkipCount(view, paramInt2);
          paramInt1 += i2 + paramInt3 + paramInt4;
          continue;
        } 
      } 
      paramInt1 = paramInt3;
      continue;
    } 
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mTotalLength : I
    //   5: aload_0
    //   6: invokevirtual getVirtualChildCount : ()I
    //   9: istore #16
    //   11: iload_1
    //   12: invokestatic getMode : (I)I
    //   15: istore #22
    //   17: iload_2
    //   18: invokestatic getMode : (I)I
    //   21: istore #21
    //   23: aload_0
    //   24: getfield mMaxAscent : [I
    //   27: ifnull -> 37
    //   30: aload_0
    //   31: getfield mMaxDescent : [I
    //   34: ifnonnull -> 51
    //   37: aload_0
    //   38: iconst_4
    //   39: newarray int
    //   41: putfield mMaxAscent : [I
    //   44: aload_0
    //   45: iconst_4
    //   46: newarray int
    //   48: putfield mMaxDescent : [I
    //   51: aload_0
    //   52: getfield mMaxAscent : [I
    //   55: astore #28
    //   57: aload_0
    //   58: getfield mMaxDescent : [I
    //   61: astore #26
    //   63: aload #28
    //   65: iconst_3
    //   66: iconst_m1
    //   67: iastore
    //   68: aload #28
    //   70: iconst_2
    //   71: iconst_m1
    //   72: iastore
    //   73: aload #28
    //   75: iconst_1
    //   76: iconst_m1
    //   77: iastore
    //   78: aload #28
    //   80: iconst_0
    //   81: iconst_m1
    //   82: iastore
    //   83: aload #26
    //   85: iconst_3
    //   86: iconst_m1
    //   87: iastore
    //   88: aload #26
    //   90: iconst_2
    //   91: iconst_m1
    //   92: iastore
    //   93: aload #26
    //   95: iconst_1
    //   96: iconst_m1
    //   97: iastore
    //   98: aload #26
    //   100: iconst_0
    //   101: iconst_m1
    //   102: iastore
    //   103: aload_0
    //   104: getfield mBaselineAligned : Z
    //   107: istore #24
    //   109: aload_0
    //   110: getfield mUseLargestChild : Z
    //   113: istore #25
    //   115: ldc 1073741824
    //   117: istore #14
    //   119: iload #22
    //   121: ldc 1073741824
    //   123: if_icmpne -> 132
    //   126: iconst_1
    //   127: istore #15
    //   129: goto -> 135
    //   132: iconst_0
    //   133: istore #15
    //   135: iconst_0
    //   136: istore #8
    //   138: iload #8
    //   140: istore #7
    //   142: iload #7
    //   144: istore #13
    //   146: iload #13
    //   148: istore #6
    //   150: iload #6
    //   152: istore #11
    //   154: iload #11
    //   156: istore #12
    //   158: iload #12
    //   160: istore #9
    //   162: iload #9
    //   164: istore #10
    //   166: iconst_1
    //   167: istore #5
    //   169: fconst_0
    //   170: fstore_3
    //   171: iload #8
    //   173: iload #16
    //   175: if_icmpge -> 885
    //   178: aload_0
    //   179: iload #8
    //   181: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   184: astore #27
    //   186: aload #27
    //   188: ifnonnull -> 221
    //   191: aload_0
    //   192: aload_0
    //   193: getfield mTotalLength : I
    //   196: aload_0
    //   197: iload #8
    //   199: invokevirtual measureNullChild : (I)I
    //   202: iadd
    //   203: putfield mTotalLength : I
    //   206: iload #8
    //   208: istore #17
    //   210: iload #14
    //   212: istore #8
    //   214: iload #17
    //   216: istore #14
    //   218: goto -> 868
    //   221: aload #27
    //   223: invokevirtual getVisibility : ()I
    //   226: bipush #8
    //   228: if_icmpne -> 247
    //   231: iload #8
    //   233: aload_0
    //   234: aload #27
    //   236: iload #8
    //   238: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   241: iadd
    //   242: istore #8
    //   244: goto -> 206
    //   247: aload_0
    //   248: iload #8
    //   250: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   253: ifeq -> 269
    //   256: aload_0
    //   257: aload_0
    //   258: getfield mTotalLength : I
    //   261: aload_0
    //   262: getfield mDividerWidth : I
    //   265: iadd
    //   266: putfield mTotalLength : I
    //   269: aload #27
    //   271: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   274: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   277: astore #29
    //   279: fload_3
    //   280: aload #29
    //   282: getfield weight : F
    //   285: fadd
    //   286: fstore_3
    //   287: iload #22
    //   289: iload #14
    //   291: if_icmpne -> 403
    //   294: aload #29
    //   296: getfield width : I
    //   299: ifne -> 403
    //   302: aload #29
    //   304: getfield weight : F
    //   307: fconst_0
    //   308: fcmpl
    //   309: ifle -> 403
    //   312: iload #15
    //   314: ifeq -> 340
    //   317: aload_0
    //   318: aload_0
    //   319: getfield mTotalLength : I
    //   322: aload #29
    //   324: getfield leftMargin : I
    //   327: aload #29
    //   329: getfield rightMargin : I
    //   332: iadd
    //   333: iadd
    //   334: putfield mTotalLength : I
    //   337: goto -> 369
    //   340: aload_0
    //   341: getfield mTotalLength : I
    //   344: istore #14
    //   346: aload_0
    //   347: iload #14
    //   349: aload #29
    //   351: getfield leftMargin : I
    //   354: iload #14
    //   356: iadd
    //   357: aload #29
    //   359: getfield rightMargin : I
    //   362: iadd
    //   363: invokestatic max : (II)I
    //   366: putfield mTotalLength : I
    //   369: iload #24
    //   371: ifeq -> 397
    //   374: iconst_0
    //   375: iconst_0
    //   376: invokestatic makeMeasureSpec : (II)I
    //   379: istore #14
    //   381: aload #27
    //   383: iload #14
    //   385: iload #14
    //   387: invokevirtual measure : (II)V
    //   390: iload #7
    //   392: istore #14
    //   394: goto -> 587
    //   397: iconst_1
    //   398: istore #12
    //   400: goto -> 591
    //   403: aload #29
    //   405: getfield width : I
    //   408: ifne -> 434
    //   411: aload #29
    //   413: getfield weight : F
    //   416: fconst_0
    //   417: fcmpl
    //   418: ifle -> 434
    //   421: aload #29
    //   423: bipush #-2
    //   425: putfield width : I
    //   428: iconst_0
    //   429: istore #14
    //   431: goto -> 439
    //   434: ldc_w -2147483648
    //   437: istore #14
    //   439: fload_3
    //   440: fconst_0
    //   441: fcmpl
    //   442: ifne -> 454
    //   445: aload_0
    //   446: getfield mTotalLength : I
    //   449: istore #17
    //   451: goto -> 457
    //   454: iconst_0
    //   455: istore #17
    //   457: aload_0
    //   458: aload #27
    //   460: iload #8
    //   462: iload_1
    //   463: iload #17
    //   465: iload_2
    //   466: iconst_0
    //   467: invokevirtual measureChildBeforeLayout : (Landroid/view/View;IIIII)V
    //   470: iload #14
    //   472: ldc_w -2147483648
    //   475: if_icmpeq -> 485
    //   478: aload #29
    //   480: iload #14
    //   482: putfield width : I
    //   485: aload #27
    //   487: invokevirtual getMeasuredWidth : ()I
    //   490: istore #17
    //   492: iload #15
    //   494: ifeq -> 530
    //   497: aload_0
    //   498: aload_0
    //   499: getfield mTotalLength : I
    //   502: aload #29
    //   504: getfield leftMargin : I
    //   507: iload #17
    //   509: iadd
    //   510: aload #29
    //   512: getfield rightMargin : I
    //   515: iadd
    //   516: aload_0
    //   517: aload #27
    //   519: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   522: iadd
    //   523: iadd
    //   524: putfield mTotalLength : I
    //   527: goto -> 569
    //   530: aload_0
    //   531: getfield mTotalLength : I
    //   534: istore #14
    //   536: aload_0
    //   537: iload #14
    //   539: iload #14
    //   541: iload #17
    //   543: iadd
    //   544: aload #29
    //   546: getfield leftMargin : I
    //   549: iadd
    //   550: aload #29
    //   552: getfield rightMargin : I
    //   555: iadd
    //   556: aload_0
    //   557: aload #27
    //   559: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   562: iadd
    //   563: invokestatic max : (II)I
    //   566: putfield mTotalLength : I
    //   569: iload #7
    //   571: istore #14
    //   573: iload #25
    //   575: ifeq -> 587
    //   578: iload #17
    //   580: iload #7
    //   582: invokestatic max : (II)I
    //   585: istore #14
    //   587: iload #14
    //   589: istore #7
    //   591: iload #8
    //   593: istore #19
    //   595: ldc 1073741824
    //   597: istore #18
    //   599: iload #21
    //   601: ldc 1073741824
    //   603: if_icmpeq -> 625
    //   606: aload #29
    //   608: getfield height : I
    //   611: iconst_m1
    //   612: if_icmpne -> 625
    //   615: iconst_1
    //   616: istore #8
    //   618: iload #8
    //   620: istore #10
    //   622: goto -> 628
    //   625: iconst_0
    //   626: istore #8
    //   628: aload #29
    //   630: getfield topMargin : I
    //   633: aload #29
    //   635: getfield bottomMargin : I
    //   638: iadd
    //   639: istore #14
    //   641: aload #27
    //   643: invokevirtual getMeasuredHeight : ()I
    //   646: iload #14
    //   648: iadd
    //   649: istore #17
    //   651: iload #9
    //   653: aload #27
    //   655: invokevirtual getMeasuredState : ()I
    //   658: invokestatic combineMeasuredStates : (II)I
    //   661: istore #20
    //   663: iload #24
    //   665: ifeq -> 752
    //   668: aload #27
    //   670: invokevirtual getBaseline : ()I
    //   673: istore #23
    //   675: iload #23
    //   677: iconst_m1
    //   678: if_icmpeq -> 752
    //   681: aload #29
    //   683: getfield gravity : I
    //   686: ifge -> 698
    //   689: aload_0
    //   690: getfield mGravity : I
    //   693: istore #9
    //   695: goto -> 705
    //   698: aload #29
    //   700: getfield gravity : I
    //   703: istore #9
    //   705: iload #9
    //   707: bipush #112
    //   709: iand
    //   710: iconst_4
    //   711: ishr
    //   712: bipush #-2
    //   714: iand
    //   715: iconst_1
    //   716: ishr
    //   717: istore #9
    //   719: aload #28
    //   721: iload #9
    //   723: aload #28
    //   725: iload #9
    //   727: iaload
    //   728: iload #23
    //   730: invokestatic max : (II)I
    //   733: iastore
    //   734: aload #26
    //   736: iload #9
    //   738: aload #26
    //   740: iload #9
    //   742: iaload
    //   743: iload #17
    //   745: iload #23
    //   747: isub
    //   748: invokestatic max : (II)I
    //   751: iastore
    //   752: iload #13
    //   754: iload #17
    //   756: invokestatic max : (II)I
    //   759: istore #13
    //   761: iload #5
    //   763: ifeq -> 781
    //   766: aload #29
    //   768: getfield height : I
    //   771: iconst_m1
    //   772: if_icmpne -> 781
    //   775: iconst_1
    //   776: istore #5
    //   778: goto -> 784
    //   781: iconst_0
    //   782: istore #5
    //   784: aload #29
    //   786: getfield weight : F
    //   789: fconst_0
    //   790: fcmpl
    //   791: ifle -> 818
    //   794: iload #8
    //   796: ifeq -> 802
    //   799: goto -> 806
    //   802: iload #17
    //   804: istore #14
    //   806: iload #11
    //   808: iload #14
    //   810: invokestatic max : (II)I
    //   813: istore #8
    //   815: goto -> 843
    //   818: iload #8
    //   820: ifeq -> 826
    //   823: goto -> 830
    //   826: iload #17
    //   828: istore #14
    //   830: iload #6
    //   832: iload #14
    //   834: invokestatic max : (II)I
    //   837: istore #6
    //   839: iload #11
    //   841: istore #8
    //   843: aload_0
    //   844: aload #27
    //   846: iload #19
    //   848: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   851: iload #19
    //   853: iadd
    //   854: istore #14
    //   856: iload #20
    //   858: istore #9
    //   860: iload #8
    //   862: istore #11
    //   864: iload #18
    //   866: istore #8
    //   868: iload #8
    //   870: istore #17
    //   872: iload #14
    //   874: iconst_1
    //   875: iadd
    //   876: istore #8
    //   878: iload #17
    //   880: istore #14
    //   882: goto -> 171
    //   885: aload_0
    //   886: getfield mTotalLength : I
    //   889: ifle -> 914
    //   892: aload_0
    //   893: iload #16
    //   895: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   898: ifeq -> 914
    //   901: aload_0
    //   902: aload_0
    //   903: getfield mTotalLength : I
    //   906: aload_0
    //   907: getfield mDividerWidth : I
    //   910: iadd
    //   911: putfield mTotalLength : I
    //   914: aload #28
    //   916: iconst_1
    //   917: iaload
    //   918: iconst_m1
    //   919: if_icmpne -> 956
    //   922: aload #28
    //   924: iconst_0
    //   925: iaload
    //   926: iconst_m1
    //   927: if_icmpne -> 956
    //   930: aload #28
    //   932: iconst_2
    //   933: iaload
    //   934: iconst_m1
    //   935: if_icmpne -> 956
    //   938: aload #28
    //   940: iconst_3
    //   941: iaload
    //   942: iconst_m1
    //   943: if_icmpeq -> 949
    //   946: goto -> 956
    //   949: iload #13
    //   951: istore #8
    //   953: goto -> 1014
    //   956: iload #13
    //   958: aload #28
    //   960: iconst_3
    //   961: iaload
    //   962: aload #28
    //   964: iconst_0
    //   965: iaload
    //   966: aload #28
    //   968: iconst_1
    //   969: iaload
    //   970: aload #28
    //   972: iconst_2
    //   973: iaload
    //   974: invokestatic max : (II)I
    //   977: invokestatic max : (II)I
    //   980: invokestatic max : (II)I
    //   983: aload #26
    //   985: iconst_3
    //   986: iaload
    //   987: aload #26
    //   989: iconst_0
    //   990: iaload
    //   991: aload #26
    //   993: iconst_1
    //   994: iaload
    //   995: aload #26
    //   997: iconst_2
    //   998: iaload
    //   999: invokestatic max : (II)I
    //   1002: invokestatic max : (II)I
    //   1005: invokestatic max : (II)I
    //   1008: iadd
    //   1009: invokestatic max : (II)I
    //   1012: istore #8
    //   1014: iload #9
    //   1016: istore #13
    //   1018: iload #8
    //   1020: istore #14
    //   1022: iload #25
    //   1024: ifeq -> 1216
    //   1027: iload #22
    //   1029: ldc_w -2147483648
    //   1032: if_icmpeq -> 1044
    //   1035: iload #8
    //   1037: istore #14
    //   1039: iload #22
    //   1041: ifne -> 1216
    //   1044: aload_0
    //   1045: iconst_0
    //   1046: putfield mTotalLength : I
    //   1049: iconst_0
    //   1050: istore #9
    //   1052: iload #8
    //   1054: istore #14
    //   1056: iload #9
    //   1058: iload #16
    //   1060: if_icmpge -> 1216
    //   1063: aload_0
    //   1064: iload #9
    //   1066: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1069: astore #27
    //   1071: aload #27
    //   1073: ifnonnull -> 1094
    //   1076: aload_0
    //   1077: aload_0
    //   1078: getfield mTotalLength : I
    //   1081: aload_0
    //   1082: iload #9
    //   1084: invokevirtual measureNullChild : (I)I
    //   1087: iadd
    //   1088: putfield mTotalLength : I
    //   1091: goto -> 1117
    //   1094: aload #27
    //   1096: invokevirtual getVisibility : ()I
    //   1099: bipush #8
    //   1101: if_icmpne -> 1120
    //   1104: iload #9
    //   1106: aload_0
    //   1107: aload #27
    //   1109: iload #9
    //   1111: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   1114: iadd
    //   1115: istore #9
    //   1117: goto -> 1207
    //   1120: aload #27
    //   1122: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1125: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1128: astore #29
    //   1130: iload #15
    //   1132: ifeq -> 1168
    //   1135: aload_0
    //   1136: aload_0
    //   1137: getfield mTotalLength : I
    //   1140: aload #29
    //   1142: getfield leftMargin : I
    //   1145: iload #7
    //   1147: iadd
    //   1148: aload #29
    //   1150: getfield rightMargin : I
    //   1153: iadd
    //   1154: aload_0
    //   1155: aload #27
    //   1157: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1160: iadd
    //   1161: iadd
    //   1162: putfield mTotalLength : I
    //   1165: goto -> 1117
    //   1168: aload_0
    //   1169: getfield mTotalLength : I
    //   1172: istore #14
    //   1174: aload_0
    //   1175: iload #14
    //   1177: iload #14
    //   1179: iload #7
    //   1181: iadd
    //   1182: aload #29
    //   1184: getfield leftMargin : I
    //   1187: iadd
    //   1188: aload #29
    //   1190: getfield rightMargin : I
    //   1193: iadd
    //   1194: aload_0
    //   1195: aload #27
    //   1197: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1200: iadd
    //   1201: invokestatic max : (II)I
    //   1204: putfield mTotalLength : I
    //   1207: iload #9
    //   1209: iconst_1
    //   1210: iadd
    //   1211: istore #9
    //   1213: goto -> 1052
    //   1216: aload_0
    //   1217: getfield mTotalLength : I
    //   1220: aload_0
    //   1221: invokevirtual getPaddingLeft : ()I
    //   1224: aload_0
    //   1225: invokevirtual getPaddingRight : ()I
    //   1228: iadd
    //   1229: iadd
    //   1230: istore #8
    //   1232: aload_0
    //   1233: iload #8
    //   1235: putfield mTotalLength : I
    //   1238: iload #8
    //   1240: aload_0
    //   1241: invokevirtual getSuggestedMinimumWidth : ()I
    //   1244: invokestatic max : (II)I
    //   1247: iload_1
    //   1248: iconst_0
    //   1249: invokestatic resolveSizeAndState : (III)I
    //   1252: istore #18
    //   1254: ldc_w 16777215
    //   1257: iload #18
    //   1259: iand
    //   1260: aload_0
    //   1261: getfield mTotalLength : I
    //   1264: isub
    //   1265: istore #17
    //   1267: iload #12
    //   1269: ifne -> 1405
    //   1272: iload #17
    //   1274: ifeq -> 1286
    //   1277: fload_3
    //   1278: fconst_0
    //   1279: fcmpl
    //   1280: ifle -> 1286
    //   1283: goto -> 1405
    //   1286: iload #6
    //   1288: iload #11
    //   1290: invokestatic max : (II)I
    //   1293: istore #9
    //   1295: iload #25
    //   1297: ifeq -> 1390
    //   1300: iload #22
    //   1302: ldc 1073741824
    //   1304: if_icmpeq -> 1390
    //   1307: iconst_0
    //   1308: istore #6
    //   1310: iload #6
    //   1312: iload #16
    //   1314: if_icmpge -> 1390
    //   1317: aload_0
    //   1318: iload #6
    //   1320: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1323: astore #26
    //   1325: aload #26
    //   1327: ifnull -> 1381
    //   1330: aload #26
    //   1332: invokevirtual getVisibility : ()I
    //   1335: bipush #8
    //   1337: if_icmpne -> 1343
    //   1340: goto -> 1381
    //   1343: aload #26
    //   1345: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1348: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1351: getfield weight : F
    //   1354: fconst_0
    //   1355: fcmpl
    //   1356: ifle -> 1381
    //   1359: aload #26
    //   1361: iload #7
    //   1363: ldc 1073741824
    //   1365: invokestatic makeMeasureSpec : (II)I
    //   1368: aload #26
    //   1370: invokevirtual getMeasuredHeight : ()I
    //   1373: ldc 1073741824
    //   1375: invokestatic makeMeasureSpec : (II)I
    //   1378: invokevirtual measure : (II)V
    //   1381: iload #6
    //   1383: iconst_1
    //   1384: iadd
    //   1385: istore #6
    //   1387: goto -> 1310
    //   1390: iload #16
    //   1392: istore #8
    //   1394: iload #14
    //   1396: istore #7
    //   1398: iload #9
    //   1400: istore #6
    //   1402: goto -> 2148
    //   1405: aload_0
    //   1406: getfield mWeightSum : F
    //   1409: fstore #4
    //   1411: fload #4
    //   1413: fconst_0
    //   1414: fcmpl
    //   1415: ifle -> 1421
    //   1418: fload #4
    //   1420: fstore_3
    //   1421: aload #28
    //   1423: iconst_3
    //   1424: iconst_m1
    //   1425: iastore
    //   1426: aload #28
    //   1428: iconst_2
    //   1429: iconst_m1
    //   1430: iastore
    //   1431: aload #28
    //   1433: iconst_1
    //   1434: iconst_m1
    //   1435: iastore
    //   1436: aload #28
    //   1438: iconst_0
    //   1439: iconst_m1
    //   1440: iastore
    //   1441: aload #26
    //   1443: iconst_3
    //   1444: iconst_m1
    //   1445: iastore
    //   1446: aload #26
    //   1448: iconst_2
    //   1449: iconst_m1
    //   1450: iastore
    //   1451: aload #26
    //   1453: iconst_1
    //   1454: iconst_m1
    //   1455: iastore
    //   1456: aload #26
    //   1458: iconst_0
    //   1459: iconst_m1
    //   1460: iastore
    //   1461: aload_0
    //   1462: iconst_0
    //   1463: putfield mTotalLength : I
    //   1466: iconst_m1
    //   1467: istore #11
    //   1469: iload #13
    //   1471: istore #9
    //   1473: iconst_0
    //   1474: istore #13
    //   1476: iload #5
    //   1478: istore #8
    //   1480: iload #16
    //   1482: istore #7
    //   1484: iload #9
    //   1486: istore #5
    //   1488: iload #6
    //   1490: istore #9
    //   1492: iload #17
    //   1494: istore #6
    //   1496: iload #13
    //   1498: iload #7
    //   1500: if_icmpge -> 2010
    //   1503: aload_0
    //   1504: iload #13
    //   1506: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1509: astore #27
    //   1511: aload #27
    //   1513: ifnull -> 2001
    //   1516: aload #27
    //   1518: invokevirtual getVisibility : ()I
    //   1521: bipush #8
    //   1523: if_icmpne -> 1529
    //   1526: goto -> 2001
    //   1529: aload #27
    //   1531: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1534: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1537: astore #29
    //   1539: aload #29
    //   1541: getfield weight : F
    //   1544: fstore #4
    //   1546: fload #4
    //   1548: fconst_0
    //   1549: fcmpl
    //   1550: ifle -> 1713
    //   1553: iload #6
    //   1555: i2f
    //   1556: fload #4
    //   1558: fmul
    //   1559: fload_3
    //   1560: fdiv
    //   1561: f2i
    //   1562: istore #14
    //   1564: iload_2
    //   1565: aload_0
    //   1566: invokevirtual getPaddingTop : ()I
    //   1569: aload_0
    //   1570: invokevirtual getPaddingBottom : ()I
    //   1573: iadd
    //   1574: aload #29
    //   1576: getfield topMargin : I
    //   1579: iadd
    //   1580: aload #29
    //   1582: getfield bottomMargin : I
    //   1585: iadd
    //   1586: aload #29
    //   1588: getfield height : I
    //   1591: invokestatic getChildMeasureSpec : (III)I
    //   1594: istore #17
    //   1596: aload #29
    //   1598: getfield width : I
    //   1601: ifne -> 1646
    //   1604: iload #22
    //   1606: ldc 1073741824
    //   1608: if_icmpeq -> 1614
    //   1611: goto -> 1646
    //   1614: iload #14
    //   1616: ifle -> 1626
    //   1619: iload #14
    //   1621: istore #12
    //   1623: goto -> 1629
    //   1626: iconst_0
    //   1627: istore #12
    //   1629: aload #27
    //   1631: iload #12
    //   1633: ldc 1073741824
    //   1635: invokestatic makeMeasureSpec : (II)I
    //   1638: iload #17
    //   1640: invokevirtual measure : (II)V
    //   1643: goto -> 1682
    //   1646: aload #27
    //   1648: invokevirtual getMeasuredWidth : ()I
    //   1651: iload #14
    //   1653: iadd
    //   1654: istore #16
    //   1656: iload #16
    //   1658: istore #12
    //   1660: iload #16
    //   1662: ifge -> 1668
    //   1665: iconst_0
    //   1666: istore #12
    //   1668: aload #27
    //   1670: iload #12
    //   1672: ldc 1073741824
    //   1674: invokestatic makeMeasureSpec : (II)I
    //   1677: iload #17
    //   1679: invokevirtual measure : (II)V
    //   1682: iload #5
    //   1684: aload #27
    //   1686: invokevirtual getMeasuredState : ()I
    //   1689: ldc_w -16777216
    //   1692: iand
    //   1693: invokestatic combineMeasuredStates : (II)I
    //   1696: istore #5
    //   1698: fload_3
    //   1699: fload #4
    //   1701: fsub
    //   1702: fstore_3
    //   1703: iload #6
    //   1705: iload #14
    //   1707: isub
    //   1708: istore #6
    //   1710: goto -> 1713
    //   1713: iload #15
    //   1715: ifeq -> 1754
    //   1718: aload_0
    //   1719: aload_0
    //   1720: getfield mTotalLength : I
    //   1723: aload #27
    //   1725: invokevirtual getMeasuredWidth : ()I
    //   1728: aload #29
    //   1730: getfield leftMargin : I
    //   1733: iadd
    //   1734: aload #29
    //   1736: getfield rightMargin : I
    //   1739: iadd
    //   1740: aload_0
    //   1741: aload #27
    //   1743: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1746: iadd
    //   1747: iadd
    //   1748: putfield mTotalLength : I
    //   1751: goto -> 1796
    //   1754: aload_0
    //   1755: getfield mTotalLength : I
    //   1758: istore #12
    //   1760: aload_0
    //   1761: iload #12
    //   1763: aload #27
    //   1765: invokevirtual getMeasuredWidth : ()I
    //   1768: iload #12
    //   1770: iadd
    //   1771: aload #29
    //   1773: getfield leftMargin : I
    //   1776: iadd
    //   1777: aload #29
    //   1779: getfield rightMargin : I
    //   1782: iadd
    //   1783: aload_0
    //   1784: aload #27
    //   1786: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1789: iadd
    //   1790: invokestatic max : (II)I
    //   1793: putfield mTotalLength : I
    //   1796: iload #21
    //   1798: ldc 1073741824
    //   1800: if_icmpeq -> 1818
    //   1803: aload #29
    //   1805: getfield height : I
    //   1808: iconst_m1
    //   1809: if_icmpne -> 1818
    //   1812: iconst_1
    //   1813: istore #12
    //   1815: goto -> 1821
    //   1818: iconst_0
    //   1819: istore #12
    //   1821: aload #29
    //   1823: getfield topMargin : I
    //   1826: aload #29
    //   1828: getfield bottomMargin : I
    //   1831: iadd
    //   1832: istore #17
    //   1834: aload #27
    //   1836: invokevirtual getMeasuredHeight : ()I
    //   1839: iload #17
    //   1841: iadd
    //   1842: istore #16
    //   1844: iload #11
    //   1846: iload #16
    //   1848: invokestatic max : (II)I
    //   1851: istore #14
    //   1853: iload #12
    //   1855: ifeq -> 1865
    //   1858: iload #17
    //   1860: istore #11
    //   1862: goto -> 1869
    //   1865: iload #16
    //   1867: istore #11
    //   1869: iload #9
    //   1871: iload #11
    //   1873: invokestatic max : (II)I
    //   1876: istore #11
    //   1878: iload #8
    //   1880: ifeq -> 1898
    //   1883: aload #29
    //   1885: getfield height : I
    //   1888: iconst_m1
    //   1889: if_icmpne -> 1898
    //   1892: iconst_1
    //   1893: istore #8
    //   1895: goto -> 1901
    //   1898: iconst_0
    //   1899: istore #8
    //   1901: iload #24
    //   1903: ifeq -> 1990
    //   1906: aload #27
    //   1908: invokevirtual getBaseline : ()I
    //   1911: istore #12
    //   1913: iload #12
    //   1915: iconst_m1
    //   1916: if_icmpeq -> 1990
    //   1919: aload #29
    //   1921: getfield gravity : I
    //   1924: ifge -> 1936
    //   1927: aload_0
    //   1928: getfield mGravity : I
    //   1931: istore #9
    //   1933: goto -> 1943
    //   1936: aload #29
    //   1938: getfield gravity : I
    //   1941: istore #9
    //   1943: iload #9
    //   1945: bipush #112
    //   1947: iand
    //   1948: iconst_4
    //   1949: ishr
    //   1950: bipush #-2
    //   1952: iand
    //   1953: iconst_1
    //   1954: ishr
    //   1955: istore #9
    //   1957: aload #28
    //   1959: iload #9
    //   1961: aload #28
    //   1963: iload #9
    //   1965: iaload
    //   1966: iload #12
    //   1968: invokestatic max : (II)I
    //   1971: iastore
    //   1972: aload #26
    //   1974: iload #9
    //   1976: aload #26
    //   1978: iload #9
    //   1980: iaload
    //   1981: iload #16
    //   1983: iload #12
    //   1985: isub
    //   1986: invokestatic max : (II)I
    //   1989: iastore
    //   1990: iload #11
    //   1992: istore #9
    //   1994: iload #14
    //   1996: istore #11
    //   1998: goto -> 2001
    //   2001: iload #13
    //   2003: iconst_1
    //   2004: iadd
    //   2005: istore #13
    //   2007: goto -> 1496
    //   2010: aload_0
    //   2011: aload_0
    //   2012: getfield mTotalLength : I
    //   2015: aload_0
    //   2016: invokevirtual getPaddingLeft : ()I
    //   2019: aload_0
    //   2020: invokevirtual getPaddingRight : ()I
    //   2023: iadd
    //   2024: iadd
    //   2025: putfield mTotalLength : I
    //   2028: aload #28
    //   2030: iconst_1
    //   2031: iaload
    //   2032: iconst_m1
    //   2033: if_icmpne -> 2070
    //   2036: aload #28
    //   2038: iconst_0
    //   2039: iaload
    //   2040: iconst_m1
    //   2041: if_icmpne -> 2070
    //   2044: aload #28
    //   2046: iconst_2
    //   2047: iaload
    //   2048: iconst_m1
    //   2049: if_icmpne -> 2070
    //   2052: aload #28
    //   2054: iconst_3
    //   2055: iaload
    //   2056: iconst_m1
    //   2057: if_icmpeq -> 2063
    //   2060: goto -> 2070
    //   2063: iload #11
    //   2065: istore #6
    //   2067: goto -> 2128
    //   2070: iload #11
    //   2072: aload #28
    //   2074: iconst_3
    //   2075: iaload
    //   2076: aload #28
    //   2078: iconst_0
    //   2079: iaload
    //   2080: aload #28
    //   2082: iconst_1
    //   2083: iaload
    //   2084: aload #28
    //   2086: iconst_2
    //   2087: iaload
    //   2088: invokestatic max : (II)I
    //   2091: invokestatic max : (II)I
    //   2094: invokestatic max : (II)I
    //   2097: aload #26
    //   2099: iconst_3
    //   2100: iaload
    //   2101: aload #26
    //   2103: iconst_0
    //   2104: iaload
    //   2105: aload #26
    //   2107: iconst_1
    //   2108: iaload
    //   2109: aload #26
    //   2111: iconst_2
    //   2112: iaload
    //   2113: invokestatic max : (II)I
    //   2116: invokestatic max : (II)I
    //   2119: invokestatic max : (II)I
    //   2122: iadd
    //   2123: invokestatic max : (II)I
    //   2126: istore #6
    //   2128: iload #5
    //   2130: istore #13
    //   2132: iload #8
    //   2134: istore #5
    //   2136: iload #7
    //   2138: istore #8
    //   2140: iload #6
    //   2142: istore #7
    //   2144: iload #9
    //   2146: istore #6
    //   2148: iload #5
    //   2150: ifne -> 2163
    //   2153: iload #21
    //   2155: ldc 1073741824
    //   2157: if_icmpeq -> 2163
    //   2160: goto -> 2167
    //   2163: iload #7
    //   2165: istore #6
    //   2167: aload_0
    //   2168: iload #18
    //   2170: iload #13
    //   2172: ldc_w -16777216
    //   2175: iand
    //   2176: ior
    //   2177: iload #6
    //   2179: aload_0
    //   2180: invokevirtual getPaddingTop : ()I
    //   2183: aload_0
    //   2184: invokevirtual getPaddingBottom : ()I
    //   2187: iadd
    //   2188: iadd
    //   2189: aload_0
    //   2190: invokevirtual getSuggestedMinimumHeight : ()I
    //   2193: invokestatic max : (II)I
    //   2196: iload_2
    //   2197: iload #13
    //   2199: bipush #16
    //   2201: ishl
    //   2202: invokestatic resolveSizeAndState : (III)I
    //   2205: invokevirtual setMeasuredDimension : (II)V
    //   2208: iload #10
    //   2210: ifeq -> 2220
    //   2213: aload_0
    //   2214: iload #8
    //   2216: iload_1
    //   2217: invokespecial forceUniformHeight : (II)V
    //   2220: return
  }
  
  int measureNullChild(int paramInt) {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2) {
    this.mTotalLength = 0;
    int i3 = getVirtualChildCount();
    int i8 = View.MeasureSpec.getMode(paramInt1);
    int i6 = View.MeasureSpec.getMode(paramInt2);
    int i9 = this.mBaselineAlignedChildIndex;
    boolean bool = this.mUseLargestChild;
    int i = 0;
    int i5 = i;
    int n = i5;
    int j = n;
    int m = j;
    int i1 = m;
    int i4 = i1;
    int i2 = i4;
    float f = 0.0F;
    int k = 1;
    while (i1 < i3) {
      View view = getVirtualChildAt(i1);
      if (view == null) {
        this.mTotalLength += measureNullChild(i1);
      } else if (view.getVisibility() == 8) {
        i1 += getChildrenSkipCount(view, i1);
      } else {
        if (hasDividerBeforeChildAt(i1))
          this.mTotalLength += this.mDividerHeight; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (i6 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0F) {
          i4 = this.mTotalLength;
          this.mTotalLength = Math.max(i4, layoutParams.topMargin + i4 + layoutParams.bottomMargin);
          i4 = 1;
        } else {
          if (layoutParams.height == 0 && layoutParams.weight > 0.0F) {
            layoutParams.height = -2;
            i11 = 0;
          } else {
            i11 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i12 = this.mTotalLength;
          } else {
            i12 = 0;
          } 
          measureChildBeforeLayout(view, i1, paramInt1, 0, paramInt2, i12);
          if (i11 != Integer.MIN_VALUE)
            layoutParams.height = i11; 
          int i11 = view.getMeasuredHeight();
          int i12 = this.mTotalLength;
          this.mTotalLength = Math.max(i12, i12 + i11 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          if (bool)
            n = Math.max(i11, n); 
        } 
        int i10 = i1;
        if (i9 >= 0 && i9 == i10 + 1)
          this.mBaselineChildTop = this.mTotalLength; 
        if (i10 >= i9 || layoutParams.weight <= 0.0F) {
          if (i8 != 1073741824 && layoutParams.width == -1) {
            i1 = 1;
            i2 = i1;
          } else {
            i1 = 0;
          } 
          int i11 = layoutParams.leftMargin + layoutParams.rightMargin;
          int i12 = view.getMeasuredWidth() + i11;
          i5 = Math.max(i5, i12);
          int i13 = View.combineMeasuredStates(i, view.getMeasuredState());
          if (k && layoutParams.width == -1) {
            i = 1;
          } else {
            i = 0;
          } 
          if (layoutParams.weight > 0.0F) {
            if (i1 == 0)
              i11 = i12; 
            j = Math.max(j, i11);
            k = m;
          } else {
            if (i1 == 0)
              i11 = i12; 
            k = Math.max(m, i11);
          } 
          i1 = getChildrenSkipCount(view, i10);
          m = k;
          i11 = i13;
          i1 += i10;
          k = i;
          i = i11;
        } else {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        } 
      } 
      i1++;
    } 
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i3))
      this.mTotalLength += this.mDividerHeight; 
    if (bool && (i6 == Integer.MIN_VALUE || i6 == 0)) {
      this.mTotalLength = 0;
      for (i1 = 0; i1 < i3; i1++) {
        View view = getVirtualChildAt(i1);
        if (view == null) {
          this.mTotalLength += measureNullChild(i1);
        } else if (view.getVisibility() == 8) {
          i1 += getChildrenSkipCount(view, i1);
        } else {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          int i10 = this.mTotalLength;
          this.mTotalLength = Math.max(i10, i10 + n + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
        } 
      } 
    } 
    i1 = this.mTotalLength + getPaddingTop() + getPaddingBottom();
    this.mTotalLength = i1;
    int i7 = View.resolveSizeAndState(Math.max(i1, getSuggestedMinimumHeight()), paramInt2, 0);
    i1 = (0xFFFFFF & i7) - this.mTotalLength;
    if (i4 != 0 || (i1 != 0 && f > 0.0F)) {
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      this.mTotalLength = 0;
      j = i1;
      i1 = 0;
      n = i5;
      while (i1 < i3) {
        View view = getVirtualChildAt(i1);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i5 = (int)(j * f1 / f);
            int i11 = getPaddingLeft();
            int i12 = getPaddingRight();
            int i13 = layoutParams.leftMargin;
            i9 = layoutParams.rightMargin;
            int i14 = layoutParams.width;
            i4 = j - i5;
            i11 = getChildMeasureSpec(paramInt1, i11 + i12 + i13 + i9, i14);
            if (layoutParams.height != 0 || i6 != 1073741824) {
              i5 = view.getMeasuredHeight() + i5;
              j = i5;
              if (i5 < 0)
                j = 0; 
              view.measure(i11, View.MeasureSpec.makeMeasureSpec(j, 1073741824));
            } else {
              if (i5 > 0) {
                j = i5;
              } else {
                j = 0;
              } 
              view.measure(i11, View.MeasureSpec.makeMeasureSpec(j, 1073741824));
            } 
            i = View.combineMeasuredStates(i, view.getMeasuredState() & 0xFFFFFF00);
            f -= f1;
            j = i4;
          } 
          i5 = layoutParams.leftMargin + layoutParams.rightMargin;
          int i10 = view.getMeasuredWidth() + i5;
          i4 = Math.max(n, i10);
          if (i8 != 1073741824 && layoutParams.width == -1) {
            n = 1;
          } else {
            n = 0;
          } 
          if (n != 0) {
            n = i5;
          } else {
            n = i10;
          } 
          m = Math.max(m, n);
          if (k != 0 && layoutParams.width == -1) {
            k = 1;
          } else {
            k = 0;
          } 
          n = this.mTotalLength;
          this.mTotalLength = Math.max(n, view.getMeasuredHeight() + n + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          n = i4;
        } 
        i1++;
      } 
      this.mTotalLength += getPaddingTop() + getPaddingBottom();
      j = i;
      i = m;
    } else {
      m = Math.max(m, j);
      if (bool && i6 != 1073741824)
        for (j = 0; j < i3; j++) {
          View view = getVirtualChildAt(j);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(n, 1073741824)); 
        }  
      j = i;
      i = m;
      n = i5;
    } 
    if (k != 0 || i8 == 1073741824)
      i = n; 
    setMeasuredDimension(View.resolveSizeAndState(Math.max(i + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), paramInt1, j), i7);
    if (i2 != 0)
      forceUniformWidth(i3, paramInt2); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawDividersVertical(paramCanvas);
      return;
    } 
    drawDividersHorizontal(paramCanvas);
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
      return;
    } 
    measureHorizontal(paramInt1, paramInt2);
  }
  
  public void setBaselineAligned(boolean paramBoolean) {
    this.mBaselineAligned = paramBoolean;
  }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < getChildCount()) {
      this.mBaselineAlignedChildIndex = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("base aligned child index out of range (0, ");
    stringBuilder.append(getChildCount());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDivider)
      return; 
    this.mDivider = paramDrawable;
    boolean bool = false;
    if (paramDrawable != null) {
      this.mDividerWidth = paramDrawable.getIntrinsicWidth();
      this.mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
    } 
    if (paramDrawable == null)
      bool = true; 
    setWillNotDraw(bool);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt) {
    this.mDividerPadding = paramInt;
  }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    paramInt &= 0x800007;
    int i = this.mGravity;
    if ((0x800007 & i) != paramInt) {
      this.mGravity = paramInt | 0xFF7FFFF8 & i;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) {
    this.mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    paramInt &= 0x70;
    int i = this.mGravity;
    if ((i & 0x70) != paramInt) {
      this.mGravity = paramInt | i & 0xFFFFFF8F;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) {
    this.mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DividerMode {}
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int gravity = -1;
    
    public float weight;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.weight = 0.0F;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2);
      this.weight = param1Float;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.LinearLayoutCompat_Layout);
      this.weight = typedArray.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
      this.gravity = param1LayoutParams.gravity;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OrientationMode {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\appcompat\widget\LinearLayoutCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
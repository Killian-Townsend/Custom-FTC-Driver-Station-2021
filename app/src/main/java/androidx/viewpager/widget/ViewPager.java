package androidx.viewpager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
  private static final int CLOSE_ENOUGH = 2;
  
  private static final Comparator<ItemInfo> COMPARATOR;
  
  private static final boolean DEBUG = false;
  
  private static final int DEFAULT_GUTTER_SIZE = 16;
  
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  
  private static final int DRAW_ORDER_DEFAULT = 0;
  
  private static final int DRAW_ORDER_FORWARD = 1;
  
  private static final int DRAW_ORDER_REVERSE = 2;
  
  private static final int INVALID_POINTER = -1;
  
  static final int[] LAYOUT_ATTRS = new int[] { 16842931 };
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  private static final String TAG = "ViewPager";
  
  private static final boolean USE_CACHE = false;
  
  private static final Interpolator sInterpolator;
  
  private static final ViewPositionComparator sPositionComparator;
  
  private int mActivePointerId = -1;
  
  PagerAdapter mAdapter;
  
  private List<OnAdapterChangeListener> mAdapterChangeListeners;
  
  private int mBottomPageBounds;
  
  private boolean mCalledSuper;
  
  private int mChildHeightMeasureSpec;
  
  private int mChildWidthMeasureSpec;
  
  private int mCloseEnough;
  
  int mCurItem;
  
  private int mDecorChildCount;
  
  private int mDefaultGutterSize;
  
  private int mDrawingOrder;
  
  private ArrayList<View> mDrawingOrderedChildren;
  
  private final Runnable mEndScrollRunnable = new Runnable() {
      public void run() {
        ViewPager.this.setScrollState(0);
        ViewPager.this.populate();
      }
    };
  
  private int mExpectedAdapterCount;
  
  private long mFakeDragBeginTime;
  
  private boolean mFakeDragging;
  
  private boolean mFirstLayout = true;
  
  private float mFirstOffset = -3.4028235E38F;
  
  private int mFlingDistance;
  
  private int mGutterSize;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private OnPageChangeListener mInternalPageChangeListener;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsScrollStarted;
  
  private boolean mIsUnableToDrag;
  
  private final ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  private float mLastOffset = Float.MAX_VALUE;
  
  private EdgeEffect mLeftEdge;
  
  private Drawable mMarginDrawable;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private boolean mNeedCalculatePageOffsets = false;
  
  private PagerObserver mObserver;
  
  private int mOffscreenPageLimit = 1;
  
  private OnPageChangeListener mOnPageChangeListener;
  
  private List<OnPageChangeListener> mOnPageChangeListeners;
  
  private int mPageMargin;
  
  private PageTransformer mPageTransformer;
  
  private int mPageTransformerLayerType;
  
  private boolean mPopulatePending;
  
  private Parcelable mRestoredAdapterState = null;
  
  private ClassLoader mRestoredClassLoader = null;
  
  private int mRestoredCurItem = -1;
  
  private EdgeEffect mRightEdge;
  
  private int mScrollState = 0;
  
  private Scroller mScroller;
  
  private boolean mScrollingCacheEnabled;
  
  private final ItemInfo mTempItem = new ItemInfo();
  
  private final Rect mTempRect = new Rect();
  
  private int mTopPageBounds;
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  static {
    COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ViewPager.ItemInfo param1ItemInfo1, ViewPager.ItemInfo param1ItemInfo2) {
          return param1ItemInfo1.position - param1ItemInfo2.position;
        }
      };
    sInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
    sPositionComparator = new ViewPositionComparator();
  }
  
  public ViewPager(Context paramContext) {
    super(paramContext);
    initViewPager();
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2) {
    float f2;
    int m = this.mAdapter.getCount();
    int i = getClientWidth();
    if (i > 0) {
      f2 = this.mPageMargin / i;
    } else {
      f2 = 0.0F;
    } 
    if (paramItemInfo2 != null) {
      i = paramItemInfo2.position;
      if (i < paramItemInfo1.position) {
        f1 = paramItemInfo2.offset + paramItemInfo2.widthFactor + f2;
        i++;
        int n = 0;
        while (i <= paramItemInfo1.position && n < this.mItems.size()) {
          float f;
          int i1;
          paramItemInfo2 = this.mItems.get(n);
          while (true) {
            i1 = i;
            f = f1;
            if (i > paramItemInfo2.position) {
              i1 = i;
              f = f1;
              if (n < this.mItems.size() - 1) {
                paramItemInfo2 = this.mItems.get(++n);
                continue;
              } 
            } 
            break;
          } 
          while (i1 < paramItemInfo2.position) {
            f += this.mAdapter.getPageWidth(i1) + f2;
            i1++;
          } 
          paramItemInfo2.offset = f;
          f1 = f + paramItemInfo2.widthFactor + f2;
          i = i1 + 1;
        } 
      } else if (i > paramItemInfo1.position) {
        int n = this.mItems.size() - 1;
        f1 = paramItemInfo2.offset;
        while (--i >= paramItemInfo1.position && n >= 0) {
          float f;
          int i1;
          paramItemInfo2 = this.mItems.get(n);
          while (true) {
            i1 = i;
            f = f1;
            if (i < paramItemInfo2.position) {
              i1 = i;
              f = f1;
              if (n > 0) {
                paramItemInfo2 = this.mItems.get(--n);
                continue;
              } 
            } 
            break;
          } 
          while (i1 > paramItemInfo2.position) {
            f -= this.mAdapter.getPageWidth(i1) + f2;
            i1--;
          } 
          f1 = f - paramItemInfo2.widthFactor + f2;
          paramItemInfo2.offset = f1;
          i = i1 - 1;
        } 
      } 
    } 
    int k = this.mItems.size();
    float f3 = paramItemInfo1.offset;
    i = paramItemInfo1.position - 1;
    if (paramItemInfo1.position == 0) {
      f1 = paramItemInfo1.offset;
    } else {
      f1 = -3.4028235E38F;
    } 
    this.mFirstOffset = f1;
    int j = paramItemInfo1.position;
    if (j == --m) {
      f1 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
    } else {
      f1 = Float.MAX_VALUE;
    } 
    this.mLastOffset = f1;
    j = paramInt - 1;
    float f1 = f3;
    while (j >= 0) {
      paramItemInfo2 = this.mItems.get(j);
      while (i > paramItemInfo2.position) {
        f1 -= this.mAdapter.getPageWidth(i) + f2;
        i--;
      } 
      f1 -= paramItemInfo2.widthFactor + f2;
      paramItemInfo2.offset = f1;
      if (paramItemInfo2.position == 0)
        this.mFirstOffset = f1; 
      j--;
      i--;
    } 
    f1 = paramItemInfo1.offset + paramItemInfo1.widthFactor + f2;
    j = paramItemInfo1.position + 1;
    i = paramInt + 1;
    for (paramInt = j; i < k; paramInt++) {
      paramItemInfo1 = this.mItems.get(i);
      while (paramInt < paramItemInfo1.position) {
        f1 += this.mAdapter.getPageWidth(paramInt) + f2;
        paramInt++;
      } 
      if (paramItemInfo1.position == m)
        this.mLastOffset = paramItemInfo1.widthFactor + f1 - 1.0F; 
      paramItemInfo1.offset = f1;
      f1 += paramItemInfo1.widthFactor + f2;
      i++;
    } 
    this.mNeedCalculatePageOffsets = false;
  }
  
  private void completeScroll(boolean paramBoolean) {
    boolean bool;
    if (this.mScrollState == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      setScrollingCacheEnabled(false);
      if ((this.mScroller.isFinished() ^ true) != 0) {
        this.mScroller.abortAnimation();
        int j = getScrollX();
        int k = getScrollY();
        int m = this.mScroller.getCurrX();
        int n = this.mScroller.getCurrY();
        if (j != m || k != n) {
          scrollTo(m, n);
          if (m != j)
            pageScrolled(m); 
        } 
      } 
    } 
    this.mPopulatePending = false;
    for (int i = 0; i < this.mItems.size(); i++) {
      ItemInfo itemInfo = this.mItems.get(i);
      if (itemInfo.scrolling) {
        itemInfo.scrolling = false;
        bool = true;
      } 
    } 
    if (bool) {
      if (paramBoolean) {
        ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
        return;
      } 
      this.mEndScrollRunnable.run();
    } 
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3) {
    if (Math.abs(paramInt3) > this.mFlingDistance && Math.abs(paramInt2) > this.mMinimumVelocity) {
      if (paramInt2 <= 0)
        paramInt1++; 
    } else {
      float f;
      if (paramInt1 >= this.mCurItem) {
        f = 0.4F;
      } else {
        f = 0.6F;
      } 
      paramInt1 += (int)(paramFloat + f);
    } 
    paramInt2 = paramInt1;
    if (this.mItems.size() > 0) {
      ItemInfo itemInfo1 = this.mItems.get(0);
      ArrayList<ItemInfo> arrayList = this.mItems;
      ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
      paramInt2 = Math.max(itemInfo1.position, Math.min(paramInt1, itemInfo2.position));
    } 
    return paramInt2;
  }
  
  private void dispatchOnPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrolled(paramInt1, paramFloat, paramInt2); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      int i = 0;
      int j = list.size();
      while (i < j) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
        i++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrolled(paramInt1, paramFloat, paramInt2); 
  }
  
  private void dispatchOnPageSelected(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageSelected(paramInt); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      int i = 0;
      int j = list.size();
      while (i < j) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageSelected(paramInt); 
        i++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageSelected(paramInt); 
  }
  
  private void dispatchOnScrollStateChanged(int paramInt) {
    OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListener;
    if (onPageChangeListener2 != null)
      onPageChangeListener2.onPageScrollStateChanged(paramInt); 
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null) {
      int i = 0;
      int j = list.size();
      while (i < j) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrollStateChanged(paramInt); 
        i++;
      } 
    } 
    OnPageChangeListener onPageChangeListener1 = this.mInternalPageChangeListener;
    if (onPageChangeListener1 != null)
      onPageChangeListener1.onPageScrollStateChanged(paramInt); 
  }
  
  private void enableLayers(boolean paramBoolean) {
    int j = getChildCount();
    for (int i = 0; i < j; i++) {
      boolean bool;
      if (paramBoolean) {
        bool = this.mPageTransformerLayerType;
      } else {
        bool = false;
      } 
      getChildAt(i).setLayerType(bool, null);
    } 
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView) {
    Rect rect = paramRect;
    if (paramRect == null)
      rect = new Rect(); 
    if (paramView == null) {
      rect.set(0, 0, 0, 0);
      return rect;
    } 
    rect.left = paramView.getLeft();
    rect.right = paramView.getRight();
    rect.top = paramView.getTop();
    rect.bottom = paramView.getBottom();
    ViewParent viewParent = paramView.getParent();
    while (viewParent instanceof ViewGroup && viewParent != this) {
      ViewGroup viewGroup = (ViewGroup)viewParent;
      rect.left += viewGroup.getLeft();
      rect.right += viewGroup.getRight();
      rect.top += viewGroup.getTop();
      rect.bottom += viewGroup.getBottom();
      ViewParent viewParent1 = viewGroup.getParent();
    } 
    return rect;
  }
  
  private int getClientWidth() {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }
  
  private ItemInfo infoForCurrentScrollPosition() {
    float f1;
    float f2;
    int i = getClientWidth();
    float f3 = 0.0F;
    if (i > 0) {
      f1 = getScrollX() / i;
    } else {
      f1 = 0.0F;
    } 
    if (i > 0) {
      f2 = this.mPageMargin / i;
    } else {
      f2 = 0.0F;
    } 
    ItemInfo itemInfo = null;
    i = 0;
    int j = -1;
    boolean bool = true;
    float f4 = 0.0F;
    while (i < this.mItems.size()) {
      ItemInfo itemInfo2 = this.mItems.get(i);
      int k = i;
      ItemInfo itemInfo1 = itemInfo2;
      if (!bool) {
        int m = itemInfo2.position;
        j++;
        k = i;
        itemInfo1 = itemInfo2;
        if (m != j) {
          itemInfo1 = this.mTempItem;
          itemInfo1.offset = f3 + f4 + f2;
          itemInfo1.position = j;
          itemInfo1.widthFactor = this.mAdapter.getPageWidth(itemInfo1.position);
          k = i - 1;
        } 
      } 
      f3 = itemInfo1.offset;
      f4 = itemInfo1.widthFactor;
      if (bool || f1 >= f3) {
        if (f1 >= f4 + f3 + f2) {
          if (k == this.mItems.size() - 1)
            return itemInfo1; 
          j = itemInfo1.position;
          f4 = itemInfo1.widthFactor;
          i = k + 1;
          bool = false;
          itemInfo = itemInfo1;
          continue;
        } 
        return itemInfo1;
      } 
      return itemInfo;
    } 
    return itemInfo;
  }
  
  private static boolean isDecorView(View paramView) {
    return (paramView.getClass().getAnnotation(DecorView.class) != null);
  }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2) {
    return ((paramFloat1 < this.mGutterSize && paramFloat2 > 0.0F) || (paramFloat1 > (getWidth() - this.mGutterSize) && paramFloat2 < 0.0F));
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionX = paramMotionEvent.getX(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null)
        velocityTracker.clear(); 
    } 
  }
  
  private boolean pageScrolled(int paramInt) {
    if (this.mItems.size() == 0) {
      if (this.mFirstLayout)
        return false; 
      this.mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (this.mCalledSuper)
        return false; 
      throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    } 
    ItemInfo itemInfo = infoForCurrentScrollPosition();
    int j = getClientWidth();
    int k = this.mPageMargin;
    float f2 = k;
    float f1 = j;
    f2 /= f1;
    int i = itemInfo.position;
    f1 = (paramInt / f1 - itemInfo.offset) / (itemInfo.widthFactor + f2);
    paramInt = (int)((j + k) * f1);
    this.mCalledSuper = false;
    onPageScrolled(i, f1, paramInt);
    if (this.mCalledSuper)
      return true; 
    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
  }
  
  private boolean performDrag(float paramFloat) {
    boolean bool1;
    float f1 = this.mLastMotionX;
    this.mLastMotionX = paramFloat;
    float f2 = getScrollX() + f1 - paramFloat;
    float f3 = getClientWidth();
    paramFloat = this.mFirstOffset * f3;
    f1 = this.mLastOffset * f3;
    ArrayList<ItemInfo> arrayList1 = this.mItems;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool2 = false;
    ItemInfo itemInfo1 = arrayList1.get(0);
    ArrayList<ItemInfo> arrayList2 = this.mItems;
    ItemInfo itemInfo2 = arrayList2.get(arrayList2.size() - 1);
    if (itemInfo1.position != 0) {
      paramFloat = itemInfo1.offset * f3;
      i = 0;
    } else {
      i = 1;
    } 
    if (itemInfo2.position != this.mAdapter.getCount() - 1) {
      f1 = itemInfo2.offset * f3;
      bool1 = false;
    } else {
      bool1 = true;
    } 
    if (f2 < paramFloat) {
      if (i) {
        this.mLeftEdge.onPull(Math.abs(paramFloat - f2) / f3);
        bool2 = true;
      } 
    } else {
      bool2 = bool4;
      paramFloat = f2;
      if (f2 > f1) {
        bool2 = bool3;
        if (bool1) {
          this.mRightEdge.onPull(Math.abs(f2 - f1) / f3);
          bool2 = true;
        } 
        paramFloat = f1;
      } 
    } 
    f1 = this.mLastMotionX;
    int i = (int)paramFloat;
    this.mLastMotionX = f1 + paramFloat - i;
    scrollTo(i, getScrollY());
    pageScrolled(i);
    return bool2;
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f;
    if (paramInt2 > 0 && !this.mItems.isEmpty()) {
      if (!this.mScroller.isFinished()) {
        this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
        return;
      } 
      int i = getPaddingLeft();
      int j = getPaddingRight();
      int k = getPaddingLeft();
      int m = getPaddingRight();
      scrollTo((int)(getScrollX() / (paramInt2 - k - m + paramInt4) * (paramInt1 - i - j + paramInt3)), getScrollY());
      return;
    } 
    ItemInfo itemInfo = infoForPosition(this.mCurItem);
    if (itemInfo != null) {
      f = Math.min(itemInfo.offset, this.mLastOffset);
    } else {
      f = 0.0F;
    } 
    paramInt1 = (int)(f * (paramInt1 - getPaddingLeft() - getPaddingRight()));
    if (paramInt1 != getScrollX()) {
      completeScroll(false);
      scrollTo(paramInt1, getScrollY());
    } 
  }
  
  private void removeNonDecorViews() {
    for (int i = 0; i < getChildCount(); i = j + 1) {
      int j = i;
      if (!((LayoutParams)getChildAt(i).getLayoutParams()).isDecor) {
        removeViewAt(i);
        j = i - 1;
      } 
    } 
  }
  
  private void requestParentDisallowInterceptTouchEvent(boolean paramBoolean) {
    ViewParent viewParent = getParent();
    if (viewParent != null)
      viewParent.requestDisallowInterceptTouchEvent(paramBoolean); 
  }
  
  private boolean resetTouch() {
    this.mActivePointerId = -1;
    endDrag();
    this.mLeftEdge.onRelease();
    this.mRightEdge.onRelease();
    return (this.mLeftEdge.isFinished() || this.mRightEdge.isFinished());
  }
  
  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2) {
    boolean bool;
    ItemInfo itemInfo = infoForPosition(paramInt1);
    if (itemInfo != null) {
      bool = (int)(getClientWidth() * Math.max(this.mFirstOffset, Math.min(itemInfo.offset, this.mLastOffset)));
    } else {
      bool = false;
    } 
    if (paramBoolean1) {
      smoothScrollTo(bool, 0, paramInt2);
      if (paramBoolean2) {
        dispatchOnPageSelected(paramInt1);
        return;
      } 
    } else {
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1); 
      completeScroll(false);
      scrollTo(bool, 0);
      pageScrolled(bool);
    } 
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean) {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean; 
  }
  
  private void sortChildDrawingOrder() {
    if (this.mDrawingOrder != 0) {
      ArrayList<View> arrayList = this.mDrawingOrderedChildren;
      if (arrayList == null) {
        this.mDrawingOrderedChildren = new ArrayList<View>();
      } else {
        arrayList.clear();
      } 
      int j = getChildCount();
      for (int i = 0; i < j; i++) {
        View view = getChildAt(i);
        this.mDrawingOrderedChildren.add(view);
      } 
      Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
    } 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216) {
      int k;
      for (k = 0; k < getChildCount(); k++) {
        View view = getChildAt(k);
        if (view.getVisibility() == 0) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null && itemInfo.position == this.mCurItem)
            view.addFocusables(paramArrayList, paramInt1, paramInt2); 
        } 
      } 
    } 
    if (j != 262144 || i == paramArrayList.size()) {
      if (!isFocusable())
        return; 
      if ((paramInt2 & 0x1) == 1 && isInTouchMode() && !isFocusableInTouchMode())
        return; 
      if (paramArrayList != null)
        paramArrayList.add(this); 
    } 
  }
  
  ItemInfo addNewItem(int paramInt1, int paramInt2) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.position = paramInt1;
    itemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    itemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if (paramInt2 < 0 || paramInt2 >= this.mItems.size()) {
      this.mItems.add(itemInfo);
      return itemInfo;
    } 
    this.mItems.add(paramInt2, itemInfo);
    return itemInfo;
  }
  
  public void addOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
    if (this.mAdapterChangeListeners == null)
      this.mAdapterChangeListeners = new ArrayList<OnAdapterChangeListener>(); 
    this.mAdapterChangeListeners.add(paramOnAdapterChangeListener);
  }
  
  public void addOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    if (this.mOnPageChangeListeners == null)
      this.mOnPageChangeListeners = new ArrayList<OnPageChangeListener>(); 
    this.mOnPageChangeListeners.add(paramOnPageChangeListener);
  }
  
  public void addTouchables(ArrayList<View> paramArrayList) {
    for (int i = 0; i < getChildCount(); i++) {
      View view = getChildAt(i);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem)
          view.addTouchables(paramArrayList); 
      } 
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    ViewGroup.LayoutParams layoutParams = paramLayoutParams;
    if (!checkLayoutParams(paramLayoutParams))
      layoutParams = generateLayoutParams(paramLayoutParams); 
    paramLayoutParams = layoutParams;
    ((LayoutParams)paramLayoutParams).isDecor |= isDecorView(paramView);
    if (this.mInLayout) {
      if (paramLayoutParams == null || !((LayoutParams)paramLayoutParams).isDecor) {
        ((LayoutParams)paramLayoutParams).needsMeasure = true;
        addViewInLayout(paramView, paramInt, layoutParams);
        return;
      } 
      throw new IllegalStateException("Cannot add pager decor view during layout");
    } 
    super.addView(paramView, paramInt, layoutParams);
  }
  
  public boolean arrowScroll(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual findFocus : ()Landroid/view/View;
    //   4: astore #6
    //   6: iconst_0
    //   7: istore #4
    //   9: aload #6
    //   11: aload_0
    //   12: if_acmpne -> 21
    //   15: aconst_null
    //   16: astore #5
    //   18: goto -> 194
    //   21: aload #6
    //   23: astore #5
    //   25: aload #6
    //   27: ifnull -> 194
    //   30: aload #6
    //   32: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   35: astore #5
    //   37: aload #5
    //   39: instanceof android/view/ViewGroup
    //   42: ifeq -> 68
    //   45: aload #5
    //   47: aload_0
    //   48: if_acmpne -> 56
    //   51: iconst_1
    //   52: istore_2
    //   53: goto -> 70
    //   56: aload #5
    //   58: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   63: astore #5
    //   65: goto -> 37
    //   68: iconst_0
    //   69: istore_2
    //   70: aload #6
    //   72: astore #5
    //   74: iload_2
    //   75: ifne -> 194
    //   78: new java/lang/StringBuilder
    //   81: dup
    //   82: invokespecial <init> : ()V
    //   85: astore #7
    //   87: aload #7
    //   89: aload #6
    //   91: invokevirtual getClass : ()Ljava/lang/Class;
    //   94: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: aload #6
    //   103: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   106: astore #5
    //   108: aload #5
    //   110: instanceof android/view/ViewGroup
    //   113: ifeq -> 151
    //   116: aload #7
    //   118: ldc_w ' => '
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload #7
    //   127: aload #5
    //   129: invokevirtual getClass : ()Ljava/lang/Class;
    //   132: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   135: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: pop
    //   139: aload #5
    //   141: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   146: astore #5
    //   148: goto -> 108
    //   151: new java/lang/StringBuilder
    //   154: dup
    //   155: invokespecial <init> : ()V
    //   158: astore #5
    //   160: aload #5
    //   162: ldc_w 'arrowScroll tried to find focus based on non-child current focused view '
    //   165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: pop
    //   169: aload #5
    //   171: aload #7
    //   173: invokevirtual toString : ()Ljava/lang/String;
    //   176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: ldc 'ViewPager'
    //   182: aload #5
    //   184: invokevirtual toString : ()Ljava/lang/String;
    //   187: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   190: pop
    //   191: goto -> 15
    //   194: invokestatic getInstance : ()Landroid/view/FocusFinder;
    //   197: aload_0
    //   198: aload #5
    //   200: iload_1
    //   201: invokevirtual findNextFocus : (Landroid/view/ViewGroup;Landroid/view/View;I)Landroid/view/View;
    //   204: astore #6
    //   206: aload #6
    //   208: ifnull -> 344
    //   211: aload #6
    //   213: aload #5
    //   215: if_acmpeq -> 344
    //   218: iload_1
    //   219: bipush #17
    //   221: if_icmpne -> 281
    //   224: aload_0
    //   225: aload_0
    //   226: getfield mTempRect : Landroid/graphics/Rect;
    //   229: aload #6
    //   231: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   234: getfield left : I
    //   237: istore_2
    //   238: aload_0
    //   239: aload_0
    //   240: getfield mTempRect : Landroid/graphics/Rect;
    //   243: aload #5
    //   245: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   248: getfield left : I
    //   251: istore_3
    //   252: aload #5
    //   254: ifnull -> 271
    //   257: iload_2
    //   258: iload_3
    //   259: if_icmplt -> 271
    //   262: aload_0
    //   263: invokevirtual pageLeft : ()Z
    //   266: istore #4
    //   268: goto -> 278
    //   271: aload #6
    //   273: invokevirtual requestFocus : ()Z
    //   276: istore #4
    //   278: goto -> 384
    //   281: iload_1
    //   282: bipush #66
    //   284: if_icmpne -> 384
    //   287: aload_0
    //   288: aload_0
    //   289: getfield mTempRect : Landroid/graphics/Rect;
    //   292: aload #6
    //   294: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   297: getfield left : I
    //   300: istore_2
    //   301: aload_0
    //   302: aload_0
    //   303: getfield mTempRect : Landroid/graphics/Rect;
    //   306: aload #5
    //   308: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   311: getfield left : I
    //   314: istore_3
    //   315: aload #5
    //   317: ifnull -> 334
    //   320: iload_2
    //   321: iload_3
    //   322: if_icmpgt -> 334
    //   325: aload_0
    //   326: invokevirtual pageRight : ()Z
    //   329: istore #4
    //   331: goto -> 278
    //   334: aload #6
    //   336: invokevirtual requestFocus : ()Z
    //   339: istore #4
    //   341: goto -> 278
    //   344: iload_1
    //   345: bipush #17
    //   347: if_icmpeq -> 378
    //   350: iload_1
    //   351: iconst_1
    //   352: if_icmpne -> 358
    //   355: goto -> 378
    //   358: iload_1
    //   359: bipush #66
    //   361: if_icmpeq -> 369
    //   364: iload_1
    //   365: iconst_2
    //   366: if_icmpne -> 384
    //   369: aload_0
    //   370: invokevirtual pageRight : ()Z
    //   373: istore #4
    //   375: goto -> 384
    //   378: aload_0
    //   379: invokevirtual pageLeft : ()Z
    //   382: istore #4
    //   384: iload #4
    //   386: ifeq -> 397
    //   389: aload_0
    //   390: iload_1
    //   391: invokestatic getContantForFocusDirection : (I)I
    //   394: invokevirtual playSoundEffect : (I)V
    //   397: iload #4
    //   399: ireturn
  }
  
  public boolean beginFakeDrag() {
    if (this.mIsBeingDragged)
      return false; 
    this.mFakeDragging = true;
    setScrollState(1);
    this.mLastMotionX = 0.0F;
    this.mInitialMotionX = 0.0F;
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
    } else {
      velocityTracker.clear();
    } 
    long l = SystemClock.uptimeMillis();
    MotionEvent motionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
    this.mVelocityTracker.addMovement(motionEvent);
    motionEvent.recycle();
    this.mFakeDragBeginTime = l;
    return true;
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int j = paramView.getScrollX();
      int k = paramView.getScrollY();
      int i;
      for (i = viewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = viewGroup.getChildAt(i);
        int m = paramInt2 + j;
        if (m >= view.getLeft() && m < view.getRight()) {
          int n = paramInt3 + k;
          if (n >= view.getTop() && n < view.getBottom() && canScroll(view, true, paramInt1, m - view.getLeft(), n - view.getTop()))
            return true; 
        } 
      } 
    } 
    return (paramBoolean && paramView.canScrollHorizontally(-paramInt1));
  }
  
  public boolean canScrollHorizontally(int paramInt) {
    PagerAdapter pagerAdapter = this.mAdapter;
    boolean bool2 = false;
    boolean bool1 = false;
    if (pagerAdapter == null)
      return false; 
    int i = getClientWidth();
    int j = getScrollX();
    if (paramInt < 0) {
      if (j > (int)(i * this.mFirstOffset))
        bool1 = true; 
      return bool1;
    } 
    bool1 = bool2;
    if (paramInt > 0) {
      bool1 = bool2;
      if (j < (int)(i * this.mLastOffset))
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams));
  }
  
  public void clearOnPageChangeListeners() {
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null)
      list.clear(); 
  }
  
  public void computeScroll() {
    this.mIsScrollStarted = true;
    if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if (i != k || j != m) {
        scrollTo(k, m);
        if (!pageScrolled(k)) {
          this.mScroller.abortAnimation();
          scrollTo(0, m);
        } 
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
      return;
    } 
    completeScroll(true);
  }
  
  void dataSetChanged() {
    Object object1;
    int i;
    Object object2;
    int k;
    boolean bool;
    int i1 = this.mAdapter.getCount();
    this.mExpectedAdapterCount = i1;
    if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < i1) {
      bool = true;
    } else {
      bool = false;
    } 
    int j = this.mCurItem;
    int n = 0;
    int m = n;
    while (n < this.mItems.size()) {
      ItemInfo itemInfo = this.mItems.get(n);
      int i5 = this.mAdapter.getItemPosition(itemInfo.object);
      if (i5 == -1) {
        Object object3 = object1;
        int i6 = n;
        Object object4 = object2;
        continue;
      } 
      if (i5 == -2) {
        byte b;
        this.mItems.remove(n);
        int i6 = n - 1;
        Object object = object2;
        if (object2 == null) {
          this.mAdapter.startUpdate(this);
          b = 1;
        } 
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
        n = i6;
        k = b;
        if (this.mCurItem == itemInfo.position) {
          i = Math.max(0, Math.min(this.mCurItem, i1 - 1));
          k = b;
          n = i6;
        } 
      } else {
        int i6 = i;
        int i8 = n;
        int i7 = k;
        if (itemInfo.position != i5) {
          if (itemInfo.position == this.mCurItem)
            i = i5; 
          itemInfo.position = i5;
        } else {
          continue;
        } 
      } 
      bool = true;
      int i2 = i;
      int i4 = n;
      int i3 = k;
      continue;
      n = SYNTHETIC_LOCAL_VARIABLE_7 + 1;
      object1 = SYNTHETIC_LOCAL_VARIABLE_5;
      object2 = SYNTHETIC_LOCAL_VARIABLE_6;
    } 
    if (k)
      this.mAdapter.finishUpdate(this); 
    Collections.sort(this.mItems, COMPARATOR);
    if (bool) {
      n = getChildCount();
      for (k = 0; k < n; k++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(k).getLayoutParams();
        if (!layoutParams.isDecor)
          layoutParams.widthFactor = 0.0F; 
      } 
      setCurrentItemInternal(i, false, true);
      requestLayout();
    } 
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (paramAccessibilityEvent.getEventType() == 4096)
      return super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent); 
    int j = getChildCount();
    for (int i = 0; i < j; i++) {
      View view = getChildAt(i);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent))
          return true; 
      } 
    } 
    return false;
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin(((paramFloat - 0.5F) * 0.47123894F));
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial draw : (Landroid/graphics/Canvas;)V
    //   5: aload_0
    //   6: invokevirtual getOverScrollMode : ()I
    //   9: istore #4
    //   11: iconst_0
    //   12: istore_3
    //   13: iconst_0
    //   14: istore_2
    //   15: iload #4
    //   17: ifeq -> 66
    //   20: iload #4
    //   22: iconst_1
    //   23: if_icmpne -> 49
    //   26: aload_0
    //   27: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   30: astore #8
    //   32: aload #8
    //   34: ifnull -> 49
    //   37: aload #8
    //   39: invokevirtual getCount : ()I
    //   42: iconst_1
    //   43: if_icmple -> 49
    //   46: goto -> 66
    //   49: aload_0
    //   50: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   53: invokevirtual finish : ()V
    //   56: aload_0
    //   57: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   60: invokevirtual finish : ()V
    //   63: goto -> 256
    //   66: aload_0
    //   67: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   70: invokevirtual isFinished : ()Z
    //   73: ifne -> 155
    //   76: aload_1
    //   77: invokevirtual save : ()I
    //   80: istore_3
    //   81: aload_0
    //   82: invokevirtual getHeight : ()I
    //   85: aload_0
    //   86: invokevirtual getPaddingTop : ()I
    //   89: isub
    //   90: aload_0
    //   91: invokevirtual getPaddingBottom : ()I
    //   94: isub
    //   95: istore_2
    //   96: aload_0
    //   97: invokevirtual getWidth : ()I
    //   100: istore #4
    //   102: aload_1
    //   103: ldc_w 270.0
    //   106: invokevirtual rotate : (F)V
    //   109: aload_1
    //   110: iload_2
    //   111: ineg
    //   112: aload_0
    //   113: invokevirtual getPaddingTop : ()I
    //   116: iadd
    //   117: i2f
    //   118: aload_0
    //   119: getfield mFirstOffset : F
    //   122: iload #4
    //   124: i2f
    //   125: fmul
    //   126: invokevirtual translate : (FF)V
    //   129: aload_0
    //   130: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   133: iload_2
    //   134: iload #4
    //   136: invokevirtual setSize : (II)V
    //   139: iconst_0
    //   140: aload_0
    //   141: getfield mLeftEdge : Landroid/widget/EdgeEffect;
    //   144: aload_1
    //   145: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   148: ior
    //   149: istore_2
    //   150: aload_1
    //   151: iload_3
    //   152: invokevirtual restoreToCount : (I)V
    //   155: iload_2
    //   156: istore_3
    //   157: aload_0
    //   158: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   161: invokevirtual isFinished : ()Z
    //   164: ifne -> 256
    //   167: aload_1
    //   168: invokevirtual save : ()I
    //   171: istore #4
    //   173: aload_0
    //   174: invokevirtual getWidth : ()I
    //   177: istore_3
    //   178: aload_0
    //   179: invokevirtual getHeight : ()I
    //   182: istore #5
    //   184: aload_0
    //   185: invokevirtual getPaddingTop : ()I
    //   188: istore #6
    //   190: aload_0
    //   191: invokevirtual getPaddingBottom : ()I
    //   194: istore #7
    //   196: aload_1
    //   197: ldc_w 90.0
    //   200: invokevirtual rotate : (F)V
    //   203: aload_1
    //   204: aload_0
    //   205: invokevirtual getPaddingTop : ()I
    //   208: ineg
    //   209: i2f
    //   210: aload_0
    //   211: getfield mLastOffset : F
    //   214: fconst_1
    //   215: fadd
    //   216: fneg
    //   217: iload_3
    //   218: i2f
    //   219: fmul
    //   220: invokevirtual translate : (FF)V
    //   223: aload_0
    //   224: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   227: iload #5
    //   229: iload #6
    //   231: isub
    //   232: iload #7
    //   234: isub
    //   235: iload_3
    //   236: invokevirtual setSize : (II)V
    //   239: iload_2
    //   240: aload_0
    //   241: getfield mRightEdge : Landroid/widget/EdgeEffect;
    //   244: aload_1
    //   245: invokevirtual draw : (Landroid/graphics/Canvas;)Z
    //   248: ior
    //   249: istore_3
    //   250: aload_1
    //   251: iload #4
    //   253: invokevirtual restoreToCount : (I)V
    //   256: iload_3
    //   257: ifeq -> 264
    //   260: aload_0
    //   261: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   264: return
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    Drawable drawable = this.mMarginDrawable;
    if (drawable != null && drawable.isStateful())
      drawable.setState(getDrawableState()); 
  }
  
  public void endFakeDrag() {
    if (this.mFakeDragging) {
      if (this.mAdapter != null) {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int i = (int)velocityTracker.getXVelocity(this.mActivePointerId);
        this.mPopulatePending = true;
        int j = getClientWidth();
        int k = getScrollX();
        ItemInfo itemInfo = infoForCurrentScrollPosition();
        setCurrentItemInternal(determineTargetPage(itemInfo.position, (k / j - itemInfo.offset) / itemInfo.widthFactor, i, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, i);
      } 
      endDrag();
      this.mFakeDragging = false;
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getAction() == 0) {
      int i = paramKeyEvent.getKeyCode();
      if (i != 21) {
        if (i != 22) {
          if (i == 61) {
            if (paramKeyEvent.hasNoModifiers())
              return arrowScroll(2); 
            if (paramKeyEvent.hasModifiers(1))
              return arrowScroll(1); 
          } 
        } else {
          return paramKeyEvent.hasModifiers(2) ? pageRight() : arrowScroll(66);
        } 
      } else {
        return paramKeyEvent.hasModifiers(2) ? pageLeft() : arrowScroll(17);
      } 
    } 
    return false;
  }
  
  public void fakeDragBy(float paramFloat) {
    if (this.mFakeDragging) {
      if (this.mAdapter == null)
        return; 
      this.mLastMotionX += paramFloat;
      float f2 = getScrollX() - paramFloat;
      float f3 = getClientWidth();
      paramFloat = this.mFirstOffset * f3;
      float f1 = this.mLastOffset * f3;
      ItemInfo itemInfo1 = this.mItems.get(0);
      ArrayList<ItemInfo> arrayList = this.mItems;
      ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
      if (itemInfo1.position != 0)
        paramFloat = itemInfo1.offset * f3; 
      if (itemInfo2.position != this.mAdapter.getCount() - 1)
        f1 = itemInfo2.offset * f3; 
      if (f2 >= paramFloat) {
        paramFloat = f2;
        if (f2 > f1)
          paramFloat = f1; 
      } 
      f1 = this.mLastMotionX;
      int i = (int)paramFloat;
      this.mLastMotionX = f1 + paramFloat - i;
      scrollTo(i, getScrollY());
      pageScrolled(i);
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
      this.mVelocityTracker.addMovement(motionEvent);
      motionEvent.recycle();
      return;
    } 
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return generateDefaultLayoutParams();
  }
  
  public PagerAdapter getAdapter() {
    return this.mAdapter;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    int i = paramInt2;
    if (this.mDrawingOrder == 2)
      i = paramInt1 - 1 - paramInt2; 
    return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(i)).getLayoutParams()).childIndex;
  }
  
  public int getCurrentItem() {
    return this.mCurItem;
  }
  
  public int getOffscreenPageLimit() {
    return this.mOffscreenPageLimit;
  }
  
  public int getPageMargin() {
    return this.mPageMargin;
  }
  
  ItemInfo infoForAnyChild(View paramView) {
    while (true) {
      ViewParent viewParent = paramView.getParent();
      if (viewParent != this) {
        if (viewParent != null) {
          if (!(viewParent instanceof View))
            return null; 
          paramView = (View)viewParent;
          continue;
        } 
        continue;
      } 
      return infoForChild(paramView);
    } 
  }
  
  ItemInfo infoForChild(View paramView) {
    for (int i = 0; i < this.mItems.size(); i++) {
      ItemInfo itemInfo = this.mItems.get(i);
      if (this.mAdapter.isViewFromObject(paramView, itemInfo.object))
        return itemInfo; 
    } 
    return null;
  }
  
  ItemInfo infoForPosition(int paramInt) {
    for (int i = 0; i < this.mItems.size(); i++) {
      ItemInfo itemInfo = this.mItems.get(i);
      if (itemInfo.position == paramInt)
        return itemInfo; 
    } 
    return null;
  }
  
  void initViewPager() {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context context = getContext();
    this.mScroller = new Scroller(context, sInterpolator);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    float f = (context.getResources().getDisplayMetrics()).density;
    this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    this.mMinimumVelocity = (int)(400.0F * f);
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffect(context);
    this.mRightEdge = new EdgeEffect(context);
    this.mFlingDistance = (int)(25.0F * f);
    this.mCloseEnough = (int)(2.0F * f);
    this.mDefaultGutterSize = (int)(f * 16.0F);
    ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility((View)this) == 0)
      ViewCompat.setImportantForAccessibility((View)this, 1); 
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          private final Rect mTempRect = new Rect();
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            WindowInsetsCompat windowInsetsCompat = ViewCompat.onApplyWindowInsets(param1View, param1WindowInsetsCompat);
            if (windowInsetsCompat.isConsumed())
              return windowInsetsCompat; 
            Rect rect = this.mTempRect;
            rect.left = windowInsetsCompat.getSystemWindowInsetLeft();
            rect.top = windowInsetsCompat.getSystemWindowInsetTop();
            rect.right = windowInsetsCompat.getSystemWindowInsetRight();
            rect.bottom = windowInsetsCompat.getSystemWindowInsetBottom();
            int i = 0;
            int j = ViewPager.this.getChildCount();
            while (i < j) {
              WindowInsetsCompat windowInsetsCompat1 = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), windowInsetsCompat);
              rect.left = Math.min(windowInsetsCompat1.getSystemWindowInsetLeft(), rect.left);
              rect.top = Math.min(windowInsetsCompat1.getSystemWindowInsetTop(), rect.top);
              rect.right = Math.min(windowInsetsCompat1.getSystemWindowInsetRight(), rect.right);
              rect.bottom = Math.min(windowInsetsCompat1.getSystemWindowInsetBottom(), rect.bottom);
              i++;
            } 
            return windowInsetsCompat.replaceSystemWindowInsets(rect.left, rect.top, rect.right, rect.bottom);
          }
        });
  }
  
  public boolean isFakeDragging() {
    return this.mFakeDragging;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.mEndScrollRunnable);
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished())
      this.mScroller.abortAnimation(); 
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
      int k = getScrollX();
      int m = getWidth();
      float f1 = this.mPageMargin;
      float f3 = m;
      float f2 = f1 / f3;
      ArrayList<ItemInfo> arrayList = this.mItems;
      int j = 0;
      ItemInfo itemInfo = arrayList.get(0);
      f1 = itemInfo.offset;
      int n = this.mItems.size();
      int i = itemInfo.position;
      int i1 = ((ItemInfo)this.mItems.get(n - 1)).position;
      while (i < i1) {
        float f;
        ItemInfo itemInfo1;
        while (i > itemInfo.position && j < n) {
          ArrayList<ItemInfo> arrayList1 = this.mItems;
          itemInfo1 = arrayList1.get(++j);
        } 
        if (i == itemInfo1.position) {
          f = (itemInfo1.offset + itemInfo1.widthFactor) * f3;
          f1 = itemInfo1.offset + itemInfo1.widthFactor + f2;
        } else {
          float f4 = this.mAdapter.getPageWidth(i);
          f = f1 + f4 + f2;
          f4 = (f1 + f4) * f3;
          f1 = f;
          f = f4;
        } 
        if (this.mPageMargin + f > k) {
          this.mMarginDrawable.setBounds(Math.round(f), this.mTopPageBounds, Math.round(this.mPageMargin + f), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        } 
        if (f > (k + m))
          return; 
        i++;
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i == 3 || i == 1) {
      resetTouch();
      return false;
    } 
    if (i != 0) {
      if (this.mIsBeingDragged)
        return true; 
      if (this.mIsUnableToDrag)
        return false; 
    } 
    if (i != 0) {
      if (i != 2) {
        if (i == 6)
          onSecondaryPointerUp(paramMotionEvent); 
      } else {
        i = this.mActivePointerId;
        if (i != -1) {
          i = paramMotionEvent.findPointerIndex(i);
          float f2 = paramMotionEvent.getX(i);
          float f1 = f2 - this.mLastMotionX;
          float f4 = Math.abs(f1);
          float f3 = paramMotionEvent.getY(i);
          float f5 = Math.abs(f3 - this.mInitialMotionY);
          i = f1 cmp 0.0F;
          if (i != 0 && !isGutterDrag(this.mLastMotionX, f1) && canScroll((View)this, false, (int)f1, (int)f2, (int)f3)) {
            this.mLastMotionX = f2;
            this.mLastMotionY = f3;
            this.mIsUnableToDrag = true;
            return false;
          } 
          if (f4 > this.mTouchSlop && f4 * 0.5F > f5) {
            this.mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            setScrollState(1);
            f1 = this.mInitialMotionX;
            f4 = this.mTouchSlop;
            if (i > 0) {
              f1 += f4;
            } else {
              f1 -= f4;
            } 
            this.mLastMotionX = f1;
            this.mLastMotionY = f3;
            setScrollingCacheEnabled(true);
          } else if (f5 > this.mTouchSlop) {
            this.mIsUnableToDrag = true;
          } 
          if (this.mIsBeingDragged && performDrag(f2))
            ViewCompat.postInvalidateOnAnimation((View)this); 
        } 
      } 
    } else {
      float f = paramMotionEvent.getX();
      this.mInitialMotionX = f;
      this.mLastMotionX = f;
      f = paramMotionEvent.getY();
      this.mInitialMotionY = f;
      this.mLastMotionY = f;
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      this.mIsUnableToDrag = false;
      this.mIsScrollStarted = true;
      this.mScroller.computeScrollOffset();
      if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        this.mIsBeingDragged = true;
        requestParentDisallowInterceptTouchEvent(true);
        setScrollState(1);
      } else {
        completeScroll(false);
        this.mIsBeingDragged = false;
      } 
    } 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    return this.mIsBeingDragged;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int m = getChildCount();
    int n = paramInt3 - paramInt1;
    int i1 = paramInt4 - paramInt2;
    paramInt2 = getPaddingLeft();
    paramInt1 = getPaddingTop();
    paramInt4 = getPaddingRight();
    paramInt3 = getPaddingBottom();
    int i2 = getScrollX();
    int k = 0;
    int j;
    for (j = 0; k < m; j = i3) {
      View view = getChildAt(k);
      int i7 = paramInt2;
      int i6 = paramInt1;
      int i5 = paramInt4;
      int i4 = paramInt3;
      int i3 = j;
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        i7 = paramInt2;
        i6 = paramInt1;
        i5 = paramInt4;
        i4 = paramInt3;
        i3 = j;
        if (layoutParams.isDecor) {
          i3 = layoutParams.gravity & 0x7;
          i5 = layoutParams.gravity & 0x70;
          if (i3 != 1) {
            if (i3 != 3) {
              if (i3 != 5) {
                i3 = paramInt2;
                i4 = paramInt2;
                paramInt2 = i3;
              } else {
                i3 = n - paramInt4 - view.getMeasuredWidth();
                paramInt4 += view.getMeasuredWidth();
                i4 = i3;
              } 
            } else {
              i3 = view.getMeasuredWidth() + paramInt2;
              i4 = paramInt2;
              paramInt2 = i3;
            } 
          } else {
            i3 = Math.max((n - view.getMeasuredWidth()) / 2, paramInt2);
            i4 = i3;
          } 
          if (i5 != 16) {
            if (i5 != 48) {
              if (i5 != 80) {
                i5 = paramInt1;
                i3 = paramInt1;
                paramInt1 = i5;
              } else {
                i3 = i1 - paramInt3 - view.getMeasuredHeight();
                paramInt3 += view.getMeasuredHeight();
              } 
            } else {
              i5 = view.getMeasuredHeight() + paramInt1;
              i3 = paramInt1;
              paramInt1 = i5;
            } 
          } else {
            i3 = Math.max((i1 - view.getMeasuredHeight()) / 2, paramInt1);
          } 
          i4 += i2;
          view.layout(i4, i3, view.getMeasuredWidth() + i4, i3 + view.getMeasuredHeight());
          i3 = j + 1;
          i4 = paramInt3;
          i5 = paramInt4;
          i6 = paramInt1;
          i7 = paramInt2;
        } 
      } 
      k++;
      paramInt2 = i7;
      paramInt1 = i6;
      paramInt4 = i5;
      paramInt3 = i4;
    } 
    int i;
    for (i = 0; i < m; i++) {
      View view = getChildAt(i);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.isDecor) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null) {
            float f = (n - paramInt2 - paramInt4);
            k = (int)(itemInfo.offset * f) + paramInt2;
            if (layoutParams.needsMeasure) {
              layoutParams.needsMeasure = false;
              view.measure(View.MeasureSpec.makeMeasureSpec((int)(f * layoutParams.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(i1 - paramInt1 - paramInt3, 1073741824));
            } 
            view.layout(k, paramInt1, view.getMeasuredWidth() + k, view.getMeasuredHeight() + paramInt1);
          } 
        } 
      } 
    } 
    this.mTopPageBounds = paramInt1;
    this.mBottomPageBounds = i1 - paramInt3;
    this.mDecorChildCount = j;
    if (this.mFirstLayout)
      scrollToItem(this.mCurItem, false, 0, false); 
    this.mFirstLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    paramInt1 = getMeasuredWidth();
    this.mGutterSize = Math.min(paramInt1 / 10, this.mDefaultGutterSize);
    paramInt1 = paramInt1 - getPaddingLeft() - getPaddingRight();
    paramInt2 = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int j = getChildCount();
    int i = 0;
    while (true) {
      int m = 1;
      int n = 1073741824;
      if (i < j) {
        View view = getChildAt(i);
        int i1 = paramInt1;
        int i2 = paramInt2;
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          i1 = paramInt1;
          i2 = paramInt2;
          if (layoutParams != null) {
            i1 = paramInt1;
            i2 = paramInt2;
            if (layoutParams.isDecor) {
              boolean bool;
              i1 = layoutParams.gravity & 0x7;
              i2 = layoutParams.gravity & 0x70;
              if (i2 == 48 || i2 == 80) {
                bool = true;
              } else {
                bool = false;
              } 
              int i3 = m;
              if (i1 != 3)
                if (i1 == 5) {
                  i3 = m;
                } else {
                  i3 = 0;
                }  
              i2 = Integer.MIN_VALUE;
              if (bool) {
                i1 = Integer.MIN_VALUE;
                i2 = 1073741824;
              } else if (i3) {
                i1 = 1073741824;
              } else {
                i1 = Integer.MIN_VALUE;
              } 
              if (layoutParams.width != -2) {
                if (layoutParams.width != -1) {
                  i2 = layoutParams.width;
                } else {
                  i2 = paramInt1;
                } 
                m = 1073741824;
              } else {
                m = i2;
                i2 = paramInt1;
              } 
              if (layoutParams.height != -2) {
                if (layoutParams.height != -1) {
                  i1 = layoutParams.height;
                } else {
                  i1 = paramInt2;
                } 
              } else {
                int i4 = paramInt2;
                n = i1;
                i1 = i4;
              } 
              view.measure(View.MeasureSpec.makeMeasureSpec(i2, m), View.MeasureSpec.makeMeasureSpec(i1, n));
              if (bool) {
                i2 = paramInt2 - view.getMeasuredHeight();
                i1 = paramInt1;
              } else {
                i1 = paramInt1;
                i2 = paramInt2;
                if (i3 != 0) {
                  i1 = paramInt1 - view.getMeasuredWidth();
                  i2 = paramInt2;
                } 
              } 
            } 
          } 
        } 
        i++;
        paramInt1 = i1;
        paramInt2 = i2;
        continue;
      } 
      this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
      this.mInLayout = true;
      populate();
      paramInt2 = 0;
      this.mInLayout = false;
      int k = getChildCount();
      while (paramInt2 < k) {
        View view = getChildAt(paramInt2);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (layoutParams == null || !layoutParams.isDecor)
            view.measure(View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * layoutParams.widthFactor), 1073741824), this.mChildHeightMeasureSpec); 
        } 
        paramInt2++;
      } 
      return;
    } 
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    int i = this.mDecorChildCount;
    boolean bool = false;
    if (i > 0) {
      int m = getScrollX();
      i = getPaddingLeft();
      int j = getPaddingRight();
      int n = getWidth();
      int i1 = getChildCount();
      int k;
      for (k = 0; k < i1; k++) {
        View view = getChildAt(k);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isDecor) {
          int i2 = layoutParams.gravity & 0x7;
          if (i2 != 1) {
            if (i2 != 3) {
              if (i2 != 5) {
                int i3 = i;
                i2 = i;
                i = i3;
              } else {
                i2 = n - j - view.getMeasuredWidth();
                j += view.getMeasuredWidth();
              } 
            } else {
              int i3 = view.getWidth() + i;
              i2 = i;
              i = i3;
            } 
          } else {
            i2 = Math.max((n - view.getMeasuredWidth()) / 2, i);
          } 
          i2 = i2 + m - view.getLeft();
          if (i2 != 0)
            view.offsetLeftAndRight(i2); 
        } 
      } 
    } 
    dispatchOnPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mPageTransformer != null) {
      paramInt2 = getScrollX();
      i = getChildCount();
      for (paramInt1 = bool; paramInt1 < i; paramInt1++) {
        View view = getChildAt(paramInt1);
        if (!((LayoutParams)view.getLayoutParams()).isDecor) {
          paramFloat = (view.getLeft() - paramInt2) / getClientWidth();
          this.mPageTransformer.transformPage(view, paramFloat);
        } 
      } 
    } 
    this.mCalledSuper = true;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    byte b;
    int i = getChildCount();
    int j = -1;
    if ((paramInt & 0x2) != 0) {
      j = i;
      i = 0;
      b = 1;
    } else {
      i--;
      b = -1;
    } 
    while (i != j) {
      View view = getChildAt(i);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem && view.requestFocus(paramInt, paramRect))
          return true; 
      } 
      i += b;
    } 
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null) {
      pagerAdapter.restoreState(savedState.adapterState, savedState.loader);
      setCurrentItemInternal(savedState.position, false, true);
      return;
    } 
    this.mRestoredCurItem = savedState.position;
    this.mRestoredAdapterState = savedState.adapterState;
    this.mRestoredClassLoader = savedState.loader;
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.position = this.mCurItem;
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null)
      savedState.adapterState = pagerAdapter.saveState(); 
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      paramInt2 = this.mPageMargin;
      recomputeScrollPosition(paramInt1, paramInt3, paramInt2, paramInt2);
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mFakeDragging : Z
    //   4: ifeq -> 9
    //   7: iconst_1
    //   8: ireturn
    //   9: aload_1
    //   10: invokevirtual getAction : ()I
    //   13: istore #6
    //   15: iconst_0
    //   16: istore #9
    //   18: iload #6
    //   20: ifne -> 32
    //   23: aload_1
    //   24: invokevirtual getEdgeFlags : ()I
    //   27: ifeq -> 32
    //   30: iconst_0
    //   31: ireturn
    //   32: aload_0
    //   33: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   36: astore #10
    //   38: aload #10
    //   40: ifnull -> 612
    //   43: aload #10
    //   45: invokevirtual getCount : ()I
    //   48: ifne -> 53
    //   51: iconst_0
    //   52: ireturn
    //   53: aload_0
    //   54: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   57: ifnonnull -> 67
    //   60: aload_0
    //   61: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   64: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   67: aload_0
    //   68: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   71: aload_1
    //   72: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   75: aload_1
    //   76: invokevirtual getAction : ()I
    //   79: sipush #255
    //   82: iand
    //   83: istore #6
    //   85: iload #6
    //   87: ifeq -> 546
    //   90: iload #6
    //   92: iconst_1
    //   93: if_icmpeq -> 406
    //   96: iload #6
    //   98: iconst_2
    //   99: if_icmpeq -> 204
    //   102: iload #6
    //   104: iconst_3
    //   105: if_icmpeq -> 177
    //   108: iload #6
    //   110: iconst_5
    //   111: if_icmpeq -> 148
    //   114: iload #6
    //   116: bipush #6
    //   118: if_icmpeq -> 124
    //   121: goto -> 601
    //   124: aload_0
    //   125: aload_1
    //   126: invokespecial onSecondaryPointerUp : (Landroid/view/MotionEvent;)V
    //   129: aload_0
    //   130: aload_1
    //   131: aload_1
    //   132: aload_0
    //   133: getfield mActivePointerId : I
    //   136: invokevirtual findPointerIndex : (I)I
    //   139: invokevirtual getX : (I)F
    //   142: putfield mLastMotionX : F
    //   145: goto -> 601
    //   148: aload_1
    //   149: invokevirtual getActionIndex : ()I
    //   152: istore #6
    //   154: aload_0
    //   155: aload_1
    //   156: iload #6
    //   158: invokevirtual getX : (I)F
    //   161: putfield mLastMotionX : F
    //   164: aload_0
    //   165: aload_1
    //   166: iload #6
    //   168: invokevirtual getPointerId : (I)I
    //   171: putfield mActivePointerId : I
    //   174: goto -> 601
    //   177: aload_0
    //   178: getfield mIsBeingDragged : Z
    //   181: ifeq -> 601
    //   184: aload_0
    //   185: aload_0
    //   186: getfield mCurItem : I
    //   189: iconst_1
    //   190: iconst_0
    //   191: iconst_0
    //   192: invokespecial scrollToItem : (IZIZ)V
    //   195: aload_0
    //   196: invokespecial resetTouch : ()Z
    //   199: istore #9
    //   201: goto -> 601
    //   204: aload_0
    //   205: getfield mIsBeingDragged : Z
    //   208: ifne -> 376
    //   211: aload_1
    //   212: aload_0
    //   213: getfield mActivePointerId : I
    //   216: invokevirtual findPointerIndex : (I)I
    //   219: istore #6
    //   221: iload #6
    //   223: iconst_m1
    //   224: if_icmpne -> 236
    //   227: aload_0
    //   228: invokespecial resetTouch : ()Z
    //   231: istore #9
    //   233: goto -> 601
    //   236: aload_1
    //   237: iload #6
    //   239: invokevirtual getX : (I)F
    //   242: fstore_2
    //   243: fload_2
    //   244: aload_0
    //   245: getfield mLastMotionX : F
    //   248: fsub
    //   249: invokestatic abs : (F)F
    //   252: fstore #4
    //   254: aload_1
    //   255: iload #6
    //   257: invokevirtual getY : (I)F
    //   260: fstore_3
    //   261: fload_3
    //   262: aload_0
    //   263: getfield mLastMotionY : F
    //   266: fsub
    //   267: invokestatic abs : (F)F
    //   270: fstore #5
    //   272: fload #4
    //   274: aload_0
    //   275: getfield mTouchSlop : I
    //   278: i2f
    //   279: fcmpl
    //   280: ifle -> 376
    //   283: fload #4
    //   285: fload #5
    //   287: fcmpl
    //   288: ifle -> 376
    //   291: aload_0
    //   292: iconst_1
    //   293: putfield mIsBeingDragged : Z
    //   296: aload_0
    //   297: iconst_1
    //   298: invokespecial requestParentDisallowInterceptTouchEvent : (Z)V
    //   301: aload_0
    //   302: getfield mInitialMotionX : F
    //   305: fstore #4
    //   307: fload_2
    //   308: fload #4
    //   310: fsub
    //   311: fconst_0
    //   312: fcmpl
    //   313: ifle -> 328
    //   316: fload #4
    //   318: aload_0
    //   319: getfield mTouchSlop : I
    //   322: i2f
    //   323: fadd
    //   324: fstore_2
    //   325: goto -> 337
    //   328: fload #4
    //   330: aload_0
    //   331: getfield mTouchSlop : I
    //   334: i2f
    //   335: fsub
    //   336: fstore_2
    //   337: aload_0
    //   338: fload_2
    //   339: putfield mLastMotionX : F
    //   342: aload_0
    //   343: fload_3
    //   344: putfield mLastMotionY : F
    //   347: aload_0
    //   348: iconst_1
    //   349: invokevirtual setScrollState : (I)V
    //   352: aload_0
    //   353: iconst_1
    //   354: invokespecial setScrollingCacheEnabled : (Z)V
    //   357: aload_0
    //   358: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   361: astore #10
    //   363: aload #10
    //   365: ifnull -> 376
    //   368: aload #10
    //   370: iconst_1
    //   371: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   376: aload_0
    //   377: getfield mIsBeingDragged : Z
    //   380: ifeq -> 601
    //   383: iconst_0
    //   384: aload_0
    //   385: aload_1
    //   386: aload_1
    //   387: aload_0
    //   388: getfield mActivePointerId : I
    //   391: invokevirtual findPointerIndex : (I)I
    //   394: invokevirtual getX : (I)F
    //   397: invokespecial performDrag : (F)Z
    //   400: ior
    //   401: istore #9
    //   403: goto -> 601
    //   406: aload_0
    //   407: getfield mIsBeingDragged : Z
    //   410: ifeq -> 601
    //   413: aload_0
    //   414: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   417: astore #10
    //   419: aload #10
    //   421: sipush #1000
    //   424: aload_0
    //   425: getfield mMaximumVelocity : I
    //   428: i2f
    //   429: invokevirtual computeCurrentVelocity : (IF)V
    //   432: aload #10
    //   434: aload_0
    //   435: getfield mActivePointerId : I
    //   438: invokevirtual getXVelocity : (I)F
    //   441: f2i
    //   442: istore #6
    //   444: aload_0
    //   445: iconst_1
    //   446: putfield mPopulatePending : Z
    //   449: aload_0
    //   450: invokespecial getClientWidth : ()I
    //   453: istore #7
    //   455: aload_0
    //   456: invokevirtual getScrollX : ()I
    //   459: istore #8
    //   461: aload_0
    //   462: invokespecial infoForCurrentScrollPosition : ()Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   465: astore #10
    //   467: aload_0
    //   468: getfield mPageMargin : I
    //   471: i2f
    //   472: fstore_3
    //   473: iload #7
    //   475: i2f
    //   476: fstore_2
    //   477: fload_3
    //   478: fload_2
    //   479: fdiv
    //   480: fstore_3
    //   481: aload_0
    //   482: aload_0
    //   483: aload #10
    //   485: getfield position : I
    //   488: iload #8
    //   490: i2f
    //   491: fload_2
    //   492: fdiv
    //   493: aload #10
    //   495: getfield offset : F
    //   498: fsub
    //   499: aload #10
    //   501: getfield widthFactor : F
    //   504: fload_3
    //   505: fadd
    //   506: fdiv
    //   507: iload #6
    //   509: aload_1
    //   510: aload_1
    //   511: aload_0
    //   512: getfield mActivePointerId : I
    //   515: invokevirtual findPointerIndex : (I)I
    //   518: invokevirtual getX : (I)F
    //   521: aload_0
    //   522: getfield mInitialMotionX : F
    //   525: fsub
    //   526: f2i
    //   527: invokespecial determineTargetPage : (IFII)I
    //   530: iconst_1
    //   531: iconst_1
    //   532: iload #6
    //   534: invokevirtual setCurrentItemInternal : (IZZI)V
    //   537: aload_0
    //   538: invokespecial resetTouch : ()Z
    //   541: istore #9
    //   543: goto -> 601
    //   546: aload_0
    //   547: getfield mScroller : Landroid/widget/Scroller;
    //   550: invokevirtual abortAnimation : ()V
    //   553: aload_0
    //   554: iconst_0
    //   555: putfield mPopulatePending : Z
    //   558: aload_0
    //   559: invokevirtual populate : ()V
    //   562: aload_1
    //   563: invokevirtual getX : ()F
    //   566: fstore_2
    //   567: aload_0
    //   568: fload_2
    //   569: putfield mInitialMotionX : F
    //   572: aload_0
    //   573: fload_2
    //   574: putfield mLastMotionX : F
    //   577: aload_1
    //   578: invokevirtual getY : ()F
    //   581: fstore_2
    //   582: aload_0
    //   583: fload_2
    //   584: putfield mInitialMotionY : F
    //   587: aload_0
    //   588: fload_2
    //   589: putfield mLastMotionY : F
    //   592: aload_0
    //   593: aload_1
    //   594: iconst_0
    //   595: invokevirtual getPointerId : (I)I
    //   598: putfield mActivePointerId : I
    //   601: iload #9
    //   603: ifeq -> 610
    //   606: aload_0
    //   607: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   610: iconst_1
    //   611: ireturn
    //   612: iconst_0
    //   613: ireturn
  }
  
  boolean pageLeft() {
    int i = this.mCurItem;
    if (i > 0) {
      setCurrentItem(i - 1, true);
      return true;
    } 
    return false;
  }
  
  boolean pageRight() {
    PagerAdapter pagerAdapter = this.mAdapter;
    if (pagerAdapter != null && this.mCurItem < pagerAdapter.getCount() - 1) {
      setCurrentItem(this.mCurItem + 1, true);
      return true;
    } 
    return false;
  }
  
  void populate() {
    populate(this.mCurItem);
  }
  
  void populate(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mCurItem : I
    //   4: istore #5
    //   6: iload #5
    //   8: iload_1
    //   9: if_icmpeq -> 28
    //   12: aload_0
    //   13: iload #5
    //   15: invokevirtual infoForPosition : (I)Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   18: astore #14
    //   20: aload_0
    //   21: iload_1
    //   22: putfield mCurItem : I
    //   25: goto -> 31
    //   28: aconst_null
    //   29: astore #14
    //   31: aload_0
    //   32: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   35: ifnonnull -> 43
    //   38: aload_0
    //   39: invokespecial sortChildDrawingOrder : ()V
    //   42: return
    //   43: aload_0
    //   44: getfield mPopulatePending : Z
    //   47: ifeq -> 55
    //   50: aload_0
    //   51: invokespecial sortChildDrawingOrder : ()V
    //   54: return
    //   55: aload_0
    //   56: invokevirtual getWindowToken : ()Landroid/os/IBinder;
    //   59: ifnonnull -> 63
    //   62: return
    //   63: aload_0
    //   64: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   67: aload_0
    //   68: invokevirtual startUpdate : (Landroid/view/ViewGroup;)V
    //   71: aload_0
    //   72: getfield mOffscreenPageLimit : I
    //   75: istore_1
    //   76: iconst_0
    //   77: aload_0
    //   78: getfield mCurItem : I
    //   81: iload_1
    //   82: isub
    //   83: invokestatic max : (II)I
    //   86: istore #11
    //   88: aload_0
    //   89: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   92: invokevirtual getCount : ()I
    //   95: istore #9
    //   97: iload #9
    //   99: iconst_1
    //   100: isub
    //   101: aload_0
    //   102: getfield mCurItem : I
    //   105: iload_1
    //   106: iadd
    //   107: invokestatic min : (II)I
    //   110: istore #10
    //   112: iload #9
    //   114: aload_0
    //   115: getfield mExpectedAdapterCount : I
    //   118: if_icmpne -> 1210
    //   121: iconst_0
    //   122: istore_1
    //   123: iload_1
    //   124: aload_0
    //   125: getfield mItems : Ljava/util/ArrayList;
    //   128: invokevirtual size : ()I
    //   131: if_icmpge -> 181
    //   134: aload_0
    //   135: getfield mItems : Ljava/util/ArrayList;
    //   138: iload_1
    //   139: invokevirtual get : (I)Ljava/lang/Object;
    //   142: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   145: astore #13
    //   147: aload #13
    //   149: getfield position : I
    //   152: aload_0
    //   153: getfield mCurItem : I
    //   156: if_icmplt -> 174
    //   159: aload #13
    //   161: getfield position : I
    //   164: aload_0
    //   165: getfield mCurItem : I
    //   168: if_icmpne -> 181
    //   171: goto -> 184
    //   174: iload_1
    //   175: iconst_1
    //   176: iadd
    //   177: istore_1
    //   178: goto -> 123
    //   181: aconst_null
    //   182: astore #13
    //   184: aload #13
    //   186: astore #15
    //   188: aload #13
    //   190: ifnonnull -> 213
    //   193: aload #13
    //   195: astore #15
    //   197: iload #9
    //   199: ifle -> 213
    //   202: aload_0
    //   203: aload_0
    //   204: getfield mCurItem : I
    //   207: iload_1
    //   208: invokevirtual addNewItem : (II)Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   211: astore #15
    //   213: aload #15
    //   215: ifnull -> 994
    //   218: iload_1
    //   219: iconst_1
    //   220: isub
    //   221: istore #5
    //   223: iload #5
    //   225: iflt -> 245
    //   228: aload_0
    //   229: getfield mItems : Ljava/util/ArrayList;
    //   232: iload #5
    //   234: invokevirtual get : (I)Ljava/lang/Object;
    //   237: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   240: astore #13
    //   242: goto -> 248
    //   245: aconst_null
    //   246: astore #13
    //   248: aload_0
    //   249: invokespecial getClientWidth : ()I
    //   252: istore #12
    //   254: iload #12
    //   256: ifgt -> 265
    //   259: fconst_0
    //   260: fstore #4
    //   262: goto -> 284
    //   265: fconst_2
    //   266: aload #15
    //   268: getfield widthFactor : F
    //   271: fsub
    //   272: aload_0
    //   273: invokevirtual getPaddingLeft : ()I
    //   276: i2f
    //   277: iload #12
    //   279: i2f
    //   280: fdiv
    //   281: fadd
    //   282: fstore #4
    //   284: aload_0
    //   285: getfield mCurItem : I
    //   288: iconst_1
    //   289: isub
    //   290: istore #8
    //   292: fconst_0
    //   293: fstore_3
    //   294: iload #8
    //   296: iflt -> 591
    //   299: fload_3
    //   300: fload #4
    //   302: fcmpl
    //   303: iflt -> 433
    //   306: iload #8
    //   308: iload #11
    //   310: if_icmpge -> 433
    //   313: aload #13
    //   315: ifnonnull -> 321
    //   318: goto -> 591
    //   321: iload_1
    //   322: istore #7
    //   324: iload #5
    //   326: istore #6
    //   328: aload #13
    //   330: astore #16
    //   332: fload_3
    //   333: fstore_2
    //   334: iload #8
    //   336: aload #13
    //   338: getfield position : I
    //   341: if_icmpne -> 569
    //   344: iload_1
    //   345: istore #7
    //   347: iload #5
    //   349: istore #6
    //   351: aload #13
    //   353: astore #16
    //   355: fload_3
    //   356: fstore_2
    //   357: aload #13
    //   359: getfield scrolling : Z
    //   362: ifne -> 569
    //   365: aload_0
    //   366: getfield mItems : Ljava/util/ArrayList;
    //   369: iload #5
    //   371: invokevirtual remove : (I)Ljava/lang/Object;
    //   374: pop
    //   375: aload_0
    //   376: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   379: aload_0
    //   380: iload #8
    //   382: aload #13
    //   384: getfield object : Ljava/lang/Object;
    //   387: invokevirtual destroyItem : (Landroid/view/ViewGroup;ILjava/lang/Object;)V
    //   390: iload #5
    //   392: iconst_1
    //   393: isub
    //   394: istore #5
    //   396: iload_1
    //   397: iconst_1
    //   398: isub
    //   399: istore_1
    //   400: iload_1
    //   401: istore #6
    //   403: iload #5
    //   405: istore #7
    //   407: fload_3
    //   408: fstore_2
    //   409: iload #5
    //   411: iflt -> 548
    //   414: aload_0
    //   415: getfield mItems : Ljava/util/ArrayList;
    //   418: iload #5
    //   420: invokevirtual get : (I)Ljava/lang/Object;
    //   423: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   426: astore #13
    //   428: fload_3
    //   429: fstore_2
    //   430: goto -> 558
    //   433: aload #13
    //   435: ifnull -> 495
    //   438: iload #8
    //   440: aload #13
    //   442: getfield position : I
    //   445: if_icmpne -> 495
    //   448: fload_3
    //   449: aload #13
    //   451: getfield widthFactor : F
    //   454: fadd
    //   455: fstore_3
    //   456: iload #5
    //   458: iconst_1
    //   459: isub
    //   460: istore #5
    //   462: iload_1
    //   463: istore #6
    //   465: iload #5
    //   467: istore #7
    //   469: fload_3
    //   470: fstore_2
    //   471: iload #5
    //   473: iflt -> 548
    //   476: aload_0
    //   477: getfield mItems : Ljava/util/ArrayList;
    //   480: iload #5
    //   482: invokevirtual get : (I)Ljava/lang/Object;
    //   485: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   488: astore #13
    //   490: fload_3
    //   491: fstore_2
    //   492: goto -> 558
    //   495: fload_3
    //   496: aload_0
    //   497: iload #8
    //   499: iload #5
    //   501: iconst_1
    //   502: iadd
    //   503: invokevirtual addNewItem : (II)Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   506: getfield widthFactor : F
    //   509: fadd
    //   510: fstore_3
    //   511: iload_1
    //   512: iconst_1
    //   513: iadd
    //   514: istore_1
    //   515: iload_1
    //   516: istore #6
    //   518: iload #5
    //   520: istore #7
    //   522: fload_3
    //   523: fstore_2
    //   524: iload #5
    //   526: iflt -> 548
    //   529: aload_0
    //   530: getfield mItems : Ljava/util/ArrayList;
    //   533: iload #5
    //   535: invokevirtual get : (I)Ljava/lang/Object;
    //   538: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   541: astore #13
    //   543: fload_3
    //   544: fstore_2
    //   545: goto -> 558
    //   548: aconst_null
    //   549: astore #13
    //   551: iload #7
    //   553: istore #5
    //   555: iload #6
    //   557: istore_1
    //   558: aload #13
    //   560: astore #16
    //   562: iload #5
    //   564: istore #6
    //   566: iload_1
    //   567: istore #7
    //   569: iload #8
    //   571: iconst_1
    //   572: isub
    //   573: istore #8
    //   575: iload #7
    //   577: istore_1
    //   578: iload #6
    //   580: istore #5
    //   582: aload #16
    //   584: astore #13
    //   586: fload_2
    //   587: fstore_3
    //   588: goto -> 294
    //   591: aload #15
    //   593: getfield widthFactor : F
    //   596: fstore_3
    //   597: iload_1
    //   598: iconst_1
    //   599: iadd
    //   600: istore #6
    //   602: fload_3
    //   603: fconst_2
    //   604: fcmpg
    //   605: ifge -> 968
    //   608: iload #6
    //   610: aload_0
    //   611: getfield mItems : Ljava/util/ArrayList;
    //   614: invokevirtual size : ()I
    //   617: if_icmpge -> 637
    //   620: aload_0
    //   621: getfield mItems : Ljava/util/ArrayList;
    //   624: iload #6
    //   626: invokevirtual get : (I)Ljava/lang/Object;
    //   629: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   632: astore #13
    //   634: goto -> 640
    //   637: aconst_null
    //   638: astore #13
    //   640: iload #12
    //   642: ifgt -> 651
    //   645: fconst_0
    //   646: fstore #4
    //   648: goto -> 664
    //   651: aload_0
    //   652: invokevirtual getPaddingRight : ()I
    //   655: i2f
    //   656: iload #12
    //   658: i2f
    //   659: fdiv
    //   660: fconst_2
    //   661: fadd
    //   662: fstore #4
    //   664: aload_0
    //   665: getfield mCurItem : I
    //   668: istore #5
    //   670: aload #13
    //   672: astore #16
    //   674: iload #5
    //   676: iconst_1
    //   677: iadd
    //   678: istore #7
    //   680: iload #7
    //   682: iload #9
    //   684: if_icmpge -> 968
    //   687: fload_3
    //   688: fload #4
    //   690: fcmpl
    //   691: iflt -> 819
    //   694: iload #7
    //   696: iload #10
    //   698: if_icmple -> 819
    //   701: aload #16
    //   703: ifnonnull -> 709
    //   706: goto -> 968
    //   709: fload_3
    //   710: fstore_2
    //   711: iload #6
    //   713: istore #5
    //   715: aload #16
    //   717: astore #13
    //   719: iload #7
    //   721: aload #16
    //   723: getfield position : I
    //   726: if_icmpne -> 951
    //   729: fload_3
    //   730: fstore_2
    //   731: iload #6
    //   733: istore #5
    //   735: aload #16
    //   737: astore #13
    //   739: aload #16
    //   741: getfield scrolling : Z
    //   744: ifne -> 951
    //   747: aload_0
    //   748: getfield mItems : Ljava/util/ArrayList;
    //   751: iload #6
    //   753: invokevirtual remove : (I)Ljava/lang/Object;
    //   756: pop
    //   757: aload_0
    //   758: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   761: aload_0
    //   762: iload #7
    //   764: aload #16
    //   766: getfield object : Ljava/lang/Object;
    //   769: invokevirtual destroyItem : (Landroid/view/ViewGroup;ILjava/lang/Object;)V
    //   772: fload_3
    //   773: fstore_2
    //   774: iload #6
    //   776: istore #5
    //   778: iload #6
    //   780: aload_0
    //   781: getfield mItems : Ljava/util/ArrayList;
    //   784: invokevirtual size : ()I
    //   787: if_icmpge -> 813
    //   790: aload_0
    //   791: getfield mItems : Ljava/util/ArrayList;
    //   794: iload #6
    //   796: invokevirtual get : (I)Ljava/lang/Object;
    //   799: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   802: astore #13
    //   804: fload_3
    //   805: fstore_2
    //   806: iload #6
    //   808: istore #5
    //   810: goto -> 951
    //   813: aconst_null
    //   814: astore #13
    //   816: goto -> 951
    //   819: aload #16
    //   821: ifnull -> 889
    //   824: iload #7
    //   826: aload #16
    //   828: getfield position : I
    //   831: if_icmpne -> 889
    //   834: fload_3
    //   835: aload #16
    //   837: getfield widthFactor : F
    //   840: fadd
    //   841: fstore_3
    //   842: iload #6
    //   844: iconst_1
    //   845: iadd
    //   846: istore #6
    //   848: fload_3
    //   849: fstore_2
    //   850: iload #6
    //   852: istore #5
    //   854: iload #6
    //   856: aload_0
    //   857: getfield mItems : Ljava/util/ArrayList;
    //   860: invokevirtual size : ()I
    //   863: if_icmpge -> 813
    //   866: aload_0
    //   867: getfield mItems : Ljava/util/ArrayList;
    //   870: iload #6
    //   872: invokevirtual get : (I)Ljava/lang/Object;
    //   875: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   878: astore #13
    //   880: fload_3
    //   881: fstore_2
    //   882: iload #6
    //   884: istore #5
    //   886: goto -> 951
    //   889: aload_0
    //   890: iload #7
    //   892: iload #6
    //   894: invokevirtual addNewItem : (II)Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   897: astore #13
    //   899: iload #6
    //   901: iconst_1
    //   902: iadd
    //   903: istore #6
    //   905: fload_3
    //   906: aload #13
    //   908: getfield widthFactor : F
    //   911: fadd
    //   912: fstore_3
    //   913: fload_3
    //   914: fstore_2
    //   915: iload #6
    //   917: istore #5
    //   919: iload #6
    //   921: aload_0
    //   922: getfield mItems : Ljava/util/ArrayList;
    //   925: invokevirtual size : ()I
    //   928: if_icmpge -> 813
    //   931: aload_0
    //   932: getfield mItems : Ljava/util/ArrayList;
    //   935: iload #6
    //   937: invokevirtual get : (I)Ljava/lang/Object;
    //   940: checkcast androidx/viewpager/widget/ViewPager$ItemInfo
    //   943: astore #13
    //   945: iload #6
    //   947: istore #5
    //   949: fload_3
    //   950: fstore_2
    //   951: fload_2
    //   952: fstore_3
    //   953: iload #5
    //   955: istore #6
    //   957: aload #13
    //   959: astore #16
    //   961: iload #7
    //   963: istore #5
    //   965: goto -> 674
    //   968: aload_0
    //   969: aload #15
    //   971: iload_1
    //   972: aload #14
    //   974: invokespecial calculatePageOffsets : (Landroidx/viewpager/widget/ViewPager$ItemInfo;ILandroidx/viewpager/widget/ViewPager$ItemInfo;)V
    //   977: aload_0
    //   978: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   981: aload_0
    //   982: aload_0
    //   983: getfield mCurItem : I
    //   986: aload #15
    //   988: getfield object : Ljava/lang/Object;
    //   991: invokevirtual setPrimaryItem : (Landroid/view/ViewGroup;ILjava/lang/Object;)V
    //   994: aload_0
    //   995: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   998: aload_0
    //   999: invokevirtual finishUpdate : (Landroid/view/ViewGroup;)V
    //   1002: aload_0
    //   1003: invokevirtual getChildCount : ()I
    //   1006: istore #5
    //   1008: iconst_0
    //   1009: istore_1
    //   1010: iload_1
    //   1011: iload #5
    //   1013: if_icmpge -> 1097
    //   1016: aload_0
    //   1017: iload_1
    //   1018: invokevirtual getChildAt : (I)Landroid/view/View;
    //   1021: astore #14
    //   1023: aload #14
    //   1025: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1028: checkcast androidx/viewpager/widget/ViewPager$LayoutParams
    //   1031: astore #13
    //   1033: aload #13
    //   1035: iload_1
    //   1036: putfield childIndex : I
    //   1039: aload #13
    //   1041: getfield isDecor : Z
    //   1044: ifne -> 1090
    //   1047: aload #13
    //   1049: getfield widthFactor : F
    //   1052: fconst_0
    //   1053: fcmpl
    //   1054: ifne -> 1090
    //   1057: aload_0
    //   1058: aload #14
    //   1060: invokevirtual infoForChild : (Landroid/view/View;)Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   1063: astore #14
    //   1065: aload #14
    //   1067: ifnull -> 1090
    //   1070: aload #13
    //   1072: aload #14
    //   1074: getfield widthFactor : F
    //   1077: putfield widthFactor : F
    //   1080: aload #13
    //   1082: aload #14
    //   1084: getfield position : I
    //   1087: putfield position : I
    //   1090: iload_1
    //   1091: iconst_1
    //   1092: iadd
    //   1093: istore_1
    //   1094: goto -> 1010
    //   1097: aload_0
    //   1098: invokespecial sortChildDrawingOrder : ()V
    //   1101: aload_0
    //   1102: invokevirtual hasFocus : ()Z
    //   1105: ifeq -> 1209
    //   1108: aload_0
    //   1109: invokevirtual findFocus : ()Landroid/view/View;
    //   1112: astore #13
    //   1114: aload #13
    //   1116: ifnull -> 1130
    //   1119: aload_0
    //   1120: aload #13
    //   1122: invokevirtual infoForAnyChild : (Landroid/view/View;)Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   1125: astore #13
    //   1127: goto -> 1133
    //   1130: aconst_null
    //   1131: astore #13
    //   1133: aload #13
    //   1135: ifnull -> 1150
    //   1138: aload #13
    //   1140: getfield position : I
    //   1143: aload_0
    //   1144: getfield mCurItem : I
    //   1147: if_icmpeq -> 1209
    //   1150: iconst_0
    //   1151: istore_1
    //   1152: iload_1
    //   1153: aload_0
    //   1154: invokevirtual getChildCount : ()I
    //   1157: if_icmpge -> 1209
    //   1160: aload_0
    //   1161: iload_1
    //   1162: invokevirtual getChildAt : (I)Landroid/view/View;
    //   1165: astore #13
    //   1167: aload_0
    //   1168: aload #13
    //   1170: invokevirtual infoForChild : (Landroid/view/View;)Landroidx/viewpager/widget/ViewPager$ItemInfo;
    //   1173: astore #14
    //   1175: aload #14
    //   1177: ifnull -> 1202
    //   1180: aload #14
    //   1182: getfield position : I
    //   1185: aload_0
    //   1186: getfield mCurItem : I
    //   1189: if_icmpne -> 1202
    //   1192: aload #13
    //   1194: iconst_2
    //   1195: invokevirtual requestFocus : (I)Z
    //   1198: ifeq -> 1202
    //   1201: return
    //   1202: iload_1
    //   1203: iconst_1
    //   1204: iadd
    //   1205: istore_1
    //   1206: goto -> 1152
    //   1209: return
    //   1210: aload_0
    //   1211: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   1214: aload_0
    //   1215: invokevirtual getId : ()I
    //   1218: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   1221: astore #13
    //   1223: goto -> 1235
    //   1226: aload_0
    //   1227: invokevirtual getId : ()I
    //   1230: invokestatic toHexString : (I)Ljava/lang/String;
    //   1233: astore #13
    //   1235: new java/lang/StringBuilder
    //   1238: dup
    //   1239: invokespecial <init> : ()V
    //   1242: astore #14
    //   1244: aload #14
    //   1246: ldc_w 'The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: '
    //   1249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1252: pop
    //   1253: aload #14
    //   1255: aload_0
    //   1256: getfield mExpectedAdapterCount : I
    //   1259: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1262: pop
    //   1263: aload #14
    //   1265: ldc_w ', found: '
    //   1268: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1271: pop
    //   1272: aload #14
    //   1274: iload #9
    //   1276: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1279: pop
    //   1280: aload #14
    //   1282: ldc_w ' Pager id: '
    //   1285: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1288: pop
    //   1289: aload #14
    //   1291: aload #13
    //   1293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1296: pop
    //   1297: aload #14
    //   1299: ldc_w ' Pager class: '
    //   1302: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1305: pop
    //   1306: aload #14
    //   1308: aload_0
    //   1309: invokevirtual getClass : ()Ljava/lang/Class;
    //   1312: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1315: pop
    //   1316: aload #14
    //   1318: ldc_w ' Problematic adapter: '
    //   1321: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1324: pop
    //   1325: aload #14
    //   1327: aload_0
    //   1328: getfield mAdapter : Landroidx/viewpager/widget/PagerAdapter;
    //   1331: invokevirtual getClass : ()Ljava/lang/Class;
    //   1334: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1337: pop
    //   1338: new java/lang/IllegalStateException
    //   1341: dup
    //   1342: aload #14
    //   1344: invokevirtual toString : ()Ljava/lang/String;
    //   1347: invokespecial <init> : (Ljava/lang/String;)V
    //   1350: athrow
    //   1351: astore #13
    //   1353: goto -> 1226
    // Exception table:
    //   from	to	target	type
    //   1210	1223	1351	android/content/res/Resources$NotFoundException
  }
  
  public void removeOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
    List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
    if (list != null)
      list.remove(paramOnAdapterChangeListener); 
  }
  
  public void removeOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    List<OnPageChangeListener> list = this.mOnPageChangeListeners;
    if (list != null)
      list.remove(paramOnPageChangeListener); 
  }
  
  public void removeView(View paramView) {
    if (this.mInLayout) {
      removeViewInLayout(paramView);
      return;
    } 
    super.removeView(paramView);
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter) {
    PagerAdapter pagerAdapter = this.mAdapter;
    byte b = 0;
    if (pagerAdapter != null) {
      pagerAdapter.setViewPagerObserver(null);
      this.mAdapter.startUpdate(this);
      for (int i = 0; i < this.mItems.size(); i++) {
        ItemInfo itemInfo = this.mItems.get(i);
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
      } 
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    } 
    pagerAdapter = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    this.mExpectedAdapterCount = 0;
    if (paramPagerAdapter != null) {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver(); 
      this.mAdapter.setViewPagerObserver(this.mObserver);
      this.mPopulatePending = false;
      boolean bool = this.mFirstLayout;
      this.mFirstLayout = true;
      this.mExpectedAdapterCount = this.mAdapter.getCount();
      if (this.mRestoredCurItem >= 0) {
        this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
        setCurrentItemInternal(this.mRestoredCurItem, false, true);
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
      } else if (!bool) {
        populate();
      } else {
        requestLayout();
      } 
    } 
    List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
    if (list != null && !list.isEmpty()) {
      int j = this.mAdapterChangeListeners.size();
      for (int i = b; i < j; i++)
        ((OnAdapterChangeListener)this.mAdapterChangeListeners.get(i)).onAdapterChanged(this, pagerAdapter, paramPagerAdapter); 
    } 
  }
  
  public void setCurrentItem(int paramInt) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, this.mFirstLayout ^ true, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2) {
    int i;
    PagerAdapter pagerAdapter = this.mAdapter;
    boolean bool = false;
    if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (!paramBoolean2 && this.mCurItem == paramInt1 && this.mItems.size() != 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (paramInt1 < 0) {
      i = 0;
    } else {
      i = paramInt1;
      if (paramInt1 >= this.mAdapter.getCount())
        i = this.mAdapter.getCount() - 1; 
    } 
    paramInt1 = this.mOffscreenPageLimit;
    int j = this.mCurItem;
    if (i > j + paramInt1 || i < j - paramInt1)
      for (paramInt1 = 0; paramInt1 < this.mItems.size(); paramInt1++)
        ((ItemInfo)this.mItems.get(paramInt1)).scrolling = true;  
    paramBoolean2 = bool;
    if (this.mCurItem != i)
      paramBoolean2 = true; 
    if (this.mFirstLayout) {
      this.mCurItem = i;
      if (paramBoolean2)
        dispatchOnPageSelected(i); 
      requestLayout();
      return;
    } 
    populate(i);
    scrollToItem(i, paramBoolean1, paramInt2, paramBoolean2);
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return onPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt) {
    int i = paramInt;
    if (paramInt < 1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Requested offscreen page limit ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" too small; defaulting to ");
      stringBuilder.append(1);
      Log.w("ViewPager", stringBuilder.toString());
      i = 1;
    } 
    if (i != this.mOffscreenPageLimit) {
      this.mOffscreenPageLimit = i;
      populate();
    } 
  }
  
  @Deprecated
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageMargin(int paramInt) {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(int paramInt) {
    setPageMarginDrawable(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setPageMarginDrawable(Drawable paramDrawable) {
    boolean bool;
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState(); 
    if (paramDrawable == null) {
      bool = true;
    } else {
      bool = false;
    } 
    setWillNotDraw(bool);
    invalidate();
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer) {
    setPageTransformer(paramBoolean, paramPageTransformer, 2);
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer, int paramInt) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    byte b = 1;
    if (paramPageTransformer != null) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (this.mPageTransformer != null) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if (bool2 != bool3) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mPageTransformer = paramPageTransformer;
    setChildrenDrawingOrderEnabled(bool2);
    if (bool2) {
      if (paramBoolean)
        b = 2; 
      this.mDrawingOrder = b;
      this.mPageTransformerLayerType = paramInt;
    } else {
      this.mDrawingOrder = 0;
    } 
    if (bool1)
      populate(); 
  }
  
  void setScrollState(int paramInt) {
    if (this.mScrollState == paramInt)
      return; 
    this.mScrollState = paramInt;
    if (this.mPageTransformer != null) {
      boolean bool;
      if (paramInt != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      enableLayers(bool);
    } 
    dispatchOnScrollStateChanged(paramInt);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2) {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    if (getChildCount() == 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    Scroller scroller = this.mScroller;
    if (scroller != null && !scroller.isFinished()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      if (this.mIsScrollStarted) {
        i = this.mScroller.getCurrX();
      } else {
        i = this.mScroller.getStartX();
      } 
      this.mScroller.abortAnimation();
      setScrollingCacheEnabled(false);
    } else {
      i = getScrollX();
    } 
    int j = getScrollY();
    int k = paramInt1 - i;
    paramInt2 -= j;
    if (k == 0 && paramInt2 == 0) {
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
    } 
    setScrollingCacheEnabled(true);
    setScrollState(2);
    paramInt1 = getClientWidth();
    int m = paramInt1 / 2;
    float f2 = Math.abs(k);
    float f1 = paramInt1;
    float f3 = Math.min(1.0F, f2 * 1.0F / f1);
    f2 = m;
    f3 = distanceInfluenceForSnapDuration(f3);
    paramInt1 = Math.abs(paramInt3);
    if (paramInt1 > 0) {
      paramInt1 = Math.round(Math.abs((f2 + f3 * f2) / paramInt1) * 1000.0F) * 4;
    } else {
      f2 = this.mAdapter.getPageWidth(this.mCurItem);
      paramInt1 = (int)((Math.abs(k) / (f1 * f2 + this.mPageMargin) + 1.0F) * 100.0F);
    } 
    paramInt1 = Math.min(paramInt1, 600);
    this.mIsScrollStarted = false;
    this.mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mMarginDrawable);
  }
  
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public static @interface DecorView {}
  
  static class ItemInfo {
    Object object;
    
    float offset;
    
    int position;
    
    boolean scrolling;
    
    float widthFactor;
  }
  
  public static class LayoutParams extends ViewGroup.LayoutParams {
    int childIndex;
    
    public int gravity;
    
    public boolean isDecor;
    
    boolean needsMeasure;
    
    int position;
    
    float widthFactor = 0.0F;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = typedArray.getInteger(0, 48);
      typedArray.recycle();
    }
  }
  
  class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
    private boolean canScroll() {
      return (ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ViewPager.class.getName());
      param1AccessibilityEvent.setScrollable(canScroll());
      if (param1AccessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
        param1AccessibilityEvent.setItemCount(ViewPager.this.mAdapter.getCount());
        param1AccessibilityEvent.setFromIndex(ViewPager.this.mCurItem);
        param1AccessibilityEvent.setToIndex(ViewPager.this.mCurItem);
      } 
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      param1AccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      param1AccessibilityNodeInfoCompat.setScrollable(canScroll());
      if (ViewPager.this.canScrollHorizontally(1))
        param1AccessibilityNodeInfoCompat.addAction(4096); 
      if (ViewPager.this.canScrollHorizontally(-1))
        param1AccessibilityNodeInfoCompat.addAction(8192); 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      if (super.performAccessibilityAction(param1View, param1Int, param1Bundle))
        return true; 
      if (param1Int != 4096) {
        if (param1Int != 8192)
          return false; 
        if (ViewPager.this.canScrollHorizontally(-1)) {
          ViewPager viewPager = ViewPager.this;
          viewPager.setCurrentItem(viewPager.mCurItem - 1);
          return true;
        } 
        return false;
      } 
      if (ViewPager.this.canScrollHorizontally(1)) {
        ViewPager viewPager = ViewPager.this;
        viewPager.setCurrentItem(viewPager.mCurItem + 1);
        return true;
      } 
      return false;
    }
  }
  
  public static interface OnAdapterChangeListener {
    void onAdapterChanged(ViewPager param1ViewPager, PagerAdapter param1PagerAdapter1, PagerAdapter param1PagerAdapter2);
  }
  
  public static interface OnPageChangeListener {
    void onPageScrollStateChanged(int param1Int);
    
    void onPageScrolled(int param1Int1, float param1Float, int param1Int2);
    
    void onPageSelected(int param1Int);
  }
  
  public static interface PageTransformer {
    void transformPage(View param1View, float param1Float);
  }
  
  private class PagerObserver extends DataSetObserver {
    public void onChanged() {
      ViewPager.this.dataSetChanged();
    }
    
    public void onInvalidated() {
      ViewPager.this.dataSetChanged();
    }
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel) {
          return new ViewPager.SavedState(param2Parcel, null);
        }
        
        public ViewPager.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new ViewPager.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public ViewPager.SavedState[] newArray(int param2Int) {
          return new ViewPager.SavedState[param2Int];
        }
      };
    
    Parcelable adapterState;
    
    ClassLoader loader;
    
    int position;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      ClassLoader classLoader = param1ClassLoader;
      if (param1ClassLoader == null)
        classLoader = getClass().getClassLoader(); 
      this.position = param1Parcel.readInt();
      this.adapterState = param1Parcel.readParcelable(classLoader);
      this.loader = classLoader;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("FragmentPager.SavedState{");
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append(" position=");
      stringBuilder.append(this.position);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.position);
      param1Parcel.writeParcelable(this.adapterState, param1Int);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel) {
      return new ViewPager.SavedState(param1Parcel, null);
    }
    
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new ViewPager.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public ViewPager.SavedState[] newArray(int param1Int) {
      return new ViewPager.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnPageChangeListener implements OnPageChangeListener {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
  
  static class ViewPositionComparator implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      ViewPager.LayoutParams layoutParams1 = (ViewPager.LayoutParams)param1View1.getLayoutParams();
      ViewPager.LayoutParams layoutParams2 = (ViewPager.LayoutParams)param1View2.getLayoutParams();
      return (layoutParams1.isDecor != layoutParams2.isDecor) ? (layoutParams1.isDecor ? 1 : -1) : (layoutParams1.position - layoutParams2.position);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\viewpager\widget\ViewPager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
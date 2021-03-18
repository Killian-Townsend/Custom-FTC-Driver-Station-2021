package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.collection.ArraySet;
import androidx.core.util.DebugUtils;
import androidx.core.util.LogWriter;
import androidx.core.view.OneShotPreDrawListener;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
  static final int ANIM_DUR = 220;
  
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  
  static boolean DEBUG = false;
  
  static final Interpolator DECELERATE_CUBIC;
  
  static final Interpolator DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5F);
  
  static final String TAG = "FragmentManager";
  
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  
  static final String TARGET_STATE_TAG = "android:target_state";
  
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  
  static final String VIEW_STATE_TAG = "android:view_state";
  
  final HashMap<String, Fragment> mActive = new HashMap<String, Fragment>();
  
  final ArrayList<Fragment> mAdded = new ArrayList<Fragment>();
  
  ArrayList<Integer> mAvailBackStackIndices;
  
  ArrayList<BackStackRecord> mBackStack;
  
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  
  ArrayList<BackStackRecord> mBackStackIndices;
  
  FragmentContainer mContainer;
  
  ArrayList<Fragment> mCreatedMenus;
  
  int mCurState = 0;
  
  boolean mDestroyed;
  
  Runnable mExecCommit = new Runnable() {
      public void run() {
        FragmentManagerImpl.this.execPendingActions();
      }
    };
  
  boolean mExecutingActions;
  
  boolean mHavePendingDeferredStart;
  
  FragmentHostCallback mHost;
  
  private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder>();
  
  boolean mNeedMenuInvalidate;
  
  int mNextFragmentIndex = 0;
  
  private FragmentManagerViewModel mNonConfig;
  
  private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {
      public void handleOnBackPressed() {
        FragmentManagerImpl.this.handleOnBackPressed();
      }
    };
  
  private OnBackPressedDispatcher mOnBackPressedDispatcher;
  
  Fragment mParent;
  
  ArrayList<OpGenerator> mPendingActions;
  
  ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  
  Fragment mPrimaryNav;
  
  SparseArray<Parcelable> mStateArray = null;
  
  Bundle mStateBundle = null;
  
  boolean mStateSaved;
  
  boolean mStopped;
  
  ArrayList<Fragment> mTmpAddedFragments;
  
  ArrayList<Boolean> mTmpIsPop;
  
  ArrayList<BackStackRecord> mTmpRecords;
  
  static {
    DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5F);
  }
  
  private void addAddedFragments(ArraySet<Fragment> paramArraySet) {
    int i = this.mCurState;
    if (i < 1)
      return; 
    int j = Math.min(i, 3);
    int k = this.mAdded.size();
    for (i = 0; i < k; i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment.mState < j) {
        moveToState(fragment, j, fragment.getNextAnim(), fragment.getNextTransition(), false);
        if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded)
          paramArraySet.add(fragment); 
      } 
    } 
  }
  
  private void animateRemoveFragment(final Fragment fragment, AnimationOrAnimator paramAnimationOrAnimator, int paramInt) {
    EndViewTransitionAnimation endViewTransitionAnimation;
    final View viewToAnimate = fragment.mView;
    final ViewGroup container = fragment.mContainer;
    viewGroup.startViewTransition(view);
    fragment.setStateAfterAnimating(paramInt);
    if (paramAnimationOrAnimator.animation != null) {
      endViewTransitionAnimation = new EndViewTransitionAnimation(paramAnimationOrAnimator.animation, viewGroup, view);
      fragment.setAnimatingAway(fragment.mView);
      endViewTransitionAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation param1Animation) {
              container.post(new Runnable() {
                    public void run() {
                      if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                      } 
                    }
                  });
            }
            
            public void onAnimationRepeat(Animation param1Animation) {}
            
            public void onAnimationStart(Animation param1Animation) {}
          });
      fragment.mView.startAnimation((Animation)endViewTransitionAnimation);
      return;
    } 
    Animator animator = ((AnimationOrAnimator)endViewTransitionAnimation).animator;
    fragment.setAnimator(((AnimationOrAnimator)endViewTransitionAnimation).animator);
    animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            container.endViewTransition(viewToAnimate);
            param1Animator = fragment.getAnimator();
            fragment.setAnimator(null);
            if (param1Animator != null && container.indexOfChild(viewToAnimate) < 0) {
              FragmentManagerImpl fragmentManagerImpl = FragmentManagerImpl.this;
              Fragment fragment = fragment;
              fragmentManagerImpl.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
            } 
          }
        });
    animator.setTarget(fragment.mView);
    animator.start();
  }
  
  private void burpActive() {
    this.mActive.values().removeAll(Collections.singleton(null));
  }
  
  private void checkStateLoss() {
    if (!isStateSaved())
      return; 
    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
  }
  
  private void cleanupExec() {
    this.mExecutingActions = false;
    this.mTmpIsPop.clear();
    this.mTmpRecords.clear();
  }
  
  private void dispatchParentPrimaryNavigationFragmentChanged(Fragment paramFragment) {
    if (paramFragment != null && this.mActive.get(paramFragment.mWho) == paramFragment)
      paramFragment.performPrimaryNavigationFragmentChanged(); 
  }
  
  private void dispatchStateChange(int paramInt) {
    try {
      this.mExecutingActions = true;
      moveToState(paramInt, false);
      this.mExecutingActions = false;
      return;
    } finally {
      this.mExecutingActions = false;
    } 
  }
  
  private void endAnimatingAwayFragments() {
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null) {
        if (fragment.getAnimatingAway() != null) {
          int i = fragment.getStateAfterAnimating();
          View view = fragment.getAnimatingAway();
          Animation animation = view.getAnimation();
          if (animation != null) {
            animation.cancel();
            view.clearAnimation();
          } 
          fragment.setAnimatingAway(null);
          moveToState(fragment, i, 0, 0, false);
          continue;
        } 
        if (fragment.getAnimator() != null)
          fragment.getAnimator().end(); 
      } 
    } 
  }
  
  private void ensureExecReady(boolean paramBoolean) {
    if (!this.mExecutingActions) {
      if (this.mHost != null) {
        if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
          if (!paramBoolean)
            checkStateLoss(); 
          if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList<BackStackRecord>();
            this.mTmpIsPop = new ArrayList<Boolean>();
          } 
          this.mExecutingActions = true;
          try {
            executePostponedTransaction((ArrayList<BackStackRecord>)null, (ArrayList<Boolean>)null);
            return;
          } finally {
            this.mExecutingActions = false;
          } 
        } 
        throw new IllegalStateException("Must be called from main thread of fragment host");
      } 
      throw new IllegalStateException("Fragment host has been destroyed");
    } 
    throw new IllegalStateException("FragmentManager is already executing transactions");
  }
  
  private static void executeOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
      boolean bool1 = ((Boolean)paramArrayList1.get(paramInt1)).booleanValue();
      boolean bool = true;
      if (bool1) {
        backStackRecord.bumpBackStackNesting(-1);
        if (paramInt1 != paramInt2 - 1)
          bool = false; 
        backStackRecord.executePopOps(bool);
      } else {
        backStackRecord.bumpBackStackNesting(1);
        backStackRecord.executeOps();
      } 
      paramInt1++;
    } 
  }
  
  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    int i = paramInt1;
    boolean bool1 = ((BackStackRecord)paramArrayList.get(i)).mReorderingAllowed;
    ArrayList<Fragment> arrayList = this.mTmpAddedFragments;
    if (arrayList == null) {
      this.mTmpAddedFragments = new ArrayList<Fragment>();
    } else {
      arrayList.clear();
    } 
    this.mTmpAddedFragments.addAll(this.mAdded);
    Fragment fragment = getPrimaryNavigationFragment();
    boolean bool = false;
    int j;
    for (j = i; j < paramInt2; j++) {
      BackStackRecord backStackRecord = paramArrayList.get(j);
      if (!((Boolean)paramArrayList1.get(j)).booleanValue()) {
        fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
      } else {
        fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
      } 
      if (bool || backStackRecord.mAddToBackStack) {
        bool = true;
      } else {
        bool = false;
      } 
    } 
    this.mTmpAddedFragments.clear();
    if (!bool1)
      FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, paramInt2, false); 
    executeOps(paramArrayList, paramArrayList1, paramInt1, paramInt2);
    if (bool1) {
      ArraySet<Fragment> arraySet = new ArraySet();
      addAddedFragments(arraySet);
      j = postponePostponableTransactions(paramArrayList, paramArrayList1, paramInt1, paramInt2, arraySet);
      makeRemovedFragmentsInvisible(arraySet);
    } else {
      j = paramInt2;
    } 
    int k = i;
    if (j != i) {
      k = i;
      if (bool1) {
        FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, j, true);
        moveToState(this.mCurState, true);
        k = i;
      } 
    } 
    while (k < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(k);
      if (((Boolean)paramArrayList1.get(k)).booleanValue() && backStackRecord.mIndex >= 0) {
        freeBackStackIndex(backStackRecord.mIndex);
        backStackRecord.mIndex = -1;
      } 
      backStackRecord.runOnCommitRunnables();
      k++;
    } 
    if (bool)
      reportBackStackChanged(); 
  }
  
  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   4: astore #7
    //   6: aload #7
    //   8: ifnonnull -> 16
    //   11: iconst_0
    //   12: istore_3
    //   13: goto -> 22
    //   16: aload #7
    //   18: invokevirtual size : ()I
    //   21: istore_3
    //   22: iconst_0
    //   23: istore #4
    //   25: iload_3
    //   26: istore #6
    //   28: iload #4
    //   30: iload #6
    //   32: if_icmpge -> 252
    //   35: aload_0
    //   36: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   39: iload #4
    //   41: invokevirtual get : (I)Ljava/lang/Object;
    //   44: checkcast androidx/fragment/app/FragmentManagerImpl$StartEnterTransitionListener
    //   47: astore #7
    //   49: aload_1
    //   50: ifnull -> 119
    //   53: aload #7
    //   55: getfield mIsBack : Z
    //   58: ifne -> 119
    //   61: aload_1
    //   62: aload #7
    //   64: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   67: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   70: istore_3
    //   71: iload_3
    //   72: iconst_m1
    //   73: if_icmpeq -> 119
    //   76: aload_2
    //   77: iload_3
    //   78: invokevirtual get : (I)Ljava/lang/Object;
    //   81: checkcast java/lang/Boolean
    //   84: invokevirtual booleanValue : ()Z
    //   87: ifeq -> 119
    //   90: aload_0
    //   91: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   94: iload #4
    //   96: invokevirtual remove : (I)Ljava/lang/Object;
    //   99: pop
    //   100: iload #4
    //   102: iconst_1
    //   103: isub
    //   104: istore #5
    //   106: iload #6
    //   108: iconst_1
    //   109: isub
    //   110: istore_3
    //   111: aload #7
    //   113: invokevirtual cancelTransaction : ()V
    //   116: goto -> 240
    //   119: aload #7
    //   121: invokevirtual isReady : ()Z
    //   124: ifne -> 162
    //   127: iload #6
    //   129: istore_3
    //   130: iload #4
    //   132: istore #5
    //   134: aload_1
    //   135: ifnull -> 240
    //   138: iload #6
    //   140: istore_3
    //   141: iload #4
    //   143: istore #5
    //   145: aload #7
    //   147: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   150: aload_1
    //   151: iconst_0
    //   152: aload_1
    //   153: invokevirtual size : ()I
    //   156: invokevirtual interactsWith : (Ljava/util/ArrayList;II)Z
    //   159: ifeq -> 240
    //   162: aload_0
    //   163: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   166: iload #4
    //   168: invokevirtual remove : (I)Ljava/lang/Object;
    //   171: pop
    //   172: iload #4
    //   174: iconst_1
    //   175: isub
    //   176: istore #5
    //   178: iload #6
    //   180: iconst_1
    //   181: isub
    //   182: istore_3
    //   183: aload_1
    //   184: ifnull -> 235
    //   187: aload #7
    //   189: getfield mIsBack : Z
    //   192: ifne -> 235
    //   195: aload_1
    //   196: aload #7
    //   198: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   201: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   204: istore #4
    //   206: iload #4
    //   208: iconst_m1
    //   209: if_icmpeq -> 235
    //   212: aload_2
    //   213: iload #4
    //   215: invokevirtual get : (I)Ljava/lang/Object;
    //   218: checkcast java/lang/Boolean
    //   221: invokevirtual booleanValue : ()Z
    //   224: ifeq -> 235
    //   227: aload #7
    //   229: invokevirtual cancelTransaction : ()V
    //   232: goto -> 240
    //   235: aload #7
    //   237: invokevirtual completeTransaction : ()V
    //   240: iload #5
    //   242: iconst_1
    //   243: iadd
    //   244: istore #4
    //   246: iload_3
    //   247: istore #6
    //   249: goto -> 28
    //   252: return
  }
  
  private Fragment findFragmentUnder(Fragment paramFragment) {
    ViewGroup viewGroup = paramFragment.mContainer;
    View view = paramFragment.mView;
    if (viewGroup != null) {
      if (view == null)
        return null; 
      for (int i = this.mAdded.indexOf(paramFragment) - 1; i >= 0; i--) {
        paramFragment = this.mAdded.get(i);
        if (paramFragment.mContainer == viewGroup && paramFragment.mView != null)
          return paramFragment; 
      } 
    } 
    return null;
  }
  
  private void forcePostponedTransactions() {
    if (this.mPostponedTransactions != null)
      while (!this.mPostponedTransactions.isEmpty())
        ((StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();  
  }
  
  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mPendingActions : Ljava/util/ArrayList;
    //   6: astore #6
    //   8: iconst_0
    //   9: istore_3
    //   10: aload #6
    //   12: ifnull -> 102
    //   15: aload_0
    //   16: getfield mPendingActions : Ljava/util/ArrayList;
    //   19: invokevirtual size : ()I
    //   22: ifne -> 28
    //   25: goto -> 102
    //   28: aload_0
    //   29: getfield mPendingActions : Ljava/util/ArrayList;
    //   32: invokevirtual size : ()I
    //   35: istore #4
    //   37: iconst_0
    //   38: istore #5
    //   40: iload_3
    //   41: iload #4
    //   43: if_icmpge -> 76
    //   46: iload #5
    //   48: aload_0
    //   49: getfield mPendingActions : Ljava/util/ArrayList;
    //   52: iload_3
    //   53: invokevirtual get : (I)Ljava/lang/Object;
    //   56: checkcast androidx/fragment/app/FragmentManagerImpl$OpGenerator
    //   59: aload_1
    //   60: aload_2
    //   61: invokeinterface generateOps : (Ljava/util/ArrayList;Ljava/util/ArrayList;)Z
    //   66: ior
    //   67: istore #5
    //   69: iload_3
    //   70: iconst_1
    //   71: iadd
    //   72: istore_3
    //   73: goto -> 40
    //   76: aload_0
    //   77: getfield mPendingActions : Ljava/util/ArrayList;
    //   80: invokevirtual clear : ()V
    //   83: aload_0
    //   84: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   87: invokevirtual getHandler : ()Landroid/os/Handler;
    //   90: aload_0
    //   91: getfield mExecCommit : Ljava/lang/Runnable;
    //   94: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   97: aload_0
    //   98: monitorexit
    //   99: iload #5
    //   101: ireturn
    //   102: aload_0
    //   103: monitorexit
    //   104: iconst_0
    //   105: ireturn
    //   106: astore_1
    //   107: aload_0
    //   108: monitorexit
    //   109: aload_1
    //   110: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	106	finally
    //   15	25	106	finally
    //   28	37	106	finally
    //   46	69	106	finally
    //   76	99	106	finally
    //   102	104	106	finally
    //   107	109	106	finally
  }
  
  private boolean isMenuAvailable(Fragment paramFragment) {
    return ((paramFragment.mHasMenu && paramFragment.mMenuVisible) || paramFragment.mChildFragmentManager.checkForMenus());
  }
  
  static AnimationOrAnimator makeFadeAnimation(float paramFloat1, float paramFloat2) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    return new AnimationOrAnimator((Animation)alphaAnimation);
  }
  
  static AnimationOrAnimator makeOpenCloseAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    AnimationSet animationSet = new AnimationSet(false);
    ScaleAnimation scaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setInterpolator(DECELERATE_QUINT);
    scaleAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)scaleAnimation);
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat3, paramFloat4);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)alphaAnimation);
    return new AnimationOrAnimator((Animation)animationSet);
  }
  
  private void makeRemovedFragmentsInvisible(ArraySet<Fragment> paramArraySet) {
    int j = paramArraySet.size();
    for (int i = 0; i < j; i++) {
      Fragment fragment = (Fragment)paramArraySet.valueAt(i);
      if (!fragment.mAdded) {
        View view = fragment.requireView();
        fragment.mPostponedAlpha = view.getAlpha();
        view.setAlpha(0.0F);
      } 
    } 
  }
  
  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2) {
    execPendingActions();
    ensureExecReady(true);
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null && paramInt1 < 0 && paramString == null && fragment.getChildFragmentManager().popBackStackImmediate())
      return true; 
    boolean bool = popBackStackState(this.mTmpRecords, this.mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, ArraySet<Fragment> paramArraySet) {
    int i = paramInt2 - 1;
    int j;
    for (j = paramInt2; i >= paramInt1; j = k) {
      boolean bool;
      BackStackRecord backStackRecord = paramArrayList.get(i);
      boolean bool1 = ((Boolean)paramArrayList1.get(i)).booleanValue();
      if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(paramArrayList, i + 1, paramInt2)) {
        bool = true;
      } else {
        bool = false;
      } 
      int k = j;
      if (bool) {
        if (this.mPostponedTransactions == null)
          this.mPostponedTransactions = new ArrayList<StartEnterTransitionListener>(); 
        StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bool1);
        this.mPostponedTransactions.add(startEnterTransitionListener);
        backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
        if (bool1) {
          backStackRecord.executeOps();
        } else {
          backStackRecord.executePopOps(false);
        } 
        k = j - 1;
        if (i != k) {
          paramArrayList.remove(i);
          paramArrayList.add(k, backStackRecord);
        } 
        addAddedFragments(paramArraySet);
      } 
      i--;
    } 
    return j;
  }
  
  private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    if (paramArrayList != null) {
      if (paramArrayList.isEmpty())
        return; 
      if (paramArrayList1 != null && paramArrayList.size() == paramArrayList1.size()) {
        executePostponedTransaction(paramArrayList, paramArrayList1);
        int k = paramArrayList.size();
        int i = 0;
        int j;
        for (j = 0; i < k; j = m) {
          int n = i;
          int m = j;
          if (!((BackStackRecord)paramArrayList.get(i)).mReorderingAllowed) {
            if (j != i)
              executeOpsTogether(paramArrayList, paramArrayList1, j, i); 
            j = i + 1;
            m = j;
            if (((Boolean)paramArrayList1.get(i)).booleanValue())
              while (true) {
                m = j;
                if (j < k) {
                  m = j;
                  if (((Boolean)paramArrayList1.get(j)).booleanValue()) {
                    m = j;
                    if (!((BackStackRecord)paramArrayList.get(j)).mReorderingAllowed) {
                      j++;
                      continue;
                    } 
                  } 
                } 
                break;
              }  
            executeOpsTogether(paramArrayList, paramArrayList1, i, m);
            n = m - 1;
          } 
          i = n + 1;
        } 
        if (j != k)
          executeOpsTogether(paramArrayList, paramArrayList1, j, k); 
        return;
      } 
      throw new IllegalStateException("Internal error with the back stack records");
    } 
  }
  
  public static int reverseTransit(int paramInt) {
    char c = ' ';
    if (paramInt != 4097) {
      if (paramInt != 4099)
        return (paramInt != 8194) ? 0 : 4097; 
      c = 'ဃ';
    } 
    return c;
  }
  
  private void throwException(RuntimeException paramRuntimeException) {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    PrintWriter printWriter = new PrintWriter((Writer)new LogWriter("FragmentManager"));
    FragmentHostCallback fragmentHostCallback = this.mHost;
    if (fragmentHostCallback != null) {
      try {
        fragmentHostCallback.onDump("  ", (FileDescriptor)null, printWriter, new String[0]);
      } catch (Exception exception) {
        Log.e("FragmentManager", "Failed dumping state", exception);
      } 
    } else {
      try {
        dump("  ", (FileDescriptor)null, (PrintWriter)exception, new String[0]);
      } catch (Exception exception1) {
        Log.e("FragmentManager", "Failed dumping state", exception1);
      } 
    } 
    throw paramRuntimeException;
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean) {
    return (paramInt != 4097) ? ((paramInt != 4099) ? ((paramInt != 8194) ? -1 : (paramBoolean ? 3 : 4)) : (paramBoolean ? 5 : 6)) : (paramBoolean ? 1 : 2);
  }
  
  private void updateOnBackPressedCallbackEnabled() {
    ArrayList<OpGenerator> arrayList = this.mPendingActions;
    boolean bool = true;
    if (arrayList != null && !arrayList.isEmpty()) {
      this.mOnBackPressedCallback.setEnabled(true);
      return;
    } 
    OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
    if (getBackStackEntryCount() <= 0 || !isPrimaryNavigation(this.mParent))
      bool = false; 
    onBackPressedCallback.setEnabled(bool);
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord) {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList<BackStackRecord>(); 
    this.mBackStack.add(paramBackStackRecord);
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("add: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    makeActive(paramFragment);
    if (!paramFragment.mDetached)
      if (!this.mAdded.contains(paramFragment)) {
        synchronized (this.mAdded) {
          this.mAdded.add(paramFragment);
          paramFragment.mAdded = true;
          paramFragment.mRemoving = false;
          if (paramFragment.mView == null)
            paramFragment.mHiddenChanged = false; 
          if (isMenuAvailable(paramFragment))
            this.mNeedMenuInvalidate = true; 
          if (paramBoolean) {
            moveToState(paramFragment);
            return;
          } 
        } 
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment already added: ");
        stringBuilder.append(paramFragment);
        throw new IllegalStateException(stringBuilder.toString());
      }  
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList<FragmentManager.OnBackStackChangedListener>(); 
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  void addRetainedFragment(Fragment paramFragment) {
    if (isStateSaved()) {
      if (DEBUG)
        Log.v("FragmentManager", "Ignoring addRetainedFragment as the state is already saved"); 
      return;
    } 
    if (this.mNonConfig.addRetainedFragment(paramFragment) && DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Updating retained Fragments: Added ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnull -> 111
    //   9: aload_0
    //   10: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   13: invokevirtual size : ()I
    //   16: ifgt -> 22
    //   19: goto -> 111
    //   22: aload_0
    //   23: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   26: aload_0
    //   27: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   30: invokevirtual size : ()I
    //   33: iconst_1
    //   34: isub
    //   35: invokevirtual remove : (I)Ljava/lang/Object;
    //   38: checkcast java/lang/Integer
    //   41: invokevirtual intValue : ()I
    //   44: istore_2
    //   45: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   48: ifeq -> 97
    //   51: new java/lang/StringBuilder
    //   54: dup
    //   55: invokespecial <init> : ()V
    //   58: astore_3
    //   59: aload_3
    //   60: ldc_w 'Adding back stack index '
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload_3
    //   68: iload_2
    //   69: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_3
    //   74: ldc_w ' with '
    //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_3
    //   82: aload_1
    //   83: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: ldc 'FragmentManager'
    //   89: aload_3
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   101: iload_2
    //   102: aload_1
    //   103: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   106: pop
    //   107: aload_0
    //   108: monitorexit
    //   109: iload_2
    //   110: ireturn
    //   111: aload_0
    //   112: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   115: ifnonnull -> 129
    //   118: aload_0
    //   119: new java/util/ArrayList
    //   122: dup
    //   123: invokespecial <init> : ()V
    //   126: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   129: aload_0
    //   130: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   133: invokevirtual size : ()I
    //   136: istore_2
    //   137: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   140: ifeq -> 189
    //   143: new java/lang/StringBuilder
    //   146: dup
    //   147: invokespecial <init> : ()V
    //   150: astore_3
    //   151: aload_3
    //   152: ldc_w 'Setting back stack index '
    //   155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   158: pop
    //   159: aload_3
    //   160: iload_2
    //   161: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: aload_3
    //   166: ldc_w ' to '
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: aload_3
    //   174: aload_1
    //   175: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: ldc 'FragmentManager'
    //   181: aload_3
    //   182: invokevirtual toString : ()Ljava/lang/String;
    //   185: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   188: pop
    //   189: aload_0
    //   190: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   193: aload_1
    //   194: invokevirtual add : (Ljava/lang/Object;)Z
    //   197: pop
    //   198: aload_0
    //   199: monitorexit
    //   200: iload_2
    //   201: ireturn
    //   202: astore_1
    //   203: aload_0
    //   204: monitorexit
    //   205: aload_1
    //   206: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	202	finally
    //   22	97	202	finally
    //   97	109	202	finally
    //   111	129	202	finally
    //   129	189	202	finally
    //   189	200	202	finally
    //   203	205	202	finally
  }
  
  public void attachController(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment) {
    if (this.mHost == null) {
      this.mHost = paramFragmentHostCallback;
      this.mContainer = paramFragmentContainer;
      this.mParent = paramFragment;
      if (paramFragment != null)
        updateOnBackPressedCallbackEnabled(); 
      if (paramFragmentHostCallback instanceof OnBackPressedDispatcherOwner) {
        Fragment fragment;
        OnBackPressedDispatcherOwner onBackPressedDispatcherOwner = (OnBackPressedDispatcherOwner)paramFragmentHostCallback;
        this.mOnBackPressedDispatcher = onBackPressedDispatcherOwner.getOnBackPressedDispatcher();
        if (paramFragment != null)
          fragment = paramFragment; 
        this.mOnBackPressedDispatcher.addCallback(fragment, this.mOnBackPressedCallback);
      } 
      if (paramFragment != null) {
        this.mNonConfig = paramFragment.mFragmentManager.getChildNonConfig(paramFragment);
        return;
      } 
      if (paramFragmentHostCallback instanceof ViewModelStoreOwner) {
        this.mNonConfig = FragmentManagerViewModel.getInstance(((ViewModelStoreOwner)paramFragmentHostCallback).getViewModelStore());
        return;
      } 
      this.mNonConfig = new FragmentManagerViewModel(false);
      return;
    } 
    throw new IllegalStateException("Already attached");
  }
  
  public void attachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("attach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mDetached) {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded)
        if (!this.mAdded.contains(paramFragment)) {
          if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add from attach: ");
            stringBuilder.append(paramFragment);
            Log.v("FragmentManager", stringBuilder.toString());
          } 
          synchronized (this.mAdded) {
            this.mAdded.add(paramFragment);
            paramFragment.mAdded = true;
            if (isMenuAvailable(paramFragment)) {
              this.mNeedMenuInvalidate = true;
              return;
            } 
          } 
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Fragment already added: ");
          stringBuilder.append(paramFragment);
          throw new IllegalStateException(stringBuilder.toString());
        }  
    } 
  }
  
  public FragmentTransaction beginTransaction() {
    return new BackStackRecord(this);
  }
  
  boolean checkForMenus() {
    Iterator<Fragment> iterator = this.mActive.values().iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      Fragment fragment = iterator.next();
      boolean bool1 = bool;
      if (fragment != null)
        bool1 = isMenuAvailable(fragment); 
      bool = bool1;
      if (bool1)
        return true; 
    } 
    return false;
  }
  
  void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    if (paramBoolean1) {
      paramBackStackRecord.executePopOps(paramBoolean3);
    } else {
      paramBackStackRecord.executeOps();
    } 
    ArrayList<BackStackRecord> arrayList = new ArrayList(1);
    ArrayList<Boolean> arrayList1 = new ArrayList(1);
    arrayList.add(paramBackStackRecord);
    arrayList1.add(Boolean.valueOf(paramBoolean1));
    if (paramBoolean2)
      FragmentTransition.startTransitions(this, arrayList, arrayList1, 0, 1, true); 
    if (paramBoolean3)
      moveToState(this.mCurState, true); 
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && paramBackStackRecord.interactsWith(fragment.mContainerId)) {
        if (fragment.mPostponedAlpha > 0.0F)
          fragment.mView.setAlpha(fragment.mPostponedAlpha); 
        if (paramBoolean3) {
          fragment.mPostponedAlpha = 0.0F;
          continue;
        } 
        fragment.mPostponedAlpha = -1.0F;
        fragment.mIsNewlyAdded = false;
      } 
    } 
  }
  
  void completeShowHideFragment(final Fragment fragment) {
    if (fragment.mView != null) {
      AnimationOrAnimator animationOrAnimator = loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
      if (animationOrAnimator != null && animationOrAnimator.animator != null) {
        animationOrAnimator.animator.setTarget(fragment.mView);
        if (fragment.mHidden) {
          if (fragment.isHideReplaced()) {
            fragment.setHideReplaced(false);
          } else {
            final ViewGroup container = fragment.mContainer;
            final View animatingView = fragment.mView;
            viewGroup.startViewTransition(view);
            animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator param1Animator) {
                    container.endViewTransition(animatingView);
                    param1Animator.removeListener((Animator.AnimatorListener)this);
                    if (fragment.mView != null && fragment.mHidden)
                      fragment.mView.setVisibility(8); 
                  }
                });
          } 
        } else {
          fragment.mView.setVisibility(0);
        } 
        animationOrAnimator.animator.start();
      } else {
        boolean bool;
        if (animationOrAnimator != null) {
          fragment.mView.startAnimation(animationOrAnimator.animation);
          animationOrAnimator.animation.start();
        } 
        if (fragment.mHidden && !fragment.isHideReplaced()) {
          bool = true;
        } else {
          bool = false;
        } 
        fragment.mView.setVisibility(bool);
        if (fragment.isHideReplaced())
          fragment.setHideReplaced(false); 
      } 
    } 
    if (fragment.mAdded && isMenuAvailable(fragment))
      this.mNeedMenuInvalidate = true; 
    fragment.mHiddenChanged = false;
    fragment.onHiddenChanged(fragment.mHidden);
  }
  
  public void detachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("detach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mDetached) {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded) {
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("remove from detach: ");
          stringBuilder.append(paramFragment);
          Log.v("FragmentManager", stringBuilder.toString());
        } 
        synchronized (this.mAdded) {
          this.mAdded.remove(paramFragment);
          if (isMenuAvailable(paramFragment))
            this.mNeedMenuInvalidate = true; 
          paramFragment.mAdded = false;
          return;
        } 
      } 
    } 
  }
  
  public void dispatchActivityCreated() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(2);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration) {
    for (int i = 0; i < this.mAdded.size(); i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performConfigurationChanged(paramConfiguration); 
    } 
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (int i = 0; i < this.mAdded.size(); i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null && fragment.performContextItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchCreate() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(1);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge Z and I\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  public void dispatchDestroy() {
    this.mDestroyed = true;
    execPendingActions();
    dispatchStateChange(0);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
    if (this.mOnBackPressedDispatcher != null) {
      this.mOnBackPressedCallback.remove();
      this.mOnBackPressedDispatcher = null;
    } 
  }
  
  public void dispatchDestroyView() {
    dispatchStateChange(1);
  }
  
  public void dispatchLowMemory() {
    for (int i = 0; i < this.mAdded.size(); i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performLowMemory(); 
    } 
  }
  
  public void dispatchMultiWindowModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performMultiWindowModeChanged(paramBoolean); 
    } 
  }
  
  void dispatchOnFragmentActivityCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentDestroyed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentDetached(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPaused(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPreAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentPreCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentResumed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentSaveInstanceState(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentStarted(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentStopped(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentViewCreated(Fragment paramFragment, View paramView, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(paramFragment, paramView, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this, paramFragment, paramView, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentViewDestroyed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this, paramFragment); 
    } 
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (int i = 0; i < this.mAdded.size(); i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null && fragment.performOptionsItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu) {
    if (this.mCurState < 1)
      return; 
    for (int i = 0; i < this.mAdded.size(); i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performOptionsMenuClosed(paramMenu); 
    } 
  }
  
  public void dispatchPause() {
    dispatchStateChange(3);
  }
  
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performPictureInPictureModeChanged(paramBoolean); 
    } 
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu) {
    int j = this.mCurState;
    int i = 0;
    if (j < 1)
      return false; 
    boolean bool;
    for (bool = false; i < this.mAdded.size(); bool = bool1) {
      Fragment fragment = this.mAdded.get(i);
      boolean bool1 = bool;
      if (fragment != null) {
        bool1 = bool;
        if (fragment.performPrepareOptionsMenu(paramMenu))
          bool1 = true; 
      } 
      i++;
    } 
    return bool;
  }
  
  void dispatchPrimaryNavigationFragmentChanged() {
    updateOnBackPressedCallbackEnabled();
    dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
  }
  
  public void dispatchResume() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(4);
  }
  
  public void dispatchStart() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(3);
  }
  
  public void dispatchStop() {
    this.mStopped = true;
    dispatchStateChange(2);
  }
  
  void doPendingDeferredStart() {
    if (this.mHavePendingDeferredStart) {
      this.mHavePendingDeferredStart = false;
      startPendingDeferredFragments();
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #8
    //   9: aload #8
    //   11: aload_1
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload #8
    //   18: ldc_w '    '
    //   21: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload #8
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: astore #8
    //   32: aload_0
    //   33: getfield mActive : Ljava/util/HashMap;
    //   36: invokevirtual isEmpty : ()Z
    //   39: ifne -> 138
    //   42: aload_3
    //   43: aload_1
    //   44: invokevirtual print : (Ljava/lang/String;)V
    //   47: aload_3
    //   48: ldc_w 'Active Fragments in '
    //   51: invokevirtual print : (Ljava/lang/String;)V
    //   54: aload_3
    //   55: aload_0
    //   56: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   59: invokestatic toHexString : (I)Ljava/lang/String;
    //   62: invokevirtual print : (Ljava/lang/String;)V
    //   65: aload_3
    //   66: ldc_w ':'
    //   69: invokevirtual println : (Ljava/lang/String;)V
    //   72: aload_0
    //   73: getfield mActive : Ljava/util/HashMap;
    //   76: invokevirtual values : ()Ljava/util/Collection;
    //   79: invokeinterface iterator : ()Ljava/util/Iterator;
    //   84: astore #9
    //   86: aload #9
    //   88: invokeinterface hasNext : ()Z
    //   93: ifeq -> 138
    //   96: aload #9
    //   98: invokeinterface next : ()Ljava/lang/Object;
    //   103: checkcast androidx/fragment/app/Fragment
    //   106: astore #10
    //   108: aload_3
    //   109: aload_1
    //   110: invokevirtual print : (Ljava/lang/String;)V
    //   113: aload_3
    //   114: aload #10
    //   116: invokevirtual println : (Ljava/lang/Object;)V
    //   119: aload #10
    //   121: ifnull -> 86
    //   124: aload #10
    //   126: aload #8
    //   128: aload_2
    //   129: aload_3
    //   130: aload #4
    //   132: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   135: goto -> 86
    //   138: aload_0
    //   139: getfield mAdded : Ljava/util/ArrayList;
    //   142: invokevirtual size : ()I
    //   145: istore #7
    //   147: iconst_0
    //   148: istore #6
    //   150: iload #7
    //   152: ifle -> 232
    //   155: aload_3
    //   156: aload_1
    //   157: invokevirtual print : (Ljava/lang/String;)V
    //   160: aload_3
    //   161: ldc_w 'Added Fragments:'
    //   164: invokevirtual println : (Ljava/lang/String;)V
    //   167: iconst_0
    //   168: istore #5
    //   170: iload #5
    //   172: iload #7
    //   174: if_icmpge -> 232
    //   177: aload_0
    //   178: getfield mAdded : Ljava/util/ArrayList;
    //   181: iload #5
    //   183: invokevirtual get : (I)Ljava/lang/Object;
    //   186: checkcast androidx/fragment/app/Fragment
    //   189: astore_2
    //   190: aload_3
    //   191: aload_1
    //   192: invokevirtual print : (Ljava/lang/String;)V
    //   195: aload_3
    //   196: ldc_w '  #'
    //   199: invokevirtual print : (Ljava/lang/String;)V
    //   202: aload_3
    //   203: iload #5
    //   205: invokevirtual print : (I)V
    //   208: aload_3
    //   209: ldc_w ': '
    //   212: invokevirtual print : (Ljava/lang/String;)V
    //   215: aload_3
    //   216: aload_2
    //   217: invokevirtual toString : ()Ljava/lang/String;
    //   220: invokevirtual println : (Ljava/lang/String;)V
    //   223: iload #5
    //   225: iconst_1
    //   226: iadd
    //   227: istore #5
    //   229: goto -> 170
    //   232: aload_0
    //   233: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   236: astore_2
    //   237: aload_2
    //   238: ifnull -> 329
    //   241: aload_2
    //   242: invokevirtual size : ()I
    //   245: istore #7
    //   247: iload #7
    //   249: ifle -> 329
    //   252: aload_3
    //   253: aload_1
    //   254: invokevirtual print : (Ljava/lang/String;)V
    //   257: aload_3
    //   258: ldc_w 'Fragments Created Menus:'
    //   261: invokevirtual println : (Ljava/lang/String;)V
    //   264: iconst_0
    //   265: istore #5
    //   267: iload #5
    //   269: iload #7
    //   271: if_icmpge -> 329
    //   274: aload_0
    //   275: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   278: iload #5
    //   280: invokevirtual get : (I)Ljava/lang/Object;
    //   283: checkcast androidx/fragment/app/Fragment
    //   286: astore_2
    //   287: aload_3
    //   288: aload_1
    //   289: invokevirtual print : (Ljava/lang/String;)V
    //   292: aload_3
    //   293: ldc_w '  #'
    //   296: invokevirtual print : (Ljava/lang/String;)V
    //   299: aload_3
    //   300: iload #5
    //   302: invokevirtual print : (I)V
    //   305: aload_3
    //   306: ldc_w ': '
    //   309: invokevirtual print : (Ljava/lang/String;)V
    //   312: aload_3
    //   313: aload_2
    //   314: invokevirtual toString : ()Ljava/lang/String;
    //   317: invokevirtual println : (Ljava/lang/String;)V
    //   320: iload #5
    //   322: iconst_1
    //   323: iadd
    //   324: istore #5
    //   326: goto -> 267
    //   329: aload_0
    //   330: getfield mBackStack : Ljava/util/ArrayList;
    //   333: astore_2
    //   334: aload_2
    //   335: ifnull -> 433
    //   338: aload_2
    //   339: invokevirtual size : ()I
    //   342: istore #7
    //   344: iload #7
    //   346: ifle -> 433
    //   349: aload_3
    //   350: aload_1
    //   351: invokevirtual print : (Ljava/lang/String;)V
    //   354: aload_3
    //   355: ldc_w 'Back Stack:'
    //   358: invokevirtual println : (Ljava/lang/String;)V
    //   361: iconst_0
    //   362: istore #5
    //   364: iload #5
    //   366: iload #7
    //   368: if_icmpge -> 433
    //   371: aload_0
    //   372: getfield mBackStack : Ljava/util/ArrayList;
    //   375: iload #5
    //   377: invokevirtual get : (I)Ljava/lang/Object;
    //   380: checkcast androidx/fragment/app/BackStackRecord
    //   383: astore_2
    //   384: aload_3
    //   385: aload_1
    //   386: invokevirtual print : (Ljava/lang/String;)V
    //   389: aload_3
    //   390: ldc_w '  #'
    //   393: invokevirtual print : (Ljava/lang/String;)V
    //   396: aload_3
    //   397: iload #5
    //   399: invokevirtual print : (I)V
    //   402: aload_3
    //   403: ldc_w ': '
    //   406: invokevirtual print : (Ljava/lang/String;)V
    //   409: aload_3
    //   410: aload_2
    //   411: invokevirtual toString : ()Ljava/lang/String;
    //   414: invokevirtual println : (Ljava/lang/String;)V
    //   417: aload_2
    //   418: aload #8
    //   420: aload_3
    //   421: invokevirtual dump : (Ljava/lang/String;Ljava/io/PrintWriter;)V
    //   424: iload #5
    //   426: iconst_1
    //   427: iadd
    //   428: istore #5
    //   430: goto -> 364
    //   433: aload_0
    //   434: monitorenter
    //   435: aload_0
    //   436: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   439: ifnull -> 530
    //   442: aload_0
    //   443: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   446: invokevirtual size : ()I
    //   449: istore #7
    //   451: iload #7
    //   453: ifle -> 530
    //   456: aload_3
    //   457: aload_1
    //   458: invokevirtual print : (Ljava/lang/String;)V
    //   461: aload_3
    //   462: ldc_w 'Back Stack Indices:'
    //   465: invokevirtual println : (Ljava/lang/String;)V
    //   468: iconst_0
    //   469: istore #5
    //   471: iload #5
    //   473: iload #7
    //   475: if_icmpge -> 530
    //   478: aload_0
    //   479: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   482: iload #5
    //   484: invokevirtual get : (I)Ljava/lang/Object;
    //   487: checkcast androidx/fragment/app/BackStackRecord
    //   490: astore_2
    //   491: aload_3
    //   492: aload_1
    //   493: invokevirtual print : (Ljava/lang/String;)V
    //   496: aload_3
    //   497: ldc_w '  #'
    //   500: invokevirtual print : (Ljava/lang/String;)V
    //   503: aload_3
    //   504: iload #5
    //   506: invokevirtual print : (I)V
    //   509: aload_3
    //   510: ldc_w ': '
    //   513: invokevirtual print : (Ljava/lang/String;)V
    //   516: aload_3
    //   517: aload_2
    //   518: invokevirtual println : (Ljava/lang/Object;)V
    //   521: iload #5
    //   523: iconst_1
    //   524: iadd
    //   525: istore #5
    //   527: goto -> 471
    //   530: aload_0
    //   531: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   534: ifnull -> 573
    //   537: aload_0
    //   538: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   541: invokevirtual size : ()I
    //   544: ifle -> 573
    //   547: aload_3
    //   548: aload_1
    //   549: invokevirtual print : (Ljava/lang/String;)V
    //   552: aload_3
    //   553: ldc_w 'mAvailBackStackIndices: '
    //   556: invokevirtual print : (Ljava/lang/String;)V
    //   559: aload_3
    //   560: aload_0
    //   561: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   564: invokevirtual toArray : ()[Ljava/lang/Object;
    //   567: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   570: invokevirtual println : (Ljava/lang/String;)V
    //   573: aload_0
    //   574: monitorexit
    //   575: aload_0
    //   576: getfield mPendingActions : Ljava/util/ArrayList;
    //   579: astore_2
    //   580: aload_2
    //   581: ifnull -> 670
    //   584: aload_2
    //   585: invokevirtual size : ()I
    //   588: istore #7
    //   590: iload #7
    //   592: ifle -> 670
    //   595: aload_3
    //   596: aload_1
    //   597: invokevirtual print : (Ljava/lang/String;)V
    //   600: aload_3
    //   601: ldc_w 'Pending Actions:'
    //   604: invokevirtual println : (Ljava/lang/String;)V
    //   607: iload #6
    //   609: istore #5
    //   611: iload #5
    //   613: iload #7
    //   615: if_icmpge -> 670
    //   618: aload_0
    //   619: getfield mPendingActions : Ljava/util/ArrayList;
    //   622: iload #5
    //   624: invokevirtual get : (I)Ljava/lang/Object;
    //   627: checkcast androidx/fragment/app/FragmentManagerImpl$OpGenerator
    //   630: astore_2
    //   631: aload_3
    //   632: aload_1
    //   633: invokevirtual print : (Ljava/lang/String;)V
    //   636: aload_3
    //   637: ldc_w '  #'
    //   640: invokevirtual print : (Ljava/lang/String;)V
    //   643: aload_3
    //   644: iload #5
    //   646: invokevirtual print : (I)V
    //   649: aload_3
    //   650: ldc_w ': '
    //   653: invokevirtual print : (Ljava/lang/String;)V
    //   656: aload_3
    //   657: aload_2
    //   658: invokevirtual println : (Ljava/lang/Object;)V
    //   661: iload #5
    //   663: iconst_1
    //   664: iadd
    //   665: istore #5
    //   667: goto -> 611
    //   670: aload_3
    //   671: aload_1
    //   672: invokevirtual print : (Ljava/lang/String;)V
    //   675: aload_3
    //   676: ldc_w 'FragmentManager misc state:'
    //   679: invokevirtual println : (Ljava/lang/String;)V
    //   682: aload_3
    //   683: aload_1
    //   684: invokevirtual print : (Ljava/lang/String;)V
    //   687: aload_3
    //   688: ldc_w '  mHost='
    //   691: invokevirtual print : (Ljava/lang/String;)V
    //   694: aload_3
    //   695: aload_0
    //   696: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   699: invokevirtual println : (Ljava/lang/Object;)V
    //   702: aload_3
    //   703: aload_1
    //   704: invokevirtual print : (Ljava/lang/String;)V
    //   707: aload_3
    //   708: ldc_w '  mContainer='
    //   711: invokevirtual print : (Ljava/lang/String;)V
    //   714: aload_3
    //   715: aload_0
    //   716: getfield mContainer : Landroidx/fragment/app/FragmentContainer;
    //   719: invokevirtual println : (Ljava/lang/Object;)V
    //   722: aload_0
    //   723: getfield mParent : Landroidx/fragment/app/Fragment;
    //   726: ifnull -> 749
    //   729: aload_3
    //   730: aload_1
    //   731: invokevirtual print : (Ljava/lang/String;)V
    //   734: aload_3
    //   735: ldc_w '  mParent='
    //   738: invokevirtual print : (Ljava/lang/String;)V
    //   741: aload_3
    //   742: aload_0
    //   743: getfield mParent : Landroidx/fragment/app/Fragment;
    //   746: invokevirtual println : (Ljava/lang/Object;)V
    //   749: aload_3
    //   750: aload_1
    //   751: invokevirtual print : (Ljava/lang/String;)V
    //   754: aload_3
    //   755: ldc_w '  mCurState='
    //   758: invokevirtual print : (Ljava/lang/String;)V
    //   761: aload_3
    //   762: aload_0
    //   763: getfield mCurState : I
    //   766: invokevirtual print : (I)V
    //   769: aload_3
    //   770: ldc_w ' mStateSaved='
    //   773: invokevirtual print : (Ljava/lang/String;)V
    //   776: aload_3
    //   777: aload_0
    //   778: getfield mStateSaved : Z
    //   781: invokevirtual print : (Z)V
    //   784: aload_3
    //   785: ldc_w ' mStopped='
    //   788: invokevirtual print : (Ljava/lang/String;)V
    //   791: aload_3
    //   792: aload_0
    //   793: getfield mStopped : Z
    //   796: invokevirtual print : (Z)V
    //   799: aload_3
    //   800: ldc_w ' mDestroyed='
    //   803: invokevirtual print : (Ljava/lang/String;)V
    //   806: aload_3
    //   807: aload_0
    //   808: getfield mDestroyed : Z
    //   811: invokevirtual println : (Z)V
    //   814: aload_0
    //   815: getfield mNeedMenuInvalidate : Z
    //   818: ifeq -> 841
    //   821: aload_3
    //   822: aload_1
    //   823: invokevirtual print : (Ljava/lang/String;)V
    //   826: aload_3
    //   827: ldc_w '  mNeedMenuInvalidate='
    //   830: invokevirtual print : (Ljava/lang/String;)V
    //   833: aload_3
    //   834: aload_0
    //   835: getfield mNeedMenuInvalidate : Z
    //   838: invokevirtual println : (Z)V
    //   841: return
    //   842: astore_1
    //   843: aload_0
    //   844: monitorexit
    //   845: aload_1
    //   846: athrow
    // Exception table:
    //   from	to	target	type
    //   435	451	842	finally
    //   456	468	842	finally
    //   478	521	842	finally
    //   530	573	842	finally
    //   573	575	842	finally
    //   843	845	842	finally
  }
  
  public void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    // Byte code:
    //   0: iload_2
    //   1: ifne -> 8
    //   4: aload_0
    //   5: invokespecial checkStateLoss : ()V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield mDestroyed : Z
    //   14: ifne -> 61
    //   17: aload_0
    //   18: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   21: ifnonnull -> 27
    //   24: goto -> 61
    //   27: aload_0
    //   28: getfield mPendingActions : Ljava/util/ArrayList;
    //   31: ifnonnull -> 45
    //   34: aload_0
    //   35: new java/util/ArrayList
    //   38: dup
    //   39: invokespecial <init> : ()V
    //   42: putfield mPendingActions : Ljava/util/ArrayList;
    //   45: aload_0
    //   46: getfield mPendingActions : Ljava/util/ArrayList;
    //   49: aload_1
    //   50: invokevirtual add : (Ljava/lang/Object;)Z
    //   53: pop
    //   54: aload_0
    //   55: invokevirtual scheduleCommit : ()V
    //   58: aload_0
    //   59: monitorexit
    //   60: return
    //   61: iload_2
    //   62: ifeq -> 68
    //   65: aload_0
    //   66: monitorexit
    //   67: return
    //   68: new java/lang/IllegalStateException
    //   71: dup
    //   72: ldc_w 'Activity has been destroyed'
    //   75: invokespecial <init> : (Ljava/lang/String;)V
    //   78: athrow
    //   79: astore_1
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_1
    //   83: athrow
    // Exception table:
    //   from	to	target	type
    //   10	24	79	finally
    //   27	45	79	finally
    //   45	60	79	finally
    //   65	67	79	finally
    //   68	79	79	finally
    //   80	82	79	finally
  }
  
  void ensureInflatedFragmentView(Fragment paramFragment) {
    if (paramFragment.mFromLayout && !paramFragment.mPerformedCreateView) {
      paramFragment.performCreateView(paramFragment.performGetLayoutInflater(paramFragment.mSavedFragmentState), null, paramFragment.mSavedFragmentState);
      if (paramFragment.mView != null) {
        paramFragment.mInnerView = paramFragment.mView;
        paramFragment.mView.setSaveFromParentEnabled(false);
        if (paramFragment.mHidden)
          paramFragment.mView.setVisibility(8); 
        paramFragment.onViewCreated(paramFragment.mView, paramFragment.mSavedFragmentState);
        dispatchOnFragmentViewCreated(paramFragment, paramFragment.mView, paramFragment.mSavedFragmentState, false);
        return;
      } 
      paramFragment.mInnerView = null;
    } 
  }
  
  public boolean execPendingActions() {
    ensureExecReady(true);
    boolean bool = false;
    while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  public void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    if (paramBoolean && (this.mHost == null || this.mDestroyed))
      return; 
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    burpActive();
  }
  
  public boolean executePendingTransactions() {
    boolean bool = execPendingActions();
    forcePostponedTransactions();
    return bool;
  }
  
  public Fragment findFragmentById(int paramInt) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null && fragment.mFragmentId == paramInt)
        return fragment; 
    } 
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null && fragment.mFragmentId == paramInt)
        return fragment; 
    } 
    return null;
  }
  
  public Fragment findFragmentByTag(String paramString) {
    if (paramString != null)
      for (int i = this.mAdded.size() - 1; i >= 0; i--) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    if (paramString != null)
      for (Fragment fragment : this.mActive.values()) {
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    return null;
  }
  
  public Fragment findFragmentByWho(String paramString) {
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null) {
        fragment = fragment.findFragmentByWho(paramString);
        if (fragment != null)
          return fragment; 
      } 
    } 
    return null;
  }
  
  public void freeBackStackIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: iload_1
    //   7: aconst_null
    //   8: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   16: ifnonnull -> 30
    //   19: aload_0
    //   20: new java/util/ArrayList
    //   23: dup
    //   24: invokespecial <init> : ()V
    //   27: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   30: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   33: ifeq -> 68
    //   36: new java/lang/StringBuilder
    //   39: dup
    //   40: invokespecial <init> : ()V
    //   43: astore_2
    //   44: aload_2
    //   45: ldc_w 'Freeing back stack index '
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: aload_2
    //   53: iload_1
    //   54: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   57: pop
    //   58: ldc 'FragmentManager'
    //   60: aload_2
    //   61: invokevirtual toString : ()Ljava/lang/String;
    //   64: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   67: pop
    //   68: aload_0
    //   69: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   72: iload_1
    //   73: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   76: invokevirtual add : (Ljava/lang/Object;)Z
    //   79: pop
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: astore_2
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_2
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	83	finally
    //   30	68	83	finally
    //   68	82	83	finally
    //   84	86	83	finally
  }
  
  int getActiveFragmentCount() {
    return this.mActive.size();
  }
  
  List<Fragment> getActiveFragments() {
    return new ArrayList<Fragment>(this.mActive.values());
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt) {
    return this.mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount() {
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    return (arrayList != null) ? arrayList.size() : 0;
  }
  
  FragmentManagerViewModel getChildNonConfig(Fragment paramFragment) {
    return this.mNonConfig.getChildNonConfig(paramFragment);
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString) {
    String str = paramBundle.getString(paramString);
    if (str == null)
      return null; 
    Fragment fragment = this.mActive.get(str);
    if (fragment == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment no longer exists for key ");
      stringBuilder.append(paramString);
      stringBuilder.append(": unique id ");
      stringBuilder.append(str);
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    return fragment;
  }
  
  public FragmentFactory getFragmentFactory() {
    if (super.getFragmentFactory() == DEFAULT_FACTORY) {
      Fragment fragment = this.mParent;
      if (fragment != null)
        return fragment.mFragmentManager.getFragmentFactory(); 
      setFragmentFactory(new FragmentFactory() {
            public Fragment instantiate(ClassLoader param1ClassLoader, String param1String) {
              return FragmentManagerImpl.this.mHost.instantiate(FragmentManagerImpl.this.mHost.getContext(), param1String, null);
            }
          });
    } 
    return super.getFragmentFactory();
  }
  
  public List<Fragment> getFragments() {
    if (this.mAdded.isEmpty())
      return Collections.emptyList(); 
    synchronized (this.mAdded) {
      return (List)this.mAdded.clone();
    } 
  }
  
  LayoutInflater.Factory2 getLayoutInflaterFactory() {
    return this;
  }
  
  public Fragment getPrimaryNavigationFragment() {
    return this.mPrimaryNav;
  }
  
  ViewModelStore getViewModelStore(Fragment paramFragment) {
    return this.mNonConfig.getViewModelStore(paramFragment);
  }
  
  void handleOnBackPressed() {
    execPendingActions();
    if (this.mOnBackPressedCallback.isEnabled()) {
      popBackStackImmediate();
      return;
    } 
    this.mOnBackPressedDispatcher.onBackPressed();
  }
  
  public void hideFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("hide: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mHidden) {
      paramFragment.mHidden = true;
      paramFragment.mHiddenChanged = true ^ paramFragment.mHiddenChanged;
    } 
  }
  
  public boolean isDestroyed() {
    return this.mDestroyed;
  }
  
  boolean isPrimaryNavigation(Fragment paramFragment) {
    if (paramFragment == null)
      return true; 
    FragmentManagerImpl fragmentManagerImpl = paramFragment.mFragmentManager;
    return (paramFragment == fragmentManagerImpl.getPrimaryNavigationFragment() && isPrimaryNavigation(fragmentManagerImpl.mParent));
  }
  
  boolean isStateAtLeast(int paramInt) {
    return (this.mCurState >= paramInt);
  }
  
  public boolean isStateSaved() {
    return (this.mStateSaved || this.mStopped);
  }
  
  AnimationOrAnimator loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2) {
    int i = paramFragment.getNextAnim();
    boolean bool = false;
    paramFragment.setNextAnim(0);
    if (paramFragment.mContainer != null && paramFragment.mContainer.getLayoutTransition() != null)
      return null; 
    Animation animation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, i);
    if (animation != null)
      return new AnimationOrAnimator(animation); 
    Animator animator = paramFragment.onCreateAnimator(paramInt1, paramBoolean, i);
    if (animator != null)
      return new AnimationOrAnimator(animator); 
    if (i != 0) {
      boolean bool2 = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(i));
      boolean bool1 = bool;
      if (bool2)
        try {
          Animation animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
          if (animation1 != null)
            return new AnimationOrAnimator(animation1); 
          bool1 = true;
        } catch (android.content.res.Resources.NotFoundException notFoundException) {
          throw notFoundException;
        } catch (RuntimeException runtimeException) {
          bool1 = bool;
        }  
      if (!bool1)
        try {
          animator = AnimatorInflater.loadAnimator(this.mHost.getContext(), i);
          if (animator != null)
            return new AnimationOrAnimator(animator); 
        } catch (RuntimeException runtimeException) {
          Animation animation1;
          if (!bool2) {
            animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
            if (animation1 != null)
              return new AnimationOrAnimator(animation1); 
          } else {
            throw animation1;
          } 
        }  
    } 
    if (paramInt1 == 0)
      return null; 
    paramInt1 = transitToStyleIndex(paramInt1, paramBoolean);
    if (paramInt1 < 0)
      return null; 
    switch (paramInt1) {
      default:
        paramInt1 = paramInt2;
        if (paramInt2 == 0) {
          paramInt1 = paramInt2;
          if (this.mHost.onHasWindowAnimations())
            paramInt1 = this.mHost.onGetWindowAnimations(); 
        } 
        break;
      case 6:
        return makeFadeAnimation(1.0F, 0.0F);
      case 5:
        return makeFadeAnimation(0.0F, 1.0F);
      case 4:
        return makeOpenCloseAnimation(1.0F, 1.075F, 1.0F, 0.0F);
      case 3:
        return makeOpenCloseAnimation(0.975F, 1.0F, 0.0F, 1.0F);
      case 2:
        return makeOpenCloseAnimation(1.0F, 0.975F, 1.0F, 0.0F);
      case 1:
        return makeOpenCloseAnimation(1.125F, 1.0F, 0.0F, 1.0F);
    } 
    if (paramInt1 == 0);
    return null;
  }
  
  void makeActive(Fragment paramFragment) {
    if (this.mActive.get(paramFragment.mWho) != null)
      return; 
    this.mActive.put(paramFragment.mWho, paramFragment);
    if (paramFragment.mRetainInstanceChangedWhileDetached) {
      if (paramFragment.mRetainInstance) {
        addRetainedFragment(paramFragment);
      } else {
        removeRetainedFragment(paramFragment);
      } 
      paramFragment.mRetainInstanceChangedWhileDetached = false;
    } 
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Added fragment to active set ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  void makeInactive(Fragment paramFragment) {
    if (this.mActive.get(paramFragment.mWho) == null)
      return; 
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Removed fragment from active set ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null && paramFragment.mWho.equals(fragment.mTargetWho)) {
        fragment.mTarget = paramFragment;
        fragment.mTargetWho = null;
      } 
    } 
    this.mActive.put(paramFragment.mWho, null);
    removeRetainedFragment(paramFragment);
    if (paramFragment.mTargetWho != null)
      paramFragment.mTarget = this.mActive.get(paramFragment.mTargetWho); 
    paramFragment.initState();
  }
  
  void moveFragmentToExpectedState(Fragment paramFragment) {
    if (paramFragment == null)
      return; 
    if (!this.mActive.containsKey(paramFragment.mWho)) {
      if (DEBUG) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ignoring moving ");
        stringBuilder.append(paramFragment);
        stringBuilder.append(" to state ");
        stringBuilder.append(this.mCurState);
        stringBuilder.append("since it is not added to ");
        stringBuilder.append(this);
        Log.v("FragmentManager", stringBuilder.toString());
      } 
      return;
    } 
    int j = this.mCurState;
    int i = j;
    if (paramFragment.mRemoving)
      if (paramFragment.isInBackStack()) {
        i = Math.min(j, 1);
      } else {
        i = Math.min(j, 0);
      }  
    moveToState(paramFragment, i, paramFragment.getNextTransition(), paramFragment.getNextTransitionStyle(), false);
    if (paramFragment.mView != null) {
      Fragment fragment = findFragmentUnder(paramFragment);
      if (fragment != null) {
        View view = fragment.mView;
        ViewGroup viewGroup = paramFragment.mContainer;
        i = viewGroup.indexOfChild(view);
        j = viewGroup.indexOfChild(paramFragment.mView);
        if (j < i) {
          viewGroup.removeViewAt(j);
          viewGroup.addView(paramFragment.mView, i);
        } 
      } 
      if (paramFragment.mIsNewlyAdded && paramFragment.mContainer != null) {
        if (paramFragment.mPostponedAlpha > 0.0F)
          paramFragment.mView.setAlpha(paramFragment.mPostponedAlpha); 
        paramFragment.mPostponedAlpha = 0.0F;
        paramFragment.mIsNewlyAdded = false;
        AnimationOrAnimator animationOrAnimator = loadAnimation(paramFragment, paramFragment.getNextTransition(), true, paramFragment.getNextTransitionStyle());
        if (animationOrAnimator != null)
          if (animationOrAnimator.animation != null) {
            paramFragment.mView.startAnimation(animationOrAnimator.animation);
          } else {
            animationOrAnimator.animator.setTarget(paramFragment.mView);
            animationOrAnimator.animator.start();
          }  
      } 
    } 
    if (paramFragment.mHiddenChanged)
      completeShowHideFragment(paramFragment); 
  }
  
  void moveToState(int paramInt, boolean paramBoolean) {
    if (this.mHost != null || paramInt == 0) {
      if (!paramBoolean && paramInt == this.mCurState)
        return; 
      this.mCurState = paramInt;
      int i = this.mAdded.size();
      for (paramInt = 0; paramInt < i; paramInt++)
        moveFragmentToExpectedState(this.mAdded.get(paramInt)); 
      for (Fragment fragment : this.mActive.values()) {
        if (fragment != null && (fragment.mRemoving || fragment.mDetached) && !fragment.mIsNewlyAdded)
          moveFragmentToExpectedState(fragment); 
      } 
      startPendingDeferredFragments();
      if (this.mNeedMenuInvalidate) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null && this.mCurState == 4) {
          fragmentHostCallback.onSupportInvalidateOptionsMenu();
          this.mNeedMenuInvalidate = false;
        } 
      } 
      return;
    } 
    throw new IllegalStateException("No activity");
  }
  
  void moveToState(Fragment paramFragment) {
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }
  
  void moveToState(Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mAdded : Z
    //   4: istore #10
    //   6: iconst_1
    //   7: istore #9
    //   9: iconst_1
    //   10: istore #7
    //   12: iconst_1
    //   13: istore #8
    //   15: iload #10
    //   17: ifeq -> 33
    //   20: aload_1
    //   21: getfield mDetached : Z
    //   24: ifeq -> 30
    //   27: goto -> 33
    //   30: goto -> 47
    //   33: iload_2
    //   34: istore #6
    //   36: iload #6
    //   38: istore_2
    //   39: iload #6
    //   41: iconst_1
    //   42: if_icmple -> 47
    //   45: iconst_1
    //   46: istore_2
    //   47: iload_2
    //   48: istore #6
    //   50: aload_1
    //   51: getfield mRemoving : Z
    //   54: ifeq -> 94
    //   57: iload_2
    //   58: istore #6
    //   60: iload_2
    //   61: aload_1
    //   62: getfield mState : I
    //   65: if_icmple -> 94
    //   68: aload_1
    //   69: getfield mState : I
    //   72: ifne -> 88
    //   75: aload_1
    //   76: invokevirtual isInBackStack : ()Z
    //   79: ifeq -> 88
    //   82: iconst_1
    //   83: istore #6
    //   85: goto -> 94
    //   88: aload_1
    //   89: getfield mState : I
    //   92: istore #6
    //   94: iload #6
    //   96: istore_2
    //   97: aload_1
    //   98: getfield mDeferStart : Z
    //   101: ifeq -> 126
    //   104: iload #6
    //   106: istore_2
    //   107: aload_1
    //   108: getfield mState : I
    //   111: iconst_3
    //   112: if_icmpge -> 126
    //   115: iload #6
    //   117: istore_2
    //   118: iload #6
    //   120: iconst_2
    //   121: if_icmple -> 126
    //   124: iconst_2
    //   125: istore_2
    //   126: aload_1
    //   127: getfield mMaxState : Landroidx/lifecycle/Lifecycle$State;
    //   130: getstatic androidx/lifecycle/Lifecycle$State.CREATED : Landroidx/lifecycle/Lifecycle$State;
    //   133: if_acmpne -> 145
    //   136: iload_2
    //   137: iconst_1
    //   138: invokestatic min : (II)I
    //   141: istore_2
    //   142: goto -> 157
    //   145: iload_2
    //   146: aload_1
    //   147: getfield mMaxState : Landroidx/lifecycle/Lifecycle$State;
    //   150: invokevirtual ordinal : ()I
    //   153: invokestatic min : (II)I
    //   156: istore_2
    //   157: aload_1
    //   158: getfield mState : I
    //   161: iload_2
    //   162: if_icmpgt -> 1472
    //   165: aload_1
    //   166: getfield mFromLayout : Z
    //   169: ifeq -> 180
    //   172: aload_1
    //   173: getfield mInLayout : Z
    //   176: ifne -> 180
    //   179: return
    //   180: aload_1
    //   181: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   184: ifnonnull -> 194
    //   187: aload_1
    //   188: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   191: ifnull -> 216
    //   194: aload_1
    //   195: aconst_null
    //   196: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   199: aload_1
    //   200: aconst_null
    //   201: invokevirtual setAnimator : (Landroid/animation/Animator;)V
    //   204: aload_0
    //   205: aload_1
    //   206: aload_1
    //   207: invokevirtual getStateAfterAnimating : ()I
    //   210: iconst_0
    //   211: iconst_0
    //   212: iconst_1
    //   213: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   216: aload_1
    //   217: getfield mState : I
    //   220: istore #4
    //   222: iload #4
    //   224: ifeq -> 259
    //   227: iload_2
    //   228: istore_3
    //   229: iload #4
    //   231: iconst_1
    //   232: if_icmpeq -> 880
    //   235: iload #4
    //   237: iconst_2
    //   238: if_icmpeq -> 256
    //   241: iload #4
    //   243: iconst_3
    //   244: if_icmpeq -> 253
    //   247: iload_2
    //   248: istore #6
    //   250: goto -> 2273
    //   253: goto -> 1396
    //   256: goto -> 1339
    //   259: iload_2
    //   260: istore_3
    //   261: iload_2
    //   262: ifle -> 880
    //   265: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   268: ifeq -> 307
    //   271: new java/lang/StringBuilder
    //   274: dup
    //   275: invokespecial <init> : ()V
    //   278: astore #11
    //   280: aload #11
    //   282: ldc_w 'moveto CREATED: '
    //   285: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   288: pop
    //   289: aload #11
    //   291: aload_1
    //   292: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   295: pop
    //   296: ldc 'FragmentManager'
    //   298: aload #11
    //   300: invokevirtual toString : ()Ljava/lang/String;
    //   303: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   306: pop
    //   307: iload_2
    //   308: istore_3
    //   309: aload_1
    //   310: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   313: ifnull -> 466
    //   316: aload_1
    //   317: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   320: aload_0
    //   321: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   324: invokevirtual getContext : ()Landroid/content/Context;
    //   327: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   330: invokevirtual setClassLoader : (Ljava/lang/ClassLoader;)V
    //   333: aload_1
    //   334: aload_1
    //   335: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   338: ldc 'android:view_state'
    //   340: invokevirtual getSparseParcelableArray : (Ljava/lang/String;)Landroid/util/SparseArray;
    //   343: putfield mSavedViewState : Landroid/util/SparseArray;
    //   346: aload_0
    //   347: aload_1
    //   348: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   351: ldc 'android:target_state'
    //   353: invokevirtual getFragment : (Landroid/os/Bundle;Ljava/lang/String;)Landroidx/fragment/app/Fragment;
    //   356: astore #11
    //   358: aload #11
    //   360: ifnull -> 373
    //   363: aload #11
    //   365: getfield mWho : Ljava/lang/String;
    //   368: astore #11
    //   370: goto -> 376
    //   373: aconst_null
    //   374: astore #11
    //   376: aload_1
    //   377: aload #11
    //   379: putfield mTargetWho : Ljava/lang/String;
    //   382: aload_1
    //   383: getfield mTargetWho : Ljava/lang/String;
    //   386: ifnull -> 403
    //   389: aload_1
    //   390: aload_1
    //   391: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   394: ldc 'android:target_req_state'
    //   396: iconst_0
    //   397: invokevirtual getInt : (Ljava/lang/String;I)I
    //   400: putfield mTargetRequestCode : I
    //   403: aload_1
    //   404: getfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   407: ifnull -> 429
    //   410: aload_1
    //   411: aload_1
    //   412: getfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   415: invokevirtual booleanValue : ()Z
    //   418: putfield mUserVisibleHint : Z
    //   421: aload_1
    //   422: aconst_null
    //   423: putfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   426: goto -> 443
    //   429: aload_1
    //   430: aload_1
    //   431: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   434: ldc 'android:user_visible_hint'
    //   436: iconst_1
    //   437: invokevirtual getBoolean : (Ljava/lang/String;Z)Z
    //   440: putfield mUserVisibleHint : Z
    //   443: iload_2
    //   444: istore_3
    //   445: aload_1
    //   446: getfield mUserVisibleHint : Z
    //   449: ifne -> 466
    //   452: aload_1
    //   453: iconst_1
    //   454: putfield mDeferStart : Z
    //   457: iload_2
    //   458: istore_3
    //   459: iload_2
    //   460: iconst_2
    //   461: if_icmple -> 466
    //   464: iconst_2
    //   465: istore_3
    //   466: aload_1
    //   467: aload_0
    //   468: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   471: putfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   474: aload_1
    //   475: aload_0
    //   476: getfield mParent : Landroidx/fragment/app/Fragment;
    //   479: putfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   482: aload_0
    //   483: getfield mParent : Landroidx/fragment/app/Fragment;
    //   486: astore #11
    //   488: aload #11
    //   490: ifnull -> 503
    //   493: aload #11
    //   495: getfield mChildFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   498: astore #11
    //   500: goto -> 512
    //   503: aload_0
    //   504: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   507: getfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   510: astore #11
    //   512: aload_1
    //   513: aload #11
    //   515: putfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   518: aload_1
    //   519: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   522: ifnull -> 657
    //   525: aload_0
    //   526: getfield mActive : Ljava/util/HashMap;
    //   529: aload_1
    //   530: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   533: getfield mWho : Ljava/lang/String;
    //   536: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   539: aload_1
    //   540: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   543: if_acmpne -> 591
    //   546: aload_1
    //   547: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   550: getfield mState : I
    //   553: iconst_1
    //   554: if_icmpge -> 572
    //   557: aload_0
    //   558: aload_1
    //   559: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   562: iconst_1
    //   563: iconst_0
    //   564: iconst_0
    //   565: iconst_1
    //   566: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   569: goto -> 572
    //   572: aload_1
    //   573: aload_1
    //   574: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   577: getfield mWho : Ljava/lang/String;
    //   580: putfield mTargetWho : Ljava/lang/String;
    //   583: aload_1
    //   584: aconst_null
    //   585: putfield mTarget : Landroidx/fragment/app/Fragment;
    //   588: goto -> 657
    //   591: new java/lang/StringBuilder
    //   594: dup
    //   595: invokespecial <init> : ()V
    //   598: astore #11
    //   600: aload #11
    //   602: ldc_w 'Fragment '
    //   605: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   608: pop
    //   609: aload #11
    //   611: aload_1
    //   612: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   615: pop
    //   616: aload #11
    //   618: ldc_w ' declared target fragment '
    //   621: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   624: pop
    //   625: aload #11
    //   627: aload_1
    //   628: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   631: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   634: pop
    //   635: aload #11
    //   637: ldc_w ' that does not belong to this FragmentManager!'
    //   640: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: pop
    //   644: new java/lang/IllegalStateException
    //   647: dup
    //   648: aload #11
    //   650: invokevirtual toString : ()Ljava/lang/String;
    //   653: invokespecial <init> : (Ljava/lang/String;)V
    //   656: athrow
    //   657: aload_1
    //   658: getfield mTargetWho : Ljava/lang/String;
    //   661: ifnull -> 773
    //   664: aload_0
    //   665: getfield mActive : Ljava/util/HashMap;
    //   668: aload_1
    //   669: getfield mTargetWho : Ljava/lang/String;
    //   672: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   675: checkcast androidx/fragment/app/Fragment
    //   678: astore #11
    //   680: aload #11
    //   682: ifnull -> 707
    //   685: aload #11
    //   687: getfield mState : I
    //   690: iconst_1
    //   691: if_icmpge -> 773
    //   694: aload_0
    //   695: aload #11
    //   697: iconst_1
    //   698: iconst_0
    //   699: iconst_0
    //   700: iconst_1
    //   701: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   704: goto -> 773
    //   707: new java/lang/StringBuilder
    //   710: dup
    //   711: invokespecial <init> : ()V
    //   714: astore #11
    //   716: aload #11
    //   718: ldc_w 'Fragment '
    //   721: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   724: pop
    //   725: aload #11
    //   727: aload_1
    //   728: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   731: pop
    //   732: aload #11
    //   734: ldc_w ' declared target fragment '
    //   737: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   740: pop
    //   741: aload #11
    //   743: aload_1
    //   744: getfield mTargetWho : Ljava/lang/String;
    //   747: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   750: pop
    //   751: aload #11
    //   753: ldc_w ' that does not belong to this FragmentManager!'
    //   756: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   759: pop
    //   760: new java/lang/IllegalStateException
    //   763: dup
    //   764: aload #11
    //   766: invokevirtual toString : ()Ljava/lang/String;
    //   769: invokespecial <init> : (Ljava/lang/String;)V
    //   772: athrow
    //   773: aload_0
    //   774: aload_1
    //   775: aload_0
    //   776: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   779: invokevirtual getContext : ()Landroid/content/Context;
    //   782: iconst_0
    //   783: invokevirtual dispatchOnFragmentPreAttached : (Landroidx/fragment/app/Fragment;Landroid/content/Context;Z)V
    //   786: aload_1
    //   787: invokevirtual performAttach : ()V
    //   790: aload_1
    //   791: getfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   794: ifnonnull -> 808
    //   797: aload_0
    //   798: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   801: aload_1
    //   802: invokevirtual onAttachFragment : (Landroidx/fragment/app/Fragment;)V
    //   805: goto -> 816
    //   808: aload_1
    //   809: getfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   812: aload_1
    //   813: invokevirtual onAttachFragment : (Landroidx/fragment/app/Fragment;)V
    //   816: aload_0
    //   817: aload_1
    //   818: aload_0
    //   819: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   822: invokevirtual getContext : ()Landroid/content/Context;
    //   825: iconst_0
    //   826: invokevirtual dispatchOnFragmentAttached : (Landroidx/fragment/app/Fragment;Landroid/content/Context;Z)V
    //   829: aload_1
    //   830: getfield mIsCreated : Z
    //   833: ifne -> 867
    //   836: aload_0
    //   837: aload_1
    //   838: aload_1
    //   839: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   842: iconst_0
    //   843: invokevirtual dispatchOnFragmentPreCreated : (Landroidx/fragment/app/Fragment;Landroid/os/Bundle;Z)V
    //   846: aload_1
    //   847: aload_1
    //   848: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   851: invokevirtual performCreate : (Landroid/os/Bundle;)V
    //   854: aload_0
    //   855: aload_1
    //   856: aload_1
    //   857: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   860: iconst_0
    //   861: invokevirtual dispatchOnFragmentCreated : (Landroidx/fragment/app/Fragment;Landroid/os/Bundle;Z)V
    //   864: goto -> 880
    //   867: aload_1
    //   868: aload_1
    //   869: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   872: invokevirtual restoreChildFragmentState : (Landroid/os/Bundle;)V
    //   875: aload_1
    //   876: iconst_1
    //   877: putfield mState : I
    //   880: iload_3
    //   881: ifle -> 889
    //   884: aload_0
    //   885: aload_1
    //   886: invokevirtual ensureInflatedFragmentView : (Landroidx/fragment/app/Fragment;)V
    //   889: iload_3
    //   890: iconst_1
    //   891: if_icmple -> 1337
    //   894: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   897: ifeq -> 936
    //   900: new java/lang/StringBuilder
    //   903: dup
    //   904: invokespecial <init> : ()V
    //   907: astore #11
    //   909: aload #11
    //   911: ldc_w 'moveto ACTIVITY_CREATED: '
    //   914: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   917: pop
    //   918: aload #11
    //   920: aload_1
    //   921: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   924: pop
    //   925: ldc 'FragmentManager'
    //   927: aload #11
    //   929: invokevirtual toString : ()Ljava/lang/String;
    //   932: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   935: pop
    //   936: aload_1
    //   937: getfield mFromLayout : Z
    //   940: ifne -> 1299
    //   943: aload_1
    //   944: getfield mContainerId : I
    //   947: ifeq -> 1152
    //   950: aload_1
    //   951: getfield mContainerId : I
    //   954: iconst_m1
    //   955: if_icmpne -> 1008
    //   958: new java/lang/StringBuilder
    //   961: dup
    //   962: invokespecial <init> : ()V
    //   965: astore #11
    //   967: aload #11
    //   969: ldc_w 'Cannot create fragment '
    //   972: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   975: pop
    //   976: aload #11
    //   978: aload_1
    //   979: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   982: pop
    //   983: aload #11
    //   985: ldc_w ' for a container view with no id'
    //   988: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   991: pop
    //   992: aload_0
    //   993: new java/lang/IllegalArgumentException
    //   996: dup
    //   997: aload #11
    //   999: invokevirtual toString : ()Ljava/lang/String;
    //   1002: invokespecial <init> : (Ljava/lang/String;)V
    //   1005: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   1008: aload_0
    //   1009: getfield mContainer : Landroidx/fragment/app/FragmentContainer;
    //   1012: aload_1
    //   1013: getfield mContainerId : I
    //   1016: invokevirtual onFindViewById : (I)Landroid/view/View;
    //   1019: checkcast android/view/ViewGroup
    //   1022: astore #12
    //   1024: aload #12
    //   1026: astore #11
    //   1028: aload #12
    //   1030: ifnonnull -> 1155
    //   1033: aload #12
    //   1035: astore #11
    //   1037: aload_1
    //   1038: getfield mRestored : Z
    //   1041: ifne -> 1155
    //   1044: aload_1
    //   1045: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   1048: aload_1
    //   1049: getfield mContainerId : I
    //   1052: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   1055: astore #11
    //   1057: goto -> 1065
    //   1060: ldc_w 'unknown'
    //   1063: astore #11
    //   1065: new java/lang/StringBuilder
    //   1068: dup
    //   1069: invokespecial <init> : ()V
    //   1072: astore #13
    //   1074: aload #13
    //   1076: ldc_w 'No view found for id 0x'
    //   1079: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1082: pop
    //   1083: aload #13
    //   1085: aload_1
    //   1086: getfield mContainerId : I
    //   1089: invokestatic toHexString : (I)Ljava/lang/String;
    //   1092: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1095: pop
    //   1096: aload #13
    //   1098: ldc_w ' ('
    //   1101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1104: pop
    //   1105: aload #13
    //   1107: aload #11
    //   1109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1112: pop
    //   1113: aload #13
    //   1115: ldc_w ') for fragment '
    //   1118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1121: pop
    //   1122: aload #13
    //   1124: aload_1
    //   1125: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1128: pop
    //   1129: aload_0
    //   1130: new java/lang/IllegalArgumentException
    //   1133: dup
    //   1134: aload #13
    //   1136: invokevirtual toString : ()Ljava/lang/String;
    //   1139: invokespecial <init> : (Ljava/lang/String;)V
    //   1142: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   1145: aload #12
    //   1147: astore #11
    //   1149: goto -> 1155
    //   1152: aconst_null
    //   1153: astore #11
    //   1155: aload_1
    //   1156: aload #11
    //   1158: putfield mContainer : Landroid/view/ViewGroup;
    //   1161: aload_1
    //   1162: aload_1
    //   1163: aload_1
    //   1164: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1167: invokevirtual performGetLayoutInflater : (Landroid/os/Bundle;)Landroid/view/LayoutInflater;
    //   1170: aload #11
    //   1172: aload_1
    //   1173: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1176: invokevirtual performCreateView : (Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)V
    //   1179: aload_1
    //   1180: getfield mView : Landroid/view/View;
    //   1183: ifnull -> 1294
    //   1186: aload_1
    //   1187: aload_1
    //   1188: getfield mView : Landroid/view/View;
    //   1191: putfield mInnerView : Landroid/view/View;
    //   1194: aload_1
    //   1195: getfield mView : Landroid/view/View;
    //   1198: iconst_0
    //   1199: invokevirtual setSaveFromParentEnabled : (Z)V
    //   1202: aload #11
    //   1204: ifnull -> 1216
    //   1207: aload #11
    //   1209: aload_1
    //   1210: getfield mView : Landroid/view/View;
    //   1213: invokevirtual addView : (Landroid/view/View;)V
    //   1216: aload_1
    //   1217: getfield mHidden : Z
    //   1220: ifeq -> 1232
    //   1223: aload_1
    //   1224: getfield mView : Landroid/view/View;
    //   1227: bipush #8
    //   1229: invokevirtual setVisibility : (I)V
    //   1232: aload_1
    //   1233: aload_1
    //   1234: getfield mView : Landroid/view/View;
    //   1237: aload_1
    //   1238: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1241: invokevirtual onViewCreated : (Landroid/view/View;Landroid/os/Bundle;)V
    //   1244: aload_0
    //   1245: aload_1
    //   1246: aload_1
    //   1247: getfield mView : Landroid/view/View;
    //   1250: aload_1
    //   1251: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1254: iconst_0
    //   1255: invokevirtual dispatchOnFragmentViewCreated : (Landroidx/fragment/app/Fragment;Landroid/view/View;Landroid/os/Bundle;Z)V
    //   1258: aload_1
    //   1259: getfield mView : Landroid/view/View;
    //   1262: invokevirtual getVisibility : ()I
    //   1265: ifne -> 1282
    //   1268: aload_1
    //   1269: getfield mContainer : Landroid/view/ViewGroup;
    //   1272: ifnull -> 1282
    //   1275: iload #8
    //   1277: istore #5
    //   1279: goto -> 1285
    //   1282: iconst_0
    //   1283: istore #5
    //   1285: aload_1
    //   1286: iload #5
    //   1288: putfield mIsNewlyAdded : Z
    //   1291: goto -> 1299
    //   1294: aload_1
    //   1295: aconst_null
    //   1296: putfield mInnerView : Landroid/view/View;
    //   1299: aload_1
    //   1300: aload_1
    //   1301: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1304: invokevirtual performActivityCreated : (Landroid/os/Bundle;)V
    //   1307: aload_0
    //   1308: aload_1
    //   1309: aload_1
    //   1310: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1313: iconst_0
    //   1314: invokevirtual dispatchOnFragmentActivityCreated : (Landroidx/fragment/app/Fragment;Landroid/os/Bundle;Z)V
    //   1317: aload_1
    //   1318: getfield mView : Landroid/view/View;
    //   1321: ifnull -> 1332
    //   1324: aload_1
    //   1325: aload_1
    //   1326: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1329: invokevirtual restoreViewState : (Landroid/os/Bundle;)V
    //   1332: aload_1
    //   1333: aconst_null
    //   1334: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1337: iload_3
    //   1338: istore_2
    //   1339: iload_2
    //   1340: iconst_2
    //   1341: if_icmple -> 1396
    //   1344: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1347: ifeq -> 1386
    //   1350: new java/lang/StringBuilder
    //   1353: dup
    //   1354: invokespecial <init> : ()V
    //   1357: astore #11
    //   1359: aload #11
    //   1361: ldc_w 'moveto STARTED: '
    //   1364: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1367: pop
    //   1368: aload #11
    //   1370: aload_1
    //   1371: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1374: pop
    //   1375: ldc 'FragmentManager'
    //   1377: aload #11
    //   1379: invokevirtual toString : ()Ljava/lang/String;
    //   1382: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1385: pop
    //   1386: aload_1
    //   1387: invokevirtual performStart : ()V
    //   1390: aload_0
    //   1391: aload_1
    //   1392: iconst_0
    //   1393: invokevirtual dispatchOnFragmentStarted : (Landroidx/fragment/app/Fragment;Z)V
    //   1396: iload_2
    //   1397: istore #6
    //   1399: iload_2
    //   1400: iconst_3
    //   1401: if_icmple -> 2273
    //   1404: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1407: ifeq -> 1446
    //   1410: new java/lang/StringBuilder
    //   1413: dup
    //   1414: invokespecial <init> : ()V
    //   1417: astore #11
    //   1419: aload #11
    //   1421: ldc_w 'moveto RESUMED: '
    //   1424: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1427: pop
    //   1428: aload #11
    //   1430: aload_1
    //   1431: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1434: pop
    //   1435: ldc 'FragmentManager'
    //   1437: aload #11
    //   1439: invokevirtual toString : ()Ljava/lang/String;
    //   1442: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1445: pop
    //   1446: aload_1
    //   1447: invokevirtual performResume : ()V
    //   1450: aload_0
    //   1451: aload_1
    //   1452: iconst_0
    //   1453: invokevirtual dispatchOnFragmentResumed : (Landroidx/fragment/app/Fragment;Z)V
    //   1456: aload_1
    //   1457: aconst_null
    //   1458: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1461: aload_1
    //   1462: aconst_null
    //   1463: putfield mSavedViewState : Landroid/util/SparseArray;
    //   1466: iload_2
    //   1467: istore #6
    //   1469: goto -> 2273
    //   1472: iload_2
    //   1473: istore #6
    //   1475: aload_1
    //   1476: getfield mState : I
    //   1479: iload_2
    //   1480: if_icmple -> 2273
    //   1483: aload_1
    //   1484: getfield mState : I
    //   1487: istore #6
    //   1489: iload #6
    //   1491: iconst_1
    //   1492: if_icmpeq -> 1887
    //   1495: iload #6
    //   1497: iconst_2
    //   1498: if_icmpeq -> 1639
    //   1501: iload #6
    //   1503: iconst_3
    //   1504: if_icmpeq -> 1579
    //   1507: iload #6
    //   1509: iconst_4
    //   1510: if_icmpeq -> 1519
    //   1513: iload_2
    //   1514: istore #6
    //   1516: goto -> 2273
    //   1519: iload_2
    //   1520: iconst_4
    //   1521: if_icmpge -> 1576
    //   1524: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1527: ifeq -> 1566
    //   1530: new java/lang/StringBuilder
    //   1533: dup
    //   1534: invokespecial <init> : ()V
    //   1537: astore #11
    //   1539: aload #11
    //   1541: ldc_w 'movefrom RESUMED: '
    //   1544: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1547: pop
    //   1548: aload #11
    //   1550: aload_1
    //   1551: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1554: pop
    //   1555: ldc 'FragmentManager'
    //   1557: aload #11
    //   1559: invokevirtual toString : ()Ljava/lang/String;
    //   1562: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1565: pop
    //   1566: aload_1
    //   1567: invokevirtual performPause : ()V
    //   1570: aload_0
    //   1571: aload_1
    //   1572: iconst_0
    //   1573: invokevirtual dispatchOnFragmentPaused : (Landroidx/fragment/app/Fragment;Z)V
    //   1576: goto -> 1579
    //   1579: iload_2
    //   1580: iconst_3
    //   1581: if_icmpge -> 1636
    //   1584: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1587: ifeq -> 1626
    //   1590: new java/lang/StringBuilder
    //   1593: dup
    //   1594: invokespecial <init> : ()V
    //   1597: astore #11
    //   1599: aload #11
    //   1601: ldc_w 'movefrom STARTED: '
    //   1604: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1607: pop
    //   1608: aload #11
    //   1610: aload_1
    //   1611: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1614: pop
    //   1615: ldc 'FragmentManager'
    //   1617: aload #11
    //   1619: invokevirtual toString : ()Ljava/lang/String;
    //   1622: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1625: pop
    //   1626: aload_1
    //   1627: invokevirtual performStop : ()V
    //   1630: aload_0
    //   1631: aload_1
    //   1632: iconst_0
    //   1633: invokevirtual dispatchOnFragmentStopped : (Landroidx/fragment/app/Fragment;Z)V
    //   1636: goto -> 1639
    //   1639: iload_2
    //   1640: iconst_2
    //   1641: if_icmpge -> 1887
    //   1644: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1647: ifeq -> 1686
    //   1650: new java/lang/StringBuilder
    //   1653: dup
    //   1654: invokespecial <init> : ()V
    //   1657: astore #11
    //   1659: aload #11
    //   1661: ldc_w 'movefrom ACTIVITY_CREATED: '
    //   1664: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1667: pop
    //   1668: aload #11
    //   1670: aload_1
    //   1671: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1674: pop
    //   1675: ldc 'FragmentManager'
    //   1677: aload #11
    //   1679: invokevirtual toString : ()Ljava/lang/String;
    //   1682: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1685: pop
    //   1686: aload_1
    //   1687: getfield mView : Landroid/view/View;
    //   1690: ifnull -> 1716
    //   1693: aload_0
    //   1694: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   1697: aload_1
    //   1698: invokevirtual onShouldSaveFragmentState : (Landroidx/fragment/app/Fragment;)Z
    //   1701: ifeq -> 1716
    //   1704: aload_1
    //   1705: getfield mSavedViewState : Landroid/util/SparseArray;
    //   1708: ifnonnull -> 1716
    //   1711: aload_0
    //   1712: aload_1
    //   1713: invokevirtual saveFragmentViewState : (Landroidx/fragment/app/Fragment;)V
    //   1716: aload_1
    //   1717: invokevirtual performDestroyView : ()V
    //   1720: aload_0
    //   1721: aload_1
    //   1722: iconst_0
    //   1723: invokevirtual dispatchOnFragmentViewDestroyed : (Landroidx/fragment/app/Fragment;Z)V
    //   1726: aload_1
    //   1727: getfield mView : Landroid/view/View;
    //   1730: ifnull -> 1854
    //   1733: aload_1
    //   1734: getfield mContainer : Landroid/view/ViewGroup;
    //   1737: ifnull -> 1854
    //   1740: aload_1
    //   1741: getfield mContainer : Landroid/view/ViewGroup;
    //   1744: aload_1
    //   1745: getfield mView : Landroid/view/View;
    //   1748: invokevirtual endViewTransition : (Landroid/view/View;)V
    //   1751: aload_1
    //   1752: getfield mView : Landroid/view/View;
    //   1755: invokevirtual clearAnimation : ()V
    //   1758: aload_1
    //   1759: invokevirtual getParentFragment : ()Landroidx/fragment/app/Fragment;
    //   1762: ifnull -> 1775
    //   1765: aload_1
    //   1766: invokevirtual getParentFragment : ()Landroidx/fragment/app/Fragment;
    //   1769: getfield mRemoving : Z
    //   1772: ifne -> 1854
    //   1775: aload_0
    //   1776: getfield mCurState : I
    //   1779: ifle -> 1822
    //   1782: aload_0
    //   1783: getfield mDestroyed : Z
    //   1786: ifne -> 1822
    //   1789: aload_1
    //   1790: getfield mView : Landroid/view/View;
    //   1793: invokevirtual getVisibility : ()I
    //   1796: ifne -> 1822
    //   1799: aload_1
    //   1800: getfield mPostponedAlpha : F
    //   1803: fconst_0
    //   1804: fcmpl
    //   1805: iflt -> 1822
    //   1808: aload_0
    //   1809: aload_1
    //   1810: iload_3
    //   1811: iconst_0
    //   1812: iload #4
    //   1814: invokevirtual loadAnimation : (Landroidx/fragment/app/Fragment;IZI)Landroidx/fragment/app/FragmentManagerImpl$AnimationOrAnimator;
    //   1817: astore #11
    //   1819: goto -> 1825
    //   1822: aconst_null
    //   1823: astore #11
    //   1825: aload_1
    //   1826: fconst_0
    //   1827: putfield mPostponedAlpha : F
    //   1830: aload #11
    //   1832: ifnull -> 1843
    //   1835: aload_0
    //   1836: aload_1
    //   1837: aload #11
    //   1839: iload_2
    //   1840: invokespecial animateRemoveFragment : (Landroidx/fragment/app/Fragment;Landroidx/fragment/app/FragmentManagerImpl$AnimationOrAnimator;I)V
    //   1843: aload_1
    //   1844: getfield mContainer : Landroid/view/ViewGroup;
    //   1847: aload_1
    //   1848: getfield mView : Landroid/view/View;
    //   1851: invokevirtual removeView : (Landroid/view/View;)V
    //   1854: aload_1
    //   1855: aconst_null
    //   1856: putfield mContainer : Landroid/view/ViewGroup;
    //   1859: aload_1
    //   1860: aconst_null
    //   1861: putfield mView : Landroid/view/View;
    //   1864: aload_1
    //   1865: aconst_null
    //   1866: putfield mViewLifecycleOwner : Landroidx/fragment/app/FragmentViewLifecycleOwner;
    //   1869: aload_1
    //   1870: getfield mViewLifecycleOwnerLiveData : Landroidx/lifecycle/MutableLiveData;
    //   1873: aconst_null
    //   1874: invokevirtual setValue : (Ljava/lang/Object;)V
    //   1877: aload_1
    //   1878: aconst_null
    //   1879: putfield mInnerView : Landroid/view/View;
    //   1882: aload_1
    //   1883: iconst_0
    //   1884: putfield mInLayout : Z
    //   1887: iload_2
    //   1888: istore #6
    //   1890: iload_2
    //   1891: iconst_1
    //   1892: if_icmpge -> 2273
    //   1895: aload_0
    //   1896: getfield mDestroyed : Z
    //   1899: ifeq -> 1951
    //   1902: aload_1
    //   1903: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1906: ifnull -> 1928
    //   1909: aload_1
    //   1910: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1913: astore #11
    //   1915: aload_1
    //   1916: aconst_null
    //   1917: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   1920: aload #11
    //   1922: invokevirtual clearAnimation : ()V
    //   1925: goto -> 1951
    //   1928: aload_1
    //   1929: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1932: ifnull -> 1951
    //   1935: aload_1
    //   1936: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1939: astore #11
    //   1941: aload_1
    //   1942: aconst_null
    //   1943: invokevirtual setAnimator : (Landroid/animation/Animator;)V
    //   1946: aload #11
    //   1948: invokevirtual cancel : ()V
    //   1951: aload_1
    //   1952: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1955: ifnonnull -> 2261
    //   1958: aload_1
    //   1959: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1962: ifnull -> 1968
    //   1965: goto -> 2261
    //   1968: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1971: ifeq -> 2010
    //   1974: new java/lang/StringBuilder
    //   1977: dup
    //   1978: invokespecial <init> : ()V
    //   1981: astore #11
    //   1983: aload #11
    //   1985: ldc_w 'movefrom CREATED: '
    //   1988: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1991: pop
    //   1992: aload #11
    //   1994: aload_1
    //   1995: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1998: pop
    //   1999: ldc 'FragmentManager'
    //   2001: aload #11
    //   2003: invokevirtual toString : ()Ljava/lang/String;
    //   2006: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   2009: pop
    //   2010: aload_1
    //   2011: getfield mRemoving : Z
    //   2014: ifeq -> 2029
    //   2017: aload_1
    //   2018: invokevirtual isInBackStack : ()Z
    //   2021: ifne -> 2029
    //   2024: iconst_1
    //   2025: istore_3
    //   2026: goto -> 2031
    //   2029: iconst_0
    //   2030: istore_3
    //   2031: iload_3
    //   2032: ifne -> 2057
    //   2035: aload_0
    //   2036: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2039: aload_1
    //   2040: invokevirtual shouldDestroy : (Landroidx/fragment/app/Fragment;)Z
    //   2043: ifeq -> 2049
    //   2046: goto -> 2057
    //   2049: aload_1
    //   2050: iconst_0
    //   2051: putfield mState : I
    //   2054: goto -> 2142
    //   2057: aload_0
    //   2058: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   2061: astore #11
    //   2063: aload #11
    //   2065: instanceof androidx/lifecycle/ViewModelStoreOwner
    //   2068: ifeq -> 2083
    //   2071: aload_0
    //   2072: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2075: invokevirtual isCleared : ()Z
    //   2078: istore #8
    //   2080: goto -> 2115
    //   2083: iload #9
    //   2085: istore #8
    //   2087: aload #11
    //   2089: invokevirtual getContext : ()Landroid/content/Context;
    //   2092: instanceof android/app/Activity
    //   2095: ifeq -> 2115
    //   2098: iconst_1
    //   2099: aload_0
    //   2100: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   2103: invokevirtual getContext : ()Landroid/content/Context;
    //   2106: checkcast android/app/Activity
    //   2109: invokevirtual isChangingConfigurations : ()Z
    //   2112: ixor
    //   2113: istore #8
    //   2115: iload_3
    //   2116: ifne -> 2124
    //   2119: iload #8
    //   2121: ifeq -> 2132
    //   2124: aload_0
    //   2125: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2128: aload_1
    //   2129: invokevirtual clearNonConfigState : (Landroidx/fragment/app/Fragment;)V
    //   2132: aload_1
    //   2133: invokevirtual performDestroy : ()V
    //   2136: aload_0
    //   2137: aload_1
    //   2138: iconst_0
    //   2139: invokevirtual dispatchOnFragmentDestroyed : (Landroidx/fragment/app/Fragment;Z)V
    //   2142: aload_1
    //   2143: invokevirtual performDetach : ()V
    //   2146: aload_0
    //   2147: aload_1
    //   2148: iconst_0
    //   2149: invokevirtual dispatchOnFragmentDetached : (Landroidx/fragment/app/Fragment;Z)V
    //   2152: iload_2
    //   2153: istore #6
    //   2155: iload #5
    //   2157: ifne -> 2273
    //   2160: iload_3
    //   2161: ifne -> 2250
    //   2164: aload_0
    //   2165: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2168: aload_1
    //   2169: invokevirtual shouldDestroy : (Landroidx/fragment/app/Fragment;)Z
    //   2172: ifeq -> 2178
    //   2175: goto -> 2250
    //   2178: aload_1
    //   2179: aconst_null
    //   2180: putfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   2183: aload_1
    //   2184: aconst_null
    //   2185: putfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   2188: aload_1
    //   2189: aconst_null
    //   2190: putfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   2193: iload_2
    //   2194: istore #6
    //   2196: aload_1
    //   2197: getfield mTargetWho : Ljava/lang/String;
    //   2200: ifnull -> 2273
    //   2203: aload_0
    //   2204: getfield mActive : Ljava/util/HashMap;
    //   2207: aload_1
    //   2208: getfield mTargetWho : Ljava/lang/String;
    //   2211: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   2214: checkcast androidx/fragment/app/Fragment
    //   2217: astore #11
    //   2219: iload_2
    //   2220: istore #6
    //   2222: aload #11
    //   2224: ifnull -> 2273
    //   2227: iload_2
    //   2228: istore #6
    //   2230: aload #11
    //   2232: invokevirtual getRetainInstance : ()Z
    //   2235: ifeq -> 2273
    //   2238: aload_1
    //   2239: aload #11
    //   2241: putfield mTarget : Landroidx/fragment/app/Fragment;
    //   2244: iload_2
    //   2245: istore #6
    //   2247: goto -> 2273
    //   2250: aload_0
    //   2251: aload_1
    //   2252: invokevirtual makeInactive : (Landroidx/fragment/app/Fragment;)V
    //   2255: iload_2
    //   2256: istore #6
    //   2258: goto -> 2273
    //   2261: aload_1
    //   2262: iload_2
    //   2263: invokevirtual setStateAfterAnimating : (I)V
    //   2266: iload #7
    //   2268: istore #6
    //   2270: goto -> 2273
    //   2273: aload_1
    //   2274: getfield mState : I
    //   2277: iload #6
    //   2279: if_icmpeq -> 2360
    //   2282: new java/lang/StringBuilder
    //   2285: dup
    //   2286: invokespecial <init> : ()V
    //   2289: astore #11
    //   2291: aload #11
    //   2293: ldc_w 'moveToState: Fragment state for '
    //   2296: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2299: pop
    //   2300: aload #11
    //   2302: aload_1
    //   2303: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2306: pop
    //   2307: aload #11
    //   2309: ldc_w ' not updated inline; expected state '
    //   2312: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2315: pop
    //   2316: aload #11
    //   2318: iload #6
    //   2320: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2323: pop
    //   2324: aload #11
    //   2326: ldc_w ' found '
    //   2329: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2332: pop
    //   2333: aload #11
    //   2335: aload_1
    //   2336: getfield mState : I
    //   2339: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2342: pop
    //   2343: ldc 'FragmentManager'
    //   2345: aload #11
    //   2347: invokevirtual toString : ()Ljava/lang/String;
    //   2350: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   2353: pop
    //   2354: aload_1
    //   2355: iload #6
    //   2357: putfield mState : I
    //   2360: return
    //   2361: astore #11
    //   2363: goto -> 1060
    // Exception table:
    //   from	to	target	type
    //   1044	1057	2361	android/content/res/Resources$NotFoundException
  }
  
  public void noteStateNotSaved() {
    int i = 0;
    this.mStateSaved = false;
    this.mStopped = false;
    int j = this.mAdded.size();
    while (i < j) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.noteStateNotSaved(); 
      i++;
    } 
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    boolean bool = "fragment".equals(paramString);
    paramString = null;
    if (!bool)
      return null; 
    String str2 = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
    int i = 0;
    String str1 = str2;
    if (str2 == null)
      str1 = typedArray.getString(0); 
    int j = typedArray.getResourceId(1, -1);
    str2 = typedArray.getString(2);
    typedArray.recycle();
    if (str1 != null) {
      if (!FragmentFactory.isFragmentClass(paramContext.getClassLoader(), str1))
        return null; 
      if (paramView != null)
        i = paramView.getId(); 
      if (i != -1 || j != -1 || str2 != null) {
        if (j != -1)
          fragment2 = findFragmentById(j); 
        Fragment fragment1 = fragment2;
        if (fragment2 == null) {
          fragment1 = fragment2;
          if (str2 != null)
            fragment1 = findFragmentByTag(str2); 
        } 
        Fragment fragment2 = fragment1;
        if (fragment1 == null) {
          fragment2 = fragment1;
          if (i != -1)
            fragment2 = findFragmentById(i); 
        } 
        if (DEBUG) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("onCreateView: id=0x");
          stringBuilder2.append(Integer.toHexString(j));
          stringBuilder2.append(" fname=");
          stringBuilder2.append(str1);
          stringBuilder2.append(" existing=");
          stringBuilder2.append(fragment2);
          Log.v("FragmentManager", stringBuilder2.toString());
        } 
        if (fragment2 == null) {
          int k;
          fragment2 = getFragmentFactory().instantiate(paramContext.getClassLoader(), str1);
          fragment2.mFromLayout = true;
          if (j != 0) {
            k = j;
          } else {
            k = i;
          } 
          fragment2.mFragmentId = k;
          fragment2.mContainerId = i;
          fragment2.mTag = str2;
          fragment2.mInLayout = true;
          fragment2.mFragmentManager = this;
          fragment2.mHost = this.mHost;
          fragment2.onInflate(this.mHost.getContext(), paramAttributeSet, fragment2.mSavedFragmentState);
          addFragment(fragment2, true);
        } else if (!fragment2.mInLayout) {
          fragment2.mInLayout = true;
          fragment2.mHost = this.mHost;
          fragment2.onInflate(this.mHost.getContext(), paramAttributeSet, fragment2.mSavedFragmentState);
        } else {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append(paramAttributeSet.getPositionDescription());
          stringBuilder2.append(": Duplicate id 0x");
          stringBuilder2.append(Integer.toHexString(j));
          stringBuilder2.append(", tag ");
          stringBuilder2.append(str2);
          stringBuilder2.append(", or parent id 0x");
          stringBuilder2.append(Integer.toHexString(i));
          stringBuilder2.append(" with another fragment for ");
          stringBuilder2.append(str1);
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        if (this.mCurState < 1 && fragment2.mFromLayout) {
          moveToState(fragment2, 1, 0, 0, false);
        } else {
          moveToState(fragment2);
        } 
        if (fragment2.mView != null) {
          if (j != 0)
            fragment2.mView.setId(j); 
          if (fragment2.mView.getTag() == null)
            fragment2.mView.setTag(str2); 
          return fragment2.mView;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Fragment ");
        stringBuilder1.append(str1);
        stringBuilder1.append(" did not create a view.");
        throw new IllegalStateException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramAttributeSet.getPositionDescription());
      stringBuilder.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
      stringBuilder.append(str1);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    return null;
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return onCreateView((View)null, paramString, paramContext, paramAttributeSet);
  }
  
  public void performPendingDeferredStart(Fragment paramFragment) {
    if (paramFragment.mDeferStart) {
      if (this.mExecutingActions) {
        this.mHavePendingDeferredStart = true;
        return;
      } 
      paramFragment.mDeferStart = false;
      moveToState(paramFragment, this.mCurState, 0, 0, false);
    } 
  }
  
  public void popBackStack() {
    enqueueAction(new PopBackStackState(null, -1, 0), false);
  }
  
  public void popBackStack(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void popBackStack(String paramString, int paramInt) {
    enqueueAction(new PopBackStackState(paramString, -1, paramInt), false);
  }
  
  public boolean popBackStackImmediate() {
    checkStateLoss();
    return popBackStackImmediate((String)null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2) {
    checkStateLoss();
    execPendingActions();
    if (paramInt1 >= 0)
      return popBackStackImmediate((String)null, paramInt1, paramInt2); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt) {
    checkStateLoss();
    return popBackStackImmediate(paramString, -1, paramInt);
  }
  
  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, String paramString, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mBackStack : Ljava/util/ArrayList;
    //   4: astore #8
    //   6: aload #8
    //   8: ifnonnull -> 13
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_3
    //   14: ifnonnull -> 70
    //   17: iload #4
    //   19: ifge -> 70
    //   22: iload #5
    //   24: iconst_1
    //   25: iand
    //   26: ifne -> 70
    //   29: aload #8
    //   31: invokevirtual size : ()I
    //   34: iconst_1
    //   35: isub
    //   36: istore #4
    //   38: iload #4
    //   40: ifge -> 45
    //   43: iconst_0
    //   44: ireturn
    //   45: aload_1
    //   46: aload_0
    //   47: getfield mBackStack : Ljava/util/ArrayList;
    //   50: iload #4
    //   52: invokevirtual remove : (I)Ljava/lang/Object;
    //   55: invokevirtual add : (Ljava/lang/Object;)Z
    //   58: pop
    //   59: aload_2
    //   60: iconst_1
    //   61: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   64: invokevirtual add : (Ljava/lang/Object;)Z
    //   67: pop
    //   68: iconst_1
    //   69: ireturn
    //   70: aload_3
    //   71: ifnonnull -> 88
    //   74: iload #4
    //   76: iflt -> 82
    //   79: goto -> 88
    //   82: iconst_m1
    //   83: istore #4
    //   85: goto -> 265
    //   88: aload_0
    //   89: getfield mBackStack : Ljava/util/ArrayList;
    //   92: invokevirtual size : ()I
    //   95: iconst_1
    //   96: isub
    //   97: istore #6
    //   99: iload #6
    //   101: iflt -> 164
    //   104: aload_0
    //   105: getfield mBackStack : Ljava/util/ArrayList;
    //   108: iload #6
    //   110: invokevirtual get : (I)Ljava/lang/Object;
    //   113: checkcast androidx/fragment/app/BackStackRecord
    //   116: astore #8
    //   118: aload_3
    //   119: ifnull -> 137
    //   122: aload_3
    //   123: aload #8
    //   125: invokevirtual getName : ()Ljava/lang/String;
    //   128: invokevirtual equals : (Ljava/lang/Object;)Z
    //   131: ifeq -> 137
    //   134: goto -> 164
    //   137: iload #4
    //   139: iflt -> 155
    //   142: iload #4
    //   144: aload #8
    //   146: getfield mIndex : I
    //   149: if_icmpne -> 155
    //   152: goto -> 164
    //   155: iload #6
    //   157: iconst_1
    //   158: isub
    //   159: istore #6
    //   161: goto -> 99
    //   164: iload #6
    //   166: ifge -> 171
    //   169: iconst_0
    //   170: ireturn
    //   171: iload #6
    //   173: istore #7
    //   175: iload #5
    //   177: iconst_1
    //   178: iand
    //   179: ifeq -> 261
    //   182: iload #6
    //   184: iconst_1
    //   185: isub
    //   186: istore #5
    //   188: iload #5
    //   190: istore #7
    //   192: iload #5
    //   194: iflt -> 261
    //   197: aload_0
    //   198: getfield mBackStack : Ljava/util/ArrayList;
    //   201: iload #5
    //   203: invokevirtual get : (I)Ljava/lang/Object;
    //   206: checkcast androidx/fragment/app/BackStackRecord
    //   209: astore #8
    //   211: aload_3
    //   212: ifnull -> 231
    //   215: iload #5
    //   217: istore #6
    //   219: aload_3
    //   220: aload #8
    //   222: invokevirtual getName : ()Ljava/lang/String;
    //   225: invokevirtual equals : (Ljava/lang/Object;)Z
    //   228: ifne -> 182
    //   231: iload #5
    //   233: istore #7
    //   235: iload #4
    //   237: iflt -> 261
    //   240: iload #5
    //   242: istore #7
    //   244: iload #4
    //   246: aload #8
    //   248: getfield mIndex : I
    //   251: if_icmpne -> 261
    //   254: iload #5
    //   256: istore #6
    //   258: goto -> 182
    //   261: iload #7
    //   263: istore #4
    //   265: iload #4
    //   267: aload_0
    //   268: getfield mBackStack : Ljava/util/ArrayList;
    //   271: invokevirtual size : ()I
    //   274: iconst_1
    //   275: isub
    //   276: if_icmpne -> 281
    //   279: iconst_0
    //   280: ireturn
    //   281: aload_0
    //   282: getfield mBackStack : Ljava/util/ArrayList;
    //   285: invokevirtual size : ()I
    //   288: iconst_1
    //   289: isub
    //   290: istore #5
    //   292: iload #5
    //   294: iload #4
    //   296: if_icmple -> 331
    //   299: aload_1
    //   300: aload_0
    //   301: getfield mBackStack : Ljava/util/ArrayList;
    //   304: iload #5
    //   306: invokevirtual remove : (I)Ljava/lang/Object;
    //   309: invokevirtual add : (Ljava/lang/Object;)Z
    //   312: pop
    //   313: aload_2
    //   314: iconst_1
    //   315: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   318: invokevirtual add : (Ljava/lang/Object;)Z
    //   321: pop
    //   322: iload #5
    //   324: iconst_1
    //   325: isub
    //   326: istore #5
    //   328: goto -> 292
    //   331: iconst_1
    //   332: ireturn
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment) {
    if (paramFragment.mFragmentManager != this) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    paramBundle.putString(paramString, paramFragment.mWho);
  }
  
  public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean) {
    this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(paramFragmentLifecycleCallbacks, paramBoolean));
  }
  
  public void removeFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("remove: ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" nesting=");
      stringBuilder.append(paramFragment.mBackStackNesting);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    boolean bool = paramFragment.isInBackStack();
    if (!paramFragment.mDetached || (bool ^ true) != 0)
      synchronized (this.mAdded) {
        this.mAdded.remove(paramFragment);
        if (isMenuAvailable(paramFragment))
          this.mNeedMenuInvalidate = true; 
        paramFragment.mAdded = false;
        paramFragment.mRemoving = true;
        return;
      }  
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    ArrayList<FragmentManager.OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
    if (arrayList != null)
      arrayList.remove(paramOnBackStackChangedListener); 
  }
  
  void removeRetainedFragment(Fragment paramFragment) {
    if (isStateSaved()) {
      if (DEBUG)
        Log.v("FragmentManager", "Ignoring removeRetainedFragment as the state is already saved"); 
      return;
    } 
    if (this.mNonConfig.removeRetainedFragment(paramFragment) && DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Updating retained Fragments: Removed ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  void reportBackStackChanged() {
    if (this.mBackStackChangeListeners != null)
      for (int i = 0; i < this.mBackStackChangeListeners.size(); i++)
        ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(i)).onBackStackChanged();  
  }
  
  void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    if (this.mHost instanceof ViewModelStoreOwner)
      throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner")); 
    this.mNonConfig.restoreFromSnapshot(paramFragmentManagerNonConfig);
    restoreSaveState(paramParcelable);
  }
  
  void restoreSaveState(Parcelable paramParcelable) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 5
    //   4: return
    //   5: aload_1
    //   6: checkcast androidx/fragment/app/FragmentManagerState
    //   9: astore #4
    //   11: aload #4
    //   13: getfield mActive : Ljava/util/ArrayList;
    //   16: ifnonnull -> 20
    //   19: return
    //   20: aload_0
    //   21: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   24: invokevirtual getRetainedFragments : ()Ljava/util/Collection;
    //   27: invokeinterface iterator : ()Ljava/util/Iterator;
    //   32: astore #5
    //   34: aload #5
    //   36: invokeinterface hasNext : ()Z
    //   41: ifeq -> 347
    //   44: aload #5
    //   46: invokeinterface next : ()Ljava/lang/Object;
    //   51: checkcast androidx/fragment/app/Fragment
    //   54: astore #6
    //   56: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   59: ifeq -> 95
    //   62: new java/lang/StringBuilder
    //   65: dup
    //   66: invokespecial <init> : ()V
    //   69: astore_1
    //   70: aload_1
    //   71: ldc_w 'restoreSaveState: re-attaching retained '
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_1
    //   79: aload #6
    //   81: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: ldc 'FragmentManager'
    //   87: aload_1
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   94: pop
    //   95: aload #4
    //   97: getfield mActive : Ljava/util/ArrayList;
    //   100: invokevirtual iterator : ()Ljava/util/Iterator;
    //   103: astore_3
    //   104: aload_3
    //   105: invokeinterface hasNext : ()Z
    //   110: ifeq -> 141
    //   113: aload_3
    //   114: invokeinterface next : ()Ljava/lang/Object;
    //   119: checkcast androidx/fragment/app/FragmentState
    //   122: astore_1
    //   123: aload_1
    //   124: getfield mWho : Ljava/lang/String;
    //   127: aload #6
    //   129: getfield mWho : Ljava/lang/String;
    //   132: invokevirtual equals : (Ljava/lang/Object;)Z
    //   135: ifeq -> 104
    //   138: goto -> 143
    //   141: aconst_null
    //   142: astore_1
    //   143: aload_1
    //   144: ifnonnull -> 233
    //   147: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   150: ifeq -> 204
    //   153: new java/lang/StringBuilder
    //   156: dup
    //   157: invokespecial <init> : ()V
    //   160: astore_1
    //   161: aload_1
    //   162: ldc_w 'Discarding retained Fragment '
    //   165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: pop
    //   169: aload_1
    //   170: aload #6
    //   172: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   175: pop
    //   176: aload_1
    //   177: ldc_w ' that was not found in the set of active Fragments '
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: pop
    //   184: aload_1
    //   185: aload #4
    //   187: getfield mActive : Ljava/util/ArrayList;
    //   190: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: ldc 'FragmentManager'
    //   196: aload_1
    //   197: invokevirtual toString : ()Ljava/lang/String;
    //   200: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   203: pop
    //   204: aload_0
    //   205: aload #6
    //   207: iconst_1
    //   208: iconst_0
    //   209: iconst_0
    //   210: iconst_0
    //   211: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   214: aload #6
    //   216: iconst_1
    //   217: putfield mRemoving : Z
    //   220: aload_0
    //   221: aload #6
    //   223: iconst_0
    //   224: iconst_0
    //   225: iconst_0
    //   226: iconst_0
    //   227: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   230: goto -> 34
    //   233: aload_1
    //   234: aload #6
    //   236: putfield mInstance : Landroidx/fragment/app/Fragment;
    //   239: aload #6
    //   241: aconst_null
    //   242: putfield mSavedViewState : Landroid/util/SparseArray;
    //   245: aload #6
    //   247: iconst_0
    //   248: putfield mBackStackNesting : I
    //   251: aload #6
    //   253: iconst_0
    //   254: putfield mInLayout : Z
    //   257: aload #6
    //   259: iconst_0
    //   260: putfield mAdded : Z
    //   263: aload #6
    //   265: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   268: ifnull -> 283
    //   271: aload #6
    //   273: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   276: getfield mWho : Ljava/lang/String;
    //   279: astore_3
    //   280: goto -> 285
    //   283: aconst_null
    //   284: astore_3
    //   285: aload #6
    //   287: aload_3
    //   288: putfield mTargetWho : Ljava/lang/String;
    //   291: aload #6
    //   293: aconst_null
    //   294: putfield mTarget : Landroidx/fragment/app/Fragment;
    //   297: aload_1
    //   298: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   301: ifnull -> 34
    //   304: aload_1
    //   305: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   308: aload_0
    //   309: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   312: invokevirtual getContext : ()Landroid/content/Context;
    //   315: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   318: invokevirtual setClassLoader : (Ljava/lang/ClassLoader;)V
    //   321: aload #6
    //   323: aload_1
    //   324: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   327: ldc 'android:view_state'
    //   329: invokevirtual getSparseParcelableArray : (Ljava/lang/String;)Landroid/util/SparseArray;
    //   332: putfield mSavedViewState : Landroid/util/SparseArray;
    //   335: aload #6
    //   337: aload_1
    //   338: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   341: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   344: goto -> 34
    //   347: aload_0
    //   348: getfield mActive : Ljava/util/HashMap;
    //   351: invokevirtual clear : ()V
    //   354: aload #4
    //   356: getfield mActive : Ljava/util/ArrayList;
    //   359: invokevirtual iterator : ()Ljava/util/Iterator;
    //   362: astore_1
    //   363: aload_1
    //   364: invokeinterface hasNext : ()Z
    //   369: ifeq -> 498
    //   372: aload_1
    //   373: invokeinterface next : ()Ljava/lang/Object;
    //   378: checkcast androidx/fragment/app/FragmentState
    //   381: astore_3
    //   382: aload_3
    //   383: ifnull -> 363
    //   386: aload_3
    //   387: aload_0
    //   388: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   391: invokevirtual getContext : ()Landroid/content/Context;
    //   394: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   397: aload_0
    //   398: invokevirtual getFragmentFactory : ()Landroidx/fragment/app/FragmentFactory;
    //   401: invokevirtual instantiate : (Ljava/lang/ClassLoader;Landroidx/fragment/app/FragmentFactory;)Landroidx/fragment/app/Fragment;
    //   404: astore #5
    //   406: aload #5
    //   408: aload_0
    //   409: putfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   412: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   415: ifeq -> 475
    //   418: new java/lang/StringBuilder
    //   421: dup
    //   422: invokespecial <init> : ()V
    //   425: astore #6
    //   427: aload #6
    //   429: ldc_w 'restoreSaveState: active ('
    //   432: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   435: pop
    //   436: aload #6
    //   438: aload #5
    //   440: getfield mWho : Ljava/lang/String;
    //   443: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   446: pop
    //   447: aload #6
    //   449: ldc_w '): '
    //   452: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   455: pop
    //   456: aload #6
    //   458: aload #5
    //   460: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   463: pop
    //   464: ldc 'FragmentManager'
    //   466: aload #6
    //   468: invokevirtual toString : ()Ljava/lang/String;
    //   471: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   474: pop
    //   475: aload_0
    //   476: getfield mActive : Ljava/util/HashMap;
    //   479: aload #5
    //   481: getfield mWho : Ljava/lang/String;
    //   484: aload #5
    //   486: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   489: pop
    //   490: aload_3
    //   491: aconst_null
    //   492: putfield mInstance : Landroidx/fragment/app/Fragment;
    //   495: goto -> 363
    //   498: aload_0
    //   499: getfield mAdded : Ljava/util/ArrayList;
    //   502: invokevirtual clear : ()V
    //   505: aload #4
    //   507: getfield mAdded : Ljava/util/ArrayList;
    //   510: ifnull -> 749
    //   513: aload #4
    //   515: getfield mAdded : Ljava/util/ArrayList;
    //   518: invokevirtual iterator : ()Ljava/util/Iterator;
    //   521: astore_3
    //   522: aload_3
    //   523: invokeinterface hasNext : ()Z
    //   528: ifeq -> 749
    //   531: aload_3
    //   532: invokeinterface next : ()Ljava/lang/Object;
    //   537: checkcast java/lang/String
    //   540: astore #5
    //   542: aload_0
    //   543: getfield mActive : Ljava/util/HashMap;
    //   546: aload #5
    //   548: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   551: checkcast androidx/fragment/app/Fragment
    //   554: astore_1
    //   555: aload_1
    //   556: ifnonnull -> 610
    //   559: new java/lang/StringBuilder
    //   562: dup
    //   563: invokespecial <init> : ()V
    //   566: astore #6
    //   568: aload #6
    //   570: ldc_w 'No instantiated fragment for ('
    //   573: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   576: pop
    //   577: aload #6
    //   579: aload #5
    //   581: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   584: pop
    //   585: aload #6
    //   587: ldc_w ')'
    //   590: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   593: pop
    //   594: aload_0
    //   595: new java/lang/IllegalStateException
    //   598: dup
    //   599: aload #6
    //   601: invokevirtual toString : ()Ljava/lang/String;
    //   604: invokespecial <init> : (Ljava/lang/String;)V
    //   607: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   610: aload_1
    //   611: iconst_1
    //   612: putfield mAdded : Z
    //   615: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   618: ifeq -> 674
    //   621: new java/lang/StringBuilder
    //   624: dup
    //   625: invokespecial <init> : ()V
    //   628: astore #6
    //   630: aload #6
    //   632: ldc_w 'restoreSaveState: added ('
    //   635: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   638: pop
    //   639: aload #6
    //   641: aload #5
    //   643: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   646: pop
    //   647: aload #6
    //   649: ldc_w '): '
    //   652: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   655: pop
    //   656: aload #6
    //   658: aload_1
    //   659: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   662: pop
    //   663: ldc 'FragmentManager'
    //   665: aload #6
    //   667: invokevirtual toString : ()Ljava/lang/String;
    //   670: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   673: pop
    //   674: aload_0
    //   675: getfield mAdded : Ljava/util/ArrayList;
    //   678: aload_1
    //   679: invokevirtual contains : (Ljava/lang/Object;)Z
    //   682: ifne -> 715
    //   685: aload_0
    //   686: getfield mAdded : Ljava/util/ArrayList;
    //   689: astore #5
    //   691: aload #5
    //   693: monitorenter
    //   694: aload_0
    //   695: getfield mAdded : Ljava/util/ArrayList;
    //   698: aload_1
    //   699: invokevirtual add : (Ljava/lang/Object;)Z
    //   702: pop
    //   703: aload #5
    //   705: monitorexit
    //   706: goto -> 522
    //   709: astore_1
    //   710: aload #5
    //   712: monitorexit
    //   713: aload_1
    //   714: athrow
    //   715: new java/lang/StringBuilder
    //   718: dup
    //   719: invokespecial <init> : ()V
    //   722: astore_3
    //   723: aload_3
    //   724: ldc_w 'Already added '
    //   727: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   730: pop
    //   731: aload_3
    //   732: aload_1
    //   733: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   736: pop
    //   737: new java/lang/IllegalStateException
    //   740: dup
    //   741: aload_3
    //   742: invokevirtual toString : ()Ljava/lang/String;
    //   745: invokespecial <init> : (Ljava/lang/String;)V
    //   748: athrow
    //   749: aload #4
    //   751: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   754: ifnull -> 929
    //   757: aload_0
    //   758: new java/util/ArrayList
    //   761: dup
    //   762: aload #4
    //   764: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   767: arraylength
    //   768: invokespecial <init> : (I)V
    //   771: putfield mBackStack : Ljava/util/ArrayList;
    //   774: iconst_0
    //   775: istore_2
    //   776: iload_2
    //   777: aload #4
    //   779: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   782: arraylength
    //   783: if_icmpge -> 934
    //   786: aload #4
    //   788: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   791: iload_2
    //   792: aaload
    //   793: aload_0
    //   794: invokevirtual instantiate : (Landroidx/fragment/app/FragmentManagerImpl;)Landroidx/fragment/app/BackStackRecord;
    //   797: astore_1
    //   798: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   801: ifeq -> 897
    //   804: new java/lang/StringBuilder
    //   807: dup
    //   808: invokespecial <init> : ()V
    //   811: astore_3
    //   812: aload_3
    //   813: ldc_w 'restoreAllState: back stack #'
    //   816: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   819: pop
    //   820: aload_3
    //   821: iload_2
    //   822: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   825: pop
    //   826: aload_3
    //   827: ldc_w ' (index '
    //   830: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   833: pop
    //   834: aload_3
    //   835: aload_1
    //   836: getfield mIndex : I
    //   839: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   842: pop
    //   843: aload_3
    //   844: ldc_w '): '
    //   847: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   850: pop
    //   851: aload_3
    //   852: aload_1
    //   853: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   856: pop
    //   857: ldc 'FragmentManager'
    //   859: aload_3
    //   860: invokevirtual toString : ()Ljava/lang/String;
    //   863: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   866: pop
    //   867: new java/io/PrintWriter
    //   870: dup
    //   871: new androidx/core/util/LogWriter
    //   874: dup
    //   875: ldc 'FragmentManager'
    //   877: invokespecial <init> : (Ljava/lang/String;)V
    //   880: invokespecial <init> : (Ljava/io/Writer;)V
    //   883: astore_3
    //   884: aload_1
    //   885: ldc_w '  '
    //   888: aload_3
    //   889: iconst_0
    //   890: invokevirtual dump : (Ljava/lang/String;Ljava/io/PrintWriter;Z)V
    //   893: aload_3
    //   894: invokevirtual close : ()V
    //   897: aload_0
    //   898: getfield mBackStack : Ljava/util/ArrayList;
    //   901: aload_1
    //   902: invokevirtual add : (Ljava/lang/Object;)Z
    //   905: pop
    //   906: aload_1
    //   907: getfield mIndex : I
    //   910: iflt -> 922
    //   913: aload_0
    //   914: aload_1
    //   915: getfield mIndex : I
    //   918: aload_1
    //   919: invokevirtual setBackStackIndex : (ILandroidx/fragment/app/BackStackRecord;)V
    //   922: iload_2
    //   923: iconst_1
    //   924: iadd
    //   925: istore_2
    //   926: goto -> 776
    //   929: aload_0
    //   930: aconst_null
    //   931: putfield mBackStack : Ljava/util/ArrayList;
    //   934: aload #4
    //   936: getfield mPrimaryNavActiveWho : Ljava/lang/String;
    //   939: ifnull -> 968
    //   942: aload_0
    //   943: getfield mActive : Ljava/util/HashMap;
    //   946: aload #4
    //   948: getfield mPrimaryNavActiveWho : Ljava/lang/String;
    //   951: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   954: checkcast androidx/fragment/app/Fragment
    //   957: astore_1
    //   958: aload_0
    //   959: aload_1
    //   960: putfield mPrimaryNav : Landroidx/fragment/app/Fragment;
    //   963: aload_0
    //   964: aload_1
    //   965: invokespecial dispatchParentPrimaryNavigationFragmentChanged : (Landroidx/fragment/app/Fragment;)V
    //   968: aload_0
    //   969: aload #4
    //   971: getfield mNextFragmentIndex : I
    //   974: putfield mNextFragmentIndex : I
    //   977: return
    // Exception table:
    //   from	to	target	type
    //   694	706	709	finally
    //   710	713	709	finally
  }
  
  @Deprecated
  FragmentManagerNonConfig retainNonConfig() {
    if (this.mHost instanceof ViewModelStoreOwner)
      throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner.")); 
    return this.mNonConfig.getSnapshot();
  }
  
  Parcelable saveAllState() {
    StringBuilder stringBuilder;
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions();
    this.mStateSaved = true;
    boolean bool1 = this.mActive.isEmpty();
    BackStackState[] arrayOfBackStackState2 = null;
    if (bool1)
      return null; 
    ArrayList<FragmentState> arrayList = new ArrayList(this.mActive.size());
    Iterator<Fragment> iterator = this.mActive.values().iterator();
    boolean bool = false;
    int i = 0;
    while (iterator.hasNext()) {
      Fragment fragment1 = iterator.next();
      if (fragment1 != null) {
        if (fragment1.mFragmentManager != this) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Failure saving state: active ");
          stringBuilder1.append(fragment1);
          stringBuilder1.append(" was removed from the FragmentManager");
          throwException(new IllegalStateException(stringBuilder1.toString()));
        } 
        FragmentState fragmentState = new FragmentState(fragment1);
        arrayList.add(fragmentState);
        if (fragment1.mState > 0 && fragmentState.mSavedFragmentState == null) {
          fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment1);
          if (fragment1.mTargetWho != null) {
            Fragment fragment2 = this.mActive.get(fragment1.mTargetWho);
            if (fragment2 == null) {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Failure saving state: ");
              stringBuilder1.append(fragment1);
              stringBuilder1.append(" has target not in fragment manager: ");
              stringBuilder1.append(fragment1.mTargetWho);
              throwException(new IllegalStateException(stringBuilder1.toString()));
            } 
            if (fragmentState.mSavedFragmentState == null)
              fragmentState.mSavedFragmentState = new Bundle(); 
            putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment2);
            if (fragment1.mTargetRequestCode != 0)
              fragmentState.mSavedFragmentState.putInt("android:target_req_state", fragment1.mTargetRequestCode); 
          } 
        } else {
          fragmentState.mSavedFragmentState = fragment1.mSavedFragmentState;
        } 
        if (DEBUG) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Saved state of ");
          stringBuilder1.append(fragment1);
          stringBuilder1.append(": ");
          stringBuilder1.append(fragmentState.mSavedFragmentState);
          Log.v("FragmentManager", stringBuilder1.toString());
        } 
        i = 1;
      } 
    } 
    if (!i) {
      if (DEBUG)
        Log.v("FragmentManager", "saveAllState: no fragments!"); 
      return null;
    } 
    i = this.mAdded.size();
    if (i > 0) {
      ArrayList<String> arrayList2 = new ArrayList(i);
      Iterator<Fragment> iterator1 = this.mAdded.iterator();
      while (true) {
        ArrayList<String> arrayList3 = arrayList2;
        if (iterator1.hasNext()) {
          Fragment fragment1 = iterator1.next();
          arrayList2.add(fragment1.mWho);
          if (fragment1.mFragmentManager != this) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("Failure saving state: active ");
            stringBuilder1.append(fragment1);
            stringBuilder1.append(" was removed from the FragmentManager");
            throwException(new IllegalStateException(stringBuilder1.toString()));
          } 
          if (DEBUG) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("saveAllState: adding fragment (");
            stringBuilder1.append(fragment1.mWho);
            stringBuilder1.append("): ");
            stringBuilder1.append(fragment1);
            Log.v("FragmentManager", stringBuilder1.toString());
          } 
          continue;
        } 
        break;
      } 
    } else {
      iterator = null;
    } 
    ArrayList<BackStackRecord> arrayList1 = this.mBackStack;
    BackStackState[] arrayOfBackStackState1 = arrayOfBackStackState2;
    if (arrayList1 != null) {
      int j = arrayList1.size();
      arrayOfBackStackState1 = arrayOfBackStackState2;
      if (j > 0) {
        arrayOfBackStackState2 = new BackStackState[j];
        i = bool;
        while (true) {
          arrayOfBackStackState1 = arrayOfBackStackState2;
          if (i < j) {
            arrayOfBackStackState2[i] = new BackStackState(this.mBackStack.get(i));
            if (DEBUG) {
              stringBuilder = new StringBuilder();
              stringBuilder.append("saveAllState: adding back stack #");
              stringBuilder.append(i);
              stringBuilder.append(": ");
              stringBuilder.append(this.mBackStack.get(i));
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            i++;
            continue;
          } 
          break;
        } 
      } 
    } 
    FragmentManagerState fragmentManagerState = new FragmentManagerState();
    fragmentManagerState.mActive = arrayList;
    fragmentManagerState.mAdded = (ArrayList)iterator;
    fragmentManagerState.mBackStack = (BackStackState[])stringBuilder;
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null)
      fragmentManagerState.mPrimaryNavActiveWho = fragment.mWho; 
    fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
    return fragmentManagerState;
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment) {
    if (this.mStateBundle == null)
      this.mStateBundle = new Bundle(); 
    paramFragment.performSaveInstanceState(this.mStateBundle);
    dispatchOnFragmentSaveInstanceState(paramFragment, this.mStateBundle, false);
    boolean bool = this.mStateBundle.isEmpty();
    Bundle bundle2 = null;
    if (!bool) {
      bundle2 = this.mStateBundle;
      this.mStateBundle = null;
    } 
    if (paramFragment.mView != null)
      saveFragmentViewState(paramFragment); 
    Bundle bundle1 = bundle2;
    if (paramFragment.mSavedViewState != null) {
      bundle1 = bundle2;
      if (bundle2 == null)
        bundle1 = new Bundle(); 
      bundle1.putSparseParcelableArray("android:view_state", paramFragment.mSavedViewState);
    } 
    bundle2 = bundle1;
    if (!paramFragment.mUserVisibleHint) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putBoolean("android:user_visible_hint", paramFragment.mUserVisibleHint);
    } 
    return bundle2;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment) {
    if (paramFragment.mFragmentManager != this) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    int i = paramFragment.mState;
    Fragment.SavedState savedState2 = null;
    Fragment.SavedState savedState1 = savedState2;
    if (i > 0) {
      Bundle bundle = saveFragmentBasicState(paramFragment);
      savedState1 = savedState2;
      if (bundle != null)
        savedState1 = new Fragment.SavedState(bundle); 
    } 
    return savedState1;
  }
  
  void saveFragmentViewState(Fragment paramFragment) {
    if (paramFragment.mInnerView == null)
      return; 
    SparseArray<Parcelable> sparseArray = this.mStateArray;
    if (sparseArray == null) {
      this.mStateArray = new SparseArray();
    } else {
      sparseArray.clear();
    } 
    paramFragment.mInnerView.saveHierarchyState(this.mStateArray);
    if (this.mStateArray.size() > 0) {
      paramFragment.mSavedViewState = this.mStateArray;
      this.mStateArray = null;
    } 
  }
  
  void scheduleCommit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   6: astore #4
    //   8: iconst_0
    //   9: istore_3
    //   10: aload #4
    //   12: ifnull -> 100
    //   15: aload_0
    //   16: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   19: invokevirtual isEmpty : ()Z
    //   22: ifne -> 100
    //   25: iconst_1
    //   26: istore_1
    //   27: goto -> 30
    //   30: iload_3
    //   31: istore_2
    //   32: aload_0
    //   33: getfield mPendingActions : Ljava/util/ArrayList;
    //   36: ifnull -> 105
    //   39: iload_3
    //   40: istore_2
    //   41: aload_0
    //   42: getfield mPendingActions : Ljava/util/ArrayList;
    //   45: invokevirtual size : ()I
    //   48: iconst_1
    //   49: if_icmpne -> 105
    //   52: iconst_1
    //   53: istore_2
    //   54: goto -> 105
    //   57: aload_0
    //   58: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   61: invokevirtual getHandler : ()Landroid/os/Handler;
    //   64: aload_0
    //   65: getfield mExecCommit : Ljava/lang/Runnable;
    //   68: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   71: aload_0
    //   72: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   75: invokevirtual getHandler : ()Landroid/os/Handler;
    //   78: aload_0
    //   79: getfield mExecCommit : Ljava/lang/Runnable;
    //   82: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   85: pop
    //   86: aload_0
    //   87: invokespecial updateOnBackPressedCallbackEnabled : ()V
    //   90: aload_0
    //   91: monitorexit
    //   92: return
    //   93: astore #4
    //   95: aload_0
    //   96: monitorexit
    //   97: aload #4
    //   99: athrow
    //   100: iconst_0
    //   101: istore_1
    //   102: goto -> 30
    //   105: iload_1
    //   106: ifne -> 57
    //   109: iload_2
    //   110: ifeq -> 90
    //   113: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   2	8	93	finally
    //   15	25	93	finally
    //   32	39	93	finally
    //   41	52	93	finally
    //   57	90	93	finally
    //   90	92	93	finally
    //   95	97	93	finally
  }
  
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnonnull -> 20
    //   9: aload_0
    //   10: new java/util/ArrayList
    //   13: dup
    //   14: invokespecial <init> : ()V
    //   17: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   20: aload_0
    //   21: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   24: invokevirtual size : ()I
    //   27: istore #4
    //   29: iload #4
    //   31: istore_3
    //   32: iload_1
    //   33: iload #4
    //   35: if_icmpge -> 109
    //   38: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   41: ifeq -> 96
    //   44: new java/lang/StringBuilder
    //   47: dup
    //   48: invokespecial <init> : ()V
    //   51: astore #5
    //   53: aload #5
    //   55: ldc_w 'Setting back stack index '
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: aload #5
    //   64: iload_1
    //   65: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload #5
    //   71: ldc_w ' to '
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload #5
    //   80: aload_2
    //   81: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: ldc 'FragmentManager'
    //   87: aload #5
    //   89: invokevirtual toString : ()Ljava/lang/String;
    //   92: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   95: pop
    //   96: aload_0
    //   97: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   100: iload_1
    //   101: aload_2
    //   102: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   105: pop
    //   106: goto -> 269
    //   109: iload_3
    //   110: iload_1
    //   111: if_icmpge -> 202
    //   114: aload_0
    //   115: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   118: aconst_null
    //   119: invokevirtual add : (Ljava/lang/Object;)Z
    //   122: pop
    //   123: aload_0
    //   124: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   127: ifnonnull -> 141
    //   130: aload_0
    //   131: new java/util/ArrayList
    //   134: dup
    //   135: invokespecial <init> : ()V
    //   138: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   141: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   144: ifeq -> 183
    //   147: new java/lang/StringBuilder
    //   150: dup
    //   151: invokespecial <init> : ()V
    //   154: astore #5
    //   156: aload #5
    //   158: ldc_w 'Adding available back stack index '
    //   161: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: aload #5
    //   167: iload_3
    //   168: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: ldc 'FragmentManager'
    //   174: aload #5
    //   176: invokevirtual toString : ()Ljava/lang/String;
    //   179: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   182: pop
    //   183: aload_0
    //   184: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   187: iload_3
    //   188: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   191: invokevirtual add : (Ljava/lang/Object;)Z
    //   194: pop
    //   195: iload_3
    //   196: iconst_1
    //   197: iadd
    //   198: istore_3
    //   199: goto -> 109
    //   202: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   205: ifeq -> 260
    //   208: new java/lang/StringBuilder
    //   211: dup
    //   212: invokespecial <init> : ()V
    //   215: astore #5
    //   217: aload #5
    //   219: ldc_w 'Adding back stack index '
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: pop
    //   226: aload #5
    //   228: iload_1
    //   229: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   232: pop
    //   233: aload #5
    //   235: ldc_w ' with '
    //   238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: pop
    //   242: aload #5
    //   244: aload_2
    //   245: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   248: pop
    //   249: ldc 'FragmentManager'
    //   251: aload #5
    //   253: invokevirtual toString : ()Ljava/lang/String;
    //   256: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   259: pop
    //   260: aload_0
    //   261: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   264: aload_2
    //   265: invokevirtual add : (Ljava/lang/Object;)Z
    //   268: pop
    //   269: aload_0
    //   270: monitorexit
    //   271: return
    //   272: astore_2
    //   273: aload_0
    //   274: monitorexit
    //   275: aload_2
    //   276: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	272	finally
    //   20	29	272	finally
    //   38	96	272	finally
    //   96	106	272	finally
    //   114	141	272	finally
    //   141	183	272	finally
    //   183	195	272	finally
    //   202	260	272	finally
    //   260	269	272	finally
    //   269	271	272	finally
    //   273	275	272	finally
  }
  
  public void setMaxLifecycle(Fragment paramFragment, Lifecycle.State paramState) {
    if (this.mActive.get(paramFragment.mWho) == paramFragment && (paramFragment.mHost == null || paramFragment.getFragmentManager() == this)) {
      paramFragment.mMaxState = paramState;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Fragment ");
    stringBuilder.append(paramFragment);
    stringBuilder.append(" is not an active fragment of FragmentManager ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setPrimaryNavigationFragment(Fragment paramFragment) {
    if (paramFragment == null || (this.mActive.get(paramFragment.mWho) == paramFragment && (paramFragment.mHost == null || paramFragment.getFragmentManager() == this))) {
      Fragment fragment = this.mPrimaryNav;
      this.mPrimaryNav = paramFragment;
      dispatchParentPrimaryNavigationFragmentChanged(fragment);
      dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Fragment ");
    stringBuilder.append(paramFragment);
    stringBuilder.append(" is not an active fragment of FragmentManager ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void showFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("show: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mHidden) {
      paramFragment.mHidden = false;
      paramFragment.mHiddenChanged ^= 0x1;
    } 
  }
  
  void startPendingDeferredFragments() {
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null)
        performPendingDeferredStart(fragment); 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("FragmentManager{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" in ");
    Fragment fragment = this.mParent;
    if (fragment != null) {
      DebugUtils.buildShortClassTag(fragment, stringBuilder);
    } else {
      DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
    } 
    stringBuilder.append("}}");
    return stringBuilder.toString();
  }
  
  public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: iconst_0
    //   10: istore_2
    //   11: aload_0
    //   12: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   15: invokevirtual size : ()I
    //   18: istore_3
    //   19: iload_2
    //   20: iload_3
    //   21: if_icmpge -> 54
    //   24: aload_0
    //   25: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   28: iload_2
    //   29: invokevirtual get : (I)Ljava/lang/Object;
    //   32: checkcast androidx/fragment/app/FragmentManagerImpl$FragmentLifecycleCallbacksHolder
    //   35: getfield mCallback : Landroidx/fragment/app/FragmentManager$FragmentLifecycleCallbacks;
    //   38: aload_1
    //   39: if_acmpne -> 64
    //   42: aload_0
    //   43: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   46: iload_2
    //   47: invokevirtual remove : (I)Ljava/lang/Object;
    //   50: pop
    //   51: goto -> 54
    //   54: aload #4
    //   56: monitorexit
    //   57: return
    //   58: astore_1
    //   59: aload #4
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    //   64: iload_2
    //   65: iconst_1
    //   66: iadd
    //   67: istore_2
    //   68: goto -> 19
    // Exception table:
    //   from	to	target	type
    //   11	19	58	finally
    //   24	51	58	finally
    //   54	57	58	finally
    //   59	62	58	finally
  }
  
  private static class AnimationOrAnimator {
    public final Animation animation = null;
    
    public final Animator animator;
    
    AnimationOrAnimator(Animator param1Animator) {
      this.animator = param1Animator;
      if (param1Animator != null)
        return; 
      throw new IllegalStateException("Animator cannot be null");
    }
    
    AnimationOrAnimator(Animation param1Animation) {
      this.animator = null;
      if (param1Animation != null)
        return; 
      throw new IllegalStateException("Animation cannot be null");
    }
  }
  
  private static class EndViewTransitionAnimation extends AnimationSet implements Runnable {
    private boolean mAnimating = true;
    
    private final View mChild;
    
    private boolean mEnded;
    
    private final ViewGroup mParent;
    
    private boolean mTransitionEnded;
    
    EndViewTransitionAnimation(Animation param1Animation, ViewGroup param1ViewGroup, View param1View) {
      super(false);
      this.mParent = param1ViewGroup;
      this.mChild = param1View;
      addAnimation(param1Animation);
      this.mParent.post(this);
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation) {
      this.mAnimating = true;
      if (this.mEnded)
        return this.mTransitionEnded ^ true; 
      if (!super.getTransformation(param1Long, param1Transformation)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation, float param1Float) {
      this.mAnimating = true;
      if (this.mEnded)
        return this.mTransitionEnded ^ true; 
      if (!super.getTransformation(param1Long, param1Transformation, param1Float)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public void run() {
      if (!this.mEnded && this.mAnimating) {
        this.mAnimating = false;
        this.mParent.post(this);
        return;
      } 
      this.mParent.endViewTransition(this.mChild);
      this.mTransitionEnded = true;
    }
  }
  
  private static final class FragmentLifecycleCallbacksHolder {
    final FragmentManager.FragmentLifecycleCallbacks mCallback;
    
    final boolean mRecursive;
    
    FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks param1FragmentLifecycleCallbacks, boolean param1Boolean) {
      this.mCallback = param1FragmentLifecycleCallbacks;
      this.mRecursive = param1Boolean;
    }
  }
  
  static class FragmentTag {
    public static final int[] Fragment = new int[] { 16842755, 16842960, 16842961 };
    
    public static final int Fragment_id = 1;
    
    public static final int Fragment_name = 0;
    
    public static final int Fragment_tag = 2;
  }
  
  static interface OpGenerator {
    boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1);
  }
  
  private class PopBackStackState implements OpGenerator {
    final int mFlags;
    
    final int mId;
    
    final String mName;
    
    PopBackStackState(String param1String, int param1Int1, int param1Int2) {
      this.mName = param1String;
      this.mId = param1Int1;
      this.mFlags = param1Int2;
    }
    
    public boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1) {
      return (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && FragmentManagerImpl.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) ? false : FragmentManagerImpl.this.popBackStackState(param1ArrayList, param1ArrayList1, this.mName, this.mId, this.mFlags);
    }
  }
  
  static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
    final boolean mIsBack;
    
    private int mNumPostponed;
    
    final BackStackRecord mRecord;
    
    StartEnterTransitionListener(BackStackRecord param1BackStackRecord, boolean param1Boolean) {
      this.mIsBack = param1Boolean;
      this.mRecord = param1BackStackRecord;
    }
    
    public void cancelTransaction() {
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
    }
    
    public void completeTransaction() {
      int i = this.mNumPostponed;
      int j = 0;
      if (i > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
      int k = fragmentManagerImpl.mAdded.size();
      while (j < k) {
        Fragment fragment = fragmentManagerImpl.mAdded.get(j);
        fragment.setOnStartEnterTransitionListener(null);
        if (i != 0 && fragment.isPostponed())
          fragment.startPostponedEnterTransition(); 
        j++;
      } 
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, i ^ 0x1, true);
    }
    
    public boolean isReady() {
      return (this.mNumPostponed == 0);
    }
    
    public void onStartEnterTransition() {
      int i = this.mNumPostponed - 1;
      this.mNumPostponed = i;
      if (i != 0)
        return; 
      this.mRecord.mManager.scheduleCommit();
    }
    
    public void startListening() {
      this.mNumPostponed++;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\fragment\app\FragmentManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
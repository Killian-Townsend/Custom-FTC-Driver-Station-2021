package androidx.fragment.app;

import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

class FragmentTransition {
  private static final int[] INVERSE_OPS = new int[] { 
      0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 
      10 };
  
  private static final FragmentTransitionImpl PLATFORM_IMPL;
  
  private static final FragmentTransitionImpl SUPPORT_IMPL = resolveSupportImpl();
  
  private static void addSharedElementsWithMatchingNames(ArrayList<View> paramArrayList, ArrayMap<String, View> paramArrayMap, Collection<String> paramCollection) {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      View view = (View)paramArrayMap.valueAt(i);
      if (paramCollection.contains(ViewCompat.getTransitionName(view)))
        paramArrayList.add(view); 
    } 
  }
  
  private static void addToFirstInLastOut(BackStackRecord paramBackStackRecord, FragmentTransaction.Op paramOp, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mFragment : Landroidx/fragment/app/Fragment;
    //   4: astore #11
    //   6: aload #11
    //   8: ifnonnull -> 12
    //   11: return
    //   12: aload #11
    //   14: getfield mContainerId : I
    //   17: istore #8
    //   19: iload #8
    //   21: ifne -> 25
    //   24: return
    //   25: iload_3
    //   26: ifeq -> 42
    //   29: getstatic androidx/fragment/app/FragmentTransition.INVERSE_OPS : [I
    //   32: aload_1
    //   33: getfield mCmd : I
    //   36: iaload
    //   37: istore #5
    //   39: goto -> 48
    //   42: aload_1
    //   43: getfield mCmd : I
    //   46: istore #5
    //   48: iconst_0
    //   49: istore #9
    //   51: iload #5
    //   53: iconst_1
    //   54: if_icmpeq -> 282
    //   57: iload #5
    //   59: iconst_3
    //   60: if_icmpeq -> 196
    //   63: iload #5
    //   65: iconst_4
    //   66: if_icmpeq -> 145
    //   69: iload #5
    //   71: iconst_5
    //   72: if_icmpeq -> 103
    //   75: iload #5
    //   77: bipush #6
    //   79: if_icmpeq -> 196
    //   82: iload #5
    //   84: bipush #7
    //   86: if_icmpeq -> 282
    //   89: iconst_0
    //   90: istore #5
    //   92: iload #5
    //   94: istore #6
    //   96: iload #6
    //   98: istore #7
    //   100: goto -> 332
    //   103: iload #4
    //   105: ifeq -> 135
    //   108: aload #11
    //   110: getfield mHiddenChanged : Z
    //   113: ifeq -> 319
    //   116: aload #11
    //   118: getfield mHidden : Z
    //   121: ifne -> 319
    //   124: aload #11
    //   126: getfield mAdded : Z
    //   129: ifeq -> 319
    //   132: goto -> 313
    //   135: aload #11
    //   137: getfield mHidden : Z
    //   140: istore #9
    //   142: goto -> 322
    //   145: iload #4
    //   147: ifeq -> 177
    //   150: aload #11
    //   152: getfield mHiddenChanged : Z
    //   155: ifeq -> 244
    //   158: aload #11
    //   160: getfield mAdded : Z
    //   163: ifeq -> 244
    //   166: aload #11
    //   168: getfield mHidden : Z
    //   171: ifeq -> 244
    //   174: goto -> 238
    //   177: aload #11
    //   179: getfield mAdded : Z
    //   182: ifeq -> 244
    //   185: aload #11
    //   187: getfield mHidden : Z
    //   190: ifne -> 244
    //   193: goto -> 174
    //   196: iload #4
    //   198: ifeq -> 250
    //   201: aload #11
    //   203: getfield mAdded : Z
    //   206: ifne -> 244
    //   209: aload #11
    //   211: getfield mView : Landroid/view/View;
    //   214: ifnull -> 244
    //   217: aload #11
    //   219: getfield mView : Landroid/view/View;
    //   222: invokevirtual getVisibility : ()I
    //   225: ifne -> 244
    //   228: aload #11
    //   230: getfield mPostponedAlpha : F
    //   233: fconst_0
    //   234: fcmpl
    //   235: iflt -> 244
    //   238: iconst_1
    //   239: istore #5
    //   241: goto -> 269
    //   244: iconst_0
    //   245: istore #5
    //   247: goto -> 269
    //   250: aload #11
    //   252: getfield mAdded : Z
    //   255: ifeq -> 244
    //   258: aload #11
    //   260: getfield mHidden : Z
    //   263: ifne -> 244
    //   266: goto -> 238
    //   269: iload #5
    //   271: istore #7
    //   273: iconst_0
    //   274: istore #5
    //   276: iconst_1
    //   277: istore #6
    //   279: goto -> 332
    //   282: iload #4
    //   284: ifeq -> 297
    //   287: aload #11
    //   289: getfield mIsNewlyAdded : Z
    //   292: istore #9
    //   294: goto -> 322
    //   297: aload #11
    //   299: getfield mAdded : Z
    //   302: ifne -> 319
    //   305: aload #11
    //   307: getfield mHidden : Z
    //   310: ifne -> 319
    //   313: iconst_1
    //   314: istore #9
    //   316: goto -> 322
    //   319: iconst_0
    //   320: istore #9
    //   322: iconst_0
    //   323: istore #6
    //   325: iload #6
    //   327: istore #7
    //   329: iconst_1
    //   330: istore #5
    //   332: aload_2
    //   333: iload #8
    //   335: invokevirtual get : (I)Ljava/lang/Object;
    //   338: checkcast androidx/fragment/app/FragmentTransition$FragmentContainerTransition
    //   341: astore #10
    //   343: aload #10
    //   345: astore_1
    //   346: iload #9
    //   348: ifeq -> 376
    //   351: aload #10
    //   353: aload_2
    //   354: iload #8
    //   356: invokestatic ensureContainer : (Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;
    //   359: astore_1
    //   360: aload_1
    //   361: aload #11
    //   363: putfield lastIn : Landroidx/fragment/app/Fragment;
    //   366: aload_1
    //   367: iload_3
    //   368: putfield lastInIsPop : Z
    //   371: aload_1
    //   372: aload_0
    //   373: putfield lastInTransaction : Landroidx/fragment/app/BackStackRecord;
    //   376: iload #4
    //   378: ifne -> 453
    //   381: iload #5
    //   383: ifeq -> 453
    //   386: aload_1
    //   387: ifnull -> 404
    //   390: aload_1
    //   391: getfield firstOut : Landroidx/fragment/app/Fragment;
    //   394: aload #11
    //   396: if_acmpne -> 404
    //   399: aload_1
    //   400: aconst_null
    //   401: putfield firstOut : Landroidx/fragment/app/Fragment;
    //   404: aload_0
    //   405: getfield mManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   408: astore #10
    //   410: aload #11
    //   412: getfield mState : I
    //   415: iconst_1
    //   416: if_icmpge -> 453
    //   419: aload #10
    //   421: getfield mCurState : I
    //   424: iconst_1
    //   425: if_icmplt -> 453
    //   428: aload_0
    //   429: getfield mReorderingAllowed : Z
    //   432: ifne -> 453
    //   435: aload #10
    //   437: aload #11
    //   439: invokevirtual makeActive : (Landroidx/fragment/app/Fragment;)V
    //   442: aload #10
    //   444: aload #11
    //   446: iconst_1
    //   447: iconst_0
    //   448: iconst_0
    //   449: iconst_0
    //   450: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   453: aload_1
    //   454: astore #10
    //   456: iload #7
    //   458: ifeq -> 503
    //   461: aload_1
    //   462: ifnull -> 475
    //   465: aload_1
    //   466: astore #10
    //   468: aload_1
    //   469: getfield firstOut : Landroidx/fragment/app/Fragment;
    //   472: ifnonnull -> 503
    //   475: aload_1
    //   476: aload_2
    //   477: iload #8
    //   479: invokestatic ensureContainer : (Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroidx/fragment/app/FragmentTransition$FragmentContainerTransition;
    //   482: astore #10
    //   484: aload #10
    //   486: aload #11
    //   488: putfield firstOut : Landroidx/fragment/app/Fragment;
    //   491: aload #10
    //   493: iload_3
    //   494: putfield firstOutIsPop : Z
    //   497: aload #10
    //   499: aload_0
    //   500: putfield firstOutTransaction : Landroidx/fragment/app/BackStackRecord;
    //   503: iload #4
    //   505: ifne -> 534
    //   508: iload #6
    //   510: ifeq -> 534
    //   513: aload #10
    //   515: ifnull -> 534
    //   518: aload #10
    //   520: getfield lastIn : Landroidx/fragment/app/Fragment;
    //   523: aload #11
    //   525: if_acmpne -> 534
    //   528: aload #10
    //   530: aconst_null
    //   531: putfield lastIn : Landroidx/fragment/app/Fragment;
    //   534: return
  }
  
  public static void calculateFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    int j = paramBackStackRecord.mOps.size();
    for (int i = 0; i < j; i++)
      addToFirstInLastOut(paramBackStackRecord, paramBackStackRecord.mOps.get(i), paramSparseArray, false, paramBoolean); 
  }
  
  private static ArrayMap<String, String> calculateNameOverrides(int paramInt1, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt2, int paramInt3) {
    ArrayMap<String, String> arrayMap = new ArrayMap();
    while (--paramInt3 >= paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt3);
      if (backStackRecord.interactsWith(paramInt1)) {
        boolean bool = ((Boolean)paramArrayList1.get(paramInt3)).booleanValue();
        if (backStackRecord.mSharedElementSourceNames != null) {
          ArrayList<String> arrayList1;
          ArrayList<String> arrayList2;
          int j = backStackRecord.mSharedElementSourceNames.size();
          if (bool) {
            arrayList2 = backStackRecord.mSharedElementSourceNames;
            arrayList1 = backStackRecord.mSharedElementTargetNames;
          } else {
            arrayList1 = backStackRecord.mSharedElementSourceNames;
            arrayList2 = backStackRecord.mSharedElementTargetNames;
          } 
          int i;
          for (i = 0; i < j; i++) {
            String str1 = arrayList1.get(i);
            String str2 = arrayList2.get(i);
            String str3 = (String)arrayMap.remove(str2);
            if (str3 != null) {
              arrayMap.put(str1, str3);
            } else {
              arrayMap.put(str1, str2);
            } 
          } 
        } 
      } 
      paramInt3--;
    } 
    return arrayMap;
  }
  
  public static void calculatePopFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    if (!paramBackStackRecord.mManager.mContainer.onHasView())
      return; 
    for (int i = paramBackStackRecord.mOps.size() - 1; i >= 0; i--)
      addToFirstInLastOut(paramBackStackRecord, paramBackStackRecord.mOps.get(i), paramSparseArray, true, paramBoolean); 
  }
  
  static void callSharedElementStartEnd(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean1, ArrayMap<String, View> paramArrayMap, boolean paramBoolean2) {
    SharedElementCallback sharedElementCallback;
    if (paramBoolean1) {
      sharedElementCallback = paramFragment2.getEnterTransitionCallback();
    } else {
      sharedElementCallback = sharedElementCallback.getEnterTransitionCallback();
    } 
    if (sharedElementCallback != null) {
      int i;
      ArrayList<Object> arrayList1 = new ArrayList();
      ArrayList<Object> arrayList2 = new ArrayList();
      int j = 0;
      if (paramArrayMap == null) {
        i = 0;
      } else {
        i = paramArrayMap.size();
      } 
      while (j < i) {
        arrayList2.add(paramArrayMap.keyAt(j));
        arrayList1.add(paramArrayMap.valueAt(j));
        j++;
      } 
      if (paramBoolean2) {
        sharedElementCallback.onSharedElementStart(arrayList2, arrayList1, null);
        return;
      } 
      sharedElementCallback.onSharedElementEnd(arrayList2, arrayList1, null);
    } 
  }
  
  private static boolean canHandleAll(FragmentTransitionImpl paramFragmentTransitionImpl, List<Object> paramList) {
    int j = paramList.size();
    for (int i = 0; i < j; i++) {
      if (!paramFragmentTransitionImpl.canHandle(paramList.get(i)))
        return false; 
    } 
    return true;
  }
  
  static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl paramFragmentTransitionImpl, ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    ArrayList<String> arrayList;
    Fragment fragment = paramFragmentContainerTransition.lastIn;
    View view = fragment.getView();
    if (paramArrayMap.isEmpty() || paramObject == null || view == null) {
      paramArrayMap.clear();
      return null;
    } 
    ArrayMap<String, View> arrayMap = new ArrayMap();
    paramFragmentTransitionImpl.findNamedViews((Map<String, View>)arrayMap, view);
    BackStackRecord backStackRecord = paramFragmentContainerTransition.lastInTransaction;
    if (paramFragmentContainerTransition.lastInIsPop) {
      paramObject = fragment.getExitTransitionCallback();
      arrayList = backStackRecord.mSharedElementSourceNames;
    } else {
      paramObject = fragment.getEnterTransitionCallback();
      arrayList = ((BackStackRecord)arrayList).mSharedElementTargetNames;
    } 
    if (arrayList != null) {
      arrayMap.retainAll(arrayList);
      arrayMap.retainAll(paramArrayMap.values());
    } 
    if (paramObject != null) {
      paramObject.onMapSharedElements(arrayList, (Map)arrayMap);
      int i;
      for (i = arrayList.size() - 1; i >= 0; i--) {
        String str = arrayList.get(i);
        paramObject = arrayMap.get(str);
        if (paramObject == null) {
          paramObject = findKeyForValue(paramArrayMap, str);
          if (paramObject != null)
            paramArrayMap.remove(paramObject); 
        } else if (!str.equals(ViewCompat.getTransitionName((View)paramObject))) {
          str = findKeyForValue(paramArrayMap, str);
          if (str != null)
            paramArrayMap.put(str, ViewCompat.getTransitionName((View)paramObject)); 
        } 
      } 
    } else {
      retainValues(paramArrayMap, arrayMap);
    } 
    return arrayMap;
  }
  
  private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl paramFragmentTransitionImpl, ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    ArrayList<String> arrayList;
    if (paramArrayMap.isEmpty() || paramObject == null) {
      paramArrayMap.clear();
      return null;
    } 
    paramObject = paramFragmentContainerTransition.firstOut;
    ArrayMap<String, View> arrayMap = new ArrayMap();
    paramFragmentTransitionImpl.findNamedViews((Map<String, View>)arrayMap, paramObject.requireView());
    BackStackRecord backStackRecord = paramFragmentContainerTransition.firstOutTransaction;
    if (paramFragmentContainerTransition.firstOutIsPop) {
      paramObject = paramObject.getEnterTransitionCallback();
      arrayList = backStackRecord.mSharedElementTargetNames;
    } else {
      paramObject = paramObject.getExitTransitionCallback();
      arrayList = ((BackStackRecord)arrayList).mSharedElementSourceNames;
    } 
    arrayMap.retainAll(arrayList);
    if (paramObject != null) {
      paramObject.onMapSharedElements(arrayList, (Map)arrayMap);
      int i;
      for (i = arrayList.size() - 1; i >= 0; i--) {
        String str = arrayList.get(i);
        paramObject = arrayMap.get(str);
        if (paramObject == null) {
          paramArrayMap.remove(str);
        } else if (!str.equals(ViewCompat.getTransitionName((View)paramObject))) {
          str = (String)paramArrayMap.remove(str);
          paramArrayMap.put(ViewCompat.getTransitionName((View)paramObject), str);
        } 
      } 
    } else {
      paramArrayMap.retainAll(arrayMap.keySet());
    } 
    return arrayMap;
  }
  
  private static FragmentTransitionImpl chooseImpl(Fragment paramFragment1, Fragment paramFragment2) {
    ArrayList<Object> arrayList = new ArrayList();
    if (paramFragment1 != null) {
      Object object2 = paramFragment1.getExitTransition();
      if (object2 != null)
        arrayList.add(object2); 
      object2 = paramFragment1.getReturnTransition();
      if (object2 != null)
        arrayList.add(object2); 
      Object object1 = paramFragment1.getSharedElementReturnTransition();
      if (object1 != null)
        arrayList.add(object1); 
    } 
    if (paramFragment2 != null) {
      Object object = paramFragment2.getEnterTransition();
      if (object != null)
        arrayList.add(object); 
      object = paramFragment2.getReenterTransition();
      if (object != null)
        arrayList.add(object); 
      object = paramFragment2.getSharedElementEnterTransition();
      if (object != null)
        arrayList.add(object); 
    } 
    if (arrayList.isEmpty())
      return null; 
    FragmentTransitionImpl fragmentTransitionImpl = PLATFORM_IMPL;
    if (fragmentTransitionImpl != null && canHandleAll(fragmentTransitionImpl, arrayList))
      return PLATFORM_IMPL; 
    fragmentTransitionImpl = SUPPORT_IMPL;
    if (fragmentTransitionImpl != null && canHandleAll(fragmentTransitionImpl, arrayList))
      return SUPPORT_IMPL; 
    if (PLATFORM_IMPL == null && SUPPORT_IMPL == null)
      return null; 
    throw new IllegalArgumentException("Invalid Transition types");
  }
  
  static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject, Fragment paramFragment, ArrayList<View> paramArrayList, View paramView) {
    if (paramObject != null) {
      ArrayList<View> arrayList2 = new ArrayList();
      View view = paramFragment.getView();
      if (view != null)
        paramFragmentTransitionImpl.captureTransitioningViews(arrayList2, view); 
      if (paramArrayList != null)
        arrayList2.removeAll(paramArrayList); 
      ArrayList<View> arrayList1 = arrayList2;
      if (!arrayList2.isEmpty()) {
        arrayList2.add(paramView);
        paramFragmentTransitionImpl.addTargets(paramObject, arrayList2);
        return arrayList2;
      } 
    } else {
      paramFragment = null;
    } 
    return (ArrayList<View>)paramFragment;
  }
  
  private static Object configureSharedElementsOrdered(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final View nonExistentView, final ArrayMap<String, String> nameOverrides, final FragmentContainerTransition fragments, final ArrayList<View> sharedElementsOut, final ArrayList<View> sharedElementsIn, final Object enterTransition, final Object inEpicenter) {
    final Fragment inFragment = fragments.lastIn;
    final Fragment outFragment = fragments.firstOut;
    if (fragment1 != null) {
      final Object finalSharedElementTransition;
      if (fragment2 == null)
        return null; 
      final boolean inIsPop = fragments.lastInIsPop;
      if (nameOverrides.isEmpty()) {
        object = null;
      } else {
        object = getSharedElementTransition(impl, fragment1, fragment2, bool);
      } 
      ArrayMap<String, View> arrayMap = captureOutSharedElements(impl, nameOverrides, object, fragments);
      if (nameOverrides.isEmpty()) {
        object = null;
      } else {
        sharedElementsOut.addAll(arrayMap.values());
      } 
      if (enterTransition == null && inEpicenter == null && object == null)
        return null; 
      callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap, true);
      if (object != null) {
        Rect rect = new Rect();
        impl.setSharedElementTargets(object, nonExistentView, sharedElementsOut);
        setOutEpicenter(impl, object, inEpicenter, arrayMap, fragments.firstOutIsPop, fragments.firstOutTransaction);
        inEpicenter = rect;
        if (enterTransition != null) {
          impl.setEpicenter(enterTransition, rect);
          inEpicenter = rect;
        } 
      } else {
        inEpicenter = null;
      } 
      OneShotPreDrawListener.add((View)paramViewGroup, new Runnable() {
            public void run() {
              ArrayMap<String, View> arrayMap = FragmentTransition.captureInSharedElements(impl, nameOverrides, finalSharedElementTransition, fragments);
              if (arrayMap != null) {
                sharedElementsIn.addAll(arrayMap.values());
                sharedElementsIn.add(nonExistentView);
              } 
              FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, arrayMap, false);
              Object object = finalSharedElementTransition;
              if (object != null) {
                impl.swapSharedElementTargets(object, sharedElementsOut, sharedElementsIn);
                View view = FragmentTransition.getInEpicenterView(arrayMap, fragments, enterTransition, inIsPop);
                if (view != null)
                  impl.getBoundsOnScreen(view, inEpicenter); 
              } 
            }
          });
      return object;
    } 
    return null;
  }
  
  private static Object configureSharedElementsReordered(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, View paramView, ArrayMap<String, String> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Object paramObject1, Object paramObject2) {
    final Fragment inFragment = paramFragmentContainerTransition.lastIn;
    final Fragment outFragment = paramFragmentContainerTransition.firstOut;
    if (fragment1 != null)
      fragment1.requireView().setVisibility(0); 
    if (fragment1 != null) {
      Object object1;
      final View epicenterView;
      final View epicenter;
      Object object2;
      if (fragment2 == null)
        return null; 
      final boolean inIsPop = paramFragmentContainerTransition.lastInIsPop;
      if (paramArrayMap.isEmpty()) {
        object2 = null;
      } else {
        object2 = getSharedElementTransition(impl, fragment1, fragment2, bool);
      } 
      ArrayMap<String, View> arrayMap2 = captureOutSharedElements(impl, paramArrayMap, object2, paramFragmentContainerTransition);
      final ArrayMap<String, View> inSharedElements = captureInSharedElements(impl, paramArrayMap, object2, paramFragmentContainerTransition);
      if (paramArrayMap.isEmpty()) {
        if (arrayMap2 != null)
          arrayMap2.clear(); 
        if (arrayMap1 != null)
          arrayMap1.clear(); 
        paramArrayMap = null;
      } else {
        addSharedElementsWithMatchingNames(paramArrayList1, arrayMap2, paramArrayMap.keySet());
        addSharedElementsWithMatchingNames(paramArrayList2, arrayMap1, paramArrayMap.values());
        object1 = object2;
      } 
      if (paramObject1 == null && paramObject2 == null && object1 == null)
        return null; 
      callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap2, true);
      if (object1 != null) {
        paramArrayList2.add(paramView);
        impl.setSharedElementTargets(object1, paramView, paramArrayList1);
        setOutEpicenter(impl, object1, paramObject2, arrayMap2, paramFragmentContainerTransition.firstOutIsPop, paramFragmentContainerTransition.firstOutTransaction);
        Rect rect1 = new Rect();
        view1 = getInEpicenterView(arrayMap1, paramFragmentContainerTransition, paramObject1, bool);
        if (view1 != null)
          impl.setEpicenter(paramObject1, rect1); 
        Rect rect2 = rect1;
      } else {
        paramView = null;
        view2 = paramView;
        view1 = paramView;
      } 
      OneShotPreDrawListener.add((View)paramViewGroup, new Runnable() {
            public void run() {
              FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
              View view = epicenterView;
              if (view != null)
                impl.getBoundsOnScreen(view, epicenter); 
            }
          });
      return object1;
    } 
    return null;
  }
  
  private static void configureTransitionsOrdered(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap) {
    if (paramFragmentManagerImpl.mContainer.onHasView()) {
      ViewGroup viewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    } else {
      paramFragmentManagerImpl = null;
    } 
    if (paramFragmentManagerImpl == null)
      return; 
    Fragment fragment1 = paramFragmentContainerTransition.lastIn;
    Fragment fragment2 = paramFragmentContainerTransition.firstOut;
    FragmentTransitionImpl fragmentTransitionImpl = chooseImpl(fragment2, fragment1);
    if (fragmentTransitionImpl == null)
      return; 
    boolean bool1 = paramFragmentContainerTransition.lastInIsPop;
    boolean bool2 = paramFragmentContainerTransition.firstOutIsPop;
    Object object3 = getEnterTransition(fragmentTransitionImpl, fragment1, bool1);
    Object object2 = getExitTransition(fragmentTransitionImpl, fragment2, bool2);
    ArrayList<View> arrayList3 = new ArrayList();
    ArrayList<View> arrayList1 = new ArrayList();
    Object object4 = configureSharedElementsOrdered(fragmentTransitionImpl, (ViewGroup)paramFragmentManagerImpl, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList3, arrayList1, object3, object2);
    if (object3 == null && object4 == null && object2 == null)
      return; 
    ArrayList<View> arrayList2 = configureEnteringExitingViews(fragmentTransitionImpl, object2, fragment2, arrayList3, paramView);
    if (arrayList2 == null || arrayList2.isEmpty())
      object2 = null; 
    fragmentTransitionImpl.addTarget(object3, paramView);
    Object object1 = mergeTransitions(fragmentTransitionImpl, object3, object2, object4, fragment1, paramFragmentContainerTransition.lastInIsPop);
    if (object1 != null) {
      arrayList3 = new ArrayList<View>();
      fragmentTransitionImpl.scheduleRemoveTargets(object1, object3, arrayList3, object2, arrayList2, object4, arrayList1);
      scheduleTargetChange(fragmentTransitionImpl, (ViewGroup)paramFragmentManagerImpl, fragment1, paramView, arrayList1, object3, arrayList3, object2, arrayList2);
      fragmentTransitionImpl.setNameOverridesOrdered((View)paramFragmentManagerImpl, arrayList1, (Map<String, String>)paramArrayMap);
      fragmentTransitionImpl.beginDelayedTransition((ViewGroup)paramFragmentManagerImpl, object1);
      fragmentTransitionImpl.scheduleNameReset((ViewGroup)paramFragmentManagerImpl, arrayList1, (Map<String, String>)paramArrayMap);
    } 
  }
  
  private static void configureTransitionsReordered(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap) {
    if (paramFragmentManagerImpl.mContainer.onHasView()) {
      ViewGroup viewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    } else {
      paramFragmentManagerImpl = null;
    } 
    if (paramFragmentManagerImpl == null)
      return; 
    Fragment fragment2 = paramFragmentContainerTransition.lastIn;
    Fragment fragment1 = paramFragmentContainerTransition.firstOut;
    FragmentTransitionImpl fragmentTransitionImpl = chooseImpl(fragment1, fragment2);
    if (fragmentTransitionImpl == null)
      return; 
    boolean bool1 = paramFragmentContainerTransition.lastInIsPop;
    boolean bool2 = paramFragmentContainerTransition.firstOutIsPop;
    ArrayList<View> arrayList2 = new ArrayList();
    ArrayList<View> arrayList3 = new ArrayList();
    Object object3 = getEnterTransition(fragmentTransitionImpl, fragment2, bool1);
    Object<View> object2 = (Object<View>)getExitTransition(fragmentTransitionImpl, fragment1, bool2);
    Object object4 = configureSharedElementsReordered(fragmentTransitionImpl, (ViewGroup)paramFragmentManagerImpl, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList3, arrayList2, object3, object2);
    if (object3 == null && object4 == null && object2 == null)
      return; 
    Object<View> object1 = object2;
    object2 = (Object<View>)configureEnteringExitingViews(fragmentTransitionImpl, object1, fragment1, arrayList3, paramView);
    ArrayList<View> arrayList1 = configureEnteringExitingViews(fragmentTransitionImpl, object3, fragment2, arrayList2, paramView);
    setViewVisibility(arrayList1, 4);
    Object object5 = mergeTransitions(fragmentTransitionImpl, object3, object1, object4, fragment2, bool1);
    if (object5 != null) {
      replaceHide(fragmentTransitionImpl, object1, fragment1, (ArrayList<View>)object2);
      ArrayList<String> arrayList = fragmentTransitionImpl.prepareSetNameOverridesReordered(arrayList2);
      fragmentTransitionImpl.scheduleRemoveTargets(object5, object3, arrayList1, object1, (ArrayList<View>)object2, object4, arrayList2);
      fragmentTransitionImpl.beginDelayedTransition((ViewGroup)paramFragmentManagerImpl, object5);
      fragmentTransitionImpl.setNameOverridesReordered((View)paramFragmentManagerImpl, arrayList3, arrayList2, arrayList, (Map<String, String>)paramArrayMap);
      setViewVisibility(arrayList1, 0);
      fragmentTransitionImpl.swapSharedElementTargets(object4, arrayList3, arrayList2);
    } 
  }
  
  private static FragmentContainerTransition ensureContainer(FragmentContainerTransition paramFragmentContainerTransition, SparseArray<FragmentContainerTransition> paramSparseArray, int paramInt) {
    FragmentContainerTransition fragmentContainerTransition = paramFragmentContainerTransition;
    if (paramFragmentContainerTransition == null) {
      fragmentContainerTransition = new FragmentContainerTransition();
      paramSparseArray.put(paramInt, fragmentContainerTransition);
    } 
    return fragmentContainerTransition;
  }
  
  private static String findKeyForValue(ArrayMap<String, String> paramArrayMap, String paramString) {
    int j = paramArrayMap.size();
    for (int i = 0; i < j; i++) {
      if (paramString.equals(paramArrayMap.valueAt(i)))
        return (String)paramArrayMap.keyAt(i); 
    } 
    return null;
  }
  
  private static Object getEnterTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment, boolean paramBoolean) {
    Object object;
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReenterTransition();
    } else {
      object = object.getEnterTransition();
    } 
    return paramFragmentTransitionImpl.cloneTransition(object);
  }
  
  private static Object getExitTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment, boolean paramBoolean) {
    Object object;
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReturnTransition();
    } else {
      object = object.getExitTransition();
    } 
    return paramFragmentTransitionImpl.cloneTransition(object);
  }
  
  static View getInEpicenterView(ArrayMap<String, View> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, Object paramObject, boolean paramBoolean) {
    BackStackRecord backStackRecord = paramFragmentContainerTransition.lastInTransaction;
    if (paramObject != null && paramArrayMap != null && backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = backStackRecord.mSharedElementSourceNames.get(0);
      } else {
        str = ((BackStackRecord)str).mSharedElementTargetNames.get(0);
      } 
      return (View)paramArrayMap.get(str);
    } 
    return null;
  }
  
  private static Object getSharedElementTransition(FragmentTransitionImpl paramFragmentTransitionImpl, Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean) {
    Object object;
    if (paramFragment1 == null || paramFragment2 == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment2.getSharedElementReturnTransition();
    } else {
      object = object.getSharedElementEnterTransition();
    } 
    return paramFragmentTransitionImpl.wrapTransitionInSet(paramFragmentTransitionImpl.cloneTransition(object));
  }
  
  private static Object mergeTransitions(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject1, Object paramObject2, Object paramObject3, Fragment paramFragment, boolean paramBoolean) {
    if (paramObject1 != null && paramObject2 != null && paramFragment != null) {
      if (paramBoolean) {
        paramBoolean = paramFragment.getAllowReturnTransitionOverlap();
      } else {
        paramBoolean = paramFragment.getAllowEnterTransitionOverlap();
      } 
    } else {
      paramBoolean = true;
    } 
    return paramBoolean ? paramFragmentTransitionImpl.mergeTransitionsTogether(paramObject2, paramObject1, paramObject3) : paramFragmentTransitionImpl.mergeTransitionsInSequence(paramObject2, paramObject1, paramObject3);
  }
  
  private static void replaceHide(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject, Fragment paramFragment, final ArrayList<View> exitingViews) {
    if (paramFragment != null && paramObject != null && paramFragment.mAdded && paramFragment.mHidden && paramFragment.mHiddenChanged) {
      paramFragment.setHideReplaced(true);
      paramFragmentTransitionImpl.scheduleHideFragmentView(paramObject, paramFragment.getView(), exitingViews);
      OneShotPreDrawListener.add((View)paramFragment.mContainer, new Runnable() {
            public void run() {
              FragmentTransition.setViewVisibility(exitingViews, 4);
            }
          });
    } 
  }
  
  private static FragmentTransitionImpl resolveSupportImpl() {
    try {
      return Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  private static void retainValues(ArrayMap<String, String> paramArrayMap, ArrayMap<String, View> paramArrayMap1) {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      if (!paramArrayMap1.containsKey(paramArrayMap.valueAt(i)))
        paramArrayMap.removeAt(i); 
    } 
  }
  
  private static void scheduleTargetChange(final FragmentTransitionImpl impl, ViewGroup paramViewGroup, final Fragment inFragment, final View nonExistentView, final ArrayList<View> sharedElementsIn, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews) {
    OneShotPreDrawListener.add((View)paramViewGroup, new Runnable() {
          public void run() {
            Object<View> object = (Object<View>)enterTransition;
            if (object != null) {
              impl.removeTarget(object, nonExistentView);
              object = (Object<View>)FragmentTransition.configureEnteringExitingViews(impl, enterTransition, inFragment, sharedElementsIn, nonExistentView);
              enteringViews.addAll((Collection<? extends View>)object);
            } 
            if (exitingViews != null) {
              if (exitTransition != null) {
                object = (Object<View>)new ArrayList();
                object.add(nonExistentView);
                impl.replaceTargets(exitTransition, exitingViews, (ArrayList<View>)object);
              } 
              exitingViews.clear();
              exitingViews.add(nonExistentView);
            } 
          }
        });
  }
  
  private static void setOutEpicenter(FragmentTransitionImpl paramFragmentTransitionImpl, Object paramObject1, Object paramObject2, ArrayMap<String, View> paramArrayMap, boolean paramBoolean, BackStackRecord paramBackStackRecord) {
    if (paramBackStackRecord.mSharedElementSourceNames != null && !paramBackStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = paramBackStackRecord.mSharedElementTargetNames.get(0);
      } else {
        str = ((BackStackRecord)str).mSharedElementSourceNames.get(0);
      } 
      View view = (View)paramArrayMap.get(str);
      paramFragmentTransitionImpl.setEpicenter(paramObject1, view);
      if (paramObject2 != null)
        paramFragmentTransitionImpl.setEpicenter(paramObject2, view); 
    } 
  }
  
  static void setViewVisibility(ArrayList<View> paramArrayList, int paramInt) {
    if (paramArrayList == null)
      return; 
    for (int i = paramArrayList.size() - 1; i >= 0; i--)
      ((View)paramArrayList.get(i)).setVisibility(paramInt); 
  }
  
  static void startTransitions(FragmentManagerImpl paramFragmentManagerImpl, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramFragmentManagerImpl.mCurState < 1)
      return; 
    SparseArray<FragmentContainerTransition> sparseArray = new SparseArray();
    int i;
    for (i = paramInt1; i < paramInt2; i++) {
      BackStackRecord backStackRecord = paramArrayList.get(i);
      if (((Boolean)paramArrayList1.get(i)).booleanValue()) {
        calculatePopFragments(backStackRecord, sparseArray, paramBoolean);
      } else {
        calculateFragments(backStackRecord, sparseArray, paramBoolean);
      } 
    } 
    if (sparseArray.size() != 0) {
      View view = new View(paramFragmentManagerImpl.mHost.getContext());
      int j = sparseArray.size();
      for (i = 0; i < j; i++) {
        int k = sparseArray.keyAt(i);
        ArrayMap<String, String> arrayMap = calculateNameOverrides(k, paramArrayList, paramArrayList1, paramInt1, paramInt2);
        FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(i);
        if (paramBoolean) {
          configureTransitionsReordered(paramFragmentManagerImpl, k, fragmentContainerTransition, view, arrayMap);
        } else {
          configureTransitionsOrdered(paramFragmentManagerImpl, k, fragmentContainerTransition, view, arrayMap);
        } 
      } 
    } 
  }
  
  static boolean supportsTransition() {
    return (PLATFORM_IMPL != null || SUPPORT_IMPL != null);
  }
  
  static {
    FragmentTransitionImpl fragmentTransitionImpl;
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 21) {
      fragmentTransitionImpl = new FragmentTransitionCompat21();
    } else {
      fragmentTransitionImpl = null;
    } 
    PLATFORM_IMPL = fragmentTransitionImpl;
  }
  
  static class FragmentContainerTransition {
    public Fragment firstOut;
    
    public boolean firstOutIsPop;
    
    public BackStackRecord firstOutTransaction;
    
    public Fragment lastIn;
    
    public boolean lastInIsPop;
    
    public BackStackRecord lastInTransaction;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\fragment\app\FragmentTransition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
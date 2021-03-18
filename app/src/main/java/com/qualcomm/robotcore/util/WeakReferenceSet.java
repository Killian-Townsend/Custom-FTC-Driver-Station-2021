package com.qualcomm.robotcore.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.WeakHashMap;

public class WeakReferenceSet<E> implements Set<E> {
  WeakHashMap<E, Integer> members = new WeakHashMap<E, Integer>();
  
  public boolean add(E paramE) {
    synchronized (this.members) {
      WeakHashMap<E, Integer> weakHashMap = this.members;
      boolean bool = true;
      if (weakHashMap.put(paramE, Integer.valueOf(1)) != null)
        bool = false; 
      return bool;
    } 
  }
  
  public boolean addAll(Collection<? extends E> paramCollection) {
    // Byte code:
    //   0: aload_0
    //   1: getfield members : Ljava/util/WeakHashMap;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_1
    //   10: invokeinterface iterator : ()Ljava/util/Iterator;
    //   15: astore_1
    //   16: aload_1
    //   17: invokeinterface hasNext : ()Z
    //   22: ifeq -> 43
    //   25: aload_0
    //   26: aload_1
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: invokevirtual add : (Ljava/lang/Object;)Z
    //   35: ifeq -> 16
    //   38: iconst_1
    //   39: istore_2
    //   40: goto -> 16
    //   43: aload_3
    //   44: monitorexit
    //   45: iload_2
    //   46: ireturn
    //   47: astore_1
    //   48: aload_3
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   9	16	47	finally
    //   16	38	47	finally
    //   43	45	47	finally
    //   48	50	47	finally
  }
  
  public void clear() {
    synchronized (this.members) {
      this.members.clear();
      return;
    } 
  }
  
  public boolean contains(Object paramObject) {
    synchronized (this.members) {
      return this.members.containsKey(paramObject);
    } 
  }
  
  public boolean containsAll(Collection<?> paramCollection) {
    synchronized (this.members) {
      Iterator<?> iterator = paramCollection.iterator();
      while (iterator.hasNext()) {
        if (!contains(iterator.next()))
          return false; 
      } 
      return true;
    } 
  }
  
  public boolean isEmpty() {
    return (size() == 0);
  }
  
  public Iterator<E> iterator() {
    synchronized (this.members) {
      LinkedList linkedList = new LinkedList();
      Iterator iterator = this.members.keySet().iterator();
      while (iterator.hasNext())
        linkedList.add(iterator.next()); 
      return linkedList.iterator();
    } 
  }
  
  public boolean remove(Object paramObject) {
    synchronized (this.members) {
      if (this.members.remove(paramObject) != null)
        return true; 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_3} */
    return bool;
  }
  
  public boolean removeAll(Collection<?> paramCollection) {
    // Byte code:
    //   0: aload_0
    //   1: getfield members : Ljava/util/WeakHashMap;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_1
    //   10: invokeinterface iterator : ()Ljava/util/Iterator;
    //   15: astore_1
    //   16: aload_1
    //   17: invokeinterface hasNext : ()Z
    //   22: ifeq -> 43
    //   25: aload_0
    //   26: aload_1
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: invokevirtual remove : (Ljava/lang/Object;)Z
    //   35: ifeq -> 16
    //   38: iconst_1
    //   39: istore_2
    //   40: goto -> 16
    //   43: aload_3
    //   44: monitorexit
    //   45: iload_2
    //   46: ireturn
    //   47: astore_1
    //   48: aload_3
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   9	16	47	finally
    //   16	38	47	finally
    //   43	45	47	finally
    //   48	50	47	finally
  }
  
  public boolean retainAll(Collection<?> paramCollection) {
    // Byte code:
    //   0: aload_0
    //   1: getfield members : Ljava/util/WeakHashMap;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_0
    //   10: invokevirtual iterator : ()Ljava/util/Iterator;
    //   13: astore #4
    //   15: aload #4
    //   17: invokeinterface hasNext : ()Z
    //   22: ifeq -> 59
    //   25: aload #4
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: astore #5
    //   34: aload_1
    //   35: aload #5
    //   37: invokeinterface contains : (Ljava/lang/Object;)Z
    //   42: ifne -> 15
    //   45: aload_0
    //   46: aload #5
    //   48: invokevirtual remove : (Ljava/lang/Object;)Z
    //   51: ifeq -> 15
    //   54: iconst_1
    //   55: istore_2
    //   56: goto -> 15
    //   59: aload_3
    //   60: monitorexit
    //   61: iload_2
    //   62: ireturn
    //   63: astore_1
    //   64: aload_3
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   9	15	63	finally
    //   15	54	63	finally
    //   59	61	63	finally
    //   64	66	63	finally
  }
  
  public int size() {
    synchronized (this.members) {
      return this.members.size();
    } 
  }
  
  public Object[] toArray() {
    synchronized (this.members) {
      LinkedList linkedList = new LinkedList();
      Iterator iterator = this.members.keySet().iterator();
      while (iterator.hasNext())
        linkedList.add(iterator.next()); 
      return linkedList.toArray();
    } 
  }
  
  public Object[] toArray(Object[] paramArrayOfObject) {
    int j;
    Object[] arrayOfObject1;
    Object[] arrayOfObject2;
    synchronized (this.members) {
      arrayOfObject2 = toArray();
      arrayOfObject1 = paramArrayOfObject;
      if (arrayOfObject2.length > paramArrayOfObject.length)
        arrayOfObject1 = new Object[arrayOfObject2.length]; 
    } 
    int i = 0;
    while (true) {
      j = i;
      if (i < arrayOfObject2.length) {
        arrayOfObject1[i] = arrayOfObject2[i];
        i++;
        continue;
      } 
      break;
    } 
    while (j < arrayOfObject1.length) {
      arrayOfObject1[j] = null;
      j++;
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
    return arrayOfObject1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\WeakReferenceSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.system;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class Misc {
  public static final String TAG = "Misc";
  
  public static boolean approximatelyEquals(double paramDouble1, double paramDouble2) {
    return approximatelyEquals(paramDouble1, paramDouble2, 1.0E-9D);
  }
  
  public static boolean approximatelyEquals(double paramDouble1, double paramDouble2, double paramDouble3) {
    if (paramDouble1 == paramDouble2)
      return true; 
    if (paramDouble2 != 0.0D)
      paramDouble1 = paramDouble1 / paramDouble2 - 1.0D; 
    return (Math.abs(paramDouble1) < paramDouble3);
  }
  
  public static boolean contains(byte[] paramArrayOfbyte, byte paramByte) {
    int j = paramArrayOfbyte.length;
    for (int i = 0; i < j; i++) {
      if (paramArrayOfbyte[i] == paramByte)
        return true; 
    } 
    return false;
  }
  
  public static boolean contains(int[] paramArrayOfint, int paramInt) {
    int j = paramArrayOfint.length;
    for (int i = 0; i < j; i++) {
      if (paramArrayOfint[i] == paramInt)
        return true; 
    } 
    return false;
  }
  
  public static boolean contains(long[] paramArrayOflong, long paramLong) {
    int j = paramArrayOflong.length;
    for (int i = 0; i < j; i++) {
      if (paramArrayOflong[i] == paramLong)
        return true; 
    } 
    return false;
  }
  
  public static boolean contains(short[] paramArrayOfshort, short paramShort) {
    int j = paramArrayOfshort.length;
    for (int i = 0; i < j; i++) {
      if (paramArrayOfshort[i] == paramShort)
        return true; 
    } 
    return false;
  }
  
  public static String decodeEntity(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < paramString.length(); i++) {
      char c = paramString.charAt(i);
      if (c == '&') {
        int j;
        for (j = ++i; paramString.charAt(j) != ';'; j++);
        String str = paramString.substring(i, j - 1);
        i = -1;
        int k = str.hashCode();
        if (k != 3309) {
          if (k != 3464) {
            if (k != 96708) {
              if (k != 3000915) {
                if (k == 3482377 && str.equals("quot"))
                  i = 3; 
              } else if (str.equals("apos")) {
                i = 4;
              } 
            } else if (str.equals("amp")) {
              i = 0;
            } 
          } else if (str.equals("lt")) {
            i = 1;
          } 
        } else if (str.equals("gt")) {
          i = 2;
        } 
        if (i != 0) {
          if (i != 1) {
            if (i != 2) {
              if (i != 3) {
                if (i != 4) {
                  if (str.length() > 2 && str.charAt(0) == '#' && str.charAt(1) == 'x') {
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("0x");
                    stringBuilder1.append(str.substring(2));
                    stringBuilder.append((char)Integer.decode(stringBuilder1.toString()).intValue());
                  } else {
                    throw illegalArgumentException("illegal entity reference");
                  } 
                } else {
                  stringBuilder.append('\'');
                } 
              } else {
                stringBuilder.append('"');
              } 
            } else {
              stringBuilder.append('>');
            } 
          } else {
            stringBuilder.append('<');
          } 
        } else {
          stringBuilder.append('&');
        } 
        i = j;
      } else {
        stringBuilder.append(c);
      } 
    } 
    return stringBuilder.toString();
  }
  
  public static String encodeEntity(String paramString) {
    return encodeEntity(paramString, "");
  }
  
  public static String encodeEntity(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    for (char c : paramString1.toCharArray()) {
      if (c != '"') {
        if (c != '<') {
          if (c != '>') {
            if (c != '&') {
              if (c != '\'') {
                if (paramString2.indexOf(c) >= 0) {
                  stringBuilder.append(formatInvariant("&#x%x;", new Object[] { Character.valueOf(c) }));
                } else {
                  stringBuilder.append(c);
                } 
              } else {
                stringBuilder.append("&apos;");
              } 
            } else {
              stringBuilder.append("&amp;");
            } 
          } else {
            stringBuilder.append("&gt;");
          } 
        } else {
          stringBuilder.append("&lt;");
        } 
      } else {
        stringBuilder.append("&quot;");
      } 
    } 
    return stringBuilder.toString();
  }
  
  public static String formatForUser(int paramInt) {
    return AppUtil.getDefContext().getString(paramInt);
  }
  
  public static String formatForUser(int paramInt, Object... paramVarArgs) {
    return AppUtil.getDefContext().getString(paramInt, paramVarArgs);
  }
  
  public static String formatForUser(String paramString) {
    return paramString;
  }
  
  public static String formatForUser(String paramString, Object... paramVarArgs) {
    return String.format(Locale.getDefault(), paramString, paramVarArgs);
  }
  
  public static String formatInvariant(String paramString) {
    return paramString;
  }
  
  public static String formatInvariant(String paramString, Object... paramVarArgs) {
    return String.format(Locale.ROOT, paramString, paramVarArgs);
  }
  
  public static IllegalArgumentException illegalArgumentException(String paramString) {
    return new IllegalArgumentException(paramString);
  }
  
  public static IllegalArgumentException illegalArgumentException(String paramString, Object... paramVarArgs) {
    return new IllegalArgumentException(formatInvariant(paramString, paramVarArgs));
  }
  
  public static IllegalArgumentException illegalArgumentException(Throwable paramThrowable, String paramString) {
    return new IllegalArgumentException(paramString, paramThrowable);
  }
  
  public static IllegalArgumentException illegalArgumentException(Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    return new IllegalArgumentException(formatInvariant(paramString, paramVarArgs), paramThrowable);
  }
  
  public static IllegalStateException illegalStateException(String paramString) {
    return new IllegalStateException(paramString);
  }
  
  public static IllegalStateException illegalStateException(String paramString, Object... paramVarArgs) {
    return new IllegalStateException(formatInvariant(paramString, paramVarArgs));
  }
  
  public static IllegalStateException illegalStateException(Throwable paramThrowable, String paramString) {
    return new IllegalStateException(paramString, paramThrowable);
  }
  
  public static IllegalStateException illegalStateException(Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    return new IllegalStateException(formatInvariant(paramString, paramVarArgs), paramThrowable);
  }
  
  public static RuntimeException internalError(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("internal error:");
    stringBuilder.append(paramString);
    return new RuntimeException(stringBuilder.toString());
  }
  
  public static RuntimeException internalError(String paramString, Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("internal error:");
    stringBuilder.append(formatInvariant(paramString, paramVarArgs));
    return new RuntimeException(stringBuilder.toString());
  }
  
  public static RuntimeException internalError(Throwable paramThrowable, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("internal error:");
    stringBuilder.append(paramString);
    return new RuntimeException(stringBuilder.toString(), paramThrowable);
  }
  
  public static RuntimeException internalError(Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("internal error:");
    stringBuilder.append(formatInvariant(paramString, paramVarArgs));
    return new RuntimeException(stringBuilder.toString(), paramThrowable);
  }
  
  public static <E> Set<E> intersect(Set<E> paramSet1, Set<E> paramSet2) {
    // Byte code:
    //   0: new java/util/HashSet
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_2
    //   8: aload_0
    //   9: invokeinterface iterator : ()Ljava/util/Iterator;
    //   14: astore_0
    //   15: aload_0
    //   16: invokeinterface hasNext : ()Z
    //   21: ifeq -> 52
    //   24: aload_0
    //   25: invokeinterface next : ()Ljava/lang/Object;
    //   30: astore_3
    //   31: aload_1
    //   32: aload_3
    //   33: invokeinterface contains : (Ljava/lang/Object;)Z
    //   38: ifeq -> 15
    //   41: aload_2
    //   42: aload_3
    //   43: invokeinterface add : (Ljava/lang/Object;)Z
    //   48: pop
    //   49: goto -> 15
    //   52: aload_2
    //   53: areturn
  }
  
  public static boolean isEven(byte paramByte) {
    return ((paramByte & 0x1) == 0);
  }
  
  public static boolean isEven(int paramInt) {
    return ((paramInt & 0x1) == 0);
  }
  
  public static boolean isEven(long paramLong) {
    return ((paramLong & 0x1L) == 0L);
  }
  
  public static boolean isEven(short paramShort) {
    return ((paramShort & 0x1) == 0);
  }
  
  public static boolean isFinite(double paramDouble) {
    return (!Double.isNaN(paramDouble) && !Double.isInfinite(paramDouble));
  }
  
  public static boolean isOdd(byte paramByte) {
    return isEven(paramByte) ^ true;
  }
  
  public static boolean isOdd(int paramInt) {
    return isEven(paramInt) ^ true;
  }
  
  public static boolean isOdd(long paramLong) {
    return isEven(paramLong) ^ true;
  }
  
  public static boolean isOdd(short paramShort) {
    return isEven(paramShort) ^ true;
  }
  
  public static int saturatingAdd(int paramInt1, int paramInt2) {
    if (paramInt1 != 0 && paramInt2 != 0) {
      boolean bool1;
      boolean bool2 = true;
      if (paramInt1 > 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (paramInt2 <= 0)
        bool2 = false; 
      if ((bool2 ^ bool1) == 0)
        return (paramInt1 > 0) ? ((Integer.MAX_VALUE - paramInt1 < paramInt2) ? Integer.MAX_VALUE : (paramInt1 + paramInt2)) : ((Integer.MIN_VALUE - paramInt1 > paramInt2) ? Integer.MIN_VALUE : (paramInt1 + paramInt2)); 
    } 
    return paramInt1 + paramInt2;
  }
  
  public static long saturatingAdd(long paramLong1, long paramLong2) {
    int i = paramLong1 cmp 0L;
    if (i != 0) {
      int j = paramLong2 cmp 0L;
      if (j != 0) {
        boolean bool1;
        boolean bool2 = true;
        if (i > 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (j <= 0)
          bool2 = false; 
        if ((bool1 ^ bool2) == 0)
          return (i > 0) ? ((Long.MAX_VALUE - paramLong1 < paramLong2) ? Long.MAX_VALUE : (paramLong1 + paramLong2)) : ((Long.MIN_VALUE - paramLong1 > paramLong2) ? Long.MIN_VALUE : (paramLong1 + paramLong2)); 
      } 
    } 
    return paramLong1 + paramLong2;
  }
  
  public static <T> T[] toArray(T[] paramArrayOfT, ArrayList<T> paramArrayList) {
    return paramArrayList.toArray(paramArrayOfT);
  }
  
  public static <T> T[] toArray(T[] paramArrayOfT, Collection<T> paramCollection) {
    int j = paramCollection.size();
    T[] arrayOfT = paramArrayOfT;
    if (paramArrayOfT.length < j)
      arrayOfT = (T[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), j); 
    int i = 0;
    Iterator<T> iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      arrayOfT[i] = iterator.next();
      i++;
    } 
    if (arrayOfT.length > j)
      arrayOfT[j] = null; 
    return arrayOfT;
  }
  
  public static byte[] toByteArray(Collection<Byte> paramCollection) {
    byte[] arrayOfByte = new byte[paramCollection.size()];
    Iterator<Byte> iterator = paramCollection.iterator();
    for (int i = 0; iterator.hasNext(); i++)
      arrayOfByte[i] = ((Byte)iterator.next()).byteValue(); 
    return arrayOfByte;
  }
  
  public static int[] toIntArray(Collection<Integer> paramCollection) {
    int[] arrayOfInt = new int[paramCollection.size()];
    Iterator<Integer> iterator = paramCollection.iterator();
    for (int i = 0; iterator.hasNext(); i++)
      arrayOfInt[i] = ((Integer)iterator.next()).intValue(); 
    return arrayOfInt;
  }
  
  public static long[] toLongArray(Collection<Long> paramCollection) {
    long[] arrayOfLong = new long[paramCollection.size()];
    Iterator<Long> iterator = paramCollection.iterator();
    for (int i = 0; iterator.hasNext(); i++)
      arrayOfLong[i] = ((Long)iterator.next()).longValue(); 
    return arrayOfLong;
  }
  
  public static short[] toShortArray(Collection<Short> paramCollection) {
    short[] arrayOfShort = new short[paramCollection.size()];
    Iterator<Short> iterator = paramCollection.iterator();
    for (int i = 0; iterator.hasNext(); i++)
      arrayOfShort[i] = ((Short)iterator.next()).shortValue(); 
    return arrayOfShort;
  }
  
  public static UUID uuidFromBytes(byte[] paramArrayOfbyte, ByteOrder paramByteOrder) {
    boolean bool;
    if (paramArrayOfbyte.length == 16) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    ByteBuffer byteBuffer1 = ByteBuffer.wrap(paramArrayOfbyte).order(paramByteOrder);
    ByteBuffer byteBuffer2 = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
    byteBuffer2.putInt(byteBuffer1.getInt());
    byteBuffer2.putShort(byteBuffer1.getShort());
    byteBuffer2.putShort(byteBuffer1.getShort());
    byteBuffer2.rewind();
    long l = byteBuffer2.getLong();
    byteBuffer2.rewind();
    byteBuffer2.put(byteBuffer1);
    byteBuffer2.rewind();
    return new UUID(l, byteBuffer2.getLong());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Misc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Formatter;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexIndexOverflowException;

public abstract class MemberIdsSection extends UniformItemSection {
  public MemberIdsSection(String paramString, DexFile paramDexFile) {
    super(paramString, paramDexFile, 4);
  }
  
  private String getTooManyMembersMessage() {
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    Iterator<? extends Item> iterator = items().iterator();
    while (iterator.hasNext()) {
      String str = ((MemberIdItem)iterator.next()).getDefiningClass().getPackageName();
      AtomicInteger atomicInteger2 = (AtomicInteger)treeMap.get(str);
      AtomicInteger atomicInteger1 = atomicInteger2;
      if (atomicInteger2 == null) {
        atomicInteger1 = new AtomicInteger();
        treeMap.put(str, atomicInteger1);
      } 
      atomicInteger1.incrementAndGet();
    } 
    Formatter formatter = new Formatter();
  }
  
  protected void orderItems() {
    if (items().size() <= 65536) {
      Iterator<? extends Item> iterator = items().iterator();
      for (int i = 0; iterator.hasNext(); i++)
        ((MemberIdItem)iterator.next()).setIndex(i); 
      return;
    } 
    throw new DexIndexOverflowException(getTooManyMembersMessage());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\MemberIdsSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class LocalList extends FixedSizeList {
  private static final boolean DEBUG = false;
  
  public static final LocalList EMPTY = new LocalList(0);
  
  public LocalList(int paramInt) {
    super(paramInt);
  }
  
  private static void debugVerify(LocalList paramLocalList) {
    try {
      debugVerify0(paramLocalList);
      return;
    } catch (RuntimeException runtimeException) {
      int j = paramLocalList.size();
      for (int i = 0; i < j; i++)
        System.err.println(paramLocalList.get(i)); 
      throw runtimeException;
    } 
  }
  
  private static void debugVerify0(LocalList paramLocalList) {
    int j = paramLocalList.size();
    Entry[] arrayOfEntry = new Entry[65536];
    for (int i = 0; i < j; i++) {
      StringBuilder stringBuilder;
      Entry entry = paramLocalList.get(i);
      int k = entry.getRegister();
      if (entry.isStart()) {
        Entry entry1 = arrayOfEntry[k];
        if (entry1 == null || !entry.matches(entry1)) {
          arrayOfEntry[k] = entry;
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("redundant start at ");
          stringBuilder.append(Integer.toHexString(entry.getAddress()));
          stringBuilder.append(": got ");
          stringBuilder.append(entry);
          stringBuilder.append("; had ");
          stringBuilder.append(entry1);
          throw new RuntimeException(stringBuilder.toString());
        } 
      } else if (arrayOfEntry[k] != null) {
        int n = entry.getAddress();
        int m = i + 1;
        boolean bool = false;
        while (m < j) {
          Entry entry1 = stringBuilder.get(m);
          if (entry1.getAddress() != n)
            break; 
          if (entry1.getRegisterSpec().getReg() == k)
            if (entry1.isStart()) {
              if (entry.getDisposition() == Disposition.END_REPLACED) {
                bool = true;
              } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("improperly marked end at ");
                stringBuilder.append(Integer.toHexString(n));
                throw new RuntimeException(stringBuilder.toString());
              } 
            } else {
              stringBuilder = new StringBuilder();
              stringBuilder.append("redundant end at ");
              stringBuilder.append(Integer.toHexString(n));
              throw new RuntimeException(stringBuilder.toString());
            }  
          m++;
        } 
        if (bool || entry.getDisposition() != Disposition.END_REPLACED) {
          arrayOfEntry[k] = null;
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("improper end replacement claim at ");
          stringBuilder.append(Integer.toHexString(n));
          throw new RuntimeException(stringBuilder.toString());
        } 
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append("redundant end at ");
        stringBuilder.append(Integer.toHexString(entry.getAddress()));
        throw new RuntimeException(stringBuilder.toString());
      } 
    } 
  }
  
  public static LocalList make(DalvInsnList paramDalvInsnList) {
    int j = paramDalvInsnList.size();
    MakeState makeState = new MakeState(j);
    for (int i = 0; i < j; i++) {
      DalvInsn dalvInsn = paramDalvInsnList.get(i);
      if (dalvInsn instanceof LocalSnapshot) {
        RegisterSpecSet registerSpecSet = ((LocalSnapshot)dalvInsn).getLocals();
        makeState.snapshot(dalvInsn.getAddress(), registerSpecSet);
      } else if (dalvInsn instanceof LocalStart) {
        RegisterSpec registerSpec = ((LocalStart)dalvInsn).getLocal();
        makeState.startLocal(dalvInsn.getAddress(), registerSpec);
      } 
    } 
    return makeState.finish();
  }
  
  public void debugPrint(PrintStream paramPrintStream, String paramString) {
    int j = size();
    for (int i = 0; i < j; i++) {
      paramPrintStream.print(paramString);
      paramPrintStream.println(get(i));
    } 
  }
  
  public Entry get(int paramInt) {
    return (Entry)get0(paramInt);
  }
  
  public void set(int paramInt, Entry paramEntry) {
    set0(paramInt, paramEntry);
  }
  
  public enum Disposition {
    END_CLOBBERED_BY_NEXT, END_CLOBBERED_BY_PREV, END_MOVED, END_REPLACED, END_SIMPLY, START;
    
    static {
      END_REPLACED = new Disposition("END_REPLACED", 2);
      END_MOVED = new Disposition("END_MOVED", 3);
      END_CLOBBERED_BY_PREV = new Disposition("END_CLOBBERED_BY_PREV", 4);
      Disposition disposition = new Disposition("END_CLOBBERED_BY_NEXT", 5);
      END_CLOBBERED_BY_NEXT = disposition;
      $VALUES = new Disposition[] { START, END_SIMPLY, END_REPLACED, END_MOVED, END_CLOBBERED_BY_PREV, disposition };
    }
  }
  
  public static class Entry implements Comparable<Entry> {
    private final int address;
    
    private final LocalList.Disposition disposition;
    
    private final RegisterSpec spec;
    
    private final CstType type;
    
    public Entry(int param1Int, LocalList.Disposition param1Disposition, RegisterSpec param1RegisterSpec) {
      if (param1Int >= 0) {
        if (param1Disposition != null)
          try {
            LocalItem localItem = param1RegisterSpec.getLocalItem();
            if (localItem != null) {
              this.address = param1Int;
              this.disposition = param1Disposition;
              this.spec = param1RegisterSpec;
              this.type = CstType.intern(param1RegisterSpec.getType());
              return;
            } 
            throw new NullPointerException("spec.getLocalItem() == null");
          } catch (NullPointerException nullPointerException) {
            throw new NullPointerException("spec == null");
          }  
        throw new NullPointerException("disposition == null");
      } 
      throw new IllegalArgumentException("address < 0");
    }
    
    public int compareTo(Entry param1Entry) {
      int i = this.address;
      int j = param1Entry.address;
      byte b = -1;
      if (i < j)
        return -1; 
      if (i > j)
        return 1; 
      boolean bool = isStart();
      if (bool != param1Entry.isStart()) {
        if (bool)
          b = 1; 
        return b;
      } 
      return this.spec.compareTo(param1Entry.spec);
    }
    
    public boolean equals(Object param1Object) {
      boolean bool1 = param1Object instanceof Entry;
      boolean bool = false;
      if (!bool1)
        return false; 
      if (compareTo((Entry)param1Object) == 0)
        bool = true; 
      return bool;
    }
    
    public int getAddress() {
      return this.address;
    }
    
    public LocalList.Disposition getDisposition() {
      return this.disposition;
    }
    
    public CstString getName() {
      return this.spec.getLocalItem().getName();
    }
    
    public int getRegister() {
      return this.spec.getReg();
    }
    
    public RegisterSpec getRegisterSpec() {
      return this.spec;
    }
    
    public CstString getSignature() {
      return this.spec.getLocalItem().getSignature();
    }
    
    public CstType getType() {
      return this.type;
    }
    
    public boolean isStart() {
      return (this.disposition == LocalList.Disposition.START);
    }
    
    public boolean matches(Entry param1Entry) {
      return matches(param1Entry.spec);
    }
    
    public boolean matches(RegisterSpec param1RegisterSpec) {
      return this.spec.equalsUsingSimpleType(param1RegisterSpec);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Integer.toHexString(this.address));
      stringBuilder.append(" ");
      stringBuilder.append(this.disposition);
      stringBuilder.append(" ");
      stringBuilder.append(this.spec);
      return stringBuilder.toString();
    }
    
    public Entry withDisposition(LocalList.Disposition param1Disposition) {
      return (param1Disposition == this.disposition) ? this : new Entry(this.address, param1Disposition, this.spec);
    }
  }
  
  public static class MakeState {
    private int[] endIndices;
    
    private int lastAddress;
    
    private int nullResultCount;
    
    private RegisterSpecSet regs;
    
    private final ArrayList<LocalList.Entry> result;
    
    public MakeState(int param1Int) {
      this.result = new ArrayList<LocalList.Entry>(param1Int);
      this.nullResultCount = 0;
      this.regs = null;
      this.endIndices = null;
      this.lastAddress = 0;
    }
    
    private void aboutToProcess(int param1Int1, int param1Int2) {
      boolean bool;
      if (this.endIndices == null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (param1Int1 == this.lastAddress && !bool)
        return; 
      if (param1Int1 >= this.lastAddress) {
        if (bool || param1Int2 >= this.endIndices.length) {
          param1Int1 = param1Int2 + 1;
          RegisterSpecSet registerSpecSet = new RegisterSpecSet(param1Int1);
          int[] arrayOfInt = new int[param1Int1];
          Arrays.fill(arrayOfInt, -1);
          if (!bool) {
            registerSpecSet.putAll(this.regs);
            int[] arrayOfInt1 = this.endIndices;
            System.arraycopy(arrayOfInt1, 0, arrayOfInt, 0, arrayOfInt1.length);
          } 
          this.regs = registerSpecSet;
          this.endIndices = arrayOfInt;
        } 
        return;
      } 
      throw new RuntimeException("shouldn't happen");
    }
    
    private void add(int param1Int, LocalList.Disposition param1Disposition, RegisterSpec param1RegisterSpec) {
      int i = param1RegisterSpec.getReg();
      this.result.add(new LocalList.Entry(param1Int, param1Disposition, param1RegisterSpec));
      if (param1Disposition == LocalList.Disposition.START) {
        this.regs.put(param1RegisterSpec);
        this.endIndices[i] = -1;
        return;
      } 
      this.regs.remove(param1RegisterSpec);
      this.endIndices[i] = this.result.size() - 1;
    }
    
    private void addOrUpdateEnd(int param1Int, LocalList.Disposition param1Disposition, RegisterSpec param1RegisterSpec) {
      if (param1Disposition != LocalList.Disposition.START) {
        int i = param1RegisterSpec.getReg();
        i = this.endIndices[i];
        if (i >= 0) {
          LocalList.Entry entry = this.result.get(i);
          if (entry.getAddress() == param1Int && entry.getRegisterSpec().equals(param1RegisterSpec)) {
            this.result.set(i, entry.withDisposition(param1Disposition));
            this.regs.remove(param1RegisterSpec);
            return;
          } 
        } 
        endLocal(param1Int, param1RegisterSpec, param1Disposition);
        return;
      } 
      throw new RuntimeException("shouldn't happen");
    }
    
    private boolean checkForEmptyRange(int param1Int, RegisterSpec param1RegisterSpec) {
      LocalList.Entry entry;
      boolean bool;
      int j;
      int i = this.result.size() - 1;
      while (true) {
        bool = false;
        if (i >= 0) {
          LocalList.Entry entry1 = this.result.get(i);
          if (entry1 != null) {
            if (entry1.getAddress() != param1Int)
              return false; 
            if (entry1.matches(param1RegisterSpec))
              break; 
          } 
          i--;
          continue;
        } 
        break;
      } 
      this.regs.remove(param1RegisterSpec);
      ArrayList<LocalList.Entry> arrayList = this.result;
      RegisterSpec registerSpec = null;
      arrayList.set(i, null);
      this.nullResultCount++;
      int k = param1RegisterSpec.getReg();
      param1RegisterSpec = registerSpec;
      while (true) {
        j = i - 1;
        i = bool;
        if (j >= 0) {
          LocalList.Entry entry1 = this.result.get(j);
          if (entry1 == null) {
            i = j;
            LocalList.Entry entry2 = entry1;
            continue;
          } 
          i = j;
          entry = entry1;
          if (entry1.getRegisterSpec().getReg() == k) {
            i = 1;
            entry = entry1;
            break;
          } 
          continue;
        } 
        break;
      } 
      if (i != 0) {
        this.endIndices[k] = j;
        if (entry.getAddress() == param1Int)
          this.result.set(j, entry.withDisposition(LocalList.Disposition.END_SIMPLY)); 
      } 
      return true;
    }
    
    private static RegisterSpec filterSpec(RegisterSpec param1RegisterSpec) {
      RegisterSpec registerSpec = param1RegisterSpec;
      if (param1RegisterSpec != null) {
        registerSpec = param1RegisterSpec;
        if (param1RegisterSpec.getType() == Type.KNOWN_NULL)
          registerSpec = param1RegisterSpec.withType((TypeBearer)Type.OBJECT); 
      } 
      return registerSpec;
    }
    
    public void endLocal(int param1Int, RegisterSpec param1RegisterSpec) {
      endLocal(param1Int, param1RegisterSpec, LocalList.Disposition.END_SIMPLY);
    }
    
    public void endLocal(int param1Int, RegisterSpec param1RegisterSpec, LocalList.Disposition param1Disposition) {
      int i = param1RegisterSpec.getReg();
      param1RegisterSpec = filterSpec(param1RegisterSpec);
      aboutToProcess(param1Int, i);
      if (this.endIndices[i] >= 0)
        return; 
      if (checkForEmptyRange(param1Int, param1RegisterSpec))
        return; 
      add(param1Int, param1Disposition, param1RegisterSpec);
    }
    
    public LocalList finish() {
      boolean bool = false;
      aboutToProcess(2147483647, 0);
      int i = this.result.size();
      int j = i - this.nullResultCount;
      if (j == 0)
        return LocalList.EMPTY; 
      LocalList.Entry[] arrayOfEntry = new LocalList.Entry[j];
      if (i == j) {
        this.result.toArray(arrayOfEntry);
      } else {
        Iterator<LocalList.Entry> iterator = this.result.iterator();
        i = 0;
        while (iterator.hasNext()) {
          LocalList.Entry entry = iterator.next();
          if (entry != null) {
            arrayOfEntry[i] = entry;
            i++;
          } 
        } 
      } 
      Arrays.sort((Object[])arrayOfEntry);
      LocalList localList = new LocalList(j);
      for (i = bool; i < j; i++)
        localList.set(i, arrayOfEntry[i]); 
      localList.setImmutable();
      return localList;
    }
    
    public void snapshot(int param1Int, RegisterSpecSet param1RegisterSpecSet) {
      int j = param1RegisterSpecSet.getMaxSize();
      aboutToProcess(param1Int, j - 1);
      for (int i = 0; i < j; i++) {
        RegisterSpec registerSpec1 = this.regs.get(i);
        RegisterSpec registerSpec2 = filterSpec(param1RegisterSpecSet.get(i));
        if (registerSpec1 == null) {
          if (registerSpec2 != null)
            startLocal(param1Int, registerSpec2); 
        } else if (registerSpec2 == null) {
          endLocal(param1Int, registerSpec1);
        } else if (!registerSpec2.equalsUsingSimpleType(registerSpec1)) {
          endLocal(param1Int, registerSpec1);
          startLocal(param1Int, registerSpec2);
        } 
      } 
    }
    
    public void startLocal(int param1Int, RegisterSpec param1RegisterSpec) {
      int i = param1RegisterSpec.getReg();
      param1RegisterSpec = filterSpec(param1RegisterSpec);
      aboutToProcess(param1Int, i);
      RegisterSpec registerSpec1 = this.regs.get(i);
      if (param1RegisterSpec.equalsUsingSimpleType(registerSpec1))
        return; 
      RegisterSpec registerSpec2 = this.regs.findMatchingLocal(param1RegisterSpec);
      if (registerSpec2 != null)
        addOrUpdateEnd(param1Int, LocalList.Disposition.END_MOVED, registerSpec2); 
      int j = this.endIndices[i];
      if (registerSpec1 != null) {
        add(param1Int, LocalList.Disposition.END_REPLACED, registerSpec1);
      } else if (j >= 0) {
        LocalList.Entry entry = this.result.get(j);
        if (entry.getAddress() == param1Int) {
          if (entry.matches(param1RegisterSpec)) {
            this.result.set(j, null);
            this.nullResultCount++;
            this.regs.put(param1RegisterSpec);
            this.endIndices[i] = -1;
            return;
          } 
          entry = entry.withDisposition(LocalList.Disposition.END_REPLACED);
          this.result.set(j, entry);
        } 
      } 
      if (i > 0) {
        registerSpec1 = this.regs.get(i - 1);
        if (registerSpec1 != null && registerSpec1.isCategory2())
          addOrUpdateEnd(param1Int, LocalList.Disposition.END_CLOBBERED_BY_NEXT, registerSpec1); 
      } 
      if (param1RegisterSpec.isCategory2()) {
        registerSpec1 = this.regs.get(i + 1);
        if (registerSpec1 != null)
          addOrUpdateEnd(param1Int, LocalList.Disposition.END_CLOBBERED_BY_PREV, registerSpec1); 
      } 
      add(param1Int, LocalList.Disposition.START, param1RegisterSpec);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\LocalList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
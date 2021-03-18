package org.firstinspires.ftc.robotcore.internal.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;

public class TelemetryImpl implements Telemetry, TelemetryInternal {
  protected List<Runnable> actions;
  
  protected String captionValueSeparator;
  
  protected boolean clearOnAdd;
  
  protected List<String> composedLines;
  
  protected boolean isAutoClear;
  
  protected boolean isDirty;
  
  protected String itemSeparator;
  
  protected LineableContainer lines;
  
  protected LogImpl log;
  
  protected int msTransmissionInterval;
  
  protected OpMode opMode;
  
  protected final Object theLock = new Object();
  
  protected ElapsedTime transmissionTimer;
  
  public TelemetryImpl(OpMode paramOpMode) {
    this.opMode = paramOpMode;
    this.log = new LogImpl();
    resetTelemetryForOpMode();
  }
  
  protected static String getKey(int paramInt) {
    return String.format("\000%c", new Object[] { Integer.valueOf(paramInt + 384) });
  }
  
  public Object addAction(Runnable paramRunnable) {
    synchronized (this.theLock) {
      this.actions.add(paramRunnable);
      return paramRunnable;
    } 
  }
  
  public Telemetry.Item addData(String paramString, Object paramObject) {
    return this.lines.addItemAfter(null, paramString, new Value(paramObject));
  }
  
  public <T> Telemetry.Item addData(String paramString1, String paramString2, Func<T> paramFunc) {
    return this.lines.addItemAfter(null, paramString1, new Value<T>(paramString2, paramFunc));
  }
  
  public Telemetry.Item addData(String paramString1, String paramString2, Object... paramVarArgs) {
    return this.lines.addItemAfter(null, paramString1, new Value(paramString2, paramVarArgs));
  }
  
  public <T> Telemetry.Item addData(String paramString, Func<T> paramFunc) {
    return this.lines.addItemAfter(null, paramString, new Value<T>(paramFunc));
  }
  
  public Telemetry.Line addLine() {
    return this.lines.addLineAfter(null, "");
  }
  
  public Telemetry.Line addLine(String paramString) {
    return this.lines.addLineAfter(null, paramString);
  }
  
  public void clear() {
    synchronized (this.theLock) {
      this.clearOnAdd = false;
      markClean();
      this.lines.removeAllRecurse(new Predicate<ItemImpl>() {
            public boolean test(TelemetryImpl.ItemImpl param1ItemImpl) {
              return param1ItemImpl.isRetained() ^ true;
            }
          });
      return;
    } 
  }
  
  public void clearAll() {
    synchronized (this.theLock) {
      this.clearOnAdd = false;
      markClean();
      this.actions.clear();
      this.lines.removeAllRecurse(new Predicate<ItemImpl>() {
            public boolean test(TelemetryImpl.ItemImpl param1ItemImpl) {
              return true;
            }
          });
      return;
    } 
  }
  
  public String getCaptionValueSeparator() {
    return this.captionValueSeparator;
  }
  
  public String getItemSeparator() {
    return this.itemSeparator;
  }
  
  public int getMsTransmissionInterval() {
    return this.msTransmissionInterval;
  }
  
  public boolean isAutoClear() {
    return this.isAutoClear;
  }
  
  boolean isDirty() {
    return this.isDirty;
  }
  
  public Telemetry.Log log() {
    return this.log;
  }
  
  void markClean() {
    this.isDirty = false;
  }
  
  void markDirty() {
    this.isDirty = true;
  }
  
  protected void onAddData() {
    if (this.clearOnAdd) {
      clear();
      this.clearOnAdd = false;
    } 
    markClean();
  }
  
  public boolean removeAction(Object paramObject) {
    synchronized (this.theLock) {
      return this.actions.remove(paramObject);
    } 
  }
  
  public boolean removeItem(Telemetry.Item paramItem) {
    if (paramItem instanceof ItemImpl) {
      paramItem = paramItem;
      return ((ItemImpl)paramItem).parent.remove((Lineable)paramItem);
    } 
    return false;
  }
  
  public boolean removeLine(Telemetry.Line paramLine) {
    if (paramLine instanceof LineImpl) {
      paramLine = paramLine;
      return ((LineImpl)paramLine).parent.remove((Lineable)paramLine);
    } 
    return false;
  }
  
  public void resetTelemetryForOpMode() {
    this.lines = new LineableContainer();
    this.composedLines = new ArrayList<String>();
    this.actions = new LinkedList<Runnable>();
    this.log.reset();
    this.transmissionTimer = new ElapsedTime();
    this.isDirty = false;
    this.clearOnAdd = false;
    this.isAutoClear = true;
    this.msTransmissionInterval = 250;
    this.captionValueSeparator = " : ";
    this.itemSeparator = " | ";
  }
  
  protected void saveToTransmitter(boolean paramBoolean, TelemetryMessage paramTelemetryMessage) {
    boolean bool = false;
    paramTelemetryMessage.setSorted(false);
    if (paramBoolean) {
      this.composedLines = new ArrayList<String>();
      for (Lineable lineable : this.lines)
        this.composedLines.add(lineable.getComposed(paramBoolean)); 
    } 
    int i;
    for (i = 0; i < this.composedLines.size(); i++)
      paramTelemetryMessage.addData(getKey(i), this.composedLines.get(i)); 
    int k = this.log.size();
    int j = i;
    for (i = bool; i < k; i++) {
      String str;
      if (this.log.getDisplayOrder() == Telemetry.Log.DisplayOrder.OLDEST_FIRST) {
        str = this.log.get(i);
      } else {
        str = this.log.get(k - 1 - i);
      } 
      paramTelemetryMessage.addData(getKey(j), str);
      j++;
    } 
  }
  
  public void setAutoClear(boolean paramBoolean) {
    synchronized (this.theLock) {
      this.isAutoClear = paramBoolean;
      return;
    } 
  }
  
  public void setCaptionValueSeparator(String paramString) {
    synchronized (this.theLock) {
      this.captionValueSeparator = paramString;
      return;
    } 
  }
  
  public void setDisplayFormat(Telemetry.DisplayFormat paramDisplayFormat) {
    NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_SET_TELEM_DISPL_FORMAT", paramDisplayFormat.toString()));
  }
  
  public void setItemSeparator(String paramString) {
    synchronized (this.theLock) {
      this.itemSeparator = paramString;
      return;
    } 
  }
  
  public void setMsTransmissionInterval(int paramInt) {
    synchronized (this.theLock) {
      this.msTransmissionInterval = paramInt;
      return;
    } 
  }
  
  public void speak(String paramString) {
    speak(paramString, null, null);
  }
  
  public void speak(String paramString1, String paramString2, String paramString3) {
    RobotCoreCommandList.TextToSpeech textToSpeech = new RobotCoreCommandList.TextToSpeech(paramString1, paramString2, paramString3);
    NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_TEXT_TO_SPEECH", textToSpeech.serialize()));
  }
  
  protected boolean tryUpdate(UpdateReason paramUpdateReason) {
    synchronized (this.theLock) {
      boolean bool1;
      boolean bool2;
      boolean bool3;
      double d1 = this.transmissionTimer.milliseconds();
      double d2 = this.msTransmissionInterval;
      boolean bool4 = true;
      if (d1 > d2) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (paramUpdateReason == UpdateReason.USER || paramUpdateReason == UpdateReason.LOG || (paramUpdateReason == UpdateReason.IFDIRTY && (isDirty() || this.log.isDirty()))) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (paramUpdateReason == UpdateReason.USER || isDirty()) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      if (bool1 && bool2) {
        Iterator<Runnable> iterator = this.actions.iterator();
        while (iterator.hasNext())
          ((Runnable)iterator.next()).run(); 
        TelemetryMessage telemetryMessage = new TelemetryMessage();
        saveToTransmitter(bool3, telemetryMessage);
        if (telemetryMessage.hasData())
          OpModeManagerImpl.updateTelemetryNow(this.opMode, telemetryMessage); 
        this.log.markClean();
        markClean();
        this.transmissionTimer.reset();
        bool3 = bool4;
      } else {
        if (paramUpdateReason == UpdateReason.USER)
          markDirty(); 
        bool3 = false;
      } 
      if (paramUpdateReason == UpdateReason.USER)
        this.clearOnAdd = isAutoClear(); 
      return bool3;
    } 
  }
  
  public boolean tryUpdateIfDirty() {
    return tryUpdate(UpdateReason.IFDIRTY);
  }
  
  public boolean update() {
    return tryUpdate(UpdateReason.USER);
  }
  
  protected class ItemImpl implements Telemetry.Item, Lineable {
    String caption = null;
    
    final TelemetryImpl.LineableContainer parent;
    
    Boolean retained = null;
    
    TelemetryImpl.Value value = null;
    
    ItemImpl(TelemetryImpl.LineableContainer param1LineableContainer, String param1String, TelemetryImpl.Value param1Value) {
      this.parent = param1LineableContainer;
      this.caption = param1String;
      this.value = param1Value;
      this.retained = null;
    }
    
    public Telemetry.Item addData(String param1String, Object param1Object) {
      return this.parent.addItemAfter(this, param1String, new TelemetryImpl.Value(param1Object));
    }
    
    public <T> Telemetry.Item addData(String param1String1, String param1String2, Func<T> param1Func) {
      return this.parent.addItemAfter(this, param1String1, new TelemetryImpl.Value<T>(param1String2, param1Func));
    }
    
    public Telemetry.Item addData(String param1String1, String param1String2, Object... param1VarArgs) {
      return this.parent.addItemAfter(this, param1String1, new TelemetryImpl.Value(param1String2, param1VarArgs));
    }
    
    public <T> Telemetry.Item addData(String param1String, Func<T> param1Func) {
      return this.parent.addItemAfter(this, param1String, new TelemetryImpl.Value<T>(param1Func));
    }
    
    public String getCaption() {
      return this.caption;
    }
    
    public String getComposed(boolean param1Boolean) {
      synchronized (TelemetryImpl.this.theLock) {
        return String.format("%s%s%s", new Object[] { this.caption, this.this$0.getCaptionValueSeparator(), this.value.getComposed(param1Boolean) });
      } 
    }
    
    void internalSetValue(TelemetryImpl.Value param1Value) {
      synchronized (TelemetryImpl.this.theLock) {
        this.value = param1Value;
        return;
      } 
    }
    
    boolean isProducer() {
      synchronized (TelemetryImpl.this.theLock) {
        return this.value.isProducer();
      } 
    }
    
    public boolean isRetained() {
      synchronized (TelemetryImpl.this.theLock) {
        boolean bool;
        if (this.retained != null) {
          bool = this.retained.booleanValue();
        } else {
          bool = isProducer();
        } 
        return bool;
      } 
    }
    
    public Telemetry.Item setCaption(String param1String) {
      this.caption = param1String;
      return this;
    }
    
    public Telemetry.Item setRetained(Boolean param1Boolean) {
      synchronized (TelemetryImpl.this.theLock) {
        this.retained = param1Boolean;
        return this;
      } 
    }
    
    public Telemetry.Item setValue(Object param1Object) {
      internalSetValue(new TelemetryImpl.Value(param1Object));
      return this;
    }
    
    public <T> Telemetry.Item setValue(String param1String, Func<T> param1Func) {
      internalSetValue(new TelemetryImpl.Value<T>(param1String, param1Func));
      return this;
    }
    
    public Telemetry.Item setValue(String param1String, Object... param1VarArgs) {
      internalSetValue(new TelemetryImpl.Value(param1String, param1VarArgs));
      return this;
    }
    
    public <T> Telemetry.Item setValue(Func<T> param1Func) {
      internalSetValue(new TelemetryImpl.Value<T>(param1Func));
      return this;
    }
  }
  
  protected class LineImpl implements Telemetry.Line, Lineable {
    String lineCaption;
    
    TelemetryImpl.LineableContainer lineables;
    
    final TelemetryImpl.LineableContainer parent;
    
    LineImpl(String param1String, TelemetryImpl.LineableContainer param1LineableContainer) {
      this.parent = param1LineableContainer;
      this.lineCaption = param1String;
      this.lineables = new TelemetryImpl.LineableContainer();
    }
    
    public Telemetry.Item addData(String param1String, Object param1Object) {
      return this.lineables.addItemAfter(null, param1String, new TelemetryImpl.Value(param1Object));
    }
    
    public <T> Telemetry.Item addData(String param1String1, String param1String2, Func<T> param1Func) {
      return this.lineables.addItemAfter(null, param1String1, new TelemetryImpl.Value<T>(param1String2, param1Func));
    }
    
    public Telemetry.Item addData(String param1String1, String param1String2, Object... param1VarArgs) {
      return this.lineables.addItemAfter(null, param1String1, new TelemetryImpl.Value(param1String2, param1VarArgs));
    }
    
    public <T> Telemetry.Item addData(String param1String, Func<T> param1Func) {
      return this.lineables.addItemAfter(null, param1String, new TelemetryImpl.Value<T>(param1Func));
    }
    
    public String getComposed(boolean param1Boolean) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.lineCaption);
      Iterator<TelemetryImpl.Lineable> iterator = this.lineables.iterator();
      for (boolean bool = true; iterator.hasNext(); bool = false) {
        TelemetryImpl.Lineable lineable = iterator.next();
        if (!bool)
          stringBuilder.append(TelemetryImpl.this.getItemSeparator()); 
        stringBuilder.append(lineable.getComposed(param1Boolean));
      } 
      return stringBuilder.toString();
    }
  }
  
  protected static interface Lineable {
    String getComposed(boolean param1Boolean);
  }
  
  protected class LineableContainer implements Iterable<Lineable> {
    private ArrayList<TelemetryImpl.Lineable> list = new ArrayList<TelemetryImpl.Lineable>();
    
    Telemetry.Item addItemAfter(TelemetryImpl.Lineable param1Lineable, String param1String, TelemetryImpl.Value param1Value) {
      synchronized (TelemetryImpl.this.theLock) {
        int i;
        TelemetryImpl.this.onAddData();
        TelemetryImpl.ItemImpl itemImpl = new TelemetryImpl.ItemImpl(this, param1String, param1Value);
        if (param1Lineable == null) {
          i = this.list.size();
        } else {
          i = this.list.indexOf(param1Lineable) + 1;
        } 
        boundedAddToList(i, itemImpl);
        return itemImpl;
      } 
    }
    
    Telemetry.Line addLineAfter(TelemetryImpl.Lineable param1Lineable, String param1String) {
      synchronized (TelemetryImpl.this.theLock) {
        int i;
        TelemetryImpl.this.onAddData();
        TelemetryImpl.LineImpl lineImpl = new TelemetryImpl.LineImpl(param1String, this);
        if (param1Lineable == null) {
          i = this.list.size();
        } else {
          i = this.list.indexOf(param1Lineable) + 1;
        } 
        boundedAddToList(i, lineImpl);
        return lineImpl;
      } 
    }
    
    void boundedAddToList(int param1Int, TelemetryImpl.Lineable param1Lineable) {
      if (this.list.size() < 255)
        this.list.add(param1Int, param1Lineable); 
    }
    
    boolean isEmpty() {
      synchronized (TelemetryImpl.this.theLock) {
        return this.list.isEmpty();
      } 
    }
    
    public Iterator<TelemetryImpl.Lineable> iterator() {
      synchronized (TelemetryImpl.this.theLock) {
        return this.list.iterator();
      } 
    }
    
    boolean remove(TelemetryImpl.Lineable param1Lineable) {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl;
      //   4: getfield theLock : Ljava/lang/Object;
      //   7: astore_3
      //   8: aload_3
      //   9: monitorenter
      //   10: iconst_0
      //   11: istore_2
      //   12: iload_2
      //   13: aload_0
      //   14: getfield list : Ljava/util/ArrayList;
      //   17: invokevirtual size : ()I
      //   20: if_icmpge -> 48
      //   23: aload_0
      //   24: getfield list : Ljava/util/ArrayList;
      //   27: iload_2
      //   28: invokevirtual get : (I)Ljava/lang/Object;
      //   31: aload_1
      //   32: if_acmpne -> 57
      //   35: aload_0
      //   36: getfield list : Ljava/util/ArrayList;
      //   39: iload_2
      //   40: invokevirtual remove : (I)Ljava/lang/Object;
      //   43: pop
      //   44: aload_3
      //   45: monitorexit
      //   46: iconst_1
      //   47: ireturn
      //   48: aload_3
      //   49: monitorexit
      //   50: iconst_0
      //   51: ireturn
      //   52: astore_1
      //   53: aload_3
      //   54: monitorexit
      //   55: aload_1
      //   56: athrow
      //   57: iload_2
      //   58: iconst_1
      //   59: iadd
      //   60: istore_2
      //   61: goto -> 12
      // Exception table:
      //   from	to	target	type
      //   12	46	52	finally
      //   48	50	52	finally
      //   53	55	52	finally
    }
    
    boolean removeAllRecurse(Predicate<TelemetryImpl.ItemImpl> param1Predicate) {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl;
      //   4: getfield theLock : Ljava/lang/Object;
      //   7: astore #4
      //   9: aload #4
      //   11: monitorenter
      //   12: iconst_0
      //   13: istore_2
      //   14: iconst_0
      //   15: istore_3
      //   16: iload_2
      //   17: aload_0
      //   18: getfield list : Ljava/util/ArrayList;
      //   21: invokevirtual size : ()I
      //   24: if_icmpge -> 122
      //   27: aload_0
      //   28: getfield list : Ljava/util/ArrayList;
      //   31: iload_2
      //   32: invokevirtual get : (I)Ljava/lang/Object;
      //   35: checkcast org/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl$Lineable
      //   38: astore #5
      //   40: aload #5
      //   42: instanceof org/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl$LineImpl
      //   45: ifeq -> 88
      //   48: aload #5
      //   50: checkcast org/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl$LineImpl
      //   53: astore #5
      //   55: aload #5
      //   57: getfield lineables : Lorg/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl$LineableContainer;
      //   60: aload_1
      //   61: invokevirtual removeAllRecurse : (Lorg/firstinspires/ftc/robotcore/external/Predicate;)Z
      //   64: pop
      //   65: aload #5
      //   67: getfield lineables : Lorg/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl$LineableContainer;
      //   70: invokevirtual isEmpty : ()Z
      //   73: ifeq -> 138
      //   76: aload_0
      //   77: getfield list : Ljava/util/ArrayList;
      //   80: iload_2
      //   81: invokevirtual remove : (I)Ljava/lang/Object;
      //   84: pop
      //   85: goto -> 133
      //   88: aload #5
      //   90: instanceof org/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl$ItemImpl
      //   93: ifeq -> 138
      //   96: aload_1
      //   97: aload #5
      //   99: checkcast org/firstinspires/ftc/robotcore/internal/opmode/TelemetryImpl$ItemImpl
      //   102: invokeinterface test : (Ljava/lang/Object;)Z
      //   107: ifeq -> 138
      //   110: aload_0
      //   111: getfield list : Ljava/util/ArrayList;
      //   114: iload_2
      //   115: invokevirtual remove : (I)Ljava/lang/Object;
      //   118: pop
      //   119: goto -> 133
      //   122: aload #4
      //   124: monitorexit
      //   125: iload_3
      //   126: ireturn
      //   127: astore_1
      //   128: aload #4
      //   130: monitorexit
      //   131: aload_1
      //   132: athrow
      //   133: iconst_1
      //   134: istore_3
      //   135: goto -> 16
      //   138: iload_2
      //   139: iconst_1
      //   140: iadd
      //   141: istore_2
      //   142: goto -> 16
      // Exception table:
      //   from	to	target	type
      //   16	85	127	finally
      //   88	119	127	finally
      //   122	125	127	finally
      //   128	131	127	finally
    }
    
    int size() {
      synchronized (TelemetryImpl.this.theLock) {
        return this.list.size();
      } 
    }
  }
  
  protected class LogImpl implements Telemetry.Log {
    int capacity;
    
    Telemetry.Log.DisplayOrder displayOrder;
    
    List<String> entries;
    
    boolean isDirty;
    
    LogImpl() {
      reset();
    }
    
    public void add(String param1String) {
      add("%s", new Object[] { param1String });
    }
    
    public void add(String param1String, Object... param1VarArgs) {
      synchronized (getLock()) {
        param1String = String.format(param1String, param1VarArgs);
        this.entries.add(param1String);
        markDirty();
        prune();
        TelemetryImpl.this.tryUpdate(TelemetryImpl.UpdateReason.LOG);
        return;
      } 
    }
    
    public void clear() {
      synchronized (getLock()) {
        this.entries.clear();
        markDirty();
        return;
      } 
    }
    
    String get(int param1Int) {
      return this.entries.get(param1Int);
    }
    
    public int getCapacity() {
      return this.capacity;
    }
    
    public Telemetry.Log.DisplayOrder getDisplayOrder() {
      return this.displayOrder;
    }
    
    Object getLock() {
      return TelemetryImpl.this;
    }
    
    boolean isDirty() {
      return this.isDirty;
    }
    
    void markClean() {
      this.isDirty = false;
    }
    
    void markDirty() {
      this.isDirty = true;
    }
    
    void prune() {
      synchronized (getLock()) {
        while (this.entries.size() > this.capacity && this.entries.size() > 0)
          this.entries.remove(0); 
        return;
      } 
    }
    
    void reset() {
      this.entries = new ArrayList<String>();
      this.capacity = 9;
      this.isDirty = false;
      this.displayOrder = Telemetry.Log.DisplayOrder.OLDEST_FIRST;
    }
    
    public void setCapacity(int param1Int) {
      synchronized (getLock()) {
        this.capacity = param1Int;
        prune();
        return;
      } 
    }
    
    public void setDisplayOrder(Telemetry.Log.DisplayOrder param1DisplayOrder) {
      synchronized (getLock()) {
        this.displayOrder = param1DisplayOrder;
        return;
      } 
    }
    
    int size() {
      return this.entries.size();
    }
  }
  
  protected enum UpdateReason {
    IFDIRTY, LOG, USER;
    
    static {
      UpdateReason updateReason = new UpdateReason("IFDIRTY", 2);
      IFDIRTY = updateReason;
      $VALUES = new UpdateReason[] { USER, LOG, updateReason };
    }
  }
  
  protected class Value<T> {
    protected String composed = null;
    
    protected String format = null;
    
    protected Object[] formatArgs = null;
    
    protected Object value = null;
    
    protected Func<T> valueProducer = null;
    
    Value(Object param1Object) {
      this.value = param1Object;
    }
    
    Value(String param1String, Func<T> param1Func) {
      this.format = param1String;
      this.valueProducer = param1Func;
    }
    
    Value(String param1String, Object... param1VarArgs) {
      this.format = param1String;
      this.formatArgs = param1VarArgs;
    }
    
    Value(Func<T> param1Func) {
      this.valueProducer = param1Func;
    }
    
    protected String compose() {
      String str = this.format;
      if (str != null) {
        Object[] arrayOfObject = this.formatArgs;
        if (arrayOfObject != null)
          return String.format(str, arrayOfObject); 
        Func<T> func = this.valueProducer;
        if (func != null)
          return String.format(str, new Object[] { func.value() }); 
      } else {
        Object<T> object = (Object<T>)this.value;
        if (object != null)
          return object.toString(); 
        object = (Object<T>)this.valueProducer;
        if (object != null)
          return object.value().toString(); 
      } 
      return "";
    }
    
    String getComposed(boolean param1Boolean) {
      if (param1Boolean || this.composed == null)
        this.composed = compose(); 
      return this.composed;
    }
    
    boolean isProducer() {
      return (this.valueProducer != null);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\TelemetryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
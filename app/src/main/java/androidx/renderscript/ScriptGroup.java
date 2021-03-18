package androidx.renderscript;

import android.os.Build;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ScriptGroup extends BaseObj {
  private static final int MIN_API_VERSION = 23;
  
  private static final String TAG = "ScriptGroup";
  
  private List<Closure> mClosures;
  
  IO[] mInputs;
  
  private List<Input> mInputs2;
  
  private String mName;
  
  private ArrayList<Node> mNodes;
  
  IO[] mOutputs;
  
  private Future[] mOutputs2;
  
  private boolean mUseIncSupp;
  
  ScriptGroup(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
    this.mUseIncSupp = false;
    this.mNodes = new ArrayList<Node>();
  }
  
  ScriptGroup(RenderScript paramRenderScript, String paramString, List<Closure> paramList, List<Input> paramList1, Future[] paramArrayOfFuture) {
    super(0L, paramRenderScript);
    int i = 0;
    this.mUseIncSupp = false;
    this.mNodes = new ArrayList<Node>();
    if (Build.VERSION.SDK_INT >= 23 || !paramRenderScript.isUseNative()) {
      this.mName = paramString;
      this.mClosures = paramList;
      this.mInputs2 = paramList1;
      this.mOutputs2 = paramArrayOfFuture;
      int j = paramList.size();
      long[] arrayOfLong = new long[j];
      while (i < j) {
        arrayOfLong[i] = ((Closure)paramList.get(i)).getID(paramRenderScript);
        i++;
      } 
      setID(paramRenderScript.nScriptGroup2Create(paramString, paramRenderScript.getApplicationContext().getCacheDir().toString(), arrayOfLong));
      return;
    } 
    throw new RSRuntimeException("ScriptGroup2 not supported in this API level");
  }
  
  @Deprecated
  public void execute() {
    if (!this.mUseIncSupp) {
      this.mRS.nScriptGroupExecute(getID(this.mRS));
      return;
    } 
    int i;
    for (i = 0; i < this.mNodes.size(); i++) {
      Node node = this.mNodes.get(i);
      for (int j = 0; j < node.mOutputs.size(); j++) {
        ConnectLine connectLine = node.mOutputs.get(j);
        if (connectLine.mAllocation == null) {
          Allocation allocation = Allocation.createTyped(this.mRS, connectLine.mAllocationType, Allocation.MipmapControl.MIPMAP_NONE, 1);
          connectLine.mAllocation = allocation;
          for (int k = j + 1; k < node.mOutputs.size(); k++) {
            if (((ConnectLine)node.mOutputs.get(k)).mFrom == connectLine.mFrom)
              ((ConnectLine)node.mOutputs.get(k)).mAllocation = allocation; 
          } 
        } 
      } 
    } 
    for (Node node : this.mNodes) {
      for (Script.KernelID kernelID : node.mKernels) {
        Iterator<ConnectLine> iterator1 = node.mInputs.iterator();
        Allocation allocation1 = null;
        while (iterator1.hasNext()) {
          ConnectLine connectLine = iterator1.next();
          if (connectLine.mToK == kernelID)
            allocation1 = connectLine.mAllocation; 
        } 
        IO[] arrayOfIO = this.mInputs;
        null = arrayOfIO.length;
        i = 0;
        Allocation allocation2 = allocation1;
        while (i < null) {
          IO iO = arrayOfIO[i];
          if (iO.mKID == kernelID)
            allocation2 = iO.mAllocation; 
          i++;
        } 
        Iterator<ConnectLine> iterator2 = node.mOutputs.iterator();
        allocation1 = null;
        while (iterator2.hasNext()) {
          ConnectLine connectLine = iterator2.next();
          if (connectLine.mFrom == kernelID)
            allocation1 = connectLine.mAllocation; 
        } 
        for (IO iO : this.mOutputs) {
          if (iO.mKID == kernelID)
            allocation1 = iO.mAllocation; 
        } 
        kernelID.mScript.forEach(kernelID.mSlot, allocation2, allocation1, (FieldPacker)null);
      } 
    } 
  }
  
  public Object[] execute(Object... paramVarArgs) {
    if (paramVarArgs.length < this.mInputs2.size()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(toString());
      stringBuilder.append(" receives ");
      stringBuilder.append(paramVarArgs.length);
      stringBuilder.append(" inputs, less than expected ");
      stringBuilder.append(this.mInputs2.size());
      Log.e("ScriptGroup", stringBuilder.toString());
      return null;
    } 
    if (paramVarArgs.length > this.mInputs2.size()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(toString());
      stringBuilder.append(" receives ");
      stringBuilder.append(paramVarArgs.length);
      stringBuilder.append(" inputs, more than expected ");
      stringBuilder.append(this.mInputs2.size());
      Log.i("ScriptGroup", stringBuilder.toString());
    } 
    int j = 0;
    int i;
    for (i = 0; i < this.mInputs2.size(); i++) {
      Object object = paramVarArgs[i];
      if (object instanceof Future || object instanceof Input) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toString());
        stringBuilder.append(": input ");
        stringBuilder.append(i);
        stringBuilder.append(" is a future or unbound value");
        Log.e("ScriptGroup", stringBuilder.toString());
        return null;
      } 
      ((Input)this.mInputs2.get(i)).set(object);
    } 
    this.mRS.nScriptGroup2Execute(getID(this.mRS));
    Future[] arrayOfFuture = this.mOutputs2;
    Object[] arrayOfObject = new Object[arrayOfFuture.length];
    int k = arrayOfFuture.length;
    for (i = 0; j < k; i++) {
      Object object2 = arrayOfFuture[j].getValue();
      Object object1 = object2;
      if (object2 instanceof Input)
        object1 = ((Input)object2).get(); 
      arrayOfObject[i] = object1;
      j++;
    } 
    return arrayOfObject;
  }
  
  @Deprecated
  public void setInput(Script.KernelID paramKernelID, Allocation paramAllocation) {
    int i = 0;
    while (true) {
      IO[] arrayOfIO = this.mInputs;
      if (i < arrayOfIO.length) {
        if ((arrayOfIO[i]).mKID == paramKernelID) {
          (this.mInputs[i]).mAllocation = paramAllocation;
          if (!this.mUseIncSupp)
            this.mRS.nScriptGroupSetInput(getID(this.mRS), paramKernelID.getID(this.mRS), this.mRS.safeID(paramAllocation)); 
          return;
        } 
        i++;
        continue;
      } 
      throw new RSIllegalArgumentException("Script not found");
    } 
  }
  
  @Deprecated
  public void setOutput(Script.KernelID paramKernelID, Allocation paramAllocation) {
    int i = 0;
    while (true) {
      IO[] arrayOfIO = this.mOutputs;
      if (i < arrayOfIO.length) {
        if ((arrayOfIO[i]).mKID == paramKernelID) {
          (this.mOutputs[i]).mAllocation = paramAllocation;
          if (!this.mUseIncSupp)
            this.mRS.nScriptGroupSetOutput(getID(this.mRS), paramKernelID.getID(this.mRS), this.mRS.safeID(paramAllocation)); 
          return;
        } 
        i++;
        continue;
      } 
      throw new RSIllegalArgumentException("Script not found");
    } 
  }
  
  public static final class Binding {
    private final Script.FieldID mField;
    
    private final Object mValue;
    
    public Binding(Script.FieldID param1FieldID, Object param1Object) {
      this.mField = param1FieldID;
      this.mValue = param1Object;
    }
    
    public Script.FieldID getField() {
      return this.mField;
    }
    
    public Object getValue() {
      return this.mValue;
    }
  }
  
  @Deprecated
  public static final class Builder {
    private int mKernelCount;
    
    private ArrayList<ScriptGroup.ConnectLine> mLines = new ArrayList<ScriptGroup.ConnectLine>();
    
    private ArrayList<ScriptGroup.Node> mNodes = new ArrayList<ScriptGroup.Node>();
    
    private RenderScript mRS;
    
    private boolean mUseIncSupp = false;
    
    public Builder(RenderScript param1RenderScript) {
      this.mRS = param1RenderScript;
    }
    
    private boolean calcOrder() {
      Iterator<ScriptGroup.Node> iterator = this.mNodes.iterator();
      boolean bool = true;
      while (iterator.hasNext()) {
        ScriptGroup.Node node = iterator.next();
        if (node.mInputs.size() == 0) {
          Iterator<ScriptGroup.Node> iterator1 = this.mNodes.iterator();
          while (iterator1.hasNext())
            ((ScriptGroup.Node)iterator1.next()).mSeen = false; 
          bool &= calcOrderRecurse(node, 1);
        } 
      } 
      Collections.sort(this.mNodes, new Comparator<ScriptGroup.Node>() {
            public int compare(ScriptGroup.Node param2Node1, ScriptGroup.Node param2Node2) {
              return param2Node1.mOrder - param2Node2.mOrder;
            }
          });
      return bool;
    }
    
    private boolean calcOrderRecurse(ScriptGroup.Node param1Node, int param1Int) {
      param1Node.mSeen = true;
      if (param1Node.mOrder < param1Int)
        param1Node.mOrder = param1Int; 
      Iterator<ScriptGroup.ConnectLine> iterator = param1Node.mOutputs.iterator();
      boolean bool;
      for (bool = true; iterator.hasNext(); bool &= calcOrderRecurse(node, param1Node.mOrder + 1)) {
        ScriptGroup.Node node;
        ScriptGroup.ConnectLine connectLine = iterator.next();
        if (connectLine.mToF != null) {
          node = findNode(connectLine.mToF.mScript);
        } else {
          node = findNode(((ScriptGroup.ConnectLine)node).mToK.mScript);
        } 
        if (node.mSeen)
          return false; 
      } 
      return bool;
    }
    
    private ScriptGroup.Node findNode(Script.KernelID param1KernelID) {
      for (int i = 0; i < this.mNodes.size(); i++) {
        ScriptGroup.Node node = this.mNodes.get(i);
        for (int j = 0; j < node.mKernels.size(); j++) {
          if (param1KernelID == node.mKernels.get(j))
            return node; 
        } 
      } 
      return null;
    }
    
    private ScriptGroup.Node findNode(Script param1Script) {
      for (int i = 0; i < this.mNodes.size(); i++) {
        if (param1Script == ((ScriptGroup.Node)this.mNodes.get(i)).mScript)
          return this.mNodes.get(i); 
      } 
      return null;
    }
    
    private void mergeDAGs(int param1Int1, int param1Int2) {
      for (int i = 0; i < this.mNodes.size(); i++) {
        if (((ScriptGroup.Node)this.mNodes.get(i)).dagNumber == param1Int2)
          ((ScriptGroup.Node)this.mNodes.get(i)).dagNumber = param1Int1; 
      } 
    }
    
    private void validateCycle(ScriptGroup.Node param1Node1, ScriptGroup.Node param1Node2) {
      for (int i = 0; i < param1Node1.mOutputs.size(); i++) {
        ScriptGroup.ConnectLine connectLine = param1Node1.mOutputs.get(i);
        if (connectLine.mToK != null) {
          ScriptGroup.Node node = findNode(connectLine.mToK.mScript);
          if (!node.equals(param1Node2)) {
            validateCycle(node, param1Node2);
          } else {
            throw new RSInvalidStateException("Loops in group not allowed.");
          } 
        } 
        if (connectLine.mToF != null) {
          ScriptGroup.Node node = findNode(connectLine.mToF.mScript);
          if (!node.equals(param1Node2)) {
            validateCycle(node, param1Node2);
          } else {
            throw new RSInvalidStateException("Loops in group not allowed.");
          } 
        } 
      } 
    }
    
    private void validateDAG() {
      boolean bool = false;
      int i;
      for (i = 0; i < this.mNodes.size(); i++) {
        ScriptGroup.Node node = this.mNodes.get(i);
        if (node.mInputs.size() == 0)
          if (node.mOutputs.size() != 0 || this.mNodes.size() <= 1) {
            validateDAGRecurse(node, i + 1);
          } else {
            throw new RSInvalidStateException("Groups cannot contain unconnected scripts");
          }  
      } 
      int j = ((ScriptGroup.Node)this.mNodes.get(0)).dagNumber;
      i = bool;
      while (i < this.mNodes.size()) {
        if (((ScriptGroup.Node)this.mNodes.get(i)).dagNumber == j) {
          i++;
          continue;
        } 
        throw new RSInvalidStateException("Multiple DAGs in group not allowed.");
      } 
    }
    
    private void validateDAGRecurse(ScriptGroup.Node param1Node, int param1Int) {
      if (param1Node.dagNumber != 0 && param1Node.dagNumber != param1Int) {
        mergeDAGs(param1Node.dagNumber, param1Int);
        return;
      } 
      param1Node.dagNumber = param1Int;
      for (int i = 0; i < param1Node.mOutputs.size(); i++) {
        ScriptGroup.ConnectLine connectLine = param1Node.mOutputs.get(i);
        if (connectLine.mToK != null)
          validateDAGRecurse(findNode(connectLine.mToK.mScript), param1Int); 
        if (connectLine.mToF != null)
          validateDAGRecurse(findNode(connectLine.mToF.mScript), param1Int); 
      } 
    }
    
    public Builder addConnection(Type param1Type, Script.KernelID param1KernelID, Script.FieldID param1FieldID) {
      ScriptGroup.Node node = findNode(param1KernelID);
      if (node != null) {
        ScriptGroup.Node node1 = findNode(param1FieldID.mScript);
        if (node1 != null) {
          ScriptGroup.ConnectLine connectLine = new ScriptGroup.ConnectLine(param1Type, param1KernelID, param1FieldID);
          this.mLines.add(new ScriptGroup.ConnectLine(param1Type, param1KernelID, param1FieldID));
          node.mOutputs.add(connectLine);
          node1.mInputs.add(connectLine);
          validateCycle(node, node);
          return this;
        } 
        throw new RSInvalidStateException("To script not found.");
      } 
      throw new RSInvalidStateException("From script not found.");
    }
    
    public Builder addConnection(Type param1Type, Script.KernelID param1KernelID1, Script.KernelID param1KernelID2) {
      ScriptGroup.Node node = findNode(param1KernelID1);
      if (node != null) {
        ScriptGroup.Node node1 = findNode(param1KernelID2);
        if (node1 != null) {
          ScriptGroup.ConnectLine connectLine = new ScriptGroup.ConnectLine(param1Type, param1KernelID1, param1KernelID2);
          this.mLines.add(new ScriptGroup.ConnectLine(param1Type, param1KernelID1, param1KernelID2));
          node.mOutputs.add(connectLine);
          node1.mInputs.add(connectLine);
          validateCycle(node, node);
          return this;
        } 
        throw new RSInvalidStateException("To script not found.");
      } 
      throw new RSInvalidStateException("From script not found.");
    }
    
    public Builder addKernel(Script.KernelID param1KernelID) {
      if (this.mLines.size() == 0) {
        if (param1KernelID.mScript.isIncSupp())
          this.mUseIncSupp = true; 
        if (findNode(param1KernelID) != null)
          return this; 
        this.mKernelCount++;
        ScriptGroup.Node node2 = findNode(param1KernelID.mScript);
        ScriptGroup.Node node1 = node2;
        if (node2 == null) {
          node1 = new ScriptGroup.Node(param1KernelID.mScript);
          this.mNodes.add(node1);
        } 
        node1.mKernels.add(param1KernelID);
        return this;
      } 
      throw new RSInvalidStateException("Kernels may not be added once connections exist.");
    }
    
    public ScriptGroup create() {
      if (this.mNodes.size() != 0) {
        boolean bool = false;
        int i;
        for (i = 0; i < this.mNodes.size(); i++)
          ((ScriptGroup.Node)this.mNodes.get(i)).dagNumber = 0; 
        validateDAG();
        ArrayList<ScriptGroup.IO> arrayList1 = new ArrayList();
        ArrayList<ScriptGroup.IO> arrayList2 = new ArrayList();
        long[] arrayOfLong = new long[this.mKernelCount];
        i = 0;
        int j = i;
        while (i < this.mNodes.size()) {
          ScriptGroup.Node node = this.mNodes.get(i);
          int k = 0;
          while (k < node.mKernels.size()) {
            Script.KernelID kernelID = node.mKernels.get(k);
            arrayOfLong[j] = kernelID.getID(this.mRS);
            int n = 0;
            int m = n;
            while (n < node.mInputs.size()) {
              if (((ScriptGroup.ConnectLine)node.mInputs.get(n)).mToK == kernelID)
                m = 1; 
              n++;
            } 
            n = 0;
            int i1 = n;
            while (n < node.mOutputs.size()) {
              if (((ScriptGroup.ConnectLine)node.mOutputs.get(n)).mFrom == kernelID)
                i1 = 1; 
              n++;
            } 
            if (m == 0)
              arrayList1.add(new ScriptGroup.IO(kernelID)); 
            if (i1 == 0)
              arrayList2.add(new ScriptGroup.IO(kernelID)); 
            k++;
            j++;
          } 
          i++;
        } 
        if (j == this.mKernelCount) {
          boolean bool1 = this.mUseIncSupp;
          long l = 0L;
          if (!bool1) {
            long[] arrayOfLong1 = new long[this.mLines.size()];
            long[] arrayOfLong2 = new long[this.mLines.size()];
            long[] arrayOfLong3 = new long[this.mLines.size()];
            long[] arrayOfLong4 = new long[this.mLines.size()];
            for (i = 0; i < this.mLines.size(); i++) {
              ScriptGroup.ConnectLine connectLine = this.mLines.get(i);
              arrayOfLong1[i] = connectLine.mFrom.getID(this.mRS);
              if (connectLine.mToK != null)
                arrayOfLong2[i] = connectLine.mToK.getID(this.mRS); 
              if (connectLine.mToF != null)
                arrayOfLong3[i] = connectLine.mToF.getID(this.mRS); 
              arrayOfLong4[i] = connectLine.mAllocationType.getID(this.mRS);
            } 
            l = this.mRS.nScriptGroupCreate(arrayOfLong, arrayOfLong1, arrayOfLong2, arrayOfLong3, arrayOfLong4);
            if (l == 0L)
              throw new RSRuntimeException("Object creation error, should not happen."); 
          } else {
            calcOrder();
          } 
          ScriptGroup scriptGroup = new ScriptGroup(l, this.mRS);
          scriptGroup.mOutputs = new ScriptGroup.IO[arrayList2.size()];
          for (i = 0; i < arrayList2.size(); i++)
            scriptGroup.mOutputs[i] = arrayList2.get(i); 
          scriptGroup.mInputs = new ScriptGroup.IO[arrayList1.size()];
          for (i = bool; i < arrayList1.size(); i++)
            scriptGroup.mInputs[i] = arrayList1.get(i); 
          ScriptGroup.access$002(scriptGroup, this.mNodes);
          ScriptGroup.access$102(scriptGroup, this.mUseIncSupp);
          return scriptGroup;
        } 
        throw new RSRuntimeException("Count mismatch, should not happen.");
      } 
      throw new RSInvalidStateException("Empty script groups are not allowed");
    }
  }
  
  class null implements Comparator<Node> {
    public int compare(ScriptGroup.Node param1Node1, ScriptGroup.Node param1Node2) {
      return param1Node1.mOrder - param1Node2.mOrder;
    }
  }
  
  public static final class Builder2 {
    private static final String TAG = "ScriptGroup.Builder2";
    
    List<ScriptGroup.Closure> mClosures;
    
    List<ScriptGroup.Input> mInputs;
    
    RenderScript mRS;
    
    public Builder2(RenderScript param1RenderScript) {
      this.mRS = param1RenderScript;
      this.mClosures = new ArrayList<ScriptGroup.Closure>();
      this.mInputs = new ArrayList<ScriptGroup.Input>();
    }
    
    private ScriptGroup.Closure addInvokeInternal(Script.InvokeID param1InvokeID, Object[] param1ArrayOfObject, Map<Script.FieldID, Object> param1Map) {
      ScriptGroup.Closure closure = new ScriptGroup.Closure(this.mRS, param1InvokeID, param1ArrayOfObject, param1Map);
      this.mClosures.add(closure);
      return closure;
    }
    
    private ScriptGroup.Closure addKernelInternal(Script.KernelID param1KernelID, Type param1Type, Object[] param1ArrayOfObject, Map<Script.FieldID, Object> param1Map) {
      ScriptGroup.Closure closure = new ScriptGroup.Closure(this.mRS, param1KernelID, param1Type, param1ArrayOfObject, param1Map);
      this.mClosures.add(closure);
      return closure;
    }
    
    private boolean seperateArgsAndBindings(Object[] param1ArrayOfObject, ArrayList<Object> param1ArrayList, Map<Script.FieldID, Object> param1Map) {
      int j;
      int i = 0;
      while (true) {
        j = i;
        if (i < param1ArrayOfObject.length) {
          if (param1ArrayOfObject[i] instanceof ScriptGroup.Binding) {
            j = i;
            break;
          } 
          param1ArrayList.add(param1ArrayOfObject[i]);
          i++;
          continue;
        } 
        break;
      } 
      while (j < param1ArrayOfObject.length) {
        if (!(param1ArrayOfObject[j] instanceof ScriptGroup.Binding))
          return false; 
        ScriptGroup.Binding binding = (ScriptGroup.Binding)param1ArrayOfObject[j];
        param1Map.put(binding.getField(), binding.getValue());
        j++;
      } 
      return true;
    }
    
    public ScriptGroup.Input addInput() {
      ScriptGroup.Input input = new ScriptGroup.Input();
      this.mInputs.add(input);
      return input;
    }
    
    public ScriptGroup.Closure addInvoke(Script.InvokeID param1InvokeID, Object... param1VarArgs) {
      ArrayList<Object> arrayList = new ArrayList();
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      return !seperateArgsAndBindings(param1VarArgs, arrayList, (Map)hashMap) ? null : addInvokeInternal(param1InvokeID, arrayList.toArray(), (Map)hashMap);
    }
    
    public ScriptGroup.Closure addKernel(Script.KernelID param1KernelID, Type param1Type, Object... param1VarArgs) {
      ArrayList<Object> arrayList = new ArrayList();
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      return !seperateArgsAndBindings(param1VarArgs, arrayList, (Map)hashMap) ? null : addKernelInternal(param1KernelID, param1Type, arrayList.toArray(), (Map)hashMap);
    }
    
    public ScriptGroup create(String param1String, ScriptGroup.Future... param1VarArgs) {
      if (param1String != null && !param1String.isEmpty() && param1String.length() <= 100 && param1String.equals(param1String.replaceAll("[^a-zA-Z0-9-]", "_")))
        return new ScriptGroup(this.mRS, param1String, this.mClosures, this.mInputs, param1VarArgs); 
      throw new RSIllegalArgumentException("invalid script group name");
    }
  }
  
  public static final class Closure extends BaseObj {
    private static final String TAG = "Closure";
    
    private Object[] mArgs;
    
    private Map<Script.FieldID, Object> mBindings;
    
    private FieldPacker mFP;
    
    private Map<Script.FieldID, ScriptGroup.Future> mGlobalFuture;
    
    private ScriptGroup.Future mReturnFuture;
    
    private Allocation mReturnValue;
    
    Closure(long param1Long, RenderScript param1RenderScript) {
      super(param1Long, param1RenderScript);
    }
    
    Closure(RenderScript param1RenderScript, Script.InvokeID param1InvokeID, Object[] param1ArrayOfObject, Map<Script.FieldID, Object> param1Map) {
      super(0L, param1RenderScript);
      if (Build.VERSION.SDK_INT >= 23 || !param1RenderScript.isUseNative()) {
        this.mFP = FieldPacker.createFromArray(param1ArrayOfObject);
        this.mArgs = param1ArrayOfObject;
        this.mBindings = param1Map;
        this.mGlobalFuture = new HashMap<Script.FieldID, ScriptGroup.Future>();
        int i = param1Map.size();
        long[] arrayOfLong1 = new long[i];
        long[] arrayOfLong2 = new long[i];
        int[] arrayOfInt = new int[i];
        long[] arrayOfLong3 = new long[i];
        long[] arrayOfLong4 = new long[i];
        Iterator<Map.Entry> iterator = param1Map.entrySet().iterator();
        for (i = 0; iterator.hasNext(); i++) {
          Map.Entry entry = iterator.next();
          Object object = entry.getValue();
          Script.FieldID fieldID = (Script.FieldID)entry.getKey();
          arrayOfLong1[i] = fieldID.getID(param1RenderScript);
          retrieveValueAndDependenceInfo(param1RenderScript, i, fieldID, object, arrayOfLong2, arrayOfInt, arrayOfLong3, arrayOfLong4);
        } 
        setID(param1RenderScript.nInvokeClosureCreate(param1InvokeID.getID(param1RenderScript), this.mFP.getData(), arrayOfLong1, arrayOfLong2, arrayOfInt));
        return;
      } 
      throw new RSRuntimeException("ScriptGroup2 not supported in this API level");
    }
    
    Closure(RenderScript param1RenderScript, Script.KernelID param1KernelID, Type param1Type, Object[] param1ArrayOfObject, Map<Script.FieldID, Object> param1Map) {
      super(0L, param1RenderScript);
      if (Build.VERSION.SDK_INT >= 23 || !param1RenderScript.isUseNative()) {
        this.mArgs = param1ArrayOfObject;
        this.mReturnValue = Allocation.createTyped(param1RenderScript, param1Type);
        this.mBindings = param1Map;
        this.mGlobalFuture = new HashMap<Script.FieldID, ScriptGroup.Future>();
        int i = param1ArrayOfObject.length + param1Map.size();
        long[] arrayOfLong4 = new long[i];
        long[] arrayOfLong3 = new long[i];
        int[] arrayOfInt = new int[i];
        long[] arrayOfLong2 = new long[i];
        long[] arrayOfLong1 = new long[i];
        for (i = 0; i < param1ArrayOfObject.length; i++) {
          arrayOfLong4[i] = 0L;
          retrieveValueAndDependenceInfo(param1RenderScript, i, (Script.FieldID)null, param1ArrayOfObject[i], arrayOfLong3, arrayOfInt, arrayOfLong2, arrayOfLong1);
        } 
        for (Map.Entry<Script.FieldID, Object> entry : param1Map.entrySet()) {
          param1Map = (Map<Script.FieldID, Object>)entry.getValue();
          Script.FieldID fieldID = (Script.FieldID)entry.getKey();
          arrayOfLong4[i] = fieldID.getID(param1RenderScript);
          retrieveValueAndDependenceInfo(param1RenderScript, i, fieldID, param1Map, arrayOfLong3, arrayOfInt, arrayOfLong2, arrayOfLong1);
          i++;
        } 
        setID(param1RenderScript.nClosureCreate(param1KernelID.getID(param1RenderScript), this.mReturnValue.getID(param1RenderScript), arrayOfLong4, arrayOfLong3, arrayOfInt, arrayOfLong2, arrayOfLong1));
        return;
      } 
      throw new RSRuntimeException("ScriptGroup2 not supported in this API level");
    }
    
    private void retrieveValueAndDependenceInfo(RenderScript param1RenderScript, int param1Int, Script.FieldID param1FieldID, Object param1Object, long[] param1ArrayOflong1, int[] param1ArrayOfint, long[] param1ArrayOflong2, long[] param1ArrayOflong3) {
      ScriptGroup.Input input;
      Script.FieldID fieldID;
      if (param1Object instanceof ScriptGroup.Future) {
        long l;
        ScriptGroup.Future future = (ScriptGroup.Future)param1Object;
        param1Object = future.getValue();
        param1ArrayOflong2[param1Int] = future.getClosure().getID(param1RenderScript);
        fieldID = future.getFieldID();
        if (fieldID != null) {
          l = fieldID.getID(param1RenderScript);
        } else {
          l = 0L;
        } 
        param1ArrayOflong3[param1Int] = l;
      } else {
        fieldID[param1Int] = 0L;
        param1ArrayOflong3[param1Int] = 0L;
      } 
      if (param1Object instanceof ScriptGroup.Input) {
        input = (ScriptGroup.Input)param1Object;
        if (param1Int < this.mArgs.length) {
          input.addReference(this, param1Int);
        } else {
          input.addReference(this, param1FieldID);
        } 
        param1ArrayOflong1[param1Int] = 0L;
        param1ArrayOfint[param1Int] = 0;
        return;
      } 
      ValueAndSize valueAndSize = new ValueAndSize((RenderScript)input, param1Object);
      param1ArrayOflong1[param1Int] = valueAndSize.value;
      param1ArrayOfint[param1Int] = valueAndSize.size;
    }
    
    public ScriptGroup.Future getGlobal(Script.FieldID param1FieldID) {
      ScriptGroup.Future future = this.mGlobalFuture.get(param1FieldID);
      Object object = future;
      if (future == null) {
        future = (ScriptGroup.Future)this.mBindings.get(param1FieldID);
        object = future;
        if (future instanceof ScriptGroup.Future)
          object = future.getValue(); 
        object = new ScriptGroup.Future(this, param1FieldID, object);
        this.mGlobalFuture.put(param1FieldID, object);
      } 
      return (ScriptGroup.Future)object;
    }
    
    public ScriptGroup.Future getReturn() {
      if (this.mReturnFuture == null)
        this.mReturnFuture = new ScriptGroup.Future(this, null, this.mReturnValue); 
      return this.mReturnFuture;
    }
    
    void setArg(int param1Int, Object param1Object) {
      Object object = param1Object;
      if (param1Object instanceof ScriptGroup.Future)
        object = ((ScriptGroup.Future)param1Object).getValue(); 
      this.mArgs[param1Int] = object;
      param1Object = new ValueAndSize(this.mRS, object);
      this.mRS.nClosureSetArg(getID(this.mRS), param1Int, ((ValueAndSize)param1Object).value, ((ValueAndSize)param1Object).size);
    }
    
    void setGlobal(Script.FieldID param1FieldID, Object param1Object) {
      Object object = param1Object;
      if (param1Object instanceof ScriptGroup.Future)
        object = ((ScriptGroup.Future)param1Object).getValue(); 
      this.mBindings.put(param1FieldID, object);
      param1Object = new ValueAndSize(this.mRS, object);
      this.mRS.nClosureSetGlobal(getID(this.mRS), param1FieldID.getID(this.mRS), ((ValueAndSize)param1Object).value, ((ValueAndSize)param1Object).size);
    }
    
    private static final class ValueAndSize {
      public int size;
      
      public long value;
      
      public ValueAndSize(RenderScript param2RenderScript, Object param2Object) {
        if (param2Object instanceof Allocation) {
          this.value = ((Allocation)param2Object).getID(param2RenderScript);
          this.size = -1;
          return;
        } 
        if (param2Object instanceof Boolean) {
          long l;
          if (((Boolean)param2Object).booleanValue()) {
            l = 1L;
          } else {
            l = 0L;
          } 
          this.value = l;
          this.size = 4;
          return;
        } 
        if (param2Object instanceof Integer) {
          this.value = ((Integer)param2Object).longValue();
          this.size = 4;
          return;
        } 
        if (param2Object instanceof Long) {
          this.value = ((Long)param2Object).longValue();
          this.size = 8;
          return;
        } 
        if (param2Object instanceof Float) {
          this.value = Float.floatToRawIntBits(((Float)param2Object).floatValue());
          this.size = 4;
          return;
        } 
        if (param2Object instanceof Double) {
          this.value = Double.doubleToRawLongBits(((Double)param2Object).doubleValue());
          this.size = 8;
        } 
      }
    }
  }
  
  private static final class ValueAndSize {
    public int size;
    
    public long value;
    
    public ValueAndSize(RenderScript param1RenderScript, Object param1Object) {
      if (param1Object instanceof Allocation) {
        this.value = ((Allocation)param1Object).getID(param1RenderScript);
        this.size = -1;
        return;
      } 
      if (param1Object instanceof Boolean) {
        long l;
        if (((Boolean)param1Object).booleanValue()) {
          l = 1L;
        } else {
          l = 0L;
        } 
        this.value = l;
        this.size = 4;
        return;
      } 
      if (param1Object instanceof Integer) {
        this.value = ((Integer)param1Object).longValue();
        this.size = 4;
        return;
      } 
      if (param1Object instanceof Long) {
        this.value = ((Long)param1Object).longValue();
        this.size = 8;
        return;
      } 
      if (param1Object instanceof Float) {
        this.value = Float.floatToRawIntBits(((Float)param1Object).floatValue());
        this.size = 4;
        return;
      } 
      if (param1Object instanceof Double) {
        this.value = Double.doubleToRawLongBits(((Double)param1Object).doubleValue());
        this.size = 8;
      } 
    }
  }
  
  static class ConnectLine {
    Allocation mAllocation;
    
    Type mAllocationType;
    
    Script.KernelID mFrom;
    
    Script.FieldID mToF;
    
    Script.KernelID mToK;
    
    ConnectLine(Type param1Type, Script.KernelID param1KernelID, Script.FieldID param1FieldID) {
      this.mFrom = param1KernelID;
      this.mToF = param1FieldID;
      this.mAllocationType = param1Type;
    }
    
    ConnectLine(Type param1Type, Script.KernelID param1KernelID1, Script.KernelID param1KernelID2) {
      this.mFrom = param1KernelID1;
      this.mToK = param1KernelID2;
      this.mAllocationType = param1Type;
    }
  }
  
  public static final class Future {
    ScriptGroup.Closure mClosure;
    
    Script.FieldID mFieldID;
    
    Object mValue;
    
    Future(ScriptGroup.Closure param1Closure, Script.FieldID param1FieldID, Object param1Object) {
      this.mClosure = param1Closure;
      this.mFieldID = param1FieldID;
      this.mValue = param1Object;
    }
    
    ScriptGroup.Closure getClosure() {
      return this.mClosure;
    }
    
    Script.FieldID getFieldID() {
      return this.mFieldID;
    }
    
    Object getValue() {
      return this.mValue;
    }
  }
  
  static class IO {
    Allocation mAllocation;
    
    Script.KernelID mKID;
    
    IO(Script.KernelID param1KernelID) {
      this.mKID = param1KernelID;
    }
  }
  
  public static final class Input {
    List<Pair<ScriptGroup.Closure, Integer>> mArgIndex = new ArrayList<Pair<ScriptGroup.Closure, Integer>>();
    
    List<Pair<ScriptGroup.Closure, Script.FieldID>> mFieldID = new ArrayList<Pair<ScriptGroup.Closure, Script.FieldID>>();
    
    Object mValue;
    
    void addReference(ScriptGroup.Closure param1Closure, int param1Int) {
      this.mArgIndex.add(Pair.create(param1Closure, Integer.valueOf(param1Int)));
    }
    
    void addReference(ScriptGroup.Closure param1Closure, Script.FieldID param1FieldID) {
      this.mFieldID.add(Pair.create(param1Closure, param1FieldID));
    }
    
    Object get() {
      return this.mValue;
    }
    
    void set(Object param1Object) {
      this.mValue = param1Object;
      for (Pair<ScriptGroup.Closure, Integer> pair : this.mArgIndex)
        ((ScriptGroup.Closure)pair.first).setArg(((Integer)pair.second).intValue(), param1Object); 
      for (Pair<ScriptGroup.Closure, Script.FieldID> pair : this.mFieldID)
        ((ScriptGroup.Closure)pair.first).setGlobal((Script.FieldID)pair.second, param1Object); 
    }
  }
  
  static class Node {
    int dagNumber;
    
    ArrayList<ScriptGroup.ConnectLine> mInputs = new ArrayList<ScriptGroup.ConnectLine>();
    
    ArrayList<Script.KernelID> mKernels = new ArrayList<Script.KernelID>();
    
    Node mNext;
    
    int mOrder;
    
    ArrayList<ScriptGroup.ConnectLine> mOutputs = new ArrayList<ScriptGroup.ConnectLine>();
    
    Script mScript;
    
    boolean mSeen;
    
    Node(Script param1Script) {
      this.mScript = param1Script;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
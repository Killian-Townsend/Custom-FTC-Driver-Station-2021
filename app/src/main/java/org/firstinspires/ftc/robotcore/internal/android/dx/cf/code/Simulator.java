package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInterfaceMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public class Simulator {
  private static final String LOCAL_MISMATCH_ERROR = "This is symptomatic of .class transformation tools that ignore local variable information.";
  
  private final BytecodeArray code;
  
  private final LocalVariableList localVariables;
  
  private final Machine machine;
  
  private final SimVisitor visitor;
  
  public Simulator(Machine paramMachine, ConcreteMethod paramConcreteMethod) {
    if (paramMachine != null) {
      if (paramConcreteMethod != null) {
        this.machine = paramMachine;
        this.code = paramConcreteMethod.getCode();
        this.localVariables = paramConcreteMethod.getLocalVariables();
        this.visitor = new SimVisitor();
        return;
      } 
      throw new NullPointerException("method == null");
    } 
    throw new NullPointerException("machine == null");
  }
  
  private static SimException illegalTos() {
    return new SimException("stack mismatch: illegal top-of-stack for opcode");
  }
  
  private static Type requiredArrayTypeFor(Type paramType1, Type paramType2) {
    return (paramType2 == Type.KNOWN_NULL) ? (paramType1.isReference() ? Type.KNOWN_NULL : paramType1.getArrayType()) : ((paramType1 == Type.OBJECT && paramType2.isArray() && paramType2.getComponentType().isReference()) ? paramType2 : ((paramType1 == Type.BYTE && paramType2 == Type.BOOLEAN_ARRAY) ? Type.BOOLEAN_ARRAY : paramType1.getArrayType()));
  }
  
  public int simulate(int paramInt, Frame paramFrame) {
    this.visitor.setFrame(paramFrame);
    return this.code.parseInstruction(paramInt, this.visitor);
  }
  
  public void simulate(ByteBlock paramByteBlock, Frame paramFrame) {
    int i = paramByteBlock.getEnd();
    this.visitor.setFrame(paramFrame);
    try {
      for (int j = paramByteBlock.getStart(); j < i; j += k) {
        int k = this.code.parseInstruction(j, this.visitor);
        this.visitor.setPreviousOffset(j);
      } 
      return;
    } catch (SimException simException) {
      paramFrame.annotate(simException);
      throw simException;
    } 
  }
  
  private class SimVisitor implements BytecodeArray.Visitor {
    private Frame frame = null;
    
    private final Machine machine = Simulator.this.machine;
    
    private int previousOffset;
    
    private void checkReturnType(Type param1Type) {
      Type type = this.machine.getPrototype().getReturnType();
      if (Merger.isPossiblyAssignableFrom((TypeBearer)type, (TypeBearer)param1Type))
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("return type mismatch: prototype indicates ");
      stringBuilder.append(type.toHuman());
      stringBuilder.append(", but encountered type ");
      stringBuilder.append(param1Type.toHuman());
      throw new SimException(stringBuilder.toString());
    }
    
    public int getPreviousOffset() {
      return this.previousOffset;
    }
    
    public void setFrame(Frame param1Frame) {
      if (param1Frame != null) {
        this.frame = param1Frame;
        return;
      } 
      throw new NullPointerException("frame == null");
    }
    
    public void setPreviousOffset(int param1Int) {
      this.previousOffset = param1Int;
    }
    
    public void visitBranch(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      switch (param1Int1) {
        default:
          switch (param1Int1) {
            default:
              visitInvalid(param1Int1, param1Int2, param1Int3);
              return;
            case 198:
            case 199:
              this.machine.popArgs(this.frame, Type.OBJECT);
              break;
            case 200:
            case 201:
              break;
          } 
        case 167:
        case 168:
          this.machine.clearArgs();
          break;
        case 165:
        case 166:
          this.machine.popArgs(this.frame, Type.OBJECT, Type.OBJECT);
          break;
        case 159:
        case 160:
        case 161:
        case 162:
        case 163:
        case 164:
          this.machine.popArgs(this.frame, Type.INT, Type.INT);
          break;
        case 153:
        case 154:
        case 155:
        case 156:
        case 157:
        case 158:
          this.machine.popArgs(this.frame, Type.INT);
          break;
      } 
      this.machine.auxTargetArg(param1Int4);
      this.machine.run(this.frame, param1Int2, param1Int1);
    }
    
    public void visitConstant(int param1Int1, int param1Int2, int param1Int3, Constant param1Constant, int param1Int4) {
      Prototype prototype;
      if (param1Int1 != 189) {
        if (param1Int1 != 197) {
          if (param1Int1 != 192 && param1Int1 != 193) {
            CstMethodRef cstMethodRef;
            Prototype prototype1;
            Type type;
            Constant constant = param1Constant;
            switch (param1Int1) {
              default:
                this.machine.clearArgs();
                break;
              case 185:
                cstMethodRef = ((CstInterfaceMethodRef)param1Constant).toMethodRef();
              case 184:
                prototype1 = ((CstMethodRef)param1Constant).getPrototype(true);
                this.machine.popArgs(this.frame, prototype1);
                break;
              case 182:
              case 183:
                prototype = ((CstMethodRef)prototype1).getPrototype(false);
                this.machine.popArgs(this.frame, prototype);
                prototype = prototype1;
                break;
              case 181:
                type = ((CstFieldRef)prototype).getType();
                this.machine.popArgs(this.frame, Type.OBJECT, type);
                break;
              case 179:
                type = ((CstFieldRef)prototype).getType();
                this.machine.popArgs(this.frame, type);
                break;
              case 180:
                this.machine.popArgs(this.frame, Type.OBJECT);
                break;
            } 
          } else {
          
          } 
        } else {
          Prototype prototype1 = Prototype.internInts(Type.VOID, param1Int4);
          this.machine.popArgs(this.frame, prototype1);
        } 
      } else {
        this.machine.popArgs(this.frame, Type.INT);
      } 
      this.machine.auxIntArg(param1Int4);
      this.machine.auxCstArg((Constant)prototype);
      this.machine.run(this.frame, param1Int2, param1Int1);
    }
    
    public void visitInvalid(int param1Int1, int param1Int2, int param1Int3) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("invalid opcode ");
      stringBuilder.append(Hex.u1(param1Int1));
      throw new SimException(stringBuilder.toString());
    }
    
    public void visitLocal(int param1Int1, int param1Int2, int param1Int3, int param1Int4, Type param1Type, int param1Int5) {
      int i;
      boolean bool;
      Type type;
      if (param1Int1 == 54) {
        i = param1Int2 + param1Int3;
      } else {
        i = param1Int2;
      } 
      LocalVariableList.Item item = Simulator.this.localVariables.pcAndIndexToLocal(i, param1Int4);
      if (item != null) {
        Type type1 = item.getType();
        type = type1;
        if (type1.getBasicFrameType() != param1Type.getBasicFrameType()) {
          BaseMachine.throwLocalMismatch((TypeBearer)param1Type, (TypeBearer)type1);
          return;
        } 
      } else {
        type = param1Type;
      } 
      if (param1Int1 != 21) {
        LocalItem localItem2 = null;
        LocalItem localItem1 = null;
        if (param1Int1 != 54) {
          if (param1Int1 != 132) {
            if (param1Int1 != 169) {
              visitInvalid(param1Int1, param1Int2, param1Int3);
              return;
            } 
          } else {
            if (item != null)
              localItem1 = item.getLocalItem(); 
            this.machine.localArg(this.frame, param1Int4);
            this.machine.localTarget(param1Int4, type, localItem1);
            this.machine.auxType(param1Type);
            this.machine.auxIntArg(param1Int5);
            this.machine.auxCstArg((Constant)CstInteger.make(param1Int5));
            this.machine.run(this.frame, param1Int2, param1Int1);
          } 
        } else {
          if (item == null) {
            localItem1 = localItem2;
          } else {
            localItem1 = item.getLocalItem();
          } 
          this.machine.popArgs(this.frame, param1Type);
          this.machine.auxType(param1Type);
          this.machine.localTarget(param1Int4, type, localItem1);
          this.machine.run(this.frame, param1Int2, param1Int1);
        } 
      } 
      this.machine.localArg(this.frame, param1Int4);
      Machine machine = this.machine;
      if (item != null) {
        bool = true;
      } else {
        bool = false;
      } 
      machine.localInfo(bool);
      this.machine.auxType(param1Type);
      this.machine.run(this.frame, param1Int2, param1Int1);
    }
    
    public void visitNewarray(int param1Int1, int param1Int2, CstType param1CstType, ArrayList<Constant> param1ArrayList) {
      this.machine.popArgs(this.frame, Type.INT);
      this.machine.auxInitValues(param1ArrayList);
      this.machine.auxCstArg((Constant)param1CstType);
      this.machine.run(this.frame, param1Int1, 188);
    }
    
    public void visitNoArgs(int param1Int1, int param1Int2, int param1Int3, Type param1Type) {
      if (param1Int1 != 0) {
        Type type;
        if (param1Int1 != 190) {
          if (param1Int1 != 191 && param1Int1 != 194 && param1Int1 != 195) {
            boolean bool;
            Type type4;
            ExecutionStack executionStack4;
            Type type3;
            ExecutionStack executionStack3;
            Type type2;
            ExecutionStack executionStack2;
            Type type1;
            ExecutionStack executionStack1;
            Type type5;
            byte b = 3;
            switch (param1Int1) {
              default:
                b = 17;
                switch (param1Int1) {
                  default:
                    switch (param1Int1) {
                      default:
                        visitInvalid(param1Int1, param1Int2, param1Int3);
                        return;
                      case 151:
                      case 152:
                        this.machine.popArgs(this.frame, Type.DOUBLE, Type.DOUBLE);
                        type4 = param1Type;
                        break;
                      case 149:
                      case 150:
                        this.machine.popArgs(this.frame, Type.FLOAT, Type.FLOAT);
                        type4 = param1Type;
                        break;
                      case 148:
                        this.machine.popArgs(this.frame, Type.LONG, Type.LONG);
                        type4 = param1Type;
                        break;
                      case 142:
                      case 143:
                      case 144:
                        this.machine.popArgs(this.frame, Type.DOUBLE);
                        type4 = param1Type;
                        break;
                      case 139:
                      case 140:
                      case 141:
                        this.machine.popArgs(this.frame, Type.FLOAT);
                        type4 = param1Type;
                        break;
                      case 136:
                      case 137:
                      case 138:
                        this.machine.popArgs(this.frame, Type.LONG);
                        type4 = param1Type;
                        break;
                      case 133:
                      case 134:
                      case 135:
                      case 145:
                      case 146:
                      case 147:
                        break;
                    } 
                    this.machine.popArgs(this.frame, Type.INT);
                    type4 = param1Type;
                    break;
                  case 95:
                    executionStack4 = this.frame.getStack();
                    if (executionStack4.peekType(0).isCategory1() && executionStack4.peekType(1).isCategory1()) {
                      this.machine.popArgs(this.frame, 2);
                      this.machine.auxIntArg(18);
                      type3 = param1Type;
                      break;
                    } 
                    throw Simulator.illegalTos();
                  case 94:
                    executionStack4 = this.frame.getStack();
                    if (executionStack4.peekType(0).isCategory2()) {
                      if (executionStack4.peekType(2).isCategory2()) {
                        this.machine.popArgs(this.frame, 2);
                        this.machine.auxIntArg(530);
                        type3 = param1Type;
                        break;
                      } 
                      if (type3.peekType(3).isCategory1()) {
                        this.machine.popArgs(this.frame, 3);
                        this.machine.auxIntArg(12819);
                        type3 = param1Type;
                        break;
                      } 
                      throw Simulator.illegalTos();
                    } 
                    if (type3.peekType(1).isCategory1()) {
                      if (type3.peekType(2).isCategory2()) {
                        this.machine.popArgs(this.frame, 3);
                        this.machine.auxIntArg(205106);
                        type3 = param1Type;
                        break;
                      } 
                      if (type3.peekType(3).isCategory1()) {
                        this.machine.popArgs(this.frame, 4);
                        this.machine.auxIntArg(4399427);
                        type3 = param1Type;
                        break;
                      } 
                      throw Simulator.illegalTos();
                    } 
                    throw Simulator.illegalTos();
                  case 93:
                    executionStack3 = this.frame.getStack();
                    if (executionStack3.peekType(0).isCategory2()) {
                      if (!executionStack3.peekType(2).isCategory2()) {
                        this.machine.popArgs(this.frame, 2);
                        this.machine.auxIntArg(530);
                        type2 = param1Type;
                        break;
                      } 
                      throw Simulator.illegalTos();
                    } 
                    if (!type2.peekType(1).isCategory2() && !type2.peekType(2).isCategory2()) {
                      this.machine.popArgs(this.frame, 3);
                      this.machine.auxIntArg(205106);
                      type2 = param1Type;
                      break;
                    } 
                    throw Simulator.illegalTos();
                  case 91:
                    executionStack2 = this.frame.getStack();
                    if (!executionStack2.peekType(0).isCategory2()) {
                      Type type6;
                      if (executionStack2.peekType(1).isCategory2()) {
                        this.machine.popArgs(this.frame, 2);
                        this.machine.auxIntArg(530);
                        type6 = param1Type;
                        break;
                      } 
                      if (type6.peekType(2).isCategory1()) {
                        this.machine.popArgs(this.frame, 3);
                        this.machine.auxIntArg(12819);
                        type6 = param1Type;
                        break;
                      } 
                      throw Simulator.illegalTos();
                    } 
                    throw Simulator.illegalTos();
                  case 90:
                    executionStack2 = this.frame.getStack();
                    if (executionStack2.peekType(0).isCategory1() && executionStack2.peekType(1).isCategory1()) {
                      this.machine.popArgs(this.frame, 2);
                      this.machine.auxIntArg(530);
                      Type type6 = param1Type;
                      break;
                    } 
                    throw Simulator.illegalTos();
                  case 89:
                    if (!this.frame.getStack().peekType(0).isCategory2()) {
                      this.machine.popArgs(this.frame, 1);
                      this.machine.auxIntArg(17);
                      Type type6 = param1Type;
                      break;
                    } 
                    throw Simulator.illegalTos();
                  case 88:
                  case 92:
                    executionStack2 = this.frame.getStack();
                    if (executionStack2.peekType(0).isCategory2()) {
                      this.machine.popArgs(this.frame, 1);
                      param1Int3 = b;
                    } else if (executionStack2.peekType(1).isCategory1()) {
                      this.machine.popArgs(this.frame, 2);
                      param1Int3 = 8481;
                    } else {
                      throw Simulator.illegalTos();
                    } 
                    type1 = param1Type;
                    if (param1Int1 == 92) {
                      this.machine.auxIntArg(param1Int3);
                      type1 = param1Type;
                    } 
                    break;
                  case 87:
                    if (!this.frame.getStack().peekType(0).isCategory2()) {
                      this.machine.popArgs(this.frame, 1);
                      type1 = param1Type;
                      break;
                    } 
                    throw Simulator.illegalTos();
                  case 96:
                    break;
                } 
              case 177:
                this.machine.clearArgs();
                checkReturnType(Type.VOID);
                type1 = param1Type;
                break;
              case 172:
                if (param1Type == Type.OBJECT) {
                  type1 = this.frame.getStack().peekType(0);
                } else {
                  type1 = param1Type;
                } 
                this.machine.popArgs(this.frame, param1Type);
                checkReturnType(type1);
                type1 = param1Type;
                break;
              case 120:
              case 122:
              case 124:
                this.machine.popArgs(this.frame, param1Type, Type.INT);
                type1 = param1Type;
                break;
              case 116:
                this.machine.popArgs(this.frame, param1Type);
                type1 = param1Type;
                break;
              case 100:
              case 104:
              case 108:
              case 112:
              case 126:
              case 128:
              case 130:
                this.machine.popArgs(this.frame, param1Type, param1Type);
                type1 = param1Type;
                break;
              case 79:
                executionStack1 = this.frame.getStack();
                param1Int3 = b;
                if (param1Type.isCategory1())
                  param1Int3 = 2; 
                type5 = executionStack1.peekType(param1Int3);
                bool = executionStack1.peekLocal(param1Int3);
                type = Simulator.requiredArrayTypeFor(param1Type, type5);
                if (bool)
                  if (type == Type.KNOWN_NULL) {
                    param1Type = Type.KNOWN_NULL;
                  } else {
                    param1Type = type.getComponentType();
                  }  
                this.machine.popArgs(this.frame, type, Type.INT, param1Type);
                type = param1Type;
                break;
              case 46:
                type = Simulator.requiredArrayTypeFor(param1Type, this.frame.getStack().peekType(1));
                if (type == Type.KNOWN_NULL) {
                  param1Type = Type.KNOWN_NULL;
                } else {
                  param1Type = type.getComponentType();
                } 
                this.machine.popArgs(this.frame, type, Type.INT);
                type = param1Type;
                break;
              case 0:
                this.machine.clearArgs();
                type = param1Type;
                break;
            } 
          } else {
            this.machine.popArgs(this.frame, Type.OBJECT);
            type = param1Type;
          } 
        } else {
          type = this.frame.getStack().peekType(0);
          if (type.isArrayOrKnownNull()) {
            this.machine.popArgs(this.frame, Type.OBJECT);
            type = param1Type;
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("type mismatch: expected array type but encountered ");
            stringBuilder.append(type.toHuman());
            throw new SimException(stringBuilder.toString());
          } 
        } 
        this.machine.auxType(type);
        this.machine.run(this.frame, param1Int2, param1Int1);
        return;
      } 
    }
    
    public void visitSwitch(int param1Int1, int param1Int2, int param1Int3, SwitchList param1SwitchList, int param1Int4) {
      this.machine.popArgs(this.frame, Type.INT);
      this.machine.auxIntArg(param1Int4);
      this.machine.auxSwitchArg(param1SwitchList);
      this.machine.run(this.frame, param1Int2, param1Int1);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\Simulator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
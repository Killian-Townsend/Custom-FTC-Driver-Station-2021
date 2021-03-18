package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntSet;

public class DomFront {
  private static boolean DEBUG = false;
  
  private final DomInfo[] domInfos;
  
  private final SsaMethod meth;
  
  private final ArrayList<SsaBasicBlock> nodes;
  
  public DomFront(SsaMethod paramSsaMethod) {
    this.meth = paramSsaMethod;
    ArrayList<SsaBasicBlock> arrayList = paramSsaMethod.getBlocks();
    this.nodes = arrayList;
    int j = arrayList.size();
    this.domInfos = new DomInfo[j];
    for (int i = 0; i < j; i++)
      this.domInfos[i] = new DomInfo(); 
  }
  
  private void buildDomTree() {
    int j = this.nodes.size();
    for (int i = 0; i < j; i++) {
      DomInfo domInfo = this.domInfos[i];
      if (domInfo.idom != -1)
        ((SsaBasicBlock)this.nodes.get(domInfo.idom)).addDomChild(this.nodes.get(i)); 
    } 
  }
  
  private void calcDomFronts() {
    int j = this.nodes.size();
    for (int i = 0; i < j; i++) {
      SsaBasicBlock ssaBasicBlock = this.nodes.get(i);
      DomInfo domInfo = this.domInfos[i];
      BitSet bitSet = ssaBasicBlock.getPredecessors();
      if (bitSet.cardinality() > 1)
        for (int k = bitSet.nextSetBit(0); k >= 0; k = bitSet.nextSetBit(k + 1)) {
          for (int m = k; m != domInfo.idom && m != -1; m = domInfo1.idom) {
            DomInfo domInfo1 = this.domInfos[m];
            if (domInfo1.dominanceFrontiers.has(i))
              break; 
            domInfo1.dominanceFrontiers.add(i);
          } 
        }  
    } 
  }
  
  private void debugPrintDomChildren() {
    int j = this.nodes.size();
    for (int i = 0; i < j; i++) {
      SsaBasicBlock ssaBasicBlock = this.nodes.get(i);
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append('{');
      Iterator<SsaBasicBlock> iterator = ssaBasicBlock.getDomChildren().iterator();
      for (boolean bool = false; iterator.hasNext(); bool = true) {
        SsaBasicBlock ssaBasicBlock1 = iterator.next();
        if (bool)
          stringBuffer.append(','); 
        stringBuffer.append(ssaBasicBlock1);
      } 
      stringBuffer.append('}');
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("domChildren[");
      stringBuilder.append(ssaBasicBlock);
      stringBuilder.append("]: ");
      stringBuilder.append(stringBuffer);
      printStream.println(stringBuilder.toString());
    } 
  }
  
  public DomInfo[] run() {
    int j = this.nodes.size();
    boolean bool1 = DEBUG;
    boolean bool = false;
    if (bool1)
      for (int k = 0; k < j; k++) {
        SsaBasicBlock ssaBasicBlock = this.nodes.get(k);
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pred[");
        stringBuilder.append(k);
        stringBuilder.append("]: ");
        stringBuilder.append(ssaBasicBlock.getPredecessors());
        printStream.println(stringBuilder.toString());
      }  
    Dominators.make(this.meth, this.domInfos, false);
    if (DEBUG)
      for (int k = 0; k < j; k++) {
        DomInfo domInfo = this.domInfos[k];
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("idom[");
        stringBuilder.append(k);
        stringBuilder.append("]: ");
        stringBuilder.append(domInfo.idom);
        printStream.println(stringBuilder.toString());
      }  
    buildDomTree();
    if (DEBUG)
      debugPrintDomChildren(); 
    int i;
    for (i = 0; i < j; i++)
      (this.domInfos[i]).dominanceFrontiers = SetFactory.makeDomFrontSet(j); 
    calcDomFronts();
    if (DEBUG)
      for (i = bool; i < j; i++) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("df[");
        stringBuilder.append(i);
        stringBuilder.append("]: ");
        stringBuilder.append((this.domInfos[i]).dominanceFrontiers);
        printStream.println(stringBuilder.toString());
      }  
    return this.domInfos;
  }
  
  public static class DomInfo {
    public IntSet dominanceFrontiers;
    
    public int idom = -1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\DomFront.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
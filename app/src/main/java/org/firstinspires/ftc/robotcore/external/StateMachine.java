package org.firstinspires.ftc.robotcore.external;

import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StateMachine {
  private static final String TAG = "StateMachine";
  
  protected State currentState;
  
  protected ArrayList<Event> maskList = new ArrayList<Event>();
  
  protected HashMap<State, ArrayList<StateTransition>> stateGraph = new HashMap<State, ArrayList<StateTransition>>();
  
  public void addTransition(StateTransition paramStateTransition) {
    ArrayList<StateTransition> arrayList = this.stateGraph.get(paramStateTransition.from);
    if (arrayList == null) {
      arrayList = new ArrayList();
      arrayList.add(paramStateTransition);
      this.stateGraph.put(paramStateTransition.from, arrayList);
      return;
    } 
    arrayList.add(paramStateTransition);
  }
  
  public State consumeEvent(Event paramEvent) {
    if (this.maskList.contains(paramEvent)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Ignoring ");
      stringBuilder.append(paramEvent.getName());
      stringBuilder.append(" masked");
      RobotLog.ii("StateMachine", stringBuilder.toString());
      return this.currentState;
    } 
    State state = transition(paramEvent);
    if (state != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Old State: ");
      stringBuilder.append(this.currentState.toString());
      stringBuilder.append(", Event: ");
      stringBuilder.append(paramEvent.getName());
      stringBuilder.append(", New State: ");
      stringBuilder.append(state.toString());
      RobotLog.ii("StateMachine", stringBuilder.toString());
      this.currentState.onExit(paramEvent);
      this.currentState = state;
      state.onEnter(paramEvent);
    } 
    return this.currentState;
  }
  
  public void maskEvent(Event paramEvent) {
    if (!this.maskList.contains(paramEvent)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Masking ");
      stringBuilder.append(paramEvent.getName());
      RobotLog.ii("StateMachine", stringBuilder.toString());
      this.maskList.add(paramEvent);
    } 
  }
  
  protected void start(State paramState) {
    this.currentState = paramState;
  }
  
  public String toString() {
    StateTransition stateTransition;
    Iterator<Map.Entry> iterator = this.stateGraph.entrySet().iterator();
    String str = "\n";
    label11: while (iterator.hasNext()) {
      Map.Entry entry = iterator.next();
      State state = (State)entry.getKey();
      ArrayList arrayList = (ArrayList)entry.getValue();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(state.toString());
      stringBuilder.append("\n");
      String str1 = stringBuilder.toString();
      Iterator<StateTransition> iterator1 = arrayList.iterator();
      while (true) {
        str = str1;
        if (iterator1.hasNext()) {
          stateTransition = iterator1.next();
          stringBuilder = new StringBuilder();
          stringBuilder.append(str1);
          stringBuilder.append("\t");
          stringBuilder.append(stateTransition.toString());
          stringBuilder.append("\n");
          str1 = stringBuilder.toString();
          continue;
        } 
        continue label11;
      } 
    } 
    return (String)stateTransition;
  }
  
  protected State transition(Event paramEvent) {
    StringBuilder stringBuilder;
    ArrayList arrayList = this.stateGraph.get(this.currentState);
    if (arrayList == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("State with no transitions: ");
      stringBuilder.append(this.currentState.toString());
      RobotLog.vv("StateMachine", stringBuilder.toString());
      return null;
    } 
    for (StateTransition stateTransition : arrayList) {
      if (stateTransition.event == stringBuilder)
        return stateTransition.to; 
    } 
    return null;
  }
  
  public void unMaskEvent(Event paramEvent) {
    if (this.maskList.contains(paramEvent)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unmasking ");
      stringBuilder.append(paramEvent.getName());
      RobotLog.ii("StateMachine", stringBuilder.toString());
      this.maskList.remove(paramEvent);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\StateMachine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
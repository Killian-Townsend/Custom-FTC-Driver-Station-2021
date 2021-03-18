package org.firstinspires.ftc.robotcore.external;

public class StateTransition {
  protected Event event;
  
  protected State from;
  
  protected State to;
  
  public StateTransition(State paramState1, Event paramEvent, State paramState2) {
    this.from = paramState1;
    this.event = paramEvent;
    this.to = paramState2;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("From: ");
    stringBuilder.append(this.from);
    stringBuilder.append(" To: ");
    stringBuilder.append(this.to);
    stringBuilder.append(" On Event: ");
    stringBuilder.append(this.event.getName());
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\StateTransition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
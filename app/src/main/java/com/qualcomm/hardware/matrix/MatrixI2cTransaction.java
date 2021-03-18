package com.qualcomm.hardware.matrix;

import com.qualcomm.robotcore.util.RobotLog;

public class MatrixI2cTransaction {
  public byte mode;
  
  public byte motor;
  
  public I2cTransactionProperty property;
  
  public byte servo;
  
  public byte speed;
  
  public I2cTransactionState state;
  
  public int target;
  
  public int value;
  
  public boolean write;
  
  MatrixI2cTransaction(byte paramByte1, byte paramByte2, byte paramByte3) {
    this.servo = paramByte1;
    this.speed = paramByte3;
    this.target = paramByte2;
    this.property = I2cTransactionProperty.PROPERTY_SERVO;
    this.state = I2cTransactionState.QUEUED;
    this.write = true;
  }
  
  MatrixI2cTransaction(byte paramByte1, byte paramByte2, int paramInt, byte paramByte3) {
    this.motor = paramByte1;
    this.speed = paramByte2;
    this.target = paramInt;
    this.mode = paramByte3;
    this.property = I2cTransactionProperty.PROPERTY_MOTOR_BATCH;
    this.state = I2cTransactionState.QUEUED;
    this.write = true;
  }
  
  MatrixI2cTransaction(byte paramByte, I2cTransactionProperty paramI2cTransactionProperty) {
    this.motor = paramByte;
    this.property = paramI2cTransactionProperty;
    this.state = I2cTransactionState.QUEUED;
    this.write = false;
  }
  
  MatrixI2cTransaction(byte paramByte, I2cTransactionProperty paramI2cTransactionProperty, int paramInt) {
    this.motor = paramByte;
    this.value = paramInt;
    this.property = paramI2cTransactionProperty;
    this.state = I2cTransactionState.QUEUED;
    this.write = true;
  }
  
  public boolean isEqual(MatrixI2cTransaction paramMatrixI2cTransaction) {
    StringBuilder stringBuilder;
    I2cTransactionProperty i2cTransactionProperty1 = this.property;
    I2cTransactionProperty i2cTransactionProperty2 = paramMatrixI2cTransaction.property;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool2 = false;
    if (i2cTransactionProperty1 != i2cTransactionProperty2)
      return false; 
    switch (this.property) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Can not compare against unknown transaction property ");
        stringBuilder.append(paramMatrixI2cTransaction.toString());
        RobotLog.e(stringBuilder.toString());
        return false;
      case null:
        bool1 = bool2;
        if (this.write == paramMatrixI2cTransaction.write) {
          bool1 = bool2;
          if (this.value == paramMatrixI2cTransaction.value)
            bool1 = true; 
        } 
        return bool1;
      case null:
        bool1 = bool3;
        if (this.write == paramMatrixI2cTransaction.write) {
          bool1 = bool3;
          if (this.servo == paramMatrixI2cTransaction.servo) {
            bool1 = bool3;
            if (this.speed == paramMatrixI2cTransaction.speed) {
              bool1 = bool3;
              if (this.target == paramMatrixI2cTransaction.target)
                bool1 = true; 
            } 
          } 
        } 
        return bool1;
      case null:
        bool1 = bool4;
        if (this.write == paramMatrixI2cTransaction.write) {
          bool1 = bool4;
          if (this.motor == paramMatrixI2cTransaction.motor) {
            bool1 = bool4;
            if (this.speed == paramMatrixI2cTransaction.speed) {
              bool1 = bool4;
              if (this.target == paramMatrixI2cTransaction.target) {
                bool1 = bool4;
                if (this.mode == paramMatrixI2cTransaction.mode)
                  bool1 = true; 
              } 
            } 
          } 
        } 
        return bool1;
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    boolean bool1 = bool5;
    if (this.write == paramMatrixI2cTransaction.write) {
      bool1 = bool5;
      if (this.motor == paramMatrixI2cTransaction.motor) {
        bool1 = bool5;
        if (this.value == paramMatrixI2cTransaction.value)
          bool1 = true; 
      } 
    } 
    return bool1;
  }
  
  public String toString() {
    if (this.property == I2cTransactionProperty.PROPERTY_MOTOR_BATCH) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Matrix motor transaction: ");
      stringBuilder1.append(this.property);
      stringBuilder1.append(" motor ");
      stringBuilder1.append(this.motor);
      stringBuilder1.append(" write ");
      stringBuilder1.append(this.write);
      stringBuilder1.append(" speed ");
      stringBuilder1.append(this.speed);
      stringBuilder1.append(" target ");
      stringBuilder1.append(this.target);
      stringBuilder1.append(" mode ");
      stringBuilder1.append(this.mode);
      return stringBuilder1.toString();
    } 
    if (this.property == I2cTransactionProperty.PROPERTY_SERVO) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Matrix servo transaction: ");
      stringBuilder1.append(this.property);
      stringBuilder1.append(" servo ");
      stringBuilder1.append(this.servo);
      stringBuilder1.append(" write ");
      stringBuilder1.append(this.write);
      stringBuilder1.append(" change rate ");
      stringBuilder1.append(this.speed);
      stringBuilder1.append(" target ");
      stringBuilder1.append(this.target);
      return stringBuilder1.toString();
    } 
    if (this.property == I2cTransactionProperty.PROPERTY_SERVO_ENABLE) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Matrix servo transaction: ");
      stringBuilder1.append(this.property);
      stringBuilder1.append(" servo ");
      stringBuilder1.append(this.servo);
      stringBuilder1.append(" write ");
      stringBuilder1.append(this.write);
      stringBuilder1.append(" value ");
      stringBuilder1.append(this.value);
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Matrix motor transaction: ");
    stringBuilder.append(this.property);
    stringBuilder.append(" motor ");
    stringBuilder.append(this.motor);
    stringBuilder.append(" write ");
    stringBuilder.append(this.write);
    stringBuilder.append(" value ");
    stringBuilder.append(this.value);
    return stringBuilder.toString();
  }
  
  enum I2cTransactionProperty {
    PROPERTY_BATTERY, PROPERTY_MODE, PROPERTY_MOTOR_BATCH, PROPERTY_POSITION, PROPERTY_SERVO, PROPERTY_SERVO_ENABLE, PROPERTY_SPEED, PROPERTY_START, PROPERTY_TARGET, PROPERTY_TIMEOUT;
    
    static {
      PROPERTY_BATTERY = new I2cTransactionProperty("PROPERTY_BATTERY", 3);
      PROPERTY_POSITION = new I2cTransactionProperty("PROPERTY_POSITION", 4);
      PROPERTY_MOTOR_BATCH = new I2cTransactionProperty("PROPERTY_MOTOR_BATCH", 5);
      PROPERTY_SERVO = new I2cTransactionProperty("PROPERTY_SERVO", 6);
      PROPERTY_SERVO_ENABLE = new I2cTransactionProperty("PROPERTY_SERVO_ENABLE", 7);
      PROPERTY_START = new I2cTransactionProperty("PROPERTY_START", 8);
      I2cTransactionProperty i2cTransactionProperty = new I2cTransactionProperty("PROPERTY_TIMEOUT", 9);
      PROPERTY_TIMEOUT = i2cTransactionProperty;
      $VALUES = new I2cTransactionProperty[] { PROPERTY_MODE, PROPERTY_TARGET, PROPERTY_SPEED, PROPERTY_BATTERY, PROPERTY_POSITION, PROPERTY_MOTOR_BATCH, PROPERTY_SERVO, PROPERTY_SERVO_ENABLE, PROPERTY_START, i2cTransactionProperty };
    }
  }
  
  enum I2cTransactionState {
    QUEUED, DONE, PENDING_I2C_READ, PENDING_I2C_WRITE, PENDING_READ_DONE;
    
    static {
      I2cTransactionState i2cTransactionState = new I2cTransactionState("DONE", 4);
      DONE = i2cTransactionState;
      $VALUES = new I2cTransactionState[] { QUEUED, PENDING_I2C_READ, PENDING_I2C_WRITE, PENDING_READ_DONE, i2cTransactionState };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\matrix\MatrixI2cTransaction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
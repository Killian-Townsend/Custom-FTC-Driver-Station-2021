package com.qualcomm.robotcore.hardware;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.robocol.RobocolParsableBase;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.ui.GamepadUser;

public class Gamepad extends RobocolParsableBase {
  private static final short BUFFER_SIZE = 48;
  
  public static final int ID_SYNTHETIC = -2;
  
  public static final int ID_UNASSOCIATED = -1;
  
  private static final float MAX_MOTION_RANGE = 1.0F;
  
  private static final short PAYLOAD_SIZE = 43;
  
  private static final byte ROBOCOL_GAMEPAD_VERSION = 3;
  
  private static Set<DeviceId> deviceWhitelist;
  
  private static Set<Integer> gameControllerDeviceIdCache = new HashSet<Integer>();
  
  public boolean a = false;
  
  public boolean b = false;
  
  public boolean back = false;
  
  private final GamepadCallback callback;
  
  public boolean circle = false;
  
  public boolean cross = false;
  
  protected float dpadThreshold = 0.2F;
  
  public boolean dpad_down = false;
  
  public boolean dpad_left = false;
  
  public boolean dpad_right = false;
  
  public boolean dpad_up = false;
  
  public boolean guide = false;
  
  public int id = -1;
  
  protected float joystickDeadzone = 0.2F;
  
  public boolean left_bumper = false;
  
  public boolean left_stick_button = false;
  
  public float left_stick_x = 0.0F;
  
  public float left_stick_y = 0.0F;
  
  public float left_trigger = 0.0F;
  
  public boolean options = false;
  
  public int pid = -1;
  
  public boolean ps = false;
  
  public boolean right_bumper = false;
  
  public boolean right_stick_button = false;
  
  public float right_stick_x = 0.0F;
  
  public float right_stick_y = 0.0F;
  
  public float right_trigger = 0.0F;
  
  public boolean share = false;
  
  public boolean square = false;
  
  public boolean start = false;
  
  public long timestamp = 0L;
  
  public boolean touchpad = false;
  
  public boolean triangle = false;
  
  Type type = Type.UNKNOWN;
  
  protected byte user = -1;
  
  public int vid = -1;
  
  public boolean x = false;
  
  public boolean y = false;
  
  static {
    deviceWhitelist = null;
  }
  
  public Gamepad() {
    this((GamepadCallback)null);
  }
  
  public Gamepad(GamepadCallback paramGamepadCallback) {
    this.callback = paramGamepadCallback;
    this.type = type();
  }
  
  public static void clearWhitelistFilter() {
    deviceWhitelist = null;
  }
  
  public static void enableWhitelistFilter(int paramInt1, int paramInt2) {
    if (deviceWhitelist == null)
      deviceWhitelist = new HashSet<DeviceId>(); 
    deviceWhitelist.add(new DeviceId(paramInt1, paramInt2));
  }
  
  public static boolean isGamepadDevice(int paramInt) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/hardware/Gamepad
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/hardware/Gamepad.gameControllerDeviceIdCache : Ljava/util/Set;
    //   6: iload_0
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface contains : (Ljava/lang/Object;)Z
    //   15: istore #5
    //   17: iload #5
    //   19: ifeq -> 27
    //   22: ldc com/qualcomm/robotcore/hardware/Gamepad
    //   24: monitorexit
    //   25: iconst_1
    //   26: ireturn
    //   27: new java/util/HashSet
    //   30: dup
    //   31: invokespecial <init> : ()V
    //   34: putstatic com/qualcomm/robotcore/hardware/Gamepad.gameControllerDeviceIdCache : Ljava/util/Set;
    //   37: invokestatic getDeviceIds : ()[I
    //   40: astore #6
    //   42: aload #6
    //   44: arraylength
    //   45: istore_2
    //   46: iconst_0
    //   47: istore_1
    //   48: iload_1
    //   49: iload_2
    //   50: if_icmpge -> 167
    //   53: aload #6
    //   55: iload_1
    //   56: iaload
    //   57: istore_3
    //   58: iload_3
    //   59: invokestatic getDevice : (I)Landroid/view/InputDevice;
    //   62: astore #7
    //   64: aload #7
    //   66: invokevirtual getSources : ()I
    //   69: istore #4
    //   71: iload #4
    //   73: sipush #1025
    //   76: iand
    //   77: sipush #1025
    //   80: if_icmpeq -> 93
    //   83: iload #4
    //   85: ldc 16777232
    //   87: iand
    //   88: ldc 16777232
    //   90: if_icmpne -> 204
    //   93: getstatic android/os/Build$VERSION.SDK_INT : I
    //   96: bipush #19
    //   98: if_icmplt -> 151
    //   101: getstatic com/qualcomm/robotcore/hardware/Gamepad.deviceWhitelist : Ljava/util/Set;
    //   104: ifnull -> 135
    //   107: getstatic com/qualcomm/robotcore/hardware/Gamepad.deviceWhitelist : Ljava/util/Set;
    //   110: new com/qualcomm/robotcore/hardware/Gamepad$DeviceId
    //   113: dup
    //   114: aload #7
    //   116: invokevirtual getVendorId : ()I
    //   119: aload #7
    //   121: invokevirtual getProductId : ()I
    //   124: invokespecial <init> : (II)V
    //   127: invokeinterface contains : (Ljava/lang/Object;)Z
    //   132: ifeq -> 204
    //   135: getstatic com/qualcomm/robotcore/hardware/Gamepad.gameControllerDeviceIdCache : Ljava/util/Set;
    //   138: iload_3
    //   139: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   142: invokeinterface add : (Ljava/lang/Object;)Z
    //   147: pop
    //   148: goto -> 204
    //   151: getstatic com/qualcomm/robotcore/hardware/Gamepad.gameControllerDeviceIdCache : Ljava/util/Set;
    //   154: iload_3
    //   155: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   158: invokeinterface add : (Ljava/lang/Object;)Z
    //   163: pop
    //   164: goto -> 204
    //   167: getstatic com/qualcomm/robotcore/hardware/Gamepad.gameControllerDeviceIdCache : Ljava/util/Set;
    //   170: iload_0
    //   171: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   174: invokeinterface contains : (Ljava/lang/Object;)Z
    //   179: istore #5
    //   181: iload #5
    //   183: ifeq -> 191
    //   186: ldc com/qualcomm/robotcore/hardware/Gamepad
    //   188: monitorexit
    //   189: iconst_1
    //   190: ireturn
    //   191: ldc com/qualcomm/robotcore/hardware/Gamepad
    //   193: monitorexit
    //   194: iconst_0
    //   195: ireturn
    //   196: astore #6
    //   198: ldc com/qualcomm/robotcore/hardware/Gamepad
    //   200: monitorexit
    //   201: aload #6
    //   203: athrow
    //   204: iload_1
    //   205: iconst_1
    //   206: iadd
    //   207: istore_1
    //   208: goto -> 48
    // Exception table:
    //   from	to	target	type
    //   3	17	196	finally
    //   27	46	196	finally
    //   58	71	196	finally
    //   93	135	196	finally
    //   135	148	196	finally
    //   151	164	196	finally
    //   167	181	196	finally
  }
  
  public boolean atRest() {
    return (this.left_stick_x == 0.0F && this.left_stick_y == 0.0F && this.right_stick_x == 0.0F && this.right_stick_y == 0.0F && this.left_trigger == 0.0F && this.right_trigger == 0.0F);
  }
  
  protected void callCallback() {
    GamepadCallback gamepadCallback = this.callback;
    if (gamepadCallback != null)
      gamepadCallback.gamepadChanged(this); 
  }
  
  protected float cleanMotionValues(float paramFloat) {
    double d;
    float f = this.joystickDeadzone;
    if (paramFloat < f && paramFloat > -f)
      return 0.0F; 
    if (paramFloat > 1.0F)
      return 1.0F; 
    if (paramFloat < -1.0F)
      return -1.0F; 
    if (paramFloat > 0.0F) {
      d = Range.scale(paramFloat, this.joystickDeadzone, 1.0D, 0.0D, 1.0D);
    } else {
      d = Range.scale(paramFloat, -this.joystickDeadzone, -1.0D, 0.0D, -1.0D);
    } 
    return (float)d;
  }
  
  public void copy(Gamepad paramGamepad) throws RobotCoreException {
    fromByteArray(paramGamepad.toByteArray());
  }
  
  public void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException {
    ByteBuffer byteBuffer;
    if (paramArrayOfbyte.length >= 48) {
      byteBuffer = getReadBuffer(paramArrayOfbyte);
      byte b = byteBuffer.get();
      boolean bool = true;
      if (b >= 1) {
        boolean bool1;
        this.id = byteBuffer.getInt();
        this.timestamp = byteBuffer.getLong();
        this.left_stick_x = byteBuffer.getFloat();
        this.left_stick_y = byteBuffer.getFloat();
        this.right_stick_x = byteBuffer.getFloat();
        this.right_stick_y = byteBuffer.getFloat();
        this.left_trigger = byteBuffer.getFloat();
        this.right_trigger = byteBuffer.getFloat();
        int i = byteBuffer.getInt();
        if ((0x8000 & i) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.touchpad = bool1;
        if ((i & 0x4000) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.left_stick_button = bool1;
        if ((i & 0x2000) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.right_stick_button = bool1;
        if ((i & 0x1000) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.dpad_up = bool1;
        if ((i & 0x800) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.dpad_down = bool1;
        if ((i & 0x400) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.dpad_left = bool1;
        if ((i & 0x200) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.dpad_right = bool1;
        if ((i & 0x100) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.a = bool1;
        if ((i & 0x80) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.b = bool1;
        if ((i & 0x40) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.x = bool1;
        if ((i & 0x20) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.y = bool1;
        if ((i & 0x10) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.guide = bool1;
        if ((i & 0x8) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.start = bool1;
        if ((i & 0x4) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.back = bool1;
        if ((i & 0x2) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.left_bumper = bool1;
        if ((i & 0x1) != 0) {
          bool1 = bool;
        } else {
          bool1 = false;
        } 
        this.right_bumper = bool1;
      } 
      if (b >= 2)
        this.user = byteBuffer.get(); 
      if (b >= 3)
        this.type = Type.values[byteBuffer.get()]; 
      updateButtonAliases();
      callCallback();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected buffer of at least 48 bytes, received ");
    stringBuilder.append(byteBuffer.length);
    throw new RobotCoreException(stringBuilder.toString());
  }
  
  protected String genericToString() {
    String str2 = new String();
    String str1 = str2;
    if (this.dpad_up) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("dpad_up ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.dpad_down) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("dpad_down ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.dpad_left) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("dpad_left ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.dpad_right) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("dpad_right ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.a) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("a ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.b) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("b ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.x) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("x ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.y) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("y ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.guide) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("guide ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.start) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("start ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.back) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("back ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.left_bumper) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("left_bumper ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.right_bumper) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("right_bumper ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.left_stick_button) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("left stick button ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.right_stick_button) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("right stick button ");
      str1 = stringBuilder.toString();
    } 
    return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", new Object[] { Integer.valueOf(this.id), Byte.valueOf(this.user), Float.valueOf(this.left_stick_x), Float.valueOf(this.left_stick_y), Float.valueOf(this.right_stick_x), Float.valueOf(this.right_stick_y), Float.valueOf(this.left_trigger), Float.valueOf(this.right_trigger), str1 });
  }
  
  public int getGamepadId() {
    return this.id;
  }
  
  public RobocolParsable.MsgType getRobocolMsgType() {
    return RobocolParsable.MsgType.GAMEPAD;
  }
  
  public GamepadUser getUser() {
    return GamepadUser.from(this.user);
  }
  
  protected boolean pressed(KeyEvent paramKeyEvent) {
    return (paramKeyEvent.getAction() == 0);
  }
  
  protected String ps4ToString() {
    String str2 = new String();
    String str1 = str2;
    if (this.dpad_up) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("dpad_up ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.dpad_down) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("dpad_down ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.dpad_left) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("dpad_left ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.dpad_right) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("dpad_right ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.cross) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("cross ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.circle) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("circle ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.square) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("square ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.triangle) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("triangle ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.ps) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("ps ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.share) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("share ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.options) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("options ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.touchpad) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("touchpad ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.left_bumper) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("left_bumper ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.right_bumper) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("right_bumper ");
      str2 = stringBuilder.toString();
    } 
    str1 = str2;
    if (this.left_stick_button) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("left stick button ");
      str1 = stringBuilder.toString();
    } 
    str2 = str1;
    if (this.right_stick_button) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("right stick button ");
      str2 = stringBuilder.toString();
    } 
    return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", new Object[] { Integer.valueOf(this.id), Byte.valueOf(this.user), Float.valueOf(this.left_stick_x), Float.valueOf(this.left_stick_y), Float.valueOf(this.right_stick_x), Float.valueOf(this.right_stick_y), Float.valueOf(this.left_trigger), Float.valueOf(this.right_trigger), str2 });
  }
  
  public void refreshTimestamp() {
    setTimestamp(SystemClock.uptimeMillis());
  }
  
  public void reset() {
    try {
      copy(new Gamepad());
      return;
    } catch (RobotCoreException robotCoreException) {
      RobotLog.e("Gamepad library in an invalid state");
      throw new IllegalStateException("Gamepad library in an invalid state");
    } 
  }
  
  public void setGamepadId(int paramInt) {
    this.id = paramInt;
  }
  
  public void setJoystickDeadzone(float paramFloat) {
    if (paramFloat >= 0.0F && paramFloat <= 1.0F) {
      this.joystickDeadzone = paramFloat;
      return;
    } 
    throw new IllegalArgumentException("deadzone cannot be greater than max joystick value");
  }
  
  public void setTimestamp(long paramLong) {
    this.timestamp = paramLong;
  }
  
  public void setUser(GamepadUser paramGamepadUser) {
    this.user = paramGamepadUser.id;
  }
  
  public void setVidPid(int paramInt1, int paramInt2) {
    this.vid = paramInt1;
    this.pid = paramInt2;
  }
  
  public byte[] toByteArray() throws RobotCoreException {
    ByteBuffer byteBuffer = getWriteBuffer(43);
    try {
      byte b1;
      byte b2;
      byte b3;
      byte b4;
      byte b5;
      byte b6;
      byte b7;
      byte b8;
      byte b9;
      byte b10;
      byte b11;
      byte b12;
      byte b13;
      byte b14;
      byte b15;
      byteBuffer.put((byte)3);
      byteBuffer.putInt(this.id);
      byteBuffer.putLong(this.timestamp).array();
      byteBuffer.putFloat(this.left_stick_x).array();
      byteBuffer.putFloat(this.left_stick_y).array();
      byteBuffer.putFloat(this.right_stick_x).array();
      byteBuffer.putFloat(this.right_stick_y).array();
      byteBuffer.putFloat(this.left_trigger).array();
      byteBuffer.putFloat(this.right_trigger).array();
      boolean bool = this.touchpad;
      byte b16 = 0;
      if (bool) {
        b1 = 1;
      } else {
        b1 = 0;
      } 
      if (this.left_stick_button) {
        b2 = 1;
      } else {
        b2 = 0;
      } 
      if (this.right_stick_button) {
        b3 = 1;
      } else {
        b3 = 0;
      } 
      if (this.dpad_up) {
        b4 = 1;
      } else {
        b4 = 0;
      } 
      if (this.dpad_down) {
        b5 = 1;
      } else {
        b5 = 0;
      } 
      if (this.dpad_left) {
        b6 = 1;
      } else {
        b6 = 0;
      } 
      if (this.dpad_right) {
        b7 = 1;
      } else {
        b7 = 0;
      } 
      if (this.a) {
        b8 = 1;
      } else {
        b8 = 0;
      } 
      if (this.b) {
        b9 = 1;
      } else {
        b9 = 0;
      } 
      if (this.x) {
        b10 = 1;
      } else {
        b10 = 0;
      } 
      if (this.y) {
        b11 = 1;
      } else {
        b11 = 0;
      } 
      if (this.guide) {
        b12 = 1;
      } else {
        b12 = 0;
      } 
      if (this.start) {
        b13 = 1;
      } else {
        b13 = 0;
      } 
      if (this.back) {
        b14 = 1;
      } else {
        b14 = 0;
      } 
      if (this.left_bumper) {
        b15 = 1;
      } else {
        b15 = 0;
      } 
      if (this.right_bumper)
        b16 = 1; 
      byteBuffer.putInt((((((((((((((((b1 + 0 << 1) + b2 << 1) + b3 << 1) + b4 << 1) + b5 << 1) + b6 << 1) + b7 << 1) + b8 << 1) + b9 << 1) + b10 << 1) + b11 << 1) + b12 << 1) + b13 << 1) + b14 << 1) + b15 << 1) + b16);
      byteBuffer.put(this.user);
      byteBuffer.put((byte)this.type.ordinal());
    } catch (BufferOverflowException bufferOverflowException) {
      RobotLog.logStacktrace(bufferOverflowException);
    } 
    return byteBuffer.array();
  }
  
  public String toString() {
    return (null.$SwitchMap$com$qualcomm$robotcore$hardware$Gamepad$Type[this.type.ordinal()] != 1) ? genericToString() : ps4ToString();
  }
  
  public Type type() {
    return this.type;
  }
  
  public void update(KeyEvent paramKeyEvent) {
    setGamepadId(paramKeyEvent.getDeviceId());
    setTimestamp(paramKeyEvent.getEventTime());
    int i = paramKeyEvent.getKeyCode();
    if (i == 19) {
      this.dpad_up = pressed(paramKeyEvent);
    } else if (i == 20) {
      this.dpad_down = pressed(paramKeyEvent);
    } else if (i == 22) {
      this.dpad_right = pressed(paramKeyEvent);
    } else if (i == 21) {
      this.dpad_left = pressed(paramKeyEvent);
    } else if (i == 96) {
      this.a = pressed(paramKeyEvent);
    } else if (i == 97) {
      this.b = pressed(paramKeyEvent);
    } else if (i == 99) {
      this.x = pressed(paramKeyEvent);
    } else if (i == 100) {
      this.y = pressed(paramKeyEvent);
    } else if (i == 110) {
      this.guide = pressed(paramKeyEvent);
    } else if (i == 108) {
      this.start = pressed(paramKeyEvent);
    } else if (i == 109 || i == 4) {
      this.back = pressed(paramKeyEvent);
    } else if (i == 103) {
      this.right_bumper = pressed(paramKeyEvent);
    } else if (i == 102) {
      this.left_bumper = pressed(paramKeyEvent);
    } else if (i == 106) {
      this.left_stick_button = pressed(paramKeyEvent);
    } else if (i == 107) {
      this.right_stick_button = pressed(paramKeyEvent);
    } 
    updateButtonAliases();
    callCallback();
  }
  
  public void update(MotionEvent paramMotionEvent) {
    setGamepadId(paramMotionEvent.getDeviceId());
    setTimestamp(paramMotionEvent.getEventTime());
    boolean bool2 = false;
    this.left_stick_x = cleanMotionValues(paramMotionEvent.getAxisValue(0));
    this.left_stick_y = cleanMotionValues(paramMotionEvent.getAxisValue(1));
    this.right_stick_x = cleanMotionValues(paramMotionEvent.getAxisValue(11));
    this.right_stick_y = cleanMotionValues(paramMotionEvent.getAxisValue(14));
    this.left_trigger = paramMotionEvent.getAxisValue(17);
    this.right_trigger = paramMotionEvent.getAxisValue(18);
    if (paramMotionEvent.getAxisValue(16) > this.dpadThreshold) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.dpad_down = bool1;
    if (paramMotionEvent.getAxisValue(16) < -this.dpadThreshold) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.dpad_up = bool1;
    if (paramMotionEvent.getAxisValue(15) > this.dpadThreshold) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.dpad_right = bool1;
    boolean bool1 = bool2;
    if (paramMotionEvent.getAxisValue(15) < -this.dpadThreshold)
      bool1 = true; 
    this.dpad_left = bool1;
    callCallback();
  }
  
  protected void updateButtonAliases() {
    this.circle = this.b;
    this.cross = this.a;
    this.triangle = this.y;
    this.square = this.x;
    this.share = this.back;
    this.options = this.start;
    this.ps = this.guide;
  }
  
  private static class DeviceId extends AbstractMap.SimpleEntry<Integer, Integer> {
    private static final long serialVersionUID = -6429575391769944899L;
    
    public DeviceId(int param1Int1, int param1Int2) {
      super(Integer.valueOf(param1Int1), Integer.valueOf(param1Int2));
    }
    
    public int getProductId() {
      return getValue().intValue();
    }
    
    public int getVendorId() {
      return getKey().intValue();
    }
  }
  
  public static interface GamepadCallback {
    void gamepadChanged(Gamepad param1Gamepad);
  }
  
  public enum Type {
    LOGITECH_F310, SONY_PS4, UNKNOWN, XBOX_360;
    
    static Type[] values;
    
    static {
      Type type = new Type("SONY_PS4", 3);
      SONY_PS4 = type;
      $VALUES = new Type[] { UNKNOWN, LOGITECH_F310, XBOX_360, type };
      values = values();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\Gamepad.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
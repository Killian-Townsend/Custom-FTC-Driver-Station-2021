package com.qualcomm.ftcdriverstation;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.preference.PreferenceManager;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.logitech.LogitechGamepadF310;
import com.qualcomm.hardware.microsoft.MicrosoftGamepadXbox360;
import com.qualcomm.hardware.sony.SonyGamepadPS4;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.firstinspires.ftc.robotcore.internal.ui.GamepadUser;
import org.firstinspires.ftc.robotcore.internal.ui.RobotCoreGamepadManager;

public class GamepadManager implements RobotCoreGamepadManager, InputManager.InputDeviceListener {
  protected static int SOUND_ID_GAMEPAD_CONNECT = 2131558404;
  
  protected static int SOUND_ID_GAMEPAD_DISCONNECT = 2131558405;
  
  public static final String TAG = "GamepadManager";
  
  protected final Context context;
  
  protected boolean debug = false;
  
  protected boolean enabled = true;
  
  protected Map<Integer, Gamepad> gamepadIdToGamepadMap = new ConcurrentHashMap<Integer, Gamepad>();
  
  protected Map<Integer, GamepadPlaceholder> gamepadIdToPlaceholderMap = new ConcurrentHashMap<Integer, GamepadPlaceholder>();
  
  protected Map<GamepadUser, GamepadIndicator> gamepadIndicators = null;
  
  protected SharedPreferences preferences;
  
  protected Set<GamepadUser> recentlyUnassignedUsers = Collections.newSetFromMap(new ConcurrentHashMap<GamepadUser, Boolean>());
  
  protected ArrayList<RemovedGamepadMemory> removedGamepadMemories = new ArrayList<RemovedGamepadMemory>();
  
  protected GamepadTypeOverrideMapper typeOverrideMapper;
  
  protected Map<GamepadUser, Integer> userToGamepadIdMap = new ConcurrentHashMap<GamepadUser, Integer>();
  
  public GamepadManager(Context paramContext) {
    this.context = paramContext;
    this.typeOverrideMapper = new GamepadTypeOverrideMapper(paramContext);
    SoundPlayer.getInstance().preload(paramContext, SOUND_ID_GAMEPAD_CONNECT);
    SoundPlayer.getInstance().preload(paramContext, SOUND_ID_GAMEPAD_DISCONNECT);
  }
  
  public void assignGamepad(int paramInt, GamepadUser paramGamepadUser) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield debug : Z
    //   6: ifeq -> 31
    //   9: ldc 'GamepadManager'
    //   11: ldc 'assigning gampadId=%d user=%s'
    //   13: iconst_2
    //   14: anewarray java/lang/Object
    //   17: dup
    //   18: iconst_0
    //   19: iload_1
    //   20: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   23: aastore
    //   24: dup
    //   25: iconst_1
    //   26: aload_2
    //   27: aastore
    //   28: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   31: aload_0
    //   32: getfield removedGamepadMemories : Ljava/util/ArrayList;
    //   35: invokevirtual iterator : ()Ljava/util/Iterator;
    //   38: astore #4
    //   40: aload #4
    //   42: invokeinterface hasNext : ()Z
    //   47: ifeq -> 81
    //   50: aload #4
    //   52: invokeinterface next : ()Ljava/lang/Object;
    //   57: checkcast com/qualcomm/ftcdriverstation/GamepadManager$RemovedGamepadMemory
    //   60: astore #5
    //   62: aload #5
    //   64: getfield user : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   67: aload_2
    //   68: if_acmpne -> 40
    //   71: aload_0
    //   72: getfield removedGamepadMemories : Ljava/util/ArrayList;
    //   75: aload #5
    //   77: invokevirtual remove : (Ljava/lang/Object;)Z
    //   80: pop
    //   81: aload_0
    //   82: getfield userToGamepadIdMap : Ljava/util/Map;
    //   85: invokeinterface entrySet : ()Ljava/util/Set;
    //   90: invokeinterface iterator : ()Ljava/util/Iterator;
    //   95: astore #4
    //   97: aload #4
    //   99: invokeinterface hasNext : ()Z
    //   104: ifeq -> 187
    //   107: aload #4
    //   109: invokeinterface next : ()Ljava/lang/Object;
    //   114: checkcast java/util/Map$Entry
    //   117: astore #5
    //   119: aload #5
    //   121: invokeinterface getValue : ()Ljava/lang/Object;
    //   126: checkcast java/lang/Integer
    //   129: invokevirtual intValue : ()I
    //   132: iload_1
    //   133: if_icmpne -> 97
    //   136: aload_0
    //   137: getfield userToGamepadIdMap : Ljava/util/Map;
    //   140: aload_2
    //   141: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   146: checkcast java/lang/Integer
    //   149: astore #6
    //   151: aload #6
    //   153: ifnull -> 170
    //   156: aload #6
    //   158: invokevirtual intValue : ()I
    //   161: istore_3
    //   162: iload_3
    //   163: iload_1
    //   164: if_icmpne -> 170
    //   167: aload_0
    //   168: monitorexit
    //   169: return
    //   170: aload_0
    //   171: aload #5
    //   173: invokeinterface getKey : ()Ljava/lang/Object;
    //   178: checkcast org/firstinspires/ftc/robotcore/internal/ui/GamepadUser
    //   181: invokevirtual internalUnassignUser : (Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   184: goto -> 97
    //   187: aload_0
    //   188: getfield userToGamepadIdMap : Ljava/util/Map;
    //   191: aload_2
    //   192: iload_1
    //   193: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   196: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   201: pop
    //   202: aload_0
    //   203: getfield recentlyUnassignedUsers : Ljava/util/Set;
    //   206: aload_2
    //   207: invokeinterface remove : (Ljava/lang/Object;)Z
    //   212: pop
    //   213: aload_0
    //   214: getfield gamepadIndicators : Ljava/util/Map;
    //   217: aload_2
    //   218: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   223: checkcast com/qualcomm/ftcdriverstation/GamepadIndicator
    //   226: getstatic com/qualcomm/ftcdriverstation/GamepadIndicator$State.VISIBLE : Lcom/qualcomm/ftcdriverstation/GamepadIndicator$State;
    //   229: invokevirtual setState : (Lcom/qualcomm/ftcdriverstation/GamepadIndicator$State;)V
    //   232: aload_0
    //   233: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   236: iload_1
    //   237: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   240: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   245: checkcast com/qualcomm/robotcore/hardware/Gamepad
    //   248: astore #4
    //   250: aload #4
    //   252: aload_2
    //   253: invokevirtual setUser : (Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   256: aload #4
    //   258: invokevirtual refreshTimestamp : ()V
    //   261: ldc 'GamepadManager'
    //   263: ldc 'assigned id=%d user=%s type=%s class=%s'
    //   265: iconst_4
    //   266: anewarray java/lang/Object
    //   269: dup
    //   270: iconst_0
    //   271: iload_1
    //   272: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   275: aastore
    //   276: dup
    //   277: iconst_1
    //   278: aload_2
    //   279: aastore
    //   280: dup
    //   281: iconst_2
    //   282: aload #4
    //   284: invokevirtual type : ()Lcom/qualcomm/robotcore/hardware/Gamepad$Type;
    //   287: aastore
    //   288: dup
    //   289: iconst_3
    //   290: aload #4
    //   292: invokevirtual getClass : ()Ljava/lang/Class;
    //   295: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   298: aastore
    //   299: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   302: invokestatic getInstance : ()Lcom/qualcomm/ftccommon/SoundPlayer;
    //   305: aload_0
    //   306: getfield context : Landroid/content/Context;
    //   309: getstatic com/qualcomm/ftcdriverstation/GamepadManager.SOUND_ID_GAMEPAD_CONNECT : I
    //   312: fconst_1
    //   313: iconst_0
    //   314: fconst_1
    //   315: invokevirtual play : (Landroid/content/Context;IFIF)V
    //   318: aload_0
    //   319: monitorexit
    //   320: return
    //   321: astore_2
    //   322: aload_0
    //   323: monitorexit
    //   324: aload_2
    //   325: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	321	finally
    //   31	40	321	finally
    //   40	81	321	finally
    //   81	97	321	finally
    //   97	151	321	finally
    //   156	162	321	finally
    //   170	184	321	finally
    //   187	318	321	finally
  }
  
  public void automagicallyReassignIfPossible(InputDevice paramInputDevice, RemovedGamepadMemory paramRemovedGamepadMemory) {
    if (paramRemovedGamepadMemory.memoriesAmbiguousIfBothPositionsEmpty() && this.userToGamepadIdMap.isEmpty()) {
      this.removedGamepadMemories.clear();
      RobotLog.vv("GamepadManager", "Input device %d was considered for automatic recovery after dropping off the USB bus; however due to ambiguity, no recovery will be performed, and all memories of previously connected gamepads will be forgotten.", new Object[] { Integer.valueOf(paramInputDevice.getId()) });
      Toast.makeText(this.context, "Gamepad not recovered due to ambiguity", 0).show();
      return;
    } 
    if (this.userToGamepadIdMap.get(paramRemovedGamepadMemory.user) == null) {
      SonyGamepadPS4 sonyGamepadPS4;
      if (paramRemovedGamepadMemory.type == Gamepad.Type.XBOX_360) {
        MicrosoftGamepadXbox360 microsoftGamepadXbox360 = new MicrosoftGamepadXbox360();
      } else if (paramRemovedGamepadMemory.type == Gamepad.Type.LOGITECH_F310) {
        LogitechGamepadF310 logitechGamepadF310 = new LogitechGamepadF310();
      } else if (paramRemovedGamepadMemory.type == Gamepad.Type.SONY_PS4) {
        sonyGamepadPS4 = new SonyGamepadPS4();
      } else {
        throw new IllegalStateException();
      } 
      RobotLog.vv("GamepadManager", "Input device %d has been automagically recovered based on USB VID and PID after dropping off the USB bus; treating as %s because that's what we were treating it as when it dropped.", new Object[] { Integer.valueOf(paramInputDevice.getId()), sonyGamepadPS4.getClass().getSimpleName() });
      sonyGamepadPS4.setVidPid(paramInputDevice.getVendorId(), paramInputDevice.getProductId());
      this.gamepadIdToGamepadMap.put(Integer.valueOf(paramInputDevice.getId()), sonyGamepadPS4);
      assignGamepad(paramInputDevice.getId(), paramRemovedGamepadMemory.user);
      Toast.makeText(this.context, String.format("Gamepad %d auto-recovered", new Object[] { Byte.valueOf(paramRemovedGamepadMemory.user.id) }), 0).show();
      return;
    } 
  }
  
  public void clearGamepadAssignments() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield userToGamepadIdMap : Ljava/util/Map;
    //   6: invokeinterface keySet : ()Ljava/util/Set;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_1
    //   17: aload_1
    //   18: invokeinterface hasNext : ()Z
    //   23: ifeq -> 42
    //   26: aload_0
    //   27: aload_1
    //   28: invokeinterface next : ()Ljava/lang/Object;
    //   33: checkcast org/firstinspires/ftc/robotcore/internal/ui/GamepadUser
    //   36: invokevirtual unassignUser : (Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   39: goto -> 17
    //   42: aload_0
    //   43: getfield removedGamepadMemories : Ljava/util/ArrayList;
    //   46: invokevirtual clear : ()V
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	52	finally
    //   17	39	52	finally
    //   42	49	52	finally
  }
  
  public void clearTrackedGamepads() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   6: invokeinterface clear : ()V
    //   11: aload_0
    //   12: getfield gamepadIdToPlaceholderMap : Ljava/util/Map;
    //   15: invokeinterface clear : ()V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  public void close() {}
  
  public void considerInputDeviceForAutomagicReassignment(InputDevice paramInputDevice) {
    // Byte code:
    //   0: aload_0
    //   1: getfield removedGamepadMemories : Ljava/util/ArrayList;
    //   4: invokevirtual iterator : ()Ljava/util/Iterator;
    //   7: astore_3
    //   8: aload_3
    //   9: invokeinterface hasNext : ()Z
    //   14: ifeq -> 38
    //   17: aload_3
    //   18: invokeinterface next : ()Ljava/lang/Object;
    //   23: checkcast com/qualcomm/ftcdriverstation/GamepadManager$RemovedGamepadMemory
    //   26: astore_2
    //   27: aload_2
    //   28: aload_1
    //   29: invokevirtual isTargetForAutomagicReassignment : (Landroid/view/InputDevice;)Z
    //   32: ifeq -> 8
    //   35: goto -> 40
    //   38: aconst_null
    //   39: astore_2
    //   40: aload_2
    //   41: ifnull -> 67
    //   44: aload_0
    //   45: aload_1
    //   46: aload_2
    //   47: invokevirtual automagicallyReassignIfPossible : (Landroid/view/InputDevice;Lcom/qualcomm/ftcdriverstation/GamepadManager$RemovedGamepadMemory;)V
    //   50: goto -> 58
    //   53: astore_1
    //   54: aload_1
    //   55: invokevirtual printStackTrace : ()V
    //   58: aload_0
    //   59: getfield removedGamepadMemories : Ljava/util/ArrayList;
    //   62: aload_2
    //   63: invokevirtual remove : (Ljava/lang/Object;)Z
    //   66: pop
    //   67: return
    // Exception table:
    //   from	to	target	type
    //   44	50	53	java/lang/IllegalStateException
  }
  
  public Gamepad getAssignedGamepadById(Integer paramInteger) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 79
    //   6: aload_0
    //   7: getfield userToGamepadIdMap : Ljava/util/Map;
    //   10: invokeinterface entrySet : ()Ljava/util/Set;
    //   15: invokeinterface iterator : ()Ljava/util/Iterator;
    //   20: astore_2
    //   21: aload_2
    //   22: invokeinterface hasNext : ()Z
    //   27: ifeq -> 79
    //   30: aload_2
    //   31: invokeinterface next : ()Ljava/lang/Object;
    //   36: checkcast java/util/Map$Entry
    //   39: astore_3
    //   40: aload_3
    //   41: invokeinterface getValue : ()Ljava/lang/Object;
    //   46: checkcast java/lang/Integer
    //   49: aload_1
    //   50: invokevirtual equals : (Ljava/lang/Object;)Z
    //   53: ifeq -> 21
    //   56: aload_0
    //   57: aload_3
    //   58: invokeinterface getValue : ()Ljava/lang/Object;
    //   63: checkcast java/lang/Integer
    //   66: invokevirtual getGamepadById : (Ljava/lang/Integer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: areturn
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    //   79: aload_0
    //   80: monitorexit
    //   81: aconst_null
    //   82: areturn
    // Exception table:
    //   from	to	target	type
    //   6	21	74	finally
    //   21	70	74	finally
  }
  
  public Gamepad getGamepadById(Integer paramInteger) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 29
    //   6: aload_0
    //   7: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   10: aload_1
    //   11: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   16: checkcast com/qualcomm/robotcore/hardware/Gamepad
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: areturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    //   29: aload_0
    //   30: monitorexit
    //   31: aconst_null
    //   32: areturn
    // Exception table:
    //   from	to	target	type
    //   6	20	24	finally
  }
  
  public List<Gamepad> getGamepadsForTransmission() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield enabled : Z
    //   6: ifne -> 17
    //   9: invokestatic emptyList : ()Ljava/util/List;
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: new java/util/ArrayList
    //   20: dup
    //   21: iconst_2
    //   22: invokespecial <init> : (I)V
    //   25: astore_1
    //   26: aload_0
    //   27: getfield userToGamepadIdMap : Ljava/util/Map;
    //   30: invokeinterface entrySet : ()Ljava/util/Set;
    //   35: invokeinterface iterator : ()Ljava/util/Iterator;
    //   40: astore_2
    //   41: aload_2
    //   42: invokeinterface hasNext : ()Z
    //   47: ifeq -> 81
    //   50: aload_1
    //   51: aload_0
    //   52: aload_2
    //   53: invokeinterface next : ()Ljava/lang/Object;
    //   58: checkcast java/util/Map$Entry
    //   61: invokeinterface getValue : ()Ljava/lang/Object;
    //   66: checkcast java/lang/Integer
    //   69: invokevirtual getGamepadById : (Ljava/lang/Integer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   72: invokeinterface add : (Ljava/lang/Object;)Z
    //   77: pop
    //   78: goto -> 41
    //   81: aload_0
    //   82: getfield recentlyUnassignedUsers : Ljava/util/Set;
    //   85: invokeinterface iterator : ()Ljava/util/Iterator;
    //   90: astore_2
    //   91: aload_2
    //   92: invokeinterface hasNext : ()Z
    //   97: ifeq -> 176
    //   100: aload_2
    //   101: invokeinterface next : ()Ljava/lang/Object;
    //   106: checkcast org/firstinspires/ftc/robotcore/internal/ui/GamepadUser
    //   109: astore_3
    //   110: ldc 'GamepadManager'
    //   112: ldc_w 'transmitting synthetic gamepad user=%s'
    //   115: iconst_1
    //   116: anewarray java/lang/Object
    //   119: dup
    //   120: iconst_0
    //   121: aload_3
    //   122: aastore
    //   123: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   126: new com/qualcomm/robotcore/hardware/Gamepad
    //   129: dup
    //   130: invokespecial <init> : ()V
    //   133: astore #4
    //   135: aload #4
    //   137: bipush #-2
    //   139: invokevirtual setGamepadId : (I)V
    //   142: aload #4
    //   144: invokevirtual refreshTimestamp : ()V
    //   147: aload #4
    //   149: aload_3
    //   150: invokevirtual setUser : (Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   153: aload_1
    //   154: aload #4
    //   156: invokeinterface add : (Ljava/lang/Object;)Z
    //   161: pop
    //   162: aload_0
    //   163: getfield recentlyUnassignedUsers : Ljava/util/Set;
    //   166: aload_3
    //   167: invokeinterface remove : (Ljava/lang/Object;)Z
    //   172: pop
    //   173: goto -> 91
    //   176: aload_0
    //   177: monitorexit
    //   178: aload_1
    //   179: areturn
    //   180: astore_1
    //   181: aload_0
    //   182: monitorexit
    //   183: aload_1
    //   184: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	180	finally
    //   17	41	180	finally
    //   41	78	180	finally
    //   81	91	180	finally
    //   91	173	180	finally
  }
  
  public Gamepad guessGamepadType(KeyEvent paramKeyEvent, GamepadUserPointer paramGamepadUserPointer) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield gamepadIdToPlaceholderMap : Ljava/util/Map;
    //   6: aload_1
    //   7: invokevirtual getDeviceId : ()I
    //   10: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   13: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   18: checkcast com/qualcomm/ftcdriverstation/GamepadManager$GamepadPlaceholder
    //   21: astore #4
    //   23: aload #4
    //   25: astore_3
    //   26: aload #4
    //   28: ifnonnull -> 58
    //   31: new com/qualcomm/ftcdriverstation/GamepadManager$GamepadPlaceholder
    //   34: dup
    //   35: aload_0
    //   36: invokespecial <init> : (Lcom/qualcomm/ftcdriverstation/GamepadManager;)V
    //   39: astore_3
    //   40: aload_0
    //   41: getfield gamepadIdToPlaceholderMap : Ljava/util/Map;
    //   44: aload_1
    //   45: invokevirtual getDeviceId : ()I
    //   48: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   51: aload_3
    //   52: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   57: pop
    //   58: aload_3
    //   59: aload_1
    //   60: invokevirtual update : (Landroid/view/KeyEvent;)V
    //   63: aload_3
    //   64: getfield key105depressed : Z
    //   67: ifeq -> 137
    //   70: aload_3
    //   71: getfield key97depressed : Z
    //   74: ifne -> 84
    //   77: aload_3
    //   78: getfield key98depressed : Z
    //   81: ifeq -> 211
    //   84: aload_0
    //   85: getfield gamepadIdToPlaceholderMap : Ljava/util/Map;
    //   88: aload_1
    //   89: invokevirtual getDeviceId : ()I
    //   92: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   95: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   100: pop
    //   101: aload_3
    //   102: getfield key97depressed : Z
    //   105: ifeq -> 118
    //   108: aload_2
    //   109: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.ONE : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   112: putfield val : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   115: goto -> 125
    //   118: aload_2
    //   119: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.TWO : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   122: putfield val : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   125: new com/qualcomm/hardware/sony/SonyGamepadPS4
    //   128: dup
    //   129: invokespecial <init> : ()V
    //   132: astore_1
    //   133: aload_0
    //   134: monitorexit
    //   135: aload_1
    //   136: areturn
    //   137: aload_3
    //   138: getfield key108depressed : Z
    //   141: ifeq -> 211
    //   144: aload_3
    //   145: getfield key96depressed : Z
    //   148: ifne -> 158
    //   151: aload_3
    //   152: getfield key97depressed : Z
    //   155: ifeq -> 211
    //   158: aload_0
    //   159: getfield gamepadIdToPlaceholderMap : Ljava/util/Map;
    //   162: aload_1
    //   163: invokevirtual getDeviceId : ()I
    //   166: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   169: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   174: pop
    //   175: aload_3
    //   176: getfield key96depressed : Z
    //   179: ifeq -> 192
    //   182: aload_2
    //   183: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.ONE : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   186: putfield val : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   189: goto -> 199
    //   192: aload_2
    //   193: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.TWO : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   196: putfield val : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   199: new com/qualcomm/hardware/microsoft/MicrosoftGamepadXbox360
    //   202: dup
    //   203: invokespecial <init> : ()V
    //   206: astore_1
    //   207: aload_0
    //   208: monitorexit
    //   209: aload_1
    //   210: areturn
    //   211: aload_0
    //   212: monitorexit
    //   213: aconst_null
    //   214: areturn
    //   215: astore_1
    //   216: aload_0
    //   217: monitorexit
    //   218: aload_1
    //   219: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	215	finally
    //   31	58	215	finally
    //   58	84	215	finally
    //   84	115	215	finally
    //   118	125	215	finally
    //   125	133	215	finally
    //   137	158	215	finally
    //   158	189	215	finally
    //   192	199	215	finally
    //   199	207	215	finally
  }
  
  public void handleGamepadEvent(KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual getDeviceId : ()I
    //   6: invokestatic getDevice : (I)Landroid/view/InputDevice;
    //   9: astore_2
    //   10: aload_0
    //   11: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   14: aload_2
    //   15: invokevirtual getId : ()I
    //   18: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   21: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   26: checkcast com/qualcomm/robotcore/hardware/Gamepad
    //   29: astore_3
    //   30: aload_3
    //   31: ifnull -> 114
    //   34: aload_3
    //   35: getfield start : Z
    //   38: ifeq -> 91
    //   41: aload_3
    //   42: getfield a : Z
    //   45: ifne -> 55
    //   48: aload_3
    //   49: getfield b : Z
    //   52: ifeq -> 91
    //   55: aload_3
    //   56: getfield a : Z
    //   59: ifeq -> 73
    //   62: aload_0
    //   63: aload_2
    //   64: invokevirtual getId : ()I
    //   67: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.ONE : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   70: invokevirtual assignGamepad : (ILorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   73: aload_3
    //   74: getfield b : Z
    //   77: ifeq -> 91
    //   80: aload_0
    //   81: aload_2
    //   82: invokevirtual getId : ()I
    //   85: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.TWO : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   88: invokevirtual assignGamepad : (ILorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   91: aload_3
    //   92: aload_1
    //   93: invokevirtual update : (Landroid/view/KeyEvent;)V
    //   96: aload_0
    //   97: aload_0
    //   98: aload_1
    //   99: invokevirtual getDeviceId : ()I
    //   102: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   105: invokevirtual getAssignedGamepadById : (Ljava/lang/Integer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   108: invokevirtual indicateGamepad : (Lcom/qualcomm/robotcore/hardware/Gamepad;)V
    //   111: goto -> 391
    //   114: aload_0
    //   115: aload_2
    //   116: invokevirtual knownInputDeviceToGamepad : (Landroid/view/InputDevice;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   119: astore_3
    //   120: aload_3
    //   121: ifnull -> 194
    //   124: aload_3
    //   125: aload_1
    //   126: invokevirtual update : (Landroid/view/KeyEvent;)V
    //   129: aload_3
    //   130: aload_2
    //   131: invokevirtual getVendorId : ()I
    //   134: aload_2
    //   135: invokevirtual getProductId : ()I
    //   138: invokevirtual setVidPid : (II)V
    //   141: aload_0
    //   142: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   145: aload_2
    //   146: invokevirtual getId : ()I
    //   149: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   152: aload_3
    //   153: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   158: pop
    //   159: ldc 'GamepadManager'
    //   161: ldc_w 'Input device %d has been autodetected based on USB VID and PID as %s'
    //   164: iconst_2
    //   165: anewarray java/lang/Object
    //   168: dup
    //   169: iconst_0
    //   170: aload_2
    //   171: invokevirtual getId : ()I
    //   174: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   177: aastore
    //   178: dup
    //   179: iconst_1
    //   180: aload_3
    //   181: invokevirtual getClass : ()Ljava/lang/Class;
    //   184: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   187: aastore
    //   188: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   191: aload_0
    //   192: monitorexit
    //   193: return
    //   194: aload_0
    //   195: aload_2
    //   196: invokevirtual overriddenInputDeviceToGamepad : (Landroid/view/InputDevice;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   199: astore_3
    //   200: aload_3
    //   201: ifnull -> 274
    //   204: aload_3
    //   205: aload_1
    //   206: invokevirtual update : (Landroid/view/KeyEvent;)V
    //   209: aload_3
    //   210: aload_2
    //   211: invokevirtual getVendorId : ()I
    //   214: aload_2
    //   215: invokevirtual getProductId : ()I
    //   218: invokevirtual setVidPid : (II)V
    //   221: aload_0
    //   222: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   225: aload_2
    //   226: invokevirtual getId : ()I
    //   229: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   232: aload_3
    //   233: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   238: pop
    //   239: ldc 'GamepadManager'
    //   241: ldc_w 'Input device %d has a USB VID and PID that has an entry in override list; treating as %s'
    //   244: iconst_2
    //   245: anewarray java/lang/Object
    //   248: dup
    //   249: iconst_0
    //   250: aload_2
    //   251: invokevirtual getId : ()I
    //   254: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   257: aastore
    //   258: dup
    //   259: iconst_1
    //   260: aload_3
    //   261: invokevirtual getClass : ()Ljava/lang/Class;
    //   264: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   267: aastore
    //   268: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   271: aload_0
    //   272: monitorexit
    //   273: return
    //   274: new com/qualcomm/ftcdriverstation/GamepadManager$GamepadUserPointer
    //   277: dup
    //   278: aload_0
    //   279: invokespecial <init> : (Lcom/qualcomm/ftcdriverstation/GamepadManager;)V
    //   282: astore_3
    //   283: aload_0
    //   284: aload_1
    //   285: aload_3
    //   286: invokevirtual guessGamepadType : (Landroid/view/KeyEvent;Lcom/qualcomm/ftcdriverstation/GamepadManager$GamepadUserPointer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   289: astore_1
    //   290: aload_1
    //   291: ifnull -> 391
    //   294: aload_1
    //   295: aload_2
    //   296: invokevirtual getVendorId : ()I
    //   299: aload_2
    //   300: invokevirtual getProductId : ()I
    //   303: invokevirtual setVidPid : (II)V
    //   306: aload_0
    //   307: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   310: aload_2
    //   311: invokevirtual getId : ()I
    //   314: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   317: aload_1
    //   318: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   323: pop
    //   324: ldc 'GamepadManager'
    //   326: ldc_w 'Using quantum superposition to guess that input device %d should be treated as %s'
    //   329: iconst_2
    //   330: anewarray java/lang/Object
    //   333: dup
    //   334: iconst_0
    //   335: aload_2
    //   336: invokevirtual getId : ()I
    //   339: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   342: aastore
    //   343: dup
    //   344: iconst_1
    //   345: aload_1
    //   346: invokevirtual getClass : ()Ljava/lang/Class;
    //   349: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   352: aastore
    //   353: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   356: aload_3
    //   357: getfield val : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   360: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.ONE : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   363: if_acmpne -> 380
    //   366: aload_0
    //   367: aload_2
    //   368: invokevirtual getId : ()I
    //   371: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.ONE : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   374: invokevirtual assignGamepad : (ILorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   377: goto -> 391
    //   380: aload_0
    //   381: aload_2
    //   382: invokevirtual getId : ()I
    //   385: getstatic org/firstinspires/ftc/robotcore/internal/ui/GamepadUser.TWO : Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   388: invokevirtual assignGamepad : (ILorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   391: aload_0
    //   392: monitorexit
    //   393: return
    //   394: astore_1
    //   395: aload_0
    //   396: monitorexit
    //   397: aload_1
    //   398: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	394	finally
    //   34	55	394	finally
    //   55	73	394	finally
    //   73	91	394	finally
    //   91	111	394	finally
    //   114	120	394	finally
    //   124	191	394	finally
    //   194	200	394	finally
    //   204	271	394	finally
    //   274	290	394	finally
    //   294	377	394	finally
    //   380	391	394	finally
  }
  
  public void handleGamepadEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual getDeviceId : ()I
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokevirtual getGamepadById : (Ljava/lang/Integer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   13: astore_2
    //   14: aload_2
    //   15: ifnonnull -> 21
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: aload_2
    //   22: aload_1
    //   23: invokevirtual update : (Landroid/view/MotionEvent;)V
    //   26: aload_0
    //   27: aload_0
    //   28: aload_1
    //   29: invokevirtual getDeviceId : ()I
    //   32: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   35: invokevirtual getAssignedGamepadById : (Ljava/lang/Integer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   38: invokevirtual indicateGamepad : (Lcom/qualcomm/robotcore/hardware/Gamepad;)V
    //   41: aload_0
    //   42: monitorexit
    //   43: return
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	44	finally
    //   21	41	44	finally
  }
  
  protected void indicateGamepad(Gamepad paramGamepad) {
    if (paramGamepad != null)
      ((GamepadIndicator)this.gamepadIndicators.get(paramGamepad.getUser())).setState(GamepadIndicator.State.INDICATE); 
  }
  
  protected void internalUnassignUser(GamepadUser paramGamepadUser) {
    ((GamepadIndicator)this.gamepadIndicators.get(paramGamepadUser)).setState(GamepadIndicator.State.INVISIBLE);
    this.userToGamepadIdMap.remove(paramGamepadUser);
    this.recentlyUnassignedUsers.add(paramGamepadUser);
  }
  
  public Gamepad knownInputDeviceToGamepad(InputDevice paramInputDevice) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual getVendorId : ()I
    //   6: istore_2
    //   7: aload_1
    //   8: invokevirtual getProductId : ()I
    //   11: istore_3
    //   12: iload_2
    //   13: iload_3
    //   14: invokestatic matchesVidPid : (II)Z
    //   17: ifeq -> 32
    //   20: new com/qualcomm/hardware/microsoft/MicrosoftGamepadXbox360
    //   23: dup
    //   24: invokespecial <init> : ()V
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: areturn
    //   32: iload_2
    //   33: iload_3
    //   34: invokestatic matchesVidPid : (II)Z
    //   37: ifeq -> 52
    //   40: new com/qualcomm/hardware/logitech/LogitechGamepadF310
    //   43: dup
    //   44: invokespecial <init> : ()V
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: areturn
    //   52: iload_2
    //   53: iload_3
    //   54: invokestatic matchesVidPid : (II)Z
    //   57: ifeq -> 72
    //   60: new com/qualcomm/hardware/sony/SonyGamepadPS4
    //   63: dup
    //   64: invokespecial <init> : ()V
    //   67: astore_1
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_1
    //   71: areturn
    //   72: aload_0
    //   73: monitorexit
    //   74: aconst_null
    //   75: areturn
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	76	finally
    //   32	48	76	finally
    //   52	68	76	finally
  }
  
  public void onAssignedGamepadDropped(Gamepad paramGamepad) {
    SoundPlayer.getInstance().play(this.context, SOUND_ID_GAMEPAD_DISCONNECT, 1.0F, 0, 1.0F);
    Toast.makeText(this.context, String.format("Gamepad %d connection lost", new Object[] { Byte.valueOf((paramGamepad.getUser()).id) }), 0).show();
    RemovedGamepadMemory removedGamepadMemory = new RemovedGamepadMemory();
    removedGamepadMemory.vid = paramGamepad.vid;
    removedGamepadMemory.pid = paramGamepad.pid;
    removedGamepadMemory.user = paramGamepad.getUser();
    removedGamepadMemory.type = paramGamepad.type();
    if (paramGamepad.getUser() == GamepadUser.ONE) {
      paramGamepad = getAssignedGamepadById(this.userToGamepadIdMap.get(GamepadUser.TWO));
    } else {
      paramGamepad = getAssignedGamepadById(this.userToGamepadIdMap.get(GamepadUser.ONE));
    } 
    if (paramGamepad != null) {
      removedGamepadMemory.othergamepad_vid = paramGamepad.vid;
      removedGamepadMemory.othergamepad_pid = paramGamepad.pid;
    } 
    this.removedGamepadMemories.add(removedGamepadMemory);
  }
  
  public void onInputDeviceAdded(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'GamepadManager'
    //   4: ldc_w 'New input device (id = %d) detected.'
    //   7: iconst_1
    //   8: anewarray java/lang/Object
    //   11: dup
    //   12: iconst_0
    //   13: iload_1
    //   14: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   17: aastore
    //   18: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   21: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   24: aload_0
    //   25: iload_1
    //   26: invokestatic getDevice : (I)Landroid/view/InputDevice;
    //   29: invokevirtual considerInputDeviceForAutomagicReassignment : (Landroid/view/InputDevice;)V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: astore_2
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_2
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	35	finally
  }
  
  public void onInputDeviceChanged(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'GamepadManager'
    //   4: ldc_w 'Input device (id = %d) modified.'
    //   7: iconst_1
    //   8: anewarray java/lang/Object
    //   11: dup
    //   12: iconst_0
    //   13: iload_1
    //   14: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   17: aastore
    //   18: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   21: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore_2
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_2
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	27	finally
  }
  
  public void onInputDeviceRemoved(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'GamepadManager'
    //   4: ldc_w 'Input device (id = %d) removed.'
    //   7: iconst_1
    //   8: anewarray java/lang/Object
    //   11: dup
    //   12: iconst_0
    //   13: iload_1
    //   14: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   17: aastore
    //   18: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   21: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   24: aload_0
    //   25: iload_1
    //   26: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   29: invokevirtual getAssignedGamepadById : (Ljava/lang/Integer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   32: astore_2
    //   33: aload_2
    //   34: ifnull -> 42
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual onAssignedGamepadDropped : (Lcom/qualcomm/robotcore/hardware/Gamepad;)V
    //   42: aload_0
    //   43: iload_1
    //   44: invokevirtual removeGamepad : (I)V
    //   47: aload_0
    //   48: monitorexit
    //   49: return
    //   50: astore_2
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_2
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	50	finally
    //   37	42	50	finally
    //   42	47	50	finally
  }
  
  public void open() {
    this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
  }
  
  public Gamepad overriddenInputDeviceToGamepad(InputDevice paramInputDevice) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual getVendorId : ()I
    //   6: istore_2
    //   7: aload_1
    //   8: invokevirtual getProductId : ()I
    //   11: istore_3
    //   12: aload_0
    //   13: getfield typeOverrideMapper : Lcom/qualcomm/ftcdriverstation/GamepadTypeOverrideMapper;
    //   16: iload_2
    //   17: iload_3
    //   18: invokevirtual getEntryFor : (II)Lcom/qualcomm/ftcdriverstation/GamepadTypeOverrideMapper$GamepadTypeOverrideEntry;
    //   21: astore_1
    //   22: aload_1
    //   23: ifnonnull -> 30
    //   26: aload_0
    //   27: monitorexit
    //   28: aconst_null
    //   29: areturn
    //   30: aload_1
    //   31: invokevirtual createGamepad : ()Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: areturn
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	39	finally
    //   30	35	39	finally
  }
  
  public void removeGamepad(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   7: invokevirtual getAssignedGamepadById : (Ljava/lang/Integer;)Lcom/qualcomm/robotcore/hardware/Gamepad;
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 23
    //   15: aload_0
    //   16: aload_2
    //   17: invokevirtual getUser : ()Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;
    //   20: invokevirtual internalUnassignUser : (Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   23: aload_0
    //   24: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   27: iload_1
    //   28: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   31: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   36: pop
    //   37: aload_0
    //   38: getfield gamepadIdToPlaceholderMap : Ljava/util/Map;
    //   41: iload_1
    //   42: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   45: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   50: ifnull -> 67
    //   53: aload_0
    //   54: getfield gamepadIdToPlaceholderMap : Ljava/util/Map;
    //   57: iload_1
    //   58: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   61: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   66: pop
    //   67: aload_0
    //   68: monitorexit
    //   69: return
    //   70: astore_2
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_2
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	70	finally
    //   15	23	70	finally
    //   23	67	70	finally
  }
  
  public void setDebug(boolean paramBoolean) {
    this.debug = paramBoolean;
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.enabled = paramBoolean;
  }
  
  public void setGamepadIndicators(Map<GamepadUser, GamepadIndicator> paramMap) {
    this.gamepadIndicators = paramMap;
  }
  
  public void unassignUser(GamepadUser paramGamepadUser) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield userToGamepadIdMap : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast java/lang/Integer
    //   15: astore_2
    //   16: aload_2
    //   17: ifnull -> 31
    //   20: aload_0
    //   21: getfield gamepadIdToGamepadMap : Ljava/util/Map;
    //   24: aload_2
    //   25: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   30: pop
    //   31: aload_0
    //   32: aload_1
    //   33: invokevirtual internalUnassignUser : (Lorg/firstinspires/ftc/robotcore/internal/ui/GamepadUser;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	39	finally
    //   20	31	39	finally
    //   31	36	39	finally
  }
  
  class GamepadPlaceholder {
    boolean key105depressed = false;
    
    boolean key108depressed = false;
    
    boolean key96depressed = false;
    
    boolean key97depressed = false;
    
    boolean key98depressed = false;
    
    boolean pressed(KeyEvent param1KeyEvent) {
      return (param1KeyEvent.getAction() == 0);
    }
    
    void update(KeyEvent param1KeyEvent) {
      if (param1KeyEvent.getKeyCode() == 105) {
        this.key105depressed = pressed(param1KeyEvent);
        return;
      } 
      if (param1KeyEvent.getKeyCode() == 108) {
        this.key108depressed = pressed(param1KeyEvent);
        return;
      } 
      if (param1KeyEvent.getKeyCode() == 96) {
        this.key96depressed = pressed(param1KeyEvent);
        return;
      } 
      if (param1KeyEvent.getKeyCode() == 97) {
        this.key97depressed = pressed(param1KeyEvent);
        return;
      } 
      if (param1KeyEvent.getKeyCode() == 98)
        this.key98depressed = pressed(param1KeyEvent); 
    }
  }
  
  class GamepadUserPointer {
    GamepadUser val;
  }
  
  class RemovedGamepadMemory {
    int othergamepad_pid;
    
    int othergamepad_vid;
    
    int pid;
    
    Gamepad.Type type;
    
    GamepadUser user;
    
    int vid;
    
    boolean isTargetForAutomagicReassignment(InputDevice param1InputDevice) {
      return (param1InputDevice.getVendorId() == this.vid && param1InputDevice.getProductId() == this.pid);
    }
    
    boolean memoriesAmbiguousIfBothPositionsEmpty() {
      return (this.vid == this.othergamepad_vid && this.pid == this.othergamepad_pid);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\GamepadManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
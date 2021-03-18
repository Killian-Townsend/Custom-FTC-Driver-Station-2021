package org.firstinspires.ftc.robotcore.external.android;

import android.content.Context;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class AndroidTextToSpeech {
  private static final String TAG = "AndroidTextToSpeech";
  
  private volatile boolean initializationFailed = false;
  
  private CountDownLatch initializationLatch = new CountDownLatch(1);
  
  private TextToSpeech textToSpeech;
  
  private void waitForInitializationToFinish() throws InterruptedException {
    if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
      this.initializationLatch.await();
      return;
    } 
    RobotLog.ee("AndroidTextToSpeech", "AndroidTextToSpeech used from Android Main Thread. This is not allowed.");
    throw new RuntimeException("AndroidTextToSpeech used from Android Main Thread. This is not allowed.");
  }
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: ifnull -> 33
    //   9: aload_0
    //   10: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   13: invokevirtual shutdown : ()V
    //   16: aload_0
    //   17: aconst_null
    //   18: putfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   21: aload_0
    //   22: new java/util/concurrent/CountDownLatch
    //   25: dup
    //   26: iconst_1
    //   27: invokespecial <init> : (I)V
    //   30: putfield initializationLatch : Ljava/util/concurrent/CountDownLatch;
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore_1
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_1
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	36	finally
  }
  
  public String getCountryCode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 56
    //   11: aload_0
    //   12: invokespecial waitForInitializationToFinish : ()V
    //   15: aload_0
    //   16: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   19: invokevirtual getLanguage : ()Ljava/util/Locale;
    //   22: astore_1
    //   23: aload_1
    //   24: ifnull -> 51
    //   27: aload_1
    //   28: invokevirtual getCountry : ()Ljava/lang/String;
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: areturn
    //   36: astore_1
    //   37: ldc 'AndroidTextToSpeech'
    //   39: aload_1
    //   40: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   42: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   45: invokestatic currentThread : ()Ljava/lang/Thread;
    //   48: invokevirtual interrupt : ()V
    //   51: aload_0
    //   52: monitorexit
    //   53: ldc ''
    //   55: areturn
    //   56: new java/lang/IllegalStateException
    //   59: dup
    //   60: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   62: invokespecial <init> : (Ljava/lang/String;)V
    //   65: athrow
    //   66: astore_1
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_1
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	66	finally
    //   11	23	36	java/lang/InterruptedException
    //   11	23	66	finally
    //   27	32	36	java/lang/InterruptedException
    //   27	32	66	finally
    //   37	51	66	finally
    //   56	66	66	finally
  }
  
  public String getLanguageCode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 56
    //   11: aload_0
    //   12: invokespecial waitForInitializationToFinish : ()V
    //   15: aload_0
    //   16: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   19: invokevirtual getLanguage : ()Ljava/util/Locale;
    //   22: astore_1
    //   23: aload_1
    //   24: ifnull -> 51
    //   27: aload_1
    //   28: invokevirtual getLanguage : ()Ljava/lang/String;
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: areturn
    //   36: astore_1
    //   37: ldc 'AndroidTextToSpeech'
    //   39: aload_1
    //   40: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   42: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   45: invokestatic currentThread : ()Ljava/lang/Thread;
    //   48: invokevirtual interrupt : ()V
    //   51: aload_0
    //   52: monitorexit
    //   53: ldc ''
    //   55: areturn
    //   56: new java/lang/IllegalStateException
    //   59: dup
    //   60: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   62: invokespecial <init> : (Ljava/lang/String;)V
    //   65: athrow
    //   66: astore_1
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_1
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	66	finally
    //   11	23	36	java/lang/InterruptedException
    //   11	23	66	finally
    //   27	32	36	java/lang/InterruptedException
    //   27	32	66	finally
    //   37	51	66	finally
    //   56	66	66	finally
  }
  
  public String getStatus() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: ifnull -> 41
    //   9: aload_0
    //   10: getfield initializationLatch : Ljava/util/concurrent/CountDownLatch;
    //   13: invokevirtual getCount : ()J
    //   16: lconst_0
    //   17: lcmp
    //   18: ifle -> 24
    //   21: goto -> 41
    //   24: aload_0
    //   25: getfield initializationFailed : Z
    //   28: ifeq -> 36
    //   31: aload_0
    //   32: monitorexit
    //   33: ldc 'Error code -1'
    //   35: areturn
    //   36: aload_0
    //   37: monitorexit
    //   38: ldc 'Success'
    //   40: areturn
    //   41: aload_0
    //   42: monitorexit
    //   43: ldc 'Not initialized'
    //   45: areturn
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	46	finally
    //   24	31	46	finally
  }
  
  public void initialize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic getDefContext : ()Landroid/content/Context;
    //   5: astore_1
    //   6: ldc 'AndroidTextToSpeech'
    //   8: ldc 'Beginning TTS initialization'
    //   10: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   13: aload_0
    //   14: new android/speech/tts/TextToSpeech
    //   17: dup
    //   18: aload_1
    //   19: new org/firstinspires/ftc/robotcore/external/android/AndroidTextToSpeech$1
    //   22: dup
    //   23: aload_0
    //   24: aload_1
    //   25: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/android/AndroidTextToSpeech;Landroid/content/Context;)V
    //   28: invokespecial <init> : (Landroid/content/Context;Landroid/speech/tts/TextToSpeech$OnInitListener;)V
    //   31: putfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	37	finally
  }
  
  public boolean isLanguageAndCountryAvailable(String paramString1, String paramString2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore #5
    //   8: aload #5
    //   10: ifnull -> 71
    //   13: iconst_0
    //   14: istore #4
    //   16: aload_0
    //   17: invokespecial waitForInitializationToFinish : ()V
    //   20: new java/util/Locale
    //   23: dup
    //   24: aload_1
    //   25: aload_2
    //   26: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   29: astore_1
    //   30: aload_0
    //   31: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   34: aload_1
    //   35: invokevirtual isLanguageAvailable : (Ljava/util/Locale;)I
    //   38: istore_3
    //   39: iload_3
    //   40: iconst_2
    //   41: if_icmpne -> 47
    //   44: iconst_1
    //   45: istore #4
    //   47: aload_0
    //   48: monitorexit
    //   49: iload #4
    //   51: ireturn
    //   52: astore_1
    //   53: ldc 'AndroidTextToSpeech'
    //   55: aload_1
    //   56: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   58: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   61: invokestatic currentThread : ()Ljava/lang/Thread;
    //   64: invokevirtual interrupt : ()V
    //   67: aload_0
    //   68: monitorexit
    //   69: iconst_0
    //   70: ireturn
    //   71: new java/lang/IllegalStateException
    //   74: dup
    //   75: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   77: invokespecial <init> : (Ljava/lang/String;)V
    //   80: athrow
    //   81: astore_1
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_1
    //   85: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	81	finally
    //   16	39	52	java/lang/InterruptedException
    //   16	39	81	finally
    //   53	67	81	finally
    //   71	81	81	finally
  }
  
  public boolean isLanguageAvailable(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: ifnull -> 63
    //   9: new java/util/Locale
    //   12: dup
    //   13: aload_1
    //   14: invokespecial <init> : (Ljava/lang/String;)V
    //   17: astore_1
    //   18: iconst_0
    //   19: istore_3
    //   20: aload_0
    //   21: invokespecial waitForInitializationToFinish : ()V
    //   24: aload_0
    //   25: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   28: aload_1
    //   29: invokevirtual isLanguageAvailable : (Ljava/util/Locale;)I
    //   32: istore_2
    //   33: iload_2
    //   34: iconst_2
    //   35: if_icmpne -> 40
    //   38: iconst_1
    //   39: istore_3
    //   40: aload_0
    //   41: monitorexit
    //   42: iload_3
    //   43: ireturn
    //   44: astore_1
    //   45: ldc 'AndroidTextToSpeech'
    //   47: aload_1
    //   48: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   50: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   53: invokestatic currentThread : ()Ljava/lang/Thread;
    //   56: invokevirtual interrupt : ()V
    //   59: aload_0
    //   60: monitorexit
    //   61: iconst_0
    //   62: ireturn
    //   63: new java/lang/IllegalStateException
    //   66: dup
    //   67: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   69: invokespecial <init> : (Ljava/lang/String;)V
    //   72: athrow
    //   73: astore_1
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_1
    //   77: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	73	finally
    //   20	33	44	java/lang/InterruptedException
    //   20	33	73	finally
    //   45	59	73	finally
    //   63	73	73	finally
  }
  
  public boolean isLocaleAvailable(Locale paramLocale) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore #4
    //   8: aload #4
    //   10: ifnull -> 58
    //   13: iconst_0
    //   14: istore_3
    //   15: aload_0
    //   16: invokespecial waitForInitializationToFinish : ()V
    //   19: aload_0
    //   20: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   23: aload_1
    //   24: invokevirtual isLanguageAvailable : (Ljava/util/Locale;)I
    //   27: istore_2
    //   28: iload_2
    //   29: iconst_2
    //   30: if_icmpne -> 35
    //   33: iconst_1
    //   34: istore_3
    //   35: aload_0
    //   36: monitorexit
    //   37: iload_3
    //   38: ireturn
    //   39: astore_1
    //   40: ldc 'AndroidTextToSpeech'
    //   42: aload_1
    //   43: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   45: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   48: invokestatic currentThread : ()Ljava/lang/Thread;
    //   51: invokevirtual interrupt : ()V
    //   54: aload_0
    //   55: monitorexit
    //   56: iconst_0
    //   57: ireturn
    //   58: new java/lang/IllegalStateException
    //   61: dup
    //   62: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   64: invokespecial <init> : (Ljava/lang/String;)V
    //   67: athrow
    //   68: astore_1
    //   69: aload_0
    //   70: monitorexit
    //   71: aload_1
    //   72: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	68	finally
    //   15	28	39	java/lang/InterruptedException
    //   15	28	68	finally
    //   40	54	68	finally
    //   58	68	68	finally
  }
  
  public boolean isSpeaking() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: ifnull -> 21
    //   9: aload_0
    //   10: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   13: invokevirtual isSpeaking : ()Z
    //   16: istore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: iload_1
    //   20: ireturn
    //   21: new java/lang/IllegalStateException
    //   24: dup
    //   25: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   27: invokespecial <init> : (Ljava/lang/String;)V
    //   30: athrow
    //   31: astore_2
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_2
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	31	finally
    //   21	31	31	finally
  }
  
  public void setLanguage(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 52
    //   11: aload_0
    //   12: invokespecial waitForInitializationToFinish : ()V
    //   15: aload_0
    //   16: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   19: new java/util/Locale
    //   22: dup
    //   23: aload_1
    //   24: invokespecial <init> : (Ljava/lang/String;)V
    //   27: invokevirtual setLanguage : (Ljava/util/Locale;)I
    //   30: pop
    //   31: goto -> 49
    //   34: astore_1
    //   35: ldc 'AndroidTextToSpeech'
    //   37: aload_1
    //   38: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   40: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   43: invokestatic currentThread : ()Ljava/lang/Thread;
    //   46: invokevirtual interrupt : ()V
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: new java/lang/IllegalStateException
    //   55: dup
    //   56: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   58: invokespecial <init> : (Ljava/lang/String;)V
    //   61: athrow
    //   62: astore_1
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_1
    //   66: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	62	finally
    //   11	31	34	java/lang/InterruptedException
    //   11	31	62	finally
    //   35	49	62	finally
    //   52	62	62	finally
  }
  
  public void setLanguageAndCountry(String paramString1, String paramString2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore_3
    //   7: aload_3
    //   8: ifnull -> 53
    //   11: aload_0
    //   12: invokespecial waitForInitializationToFinish : ()V
    //   15: aload_0
    //   16: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   19: new java/util/Locale
    //   22: dup
    //   23: aload_1
    //   24: aload_2
    //   25: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   28: invokevirtual setLanguage : (Ljava/util/Locale;)I
    //   31: pop
    //   32: goto -> 50
    //   35: astore_1
    //   36: ldc 'AndroidTextToSpeech'
    //   38: aload_1
    //   39: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   41: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   44: invokestatic currentThread : ()Ljava/lang/Thread;
    //   47: invokevirtual interrupt : ()V
    //   50: aload_0
    //   51: monitorexit
    //   52: return
    //   53: new java/lang/IllegalStateException
    //   56: dup
    //   57: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   59: invokespecial <init> : (Ljava/lang/String;)V
    //   62: athrow
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	63	finally
    //   11	32	35	java/lang/InterruptedException
    //   11	32	63	finally
    //   36	50	63	finally
    //   53	63	63	finally
  }
  
  public void setPitch(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 45
    //   11: aload_0
    //   12: invokespecial waitForInitializationToFinish : ()V
    //   15: aload_0
    //   16: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   19: fload_1
    //   20: invokevirtual setPitch : (F)I
    //   23: pop
    //   24: goto -> 42
    //   27: astore_2
    //   28: ldc 'AndroidTextToSpeech'
    //   30: aload_2
    //   31: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   33: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   36: invokestatic currentThread : ()Ljava/lang/Thread;
    //   39: invokevirtual interrupt : ()V
    //   42: aload_0
    //   43: monitorexit
    //   44: return
    //   45: new java/lang/IllegalStateException
    //   48: dup
    //   49: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   51: invokespecial <init> : (Ljava/lang/String;)V
    //   54: athrow
    //   55: astore_2
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_2
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	55	finally
    //   11	24	27	java/lang/InterruptedException
    //   11	24	55	finally
    //   28	42	55	finally
    //   45	55	55	finally
  }
  
  public void setSpeechRate(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 45
    //   11: aload_0
    //   12: invokespecial waitForInitializationToFinish : ()V
    //   15: aload_0
    //   16: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   19: fload_1
    //   20: invokevirtual setSpeechRate : (F)I
    //   23: pop
    //   24: goto -> 42
    //   27: astore_2
    //   28: ldc 'AndroidTextToSpeech'
    //   30: aload_2
    //   31: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   33: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   36: invokestatic currentThread : ()Ljava/lang/Thread;
    //   39: invokevirtual interrupt : ()V
    //   42: aload_0
    //   43: monitorexit
    //   44: return
    //   45: new java/lang/IllegalStateException
    //   48: dup
    //   49: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   51: invokespecial <init> : (Ljava/lang/String;)V
    //   54: athrow
    //   55: astore_2
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_2
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	55	finally
    //   11	24	27	java/lang/InterruptedException
    //   11	24	55	finally
    //   28	42	55	finally
    //   45	55	55	finally
  }
  
  public void speak(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 47
    //   11: aload_0
    //   12: invokespecial waitForInitializationToFinish : ()V
    //   15: aload_0
    //   16: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   19: aload_1
    //   20: iconst_0
    //   21: aconst_null
    //   22: invokevirtual speak : (Ljava/lang/String;ILjava/util/HashMap;)I
    //   25: pop
    //   26: goto -> 44
    //   29: astore_1
    //   30: ldc 'AndroidTextToSpeech'
    //   32: aload_1
    //   33: ldc 'InterruptedException thrown while waiting for TTS initialization'
    //   35: invokestatic ww : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   38: invokestatic currentThread : ()Ljava/lang/Thread;
    //   41: invokevirtual interrupt : ()V
    //   44: aload_0
    //   45: monitorexit
    //   46: return
    //   47: new java/lang/IllegalStateException
    //   50: dup
    //   51: ldc 'You forgot to call AndroidTextToSpeech.initialize!'
    //   53: invokespecial <init> : (Ljava/lang/String;)V
    //   56: athrow
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	57	finally
    //   11	26	29	java/lang/InterruptedException
    //   11	26	57	finally
    //   30	44	57	finally
    //   47	57	57	finally
  }
  
  public void stop() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   6: ifnull -> 17
    //   9: aload_0
    //   10: getfield textToSpeech : Landroid/speech/tts/TextToSpeech;
    //   13: invokevirtual stop : ()I
    //   16: pop
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\android\AndroidTextToSpeech.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
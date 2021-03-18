package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTypeAdapter extends TypeAdapter<Date> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
        return (param1TypeToken.getRawType() == Date.class) ? new DateTypeAdapter() : null;
      }
    };
  
  private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
  
  private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);
  
  private Date deserializeToDate(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield localFormat : Ljava/text/DateFormat;
    //   6: aload_1
    //   7: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   10: astore_2
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_2
    //   14: areturn
    //   15: astore_1
    //   16: goto -> 60
    //   19: aload_0
    //   20: getfield enUsFormat : Ljava/text/DateFormat;
    //   23: aload_1
    //   24: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   27: astore_2
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_2
    //   31: areturn
    //   32: aload_1
    //   33: new java/text/ParsePosition
    //   36: dup
    //   37: iconst_0
    //   38: invokespecial <init> : (I)V
    //   41: invokestatic parse : (Ljava/lang/String;Ljava/text/ParsePosition;)Ljava/util/Date;
    //   44: astore_2
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_2
    //   48: areturn
    //   49: astore_2
    //   50: new com/google/gson/JsonSyntaxException
    //   53: dup
    //   54: aload_1
    //   55: aload_2
    //   56: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   59: athrow
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    //   64: astore_2
    //   65: goto -> 19
    //   68: astore_2
    //   69: goto -> 32
    // Exception table:
    //   from	to	target	type
    //   2	11	64	java/text/ParseException
    //   2	11	15	finally
    //   19	28	68	java/text/ParseException
    //   19	28	15	finally
    //   32	45	49	java/text/ParseException
    //   32	45	15	finally
    //   50	60	15	finally
  }
  
  public Date read(JsonReader paramJsonReader) throws IOException {
    if (paramJsonReader.peek() == JsonToken.NULL) {
      paramJsonReader.nextNull();
      return null;
    } 
    return deserializeToDate(paramJsonReader.nextString());
  }
  
  public void write(JsonWriter paramJsonWriter, Date paramDate) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: ifnonnull -> 14
    //   6: aload_1
    //   7: invokevirtual nullValue : ()Lcom/google/gson/stream/JsonWriter;
    //   10: pop
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_1
    //   15: aload_0
    //   16: getfield enUsFormat : Ljava/text/DateFormat;
    //   19: aload_2
    //   20: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   23: invokevirtual value : (Ljava/lang/String;)Lcom/google/gson/stream/JsonWriter;
    //   26: pop
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   6	11	30	finally
    //   14	27	30	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\internal\bind\DateTypeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
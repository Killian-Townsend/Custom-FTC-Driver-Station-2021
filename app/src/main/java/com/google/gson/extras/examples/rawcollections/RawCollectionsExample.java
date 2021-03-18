package com.google.gson.extras.examples.rawcollections;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.PrintStream;
import java.util.ArrayList;

public class RawCollectionsExample {
  public static void main(String[] paramArrayOfString) {
    Gson gson = new Gson();
    ArrayList<String> arrayList = new ArrayList();
    arrayList.add("hello");
    arrayList.add(Integer.valueOf(5));
    arrayList.add(new Event("GREETINGS", "guest"));
    String str = gson.toJson(arrayList);
    PrintStream printStream = System.out;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Using Gson.toJson() on a raw collection: ");
    stringBuilder.append(str);
    printStream.println(stringBuilder.toString());
    JsonArray jsonArray = (new JsonParser()).parse(str).getAsJsonArray();
    str = (String)gson.fromJson(jsonArray.get(0), String.class);
    int i = ((Integer)gson.fromJson(jsonArray.get(1), int.class)).intValue();
    Event event = (Event)gson.fromJson(jsonArray.get(2), Event.class);
    System.out.printf("Using Gson.fromJson() to get: %s, %d, %s", new Object[] { str, Integer.valueOf(i), event });
  }
  
  static class Event {
    private String name;
    
    private String source;
    
    private Event(String param1String1, String param1String2) {
      this.name = param1String1;
      this.source = param1String2;
    }
    
    public String toString() {
      return String.format("(name=%s, source=%s)", new Object[] { this.name, this.source });
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\extras\examples\rawcollections\RawCollectionsExample.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
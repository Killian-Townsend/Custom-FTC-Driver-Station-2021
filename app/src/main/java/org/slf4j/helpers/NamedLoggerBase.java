package org.slf4j.helpers;

import java.io.ObjectStreamException;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class NamedLoggerBase implements Logger, Serializable {
  private static final long serialVersionUID = 7535258609338176893L;
  
  protected String name;
  
  public String getName() {
    return this.name;
  }
  
  protected Object readResolve() throws ObjectStreamException {
    return LoggerFactory.getLogger(getName());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\NamedLoggerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
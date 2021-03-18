package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

public class FT_EEPROM_2232H extends FT_EEPROM {
  public byte AH_DriveCurrent = 0;
  
  public boolean AH_SchmittInput = false;
  
  public boolean AH_SlowSlew = false;
  
  public byte AL_DriveCurrent = 0;
  
  public boolean AL_SchmittInput = false;
  
  public boolean AL_SlowSlew = false;
  
  public boolean A_FIFO = false;
  
  public boolean A_FIFOTarget = false;
  
  public boolean A_FastSerial = false;
  
  public boolean A_LoadD2XX = false;
  
  public boolean A_LoadVCP = false;
  
  public boolean A_UART = false;
  
  public byte BH_DriveCurrent = 0;
  
  public boolean BH_SchmittInput = false;
  
  public boolean BH_SlowSlew = false;
  
  public byte BL_DriveCurrent = 0;
  
  public boolean BL_SchmittInput = false;
  
  public boolean BL_SlowSlew = false;
  
  public boolean B_FIFO = false;
  
  public boolean B_FIFOTarget = false;
  
  public boolean B_FastSerial = false;
  
  public boolean B_LoadD2XX = false;
  
  public boolean B_LoadVCP = false;
  
  public boolean B_UART = false;
  
  public boolean PowerSaveEnable = false;
  
  public int TPRDRV = 0;
  
  public static final class DRIVE_STRENGTH {
    static final byte DRIVE_12mA = 2;
    
    static final byte DRIVE_16mA = 3;
    
    static final byte DRIVE_4mA = 0;
    
    static final byte DRIVE_8mA = 1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EEPROM_2232H.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
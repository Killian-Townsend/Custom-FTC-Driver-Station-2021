package org.firstinspires.ftc.robotcore.internal.ftdi;

public class FtConstants {
  public static final byte BI = 16;
  
  public static final byte BITMODE_ASYNC_BITBANG = 1;
  
  public static final byte BITMODE_CBUS_BITBANG = 32;
  
  public static final byte BITMODE_FAST_SERIAL = 16;
  
  public static final byte BITMODE_MCU_HOST = 8;
  
  public static final byte BITMODE_MPSSE = 2;
  
  public static final byte BITMODE_RESET = 0;
  
  public static final byte BITMODE_SYNC_BITBANG = 4;
  
  public static final byte BITMODE_SYNC_FIFO = 64;
  
  public static final int CLOCK_RATE = 3000000;
  
  public static final int CLOCK_RATE_HI = 12000000;
  
  public static final byte CTS = 16;
  
  public static final byte DATA_BITS_7 = 7;
  
  public static final byte DATA_BITS_8 = 8;
  
  public static final byte DCD = -128;
  
  public static final int DEVICE_2232 = 4;
  
  public static final int DEVICE_2232H = 6;
  
  public static final int DEVICE_232B = 0;
  
  public static final int DEVICE_232H = 8;
  
  public static final int DEVICE_232R = 5;
  
  public static final int DEVICE_245R = 5;
  
  public static final int DEVICE_4222_0 = 10;
  
  public static final int DEVICE_4222_1_2 = 11;
  
  public static final int DEVICE_4222_3 = 12;
  
  public static final int DEVICE_4232H = 7;
  
  public static final int DEVICE_8U232AM = 1;
  
  public static final int DEVICE_UNKNOWN = 3;
  
  public static final int DEVICE_X_SERIES = 9;
  
  public static final byte DSR = 32;
  
  public static final byte EVENT_LINE_STATUS = 4;
  
  public static final byte EVENT_MODEM_STATUS = 2;
  
  public static final byte EVENT_REMOVED = 8;
  
  public static final byte EVENT_RXCHAR = 1;
  
  public static final byte FE = 8;
  
  public static final byte FLAGS_HI_SPEED = 2;
  
  public static final byte FLAGS_OPENED = 1;
  
  public static final short FLOW_DTR_DSR = 512;
  
  public static final short FLOW_NONE = 0;
  
  public static final short FLOW_RTS_CTS = 256;
  
  public static final short FLOW_XON_XOFF = 1024;
  
  public static final int FTDI_BREAK_OFF = 0;
  
  public static final int FTDI_BREAK_ON = 16384;
  
  public static final int FTDI_PIT_DEFAULT = 0;
  
  public static final int FTDI_PIT_PARALLEL = 3;
  
  public static final int FTDI_PIT_SIOA = 1;
  
  public static final int FTDI_PIT_SIOB = 2;
  
  public static final int FTDI_SIO_CTS_MASK = 16;
  
  public static final int FTDI_SIO_DISABLE_FLOW_CTRL = 0;
  
  public static final int FTDI_SIO_DSR_MASK = 32;
  
  public static final int FTDI_SIO_DTR_DSR_HS = 2;
  
  public static final int FTDI_SIO_ERASE_EEPROM = 146;
  
  public static final int FTDI_SIO_GET_BITMODE = 12;
  
  public static final int FTDI_SIO_GET_LATENCY = 10;
  
  public static final int FTDI_SIO_GET_STATUS = 5;
  
  public static final int FTDI_SIO_MODEM_CTRL = 1;
  
  public static final int FTDI_SIO_READ_EEPROM = 144;
  
  public static final int FTDI_SIO_RESET = 0;
  
  public static final int FTDI_SIO_RESET_PURGE_RX = 1;
  
  public static final int FTDI_SIO_RESET_PURGE_TX = 2;
  
  public static final int FTDI_SIO_RESET_SIO = 0;
  
  public static final int FTDI_SIO_RI_MASK = 64;
  
  public static final int FTDI_SIO_RLSD_MASK = 128;
  
  public static final int FTDI_SIO_RTS_CTS_HS = 1;
  
  public static final int FTDI_SIO_SET_BAUDRATE = 3;
  
  public static final int FTDI_SIO_SET_BITMODE = 11;
  
  public static final int FTDI_SIO_SET_BREAK = 16384;
  
  public static final int FTDI_SIO_SET_DATA = 4;
  
  public static final int FTDI_SIO_SET_DATA_PARITY_EVEN = 512;
  
  public static final int FTDI_SIO_SET_DATA_PARITY_MARK = 768;
  
  public static final int FTDI_SIO_SET_DATA_PARITY_NONE = 0;
  
  public static final int FTDI_SIO_SET_DATA_PARITY_ODD = 256;
  
  public static final int FTDI_SIO_SET_DATA_PARITY_SPACE = 1024;
  
  public static final int FTDI_SIO_SET_DATA_STOP_BITS_1 = 0;
  
  public static final int FTDI_SIO_SET_DATA_STOP_BITS_15 = 2048;
  
  public static final int FTDI_SIO_SET_DATA_STOP_BITS_2 = 4096;
  
  public static final int FTDI_SIO_SET_DTR_HIGH = 257;
  
  public static final int FTDI_SIO_SET_DTR_LOW = 256;
  
  public static final int FTDI_SIO_SET_DTR_MASK = 1;
  
  public static final int FTDI_SIO_SET_ERROR_CHAR = 7;
  
  public static final int FTDI_SIO_SET_EVENT_CHAR = 6;
  
  public static final int FTDI_SIO_SET_FLOW_CTRL = 2;
  
  public static final int FTDI_SIO_SET_LATENCY = 9;
  
  public static final int FTDI_SIO_SET_RTS_HIGH = 514;
  
  public static final int FTDI_SIO_SET_RTS_LOW = 512;
  
  public static final int FTDI_SIO_SET_RTS_MASK = 2;
  
  public static final int FTDI_SIO_WRITE_EEPROM = 145;
  
  public static final int FTDI_SIO_XON_XOFF_HS = 4;
  
  public static final byte MODEM_STATUS_SIZE = 2;
  
  public static final byte OE = 2;
  
  public static final byte PACKET_SIZE = 64;
  
  public static final int PACKET_SIZE_HI = 512;
  
  public static final byte PARITY_EVEN = 2;
  
  public static final byte PARITY_MARK = 3;
  
  public static final byte PARITY_NONE = 0;
  
  public static final byte PARITY_ODD = 1;
  
  public static final byte PARITY_SPACE = 4;
  
  public static final byte PE = 4;
  
  public static final byte PURGE_RX = 1;
  
  public static final byte PURGE_TX = 2;
  
  public static final byte RI = 64;
  
  public static final byte STOP_BITS_1 = 0;
  
  public static final byte STOP_BITS_2 = 2;
  
  public static final int SUB_INT_0_0 = 0;
  
  public static final int SUB_INT_0_125 = 49152;
  
  public static final int SUB_INT_0_25 = 32768;
  
  public static final int SUB_INT_0_375 = 0;
  
  public static final int SUB_INT_0_5 = 16384;
  
  public static final int SUB_INT_0_625 = 16384;
  
  public static final int SUB_INT_0_75 = 32768;
  
  public static final int SUB_INT_0_875 = 49152;
  
  public static final int SUB_INT_MASK = 49152;
  
  public static final int UFTDI_JTAG_CHECK_STRING = 255;
  
  public static final int UFTDI_JTAG_IFACES_MAX = 8;
  
  public static final int UFTDI_JTAG_MASK = 255;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\FtConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
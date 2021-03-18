package org.firstinspires.ftc.robotcore.internal.usb;

import android.text.TextUtils;
import com.qualcomm.robotcore.R;
import java.util.Arrays;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class UsbConstants {
  public static final int PRODUCT_ID_LOGITECH_C270 = 2085;
  
  public static final int PRODUCT_ID_LOGITECH_C310 = 2075;
  
  public static final int PRODUCT_ID_LOGITECH_C920 = 2093;
  
  public static final int PRODUCT_ID_LOGITECH_F310 = 49693;
  
  public static final int PRODUCT_ID_MICROSOFT_LIFECAM_HD_3000 = 2064;
  
  public static final int PRODUCT_ID_MICROSOFT_XBOX360_WIRED = 654;
  
  public static final int PRODUCT_ID_SONY_GAMEPAD_PS4 = 2508;
  
  public static final int USB_CLASS_APP_SPEC = 254;
  
  public static final int USB_CLASS_AUDIO = 1;
  
  public static final int USB_CLASS_CDC_DATA = 10;
  
  public static final int USB_CLASS_COMM = 2;
  
  public static final int USB_CLASS_CONTENT_SEC = 13;
  
  public static final int USB_CLASS_CSCID = 11;
  
  public static final int USB_CLASS_HID = 3;
  
  public static final int USB_CLASS_HUB = 9;
  
  public static final int USB_CLASS_MASS_STORAGE = 8;
  
  public static final int USB_CLASS_MISC = 239;
  
  public static final int USB_CLASS_PER_INTERFACE = 0;
  
  public static final int USB_CLASS_PHYSICA = 5;
  
  public static final int USB_CLASS_PRINTER = 7;
  
  public static final int USB_CLASS_STILL_IMAGE = 6;
  
  public static final int USB_CLASS_VENDOR_SPEC = 255;
  
  public static final int USB_CLASS_VIDEO = 14;
  
  public static final int USB_CLASS_WIRELESS_CONTROLLER = 224;
  
  public static final int USB_DIR_IN = 128;
  
  public static final int USB_DIR_OUT = 0;
  
  public static final int USB_ENDPOINT_DIR_MASK = 128;
  
  public static final int USB_ENDPOINT_NUMBER_MASK = 15;
  
  public static final int USB_ENDPOINT_XFERTYPE_MASK = 3;
  
  public static final int USB_ENDPOINT_XFER_BULK = 2;
  
  public static final int USB_ENDPOINT_XFER_CONTROL = 0;
  
  public static final int USB_ENDPOINT_XFER_INT = 3;
  
  public static final int USB_ENDPOINT_XFER_ISOC = 1;
  
  public static final int USB_INTERFACE_SUBCLASS_BOOT = 1;
  
  public static final int USB_SUBCLASS_VENDOR_SPEC = 255;
  
  public static final int USB_TYPE_CLASS = 32;
  
  public static final int USB_TYPE_MASK = 96;
  
  public static final int USB_TYPE_RESERVED = 96;
  
  public static final int USB_TYPE_STANDARD = 0;
  
  public static final int USB_TYPE_VENDOR = 64;
  
  public static final int USB_VIDEO_CLASS_DESCRIPTOR_CONFIGURATION = 34;
  
  public static final int USB_VIDEO_CLASS_DESCRIPTOR_DEVICE = 33;
  
  public static final int USB_VIDEO_CLASS_DESCRIPTOR_ENDPOINT = 37;
  
  public static final int USB_VIDEO_CLASS_DESCRIPTOR_INTERFACE = 36;
  
  public static final int USB_VIDEO_CLASS_DESCRIPTOR_STRING = 35;
  
  public static final int USB_VIDEO_CLASS_DESCRIPTOR_UNDEFINED = 32;
  
  public static final int USB_VIDEO_INTERFACE_PROTOCOL_15 = 1;
  
  public static final int USB_VIDEO_INTERFACE_PROTOCOL_UNDEFINED = 0;
  
  public static final int USB_VIDEO_INTERFACE_SUBCLASS_CONTROL = 1;
  
  public static final int USB_VIDEO_INTERFACE_SUBCLASS_INTERFACE_COLLECTION = 3;
  
  public static final int USB_VIDEO_INTERFACE_SUBCLASS_STREAMING = 2;
  
  public static final int USB_VIDEO_INTERFACE_SUBCLASS_UNDEFINED = 0;
  
  public static final int VENDOR_ID_ACER = 1282;
  
  public static final int VENDOR_ID_ANYDATA = 5845;
  
  public static final int VENDOR_ID_ARCHOS = 3705;
  
  public static final int VENDOR_ID_ASUS = 2821;
  
  public static final int VENDOR_ID_BYD = 6609;
  
  public static final int VENDOR_ID_COMPAL = 4633;
  
  public static final int VENDOR_ID_DELL = 16700;
  
  public static final int VENDOR_ID_ECS = 1020;
  
  public static final int VENDOR_ID_FOXCONN = 1161;
  
  public static final int VENDOR_ID_FTDI = 1027;
  
  public static final int VENDOR_ID_FUJITSU = 1221;
  
  public static final int VENDOR_ID_FUNAI = 3868;
  
  public static final int VENDOR_ID_GARMIN_ASUS = 2334;
  
  public static final int VENDOR_ID_GENERIC = 6408;
  
  public static final int VENDOR_ID_GIGABYTE = 1044;
  
  public static final int VENDOR_ID_GOOGLE = 6353;
  
  public static final int VENDOR_ID_HAIER = 8222;
  
  public static final int VENDOR_ID_HARRIS = 6565;
  
  public static final int VENDOR_ID_HISENSE = 4251;
  
  public static final int VENDOR_ID_HTC = 2996;
  
  public static final int VENDOR_ID_HUAWEI = 4817;
  
  public static final int VENDOR_ID_INQ_MOBILE = 8980;
  
  public static final int VENDOR_ID_INTEL = 32903;
  
  public static final int VENDOR_ID_IRIVER = 9248;
  
  public static final int VENDOR_ID_KOBO = 8759;
  
  public static final int VENDOR_ID_KT_TECH = 8470;
  
  public static final int VENDOR_ID_KYOCERA = 1154;
  
  public static final int VENDOR_ID_K_TOUCH = 9443;
  
  public static final int VENDOR_ID_LAB126 = 6473;
  
  public static final int VENDOR_ID_LENOVO = 6127;
  
  public static final int VENDOR_ID_LENOVOMOBILE = 8198;
  
  public static final int VENDOR_ID_LGE = 4100;
  
  public static final int VENDOR_ID_LOGITECH = 1133;
  
  public static final int VENDOR_ID_LUMIGON = 9699;
  
  public static final int VENDOR_ID_MICROSOFT = 1118;
  
  public static final int VENDOR_ID_MOTOROLA = 8888;
  
  public static final int VENDOR_ID_MTK = 3725;
  
  public static final int VENDOR_ID_NEC = 1033;
  
  public static final int VENDOR_ID_NOOK = 8320;
  
  public static final int VENDOR_ID_NVIDIA = 2389;
  
  public static final int VENDOR_ID_OPPO = 8921;
  
  public static final int VENDOR_ID_OTGV = 8791;
  
  public static final int VENDOR_ID_OUYA = 10294;
  
  public static final int VENDOR_ID_PANTECH = 4265;
  
  public static final int VENDOR_ID_PEGATRON = 7501;
  
  public static final int VENDOR_ID_PHILIPS = 1137;
  
  public static final int VENDOR_ID_PMC = 1242;
  
  public static final int VENDOR_ID_POSITIVO = 5730;
  
  public static final int VENDOR_ID_QISDA = 7493;
  
  public static final int VENDOR_ID_QUALCOMM = 1478;
  
  public static final int VENDOR_ID_QUANTA = 1032;
  
  public static final int VENDOR_ID_SAMSUNG = 1256;
  
  public static final int VENDOR_ID_SHARP = 1245;
  
  public static final int VENDOR_ID_SK_TELESYS = 8019;
  
  public static final int VENDOR_ID_SONY = 1356;
  
  public static final int VENDOR_ID_SONY_ERICSSON = 4046;
  
  public static final int VENDOR_ID_TELEEPOCH = 9024;
  
  public static final int VENDOR_ID_TI = 1105;
  
  public static final int VENDOR_ID_TOSHIBA = 2352;
  
  public static final int VENDOR_ID_T_AND_A = 7099;
  
  public static final int VENDOR_ID_VIZIO = 57408;
  
  public static final int VENDOR_ID_XIAOMI = 10007;
  
  public static final int VENDOR_ID_YULONG_COOLPAD = 7871;
  
  public static final int VENDOR_ID_ZTE = 6610;
  
  public static final List<String> manufacturerNamesToIgnore = Arrays.asList(new String[] { "generic" });
  
  public static String getManufacturerName(int paramInt) {
    paramInt = getManufacturerResourceId(paramInt);
    return (paramInt != 0) ? AppUtil.getDefContext().getString(paramInt) : null;
  }
  
  public static String getManufacturerName(String paramString, int paramInt) {
    if (TextUtils.isEmpty(paramString) || manufacturerNamesToIgnore.contains(paramString.toLowerCase())) {
      String str = getManufacturerName(paramInt);
      if (str != null)
        return str; 
    } 
    return paramString;
  }
  
  protected static int getManufacturerResourceId(int paramInt) {
    return (paramInt != 1027) ? ((paramInt != 1118) ? ((paramInt != 1133) ? ((paramInt != 1478) ? ((paramInt != 6353) ? ((paramInt != 6408) ? ((paramInt != 16700) ? 0 : R.string.usb_vid_name_ftdi) : R.string.usb_vid_name_generic) : R.string.usb_vid_name_google) : R.string.usb_vid_name_qualcomm) : R.string.usb_vid_name_logitech) : R.string.usb_vid_name_microsoft) : R.string.usb_vid_name_ftdi;
  }
  
  public static String getProductName(int paramInt1, int paramInt2) {
    paramInt1 = getProductNameResourceId(paramInt1, paramInt2);
    return (paramInt1 != 0) ? AppUtil.getDefContext().getString(paramInt1) : null;
  }
  
  public static String getProductName(String paramString, int paramInt1, int paramInt2) {
    if (TextUtils.isEmpty(paramString)) {
      String str = getProductName(paramInt1, paramInt2);
      if (str != null)
        return str; 
    } 
    return paramString;
  }
  
  protected static int getProductNameResourceId(int paramInt1, int paramInt2) {
    return (paramInt1 == 1133) ? ((paramInt2 != 2075) ? ((paramInt2 == 2093) ? R.string.usb_pid_name_logitech_c920 : 0) : R.string.usb_pid_name_logitech_c310) : 0;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\UsbConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
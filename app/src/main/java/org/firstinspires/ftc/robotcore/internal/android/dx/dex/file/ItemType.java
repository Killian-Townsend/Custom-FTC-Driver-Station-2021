package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public enum ItemType implements ToHuman {
  TYPE_ANNOTATIONS_DIRECTORY_ITEM,
  TYPE_ANNOTATION_ITEM,
  TYPE_ANNOTATION_SET_ITEM,
  TYPE_ANNOTATION_SET_REF_ITEM,
  TYPE_ANNOTATION_SET_REF_LIST,
  TYPE_CLASS_DATA_ITEM,
  TYPE_CLASS_DEF_ITEM,
  TYPE_CODE_ITEM,
  TYPE_DEBUG_INFO_ITEM,
  TYPE_ENCODED_ARRAY_ITEM,
  TYPE_EXCEPTION_HANDLER_ITEM,
  TYPE_FIELD_ID_ITEM,
  TYPE_HEADER_ITEM(0, "header_item"),
  TYPE_MAP_ITEM(0, "header_item"),
  TYPE_MAP_LIST(0, "header_item"),
  TYPE_METHOD_ID_ITEM(0, "header_item"),
  TYPE_PROTO_ID_ITEM(0, "header_item"),
  TYPE_STRING_DATA_ITEM(0, "header_item"),
  TYPE_STRING_ID_ITEM(1, "string_id_item"),
  TYPE_TYPE_ID_ITEM(2, "type_id_item"),
  TYPE_TYPE_ITEM(2, "type_id_item"),
  TYPE_TYPE_LIST(2, "type_id_item");
  
  private final String humanName;
  
  private final int mapValue;
  
  private final String typeName;
  
  static {
    TYPE_PROTO_ID_ITEM = new ItemType("TYPE_PROTO_ID_ITEM", 3, 3, "proto_id_item");
    TYPE_FIELD_ID_ITEM = new ItemType("TYPE_FIELD_ID_ITEM", 4, 4, "field_id_item");
    TYPE_METHOD_ID_ITEM = new ItemType("TYPE_METHOD_ID_ITEM", 5, 5, "method_id_item");
    TYPE_CLASS_DEF_ITEM = new ItemType("TYPE_CLASS_DEF_ITEM", 6, 6, "class_def_item");
    TYPE_MAP_LIST = new ItemType("TYPE_MAP_LIST", 7, 4096, "map_list");
    TYPE_TYPE_LIST = new ItemType("TYPE_TYPE_LIST", 8, 4097, "type_list");
    TYPE_ANNOTATION_SET_REF_LIST = new ItemType("TYPE_ANNOTATION_SET_REF_LIST", 9, 4098, "annotation_set_ref_list");
    TYPE_ANNOTATION_SET_ITEM = new ItemType("TYPE_ANNOTATION_SET_ITEM", 10, 4099, "annotation_set_item");
    TYPE_CLASS_DATA_ITEM = new ItemType("TYPE_CLASS_DATA_ITEM", 11, 8192, "class_data_item");
    TYPE_CODE_ITEM = new ItemType("TYPE_CODE_ITEM", 12, 8193, "code_item");
    TYPE_STRING_DATA_ITEM = new ItemType("TYPE_STRING_DATA_ITEM", 13, 8194, "string_data_item");
    TYPE_DEBUG_INFO_ITEM = new ItemType("TYPE_DEBUG_INFO_ITEM", 14, 8195, "debug_info_item");
    TYPE_ANNOTATION_ITEM = new ItemType("TYPE_ANNOTATION_ITEM", 15, 8196, "annotation_item");
    TYPE_ENCODED_ARRAY_ITEM = new ItemType("TYPE_ENCODED_ARRAY_ITEM", 16, 8197, "encoded_array_item");
    TYPE_ANNOTATIONS_DIRECTORY_ITEM = new ItemType("TYPE_ANNOTATIONS_DIRECTORY_ITEM", 17, 8198, "annotations_directory_item");
    TYPE_MAP_ITEM = new ItemType("TYPE_MAP_ITEM", 18, -1, "map_item");
    TYPE_TYPE_ITEM = new ItemType("TYPE_TYPE_ITEM", 19, -1, "type_item");
    TYPE_EXCEPTION_HANDLER_ITEM = new ItemType("TYPE_EXCEPTION_HANDLER_ITEM", 20, -1, "exception_handler_item");
    ItemType itemType = new ItemType("TYPE_ANNOTATION_SET_REF_ITEM", 21, -1, "annotation_set_ref_item");
    TYPE_ANNOTATION_SET_REF_ITEM = itemType;
    $VALUES = new ItemType[] { 
        TYPE_HEADER_ITEM, TYPE_STRING_ID_ITEM, TYPE_TYPE_ID_ITEM, TYPE_PROTO_ID_ITEM, TYPE_FIELD_ID_ITEM, TYPE_METHOD_ID_ITEM, TYPE_CLASS_DEF_ITEM, TYPE_MAP_LIST, TYPE_TYPE_LIST, TYPE_ANNOTATION_SET_REF_LIST, 
        TYPE_ANNOTATION_SET_ITEM, TYPE_CLASS_DATA_ITEM, TYPE_CODE_ITEM, TYPE_STRING_DATA_ITEM, TYPE_DEBUG_INFO_ITEM, TYPE_ANNOTATION_ITEM, TYPE_ENCODED_ARRAY_ITEM, TYPE_ANNOTATIONS_DIRECTORY_ITEM, TYPE_MAP_ITEM, TYPE_TYPE_ITEM, 
        TYPE_EXCEPTION_HANDLER_ITEM, itemType };
  }
  
  ItemType(int paramInt1, String paramString1) {
    this.mapValue = paramInt1;
    this.typeName = paramString1;
    this$enum$name = paramString1;
    if (paramString1.endsWith("_item"))
      this$enum$name = paramString1.substring(0, paramString1.length() - 5); 
    this.humanName = this$enum$name.replace('_', ' ');
  }
  
  public int getMapValue() {
    return this.mapValue;
  }
  
  public String getTypeName() {
    return this.typeName;
  }
  
  public String toHuman() {
    return this.humanName;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\ItemType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.google.blocks.ftcrobotcontroller.runtime;

enum BlockType {
  CREATE, EVENT, FUNCTION, GETTER, SETTER, SPECIAL;
  
  static {
    EVENT = new BlockType("EVENT", 1);
    CREATE = new BlockType("CREATE", 2);
    SETTER = new BlockType("SETTER", 3);
    GETTER = new BlockType("GETTER", 4);
    BlockType blockType = new BlockType("FUNCTION", 5);
    FUNCTION = blockType;
    $VALUES = new BlockType[] { SPECIAL, EVENT, CREATE, SETTER, GETTER, blockType };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\BlockType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.opengl.models;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class SavedMeshObject extends MeshObject {
  private ByteBuffer norms;
  
  int numVerts = 0;
  
  private ByteBuffer textCoords;
  
  private ByteBuffer verts;
  
  public Buffer getBuffer(MeshObject.BUFFER_TYPE paramBUFFER_TYPE) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$opengl$models$MeshObject$BUFFER_TYPE[paramBUFFER_TYPE.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? null : this.norms) : this.textCoords) : this.verts;
  }
  
  public int getNumObjectIndex() {
    return 0;
  }
  
  public int getNumObjectVertex() {
    return this.numVerts;
  }
  
  public void loadModel(AssetManager paramAssetManager, String paramString) throws IOException {
    try {
      InputStream inputStream = paramAssetManager.open(paramString);
    } finally {
      paramString = null;
    } 
    if (paramAssetManager != null)
      paramAssetManager.close(); 
    throw paramString;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\SavedMeshObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.opengl.GLES20;
import com.qualcomm.robotcore.R;
import com.vuforia.Mesh;
import org.firstinspires.ftc.robotcore.internal.opengl.models.MeshObject;

public class CubeMeshVertexShader extends SimpleVertexShader {
  public static final int resourceId = R.raw.cube_mesh_vertex_shader;
  
  protected final int a_vertexNormal;
  
  protected final int a_vertexTexCoord;
  
  public CubeMeshVertexShader(int paramInt) {
    super(paramInt);
    this.a_vertexNormal = GLES20.glGetAttribLocation(paramInt, "vertexNormal");
    this.a_vertexTexCoord = GLES20.glGetAttribLocation(paramInt, "vertexTexCoord");
  }
  
  public void disableAttributes() {
    super.disableAttributes();
    GLES20.glDisableVertexAttribArray(this.a_vertexNormal);
    GLES20.glDisableVertexAttribArray(this.a_vertexTexCoord);
  }
  
  public void setCoordinates(Mesh paramMesh) {
    GLES20.glVertexAttribPointer(this.a_vertexPosition, 3, 5126, false, 0, paramMesh.getPositions());
    GLES20.glVertexAttribPointer(this.a_vertexTexCoord, 2, 5126, false, 0, paramMesh.getUVs());
    GLES20.glEnableVertexAttribArray(this.a_vertexPosition);
    GLES20.glEnableVertexAttribArray(this.a_vertexTexCoord);
  }
  
  public void setCoordinates(MeshObject paramMeshObject) {
    GLES20.glVertexAttribPointer(this.a_vertexPosition, 3, 5126, false, 0, paramMeshObject.getVertices());
    GLES20.glVertexAttribPointer(this.a_vertexNormal, 3, 5126, false, 0, paramMeshObject.getNormals());
    GLES20.glVertexAttribPointer(this.a_vertexTexCoord, 2, 5126, false, 0, paramMeshObject.getTexCoords());
    GLES20.glEnableVertexAttribArray(this.a_vertexPosition);
    GLES20.glEnableVertexAttribArray(this.a_vertexNormal);
    GLES20.glEnableVertexAttribArray(this.a_vertexTexCoord);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\CubeMeshVertexShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
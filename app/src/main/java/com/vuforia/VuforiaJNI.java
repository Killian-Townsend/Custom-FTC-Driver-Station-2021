package com.vuforia;

import java.math.BigInteger;
import java.nio.ByteBuffer;

class VuforiaJNI {
  static {
    swig_module_init();
  }
  
  public static final native long AnchorResult_SWIGUpcast(long paramLong);
  
  public static final native long AnchorResult_getClassType();
  
  public static final native long AnchorResult_getTrackable(long paramLong, AnchorResult paramAnchorResult);
  
  public static final native long Anchor_SWIGUpcast(long paramLong);
  
  public static final native long Anchor_getClassType();
  
  public static final native int Area_getType(long paramLong, Area paramArea);
  
  public static final native long Box3D_getMaximumPosition(long paramLong, Box3D paramBox3D);
  
  public static final native long Box3D_getMinimumPosition(long paramLong, Box3D paramBox3D);
  
  public static final native long CameraCalibration_getDistortionParameters(long paramLong, CameraCalibration paramCameraCalibration);
  
  public static final native long CameraCalibration_getFieldOfViewRads(long paramLong, CameraCalibration paramCameraCalibration);
  
  public static final native long CameraCalibration_getFocalLength(long paramLong, CameraCalibration paramCameraCalibration);
  
  public static final native long CameraCalibration_getPrincipalPoint(long paramLong, CameraCalibration paramCameraCalibration);
  
  public static final native long CameraCalibration_getSize(long paramLong, CameraCalibration paramCameraCalibration);
  
  public static final native boolean CameraDevice_deinit(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native long CameraDevice_getCameraCalibration(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native int CameraDevice_getCameraDirection(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native boolean CameraDevice_getCameraField(long paramLong1, CameraDevice paramCameraDevice, int paramInt, long paramLong2, CameraField paramCameraField);
  
  public static final native boolean CameraDevice_getFieldBool(long paramLong, CameraDevice paramCameraDevice, String paramString, boolean[] paramArrayOfboolean);
  
  public static final native boolean CameraDevice_getFieldFloat(long paramLong, CameraDevice paramCameraDevice, String paramString, float[] paramArrayOffloat);
  
  public static final native boolean CameraDevice_getFieldInt64(long paramLong, CameraDevice paramCameraDevice, String paramString, long[] paramArrayOflong);
  
  public static final native boolean CameraDevice_getFieldInt64Range(long paramLong, CameraDevice paramCameraDevice, String paramString, long[] paramArrayOflong);
  
  public static final native String CameraDevice_getFieldString(long paramLong, CameraDevice paramCameraDevice, String paramString);
  
  public static final native long CameraDevice_getInstance();
  
  public static final native int CameraDevice_getNumFields(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native int CameraDevice_getNumVideoModes(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native long CameraDevice_getVideoMode(long paramLong, CameraDevice paramCameraDevice, int paramInt);
  
  public static final native boolean CameraDevice_init__SWIG_0(long paramLong, CameraDevice paramCameraDevice, int paramInt);
  
  public static final native boolean CameraDevice_init__SWIG_1(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native boolean CameraDevice_selectVideoMode(long paramLong, CameraDevice paramCameraDevice, int paramInt);
  
  public static final native boolean CameraDevice_setField__SWIG_0(long paramLong, CameraDevice paramCameraDevice, String paramString1, String paramString2);
  
  public static final native boolean CameraDevice_setField__SWIG_1(long paramLong1, CameraDevice paramCameraDevice, String paramString, long paramLong2);
  
  public static final native boolean CameraDevice_setField__SWIG_2(long paramLong, CameraDevice paramCameraDevice, String paramString, float paramFloat);
  
  public static final native boolean CameraDevice_setField__SWIG_3(long paramLong, CameraDevice paramCameraDevice, String paramString, boolean paramBoolean);
  
  public static final native boolean CameraDevice_setField__SWIG_4(long paramLong, CameraDevice paramCameraDevice, String paramString, long[] paramArrayOflong);
  
  public static final native boolean CameraDevice_setFlashTorchMode(long paramLong, CameraDevice paramCameraDevice, boolean paramBoolean);
  
  public static final native boolean CameraDevice_setFocusMode(long paramLong, CameraDevice paramCameraDevice, int paramInt);
  
  public static final native boolean CameraDevice_start(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native boolean CameraDevice_stop(long paramLong, CameraDevice paramCameraDevice);
  
  public static final native String CameraField_Key_get(long paramLong, CameraField paramCameraField);
  
  public static final native int CameraField_Type_get(long paramLong, CameraField paramCameraField);
  
  public static final native long CustomViewerParameters_SWIGUpcast(long paramLong);
  
  public static final native void CustomViewerParameters_addDistortionCoefficient(long paramLong, CustomViewerParameters paramCustomViewerParameters, float paramFloat);
  
  public static final native void CustomViewerParameters_clearDistortionCoefficients(long paramLong, CustomViewerParameters paramCustomViewerParameters);
  
  public static final native void CustomViewerParameters_setButtonType(long paramLong, CustomViewerParameters paramCustomViewerParameters, int paramInt);
  
  public static final native void CustomViewerParameters_setContainsMagnet(long paramLong, CustomViewerParameters paramCustomViewerParameters, boolean paramBoolean);
  
  public static final native void CustomViewerParameters_setFieldOfView(long paramLong1, CustomViewerParameters paramCustomViewerParameters, long paramLong2, Vec4F paramVec4F);
  
  public static final native void CustomViewerParameters_setInterLensDistance(long paramLong, CustomViewerParameters paramCustomViewerParameters, float paramFloat);
  
  public static final native void CustomViewerParameters_setLensCentreToTrayDistance(long paramLong, CustomViewerParameters paramCustomViewerParameters, float paramFloat);
  
  public static final native void CustomViewerParameters_setScreenToLensDistance(long paramLong, CustomViewerParameters paramCustomViewerParameters, float paramFloat);
  
  public static final native void CustomViewerParameters_setTrayAlignment(long paramLong, CustomViewerParameters paramCustomViewerParameters, int paramInt);
  
  public static final native long CylinderTargetResult_SWIGUpcast(long paramLong);
  
  public static final native long CylinderTargetResult_getClassType();
  
  public static final native long CylinderTargetResult_getTrackable(long paramLong, CylinderTargetResult paramCylinderTargetResult);
  
  public static final native long CylinderTarget_SWIGUpcast(long paramLong);
  
  public static final native float CylinderTarget_getBottomDiameter(long paramLong, CylinderTarget paramCylinderTarget);
  
  public static final native long CylinderTarget_getClassType();
  
  public static final native float CylinderTarget_getSideLength(long paramLong, CylinderTarget paramCylinderTarget);
  
  public static final native float CylinderTarget_getTopDiameter(long paramLong, CylinderTarget paramCylinderTarget);
  
  public static final native boolean CylinderTarget_setBottomDiameter(long paramLong, CylinderTarget paramCylinderTarget, float paramFloat);
  
  public static final native boolean CylinderTarget_setSideLength(long paramLong, CylinderTarget paramCylinderTarget, float paramFloat);
  
  public static final native boolean CylinderTarget_setTopDiameter(long paramLong, CylinderTarget paramCylinderTarget, float paramFloat);
  
  public static final native long DataSet_createMultiTarget(long paramLong, DataSet paramDataSet, String paramString);
  
  public static final native long DataSet_createTrackable(long paramLong1, DataSet paramDataSet, long paramLong2, TrackableSource paramTrackableSource);
  
  public static final native boolean DataSet_destroy(long paramLong1, DataSet paramDataSet, long paramLong2, Trackable paramTrackable);
  
  public static final native boolean DataSet_exists(String paramString, int paramInt);
  
  public static final native int DataSet_getNumTrackables(long paramLong, DataSet paramDataSet);
  
  public static final native long DataSet_getTrackable(long paramLong, DataSet paramDataSet, int paramInt);
  
  public static final native boolean DataSet_hasReachedTrackableLimit(long paramLong, DataSet paramDataSet);
  
  public static final native boolean DataSet_isActive(long paramLong, DataSet paramDataSet);
  
  public static final native boolean DataSet_load(long paramLong, DataSet paramDataSet, String paramString, int paramInt);
  
  public static final native long DeviceTrackableResult_SWIGUpcast(long paramLong);
  
  public static final native long DeviceTrackableResult_getClassType();
  
  public static final native long DeviceTrackableResult_getTrackable(long paramLong, DeviceTrackableResult paramDeviceTrackableResult);
  
  public static final native long DeviceTrackable_SWIGUpcast(long paramLong);
  
  public static final native long DeviceTrackable_getClassType();
  
  public static final native long DeviceTracker_SWIGUpcast(long paramLong);
  
  public static final native long DeviceTracker_getClassType();
  
  public static final native long DeviceTracker_getWorldToDeviceBaseTransform(long paramLong, DeviceTracker paramDeviceTracker);
  
  public static final native boolean DeviceTracker_setWorldToDeviceBaseTransform(long paramLong1, DeviceTracker paramDeviceTracker, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native long Device_getClassType();
  
  public static final native long Device_getInstance();
  
  public static final native int Device_getMode(long paramLong, Device paramDevice);
  
  public static final native long Device_getRenderingPrimitives(long paramLong, Device paramDevice);
  
  public static final native long Device_getSelectedViewer(long paramLong, Device paramDevice);
  
  public static final native long Device_getType(long paramLong, Device paramDevice);
  
  public static final native long Device_getViewerList(long paramLong, Device paramDevice);
  
  public static final native boolean Device_isOfType(long paramLong1, Device paramDevice, long paramLong2, Type paramType);
  
  public static final native boolean Device_isViewerActive(long paramLong, Device paramDevice);
  
  public static final native boolean Device_selectViewer(long paramLong1, Device paramDevice, long paramLong2, ViewerParameters paramViewerParameters);
  
  public static final native void Device_setConfigurationChanged(long paramLong, Device paramDevice);
  
  public static final native boolean Device_setMode(long paramLong, Device paramDevice, int paramInt);
  
  public static final native void Device_setViewerActive(long paramLong, Device paramDevice, boolean paramBoolean);
  
  public static final native boolean EyewearCalibrationProfileManager_clearProfile(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt);
  
  public static final native int EyewearCalibrationProfileManager_getActiveProfile(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager);
  
  public static final native long EyewearCalibrationProfileManager_getCameraToEyePose(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt1, int paramInt2);
  
  public static final native long EyewearCalibrationProfileManager_getEyeProjection(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt1, int paramInt2);
  
  public static final native long EyewearCalibrationProfileManager_getMaxCount(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager);
  
  public static final native short[] EyewearCalibrationProfileManager_getProfileName(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt);
  
  public static final native long EyewearCalibrationProfileManager_getUsedCount(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager);
  
  public static final native boolean EyewearCalibrationProfileManager_isProfileUsed(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt);
  
  public static final native boolean EyewearCalibrationProfileManager_setActiveProfile(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt);
  
  public static final native boolean EyewearCalibrationProfileManager_setCameraToEyePose(long paramLong1, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt1, int paramInt2, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native boolean EyewearCalibrationProfileManager_setEyeProjection(long paramLong1, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt1, int paramInt2, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native boolean EyewearCalibrationProfileManager_setProfileName(long paramLong, EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager, int paramInt, short[] paramArrayOfshort);
  
  public static final native float EyewearCalibrationReading_CenterX_get(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading);
  
  public static final native void EyewearCalibrationReading_CenterX_set(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading, float paramFloat);
  
  public static final native float EyewearCalibrationReading_CenterY_get(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading);
  
  public static final native void EyewearCalibrationReading_CenterY_set(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading, float paramFloat);
  
  public static final native long EyewearCalibrationReading_Pose_get(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading);
  
  public static final native void EyewearCalibrationReading_Pose_set(long paramLong1, EyewearCalibrationReading paramEyewearCalibrationReading, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native float EyewearCalibrationReading_Scale_get(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading);
  
  public static final native void EyewearCalibrationReading_Scale_set(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading, float paramFloat);
  
  public static final native int EyewearCalibrationReading_Type_get(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading);
  
  public static final native void EyewearCalibrationReading_Type_set(long paramLong, EyewearCalibrationReading paramEyewearCalibrationReading, int paramInt);
  
  public static final native long EyewearDevice_SWIGUpcast(long paramLong);
  
  public static final native long EyewearDevice_getCalibrationProfileManager(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native long EyewearDevice_getClassType();
  
  public static final native int EyewearDevice_getScreenOrientation(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native long EyewearDevice_getUserCalibrator(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native boolean EyewearDevice_isDisplayExtended(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native boolean EyewearDevice_isDisplayExtendedGLOnly(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native boolean EyewearDevice_isDualDisplay(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native boolean EyewearDevice_isPredictiveTrackingEnabled(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native boolean EyewearDevice_isSeeThru(long paramLong, EyewearDevice paramEyewearDevice);
  
  public static final native boolean EyewearDevice_setDisplayExtended(long paramLong, EyewearDevice paramEyewearDevice, boolean paramBoolean);
  
  public static final native boolean EyewearDevice_setPredictiveTracking(long paramLong, EyewearDevice paramEyewearDevice, boolean paramBoolean);
  
  public static final native float EyewearUserCalibrator_getDrawingAspectRatio(long paramLong1, EyewearUserCalibrator paramEyewearUserCalibrator, long paramLong2, long paramLong3);
  
  public static final native float EyewearUserCalibrator_getMaxScaleHint(long paramLong, EyewearUserCalibrator paramEyewearUserCalibrator);
  
  public static final native float EyewearUserCalibrator_getMinScaleHint(long paramLong, EyewearUserCalibrator paramEyewearUserCalibrator);
  
  public static final native int EyewearUserCalibrator_getProjectionMatrices(long paramLong1, EyewearUserCalibrator paramEyewearUserCalibrator, long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2, long paramLong2, Matrix34F paramMatrix34F1, long paramLong3, Matrix34F paramMatrix34F2, long paramLong4, Matrix34F paramMatrix34F3, long paramLong5, Matrix34F paramMatrix34F4);
  
  public static final native boolean EyewearUserCalibrator_getProjectionMatrix(long paramLong1, EyewearUserCalibrator paramEyewearUserCalibrator, long[] paramArrayOflong, int paramInt, long paramLong2, Matrix34F paramMatrix34F1, long paramLong3, Matrix34F paramMatrix34F2);
  
  public static final native boolean EyewearUserCalibrator_init(long paramLong1, EyewearUserCalibrator paramEyewearUserCalibrator, long paramLong2, long paramLong3, float paramFloat1, float paramFloat2);
  
  public static final native boolean EyewearUserCalibrator_isStereoStretched(long paramLong, EyewearUserCalibrator paramEyewearUserCalibrator);
  
  public static final native long Frame_getImage(long paramLong, Frame paramFrame, int paramInt);
  
  public static final native int Frame_getIndex(long paramLong, Frame paramFrame);
  
  public static final native long Frame_getNumImages(long paramLong, Frame paramFrame);
  
  public static final native double Frame_getTimeStamp(long paramLong, Frame paramFrame);
  
  public static final native long GLTextureData_SWIGUpcast(long paramLong);
  
  public static final native int GLTextureData_VideoBackgroundTextureID_get(long paramLong, GLTextureData paramGLTextureData);
  
  public static final native void GLTextureData_VideoBackgroundTextureID_set(long paramLong, GLTextureData paramGLTextureData, int paramInt);
  
  public static final native long GLTextureUnit_SWIGUpcast(long paramLong);
  
  public static final native int GLTextureUnit_TextureUnit_get(long paramLong, GLTextureUnit paramGLTextureUnit);
  
  public static final native void GLTextureUnit_TextureUnit_set(long paramLong, GLTextureUnit paramGLTextureUnit, int paramInt);
  
  public static final native long GuideView_getImage(long paramLong, GuideView paramGuideView);
  
  public static final native long GuideView_getIntrinsics(long paramLong, GuideView paramGuideView);
  
  public static final native long GuideView_getPose(long paramLong, GuideView paramGuideView);
  
  public static final native void GuideView_setPose(long paramLong1, GuideView paramGuideView, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native long HandheldTransformModel_SWIGUpcast(long paramLong);
  
  public static final native long HandheldTransformModel_getPivotPoint(long paramLong, HandheldTransformModel paramHandheldTransformModel);
  
  public static final native int HandheldTransformModel_getType(long paramLong, HandheldTransformModel paramHandheldTransformModel);
  
  public static final native boolean HandheldTransformModel_setPivotPoint(long paramLong1, HandheldTransformModel paramHandheldTransformModel, long paramLong2, Vec3F paramVec3F);
  
  public static final native long HeadTransformModel_SWIGUpcast(long paramLong);
  
  public static final native long HeadTransformModel_getPivotPoint(long paramLong, HeadTransformModel paramHeadTransformModel);
  
  public static final native int HeadTransformModel_getType(long paramLong, HeadTransformModel paramHeadTransformModel);
  
  public static final native boolean HeadTransformModel_setPivotPoint(long paramLong1, HeadTransformModel paramHeadTransformModel, long paramLong2, Vec3F paramVec3F);
  
  public static final native long HitTestResult_getPose(long paramLong, HitTestResult paramHitTestResult);
  
  public static final native float Illumination_AMBIENT_COLOR_TEMPERATURE_UNAVAILABLE_get();
  
  public static final native float Illumination_AMBIENT_INTENSITY_UNAVAILABLE_get();
  
  public static final native float Illumination_getAmbientColorTemperature(long paramLong, Illumination paramIllumination);
  
  public static final native float Illumination_getAmbientIntensity(long paramLong, Illumination paramIllumination);
  
  public static final native boolean ImageTargetBuilder_build(long paramLong, ImageTargetBuilder paramImageTargetBuilder, String paramString, float paramFloat);
  
  public static final native int ImageTargetBuilder_getFrameQuality(long paramLong, ImageTargetBuilder paramImageTargetBuilder);
  
  public static final native long ImageTargetBuilder_getTrackableSource(long paramLong, ImageTargetBuilder paramImageTargetBuilder);
  
  public static final native void ImageTargetBuilder_startScan(long paramLong, ImageTargetBuilder paramImageTargetBuilder);
  
  public static final native void ImageTargetBuilder_stopScan(long paramLong, ImageTargetBuilder paramImageTargetBuilder);
  
  public static final native long ImageTargetResult_SWIGUpcast(long paramLong);
  
  public static final native long ImageTargetResult_getClassType();
  
  public static final native int ImageTargetResult_getNumVirtualButtons(long paramLong, ImageTargetResult paramImageTargetResult);
  
  public static final native long ImageTargetResult_getTrackable(long paramLong, ImageTargetResult paramImageTargetResult);
  
  public static final native long ImageTargetResult_getVirtualButtonResult__SWIG_0(long paramLong, ImageTargetResult paramImageTargetResult, int paramInt);
  
  public static final native long ImageTargetResult_getVirtualButtonResult__SWIG_1(long paramLong, ImageTargetResult paramImageTargetResult, String paramString);
  
  public static final native long ImageTarget_SWIGUpcast(long paramLong);
  
  public static final native long ImageTarget_createVirtualButton(long paramLong1, ImageTarget paramImageTarget, String paramString, long paramLong2, Area paramArea);
  
  public static final native boolean ImageTarget_destroyVirtualButton(long paramLong1, ImageTarget paramImageTarget, long paramLong2, VirtualButton paramVirtualButton);
  
  public static final native long ImageTarget_getClassType();
  
  public static final native String ImageTarget_getMetaData(long paramLong, ImageTarget paramImageTarget);
  
  public static final native int ImageTarget_getNumVirtualButtons(long paramLong, ImageTarget paramImageTarget);
  
  public static final native long ImageTarget_getVirtualButton__SWIG_0(long paramLong, ImageTarget paramImageTarget, int paramInt);
  
  public static final native long ImageTarget_getVirtualButton__SWIG_1(long paramLong, ImageTarget paramImageTarget, String paramString);
  
  public static final native int Image_getBufferHeight(long paramLong, Image paramImage);
  
  public static final native int Image_getBufferWidth(long paramLong, Image paramImage);
  
  public static final native int Image_getFormat(long paramLong, Image paramImage);
  
  public static final native int Image_getHeight(long paramLong, Image paramImage);
  
  public static final native ByteBuffer Image_getPixels(long paramLong, Image paramImage);
  
  public static final native int Image_getStride(long paramLong, Image paramImage);
  
  public static final native int Image_getWidth(long paramLong, Image paramImage);
  
  public static final native ByteBuffer InstanceId_getBuffer(long paramLong, InstanceId paramInstanceId);
  
  public static final native int InstanceId_getDataType(long paramLong, InstanceId paramInstanceId);
  
  public static final native long InstanceId_getLength(long paramLong, InstanceId paramInstanceId);
  
  public static final native BigInteger InstanceId_getNumericValue(long paramLong, InstanceId paramInstanceId);
  
  public static final native float[] Matrix34F_data_get(long paramLong, Matrix34F paramMatrix34F);
  
  public static final native void Matrix34F_data_set(long paramLong, Matrix34F paramMatrix34F, float[] paramArrayOffloat);
  
  public static final native float[] Matrix44F_data_get(long paramLong, Matrix44F paramMatrix44F);
  
  public static final native void Matrix44F_data_set(long paramLong, Matrix44F paramMatrix44F, float[] paramArrayOffloat);
  
  public static final native ByteBuffer Mesh_getNormals(long paramLong, Mesh paramMesh);
  
  public static final native int Mesh_getNumTriangles(long paramLong, Mesh paramMesh);
  
  public static final native int Mesh_getNumVertices(long paramLong, Mesh paramMesh);
  
  public static final native ByteBuffer Mesh_getPositions(long paramLong, Mesh paramMesh);
  
  public static final native ByteBuffer Mesh_getTriangles(long paramLong, Mesh paramMesh);
  
  public static final native ByteBuffer Mesh_getUVs(long paramLong, Mesh paramMesh);
  
  public static final native boolean Mesh_hasNormals(long paramLong, Mesh paramMesh);
  
  public static final native boolean Mesh_hasPositions(long paramLong, Mesh paramMesh);
  
  public static final native boolean Mesh_hasUVs(long paramLong, Mesh paramMesh);
  
  public static final native long ModelTargetResult_SWIGUpcast(long paramLong);
  
  public static final native long ModelTargetResult_getClassType();
  
  public static final native long ModelTargetResult_getTrackable(long paramLong, ModelTargetResult paramModelTargetResult);
  
  public static final native long ModelTarget_SWIGUpcast(long paramLong);
  
  public static final native long ModelTarget_getBoundingBox(long paramLong, ModelTarget paramModelTarget);
  
  public static final native long ModelTarget_getClassType();
  
  public static final native long ModelTarget_getGuideView(long paramLong, ModelTarget paramModelTarget, int paramInt);
  
  public static final native int ModelTarget_getNumGuideViews(long paramLong, ModelTarget paramModelTarget);
  
  public static final native long ModelTarget_getSize(long paramLong, ModelTarget paramModelTarget);
  
  public static final native String ModelTarget_getUniqueTargetId(long paramLong, ModelTarget paramModelTarget);
  
  public static final native boolean ModelTarget_setSize(long paramLong1, ModelTarget paramModelTarget, long paramLong2, Vec3F paramVec3F);
  
  public static final native long MultiTargetResult_SWIGUpcast(long paramLong);
  
  public static final native long MultiTargetResult_getClassType();
  
  public static final native int MultiTargetResult_getNumPartResults(long paramLong, MultiTargetResult paramMultiTargetResult);
  
  public static final native long MultiTargetResult_getPartResult__SWIG_0(long paramLong, MultiTargetResult paramMultiTargetResult, int paramInt);
  
  public static final native long MultiTargetResult_getPartResult__SWIG_1(long paramLong, MultiTargetResult paramMultiTargetResult, String paramString);
  
  public static final native long MultiTargetResult_getTrackable(long paramLong, MultiTargetResult paramMultiTargetResult);
  
  public static final native long MultiTarget_SWIGUpcast(long paramLong);
  
  public static final native int MultiTarget_addPart(long paramLong1, MultiTarget paramMultiTarget, long paramLong2, Trackable paramTrackable);
  
  public static final native long MultiTarget_getClassType();
  
  public static final native int MultiTarget_getNumParts(long paramLong, MultiTarget paramMultiTarget);
  
  public static final native boolean MultiTarget_getPartOffset(long paramLong1, MultiTarget paramMultiTarget, int paramInt, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native long MultiTarget_getPart__SWIG_0(long paramLong, MultiTarget paramMultiTarget, int paramInt);
  
  public static final native long MultiTarget_getPart__SWIG_1(long paramLong, MultiTarget paramMultiTarget, String paramString);
  
  public static final native boolean MultiTarget_removePart(long paramLong, MultiTarget paramMultiTarget, int paramInt);
  
  public static final native boolean MultiTarget_setPartOffset(long paramLong1, MultiTarget paramMultiTarget, int paramInt, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native long Obb2D_getCenter(long paramLong, Obb2D paramObb2D);
  
  public static final native long Obb2D_getHalfExtents(long paramLong, Obb2D paramObb2D);
  
  public static final native float Obb2D_getRotation(long paramLong, Obb2D paramObb2D);
  
  public static final native long Obb3D_getCenter(long paramLong, Obb3D paramObb3D);
  
  public static final native long Obb3D_getHalfExtents(long paramLong, Obb3D paramObb3D);
  
  public static final native float Obb3D_getRotationZ(long paramLong, Obb3D paramObb3D);
  
  public static final native long ObjectTargetResult_SWIGUpcast(long paramLong);
  
  public static final native long ObjectTargetResult_getClassType();
  
  public static final native long ObjectTargetResult_getTrackable(long paramLong, ObjectTargetResult paramObjectTargetResult);
  
  public static final native long ObjectTarget_SWIGUpcast(long paramLong);
  
  public static final native long ObjectTarget_getClassType();
  
  public static final native long ObjectTarget_getSize(long paramLong, ObjectTarget paramObjectTarget);
  
  public static final native String ObjectTarget_getUniqueTargetId(long paramLong, ObjectTarget paramObjectTarget);
  
  public static final native boolean ObjectTarget_setSize(long paramLong1, ObjectTarget paramObjectTarget, long paramLong2, Vec3F paramVec3F);
  
  public static final native long ObjectTracker_SWIGUpcast(long paramLong);
  
  public static final native boolean ObjectTracker_activateDataSet(long paramLong1, ObjectTracker paramObjectTracker, long paramLong2, DataSet paramDataSet);
  
  public static final native long ObjectTracker_createDataSet(long paramLong, ObjectTracker paramObjectTracker);
  
  public static final native boolean ObjectTracker_deactivateDataSet(long paramLong1, ObjectTracker paramObjectTracker, long paramLong2, DataSet paramDataSet);
  
  public static final native boolean ObjectTracker_destroyDataSet(long paramLong1, ObjectTracker paramObjectTracker, long paramLong2, DataSet paramDataSet);
  
  public static final native long ObjectTracker_getActiveDataSet(long paramLong, ObjectTracker paramObjectTracker, int paramInt);
  
  public static final native int ObjectTracker_getActiveDataSetCount(long paramLong, ObjectTracker paramObjectTracker);
  
  public static final native long ObjectTracker_getClassType();
  
  public static final native long ObjectTracker_getImageTargetBuilder(long paramLong, ObjectTracker paramObjectTracker);
  
  public static final native long ObjectTracker_getTargetFinder(long paramLong, ObjectTracker paramObjectTracker);
  
  public static final native boolean ObjectTracker_persistExtendedTracking(long paramLong, ObjectTracker paramObjectTracker, boolean paramBoolean);
  
  public static final native boolean ObjectTracker_resetExtendedTracking(long paramLong, ObjectTracker paramObjectTracker);
  
  public static final native long PositionalDeviceTracker_SWIGUpcast(long paramLong);
  
  public static final native long PositionalDeviceTracker_createAnchor__SWIG_0(long paramLong1, PositionalDeviceTracker paramPositionalDeviceTracker, String paramString, long paramLong2, Matrix34F paramMatrix34F);
  
  public static final native long PositionalDeviceTracker_createAnchor__SWIG_1(long paramLong1, PositionalDeviceTracker paramPositionalDeviceTracker, String paramString, long paramLong2, HitTestResult paramHitTestResult);
  
  public static final native boolean PositionalDeviceTracker_destroyAnchor(long paramLong1, PositionalDeviceTracker paramPositionalDeviceTracker, long paramLong2, Anchor paramAnchor);
  
  public static final native long PositionalDeviceTracker_getAnchor(long paramLong, PositionalDeviceTracker paramPositionalDeviceTracker, int paramInt);
  
  public static final native long PositionalDeviceTracker_getClassType();
  
  public static final native int PositionalDeviceTracker_getNumAnchors(long paramLong, PositionalDeviceTracker paramPositionalDeviceTracker);
  
  public static final native long RectangleInt_SWIGUpcast(long paramLong);
  
  public static final native int RectangleInt_getAreaSize(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native int RectangleInt_getHeight(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native int RectangleInt_getLeftTopX(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native int RectangleInt_getLeftTopY(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native int RectangleInt_getRightBottomX(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native int RectangleInt_getRightBottomY(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native int RectangleInt_getType(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native int RectangleInt_getWidth(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native long Rectangle_SWIGUpcast(long paramLong);
  
  public static final native float Rectangle_getAreaSize(long paramLong, Rectangle paramRectangle);
  
  public static final native float Rectangle_getHeight(long paramLong, Rectangle paramRectangle);
  
  public static final native float Rectangle_getLeftTopX(long paramLong, Rectangle paramRectangle);
  
  public static final native float Rectangle_getLeftTopY(long paramLong, Rectangle paramRectangle);
  
  public static final native float Rectangle_getRightBottomX(long paramLong, Rectangle paramRectangle);
  
  public static final native float Rectangle_getRightBottomY(long paramLong, Rectangle paramRectangle);
  
  public static final native int Rectangle_getType(long paramLong, Rectangle paramRectangle);
  
  public static final native float Rectangle_getWidth(long paramLong, Rectangle paramRectangle);
  
  public static final native boolean RendererHelper_drawVideoBackground();
  
  public static final native long Renderer_begin__SWIG_0(long paramLong, Renderer paramRenderer);
  
  public static final native void Renderer_begin__SWIG_1(long paramLong1, Renderer paramRenderer, long paramLong2, State paramState);
  
  public static final native void Renderer_end(long paramLong, Renderer paramRenderer);
  
  public static final native long Renderer_getInstance();
  
  public static final native int Renderer_getRecommendedFps__SWIG_0(long paramLong, Renderer paramRenderer, int paramInt);
  
  public static final native int Renderer_getRecommendedFps__SWIG_1(long paramLong, Renderer paramRenderer);
  
  public static final native long Renderer_getVideoBackgroundConfig(long paramLong, Renderer paramRenderer);
  
  public static final native long Renderer_getVideoBackgroundTextureInfo(long paramLong, Renderer paramRenderer);
  
  public static final native boolean Renderer_setTargetFps(long paramLong, Renderer paramRenderer, int paramInt);
  
  public static final native void Renderer_setVideoBackgroundConfig(long paramLong1, Renderer paramRenderer, long paramLong2, VideoBackgroundConfig paramVideoBackgroundConfig);
  
  public static final native boolean Renderer_setVideoBackgroundTexture(long paramLong1, Renderer paramRenderer, long paramLong2, TextureData paramTextureData);
  
  public static final native boolean Renderer_updateVideoBackgroundTexture__SWIG_0(long paramLong1, Renderer paramRenderer, long paramLong2, TextureUnit paramTextureUnit);
  
  public static final native boolean Renderer_updateVideoBackgroundTexture__SWIG_1(long paramLong, Renderer paramRenderer);
  
  public static final native long RenderingPrimitives_getDistortionTextureMesh(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getDistortionTextureSize(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getDistortionTextureViewport(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getEffectiveFov(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getEyeDisplayAdjustmentMatrix(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getNormalizedViewport(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getProjectionMatrix__SWIG_0(long paramLong1, RenderingPrimitives paramRenderingPrimitives, int paramInt1, int paramInt2, long paramLong2, CameraCalibration paramCameraCalibration, boolean paramBoolean);
  
  public static final native long RenderingPrimitives_getProjectionMatrix__SWIG_1(long paramLong1, RenderingPrimitives paramRenderingPrimitives, int paramInt1, int paramInt2, long paramLong2, CameraCalibration paramCameraCalibration);
  
  public static final native long RenderingPrimitives_getRenderingViews(long paramLong, RenderingPrimitives paramRenderingPrimitives);
  
  public static final native long RenderingPrimitives_getVideoBackgroundMesh(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getVideoBackgroundProjectionMatrix__SWIG_0(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt1, int paramInt2, boolean paramBoolean);
  
  public static final native long RenderingPrimitives_getVideoBackgroundProjectionMatrix__SWIG_1(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt1, int paramInt2);
  
  public static final native long RenderingPrimitives_getVideoBackgroundTextureSize(long paramLong, RenderingPrimitives paramRenderingPrimitives);
  
  public static final native long RenderingPrimitives_getViewport(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt);
  
  public static final native long RenderingPrimitives_getViewportCentreToEyeAxis(long paramLong, RenderingPrimitives paramRenderingPrimitives, int paramInt1, int paramInt2);
  
  public static final native long RotationalDeviceTracker_SWIGUpcast(long paramLong);
  
  public static final native long RotationalDeviceTracker_getClassType();
  
  public static final native long RotationalDeviceTracker_getDefaultHandheldModel(long paramLong, RotationalDeviceTracker paramRotationalDeviceTracker);
  
  public static final native long RotationalDeviceTracker_getDefaultHeadModel(long paramLong, RotationalDeviceTracker paramRotationalDeviceTracker);
  
  public static final native long RotationalDeviceTracker_getModelCorrection(long paramLong, RotationalDeviceTracker paramRotationalDeviceTracker);
  
  public static final native boolean RotationalDeviceTracker_getPosePrediction(long paramLong, RotationalDeviceTracker paramRotationalDeviceTracker);
  
  public static final native boolean RotationalDeviceTracker_recenter(long paramLong, RotationalDeviceTracker paramRotationalDeviceTracker);
  
  public static final native boolean RotationalDeviceTracker_setModelCorrection(long paramLong1, RotationalDeviceTracker paramRotationalDeviceTracker, long paramLong2, TransformModel paramTransformModel);
  
  public static final native boolean RotationalDeviceTracker_setPosePrediction(long paramLong, RotationalDeviceTracker paramRotationalDeviceTracker, boolean paramBoolean);
  
  public static final native long SmartTerrain_SWIGUpcast(long paramLong);
  
  public static final native long SmartTerrain_getClassType();
  
  public static final native long SmartTerrain_getHitTestResult(long paramLong, SmartTerrain paramSmartTerrain, int paramInt);
  
  public static final native int SmartTerrain_getHitTestResultCount(long paramLong, SmartTerrain paramSmartTerrain);
  
  public static final native void SmartTerrain_hitTest(long paramLong1, SmartTerrain paramSmartTerrain, long paramLong2, State paramState, long paramLong3, Vec2F paramVec2F, float paramFloat, int paramInt);
  
  public static final native double StateUpdater_getCurrentTimeStamp(long paramLong, StateUpdater paramStateUpdater);
  
  public static final native long StateUpdater_getLatestState(long paramLong, StateUpdater paramStateUpdater);
  
  public static final native long StateUpdater_updateState(long paramLong, StateUpdater paramStateUpdater);
  
  public static final native long State_getCameraCalibration(long paramLong, State paramState);
  
  public static final native long State_getDeviceTrackableResult(long paramLong, State paramState);
  
  public static final native long State_getFrame(long paramLong, State paramState);
  
  public static final native long State_getIllumination(long paramLong, State paramState);
  
  public static final native int State_getNumTrackableResults(long paramLong, State paramState);
  
  public static final native int State_getNumTrackables(long paramLong, State paramState);
  
  public static final native long State_getTrackable(long paramLong, State paramState, int paramInt);
  
  public static final native long State_getTrackableResult(long paramLong, State paramState, int paramInt);
  
  public static void SwigDirector_UpdateCallback_Vuforia_onUpdate(UpdateCallback paramUpdateCallback, long paramLong) {
    paramUpdateCallback.Vuforia_onUpdate(new State(paramLong, false));
  }
  
  public static final native void TargetFinder_clearTrackables(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native boolean TargetFinder_deinit(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native long TargetFinder_enableTracking(long paramLong1, TargetFinder paramTargetFinder, long paramLong2, TargetSearchResult paramTargetSearchResult);
  
  public static final native long TargetFinder_getImageTarget(long paramLong, TargetFinder paramTargetFinder, int paramInt);
  
  public static final native int TargetFinder_getInitState(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native int TargetFinder_getNumImageTargets(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native long TargetFinder_getResult(long paramLong, TargetFinder paramTargetFinder, int paramInt);
  
  public static final native int TargetFinder_getResultCount(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native boolean TargetFinder_isRequesting(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native boolean TargetFinder_startInit(long paramLong, TargetFinder paramTargetFinder, String paramString1, String paramString2);
  
  public static final native boolean TargetFinder_startRecognition(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native boolean TargetFinder_stop(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native int TargetFinder_updateSearchResults__SWIG_0(long paramLong, TargetFinder paramTargetFinder, int paramInt);
  
  public static final native int TargetFinder_updateSearchResults__SWIG_1(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native void TargetFinder_waitUntilInitFinished(long paramLong, TargetFinder paramTargetFinder);
  
  public static final native String TargetSearchResult_getMetaData(long paramLong, TargetSearchResult paramTargetSearchResult);
  
  public static final native String TargetSearchResult_getTargetName(long paramLong, TargetSearchResult paramTargetSearchResult);
  
  public static final native float TargetSearchResult_getTargetSize(long paramLong, TargetSearchResult paramTargetSearchResult);
  
  public static final native short TargetSearchResult_getTrackingRating(long paramLong, TargetSearchResult paramTargetSearchResult);
  
  public static final native String TargetSearchResult_getUniqueTargetId(long paramLong, TargetSearchResult paramTargetSearchResult);
  
  public static final native int TextureData_type(long paramLong, TextureData paramTextureData);
  
  public static final native int TextureUnit_type(long paramLong, TextureUnit paramTextureUnit);
  
  public static final native long Tool_convert2GLMatrix(long paramLong, Matrix34F paramMatrix34F);
  
  public static final native long Tool_convertPerspectiveProjection2GLMatrix(long paramLong, Matrix34F paramMatrix34F, float paramFloat1, float paramFloat2);
  
  public static final native long Tool_convertPose2GLMatrix(long paramLong, Matrix34F paramMatrix34F);
  
  public static final native long Tool_getProjectionGL(long paramLong, CameraCalibration paramCameraCalibration, float paramFloat1, float paramFloat2);
  
  public static final native long Tool_multiplyGL(long paramLong1, Matrix44F paramMatrix44F1, long paramLong2, Matrix44F paramMatrix44F2);
  
  public static final native long Tool_multiply__SWIG_0(long paramLong1, Matrix34F paramMatrix34F1, long paramLong2, Matrix34F paramMatrix34F2);
  
  public static final native long Tool_multiply__SWIG_1(long paramLong1, Matrix44F paramMatrix44F1, long paramLong2, Matrix44F paramMatrix44F2);
  
  public static final native long Tool_multiply__SWIG_2(long paramLong1, Vec4F paramVec4F, long paramLong2, Matrix44F paramMatrix44F);
  
  public static final native long Tool_projectPoint(long paramLong1, CameraCalibration paramCameraCalibration, long paramLong2, Matrix34F paramMatrix34F, long paramLong3, Vec3F paramVec3F);
  
  public static final native void Tool_setRotation(long paramLong1, Matrix34F paramMatrix34F, long paramLong2, Vec3F paramVec3F, float paramFloat);
  
  public static final native void Tool_setTranslation(long paramLong1, Matrix34F paramMatrix34F, long paramLong2, Vec3F paramVec3F);
  
  public static final native long TrackableResult_getClassType();
  
  public static final native int TrackableResult_getCoordinateSystem(long paramLong, TrackableResult paramTrackableResult);
  
  public static final native long TrackableResult_getPose(long paramLong, TrackableResult paramTrackableResult);
  
  public static final native int TrackableResult_getStatus(long paramLong, TrackableResult paramTrackableResult);
  
  public static final native int TrackableResult_getStatusInfo(long paramLong, TrackableResult paramTrackableResult);
  
  public static final native double TrackableResult_getTimeStamp(long paramLong, TrackableResult paramTrackableResult);
  
  public static final native long TrackableResult_getTrackable(long paramLong, TrackableResult paramTrackableResult);
  
  public static final native long TrackableResult_getType(long paramLong, TrackableResult paramTrackableResult);
  
  public static final native boolean TrackableResult_isOfType(long paramLong1, TrackableResult paramTrackableResult, long paramLong2, Type paramType);
  
  public static final native long Trackable_getClassType();
  
  public static final native int Trackable_getId(long paramLong, Trackable paramTrackable);
  
  public static final native String Trackable_getName(long paramLong, Trackable paramTrackable);
  
  public static final native long Trackable_getType(long paramLong, Trackable paramTrackable);
  
  public static final native boolean Trackable_isExtendedTrackingStarted(long paramLong, Trackable paramTrackable);
  
  public static final native boolean Trackable_isOfType(long paramLong1, Trackable paramTrackable, long paramLong2, Type paramType);
  
  public static final native boolean Trackable_startExtendedTracking(long paramLong, Trackable paramTrackable);
  
  public static final native boolean Trackable_stopExtendedTracking(long paramLong, Trackable paramTrackable);
  
  public static final native boolean TrackerManager_deinitTracker(long paramLong1, TrackerManager paramTrackerManager, long paramLong2, Type paramType);
  
  public static final native long TrackerManager_getInstance();
  
  public static final native long TrackerManager_getStateUpdater(long paramLong, TrackerManager paramTrackerManager);
  
  public static final native long TrackerManager_getTracker(long paramLong1, TrackerManager paramTrackerManager, long paramLong2, Type paramType);
  
  public static final native long TrackerManager_initTracker(long paramLong1, TrackerManager paramTrackerManager, long paramLong2, Type paramType);
  
  public static final native long Tracker_getClassType();
  
  public static final native long Tracker_getType(long paramLong, Tracker paramTracker);
  
  public static final native boolean Tracker_isOfType(long paramLong1, Tracker paramTracker, long paramLong2, Type paramType);
  
  public static final native boolean Tracker_start(long paramLong, Tracker paramTracker);
  
  public static final native void Tracker_stop(long paramLong, Tracker paramTracker);
  
  public static final native int TransformModel_getType(long paramLong, TransformModel paramTransformModel);
  
  public static final native boolean Type_isOfType(long paramLong1, Type paramType1, long paramLong2, Type paramType2);
  
  public static final native void UpdateCallback_Vuforia_onUpdate(long paramLong1, UpdateCallback paramUpdateCallback, long paramLong2, State paramState);
  
  public static final native void UpdateCallback_change_ownership(UpdateCallback paramUpdateCallback, long paramLong, boolean paramBoolean);
  
  public static final native void UpdateCallback_director_connect(UpdateCallback paramUpdateCallback, long paramLong, boolean paramBoolean1, boolean paramBoolean2);
  
  public static final native float[] Vec2F_data_get(long paramLong, Vec2F paramVec2F);
  
  public static final native void Vec2F_data_set(long paramLong, Vec2F paramVec2F, float[] paramArrayOffloat);
  
  public static final native int[] Vec2I_data_get(long paramLong, Vec2I paramVec2I);
  
  public static final native void Vec2I_data_set(long paramLong, Vec2I paramVec2I, int[] paramArrayOfint);
  
  public static final native float[] Vec3F_data_get(long paramLong, Vec3F paramVec3F);
  
  public static final native void Vec3F_data_set(long paramLong, Vec3F paramVec3F, float[] paramArrayOffloat);
  
  public static final native int[] Vec3I_data_get(long paramLong, Vec3I paramVec3I);
  
  public static final native void Vec3I_data_set(long paramLong, Vec3I paramVec3I, int[] paramArrayOfint);
  
  public static final native float[] Vec4F_data_get(long paramLong, Vec4F paramVec4F);
  
  public static final native void Vec4F_data_set(long paramLong, Vec4F paramVec4F, float[] paramArrayOffloat);
  
  public static final native int[] Vec4I_data_get(long paramLong, Vec4I paramVec4I);
  
  public static final native void Vec4I_data_set(long paramLong, Vec4I paramVec4I, int[] paramArrayOfint);
  
  public static final native boolean VideoBackgroundConfig_Enabled_get(long paramLong, VideoBackgroundConfig paramVideoBackgroundConfig);
  
  public static final native void VideoBackgroundConfig_Enabled_set(long paramLong, VideoBackgroundConfig paramVideoBackgroundConfig, boolean paramBoolean);
  
  public static final native long VideoBackgroundConfig_Position_get(long paramLong, VideoBackgroundConfig paramVideoBackgroundConfig);
  
  public static final native void VideoBackgroundConfig_Position_set(long paramLong1, VideoBackgroundConfig paramVideoBackgroundConfig, long paramLong2, Vec2I paramVec2I);
  
  public static final native int VideoBackgroundConfig_Reflection_get(long paramLong, VideoBackgroundConfig paramVideoBackgroundConfig);
  
  public static final native void VideoBackgroundConfig_Reflection_set(long paramLong, VideoBackgroundConfig paramVideoBackgroundConfig, int paramInt);
  
  public static final native long VideoBackgroundConfig_Size_get(long paramLong, VideoBackgroundConfig paramVideoBackgroundConfig);
  
  public static final native void VideoBackgroundConfig_Size_set(long paramLong1, VideoBackgroundConfig paramVideoBackgroundConfig, long paramLong2, Vec2I paramVec2I);
  
  public static final native long VideoBackgroundTextureInfo_ImageSize_get(long paramLong, VideoBackgroundTextureInfo paramVideoBackgroundTextureInfo);
  
  public static final native int VideoBackgroundTextureInfo_PixelFormat_get(long paramLong, VideoBackgroundTextureInfo paramVideoBackgroundTextureInfo);
  
  public static final native long VideoBackgroundTextureInfo_TextureSize_get(long paramLong, VideoBackgroundTextureInfo paramVideoBackgroundTextureInfo);
  
  public static final native float VideoMode_Framerate_get(long paramLong, VideoMode paramVideoMode);
  
  public static final native int VideoMode_Height_get(long paramLong, VideoMode paramVideoMode);
  
  public static final native int VideoMode_Width_get(long paramLong, VideoMode paramVideoMode);
  
  public static final native boolean ViewList_contains(long paramLong, ViewList paramViewList, int paramInt);
  
  public static final native long ViewList_getNumViews(long paramLong, ViewList paramViewList);
  
  public static final native int ViewList_getView(long paramLong, ViewList paramViewList, int paramInt);
  
  public static final native long ViewerParametersList_begin(long paramLong, ViewerParametersList paramViewerParametersList);
  
  public static final native long ViewerParametersList_end(long paramLong, ViewerParametersList paramViewerParametersList);
  
  public static final native long ViewerParametersList_getListForAuthoringTools();
  
  public static final native long ViewerParametersList_get__SWIG_0(long paramLong1, ViewerParametersList paramViewerParametersList, long paramLong2);
  
  public static final native long ViewerParametersList_get__SWIG_1(long paramLong, ViewerParametersList paramViewerParametersList, String paramString1, String paramString2);
  
  public static final native long ViewerParametersList_next(long paramLong1, ViewerParametersList paramViewerParametersList, long paramLong2, ViewerParameters paramViewerParameters);
  
  public static final native void ViewerParametersList_setSDKFilter(long paramLong, ViewerParametersList paramViewerParametersList, String paramString);
  
  public static final native long ViewerParametersList_size(long paramLong, ViewerParametersList paramViewerParametersList);
  
  public static final native boolean ViewerParameters_containsMagnet(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native int ViewerParameters_getButtonType(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native float ViewerParameters_getDistortionCoefficient(long paramLong, ViewerParameters paramViewerParameters, int paramInt);
  
  public static final native long ViewerParameters_getFieldOfView(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native float ViewerParameters_getInterLensDistance(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native float ViewerParameters_getLensCentreToTrayDistance(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native String ViewerParameters_getManufacturer(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native String ViewerParameters_getName(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native long ViewerParameters_getNumDistortionCoefficients(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native float ViewerParameters_getScreenToLensDistance(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native int ViewerParameters_getTrayAlignment(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native float ViewerParameters_getVersion(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native long VirtualButtonResult_getVirtualButton(long paramLong, VirtualButtonResult paramVirtualButtonResult);
  
  public static final native boolean VirtualButtonResult_isPressed(long paramLong, VirtualButtonResult paramVirtualButtonResult);
  
  public static final native long VirtualButton_getArea(long paramLong, VirtualButton paramVirtualButton);
  
  public static final native int VirtualButton_getID(long paramLong, VirtualButton paramVirtualButton);
  
  public static final native String VirtualButton_getName(long paramLong, VirtualButton paramVirtualButton);
  
  public static final native boolean VirtualButton_isEnabled(long paramLong, VirtualButton paramVirtualButton);
  
  public static final native boolean VirtualButton_setArea(long paramLong1, VirtualButton paramVirtualButton, long paramLong2, Area paramArea);
  
  public static final native boolean VirtualButton_setEnabled(long paramLong, VirtualButton paramVirtualButton, boolean paramBoolean);
  
  public static final native boolean VirtualButton_setSensitivity(long paramLong, VirtualButton paramVirtualButton, int paramInt);
  
  public static final native long VuMarkTargetResult_SWIGUpcast(long paramLong);
  
  public static final native long VuMarkTargetResult_getClassType();
  
  public static final native int VuMarkTargetResult_getId(long paramLong, VuMarkTargetResult paramVuMarkTargetResult);
  
  public static final native long VuMarkTargetResult_getTrackable(long paramLong, VuMarkTargetResult paramVuMarkTargetResult);
  
  public static final native long VuMarkTarget_SWIGUpcast(long paramLong);
  
  public static final native long VuMarkTarget_getClassType();
  
  public static final native long VuMarkTarget_getInstanceId(long paramLong, VuMarkTarget paramVuMarkTarget);
  
  public static final native long VuMarkTarget_getInstanceImage(long paramLong, VuMarkTarget paramVuMarkTarget);
  
  public static final native long VuMarkTarget_getTemplate(long paramLong, VuMarkTarget paramVuMarkTarget);
  
  public static final native long VuMarkTemplate_SWIGUpcast(long paramLong);
  
  public static final native long VuMarkTemplate_getClassType();
  
  public static final native long VuMarkTemplate_getOrigin(long paramLong, VuMarkTemplate paramVuMarkTemplate);
  
  public static final native String VuMarkTemplate_getVuMarkUserData(long paramLong, VuMarkTemplate paramVuMarkTemplate);
  
  public static final native boolean VuMarkTemplate_isTrackingFromRuntimeAppearanceEnabled(long paramLong, VuMarkTemplate paramVuMarkTemplate);
  
  public static final native void VuMarkTemplate_setTrackingFromRuntimeAppearance(long paramLong, VuMarkTemplate paramVuMarkTemplate, boolean paramBoolean);
  
  public static final native void deinit();
  
  public static final native void delete_Anchor(long paramLong);
  
  public static final native void delete_AnchorResult(long paramLong);
  
  public static final native void delete_Area(long paramLong);
  
  public static final native void delete_Box3D(long paramLong);
  
  public static final native void delete_CameraCalibration(long paramLong);
  
  public static final native void delete_CameraDevice(long paramLong);
  
  public static final native void delete_CameraField(long paramLong);
  
  public static final native void delete_CustomViewerParameters(long paramLong);
  
  public static final native void delete_CylinderTarget(long paramLong);
  
  public static final native void delete_CylinderTargetResult(long paramLong);
  
  public static final native void delete_DataSet(long paramLong);
  
  public static final native void delete_Device(long paramLong);
  
  public static final native void delete_DeviceTrackable(long paramLong);
  
  public static final native void delete_DeviceTrackableResult(long paramLong);
  
  public static final native void delete_DeviceTracker(long paramLong);
  
  public static final native void delete_EyewearCalibrationProfileManager(long paramLong);
  
  public static final native void delete_EyewearCalibrationReading(long paramLong);
  
  public static final native void delete_EyewearDevice(long paramLong);
  
  public static final native void delete_EyewearUserCalibrator(long paramLong);
  
  public static final native void delete_Frame(long paramLong);
  
  public static final native void delete_GLTextureData(long paramLong);
  
  public static final native void delete_GLTextureUnit(long paramLong);
  
  public static final native void delete_GuideView(long paramLong);
  
  public static final native void delete_HandheldTransformModel(long paramLong);
  
  public static final native void delete_HeadTransformModel(long paramLong);
  
  public static final native void delete_HitTestResult(long paramLong);
  
  public static final native void delete_Illumination(long paramLong);
  
  public static final native void delete_ImageTarget(long paramLong);
  
  public static final native void delete_ImageTargetResult(long paramLong);
  
  public static final native void delete_InstanceId(long paramLong);
  
  public static final native void delete_Matrix34F(long paramLong);
  
  public static final native void delete_Matrix44F(long paramLong);
  
  public static final native void delete_Mesh(long paramLong);
  
  public static final native void delete_ModelTarget(long paramLong);
  
  public static final native void delete_ModelTargetResult(long paramLong);
  
  public static final native void delete_MultiTarget(long paramLong);
  
  public static final native void delete_MultiTargetResult(long paramLong);
  
  public static final native void delete_Obb2D(long paramLong);
  
  public static final native void delete_Obb3D(long paramLong);
  
  public static final native void delete_ObjectTarget(long paramLong);
  
  public static final native void delete_ObjectTargetResult(long paramLong);
  
  public static final native void delete_ObjectTracker(long paramLong);
  
  public static final native void delete_PositionalDeviceTracker(long paramLong);
  
  public static final native void delete_Rectangle(long paramLong);
  
  public static final native void delete_RectangleInt(long paramLong);
  
  public static final native void delete_Renderer(long paramLong);
  
  public static final native void delete_RendererHelper(long paramLong);
  
  public static final native void delete_RenderingPrimitives(long paramLong);
  
  public static final native void delete_RotationalDeviceTracker(long paramLong);
  
  public static final native void delete_SmartTerrain(long paramLong);
  
  public static final native void delete_State(long paramLong);
  
  public static final native void delete_StateUpdater(long paramLong);
  
  public static final native void delete_TargetFinder(long paramLong);
  
  public static final native void delete_TargetSearchResult(long paramLong);
  
  public static final native void delete_Tool(long paramLong);
  
  public static final native void delete_Trackable(long paramLong);
  
  public static final native void delete_TrackableResult(long paramLong);
  
  public static final native void delete_TrackableSource(long paramLong);
  
  public static final native void delete_Tracker(long paramLong);
  
  public static final native void delete_TrackerManager(long paramLong);
  
  public static final native void delete_TransformModel(long paramLong);
  
  public static final native void delete_Type(long paramLong);
  
  public static final native void delete_UpdateCallback(long paramLong);
  
  public static final native void delete_Vec2F(long paramLong);
  
  public static final native void delete_Vec2I(long paramLong);
  
  public static final native void delete_Vec3F(long paramLong);
  
  public static final native void delete_Vec3I(long paramLong);
  
  public static final native void delete_Vec4F(long paramLong);
  
  public static final native void delete_Vec4I(long paramLong);
  
  public static final native void delete_VideoBackgroundConfig(long paramLong);
  
  public static final native void delete_VideoBackgroundTextureInfo(long paramLong);
  
  public static final native void delete_VideoMode(long paramLong);
  
  public static final native void delete_ViewList(long paramLong);
  
  public static final native void delete_ViewerParameters(long paramLong);
  
  public static final native void delete_ViewerParametersList(long paramLong);
  
  public static final native void delete_VuMarkTarget(long paramLong);
  
  public static final native void delete_VuMarkTargetResult(long paramLong);
  
  public static final native void delete_VuMarkTemplate(long paramLong);
  
  public static final native int getBitsPerPixel(int paramInt);
  
  public static final native int getBufferSize(int paramInt1, int paramInt2, int paramInt3);
  
  public static final native String getLibraryVersion();
  
  public static final native int init();
  
  public static final native boolean isInitialized();
  
  public static final native long new_Box3D__SWIG_0();
  
  public static final native long new_Box3D__SWIG_1(long paramLong, Box3D paramBox3D);
  
  public static final native long new_Box3D__SWIG_2(long paramLong1, Vec3F paramVec3F1, long paramLong2, Vec3F paramVec3F2);
  
  public static final native long new_CameraField();
  
  public static final native long new_CustomViewerParameters__SWIG_0(float paramFloat, String paramString1, String paramString2);
  
  public static final native long new_CustomViewerParameters__SWIG_1(long paramLong, CustomViewerParameters paramCustomViewerParameters);
  
  public static final native long new_EyewearCalibrationReading();
  
  public static final native long new_Frame__SWIG_0();
  
  public static final native long new_Frame__SWIG_1(long paramLong, Frame paramFrame);
  
  public static final native long new_GLTextureData__SWIG_0(int paramInt);
  
  public static final native long new_GLTextureData__SWIG_1();
  
  public static final native long new_GLTextureUnit__SWIG_0(int paramInt);
  
  public static final native long new_GLTextureUnit__SWIG_1();
  
  public static final native long new_HandheldTransformModel__SWIG_0();
  
  public static final native long new_HandheldTransformModel__SWIG_1(long paramLong, HandheldTransformModel paramHandheldTransformModel);
  
  public static final native long new_HandheldTransformModel__SWIG_2(long paramLong, Vec3F paramVec3F);
  
  public static final native long new_HeadTransformModel__SWIG_0();
  
  public static final native long new_HeadTransformModel__SWIG_1(long paramLong, HeadTransformModel paramHeadTransformModel);
  
  public static final native long new_HeadTransformModel__SWIG_2(long paramLong, Vec3F paramVec3F);
  
  public static final native long new_Matrix34F__SWIG_0();
  
  public static final native long new_Matrix34F__SWIG_1(long paramLong, Matrix34F paramMatrix34F);
  
  public static final native long new_Matrix44F__SWIG_0();
  
  public static final native long new_Matrix44F__SWIG_1(long paramLong, Matrix44F paramMatrix44F);
  
  public static final native long new_Obb2D__SWIG_0();
  
  public static final native long new_Obb2D__SWIG_1(long paramLong, Obb2D paramObb2D);
  
  public static final native long new_Obb2D__SWIG_2(long paramLong1, Vec2F paramVec2F1, long paramLong2, Vec2F paramVec2F2, float paramFloat);
  
  public static final native long new_Obb3D__SWIG_0();
  
  public static final native long new_Obb3D__SWIG_1(long paramLong, Obb3D paramObb3D);
  
  public static final native long new_Obb3D__SWIG_2(long paramLong1, Vec3F paramVec3F1, long paramLong2, Vec3F paramVec3F2, float paramFloat);
  
  public static final native long new_RectangleInt__SWIG_0();
  
  public static final native long new_RectangleInt__SWIG_1(long paramLong, RectangleInt paramRectangleInt);
  
  public static final native long new_RectangleInt__SWIG_2(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public static final native long new_Rectangle__SWIG_0();
  
  public static final native long new_Rectangle__SWIG_1(long paramLong, Rectangle paramRectangle);
  
  public static final native long new_Rectangle__SWIG_2(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public static final native long new_RendererHelper();
  
  public static final native long new_RenderingPrimitives(long paramLong, RenderingPrimitives paramRenderingPrimitives);
  
  public static final native long new_State__SWIG_0();
  
  public static final native long new_State__SWIG_1(long paramLong, State paramState);
  
  public static final native long new_Tool();
  
  public static final native long new_TrackableSource();
  
  public static final native long new_Type__SWIG_0();
  
  public static final native long new_Type__SWIG_1(short paramShort);
  
  public static final native long new_UpdateCallback();
  
  public static final native long new_Vec2F__SWIG_0();
  
  public static final native long new_Vec2F__SWIG_1(float[] paramArrayOffloat);
  
  public static final native long new_Vec2F__SWIG_2(float paramFloat1, float paramFloat2);
  
  public static final native long new_Vec2F__SWIG_3(long paramLong, Vec2F paramVec2F);
  
  public static final native long new_Vec2I__SWIG_0();
  
  public static final native long new_Vec2I__SWIG_1(int[] paramArrayOfint);
  
  public static final native long new_Vec2I__SWIG_2(int paramInt1, int paramInt2);
  
  public static final native long new_Vec2I__SWIG_3(long paramLong, Vec2I paramVec2I);
  
  public static final native long new_Vec3F__SWIG_0();
  
  public static final native long new_Vec3F__SWIG_1(float[] paramArrayOffloat);
  
  public static final native long new_Vec3F__SWIG_2(float paramFloat1, float paramFloat2, float paramFloat3);
  
  public static final native long new_Vec3F__SWIG_3(long paramLong, Vec3F paramVec3F);
  
  public static final native long new_Vec3I__SWIG_0();
  
  public static final native long new_Vec3I__SWIG_1(int[] paramArrayOfint);
  
  public static final native long new_Vec4F__SWIG_0();
  
  public static final native long new_Vec4F__SWIG_1(float[] paramArrayOffloat);
  
  public static final native long new_Vec4F__SWIG_2(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public static final native long new_Vec4F__SWIG_3(long paramLong, Vec4F paramVec4F);
  
  public static final native long new_Vec4I__SWIG_0();
  
  public static final native long new_Vec4I__SWIG_1(int[] paramArrayOfint);
  
  public static final native long new_VideoBackgroundConfig();
  
  public static final native long new_VideoBackgroundTextureInfo();
  
  public static final native long new_VideoMode__SWIG_0();
  
  public static final native long new_VideoMode__SWIG_1(long paramLong, VideoMode paramVideoMode);
  
  public static final native long new_ViewerParameters(long paramLong, ViewerParameters paramViewerParameters);
  
  public static final native void onPause();
  
  public static final native void onResume();
  
  public static final native void onSurfaceChanged(int paramInt1, int paramInt2);
  
  public static final native void onSurfaceCreated();
  
  public static final native void registerCallback(long paramLong, UpdateCallback paramUpdateCallback);
  
  public static final native boolean requiresAlpha();
  
  public static final native boolean setFrameFormat(int paramInt, boolean paramBoolean);
  
  public static final native boolean setHint(long paramLong, int paramInt);
  
  private static final native void swig_module_init();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\VuforiaJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
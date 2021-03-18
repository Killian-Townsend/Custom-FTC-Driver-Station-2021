package com.qualcomm.robotcore.hardware.configuration;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.util.ClassUtil;
import java.io.Serializable;

public class DistributorInfoState implements Serializable, Cloneable {
  @Expose
  private String distributor = "";
  
  @Expose
  private String model = "";
  
  @Expose
  private String url = "";
  
  public static DistributorInfoState from(DistributorInfo paramDistributorInfo) {
    DistributorInfoState distributorInfoState = new DistributorInfoState();
    distributorInfoState.setDistributor(ClassUtil.decodeStringRes(paramDistributorInfo.distributor()));
    distributorInfoState.setModel(ClassUtil.decodeStringRes(paramDistributorInfo.model()));
    distributorInfoState.setUrl(ClassUtil.decodeStringRes(paramDistributorInfo.url()));
    return distributorInfoState;
  }
  
  public DistributorInfoState clone() {
    try {
      return (DistributorInfoState)super.clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new RuntimeException("internal error: Parameters not cloneable");
    } 
  }
  
  public String getDistributor() {
    return this.distributor;
  }
  
  public String getModel() {
    return this.model;
  }
  
  public String getUrl() {
    return this.url;
  }
  
  public void setDistributor(String paramString) {
    this.distributor = paramString.trim();
  }
  
  public void setModel(String paramString) {
    this.model = paramString.trim();
  }
  
  public void setUrl(String paramString) {
    this.url = paramString.trim();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\DistributorInfoState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
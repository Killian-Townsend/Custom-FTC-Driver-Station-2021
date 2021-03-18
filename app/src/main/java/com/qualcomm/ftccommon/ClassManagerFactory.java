package com.qualcomm.ftccommon;

import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.ftccommon.configuration.RobotConfigResFilter;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import java.util.Collection;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.internal.opmode.AnnotatedOpModeClassFilter;
import org.firstinspires.ftc.robotcore.internal.opmode.BlocksClassFilter;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassFilter;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassManager;

public class ClassManagerFactory {
  public static void processAllClasses() {
    ClassManager.getInstance().processAllClasses();
  }
  
  public static void registerFilters() {
    registerResourceFilters();
    ClassManager classManager = ClassManager.getInstance();
    classManager.registerFilter((ClassFilter)AnnotatedOpModeClassFilter.getInstance());
    classManager.registerFilter((ClassFilter)BlocksClassFilter.getInstance());
    classManager.registerFilter((ClassFilter)ConfigurationTypeManager.getInstance());
  }
  
  public static void registerResourceFilters() {
    ClassManager classManager = ClassManager.getInstance();
    final RobotConfigResFilter idResFilter = new RobotConfigResFilter(RobotConfigFileManager.getRobotConfigTypeAttribute());
    RobotConfigFileManager.setXmlResourceIdSupplier(new Supplier<Collection<Integer>>() {
          public Collection<Integer> get() {
            return idResFilter.getXmlIds();
          }
        });
    final RobotConfigResFilter idTemplateResFilter = new RobotConfigResFilter(RobotConfigFileManager.getRobotConfigTemplateAttribute());
    RobotConfigFileManager.setXmlResourceTemplateIdSupplier(new Supplier<Collection<Integer>>() {
          public Collection<Integer> get() {
            return idTemplateResFilter.getXmlIds();
          }
        });
    classManager.registerFilter((ClassFilter)robotConfigResFilter1);
    classManager.registerFilter((ClassFilter)robotConfigResFilter2);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\ClassManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
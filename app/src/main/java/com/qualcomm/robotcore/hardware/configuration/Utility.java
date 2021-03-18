package com.qualcomm.robotcore.hardware.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class Utility {
  private Activity activity;
  
  public Utility(Activity paramActivity) {
    this.activity = paramActivity;
  }
  
  public AlertDialog.Builder buildBuilder(String paramString1, String paramString2) {
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.activity);
    builder.setTitle(paramString1).setMessage(paramString2);
    return builder;
  }
  
  public Activity getActivity() {
    return this.activity;
  }
  
  public CharSequence[] getFeedbackText(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    boolean bool;
    CharSequence charSequence1;
    LinearLayout linearLayout = (LinearLayout)this.activity.findViewById(paramInt1);
    if (linearLayout != null) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    TextView textView1 = (TextView)linearLayout.findViewById(paramInt3);
    TextView textView2 = (TextView)linearLayout.findViewById(paramInt4);
    if (textView1 == null || textView1.getText().length() == 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (textView2 == null || textView2.getText().length() == 0) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    if (paramInt1 != 0 && paramInt2 != 0)
      return null; 
    CharSequence charSequence2 = "";
    if (textView1 == null) {
      charSequence1 = "";
    } else {
      charSequence1 = charSequence1.getText();
    } 
    if (textView2 != null)
      charSequence2 = textView2.getText(); 
    return new CharSequence[] { charSequence1, charSequence2 };
  }
  
  public void hideFeedbackText(int paramInt) {
    LinearLayout linearLayout = (LinearLayout)this.activity.findViewById(paramInt);
    linearLayout.removeAllViews();
    linearLayout.setVisibility(8);
  }
  
  public void setFeedbackText(CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    setFeedbackText(paramCharSequence1, paramCharSequence2, paramInt1, paramInt2, paramInt3, paramInt4, 0);
  }
  
  public void setFeedbackText(CharSequence paramCharSequence1, CharSequence paramCharSequence2, final int idParent, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    LinearLayout linearLayout = (LinearLayout)this.activity.findViewById(idParent);
    linearLayout.setVisibility(0);
    linearLayout.removeAllViews();
    this.activity.getLayoutInflater().inflate(paramInt2, (ViewGroup)linearLayout, true);
    TextView textView1 = (TextView)linearLayout.findViewById(paramInt3);
    TextView textView2 = (TextView)linearLayout.findViewById(paramInt4);
    Button button = (Button)linearLayout.findViewById(paramInt5);
    if (textView1 != null)
      textView1.setText(paramCharSequence1); 
    if (textView2 != null)
      textView2.setText(paramCharSequence2); 
    if (button != null) {
      button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              Utility.this.hideFeedbackText(idParent);
            }
          });
      button.setVisibility(0);
    } 
  }
  
  public void setFeedbackText(CharSequence[] paramArrayOfCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    setFeedbackText(paramArrayOfCharSequence[0], paramArrayOfCharSequence[1], paramInt1, paramInt2, paramInt3, paramInt4);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\Utility.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
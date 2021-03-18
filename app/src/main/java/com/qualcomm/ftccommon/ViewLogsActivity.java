package com.qualcomm.ftccommon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import androidx.core.content.FileProvider;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.inspection.R;

public class ViewLogsActivity extends ThemedActivity {
  public static final String FILENAME = "org.firstinspires.ftc.ftccommon.logFilename";
  
  public static final String TAG = "ViewLogsActivity";
  
  int DEFAULT_NUMBER_OF_LINES = 500;
  
  int errorColor;
  
  String filepath = " ";
  
  private File logFile;
  
  WebView webViewForLogcat;
  
  private Spannable colorize(String paramString) {
    SpannableString spannableString = new SpannableString(paramString);
    String[] arrayOfString = paramString.split("\\n");
    int k = arrayOfString.length;
    int i = 0;
    int j = 0;
    while (i < k) {
      String str = arrayOfString[i];
      if (str.contains(" E "))
        spannableString.setSpan(new ForegroundColorSpan(this.errorColor), j, str.length() + j, 33); 
      j = j + str.length() + 1;
      i++;
    } 
    return (Spannable)spannableString;
  }
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_view_logs);
    this.errorColor = getResources().getColor(R.color.text_warning);
    WebView webView = (WebView)findViewById(R.id.webView);
    this.webViewForLogcat = webView;
    webView.getSettings().setBuiltInZoomControls(true);
    this.webViewForLogcat.getSettings().setDisplayZoomControls(false);
    this.webViewForLogcat.setBackgroundColor(getResources().getColor(R.color.logviewer_bgcolor));
  }
  
  protected void onStart() {
    super.onStart();
    Serializable serializable = getIntent().getSerializableExtra("org.firstinspires.ftc.ftccommon.logFilename");
    if (serializable != null)
      this.filepath = (String)serializable; 
    this.logFile = new File(this.filepath);
    try {
      serializable = String.format("<span style='white-space: nowrap;'><font face='monospace' color='white'>%s</font></span>", new Object[] { Html.toHtml((Spanned)colorize(readNLines(this.DEFAULT_NUMBER_OF_LINES))) });
      this.webViewForLogcat.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView param1WebView, String param1String) {
              ViewLogsActivity.this.webViewForLogcat.scrollTo(0, 900000000);
            }
          });
      this.webViewForLogcat.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View param1View) {
              Intent intent = new Intent("android.intent.action.SEND");
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("FTC Robot Log - ");
              stringBuilder1.append(DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
              intent.putExtra("android.intent.extra.SUBJECT", stringBuilder1.toString());
              ViewLogsActivity viewLogsActivity = ViewLogsActivity.this;
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append(ViewLogsActivity.this.getPackageName());
              stringBuilder2.append(".provider");
              intent.putExtra("android.intent.extra.STREAM", (Parcelable)FileProvider.getUriForFile((Context)viewLogsActivity, stringBuilder2.toString(), ViewLogsActivity.this.logFile));
              intent.setType("text/plain");
              ViewLogsActivity.this.startActivity(intent);
              return false;
            }
          });
      this.webViewForLogcat.loadData((String)serializable, "text/html", "UTF-8");
      return;
    } catch (IOException iOException) {
      RobotLog.ee("ViewLogsActivity", iOException, "Exception loading logcat data");
      this.webViewForLogcat.loadData("<font color='white'>Error loading logcat data</font>", "text/html", "UTF-8");
      return;
    } 
  }
  
  public String readNLines(int paramInt) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(this.logFile));
    String[] arrayOfString = new String[paramInt];
    int i = 0;
    int j = 0;
    while (true) {
      String str2 = bufferedReader.readLine();
      if (str2 != null) {
        arrayOfString[j % paramInt] = str2;
        j++;
        continue;
      } 
      int k = j - paramInt;
      if (k >= 0)
        i = k; 
      String str1 = "";
      while (i < j) {
        str2 = arrayOfString[i % paramInt];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str1);
        stringBuilder.append(str2);
        stringBuilder.append("\n");
        str1 = stringBuilder.toString();
        i++;
      } 
      paramInt = str1.lastIndexOf("--------- beginning");
      return (paramInt < 0) ? str1 : str1.substring(paramInt);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\ViewLogsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
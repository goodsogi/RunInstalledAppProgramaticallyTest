package com.example.usemaximummemorytest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //안드로이드 11에서 모든 앱의 정보를 가져오려면 AndroidManifest 파일에
        // <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>
        //를 선언해야 함 

        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);

        main.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);


        Handler handler1 = new Handler();

        for (int a=14; a <= 20; a++) {
            Log.d("MainActivity", "plusapps a: " + a);

            //1초에 1개의 앱을 실행하려고 하니 서너개의 앱만 실행됨
            int finalA = a;
            handler1.postDelayed(new Runnable() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityInfo activity = launchables.get(finalA).activityInfo;

                           //제외할 앱 패키지명(라이브스코어)
                            if (activity.applicationInfo.packageName.contains("kr.co.psynet")) {
                                return;
                            }


                            Log.d("MainActivity", "plusapps activity.applicationInfo.packageName: " + activity.applicationInfo.packageName);
                            ComponentName name = new
                                    ComponentName(activity.applicationInfo.packageName, activity.name);
                            Intent i = new Intent(Intent.ACTION_MAIN);

                            i.addCategory(Intent.CATEGORY_LAUNCHER);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            i.setComponent(name);

                            try {
                                startActivity(i);
                            } catch (Exception e) {
                                Log.d("MainActivity", "plusapps startActivity error: " + e.getMessage());

                            }
                        }
                    });

                }
            }, 1000 * a);
        }


    }
}
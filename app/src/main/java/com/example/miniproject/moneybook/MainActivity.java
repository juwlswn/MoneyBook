package com.example.miniproject.moneybook;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    EditText total, days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total = (EditText)findViewById(R.id.total);
        days = (EditText)findViewById(R.id.days);
    }

    public void onBackPressed(){
    }

    public void buttonClicked(View v){
        if (v.getId() == R.id.btn){

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, SubActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),android.R.drawable.star_on));
            builder.setSmallIcon(android.R.drawable.star_on);
            String t = total.getText().toString();
            String d = days.getText().toString();
            try {
                FileOutputStream fos = openFileOutput
                        ("total.txt", // 파일명 지정
                                Context.MODE_APPEND);// 저장모드
                PrintWriter out = new PrintWriter(fos);
                out.write(t);
                out.close();
                Toast.makeText(MainActivity.this,"저장오케이",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fos = openFileOutput
                        ("days.txt", // 파일명 지정
                                Context.MODE_APPEND);// 저장모드
                PrintWriter out = new PrintWriter(fos);
                out.write(d);
                out.close();
                Toast.makeText(MainActivity.this,"저장오케이2",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            builder.setTicker("나만의 가계부를 만들어보아요");
            builder.setContentTitle("간단한가계부");
            builder.setContentText("생존일:" + d +"일까지 화이팅!");
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            builder.setNumber(999);
            notificationManager.notify(0, builder.build());

            Intent intent2 = new Intent(this, SubActivity.class);
            intent2.putExtra("start", t);

            startActivityForResult(intent2,1);

        }
    }
}

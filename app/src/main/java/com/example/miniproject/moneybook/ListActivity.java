package com.example.miniproject.moneybook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ListActivity extends Activity{
    TextView listview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listview = findViewById(R.id.listview);
        try {
            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            StringBuffer data = new StringBuffer();
            FileInputStream fis = openFileInput("myfile.txt");//파일명
            BufferedReader buffer = new BufferedReader
                    (new InputStreamReader(fis));
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
            while (str != null) {
                data.append(str + "\n");
                str = buffer.readLine();
            }
            listview.setText(data);
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backClicked(View v){
        if (v.getId() == R.id.back){
            Intent intent = new Intent(ListActivity.this, SubActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

package com.example.miniproject.moneybook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AddActivity extends Activity {
    EditText cash,account;
    String c,a,t,count;
    Button deposit, withdrawal;
    int cnt,amount;
    ArrayList<MoneyHistory> mList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        t = intent.getStringExtra("total");
        count= intent.getStringExtra("count");

        cnt = Integer.parseInt(count);
        cash = findViewById(R.id.cash);
        account = findViewById(R.id.account);
        deposit = findViewById(R.id.deposit);
        withdrawal = findViewById(R.id.withdrawal);

    }

    public void btnOnclicked(View v){
        switch (v.getId()){
            case R.id.delete:
                finish();
                break;
            case R.id.deposit:
                Intent intent = new Intent();
                c = cash.getText().toString();
                a = account.getText().toString();
                amount = Integer.parseInt(c);
                MoneyHistory m = new MoneyHistory(amount,a,cnt,"입금");
                try {
                    FileOutputStream fos = openFileOutput
                            ("myfile.txt", // 파일명 지정
                                    Context.MODE_APPEND);// 저장모드
                    PrintWriter out = new PrintWriter(fos);
                    out.println(m);
                    out.close();

                    Toast.makeText(AddActivity.this,"저장되었다요",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                intent.putExtra("plus",c);
                setResult(1,intent);
                finish();
                break;
            case R.id.withdrawal:
                c = cash.getText().toString();
                a = account.getText().toString();
                amount = Integer.parseInt(c);
                //출력스트림 생성
                MoneyHistory m2 = new MoneyHistory(amount,a,cnt,"출금");
                try {
                    FileOutputStream fos = openFileOutput
                            ("myfile.txt", // 파일명 지정
                                    Context.MODE_APPEND);// 저장모드
                    PrintWriter out = new PrintWriter(fos);
                    out.println(m2);
                    out.close();

                    Toast.makeText(AddActivity.this,"저장되었다요",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(AddActivity.this,m2+"갑니까",Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent();
                intent2.putExtra("minus",c);
                setResult(2,intent2);
                finish();
                break;
        }
    }
}

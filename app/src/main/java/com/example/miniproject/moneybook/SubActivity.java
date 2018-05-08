package com.example.miniproject.moneybook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class SubActivity extends Activity {
    TextView count,days,total,todaymoney,todayper,totalper;
    int cnt=1, today=0, today_M =0, warning =1, start_price =0, total_plus = 0, total_minus =0,day =0, nextday_M,getper,p,toper,p2,tomoney;
    double per,per2;
    String arr[] = {"こんにちは！", "触るな", "何もしたくない(笑)", "いい？"};
    ImageView tr1,tr2,tr3,tr4;
    String t,d,plus,minus,daycount,s,finish;
    ProgressBar bar1,bar2;
    Vibrator vibrator;
    private BackPressCloseHandler backPressCloseHandler;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        // 초기 금액 입력값 받기
        Intent intent = getIntent();
        s = intent.getStringExtra("start");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        // 근데 지금 얘가 이걸 못받아오는것 같답!!!*********************************
        if (s!= null) {
            try {
                FileOutputStream fos = openFileOutput
                        ("start.txt", // 파일명 지정
                                Context.MODE_APPEND);// 저장모드
                PrintWriter out = new PrintWriter(fos);
                out.write(s);
                out.close();
                Toast.makeText(SubActivity.this, "setstartmoney", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            FileInputStream fos = null;
            try {
                fos = openFileInput("start.txt");//파일명
                byte[] byt = new byte[fos.available()];
                fos.read(byt);
                String tt = new String(byt);
                s = tt;
                Toast.makeText(SubActivity.this, tt, Toast.LENGTH_SHORT).show();
                fos.close();
            } catch (IOException e) {
                Toast.makeText(SubActivity.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        // 진동
        // 1. 진동 권한을 획득해야한다. AndroidManifest.xml
        // 2. Vibrator 객체를 얻어서 진동시킨다
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        backPressCloseHandler = new BackPressCloseHandler(this);

        //총 생존 날짜 중 현재 얼마 지났는지.
        count = (TextView)findViewById(R.id.count);
        daycount = count.getText().toString();

        //생존 날짜
        days = (TextView)findViewById(R.id.days);
        //오늘 쓴 돈
        todaymoney = (TextView)findViewById(R.id.todaymoney);
        //오늘 쓴 돈 퍼센트
        todayper = (TextView)findViewById(R.id.todayper);
        //남은 금액
        total = (TextView)findViewById(R.id.total);
        //남은금액 퍼센트
        totalper = (TextView)findViewById(R.id.totalper);

        //상태바
        bar1 = (ProgressBar)findViewById(R.id.bar1);
        bar2 = (ProgressBar)findViewById(R.id.bar2);

        //나무 이미지
        tr1 = (ImageView)findViewById(R.id.tr1);
        tr2 = (ImageView)findViewById(R.id.tr2);
        tr3 = (ImageView)findViewById(R.id.tr3);
        tr4 = (ImageView)findViewById(R.id.tr4);

        //프로그레스바, % 불러오기.
        getPreferences();

        getper = bar1.getProgress();
        toper = bar2.getProgress();


        // 총 금액 바뀔때마다 담을 파일
        FileInputStream in = null;
        try{
            in = openFileInput("total.txt");
            byte byt[] = new byte[in.available()];
            in.read(byt);
            String tt = new String(byt);
            total.setText(tt);
            t = tt;
            Toast.makeText(SubActivity.this,t,Toast.LENGTH_SHORT).show();
            in.close();
        }catch (IOException e){
            Toast.makeText(SubActivity.this,"불러오기 실패",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
    }

        // 생존 날짜
        FileInputStream in2 = null;
        try{
            in2 = openFileInput("days.txt");
            byte byt[] = new byte[in2.available()];
            in2.read(byt);
            String tt = new String(byt);
            days.setText(tt);
            d = tt;
            Toast.makeText(SubActivity.this,d,Toast.LENGTH_SHORT).show();
            in2.close();
        }catch (IOException e){
            Toast.makeText(SubActivity.this,"불러오기 실패",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // 생존 날짜 중 현재 몇일인지 담을 파일
        FileInputStream in3 = null;
            try {
                in3 = openFileInput("daycount.txt");//파일명
                byte[] byt = new byte[in3.available()];
                in3.read(byt);
                String tt = new String(byt);
                count.setText(tt);
                cnt = Integer.parseInt(tt);
                Toast.makeText(SubActivity.this, tt, Toast.LENGTH_SHORT).show();
                in3.close();
            } catch (IOException e) {
                Toast.makeText(SubActivity.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        //불러온 남은 금액 파일 int로 바꿈
        today = Integer.parseInt(t);

        //처음 시작한 돈
        start_price = Integer.parseInt(s);

        //불러온 생존 날짜 int로 바꿈
        day = Integer.parseInt(d);

        //오늘의 돈
        today_M = today/day;

        savetoday_M();
        todaymoney.setText(Integer.toString(tomoney));


    }

    //뒤로 가기 버튼 막기, 2번 누르면 종료
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();

    }

    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis(); showGuide(); return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish(); toast.cancel();
            }
        }

        private void showGuide() {
            toast = Toast.makeText(
                    activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT); toast.show();
        }

    }

    public void btnClicked(View v){
        switch (v.getId()){
            case R.id.forward:
                AlertDialog.Builder d2 = new AlertDialog.Builder(this);
                if (cnt==day) {
                    d2.setTitle("생존"+ cnt + "일 차 입니다.");
                    d2.setMessage("종료하시겠습니까?");
                    d2.setCancelable(false);
                    d2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            filedelete();
                        }
                    });
                    d2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String s = String.valueOf(i);
                            Toast.makeText(SubActivity.this,s,Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    d2.setTitle("생존"+ cnt + "일 차 입니다.");
                    d2.setMessage("생존" +(cnt+1)+ "일차로 넘길까요?");
                    d2.setCancelable(false);
                    d2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cnt++;
                            warning=1;
                            count.setText(Integer.toString(cnt));
                            today_M = nextday_M;
                            todaymoney.setText(Integer.toString(today_M));

                            boolean daycountDeleted = deleteFile("daycount.txt");
                            if(daycountDeleted){
                                Toast.makeText(SubActivity.this, "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SubActivity.this, "파일 삭제 실패", Toast.LENGTH_SHORT).show();
                            }

                            try {
                                FileOutputStream fos = openFileOutput
                                        ("daycount.txt", // 파일명 지정
                                                Context.MODE_APPEND);// 저장모드
                                PrintWriter out = new PrintWriter(fos);
                                out.write(Integer.toString(cnt));
                                out.close();
                                Toast.makeText(SubActivity.this,"저장오케이",Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    d2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String s = String.valueOf(i);
                            Toast.makeText(SubActivity.this,s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                d2.show();

                break;
            case R.id.delete:
                AlertDialog.Builder d3 = new AlertDialog.Builder(this);
                d3.setTitle("생존 포기");
                d3.setMessage("생존을 포기하시겠습니까?");
                d3.setCancelable(false);
                d3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filedelete();
                    }
                });
                d3.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = String.valueOf(i);
                        Toast.makeText(SubActivity.this,s,Toast.LENGTH_SHORT).show();
                    }
                });
                d3.show();
                break;
            case R.id.money:
                    Intent intent = new Intent(this, AddActivity.class);
                    intent.putExtra("total", total.toString());
                    intent.putExtra("count", count.getText());
                    startActivityForResult(intent, 1);
                break;
            case R.id.list:
                Intent intent2 = new Intent(this, ListActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //실행되었을때 현재 상황 파악 어디갔다왔냐가 requestCode/ resultCode는 올때 정한 값(ex) 숫자또는 RESULT_OK)

        if (requestCode ==1) {
            if (resultCode == 1){
                plus = data.getStringExtra("plus");
                int plus_money = Integer.parseInt(plus);
                today = today + plus_money;
                total.setText(Integer.toString(today));
                //Toast.makeText(SubActivity.this,plus,Toast.LENGTH_SHORT).show();
                double per = ((double)plus_money/ start_price) * 100;

                //Toast.makeText(SubActivity.this,per + "가?",Toast.LENGTH_SHORT).show();
                int getper = bar1.getProgress();
                p = getper+(int)per;
                savePreferences();
                bar1.setProgress(p);
                totalper.setText(p+"%");
                changeImage();
            }
            if (resultCode == 2){
                minus = data.getStringExtra("minus");
                int minu_money = Integer.parseInt(minus);
                today = today - minu_money;
                today_M = today_M - minu_money;

                boolean totalDeleted = deleteFile("total.txt");
                if(totalDeleted){
                    Toast.makeText(SubActivity.this, "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SubActivity.this, "파일 삭제 실패", Toast.LENGTH_SHORT).show();
                }

               saveTotalMoney(today);
                total.setText(Integer.toString(today));
                todaymoney.setText(Integer.toString(today_M));
                //Toast.makeText(SubActivity.this,minus,Toast.LENGTH_SHORT).show();

                if (today_M <= 0){
                    AlertDialog.Builder d = new AlertDialog.Builder(this);
                    d.setTitle("경고");
                    d.setMessage("하루 가능 금액을 초과했습니다. 누적경고: " + warning);
                    d.setCancelable(false);
                    d.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            warning++;
                        }
                    });
                    d.show();
                }

                if (warning == 4){
                    AlertDialog.Builder d = new AlertDialog.Builder(this);
                    d.setTitle("강제 초기화");
                    d.setMessage("목표 미달성 3회를 초과하면 초기화됩니다." );
                    d.setCancelable(false);
                    d.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            warning=0;
                            filedelete();
                        }
                    });
                    d.show();
                }

                if (today<=0){
                    AlertDialog.Builder d = new AlertDialog.Builder(this);
                    d.setTitle("생존 실패");
                    d.setMessage("금액 초과로 생존에 실패하였습니다." );
                    d.setMessage("현재금액: " + today);
                    d.setCancelable(false);
                    d.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            warning=0;
                            filedelete();
                        }
                    });
                    d.show();
                }
                /*per = (minu_money*100) / Integer.parseInt(s);
                per2 = ((double)minu_money / tomoney) * 100;
                //Toast.makeText(SubActivity.this,per + "가?",Toast.LENGTH_SHORT).show();
                getper = bar1.getProgress();
                toper = bar2.getProgress();

                p = getper-(int)per;
                p2 = toper-(int)per2;*/

                savePreferences();
                int nokottaOkanePer = getPercentage(today, Integer.parseInt(s));
                int nokottaKyounoOkanePer = getPercentage(today_M, Integer.parseInt(s)/Integer.parseInt(d));
                bar1.setProgress(nokottaOkanePer);
                bar2.setProgress(nokottaKyounoOkanePer);

                totalper.setText(nokottaOkanePer+"%");
                todayper.setText(nokottaKyounoOkanePer+"%");
                changeImage();
            }
        }
    }

    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("p", p);
        editor.putInt("next",nextday_M);
        editor.commit();
        Toast.makeText(this,"savePreferences",Toast.LENGTH_SHORT).show();
    }

    private void savetoday_M(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("tomoney", today_M);
        editor.commit();
        Toast.makeText(this,"savetoday_M",Toast.LENGTH_SHORT).show();
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        p = pref.getInt("p", 100);

        if (p != 0) {
            bar1.setProgress(p);
            //bar1.setProgress(getper-(int)per);
            totalper.setText(p + "%");
            //totalper.setText(getper-(int)per+"%");
        } else {
            bar1.setProgress(100);
            totalper.setText(100 + "%");
        }
        tomoney = pref.getInt("tomoney",0);
    }

    private void changeImage(){
        if (bar1.getProgress()>=80){
            tr1.setVisibility(View.VISIBLE);
            tr2.setVisibility(View.INVISIBLE);
            tr3.setVisibility(View.INVISIBLE);
            tr4.setVisibility(View.INVISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            tr1.startAnimation(anim);
            vibrator.vibrate(1000); // miliSecond, 지정한 시간동안 진동

        } else if (bar1.getProgress()<80 && bar1.getProgress()>=50) {
            tr2.setVisibility(View.VISIBLE);
            tr1.setVisibility(View.INVISIBLE);
            tr3.setVisibility(View.INVISIBLE);
            tr4.setVisibility(View.INVISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            tr2.startAnimation(anim);
            vibrator.vibrate(1000); // miliSecond, 지정한 시간동안 진동

        } else if (bar1.getProgress()<50 && bar1.getProgress()>10) {
            tr3.setVisibility(View.VISIBLE);
            tr1.setVisibility(View.INVISIBLE);
            tr2.setVisibility(View.INVISIBLE);
            tr4.setVisibility(View.INVISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            tr3.startAnimation(anim);
            vibrator.vibrate(1000); // miliSecond, 지정한 시간동안 진동

        } else if (bar1.getProgress()<=10){
            tr4.setVisibility(View.VISIBLE);
            tr1.setVisibility(View.INVISIBLE);
            tr2.setVisibility(View.INVISIBLE);
            tr3.setVisibility(View.INVISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            tr4.startAnimation(anim);
            vibrator.vibrate(1000); // miliSecond, 지정한 시간동안 진동
        }
    }

    public void filedelete(){
        boolean isDeleted = deleteFile("myfile.txt");
        if(isDeleted){
            Toast.makeText(SubActivity.this, "파일 삭제 성공", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SubActivity.this, "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }

        boolean totalDeleted = deleteFile("total.txt");
        if(totalDeleted){
            Toast.makeText(SubActivity.this, "파일 삭제 성공", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SubActivity.this, "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }

        boolean daysDeleted = deleteFile("days.txt");
        if(daysDeleted){
            Toast.makeText(SubActivity.this, "파일 삭제 성공", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SubActivity.this, "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }

        boolean daycountDeleted = deleteFile("daycount.txt");
        if(daycountDeleted){
            Toast.makeText(SubActivity.this, "파일 삭제 성공", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SubActivity.this, "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }

        boolean daystartmoney = deleteFile("start.txt");
        if(daystartmoney){
            Toast.makeText(SubActivity.this, "파일 삭제 성공", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SubActivity.this, "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }


        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("p");
        editor.remove("tomoney");
        editor.commit();


        Intent intent2 = new Intent(this, LoadingActivity.class);
        startActivity(intent2);
    }

    public void imageClicked(View view){
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        Random rnd = new Random();
        int randNum = rnd.nextInt(arr.length);
        d.setMessage(arr[randNum]);
        d.setCancelable(false);
        d.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        d.show();
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        view.startAnimation(anim);
        vibrator.vibrate(1000);
    }

    public int getPercentage(int nowMoney, int totalMoney) {
        int result = (nowMoney*100)/totalMoney;

        return result;
    }

    public void saveTotalMoney(int totalMoney) {
        try {
            FileOutputStream fos = openFileOutput
                    ("total.txt", // 파일명 지정
                            Context.MODE_APPEND);// 저장모드
            PrintWriter out = new PrintWriter(fos);
            out.write(Integer.toString(totalMoney));
            out.close();
            Toast.makeText(SubActivity.this,"저장오케이",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

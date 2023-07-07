package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class IntroActivity extends AppCompatActivity {

    TextView school_intro;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    school_intro.setText((String)msg.obj);
                    break;
                case 2:
                    break;
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        school_intro = (TextView)findViewById(R.id.school_intro);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = "电子科技大学坐落于四川省成都市，原名成都电讯工程学院，是1956年在周恩来总理的亲自部署下" +
                        "，由交通大学（现上海交通大学、西安交通大学）、南京工学院（现东南大学）、华南工学院（现华" +
                        "南理工大学）的电讯工程有关专业合并创建而成。 学校1960年被中共中央列为全国重点高等学校，" +
                        "1961年被中共中央确定为七所国防工业院校之一，1988年更名为电子科技大学，1997年被确定为国家" +
                        "首批“211工程”建设的重点大学，2000年由原信息产业部主管划转为教育部主管，2001年进入国家“985" +
                        "工程”重点建设大学行列，2017年进入国家建设“世界一流大学”A类高校行列。 2019年教育部和四川省" +
                        "签约共同推进我校世界一流大学建设。 2022年入选国家第二轮“双一流”建设高校名单。";
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }
}
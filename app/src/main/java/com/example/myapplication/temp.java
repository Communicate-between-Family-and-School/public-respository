package com.example.myapplication;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class temp extends AppCompatActivity {
    TextView textView1, textView2, textView3;
    private static final String Tag = "mysql1111";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x11:
                case 0x12:
                    String s = (String) msg.obj;
                        textView3.setText(s);
                        break;
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i(Tag, "start");
        textView1 = (TextView) findViewById(R.id.textView1);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        //添加切换事件，也可以添加点击事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1)
                    textView1.setText("Button1 is clicked");
                else if (checkedId == R.id.radioButton2)
                    textView1.setText("Button2 is clicked");
                else if (checkedId == R.id.radioButton3)
                    textView1.setText("Button3 is clicked");
            }
        });

        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText("");
        RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        RadioButton radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        RadioButton radioButton6 = (RadioButton) findViewById(R.id.radioButton6);
        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView2.getText() == "") {
                    textView2.append("Button4 is clicked");
                } else {
                    textView2.append("\nButton4 is clicked");
                }
            }
        });
        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView2.getText() == "") {
                    textView2.append("Button5 is clicked");
                } else {
                    textView2.append("\nButton5 is clicked");
                }
            }
        });
        radioButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView2.getText() == "") {
                    textView2.append("Button6 is clicked");
                } else {
                    textView2.append("\nButton6 is clicked");
                }
            }
        });
        textView3 = (TextView) findViewById(R.id.textView3);
        Button button7 = (Button) findViewById(R.id.button7);
        // 按钮点击事件
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                        HashMap<String, Object> map = com.example.myapplication.DBUtils.getInfoByName("zhz");
                        Message message = handler.obtainMessage();
                        if (map != null) {
                            String s = "";
                            for (String key : map.keySet()) {
                                s += key + ":" + map.get(key) + "\n";
                            }
                            message.what = 0x12;
                            message.obj = s;
                        } else {
                            message.what = 0x11;
                            message.obj = "查询结果为空";
                        }
                        // 发消息通知主线程更新UI
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}


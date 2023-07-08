package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Attendance extends AppCompatActivity {
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        back = back.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });
        ListView listView = (ListView) findViewById(R.id.list_view);
        String[] data={"15/09/08 未刷卡","15/09/09 未刷卡","15/09/10 未刷卡","15/09/11 未刷卡", "15/09/12 刷卡", "15/09/13 未刷卡", "15/09/14 刷卡","15/09/15 未刷卡","15/09/16 未刷卡","15/09/17 未刷卡"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(Attendance.this,android.R.layout.simple_list_item_1,data);

    }
}

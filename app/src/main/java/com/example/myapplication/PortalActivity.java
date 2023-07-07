package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PortalActivity extends AppCompatActivity {

    private View.OnClickListener l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
        Button intro_btn = (Button) findViewById(R.id.introduction);
        Button info_btn = (Button)findViewById(R.id.information);
        Button login_btn = (Button)findViewById(R.id.login);
        intro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortalActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });
        info_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PortalActivity.this, SchoolInfoActivity.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortalActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
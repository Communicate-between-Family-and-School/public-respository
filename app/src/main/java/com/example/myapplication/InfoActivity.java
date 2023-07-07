package com.example.myapplication;

import static com.example.myapplication.IDENTITY.IdentityType.LEADER;
import static com.example.myapplication.IDENTITY.IdentityType.PARENT;
import static com.example.myapplication.IDENTITY.IdentityType.STUDENT;
import static com.example.myapplication.IDENTITY.IdentityType.TEACHER;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoActivity extends AppCompatActivity {
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
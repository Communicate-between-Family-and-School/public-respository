package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class audit extends AppCompatActivity {
    TextView audit_id;/*家长编号*/
    TextView audit_pname;/*家长名*/
    TextView audit_ppwd;/*家长密码*/
    TextView audit_sid;/*学生编号*/
    TextView audit_sname;/*学生名*/
    Button back;
    Button reject;/*拒绝*/
    Button pass;/*通过*/
    Button next;/*下一位*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        audit_id = findViewById(R.id.audit_pid);
        audit_pname = findViewById(R.id.audit_pname);
        audit_ppwd = findViewById(R.id.audit_ppwd);
        audit_sid = findViewById(R.id.audit_sid);
        audit_sname = findViewById(R.id.audit_sname);
        reject = findViewById(R.id.reject);
        pass = findViewById(R.id.pass);
        next = findViewById(R.id.parent_next);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
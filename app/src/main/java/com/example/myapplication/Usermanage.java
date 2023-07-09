package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Usermanage extends AppCompatActivity {
    Button back;/*返回按键*/
    RadioGroup operate;/*操作组*/
    String stroperate;/*操作标识"insert"插入，"update"修改，"delete"删除*/
    RadioGroup role;/*角色选项*/
    String strrole;/*角色标识*/
    EditText editid;/*用户id*/
    EditText editname;/*用户名*/
    EditText editpwd;/*密码*/
    EditText classnum;/*班号*/
    Button vertify;/*确认按钮*/
    TextView txtid;/*显示id*/
    TextView txtname;/*显示用户名*/
    TextView txtclassnum;/*显示关联班级号*/
    TextView txttype;/*显示用户类型*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanage);
        operate = findViewById(R.id.adm_operate);
        operate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.adm_insert){editid.setEnabled(false);stroperate = "insert";}
                if(i==R.id.adm_update){editid.setEnabled(true);stroperate = "update";}
                if(i==R.id.adm_delete){editid.setEnabled(true);stroperate = "delete";}
            }
        });
        role = findViewById(R.id.adm_role);
        role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.adm_leader){classnum.setEnabled(false);strrole = "leader";}
                if(i==R.id.adm_student){classnum.setEnabled(true);strrole = "student";}
                if(i==R.id.adm_parent){classnum.setEnabled(true);strrole = "parent";}
                if(i==R.id.adm_teacher){classnum.setEnabled(true);strrole = "teacher";}
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vertify = findViewById(R.id.adm_vertify);
        editid = findViewById(R.id.adm_editid);
        editname = findViewById(R.id.adm_editname);
        editpwd = findViewById(R.id.adm_editpwd);
        classnum = findViewById(R.id.adm_editclass);
        txtid = findViewById(R.id.adm_uid);
        txtname = findViewById(R.id.adm_name);
        txtclassnum = findViewById(R.id.adm_classnum);
        txttype = findViewById(R.id.adm_type);
    }
}
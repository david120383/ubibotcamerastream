package com.tutk.sample.AVAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends Activity {

    private String maddflg = "1";//新增、更新数据标识（1：新增；2：更新）
    private EditText nameEdit;
    private EditText uidEdit;
    private EditText accountEdit;
    private EditText passwordEdit;
    private String name;
    private String uid;
    private String account;
    private String password;
    private String addflg;//新增、更新数据标识（1：新增；2：更新）
    private String listindexupdate;//需要更新的数组index
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        TextView save = (TextView) findViewById(R.id.add_save);
        nameEdit = findViewById(R.id.add_edit_camera_name);
        uidEdit = findViewById(R.id.add_edit_uid);
        accountEdit = findViewById(R.id.add_edit_account);
        passwordEdit = findViewById(R.id.add_edit_password);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEdit.getText().toString();
                uid = uidEdit.getText().toString();
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.putExtra("addflg", maddflg);
                intent.putExtra("name", name);
                intent.putExtra("uid", uid);
                intent.putExtra("password", password);
                intent.putExtra("account", account);
                if (maddflg.equals("2")) {
                    intent.putExtra("listindexupdate", listindexupdate);
                }
//                Toasty.success(this, "添加成功", Toast.LENGTH_SHORT, true).show();
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        addflg = intent.getStringExtra("addflg");
        listindexupdate = intent.getStringExtra("listindexupdate");
        name = intent.getStringExtra("name");
        uid = intent.getStringExtra("uid");
        password = intent.getStringExtra("password");
        account = intent.getStringExtra("account");
        if (name != null && uid != null && password != null && account != null) {
            nameEdit.setText(name);
            uidEdit.setText(uid);
            accountEdit.setText(account);
            passwordEdit.setText(password);
        }
        if (addflg != null) {
            maddflg = addflg;
        }
    }

}

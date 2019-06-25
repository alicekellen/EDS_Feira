package com.engenharia.feiraapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.service.DBCommandsUser;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mRoot;
    private EditText mEdtLogin;
    private EditText mEdtPassword;
    private Button mBtnLogin;
    private Button mBtnNewUser;
    private Button mBtnNewPassword;
    private DBCommandsUser commandsUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commandsUser = new DBCommandsUser(this);
        initUI();
        configureListener();
    }

    private void initUI() {
        mRoot = (LinearLayout) findViewById(R.id.login_root);
        mEdtLogin = (EditText) findViewById(R.id.edt_login);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnNewUser = (Button) findViewById(R.id.btn_new_user);
        mBtnNewPassword = (Button) findViewById(R.id.btn_new_password);
    }

    private void configureListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyFields()){
                    long user = commandsUser.login(mEdtLogin.getText().toString(), mEdtPassword.getText().toString());
                    if(user != -1){
                        FeiraApplication.getInstance().setUserSession(user);
                        Intent intent = new Intent(MainActivity.this, StockActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Não foi possível realizar o ligin.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mBtnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });

        mBtnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nova senha
            }
        });
    }

    private boolean verifyFields() {
        if(mEdtLogin.getText().toString().equals("") || mEdtPassword.getText().toString().equals("")){
            Toast.makeText(this, "Login e Senha são obrigatórios.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

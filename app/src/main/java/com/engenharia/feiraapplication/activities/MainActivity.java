package com.engenharia.feiraapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.model.User;
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
        configFields();
    }

    private void configFields() {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String regexFilter = "[a-zA-Z0-9]*";
                if (!source.toString().matches(regexFilter)) {
                    return "";
                }
                return null;
            }
        };
        mEdtLogin.setFilters(filterArray);
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
                if(!mEdtLogin.getText().toString().equals("")){
                    User userTemp = commandsUser.selectUser(mEdtLogin.getText().toString());
                    if(userTemp != null){
                        if(verifyExistUserPwd(mEdtLogin.getText().toString())){
                            if (verifyFields()) {
                                long user = commandsUser.login(mEdtLogin.getText().toString(), mEdtPassword.getText().toString());
                                if (user != -1) {
                                    FeiraApplication.getInstance().setUserSession(user);
                                    FeiraApplication.getInstance().setRecoverPassword(false);
                                    Intent intent = new Intent(MainActivity.this, StockActivity.class);
                                    MainActivity.this.startActivity(intent);
                                    clearFields();
                                } else {
                                    Toast.makeText(MainActivity.this, "Não foi possível realizar o ligin.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else if(!mEdtPassword.getText().toString().equals("")){
                            Toast.makeText(MainActivity.this, "Não foi possível realizar o ligin.", Toast.LENGTH_SHORT).show();
                        }else{
                            //Obriga a redefinir uma senha
                            FeiraApplication.getInstance().setUserSession(userTemp.getId());
                            FeiraApplication.getInstance().setRecoverPassword(true);
                            Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                            startActivity(intent);
                            clearFields();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Não foi possível realizar o ligin.", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }
                Toast.makeText(MainActivity.this, "Não foi possível realizar o ligin.", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeiraApplication.getInstance().setRecoverPassword(false);
                Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(intent);
                clearFields();
            }
        });

        mBtnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeiraApplication.getInstance().setRecoverPassword(false);
                Intent intent = new Intent(MainActivity.this, NewPasswordActivity.class);
                startActivity(intent);
                clearFields();
            }
        });
    }

    private boolean verifyExistUserPwd(String user) {
        User userTemp = commandsUser.selectUser(user);
        if(userTemp.getPassword() != null && !userTemp.getPassword().equals("")){
            return true;
        }
        return false;
    }

    private void clearFields() {
        mEdtLogin.setText("");
        mEdtPassword.setText("");
    }

    private boolean verifyFields() {
        if (mEdtLogin.getText().toString().equals("") || mEdtPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Login e Senha são obrigatórios.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

package com.engenharia.feiraapplication.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.model.User;
import com.engenharia.feiraapplication.service.DBCommandsUser;

public class NewUserActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private Button mBtnRegister;
    private DBCommandsUser commandsUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        commandsUser = new DBCommandsUser(this);
        initUI();
        configureListener();
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
    }

    private void configureListener() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyFields()){
                    if(commandsUser.insert(getUser()) > 0){
                        finish();
                    }else{
                        Toast.makeText(NewUserActivity.this, "Não foi possível realizar o cadastro.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private User getUser() {
        User user = new User();
        user.setName(mEdtName.getText().toString());
        user.setPassword(mEdtPassword.getText().toString());
        return user;
    }

    private boolean verifyFields() {
        if(mEdtName.getText().toString().equals("") || mEdtPassword.getText().toString().equals("") || mEdtConfirmPassword.getText().toString().equals("")){
            Toast.makeText(NewUserActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!mEdtPassword.getText().toString().equals(mEdtConfirmPassword.getText().toString())){
            Toast.makeText(NewUserActivity.this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
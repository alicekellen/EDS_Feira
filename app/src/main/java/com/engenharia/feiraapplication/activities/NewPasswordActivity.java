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
import com.engenharia.feiraapplication.service.DBCommandsUser;

public class NewPasswordActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private Button mBtnNewPassword;
    private DBCommandsUser commandsUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        commandsUser = new DBCommandsUser(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUI();
        configureListener();
    }

    private void configureListener() {
        mBtnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEdtName.getText().equals("") || mEdtPassword.getText().equals("") || mEdtConfirmPassword.getText().equals("")){
                    Toast.makeText(NewPasswordActivity.this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show();
                }else{
                    long result = commandsUser.changePassword(mEdtName.getText().toString(), mEdtPassword.getText().toString());
                    if (result > 0){
                        finish();
                    }else{
                        Toast.makeText(NewPasswordActivity.this, "Não foi possível redefinir a senha", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initUI() {
        mEdtName = (EditText) findViewById(R.id.edt_name_new_password);
        mEdtPassword = (EditText) findViewById(R.id.edt_new_password);
        mEdtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        mBtnNewPassword = (Button) findViewById(R.id.btn_new_password);
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

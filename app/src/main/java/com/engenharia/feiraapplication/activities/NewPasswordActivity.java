package com.engenharia.feiraapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.service.DBCommandsUser;

public class NewPasswordActivity extends AppCompatActivity {

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private TextView mTxvNewPassword;
    private TextView mTxvConfirmNewPwd;
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
        if (FeiraApplication.getInstance().getUserSession() != 0) {
            mTxvNewPassword.setVisibility(View.VISIBLE);
            mEdtPassword.setVisibility(View.VISIBLE);
            mTxvConfirmNewPwd.setVisibility(View.VISIBLE);
            mEdtConfirmPassword.setVisibility(View.VISIBLE);
            mBtnNewPassword.setText(getString(R.string.new_pwd));
        }else{
            mBtnNewPassword.setText(getString(R.string.text_button_new_pwd));
        }
    }

    private void configureListener() {
        mBtnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FeiraApplication.getInstance().getUserSession() != 0 && mEdtEmail.getText().equals("")) {
                    Toast.makeText(NewPasswordActivity.this, "O campo e-mail é obrigatório.", Toast.LENGTH_SHORT).show();
                }else if (mEdtEmail.getText().equals("") || mEdtPassword.getText().equals("") || mEdtConfirmPassword.getText().equals("")) {
                    Toast.makeText(NewPasswordActivity.this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show();
                } else {
                    long result = commandsUser.changePassword(mEdtEmail.getText().toString(), mEdtPassword.getText().toString());
                    if (result > 0) {
                        if (FeiraApplication.getInstance().getUserSession() != 0){
                            Toast.makeText(NewPasswordActivity.this, "Senha redefinida com sucesso.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(NewPasswordActivity.this, "Senha recuperada com sucesso.", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else {
                        Toast.makeText(NewPasswordActivity.this, "Não foi possível redefinir a senha", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initUI() {
        mEdtEmail = (EditText) findViewById(R.id.edt_email_new_password);
        mEdtPassword = (EditText) findViewById(R.id.edt_new_password);
        mEdtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        mBtnNewPassword = (Button) findViewById(R.id.btn_new_password);
        mTxvNewPassword = (TextView) findViewById(R.id.txv_new_pwd);
        mTxvConfirmNewPwd = (TextView) findViewById(R.id.txv_confirm_new_pwd);
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

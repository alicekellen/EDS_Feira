package com.engenharia.feiraapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.model.User;
import com.engenharia.feiraapplication.service.DBCommandsUser;

public class NewUserActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtPassword;
    private TextView mTxvConfirmPassword;
    private EditText mEdtConfirmPassword;
    private EditText mEdtEmail;
    private Button mBtnRegister;
    private Button mBtnDelete;
    private DBCommandsUser commandsUser;
    private boolean recoverPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        commandsUser = new DBCommandsUser(this);
        initUI();
        configureListener();

        recoverPassword = FeiraApplication.getInstance().isRecoverPassword();

        long userId = FeiraApplication.getInstance().getUserSession();
        if (FeiraApplication.getInstance().getUserSession() != 0) {
            User user = commandsUser.selectUserById(userId);
            mEdtName.setText(user.getName());
            mEdtEmail.setText(user.getEmail());
            if(recoverPassword){
                mBtnRegister.setText("Atualizar Senha");
                mBtnDelete.setVisibility(View.GONE);
            }else{
                mBtnRegister.setText("Atualizar");
                mBtnDelete.setVisibility(View.VISIBLE);
            }
            mTxvConfirmPassword.setVisibility(View.GONE);
            mEdtConfirmPassword.setVisibility(View.GONE);
        }
        configFields();
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mTxvConfirmPassword = (TextView) findViewById(R.id.txv_confirm_password);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnDelete = (Button) findViewById(R.id.btn_delete);
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
        mEdtName.setFilters(filterArray);
    }

    private void configureListener() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyFields()) {
                    if (FeiraApplication.getInstance().getUserSession() != 0) {
                        User user = getUser();
                        user.setId(FeiraApplication.getInstance().getUserSession());
                        if (commandsUser.update(user) > 0) {
                            Toast.makeText(NewUserActivity.this, "Usuário atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(NewUserActivity.this, "Não foi possível atualizar os dados.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (commandsUser.insert(getUser()) > 0) {
                        Toast.makeText(NewUserActivity.this, "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NewUserActivity.this, "Não foi possível realizar o cadastro.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = getUser();
                user.setId(FeiraApplication.getInstance().getUserSession());
                if (commandsUser.delete(user) > 0) {
                    setResult(RESULT_OK);
                    FeiraApplication.getInstance().setUserSession(0);
                    finish();
                } else {
                    Toast.makeText(NewUserActivity.this, "Não foi possível excluir o cadastro.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private User getUser() {
        User user = new User();
        user.setName(mEdtName.getText().toString());
        user.setPassword(mEdtPassword.getText().toString());
        user.setEmail(mEdtEmail.getText().toString());
        return user;
    }

    private boolean verifyFields() {
        if (FeiraApplication.getInstance().getUserSession() != 0) {
            if (mEdtName.getText().toString().equals("") || mEdtPassword.getText().toString().equals("") || mEdtEmail.getText().toString().equals("")) {
                Toast.makeText(NewUserActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (mEdtName.getText().toString().equals("") || mEdtPassword.getText().toString().equals("") || mEdtConfirmPassword.getText().toString().equals("") || mEdtEmail.getText().toString().equals("")) {
                Toast.makeText(NewUserActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!mEdtPassword.getText().toString().equals(mEdtConfirmPassword.getText().toString())) {
                Toast.makeText(NewUserActivity.this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.out:
                FeiraApplication.getInstance().setUserSession(0);
                this.finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

package com.engenharia.feiraapplication.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.model.Product;
import com.engenharia.feiraapplication.model.User;
import com.engenharia.feiraapplication.service.DBCommandsStock;

import java.util.Date;

public class NewProductActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtMarketplace;
    private EditText mEdtQuantity;
    private EditText mEdtPrice;
    private Button mBtnRegisterProduct;
    private DBCommandsStock commandsStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        commandsStock = new DBCommandsStock(this);
        initUI();
        configureListener();
    }

    private void configureListener() {
        mBtnRegisterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyFields()){
                    long result = commandsStock.insert(getProduct());
                    if(result > 0){
                        finish();
                    }else{
                        Toast.makeText(NewProductActivity.this, "Não foi possível cadastrar o produto.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Product getProduct() {
        Product product = new Product();
        product.setName(mEdtName.getText().toString());
        product.setMarketplace(mEdtMarketplace.getText().toString());
        Date date = new Date();
        product.setDate(date.toString());
        product.setQuantity(Integer.parseInt(mEdtQuantity.getText().toString()));
        product.setPrice(mEdtPrice.getText().toString());
        product.setIdUser(FeiraApplication.getInstance().getUserSession());

        return product;
    }

    private boolean verifyFields() {
        if(mEdtName.getText().toString().equals("")
        || mEdtMarketplace.getText().toString().equals("")
        || mEdtQuantity.getText().toString().equals("")
        || mEdtPrice.getText().toString().equals("")){
            Toast.makeText(NewProductActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEdtName = (EditText) findViewById(R.id.edt_name_product);
        mEdtMarketplace = (EditText) findViewById(R.id.edt_marketplace_product);
        mEdtQuantity = (EditText) findViewById(R.id.edt_quantity_product);
        mEdtPrice = (EditText) findViewById(R.id.edt_price_product);
        mBtnRegisterProduct = (Button) findViewById(R.id.btn_register_product);
    }
}

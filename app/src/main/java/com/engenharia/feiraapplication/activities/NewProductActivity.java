package com.engenharia.feiraapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.model.Product;
import com.engenharia.feiraapplication.service.DBCommandsStock;

import java.util.Date;

public class NewProductActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtMarketplace;
    private EditText mEdtQuantity;
    private EditText mEdtPrice;
    private Button mBtnRegisterProduct;
    private Button mBtnDeleteProduct;
    private DBCommandsStock commandsStock;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        commandsStock = new DBCommandsStock(this);
        initUI();
        configureListener();
        getExtras();
        configureLayout();
    }

    private void configureLayout() {
        mBtnDeleteProduct.setVisibility(View.GONE);
        if (product != null) {
            mEdtName.setText(product.getName());
            mEdtMarketplace.setText(product.getMarketplace());
            mEdtQuantity.setText(String.valueOf(product.getQuantity()));
            mEdtPrice.setText(product.getPrice());
            mBtnRegisterProduct.setText("Atualizar dados");
            mBtnDeleteProduct.setVisibility(View.VISIBLE);
        }
    }

    private void getExtras() {
        product = (Product) getIntent().getExtras().getSerializable("product");
    }

    private void configureListener() {
        mBtnRegisterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyFields()) {
                    if (product != null) {
                        product.setName(mEdtName.getText().toString());
                        product.setMarketplace(mEdtMarketplace.getText().toString());
                        product.setQuantity(Integer.parseInt(mEdtQuantity.getText().toString()));
                        product.setPrice(mEdtPrice.getText().toString());
                        long result = commandsStock.update(product);
                        if (result > 0) {
                            finish();
                        } else {
                            Toast.makeText(NewProductActivity.this, "Não foi possível atualizar o produto.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        long result = commandsStock.insert(getProduct());
                        if (result > 0) {
                            if (product != null){
                                Toast.makeText(NewProductActivity.this, "Produto atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(NewProductActivity.this, "Produto cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } else {
                            Toast.makeText(NewProductActivity.this, "Não foi possível cadastrar o produto.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        mBtnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyFields()) {
                    long result = commandsStock.delete(product);
                    if (result > 0) {
                        finish();
                    } else {
                        Toast.makeText(NewProductActivity.this, "Não foi possível excluir o produto.", Toast.LENGTH_SHORT).show();
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
        if (mEdtName.getText().toString().equals("")
                || mEdtMarketplace.getText().toString().equals("")
                || mEdtQuantity.getText().toString().equals("")
                || mEdtPrice.getText().toString().equals("")) {
            Toast.makeText(NewProductActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEdtName = (EditText) findViewById(R.id.edt_name_product);
        mEdtMarketplace = (EditText) findViewById(R.id.edt_marketplace_product);
        mEdtQuantity = (EditText) findViewById(R.id.edt_quantity_product);
        mEdtPrice = (EditText) findViewById(R.id.edt_price_product);
        mBtnRegisterProduct = (Button) findViewById(R.id.btn_register_product);
        mBtnDeleteProduct = (Button) findViewById(R.id.btn_delete_product);
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

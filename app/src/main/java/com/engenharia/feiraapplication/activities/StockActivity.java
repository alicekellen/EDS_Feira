package com.engenharia.feiraapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.adapter.StockAdapter;
import com.engenharia.feiraapplication.model.Product;
import com.engenharia.feiraapplication.service.DBCommandsStock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    private ListView mLvStock;
    private List<Product> stock;
    private List<Product> listFilter;
    private DBCommandsStock commandsStock;
    private EditText mEdtFilter;
    private Button mBtnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        commandsStock = new DBCommandsStock(this);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configureStock();
    }

    private void configureStock() {
        stock = new ArrayList<>();
        if (listFilter == null || listFilter.isEmpty()) {
            stock = commandsStock.selectAll(FeiraApplication.getInstance().getUserSession());

        }else{
            stock = listFilter;
        }
        if (!stock.isEmpty()){
            StockAdapter adapter = new StockAdapter(stock, this);
            mLvStock.setAdapter(adapter);
            listFilter = new ArrayList<>();
        }
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLvStock = (ListView) findViewById(R.id.lv_stock);
        mEdtFilter = (EditText) findViewById(R.id.edt_filter);
        mBtnFilter = (Button) findViewById(R.id.btn_filter);

        mBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stock.isEmpty()){
                    for (Product p : stock){
                        if(p.getName().toLowerCase().contains(mEdtFilter.getText().toString().toLowerCase())){
                            listFilter.add(p);
                        }
                    }
                    configureStock();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(StockActivity.this, NewProductActivity.class);
                intent.putExtra("product", (Serializable) null);
                startActivityForResult(intent, 1);
                return true;
            case android.R.id.home:
                Intent intent1 = new Intent(this, NewUserActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            finish();
        }
    }
}

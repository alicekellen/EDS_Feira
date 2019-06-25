package com.engenharia.feiraapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.engenharia.feiraapplication.FeiraApplication;
import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.model.Product;
import com.engenharia.feiraapplication.service.DBCommandsStock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    private ListView mLvStock;
    private List<Product> stock;
    private DBCommandsStock commandsStock;

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
        stock = commandsStock.selectAll(FeiraApplication.getInstance().getUserSession());
        if (!stock.isEmpty()){
            ArrayAdapter<Product> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stock);
            mLvStock.setAdapter(adapter);
        }
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLvStock = (ListView) findViewById(R.id.lv_stock);
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
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

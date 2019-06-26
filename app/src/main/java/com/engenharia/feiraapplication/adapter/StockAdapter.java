package com.engenharia.feiraapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.engenharia.feiraapplication.R;
import com.engenharia.feiraapplication.activities.NewProductActivity;
import com.engenharia.feiraapplication.model.Product;

import java.io.Serializable;
import java.util.List;

public class StockAdapter extends BaseAdapter {

    private final List<Product> stock;
    private final Activity act;

    public StockAdapter(List<Product> stock, Activity act) {
        this.stock = stock;
        this.act = act;
    }

    @Override
    public int getCount() {
        return stock.size();
    }

    @Override
    public Object getItem(int position) {
        return stock.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.product_item, parent, false);
        TextView name = (TextView) view.findViewById(R.id.txv_name);
        TextView quantity = (TextView) view.findViewById(R.id.txv_quantity);
        Product item = stock.get(position);

        name.setText(item.getName());
        quantity.setText(String.valueOf(item.getQuantity()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act, NewProductActivity.class);
                intent.putExtra("product", (Serializable) stock.get(position));
                act.startActivity(intent);
            }
        });

        return view;
    }
}

package com.engenharia.feiraapplication.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.engenharia.feiraapplication.model.Product;
import com.engenharia.feiraapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBCommandsStock {

    private SQLiteDatabase db;

    public DBCommandsStock(Context context){
        FeiraCore auxDB = new FeiraCore(context);
        db = auxDB.getWritableDatabase();
    }

    public long insert(Product product){

        ContentValues valores = new ContentValues();
        valores.put("name", product.getName());
        valores.put("marketplace", product.getMarketplace());
        valores.put("date", product.getDate());
        valores.put("quantity", product.getQuantity());
        valores.put("price", product.getPrice());
        valores.put("idUser", product.getIdUser());
        try{
            long result = db.insert("product", null, valores);
            return result;
        }catch (Exception e){
            return -1;
        }
    }

    public long update(Product product){
        ContentValues valores = new ContentValues();
        valores.put("name", product.getName());
        valores.put("marketplace", product.getMarketplace());
        valores.put("date", product.getDate());
        valores.put("quantity", product.getQuantity());
        valores.put("price", product.getPrice());
        try{
            long result = db.update("product", valores, " name = ? AND marketplace = ? AND date = ? AND quantity = ? AND price = ? ",
                    new String[]{product.getName(), product.getMarketplace(), product.getDate(), String.valueOf(product.getQuantity()), product.getPrice()});

            return result;
        }catch (Exception e){
            return -1;
        }
    }

    public long changePassword(String email, String password){
        User validation = selectUser(email);
        if(validation == null){
            return 0;
        }else{
            ContentValues valores = new ContentValues();
            valores.put("password", password);
            try{
                long result = db.update("user", valores, " email = ?", new String[]{email});
                return result;
            }catch (Exception e){
                return -1;
            }
        }
    }

    public long delete(Product product){
        return db.delete("product", " _id = ?", new String[]{String.valueOf(product.getId())});
    }

    public List<Product> selectAll(long idUser){
        List<Product> stock = new ArrayList<Product>();
        List<Product> result = new ArrayList<Product>();

        String[] colunas = new String[]{"_id", "name", "barcode", "price", "date", "marketplace", "quantity", "idUser"};
        Cursor cursor = db.query("product", colunas, null, null, null, null, "name ASC");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                Product product = new Product();
                product.setId(cursor.getLong(0));
                product.setName(cursor.getString(1));
                product.setBarcode(cursor.getInt(2));
                product.setPrice(cursor.getString(3));
                product.setDate(cursor.getString(4));
                product.setMarketplace(cursor.getString(5));
                product.setQuantity(cursor.getInt(6));
                product.setIdUser(cursor.getInt(7));
                stock.add(product);
            }while (cursor.moveToNext());
        }
        if (!stock.isEmpty()){
            for(Product p : stock){
                if(p.getIdUser() == idUser){
                    result.add(p);
                }
            }
        }
        return result;
    }

    public User selectUser(String name){
        String[] colunas = new String[]{"_id", "name", "password"};
        Cursor cursor = db.query("user", colunas, " email = ?", new String[]{name}, null, null, null);
        if(cursor != null && cursor.getCount() != 0){
            User user = new User();
            cursor.moveToFirst();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            return user;
        }
        return null;
    }

    public boolean login(String login, String password){
        String[] colunas = new String[]{"_id", "name", "password"};
        Cursor cursor = db.query("user", colunas, " name = ?", new String[]{login}, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() != 0){
            return true;
        }
        return false;
    }
}

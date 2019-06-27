package com.engenharia.feiraapplication.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.engenharia.feiraapplication.model.Product;
import com.engenharia.feiraapplication.model.User;

public class DBCommandsUser {

    private SQLiteDatabase db;

    public DBCommandsUser(Context context){
        FeiraCore auxDB = new FeiraCore(context);
        db = auxDB.getWritableDatabase();
    }

    public long insert(User user){
        User validation = selectUser(user.getName());
        if(validation != null){
            return 0;
        }else{
            ContentValues valores = new ContentValues();
            valores.put("name", user.getName());
            valores.put("password", user.getPassword());
            try{
                long result = db.insert("user", null, valores);
                return result;
            }catch (Exception e){
                return -1;
            }
        }
    }

    public long update(User user){
        ContentValues valores = new ContentValues();
        valores.put("name", user.getName());
        valores.put("password", user.getPassword());
        try{
            long result = db.update("user", valores, " _id = ? ",
                    new String[]{String.valueOf(user.getId())});

            return result;
        }catch (Exception e){
            return -1;
        }
    }

    public long delete(User user){
        return db.delete("user", " _id = ?", new String[]{String.valueOf(user.getId())});
    }

    public long changePassword(String name, String password){
        User validation = selectUser(name);
        if(validation == null){
            return 0;
        }else{
            ContentValues valores = new ContentValues();
            valores.put("password", password);
            try{
                long result = db.update("user", valores, " name = ?", new String[]{name});
                return result;
            }catch (Exception e){
                return -1;
            }
        }
    }

    public User selectUser(String name){
        String[] colunas = new String[]{"_id", "name", "password"};
        Cursor cursor = db.query("user", colunas, " name = ?", new String[]{name}, null, null, null);
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

    public User selectUserById(long id){
        String[] colunas = new String[]{"_id", "name", "password"};
        Cursor cursor = db.query("user", colunas, " _id = ?", new String[]{String.valueOf(id)}, null, null, null);
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

    public long login(String login, String password){
        String[] colunas = new String[]{"_id", "name", "password"};
        Cursor cursor = db.query("user", colunas, " name = ? AND password = ?", new String[]{login, password}, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() != 0){
            return cursor.getLong(0);
        }
        return -1;
    }
}

package com.engenharia.feiraapplication.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.engenharia.feiraapplication.model.User;

import java.util.ArrayList;
import java.util.List;

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

    public void selectAll(){
        List<User> users = new ArrayList<User>();
        String[] colunas = new String[]{"_id", "name", "password"};
        Cursor cursor = db.query("user", colunas, null, null, null, null, "name ASC");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                User user = new User();
                user.setId(cursor.getLong(0));
                user.setName(cursor.getString(1));
                user.setPassword(cursor.getString(4));
                users.add(user);
            }while (cursor.moveToNext());
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

    public long login(String login, String password){
        String[] colunas = new String[]{"_id", "name", "password"};
        Cursor cursor = db.query("user", colunas, " name = ?", new String[]{login}, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() != 0){
            return cursor.getLong(0);
        }
        return -1;
    }
}

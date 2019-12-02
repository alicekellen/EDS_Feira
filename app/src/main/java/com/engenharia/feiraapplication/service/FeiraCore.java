package com.engenharia.feiraapplication.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeiraCore extends SQLiteOpenHelper {

    private static final String NOME_BD = "ada";
    private static final int VERSAO_BD = 2;


    public FeiraCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(_id integer primary key autoincrement, name text not null, password text not null, email text not null);");
        db.execSQL("create table product(_id integer primary key autoincrement, name text not null, barcode integer, price text not null, date text not null, marketplace text not null, quantity integer not null, idUser integer not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table user;");
        db.execSQL("drop table product;");
        onCreate(db);
    }
}

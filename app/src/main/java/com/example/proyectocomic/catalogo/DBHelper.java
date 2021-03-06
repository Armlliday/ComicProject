package com.example.proyectocomic.catalogo;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "datosDB";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE tabla (nombre TEXT NOT NULL,dibujante TEXT NOT NULL,escritor TEXT NOT NULL,tomo TEXT NOT NULL,categorias TEXT NOT NULL)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.execute();
        //En tomo va el nombre del tomo, y en categorias van separadas de un espacio las categorias.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tabla");
    }
}

package com.example.amfgps.tmpdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Table Name
    public static final String TABLE_NAME = "client";
    public static final String TABLE_ARTTALLAS_DP = "articulotallasdp";

    // table columns TABLE_ARTTALLAS_DP
    public static final String ARTL_ID = "id";
    public static final String ARTL_IDTALLA = "idtalla";
    public static final String ARTL_CANTIDAD = "cantidad";
    public static final String ARTL_EXISTENCIA = "existencia";
    public static final String ARTL_DESCRIP_TALLA = "decriptall";
    public static final String ARTL_DPFK = "dpfk";
    public static final String ARTL_ESTADO = "estado";
    // Table columns TABLE_NAME
    public static final String _ID = "_id";
    public static final String NAME = "usuarioc";
    public static final String PASSWD = "clavec";
    public static final String COMPANY = "empresac";

    // Database Information
    static final String DB_NAME = "SilverPedidos.DB";

    // database version
    static final int DB_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + PASSWD + " TEXT, " + COMPANY + " TEXT);";
}

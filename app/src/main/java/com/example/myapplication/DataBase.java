package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    public static String DATABASE_NAME;

    public static String COLUMN_ARTICLE_WORD_1 = "ARTICLE_WORD_1";
    public static String COLUMN_WORD_1 = "WORD_1";
    public static String COLUMN_WORD_1_STORED_AT = "WORD_1_STORED_AT";
    public static String COLUMN_ARTICLE_WORD_2 = "ARTICLE_WORD_2";
    public static String COLUMN_WORD_2 = "WORD_2";
    public static String COLUMN_WORD_2_STORED_AT = "WORD_2_STORED_AT";

    public static String COLUMN_COUNT = "COUNT";
    public static String COLUMN_SUCCESS = "SUCCESS";
    public static String COLUMN_PERCENTAGE = "PERCENTAGE";

    public static String COLUMN_CATEGORY = "CATEGORY";
    public static String COLUMN_DATE = "DATE";
    public static String COLUMN_BOOKMARKED = "BOOKMARKED";


    public DataBase(@Nullable Context context, String name) {
        super(context, name, null, 1);
        DATABASE_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + DATABASE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ARTICLE_WORD_1 + " TEXT, " +
                COLUMN_WORD_1 + " TEXT, " +
                COLUMN_WORD_1_STORED_AT + " TEXT, " +
                COLUMN_ARTICLE_WORD_2 + " TEXT, " +
                COLUMN_WORD_2 + " TEXT, " +
                COLUMN_WORD_2_STORED_AT +  " TEXT, " +
                COLUMN_COUNT + " INTEGER, " +
                COLUMN_SUCCESS +  " INTEGER, " +
                COLUMN_PERCENTAGE +  " REAL, " +
                COLUMN_CATEGORY +  " TEXT, " +
                COLUMN_DATE +  " TEXT, " +
                COLUMN_BOOKMARKED + " INTEGER)" ;

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean replaceWord(WordItem wi, String pv1, String pv2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        cv.put(COLUMN_ARTICLE_WORD_1, wi.getArticleWord1());
        cv.put(COLUMN_WORD_1, wi.getWordLanguage1());
        cv.put(COLUMN_WORD_1_STORED_AT, wi.getWord1StoredAt());
        cv.put(COLUMN_ARTICLE_WORD_2, wi.getArticleWord2());
        cv.put(COLUMN_WORD_2, wi.getWordLanguage2());
        cv.put(COLUMN_WORD_2_STORED_AT, wi.getWord2StoredAt());
        cv.put(COLUMN_COUNT, 0);
        cv.put(COLUMN_SUCCESS, 0);
        cv.put(COLUMN_PERCENTAGE, 0);
        cv.put(COLUMN_CATEGORY, wi.getCategory());
        cv.put(COLUMN_DATE, dateFormat.format(date));
        cv.put(COLUMN_BOOKMARKED, wi.getBookmarked());

        long insert = db.update(DATABASE_NAME,cv,COLUMN_WORD_1 + " = ? AND " + COLUMN_WORD_2 + " = ?",new String[]{pv1, pv2});
        //long insert =  db.insert(DATABASE_NAME, null, cv);

        if(insert < 0 )
            return true;
        else{
            return false;
        }
    }

    public boolean addOne(WordItem wi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        cv.put(COLUMN_ARTICLE_WORD_1, wi.getArticleWord1());
        cv.put(COLUMN_WORD_1, wi.getWordLanguage1());
        cv.put(COLUMN_WORD_1_STORED_AT, wi.getWord1StoredAt());
        cv.put(COLUMN_ARTICLE_WORD_2, wi.getArticleWord2());
        cv.put(COLUMN_WORD_2, wi.getWordLanguage2());
        cv.put(COLUMN_WORD_2_STORED_AT, wi.getWord2StoredAt());
        cv.put(COLUMN_COUNT, 0);
        cv.put(COLUMN_SUCCESS, 0);
        cv.put(COLUMN_PERCENTAGE, 0);
        cv.put(COLUMN_CATEGORY, wi.getCategory());
        cv.put(COLUMN_DATE, dateFormat.format(date));
        cv.put(COLUMN_BOOKMARKED, wi.getBookmarked());

        long insert =  db.insert(DATABASE_NAME, null, cv);

        if(insert < 0 )
            return true;
        else{
            return false;
        }
    }

    public void deleteWord(String pw1, String pw2){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_NAME, COLUMN_WORD_1 + " = ? AND " + COLUMN_WORD_2 + " = ?",new String[]{pw1, pw2});
    }

    public List<WordItem> getAllWords(Integer mode, String cdt1){
        List<WordItem> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + DATABASE_NAME;
        switch (mode){
            //Insert Oder
            case 1:
                queryString = "SELECT * FROM " + DATABASE_NAME;
                break;

            //alphabetical language 1
            case 2:
                queryString = "SELECT * FROM " + DATABASE_NAME + " ORDER BY " + COLUMN_WORD_1 + " COLLATE UNICODE ASC";
                break;

            //alphabetical language 2
            case 3:
                queryString = "SELECT * FROM " + DATABASE_NAME + " ORDER BY " + COLUMN_WORD_2 + " COLLATE UNICODE ASC";
                break;

            //UNalphabetical language 1
            case 4:
                queryString = "SELECT * FROM " + DATABASE_NAME + " ORDER BY " + COLUMN_WORD_1 + " COLLATE UNICODE DESC";
                break;

            //UNalphabetical language 2
            case 5:
                queryString = "SELECT * FROM " + DATABASE_NAME + " ORDER BY " + COLUMN_WORD_2 + " COLLATE UNICODE DESC";
                break;

            //random All
            case 6:
                queryString = "SELECT * FROM " + DATABASE_NAME + " ORDER BY RANDOM()";
                break;

            //random specific letter language1
            case 7:
                queryString = "SELECT * FROM " + DATABASE_NAME + " WHERE " + COLUMN_WORD_1_STORED_AT + " = " + "'" + cdt1 + "'" + " COLLATE NOCASE" + " ORDER BY RANDOM()";
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            //loop through the results:
            do{
                Integer wordID = cursor.getInt(0);
                String articleWord1 = cursor.getString(1);
                String word1 = cursor.getString(2);
                String word1StoredAt = cursor.getString(3);
                String articleWord2 = cursor.getString(4);
                String word2 = cursor.getString(5);
                String word2StoredAt = cursor.getString(6);
                Integer count = cursor.getInt(7);
                Integer success = cursor.getInt(8);
                Float percentage = cursor.getFloat(9);
                String category = cursor.getString(10);
                String date = cursor.getString(11);
                Integer bookmarked = cursor.getInt(12);

                WordItem wi = new WordItem(articleWord1, word1, word1StoredAt, articleWord2, word2, word2StoredAt, count, success, percentage, category, date, bookmarked);

                returnList.add(wi);

            }while(cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return returnList;
    }
}

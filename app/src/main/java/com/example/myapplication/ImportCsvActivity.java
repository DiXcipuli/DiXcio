package com.example.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImportCsvActivity extends AppCompatActivity {

    Button importCsvButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_csv);

        importCsvButton = (Button)findViewById(R.id.importCsvButton);
        importCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String downloadFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                Toast.makeText(getApplicationContext(), downloadFolder, Toast.LENGTH_LONG).show();
                FileReader file = null;
                try {
                    file = new FileReader(downloadFolder + File.separator + "DiXcio.csv");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_LONG).show();
                }
                BufferedReader br = new BufferedReader(file);
                String line = "";
                String tableName = MainActivity.currentProjectName;
                String columns = DataBase.COLUMN_ARTICLE_WORD_1 + ", " +
                                    DataBase.COLUMN_WORD_1 + ", " +
                                    DataBase.COLUMN_WORD_1_STORED_AT + ", " +
                                    DataBase.COLUMN_ARTICLE_WORD_2 + ", " +
                                    DataBase.COLUMN_WORD_2 + ", " +
                                    DataBase.COLUMN_WORD_2_STORED_AT + ", " +
                                    DataBase.COLUMN_COUNT + ", " +
                                    DataBase.COLUMN_SUCCESS + ", " +
                                    DataBase.COLUMN_PERCENTAGE + ", " +
                                    DataBase.COLUMN_CATEGORY + ", " +
                                    DataBase.COLUMN_DATE;
                String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
                String str2 = ");";

                SQLiteDatabase dataBase = new DataBase(getApplicationContext(), MainActivity.currentProjectName).getWritableDatabase();
                dataBase.beginTransaction();

                while(true){
                    try {
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");

                    //Check if definitions are not empty
                    if(str[1].length() != 0 && str[3].length() != 0){
                        String word1StoredAt = Character.toString(str[1].charAt(0));
                        String word2StoredAt = Character.toString(str[3].charAt(0));;

                        if(!WordDefinitionActivity.map.containsKey(word1StoredAt)){
                            word1StoredAt = "Other";
                        }
                        if(!WordDefinitionActivity.map.containsKey(word2StoredAt)){
                            word2StoredAt = "Other";
                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();

                        for(int i = 0; i < str.length; i++){
                            if(str[i].contains("'")){
                                str[i] = str[i].replace("'","''");
                            }
                        }


                        sb.append("'" + str[0] + "', '");
                        sb.append(str[1] + "', '");
                        sb.append(word1StoredAt + "', '");
                        sb.append(str[2] + "', '");
                        sb.append(str[3] + "', '");
                        sb.append(word2StoredAt + "', '");
                        sb.append("0', '");
                        sb.append("0', '");
                        sb.append("0', '");
                        sb.append("None', '");
                        sb.append(dateFormat.format(date).toString() + "'");
                        sb.append(str2);
                        dataBase.execSQL(sb.toString());
                    }
                }
                dataBase.setTransactionSuccessful();
                dataBase.endTransaction();
            }
        });
    }
}
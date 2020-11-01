package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    private Integer formatSize = 2;

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.currentProjectName.isEmpty()){
            Toast.makeText(getApplicationContext(), "Reset Security", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_csv);

        importCsvButton = (Button)findViewById(R.id.importCsvButton);
        importCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processFile();
            }
        });
    }

    private void processFile(){
        FileChooser fileChooser = new FileChooser(ImportCsvActivity.this);

        fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(final File file) {
                // ....do something with the file
                String filename = file.getAbsolutePath();
                Log.i("File Name", filename);
                if(file.exists()) {
                    parseCsv(filename);
                }
                // then actually do something in another module
            }
        });
// Set up and filter my extension I am looking for
        fileChooser.setExtension("csv");
        fileChooser.showDialog();
    }

    private void parseCsv(String path){
        //String downloadFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        //Toast.makeText(getApplicationContext(), downloadFolder, Toast.LENGTH_LONG).show();
        FileReader file = null;
        FileReader file2 = null;
        try {
            //Try to find the file, otherwise, throw an error: file not found.
            //file = new FileReader(downloadFolder + File.separator + "DiXcio.csv");
            file = new FileReader(path);
            BufferedReader br = new BufferedReader(file);

            //Then we first check that the format is respected for every line, and that no comma has been ommited
            String currentLine;
            boolean isFormatRespected = true;
            Integer lineIndex = 0;
            while(true) {
                try {
                    if (!((currentLine = br.readLine()) != null)) break;
                    String[] currentStr = currentLine.split(";");
                    if(currentStr.length != formatSize){
                        isFormatRespected = false;
                        break;
                    }
                    lineIndex++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            br.close();

            if(isFormatRespected){
                String line = "";
                String tableName = MainActivity.currentProjectName;
                String columns = DataBase.COLUMN_WORD_1 + ", " +
                        DataBase.COLUMN_WORD_2 + ", " +
                        DataBase.COLUMN_COUNT + ", " +
                        DataBase.COLUMN_SUCCESS + ", " +
                        DataBase.COLUMN_PERCENTAGE + ", " +
                        DataBase.COLUMN_CATEGORY + ", " +
                        DataBase.COLUMN_DATE;
                String str1 = "INSERT INTO " + tableName + " (" + columns + ") values('";
                String str2 = ");";

                SQLiteDatabase dataBase = new DataBase(getApplicationContext(), MainActivity.currentProjectName).getWritableDatabase();
                dataBase.beginTransaction();

                try {
                    //Try to find the file, otherwise, throw an error: file not found.
                    //file2 = new FileReader(downloadFolder + File.separator + "DiXcio.csv");
                    file2 = new FileReader(path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader br2 = new BufferedReader(file2);
                while(true){
                    try {
                        if (!((line = br2.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(";");

                    for(int i = 0; i < str.length; i++){
                        if(str[i].contains("'")){
                            str[i] = str[i].replace("'","''");
                        }

                        while(str[i].length() != 0){
                            if(Character.toString(str[i].charAt(0)).equals(" "))
                                str[i] = str[i].substring(1);
                            else{
                                break;
                            }
                        }
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();

                    sb.append(str[0] + "', '");
                    sb.append(str[1] + "', '");
                    sb.append("0', '");
                    sb.append("0', '");
                    sb.append("0', '");
                    sb.append("None', '");
                    sb.append(dateFormat.format(date).toString() + "'");
                    sb.append(str2);
                    dataBase.execSQL(sb.toString());
                }
                dataBase.setTransactionSuccessful();
                dataBase.endTransaction();
                Toast.makeText(getApplicationContext(), Integer.toString(lineIndex) + " words have been added!", Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(getApplicationContext(), "Format error at line: " + Integer.toString(lineIndex + 1), Toast.LENGTH_LONG).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
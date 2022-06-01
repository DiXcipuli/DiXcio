package com.DiXcipuli.DiXcio;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectMenuActivity extends AppCompatActivity implements DeleteDialog.DeleteDialogListener{

    Button addWordsButton, browseWordsButton, trainButton, importCsvButton, exportCsvButton, deleteProjectButton, notesButton;
    TextView subTitleProjectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.currentProjectName == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_project_menu);

        MainActivity.resetToDefault();

        MainActivity.dataBase = new DataBase(ProjectMenuActivity.this,  MainActivity.currentProjectName);
        MainActivity.wordTrainList =  MainActivity.dataBase.getWords(2, null, null);
        MainActivity.wordBrowseList =  MainActivity.dataBase.getWords(3, null, null);

        addWordsButton = (Button)findViewById(R.id.addWordsButton);
        browseWordsButton = (Button)findViewById(R.id.browseButton);
        trainButton = (Button)findViewById(R.id.trainButton);
        subTitleProjectName = (TextView)findViewById(R.id.subTitleProjectName);
        subTitleProjectName.setText(MainActivity.currentProjectName);
        importCsvButton = (Button)findViewById(R.id.importCSVButtonMenu);
        exportCsvButton = (Button)findViewById(R.id.exportCSVButton);
        deleteProjectButton = (Button)findViewById(R.id.deleteProject);
        deleteProjectButton = (Button)findViewById(R.id.deleteProject);
        notesButton = (Button)findViewById(R.id.notesButton);


        deleteProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        importCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processFile();
            }
        });

        addWordsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openNewWordActivity();
            }
        });
        browseWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWords();
            }
        });
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainActivity.class);
                //intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        exportCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExportCsvActivity.class);
                startActivity(intent);
            }
        });

        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
                startActivity(intent);
            }
        });

    }

    public void openDialog(){
        DeleteDialog dialog = new DeleteDialog();
        dialog.show(getSupportFragmentManager(), "Delete Dialog");
    }

    @Override
    public void onYesClicked() {
        Integer index = 0;

        for (int i = 0; i < MainActivity.projectList.size(); i++) {
            if (MainActivity.projectList.get(i).getProjectName().equals(MainActivity.currentProjectName)) {
                MainActivity.layout.removeViewAt(i + 1);
                this.deleteDatabase(MainActivity.projectList.get(i).getProjectName());
                MainActivity.projectList.remove(i);
                break;
            }
        }

        File root = Environment.getExternalStorageDirectory();
        //File dir = new File(root.getAbsolutePath() + File.separator + R.string.app_name);
        File dir = new File(getApplicationInfo().dataDir);
        File file = new File(dir + File.separator + "DiXcioProjects.txt");
        file.delete();

        String message = "";
        for (int i = 0; i < MainActivity.projectList.size(); i++) {
            message = message + MainActivity.projectList.get(i).getProjectName() + "," + MainActivity.projectList.get(i).getLanguage1() + "," + MainActivity.projectList.get(i).getLanguage2() + "\n";
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(message.getBytes());
            fos.close();

            //Reset
            MainActivity.currentProjectName = "";
            MainActivity.currentLanguage1 = "";
            MainActivity.currentLanguage2 = "";

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Project deleted!", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void openNewWordActivity(){
        Intent intent = new Intent(this, WordDefinitionActivity.class);
        intent.putExtra("modifyMode", false);
        startActivity(intent);
    }

    public void loadWords(){
        Intent intent = new Intent(this, WordBrowserActivity.class);
        startActivity(intent);
    }

    private void processFile(){
        FileChooser fileChooser = new FileChooser(ProjectMenuActivity.this);

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
        // Extension I am looking for
        fileChooser.setExtension("csv");
        fileChooser.showDialog();
    }

    private void parseCsv(String path){
        FileReader file = null;
        FileReader file2 = null;
        Integer formatSize = 2;
        try {
            file = new FileReader(path);
            BufferedReader br = new BufferedReader(file);

            //Then we first check that the format is respected for every line, and that no comma has been omitted
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

            if(isFormatRespected){ //We will load the csv only if the previous check was fine, and the format respected
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
                    file2 = new FileReader(path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader br2 = new BufferedReader(file2);
                String line = "";
                while(true){
                    try {
                        if (!((line = br2.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(";");

                    for(int i = 0; i < str.length; i++){
                        if(str[i].contains("'")){ // Need some char replacement, which may cause conflict in the database
                            str[i] = str[i].replace("'","''");
                        }

                        while(str[i].length() != 0){ // We remove the useless spaces
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

                //Refresh TrainDatabase list.
                //Set static MainActivity values to default.
                MainActivity.isCurrentCardLanguage1 = true;
                MainActivity.isGuessModeLanguage1 = true;
                MainActivity.trainIndex = 0;
                MainActivity.modeSpinnerIndex = 0;
                MainActivity.numberSpinnerIndex = 2;
                MainActivity.wordTrainList =  MainActivity.dataBase.getWords(2, null, null);

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
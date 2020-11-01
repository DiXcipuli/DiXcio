package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProjectMenuActivity extends AppCompatActivity implements DeleteDialog.DeleteDialogListener{

    Button addWordsButton, browseWordsButton, trainButton, importCsvButton, exportCsvButton, deleteProjectButton;
    TextView subTitleProjectName;

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
        setContentView(R.layout.activity_project_menu);

        //Set static MainActivity values to default.
        MainActivity.isCurrentCardLanguage1 = true;
        MainActivity.isGuessModeLanguage1 = true;
        MainActivity.wordHasBeenDeleted = false;
        MainActivity.trainIndex = 0;
        MainActivity.modeSpinnerIndex = 0;
        MainActivity.numberSpinnerIndex = 0;
        MainActivity.browseScrollIndex = 0;
        MainActivity.browserScrollTop = 0;
        MainActivity.browseSearch = "";
        MainActivity.browseLanguage1 = true;
        MainActivity.browseAlphabetical = true;
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

        deleteProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        importCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImportCsvActivity.class);
                startActivity(intent);
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
        File dir = new File(root.getAbsolutePath() + File.separator + R.string.app_name);
        File file = new File(dir + File.separator + R.string.app_name + ".txt");
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
}
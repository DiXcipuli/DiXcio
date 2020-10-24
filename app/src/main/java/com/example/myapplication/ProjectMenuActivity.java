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

    Button addWordsButton, browseWordsButton, trainButton, importCsvButton, exportCsvButton, deleteProjectButton, setArticleButton;
    TextView subTitleProjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);

        addWordsButton = (Button)findViewById(R.id.addWordsButton);
        browseWordsButton = (Button)findViewById(R.id.browseButton);
        trainButton = (Button)findViewById(R.id.trainButton);
        subTitleProjectName = (TextView)findViewById(R.id.subTitleProjectName);
        subTitleProjectName.setText(MainActivity.currentProjectName);
        importCsvButton = (Button)findViewById(R.id.importCSVButtonMenu);
        exportCsvButton = (Button)findViewById(R.id.exportCSVButton);
        deleteProjectButton = (Button)findViewById(R.id.deleteProject);
        setArticleButton = (Button)findViewById(R.id.setArticleButton);

        deleteProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        setArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetArticleActivity.class);
                startActivity(intent);
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
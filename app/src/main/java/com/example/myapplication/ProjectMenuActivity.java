package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProjectMenuActivity extends AppCompatActivity {

    Button addWordsButton, browseWordsButton, trainButton, importCsvButton, exportCsvButton;
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

    public void openNewWordActivity(){
        Intent intent = new Intent(this, WordDefinitionActivity.class);
        startActivity(intent);
    }

    public void loadWords(){
        Intent intent = new Intent(this, WordBrowserActivity.class);
        startActivity(intent);
    }
}
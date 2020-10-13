package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProjectMenuActivity extends AppCompatActivity {

    Button addWordsButton, browseWordsButton;
    TextView subTitleProjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);

        addWordsButton = (Button)findViewById(R.id.addWordsButton);
        browseWordsButton = (Button)findViewById(R.id.browseButton);
        subTitleProjectName = (TextView)findViewById(R.id.subTitleProjectName);
        subTitleProjectName.setText(MainActivity.currentProjectName);

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
package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class WordBrowserActivity extends AppCompatActivity {

    ListView wordListView;
    Button word1Button, word2Button;
    TextView wordNumber;
    ConstraintLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_browser);

        wordListView = (ListView)findViewById(R.id.wordListView);
        word1Button = (Button)findViewById(R.id.language1Button);
        word2Button = (Button)findViewById(R.id.language2Button);
        wordNumber = (TextView)findViewById(R.id.wordNumber);
        background = (ConstraintLayout)findViewById(R.id.background);

        word1Button.setText(MainActivity.currentLanguage1);
        word2Button.setText(MainActivity.currentLanguage2);

        word1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase dataBase = new DataBase(WordBrowserActivity.this, MainActivity.currentProjectName);
                List<WordItem> allWordsList = dataBase.getAllWords(2);
                ArrayAdapter wordArrayAdapter = new ArrayAdapter<WordItem>(WordBrowserActivity.this, android.R.layout.simple_list_item_1, allWordsList);
                wordListView.setAdapter(wordArrayAdapter);
                background.setBackgroundColor(Color.parseColor("#33B5E6"));
                wordListView.setBackgroundColor(Color.parseColor("#33B5E6"));
            }
        });

        word2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase dataBase = new DataBase(WordBrowserActivity.this, MainActivity.currentProjectName);
                List<WordItem> allWordsList = dataBase.getAllWords(3);
                ArrayAdapter wordArrayAdapter = new ArrayAdapter<WordItem>(WordBrowserActivity.this, android.R.layout.simple_list_item_1, allWordsList);
                wordListView.setAdapter(wordArrayAdapter);
                background.setBackgroundColor(Color.parseColor("#FFBB34"));
                wordListView.setBackgroundColor(Color.parseColor("#FFBB34"));
            }
        });

        DataBase dataBase = new DataBase(WordBrowserActivity.this, MainActivity.currentProjectName);
        List<WordItem> allWordsList = dataBase.getAllWords(2);
        ArrayAdapter wordArrayAdapter = new ArrayAdapter<WordItem>(WordBrowserActivity.this, android.R.layout.simple_list_item_1, allWordsList);
        wordListView.setAdapter(wordArrayAdapter);

        wordNumber.setText(Integer.toString(allWordsList.size()) + " words");
    }
}
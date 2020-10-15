package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TrainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button guessLanguage1, guessLanguage2, mainTrainPannel;
    ImageButton successButton, failButton;
    Spinner orderSpinner, alphabetSpinner;
    List<WordItem> wordList;
    boolean boolGuessLanguage1;
    boolean currentCardLanguage1;

    Integer index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        index = 0;
        boolGuessLanguage1 = true;
        currentCardLanguage1 = true;

        orderSpinner = (Spinner)findViewById(R.id.orderSpinner);
        alphabetSpinner = (Spinner)findViewById(R.id.alphabetSpinner);
        successButton = (ImageButton)findViewById(R.id.successButton);
        failButton = (ImageButton)findViewById(R.id.failButton);

        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(this, R.array.browsingOrder, R.layout.spinner_row);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(orderAdapter);
        orderSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> alphabetAdapter = ArrayAdapter.createFromResource(this, R.array.alphabet, R.layout.spinner_row);
        alphabetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alphabetSpinner.setAdapter(alphabetAdapter);
        alphabetSpinner.setOnItemSelectedListener(this);

        guessLanguage1 = (Button)findViewById(R.id.guessLanguage1);
        guessLanguage2 = (Button)findViewById(R.id.guessLanguage2);
        mainTrainPannel = (Button)findViewById(R.id.mainTrainPannel);

        guessLanguage1.setText(MainActivity.currentLanguage1);
        guessLanguage2.setText(MainActivity.currentLanguage2);

        guessLanguage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolGuessLanguage1 = true;
                currentCardLanguage1 = true;
                mainTrainPannel.setBackgroundColor(Color.parseColor("#33B5E6"));
                index = 0;
                displayNewWord();
            }
        });

        guessLanguage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolGuessLanguage1 = false;
                currentCardLanguage1 = false;
                mainTrainPannel.setBackgroundColor(Color.parseColor("#FFBB34"));
                index = 0;
                displayNewWord();
            }
        });

        DataBase dataBase = new DataBase(getApplicationContext(), MainActivity.currentProjectName);
        wordList = dataBase.getAllWords(4);

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index ++;
                displayNewWord();
            }
        });

        mainTrainPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    String article1 = wordList.get(index).getArticleWord1();
                    String article2 = wordList.get(index).getArticleWord2();
                    if(article1.equals("None")){
                        article1 = "";
                    }
                    if(article2.equals("None")){
                        article2 = "";
                    }
                    if(currentCardLanguage1){
                        mainTrainPannel.setBackgroundColor(Color.parseColor("#FFBB34"));
                        mainTrainPannel.setText(article2 + " " + wordList.get(index).getWordLanguage2());
                        currentCardLanguage1 = false;
                    }
                    else{
                        mainTrainPannel.setBackgroundColor(Color.parseColor("#33B5E6"));
                        mainTrainPannel.setText(article1 + " " + wordList.get(index).getWordLanguage1());
                        currentCardLanguage1 = true;
                    }
                }
            }
        });

        displayNewWord();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void displayNewWord(){
        if(index >= wordList.size()){
            mainTrainPannel.setText("No more datas!");
            index = -1;
        }
        else{
            String article1 = wordList.get(index).getArticleWord1();
            String article2 = wordList.get(index).getArticleWord2();
            if(article1.equals("None")){
                article1 = "";
            }
            if(article2.equals("None")){
                article2 = "";
            }

            String word;
            if(boolGuessLanguage1) {
                word = article1 + " " + wordList.get(index).getWordLanguage1();
                mainTrainPannel.setBackgroundColor(Color.parseColor("#33B5E6"));

            }
            else{
                word = article2 + " " + wordList.get(index).getWordLanguage2();
                mainTrainPannel.setBackgroundColor(Color.parseColor("#FFBB34"));
            }
            mainTrainPannel.setText(word);
        }
    }
}
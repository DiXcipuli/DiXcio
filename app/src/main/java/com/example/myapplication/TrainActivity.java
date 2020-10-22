package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class TrainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button guessLanguage1, guessLanguage2, mainTrainPannel;
    ImageButton successButton, failButton, bookmarkButton;
    Spinner orderSpinner, alphabetSpinner;
    List<WordItem> wordList;
    boolean boolGuessLanguage1;
    boolean currentCardLanguage1;
    TextView wordProgression;
    DataBase dataBase;
    Integer currentBrowseMode;
    Integer currentAlphabetMode;
    ConstraintLayout background;

    Integer index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        currentAlphabetMode = 0;
        currentBrowseMode = 0;
        index = 0;
        boolGuessLanguage1 = true;
        currentCardLanguage1 = true;

        orderSpinner = (Spinner)findViewById(R.id.orderSpinner);
        alphabetSpinner = (Spinner)findViewById(R.id.alphabetSpinner);
        successButton = (ImageButton)findViewById(R.id.successButton);
        failButton = (ImageButton)findViewById(R.id.failButton);
        wordProgression = (TextView)findViewById(R.id.wordProgression);
        background = (ConstraintLayout)findViewById(R.id.background);
        bookmarkButton = (ImageButton)findViewById(R.id.bookmarkButton);

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

        successButton.setBackgroundResource(R.drawable.yellowblack);
        failButton.setBackgroundResource(R.drawable.yellowblack);
        successButton.setImageResource(R.drawable.tick_blue_black);
        failButton.setImageResource(R.drawable.cross_blue_black);

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    if(wordList.get(index).getBookmarked() == 1){
                        wordList.get(index).setBookmarked(0);
                        bookmarkButton.setImageResource(R.drawable.bookmark);
                    }
                    else{
                        wordList.get(index).setBookmarked(1);
                        bookmarkButton.setImageResource(R.drawable.bookmark_red);
                    }
                    DataBase dataBase = new DataBase(TrainActivity.this, MainActivity.currentProjectName);
                    dataBase.replaceWord(wordList.get(index), wordList.get(index).getWordLanguage1(), wordList.get(index).getWordLanguage2());
                }
            }
        });

        guessLanguage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordList = dataBase.getAllWords(currentBrowseMode, null);
                boolGuessLanguage1 = true;
                currentCardLanguage1 = true;
                background.setBackgroundColor(Color.parseColor("#33B5E6"));
                mainTrainPannel.setBackgroundResource(R.drawable.blueblack);
                index = 0;
                displayNewWord();
                successButton.setBackgroundResource(R.drawable.yellowblack);
                failButton.setBackgroundResource(R.drawable.yellowblack);
                successButton.setImageResource(R.drawable.tick_blue_black);
                failButton.setImageResource(R.drawable.cross_blue_black);
            }
        });

        guessLanguage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordList = dataBase.getAllWords(currentBrowseMode, null);
                boolGuessLanguage1 = false;
                currentCardLanguage1 = false;
                background.setBackgroundColor(Color.parseColor("#FFBB34"));
                mainTrainPannel.setBackgroundResource(R.drawable.yellowblack);
                successButton.setBackgroundResource(R.drawable.blueblack);
                failButton.setBackgroundResource(R.drawable.blueblack);
                successButton.setImageResource(R.drawable.tick_yellow_black);
                failButton.setImageResource(R.drawable.cross_yellow_black);
                index = 0;
                displayNewWord();
            }
        });

        currentBrowseMode = 6;
        dataBase = new DataBase(getApplicationContext(), MainActivity.currentProjectName);
        wordList = dataBase.getAllWords(currentBrowseMode,null);

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index ++;
                currentCardLanguage1 = boolGuessLanguage1;
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
                        mainTrainPannel.setBackgroundResource(R.drawable.yellowblack);
                        mainTrainPannel.setText(article2 + " " + wordList.get(index).getWordLanguage2());
                        currentCardLanguage1 = false;
                    }
                    else{
                        mainTrainPannel.setBackgroundResource(R.drawable.blueblack);
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
        index = 0;
        updateBrowseMode();
    }

    public void updateBrowseMode(){
        if(orderSpinner.getSelectedItemPosition() == 0 || orderSpinner.getSelectedItemPosition() ==2){
            alphabetSpinner.setEnabled(false);
        }
        else{
            alphabetSpinner.setEnabled(true);
        }

        switch (orderSpinner.getSelectedItemPosition()){
            case 0:
                wordList = dataBase.getAllWords(6, null);
                break;

            case 1:
                if(boolGuessLanguage1){
                    wordList = dataBase.getAllWords(7, alphabetSpinner.getSelectedItem().toString());
                }
        }
        displayNewWord();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void displayNewWord(){

        if(index >= wordList.size()){
            wordProgression.setText("0 / " + Integer.toString(wordList.size()) + " words");
            mainTrainPannel.setText("No more datas!");
            bookmarkButton.setImageResource(R.drawable.bookmark);
            if(boolGuessLanguage1){
                bookmarkButton.setBackgroundResource(R.drawable.yellowblack);
            }
            else{
                bookmarkButton.setBackgroundResource(R.drawable.blueblack);
            }
            index = -1;
        }
        else{
            wordProgression.setText(Integer.toString(index + 1) + " / " + Integer.toString(wordList.size()) + " words");
            if(wordList.get(index).getBookmarked() == 1){
                bookmarkButton.setImageResource(R.drawable.bookmark_red);
                if(boolGuessLanguage1){
                    bookmarkButton.setBackgroundResource(R.drawable.yellowblack);
                }
                else{
                    bookmarkButton.setBackgroundResource(R.drawable.blueblack);
                }
            }
            else{
                bookmarkButton.setImageResource(R.drawable.bookmark);
                if(boolGuessLanguage1){
                    bookmarkButton.setBackgroundResource(R.drawable.yellowblack);
                }
                else{
                    bookmarkButton.setBackgroundResource(R.drawable.blueblack);
                }
            }

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
                mainTrainPannel.setBackgroundResource(R.drawable.blueblack);

            }
            else{
                word = article2 + " " + wordList.get(index).getWordLanguage2();
                mainTrainPannel.setBackgroundResource(R.drawable.yellowblack);
            }
            mainTrainPannel.setText(word);
        }
    }
}
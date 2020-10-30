package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TrainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button guessLanguage1, guessLanguage2, mainTrainPannel, modifyButton;
    ImageButton successButton, failButton, bookmarkButton;
    Spinner orderSpinner, numberSpinner;
    TextView wordProgression;
    ConstraintLayout background;
    private Integer spinnerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        //Define components
        orderSpinner = (Spinner)findViewById(R.id.orderSpinner);
        numberSpinner = (Spinner)findViewById(R.id.numberSpinner);
        successButton = (ImageButton)findViewById(R.id.successButton);
        failButton = (ImageButton)findViewById(R.id.failButton);
        wordProgression = (TextView)findViewById(R.id.wordProgression);
        background = (ConstraintLayout)findViewById(R.id.background);
        bookmarkButton = (ImageButton)findViewById(R.id.bookmarkButton);
        guessLanguage1 = (Button)findViewById(R.id.guessLanguage1);
        guessLanguage2 = (Button)findViewById(R.id.guessLanguage2);
        mainTrainPannel = (Button)findViewById(R.id.mainTrainPannel);
        modifyButton = (Button)findViewById(R.id.modifyButton);

        mainTrainPannel.setTransformationMethod(null);

        //Set Spinners -------------------------------------------------------
        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(this, R.array.browsingOrder, R.layout.spinner_row);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(orderAdapter);
        orderSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> alphabetAdapter = ArrayAdapter.createFromResource(this, R.array.numbers, R.layout.spinner_row);
        alphabetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberSpinner.setAdapter(alphabetAdapter);
        numberSpinner.setOnItemSelectedListener(this);
        //---------------------------------------------------------------------

        //Set language TextView
        guessLanguage1.setText(MainActivity.currentLanguage1);
        guessLanguage2.setText(MainActivity.currentLanguage2);

        setDisplayGraphics();
        displayWord();

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.trainIndex != -1){
                    if(MainActivity.wordTrainList.get(MainActivity.trainIndex).getBookmarked() == 1){
                        MainActivity.wordTrainList.get(MainActivity.trainIndex).setBookmarked(0);
                        bookmarkButton.setImageResource(R.drawable.bookmark);
                    }
                    else{
                        MainActivity.wordTrainList.get(MainActivity.trainIndex).setBookmarked(1);
                        bookmarkButton.setImageResource(R.drawable.bookmark_red);
                    }
                    DataBase dataBase = new DataBase(TrainActivity.this, MainActivity.currentProjectName);
                    //replaceWord(WordItem, previousWord1, previousWord2)
                    dataBase.replaceWord(MainActivity.wordTrainList.get(MainActivity.trainIndex), MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage1(), MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage2());
                }
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.trainIndex != -1) {
                    Intent intent = new Intent(getApplicationContext(), WordDefinitionActivity.class);
                    intent.putExtra("modifyMode", true);
                    intent.putExtra("word", MainActivity.wordTrainList.get(MainActivity.trainIndex));
                    startActivity(intent);
                }
            }
        });

        guessLanguage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isGuessModeLanguage1 = true;
                MainActivity.isCurrentCardLanguage1 = true;
                setDisplayGraphics();
                displayWord();
            }
        });

        guessLanguage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isGuessModeLanguage1 = false;
                MainActivity.isCurrentCardLanguage1 = false;
                setDisplayGraphics();
                displayWord();
            }
        });

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.trainIndex != -1){
                    MainActivity.wordTrainList.get(MainActivity.trainIndex).setCount(MainActivity.wordTrainList.get(MainActivity.trainIndex).getCount() + 1);
                    MainActivity.wordTrainList.get(MainActivity.trainIndex).setSuccess(MainActivity.wordTrainList.get(MainActivity.trainIndex).getSuccess() + 1);
                    MainActivity.wordTrainList.get(MainActivity.trainIndex).setPercentage(((float)MainActivity.wordTrainList.get(MainActivity.trainIndex).getSuccess() / MainActivity.wordTrainList.get(MainActivity.trainIndex).getCount()) * 100);
                    MainActivity.dataBase.replaceWord(MainActivity.wordTrainList.get(MainActivity.trainIndex), MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage1(), MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage2());
                }
                MainActivity.trainIndex ++;
                MainActivity.isCurrentCardLanguage1 = MainActivity.isGuessModeLanguage1;
                displayWord();
            }
        });

        failButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.trainIndex != -1){
                    MainActivity.wordTrainList.get(MainActivity.trainIndex).setCount(MainActivity.wordTrainList.get(MainActivity.trainIndex).getCount() + 1);
                    MainActivity.wordTrainList.get(MainActivity.trainIndex).setPercentage(((float)MainActivity.wordTrainList.get(MainActivity.trainIndex).getSuccess() / MainActivity.wordTrainList.get(MainActivity.trainIndex).getCount()) * 100);
                    MainActivity.dataBase.replaceWord(MainActivity.wordTrainList.get(MainActivity.trainIndex), MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage1(), MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage2());
                }
                MainActivity.trainIndex ++;
                MainActivity.isCurrentCardLanguage1 = MainActivity.isGuessModeLanguage1;
                displayWord();
            }
        });

        mainTrainPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.trainIndex != -1){
                    if(MainActivity.isCurrentCardLanguage1){
                        mainTrainPannel.setBackgroundResource(R.drawable.yellowblack);
                        mainTrainPannel.setText(MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage2());
                        MainActivity.isCurrentCardLanguage1 = false;
                    }
                    else{
                        mainTrainPannel.setBackgroundResource(R.drawable.blueblack);
                        mainTrainPannel.setText(MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage1());
                        MainActivity.isCurrentCardLanguage1 = true;
                    }
                }
            }
        });
    }

    public void setDisplayGraphics(){
        if(MainActivity.isGuessModeLanguage1) {
            background.setBackgroundColor(Color.parseColor("#33B5E6"));
            successButton.setBackgroundResource(R.drawable.yellowblack);
            failButton.setBackgroundResource(R.drawable.yellowblack);
            successButton.setImageResource(R.drawable.tick_blue_black);
            failButton.setImageResource(R.drawable.cross_blue_black);
            bookmarkButton.setBackgroundResource(R.drawable.yellowblack);
            modifyButton.setBackgroundResource(R.drawable.yellowblack);
        }
        else{
            background.setBackgroundColor(Color.parseColor("#FFBB34"));
            mainTrainPannel.setBackgroundResource(R.drawable.yellowblack);
            successButton.setBackgroundResource(R.drawable.blueblack);
            failButton.setBackgroundResource(R.drawable.blueblack);
            successButton.setImageResource(R.drawable.tick_yellow_black);
            failButton.setImageResource(R.drawable.cross_yellow_black);
            bookmarkButton.setBackgroundResource(R.drawable.blueblack);
            modifyButton.setBackgroundResource(R.drawable.blueblack);
        }
        if(MainActivity.isCurrentCardLanguage1){
            mainTrainPannel.setBackgroundResource(R.drawable.blueblack);
        }
        else{
            mainTrainPannel.setBackgroundResource(R.drawable.yellowblack);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerCount ++;

        if(parent.getId() == R.id.orderSpinner && spinnerCount > 3) {
            setSpinner();
            updateBrowseMode();
        }

        if(parent.getId() == R.id.numberSpinner && spinnerCount > 3){
            updateBrowseMode();
        }
    }

    public void setSpinner(){
        //Disable them if not needed in the current mode
        if(orderSpinner.getSelectedItemPosition() == 0){
            numberSpinner.setEnabled(false);
        }
        else{ //Or enable them
            numberSpinner.setEnabled(true);

        }
    }

    public void updateBrowseMode(){
        MainActivity.trainIndex = 0;
        switch (orderSpinner.getSelectedItemPosition()){
            case 0: //All words in random
                MainActivity.wordTrainList = MainActivity.dataBase.getWords(2, null, null);
                break;
            case 1://By date
                MainActivity.wordTrainList = MainActivity.dataBase.getWords(8, Integer.parseInt(numberSpinner.getSelectedItem().toString()), null);
                break;
            case 2://Most failed
                MainActivity.wordTrainList = MainActivity.dataBase.getWords(11, Integer.parseInt(numberSpinner.getSelectedItem().toString()), null);
                break;
            case 3://Bookmark
                MainActivity.wordTrainList = MainActivity.dataBase.getWords(7, Integer.parseInt(numberSpinner.getSelectedItem().toString()), null);
                break;
            case 4://Less met
                MainActivity.wordTrainList = MainActivity.dataBase.getWords(12, Integer.parseInt(numberSpinner.getSelectedItem().toString()), null);
                break;
        }
        displayWord();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void displayWord(){

        if(MainActivity.trainIndex >= MainActivity.wordTrainList.size() || MainActivity.trainIndex == -1){
            wordProgression.setText("0 / " + Integer.toString(MainActivity.wordTrainList.size()) + " words");
            mainTrainPannel.setText("No more datas!");
            bookmarkButton.setImageResource(R.drawable.bookmark);
            MainActivity.trainIndex = -1;
        }
        else{
            wordProgression.setText(Integer.toString(MainActivity.trainIndex + 1) + " / " + Integer.toString(MainActivity.wordTrainList.size()) + " words");
            if(MainActivity.wordTrainList.get(MainActivity.trainIndex).getBookmarked() == 1){
                bookmarkButton.setImageResource(R.drawable.bookmark_red);
            }
            else{
                bookmarkButton.setImageResource(R.drawable.bookmark);
            }

            String word;
            if(MainActivity.isCurrentCardLanguage1) {
                word = MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage1();
                //mainTrainPannel.setBackgroundResource(R.drawable.blueblack);
            }
            else{
                word = MainActivity.wordTrainList.get(MainActivity.trainIndex).getWordLanguage2();
                //mainTrainPannel.setBackgroundResource(R.drawable.yellowblack);
            }
            mainTrainPannel.setText(word);
            if(word.length() >= 10){
                mainTrainPannel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
            }
            else{
                mainTrainPannel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.wordHasBeenDeleted){
            MainActivity.wordHasBeenDeleted = false;
            MainActivity.trainIndex ++;
            MainActivity.isCurrentCardLanguage1 = MainActivity.isGuessModeLanguage1;

        }

        displayWord();
    }
}
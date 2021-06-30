package com.DiXcipuli.DiXcio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//Activity loaded to allow the user to enter a new word in the Database
public class WordDefinitionActivity extends AppCompatActivity {
    //This Activity can be opened under two different situations:
    //  - When entering a new word
    //  - When modifying an existing word

    Button saveWordButton, deleteWordButton;
    ImageButton bookmarkButton;
    EditText word1, word2;
    boolean modifyMode;    // Used to dispaly the 'deleteWordButton' or not.
    boolean modifiedFromBrowser;    // The behavior is different if comming from the training mode. If it is the case, the word have to be refresh as they might have been modified
    String previousWord1, previousWord2;    // Used to replace the right words in the database
    Integer bookmarked; // Integer acting as a boolean, but more convinient for the database
    TextView wordInfo;  //Success % ...
    WordItem wordItem;

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.currentProjectName == null){
            Toast.makeText(getApplicationContext(), "Reset Security", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookmarked = 0;

        setContentView(R.layout.activity_word_definition);

        word1 = (EditText)findViewById(R.id.word1);
        word2 = (EditText)findViewById(R.id.word2);
        bookmarkButton = (ImageButton)findViewById(R.id.bookmarkButton);
        deleteWordButton = (Button)findViewById(R.id.deleteWordButton);
        wordInfo = (TextView)findViewById(R.id.wordInfo);

        word1.setHint(MainActivity.currentLanguage1);
        word2.setHint(MainActivity.currentLanguage2);

        // Getting parameters from the previous Activity
        Bundle b = getIntent().getExtras();
        modifyMode = b.getBoolean("modifyMode");

        if(modifyMode){
            wordItem = (WordItem) b.getSerializable("word");
            word1.setText(wordItem.getWordLanguage1());
            word2.setText(wordItem.getWordLanguage2());

            modifiedFromBrowser = b.getBoolean("modifiedFromBrowser");

            previousWord1 = wordItem.getWordLanguage1();
            previousWord2 = wordItem.getWordLanguage2();
            deleteWordButton.setVisibility(View.VISIBLE);
            wordInfo.setVisibility(View.VISIBLE);
            wordInfo.setText(wordItem.getInfo());

            if(wordItem.getBookmarked() == 1){
                bookmarkButton.setImageResource(R.drawable.bookmark_red);
                bookmarked = 1;
            }
        }
        else{ // No need to display those 2 if we just want to add a new word
            deleteWordButton.setVisibility(View.GONE);
            wordInfo.setVisibility(View.GONE);
        }

        deleteWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = new DataBase(WordDefinitionActivity.this, MainActivity.currentProjectName);
                db.deleteWord(previousWord1, previousWord2);
                Toast.makeText(getApplicationContext(), "Word deleted!", Toast.LENGTH_LONG).show();
                MainActivity.wordHasBeenDeleted = true;

                //Refresh TrainDatabase list.
                //Set static MainActivity values to default.
                MainActivity.isCurrentCardLanguage1 = true;
                MainActivity.isGuessModeLanguage1 = true;
                MainActivity.trainIndex = 0;
                MainActivity.modeSpinnerIndex = 0;
                MainActivity.numberSpinnerIndex = 0;
                MainActivity.wordTrainList =  MainActivity.dataBase.getWords(2, null, null);

                finish();
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookmarked == 1){
                    bookmarked = 0;
                    bookmarkButton.setImageResource(R.drawable.bookmark);
                }
                else{
                    bookmarked = 1;
                    bookmarkButton.setImageResource(R.drawable.bookmark_red);
                }
            }
        });

        saveWordButton = (Button)findViewById(R.id.saveWordButton);
        saveWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(word1.getText()) || TextUtils.isEmpty(word2.getText())){
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_LONG).show();
                }
                else{
                    boolean isForbiddenCharEntered = false;

                    String word1String, word2String;
                    word1String = word1.getText().toString();
                    word2String = word2.getText().toString();

                    for(int i = 0; i<word1String.length(); i++){
                        if(Character.toString(word1String.charAt(i)).equals(";")){
                            isForbiddenCharEntered = true;
                        }
                    }
                    for(int i = 0; i<word2String.length(); i++){
                        if(Character.toString(word2String.charAt(i)).equals(";")){
                            isForbiddenCharEntered = true;
                        }
                    }

                    if(isForbiddenCharEntered){
                        Toast.makeText(getApplicationContext(), "Character ' ; ' is forbidden! Used for CSV", Toast.LENGTH_LONG).show();
                    }

                    else{

                        //erases unwanted white spaces
                        while(Character.toString(word1String.charAt(0)).equals(" ")){
                            word1String.substring(1);
                        }
                        while(Character.toString(word2String.charAt(0)).equals(" ")){
                            word2String.substring(1);
                        }

                        WordItem word = null;

                        DataBase db = new DataBase(WordDefinitionActivity.this, MainActivity.currentProjectName);

                        if(modifyMode){
                            word = new WordItem(word1String,
                                    word2String,
                                    wordItem.getCount(),
                                    wordItem.getSuccess(),
                                    wordItem.getPercentage(),
                                    wordItem.getCategory(),
                                    wordItem.getDate(),
                                    bookmarked);

                            if(modifiedFromBrowser){
                                //Refresh TrainDatabase list.
                                //Set static MainActivity values to default.
                                MainActivity.isCurrentCardLanguage1 = true;
                                MainActivity.isGuessModeLanguage1 = true;
                                MainActivity.trainIndex = 0;
                                MainActivity.modeSpinnerIndex = 0;
                                MainActivity.numberSpinnerIndex = 0;
                                MainActivity.wordTrainList =  MainActivity.dataBase.getWords(2, null, null);
                            }
                            else{
                                MainActivity.wordTrainList.get(MainActivity.trainIndex).setWordLanguage1(word1String);
                                MainActivity.wordTrainList.get(MainActivity.trainIndex).setWordLanguage2(word2String);
                                MainActivity.wordTrainList.get(MainActivity.trainIndex).setBookmarked(bookmarked);
                            }

                            boolean success = db.replaceWord(word, previousWord1, previousWord2);
                            Toast.makeText(getApplicationContext(), "Definition modified!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            word = new WordItem(word1String,
                                    word2String,
                                    0,
                                    0,
                                    (float)0,
                                    "None",
                                    "",
                                    bookmarked);

                            boolean success = db.addOne(word);
                            Toast.makeText(getApplicationContext(), "Word saved!", Toast.LENGTH_LONG).show();
                            word1.setText("");
                            word2.setText("");
                            bookmarkButton.setImageResource(R.drawable.bookmark);
                        }
                        bookmarked = 0;

                    }
                }
            }
        });
    }
}
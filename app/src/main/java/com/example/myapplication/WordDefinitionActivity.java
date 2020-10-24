package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;

public class WordDefinitionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner articleWord1Spinner, articleWord2Spinner, word1StoredAtSpinner, word2StoredAtSpinner;
    Button saveWordButton, deleteWordButton;
    ImageButton bookmarkButton;
    private String articleWord1, articleWord2, word1StoredAt, word2StoredAt;
    EditText word1, word2;
    boolean modifyMode;
    String previousWord1, previousWord2;
    Integer bookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookmarked = 0;

        setContentView(R.layout.activity_word_definition);

        word1 = (EditText)findViewById(R.id.word1);
        word2 = (EditText)findViewById(R.id.word2);
        articleWord1Spinner = (Spinner)findViewById(R.id.articleWord1Spinner);
        articleWord2Spinner = (Spinner)findViewById(R.id.articleWord2Spinner);
        word1StoredAtSpinner = (Spinner)findViewById(R.id.word1StoredAtSpinner);
        word2StoredAtSpinner = (Spinner)findViewById(R.id.word2StoredAtSpinner);
        bookmarkButton = (ImageButton)findViewById(R.id.bookmarkButton);
        deleteWordButton = (Button)findViewById(R.id.deleteWordButton);

        word1.setHint(MainActivity.currentLanguage1);
        word2.setHint(MainActivity.currentLanguage2);

        Bundle b = getIntent().getExtras();
        modifyMode = b.getBoolean("modifyMode");

        if(modifyMode){
            word1.setText(b.getString("word1"));
            word2.setText(b.getString("word2"));
            previousWord1 = word1.getText().toString();
            previousWord2 = word2.getText().toString();
            deleteWordButton.setVisibility(View.VISIBLE);

            if(b.getInt("bookmarked") == 1){
                bookmarkButton.setImageResource(R.drawable.bookmark_red);
                bookmarked = 1;
            }
        }
        else{
            deleteWordButton.setVisibility(View.GONE);
        }

        deleteWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = new DataBase(WordDefinitionActivity.this, MainActivity.currentProjectName);
                db.deleteWord(previousWord1, previousWord2);
                Toast.makeText(getApplicationContext(), "Word deleted!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        word1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
             @Override
             public void onTextChanged(CharSequence s, int start,
                                       int before, int count) {
                 if (s.length() != 0 && MainActivity.map.containsKey(StringUtils.stripAccents(String.valueOf(s.charAt(0))).toLowerCase()))
                    word1StoredAtSpinner.setSelection(MainActivity.map.get(StringUtils.stripAccents(String.valueOf(s.charAt(0))).toLowerCase()));
                 else{
                     word1StoredAtSpinner.setSelection(26);
                 }
             }
         });

        word2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0 && MainActivity.map.containsKey(StringUtils.stripAccents(String.valueOf(s.charAt(0))).toLowerCase()))
                    word2StoredAtSpinner.setSelection(MainActivity.map.get(StringUtils.stripAccents(String.valueOf(s.charAt(0))).toLowerCase()));
                else{
                    word2StoredAtSpinner.setSelection(26);
                }
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
                    String word1String, word2String;
                    word1String = word1.getText().toString();
                    word2String = word2.getText().toString();

                    //erases unwanted white spaces
                    while(Character.toString(word1String.charAt(0)).equals(" ")){
                        word1String.substring(1);
                    }
                    while(Character.toString(word2String.charAt(0)).equals(" ")){
                        word2String.substring(1);
                    }

                    WordItem word = new WordItem(articleWord1Spinner.getSelectedItem().toString(),
                            word1String,
                            word1StoredAtSpinner.getSelectedItem().toString().toLowerCase(),
                            articleWord2Spinner.getSelectedItem().toString(),
                            word2String,
                            word2StoredAtSpinner.getSelectedItem().toString().toLowerCase(),
                            0,
                            0,
                            (float)0,
                            "None",
                            "",
                            bookmarked);

                    bookmarked = 0;

                    DataBase db = new DataBase(WordDefinitionActivity.this, MainActivity.currentProjectName);

                    if(modifyMode){
                        boolean success = db.replaceWord(word, previousWord1, previousWord2);
                        Toast.makeText(getApplicationContext(), "Definition modified!", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(), WordBrowserActivity.class);
//                        startActivity(intent);
                        finish();
                    }
                    else {

                        boolean success = db.addOne(word);
                        Toast.makeText(getApplicationContext(), "Word saved!", Toast.LENGTH_LONG).show();
                        word1.setText("");
                        word2.setText("");
                        bookmarkButton.setImageResource(R.drawable.bookmark);
                    }
                }
            }
        });





        //articleWord1Adapter
        ArrayAdapter<String> articleWord1Adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_row, MainActivity.articleListLanguage1);
        articleWord1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        articleWord1Spinner.setAdapter(articleWord1Adapter);
        articleWord1Spinner.setOnItemSelectedListener(this);

        //articleWord2Adapter
        ArrayAdapter<String> articleWord2Adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_row, MainActivity.articleListLanguage2);
        articleWord2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        articleWord2Spinner.setAdapter(articleWord2Adapter);
        articleWord2Spinner.setOnItemSelectedListener(this);

        //word1StoredAtAdapter
        ArrayAdapter<CharSequence> word1StoredAtAdapter = ArrayAdapter.createFromResource(this, R.array.alphabet, R.layout.spinner_row);
        word1StoredAtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        word1StoredAtSpinner.setAdapter(word1StoredAtAdapter);
        word1StoredAtSpinner.setOnItemSelectedListener(this);

        //word2StoredAtAdapter
        ArrayAdapter<CharSequence> word2StoredAtAdapter = ArrayAdapter.createFromResource(this, R.array.alphabet, R.layout.spinner_row);
        word2StoredAtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        word2StoredAtSpinner.setAdapter(word2StoredAtAdapter);
        word2StoredAtSpinner.setOnItemSelectedListener(this);

//        word1StoredAtSpinner.setSelection(1);
//        word2StoredAtSpinner.setSelection(4);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.articleWord1Spinner:
                articleWord1 = parent.getItemAtPosition(position).toString();
                break;
            case R.id.word1StoredAtSpinner:
                word1StoredAt = parent.getItemAtPosition(position).toString();
                break;
            case R.id.articleWord2Spinner:
                articleWord2 = parent.getItemAtPosition(position).toString();
                break;
            case R.id.word2StoredAtSpinner:
                word2StoredAt = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
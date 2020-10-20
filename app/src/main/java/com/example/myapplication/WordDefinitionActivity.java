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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WordDefinitionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner articleWord1Spinner, articleWord2Spinner, word1StoredAtSpinner, word2StoredAtSpinner;
    Button saveWordButton;
    private String articleWord1, articleWord2, word1StoredAt, word2StoredAt;
    EditText word1, word2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_word_definition);

        word1 = (EditText)findViewById(R.id.word1);
        word2 = (EditText)findViewById(R.id.word2);
        articleWord1Spinner = (Spinner)findViewById(R.id.articleWord1Spinner);
        articleWord2Spinner = (Spinner)findViewById(R.id.articleWord2Spinner);
        word1StoredAtSpinner = (Spinner)findViewById(R.id.word1StoredAtSpinner);
        word2StoredAtSpinner = (Spinner)findViewById(R.id.word2StoredAtSpinner);

        word1.setHint(MainActivity.currentLanguage1);
        word2.setHint(MainActivity.currentLanguage2);

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
                 if (s.length() != 0 && MainActivity.map.containsKey(String.valueOf(s.charAt(0))))
                    word1StoredAtSpinner.setSelection(MainActivity.map.get(String.valueOf(s.charAt(0))));
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
                if (s.length() != 0 && MainActivity.map.containsKey(String.valueOf(s.charAt(0))))
                    word2StoredAtSpinner.setSelection(MainActivity.map.get(String.valueOf(s.charAt(0))));
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
                    WordItem word = new WordItem(articleWord1Spinner.getSelectedItem().toString(),
                            word1.getText().toString(),
                            word1StoredAtSpinner.getSelectedItem().toString(),
                            articleWord2Spinner.getSelectedItem().toString(),
                            word2.getText().toString(),
                            word2StoredAtSpinner.getSelectedItem().toString(),
                            0,
                            0,
                            (float)0,
                            "None",
                            "");

                    word1.setText("");
                    word2.setText("");

                    DataBase db = new DataBase(WordDefinitionActivity.this, MainActivity.currentProjectName);
                    boolean success =  db.addOne(word);
                }
            }
        });





        //articleWord1Adapter
        ArrayAdapter<CharSequence> articleWord1Adapter = ArrayAdapter.createFromResource(this, R.array.germanArticle, R.layout.spinner_row);
        articleWord1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        articleWord1Spinner.setAdapter(articleWord1Adapter);
        articleWord1Spinner.setOnItemSelectedListener(this);

        //articleWord2Adapter
        ArrayAdapter<CharSequence> articleWord2Adapter = ArrayAdapter.createFromResource(this, R.array.frenchArticle, R.layout.spinner_row);
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
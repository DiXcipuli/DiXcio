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

import java.util.HashMap;
import java.util.Map;

public class WordDefinitionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner articleWord1Spinner, articleWord2Spinner, word1StoredAtSpinner, word2StoredAtSpinner;
    Button saveWordButton;
    private String articleWord1, articleWord2, word1StoredAt, word2StoredAt;
    EditText word1, word2;
    Map<String, Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map = new HashMap<String, Integer>();
        map.put("a", 0);
        map.put("b", 1);
        map.put("c", 2);
        map.put("d", 3);
        map.put("e", 4);
        map.put("f", 5);
        map.put("g", 6);
        map.put("h", 7);
        map.put("i", 8);
        map.put("j", 9);
        map.put("k", 10);
        map.put("l", 11);
        map.put("m", 12);
        map.put("n", 13);
        map.put("o", 14);
        map.put("p", 15);
        map.put("q", 16);
        map.put("r", 17);
        map.put("s", 18);
        map.put("t", 19);
        map.put("u", 20);
        map.put("v", 21);
        map.put("w", 22);
        map.put("x", 23);
        map.put("y", 24);
        map.put("z", 25);
        map.put("A", 0);
        map.put("B", 1);
        map.put("C", 2);
        map.put("D", 3);
        map.put("E", 4);
        map.put("F", 5);
        map.put("G", 6);
        map.put("H", 7);
        map.put("I", 8);
        map.put("J", 9);
        map.put("K", 10);
        map.put("L", 11);
        map.put("M", 12);
        map.put("N", 13);
        map.put("O", 14);
        map.put("P", 15);
        map.put("Q", 16);
        map.put("R", 17);
        map.put("S", 18);
        map.put("T", 19);
        map.put("U", 20);
        map.put("V", 21);
        map.put("W", 22);
        map.put("X", 23);
        map.put("Y", 24);
        map.put("Z", 25);

        System.out.println(map.get("dog"));

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
                 if (s.length() != 0 && map.containsKey(String.valueOf(s.charAt(0))))
                    word1StoredAtSpinner.setSelection(map.get(String.valueOf(s.charAt(0))));
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
                if (s.length() != 0 && map.containsKey(String.valueOf(s.charAt(0))))
                    word2StoredAtSpinner.setSelection(map.get(String.valueOf(s.charAt(0))));
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
                            "None");

                    word1.setText("");
                    word2.setText("");

                    DataBase db = new DataBase(WordDefinitionActivity.this, MainActivity.currentProjectName);
                    boolean success =  db.addOne(word);
                    Toast.makeText(WordDefinitionActivity.this, "Success = " + success, Toast.LENGTH_LONG).show();
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
        Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
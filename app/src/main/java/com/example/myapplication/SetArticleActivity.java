package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetArticleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button addArticle1, addArticle2, deleteArticle1Button, deleteArticle2Button;
    TextView article1, article2;
    Spinner spinnerArticle1, spinnerArticle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_article);

        addArticle1 = (Button)findViewById(R.id.addButtonArticle1);
        addArticle2 = (Button)findViewById(R.id.addButtonArticle2);
        article1 = (TextView)findViewById(R.id.textArticle1);
        article2 = (TextView)findViewById(R.id.textArticle2);
        spinnerArticle1 = (Spinner)findViewById(R.id.spinnerArticle1);
        spinnerArticle2 = (Spinner)findViewById(R.id.spinnerArticle2);
        deleteArticle1Button = (Button)findViewById(R.id.buttonDeleteArticle1);
        deleteArticle2Button = (Button)findViewById(R.id.buttonDeleteArticle2);

        setAdapters();

        addArticle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!article1.getText().toString().isEmpty()) {
                    MainActivity.articleListLanguage1.add(article1.getText().toString().toLowerCase());
                    setAdapters();
                    article1.setText("");

                    Toast.makeText(getApplicationContext(), "article added!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Field empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        addArticle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!article2.getText().toString().isEmpty()) {
                    MainActivity.articleListLanguage2.add(article2.getText().toString().toLowerCase());
                    setAdapters();
                    article2.setText("");

                    Toast.makeText(getApplicationContext(), "article added!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Field empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteArticle1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerArticle1.getSelectedItemPosition() != 0) {
                    MainActivity.articleListLanguage1.remove(spinnerArticle1.getSelectedItemPosition());
                    setAdapters();

                    Toast.makeText(getApplicationContext(), "article deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteArticle2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerArticle2.getSelectedItemPosition() != 0) {
                    MainActivity.articleListLanguage2.remove(spinnerArticle2.getSelectedItemPosition());
                    setAdapters();

                    Toast.makeText(getApplicationContext(), "article deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setAdapters(){
        //Adapter1
        ArrayAdapter articleWord1Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, MainActivity.articleListLanguage1);
        articleWord1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticle1.setAdapter(articleWord1Adapter);
        spinnerArticle1.setSelection(MainActivity.articleListLanguage1.size() - 1);

        //Adapter2
        ArrayAdapter articleWord2Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, MainActivity.articleListLanguage2);
        articleWord2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticle2.setAdapter(articleWord2Adapter);
        spinnerArticle2.setSelection(MainActivity.articleListLanguage2.size() - 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    List<WordItem> allWordsList;
    boolean language1;
    boolean alpha;
    Integer scrollPosition;

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

        scrollPosition = 0;
        language1 = true;
        alpha = true;

        word1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(language1){
                    if(alpha){
                        alpha = false;
                        updateBrowser(4);
                    }
                    else{
                        alpha = true;
                        updateBrowser(2);
                    }
                }
                else{
                    language1 = true;
                    alpha = true;
                    updateBrowser(2);
                }
            }
        });

        word2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!language1){
                    if(alpha){
                        alpha = false;
                        updateBrowser(5);
                    }
                    else{
                        alpha = true;
                        updateBrowser(3);
                    }
                }
                else{
                    language1 = false;
                    alpha = true;
                    updateBrowser(3);
                }
            }
        });

        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), allWordsList.get(position).toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), WordDefinitionActivity.class);
                intent.putExtra("modifyMode", true);
                intent.putExtra("word1", allWordsList.get(position).getWordLanguage1());
                intent.putExtra("word2", allWordsList.get(position).getWordLanguage2());
                intent.putExtra("bookmarked", allWordsList.get(position).getBookmarked());
                scrollPosition = wordListView.getPositionForView(view);
                startActivity(intent);
            }
        });

        updateBrowser(2);
    }

    public void updateBrowser(Integer mode){
        DataBase dataBase = new DataBase(WordBrowserActivity.this, MainActivity.currentProjectName);
        allWordsList = dataBase.getAllWords(mode, null);
        ArrayAdapter wordArrayAdapter = new ArrayAdapter<WordItem>(WordBrowserActivity.this, android.R.layout.simple_list_item_1, allWordsList);
        wordListView.setAdapter(wordArrayAdapter);

        if(mode == 2 || mode == 4){
            background.setBackgroundColor(Color.parseColor("#33B5E6"));
            wordListView.setBackgroundColor(Color.parseColor("#33B5E6"));
        }
        else{
            background.setBackgroundColor(Color.parseColor("#FFBB34"));
            wordListView.setBackgroundColor(Color.parseColor("#FFBB34"));
        }
        wordNumber.setText(Integer.toString(allWordsList.size()) + " words");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(language1){
            if(alpha){
                updateBrowser(2);
            }
            else{
                updateBrowser(4);
            }
        }
        else{
            if(alpha){
                updateBrowser(3);
            }
            else{
                updateBrowser(5);
            }
        }

        wordListView.setSelection(scrollPosition);
    }
}
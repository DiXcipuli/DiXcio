package com.DiXcipuli.DiXcio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WordBrowserActivity extends AppCompatActivity {

    ListView wordListView;
    Button word1Button, word2Button;
    TextView wordNumber;
    ConstraintLayout background;
    EditText search;

    @Override
    public void onBackPressed() {
        // The position of the scroll browser is stored, then, when coming back, we have the same display, at the same scroll position
        MainActivity.browseScrollIndex = wordListView.getFirstVisiblePosition();
        View v =  wordListView.getChildAt(0);
        MainActivity.browserScrollTop = (v == null) ? 0 : (v.getTop() - wordListView.getPaddingTop());
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.currentProjectName == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_word_browser);

        wordListView = (ListView)findViewById(R.id.wordListView);
        word1Button = (Button)findViewById(R.id.language1Button);
        word2Button = (Button)findViewById(R.id.language2Button);
        wordNumber = (TextView)findViewById(R.id.wordNumber);
        background = (ConstraintLayout)findViewById(R.id.background);
        search = (EditText)findViewById(R.id.search);

        word1Button.setText(MainActivity.currentLanguage1);
        word2Button.setText(MainActivity.currentLanguage2);

        //Search bar
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { ;
                MainActivity.browseSearch = s.toString();
                if(!s.toString().isEmpty()) {
                    MainActivity.wordBrowseList = MainActivity.dataBase.getWords(9, null, s.toString());
                    BrowserAdapter wordArrayAdapter = new BrowserAdapter(getApplicationContext(), 0, MainActivity.wordBrowseList);
                    wordListView.setAdapter(wordArrayAdapter);
                    wordNumber.setText(Integer.toString(MainActivity.wordBrowseList.size()) + " words");
                }
                else{
                    if(MainActivity.browseLanguage1){
                        if(MainActivity.browseAlphabetical){
                            updateBrowser(3);
                        }
                        else{
                            updateBrowser(4);
                        }
                    }
                    else{
                        if(MainActivity.browseAlphabetical){
                            updateBrowser(5);
                        }
                        else{
                            updateBrowser(6);
                        }
                    }
                }
            }
        });

        // Allow the user to browser words accordingly to the language 1 or 2
        word1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                MainActivity.browseSearch = "";
                if(MainActivity.browseLanguage1){
                    if(MainActivity.browseAlphabetical){
                        MainActivity.browseAlphabetical = false;
                        updateBrowser(4);
                    }
                    else{
                        MainActivity.browseAlphabetical = true;
                        updateBrowser(3);
                    }
                }
                else{
                    MainActivity.browseLanguage1 = true;
                    MainActivity.browseAlphabetical = true;
                    updateBrowser(3);
                }
            }
        });

        // Allow the user to browser words accordingly to the language 1 or 2
        word2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                MainActivity.browseSearch = "";
                if(!MainActivity.browseLanguage1){
                    if(MainActivity.browseAlphabetical){
                        MainActivity.browseAlphabetical = false;
                        updateBrowser(6);
                    }
                    else{
                        MainActivity.browseAlphabetical = true;
                        updateBrowser(5);
                    }
                }
                else{
                    MainActivity.browseLanguage1 = false;
                    MainActivity.browseAlphabetical = true;
                    updateBrowser(5);
                }
            }
        });

        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), allWordsList.get(position).toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), WordDefinitionActivity.class);
                intent.putExtra("modifyMode", true);
                intent.putExtra("modifiedFromBrowser", true);
                intent.putExtra("word", MainActivity.wordBrowseList.get(position));
                MainActivity.browseScrollIndex = wordListView.getFirstVisiblePosition();
                View v =  wordListView.getChildAt(0);
                MainActivity.browserScrollTop = (v == null) ? 0 : (v.getTop() - wordListView.getPaddingTop());
                startActivity(intent);
            }
        });
    }

    public void updateBrowser(Integer mode){
        MainActivity.wordBrowseList = MainActivity.dataBase.getWords(mode, null, null);
        BrowserAdapter wordArrayAdapter = new BrowserAdapter(this,0, MainActivity.wordBrowseList);
        wordListView.setAdapter(wordArrayAdapter);
        wordNumber.setText(Integer.toString(MainActivity.wordBrowseList.size()) + " words");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(MainActivity.currentProjectName == null){
            Toast.makeText(getApplicationContext(), "Reset Security", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        else{
            search.setText(MainActivity.browseSearch);
            if(!MainActivity.browseSearch.isEmpty()){
                MainActivity.wordBrowseList = MainActivity.dataBase.getWords(9, null, MainActivity.browseSearch);
                BrowserAdapter wordArrayAdapter = new BrowserAdapter(getApplicationContext(), 0, MainActivity.wordBrowseList);
                wordListView.setAdapter(wordArrayAdapter);
                wordNumber.setText(Integer.toString(MainActivity.wordBrowseList.size()) + " words");
            }
            else {
                if (MainActivity.browseLanguage1) {
                    if (MainActivity.browseAlphabetical) {
                        updateBrowser(3);
                    } else {
                        updateBrowser(4);
                    }
                } else {
                    if (MainActivity.browseAlphabetical) {
                        updateBrowser(5);
                    } else {
                        updateBrowser(6);
                    }
                }
            }

            wordListView.setSelectionFromTop(MainActivity.browseScrollIndex, MainActivity.browserScrollTop);

            //Not really good
            MainActivity.wordHasBeenDeleted = false;

        }
    }
}
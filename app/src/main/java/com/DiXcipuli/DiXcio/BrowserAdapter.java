package com.DiXcipuli.DiXcio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//This class allows us to display nicely custom objects in a list
public class BrowserAdapter extends ArrayAdapter<WordItem> {

    private Context mContext;
    private List<WordItem> wordList = new ArrayList<>();

    // Constructor expects a list of words
    public BrowserAdapter(@NonNull Context context, int resource, @NonNull List<WordItem> objects) {
        super(context, resource, objects);

        wordList = objects;
        mContext = context;
    }

    @NonNull
    @Override
    // Set the layout for the considered word in the list, using the custom 'browser_layout'
    // Tutorial: https://medium.com/mindorks/custom-array-adapters-made-easy-b6c4930560dd
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            /* If the View is not created yet, we assign it to our custom 'browser_layout.xml'
             This layout has 3 fields:
                - Language 1
                - Language 2
                - Index
             Check the file under res/layout/browser_layout.xml
            */
            listItem = LayoutInflater.from(mContext).inflate(R.layout.browser_layout,parent,false);
        }

        //Word to display in the browser list
        WordItem currentWord = wordList.get(position);
        TextView language1 = (TextView) listItem.findViewById(R.id.language1);
        TextView language2 = (TextView) listItem.findViewById(R.id.language2);


        // Words are ordered alphabetically or reverse alpha. and according to the language 1 or 2
        if(MainActivity.browseLanguage1){ // If words are ordered according to language 1 : Language 1 displayed first, then 2
            language1.setText(currentWord.getWordLanguage1());
            language1.setBackgroundResource(MainActivity.backgroundLanguage1);      // Set background color
            language2.setText(currentWord.getWordLanguage2());
            language2.setBackgroundResource(MainActivity.backgroundLanguage2);    // Set background color
        }

        else{ // If words are ordered according to language 2 : Language 2 displayed first, then 1
            language1.setText(currentWord.getWordLanguage2());
            language1.setBackgroundResource(MainActivity.backgroundLanguage2);    // Set background color
            language2.setText(currentWord.getWordLanguage1());
            language2.setBackgroundResource(MainActivity.backgroundLanguage1);      // Set background color
        }

        TextView id = (TextView) listItem.findViewById(R.id.currentIndex);
        id.setText(Integer.toString( wordList.indexOf(currentWord) + 1));
        return listItem;
    }
}

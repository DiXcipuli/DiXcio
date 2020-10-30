package com.example.myapplication;

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

public class BrowserAdapter extends ArrayAdapter<WordItem> {

    private Context mContext;
    private List<WordItem> wordList = new ArrayList<>();

    public BrowserAdapter(@NonNull Context context, int resource, @NonNull List<WordItem> objects) {
        super(context, resource, objects);

        wordList = objects;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.browser_layout,parent,false);

        WordItem currentWord = wordList.get(position);

        if(MainActivity.browseLanguage1){
            TextView language1 = (TextView) listItem.findViewById(R.id.language1);
            language1.setText(currentWord.getWordLanguage1());
            language1.setBackgroundResource(R.drawable.blueblack);

            TextView language2 = (TextView) listItem.findViewById(R.id.language2);
            language2.setText(currentWord.getWordLanguage2());
            language2.setBackgroundResource(R.drawable.yellowblack);
        }

        else{
            TextView language1 = (TextView) listItem.findViewById(R.id.language1);
            language1.setText(currentWord.getWordLanguage2());
            language1.setBackgroundResource(R.drawable.yellowblack);

            TextView language2 = (TextView) listItem.findViewById(R.id.language2);
            language2.setText(currentWord.getWordLanguage1());
            language2.setBackgroundResource(R.drawable.blueblack);

        }

        TextView id = (TextView) listItem.findViewById(R.id.currentIndex);
        id.setText(Integer.toString( wordList.indexOf(currentWord) + 1));
        return listItem;
    }
}

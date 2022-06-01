package com.DiXcipuli.DiXcio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class NotesActivity extends AppCompatActivity {

    EditText notesEdit;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.currentProjectName == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_notes);
        db = new DataBase(NotesActivity.this, MainActivity.currentProjectName);

        notesEdit = (EditText)findViewById(R.id.editTextNotes);

        notesEdit.setText(db.getNotes());

    }

    @Override
    public void onBackPressed() {
        db.updateNotes(notesEdit.getText().toString());
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.updateNotes(notesEdit.getText().toString());
    }
}
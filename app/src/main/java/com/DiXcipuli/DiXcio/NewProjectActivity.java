package com.DiXcipuli.DiXcio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewProjectActivity extends AppCompatActivity {

    EditText newProjectName, language1, language2;
    Button saveNewProjectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        newProjectName = (EditText)findViewById(R.id.newProjectName);
        language1 = (EditText)findViewById(R.id.newProjectLanguage1);
        language2 = (EditText)findViewById(R.id.newProjectLanguage2);
        saveNewProjectButton = (Button)findViewById(R.id.newProjectSaveButton);

        saveNewProjectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(newProjectName.getText()) || TextUtils.isEmpty(language1.getText()) || TextUtils.isEmpty(language2.getText())){
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_LONG).show();
                }

                else {
                    saveProject(v);
                    createDatabase();

                }
            }
        });
    }

    public void openProjectMenuActivity(){
        Intent intent = new Intent(this, ProjectMenuActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void saveProject(final View view){
        String state;
        state = Environment.getExternalStorageState();

        boolean databaseNameContainsSpace = false;
        String projectName = newProjectName.getText().toString();
        for(int i = 0; i < projectName.length(); i++){
            if(Character.toString(projectName.charAt(i)).equals(" ")){
                    databaseNameContainsSpace = true;
            }
        }

        if(Character.isDigit(newProjectName.getText().toString().charAt(0))){
            Toast.makeText(getApplicationContext(), "Project name can't start with a digit, please modify it", Toast.LENGTH_LONG).show();
        }

        else if(databaseNameContainsSpace){
            Toast.makeText(getApplicationContext(), "Project name can't have any space", Toast.LENGTH_LONG).show();
        }

        else if(!getApplicationContext().getDatabasePath(newProjectName.getText().toString()).exists()) {

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File root = Environment.getExternalStorageDirectory();
                //File dir = new File(root.getAbsolutePath() + File.separator + R.string.app_name);
                File dir = new File(getApplicationInfo().dataDir);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir + File.separator + "DiXcioProjects.txt");
                String message = newProjectName.getText().toString() + "," + language1.getText().toString() + "," + language2.getText().toString() + "\n";

                ProjectItem pi = new ProjectItem(newProjectName.getText().toString(), language1.getText().toString(), language2.getText().toString());
                MainActivity.projectList.add(pi);

                MainActivity.currentProjectName = newProjectName.getText().toString();
                MainActivity.currentLanguage1 = language1.getText().toString();
                MainActivity.currentLanguage2 = language2.getText().toString();

                try {
                    FileOutputStream fos = new FileOutputStream(file, true);
                    fos.write(message.getBytes());
                    fos.close();

                    //set the properties for button
                    final Button newCreatedProject = new Button(this);
                    newCreatedProject.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                    newCreatedProject.setText(newProjectName.getText().toString());
                    newCreatedProject.setTransformationMethod(null);
                    //newCreatedProject.setId(MainActivity.projectCount);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                    params.setMargins(0, 8, 0, 0);
                    newCreatedProject.setLayoutParams(params);

                    newCreatedProject.setBackgroundColor(Color.parseColor("#FFBB34"));

                    newCreatedProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.currentProjectName = MainActivity.projectList.elementAt(MainActivity.layout.indexOfChild(v) - 1).getProjectName();
                            MainActivity.currentLanguage1 = MainActivity.projectList.elementAt(MainActivity.layout.indexOfChild(v) - 1).getLanguage1();
                            MainActivity.currentLanguage2 = MainActivity.projectList.elementAt(MainActivity.layout.indexOfChild(v) - 1).getLanguage2();

                            Intent intent = new Intent(getApplicationContext(), ProjectMenuActivity.class);
                            startActivity(intent);

                        }
                    });

                    newProjectName.setText("");
                    language1.setText("");
                    language2.setText("");

                    //add button to the layout
                    MainActivity.layout.addView(newCreatedProject);

                    Toast.makeText(getApplicationContext(), "Project Saved", Toast.LENGTH_LONG).show();

                    openProjectMenuActivity();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        else{//Project name/db already exists
            Toast.makeText(getApplicationContext(), "Project/database name already exists", Toast.LENGTH_LONG).show();
        }
    }

    public void createDatabase(){
        DataBase dataBase = new DataBase(NewProjectActivity.this, MainActivity.currentProjectName);
    }
}
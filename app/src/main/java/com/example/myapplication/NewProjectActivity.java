package com.example.myapplication;

import android.content.Intent;
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
                    openNewWordActivity();

                }
            }
        });
    }

    public void openNewWordActivity(){
        Intent intent = new Intent(this, ProjectMenuActivity.class);
        startActivity(intent);
    }

    public void saveProject(final View view){
        String state;
        state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + File.separator +  R.string.app_name);

            if(!dir.exists()){
                dir.mkdirs();
            }

            File file = new File(dir + File.separator + R.string.app_name +  ".txt");
            String message = newProjectName.getText().toString() + "," + language1.getText().toString() + "," + language2.getText().toString() + "\n";

            ProjectItem pi = new ProjectItem(newProjectName.getText().toString(), language1.getText().toString(), language2.getText().toString());
            MainActivity.projectList.add(pi);

            MainActivity.currentProjectName = newProjectName.getText().toString();
            MainActivity.currentLanguage1 = language1.getText().toString();
            MainActivity.currentLanguage2 = language2.getText().toString();

            try{
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(message.getBytes());
                fos.close();


                //set the properties for button
                final Button newCreatedProject = new Button(this);
                newCreatedProject.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                newCreatedProject.setText(newProjectName.getText().toString());
                newCreatedProject.setId(MainActivity.projectCount);

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
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void createDatabase(){
        DataBase dataBase = new DataBase(NewProjectActivity.this, MainActivity.currentProjectName);
    }
}
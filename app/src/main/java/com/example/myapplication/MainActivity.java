package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    public static LinearLayout layout;
    public static String currentProjectName, currentLanguage1, currentLanguage2;
    public static Vector<ProjectItem>  projectList= new Vector<ProjectItem>();
    public static boolean wordHasBeenDeleted = false;

    public static DataBase dataBase;
    public static List<WordItem> wordTrainList;
    public static List<WordItem> wordBrowseList;
    public static Integer trainIndex = 0;

    //static variable to set the train activity
    public static boolean isGuessModeLanguage1;
    public static boolean isCurrentCardLanguage1;
    public static Integer modeSpinnerIndex = 0;
    public static Integer numberSpinnerIndex = 0;

    //static variable to set the browse activity
    public static Integer browseScrollIndex = 0;
    public static Integer browserScrollTop = 0;
    public static String browseSearch = "";
    public static boolean browseLanguage1 = true;
    public static boolean browseAlphabetical = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newProjectButton = (Button) findViewById(R.id.new_project);
        layout = (LinearLayout) findViewById(R.id.projectLayout);

        newProjectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondActivity();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        loadProject();
    }

    public void openSecondActivity(){
        Intent intent = new Intent(this, NewProjectActivity.class);
        startActivity(intent);
    }

    public void loadProject() {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + File.separator +  R.string.app_name);
        File file = new File(dir + File.separator + R.string.app_name +  ".txt");

        String message;
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            projectList.clear();

            for(int i = 1; i < layout.getChildCount(); i++){
                layout.removeViewAt(i);
            }

            while((message = br.readLine()) != null){
                //sb.append(message + "\n");

                String[] splittedMessage = message.split(",");
                ProjectItem pi = new ProjectItem(splittedMessage[0], splittedMessage[1], splittedMessage[2]);
                MainActivity.projectList.add(pi);

                //set the properties for button
                Button newCreatedProject = new Button(this);
                newCreatedProject.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                newCreatedProject.setText(splittedMessage[0]);
                newCreatedProject.setBackgroundColor(Color.parseColor("#FFBB34"));
                newCreatedProject.setTransformationMethod(null);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                params.setMargins(0, 8, 0, 0);
                newCreatedProject.setLayoutParams(params);
                //newCreatedProject.setId(R.id.project1Button);

                newCreatedProject.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentProjectName = projectList.elementAt( layout.indexOfChild(v) - 1).getProjectName();
                        currentLanguage1 = projectList.elementAt(layout.indexOfChild(v) - 1).getLanguage1();
                        currentLanguage2 = projectList.elementAt(layout.indexOfChild(v) - 1).getLanguage2();

                        Intent intent = new Intent(getApplicationContext(), ProjectMenuActivity.class);
                        startActivity(intent);
                    }
                });

                //add button to the layout
                layout.addView(newCreatedProject);
            }

            


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch(IOException e){
            e.printStackTrace();
        }
    }
}
package com.DiXcipuli.DiXcio;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    public static String appFolderName =  "DiXcio"; //Used to save the catlogs and the exported csv -> storage/emulated/0/DiXcio
    public static String projectsInfoFile = "DiXcioProjects.txt";
    public static LinearLayout layout;
    public static String currentProjectName, currentLanguage1, currentLanguage2;
    public static Vector<ProjectItem>  projectList= new Vector<ProjectItem>();
    public static boolean wordHasBeenDeleted = false;

    public static DataBase dataBase;
    public static List<WordItem> wordTrainList;
    public static List<WordItem> wordBrowseList;
    public static Integer trainIndex = 0;

    //static variable to set the train activity
    public static boolean isGuessModeLanguage1 = true;
    public static boolean isCurrentCardLanguage1 = true;
    public static Integer modeSpinnerIndex = 0;
    public static Integer numberSpinnerIndex = 2;

    //static variable to set the browse activity
    public static Integer browseScrollIndex = 0;
    public static Integer browserScrollTop = 0;
    public static String browseSearch = "";
    public static boolean browseLanguage1 = true;
    public static boolean browseAlphabetical = true;

    public static int backgroundLanguage1 = R.drawable.blueblack;
    public static int backgroundLanguage2 = R.drawable.yellowblack;


    public static void resetToDefault(){
        trainIndex = 0;
        isGuessModeLanguage1 = true;
        isCurrentCardLanguage1 = true;
        modeSpinnerIndex = 0;
        numberSpinnerIndex = 2;
        browseScrollIndex = 0;
        browserScrollTop = 0;
        browseSearch = "";
        browseLanguage1 = true;
        browseAlphabetical = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newProjectButton = (Button) findViewById(R.id.new_project);
        layout = (LinearLayout) findViewById(R.id.projectLayout);

        newProjectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newProjectActivity();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // WRITE PERMISSION also gives READ PERMISSION
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            else{
                //generateCatLog();
            }
        }

        loadProject();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //generateCatLog();
        }
        else{
            // Quit the app
            this.finish();
            System.exit(0);
        }
    }

    public void generateCatLog(){
        // Environment.getExternalStorageDirectory() returns /storage/emulated/0 , which is the folder where Documents, Downloads... are.
        File appDirectory = new File( Environment.getExternalStorageDirectory() +  File.separator + appFolderName );
        File logDirectory = new File( appDirectory +  File.separator + "logs" );
        File logFile = new File( logDirectory, "logcat_" + System.currentTimeMillis() + ".txt" );

        // create app folder
        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        // create log folder
        if ( !logDirectory.exists() ) {
            logDirectory.mkdir();
        }

        // clear the previous logcat and then write the new one to the file
        try {
            Process process = Runtime.getRuntime().exec("logcat -c");
            process = Runtime.getRuntime().exec("logcat -f " + logFile);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void newProjectActivity(){
        Intent intent = new Intent(this, NewProjectActivity.class);
        startActivity(intent);
    }

    // Loading all the project stored under /data/user/0/com.app.name
    // This path is not accessible from the device explorer though
    public void loadProject() {
        File dir = new File(getApplicationInfo().dataDir);      // Returns /data/user/0/com.app.name
        File file = new File(dir + File.separator +  projectsInfoFile);

        String message;
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();

            // Clear the project list, and delete each "Button Project"
            projectList.clear();
            for(int i = 1; i < layout.getChildCount(); i++){
                layout.removeViewAt(i);
            }

            while((message = br.readLine()) != null){
                // Each line in the text file contains: title, language1, language2
                String[] splittedMessage = message.split(",");

                //A ProjectItem contains a Title, and Language1 and a Language2 fields
                ProjectItem pi = new ProjectItem(splittedMessage[0], splittedMessage[1], splittedMessage[2]);
                MainActivity.projectList.add(pi);

                // Create a new button to access the loaded project
                Button newCreatedProject = new Button(this);
                newCreatedProject.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                newCreatedProject.setText(splittedMessage[0]);
                newCreatedProject.setBackgroundColor(Color.parseColor("#FFBB34"));
                newCreatedProject.setTransformationMethod(null);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                params.setMargins(0, 8, 0, 0);
                newCreatedProject.setLayoutParams(params);

                // When clicked, Open the corresponding project menu, and update the vars, like currentProjectName
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
package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteDialogListener {
    Button deleteProjectsButton;
    public static LinearLayout layout;
    public static String currentProjectName, currentLanguage1, currentLanguage2;
    public static Integer projectCount = 1;
    public static Vector<ProjectItem>  projectList= new Vector<ProjectItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quitButton = (Button) findViewById(R.id.quit_app);
        Button newProjectButton = (Button) findViewById(R.id.new_project);
        deleteProjectsButton = (Button)findViewById(R.id.deleteProjectsButton);
        layout = (LinearLayout) findViewById(R.id.projectLayout);

        quitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        deleteProjectsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

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

    public void openDialog(){
        DeleteDialog dialog = new DeleteDialog();
        dialog.show(getSupportFragmentManager(), "Delete Dialog");
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

            while((message = br.readLine()) != null){
                //sb.append(message + "\n");

                String[] splittedMessage = message.split(",");
                ProjectItem pi = new ProjectItem(splittedMessage[0], splittedMessage[1], splittedMessage[2]);
                MainActivity.projectList.add(pi);

                //set the properties for button
                Button newCreatedProject = new Button(this);
                newCreatedProject.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                newCreatedProject.setText(splittedMessage[0]);
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

    @Override
    public void onYesClicked() {
        Integer count = layout.getChildCount();
        for(Integer i = 1; i < count; i++){
            layout.removeViewAt(1);
        }

        projectList.clear();

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + File.separator +  R.string.app_name);
        File file = new File(dir + File.separator + R.string.app_name +  ".txt");
        file.delete();
    }
}
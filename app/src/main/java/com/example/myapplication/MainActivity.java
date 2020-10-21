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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteDialogListener {
    Button deleteProjectsButton;
    public static LinearLayout layout;
    public static String currentProjectName, currentLanguage1, currentLanguage2;
    public static Integer projectCount = 1;
    public static Vector<ProjectItem>  projectList= new Vector<ProjectItem>();
    public static Map<String, Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setMap();
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
                newCreatedProject.setBackgroundColor(Color.parseColor("#FFBB34"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                params.setMargins(0, 4, 0, 0);
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

    @Override
    public void onYesClicked() {
        Integer count = layout.getChildCount();
        for(Integer i = 1; i < count; i++){
            layout.removeViewAt(1);
            this.deleteDatabase(projectList.elementAt(i-1).getProjectName());
        }

        projectList.clear();

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + File.separator +  R.string.app_name);
        File file = new File(dir + File.separator + R.string.app_name +  ".txt");
        file.delete();
    }

    public void setMap(){
        map = new HashMap<String, Integer>();
        map.put("a", 0);
        map.put("b", 1);
        map.put("c", 2);
        map.put("d", 3);
        map.put("e", 4);
        map.put("f", 5);
        map.put("g", 6);
        map.put("h", 7);
        map.put("i", 8);
        map.put("j", 9);
        map.put("k", 10);
        map.put("l", 11);
        map.put("m", 12);
        map.put("n", 13);
        map.put("o", 14);
        map.put("p", 15);
        map.put("q", 16);
        map.put("r", 17);
        map.put("s", 18);
        map.put("t", 19);
        map.put("u", 20);
        map.put("v", 21);
        map.put("w", 22);
        map.put("x", 23);
        map.put("y", 24);
        map.put("z", 25);
        map.put("A", 0);
        map.put("B", 1);
        map.put("C", 2);
        map.put("D", 3);
        map.put("E", 4);
        map.put("F", 5);
        map.put("G", 6);
        map.put("H", 7);
        map.put("I", 8);
        map.put("J", 9);
        map.put("K", 10);
        map.put("L", 11);
        map.put("M", 12);
        map.put("N", 13);
        map.put("O", 14);
        map.put("P", 15);
        map.put("Q", 16);
        map.put("R", 17);
        map.put("S", 18);
        map.put("T", 19);
        map.put("U", 20);
        map.put("V", 21);
        map.put("W", 22);
        map.put("X", 23);
        map.put("Y", 24);
        map.put("Z", 25);
        map.put("à", 0);
        map.put("â", 0);
        map.put("é", 4);
        map.put("è", 4);
        map.put("ê", 25);
        map.put("ç", 2);
    }
}
package com.example.myapplication;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportCsvActivity extends AppCompatActivity {

    Button exportCsvButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_csv);

        exportCsvButton = (Button)findViewById(R.id.exportCsvButton);

        exportCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDir = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                    DataBase dataBase = new DataBase(getApplicationContext(), MainActivity.currentProjectName);
                    List<WordItem> wordList = dataBase.getAllWords(1, null);

                    mDir = Environment.DIRECTORY_DOCUMENTS;
                    File root = Environment.getExternalStoragePublicDirectory(mDir);

                    if (!root.exists()) {
                        root.mkdirs();
                    }

                    File dir = new File(root.getAbsolutePath() + File.separator + "DiXcio");

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File file = new File(dir + File.separator + MainActivity.currentProjectName + ".csv");

                    String message = "";

                    for(int i =0; i<wordList.size(); i++){
                        message = message + wordList.get(i).toCSV();
                    }

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        fos.write(message.getBytes());
                        fos.close();
                        Toast.makeText(getApplicationContext(), "Export Successful! stored at " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Export Failed", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Export Failed", Toast.LENGTH_LONG).show();
                    }


                    scanFile(file, "text/csv");


                }

            }
        });
    }

    public void scanFile(File f, String mimeType) {
        MediaScannerConnection
                .scanFile(getApplicationContext(), new String[] {f.getAbsolutePath()},
                        new String[] {mimeType}, null);
    }
}
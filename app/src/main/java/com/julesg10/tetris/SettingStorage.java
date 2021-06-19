package com.julesg10.tetris;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingStorage {

    private final String STORAGE_NAME = "settings.txt";
    private Context context;
    private String[] dataArray;
    private boolean isload = false;

    public SettingStorage(Context ctx)
    {
        this.dataArray = new String[]{"false","false","false"};

        this.context = ctx;
    }

    public void set(int index,String value)
    {
        if(index  >= 0 && index < this.dataArray.length)
        {
            this.dataArray[index] = value;
        }
    }

    public String get(int index)
    {
        if(index  >= 0 && index < this.dataArray.length)
        {
            return this.dataArray[index];
        }

        return "";
    }

    public void save()
    {
        FileOutputStream fos = null;
        try {
            fos = this.context.openFileOutput(STORAGE_NAME, this.context.MODE_PRIVATE);
            String data = this.dataArray.toString();
            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public boolean load()
    {
        FileInputStream fis = null;
        try {
            fis = this.context.openFileInput(STORAGE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            };
            this.dataArray = sb.toString().split("\n");
            this.isload = true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }

        return this.isload;
    }
}

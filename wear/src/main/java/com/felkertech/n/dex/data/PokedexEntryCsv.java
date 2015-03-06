package com.felkertech.n.dex.data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by N on 12/30/2014.
 */
public class PokedexEntryCsv extends ParsedCsv {
    public static final String TAG = "PokedexEntryCsv";
    public PokedexEntryCsv(InputStream is) {
        super();
        Log.d(TAG, "Open");
        int i = -1;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            reader.reset();
            String line;
            Log.d(TAG, "Start iterating");
            Log.d(TAG, reader.ready() + "");
//            Log.d(TAG, reader.readLine());
            while ((line = reader.readLine()) != null) {
//                Log.d(TAG, line);
                String[] RowData = line.split(",");
//                Log.d(TAG, RowData.length+"");
                if(RowData.length == 4) {
                    i++;
                    mtx.add(new CsvRow());
                    for (String row : RowData) {
                        mtx.get(i).add(row);
                    }
                } else {
                    //Add on to previous row
                    mtx.get(i).al().set(3, mtx.get(i).al().get(3) + " " + line);
                }
            }
        }
        catch (IOException ex) {
            // handle exception
            ex.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                // handle exception
            }
            fieldNames = mtx.get(0);
        }
    }
}
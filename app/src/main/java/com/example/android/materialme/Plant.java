/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.materialme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Data model for each row of the RecyclerView
 */
class Plant {

    // Member variables representing the title and information about the sport.
    final private String name;
    final private String imageURL;
    final private String area;
    final private String engName;
    Context context;
    String id;
    int position;
    public Bitmap bitmap;
    public static Map<String, Plant> plants = new HashMap<String, Plant>();

    /**
     * Constructor for the Sport data model.
     *
     * @param name     The name if the sport.
     * @param imageURL Information about the sport.
     */
    public Plant(String name, String imageURL, String engName, String area, Context context, String id) {
        this.name = name;
        this.imageURL = imageURL;
        this.area = area;
        this.engName = engName;
        this.context = context;
        this.id = id;
        plants.put(imageURL, this);
    }

    /**
     * Gets the title of the sport.
     *
     * @return The title of the sport.
     */
    String getName() {
        return name;
    }

    String getArea() {
        return area;
    }

    String getEngName() {
        return engName;
    }

    /**
     * Gets the info about the sport.
     *
     * @return The info about the sport.
     */
    String getImageURL() {
        return imageURL;
    }


    public void fetchImage() {
        if (this.bitmap != null)
            return;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(id);
            this.bitmap = BitmapFactory.decodeStream(fileInputStream);
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a BookLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {
            InputStream is=null;
            FileOutputStream os=null;
            try {
                URL url = new URL(this.imageURL);
                is = url.openStream();
                os = context.openFileOutput(id, Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];
                int len;
                while(true){
                    len = is.read(buf);
                    Log.d("DEBUG", "len "+len);
                    if (len > 0){
                        os.write(buf,0,len);
                    }else{
                        break;
                    }
                }
                fetchImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (is!=null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(os!=null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

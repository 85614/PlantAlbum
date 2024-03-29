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
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/***
 * Main Activity for the Material Me app, a mock sports news application.
 */
public class MainActivity extends CallbackAppCompatActivity {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Plant> mSportsData;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ArrayList that will contain the data.
        mSportsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new MainAdapter(this, mSportsData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();

        try {
            FileInputStream fis;
            fis = openFileInput("temp");
            byte[] buf = new byte[128];
            int len = fis.read(buf);
            Log.d("DEBUG", "read file success " + new String(buf, 0, len));

            FileOutputStream fos = openFileOutput("temp", Context.MODE_PRIVATE);
            fos.write("hello".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DEBUG", "read file fail "+ e);
        }
        finally {
            Log.d("DEBUG", "read file finally");
        }

    }

    /**
     * Initialize the sports data from resources.
     */
    private void initializeData() {
        JSONObject json = null;
        try {
            String s = getResources().getString(R.string.types);
            json = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "json " + json);
        JSONArray plantArray = null;
        try {
            plantArray = json.getJSONObject("result").getJSONArray("plantList");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int l = plantArray.length();
        String[] sportsList = new String[l];
        String[] sportsInfo = new String[l];
        // Get the resources from the XML file.
//        String[] sportsList = getResources()
//                .getStringArray(R.array.sports_titles);
//        String[] sportsInfo = getResources()
//                .getStringArray(R.array.sports_info);
//        TypedArray sportsImageResources = getResources()
//                .obtainTypedArray(R.array.sports_images);

        // Clear the existing data (to avoid duplication).
        mSportsData.clear();

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport
        for (int i = 0; i < sportsList.length; i++) {
            try {
                JSONObject o = plantArray.getJSONObject(i);
                sportsList[i] = o.getString("name");
                sportsInfo[i] = o.getString("coverURL");
                String engName = o.getString("engName");
                String area = o.getString("area");
                Plant s = new Plant(sportsList[i], sportsInfo[i],
                        engName != null && !engName.isEmpty() ? "English name: " + engName : "",
                        area != null && !area.isEmpty() ? "area: " + area : "",
                        this, o.getString("plantID"));
                s.position = i;
                s.bitmap = null;
                mSportsData.add(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Recycle the typed array.
//        sportsImageResources.recycle();

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

    /**
     * onClick method for th FAB that resets the data.
     *
     * @param view The button view that was clicked.
     */
    public void resetSports(View view) {
        initializeData();
    }
}

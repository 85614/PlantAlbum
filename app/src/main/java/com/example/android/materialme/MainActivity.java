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

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/***
 * Main Activity for the Material Me app, a mock sports news application.
 */
public class MainActivity extends CallbackAppCompatActivity {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Plant> mSportsData;
    private SportsAdapter mAdapter;

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
        mAdapter = new SportsAdapter(this, mSportsData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();

        // Helper class for creating swipe to dismiss and drag and drop
        // functionality.
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | 
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            /**
             * Defines the drag and drop functionality.
             *
             * @param recyclerView The RecyclerView that contains the list items
             * @param viewHolder The SportsViewHolder that is being moved
             * @param target The SportsViewHolder that you are switching the
             *               original one with.
             * @return true if the item was moved, false otherwise
             */
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                // Get the from and to positions.
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                // Swap the items and notify the adapter.
                Collections.swap(mSportsData, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            /**
             * Defines the swipe to dismiss functionality.
             *
             * @param viewHolder The viewholder being swiped.
             * @param direction The direction it is swiped in.
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                // Remove the item from the dataset.
                mSportsData.remove(viewHolder.getAdapterPosition());
                // Notify the adapter.
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        // Attach the helper to the RecyclerView.
        helper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Initialize the sports data from resources.
     */
    private void initializeData() {

        JSONObject json = null;
        try {
            String s = "{\n" +
                    "  \"statusCode\":\"000000\",\n" +
                    "  \"desc\":\"请求成功\",\n" +
                    "  \"result\":{\n" +
                    "      \"plantList\":[\n" +
                    "          {\n" +
                    "              \"area\":\"西南、华南。原产美洲的沙漠地带\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick37971445246358.jpg\",\n" +
                    "              \"engName\":\"Agave americanavar. Marginata\",\n" +
                    "              \"name\":\"金边龙舌兰\",\n" +
                    "              \"plantID\":1\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick97441452591977.jpg\",\n" +
                    "              \"engName\":\"Renanthera coccinea Lour.\",\n" +
                    "              \"name\":\"火焰兰\",\n" +
                    "              \"plantID\":5\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"原产欧洲南部\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick64291438766891.jpg\",\n" +
                    "              \"engName\":\"Matthiola incana\",\n" +
                    "              \"name\":\"紫罗兰\",\n" +
                    "              \"plantID\":18\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick6521447234735.jpg\",\n" +
                    "              \"engName\":\"Magnolia grandiflora L.\",\n" +
                    "              \"name\":\"荷花玉兰\",\n" +
                    "              \"plantID\":26\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"东南亚\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick7191452763089.jpg\",\n" +
                    "              \"engName\":\"Paphiopedilum hirsutissimum\",\n" +
                    "              \"name\":\"带叶兜兰\",\n" +
                    "              \"plantID\":37\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick88131443519815.jpg\",\n" +
                    "              \"engName\":\"Magnolia liliflora Desr.\",\n" +
                    "              \"name\":\"紫玉兰\",\n" +
                    "              \"plantID\":110\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"南美各国，中国栽培广泛\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick65321448617401.jpg\",\n" +
                    "              \"engName\":\"Zephyranthes candida （Lindl.）Herb.\",\n" +
                    "              \"name\":\"葱兰\",\n" +
                    "              \"plantID\":119\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick23941450172150.jpg\",\n" +
                    "              \"engName\":\"Michelia champaca Linn.\",\n" +
                    "              \"name\":\"黄兰\",\n" +
                    "              \"plantID\":126\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"非洲东部\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick12051451555061.jpg\",\n" +
                    "              \"engName\":\"Saintpaulia ionantha Wendl\",\n" +
                    "              \"name\":\"非洲紫罗兰\",\n" +
                    "              \"plantID\":137\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"中国大陆的广州、杭州、昆明等地\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick50411452244725.jpg\",\n" +
                    "              \"engName\":\"Magnolia soulangeana Soul.-Bod.\",\n" +
                    "              \"name\":\"二乔木兰\",\n" +
                    "              \"plantID\":176\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick75601452677466.jpg\",\n" +
                    "              \"engName\":\"Cymbidium lowianum （Rchb. f.） Rchb. f.\",\n" +
                    "              \"name\":\"碧玉兰\",\n" +
                    "              \"plantID\":178\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"原产于长江流域，伏牛山、庐山、黄山、峨眉山等处有野生\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick81361441791493.jpg\",\n" +
                    "              \"engName\":\"Magnolia\",\n" +
                    "              \"name\":\"玉兰\",\n" +
                    "              \"plantID\":236\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"原产北美东部和东南部。中国长江流域及以南和山东，河南有引种\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick13461444899797.jpg\",\n" +
                    "              \"engName\":\"Yucca gloriosa L\",\n" +
                    "              \"name\":\"凤尾兰\",\n" +
                    "              \"plantID\":239\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"巴西、美国、哥伦比亚、厄瓜多尔及秘鲁等国家\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick57221451983623.jpg\",\n" +
                    "              \"engName\":\"Dancing－Doll Orchid\",\n" +
                    "              \"name\":\"文心兰\",\n" +
                    "              \"plantID\":259\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"广西西南部、四川西南部、贵州西南部、云南和西藏东南部\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick85241440496118.jpg\",\n" +
                    "              \"engName\":\"Cymbidium\",\n" +
                    "              \"name\":\"大花蕙兰\",\n" +
                    "              \"plantID\":261\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"原产热带美洲\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick33941452156679.jpg\",\n" +
                    "              \"engName\":\"Cattleya Hybrida\",\n" +
                    "              \"name\":\"卡特兰\",\n" +
                    "              \"plantID\":276\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"非洲南部好望角\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick54011451382522.jpg\",\n" +
                    "              \"engName\":\"Freesia\",\n" +
                    "              \"name\":\"小苍兰\",\n" +
                    "              \"plantID\":286\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"非洲南部，美国，德国，意大利，荷兰，菲律宾，中国\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick23051440409379.jpg\",\n" +
                    "              \"engName\":\"Strelitzia reginae Aiton\",\n" +
                    "              \"name\":\"鹤望兰\",\n" +
                    "              \"plantID\":291\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"原产于我国中部，现在各省均有栽培\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick43961441533674.jpg\",\n" +
                    "              \"engName\":\"magnolia\",\n" +
                    "              \"name\":\"木兰花\",\n" +
                    "              \"plantID\":312\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"中国、日本、朝鲜半岛南端等\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick36641442223694.jpg\",\n" +
                    "              \"engName\":\"Cymbidium goeringii （Rchb. f.） Rchb. f.\",\n" +
                    "              \"name\":\"春兰\",\n" +
                    "              \"plantID\":320\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"北美\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick32821440064459.jpg\",\n" +
                    "              \"engName\":\"New York Aster\",\n" +
                    "              \"name\":\"荷兰菊\",\n" +
                    "              \"plantID\":323\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"原产非洲南部，在世界各地广为栽培。\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick62441443000755.jpg\",\n" +
                    "              \"engName\":\"string－of－pearls\",\n" +
                    "              \"name\":\"珍珠吊兰\",\n" +
                    "              \"plantID\":329\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick11801442224359.jpg\",\n" +
                    "              \"engName\":\"Convallaria majalis Linn.\",\n" +
                    "              \"name\":\"铃兰\",\n" +
                    "              \"plantID\":330\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick27111451295168.jpg\",\n" +
                    "              \"engName\":\"Neofinetia falcata （Thunb. ex A. Murray） H. H. Hu\",\n" +
                    "              \"name\":\"风兰\",\n" +
                    "              \"plantID\":381\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"原产热带、亚洲。中国台湾、广东、福建等省有野生\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick15451451467914.jpg\",\n" +
                    "              \"engName\":\"Crinum asiaticum L. var. sinicum （Roxb. ex Herb.） \",\n" +
                    "              \"name\":\"文殊兰\",\n" +
                    "              \"plantID\":391\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"印度、马来西亚、菲律宾、中国南方以及澳大利亚北方\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick4441448444051.jpg\",\n" +
                    "              \"engName\":\"Vanda\",\n" +
                    "              \"name\":\"万代兰\",\n" +
                    "              \"plantID\":399\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"墨西哥\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick33841444468734.jpg\",\n" +
                    "              \"engName\":\"Beaucarnea recurvata Lem.\",\n" +
                    "              \"name\":\"酒瓶兰\",\n" +
                    "              \"plantID\":403\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"非洲及亚洲南部，中国各地有栽培\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick29661442482550.jpg\",\n" +
                    "              \"engName\":\"Sansevieria trifasciata Prain\",\n" +
                    "              \"name\":\"虎尾兰\",\n" +
                    "              \"plantID\":407\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick24831441098711.jpg\",\n" +
                    "              \"engName\":\"Phalaenopsis\",\n" +
                    "              \"name\":\"蝴蝶兰\",\n" +
                    "              \"plantID\":431\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"area\":\"\",\n" +
                    "              \"coverURL\":\"http://img.boqiicdn.com/Data/BK/P/imagick34441444469318.jpg\",\n" +
                    "              \"engName\":\"Hoya carnosa （L. f.） R. Br.\",\n" +
                    "              \"name\":\"球兰\",\n" +
                    "              \"plantID\":436\n" +
                    "          }\n" +
                    "      ],\n" +
                    "      \"totalCount\":44\n" +
                    "  }\n" +
                    "}";
            json = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "json " + json);
        JSONArray plantArray = null ;
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
                sportsList[i] = plantArray.getJSONObject(i).getString("name");
                sportsInfo[i] = plantArray.getJSONObject(i).getString("coverURL");
                Plant s = new Plant(sportsList[i], sportsInfo[i], 0);
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

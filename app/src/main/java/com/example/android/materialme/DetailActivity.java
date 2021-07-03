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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;

/***
 * Detail Activity that is launched when a list item is clicked.
 * It shows more info on the sport.
 */
public class DetailActivity extends CallbackAppCompatActivity {

    /**
     * Initializes the activity, filling in the data from the Intent.
     *
     * @param savedInstanceState Contains information about the saved state
     *                           of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize the views.
        TextView sportsTitle = findViewById(R.id.titleDetail);
        final ImageView sportsImage = findViewById(R.id.sportsImageDetail);

        // Set the text from the Intent extra.
        sportsTitle.setText(getIntent().getStringExtra("title"));

        // Load the image using the Glide library and the Intent extra.
//        Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
//                .into(sportsImage);

        final String queryString =  getIntent().getStringExtra("image_url");
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a BookLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            Log.d("DEBUG", "network ok!");
            Bundle queryBundle = new Bundle();
            final Context c =  this;
            getSupportLoaderManager().restartLoader(0, queryBundle, new MyCallBack(this, new Supplier<Function<Object, Object>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public Function<Object, Object> get() {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(new URL(queryString).openStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final Bitmap finalBitmap = bitmap;
                    return new Function<Object, Object>(){
                        @Override
                        public Object apply(Object o) {
                            if (finalBitmap !=null)
                                sportsImage.setImageBitmap(finalBitmap);
                            return o;
                        }
                    };

                }
            }));
        }
    }
}

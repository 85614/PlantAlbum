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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Data model for each row of the RecyclerView
 */
class Plant {

    // Member variables representing the title and information about the sport.
    private String name;
    private String imageURL;
    private final int imageResource;

    int position;
    public Bitmap bitmap;
    public static Map<String, Plant> sports = new HashMap<String, Plant>();
    /**
     * Constructor for the Sport data model.
     *
     * @param name The name if the sport.
     * @param imageURL Information about the sport.
     */
    public Plant(String name, String imageURL, int imageResource) {
        this.name = name;
        this.imageURL = imageURL;
        this.imageResource = imageResource;
    }

    /**
     * Gets the title of the sport.
     *
     * @return The title of the sport.
     */
    String getName() {
        return name;
    }

    /**
     * Gets the info about the sport.
     *
     * @return The info about the sport.
     */
    String getImageURL() {
        return imageURL;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void fetchImage()
    {
        if (this.bitmap!=null)
            return;
        try {
            URL url = new URL(this.imageURL);
            this.bitmap = BitmapFactory.decodeStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

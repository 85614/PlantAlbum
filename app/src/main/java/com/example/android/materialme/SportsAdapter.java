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

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

/***
 * The adapter class for the RecyclerView, contains the sports data.
 */
class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder>  {

    // Member variables.
    private ArrayList<Plant> mSportsData;
    private CallbackAppCompatActivity mContext;

    /**
     * Constructor that passes in the sports data and the context.
     *
     * @param sportsData ArrayList containing the sports data.
     * @param context Context of the application.
     */
    SportsAdapter(CallbackAppCompatActivity context, ArrayList<Plant> sportsData) {
        this.mSportsData = sportsData;
        this.mContext = context;
    }


    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent The ViewGroup into which the new View will be added
     *               after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */
    @Override
    public SportsAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false));
    }

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(SportsAdapter.ViewHolder holder,
                                 int position) {
        // Get current sport.
        Plant currentPlant = mSportsData.get(position);
        Log.d("DEBUG",
                "bindTo currentSport.position="+ currentPlant.position + ", position="+ position
        + " has_bitmap="+(currentPlant.bitmap!=null));
        // Populate the textviews with data.
        holder.bindTo(currentPlant);
    }

    /**
     * Required method for determining the size of the data set.
     *
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return mSportsData.size();
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mSportsImage;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         *
         * @param itemView The rootview of the list_item.xml layout file.
         */
        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.title);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mSportsImage = itemView.findViewById(R.id.sportsImage);

            // Set the OnClickListener to the entire view.
            itemView.setOnClickListener(this);
        }

        void bindTo(final Plant currentPlant){
            if (currentPlant.bitmap !=null) {
                mSportsImage.setImageBitmap(currentPlant.bitmap);
                mTitleText.setText(currentPlant.getName());
                mInfoText.setText(currentPlant.getImageURL());
                return;
            }
            // Populate the textviews with data.
            mTitleText.setText(currentPlant.getName());
            mInfoText.setText(currentPlant.getImageURL());

            // Load the images into the ImageView using the Glide library.
//            Glide.with(mContext).load(
//                    currentSport.getImageResource()).into(mSportsImage);
            mContext.call(mContext, new Supplier<Function<Object, Object>>() {
                @Override
                public Function<Object, Object> get() {
                    currentPlant.fetchImage();
                    return new Function<Object, Object>() {
                        @Override
                        public Object apply(Object o) {
                            if (currentPlant.bitmap != null) {
                                SportsAdapter.this.notifyItemChanged(currentPlant.position);
                                Log.d("DEBUG", "notifyItemChanged(" + currentPlant.position +  ")");
                            }
                            return o;
                        }
                    };
                }
            });
        }

        /**
         * Handle click to show DetailActivity.
         *
         * @param view View that is clicked.
         */
        @Override
        public void onClick(View view) {
            Plant currentPlant = mSportsData.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra("title", currentPlant.getName());
            detailIntent.putExtra("image_url", currentPlant.getImageURL());
            detailIntent.putExtra("image_resource",
                    currentPlant.getImageResource());

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) mContext,
                            mSportsImage, "switch");

            mContext.startActivity(detailIntent, options.toBundle());
        }
    }
}

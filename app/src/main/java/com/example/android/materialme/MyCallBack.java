package com.example.android.materialme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

class MyCallBack implements LoaderManager.LoaderCallbacks<Function<Object, Object>> {
    final private Supplier<Function<Object, Object>> task;
    Context context;

    MyCallBack(Context context, Supplier<Function<Object, Object>> task) {
        this.task = task;
        this.context = context;
    }

    public static void call(AppCompatActivity context, Supplier<Function<Object, Object>> task) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a BookLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("DEBUG", "network ok!");
            Bundle queryBundle = new Bundle();

            context.getSupportLoaderManager().restartLoader(0, queryBundle, new MyCallBack(context, task));
        }
    }

    @Override
    public Loader<Function<Object, Object>> onCreateLoader(int id, Bundle args) {
        return new MyAsyncTaskLoader(context, task);
    }

    @Override
    public void onLoadFinished(Loader<Function<Object, Object>> loader, Function<Object, Object> data) {
        data.apply(null);
    }

    @Override
    public void onLoaderReset(Loader<Function<Object, Object>> loader) {

    }

    static class MyAsyncTaskLoader extends AsyncTaskLoader<Function<Object, Object>> {
        private final Supplier<Function<Object, Object>> supplier;

        public MyAsyncTaskLoader(Context context, Supplier<Function<Object, Object>> supplier) {
            super(context);
            this.supplier = supplier;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            forceLoad();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Nullable
        @Override
        public Function<Object, Object> loadInBackground() {
            return supplier.get();
        }

    }
}


abstract class CallbackAppCompatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Function<Object, Object>> {

    private Map<Integer, Supplier<Function<Object, Object>>> tasks = new HashMap<Integer, Supplier<Function<Object, Object>>>();

    public void call(AppCompatActivity context, Supplier<Function<Object, Object>> task) {
        Bundle queryBundle = new Bundle();
        tasks.put(task.hashCode(), task);
        context.getSupportLoaderManager().restartLoader(task.hashCode(), queryBundle, this);
    }

    @Override
    public Loader<Function<Object, Object>> onCreateLoader(int id, Bundle args) {
        return new MyAsyncTaskLoader(this, tasks.get(id));
    }

    @Override
    public void onLoadFinished(Loader<Function<Object, Object>> loader, Function<Object, Object> data) {
        data.apply(null);
    }

    @Override
    public void onLoaderReset(Loader<Function<Object, Object>> loader) {

    }

    static class MyAsyncTaskLoader extends AsyncTaskLoader<Function<Object, Object>> {
        private final Supplier<Function<Object, Object>> supplier;

        public MyAsyncTaskLoader(Context context, Supplier<Function<Object, Object>> supplier) {
            super(context);
            this.supplier = supplier;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            forceLoad();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Nullable
        @Override
        public Function<Object, Object> loadInBackground() {
            return supplier.get();
        }

    }
}

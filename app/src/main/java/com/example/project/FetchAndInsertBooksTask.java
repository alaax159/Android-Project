package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("deprecation")
public class FetchAndInsertBooksTask extends AsyncTask<String, Void, Integer> {

    private final Activity activity;
    private final DataBaseHelper dbHelper;
    private final Button triggerButton;
    private Exception error;

    public FetchAndInsertBooksTask(Activity activity, DataBaseHelper dbHelper, Button triggerButton) {
        this.activity = activity;
        this.dbHelper = dbHelper;
        this.triggerButton = triggerButton;
    }

    @Override
    protected void onPreExecute() {
        if (triggerButton != null) {
            triggerButton.setEnabled(false);
            triggerButton.setText("Connecting...");
        }
    }

    @Override
    protected Integer doInBackground(String... params) {
        String urlStr = (params != null && params.length > 0) ? params[0] : null;
        if (urlStr == null) return 0;

        try {
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream(),
                    java.nio.charset.StandardCharsets.UTF_8
            ));
            StringBuilder sb = new StringBuilder();
            String line; while ((line = br.readLine()) != null) sb.append(line).append('\n');
            br.close();
            conn.disconnect();
            if (code < 200 || code >= 300) throw new RuntimeException("HTTP " + code + ": " + sb);

            Object root = new JSONTokener(sb.toString().trim()).nextValue();

            int inserted = 0;
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if (root instanceof JSONObject) {

                JSONObject o = (JSONObject) root;
                inserted += insertOne(db, o);

            } else if (root instanceof JSONArray) {

                JSONArray arr = (JSONArray) root;
                db.beginTransaction();
                try {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject o = arr.optJSONObject(i);
                        if (o == null) continue;
                        inserted += insertOne(db, o);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            return inserted;

        } catch (Exception e) {
            error = e;
            Log.e("FetchInsertBooks", "Error", e);
            return 0;
        }
    }

    private int insertOne(SQLiteDatabase db, JSONObject o) {
        android.content.ContentValues cv = new android.content.ContentValues();

        // Adjust keys to match your API. These match your Books table.
        cv.put("title",            o.optString("title", null));
        cv.put("author",           o.optString("author", null));
        cv.put("isbn",             o.optString("isbn", null));     // UNIQUE
        cv.put("category",         o.optString("category", null));
        cv.put("availability",     o.optInt("availability", 1));
        cv.put("cover_url",        o.optString("cover_url", null)); // change to "coverUrl" if needed

        if (o.has("publication_year") && !o.isNull("publication_year")) {
            cv.put("publication_year", o.optInt("publication_year", 0));
        }

        String title = cv.getAsString("title");
        if (title == null || title.trim().isEmpty()) return 0;

        long rowId = db.insertWithOnConflict("Books", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return rowId == -1 ? 0 : 1;
    }

    @Override
    protected void onPostExecute(Integer insertedCount) {
        if (triggerButton != null) {
            triggerButton.setEnabled(true);
            triggerButton.setText("Get Started");
        }

        if (error != null) {
            Toast.makeText(activity, "Failed to load: " + error.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            triggerButton.setText("Connected");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }
            }, 2000);
            Toast.makeText(activity, "Connected: ", Toast.LENGTH_LONG).show();

        }
    }

}

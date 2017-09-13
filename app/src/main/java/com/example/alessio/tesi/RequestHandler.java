package com.example.alessio.tesi;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler {
    private HttpURLConnection httpConn;
    private String json;
    private URL url;
    private JsonReader response;

    public RequestHandler(String json, String url_file) {
        this.json = json;
        try {
            this.url = new URL("https://webdev.dibris.unige.it/~S4052357/" + url_file);
            httpConn = (HttpURLConnection) url.openConnection();
            new BackgroundTask().execute();
            httpConn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public JsonReader getResponse() {
        return this.response;
    }

    private class BackgroundTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                httpConn.setUseCaches(false);
                httpConn.setRequestProperty("Content-type", "application/json");
                httpConn.setRequestMethod("POST");
                // INVIO LA RICHIESTA
                httpConn.connect();
                OutputStream out = new BufferedOutputStream(httpConn.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(json);
                writer.flush();
                writer.close();
                // RICEVO LA RISPOSTA
                InputStream is = httpConn.getInputStream();
                if(is != null) {
                    response = new JsonReader(new InputStreamReader(is,"UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

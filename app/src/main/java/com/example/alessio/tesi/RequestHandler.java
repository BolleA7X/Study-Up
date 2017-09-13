package com.example.alessio.tesi;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler {
    private HttpURLConnection httpConn;
    private String json;
    private URL url;

    public RequestHandler(String json, String url_file) {
        this.json = json;
        try {
            this.url = new URL("https://webdev.dibris.unige.it/~S4052357/" + url_file);
            httpConn = (HttpURLConnection) url.openConnection();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject makeRequest() {
        JSONObject response;
        try {
            httpConn.disconnect();
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
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while((line = in.readLine()) != null)
                sb.append(line);
            in.close();
            response = new JSONObject(sb.toString().replace(" ",""));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

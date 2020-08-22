package com.example.weatherapp.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Helper  {
    static String streem=null ;
    public Helper()
    {}
    public String getHTTPData(String UrlString)
    {
        try
        {
            URL url=new URL(UrlString);
            HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();

            if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String Line;
                while ((Line = r.readLine()) != null) {
                    sb.append(Line);
                    streem = sb.toString();
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }return streem;
    }
}

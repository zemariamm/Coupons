package com.zemariamm.coupons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
// Url encode - http://stackoverflow.com/questions/3792124/encode-a-url-containing-a-jsonarray
// http://www.ibm.com/developerworks/web/library/x-andbene1/?ca=drs-
// see also: http://w3mentor.com/learn/java/android-development/android-http-services/example-of-http-get-request-using-httpclient-in-android/
// http://stackoverflow.com/questions/1541125/parsing-json-in-java
// http://developer.android.com/reference/org/json/package-summary.html
// http://www.androidcompetencycenter.com/2009/10/json-parsing-in-android/
public class RestClient {
 
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
 
    /* This is a test function which will connects to a given
     * rest service and prints it's response to Android Log with
     * labels "ArtistExplorer".
     */
    public static String connect(String url)
    {
    	//Log.d("Hooligans","connect");
        HttpClient httpclient = new DefaultHttpClient();
 
        // Prepare a request object
        //Log.d("Hooligans","url:" + url);
        HttpGet httpget = new HttpGet(url); 
 
        // Execute the request
        HttpResponse response;
        

        try {
        	//Log.d("ArtistExplorer","try");
            response = httpclient.execute(httpget);
            // Examine the response status
            //Log.d("Hooligans",response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
 
            if (entity != null) {
 
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                //Log.d("Hooligans",result);
                //Log.d("ArtistExplorer",result);
 
                // hack!! change this later	
                if (true == true)
                {
                	return result;
                }
                // A Simple JSONObject Creation
                JSONObject json=new JSONObject(result);
                //Log.d("ArtistExplorer","<jsonobject>\n"+json.toString()+"\n</jsonobject>");
 
                // A Simple JSONObject Parsing
                JSONArray nameArray=json.names();
                JSONArray valArray=json.toJSONArray(nameArray);
                for(int i=0;i<valArray.length();i++)
                {
                	//echoParser.parseArtists(valArray.getString(i));
                	//Log.d("ArtistExplorer","Returning value:" + valArray.getString(i));
                	return valArray.getString(i);
                    //Log.d("ArtistExplorer","<jsonname"+i+">\n"+nameArray.getString(i)+"\n</jsonname"+i+">\n"+"<jsonvalue"+i+">\n"+valArray.getString(i)+"\n</jsonvalue"+i+">");
                }
 
                // A Simple JSONObject Value Pushing
                //json.put("sample key", "sample value");
                //Log.d("ArtistExplorer","<jsonobject>\n"+json.toString()+"\n</jsonobject>");
 
                // Closing the input stream will trigger connection release
                instream.close();
            }
 
 
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	//Log.d("Hooligans","ClientProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
            //Log.d("Hooligans","No internet connection : " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
        	//Log.d("ArtistExplorer","JSONException");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
 
}

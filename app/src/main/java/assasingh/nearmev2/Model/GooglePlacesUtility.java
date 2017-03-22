package assasingh.nearmev2.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.View.MainActivity;

/**
 * Created by beaumoaj on 12/02/15.
 */
public class GooglePlacesUtility {

    private Context context;


    public GooglePlacesUtility(Context context) {

        this.context = context;
    }

    public static String readGooglePlaces(String uri, String referer) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(uri);
            Log.i("AJB", url.toString());
            conn = (HttpURLConnection) url.openConnection();
            if (referer != null) {
                conn.setRequestProperty("Referer", referer);
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Log.i("AJB", "RESULTS: " + jsonResults);
        } catch (MalformedURLException e) {
            Log.i("Google Places Utility", "Error processing Places API URL");
            return null;
        } catch (IOException e) {
            Log.i("Google Places Utility", "Error connecting to Places API");
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return jsonResults.toString();
        /*
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("Referer", "http://aston.ac.uk/uk.ac.aston.cs3040.rssreaderwithmaps.model");
                Log.i(RssItemActivity.TAG, "GET Request: " + httpGet.getURI());
        try {
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.i(RssItemActivity.TAG, "Failed to download file " + uri);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
        */
    }

    public List<SimpleGooglePlace> networkCall(String url) throws Exception {
        ArrayList<SimpleGooglePlace> result = new ArrayList<SimpleGooglePlace>();

        String jsonData = urlRequest(url);

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        String jsonStatus = jsonObject.getString("status");

        jsonStatus = jsonStatus.trim();

        Log.d("JSONRES", jsonObject.getString("status"));

        SimpleGooglePlace errorPlace = new SimpleGooglePlace(52.500957, -1.9370728); //ERROR PLACE

        if (jsonStatus.equals("INVALID_REQUEST")) {

            MainActivity.ERROR = "invalid request params";

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return result;
        } else if (jsonStatus.equals("REQUEST_DENIED")) {


            MainActivity.ERROR = "Invalid places kay, please contact the developer";


            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return result;
        } else if (jsonStatus.equals("OVER_QUERY_LIMIT")) {

            MainActivity.ERROR = "Unfortunately we've run out of requests for one day :(";


            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return result;
        } else if (jsonStatus.equals("ZERO_RESULTS")) {

            MainActivity.ERROR = "Your search didn't seem to return any results, try something else";

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return result;
        } else {


            for (int i = 0; i < jsonArray.length(); i++) {


                double lat = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double lng = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                String name = jsonArray.getJSONObject(i).getString("name");

                SimpleGooglePlace place = new SimpleGooglePlace(lat, lng);

                String photoRef = "";

                if (jsonArray.getJSONObject(i).has("photos")) {
                    photoRef = jsonArray.getJSONObject(i).getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                }

                String openNow = "false";
                String exceptionalDates = "";
                String weekdayText = "";


                String types = "";

                if (jsonArray.getJSONObject(i).has("types")) {
                    for (int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("types").length(); j++) {
                        types += "+" + jsonArray.getJSONObject(i).getJSONArray("types").getString(j);
                    }
                    //Log.d("UTILITY", types);
                }


                double rating = 0.0;

                if (jsonArray.getJSONObject(i).has("rating")) {
                    rating = jsonArray.getJSONObject(i).getDouble("rating");
                }

                String placeID = jsonArray.getJSONObject(i).getString("place_id");

                //Log.d("PLACE_ID", placeID);

                String extraInfoUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=AIzaSyB3Qirj2H1pL_63c7yXcMIMCjcQUinyHS4";

                String res = urlRequest(extraInfoUrl); //TODO

                String phone = "000000";
                String address = "";

                JSONObject jObject = new JSONObject(res);

                JSONObject obj = jObject.getJSONObject("result");

                address = obj.getString("formatted_address");


                if (obj.has("formatted_phone_number")) {
                    phone = obj.getString("formatted_phone_number");
                }

                if (obj.has("opening_hours")) {
                    openNow = obj.getJSONObject("opening_hours").getString("open_now");
                    if (obj.has("exceptional_date")) {
                        exceptionalDates = obj.getJSONObject("opening_hours").getJSONArray("exceptional_date").toString();
                    }
                    for (int j = 0; j <= 6; j++) {
                        weekdayText += obj.getJSONObject("opening_hours").getJSONArray("weekday_text").getString(j) + "+";
                    }
                }

                String website = "https://en.wikipedia.org/wiki/HTTP_404";

                if (obj.has("website")) {
                    website = obj.getString("website");
                }


                place.setLatitude(lat);
                place.setLongitude(lng);
                place.setName(name);
                place.setPhotoRef(photoRef);
                place.setOpenNow(openNow);
                place.setRating(rating);
                place.setExceptionalDates(exceptionalDates);
                place.setWeekdayText(weekdayText);
                place.setTypes(types);
                place.setAddress(address);
                place.setPhone(phone);
                place.setWebsite(website);

                result.add(place);
            }


            return result;
        }
    }

    public List<DirectionModel> getPolyLine(String url) throws Exception {
        String jsonData = urlRequest(url);

        DirectionModel directionModel = new DirectionModel();

        ArrayList<DirectionModel> result = new ArrayList<DirectionModel>();


        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonArray = jsonObject.getJSONArray("routes");
        JSONObject route = jsonArray.getJSONObject(0);
        JSONObject polyLineObj = route.getJSONObject("overview_polyline");

        String polyLine = polyLineObj.getString("points");
        String startAddress = route.getJSONArray("legs").getJSONObject(0).getString("start_address");
        String endAddress = route.getJSONArray("legs").getJSONObject(0).getString("end_address");
        String distance = route.getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
        String duration = route.getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");

        directionModel.setDistance(distance);
        directionModel.setDuration(duration);
        directionModel.setPolyLine(polyLine);
        directionModel.setStartAddress(startAddress);
        directionModel.setEndAddress(endAddress);

        result.add(directionModel);


        return result;

    }

    private String urlRequest(String s) throws Exception {
        String request = s;

        StringBuilder sb = new StringBuilder();

        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = new BufferedInputStream(conn.getInputStream());

            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            String inputLine;

            while ((inputLine = bin.readLine()) != null) {
                sb.append(inputLine);
            }

        } finally {
            conn.disconnect();
        }

        return sb.toString();
    }
}

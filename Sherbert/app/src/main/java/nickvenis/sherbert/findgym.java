package nickvenis.sherbert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.Manifest;
import android.widget.SimpleAdapter;
import android.location.LocationListener;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class findgym extends AppCompatActivity {
    double latitude;
    double longitude;
    ArrayList<HashMap<String, String>> gyms = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findgym);


        ImageView i = (ImageView) findViewById(R.id.opensettings);
        i.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newactivity = new Intent(findgym.this, home2.class);
                startActivity(newactivity);
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationlistenerGPS);
        getlocation();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Intent i = new Intent(findgym.this, home2.class);
                    startActivity(i);
                }
                return;
            }
        }
    }

    LocationListener locationlistenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void getlocation() {
        String gym = "gym";
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?
        Uri.Builder apiurl = new Uri.Builder();
        apiurl.scheme("https")
                .authority("maps.googleapis.com")
                .appendPath("maps")
                .appendPath("api")
                .appendPath("place")
                .appendPath("nearbysearch")
                .appendPath("json")
                .appendQueryParameter("location", latitude + "," + longitude)
                .appendQueryParameter("radius", "10000")
                .appendQueryParameter("type", gym)
                .appendQueryParameter("key", "AIzaSyBPQRy4JkzmJJjYqIlLFTIB3qE0lYC5T-E");
        String url = apiurl.build().toString();
        new AsyncTaskParseJson().execute(url);



    }


    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
        String ServiceUrl;
        String jsondata;
        @Override

        protected void onPreExecute() {
        }

        @Override
        // this method is used for
        protected String doInBackground(String... params) {
            ServiceUrl = params[0];
            try {
                // create new instance of the httpConnect class
                gethttp jParser = new gethttp();

                // get json string from service url
                jsondata = jParser.getJSONFromUrl(ServiceUrl);
                SharedPreferences jsondatasp = getSharedPreferences("jsontext", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = jsondatasp.edit();
                editor.putString("JSON TEXT",jsondata);
                editor.apply();


                // parse returned json string into json array
                JSONObject jsonobj = new JSONObject(jsondata);

                JSONArray results =  jsonobj.getJSONArray("results");


                // loop through json array and add each tweet to item in arrayList
                for (int i = 0; i < results.length(); i++) {
                    JSONObject g = results.getJSONObject(i);
                    String name = g.getString("name");
                    String rating = g.getString("rating");
                    String vicinity = g.getString("vicinity");

                    HashMap<String, String> gymhash = new HashMap<>();

                    gymhash.put("name",name);
                    gymhash.put("rating",rating);
                    gymhash.put("vicinity",vicinity);

                    gyms.add(gymhash);

                }

            } catch (Exception e) {
                Log.e("PARSE ERROR: ","EXCEPTION ",e);
            }
            return null;
        }

        @Override
        // below method will run when service HTTP request is complete, will then bind tweet text in arrayList to ListView
        protected void onPostExecute(String result) {
            try {
                ListView lv = (ListView) findViewById(R.id.listgyms);
                ListAdapter adapter = new SimpleAdapter(findgym.this, gyms,
                        R.layout.list_items, new String[]{"Name", "Rating", "Vicinity"},
                        new int[]{R.id.name, R.id.rating, R.id.vicinity});
                lv.setAdapter(adapter);
            }catch (Exception e){
                Log.e("DISPLAY LIST ERROR: ", "EXCEPTION ", e);
            }
        }

    }
}

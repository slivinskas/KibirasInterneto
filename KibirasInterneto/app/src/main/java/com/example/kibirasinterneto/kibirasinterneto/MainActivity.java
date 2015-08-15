package com.example.kibirasinterneto.kibirasinterneto;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Location oldLocation;
    double teleportLatitude = 40.7580441;
    double teleportLongitude =  -73.9854593;
    Location diffLocation;
    Location newTeleportLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        String url = "http://kibirasinterneto.azurewebsites.net";
        WebView view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);
        String provider = getProviderName();
        diffLocation = new Location(provider);
        newTeleportLocation = new Location(provider);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                if (oldLocation == null) {
                    oldLocation = location;
                    return;

                }
                diffLocation.setLatitude(oldLocation.getLatitude() - location.getLatitude());
                diffLocation.setLongitude(oldLocation.getLongitude() - location.getLongitude());
                oldLocation = location;
                ConvertCoordinates();
            }
        };
        locationManager.requestLocationUpdates(getProviderName(), 1000,
                1, locationListener);
    }

    String getProviderName() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.ACCURACY_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        return locationManager.getBestProvider(criteria, true);
    }

    void ConvertCoordinates(){
        if(newTeleportLocation.getLatitude() == 0 && newTeleportLocation.getLongitude() ==0){

            newTeleportLocation.setLatitude(teleportLatitude);
            newTeleportLocation.setLongitude(teleportLongitude);
        }else {
            newTeleportLocation.setLatitude( newTeleportLocation.getLatitude() + diffLocation.getLatitude());
            newTeleportLocation.setLongitude(newTeleportLocation.getLongitude() + diffLocation.getLongitude());
        }

    }
}

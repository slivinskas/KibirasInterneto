package com.example.kibirasinterneto.kibirasinterneto;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
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
import android.os.Bundle;

import android.app.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Location oldLocation;
    double teleportLatitude = 40.7580441;
    double teleportLongitude =  -73.9854593;
    Location diffLocation;
    Location newTeleportLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the sensor listeners
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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

    Float azimut;  // View to draw a compass







    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;



    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

    float[] mGravity;
    float[] mGeomagnetic;
    float tempAz;
    float prevAz;
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                if (azimut != null){
                    System.out.println(tempAz);
                    tempAz = orientation[0];
                    if (tempAz - prevAz > 0.10f || tempAz - prevAz < -0.1f){
                        azimut = tempAz;
                        prevAz=azimut;
                    }
                } else {
                    azimut = orientation[0];
                    prevAz = azimut;// orientation contains: azimut, pitch and roll
                }
            }
        }

    }

}

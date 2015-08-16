package com.example.kibirasinterneto.kibirasinterneto;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.hardware.SensorEventListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // 38.909622,-77.034628
    // time sq 40.7580441, -73.9854593
    // kaunas 54.9019108,23.9377343
    //parkas 40.7794498,-73.9648379

    Location oldLocation;
    double teleportLatitude = 40.7794498;
    double teleportLongitude = -73.9648379;

    Location diffLocation;
    Location newTeleportLocation;

    String url = "http://kibirasinterneto.azurewebsites.net/Web/template.html";

    WebView view;

    Boolean WebViewOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the sensor listeners
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);

        view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:web.setXPos(" + heading + ")");
                WebViewOver = true;
                ImageView loadingScreen = (ImageView) findViewById(R.id.imageView);
                loadingScreen.setVisibility(View.GONE);
            }
        });

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.print("asd");
                view.loadUrl("javascript:changeAdds('android')");
            }
        });


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

                    System.out.print("current x " + location.getLatitude());
                    System.out.print("current y " + location.getLatitude());

                    return;
                }
                if(oldLocation.getLatitude() > location.getLatitude()){
                    diffLocation.setLatitude(location.getLatitude() - oldLocation.getLatitude());
                }else{
                    diffLocation.setLatitude(oldLocation.getLatitude() -location.getLatitude());
                }
                if(oldLocation.getLongitude() > location.getLongitude()){
                    diffLocation.setLongitude(location.getLongitude() - oldLocation.getLongitude());
                }else{
                    diffLocation.setLongitude( oldLocation.getLongitude()-location.getLongitude());
                }


                float diff = distFrom((float)oldLocation.getLatitude(),(float) oldLocation.getLongitude(),(float)location.getLatitude(), (float)location.getLongitude() );
                oldLocation = location;

                ConvertCoordinates((double) diff);
               // Toast.makeText(getApplicationContext(), "old: "+location.getLatitude()+" "+location.getLongitude()+" new "+newTeleportLocation.getLatitude()+","+newTeleportLocation.getLongitude()+" skirtumas: "+diff+"" , Toast.LENGTH_LONG).show();
            }

        };

        locationManager.requestLocationUpdates(getProviderName(), 1000,  1, locationListener);
    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (float) (earthRadius * c);
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

    void ConvertCoordinates(double pDistanceInMeters){
        if(newTeleportLocation.getLatitude() == 0 && newTeleportLocation.getLongitude() ==0){
            newTeleportLocation.setLatitude(teleportLatitude);
            newTeleportLocation.setLongitude(teleportLongitude);
        }else {
            newTeleportLocation.setLatitude((newTeleportLocation.getLatitude() + diffLocation.getLatitude())+0.00005);
            newTeleportLocation.setLongitude((newTeleportLocation.getLongitude() + diffLocation.getLongitude())+0.00005);

            System.out.println("Minusas:");
            System.out.println("lat: " + newTeleportLocation.getLatitude() + " lon: " + newTeleportLocation.getLongitude() + "");
        }

        view.loadUrl("javascript:web.setLocation(+" + newTeleportLocation.getLatitude() + "," + newTeleportLocation.getLongitude() + ")");
    }

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

    public void onClick(View v) {
        view.loadUrl("javascript:changeAdds('photo')");
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

    float[] mGravity;
    float[] mGeomagnetic;
    long senMagnetometerlastUpdate = 0;
    long senAccelerometerlastUpdate = 0;
    double prevH = 0;
    double heading=0;
    double prevP = 0;

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, null, mGravity, mGeomagnetic);

            if (success) {
                float orientation[] = new float[6];
                SensorManager.getOrientation(R, orientation);
                float azimuthInRadians = orientation[0];
                float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

                heading = (Math.round(azimuthInDegress));


                long curTime = System.currentTimeMillis();
                if (heading - prevH > 7 || heading - prevH <-7)
                 {
                    if(WebViewOver) {
                        System.out.println(heading);
                        prevH = heading;
                        view.loadUrl("javascript:web.setXPos(" + heading + ")");
                    }

                     senMagnetometerlastUpdate = curTime;
                }
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float mOrientation[] = new float[3];
                    System.arraycopy(event.values, 0, mGravity, 0, event.values.length);

                    SensorManager.getRotationMatrix(null, I, mGravity, mGeomagnetic);

                    SensorManager.getOrientation(R, mOrientation);

                    float pitchInRadians = mOrientation[2];
                    float pitchInDegress = (float) (Math.toDegrees(pitchInRadians) + 360) % 360;

                    double pitch = (Math.round(pitchInDegress));

                    if (pitch < 180){
                        pitch = pitch  - 90;
                    } else
                    {
                        pitch = -pitch  + 270;
                    }

                    if (pitch - prevP >3 || pitch - prevP <-3)
                     {
                        if(WebViewOver) {
                            System.out.println(pitch);
                            prevP= pitch;
                            view.loadUrl("javascript:web.setZPos(" + pitch + ")");
                        }
                        senAccelerometerlastUpdate = curTime;
                    }
                }
            }

        }

    }
}

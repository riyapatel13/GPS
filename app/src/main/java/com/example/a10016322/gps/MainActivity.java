package com.example.a10016322.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.a10016322.gps.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    TextView lat, lon, add, total, tim, lontim, lonloc, alltimes;
    List<Address> addresses;
    ArrayList<Long> times;
    ArrayList<Location> locations;
    Geocoder geocoder;
    Location firstLoc = new Location("new location");
    int x=0;
    float sum=0;
    long start = 0, newStart = 0, finish = 0, max = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = SystemClock.elapsedRealtime();
        lat = (TextView)findViewById(R.id.latitude_id);
        lon = (TextView)findViewById(R.id.longitude_id);
        add = (TextView)findViewById(R.id.address_id);
        total = (TextView)findViewById(R.id.sum_id);
        tim = (TextView)findViewById(R.id.time_id);
        lontim = (TextView)findViewById(R.id.longtime_id);
        lonloc = (TextView)findViewById(R.id.longloc_id);
        alltimes = (TextView)findViewById(R.id.timess_id);
        times = new ArrayList<>();
        locations = new ArrayList<>();

        start = SystemClock.elapsedRealtime();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.25f, this);

    }

    @Override
    public void onLocationChanged(Location location)
    {
        locations.add(location);
        Log.d("TIMMY", "locs: "+locations);
        finish = SystemClock.elapsedRealtime();
        times.add(finish-newStart);
        Log.d("TIMMY", "times: "+times);
        newStart = SystemClock.elapsedRealtime();
        tim.setText("Time: "+newStart);
        if (newStart - start>=5000){ //from onlocchanged to app start time
            if (x==0)
            {
                firstLoc.setLatitude(location.getLatitude());
                firstLoc.setLongitude(location.getLongitude());
                times.add(newStart-start);

                x++;
            }
            lat.setText("latitude: "+location.getLatitude());
            lon.setText("longitude: "+location.getLongitude());
            geocoder = new Geocoder(this, Locale.US);
            try
            {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                add.setText("Address: " + addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1));
            }catch(Exception e){}

            sum += location.distanceTo(firstLoc);
            total.setText("Distance travelled: "+sum+ " m");
            firstLoc.setLatitude(location.getLatitude());
            firstLoc.setLongitude(location.getLongitude());
            for (int i=1; i<times.size(); i++)
            {
                if (times.get(i)>max)
                {
                    max = times.get(i);
                    lontim.setText("Long time: "+max);
                    lonloc.setText("Long location: "+locations.get(i-1).getLatitude()+", "+locations.get(i-1).getLongitude());
                }


            alltimes.setText("Times at locations: "+times);

        }

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

}

package si.parks.parks;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import si.parks.parks.eventbus.MessageEventSettingsLocationUpdateInterval;
import si.parks.parks.eventbus.MessageEventUpdateLocation;


public class GPSTracker extends Service {


    private static final String TAG = GPSTracker.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static int LOCATION_INTERVAL = 1000; //1000ms = 1s
    private static final float LOCATION_DISTANCE = 0.5f; //5m


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        boolean isOK;

        public void setOK(boolean OK) {
            isOK = OK;
        }

        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
            isOK = true;
//            sendBroadcastMessage();
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
            EventBus.getDefault().post(new MessageEventUpdateLocation(location));

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
            isOK = false;
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
            isOK = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
            if (status == LocationProvider.AVAILABLE) isOK = true;
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public boolean isAvalable() {
        if (mLocationListeners==null) return false;
        if (mLocationListeners[0].isOK) return true;
        if (mLocationListeners[1].isOK) return true;
        return false;
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        if (mLocationManager==null) {
            create();
        } else
        {
            if (!isAvalable()) {
                create();
            }
        }

        return START_NOT_STICKY;// START_STICKY;
    }

    @Subscribe
    public void onMessageEvent(MessageEventSettingsLocationUpdateInterval event) {
        Log.i(TAG,"MessageEventSettingsLocationUpdateInterval "+event.getInterval());
    }

    public void create() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        LOCATION_INTERVAL = Integer.parseInt(sharedPref.getString(ActivityMySettings.LOCATION_INTERVAL_KEY, "1000"));
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
            mLocationListeners[1].setOK(true);
        } catch (java.lang.SecurityException ex) {
            mLocationListeners[1].setOK(false);
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            mLocationListeners[1].setOK(false);
            Log.i(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
            mLocationListeners[0].setOK(true);
        } catch (java.lang.SecurityException ex) {
            mLocationListeners[0].setOK(false);
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            mLocationListeners[0].setOK(false);
            Log.i(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        EventBus.getDefault().register(this);
        create();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }

    }

    private void initializeLocationManager() {
        Log.i(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}
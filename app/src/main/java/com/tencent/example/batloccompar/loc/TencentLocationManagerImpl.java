package com.tencent.example.batloccompar.loc;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class TencentLocationManagerImpl extends AbsLocationManager {
    public static final String TAG = "TencentLocationMgrImpl";
    private final TencentLocationManager locationManager;
    private final TencentLocationRequest request;
    private final TencentLocationListener locationListener;
    private ILocationListener mUserLisener;

    public TencentLocationManagerImpl(Context context) {
        super(context);
        locationManager = TencentLocationManager.getInstance(mCtx);
        request = TencentLocationRequest.create()
                .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA)
                .setAllowDirection(true)
                .setAllowGPS(true)
                .setInterval(2000);
        locationListener = new TencentLocationListener() {

            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
                if (mUserLisener != null) {
                    mUserLisener.onLocationChanged(transferLocation(tencentLocation));
                }
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {

            }

            private Location transferLocation(TencentLocation tencentLocation) {
                Location location = new Location(tencentLocation.getProvider());
                location.setAccuracy(tencentLocation.getAccuracy());
                location.setAltitude(tencentLocation.getAltitude());
                location.setLatitude(tencentLocation.getLatitude());
                location.setLongitude(tencentLocation.getLongitude());
                location.setBearing(tencentLocation.getBearing());
                location.setSpeed(tencentLocation.getSpeed());
                location.setExtras(tencentLocation.getExtra());
                location.setProvider(tencentLocation.getProvider());
                return location;
            }
        };
    }

    @Override
    public void startLocation(ILocationListener listener) {
        mUserLisener = listener;
        int i = locationManager.requestLocationUpdates(request, locationListener, Looper.getMainLooper());
        Log.i(TAG, "re = " + i);
    }

    @Override
    public void stopLocation() {
        locationManager.removeUpdates(locationListener);
    }
}

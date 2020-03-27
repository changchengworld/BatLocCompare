package com.tencent.example.batloccompar.loc;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class TencentLocationManagerImpl extends AbsLocationManager {
    private final TencentLocationManager locationManager;
    private final TencentLocationRequest request;
    private final TencentLocationListener locationListener;
    private ILocationListener mUserLisener;

    TencentLocationManagerImpl(Context context) {
        super(context);
        locationManager = TencentLocationManager.getInstance(mCtx);
        request = TencentLocationRequest.create()
                .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA)
                .setAllowDirection(true)
                .setAllowGPS(true)
                .setInterval(2000);
        locationListener = new TencentLocationListener() {

            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int code, String reason) {
                if (mUserLisener != null) {
                    mUserLisener.onLocationChanged(transferLocation(tencentLocation, code, reason));
                }
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {

            }

            private Location transferLocation(TencentLocation tencentLocation, int code, String reason) {
                Location location = new Location(tencentLocation.getProvider());
                location.setAccuracy(tencentLocation.getAccuracy());
                location.setAltitude(tencentLocation.getAltitude());
                location.setLatitude(tencentLocation.getLatitude());
                location.setLongitude(tencentLocation.getLongitude());
                location.setBearing(tencentLocation.getBearing());
                location.setSpeed(tencentLocation.getSpeed());
                Bundle bundle = new Bundle(tencentLocation.getExtra());
                bundle.putInt("code", code);
                bundle.putString("reason", reason);
                location.setExtras(bundle);
                location.setProvider(tencentLocation.getProvider());
                return location;
            }
        };
    }

    @Override
    public void startLocation(ILocationListener listener) {
        mUserLisener = listener;
        locationManager.requestLocationUpdates(request, locationListener, Looper.getMainLooper());
    }

    @Override
    public void stopLocation() {
        locationManager.removeUpdates(locationListener);
    }
}

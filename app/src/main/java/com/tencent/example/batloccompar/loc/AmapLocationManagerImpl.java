package com.tencent.example.batloccompar.loc;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class AmapLocationManagerImpl extends AbsLocationManager {
    private AMapLocationClient mLocationClient;
    private ILocationListener mUserLisener;

    AmapLocationManagerImpl(Context context) {
        super(context);
        mLocationClient = new AMapLocationClient(context);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setInterval(2000);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (mUserLisener != null) {
                    mUserLisener.onLocationChanged(transferLocation(aMapLocation));
                }
            }

            private Location transferLocation(AMapLocation aMapLocation) {
                Location location = new Location(aMapLocation.getProvider());
                location.setAccuracy(aMapLocation.getAccuracy());
                location.setAltitude(aMapLocation.getAltitude());
                location.setLatitude(aMapLocation.getLatitude());
                location.setLongitude(aMapLocation.getLongitude());
                location.setBearing(aMapLocation.getBearing());
                location.setSpeed(aMapLocation.getSpeed());
                Bundle bundle = new Bundle();
                bundle.putInt("code", aMapLocation.getErrorCode());
                bundle.putString("reason", aMapLocation.getErrorInfo());
                location.setExtras(bundle);
                location.setProvider(aMapLocation.getProvider());
                return location;
            }
        });
    }

    @Override
    public void startLocation(ILocationListener listener) {
        mUserLisener = listener;
        mLocationClient.startLocation();
    }

    @Override
    public void stopLocation() {
        mLocationClient.stopLocation();
    }
}

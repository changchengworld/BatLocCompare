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
                int locationType = aMapLocation.getLocationType();
                String provider = "";
                switch (locationType) {
                    case 1:
                        provider = "gps";
                        break;
                    case 2:
                    case 4:
                    case 5:
                    case 6:
                        provider = "network";
                        break;
                    case 8:
                        provider = "offline";
                        break;
                    case 9:
                        provider = "last";
                        break;
                }
                Location location = new Location(provider);
                location.setAccuracy(aMapLocation.getAccuracy());
                location.setAltitude(aMapLocation.getAltitude());
                location.setLatitude(aMapLocation.getLatitude());
                location.setLongitude(aMapLocation.getLongitude());
                location.setBearing(aMapLocation.getBearing());
                location.setSpeed(aMapLocation.getSpeed());
                Bundle bundle = new Bundle();
                bundle.putInt("code", aMapLocation.getErrorCode());
                bundle.putString("reason", aMapLocation.getErrorInfo());
                bundle.putString("address", aMapLocation.getAddress());
                location.setExtras(bundle);
                location.setProvider(provider);
                return location;
            }
        });
    }

    @Override
    public void startLocation(ILocationListener listener) {
        mUserLisener = listener;
        if (mLocationClient != null) {
            mLocationClient.enableBackgroundLocation(LOC_NOTIFICATIONID, buildNotification());
            mLocationClient.startLocation();
        }
    }

    @Override
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.disableBackgroundLocation(true);
        }
    }
}

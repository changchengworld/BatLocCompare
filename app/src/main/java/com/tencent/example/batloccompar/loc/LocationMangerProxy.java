package com.tencent.example.batloccompar.loc;

import android.content.Context;

public class LocationMangerProxy extends AbsLocationManager {
    private AbsLocationManager mLocationManager;
    private Context mCtx;

    public LocationMangerProxy(Context context) {
        super(context.getApplicationContext());
        mCtx = context.getApplicationContext();
        mLocationManager = new BaiduLocationManagerImpl(mCtx);
    }

    public void checkLocationManger(int index) {
        switch (index) {
            case 0:
                mLocationManager = new BaiduLocationManagerImpl(mCtx);
                break;
            case 1:
                mLocationManager = new AmapLocationManagerImpl(mCtx);
                break;
            case 2:
                mLocationManager = new TencentLocationManagerImpl(mCtx);
                break;
            default:
                throw new IllegalArgumentException("illegal index!!!");
        }
    }

    @Override
    public void startLocation(ILocationListener listener) {
        mLocationManager.startLocation(listener);
    }

    @Override
    public void stopLocation() {
        mLocationManager.stopLocation();
    }
}

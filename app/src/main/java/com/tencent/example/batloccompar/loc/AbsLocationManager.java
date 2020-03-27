package com.tencent.example.batloccompar.loc;

import android.content.Context;

abstract class AbsLocationManager {
    Context mCtx;

    AbsLocationManager(Context context) {
        mCtx = context;
    }

    abstract void startLocation(ILocationListener listener);

    abstract void stopLocation();
}

package com.tencent.example.batloccompar;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.example.batloccompar.loc.ILocationListener;
import com.tencent.example.batloccompar.loc.LocationMangerProxy;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final static String check = "check_";
    private TextView tv_content;
    private Button btn_check;
    private String[] sdk_list = new String[]{"b", "a", "t"};
    private int cur_sdk_index;
    private LocationMangerProxy mLocationProxy;
    private CustomLocationListener mListener;
    private boolean isStopLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_content = findViewById(R.id.tv_content);
        btn_check = findViewById(R.id.btn_check);
        String btnCheckContent = check + sdk_list[cur_sdk_index];
        btn_check.setText(btnCheckContent);
        mLocationProxy = new LocationMangerProxy(this);
        mListener = new CustomLocationListener(new WeakReference<>(this));
    }

    public void startLocation(View view) {
        mLocationProxy.startLocation(mListener);
        isStopLocation = false;
        tv_content.append(sdk_list[cur_sdk_index]);
        tv_content.append("\r\n");
    }

    public void stopLocation(View view) {
        mLocationProxy.stopLocation();
        isStopLocation = true;
        tv_content.append("--------------------");
        tv_content.append("\r\n");
        tv_content.append("\r\n");
    }

    public void checkSDK(View view) {
        if (isStopLocation) {
            cur_sdk_index++;
            cur_sdk_index = cur_sdk_index % 3;
            String btnCheckContent = check + sdk_list[cur_sdk_index];
            btn_check.setText(btnCheckContent);
            mLocationProxy.checkLocationManger(cur_sdk_index);
        } else {
            Toast.makeText(this, "You have to stop first", Toast.LENGTH_SHORT).show();
        }
    }

    private static class CustomLocationListener implements ILocationListener {
        private WeakReference<MainActivity> mWR;

        public CustomLocationListener(WeakReference<MainActivity> wr) {
            this.mWR = wr;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (mWR != null) {
                MainActivity instance = mWR.get();
                if (instance != null) {
                    instance.tv_content.append("Latitude: " + location.getLatitude());
                    instance.tv_content.append(", Longitude: " + location.getLongitude());
                    instance.tv_content.append(", Altitude: " + location.getAltitude());
                    instance.tv_content.append(", Accuracy: " + location.getAccuracy());
                    instance.tv_content.append(", Bearing: " + location.getBearing());
                    instance.tv_content.append(", Speed: " + location.getSpeed());
                    instance.tv_content.append(", Provider: " + location.getProvider());
                    instance.tv_content.append("\r\n");
                    instance.tv_content.append("\r\n");
                }
            }
        }
    }
}

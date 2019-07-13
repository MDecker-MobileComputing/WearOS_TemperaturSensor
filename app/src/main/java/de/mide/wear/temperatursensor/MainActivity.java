package de.mide.wear.temperatursensor;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;


/**
 * Haupt-Activity der App.
 * <br><br>
 *
 * This file is licensed under the terms of the BSD 3-Clause License.
 */
public class MainActivity extends WearableActivity {

    public static final String TAG4LOGGING = "TemperaturMain";

    /**
     * Lifecycle-Methode, wird einmalig beim Start der Activity ausgefüllt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAmbientEnabled(); // Enables Always-on
    }

}

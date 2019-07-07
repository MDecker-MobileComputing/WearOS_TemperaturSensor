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


public class MainActivity extends WearableActivity
                          implements SensorEventListener {

    public static final String TAG4LOGGING = "TemperaturSensor";

    /** Manager-Objekt für Zugriff auf Sensoren. */
    protected SensorManager _sensorManager = null;

    /** Sensor-Objekt für Umgebungstemperatur. */
    protected Sensor _temperaturSensor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        holeTemperaturSensor();

        setAmbientEnabled(); // Enables Always-on
    }



    protected void holeTemperaturSensor() {

        _sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (_sensorManager == null) {

            zeigeDialog("Sensor-Manager nicht verfügbar.", true);
            return;
        }


        _temperaturSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (_temperaturSensor == null) {

            zeigeDialog("Temperatur-Sensor nicht verfügbar.", true);
            return;
        }

        Log.i(TAG4LOGGING, "Temperatur-Sensor gefunden: " + _temperaturSensor);
    }


    /**
     * Lifecycle-Methode für Übergang von "Sichtbar" auf "Aktiv".
     * Die "gegenteilige" Lifecycle-Methode ist {@link MainActivity#onPause()}.
     * Activity-Instanz wird als Event-Handler-Objekt für den Temperatur-Sensor
     * registriert.
     */
    @Override
    public void onResume() {

        super.onResume();

        _sensorManager.registerListener(this, _temperaturSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Log.i(TAG4LOGGING, "Sensor-Event-Handler registriert.");
    }


    /**
     * Lifecycle-Methode für Übergang von "Aktiv" auf "Sichtbar".
     * Die "gegenteilige" Lifecycle-Methode ist {@link MainActivity#onResume()}.
     * Registrierung der Activity-Instanz als Event-Handler-Objekt wird aufgehoben.
     */
    @Override
    public void onPause() {

        _sensorManager.unregisterListener(this);

        Log.i(TAG4LOGGING, "Registrierung Sensor-Event-Handler aufgehoben.");

        super.onPause();
    }


    /**
     * Eine der beiden Methode aus dem Interface {@link SensorEventListener}.
     *
     * @param event  Durch Änderung Sensor-Wert ausgelöstet Event.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor quellSensor = event.sensor;
        if (quellSensor != _temperaturSensor) {

            String fehlerText = "Interner Fehler: Sensor-Änderung für unerwarteten Sensor " + quellSensor.getName();

            zeigeDialog(fehlerText, true );

            return;
        }

        float[] sensorWerteArray = event.values;

        // Anzahl Sensor-Werte hängt vom Typ des Sensors ab
        Log.i(TAG4LOGGING, "Anzahl Sensor-Werte in Event: " + sensorWerteArray.length);

        float temperaturCelcius = sensorWerteArray[0];

        zeigeDialog("Temperatur: " + temperaturCelcius + " Celsius", false);
    }


    /**
     * Eine der beiden Methode aus dem Interface {@link SensorEventListener}.
     *
     * @param sensor  Sensor, der Event ausgelöst hat (in unserem Fall der Temperatur-Sensor).
     *
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * Hilfsmethode zur Anzeige nach mit einem Dialog.
     *
     * @param nachricht  Text, der im Dialog anzuzeigen ist.
     *
     * @param istFehler {@code true} wenn es sich um eine Fehler-Meldung handelt.
     */
    protected void zeigeDialog(String nachricht, boolean istFehler) {

        if (istFehler) { Log.e(TAG4LOGGING, nachricht); }


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        if(istFehler) {

            dialogBuilder.setTitle(getString(R.string.dialog_titel_fehlermeldung));
        } else {

            dialogBuilder.setTitle(getString(R.string.dialog_titel_ergebnis));
        }
        dialogBuilder.setMessage(nachricht);
        dialogBuilder.setPositiveButton( getString(R.string.dialog_button_ok), null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}

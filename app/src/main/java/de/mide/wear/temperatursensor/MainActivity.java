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
 * Activity zur Abfrage des Sensors für die Messung der aktuellen
 * Umgebungstemperatur.
 * <br><br>
 *
 * This file is licensed under the terms of the BSD 3-Clause License.
 */
public class MainActivity extends WearableActivity
                          implements SensorEventListener {

    public static final String TAG4LOGGING = "TemperaturSensor";

    /** Manager-Objekt für Zugriff auf Sensoren. */
    protected SensorManager _sensorManager = null;

    /** Sensor-Objekt für Umgebungstemperatur. */
    protected Sensor _temperaturSensor = null;

    /** In ScrollView-Element eingebettetes TextView-Element. */
    protected TextView _textView = null;


    /**
     * Lifecycle-Methode, wird einmalig beim Start der Activity ausgefüllt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _textView = findViewById(R.id.hauptTextView);

        holeSensorManagerUndTemperaturSensor();

        setAmbientEnabled(); // Enables Always-on
    }


    /**
     * Methode füllt die Member-Variablen {@link MainActivity#_sensorManager} und
     * {@link MainActivity#_temperaturSensor}; es wird aber noch keine Temperatur-
     * Abfrage gestartet!
     */
    protected void holeSensorManagerUndTemperaturSensor() {

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
     * Methode startet asynchrone Abfrage der Temperatur indem die Activity-Instanz als
     * Event-Handler-Objekt registriert wird.
     */
    protected void starteTemperaturAnfrage() {

        _textView.setText("Abfrage Temperatur-Sensor ...");

        if (_sensorManager == null) {

            zeigeDialog("Interner Fehler: ", true);
            return;
        }
        _sensorManager.registerListener(this, _temperaturSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Log.i(TAG4LOGGING, "Temperatur-Anfrage gestartet.");
    }


    /**
     * Temperaturabfrage wird "beendet" indem die Registrierung der Activity-Instanz
     * als Sensor-Event-Handler aufgehoben wird.
     */
    protected void stoppeTemperaturAnfrage() {

        _sensorManager.unregisterListener(this);

        Log.i(TAG4LOGGING, "Temperatur-Abfrage beendet.");
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

        starteTemperaturAnfrage();
    }


    /**
     * Lifecycle-Methode für Übergang von "Aktiv" auf "Sichtbar".
     * Die "gegenteilige" Lifecycle-Methode ist {@link MainActivity#onResume()}.
     * Registrierung der Activity-Instanz als Event-Handler-Objekt wird aufgehoben.
     */
    @Override
    public void onPause() {

        stoppeTemperaturAnfrage();

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

        float temperaturCelsius = sensorWerteArray[0];

        stoppeTemperaturAnfrage();

        temperaturDarstellen(temperaturCelsius);
    }

    /**
     *
     * @param temperaturCelsius  Darzustellender Temperatur-Wert in Grad Celsius
     */
    protected void temperaturDarstellen(float temperaturCelsius) {

        double temperaturGerundet = ( (int)(temperaturCelsius * 10) ) / 10.0;

        _textView.setText( "Umgebungs-Temperatur:\n\n" + temperaturGerundet + " °C");
    }


    /**
     * Eine der beiden Methode aus dem Interface {@link SensorEventListener}.
     *
     * @param sensor  Sensor, der Event ausgelöst hat (in unserem Fall der Temperatur-Sensor).
     *
     * @param accuracy  Eine der Konstanten {@code SensorManager.SENSOR_STATUS_*}, z.B.
     *                  {@link SensorManager#SENSOR_STATUS_ACCURACY_LOW}.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        String statusStr = "";

        switch (accuracy) {

            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                statusStr = "Hoch";
                break;

            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                statusStr = "Niedrig";
                break;

            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                statusStr = "Mittel";
                break;

            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                statusStr = "Unzuverlässig";
                break;

            case SensorManager.SENSOR_STATUS_NO_CONTACT:
                statusStr = "Sensor hat keinen Kontakt";
                break;

            default: statusStr = "???";
        }

        Log.i(TAG4LOGGING, "Sensor-Genauigkeit: " + statusStr);
    }


    /**
     * Hilfsmethode zur Anzeige nach mit einem Dialog.
     *
     * @param nachricht  Text, der im Dialog anzuzeigen ist.
     *
     * @param istFehler  {@code true} wenn es sich um eine Fehler-Meldung handelt.
     */
    protected void zeigeDialog(String nachricht, boolean istFehler) {

        if (istFehler) { Log.e(TAG4LOGGING, nachricht); }


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        String titleString = "";

        if(istFehler) {
            titleString = getString(R.string.dialog_titel_fehlermeldung);
        } else {
            titleString = getString(R.string.dialog_titel_ergebnis);
        }

        dialogBuilder.setTitle(titleString);
        dialogBuilder.setMessage(nachricht);
        dialogBuilder.setPositiveButton( getString(R.string.dialog_button_ok), null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}

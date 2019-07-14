package de.mide.wear.temperatursensor;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;


/**
 * Fragment für Auslesen der Temperatur von in Smartwatch eingebauten Sensor.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class TemperaturFragment extends Fragment
                                implements SensorEventListener {

    public static final String TAG4LOGGING = "TemperaturFragment";

    /** Manager-Objekt für Zugriff auf Sensoren. */
    protected SensorManager _sensorManager = null;

    /** Sensor-Objekt für Umgebungstemperatur. */
    protected Sensor _temperaturSensor = null;

    /** TextView-Element zur Anzeige Ergebnis */
    protected TextView _textView = null;


    /**
     * Layout-Datei für Fragment mit Inflater laden und View daraus erzeugen ("aufblasen").
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate( R.layout.fragment_temperatur, container, false );
        // attachToRoot=false
    }


    /**
     * Diese Methode entspricht der Methode {@code onCreate(Bundle)} in der Klasse
     * {@link android.app.Activity}
     *
     * @param view Referenz auf View-Objekt, das von Methode
     *             {@link android.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *             mit Inflater erstellt und mit return zurückgegeben wurde.
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        _textView = view.findViewById(R.id.hauptTextView);

        holeSensorManagerUndTemperaturSensor();
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
     * Lifecycle-Methode für Übergang von "Sichtbar" auf "Aktiv".
     * Die "gegenteilige" Lifecycle-Methode ist {@link Fragment#onPause()}.
     * Activity-Instanz wird als Event-Handler-Objekt für den Temperatur-Sensor registriert.
     */
    @Override
    public void onResume() {

        super.onResume();

        starteTemperaturAnfrage();
    }


    /**
     * Lifecycle-Methode für Übergang von "Aktiv" auf "Sichtbar".
     * Die "gegenteilige" Lifecycle-Methode ist {@link Fragment#onResume()}.
     * Registrierung der Activity-Instanz als Event-Handler-Objekt wird aufgehoben.
     */
    @Override
    public void onPause() {

        stoppeTemperaturAnfrage();

        super.onPause();
    }

    /**
     * Methode füllt die Member-Variablen {@link TemperaturFragment#_sensorManager} und
     * {@link TemperaturFragment#_temperaturSensor}; es wird aber noch keine Temperatur-
     * Abfrage gestartet!
     */
    protected void holeSensorManagerUndTemperaturSensor() {

        _sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
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
     * Eine der beiden Methode aus dem Interface {@link SensorEventListener}.
     *
     * @param event  Sensor-Event mit neuem Wert.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor quellSensor = event.sensor;
        if (quellSensor != _temperaturSensor) {

            String fehlerText =
                    "Interner Fehler: Sensor-Änderung für unerwarteten Sensor " +
                    quellSensor.getName() + " erhalten.";
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
     * Methode zur Anzeige der Temperatur auf der UI.
     *
     * @param temperaturCelsius  Darzustellender Temperatur-Wert in Grad Celsius
     */
    protected void temperaturDarstellen(float temperaturCelsius) {

        double temperaturGerundet = ( (int)(temperaturCelsius * 10) ) / 10.0;

        _textView.setText( "Umgebungs-Temperatur:\n\n" + temperaturGerundet + " °C");
    }


    /**
     * Hilfsmethode zur Anzeige von Text mit einem Dialog.
     *
     * @param nachricht  Text, der im Dialog anzuzeigen ist.
     *
     * @param istFehler  {@code true} wenn es sich um eine Fehler-Meldung handelt.
     */
    protected void zeigeDialog(String nachricht, boolean istFehler) {

        if (istFehler) { Log.e(TAG4LOGGING, nachricht); }


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( getContext() );

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

package de.mide.wear.temperatursensor;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * Fragment, in dem der Nutzer einstellen kann, ob er das Ergebnis in der Einheit
 * "Grad Celsius" oder "Grad Fahrenheit" angezeigt bekommen möchte.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class EinstellungenFragment
             extends Fragment
             implements OnCheckedChangeListener {

    public static final String TAG4LOGGING = "EinstellungenFragment";


    /** Dateiname für {@link SharedPreferences}, die von dieser App verwendet werden. */
    public static final String PREF_DATEINAME = "TempPrefs";

    /**
     * Key für {@link SharedPreferences}, unter dem die vom Nutzer gewählte Einheit
     * ({@link #PREF_VALUE_TEMP_EINHEIT_CELSIUS} oder {@link #PREF_VALUE_TEMP_EINHEIT_FAHRENHEIT})
     * für die Anzeige der Temperatur abgewählt wird.
     */
    public static final String PREF_KEY_TEMP_EINHEIT = "temp_einheit";

    /**
     * Wert für Preference mit Key {@link #PREF_KEY_TEMP_EINHEIT}, wenn Nutzer Temperatur in
     * Grad Celsius angezeigt bekommen möchte.
     */
    public static final String PREF_VALUE_TEMP_EINHEIT_CELSIUS = "celsius";

    /**
     * Wert für Preference mit Key {@link #PREF_KEY_TEMP_EINHEIT}, wenn Nutzer Temperatur in
     * Grad Fahrenheit angezeigt bekommen möchte.
     */
    public static final String PREF_VALUE_TEMP_EINHEIT_FAHRENHEIT = "fahrenheit";


    /** RadioButton für Anzeige Temperatur in Grad Celsius. */
    protected RadioButton _radioButtonCelsius = null;

    /** RadioButton für Anzeige Temperatur in Grad Fahrendheit. */
    protected RadioButton _radioButtonFahrenheit = null;

    /**
     * Einstellungen mit Datei-Name {@link #PREF_DATEINAME}, mit Zugriffs-Modus
     * {@link Context#MODE_PRIVATE}, also hat nur diese App selbst Zugriff darauf.
     */
    protected SharedPreferences _sharedPreferences = null;


    /**
     * Layout-Datei für Fragment mit Inflater laden und View daraus erzeugen ("aufblasen").
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate( R.layout.fragment_einstellungen, container, false );
        // attachToRoot=false
    }


    /**
     * Diese Methode entspricht der Methode {@code onCreate(Bundle)} in der Klasse
     * {@link android.app.Activity}. Es wird auch der in den SharedPreferences gespeicherte
     * Zustand wieder hergestellt.
     *
     * @param view  Referenz auf View-Objekt, das von Methode
     *              {@link android.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *              mit Inflater erstellt und mit return zurückgegeben wurde.
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        _radioButtonCelsius    = view.findViewById(R.id.radiobutton_celsius   );
        _radioButtonFahrenheit = view.findViewById(R.id.radiobutton_fahrenheit);

        RadioGroup radioGroup = view.findViewById(R.id.radiogroup_temperatureinheit);
        radioGroup.setOnCheckedChangeListener(this);

        sharedPrefAuswerten( view.getContext() );
    }


    /**
     * Methode liest derzeitigen Wert in SharedPreferences für diese App aus und stellt
     * in Checkbox entsprechend ein.
     *
     * @param context  Context der Activity
     */
    protected void sharedPrefAuswerten(Context context) {

        _sharedPreferences = context.getSharedPreferences(PREF_DATEINAME,
                                                          Context.MODE_PRIVATE );

        String prefEinheit = _sharedPreferences.getString(PREF_KEY_TEMP_EINHEIT,
                PREF_VALUE_TEMP_EINHEIT_CELSIUS
                                    );
        // Celsius ist Default-Wert wenn kein Wert für Key PREF_KEY_TEMP_EINHEIT gefunden wird

        Log.i(TAG4LOGGING, "Einheit für Temperatur ausgelesen: \"" + prefEinheit + "\"");

        switch(prefEinheit) {

            case PREF_VALUE_TEMP_EINHEIT_CELSIUS:
                _radioButtonCelsius.setChecked(true);
            break;

            case PREF_VALUE_TEMP_EINHEIT_FAHRENHEIT:
                _radioButtonFahrenheit.setChecked(true);
            break;

            default:
                Log.w(TAG4LOGGING, "Unerwartete Einheit für Temperatur: \"" + prefEinheit + "\"");
        }
    }


    /**
     * Event-Handler-Methode für die {@link RadioGroup} mit den beiden {@link RadioButton}s
     * zur Auswahl der Einheit für die Temperatur.
     *
     * @param group  RadioGroup-Element, in der sich der ausgewählte RadioButton geändert hat.
     *
     * @param checkedId  ID des RadioButtons, der jetzt ausgewählt ist.
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        String neuerWertPrefTemp = "";

        if (checkedId == _radioButtonCelsius.getId()) {

            neuerWertPrefTemp = PREF_VALUE_TEMP_EINHEIT_CELSIUS;

        } else if (checkedId == _radioButtonFahrenheit.getId()) {

            neuerWertPrefTemp = PREF_VALUE_TEMP_EINHEIT_FAHRENHEIT;

        } else {

            Log.w(TAG4LOGGING,
                    "Event-Handler-Methode für unerwartetes UI-Element aufgerufen: " + checkedId);
            return;
        }


        // Neuen Wert in SharedPreferences-Objekt schreiben
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(PREF_KEY_TEMP_EINHEIT, neuerWertPrefTemp);
        editor.commit();


        Log.i(TAG4LOGGING, "Neue Auswahl für Temperatur-Einheit geschrieben: \"" + neuerWertPrefTemp + "\"");
    }

}

package de.mide.wear.temperatursensor;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.wear.widget.drawer.WearableDrawerController;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;


/**
 * Haupt-Activity der App, konfiguriert den NavigationDrawer.
 * <br><br>
 *
 * Fragment 1: TemperaturFragment<br>
 * Fragment 2: EinstellungenFragment<br>
 * Fragment 3: UeberFragment
 * <br><br>
 *
 * This file is licensed under the terms of the BSD 3-Clause License.
 */
public class MainActivity extends WearableActivity
                          implements WearableNavigationDrawerView.OnItemSelectedListener {

    public static final String TAG4LOGGING = "TemperaturMain";


    /** UI-Element für die "Schublade". */
    private WearableNavigationDrawerView _wearableNavigationDrawerView = null;

    /** Referenz auf FragmentManager wird für Austausch von Fragmenten zur Laufzeit benötigt. */
    private FragmentManager _fragmentManager = null;


    /**
     * Lifecycle-Methode, wird einmalig beim Start der Activity ausgefüllt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _fragmentManager = getFragmentManager();

        Fragment tempFragment = new TemperaturFragment();

        // Fragment einsetzen
        FragmentTransaction ft = _fragmentManager.beginTransaction();
        ft.replace(R.id.platzhalter_inhalt, tempFragment);
        ft.commit();

        _wearableNavigationDrawerView = findViewById(R.id.navigation_drawer_oben);

        MeinNavigationAdapter navigationAdapter = new MeinNavigationAdapter();

        _wearableNavigationDrawerView.setAdapter(navigationAdapter);
        _wearableNavigationDrawerView.addOnItemSelectedListener(this);


        // Kurz die Schublade aufmachen, damit der Nutzer sich ihrer bewusst ist
        WearableDrawerController drawerController = _wearableNavigationDrawerView.getController();
        drawerController.peekDrawer();

        setAmbientEnabled(); // Enables Always-on
    }


    /**
     * Methode wird aufgerufen, wenn mit dem NavigationDrawer ein neues Fragment gewählt wurde.
     * <br>
     *
     * Einzige Methode aus Interface {@link WearableNavigationDrawerView.OnItemSelectedListener}.
     *
     * @param position  0-basierter Index für Element in Schublade.
     */
    @Override
    public void onItemSelected(int position) {

        Fragment fragmentNeu = null;

        switch (position) {

            case 0:
                fragmentNeu = new TemperaturFragment();
                break;

            case 1:
                fragmentNeu = new EinstellungenFragment();
                break;

            case 2:
                fragmentNeu = new UeberFragment();
                break;

            default:
                fragmentNeu = new TemperaturFragment();
                Log.w(TAG4LOGGING, "Unerwartetes Item von NavigationDrawer aufgerufen: " + position);
        }

        FragmentTransaction ft = _fragmentManager.beginTransaction();
        ft.replace(R.id.platzhalter_inhalt, fragmentNeu);
        ft.commit();
    }


    /**
     * Innere Klasse zur Steuerung des NavigationDrawers.
     * Die Member-Variablen der inneren Klasse haben einen doppelten Unterstrich
     * als Prefix.
     */
    private final class MeinNavigationAdapter
            extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter {

        /**
         * Methode zur Abfrage des Anzeige-Texts für ein bestimmtes Element in der Schublade.
         *
         * @param position  0-basierter Index für Element in der Schublade.
         *
         * @return  Anzeige-Text für Schubladen-Eintrag.
         */
        @Override
        public String getItemText(int position) {

            switch (position) {

                case 0:
                    return "Temperatur";

                case 1:
                    return "Einstellungen";

                case 2:
                    return "Über die App";

                default:
                    Log.w(TAG4LOGGING, "Unerwartetes Item von NavigationDrawer aufgerufen: " + position);
                    return "???";
            }
        }


        /**
         * Methode liefert Icon für ein bestimmtes Schubladen-Element.
         * Es wurden die "Material Icons" von Google, die auch in Android Studio "eingebaut"
         * sind, verwendet;
         * siehe <a href="https://material.io/tools/icons/" target="_blank">hier</a> für alle diese
         * Icons auf einer Webseite.
         * Icons werden dem Projekt mit
         * <a href="https://developer.android.com/studio/write/vector-asset-studio#running" target="_blank">Assert Studio</a>
         * hinzugefügt.
         *
         * @param position  0-basierter Index für Element in der Schublade.
         *
         * @return  Icon für Element in Schublade.
         */
        @Override
        public Drawable getItemDrawable(int position) {

            Log.i(TAG4LOGGING, "Icon für Position: " + position);

            int drawableID = android.R.drawable.ic_dialog_alert;

            switch (position) {

                case 0: // TemperaturFragment
                    drawableID = R.drawable.ic_home_black_24dp;
                    break;

                case 1: // EinstellungenFragment
                    drawableID = R.drawable.ic_build_black_24dp;
                    break;

                case 2: // UeberFragment
                    drawableID = R.drawable.ic_info_outline_black_24dp;
                    break;

                default:
                    Log.w(TAG4LOGGING, "Unerwartetes Icon angefordert: " + position);
            }

            return getDrawable(drawableID);
        }


        /**
         * Methode zur Abfrage der Anzahl der Element/Fragmente in der Schublade.
         *
         * @return  Anzahl der Elemente in der Schublade; gibt immer 3 zurück, weil
         *          die App drei Fragmente hat.
         */
        @Override
        public int getCount() {

            return 3;
        }

    } // Ende innere Klasse MeinNavigationAdapter

}

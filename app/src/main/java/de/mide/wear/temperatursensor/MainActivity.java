package de.mide.wear.temperatursensor;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.content.Context;
import android.util.Log;
import android.support.wear.widget.drawer.WearableDrawerLayout;
import android.view.ViewTreeObserver;
import android.view.Gravity;


/**
 * Haupt-Activity der App.
 * <br><br>
 *
 * This file is licensed under the terms of the BSD 3-Clause License.
 */
public class MainActivity extends WearableActivity
        implements ViewTreeObserver.OnGlobalLayoutListener,
                   WearableNavigationDrawerView.OnItemSelectedListener {

    public static final String TAG4LOGGING = "TemperaturMain";

    /** Layout, das den NavigationDrawer ("Schublade") enthält. */
    protected WearableDrawerLayout _wearableDrawerLayout = null;

    /** UI-Element für die "Schublade". */
    protected WearableNavigationDrawerView _wearableNavigationDrawerView = null;

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

        FragmentTransaction ft = _fragmentManager.beginTransaction();
        ft.replace(R.id.platzhalter_inhalt, tempFragment);
        ft.commit();

        _wearableDrawerLayout         = findViewById(R.id.mein_drawer_layout    );
        _wearableNavigationDrawerView = findViewById(R.id.navigation_drawer_oben);

        MeinNavigationAdapter navigationAdapter = new MeinNavigationAdapter( this );

        _wearableNavigationDrawerView.setAdapter(navigationAdapter);
        _wearableNavigationDrawerView.addOnItemSelectedListener(this);


        // siehe Methode onGlobalLayout()
        ViewTreeObserver observer = _wearableDrawerLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(this);

        setAmbientEnabled(); // Enables Always-on
    }


    /**
     * Einzige Methode aus Interface {@link ViewTreeObserver.OnGlobalLayoutListener}.
     * <br><br>
     *
     * Enthält Code, um kurz den NavigationDrawer zu zeigen, so dass der weiß, dass es
     * ihn gibt.
     */
    @Override
    public void onGlobalLayout() {
        /*
        _wearableDrawerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        _wearableDrawerLayout.peekDrawer(Gravity.TOP);
        _wearableDrawerLayout.peekDrawer(Gravity.BOTTOM);
        */
    }

    /**
     * Methode wird aufgerufen, wenn mit dem NavigationDrawer ein neues Fragment
     * gewählt wurde.<br>
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
                fragmentNeu = new AboutFragment();
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

        /** Kontext-Objekt der Activity, die diesen Objekt verwendet. */
        private Context __context = null;


        /**
         * Konstruktor der inneren Klasse.
         *
         * @param context  Kontext-Objekt der Activity, die dieses Objekt verwendet.
         */
        public MeinNavigationAdapter(Context context) {

            __context         = context;
        }


        /**
         * Methode zur Abfrage des Anzeige-Texts für ein bestimmtes Element in der Schublade.
         *
         * @param position  Index in der Schublade
         *
         * @return  Anzeige-Text für Schubladen-Eintrag.
         */
        @Override
        public String getItemText(int position) {

            switch (position) {

                case 0:
                    return "Temperatur";

                case 1:
                    return "Über die App";

                default:
                    Log.w(TAG4LOGGING, "Unerwarteten Item von NavigationDrawer aufgerufen: " + position);
                    return "???";
            }
        }


        /**
         * Methode liefert Icon für ein bestimmtes Schubladen-Element.
         * Um sich die standardmäßig verfügbaren Icons anzuschauen kann man z.B.
         * in den folgenden Ordner des Android-SDKs schauen:
         * {@code SDK/platforms/android-28/data/res/drawable-xxxhdpi}.
         *
         * @param position  0-basierter Index für Element in Schublade
         *
         * @return  Icon für Element in Schublade
         */
        @Override
        public Drawable getItemDrawable(int position) {

            int drawableID = android.R.drawable.ic_dialog_alert;

            switch (position) {

                case 0: // TemperaturFragment
                    drawableID = android.R.drawable.star_on;
                    break;

                case 1: // AboutFragment
                    drawableID = android.R.drawable.ic_dialog_info;
                    break;

                default:
                    Log.w(TAG4LOGGING, "Unerwartetes Icon angefordert: " + position);
            }

            return getDrawable(drawableID);
        }


        /**
         * Methode zur Abfrage der Anzahl der Element/Fragmente in der Schublade.
         *
         * @return Anzahl der Fragmente in der Schublade.
         */
        @Override
        public int getCount() {

            return 2;
        }

    } // Ende der innere Klasse MeinNavigationAdapter

}

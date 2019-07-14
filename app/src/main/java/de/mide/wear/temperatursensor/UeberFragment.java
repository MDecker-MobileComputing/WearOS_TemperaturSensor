package de.mide.wear.temperatursensor;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment für die Informationen <b>über</b> die App selbst, z.B. Copyright-Info.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class UeberFragment extends Fragment {

    /**
     * Layout-Datei für Fragment mit Inflater laden und View daraus erzeugen ("aufblasen").
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate( R.layout.fragment_ueber, container, false );
        // attachToRoot=false
    }

}

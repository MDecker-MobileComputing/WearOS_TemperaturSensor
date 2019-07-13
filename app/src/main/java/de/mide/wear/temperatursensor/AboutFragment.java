package de.mide.wear.temperatursensor;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment für die "About"-Info, z.B. Copyright.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class AboutFragment extends Fragment {

    /**
     * Layout-Datei für Fragment mit Inflater laden und View daraus erzeugen ("aufblasen").
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate( R.layout.fragment_about, container, false );
        // attachToRoot=false
    }


}

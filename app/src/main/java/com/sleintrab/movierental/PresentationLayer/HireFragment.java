package com.sleintrab.movierental.PresentationLayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sleintrab.movierental.R;

/**
 * Created by Niels on 6/15/2017.
 */

public class HireFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.hire_fragment_view,container,false);
    }
}

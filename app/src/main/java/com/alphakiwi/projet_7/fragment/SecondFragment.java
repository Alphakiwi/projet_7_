package com.alphakiwi.projet_7.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphakiwi.projet_7.R;

/**
 * Created by user on 12/31/15.
 */
public class SecondFragment extends Fragment {

    View myView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout, container, false);
        return myView;
    }
}

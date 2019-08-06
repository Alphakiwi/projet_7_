package com.alphakiwi.projet_7.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphakiwi.projet_7.MyAdapter;
import com.alphakiwi.projet_7.R;

import com.alphakiwi.projet_7.model.User;

import java.util.ArrayList;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.google.android.gms.internal.zzfsh.logger;


public class ThirdFragment extends Fragment {


    View myView;
    MyAdapter adapter;

    public static ThirdFragment newInstance() {
        ThirdFragment fragment = new ThirdFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.user_recycler, container, false);


        RecyclerView  recyclerView = (RecyclerView) myView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ArrayList<User> listUser =  getAllUser() ;

        adapter = new MyAdapter(getContext(), listUser);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        return myView;


    }







}

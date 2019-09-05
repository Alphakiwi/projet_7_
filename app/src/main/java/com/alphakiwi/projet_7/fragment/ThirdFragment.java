package com.alphakiwi.projet_7.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphakiwi.projet_7.adapter.MyAdapter;
import com.alphakiwi.projet_7.R;

import com.alphakiwi.projet_7.chat.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUserWithoutMyself;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;


public class ThirdFragment extends Fragment {


    View myView;
    MyAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.user_recycler, container, false);


        FloatingActionButton button = (FloatingActionButton ) myView.findViewById(R.id.fab);
        button.setImageResource(R.drawable.ic_baseline_message_24px);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);

            }
        });


        RecyclerView  recyclerView = (RecyclerView) myView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new MyAdapter(getContext(), getAllUserWithoutMyself(), getUserCurrent(), true);
        recyclerView.setAdapter(adapter);

        return myView;


    }

}

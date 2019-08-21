package com.alphakiwi.projet_7.fragment;

import android.content.Intent;
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

import com.alphakiwi.projet_7.HungryActivity;
import com.alphakiwi.projet_7.MyAdapter;
import com.alphakiwi.projet_7.PresentationActivity;
import com.alphakiwi.projet_7.R;

import com.alphakiwi.projet_7.mentor_chat.MentorChatActivity;
import com.alphakiwi.projet_7.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUserWithoutMyself;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;


public class ThirdFragment extends Fragment {


    View myView;
    MyAdapter adapter;
    ArrayList<User> listUser;

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

        FloatingActionButton button = (FloatingActionButton ) myView.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), MentorChatActivity.class);

                startActivity(i);


            }
        });


        RecyclerView  recyclerView = (RecyclerView) myView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new MyAdapter(getContext(), getAllUserWithoutMyself(), getUserCurrent(), true);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        return myView;


    }







}

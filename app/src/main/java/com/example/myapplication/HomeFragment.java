package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class HomeFragment extends Fragment {
    private TextView random_color_textview, random_outfit_textview;
    private Button add_out_fit_btn, logout_btn;
    private ImageView random_outfit_view;
    private OutfitFragment outfitFragment = new OutfitFragment();
    private CollectionFragment collectionFragment = new CollectionFragment();
    private String[] clrs = {"красного", "оранжевого", "жёлтого", "зелёного", "голубого",
            "синего", "фиолетового", "коричневого", "бежевого", "розового", "чёрного", "серого",
            "белого"};
    private List<String> colors = Arrays.asList(clrs);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Random randomizer = new Random();
        String random_color = colors.get(randomizer.nextInt(colors.size()));
        random_color_textview = (TextView) getView().findViewById(R.id.random_color_text);
        random_color_textview.setText("Может надеть сегодня что-то\n" + random_color + " цвета?");

        add_out_fit_btn = (Button) getView().findViewById(R.id.add_outfit);
        add_out_fit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setNewFragment(R.id.large_frameLayout, outfitFragment);
            }
        });

        // Log.d("MyLog", "LogTest " + MainActivity.outfits);
        random_outfit_textview = (TextView) getView().findViewById(R.id.random_outfit_text);

//        if (MainActivity.outfits.size() != 0) {
//            random_outfit_view = (ImageView) getView().findViewById(R.id.random_outfit);
//            random_outfit_textview.setText("Случайный образ:");
//            // Log.d("MyLog", "Log1" + MainActivity.outfits);
//            Outfit random_outfit = MainActivity.outfits.get(randomizer.nextInt(MainActivity.outfits.size()));
//            Picasso.get().load(random_outfit.image_uri).into(random_outfit_view);
//        } else {
//            random_outfit_textview.setText("перезагрузите страницу");
//        }

        if (MainActivity.outfits.size() != 0) {
            random_outfit_view = (ImageView) getView().findViewById(R.id.random_outfit);
            random_outfit_textview.setText("Случайный образ:");
            // Log.d("MyLog", "Log1" + MainActivity.outfits);
            Outfit random_outfit = MainActivity.outfits.get(randomizer.nextInt(MainActivity.outfits.size()));
            Picasso.get().load(random_outfit.image_uri).into(random_outfit_view);
        }


        logout_btn = (Button) getView().findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).logout();
            }
        });
    }
}
package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class CollectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button search_btn;
    private TextInputEditText search_by_tags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("MyLog", "Log " + MainActivity.outfits);
        GridCustomAdapter adapter = new GridCustomAdapter(getActivity(), MainActivity.outfits);
        recyclerView.setAdapter(adapter);

        search_by_tags = (TextInputEditText)getActivity().findViewById(R.id.search_text);
        search_btn = (Button)getActivity().findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_text;
                search_text = String.valueOf(search_by_tags.getText());
                List<String> search_tags =  new ArrayList<String>(Arrays.asList(search_text.split(" ")));

                List<Outfit> new_outfits_list = new ArrayList<Outfit>();
                for (int i = 0; i < MainActivity.outfits.size(); i++) {
                    Outfit outfit =  MainActivity.outfits.get(i);
                    for (int j = 0; j < search_tags.size(); j++) {
                        if (outfit.tags.contains(search_tags.get(j)) & !new_outfits_list.contains(outfit)) {
                            new_outfits_list.add(outfit);
                        }
                    }
                }

                Collections.sort(new_outfits_list, new Comparator<Outfit>() {
                    @Override
                    public int compare(Outfit o1, Outfit o2) {
                        Integer n1 = - getCounTagsInside(o1, search_tags);
                        Integer n2 = - getCounTagsInside(o2, search_tags);
                        return n1.compareTo(n2);
                    }
                });

                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(layoutManager);
                GridCustomAdapter adapter = new GridCustomAdapter(getActivity(), new_outfits_list);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private Integer getCounTagsInside(Outfit outfit, List<String> searchTags) {
        int n = 0;
        for (int i = 0; i < searchTags.size(); i++) {
            if (outfit.tags.contains(searchTags.get(i))) {
                n++;
            }
        }
        return n;
    }


}
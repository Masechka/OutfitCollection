package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridCustomAdapter extends RecyclerView.Adapter<GridCustomAdapter.MyHolder> {

    private final Context context;
    private final ArrayList<Outfit> outfits;
    private final LayoutInflater inflater;
    private OutfitFragment outfitFragment = new OutfitFragment();

    public GridCustomAdapter(Context context, List<Outfit> outfits) {
        this.context = context;
        this.outfits = (ArrayList<Outfit>) outfits;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_layout_item_list, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Picasso.get().load(outfits.get(position).image_uri).into(holder.outfit_image_view);
        holder.description.setText(first10(outfits.get(position).tags) + "...");
        holder.setClickMethod(position);
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final ImageView outfit_image_view;
        private final TextView description;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            outfit_image_view = itemView.findViewById(R.id.outfit_view);
            description = itemView.findViewById(R.id.description);
        }

        public void setClickMethod(int position) {

            outfit_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)context).openOutfitFragment(R.id.large_frameLayout, outfitFragment, outfits.get(position));
                }
            });
        }
    }

    public String first10(String str) {
        return str.length() < 10 ? str : str.substring(0, 10);
    }
}

package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class OutfitFragment extends Fragment {

    private Button back_btn, add_tag_btn, delete_tag_btn, save_btn;
    public ImageButton outfit_btn;
    private EditText tag_edit_text;
    private TextView tags_text;
    public String tags, image_uri, id;
    public Boolean is_image_added;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outfit, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        outfit_btn = (android.widget.ImageButton) getView().findViewById(R.id.outfit_btn);

        if (is_image_added == null) is_image_added = Boolean.FALSE;
        if (tags == null) tags = ""; //или из БДшки
        if (image_uri != null) {
            // outfit_btn.setImageURI(Uri.parse(image_uri));
            Picasso.get().load(image_uri).into(outfit_btn);
        }

        tags_text = (TextView) getView().findViewById(R.id.tags_text) ;
        update_tags(tags);

        back_btn = (Button) getView().findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).removeFragment(OutfitFragment.this);
            }
        });

        tag_edit_text = (EditText) getView().findViewById(R.id.tag_ed);
        add_tag_btn = (Button) getView().findViewById(R.id.add_tag_btn);
        add_tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag;
                tag = String.valueOf(tag_edit_text.getText());
                if (!TextUtils.isEmpty(tag)) {
                    if (!tags.contains(tag + ",")) {
                    tags = tags + tag + ", ";
                    update_tags(tags);
                    }
                }
            }
        });

        delete_tag_btn = (Button) getView().findViewById(R.id.delete_tag_btn);
        delete_tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag;
                tag = String.valueOf(tag_edit_text.getText());
                if (!TextUtils.isEmpty(tag)) {
                    if (tags.contains(tag + ",")) {
                        tags = tags.replace(tag + ", ", "");
                        update_tags(tags);
                    }
                }
            }
        });

        outfit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
                is_image_added = Boolean.TRUE;
            }
        });


        save_btn = (Button) getView().findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_uri != null & !is_image_added) {
                    MainActivity.save_outfit(id, Uri.parse(image_uri), tags);
                    ((MainActivity) getActivity()).removeFragment(OutfitFragment.this);
                } else if (is_image_added) {
                    uploadImage(tags);
                    ((MainActivity) getActivity()).removeFragment(OutfitFragment.this);
                }
            }
        });
    }


    public void update_tags(String tags) {
        tags_text.setText(tags);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 & data != null & data.getData() != null) {
            //if (requestCode == RESULT_OK) {
            Log.d("MyLog", "Image URI" + data.getData());
            outfit_btn.setImageURI(data.getData());
            //}
        }
    }

    public void getImage() {
        Intent intenChooser = new Intent();
        intenChooser.setType("image/*");
        intenChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intenChooser, 1);
    }


    private void uploadImage(String tags) {
        Bitmap bitmap = ((BitmapDrawable) outfit_btn.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        ((MainActivity)getActivity()).add_image_to_db(byteArray, tags, id);
    }
}
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String USERS_KEY = "User";
    public static final String OUTFIT_KEY = "Outfit";
    public static ImageButton home_btn, collection_btn;
    private HomeFragment homeFragment = new HomeFragment();
    public static DatabaseReference mDataBase, userDataBase;
    private StorageReference mStorageRef;
    private Uri uploadUri;
    public static List<Outfit> outfits = new ArrayList<Outfit>();
    public static Context aplication;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aplication = getApplicationContext();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB");

        String url = "https://outfit-collection-app-default-rtdb.europe-west1.firebasedatabase.app/";
        mDataBase = FirebaseDatabase.getInstance(url).getReference(USERS_KEY);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userDataBase = mDataBase.child(currentFirebaseUser.getUid() + "/" + OUTFIT_KEY + "/");

        getDataFromDB();

        home_btn = findViewById(R.id.home_btn);
        collection_btn = findViewById(R.id.collection_btn);

        // setNewFragment(R.id.frameLayout, homeFragment);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                setNewFragment(R.id.frameLayout, homeFragment);
                int c = Color.parseColor("#123456");
                home_btn.setBackgroundTintList(getResources().getColorStateList(R.color.darkbrown));
                collection_btn.setBackgroundTintList(getResources().getColorStateList(R.color.lightgreen));
            }
        });

        collection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionFragment collectionFragment = new CollectionFragment();
                setNewFragment(R.id.frameLayout, collectionFragment);
                collection_btn.setBackgroundTintList(getResources().getColorStateList(R.color.darkbrown));
                home_btn.setBackgroundTintList(getResources().getColorStateList(R.color.lightgreen));
            }
        });

    }

    public void setNewFragment(int frameLayout, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(frameLayout, fragment);
        ft.commit();
    }

    
    // тут сделать
    public void openOutfitFragment(int frameLayout, OutfitFragment fragment, Outfit outfit) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment.tags = outfit.tags;
        fragment.is_image_added = Boolean.TRUE;
        fragment.image_uri = outfit.image_uri;
        fragment.id = outfit.id;
        ft.replace(frameLayout, fragment);
        ft.commit();

    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();

    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public static void save_outfit(String id, Uri image_uri, String tags) {
        // проверка на наличие в бд

        Outfit new_outfit = new Outfit(id, image_uri.toString(), tags);
        userDataBase.child(id).setValue(new_outfit);
        Toast.makeText(aplication, "Образ успешно сохранён",
                Toast.LENGTH_SHORT).show();
    }

    public void add_image_to_db(byte[] byteArray, String tags, String id) {
        final StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "my_image");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                if (id == null) {
                    String new_id = userDataBase.push().getKey();
                    save_outfit(new_id, uploadUri, tags);
                } else save_outfit(id, uploadUri, tags);
            }
        });
    }

    public void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (outfits.size() > 0) outfits.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Outfit outfit = ds.getValue(Outfit.class);
                    assert outfit != null;
                    outfits.add(outfit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userDataBase.addValueEventListener(vListener);
    }
}
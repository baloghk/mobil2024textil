package com.example.myapplication;

import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TextilListActivity extends AppCompatActivity {
private static final String LOG_TAG = TextilListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth tAuth;
    private int gridNumber = 1;
    private RecyclerView tRecyclerView;
    private ArrayList<TextilItem> tItemsData;
    private TextilItemAdapter tAdapter;

    private FirebaseFirestore tFirestore;
    private CollectionReference tItems;

    private MediaPlayer mp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_textil_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        tRecyclerView = findViewById(R.id.recyclerView);

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tRecyclerView.setLayoutManager(new GridLayoutManager(
                    this, 2));
        } else {
            tRecyclerView.setLayoutManager(new GridLayoutManager(
                    this, gridNumber));
        }

        tItemsData = new ArrayList<>();
        tAdapter = new TextilItemAdapter(this, tItemsData);
        tRecyclerView.setAdapter(tAdapter);

        tFirestore = FirebaseFirestore.getInstance();
        tItems = tFirestore.collection("Items");

        mp = MediaPlayer.create(this, R.raw.jazz);
        mp.setLooping(true);

        initializeData();
        // queryData();
    }

//     private void queryData() {
//
//        tItemsData.clear();
//
//        tItems.get().addOnSuccessListener(queryDocumentSnapshots -> {
//            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                TextilItem textil = document.toObject(TextilItem.class);
//                tItemsData.add(textil);
//            }
//
//            if (tItemsData.isEmpty()) {
//                initializeData();
//                queryData();
//            }
//        });
//
//        tAdapter.notifyDataSetChanged();
//    }

    private void initializeData() {

        tItemsData.clear();

        String[] itemsList = getResources()
                .getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources()
                .getStringArray(R.array.shopping_item_price);
        TypedArray itemsImageResources =
                getResources().obtainTypedArray(R.array.shopping_item_images);

        for (int i = 0; i < itemsList.length; i++) {
            tItemsData.add(new TextilItem(
                    itemsList[i],
                    itemsInfo[i],
                    itemsPrice[i],
                    itemsImageResources.getResourceId(i, 0)));
        }

        itemsImageResources.recycle();

        tAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.textilshop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout_button) {
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }
}
package com.example.fruitproject;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitapp.adapters.FruitAdapter;
import com.example.fruitapp.models.Fruit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseFirestore db;
    private FruitAdapter fruitAdapter;
    private SearchView searchView;
    private ChipGroup categoryChipGroup;
    private List<String> categories = Arrays.asList("All", "Citrus", "Berries", "Tropical", "Stone Fruits");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup RecyclerView
        RecyclerView fruitsRecyclerView = findViewById(R.id.fruitsRecyclerView);
        fruitsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        fruitAdapter = new FruitAdapter(this);
        fruitsRecyclerView.setAdapter(fruitAdapter);

        // Setup SearchView
        setupSearchView();

        // Setup Category Chips
        setupCategoryChips();

        // Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        // Load fruits from Firestore
        loadFruits();
    }

    private void setupSearchView() {
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fruitAdapter.filterFruits(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fruitAdapter.filterFruits(newText);
                return true;
            }
        });
    }

    private void setupCategoryChips() {
        categoryChipGroup = findViewById(R.id.categoryChipGroup);

        for (String category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chip.setClickable(true);

            if (category.equals("All")) {
                chip.setChecked(true);
            }

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (category.equals("All")) {
                        loadFruits();
                    } else {
                        fruitAdapter.filterByCategory(category);
                    }
                }
            });

            categoryChipGroup.addView(chip);
        }
    }

    private void loadFruits() {
        db.collection("fruits")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Fruit> fruits = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Fruit fruit = document.toObject(Fruit.class);
                        fruits.add(fruit);
                    }
                    fruitAdapter.setFruits(fruits);
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            return true;
        } else if (itemId == R.id.navigation_cart) {
            // Navigate to Cart
            return true;
        } else if (itemId == R.id.navigation_orders) {
            // Navigate to Orders
            return true;
        } else if (itemId == R.id.navigation_profile) {
            // Navigate to Profile
            return true;
        }

        return false;
    }
}
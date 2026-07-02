package com.example.myapplication.ui; // Ensure this matches your package

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("ModelTest", "⏳ Fetching a sample product from Firestore...");

        FirebaseFirestore.getInstance().collection("products")
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Product sampleProduct = document.toObject(Product.class);
                            sampleProduct.setId(document.getId());

                            Log.d("ModelTest", "========================================");
                            Log.d("ModelTest", "✅ MAPPING SUCCESSFUL!");
                            Log.d("ModelTest", "Product ID: " + sampleProduct.getId());
                            Log.d("ModelTest", "Name: " + sampleProduct.getName());
                            Log.d("ModelTest", "Category: " + sampleProduct.getCategory());
                            Log.d("ModelTest", "Price: " + sampleProduct.getPrice());
                            Log.d("ModelTest", "Brand: " + sampleProduct.getBrand());
                            Log.d("ModelTest", "Image URL: " + sampleProduct.getImage());
                            if ("CPU".equals(sampleProduct.getCategory())) {
                                Log.d("ModelTest", "Core Count: " + sampleProduct.getCoreCount());
                                Log.d("ModelTest", "Socket Type: " + sampleProduct.getSocket());
                            }
                            Log.d("ModelTest", "========================================");

                        } catch (Exception e) {
                            Log.e("ModelTest", "Mapping failed! The data types do not line up.", e);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ModelTest", "Failed to connect to Firestore", e);
                });
        // --------------------------------------
    }
}
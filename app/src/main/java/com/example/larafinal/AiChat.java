package com.example.larafinal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
// الاستيرادات الصحيحة والمهمة جداً
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.GenerateContentResponse;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * فئة AiChat - محادثة ذكية تعتمد على الموقع الجغرافي و AI Gemini.
 */
public class AiChat extends AppCompatActivity {

    private EditText etTaskTopic;
    private Button btnSuggestSteps;
    private ProgressBar pbLoading;
    private TextView tvAiResponse;

    private GenerativeModelFutures modelFutures;
    private FusedLocationProviderClient fusedLocationClient;
    private String userLocationString = "Unknown Location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        initViews();

        // إعداد Gemini 1.5 Flash
        GenerativeModel gm = FirebaseVertexAI.getInstance().generativeModel("gemini-1.5-flash");
        modelFutures = GenerativeModelFutures.from(gm);

        // إعداد خدمات الموقع
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        pbLoading.setVisibility(View.GONE);
        btnSuggestSteps.setOnClickListener(v -> handleAiSuggestion());
    }

    private void initViews() {
        etTaskTopic = findViewById(R.id.etTaskTopic);
        btnSuggestSteps = findViewById(R.id.btnSuggestSteps);
        pbLoading = findViewById(R.id.pbLoading);
        tvAiResponse = findViewById(R.id.tvAiResponse);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                userLocationString = "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude();
            }
        });
    }

    private void handleAiSuggestion() {
        String userInput = etTaskTopic.getText().toString().trim();
        if (userInput.isEmpty()) {
            Toast.makeText(this, "Please enter a country or activity", Toast.LENGTH_SHORT).show();
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        tvAiResponse.setText("Lara AI is thinking...");
        btnSuggestSteps.setEnabled(false);

        callFirebaseAi(userInput);
    }

    private void callFirebaseAi(String userQuery) {
        String systemInstruction = "You are an expert travel assistant. " +
                "1. Suggest activities for the location. " +
                "2. Use GPS if user asks for 'nearby'. " +
                "3. Response in English, bullet points.";

        String finalPrompt = systemInstruction +
                "\nUser GPS: " + userLocationString +
                "\nUser Query: " + userQuery;

        // استدعاء الـ AI باستخدام الـ Future الصحيح
        com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.ListenableFuture<GenerateContentResponse> responseFuture = modelFutures.generateContent(finalPrompt);

        // إضافة الـ Callback الصحيح من مكتبة Guava
        Futures.addCallback(responseFuture, new androidx.test.espresso.web.util.concurrent.FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    btnSuggestSteps.setEnabled(true);
                    tvAiResponse.setText(result.getText());
                    etTaskTopic.setText(""); // تفريغ الحقل بعد النجاح
                });
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    btnSuggestSteps.setEnabled(true);
                    tvAiResponse.setText("Error: " + t.getMessage());
                    Toast.makeText(AiChat.this, "AI request failed", Toast.LENGTH_SHORT).show();
                });
            }
        }, this.getMainExecutor());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }
}
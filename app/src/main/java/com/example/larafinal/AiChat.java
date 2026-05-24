package com.example.larafinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.larafinal.data.triptable.MyJourney;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;
import com.google.firebase.vertexai.FirebaseVertexAI;

import java.util.concurrent.Executor;

/**
 * AiChat class - Intelligent assistant using Firebase Vertex AI (Gemini).
 */
public class AiChat extends AppCompatActivity {

    private EditText etTaskTopic;
    private Button btnSuggestSteps;
    private ProgressBar pbLoading;
    private TextView tvAiResponse;

    private GenerativeModelFutures model;
    private GenerativeModel ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        // Initialize UI elements
        etTaskTopic = findViewById(R.id.etTaskTopic);
        btnSuggestSteps = findViewById(R.id.btnSuggestSteps);
        pbLoading = findViewById(R.id.pbLoading);
        tvAiResponse = findViewById(R.id.tvAiResponse);

        // Get received Journey from MyJourneyAdapter
        MyJourney receivedJourney = (MyJourney) getIntent().getSerializableExtra("jrn");
        if (receivedJourney != null) {
            // Fill the topic field with the trip name/place
            etTaskTopic.setText(receivedJourney.getTripName());
        }

        // Initialize Firebase Vertex AI (Gemini 1.5 Flash)
        ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel("gemini-3-flash-preview");


        // Use the GenerativeModelFutures Java compatibility layer
        model = GenerativeModelFutures.from(ai);

        // Hide progress bar initially
        pbLoading.setVisibility(View.GONE);

        btnSuggestSteps.setOnClickListener(v -> {
            String topic = etTaskTopic.getText().toString().trim();
            if (!topic.isEmpty()) {
                askFirebaseAiGeminiForSuggestions(topic);
            } else {
                Toast.makeText(this, "Please enter a location first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void askFirebaseAiGeminiForSuggestions(String topic) {
        // Prepare UI for loading
        pbLoading.setVisibility(View.VISIBLE);
        tvAiResponse.setText("Searching for the best spots...");
        btnSuggestSteps.setEnabled(false);

        // Corrected prompt to suggest attractive sites nearby
        String promptStr = "I am visiting '" + topic + "'. " +
                "Can you suggest attractive tourist sites, hidden gems, and interesting activities nearby that I should visit? " +
                "Please provide a structured and descriptive list.";

        Content prompt = new Content.Builder()
                .addText(promptStr)
                .build();

        // Send request to AI
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
        
        // Use the main thread executor to update UI
        Executor executor = this::runOnUiThread;

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                pbLoading.setVisibility(View.GONE);
                btnSuggestSteps.setEnabled(true);
                tvAiResponse.setText(result.getText());
            }

            @Override
            public void onFailure(Throwable t) {
                pbLoading.setVisibility(View.GONE);
                btnSuggestSteps.setEnabled(true);
                tvAiResponse.setText("Error occurred while fetching suggestions.");
                Toast.makeText(AiChat.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, executor);
    }
}

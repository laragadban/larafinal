package com.example.larafinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.GenerateContentResponse;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        // Initialize UI elements
        etTaskTopic = findViewById(R.id.etTaskTopic);
        btnSuggestSteps = findViewById(R.id.btnSuggestSteps);
        pbLoading = findViewById(R.id.pbLoading);
        tvAiResponse = findViewById(R.id.tvAiResponse);

        // Initialize Firebase Vertex AI (Gemini 1.5 Flash)
        GenerativeModel gm = FirebaseVertexAI.getInstance()
                .generativeModel("gemini-1.5-flash");
        
        // Use the GenerativeModelFutures Java compatibility layer
        model = GenerativeModelFutures.from(gm);

        // Hide progress bar initially
        pbLoading.setVisibility(View.GONE);

        btnSuggestSteps.setOnClickListener(v -> {
            String topic = etTaskTopic.getText().toString().trim();
            if (!topic.isEmpty()) {
                askFirebaseAiGeminiForSteps(topic);
            } else {
                Toast.makeText(this, "Please enter a topic first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void askFirebaseAiGeminiForSteps(String topic) {
        // Prepare UI for loading
        pbLoading.setVisibility(View.VISIBLE);
        tvAiResponse.setText("Thinking...");
        btnSuggestSteps.setEnabled(false);

        // Build the prompt
        String promptStr = "I want to perform the following task: '" + topic + "'. " +
                "Can you suggest a clear, step-by-step checklist to complete this task effectively?";

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
                tvAiResponse.setText("Error occurred.");
                Toast.makeText(AiChat.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, executor);
    }
}

package com.example.larafinal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larafinal.data.Appdatabase;
import com.example.larafinal.data.MyUserTable.MyUser;
import com.example.larafinal.data.triptable.MyJourney;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * AddJourneyActivity
 *
 * <p>
 * هذه الواجهة مسؤولة عن إضافة رحلة جديدة إلى التطبيق.
 * </p>
 */
public class AddJourneyActivity extends AppCompatActivity {

    // ================== عناصر الواجهة ==================
    private TextView tvHeader, tvTripType, tvRating;
    private TextInputEditText etTripName, etCountry, etTown, etAddress, etDescription, etLatitude, etLongitude;
    private RadioGroup rgTripType;
    private RadioButton rbBusiness, rbLeisure, rbFamily;
    private Slider sliderRating;
    private Button btnSaveTrip, btnGetLocation;
    private ImageView ivSelectedImage;

    private Uri selectedImageUri;
    private ActivityResultLauncher<String> pickImage;
    private ActivityResultLauncher<String> requestLocationPermission;
    
    // Media Permissions Launchers
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initViews();
        initPermissionLaunchers();
        checkAndRequestMediaPermissions();

        btnSaveTrip.setOnClickListener(v -> validateFields());
        btnGetLocation.setOnClickListener(v -> checkLocationPermission());

        ivSelectedImage.setOnClickListener(v -> pickImage.launch("image/*"));
    }

    private void initViews() {
        tvHeader = findViewById(R.id.tvHeader);
        tvTripType = findViewById(R.id.tvTripType);
        tvRating = findViewById(R.id.tvRating);
        etTripName = findViewById(R.id.etTripName);
        etCountry = findViewById(R.id.etcountry);
        etTown = findViewById(R.id.etTown);
        etAddress = findViewById(R.id.etAddress);
        etDescription = findViewById(R.id.etDescription);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        rgTripType = findViewById(R.id.rgTripType);
        rbBusiness = findViewById(R.id.rbBusiness);
        rbLeisure = findViewById(R.id.rbLeisure);
        rbFamily = findViewById(R.id.rbFamily);
        sliderRating = findViewById(R.id.sliderRating);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        ivSelectedImage = findViewById(R.id.ivTripImage);
    }

    private void initPermissionLaunchers() {
        // Image Picker
        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                selectedImageUri = result;
                ivSelectedImage.setImageURI(result);
                ivSelectedImage.setVisibility(View.VISIBLE);
            }
        });

        // Location Permission
        requestLocationPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        // Media Permissions
        requestReadMediaImagesPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) Toast.makeText(this, "Image permission denied", Toast.LENGTH_SHORT).show();
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkAndRequestMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        etLatitude.setText(String.valueOf(location.getLatitude()));
                        etLongitude.setText(String.valueOf(location.getLongitude()));
                        Toast.makeText(AddJourneyActivity.this, "Location detected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddJourneyActivity.this, "Unable to find location. Please ensure GPS is ON.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateFields() {
        boolean isValid = true;
        String tripName = etTripName.getText().toString().trim();
        if (tripName.isEmpty()) { etTripName.setError("Required"); isValid = false; }
        
        String country = etCountry.getText().toString().trim();
        if (country.isEmpty()) { etCountry.setError("Required"); isValid = false; }

        if (isValid) {
            MyJourney myJourney = new MyJourney();
            myJourney.setTripName(tripName);
            myJourney.setCountry(country);
            myJourney.setTown(etTown.getText().toString().trim());
            myJourney.setAddress(etAddress.getText().toString().trim());
            myJourney.setDescription(etDescription.getText().toString().trim());
            
            try {
                String latStr = etLatitude.getText().toString().trim();
                String lonStr = etLongitude.getText().toString().trim();
                if (!latStr.isEmpty()) myJourney.setLat(Double.parseDouble(latStr));
                if (!lonStr.isEmpty()) myJourney.setLang(Double.parseDouble(lonStr));
            } catch (Exception e) {
                Log.e("AddJourney", "Error parsing coordinates", e);
            }

            if (selectedImageUri != null) myJourney.setImage(convertImageToString(selectedImageUri));
            myJourney.setRating(String.valueOf(sliderRating.getValue()));

            int selectedId = rgTripType.getCheckedRadioButtonId();
            if (selectedId == R.id.rbBusiness) myJourney.setType("Business");
            else if (selectedId == R.id.rbLeisure) myJourney.setType("Leisure");
            else if (selectedId == R.id.rbFamily) myJourney.setType("Family");

            saveMyJourney(myJourney);
        }
        return isValid;
    }

    private void saveMyJourney(MyJourney myJourney) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = database.child("Journeys");
        DatabaseReference newUserRef = usersRef.push();
        myJourney.setKey(newUserRef.getKey());
        
        newUserRef.setValue(myJourney).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddJourneyActivity.this, "Journey saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddJourneyActivity.this, "Failed to save journey", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String convertImageToString(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
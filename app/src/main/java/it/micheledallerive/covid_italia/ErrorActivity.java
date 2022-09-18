package it.micheledallerive.covid_italia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String messageError = getIntent().getStringExtra("error");
        ((TextView)findViewById(R.id.error_description)).setText(messageError);
        MaterialButton retryButton = findViewById(R.id.retry_button);
        retryButton.setOnClickListener(v -> {
            Intent i = new Intent(this, LoadingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }
}

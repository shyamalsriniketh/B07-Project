package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Technique_helper extends AppCompatActivity {
    WebView webView;
    Button backButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technique_helper);

        webView = findViewById(R.id.Technique_Helper_Video);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Lx_e5nXfi5w?si=9goNGZ_4oTlsFiPh\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        backButton = findViewById(R.id.technique_Helper_Back_Button);
        nextButton = findViewById(R.id.Technique_Helper_Next_Button);
        Intent i = getIntent();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Technique_helper.this, Pre_check.class);
            intent.putExtra("medicineType", i.getStringExtra("medicineType"));
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(Technique_helper.this, Logging_PostCheck.class);
            intent.putExtra("medicineType", i.getStringExtra("medicineType"));
            intent.putExtra("breathRating", i.getStringExtra("breathRating"));
            if (i.hasExtra("prePEF")) {
                intent.putExtra("prePEF", i.getIntExtra("prePEF", 0));
                intent.putExtra("preTimestamp", i.getLongExtra("preTimestamp", 0));
            }
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}

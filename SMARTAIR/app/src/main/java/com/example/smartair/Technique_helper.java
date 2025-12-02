package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Technique_helper extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    Button backButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technique_helper);
        backButton = findViewById(R.id.technique_Helper_Back_Button);
        nextButton = findViewById(R.id.Technique_Helper_Next_Button);
        Intent i = getIntent();
        youTubePlayerView = findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "Lx_e5nXfi5w";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

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
package com.example.smartair;

import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class HistoryBrowser extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ArrayList<HistoryItem> historyList = new ArrayList<>();
    private Button exportPdfBtn;
    private String childID = "childID_example";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_browser);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if (getIntent() != null) {
            if (getIntent().hasExtra("childId")) {
                childID = getIntent().getStringExtra("childId");
            } else if (getIntent().hasExtra("childID")) {
                childID = getIntent().getStringExtra("childID");
            }
        }

        recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);


        exportPdfBtn = findViewById(R.id.exportPdfBtn);
        exportPdfBtn.setOnClickListener(v -> exportPDF());

        loadHistory();
    }

    private void loadHistory() {
        DatabaseReference logsRef =
                FirebaseDatabase.getInstance().getReference("logs").child(childID);

        historyList.clear();

        logsRef.child("zoneChanges").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.getKey() == null) continue;
                    try {
                        long time = Long.parseLong(s.getKey());
                        String zone = s.getValue(String.class);
                        if (zone == null) zone = "Unknown zone";
                        String formatted = "Zone Change: " + zone;
                        historyList.add(new HistoryItem(time, HistoryItem.TYPE_ZONE, formatted));
                    } catch (NumberFormatException ignored) {}
                }
                loadIncidents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadIncidents() {
        DatabaseReference logsRef =
                FirebaseDatabase.getInstance().getReference("logs").child(childID);

        logsRef.child("incidentLogs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.getKey() == null) continue;
                    try {
                        long time = Long.parseLong(s.getKey());
                        IncidentLog log = s.getValue(IncidentLog.class);
                        if (log == null) continue;

                        String formatted =
                                "Incident Type: " + safeStr(log.category) + "\n" +
                                        "PEF: " + log.pef + "\n" +
                                        "Rescue Attempts: " + log.rescueAttempts + "\n" +
                                        "Can't Speak: " + (log.cantSpeak ? "Yes" : "No") + "\n" +
                                        "Chest Pulling: " + (log.chestPulling ? "Yes" : "No") + "\n" +
                                        "Blue Lips: " + (log.blueLips ? "Yes" : "No") + "\n" +
                                        "Steps Shown: " + safeStr(log.stepsShown);

                        historyList.add(new HistoryItem(time, HistoryItem.TYPE_INCIDENT, formatted));
                    } catch (NumberFormatException ignored) {}
                }

                // newest first
                Collections.sort(historyList, (a, b) -> Long.compare(b.timestamp, a.timestamp));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private String safeStr(String s) {
        return s == null ? "" : s;
    }

    private String prettyTime(long millis) {
        return new SimpleDateFormat("EEE, MMM d — h:mm a", Locale.getDefault()).format(new Date(millis));
    }

    private void exportPDF() {
        if (historyList.isEmpty()) return;

        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();
        paint.setTextSize(12 * getResources().getDisplayMetrics().density);

        final int pageWidth = 595;
        final int pageHeight = 842;
        int pageNumber = 1;

        PdfDocument.Page page = pdf.startPage(new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create());
        android.graphics.Canvas canvas = page.getCanvas();

        int x = 30;
        int y = 50;
        int lineHeight = (int) (18 * getResources().getDisplayMetrics().density);
        int maxLineWidth = pageWidth - x - 30;

        for (int i = 0; i < historyList.size(); i++) {
            HistoryItem item = historyList.get(i);
            String header = prettyTime(item.timestamp) + " — " + (item.type.equals(HistoryItem.TYPE_ZONE) ? "Zone change" : "Incident");
            canvas.drawText(header, x, y, paint);
            y += lineHeight;


            for (String line : wrapText(item.detail, paint, maxLineWidth)) {
                canvas.drawText(line, x + 8, y, paint);
                y += lineHeight;
            }
            y += (lineHeight / 2);

            if (y > pageHeight - 80) {
                pdf.finishPage(page);
                pageNumber++;
                page = pdf.startPage(new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create());
                canvas = page.getCanvas();
                y = 50;
            }
        }

        pdf.finishPage(page);


        File out = new File(getExternalFilesDir(null), "child_history_" + childID + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(out)) {
            pdf.writeTo(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pdf.close();
        }
    }


    private java.util.List<String> wrapText(String text, Paint paint, int maxWidth) {
        java.util.List<String> lines = new ArrayList<>();
        if (text == null) return lines;
        String[] words = text.split("\\s+");
        StringBuilder cur = new StringBuilder();
        for (String w : words) {
            String test = cur.length() == 0 ? w : cur + " " + w;
            if (paint.measureText(test) > maxWidth) {
                if (cur.length() > 0) {
                    lines.add(cur.toString());
                    cur = new StringBuilder(w);
                } else {
                    lines.add(w);
                    cur = new StringBuilder();
                }
            } else {
                if (cur.length() == 0) cur.append(w);
                else cur.append(" ").append(w);
            }
        }
        if (cur.length() > 0) lines.add(cur.toString());
        return lines;
    }
}

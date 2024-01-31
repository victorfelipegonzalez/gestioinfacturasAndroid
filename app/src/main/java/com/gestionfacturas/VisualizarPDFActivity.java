package com.gestionfacturas;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class VisualizarPDFActivity extends AppCompatActivity {
    private PDFView vistaPDF;
    private File pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pdfactivity);
        vistaPDF = findViewById(R.id.pdfView);
        pdf = (File) getIntent().getSerializableExtra("PDF");
        vistaPDF.fromFile(pdf)
                .enableDoubletap(true)
                .enableSwipe(true)
                .defaultPage(0)
                .load();


    }


}
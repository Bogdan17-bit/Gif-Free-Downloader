package com.example.giffreedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.giffreedownloader.error.ErrorHandler;
import com.example.giffreedownloader.gif.controller.GifDownloader;


public class GifFullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gif_full_screen);

        loadMainImageInImageView();

        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlImage = getStringFromIntent("imageURL");
                loadGifToStorage(urlImage);
            }
        });

    }

    private void loadMainImageInImageView() {
        ImageView singleImage = findViewById(R.id.singleImageView);
        Glide.with(this).
                load(getIntent().getStringExtra("imageURL"))
                .into(singleImage);
    }

    private String getStringFromIntent(String name) {
        return getIntent().getStringExtra(name);
    }

    private void loadGifToStorage(String gifUrl) {
        GifDownloader gifDownloader = new GifDownloader(gifUrl);
        DownloadManager.Request request = gifDownloader.createRequestOnDownload(gifUrl);
        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        ErrorHandler.makeToast(this, "Downloading started...");
    }

}
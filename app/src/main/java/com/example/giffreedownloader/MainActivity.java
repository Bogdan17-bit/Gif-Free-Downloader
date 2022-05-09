package com.example.giffreedownloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Layout;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.giffreedownloader.api.data.model.GifsSaved;
import com.example.giffreedownloader.error.ErrorHandler;
import com.example.giffreedownloader.systems.controller.DisplayProperty;
import com.example.giffreedownloader.api.api.settings.NetworkService;
import com.example.giffreedownloader.api.data.model.Gif;
import com.example.giffreedownloader.api.data.model.JSONResponse;
import com.example.giffreedownloader.systems.controller.KeyboardProperty;
import com.example.giffreedownloader.systems.controller.NetworkProperty;

import org.intellij.lang.annotations.JdkConstants;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Random;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final String GIFS_SAVED = "GIFS_SAVED";
    JSONResponse gifsOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ErrorHandler.controlConnection(this);
        checkDownloadPermission();
        gifsOriginal = (JSONResponse) getLastCustomNonConfigurationInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertGifsTableLayoutInsideActivityMain();
        setListenerButtonSearch();

        try {
            loadGifs();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        checkSizeScrollView();
    }

    private void checkDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    public void checkSizeScrollView() {
        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutScroll);
        if(!horizontalScrollView.canScrollHorizontally(0) &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            linearLayout.setLayoutParams(params);
        }
    }

    public void loadGifs() throws MalformedURLException {
        if(gifsOriginal == null) {
            setTrendingGifs();
        }
        else {
            setGifs(gifsOriginal.getGifs());
        }
    }

    @Nullable
    @Override
    public JSONResponse onRetainCustomNonConfigurationInstance() {
        return gifsOriginal;
    }

    private void insertGifsTableLayoutInsideActivityMain() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.gifs_table, null);;

        LinearLayout linearLayout = findViewById(R.id.linearGifs);
        linearLayout.addView(view);
    }

    private boolean editTextIsNotEmpty() {
        EditText editText = findViewById(R.id.searchEditTextBox);
        if(editText.getText().length() >= 2) {
            return true;
        }
        else {
            return false;
        }
    }

    private void clearEditText() {
        EditText editText = findViewById(R.id.searchEditTextBox);
        editText.setText("");
    }

    public void setListenerButtonSearch() {
        EditText editText = findViewById(R.id.searchEditTextBox);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(KeyboardProperty.enterKeyIsPressed(i, keyEvent) && editTextIsNotEmpty()) {
                    clearTable();
                    onSearchClicked(textView);
                }
                return false;
            }
        });
    }

    public void setTrendingGifs() {
        NetworkService.getInstance().getJSONAPI().getTrendingGifs().enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                try {
                    assert response.body() != null;
                    setGifs(response.body().getGifs());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
    }

    private void resetPreviousSearch() {
        clearEditText();
        clearTable();
    }

    private int getRandomOffset() {
        return new Random().nextInt(100);
    }

    public void onClickButtonWithTag(View v) throws IOException {
       resetPreviousSearch();
       NetworkService.getInstance().getJSONAPI().getGifsFromSearch(((Button) v).getText().toString(), getRandomOffset()).enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                try {
                    assert response.body() != null;
                    if(!((Button) v).getText().toString().equals("#Trends")) {
                        setGifs(response.body().getGifs());
                    }
                    else{
                        setTrendingGifs();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
    }

    public void clearTable() {
        TableLayout tableLayoutGifs = findViewById(R.id.tableView);
        tableLayoutGifs.removeAllViews();
    }

    public void onSearchClicked(View view)
    {
        if(!editTextIsNotEmpty()) {
            return;
        }
        searchGifsFromEditText();
    }

    public void searchGifsFromEditText() {
        EditText editText = findViewById(R.id.searchEditTextBox);

        KeyboardProperty.hideKeyboard(this);
        editText.clearFocus();

        String textRequest = editText.getText().toString();


        NetworkService.getInstance().getJSONAPI().getGifsFromSearch(textRequest, getRandomOffset()).enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                try {
                    assert response.body() != null;
                    clearTable();
                    setGifs(response.body().getGifs());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
    }

    public int calculateNumberGifsInRowAvailable() {
        Pair<Integer, Integer> size = DisplayProperty.getDisplaySize(this);
        if(size.first <= 500) {
            return 1;
        }
        else {
            return ((size.first + 500) / 1000 * 1000) / 500;
        }
    }

    private final ImageView.OnClickListener imageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String URL = (String)v.getTag();
            openGifsInFullScreen(URL);
        }
    };

    private void openGifsInFullScreen(String URL) {
        Intent intent = new Intent(MainActivity.this, GifFullScreenActivity.class);
        intent.putExtra("imageURL", URL);
        startActivity(intent);
    }

    private void fillingAdaptiveTable(TableLayout tableLayoutGifs, List<Gif> gifsList, int gifsInRowRequired) {

        TableRow tableRow = new TableRow(this);
        int counterGifsInRow = 0;

        for(Gif gif : gifsList) {

            ImageView imageView = new ImageView(this);
            imageView.setTag(gif.getImage().getParameters().getUrl());
            Pair<Integer, Integer> size = DisplayProperty.getDisplaySize(this);

            int width = size.first;
            int height = size.second;

            String pathCurrentGif = gif.getImage().getParameters().getUrl();
            Glide.with(this)
                    .load(pathCurrentGif)
                    .centerCrop()
                    .apply(new RequestOptions().override(width/gifsInRowRequired))
                    .into(imageView);

            if(counterGifsInRow == gifsInRowRequired - 1) {
                tableRow.addView(imageView, counterGifsInRow);
                tableRow.getVirtualChildAt(counterGifsInRow).setOnClickListener(imageClick);
                tableLayoutGifs.addView(tableRow);
                tableRow = new TableRow(this);
                counterGifsInRow = 0;
            }

            else {
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tableRow.addView(imageView, counterGifsInRow);
                tableRow.getVirtualChildAt(counterGifsInRow).setOnClickListener(imageClick);
                counterGifsInRow ++;
            }

        }

    }

    public void setGifs(List<Gif> gifs) throws MalformedURLException {

        clearTable();

        gifsOriginal = new JSONResponse(gifs);

        KeyboardProperty.hideKeyboard(this);

        int gifsInRowRequired = calculateNumberGifsInRowAvailable();

        TableLayout tableLayoutGifs = findViewById(R.id.tableView);
        fillingAdaptiveTable(tableLayoutGifs, gifs, gifsInRowRequired);

    }

}
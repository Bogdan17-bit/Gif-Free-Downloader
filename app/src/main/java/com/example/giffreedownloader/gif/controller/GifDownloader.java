package com.example.giffreedownloader.gif.controller;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.URLUtil;


public class GifDownloader {

    private DownloadManager.Request request;

    public GifDownloader(String url) {
        request = new DownloadManager.Request(Uri.parse(url));
    }

    public DownloadManager.Request getRequest() {
        return request;
    }

    public void setRequest(DownloadManager.Request request) {
        this.request = request;
    }

    private void setCookie(String url) {
        String cookie = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookie);
    }

    private void addSettings(String url, String title) {
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
    }

    private void setRequestTitleAndDescription(String url, String description) {
        request.setTitle(getTitleFromUrl(url));
        request.setDescription(description);
    }

    private String getTitleFromUrl(String url) {
        return URLUtil.guessFileName(url, null, null);
    }

    public DownloadManager.Request createRequestOnDownload(String url) {

        setRequestTitleAndDescription(url, "Downloading file...");
        setCookie(url);
        addSettings(url, getTitleFromUrl(url));

        return request;

    }

}

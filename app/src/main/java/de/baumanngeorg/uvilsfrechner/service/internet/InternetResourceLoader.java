package de.baumanngeorg.uvilsfrechner.service.internet;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class InternetResourceLoader {

    private static InternetResourceLoader instance;

    private final RequestQueue queue;

    private InternetResourceLoader(Context context) {
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
    }

    public static void initializeInternetResourceLoader(Context context) {
        if (instance == null) {
            instance = new InternetResourceLoader(context);
        }
    }

    public static InternetResourceLoader getInstance() {
        return instance;
    }

    public void addRequest(Request<?> request) {
        this.queue.add(request);
    }
}

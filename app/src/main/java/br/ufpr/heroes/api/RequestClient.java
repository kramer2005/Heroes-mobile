package br.ufpr.heroes.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;

public class RequestClient {
    public static final String API_URL = "http://192.168.0.51:3001";
    public static final String COOKIE_NAME = "heroes-api";
    private static RequestClient instance;
    private RequestQueue requestQueue;
    private final ImageLoader imageLoader;
    private static Context ctx;
    private static CookieStore cookieStore;
    private boolean firstRequest = true;

    private RequestClient(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized RequestClient getInstance(Context context) {
        if (instance == null) {
            instance = new RequestClient(context);
        }
        return instance;
    }

    public static void setCookieStore() {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        cookieStore = manager.getCookieStore();
    }

    public static CookieStore getCookieStore() {
        return RequestClient.cookieStore;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}

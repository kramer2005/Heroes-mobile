package br.ufpr.ws_mutantes.utils;

import android.os.Handler;

import androidx.annotation.NonNull;

public class Debouncer<V> {

    private final int mInterval;
    private final Callback<V> mCallback;
    private final Handler mHandler;

    public Debouncer(int intervalInMills, @NonNull Callback<V> callback) {
        mInterval = intervalInMills;
        mCallback = callback;
        mHandler = new Handler();
    }


    private Runnable currentRunnable;

    public void consume(V key) {
        mHandler.removeCallbacksAndMessages(null);
        if (currentRunnable != null) {
            mHandler.removeCallbacks(currentRunnable);// if running cancel
        }
        currentRunnable = new Counter<>(key, mCallback);
        mHandler.postDelayed(currentRunnable, mInterval);
    }

    public static class Counter<T> implements Runnable {
        private final T mDeliverable;
        private final Callback<T> mCallback;

        Counter(T deliverable, Callback<T> callback) {
            mDeliverable = deliverable;
            mCallback = callback;
        }

        @Override
        public void run() {
            mCallback.onEmit(mDeliverable);
        }
    }

    public interface Callback<T> {
        void onEmit(T key);
    }
}
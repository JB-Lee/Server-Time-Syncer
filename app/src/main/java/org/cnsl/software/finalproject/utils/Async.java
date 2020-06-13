package org.cnsl.software.finalproject.utils;

import android.os.AsyncTask;

import java.util.concurrent.Callable;

public class Async {

    interface Callback<T> {
        void onResult(T result);

        void onError(Exception e);
    }

    public static class Executor<T> extends AsyncTask<Void, Void, T> {

        private Callback<T> callback;
        private Callable<T> callable;
        private Exception exception;

        public Executor<T> setCallback(Callback<T> callback) {
            this.callback = callback;
            return this;
        }

        public Executor<T> setCallable(Callable<T> callable) {
            this.callable = callable;
            return this;
        }

        @Override
        protected T doInBackground(Void... voids) {
            try {
                return callable.call();
            } catch (Exception e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(T result) {
            if (isExcepted()) {
                notifyException();
                return;
            }
            notifyResult(result);
        }

        private boolean isExcepted() {
            return exception != null;
        }

        private void notifyException() {
            if (callback != null)
                callback.onError(exception);
        }

        private void notifyResult(T result) {
            if (callback != null)
                callback.onResult(result);
        }
    }

}

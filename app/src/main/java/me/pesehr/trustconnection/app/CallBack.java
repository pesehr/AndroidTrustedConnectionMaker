package me.pesehr.trustconnection.app;

public interface CallBack<T> {
    void onCall(T response);
    void onProcess();
}

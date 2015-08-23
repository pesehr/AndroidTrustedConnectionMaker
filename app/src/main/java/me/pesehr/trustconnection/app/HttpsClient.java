package me.pesehr.trustconnection.app;

import com.squareup.okhttp.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by pesehr on 23/08/2015.
 */
public final class HttpsClient extends OkHttpClient {

    public HttpsClient(SSLSocketFactory factory,HostnameVerifier hv){
        setSslSocketFactory(factory);
        setHostnameVerifier(hv);
    }

    public HttpsClient(SSLSocketFactory factory){
        setSslSocketFactory(factory);
        setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
    }

    public HttpsClient(SSLSocketFactory factory, final String Url){
        setSslSocketFactory(factory);
        setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return HttpsURLConnection.getDefaultHostnameVerifier(). verify(Url,sslSession);
            }
        });
    }

}

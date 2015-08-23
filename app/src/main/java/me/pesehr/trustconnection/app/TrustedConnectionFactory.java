package me.pesehr.trustconnection.app;

import android.os.AsyncTask;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;


public class TrustedConnectionFactory  {

    static {

        // add more ssl algorithm
        String TSL = "TSL";
    }

    HttpsURLConnection connection;
    CallBack<Certificate[]> callBack;
    AsyncTask connectionAsyncTask = new AsyncTask(){

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                connection.connect();
                callBack.onProcess();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                Certificate[] certificates = connection.getServerCertificates();
                callBack.onCall(certificates);
            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            }
        }
    } ;

     static HttpsURLConnection buildConnection(final String URL , String algotithm)
            throws NoSuchAlgorithmException, IOException {
        SSLContext sslContext = SSLContext.getInstance(algotithm);
        sslContext.getSocketFactory().createSocket();
        URL httpsURL = new URL(URL);
        HttpsURLConnection connection = (HttpsURLConnection) httpsURL.openConnection();

        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return s.equals(URL);
            }
        });

        return connection;
    }

     void getCertificates(String URL,String algorithm,CallBack<Certificate[]> callBack)
             throws IOException, NoSuchAlgorithmException, InterruptedException {

        connection = buildConnection(URL, algorithm);
        connectionAsyncTask.execute();

    }

    static SocketFactory getSocketFactory(String url,Certificate[] ca , String algorithm )
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException
            , IOException, KeyManagementException {

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        for(int c  = 0 ; c < ca.length ; c++)
            keyStore.setCertificateEntry("ca"+c, ca[c]);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance(algorithm);
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }


}
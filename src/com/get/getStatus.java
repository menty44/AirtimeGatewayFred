package com.get;

/*
  Created by fred on 9/14/16.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class getStatus {

    //Bypassing the SSL verification to execute our code successfully
    static {
        try {
            disableSSLVerification();
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(post.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    //Method used for bypassing SSL verification
    public static void disableSSLVerification() throws NoSuchAlgorithmException {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        } };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public  static void main(String[]args){

        String api_username = "tawi";
        String session_id = "uo2opIvCFoPSNdQf";
        String reference_number = "abc123";

//        System.out.println("\nOutput: \n" + callURL("https://localhost:8443/AirtimeGateway/0.3/getNonce?" + api_username + device_id));
        System.out.println("\nOutput: \n" + callURL("https://localhost:8443/AirtimeGateway/0.3/getNonce?" + "api_username=" + api_username
                + "&session_id=" + session_id + "&reference_number=" + reference_number));

    }

    public static String callURL(String myURL) {
        System.out.println("Requeted URL:" + myURL);
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;

        try {

            URL url = new URL(myURL);
            urlConn = url.openConnection();

            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);

            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());

                BufferedReader bufferedReader = new BufferedReader(in);

                if (bufferedReader != null) {
                    int cp;

                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);

                    }
                    bufferedReader.close();
                }

            }

            in.close();

        } catch (Exception e) {

            throw new RuntimeException("Exception while calling URL:"+ myURL, e);

        }
        return sb.toString();


    }
}

package com.example.amfgps.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;

public class Network {
    private Context _context;
    public Network(Context context) {  this._context = context;
    }

    /**
     * Función para comprobar si hay conexión a Internet
     * @param context
     * @return boolean
     */

    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if ((redes[i].getState() == NetworkInfo.State.CONNECTED) ) {
                connected = true;
            }
        }
        return connected;
    }
    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) this._context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(300);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return false;
            }
        }
        return false;
    }
}

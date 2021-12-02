package com.example.amfgps;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.amfgps.tmpdb.DBManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class AsyncSilverApp extends AsyncTask<String, String, String> {
    private DBManager dbManager;
    Context context ;
    public String user, password, company;
    private Resultado[] listaResultado;
    public boolean connect = false;

    public AsyncSilverApp(){}
    public AsyncSilverApp(Context ctx){ context =ctx;}

    @Override
    protected String doInBackground(String... strings) {
        if (f_conectarBD()) {
            connect = true;
            return "ok";
        } else {
            connect = false;
            return "err";
        }
    }
    @Override
    protected void onPostExecute(String s) {
        if (s.equals("ok")) {
            Boolean vres = Boolean.parseBoolean(listaResultado[0].getProperty(1).toString());
            String mensaje = listaResultado[0].getProperty(0).toString();
            if (vres.equals(false)) {
                Toast.makeText(context, "NO SE CONECTO: " + mensaje, Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Conectando...: " + mensaje, Toast.LENGTH_SHORT).show();

        } else Toast.makeText(context, "Error. No se pudo conectar.", Toast.LENGTH_SHORT).show();
    }

    //conectar a la base de datos
    public Boolean f_conectarBD() {
        //
        Boolean Bandera = true;
        try {
            try {
                dbManager = new DBManager(context);
                dbManager.open();
                Cursor cRetriveUser = dbManager.retriveUser();
                if (cRetriveUser != null) {
                    user = (cRetriveUser.getString(1));
                    password = (cRetriveUser.getString(2));
                    company = (cRetriveUser.getString(3));
                }
                dbManager.close();

            } catch (Exception ex) {
                return false;
            }

            if (!user.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && !company.equalsIgnoreCase("")) {
                //Get the text control value

                SoapObject request = new SoapObject(configuracion.NAMESPACE, "ConexionBaseDatos2");
                configuracion g = configuracion.getInstance();
                request.addProperty("usuario", user);
                request.addProperty("clave", password);
                request.addProperty("empresa", company);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE transporte = new HttpTransportSE(configuracion.URL);
                try {
                    transporte.call(configuracion.NAMESPACE + "ConexionBaseDatos2", envelope);
                    SoapObject resSoap = (SoapObject) envelope.getResponse();
                    listaResultado = new Resultado[resSoap.getPropertyCount()];
                    for (int i = 0; i < listaResultado.length; i++) {
                        SoapObject ic = (SoapObject) resSoap.getProperty(i);
                        Resultado lp = new Resultado();
                        lp.mensaje = ic.getProperty("mensaje").toString();
                        lp.Estado = Boolean.parseBoolean(ic.getProperty("Estado").toString());
                        listaResultado[i] = lp;
                    }
                } catch (Exception e) {
                    //Print error
                    e.printStackTrace();
                    Bandera = false;
                }
            } else Bandera = false;
        } catch (RuntimeException re) {
            Bandera = false;
        }
        return Bandera;
    }
}

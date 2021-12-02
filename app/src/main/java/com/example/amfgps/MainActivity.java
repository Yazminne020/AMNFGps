package com.example.amfgps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amfgps.tmpdb.DBManager;
import com.example.amfgps.utilities.Network;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tv;
    EditText et, et2, et3;
    ProgressBar pg;
    String editTextU;
    String editTextC;
    String editTextE;
    Context context = this;
    private DBManager dbManager;
    private Resultado[] listaResultado;
    private ProgressDialog dialogo;
    public String TAG_ACTIVITY = "Inicio";
    private int numeroIntentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TITULO DE ACTIVIDAD
        setTitle("Inicio de Sesión");
        //Name Text control
        et = findViewById(R.id.txtUsuario);
        et2 = findViewById(R.id.txtClave);
        et3 = findViewById(R.id.txtEmpresa);
        et.clearFocus();
        et.setFocusable(true);
        //Display Text control
        tv = findViewById(R.id.txtResultado);
        //Button to trigger web service invocation
        btnLogin = findViewById(R.id.btnIngresar);
        //Display progress bar until web service invocation completes
        pg = findViewById(R.id.progressBar1);
        autoLogin();
        //Button Click Listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().toString().equalsIgnoreCase("")) {
                    et.setHint("Por favor, ingrese el usuario");
                    et.setError("Campo requerido");
                    et.setFocusable(true);
                } else if (et2.getText().toString().equalsIgnoreCase("")) {
                    et2.setHint("Por favor, ingrese la contraseña");
                    et2.setError("Campo requerido");
                    et2.setFocusable(true);
                } else if (et3.getText().toString().equalsIgnoreCase("")) {
                    et3.setHint("Por favor, ingrese la empresa");
                    et3.setError("Campo requerido");
                    et3.setFocusable(true);
                } else {
                    if (Network.compruebaConexion(MainActivity.this.getApplicationContext())) {
                        new asynConectarBD().execute();
                    } else
                        Toast.makeText(MainActivity.this.getBaseContext(), "Sin conexión a internet ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // En el caso de que el usuario registre sus datos se guarda en sqlite para la recuperacion
    private void autoLogin() {
        try {
            dbManager = new DBManager(context);
            dbManager.open();
            Cursor cRetriveUser = dbManager.retriveUser();
            if (cRetriveUser != null) {
                et.setText(cRetriveUser.getString(1));
                et2.setText(cRetriveUser.getString(2));
                et3.setText(cRetriveUser.getString(3));
                if (Network.compruebaConexion(this)) {
                    new asynConectarBD().execute();
                } else
                    Toast.makeText(getBaseContext(), "Sin conexión a internet ", Toast.LENGTH_SHORT).show();
            }
            dbManager.close();

        } catch (Exception ignored) {

        }

    }
    //conectar a la base de datos
    @SuppressLint("SetTextI18n")
    public Boolean f_conectarBD() {
        //
        boolean Bandera = true;
        try {

            if (et.getText().length() != 0 && !et.getText().toString().equals("")) {
                //Get the text control value
                editTextU = et.getText().toString();
                editTextC = et2.getText().toString();
                editTextE = et3.getText().toString();

                SoapObject request = new SoapObject(configuracion.NAMESPACE, "ConexionBaseDatos2");
                request.addProperty("usuario", editTextU);
                request.addProperty("clave", editTextC);
                request.addProperty("empresa", editTextE);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
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
            } else {
                tv.setText("Ingrese Usuario");
                Bandera = false;
            }
        } catch (RuntimeException re) {
            Bandera = false;
        }
        return Bandera;
    }
    private void deleteUser() {
        dbManager = new DBManager(context);
        dbManager.open();
        dbManager.deleteAll();
        dbManager.close();
    }
    @SuppressLint("StaticFieldLeak")
    private class asynConectarBD extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            dialogo = new ProgressDialog(MainActivity.this);
            dialogo.setMessage("Cargado datos...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (f_conectarBD()) {
                return "ok";
            } else {
                return "err";
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            dialogo.dismiss();
            if (s.equals("ok")) {
                Log.i(TAG_ACTIVITY, "--- OK  " + numeroIntentos);
                Boolean vres = Boolean.parseBoolean(listaResultado[0].getProperty(1).toString());
                String mensaje = listaResultado[0].getProperty(0).toString();
                if (vres.equals(true)) {
                    Intent irbienvenido = new Intent(MainActivity.this, Bienvenido.class);
                    irbienvenido.putExtra("usuarioc", editTextU);
                    irbienvenido.putExtra("clavec", editTextC);
                    irbienvenido.putExtra("empresac", editTextE.toLowerCase());
                    //Borrar db - client
                    deleteUser();
                    //Guardar en la base de datos
                    dbManager = new DBManager(context);
                    dbManager.open();
                    dbManager.insert(editTextU, editTextC, editTextE.toLowerCase());
                    dbManager.close();
                    irbienvenido.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(irbienvenido);
                } else {
                    Toast.makeText(MainActivity.this, "NO SE CONECTO: " + mensaje, Toast.LENGTH_SHORT).show();
                    tv.setText("NO SE CONECTO. " + mensaje);
                }
           } else {
                numeroIntentos++;
                Log.i(TAG_ACTIVITY,"--- ERR  "+ numeroIntentos);
                if (numeroIntentos <= 1) {
                    try {
                        new AsyncSilverApp(getBaseContext()).execute();
                        new asynConectarBD().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Ha ocurrido un error de conexión. \nConsulte con el administrador...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    numeroIntentos = 0;
                    Toast.makeText(MainActivity.this, "Error. No se pudo conectar.", Toast.LENGTH_SHORT).show();
                    tv.setText("Error. No se pudo conectar.");
                }
            }
        }
    }
}
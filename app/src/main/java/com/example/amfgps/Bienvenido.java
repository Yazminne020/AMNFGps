package com.example.amfgps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.amfgps.utilities.Network;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Bienvenido extends AppCompatActivity implements CitaAdapter.customButtonListener {

    TextView tvFecha, tvB, tvHora;
    Button btnUbicacion;
    private String _usuario, Usuarioc, empresac, Clave;
    private ProgressDialog dialogo;
    SwipeRefreshLayout swipeRefreshLayout;
    private int numeroIntentos;
    DatePickerDialog.OnDateSetListener setListener;
    TimePickerDialog.OnTimeSetListener setListenerT;
    public String TAG_ACTIVITY = "Bienvenido";
    private String Oficina, Empresa;
    private Cliente[] listaClientes;
    private Cita[] listaCitas, auxiliar,citas,auxiliar1;
    ListView listViewPedido;
    CitaAdapter adapterp1;
    ArrayList<Cita> names = new ArrayList<Cita>();
    ArrayList<Cita> names1 = new ArrayList<Cita>();
    String val = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);


        tvFecha = findViewById(R.id.etFechaDesdeDialog);
//        tvHora = findViewById(R.id.etHora);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        tvB = findViewById(R.id.tvUsuario);
        listViewPedido = findViewById(R.id.lvCitas);
        SwipeRefreshLayout swipeRefreshLayout;

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");// /dd/MM/yyyy HH:mm:ss"/
        tvFecha.setText(sdf.format(c.getTime()));
        tvFecha.setInputType(InputType.TYPE_NULL);

        tomarFecha(year, month, day);
        tomarUbicacion();


        tvB = findViewById(R.id.tvUsuario);
        //nombre de empresa
        Empresa = "";
        Oficina = "";
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String dato = extras.getString("usuarioc");
            String datoc = extras.getString("clavec");
            String d2 = extras.getString("empresac");
            Usuarioc = dato;
            _usuario = dato;
            empresac = d2;
            Clave = datoc;
            configuracion g = configuracion.getInstance();
            g.setUsuario(dato);
            g.setClave(datoc);
            g.setEmpresa(d2);
            if (Network.compruebaConexion(getApplicationContext())) {
                new asyntodos().execute();
                new asyncitaClientes().execute();
            }
            else
                Toast.makeText(getApplicationContext(), "Sin acceso a Internet", Toast.LENGTH_LONG).show();
        }


        ImageButton button = (ImageButton) findViewById(R.id.btnFechaDesdeDialog);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent irbienvenido = new Intent(Bienvenido.this, MapsActivity.class);
                startActivity(irbienvenido);
            }
        });


       //citas = listaCitaClientes();



//        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");// /dd/MM/yyyy HH:mm:ss"/
//        tvHora.setText(sdf1.format(c.getTime()));
//        tvHora.setInputType(InputType.TYPE_NULL);
//
//        tomarHora(hour, min);


    }


    private void tomarUbicacion() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) Bienvenido.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        val = location.getLatitude() + "  " + location.getLongitude();
                        auxiliar1 = controlDistancia(val, citas);
                        if (auxiliar != null) {
                            for (int i = 0; i < auxiliar.length; i++) {

                                names.add(auxiliar[i]);
                            }
                            adapterp1 = new CitaAdapter(Bienvenido.this, R.layout.lista_cita_cliente, names);
                            listViewPedido.setAdapter(adapterp1);

                        }

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                };
                int permissionCheck = ContextCompat.checkSelfPermission(Bienvenido.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                if (auxiliar1 != null) {
                    for (int i = 0; i < auxiliar1.length; i++) {

                        names1.add(auxiliar1[i]);
                    }
                    adapterp1 = new CitaAdapter(Bienvenido.this, R.layout.lista_cita_cliente, names1);
                    listViewPedido.setAdapter(adapterp1);

                }
            }
        });

    }

    private void tomarFecha(int year, int month, int day) {
        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Bienvenido.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); //Bloquear tiempo pasado
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime() + (1000L * 60 * 60 * 24 * 31)); //Bloquear tiempo futuro
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                tvFecha.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

    }

    private void tomarHora(int hour, int min) {
        tvHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Bienvenido.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListenerT, hour, min, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });
        setListenerT = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                tvHora.setText(i + ":" + i1);
            }
        };
    }

    @Override
    public void onButtonClickListner(int position, Cita value, String boton) {

    }

    private class asynCliente extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            /*dialogo=new ProgressDialog(CrearPedidoActivity.this);
            dialogo.setMessage("Cargado datos.......");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            if (llamarCliente()) {
                return "ok";
            } else {
                return "err";
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            dialogo.dismiss();
            try {
                swipeRefreshLayout.setRefreshing(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (s.equals("ok")) {
                //cargar datos
                if (listaClientes.length > 0) {
                    configuracion g = configuracion.getInstance();
                    g.setCliente(listaClientes[0]);
                    Log.i(TAG_ACTIVITY, "--- OK  " + numeroIntentos);
                    String nombreCliente = listaClientes[0].getProperty(1).toString();
                    Oficina = listaClientes[0].getProperty(6).toString();
                    tvB.setText("\n Bienvenido: " + nombreCliente);
                    Toast.makeText(Bienvenido.this, " Bienvenido: " + nombreCliente + " \nOficina: " + Oficina, Toast.LENGTH_SHORT).show();

                } else {
                    new AsyncSilverApp(getBaseContext()).execute();
                    new asynCliente().execute();
                }
            } else {
                numeroIntentos++;
                Log.i(TAG_ACTIVITY, "--- ERR  " + numeroIntentos);
                if (numeroIntentos <= 1) {
                    try {
                        new AsyncSilverApp(getBaseContext()).execute();
                        new asynCliente().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Bienvenido.this, "Ha ocurrido un error de conexión. \nConsulte con el administrador...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    numeroIntentos = 0;
                    Toast.makeText(Bienvenido.this, "Ocurrió un error. Intente nuevamente...", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private class asyntodos extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            dialogo = new ProgressDialog(Bienvenido.this);
            dialogo.setMessage("Cargado datos...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (llamarWS())
                return "ok";
            else
                return "err";
        }

        @Override
        protected void onPostExecute(String s) {
            dialogo.dismiss();
            if (s.equals("ok")) {

                //cargar datos
                Log.i(TAG_ACTIVITY, "--- OK  " + numeroIntentos);
                if (Network.compruebaConexion(getApplicationContext()))
                    new asynCliente().execute();
                else
                    Toast.makeText(getApplicationContext(), "Sin acceso a Internet", Toast.LENGTH_LONG).show();
                //tvB.setText("Bienvenido: "+Usuarioc+" / "+Empresa);
                //Toast.makeText(Bienvenido.this, " Empresa: " + Empresa, Toast.LENGTH_SHORT).show();
            } else {
                //new Bienvenido();
                Toast.makeText(Bienvenido.this, "Tiempo de inactividad superado.\nReconectando ...", Toast.LENGTH_SHORT).show();
                numeroIntentos++;
                Log.i(TAG_ACTIVITY, "--- ERR  " + numeroIntentos);
                if (numeroIntentos <= 1) {
                    try {
                        new AsyncSilverApp(getBaseContext()).execute();
                        new asyntodos().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Bienvenido.this, "Ha ocurrido un error de conexión. \nConsulte con el administrador...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    numeroIntentos = 0;
                    Toast.makeText(Bienvenido.this, "Ocurrió un error. Intente nuevamente...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class asyncitaClientes extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
//            dialogo = new ProgressDialog(Bienvenido.this);
//            dialogo.setMessage("Cargado datos...");
//            dialogo.setIndeterminate(false);
//            dialogo.setCancelable(false);
//            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (listaCitaClientes())
                return "ok";
            else
                return "err";
        }

        @Override
        protected void onPostExecute(String s) {
            dialogo.dismiss();
            if (s.equals("ok")) {
                citas =listaCitas;
                LocationManager locationManager = (LocationManager) Bienvenido.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    @Override

                    public void onLocationChanged(@NonNull Location location) {
                        val = location.getLatitude() + "  " + location.getLongitude();
                        auxiliar = controlDistancia(val, citas);
                        names=new ArrayList<>();
                        if (auxiliar != null) {
                            for (int i = 0; i < auxiliar.length; i++) {

                                names.add(auxiliar[i]);
                            }
                            adapterp1 = new CitaAdapter(Bienvenido.this, R.layout.lista_cita_cliente, names);
                            listViewPedido.setAdapter(adapterp1);

                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                };

                ContextCompat.checkSelfPermission(Bienvenido.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                //cargar datos

            } else {
                //new Bienvenido();
                Toast.makeText(Bienvenido.this, "Tiempo de inactividad superado.\nReconectando ...", Toast.LENGTH_SHORT).show();
                numeroIntentos++;
                Log.i(TAG_ACTIVITY, "--- ERR  " + numeroIntentos);
                if (numeroIntentos <= 1) {
                    try {
                        new AsyncSilverApp(getBaseContext()).execute();
                        new asyntodos().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Bienvenido.this, "Ha ocurrido un error de conexión. \nConsulte con el administrador...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    numeroIntentos = 0;
                    Toast.makeText(Bienvenido.this, "Ocurrió un error. Intente nuevamente...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //LLAMA A DATOS DE CLIENTE
    public Boolean llamarCliente() {
        //
        boolean Bandera = true;
        SoapObject request = new SoapObject(configuracion.NAMESPACE, "ListadoClientes");
        request.addProperty("cedula", _usuario);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(configuracion.URL);
        //transporte.debug = true;
        try {
            transporte.call(configuracion.NAMESPACE + "ListadoClientes", envelope);
            // Get the response
            SoapObject resSoap = (SoapObject) envelope.getResponse();
            listaClientes = new Cliente[resSoap.getPropertyCount()];
            for (int i = 0; i < listaClientes.length; i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);

                Cliente cli = new Cliente();
                cli.id = Integer.parseInt(ic.getProperty("IdCliente").toString());
                cli.nombre = ic.getProperty("Nombrecompleto").toString();
                cli.dias = Integer.parseInt(ic.getProperty("Diascredito").toString());
                cli.saldo = Double.parseDouble(ic.getProperty("Saldo").toString());
                cli.monto = Double.parseDouble(ic.getProperty("Montocredito").toString());
                cli.cedula = (ic.getProperty("Cedula").toString());
                cli.oficina = (ic.getProperty("OficinaCl").toString());
                listaClientes[i] = cli;
            }
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return Bandera;
    }

    public Cita[] controlDistancia(String origen, Cita[] citaClientes) {  //latitude, logitude
        String[] valores = new String[citaClientes.length];
        float[] val = new float[valores.length];
        float[] list = new float[valores.length];
        Location locationA = new Location("punto A");
        Location locationB = new Location("punto B");
        String latA = origen.split(" ")[0];
        String lngA = origen.split(" ")[2];

        locationA.setLatitude(Double.parseDouble(latA));
        locationA.setLongitude(Double.parseDouble(lngA));

        for (int i = 0; i < citaClientes.length; i++) {
            locationB.setLatitude(Double.parseDouble(citaClientes[i].latitud));
            locationB.setLongitude(Double.parseDouble(citaClientes[i].longitud));
            float distance = locationA.distanceTo(locationB);
            valores[i] = (citaClientes[i].clte_id + "-" + distance);
        }
        for (int x = 0; x < valores.length; x++) {
            val[x] = Float.parseFloat(valores[x].split("-")[1]);
        }
        float[] val1 = Arrays.copyOf(val, val.length);
        list = optimizedBubbleSort(val);
        String[] orden = orden(val1, list, valores);
        Cita[] cita = ordenClientes(orden, citaClientes);

        return cita;
        //System.out.println(val);
    }

    private String[] orden(float[] val, float[] list, String[] valores) {
        String[] aux = new String[val.length];
        for (int i = 0; i < list.length; i++) {
            for (int x = 0; x < val.length; x++) {
                if (list[i] == val[x]) {
                    aux[i] = valores[x];
                }
            }
        }
        return aux;
    }

    private Cita[] ordenClientes(String[] orden, Cita[] citaClientes) {
        Cita[] aux = new Cita[orden.length];
        for (int i = 0; i < orden.length; i++) {
            for (int x = 0; x < citaClientes.length; x++) {
                String a = orden[i].split("-")[0];
                if (a.equals(citaClientes[x].clte_id)) {
                    aux[i] = citaClientes[x];
                    break;
                }
            }
        }
        return aux;
    }

    private float[] optimizedBubbleSort(float[] list) {
        float aux;

        for (int i = 0; i < list.length - 1; i++) {
            for (int x = i + 1; x < list.length; x++) {
                if (list[x] < list[i]) {
                    aux = list[i];
                    list[i] = list[x];
                    list[x] = aux;
                }
            }
        }
        return list;
    }

    public Boolean llamarWS() {
        //

        boolean Bandera = true;
        SoapObject request = new SoapObject(configuracion.NAMESPACE, configuracion.METHOD_NAME);
        request.addProperty("usuario", Usuarioc);
        request.addProperty("clave", Clave);
        request.addProperty("empresa", empresac);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(configuracion.URL);
        //transporte.debug = true;
        try {
            transporte.call(configuracion.NAMESPACE + "f_NombreEmpresa", envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Empresa = response.toString();
            //Usuarioc=g.getDataUsuario();
            //_usuario=g.getDataUsuario();
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return Bandera;
    }

    public Boolean listaCitaClientes() {
        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        boolean Bandera = true;
        String dato = "";
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            dato = extras.getString("usuarioc");
        }
        SoapObject request = new SoapObject(configuracion.NAMESPACE, "ClientesGPS");
//        request.addProperty("fecha", tvFecha.getText().toString());
        request.addProperty("fecha", "20/12/2021");
        request.addProperty("ceduruc", dato);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        new MarshalBase64().register(envelope); //serialization
        envelope.encodingStyle = SoapEnvelope.ENC;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(configuracion.URL);
        //transporte.debug = true;
        try {
            transporte.call(configuracion.NAMESPACE + "ClientesGPS", envelope);
            // Get the response
            SoapObject resSoap = (SoapObject) envelope.getResponse();
            listaCitas = new Cita[resSoap.getPropertyCount()];
            for (int i = 0; i < listaCitas.length; i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);

                Cita cita = new Cita();
                cita.diavisita = ic.getProperty("diavisita").toString();
                cita.observacion = ic.getProperty("observacion").toString();
                cita.vndr_codigo = ic.getProperty("vndr_codigo").toString();
                cita.clte_id = ic.getProperty("clte_id").toString();
                cita.longitud = ic.getProperty("longitud").toString();
                cita.latitud = ic.getProperty("latitud").toString();
                cita.cedulaVen = ic.getProperty("cedulaVen").toString();
                cita.nombreCliente = ic.getProperty("nombreCliente").toString();
                cita.direccion = ic.getProperty("direccion").toString();
                listaCitas[i] = cita;
            }
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return Bandera;
    }
}
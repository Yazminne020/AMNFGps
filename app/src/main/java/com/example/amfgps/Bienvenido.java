package com.example.amfgps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.amfgps.utilities.Network;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

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
import java.util.Date;

public class Bienvenido extends AppCompatActivity implements CitaAdapter.customButtonListener {

    TextView tvB, tvHora, tvlocalizacion;
    EditText tvFecha;
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
    private Cita[] listaCitas, auxiliar, citas, auxiliar1;
    ListView listViewPedido;
    //CitaAdapter adapterp1;
    ArrayList<Cita> names = new ArrayList<Cita>();
    ArrayList<Cita> names1 = new ArrayList<Cita>();
    String val = "";
    double latitud1, longitude1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);


        tvFecha = findViewById(R.id.etFecha);
        tvlocalizacion = findViewById(R.id.tvLocalizacion);
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
        //tomarUbicacion();
        //Permiso GPS
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

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
            } else
                Toast.makeText(getApplicationContext(), "Sin acceso a Internet", Toast.LENGTH_LONG).show();
        }


        ImageButton button = (ImageButton) findViewById(R.id.btnFechaDesdeDialog);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ubicacionGPS();
            }
        });


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Bienvenido.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            getCoordenada();

        }


        tvlocalizacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //System.out.println(s.toString() + " " + start + " " + count + " " + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ubicacionGPS();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // System.out.println(s.toString());
            }
        });
        btnUbicacion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getCoordenada();
            }
        });
    }

    private void ubicacionGPS() {
        try {
            names.clear();
            CitaAdapter adapterp1 = new CitaAdapter(Bienvenido.this, R.layout.lista_cita_cliente, names);
            if (tvlocalizacion.getText().toString().isEmpty() || tvFecha.getText().toString().isEmpty()) {

            } else {
                citas = listaCitaClientes();
                val = tvlocalizacion.getText().toString();
                auxiliar = controlDistancia(val, citas);
                if (auxiliar != null) {
                    for (int i = 0; i < auxiliar.length; i++) {

                        names.add(auxiliar[i]);
                    }
                    adapterp1 = new CitaAdapter(Bienvenido.this, R.layout.lista_cita_cliente, names);
                    listViewPedido.setAdapter(adapterp1);
                    adapterp1.setCustomButtonListner(Bienvenido.this);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ubicacionGPS();
        Toast.makeText(Bienvenido.this, "Lista Actualizada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCoordenada();
            } else {
                Toast.makeText(this, "Permiso Denegado ..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCoordenada() {

        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(Bienvenido.this).removeLocationUpdates(this);
                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                        latitud1 = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        longitude1 = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                        tvlocalizacion.setText(latitud1 + " " + longitude1);
                    }

                }

            }, Looper.myLooper());

        } catch (Exception ex) {
            System.out.println("Error es :" + ex);
        }
    }

    public void ObtenerCoordendasActual(View view) {


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Bienvenido.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            getCoordenada();
        }
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
    public void onButtonClickListner(String position, Cita value, String boton) {
        //int Position=position;
        if (boton == "CONTINUAR") {
            Intent informacionCli = new Intent(Bienvenido.this, InformacionCliente.class);
            informacionCli.putExtra("rtvi", value.rtvi);
            informacionCli.putExtra("diavisita", value.diavisita);
            informacionCli.putExtra("observacion", value.observacion);
            informacionCli.putExtra("vndr_codigo", value.vndr_codigo);
            informacionCli.putExtra("clte_id", value.clte_id);
            informacionCli.putExtra("longitud", value.longitud);
            informacionCli.putExtra("latitud", value.latitud);
            informacionCli.putExtra("longLat", value.longLat);
            informacionCli.putExtra("cedulaVen", value.cedulaVen);
            informacionCli.putExtra("nombreCliente", value.nombreCliente);
            informacionCli.putExtra("direccion", value.direccion);
            informacionCli.putExtra("telefono1", value.telefono1);
            informacionCli.putExtra("telefono2", value.telefono2);
            informacionCli.putExtra("estado", value.estado);


            startActivity(informacionCli);
        }

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
                    Toast.makeText(Bienvenido.this, " Bienvenido: " + nombreCliente, Toast.LENGTH_SHORT).show();
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
        String lngA = origen.split(" ")[1];

        locationA.setLatitude(Double.parseDouble(latA));
        locationA.setLongitude(Double.parseDouble(lngA));

        for (int i = 0; i < citaClientes.length; i++) {
            locationB.setLatitude(Double.parseDouble(citaClientes[i].latitud));
            locationB.setLongitude(Double.parseDouble(citaClientes[i].longitud));
            float distance = locationA.distanceTo(locationB);
            valores[i] = (citaClientes[i].rtvi + "-" + distance);
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
                    val[x] = 0;
                    break;
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
                if (a.equals(citaClientes[x].rtvi)) {
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

    public Cita[] listaCitaClientes() {
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
                cita.rtvi = ic.getProperty("rtvi").toString();
                cita.diavisita = ic.getProperty("diavisita").toString();
                cita.observacion = ic.getProperty("observacion").toString();
                cita.vndr_codigo = ic.getProperty("vndr_codigo").toString();
                cita.clte_id = ic.getProperty("clte_id").toString();
                cita.longitud = ic.getProperty("longitud").toString();
                cita.latitud = ic.getProperty("latitud").toString();
                cita.cedulaVen = ic.getProperty("cedulaVen").toString();
                cita.nombreCliente = ic.getProperty("nombreCliente").toString();
                cita.direccion = ic.getProperty("direccion").toString();
                cita.telefono1 = ic.getProperty("telefono1").toString();
                cita.telefono2 = ic.getProperty("telefono2").toString();
                cita.longLat = tvlocalizacion.getText().toString();
                cita.estado = ic.getProperty("estado").toString();
                listaCitas[i] = cita;
            }
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return listaCitas;
    }
}
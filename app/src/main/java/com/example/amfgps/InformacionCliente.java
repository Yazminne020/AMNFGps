package com.example.amfgps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Calendar;

public class InformacionCliente extends AppCompatActivity {

    TextView tvCliente, tvPhone, tvDireccion, tvFechHora,tvInicio,tvFin;
    EditText txtObservacion;
    Button btnMapa, btnInicio,btnFin,btnGuardar;
    RadioButton rbVisita,rbReagendar;
    int hora = 0, minuto = 0, segundo = 0;
    Thread iniReloj = null;
    Runnable r;
    boolean isUpdate = false;
    String sec, min, hor, curTime,rtviG="";
    private Resultado[] listaResultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_cliente);
        tvCliente = findViewById(R.id.tvCliente);
        tvPhone = findViewById(R.id.tvPhone);
        tvDireccion = findViewById(R.id.tvDireccion);
        btnMapa = findViewById(R.id.btnMapa);
        btnInicio = findViewById(R.id.btnInicio);
        tvFechHora=findViewById(R.id.tvFechHora);
        tvInicio=findViewById(R.id.tvInicio);
        tvFin =findViewById(R.id.tvFin);
        btnFin=findViewById(R.id.btnFin);
        rbVisita=findViewById(R.id.rbVisita);
        btnGuardar=findViewById(R.id.btnGuardar);
        rbReagendar=findViewById(R.id.rbReagendar);
        txtObservacion=findViewById(R.id.txtObservacion);

        rbVisita.setChecked(true);
        r = new RefreshClock();
        iniReloj = new Thread(r);
        iniReloj.start();

        Cita cita = new Cita();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        btnFin.setEnabled(false);
        if (extras != null) {
            cita.rtvi= extras.getString("rtvi");
            cita.diavisita = extras.getString("diavisita");
            cita.observacion = extras.getString("observacion");
            cita.vndr_codigo = extras.getString("vndr_codigo");
            cita.clte_id = extras.getString("clte_id");
            cita.longitud = extras.getString("longitud");
            cita.latitud = extras.getString("latitud");
            cita.longLat = extras.getString("longLat");
            cita.cedulaVen = extras.getString("cedulaVen");
            cita.nombreCliente = extras.getString("nombreCliente");
            cita.direccion = extras.getString("direccion");
            cita.telefono1 = extras.getString("telefono1");
            cita.telefono2 = extras.getString("telefono2");
            cita.estado = extras.getString("estado");

            rtviG=cita.rtvi;
            tvCliente.setText(cita.nombreCliente);
            tvPhone.setText(cita.telefono1 + " " + cita.telefono2);
            tvDireccion.setText(cita.direccion);
        }
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent informacionCli = new Intent(InformacionCliente.this, MapsActivity.class);
                informacionCli.putExtra("longLat", cita.longLat);
                informacionCli.putExtra("longitud", cita.longitud);
                informacionCli.putExtra("latitud", cita.latitud);
                startActivity(informacionCli);
            }
        });

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iniReloj.interrupt();
                tvInicio.setText(tvFechHora.getText());
                btnFin.setEnabled(true);
                btnInicio.setEnabled(false);
            }
        });
        btnFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iniReloj.interrupt();
                tvFin.setText(tvFechHora.getText());
                btnFin.setEnabled(false);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (rbVisita.isChecked()){
                        boolean resp= actualizarClientes(txtObservacion.getText().toString(),tvInicio.getText().toString(),tvFin.getText().toString(),"SI",rtviG);
                        if (resp){
                            Toast.makeText(InformacionCliente.this, "Actualizado con Éxito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(InformacionCliente.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if (rbReagendar.isChecked()){
                            mostrarDialogoBasico();
                           // boolean resp= guardarClientes("20/12/2021", "20/12/2021", "21/12/2021", "ob12", "EC", "9697", "MGK","RG");
                            // if (resp){
                            //    Toast.makeText(InformacionCliente.this, "Guardado con Éxito", Toast.LENGTH_SHORT).show();
                            //}else{
                            //    Toast.makeText(InformacionCliente.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                            //}
                        }
                    }
                }catch (Exception e){

                }
            }
        });
    }
    private void mostrarDialogoBasico(){
        AlertDialog.Builder builder = new AlertDialog.Builder(InformacionCliente.this);
        builder.setTitle("Reagendar");
        builder.setMessage("¿Desea reagendar una cita para este cliente?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"Si",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }
    private void initClock() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isUpdate) {
                        settingNewClock();

                    } else {
                        updateTime();
                    }
                    Calendar c = Calendar.getInstance();
                    String year =String.valueOf(c.get(Calendar.YEAR));
                    String month = String.valueOf(c.get(Calendar.MONTH));
                    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));

                    curTime =day+"/"+month+"/"+ year+" "+hor + hora + min + minuto + sec + segundo;
                    tvFechHora.setText(curTime);
                } catch (Exception e) {
                }
            }
        });
    }

    private void settingNewClock() {
        segundo += 1;
        setZeroClock();
        if (segundo >= 0 & segundo <= 59) {
        } else {
            segundo = 0;
            minuto += 1;
        }
        if (minuto >= 0 & minuto <= 59) {
        } else {
            minuto = 0;
            hora += 1;
        }
        if (hora >= 0 & hora <= 59) {
        } else {
            hora = 0;
        }
    }

    private void updateTime() {
        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);
        segundo = c.get(Calendar.SECOND);
        setZeroClock();
    }

    private void setZeroClock() {
        if (hora>=0 & hora<=9) {
            hor="0";
        } else {
            hor="";
        }
        if (minuto>=0 & minuto<=9) {
            min=":0";
        } else {
            min=":";
        }
        if (segundo>=0 & segundo<=9) {
            sec=":0";
        } else {
            sec=":";
        }

    }

    class RefreshClock implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    initClock();
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }

            }
        }
    }

    public Boolean guardarClientes(String fecha, String fechainicio,String fechafin, String observacion, String codVendedor, String idCliente,String empresa, String estado) {
        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        boolean Bandera = false;

        SoapObject request = new SoapObject(configuracion.NAMESPACE, "GuardarClientesGPS");
//        request.addProperty("fecha", tvFecha.getText().toString());
        request.addProperty("fecha", fecha);
        request.addProperty("fechainicio", fechainicio);
        request.addProperty("fechafin", fechafin);
        request.addProperty("observacion", observacion);
        request.addProperty("codVendedor", codVendedor);
        request.addProperty("idCliente", idCliente);
        request.addProperty("empresa", empresa);
        request.addProperty("estado", estado);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        //new MarshalBase64().register(envelope); //serialization
        //envelope.encodingStyle = SoapEnvelope.ENC;

        HttpTransportSE transporte = new HttpTransportSE(configuracion.URL);
        //transporte.debug = true;
        try {
            transporte.call(configuracion.NAMESPACE + "GuardarClientesGPS", envelope);
            Bandera =true;
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return Bandera;
    }
    public Boolean actualizarClientes(String observacion, String fechainicio,String fechafin,String estado, String rtvi) {
        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        boolean Bandera = false;

        SoapObject request = new SoapObject(configuracion.NAMESPACE, "ActualizarClientesGPS");
        request.addProperty("observacion", observacion);
        request.addProperty("fechainicio", fechainicio);
        request.addProperty("fechafin", fechafin);
        request.addProperty("estado", estado);
        request.addProperty("rtvi", rtvi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(configuracion.URL);
        try {
            transporte.call(configuracion.NAMESPACE + "ActualizarClientesGPS", envelope);
            Bandera =true;
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return Bandera;
    }
}
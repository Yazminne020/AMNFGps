package com.example.amfgps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InformacionCliente extends AppCompatActivity {

    TextView tvCliente, tvPhone, tvDireccion, tvFechHora, tvInicio, tvFin,tvInformacion,tvCitaid;
    EditText txtObservacion;
    Button btnMapa, btnInicio, btnFin, btnGuardar;
    RadioButton rbVisita, rbReagendar;
    int hora = 0, minuto = 0, segundo = 0;
    Thread iniReloj = null;
    Runnable r;
    boolean isUpdate = false;
    String sec, min, hor, curTime, rtviG = "";
    private Resultado[] listaResultado;
    Cita cita = new Cita();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_cliente);
        tvCliente = findViewById(R.id.tvCliente);
        tvPhone = findViewById(R.id.tvPhone);
        tvDireccion = findViewById(R.id.tvDireccion);
        btnMapa = findViewById(R.id.btnMapa);
        btnInicio = findViewById(R.id.btnInicio);
        tvFechHora = findViewById(R.id.tvFechHora);
        tvInicio = findViewById(R.id.tvInicio);
        tvFin = findViewById(R.id.tvFin);
        btnFin = findViewById(R.id.btnFin);
        rbVisita = findViewById(R.id.rbVisita);
        btnGuardar = findViewById(R.id.btnGuardar);
        rbReagendar = findViewById(R.id.rbReagendar);
        txtObservacion = findViewById(R.id.txtObservacion);
        tvInformacion=findViewById(R.id.tvInformacion);
        tvCitaid=findViewById(R.id.tvCitaid);

        rbVisita.setChecked(true);
        r = new RefreshClock();
        iniReloj = new Thread(r);
        iniReloj.start();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        btnFin.setEnabled(false);
        if (extras != null) {
            cita.rtvi = extras.getString("rtvi");
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

            rtviG = cita.rtvi;
            tvCliente.setText(cita.nombreCliente);
            tvPhone.setText(cita.telefono1 + " " + cita.telefono2);
            tvDireccion.setText(cita.direccion);
            tvCitaid.setText(cita.rtvi);
        }
        rbVisita.setEnabled(false);
        rbReagendar.setEnabled(false);
        bloquearCampos(true);
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
                rbVisita.setEnabled(true);
                rbReagendar.setEnabled(true);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (rbVisita.isChecked()) {
                        boolean resp = actualizarClientes(txtObservacion.getText().toString(), tvInicio.getText().toString(), tvFin.getText().toString(), "SI", rtviG);
                        if (resp) {
                            Toast.makeText(InformacionCliente.this, "Actualizado con Éxito " + cita.rtvi, Toast.LENGTH_SHORT).show();
                            bloquearCampos(false);

                        } else {
                            Toast.makeText(InformacionCliente.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(rbReagendar.isChecked()){
                            txtObservacion.setEnabled(true);
                            boolean resp = reagendarClientes(cita.rtvi, txtObservacion.getText().toString().split(":")[1].trim(), tvInformacion.getText().toString()+". "+txtObservacion.getText().toString(),cita.vndr_codigo , cita.clte_id);
                            if (resp) {
                                Toast.makeText(InformacionCliente.this, "Guardado con Éxito", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(InformacionCliente.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void bloquearCampos(boolean resp) {

        //txtObservacion.setEnabled(!resp);
        if (resp) {
            txtObservacion.setText("");
        }
        btnGuardar.setEnabled(resp);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbReagendar:
                if (checked) {
                    mostrarDialogoBasico();
                }
                break;
            case R.id.rbVisita:
                if (checked) {
                    txtObservacion.setText("");
                    txtObservacion.setEnabled(true);
                }
                break;
        }
    }

    private void mostrarDialogoBasico() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InformacionCliente.this);
        builder.setTitle("Reagendar");
        builder.setMessage("¿Desea reagendar una cita para este cliente?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent informacionCli = new Intent(InformacionCliente.this, Reagenda.class);
                        //informacionCli.putExtra("longLat", "cita.longLat");
                        startActivity(informacionCli);
                        txtObservacion.setText("");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date fecha = new Date();
                        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            fecha = formato.parse(tvFechHora.getText().toString().split(" ")[0]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String svalo = sumarRestarDiasFecha(fecha, 1);
                        tvInformacion.setText("Se reagendará automáticamente para el día: " + svalo);
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public String sumarRestarDiasFecha(Date fecha, int dias) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            fecha = (Date) formatter.parse(fecha.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + dias);

        return day + "/" + month + "/" + year; // Devuelve el objeto Date con los nuevos días añadidos

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
                    String year = String.valueOf(c.get(Calendar.YEAR));
                    String month = String.valueOf(c.get(Calendar.MONTH) + 1);
                    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));

                    curTime = day + "/" + month + "/" + year + " " + hor + hora + min + minuto + sec + segundo;
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
        if (hora >= 0 & hora <= 9) {
            hor = "0";
        } else {
            hor = "";
        }
        if (minuto >= 0 & minuto <= 9) {
            min = ":0";
        } else {
            min = ":";
        }
        if (segundo >= 0 & segundo <= 9) {
            sec = ":0";
        } else {
            sec = ":";
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

    public Boolean reagendarClientes(String rtvid, String fecha, String observacion,String codVendedor, String idCliente) {
        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        boolean Bandera = false;

        SoapObject request = new SoapObject(configuracion.NAMESPACE, "ReagendarClientesGPS");
//        request.addProperty("fecha", tvFecha.getText().toString());
        request.addProperty("rtvid", rtvid);
        request.addProperty("fecha", fecha);
        request.addProperty("observacion", observacion);
        request.addProperty("codVendedor", codVendedor);
        request.addProperty("idCliente", idCliente);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        //new MarshalBase64().register(envelope); //serialization
        //envelope.encodingStyle = SoapEnvelope.ENC;

        HttpTransportSE transporte = new HttpTransportSE(configuracion.URL);
        //transporte.debug = true;
        try {
            transporte.call(configuracion.NAMESPACE + "ReagendarClientesGPS", envelope);
            Bandera = true;
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return Bandera;
    }

    public Boolean actualizarClientes(String observacion, String fechainicio, String fechafin, String estado, String rtvi) {
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
            Bandera = true;
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            Bandera = false;
        }
        return Bandera;
    }
}
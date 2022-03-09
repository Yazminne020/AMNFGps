package com.example.amfgps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class InformacionCliente extends AppCompatActivity {

    TextView tvCliente, tvPhone, tvDireccion, tvMotivo, tvFechHora;
    Button btnMapa, btnInicio;
    int hora = 0, minuto = 0, segundo = 0;
    Thread iniReloj = null;
    Runnable r;
    boolean isUpdate = false;
    String sec, min, hor, curTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_cliente);
        tvCliente = findViewById(R.id.tvCliente);
        tvPhone = findViewById(R.id.tvPhone);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvMotivo = findViewById(R.id.tvMotivo);
        btnMapa = findViewById(R.id.btnMapa);
        btnInicio = findViewById(R.id.btnInicio);
        tvFechHora=findViewById(R.id.tvFechHora);

        r = new RefreshClock();
        iniReloj = new Thread(r);
        iniReloj.start();

        Cita cita = new Cita();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
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
            tvCliente.setText(cita.nombreCliente);
            tvPhone.setText(cita.telefono1 + " " + cita.telefono2);
            tvDireccion.setText(cita.direccion);
            tvMotivo.setText(cita.observacion);
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
                iniReloj.interrupt();
            }
        });


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

                    curTime =day+"/"+month+"/"+ year+"  "+hor + hora + min + minuto + sec + segundo;
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
}
package com.example.amfgps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InformacionCliente extends AppCompatActivity {

    TextView tvCliente, tvPhone, tvDireccion, tvMotivo;
    Button btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_cliente);
        tvCliente = findViewById(R.id.tvCliente);
        tvPhone = findViewById(R.id.tvPhone);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvMotivo = findViewById(R.id.tvMotivo);
        btnMapa = findViewById(R.id.btnMapa);
        Cita cita=new Cita();
;
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
            tvPhone.setText(cita.telefono1 + " " +cita.telefono2);
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
    }
}
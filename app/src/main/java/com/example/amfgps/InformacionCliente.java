package com.example.amfgps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amfgps.utilities.Network;

public class InformacionCliente extends AppCompatActivity {

    TextView tvCliente, tvPhone, tvDireccion, tvMotivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_cliente);
        tvCliente=findViewById(R.id.tvCliente);
        tvPhone=findViewById(R.id.tvPhone);
        tvDireccion=findViewById(R.id.tvDireccion);
        tvMotivo=findViewById(R.id.tvMotivo);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String diavisita = extras.getString("diavisita");
            String observacion = extras.getString("observacion");
            String vndr_codigo = extras.getString("vndr_codigo");
            String clte_id = extras.getString("clte_id");
            String longitud = extras.getString("longitud");
            String latitud = extras.getString("latitud");
            String cedulaVen = extras.getString("cedulaVen");
            String nombreCliente = extras.getString("nombreCliente");
            String direccion = extras.getString("direccion");
            String telefono1 = extras.getString("telefono1");
            String telefono2 = extras.getString("telefono2");
            tvCliente.setText(nombreCliente);
            tvPhone.setText(telefono1 + " "+ telefono2);
            tvDireccion.setText(direccion);
            tvMotivo.setText(observacion);
        }

    }
}
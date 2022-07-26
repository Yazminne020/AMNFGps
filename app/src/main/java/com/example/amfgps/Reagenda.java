package com.example.amfgps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Reagenda extends AppCompatActivity {

    TextView txtCita, txtNombre, txtDireccion, txtTelefono;
    Cita cita = new Cita();
    Button btnSave;
    String  finicio, ffin,bander, usuario, empresa;
    EditText etFecha,txtObs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reagenda);
        txtCita = findViewById(R.id.txtCita);
        txtNombre = findViewById(R.id.txtNombre);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtObs = findViewById(R.id.txtObs);
        btnSave = findViewById(R.id.btnSave);
        etFecha = findViewById(R.id.etFecha);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            cita.rtvi = extras.getString("rtvi");
            cita.vndr_codigo = extras.getString("vndr_codigo");
            cita.clte_id = extras.getString("clte_id");
            cita.nombreCliente = extras.getString("nombreCliente");
            cita.direccion = extras.getString("direccion");
            cita.telefono1 = extras.getString("telefono1");
            cita.telefono2 = extras.getString("telefono2");
            cita.estado = extras.getString("estado");
            bander = extras.getString("bander");
            finicio = extras.getString("finicio");
            ffin = extras.getString("ffin");
            usuario=extras.getString("usuario");
            empresa=extras.getString("empresa");

            txtNombre.setText(cita.nombreCliente);
            txtTelefono.setText(cita.telefono1 + " " + cita.telefono2);
            txtDireccion.setText(cita.direccion);
            txtCita.setText(cita.rtvi);
        }
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (bander.equals("true")){
                        guardarDatos(finicio,ffin);
                    }else {
                        guardarDatos("","");
                    }
                } catch (Exception e) {
                }
            }
        });
    }
    public void guardarDatos(String fechaInicio,String fechaFin) {
        InformacionCliente iCliente=new InformacionCliente();
            boolean resp = iCliente.reagendarClientes(cita.rtvi, etFecha.getText().toString().trim(), txtObs.getText().toString(), cita.vndr_codigo, cita.clte_id,fechaInicio,fechaFin);
            if (resp) {
                Toast.makeText(Reagenda.this, "Guardado con Éxito", Toast.LENGTH_SHORT).show();
                Intent informacionCli = new Intent(Reagenda.this, Bienvenido.class);
                informacionCli.putExtra("usuarioc", usuario);
                informacionCli.putExtra("empresac", empresa);
                startActivity(informacionCli);
            } else {
                Toast.makeText(Reagenda.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        btnSave.setEnabled(false);
    }
}
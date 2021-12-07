package com.example.amfgps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Bienvenido extends AppCompatActivity {

    TextView tvFecha;
    Button btnUbicacion;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);
        tvFecha = findViewById(R.id.etFechaDesdeDialog);
        btnUbicacion=findViewById(R.id.btnUbicacion);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");// /dd/MM/yyyy HH:mm:ss"/
        tvFecha.setText(sdf.format(c.getTime()));
        tvFecha.setInputType(InputType.TYPE_NULL);

        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(Bienvenido.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                tvFecha.setText(day+"/"+month+"/"+year);
            }
        };
        int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck== PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }

        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager=(LocationManager)Bienvenido.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener=new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        String val= location.getLatitude()+"  "+ location.getLongitude();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                };
                int permissionCheck= ContextCompat.checkSelfPermission(Bienvenido.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        });
    }


}
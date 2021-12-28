package com.example.amfgps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CitaAdapter extends ArrayAdapter<Cita> {
    public static final String TAG = "CitaAdapter";
    private Context mcontext;
    int mResource;
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, String boton);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public CitaAdapter(@NonNull Context context, int resource, @NonNull List<Cita> objects) {
        super(context, resource, objects);
        this.mcontext = context;
        this.mResource = resource;
    }
    @NonNull
    @Override
    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        String diavisita = getItem(position).diavisita;
        String observacion = getItem(position).observacion;
        String vndr_codigo = getItem(position).vndr_codigo;
        String clte_id = getItem(position).clte_id;
        String longitud = getItem(position).longitud;
        String latitud = getItem(position).latitud;
        String cedulaVen = getItem(position).cedulaVen;
        String nombreCliente = getItem(position).nombreCliente;
        String direccion = getItem(position).direccion;

        //ArticuloPedidos lp = new ArticuloPedidos(idPedido, nombreart, cantidad, punitario, total, cantpro, arti);
        LayoutInflater inlfater = LayoutInflater.from(mcontext);
        convertView = inlfater.inflate(mResource, parent, false);
        ImageButton btEliminari = (ImageButton) convertView.findViewById(R.id.btnVerLista);//eliminar item
        TextView tvNombreLista = (TextView) convertView.findViewById(R.id.tvNombreLista);
        TextView tvDireccionLista = (TextView) convertView.findViewById(R.id.tvDireccionLista);

        tvNombreLista.setText(nombreCliente);
        tvDireccionLista.setText(direccion);

        btEliminari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, clte_id.toString(), "CONTINUAR");
                }
            }
        });

        return convertView;
    }
}

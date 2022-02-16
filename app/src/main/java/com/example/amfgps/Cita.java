package com.example.amfgps;

import androidx.annotation.NonNull;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

import lombok.Data;

@Data
public class Cita implements KvmSerializable {

    public String diavisita, observacion, vndr_codigo, clte_id, longitud, latitud, cedulaVen, nombreCliente, direccion, telefono1, telefono2,longLat;

    public Cita() {

    }

    //    @NonNull
//    @Override
//    public String toString() {
//        if (!clte_id.isEmpty()) {
//            return " " + clte_id + "-" + nombreCliente
//                    + "\n" + direccion;
//        }
//        return "-";
//    }
    public Cita(String Diavisita, String Observacion, String Vndr_codigo, String Clte_id, String Longitud,
                String Latitud, String CedulaVen, String NombreCliente, String Direccion, String Telefono1, String Telefono2,String LongLat) {
        this.diavisita = Diavisita;
        this.observacion = Observacion;
        this.vndr_codigo = Vndr_codigo;
        this.clte_id = Clte_id;
        this.longitud = Longitud;
        this.latitud = Latitud;
        this.cedulaVen = CedulaVen;
        this.nombreCliente = NombreCliente;
        this.direccion = Direccion;
        this.telefono1 = Telefono1;
        this.telefono2 = Telefono2;
        this.longLat=LongLat;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return diavisita;
            case 1:
                return observacion;
            case 2:
                return vndr_codigo;
            case 3:
                return clte_id;
            case 4:
                return longitud;
            case 5:
                return latitud;
            case 6:
                return cedulaVen;
            case 7:
                return nombreCliente;
            case 8:
                return direccion;
            case 9:
                return telefono1;
            case 10:
                return telefono2;
            case 11:
                return longLat;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 12;
    }

    @Override
    public void setProperty(int i, Object val) {
        switch (i) {
            case 0:
                diavisita = val.toString();
                break;
            case 1:
                observacion = val.toString();
                break;
            case 2:
                vndr_codigo = val.toString();
                break;
            case 3:
                clte_id = val.toString();
                break;
            case 4:
                longitud = val.toString();
                break;
            case 5:
                latitud = val.toString();
                break;
            case 6:
                cedulaVen = val.toString();
                break;
            case 7:
                nombreCliente = val.toString();
                break;
            case 8:
                direccion = val.toString();
                break;
            case 9:
                telefono1 = val.toString();
                break;
            case 10:
                telefono2 = val.toString();
                break;
            case 11:
                longLat = val.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable ht, PropertyInfo info) {
        switch (i) {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "diavisita";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "observacion";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "vndr_codigo";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "clte_id";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "longitud";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "latitud";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "cedulaVen";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "nombreCliente";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "direccion";
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "telefono1";
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "telefono2";
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "longLat";
                break;
            default:
                break;
        }
    }
}

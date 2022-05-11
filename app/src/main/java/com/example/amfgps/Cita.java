package com.example.amfgps;

import androidx.annotation.NonNull;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

import lombok.Data;

@Data
public class Cita implements KvmSerializable {

    public String rtvi,diavisita, observacion, vndr_codigo, clte_id, longitud, latitud, cedulaVen, nombreCliente, direccion, telefono1, telefono2,longLat,estado;

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
    public Cita(String Rtvi, String Diavisita, String Observacion, String Vndr_codigo, String Clte_id, String Longitud,
                String Latitud, String CedulaVen, String NombreCliente, String Direccion, String Telefono1, String Telefono2,String LongLat,String Estado) {
        this.rtvi = Rtvi;
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
        this.estado=Estado;

    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 1:
                return rtvi;
            case 2:
                return diavisita;
            case 3:
                return observacion;
            case 4:
                return vndr_codigo;
            case 5:
                return clte_id;
            case 6:
                return longitud;
            case 7:
                return latitud;
            case 8:
                return cedulaVen;
            case 9:
                return nombreCliente;
            case 10:
                return direccion;
            case 11:
                return telefono1;
            case 12:
                return telefono2;
            case 13:
                return longLat;
            case 14:
                return estado;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 14;
    }

    @Override
    public void setProperty(int i, Object val) {
        switch (i) {
            case 0:
                rtvi = val.toString();
                break;
            case 1:
                diavisita = val.toString();
                break;
            case 2:
                observacion = val.toString();
                break;
            case 3:
                vndr_codigo = val.toString();
                break;
            case 4:
                clte_id = val.toString();
                break;
            case 5:
                longitud = val.toString();
                break;
            case 6:
                latitud = val.toString();
                break;
            case 7:
                cedulaVen = val.toString();
                break;
            case 8:
                nombreCliente = val.toString();
                break;
            case 9:
                direccion = val.toString();
                break;
            case 10:
                telefono1 = val.toString();
                break;
            case 11:
                telefono2 = val.toString();
                break;
            case 12:
                longLat = val.toString();
                break;
            case 13:
                estado = val.toString();
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
                info.name = "rtvi";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "diavisita";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "observacion";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "vndr_codigo";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "clte_id";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "longitud";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "latitud";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "cedulaVen";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "nombreCliente";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "direccion";
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "telefono1";
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "telefono2";
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "longLat";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "estado";
                break;
            default:
                break;
        }
    }
}

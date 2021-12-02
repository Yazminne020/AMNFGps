package com.example.amfgps;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class Cliente implements KvmSerializable {

    public int id;
    public String nombre, cedula, oficina;
    public int dias;
    public Double monto, saldo;

    public Cliente() {
        id = 0;
        nombre = "";
        dias = 0;
        cedula = "";
        oficina = "";
    }

    public Cliente(int id, String nombre, int dias, Double monto, Double saldo, String cedula, String oficina) {
        this.id = id;
        this.nombre = nombre;
        this.dias = dias;
        this.monto = monto;
        this.saldo = saldo;
        this.cedula = cedula;
        this.oficina = oficina;
    }
    @Override
    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return id;
            case 1:
                return nombre;
            case 2:
                return dias;
            case 3:
                return monto;
            case 4:
                return saldo;
            case 5:
                return cedula;
            case 6:
                return oficina;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 7;
    }

    @Override
    public void setProperty(int ind, Object val) {
        switch (ind) {
            case 0:
                id = Integer.parseInt(val.toString());
                break;
            case 1:
                nombre = val.toString();
                break;
            case 2:
                dias = Integer.parseInt(val.toString());
                break;
            case 3:
                monto = Double.parseDouble(val.toString());
                break;
            case 4:
                saldo = Double.parseDouble(val.toString());
                break;
            case 5:
                cedula = (val.toString());
                break;
            case 6:
                oficina = (val.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int ind, Hashtable ht, PropertyInfo info) {
        switch (ind) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Id";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Nombre";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "dias";
                break;
            case 3:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "monto";
                break;
            case 4:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "saldo";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "cedula";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "oficina";
                break;
            default:
                break;
        }
    }
}

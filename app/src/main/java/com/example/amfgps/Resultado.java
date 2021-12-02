package com.example.amfgps;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class Resultado implements KvmSerializable {
    public String mensaje;
    public Boolean Estado;
    public String cadena;
    public String Numero;
    public String Referencia;
    public String Diario;
    public String Tipo;
    public Double Valor;
    public Double SaldoVenc;
    public int Id;

    public Resultado() {
        mensaje = "";
        Estado = true;
        cadena = "";
        Numero = "";
        Referencia = "";
        Diario = "";
        Tipo = "";
        Valor = 0.0;
        SaldoVenc = 0.0;
        Id = 0;
    }

    public Resultado(String mensaje, Boolean estado, String numero, Double valor, Integer id, Double saldoVenc) {
        this.mensaje = mensaje;
        this.Estado = estado;
        this.Numero = numero;
        this.Valor = valor;
        this.Id = id;
        this.SaldoVenc = saldoVenc;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return mensaje;
            case 1:
                return Estado;
            case 2:
                return Numero;
            case 3:
                return Valor;
            case 4:
                return Id;
            case 5:
                return SaldoVenc;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 6;
    }

    @Override
    public void setProperty(int ind, Object val) {
        switch (ind) {
            case 0:
                mensaje = val.toString();
                break;
            case 1:
                Estado = Boolean.parseBoolean(val.toString());
                break;
            case 2:
                Numero = val.toString();
                break;
            case 3:
                Valor = Double.parseDouble(val.toString());
                break;
            case 4:
                Id = Integer.parseInt(val.toString());
                break;
            case 5:
                SaldoVenc = Double.parseDouble(val.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int ind, Hashtable ht, PropertyInfo info) {
        switch (ind) {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mensaje";
                break;
            case 1:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "Estado";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Numero";
                break;
            case 3:
                info.type = Double.class;
                info.name = "Valor";
                break;
            case 4:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Id";
                break;
            case 5:
                info.type = Double.class;
                info.name = "SaldoVenc";
                break;
            default:
                break;
        }
    }
}

package com.example.amfgps;

import android.app.Application;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class configuracion extends Application {
    private static configuracion instance;

    public static final String NAMESPACE = "http://android.sinv.com/";
    //  public static final String URL = "http://181.211.115.78:89/Service1.asmx";// AMNF
    //  public static final String URL = "http://192.168.1.77:82/Service1.asmx";//
    public static final String URL = "http://192.168.1.77:86/Service1.asmx";//
    //    public static final String URL = "http://190.95.238.226:86/Service1.asmx";// DMAR - DMTX WAN
    //    public static final String URL = "http://192.168.1.246:86/Service1.asmx";// DMAR - DMTX LAN
    /* Enlace para ICOPLAST */
    //    public static final String URL = "http://186.4.155.159:86/Service1.asmx";// ICPL WAN
    //    para pruebas internas
    //    public static final String URL = "http://192.168.1.101:63538/Service1.asmx";// TEST

    public static final String METHOD_NAME = "ConexionBaseDatos2";
    //numero de pedido
    private String numeroPedido;
    //usuario
    private  String bodegaSeleccionada;
    private String usuario, idCliente;
    private Cliente cliente;
    // control de existencias

    public static boolean controlExistencia = false; // ICPL
    //    public static  boolean controlExistencia = false; // DMAR, DMTX, AMNF, OVCL, MDPL
    private String clave;
    private String empresa;
    private  String oficina;
    private String nombreEmpresa;

    public static synchronized configuracion getInstance() {
        if (instance == null) {
            instance = new configuracion();
        }
        return instance;
    }

}

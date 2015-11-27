/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public abstract class SEtlConsts {
    
    public static final int MODE_CAT = 1;
    public static final int MODE_CAT_INV = 2;
    
    public static final int DB_MYSQL = 1;
    public static final int DB_SQL_SERVER = 2;
    
    public static final int AVISTA_INV_STA_APP = 2; // approved
    public static final int AVISTA_INV_STA_ARC = 3; // archived
    public static final int AVISTA_INV_TP_INV = 1; // invoice
    
    public static final String AVISTA_PAY_TERM_CNT = "CNT"; // contado
    public static final String AVISTA_BOOL_N = "N"; // no
    public static final String AVISTA_BOOL_Y = "Y"; // yes
    public static final String AVISTA_LOC_CTY = "MX"; // México
    public static final String AVISTA_LOC_STA = "MEX"; // Estado de México
    
    public static final HashMap<String, String> AvistaCountriesMap = new HashMap<>();
    public static final HashMap<String, String> AvistaStatesMap = new HashMap<>();
    
    static {
        AvistaCountriesMap.put(AVISTA_LOC_CTY, "México");
        
        AvistaStatesMap.put(AVISTA_LOC_STA, "Estado de México");
    }
    
    public static final int SIIE_DEFAULT = 1;
    public static final String SIIE_PAY_ACC_UNDEF = "NO IDENTIFICADO";
    
    public static final int RFC_LEN_PER = 13;
    public static final int RFC_LEN_ORG = 12;
    
    public static final int STEP_NA = 0; // n/a
    public static final int STEP_AUX_NA = 0; // n/a

    public static final int STEP_ETL_STA = 100; // starting ELT process
    public static final int STEP_ETL_STA_DB_SIIE = 110; // DB connection SIIE stablished
    public static final int STEP_ETL_STA_DB_AVISTA = 120; // DB connection Avista stablished
    public static final int STEP_ETL_END = 900; // ELT process finished

    public static final int STEP_CUS_STA = 200; // starting ELT customers
    public static final int STEP_AUX_CUS_AUX_1 = 1; // customers auxiliar step #1
    public static final int STEP_AUX_CUS_AUX_2 = 2; // customers auxiliar step #2
    public static final int STEP_AUX_CUS_AUX_3 = 3; // customers auxiliar step #3
    public static final int STEP_CUS_END = 299; // finished ELT customers

    public static final int STEP_ITM_STA = 300; // starting ELT items
    public static final int STEP_AUX_ITM_AUX_1 = 1; // items auxiliar step #1
    public static final int STEP_AUX_ITM_AUX_2 = 2; // items auxiliar step #2
    public static final int STEP_AUX_ITM_AUX_3 = 3; // items auxiliar step #3
    public static final int STEP_ITM_END = 399; // finished ETL items
    
    public static final String MSG_ERR = "Ha ocurrido una excepción ";
    public static final String MSG_ERR_UNK_CTY = MSG_ERR + "al identificar al país: ";
    public static final String MSG_ERR_UNK_STA = MSG_ERR + "al identificar al estado: ";
    public static final String MSG_ERR_SIIE_CUS_QRY = MSG_ERR + "al consultar el registro cliente: ";
    public static final String MSG_ERR_SIIE_CUS_INS = MSG_ERR + "al insertar el registro cliente: ";
    public static final String MSG_ERR_SIIE_CUS_UPD = MSG_ERR + "al actualizar el registro cliente: ";
    public static final String MSG_ERR_SIIE_ITM_QRY = MSG_ERR + "al consultar el registro ítem: ";
    public static final String MSG_ERR_SIIE_ITM_INS = MSG_ERR + "al insertar el registro ítem: ";
    public static final String MSG_ERR_SIIE_ITM_UPD = MSG_ERR + "al actualizar el registro ítem: ";
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public abstract class SModConsts {

    public static final int MOD_CFG = 110000;
    public static final int MOD_ETL = 210000;

    public static final int SU_SYS = 110001;
    public static final int SU_COM = 110002;

    public static final int CS_USR_TP = 111011;

    public static final int CU_USR = 112011;

    public static final int C_CFG = 113001;
    public static final int C_USR_GUI = 113101;

    public static final int AS_CUR = 211011;
    public static final int AS_UOM = 211021;
    public static final int AS_PAY_MET = 211111;

    public static final int AU_CUS = 212021;
    public static final int AU_ITM = 212031;

    public static final int A_CFG = 213001;
    public static final int A_INV = 213051;
    public static final int A_INV_ROW = 213056;
    public static final int A_EXR = 213061;
    public static final int A_ETL_LOG = 213501;
    
    public static final int AX_EXP = 215001;
    
    public static final HashMap<Integer, String> TablesMap = new HashMap<>();

    static {
        TablesMap.put(SU_SYS, "su_sys");
        TablesMap.put(SU_COM, "su_com");

        TablesMap.put(CS_USR_TP, "cs_usr_tp");

        TablesMap.put(CU_USR, "cu_usr");

        TablesMap.put(C_CFG, "c_cfg");
        TablesMap.put(C_USR_GUI, "c_usr_gui");

        TablesMap.put(AS_CUR, "as_cur");
        TablesMap.put(AS_UOM, "as_uom");
        TablesMap.put(AS_PAY_MET, "as_pay_met");

        TablesMap.put(AU_CUS, "au_cus");
        TablesMap.put(AU_ITM, "au_itm");

        TablesMap.put(A_CFG, "a_cfg");
        TablesMap.put(A_INV, "a_inv");
        TablesMap.put(A_INV_ROW, "a_inv_row");
        TablesMap.put(A_EXR, "a_exr");
        TablesMap.put(A_ETL_LOG, "a_etl_log");
    }
}

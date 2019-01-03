/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod;

/**
 *
 * @author Sergio Flores, Alfredo Pérez
 */
public abstract class SModSysConsts {

    public static final int CS_USR_TP_USR = 1;
    public static final int CS_USR_TP_ADM = 2;
    public static final int CS_USR_TP_SUP = 3;

    public static final int CU_USR_NA = 1;
    public static final int CU_USR_ADM = 2;
    public static final int CU_USR_SUP = 3;

    public static final int C_CFG_CFG = 1;

    public static final int AS_CUR_MXN = 1;
    public static final int AS_CUR_USD = 2;

    public static final int AS_UOM_MSM = 1;
    public static final int AS_UOM_MSF = 2;
    public static final int AS_UOM_SQM = 3;
    public static final int AS_UOM_SQF = 4;
    public static final int AS_UOM_PC = 5;
    public static final int AS_UOM_K = 6;
    public static final int AS_UOM_KG = 7;
    public static final int AS_UOM_TON = 8;

    public static final int AS_PAY_MET_NA = 1;
    public static final int AS_PAY_MET_CSH = 11;
    public static final int AS_PAY_MET_CHK = 12;
    public static final int AS_PAY_MET_TRN = 13;
    public static final int AS_PAY_MET_DBT = 21;
    public static final int AS_PAY_MET_CDT = 22;
    public static final int AS_PAY_MET_E_PUR = 31;
    public static final int AS_PAY_MET_E_MON = 32;
    public static final int AS_PAY_MET_FOO = 41;
    public static final int AS_PAY_MET_UND = 98;
    public static final int AS_PAY_MET_OTH = 99;

    public static final int SS_SHIPT_ST_REL_TO = 1;
    public static final int SS_SHIPT_ST_REL = 2;
    public static final int SS_SHIPT_ST_ACC_TO = 11;
    public static final int SS_SHIPT_ST_ACC = 12;
    public static final int SS_SHIPT_ST_CLO = 21;

    public static final int SS_WEB_ROLE_NA = 1;
    public static final int SS_WEB_ROLE_ADMIN = 11;
    public static final int SS_WEB_ROLE_CREDIT = 21;
    public static final int SS_WEB_ROLE_SHIPPER = 31;

    public static final int SS_WM_TICKET_TP_IN = 1;     // In
    public static final int SS_WM_TICKET_TP_OUT = 2;    // Out

    public static final int SU_VEHIC_TP_CUS = 9; //customer picks up

    public static final int SU_SHIPPER_NA = 1;

    public static final int SU_DESTIN_NA = 1;

    public static final int SU_WM_ITEM_NA = 1;
    public static final int SU_WM_ITEM_NOT_TARED = 2;
    public static final int SU_WM_ITEM_TARED = 3;

    public static final int SU_WM_ITEM_TP = 17;

    public static final int S_CFG_CFG = 1;
    
    public static final int SX_INV_SAL = 1;
    public static final int SX_INV_PUR = 2;
    public static final int SX_CN_SAL = 3;
    public static final int SX_CN_PUR = 4;

    public static final int SX_REG_TO_LINK = 1;
    public static final int SX_REG_LINKED = 2;
    public static final int SX_REG_ALL = 3;

    public static final int SX_TIC_TO_DOC_OUT_TO_INV_SAL = 50;
    public static final int SX_TIC_TO_DOC_OUT_TO_CN_PUR = 51;
    public static final int SX_TIC_TO_DOC_IN_TO_INV_PUR = 52;
    public static final int SX_TIC_TO_DOC_IN_TO_CN_SAL = 53;

    public static final int SX_DOC_TO_TIC_INV_SAL_TO_TIC_OUT = 60;
    public static final int SX_DOC_TO_TIC_CN_PUR_TO_TIC_OUT = 61;
    public static final int SX_DOC_TO_TIC_INV_PUR_TO_TIC_IN = 62;
    public static final int SX_DOC_TO_TIC_CN_SAL_TO_TIC_IN = 63;
    
    public static final int SX_TIC_OUT = 70;
    public static final int SX_TIC_IN = 80;
    
}

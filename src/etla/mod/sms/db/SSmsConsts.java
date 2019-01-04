/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

/**
 *
 * @author Alfredo Perez
 */
public abstract class SSmsConsts {
    
    /** Document class number: income. */
    public static final int DOC_CLASS_INC_NUM = 1;
    /** Document class number: expenses. */
    public static final int DOC_CLASS_EXP_NUM = 2;
    /** Document type number: invoice. */
    public static final int DOC_TYPE_INV_NUM = 3;
    /** Document type number: credit note. */
    public static final int DOC_TYPE_CN_NUM = 5;
    
    /** DOCUMENT STATUS  = NEW*/
    public static final int ST_DPS_NEW = 1;
    /** DOCUMENT STATUS  = EMITED*/
    public static final int ST_DPS_EMITTED = 2;
    /** DOCUMENT STATUS  = CANCELED*/
    public static final int ST_DPS_CANCELED = 3;
    
    /** IMPORT TYPE DOCUMENT*/
    public static final int IMPORT_TYPE_DOC = 1;
    /** IMPORT TYPE TICKET*/
    public static final int IMPORT_TYPE_TIC = 2;
    /** IMPORT TYPE ITEM*/
    public static final int IMPORT_TYPE_ITEM = 3;
    
    public static final boolean IS_NEW_REGISTRY = true;
    
    public static final int REGISTRY_CLOSE = 1;
    public static final int REGISTRY_OPEN = 2;
    
}

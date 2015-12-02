/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import erp.mod.SModSysConsts;
import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SEtlUtils {
    
    public static DecimalFormat DecimalFormatPlantBoardType = new DecimalFormat("000000");
    
    public static String composeItemCode(final String prefix, final int plantBoardType, final String flute) {
        return prefix + "-" + DecimalFormatPlantBoardType.format(plantBoardType) + "-" + flute;
    }
    
    public static SDbCustomer getEtlCustomer(final SGuiSession session, final String customerId) throws Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        SDbCustomer customer = null;
        
        statement = session.getStatement().getConnection().createStatement();
        
        sql = "SELECT id_cus, b_del "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " "
                + "WHERE src_cus_id='" + customerId + "' "
                + "ORDER BY b_del DESC, id_cus "; // priority on alive records, newest record first!
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            customer = (SDbCustomer) session.readRegistry(SModConsts.AU_CUS, new int[] { resultSet.getInt("id_cus") });
        }
        
        return customer;
    }
    
    public static double getExchangeRate(final SGuiSession session, final int currencyPk, final Date date) throws Exception {
        double exchangeRate = 0;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT exr, id_exr "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.A_EXR) + " "
                + "WHERE id_cur=" + currencyPk + " AND dat='" + SLibUtils.DbmsDateFormatDate.format(date) + "' "
                + "ORDER BY id_exr DESC "; // newest record first!
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exchangeRate = resultSet.getDouble(1);
        }
        
        return exchangeRate;
    }
    
    public static int getNextDpsNumber(final SGuiSession session, final Statement statement) throws Exception {
        int number = 0;
        String sql = "";
        ResultSet resultSet = null;
        SDbConfigAvista configAvista = ((SDbConfig) session.getConfigSystem()).getRegConfigAvista();
        
        sql = "SELECT COALESCE(MAX(CAST(num AS SIGNED)) + 1, 0) "
                + "FROM th.trn_dps "
                + "WHERE num_ser='" + configAvista.getInvoiceSeries() + "' AND "
                + "fid_ct_dps=" + SModSysConsts.TRNU_TP_DPS_SAL_INV[0] + " AND "
                + "fid_cl_dps=" + SModSysConsts.TRNU_TP_DPS_SAL_INV[1] + " AND "
                + "fid_tp_dps=" + SModSysConsts.TRNU_TP_DPS_SAL_INV[2] + " AND "
                + "b_del=0 ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            number = resultSet.getInt(1);
        }
        
        if (number == 0) {
            number = configAvista.getInvoiceNumberStarting();
        }
        
        return number;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SEtlCatalogs {
    
    private SGuiSession moSession;
    private ArrayList<SDbSysCurrency> maCurrencies;
    private ArrayList<SDbSysUnitOfMeasure> maUnitOfMeasures;
    
    public SEtlCatalogs(SGuiSession session) throws Exception {
        moSession = session;
        maCurrencies = new ArrayList<>();
        maUnitOfMeasures = new ArrayList<>();
        readCatalogs();
    }
    
    /*
     * Private methods:
     */

    private void readCatalogs() throws Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        
        statement = moSession.getStatement().getConnection().createStatement();
        
        sql = "SELECT id_cur FROM " + SModConsts.TablesMap.get(SModConsts.AS_CUR) + " ORDER BY id_cur ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDbSysCurrency cur = new SDbSysCurrency();
            cur.read(moSession, new int[] { resultSet.getInt(1) });
            maCurrencies.add(cur);
        }
        
        sql = "SELECT id_uom FROM " + SModConsts.TablesMap.get(SModConsts.AS_UOM) + " ORDER BY id_uom ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDbSysUnitOfMeasure uom = new SDbSysUnitOfMeasure();
            uom.read(moSession, new int[] { resultSet.getInt(1) });
            maUnitOfMeasures.add(uom);
        }
    }
    
    /*
     * Public methods:
     */
    
    public ArrayList<SDbSysCurrency> getCurrencies() { return maCurrencies; }
    public ArrayList<SDbSysUnitOfMeasure> getUnitOfMeasures() { return maUnitOfMeasures; }
    
    /**
     * @param sourcePk Avista's currency PK.
     */
    public int getEtlIdForCurrency(final int sourcePk) {
        int etlId = 0;
        
        for (SDbSysCurrency cur : maCurrencies) {
            if (cur.getSrcCurrencyId() == sourcePk) {
                etlId = cur.getPkCurrencyId();
                break;
            }
        }
        
        return etlId;
    }
    
    /**
     * @param sourcePk Avista's unit of measure PK.
     */
    public int getEtlIdForUnitOfMeasure(final String sourcePk) {
        int etlId = 0;
        
        for (SDbSysUnitOfMeasure uom : maUnitOfMeasures) {
            if (uom.getSrcUnitOfMeasureId().compareTo(sourcePk) == 0) {
                etlId = uom.getPkUnitOfMeasureId();
                break;
            }
        }
        
        return etlId;
    }
    
    /**
     * @param etlId ETL's currency PK.
     */
    public SDbSysCurrency getEtlCurrency(final int etlId) {
        SDbSysCurrency cur = null;
        
        for (SDbSysCurrency c : maCurrencies) {
            if (c.getPkCurrencyId() == etlId) {
                cur = c;
                break;
            }
        }
        
        return cur;
    }
    
    /**
     * @param etlId ETL's unit of measure PK.
     */
    public SDbSysUnitOfMeasure getEtlUnitOfMeasure(final int etlId) {
        SDbSysUnitOfMeasure uom = null;
        
        for (SDbSysUnitOfMeasure u : maUnitOfMeasures) {
            if (u.getPkUnitOfMeasureId() == etlId) {
                uom = u;
                break;
            }
        }
        
        return uom;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Daniel López
 */
public class SDbShipmentRow extends SDbRegistryUser{
    
        protected int mnPkShipmentId;
        protected int mnPkRowId;
        protected int mnDeliveryId;
        protected int mnDeliveryNumber;
        protected int mnInvoiceIdYear;
        protected int mnInvoiceIdDoc;
        protected String msInvoiceSeries;
        protected String msInvoiceNumber;
        protected int mnOrders;
        protected int mnBales;
        protected double mdm2;
        protected int mnFkCustomerId;
        protected int mnFkDestinationId;

    
    public SDbShipmentRow () {
        super(SModConsts.S_SHIPT_ROW);
    }
    
     /*
     * Public methods
     */
    
    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setDeliveryId(int n) { mnDeliveryId = n; }
    public void setDeliveryNumber(int n) { mnDeliveryNumber = n; }
    public void setInvoiceIdYear(int n) { mnInvoiceIdYear = n; }
    public void setInvoiceIdDoc(int n) { mnInvoiceIdDoc = n; }
    public void setInvoiceSeries(String s) { msInvoiceSeries = s; }
    public void setInvoiceNumber(String s) { msInvoiceNumber = s; }
    public void setOrders(int n) { mnOrders = n; }
    public void setBales(int n) { mnBales = n; }
    public void setm2(double d) { mdm2 = d; }
    public void setFkCustomerId(int n) { mnFkCustomerId = n; }
    public void setFkDestinationId(int n) { mnFkDestinationId = n; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getDeliveryId() { return mnDeliveryId; }
    public int getDeliveryNumber() { return mnDeliveryNumber; }
    public int getInvoiceIdYear() { return mnInvoiceIdYear; }
    public int getInvoiceIdDoc() { return mnInvoiceIdDoc; }
    public String getInvoiceSeries() { return msInvoiceSeries; }
    public String getInvoiceNumber() { return msInvoiceNumber; }
    public int getOrders() { return mnOrders; }
    public int getBales() { return mnBales; }
    public double getm2() { return mdm2; }
    public int getFkCustomerId() { return mnFkCustomerId; }
    public int getFkDestinationId() { return mnFkDestinationId; }

    
     /*
     * Overriden methods
     */


    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId };
    }

    @Override
    public void initRegistry() {
        
        initBaseRegistry();
        
        mnPkShipmentId = 0;
        mnPkRowId = 0;
        mnDeliveryId = 0;
        mnDeliveryNumber = 0;
        mnInvoiceIdYear = 0;
        mnInvoiceIdDoc = 0;
        msInvoiceSeries = "";
        msInvoiceNumber = "";
        mnOrders = 0;
        mnBales = 0;
        mdm2 = 0;
        mnFkCustomerId = 0;
        mnFkDestinationId = 0;

    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_shipt = " + mnPkShipmentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_shipt = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkShipmentId = 0;

        msSql = "SELECT COALESCE(MAX(id_shipt), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkShipmentId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkShipmentId = resultSet.getInt("id_shipt");
            mnPkRowId = resultSet.getInt("id_row");
            mnDeliveryId = resultSet.getInt("delivery_id");
            mnDeliveryNumber = resultSet.getInt("delivery_number");
            mnInvoiceIdYear = resultSet.getInt("invoice_id_year");
            mnInvoiceIdDoc = resultSet.getInt("invoice_id_doc");
            msInvoiceSeries = resultSet.getString("invoice_series");
            msInvoiceNumber = resultSet.getString("invoice_number");
            mnOrders = resultSet.getInt("orders");
            mnBales = resultSet.getInt("bales");
            mdm2 = resultSet.getDouble("m2");
            mnFkCustomerId = resultSet.getInt("fk_customer");
            mnFkDestinationId = resultSet.getInt("fk_destin");

            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {       
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
               mnPkShipmentId + ", " + 
               mnPkRowId + ", " + 
               mnDeliveryId + ", " + 
               mnDeliveryNumber + ", " + 
               mnInvoiceIdYear + ", " + 
               mnInvoiceIdDoc + ", " + 
               "'" + msInvoiceSeries + "', " + 
               "'" + msInvoiceNumber + "', " + 
               mnOrders + ", " + 
               mnBales + ", " + 
               mdm2 + ", " + 
               mnFkCustomerId + ", " + 
               mnFkDestinationId + " " +
               ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                "id_shipt = " + mnPkShipmentId + ", " +
                "id_row = " + mnPkRowId + ", " +
                "delivery_id = " + mnDeliveryId + ", " +
                "delivery_number = " + mnDeliveryNumber + ", " +
                "invoice_id_year = " + mnInvoiceIdYear + ", " +
                "invoice_id_doc = " + mnInvoiceIdDoc + ", " +
                "invoice_series = '" + msInvoiceSeries + "', " +
                "invoice_number = '" + msInvoiceNumber + "', " +
                "orders = " + mnOrders + ", " +
                "bales = " + mnBales + ", " +
                "m2 = " + mdm2 + ", " +
                "fk_customer = " + mnFkCustomerId + ", " +
                "fk_destin = " + mnFkDestinationId + " " +
                 getSqlWhere();
        }
        
        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbShipmentRow clone() throws CloneNotSupportedException {
        SDbShipmentRow  registry = new SDbShipmentRow();
        
        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setPkRowId(this.getPkRowId());
        registry.setDeliveryId(this.getDeliveryId());
        registry.setDeliveryNumber(this.getDeliveryNumber());
        registry.setInvoiceIdYear(this.getInvoiceIdYear());
        registry.setInvoiceIdDoc(this.getInvoiceIdDoc());
        registry.setInvoiceSeries(this.getInvoiceSeries());
        registry.setInvoiceNumber(this.getInvoiceNumber());
        registry.setOrders(this.getOrders());
        registry.setBales(this.getBales());
        registry.setm2(this.getm2());
        registry.setFkCustomerId(this.getFkCustomerId());
        registry.setFkDestinationId(this.getFkDestinationId());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

}

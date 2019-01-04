/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Alfredo Pérez
 */
public class SDbErpDoc extends SDbRegistryUser {
    
    public static final String CLASS_INC = "INC";
    public static final String CLASS_EXP = "EXP";
    public static final String TYPE_INV = "INV";
    public static final String TYPE_CN = "CN";
    
    public static final HashMap<String, String> Descriptions = new HashMap<>();
    
    static {
        Descriptions.put(CLASS_INC, "Ingreso");
        Descriptions.put(CLASS_INC, "Egreso");
        Descriptions.put(TYPE_INV, "Factura");
        Descriptions.put(TYPE_CN, "Nota crédito");
    }

    protected int mnPkErpDocId;
    protected int mnErpYearId;
    protected int mnErpDocId;
    protected String msDocClass;
    protected String msDocType;
    protected String msDocSeries;
    protected String msDocNumber;
    protected Date mtDocDate;
    protected int mnBizPartnerId;
    protected String msBizPartner;
    protected double mdWeight;
    protected Date mtDocUpdate;
    protected boolean mbClosed;
    protected int mnFkUserClosedId;
    protected Date mtTsUserClosed;
    
    protected String msAuxType;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected ArrayList<SDbShipmentRow> maChildRows;

    public SDbErpDoc () {
        super(SModConsts.S_ERP_DOC);
    }

    /*
     * Public methods
     */

    public void setPkErpDocId(int n) { mnPkErpDocId = n; }
    public void setErpYearId(int n) { mnErpYearId = n; }
    public void setErpDocId(int n) { mnErpDocId = n; }
    public void setDocClass(String s) { msDocClass = s; }
    public void setDocType(String s) { msDocType = s; }
    public void setDocSeries(String s) { msDocSeries = s; }
    public void setDocNumber(String s) { msDocNumber = s; }
    public void setDocDate(Date t) { mtDocDate = t; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setWeight(double d) { mdWeight = d; }
    public void setDocUpdate(Date t) { mtDocUpdate = t; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxType(String s) { msAuxType = s; }

    
    public int getPkErpDocId() { return mnPkErpDocId; }
    public int getErpYearId() { return mnErpYearId; }
    public int getErpDocId() { return mnErpDocId; }
    public String getDocClass() { return msDocClass; }
    public String getDocType() { return msDocType; }
    public String getDocSeries() { return msDocSeries; }
    public String getDocNumber() { return msDocNumber; }
    public Date getDocDate() { return mtDocDate; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public String getBizPartner() { return msBizPartner; }
    public double getWeight() { return mdWeight; }
    public Date getDocUpdate() { return mtDocUpdate; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosed() { return mtTsUserClosed; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public String getAuxType() { return msAuxType; }


    public String getDocSeriesNumber() {
        return (msDocSeries.isEmpty() ? "" : msDocSeries + "-") + msDocNumber;
    }
    public ArrayList<SDbShipmentRow> getChildRows() { return maChildRows; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
       mnPkErpDocId = pk[0];
    }

     @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkErpDocId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnErpYearId = 0;
        mnErpDocId = 0;
        msDocClass = "";
        msDocType = "";
        msDocSeries = "";
        msDocNumber = "";
        mtDocDate = null;
        mnBizPartnerId = 0;
        msBizPartner = "";
        mdWeight = 0;
        mtDocUpdate = null;
        mbClosed = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserClosedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosed = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
//        return "WHERE id_erp_doc = " + mnPkErpDocId + " ";
        return "WHERE erp_year_id = " + mnErpYearId + " AND " + "erp_doc_id = " + mnErpDocId + " AND " + "doc_num = " + msDocNumber + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_erp_doc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkErpDocId = 0;

        msSql = "SELECT COALESCE(MAX(id_erp_doc), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkErpDocId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT CONCAT (IF(S_ERP_DOC.doc_type = '" + SDbErpDoc.TYPE_INV + "', 'FACTURA','NOTA DE CREDITO'),IF(S_ERP_DOC.doc_class = '" +
                SDbErpDoc.CLASS_INC + "', ' ENTRADA',' SALIDA')) AS Tipo, S_ERP_DOC.* " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkErpDocId = resultSet.getInt("id_erp_doc");
            mnErpYearId = resultSet.getInt("erp_year_id");
            mnErpDocId = resultSet.getInt("erp_doc_id");
            msDocClass = resultSet.getString("doc_class");
            msDocType = resultSet.getString("doc_type");
            msDocSeries = resultSet.getString("doc_ser");
            msDocNumber = resultSet.getString("doc_num");
            mtDocDate = resultSet.getDate("doc_date");
            msBizPartner = resultSet.getString("biz_partner");
            mdWeight = resultSet.getDouble("weight");
            mtDocUpdate = resultSet.getDate("doc_upd");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getDate("ts_usr_ins");
            mtTsUserUpdate = resultSet.getDate("ts_usr_upd");
            msAuxType = resultSet.getString("Tipo");

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

            msSql = "INSERT INTO `etla_com`.`S_ERP_DOC` "
                + "(`id_erp_doc`, "
                    + "`erp_year_id`, "
                    + "`erp_doc_id`, "
                    + "`doc_class`, "
                    + "`doc_type`, "
                    + "`doc_ser`, "
                    + "`doc_num`, "
                    + "`doc_date`, "
                    + "`biz_partner_id`, "
                    + "`biz_partner`, "
                    + "`weight`, "
                    + "`doc_upd`, "
                    + "`b_clo`, "
                    + "`b_del`, "
                    + "`b_sys`, "
                    + "`fk_usr_clo`, "
                    + "`fk_usr_ins`, "
                    + "`fk_usr_upd`, "
                    + "`ts_usr_clo`, "
                    + "`ts_usr_ins`, "
                    + "`ts_usr_upd`) "
                + "VALUES ('" + mnPkErpDocId + "', '" + mnErpYearId + "', '" + mnErpDocId + "', '" + msDocClass + "', '" + msDocType + "', '" 
                + msDocSeries + "', '" + msDocNumber + "', '" + SLibUtils.DbmsDateFormatDate.format(mtDocDate) + "', '" + mnBizPartnerId + "', '" + 
                    msBizPartner + "', '" + mdWeight + "', '" + mtDocUpdate + "', '0', '0', '0', '1', '1', '1', NOW(), NOW(), NOW());";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_erp_doc = " + mnPkErpDocId + ", " +
                    "erp_year_id = " + mnErpYearId + ", " +
                    "erp_doc_id = " + mnErpDocId + ", " +
                    "doc_class = '" + msDocClass + "', " +
                    "doc_type = '" + msDocType + "', " +
                    "doc_ser = '" + msDocSeries + "', " +
                    "doc_num = '" + msDocNumber + "', " +
                    "doc_date = '" + SLibUtils.DbmsDateFormatDate.format(mtDocDate) + "', " +
                    "biz_partner_id = " + mnBizPartnerId + ", " +
                    "biz_partner = '" + msBizPartner + "', " +
                    "weight = " + mdWeight + ", " +
                    "doc_upd = '" + mtDocUpdate + "', " +
                    "b_clo = " + (mbClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_usr_clo = " + mnFkUserClosedId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_clo = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW() " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbErpDoc clone() throws CloneNotSupportedException {
        SDbErpDoc registry = new SDbErpDoc();

        registry.setPkErpDocId(this.getPkErpDocId());
        registry.setErpYearId(this.getErpYearId());
        registry.setErpDocId(this.getErpDocId());
        registry.setDocClass(this.getDocClass());
        registry.setDocType(this.getDocType());
        registry.setDocSeries(this.getDocSeries());
        registry.setDocNumber(this.getDocNumber());
        registry.setDocDate(this.getDocDate());
        registry.setBizPartnerId(this.getBizPartnerId());
        registry.setBizPartner(this.getBizPartner());
        registry.setWeight(this.getWeight());
        registry.setDocUpdate(this.getDocUpdate());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserClosedId(this.getFkUserClosedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosed(this.getTsUserClosed());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxType(this.getAuxType());
                
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

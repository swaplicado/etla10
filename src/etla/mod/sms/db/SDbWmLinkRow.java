/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Luis Alfredo Pérez
 */
public class SDbWmLinkRow extends SDbRegistryUser{
    
    protected int mnPkWmTicketLinkId;
    protected double mdWeightLinked;
    protected String msNotes;
    protected boolean mbApproved;
    protected boolean mbAutoApproved;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkWmTicketId;
    protected int mnFkErpDocId;
    protected int mnFkWmLinkStatusId;
    protected int mnFkUserLinkStatusId;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserLinkStatus;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    protected int TEMP;

    
    public SDbWmLinkRow () {
        super(SModConsts.S_SHIPT_ROW);
    }
    
    /*
     * Public methods
     */
    
    public void setPkWmTicketLinkId(int n) { mnPkWmTicketLinkId = n; }
    public void setWeightLinked(double d) { mdWeightLinked = d; }
    public void setNotes(String s) { msNotes = s; }
    public void setApproved(boolean b) { mbApproved = b; }
    public void setAutoApproved(boolean b) { mbAutoApproved = b; }
    @Override
    public void setDeleted(boolean b) { mbDeleted = b; }
    @Override
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkWmTicketId(int n) { mnFkWmTicketId = n; }
    public void setFkErpDocId(int n) { mnFkErpDocId = n; }
    public void setFkWmLinkStatusId(int n) { mnFkWmLinkStatusId = n; }
    public void setFkUserLinkStatusId(int n) { mnFkUserLinkStatusId = n; }
    @Override
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    @Override
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserLinkStatus(Date t) { mtTsUserLinkStatus = t; }
    @Override
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    @Override
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }


    public int getPkWmTicketLinkId() { return mnPkWmTicketLinkId; }
    public double getWeightLinked() { return mdWeightLinked; }
    public String getNotes() { return msNotes; }
    public boolean isApproved() { return mbApproved; }
    public boolean isAutoApproved() { return mbAutoApproved; }
    @Override
    public boolean isDeleted() { return mbDeleted; }
    @Override
    public boolean isSystem() { return mbSystem; }
    public int getFkWmTicketId() { return mnFkWmTicketId; }
    public int getFkErpDocId() { return mnFkErpDocId; }
    public int getFkWmLinkStatusId() { return mnFkWmLinkStatusId; }
    public int getFkUserLinkStatusId() { return mnFkUserLinkStatusId; }
    @Override
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    @Override
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserLinkStatus() { return mtTsUserLinkStatus; }
    @Override
    public Date getTsUserInsert() { return mtTsUserInsert; }
    @Override
    public Date getTsUserUpdate() { return mtTsUserUpdate; }


    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkWmTicketLinkId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkWmTicketLinkId};
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkWmTicketLinkId = 0;
        mdWeightLinked = 0;
        msNotes = "";
        mbApproved = false;
        mbAutoApproved = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkWmTicketId = 0;
        mnFkErpDocId = 0;
        mnFkWmLinkStatusId = 0;
        mnFkUserLinkStatusId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserLinkStatus = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_wm_ticket_link = " + mnPkWmTicketLinkId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_wm_ticket_link = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " WHERE id_wm_ticket_link = " + mnPkWmTicketLinkId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            TEMP = resultSet.getInt(1);
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
            mnPkWmTicketLinkId = resultSet.getInt("id_wm_ticket_link");
            mdWeightLinked = resultSet.getDouble("weight_link");
            msNotes = resultSet.getString("notes");
            mbApproved = resultSet.getBoolean("b_appd");
            mbAutoApproved = resultSet.getBoolean("b_aut_appd");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkWmTicketId = resultSet.getInt("fk_wm_ticket");
            mnFkErpDocId = resultSet.getInt("fk_erp_doc");
            mnFkWmLinkStatusId = resultSet.getInt("fk_wm_link_st");
            mnFkUserLinkStatusId = resultSet.getInt("fk_usr_link_st");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserLinkStatus = resultSet.getTimestamp("ts_usr_link_st");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {       
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        // save shipment row registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkWmTicketLinkId + ", " + 
                mdWeightLinked + ", " + 
                "'" + msNotes + "', " + 
                (mbApproved ? 1 : 0) + ", " + 
                (mbAutoApproved ? 1 : 0) + ", " + 
                (mbDeleted ? 1 : 0) + ", " + 
                (mbSystem ? 1 : 0) + ", " + 
                mnFkWmTicketId + ", " + 
                mnFkErpDocId + ", " + 
                mnFkWmLinkStatusId + ", " + 
                mnFkUserLinkStatusId + ", " + 
                mnFkUserInsertId + ", " + 
                mnFkUserUpdateId + ", " + 
                "NOW()" + ", " + 
                "NOW()" + ", " + 
                "NOW()" + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                "id_wm_ticket_link = " + mnPkWmTicketLinkId + ", " +
                "weight_link = " + mdWeightLinked + ", " +
                "notes = '" + msNotes + "', " +
                "b_appd = " + (mbApproved ? 1 : 0) + ", " +
                "b_aut_appd = " + (mbAutoApproved ? 1 : 0) + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                "fk_wm_ticket = " + mnFkWmTicketId + ", " +
                "fk_erp_doc = " + mnFkErpDocId + ", " +
                "fk_wm_link_st = " + mnFkWmLinkStatusId + ", " +
                "fk_usr_link_st = " + mnFkUserLinkStatusId + ", " +
//                "fk_usr_ins = " + mnFkUserInsertId + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_link_st = " + "NOW()" + ", " +
//                "ts_usr_ins = " + "NOW()" + ", " +
                "ts_usr_upd = " + "NOW() " +
                getSqlWhere();
        }
        
        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbWmLinkRow clone() throws CloneNotSupportedException {
        SDbWmLinkRow registry = new SDbWmLinkRow();

        registry.setPkWmTicketLinkId(this.getPkWmTicketLinkId());
        registry.setWeightLinked(this.getWeightLinked());
        registry.setNotes(this.getNotes());
        registry.setApproved(this.isApproved());
        registry.setAutoApproved(this.isAutoApproved());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkWmTicketId(this.getFkWmTicketId());
        registry.setFkErpDocId(this.getFkErpDocId());
        registry.setFkWmLinkStatusId(this.getFkWmLinkStatusId());
        registry.setFkUserLinkStatusId(this.getFkUserLinkStatusId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserLinkStatus(this.getTsUserLinkStatus());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

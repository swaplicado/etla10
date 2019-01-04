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
 * @author Alfredo Pérez
 */
public class SDbWmTicketLink extends SDbRegistryUser{

    protected int mnPkWmTicketLinkId;
    protected double mdWeightLinked;
    protected String msNotes;
    protected boolean mbApproved;
    protected boolean mbAutoApproved;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkWmTicketId;
    protected int mnFkErpDocId;
    protected int mnFkShipmentId_n;
    protected int mnFkWmLinkStatusId;
    protected int mnFkUserLinkStatusId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserLinkStatus;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected double auxWeightLinked;
    protected double auxWeightAvailableDoc;
    protected double auxWeightAvailableTic;
    protected int auxCountLinks;

    public SDbWmTicketLink () {
        super(SModConsts.S_WM_TICKET_LINK);
    }
    
    /*
     * Public methods
     */
    
    public void setPkWmTicketLinkId(int n) { mnPkWmTicketLinkId = n; }
    public void setWeightLinked(double d) { mdWeightLinked = d; }
    public void setNotes(String s) { msNotes = s; }
    public void setApproved(boolean b) { mbApproved = b; }
    public void setAutoApproved(boolean b) { mbAutoApproved = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkWmTicketId(int n) { mnFkWmTicketId = n; }
    public void setFkErpDocId(int n) { mnFkErpDocId = n; }
    public void setFkShipmentId_n(int n) { mnFkShipmentId_n = n; }
    public void setFkWmLinkStatusId(int n) { mnFkWmLinkStatusId = n; }
    public void setFkUserLinkStatusId(int n) { mnFkUserLinkStatusId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserLinkStatus(Date t) { mtTsUserLinkStatus = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxWeightLinked(double d) { auxWeightLinked = d; }
    public void setAuxWeightAvailableDoc(double d) { auxWeightAvailableDoc = d; }
    public void setAuxWeightAvailableTic(double d) { auxWeightAvailableTic = d; }
    public void setAuxCountLinks(int n) { auxCountLinks = n; }

    public int getPkWmTicketLinkId() { return mnPkWmTicketLinkId; }
    public double getWeightLinked() { return mdWeightLinked; }
    public String getNotes() { return msNotes; }
    public boolean isApproved() { return mbApproved; }
    public boolean isAutoApproved() { return mbAutoApproved; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkWmTicketId() { return mnFkWmTicketId; }
    public int getFkErpDocId() { return mnFkErpDocId; }
    public int getFkShipmentId_n() { return mnFkShipmentId_n; }
    public int getFkWmLinkStatusId() { return mnFkWmLinkStatusId; }
    public int getFkUserLinkStatusId() { return mnFkUserLinkStatusId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserLinkStatus() { return mtTsUserLinkStatus; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public double getAuxWeightLinked() { return auxWeightLinked; }
    public double getAuxWeightAvailableDoc() { return auxWeightAvailableDoc; }
    public double getAuxWeightAvailableTic() { return auxWeightAvailableTic; }
    public int getAuxCountLinks() { return auxCountLinks; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkWmTicketLinkId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkWmTicketLinkId };
    }
    
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
        mnFkShipmentId_n = 0;
        mnFkWmLinkStatusId = 0;
        mnFkUserLinkStatusId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserLinkStatus = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        auxWeightLinked = 0;
        auxWeightAvailableDoc = 0;
        auxWeightAvailableTic = 0;
        auxCountLinks = 0;
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

        mnPkWmTicketLinkId = 0;

        msSql = "SELECT COALESCE(MAX(id_wm_ticket_link), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkWmTicketLinkId = resultSet.getInt(1);
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
            mnFkShipmentId_n = resultSet.getInt("fk_shipt_n");
            mnFkWmLinkStatusId = resultSet.getInt("fk_wm_link_st");
            mnFkUserLinkStatusId = resultSet.getInt("fk_usr_link_st");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserLinkStatus = resultSet.getDate("ts_usr_link_st");
            mtTsUserInsert = resultSet.getDate("ts_usr_ins");
            mtTsUserUpdate = resultSet.getDate("ts_usr_upd");

            msSql = "SELECT * ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }else{
                auxWeightLinked = resultSet.getDouble("");
                auxWeightAvailableDoc = resultSet.getDouble("");
                auxWeightAvailableTic = resultSet.getDouble("");
                auxCountLinks = resultSet.getInt("");
            }
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
//            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
//                    "id_wm_ticket_link = " + mnPkWmTicketLinkId + ", " +
//                    "weight_link = " + mdWeightLinked + ", " +
//                    "notes = '" + msNotes + "', " +
//                    "b_appd = " + (mbApproved ? 1 : 0) + ", " +
//                    "b_aut_appd = " + (mbAutoApproved ? 1 : 0) + ", " +
//                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
//                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
//                    "fk_wm_ticket = " + mnFkWmTicketId + ", " +
//                    "fk_erp_doc = " + mnFkErpDocId + ", " +
//                    "fk_shipt_n = " + mnFkShipmentId_n + ", " +
//                    "fk_wm_link_st = " + mnFkWmLinkStatusId + ", " +
//                    "fk_usr_link_st = " + mnFkUserLinkStatusId + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
//                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_link_st = " + "NOW()" + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
//                    "ts_usr_upd = " + "NOW()" + " " +
//                     ")";
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkWmTicketLinkId + ", " +
                    mdWeightLinked + ", '" +
                    msNotes + "', " +
                    (mbApproved ? 1 : 0) + ", " +
                    (mbAutoApproved ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkWmTicketId + ", " +
                    mnFkErpDocId + ", " +
                    mnFkShipmentId_n + ", " +
                    mnFkWmLinkStatusId + ", " +
                    mnFkUserLinkStatusId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ");";
            
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_wm_ticket_link = " + mnPkWmTicketLinkId + ", " +
                    "weight_link = " + mdWeightLinked + ", " +
                    "notes = '" + msNotes + "', " +
                    "b_appd = " + (mbApproved ? 1 : 0) + ", " +
                    "b_aut_appd = " + (mbAutoApproved ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_wm_ticket = " + mnFkWmTicketId + ", " +
                    "fk_erp_doc = " + mnFkErpDocId + ", " +
                    "fk_shipt_n = " + mnFkShipmentId_n + ", " +
                    "fk_wm_link_st = " + mnFkWmLinkStatusId + ", " +
                    "fk_usr_link_st = " + mnFkUserLinkStatusId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_link_st = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbWmTicketLink clone() throws CloneNotSupportedException {
        SDbWmTicketLink registry = new SDbWmTicketLink();

        registry.setPkWmTicketLinkId(this.getPkWmTicketLinkId());
        registry.setWeightLinked(this.getWeightLinked());
        registry.setNotes(this.getNotes());
        registry.setApproved(this.isApproved());
        registry.setAutoApproved(this.isAutoApproved());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkWmTicketId(this.getFkWmTicketId());
        registry.setFkErpDocId(this.getFkErpDocId());
        registry.setFkShipmentId_n(this.getFkShipmentId_n());
        registry.setFkWmLinkStatusId(this.getFkWmLinkStatusId());
        registry.setFkUserLinkStatusId(this.getFkUserLinkStatusId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserLinkStatus(this.getTsUserLinkStatus());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxWeightLinked(this.getAuxWeightLinked());
        registry.setAuxWeightAvailableDoc(this.getAuxWeightAvailableDoc());
        registry.setAuxWeightAvailableTic(this.getAuxWeightAvailableTic());
        registry.setAuxCountLinks(this.getAuxCountLinks());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

}

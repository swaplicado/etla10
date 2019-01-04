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
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Alfredo Pérez
 */
public class SDbWmTicket extends SDbRegistryUser {

    protected int mnPkWmTicketId;
    protected int mnTicketId;
    protected Date mtTicketDatetimeArrival;
    protected Date mtTicketDatetimeDeparture;
    protected String msCompany;
    protected String msDriverName;
    protected String msVehiclePlate;
    protected double mdWeightArrival;
    protected double mdWeightDeparture;
    protected double mdWeight;
    protected boolean mbWmInfoArrival;
    protected boolean mbWmInfoDeparture;
    protected boolean mbTared;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkWmTicketTypeId;
    protected int mnFkWmItemId;
    protected int mnFkUserTareId;
    protected boolean mbClosed;
    protected int mnFkUserClosedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserTare;
    protected Date mtTsUserClosed;
    
    /*
    protected Date mtTsUserInsert;
    */
    protected Date mtTsUserUpdate;

  
    public SDbWmTicket () {
        super(SModConsts.S_WM_TICKET);
    }

    /*
     * Private methods
     */
    
    private void computeWeight() {
        mdWeight = Math.abs(mdWeightArrival - mdWeightDeparture);
    }

    /*
     * Public methods
     */
 
    public void setPkWmTicketId(int n) { mnPkWmTicketId = n; }
    public void setTicketId(int n) { mnTicketId = n; }
    public void setTicketDatetimeArrival(Date t) { mtTicketDatetimeArrival = t; }
    public void setTicketDatetimeDeparture(Date t) { mtTicketDatetimeDeparture = t; }
    public void setCompany(String s) { msCompany = s; }
    public void setDriverName(String s) { msDriverName = s; }
    public void setVehiclePlate(String s) { msVehiclePlate = s; }
    public void setWeightArrival(double d) { mdWeightArrival = d; computeWeight(); }
    public void setWeightDeparture(double d) { mdWeightDeparture = d; computeWeight(); }
    //public void setWeight(double d) { mdWeight = d; } // weight is allways computed internally
    public void setWmInfoArrival(boolean b) { mbWmInfoArrival = b; }
    public void setWmInfoDeparture(boolean b) { mbWmInfoDeparture = b; }
    public void setTared(boolean b) { mbTared = b; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkWmTicketTypeId(int n) { mnFkWmTicketTypeId = n; }
    public void setFkWmItemId(int n) { mnFkWmItemId = n; }
    public void setFkUserTareId(int n) { mnFkUserTareId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserTare(Date t) { mtTsUserTare = t; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }


    public int getPkWmTicketId() { return mnPkWmTicketId; }
    public int getTicketId() { return mnTicketId; }
    public Date getTicketDatetimeArrival() { return mtTicketDatetimeArrival; }
    public Date getTicketDatetimeDeparture() { return mtTicketDatetimeDeparture; }
    public String getCompany() { return msCompany; }
    public String getDriverName() { return msDriverName; }
    public String getVehiclePlate() { return msVehiclePlate; }
    public double getWeightArrival() { return mdWeightArrival; }
    public double getWeightDeparture() { return mdWeightDeparture; }
    public double getWeight() { return mdWeight; }
    public boolean isWmInfoArrival() { return mbWmInfoArrival; }
    public boolean isWmInfoDeparture() { return mbWmInfoDeparture; }
    public boolean isTared() { return mbTared; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkWmTicketTypeId() { return mnFkWmTicketTypeId; }
    public int getFkWmItemId() { return mnFkWmItemId; }
    public int getFkUserTareId() { return mnFkUserTareId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserTare() { return mtTsUserTare; }
    public Date getTsUserClosed() { return mtTsUserClosed; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    


    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkWmTicketId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkWmTicketId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkWmTicketId = 0;
        mnTicketId = 0;
        mtTicketDatetimeArrival = null;
        mtTicketDatetimeDeparture = null;
        msCompany = "";
        msDriverName = "";
        msVehiclePlate = "";
        mdWeightArrival = 0;
        mdWeightDeparture = 0;
        mdWeight = 0;
        mbWmInfoArrival = false;
        mbWmInfoDeparture = false;
        mbTared = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkWmTicketTypeId = 0;
        mnFkWmItemId = 1;
        mnFkUserTareId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserTare = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_wm_ticket = " + mnPkWmTicketId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_wm_ticket = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkWmTicketId = 0;

        msSql = "SELECT COALESCE(MAX(id_wm_ticket), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkWmTicketId = resultSet.getInt(1);
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
            mnPkWmTicketId = resultSet.getInt("id_wm_ticket");
            mnTicketId = resultSet.getInt("ticket_id");
            mtTicketDatetimeArrival = resultSet.getDate("ticket_dt_arr");
            mtTicketDatetimeDeparture = resultSet.getDate("ticket_dt_dep");
            msCompany = resultSet.getString("company");
            msDriverName = resultSet.getString("driver_name");
            msVehiclePlate = resultSet.getString("vehic_plate");
            mdWeightArrival = resultSet.getDouble("weight_arr");
            mdWeightDeparture = resultSet.getDouble("weight_dep");
            mdWeight = resultSet.getDouble("weight");
            mbWmInfoArrival = resultSet.getBoolean("b_wm_arr");
            mbWmInfoDeparture = resultSet.getBoolean("b_wm_dep");
            mbTared = resultSet.getBoolean("b_tared");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkWmTicketTypeId = resultSet.getInt("fk_wm_ticket_tp");
            mnFkWmItemId = resultSet.getInt("fk_wm_item");
            mnFkUserTareId = resultSet.getInt("fk_usr_tare");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserTare = resultSet.getDate("ts_usr_tare");
            mtTsUserInsert = resultSet.getDate("ts_usr_ins");
            mtTsUserUpdate = resultSet.getDate("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {       
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        if (mbTared) {
            mnFkUserTareId = session.getUser().getPkUserId();
        }

        if (mnFkUserTareId == SLibConsts.UNDEFINED) {
            mnFkUserTareId = SUtilConsts.USR_NA_ID;
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkWmTicketId + ", " + 
                    mnTicketId + ", " + 
                    "'" + mtTicketDatetimeArrival + "'" + ", " + 
                    "'" + mtTicketDatetimeDeparture + "'" + ", " + 
                    "'" + msCompany + "', " + 
                    "'" + msDriverName + "', " + 
                    "'" + msVehiclePlate + "', " + 
                    mdWeightArrival + ", " + 
                    mdWeightDeparture + ", " + 
                    mdWeight + ", " + 
                    (mbWmInfoArrival ? 1 : 0) + ", " + 
                    (mbWmInfoDeparture ? 1 : 0) + ", " + 
                    (mbTared ? 1 : 0) + ", " + 
                    (mbClosed ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkWmTicketTypeId + ", " + 
                    mnFkWmItemId + ", " + 
                    mnFkUserTareId + ", " + 
                    mnFkUserClosedId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", '" + 
                    mtTsUserUpdate + "' " + 
                     ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_wm_ticket = " + mnPkWmTicketId + ", " +
                    "ticket_id = " + mnTicketId + ", " +
                    "ticket_dt_arr = '" + mtTicketDatetimeArrival + "', " +
                    "ticket_dt_dep = '" + mtTicketDatetimeDeparture + "', " +
                    "company = '" + msCompany + "', " +
                    "driver_name = '" + msDriverName + "', " +
                    "vehic_plate = '" + msVehiclePlate + "', " +
                    "weight_arr = " + mdWeightArrival + ", " +
                    "weight_dep = " + mdWeightDeparture + ", " +
                    "weight = " + mdWeight + ", " +
                    "b_wm_arr = " + (mbWmInfoArrival ? 1 : 0) + ", " +
                    "b_wm_dep = " + (mbWmInfoDeparture ? 1 : 0) + ", " +
                    "b_tared = " + (mbTared ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_wm_ticket_tp = " + mnFkWmTicketTypeId + ", " +
                    "fk_wm_item = " + mnFkWmItemId + ", " +
                    "fk_usr_tare = " + mnFkUserTareId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_tare = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = '" + mtTsUserUpdate + "' " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbWmTicket clone() throws CloneNotSupportedException {
        SDbWmTicket registry = new SDbWmTicket();

        registry.setPkWmTicketId(this.getPkWmTicketId());
        registry.setTicketId(this.getTicketId());
        registry.setTicketDatetimeArrival(this.getTicketDatetimeArrival());
        registry.setTicketDatetimeDeparture(this.getTicketDatetimeDeparture());
        registry.setCompany(this.getCompany());
        registry.setDriverName(this.getDriverName());
        registry.setVehiclePlate(this.getVehiclePlate());
        registry.setWeightArrival(this.getWeightArrival());
        registry.setWeightDeparture(this.getWeightDeparture());
        //registry.setWeight(this.getWeight()); // weight is allways computed internally
        registry.setWmInfoArrival(this.isWmInfoArrival());
        registry.setWmInfoDeparture(this.isWmInfoDeparture());
        registry.setTared(this.isTared());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkWmTicketTypeId(this.getFkWmTicketTypeId());
        registry.setFkWmItemId(this.getFkWmItemId());
        registry.setFkUserTareId(this.getFkUserTareId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserTare(this.getTsUserTare());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

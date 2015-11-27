/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

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
 * @author Sergio Flores
 */
public class SDbConfigAvista extends SDbRegistryUser {

    protected int mnPkConfigAvistaId;
    protected String msAvistaHost;
    protected int mnAvistaPort;
    protected String msAvistaName;
    protected String msAvistaUser;
    protected String msAvistaPassword;
    protected int mnDesLocalCountryFk;
    protected int mnDesLocalCurrencyFk;
    protected int mnDesDefaultItemGenericFk;
    protected String msDesItemCodePrefix;
    protected String msSrcLocalCountryFk;
    protected String msSrcLocalStateFk;
    protected int mnSrcLocalCurrencyFk;
    protected int mnSrcDefaultCurrencyFk;
    protected String msSrcDefaultUnitOfMeasureFk;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkSrcLocalCurrencyId;
    protected int mnFkSrcDefaultCurrencyId;
    protected int mnFkSrcDefaultUnitOfMeasureId;
    protected int mnFkDesDefaultPayMethodId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbConfigAvista() {
        super(SModConsts.A_CFG);
    }

    /*
     * Public methods
     */

    public void setPkConfigAvistaId(int n) { mnPkConfigAvistaId = n; }
    public void setAvistaHost(String s) { msAvistaHost = s; }
    public void setAvistaPort(int n) { mnAvistaPort = n; }
    public void setAvistaName(String s) { msAvistaName = s; }
    public void setAvistaUser(String s) { msAvistaUser = s; }
    public void setAvistaPassword(String s) { msAvistaPassword = s; }
    public void setDesLocalCountryFk(int n) { mnDesLocalCountryFk = n; }
    public void setDesLocalCurrencyFk(int n) { mnDesLocalCurrencyFk = n; }
    public void setDesDefaultItemGenericFk(int n) { mnDesDefaultItemGenericFk = n; }
    public void setDesItemCodePrefix(String s) { msDesItemCodePrefix = s; }
    public void setSrcLocalCountryFk(String s) { msSrcLocalCountryFk = s; }
    public void setSrcLocalStateFk(String s) { msSrcLocalStateFk = s; }
    public void setSrcLocalCurrencyFk(int n) { mnSrcLocalCurrencyFk = n; }
    public void setSrcDefaultCurrencyFk(int n) { mnSrcDefaultCurrencyFk = n; }
    public void setSrcDefaultUnitOfMeasureFk(String s) { msSrcDefaultUnitOfMeasureFk = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkSrcLocalCurrencyId(int n) { mnFkSrcLocalCurrencyId = n; }
    public void setFkSrcDefaultCurrencyId(int n) { mnFkSrcDefaultCurrencyId = n; }
    public void setFkSrcDefaultUnitOfMeasureId(int n) { mnFkSrcDefaultUnitOfMeasureId = n; }
    public void setFkDesDefaultPayMethodId(int n) { mnFkDesDefaultPayMethodId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkConfigAvistaId() { return mnPkConfigAvistaId; }
    public String getAvistaHost() { return msAvistaHost; }
    public int getAvistaPort() { return mnAvistaPort; }
    public String getAvistaName() { return msAvistaName; }
    public String getAvistaUser() { return msAvistaUser; }
    public String getAvistaPassword() { return msAvistaPassword; }
    public int getDesLocalCountryFk() { return mnDesLocalCountryFk; }
    public int getDesLocalCurrencyFk() { return mnDesLocalCurrencyFk; }
    public int getDesDefaultItemGenericFk() { return mnDesDefaultItemGenericFk; }
    public String getDesItemCodePrefix() { return msDesItemCodePrefix; }
    public String getSrcLocalCountryFk() { return msSrcLocalCountryFk; }
    public String getSrcLocalStateFk() { return msSrcLocalStateFk; }
    public int getSrcLocalCurrencyFk() { return mnSrcLocalCurrencyFk; }
    public int getSrcDefaultCurrencyFk() { return mnSrcDefaultCurrencyFk; }
    public String getSrcDefaultUnitOfMeasureFk() { return msSrcDefaultUnitOfMeasureFk; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkSrcLocalCurrencyId() { return mnFkSrcLocalCurrencyId; }
    public int getFkSrcDefaultCurrencyId() { return mnFkSrcDefaultCurrencyId; }
    public int getFkSrcDefaultUnitOfMeasureId() { return mnFkSrcDefaultUnitOfMeasureId; }
    public int getFkDesDefaultPayMethodId() { return mnFkDesDefaultPayMethodId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkConfigAvistaId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConfigAvistaId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkConfigAvistaId = 0;
        msAvistaHost = "";
        mnAvistaPort = 0;
        msAvistaName = "";
        msAvistaUser = "";
        msAvistaPassword = "";
        mnDesLocalCountryFk = 0;
        mnDesLocalCurrencyFk = 0;
        mnDesDefaultItemGenericFk = 0;
        msDesItemCodePrefix = "";
        msSrcLocalCountryFk = "";
        msSrcLocalStateFk = "";
        mnSrcLocalCurrencyFk = 0;
        mnSrcDefaultCurrencyFk = 0;
        msSrcDefaultUnitOfMeasureFk = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkSrcLocalCurrencyId = 0;
        mnFkSrcDefaultCurrencyId = 0;
        mnFkSrcDefaultUnitOfMeasureId = 0;
        mnFkDesDefaultPayMethodId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cfg = " + mnPkConfigAvistaId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cfg = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConfigAvistaId = 0;

        msSql = "SELECT COALESCE(MAX(id_cfg), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConfigAvistaId = resultSet.getInt(1);
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
            mnPkConfigAvistaId = resultSet.getInt("id_cfg");
            msAvistaHost = resultSet.getString("avi_host");
            mnAvistaPort = resultSet.getInt("avi_port");
            msAvistaName = resultSet.getString("avi_name");
            msAvistaUser = resultSet.getString("avi_user");
            msAvistaPassword = resultSet.getString("avi_pswd");
            mnDesLocalCountryFk = resultSet.getInt("des_loc_cty_fk");
            mnDesLocalCurrencyFk = resultSet.getInt("des_loc_cur_fk");
            mnDesDefaultItemGenericFk = resultSet.getInt("des_def_itm_gen_fk");
            msDesItemCodePrefix = resultSet.getString("des_itm_code_pfx");
            msSrcLocalCountryFk = resultSet.getString("src_loc_cty_fk");
            msSrcLocalStateFk = resultSet.getString("src_loc_sta_fk");
            mnSrcLocalCurrencyFk = resultSet.getInt("src_loc_cur_fk");
            mnSrcDefaultCurrencyFk = resultSet.getInt("src_def_cur_fk");
            msSrcDefaultUnitOfMeasureFk = resultSet.getString("src_def_uom_fk");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkSrcLocalCurrencyId = resultSet.getInt("fk_src_loc_cur");
            mnFkSrcDefaultCurrencyId = resultSet.getInt("fk_src_def_cur");
            mnFkSrcDefaultUnitOfMeasureId = resultSet.getInt("fk_src_def_uom");
            mnFkDesDefaultPayMethodId = resultSet.getInt("fk_des_def_pay_met");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
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

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkConfigAvistaId + ", " + 
                    "'" + msAvistaHost + "', " + 
                    mnAvistaPort + ", " + 
                    "'" + msAvistaName + "', " + 
                    "'" + msAvistaUser + "', " + 
                    "'" + msAvistaPassword + "', " + 
                    mnDesLocalCountryFk + ", " + 
                    mnDesLocalCurrencyFk + ", " + 
                    mnDesDefaultItemGenericFk + ", " + 
                    "'" + msDesItemCodePrefix + "', " + 
                    "'" + msSrcLocalCountryFk + "', " + 
                    "'" + msSrcLocalStateFk + "', " + 
                    mnSrcLocalCurrencyFk + ", " + 
                    mnSrcDefaultCurrencyFk + ", " + 
                    "'" + msSrcDefaultUnitOfMeasureFk + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkSrcLocalCurrencyId + ", " + 
                    mnFkSrcDefaultCurrencyId + ", " + 
                    mnFkSrcDefaultUnitOfMeasureId + ", " + 
                    mnFkDesDefaultPayMethodId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_cfg = " + mnPkConfigAvistaId + ", " +
                    "avi_host = '" + msAvistaHost + "', " +
                    "avi_port = " + mnAvistaPort + ", " +
                    "avi_name = '" + msAvistaName + "', " +
                    "avi_user = '" + msAvistaUser + "', " +
                    "avi_pswd = '" + msAvistaPassword + "', " +
                    "des_loc_cty_fk = " + mnDesLocalCountryFk + ", " +
                    "des_loc_cur_fk = " + mnDesLocalCurrencyFk + ", " +
                    "des_def_itm_gen_fk = " + mnDesDefaultItemGenericFk + ", " +
                    "des_itm_code_pfx = '" + msDesItemCodePrefix + "', " +
                    "src_loc_cty_fk = '" + msSrcLocalCountryFk + "', " +
                    "src_loc_sta_fk = '" + msSrcLocalStateFk + "', " +
                    "src_loc_cur_fk = " + mnSrcLocalCurrencyFk + ", " +
                    "src_def_cur_fk = " + mnSrcDefaultCurrencyFk + ", " +
                    "src_def_uom_fk = '" + msSrcDefaultUnitOfMeasureFk + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_src_loc_cur = " + mnFkSrcLocalCurrencyId + ", " +
                    "fk_src_def_cur = " + mnFkSrcDefaultCurrencyId + ", " +
                    "fk_src_def_uom = " + mnFkSrcDefaultUnitOfMeasureId + ", " +
                    "fk_des_def_pay_met = " + mnFkDesDefaultPayMethodId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfigAvista clone() throws CloneNotSupportedException {
        SDbConfigAvista registry = new SDbConfigAvista();

        registry.setPkConfigAvistaId(this.getPkConfigAvistaId());
        registry.setAvistaHost(this.getAvistaHost());
        registry.setAvistaPort(this.getAvistaPort());
        registry.setAvistaName(this.getAvistaName());
        registry.setAvistaUser(this.getAvistaUser());
        registry.setAvistaPassword(this.getAvistaPassword());
        registry.setDesLocalCountryFk(this.getDesLocalCountryFk());
        registry.setDesLocalCurrencyFk(this.getDesLocalCurrencyFk());
        registry.setDesDefaultItemGenericFk(this.getDesDefaultItemGenericFk());
        registry.setDesItemCodePrefix(this.getDesItemCodePrefix());
        registry.setSrcLocalCountryFk(this.getSrcLocalCountryFk());
        registry.setSrcLocalStateFk(this.getSrcLocalStateFk());
        registry.setSrcLocalCurrencyFk(this.getSrcLocalCurrencyFk());
        registry.setSrcDefaultCurrencyFk(this.getSrcDefaultCurrencyFk());
        registry.setSrcDefaultUnitOfMeasureFk(this.getSrcDefaultUnitOfMeasureFk());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkSrcLocalCurrencyId(this.getFkSrcLocalCurrencyId());
        registry.setFkSrcDefaultCurrencyId(this.getFkSrcDefaultCurrencyId());
        registry.setFkSrcDefaultUnitOfMeasureId(this.getFkSrcDefaultUnitOfMeasureId());
        registry.setFkDesDefaultPayMethodId(this.getFkDesDefaultPayMethodId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author Alfredo Pérez
 */
public abstract class SSmsUtils2 extends SDbRegistryUser {

    private static Connection moConnectionRevuelta = null;
    private static Connection moConnectionSiie = null;
    private static String msTimeNow = "";
    private static String msInitialDate = "";

    /**
     * Start Revuelta DB connection
     */
    private static void startConnRev(SGuiSession session) throws SQLException {
        String sql = "SELECT rev_host, rev_port, rev_name, rev_user, rev_pswd FROM etla_com." + SModConsts.TablesMap.get(SModConsts.S_CFG) + ";";
        ResultSet rstEtla = getStatementEtla(session).executeQuery(sql);

        if (rstEtla.next()) {
//            moConnectionRevuelta = startConnectionRevuelta("192.168.1.33", "2638", "Revuelta", "usuario", "revuelta");
            moConnectionRevuelta = startConnectionRevuelta(rstEtla.getString("rev_host"), rstEtla.getString("rev_port"),
                    rstEtla.getString("rev_name"), rstEtla.getString("rev_user"), rstEtla.getString("rev_pswd"));
        }
    }

    /**
     *
     * Start SIIE DB connection.
     */
    private static void startConnSiie(SGuiSession session) throws SQLException {
        String sql = "SELECT sie_host, sie_port, sie_name, sie_user, sie_pswd  FROM etla_com." + SModConsts.TablesMap.get(SModConsts.C_CFG) + ";";
        ResultSet rstEtla = getStatementEtla(session).executeQuery(sql);
        if (rstEtla.next()) {
            moConnectionSiie = startConnectionSiie(rstEtla.getString("sie_host"), rstEtla.getString("sie_port"),
                    rstEtla.getString("sie_name"), rstEtla.getString("sie_user"), rstEtla.getString("sie_pswd"));
        }
    }

    public SSmsUtils2(int type) {
        super(type);
    }

    /**
     *
     * Search id_wm_item FROM pro_id string value.
     *
     * @param session
     * @param itemID
     * @return id_wm_item
     * @throws SQLException
     * @throws Exception
     */
    private static int searchIdItem(SGuiSession session, String itemID) throws SQLException, Exception {
        String sql = "";
        sql = "SELECT id_wm_item FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_ITEM) + " WHERE prod_id = '" + itemID + "';";
        ResultSet resultsetETLA = getStatementEtla(session).executeQuery(sql);
        if (resultsetETLA.next()) {
            return resultsetETLA.getInt("id_wm_item");
        } else {
            throw new Exception("Error, no se enconto el ID del ítem: " + itemID + "\n" + sql);
        }
    }

    public static void startImport(SGuiSession session) throws Exception {
        startConnSiie(session);
        startConnRev(session);
        createNow(session);
        createInitialDate(session);
        importItems(session);
        importDocs(session);
        importTickets(session);
        insertNow(session);
    }

    private static void importDocs(SGuiSession session) throws Exception {
        ResultSet oriValues = null;
        getStatementEtla(session);
        String sqlDesTemp = "";
        String id_year = "";
        String id_doc = "";
        String sqlToRegistry = "";
        ArrayList<Object> listOfIds = new ArrayList<>();

        oriValues = getOriValues(session, SSmsConsts.IMPORT_TYPE_DOC);
        // Buscar cada uno de los IDS del ORIGEN en el DESTINO.
        ResultSet resultSetSiie = null;
        ResultSet resulSetEtla = null;
        while (oriValues.next()) {
            id_year = oriValues.getString("id_year");
            id_doc = oriValues.getString("id_doc");
            listOfIds.add(id_year + "-" + id_doc);

            // Buscar cada uno de los IDS del ORIGEN en el DESTINO.
            sqlDesTemp = "SELECT s_erp_doc.id_erp_doc, s_erp_doc.erp_year_id, s_erp_doc.erp_doc_id, s_erp_doc.doc_upd FROM etla_com." + SModConsts.TablesMap.get(SModConsts.S_ERP_DOC) + " "
                    + "WHERE s_erp_doc.erp_year_id = " + id_year + " AND  s_erp_doc.erp_doc_id = " + id_doc;
            resulSetEtla = getStatementEtla(session).executeQuery(sqlDesTemp);
            sqlToRegistry = "SELECT "
                    + "dps.id_year, "
                    + "dps.id_doc, "
                    + "dps.fid_cl_dps, "
                    + "dps.fid_ct_dps, "
                    + "dps.num_ser, "
                    + "dps.num, "
                    + "dps.dt, "
                    + "bp.id_bp, "
                    + "bp.bp, "
                    + "sum(ety.weight_gross) AS weight_gross, "
                    + "dps.ts_edit, "
                    + "dps.b_del, "
                    + "dps.b_sys "
                    + "FROM trn_dps_ety AS ety " ////ADD MORE CONST ???
                    + "INNER JOIN trn_dps AS dps on ety.id_doc = dps.id_doc "
                    + "INNER JOIN erp.bpsu_bp AS BP ON dps.fid_bp_r = bp.id_bp "
                    + "WHERE dps.id_year = " + id_year + " AND dps.id_doc = " + id_doc + " AND NOT dps.b_del AND NOT ety.b_del group by id_year;";
            resultSetSiie = getStatementSiie().executeQuery(sqlToRegistry);
            if (resulSetEtla.next()) {
                //if is the same date, all its OK
                if (!resulSetEtla.getTimestamp("doc_upd").toString().equals(oriValues.getTimestamp("ts_edit").toString())) {
                    setRegistry(session, SSmsConsts.IMPORT_TYPE_DOC, !SSmsConsts.IS_NEW_REGISTRY, resultSetSiie);
                }
            } else {
                // If the origin row need to add in destiny.
                setRegistry(session, SSmsConsts.IMPORT_TYPE_DOC, SSmsConsts.IS_NEW_REGISTRY, resultSetSiie);
            }
        }
    }

    private static void importItems(SGuiSession session) throws Exception {
        Statement stmEtla = getStatementEtla(session);
        ResultSet rstRev = null;
        ResultSet rstEtla = null;
        String sql;
        rstRev = getOriValues(session, SSmsConsts.IMPORT_TYPE_ITEM);
        while (rstRev.next()) {
            sql = "SELECT id_wm_item FROM su_wm_item WHERE prod_id = '" + rstRev.getString("Pro_ID")
                    + "' AND code = '" + rstRev.getString("Pro_ID") + "' AND name = '" + rstRev.getString("Pro_Nombre") + "';";
            rstEtla = stmEtla.executeQuery(sql);
            if (!rstEtla.next()) {
//                rstEtla.first();
                setRegistry(session, SSmsConsts.IMPORT_TYPE_ITEM, SSmsConsts.IS_NEW_REGISTRY, rstRev);
            } else {
                //do nothing
                //setRegistry(session, SSmsConsts.IMPORT_TYPE_ITEM, !SSmsConsts.IS_NEW_REGISTRY, rstRev);
            }
        }
    }

    private static void importTickets(SGuiSession session) throws Exception {
        Statement stmRev = getStatementRevuelta();
        Statement stmEtla = getStatementEtla(session);
        ResultSet oriValues = null;
        ResultSet resulSetEtla = null;
        ResultSet resultSetRev = null;
        String id, sqlDesTemp, sqlToRegistry;

        oriValues = getOriValues(session, SSmsConsts.IMPORT_TYPE_TIC);
        while (oriValues.next()) {
            id = oriValues.getString("Pes_id");
            // Buscar cada uno de los id's del ORIGEN en el DESTINO.
            // Search each one Origyn id in destiny DB.
            sqlDesTemp = "SELECT ts_usr_upd FROM " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " WHERE ticket_id = " + id + ";";
            resulSetEtla = stmEtla.executeQuery(sqlDesTemp);
            sqlToRegistry = "SELECT Pes_ID, "
                    + "Pes_FecHorPri, "
                    + "Pes_FecHorSeg, "
                    + "Emp_Nombre, "
                    + "Pes_Chofer, "
                    + "Pes_Placas, "
                    + "Pes_PesoPri, "
                    + "Pes_PesoSeg, "
                    + "Pes_Bruto, "
                    + "Pes_FecHor, "
                    + "Pro_ID "
                    + "FROM dba.Pesadas "
                    + "WHERE Pes_ID = " + id + ";";

            resultSetRev = stmRev.executeQuery(sqlToRegistry);
            if (resulSetEtla.next()) {//HERE
                //if is the same date, all its OK
                if (!resulSetEtla.getTimestamp("ts_usr_upd").toString().equals(oriValues.getTimestamp("Pes_FecHor").toString())) {
                    setRegistry(session, SSmsConsts.IMPORT_TYPE_TIC, !SSmsConsts.IS_NEW_REGISTRY, resultSetRev);
                }
            } else {
                // If the origin row need to add in destiny.
                setRegistry(session, SSmsConsts.IMPORT_TYPE_TIC, SSmsConsts.IS_NEW_REGISTRY, resultSetRev);
            }
        }
    }

    /**
     * Creation of now() in sql for update late when import is completed.
     *
     * @param session
     * @throws SQLException
     */
    private static void createNow(SGuiSession session) throws SQLException {
        ResultSet rst = null;
        rst = getStatementEtla(session).executeQuery("SELECT NOW()");
        if (rst.next()) {
            setTimeNow(rst.getString(1));
        }
    }

    /**
     * Creation of initial date for import data. --Set value of initial date
     * from data base
     *
     * @param session
     * @throws SQLException
     *
     */
    private static void createInitialDate(SGuiSession session) throws Exception {
        ResultSet rst = null;
        rst = getStatementEtla(session).executeQuery("SELECT ts_etl_low_lim FROM " + SModConsts.TablesMap.get(SModConsts.S_ERP_DOC_ETL_LOG) + "");
        if (rst.next()) { 
            setInitialDate(rst.getString(1));
        } else {
            throw new Exception("Error trying to get initial date for data extraction...");
        }
    }

    /**
     * Update the initial date, change the ts_etl_end for the ts_etl_low_lim and
     * insert a new 'now()' for ts_etl_end date.
     *
     * @param session
     * @throws SQLException
     */
    private static void insertNow(SGuiSession session) throws SQLException {
        String sqlTemp;
        sqlTemp = "UPDATE etla_com.S_ERP_DOC_ETL_LOG SET ts_etl_low_lim = ts_etl_end;";
        if (getStatementEtla(session).executeUpdate(sqlTemp) == 0) {
            throw new SQLException("FIRST UPDATE ERROR: " + sqlTemp);
        }
        sqlTemp = "UPDATE etla_com." + SModConsts.TablesMap.get(SModConsts.S_ERP_DOC_ETL_LOG) + " SET ts_etl_end = '" + getTimeNow() + "';";
        if (getStatementEtla(session).executeUpdate(sqlTemp) == 0) {
            throw new SQLException("SECOND UPDATE ERROR: " + sqlTemp);
        }
    }

    private static Connection startConnectionRevuelta(final String rev_host, final String rev_port, final String rev_name, final String rev_user, final String rev_pswd) {

        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver");

            String url = "jdbc:sybase:Tds:" + rev_host + ":" + rev_port + "/" + rev_name;
            url = "jdbc:sybase:Tds:192.168.1.33:2638/Revuelta";
            Properties prop = new Properties();
            prop.put("user", rev_user);
            prop.put("password", rev_pswd);
            moConnectionRevuelta = DriverManager.getConnection(url, prop);
        } catch (ClassNotFoundException | SQLException e) {
            throw new Error(e);
        }

        return moConnectionRevuelta;
    }

    /**
     *
     * @return connection etla_com
     */
    public static java.sql.Connection openConnectionDestino() {
        String DB = "etla_com";
        String url = "jdbc:mysql://localhost:3306/" + DB;
        String username = "root";
        String password = "msroot";
        java.sql.Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
//            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        return connection;
    }
    
    public static Connection startConnectionSiie(final String siie_host, final String siie_port, final String siie_name, final String siie_user, final String siie_pswd) {
        //Cartro 10.83.32.129:3306
        //Localhost 127.0.0.1:3306
        //ACT 192.16.1.16/233:3306

        String url = "jdbc:mysql://" + siie_host + ":" + siie_port + "/" + siie_name;

//        System.out.println("Connecting database [" + siie_name + "] ...");
        java.sql.Connection connection;
        try {
            connection = DriverManager.getConnection(url, siie_user, siie_pswd);
//            System.out.println("Database " + siie_name + " connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        return connection;
    }

    public static String getInitialDate() {
        return msInitialDate;
    }

    public static Statement getStatementEtla(final SGuiSession session) {
        try {
            return session.getDatabase().getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(SSmsUtils2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Statement getStatementSiie() {
        try {
            return getConnectionSiie().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(SSmsUtils2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @return 
     */
    private static Statement getStatementRevuelta() {
        try {
            return getConnectionRev().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(SSmsUtils2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Get all the rows values from the database origin
     *
     * @param session
     * @param type
     * @return
     * @throws Exception
     */
    private static ResultSet getOriValues(SGuiSession session, int type) throws Exception {
        Statement stmRev = getStatementRevuelta();
        ResultSet rstRev = null;
        String sql = "";

        switch (type) {
            case SSmsConsts.IMPORT_TYPE_DOC: //documento
                String erpCompany = "";
                sql = "SELECT sie_name FROM " + SModConsts.TablesMap.get(SModConsts.C_CFG) + "";
                Statement stmEtla = getStatementEtla(session);
                ResultSet rstEtla = null;
                rstEtla = stmEtla.executeQuery(sql);

                if (rstEtla.next()) {
                    erpCompany = rstEtla.getString("sie_name");
                } else {
                    throw new Exception("Error, no se enconto el nombre de la empresa. SQL: " + sql + ".");
                }

                Statement stmSiie = getStatementSiie();

                sql = "SELECT trn_dps.id_year, trn_dps.id_doc, trn_dps.ts_edit FROM " + erpCompany + ".trn_dps "
                        + "WHERE NOT b_del AND fid_st_dps = " + SSmsConsts.ST_DPS_EMITTED + " AND trn_dps.dt >= '" + getInitialDate() + "' AND (fid_cl_dps = " + SSmsConsts.DOC_TYPE_CN_NUM + " OR fid_cl_dps = " + SSmsConsts.DOC_TYPE_INV_NUM + ");";

                return stmSiie.executeQuery(sql);

            case SSmsConsts.IMPORT_TYPE_TIC: //boleto
                stmRev = getStatementRevuelta();
                sql = "SELECT Pes_ID, "
                        + "Pes_FecHor "
                        + "FROM dba.Pesadas WHERE Pes_Completo = 1 AND Pes_FecHor > '" + getInitialDate() + "' AND Emp_ID = 'P00139';";

                rstRev = stmRev.executeQuery(sql);

                return rstRev;

            case SSmsConsts.IMPORT_TYPE_ITEM: //item
                stmRev = getStatementRevuelta();
                sql = "SELECT Pro_ID, Pro_Nombre FROM dba.Productos";
                rstRev = stmRev.executeQuery(sql);

                return rstRev;

            default:
        }
        return null;
    }

    /**
     *
     * @return Return connection with DB SIIE
     */
    private static Connection getConnectionSiie() {
        return moConnectionSiie;
    }

    /**
     *
     * @return Return connection with Revuelta
     */
    private static Connection getConnectionRev() {
        return moConnectionRevuelta;
    }

    public static String getTimeNow() {
        return msTimeNow;
    }

    /*
     * ALL SETTER's
     */

    /**
     * Set msTimeNow String value.
     *
     * @param s DateTime to string for future 'WHERE' condition in SQL.
     */
    public static void setTimeNow(String s) {
        SSmsUtils2.msTimeNow = s;
    }

    public static void setInitialDate(String s) {
        msInitialDate = s;
    }

    /**
     *
     * @param session
     * @param type valid values declared SModConsts: S_ERP_DOC | S_WM_TICKET
     * @param docType valid values declared in SModSysConsts: SX_INV_SAL... | SX_INV_PUR... | SX_CN_SAL... | SX_CN_PUR... | 
     * @param ticType valid values declared in SModSysConsts: SS_WM_TICKET_TP_IN | SS_WM_TICKET_TP_OUT
     * @return ArrayList SRowWmLinkRow
     * @throws Exception
     */
    public static ArrayList<SRowWmLinkRow> createWmLinkRows(final SGuiSession session, final int type, final int docType, final int ticType) throws Exception {
//        Connection moConecctionDest = SSmsUtils.openConnectionDestino();
        Connection moConecctionDest = session.getDatabase().getConnection();
        Statement stm = moConecctionDest.createStatement();
        ResultSet rst = null;
        ArrayList<SRowWmLinkRow> availableRows = new ArrayList<>();
        SRowWmLinkRow moRow = null;

        SDbErpDoc registryDoc = new SDbErpDoc();
        SDbWmTicket registryTic = new SDbWmTicket();

        String sql = "";
        String filterType = "";

        switch (type) {
            case SModConsts.S_ERP_DOC:
                switch (docType) {
                    case SModSysConsts.SX_INV_PUR:
                    case SModSysConsts.SX_CN_SAL:
                        filterType = "fk_wm_ticket_tp = " + SModSysConsts.SS_WM_TICKET_TP_IN + " ";
                        break;
                    case SModSysConsts.SX_INV_SAL:
                    case SModSysConsts.SX_CN_PUR:
                        filterType = "fk_wm_ticket_tp = " + SModSysConsts.SS_WM_TICKET_TP_OUT + " ";
                        break;
                    default:
                }

                sql = "SELECT "
                        + "tt.name AS Tipo, "
                        + "t.ticket_id AS Folio, "
                        + "t.ticket_dt_arr AS date_a, "
                        + "t.ticket_dt_dep AS date_d, "
                        + "t.company AS bp, "
                        + "SUM(l.weight_link) AS W_linked, "
                        + "IF((t.weight - SUM(l.weight_link)) IS NULL, t.weight,(t.weight - SUM(l.weight_link))) AS Remainder, "
                        + "COUNT(l.weight_link) AS 'Links', "
                        + "t.weight AS W_total, "
                        + "t.id_wm_ticket "
                        + "FROM s_wm_ticket AS t "
                        + "LEFT OUTER JOIN s_wm_ticket_link AS l ON l.fk_wm_ticket = t.id_wm_ticket "
                        + "INNER JOIN ss_wm_ticket_tp AS tt ON t.fk_wm_ticket_tp = tt.id_wm_ticket_tp "
                        + "WHERE " + filterType
                        + "GROUP BY t.id_wm_ticket "
                        + "HAVING SUM(weight_link) < t.weight OR SUM(weight_link) IS NULL " // Pendientes.
                        //+ "#HAVING SUM(weight_link) >= t.weight " // Completados o superados.
                        + "ORDER BY id_wm_ticket, fk_erp_doc DESC;";

                break;

            case SModConsts.S_WM_TICKET:
                switch (ticType) {
                    case SModSysConsts.SS_WM_TICKET_TP_IN:
                        filterType = "(doc_type = 'INV' AND doc_class = 'INC') OR (doc_type = 'CN' AND doc_class = 'EXP')";
                        break;
                    case SModSysConsts.SS_WM_TICKET_TP_OUT:
                        filterType = "(doc_type = 'INV' AND doc_class = 'EXP') OR (doc_type = 'CN' AND doc_class = 'INC') ";
                        break;
                    default:
                        break;
                }

                sql = "SELECT "
                        + "CONCAT (IF(d.doc_type = '" + SDbErpDoc.TYPE_INV + "', 'FACTURA','NOTA DE CREDITO'),IF(d.doc_class = '" + SDbErpDoc.CLASS_INC + "', ' ENTRADA',' SALIDA')) AS Tipo,"
                        + "CONCAT(d.doc_ser, IF(d.doc_ser = '', '','-'), d.doc_num) AS Folio, "
                        + "d.doc_date AS Fecha, "
                        + "d.biz_partner AS Bp, "
                        + "d.weight AS W_total, "
                        + "SUM(weight_link) AS W_linked, "
                        + "IF((d.weight - SUM(weight_link)) IS NULL, d.weight,(d.weight - SUM(weight_link))) AS Remainder, "
                        + "COUNT(weight_link) AS Links, "
                        + "d.id_erp_doc "
                        + "FROM s_erp_doc AS d "
                        + "LEFT OUTER JOIN s_wm_ticket_link AS l ON l.fk_erp_doc = d.id_erp_doc "
                        + "WHERE " + filterType
                        + "GROUP BY d.id_erp_doc "
                        + "HAVING SUM(weight_link) < d.weight OR SUM(weight_link) IS NULL " // Pendientes.
                        //+ "#HAVING SUM(weight_link) >= d.weight "// Completados o superados.
                        + "ORDER BY d.doc_type, d.doc_class, biz_partner, fk_erp_doc;";
                break;

            default:
        }

        stm.execute(sql);

        rst = stm.getResultSet();

        int mnId = 0;

        while (rst.next()){ 

            switch (type) {
                case SModConsts.S_WM_TICKET:
                    mnId = rst.getInt("id_erp_doc");
                    registryDoc = new SDbErpDoc();
                    registryDoc.read(session, new int[]{mnId});
                    moRow = new SRowWmLinkRow(registryDoc, SRowWmLinkRow.SUBTYPE_TO_LINK);
                    break;
                case SModConsts.S_ERP_DOC:
                    mnId = rst.getInt("id_wm_ticket");
                    registryTic = new SDbWmTicket();
                    registryTic.read(session, new int[]{mnId});
                    moRow = new SRowWmLinkRow(registryTic, SRowWmLinkRow.SUBTYPE_TO_LINK);
                    break;
                default:
            }

            availableRows.add(moRow);
        }

        return availableRows;
    }

    /**
     * SET REGISTRY TO SAVE IN DB
     *
     * @param session SGuiSession
     * @param type SSmsConsts: IMPORT_TYPE_DOC | IMPORT_TYPE_TIC |
     * IMPORT_TYPE_ITEM
     * @param isNew
     * @param resultSetValue
     * @throws SQLException
     * @throws Exception
     */
    private static void setRegistry(SGuiSession session, int type, boolean isNew, ResultSet resultSetValue) throws SQLException, Exception {
        switch (type) {
            case SSmsConsts.IMPORT_TYPE_DOC: //Documento
                if (resultSetValue.next()) {
                    SDbErpDoc registryDoc = new SDbErpDoc();

                    registryDoc.setRegistryNew(isNew);

                    registryDoc.setErpYearId(resultSetValue.getInt("id_year"));
                    registryDoc.setErpDocId(resultSetValue.getInt("id_doc"));
                    registryDoc.setDocClass(resultSetValue.getString("fid_cl_dps").equalsIgnoreCase("" + SSmsConsts.DOC_CLASS_INC_NUM + "") ? "INC" : "EXP");
                    registryDoc.setDocType(resultSetValue.getString("fid_ct_dps").equalsIgnoreCase("" + SSmsConsts.DOC_TYPE_INV_NUM + "") ? "INV" : "CN");
                    registryDoc.setDocSeries(resultSetValue.getString("num_ser"));
                    registryDoc.setDocNumber(resultSetValue.getString("num"));
                    registryDoc.setDocDate(resultSetValue.getDate("dt"));
                    registryDoc.setBizPartnerId(resultSetValue.getInt("id_bp"));
                    registryDoc.setBizPartner(resultSetValue.getString("bp"));
                    registryDoc.setWeight(resultSetValue.getDouble("weight_gross"));
                    registryDoc.setDocUpdate(resultSetValue.getTimestamp("ts_edit"));
                    registryDoc.setClosed(false);
                    registryDoc.setDeleted(false);
                    registryDoc.setSystem(false);
                    registryDoc.setFkUserClosedId(sa.gui.util.SUtilConsts.USR_NA_ID);
                    registryDoc.setFkUserInsertId(sa.gui.util.SUtilConsts.USR_NA_ID);
                    registryDoc.setFkUserUpdateId(sa.gui.util.SUtilConsts.USR_NA_ID);
//                    registryDoc.setTsUserClosed(null);// Now();
//                    registryDoc.setTsUserInsert(null);// Now();
//                    registryDoc.setTsUserUpdate(null);// Now();

                    registryDoc.save(session);
                }
                break;
            case SSmsConsts.IMPORT_TYPE_TIC: //Boleto
                if (resultSetValue.next()) {
                    SDbWmTicket registrytic = new SDbWmTicket();

                    registrytic.setRegistryNew(isNew);

                    registrytic.setTicketId(resultSetValue.getInt("Pes_id"));
                    registrytic.setTicketDatetimeArrival(resultSetValue.getDate("Pes_FecHorPri"));
                    registrytic.setTicketDatetimeDeparture(resultSetValue.getDate("Pes_FecHorSeg"));
                    registrytic.setCompany(resultSetValue.getString("Emp_nombre"));
                    registrytic.setDriverName(resultSetValue.getString("Pes_Chofer"));
                    registrytic.setVehiclePlate(resultSetValue.getString("Pes_Placas"));
                    registrytic.setWeightArrival(resultSetValue.getDouble("Pes_PesoPri"));
                    registrytic.setWeightDeparture(resultSetValue.getDouble("Pes_PesoSeg"));
                    //registrytic.setWeight(resultSetValue.getDouble("Pes_Bruto"));//Calculated value
                    registrytic.setWmInfoArrival(false);
                    registrytic.setWmInfoDeparture(false);
                    registrytic.setTared(false);
                    registrytic.setClosed(false);
                    registrytic.setDeleted(false);
                    registrytic.setSystem(false);
                    registrytic.setFkWmTicketTypeId(resultSetValue.getDouble("Pes_PesoPri") < resultSetValue.getDouble("Pes_PesoSeg") ? 2 : 1);//Entrada = 1; Salida = 2;
                    registrytic.setFkWmItemId(searchIdItem(session, resultSetValue.getString("Pro_Id")));
                    registrytic.setFkUserTareId(1);
                    registrytic.setFkUserClosedId(1);
                    registrytic.setFkUserInsertId(1);
                    registrytic.setFkUserUpdateId(1);
                    //registrytic.setTsUserTare(now());
                    //registrytic.setTsUserClosed(now());
                    //registrytic.setTsUserInsert(now());
                    registrytic.setTsUserUpdate(resultSetValue.getTimestamp("Pes_FecHor"));

                    registrytic.save(session);
                }
                break;
            case SSmsConsts.IMPORT_TYPE_ITEM: //Ítem
                SDbWmItem registryItem = new SDbWmItem();

                registryItem.setRegistryNew(isNew);

                registryItem.setProductId(resultSetValue.getString("Pro_Id"));
                registryItem.setCode(resultSetValue.getString("Pro_Id"));
                registryItem.setName(resultSetValue.getString("Pro_Nombre"));

                registryItem.save(session);

                break;
            default:
                break;
        }
    }
}

/*
 *
 * NO SIRVE ESTE ARCHIVO, SE REEMPLAZO POR SSmsUtils2.java
 *
 */
package etla.mod.sms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Alfredo Pérez
 */
public abstract class SSmsUtils extends SDbRegistryUser {

    private static Connection moConecctionOrigen = null;//SSmsUtils.openConnectionOrigen();
    private static Connection moConecctionDest = SSmsUtils.openConnectionRevuelta("192.168.1.33", "2638", "Revuelta", "revuelta", "usuario");
    
    public SDbWmTicket moRegistryTic = new SDbWmTicket();

    /**
     * Obtains available shipment deliveries from external system as shipment
     * rows.
     *
     * @param session Current user session.
     * @param connection Database connection to external system.
     * @param date Date to filter available shipment deliveries.
     * @return Available rows.
     * @throws SQLException
     * @throws Exception
     */
    public static ArrayList<SRowWmLinkRow> obtainAvailableRows(final SGuiSession session, final Connection connection, final Date date) throws SQLException, Exception {
        ArrayList<SRowWmLinkRow> availableRows = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        String sql = "SELECT  "
                + "d.id_erp_doc, "
                + "d.doc_ser, "
                + "d.doc_num, "
                + "d.doc_date, "
                + "d.biz_partner, "
                + "d.weight "
                + "FROM "
                + "s_wm_ticket_link AS l "
                + "RIGHT OUTER JOIN "
                + "s_erp_doc AS d ON l.fk_erp_doc = d.id_erp_doc "
                + "WHERE "
                + "l.fk_erp_doc IS NULL AND NOT d.b_del "
                //                + "AND " + SLibUtils.DbmsDateFormatDate.format(date)
                + "AND (YEAR(d.doc_date) = 2008 "
                + "AND MONTH(d.doc_date) = 7) ORDER BY d.id_erp_doc LIMIT 1;";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {

            SDbErpDoc row = new SDbErpDoc();

            row.setPkErpDocId(resultSet.getInt("id_erp_doc"));
            row.setDocSeries(resultSet.getString("doc_ser"));
            row.setDocNumber(resultSet.getString("doc_num"));
            row.setDocDate(resultSet.getDate("doc_date"));
            row.setBizPartner(resultSet.getString("biz_partner"));
            row.setWeight(resultSet.getDouble("weight"));

            availableRows.add(new SRowWmLinkRow(row, SRowWmLinkRow.SUBTYPE_TO_LINK));

        }

        return availableRows;
    }

    /**
     * Obtain conexion with system Revuelta using JDBC jconn3 Driver
     *
     * @param rev_host
     * @param rev_port
     * @param rev_name
     * @param rev_pswd
     * @param rev_user
     * @return Connection Default Params (Uppercase sensitivity): default
     * rev_host 192.168.1.33 AETH default rev_host 10.83.42.218 CARTRO default
     * rev_port 2638 default rev_name Revuelta default rev_pswd revuelta default
     * rev_user usuario
     */
    public static Connection openConnectionRevuelta(final String rev_host, final String rev_port, final String rev_name, final String rev_user, final String rev_pswd) {
        Connection connection = null;

        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver");

            String url = "jdbc:sybase:Tds:" + rev_host + ":" + rev_port + "/" + rev_name;
            Properties prop = new Properties();
            prop.put("user", rev_user);
            prop.put("password", rev_pswd);
            connection = DriverManager.getConnection(url, prop);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }

        return connection;
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

//    public static ArrayList<SRowWmLinkRow> createWmLinkRows(final SGuiSession session, final int type, final String docClass, final String docType, final int ticketType) throws Exception {
////        Connection moConecctionDest = SSmsUtils.openConnectionDestino();
//        Connection moConecctionDest = session.getDatabase().getConnection();
//        Statement stm = moConecctionDest.createStatement();
//        ResultSet rst = null;
//        ArrayList<SRowWmLinkRow> availableRows = new ArrayList<>();
//        SRowWmLinkRow moRow = null;
//
//        SDbErpDoc registryDoc = new SDbErpDoc();
//        SDbWmTicket registryTic = new SDbWmTicket();
//
//        String sql = "";
//        String filterType = "";
//        String orderBy = "";
//
//        switch (type) {
//            case SModConsts.S_ERP_DOC:
//                switch (docType) {
//                    case SSmsConsts.DOC_TYPE_INV:
//                        filterType = "doc_type = '" + SSmsConsts.DOC_TYPE_INV + "' ";
//                        break;
//                    case SSmsConsts.DOC_TYPE_CN:
//                        filterType = "doc_type = '" + SSmsConsts.DOC_TYPE_CN + "' ";
//                        break;
//                    default:
//                }
//                orderBy = "ORDER BY biz_partner, fk_erp_doc;";
//                switch (docClass) {
//                    case SSmsConsts.DOC_CLASS_INC:
//                        filterType += "AND doc_class = '" + SSmsConsts.DOC_CLASS_INC + "' ";
//                        break;
//                    case SSmsConsts.DOC_CLASS_EXP:
//                        filterType += "AND doc_class = '" + SSmsConsts.DOC_CLASS_EXP + "' ";
//                        break;
//                    default:
//                        break;
//                }
//
//                sql = "SELECT "
//                        + "d.id_erp_doc AS Id, "
//                        + "d.doc_date AS Fecha , "
//                        + "d.weight AS W_total, "
//                        + "d.biz_partner AS Name, "
//                        + "SUM(weight_link) AS W_linked, "
//                        + "COUNT(weight_link) AS Links, "
//                        + "IF((d.weight - SUM(weight_link)) IS NULL, d.weight,(d.weight - SUM(weight_link))) AS Remainder "
//                        + "FROM s_erp_doc AS d "
//                        + "LEFT OUTER JOIN s_wm_ticket_link AS l ON l.fk_erp_doc = d.id_erp_doc "
//                        + "WHERE " + filterType
//                        + "GROUP BY d.id_erp_doc "
//                        + "HAVING SUM(weight_link) < d.weight OR SUM(weight_link) IS NULL " // Pendientes.
//                        //+ "#HAVING SUM(weight_link) >= d.weight "// Completados o superados.
//                        + orderBy;
//                break;
//
//            case SModConsts.S_WM_TICKET:
//                switch (ticketType) {
//                    case SModSysConsts.SS_WM_TICKET_TP_IN:
//                        filterType = "fk_wm_ticket_tp = " + SModSysConsts.SS_WM_TICKET_TP_IN + " "; //ENTRADA
//                        break;
//                    case SModSysConsts.SS_WM_TICKET_TP_OUT:
//                        filterType = "fk_wm_ticket_tp = " + SModSysConsts.SS_WM_TICKET_TP_OUT + " "; //SALIDA
//                        break;
//                    default:
//                        break;
//                }
//                orderBy = "ORDER BY id_wm_ticket, fk_erp_doc DESC;";
//                sql = "SELECT "
//                        + "t.id_wm_ticket AS Id, "
//                        + "tt.name AS Name, "
//                        + "t.weight AS W_total, "
//                        + "SUM(l.weight_link) AS W_linked, "
//                        + "COUNT(l.weight_link) AS 'Links', "
//                        + "IF((t.weight - SUM(L.weight_link)) IS NULL, t.weight,(t.weight - SUM(l.weight_link))) AS Remainder "
//                        + "FROM s_wm_ticket AS t "
//                        + "LEFT OUTER JOIN s_wm_ticket AS l ON l.fk_wm_ticket = t.id_wm_ticket "
//                        + "INNER JOIN ss_wm_ticket_tp AS tt ON t.fk_wm_ticket_tp = tt.id_wm_ticket_tp "
//                        + "WHERE " + filterType
//                        + "GROUP BY t.id_wm_ticket "
//                        + "HAVING SUM(weight_link) < t.weight OR SUM(weight_link) IS NULL " // Pendientes.
//                        //+ "#HAVING SUM(weight_link) >= t.weight " // Completados o superados.
//                        + orderBy;
//                break;
//
//            default:
//        }
//
//        stm.execute(sql);
//
//        rst = stm.getResultSet();
//
//        int mnId = 0;
//
//        while (rst.next()) {
//            mnId = rst.getInt("Id");
//
//            switch (type) {
//                case SModConsts.S_ERP_DOC:
//                    registryDoc = new SDbErpDoc();
//                    registryDoc.read(session, new int[]{mnId});
//                    moRow = new SRowWmLinkRow(registryDoc, SRowWmLinkRow.SUBTYPE_TO_LINK);
//                    break;
//                case SModConsts.S_WM_TICKET:
//                    registryTic = new SDbWmTicket();
//                    registryTic.read(session, new int[]{mnId});
//                    moRow = new SRowWmLinkRow(registryTic, SRowWmLinkRow.SUBTYPE_TO_LINK);
//                    break;
//                default:
//            }
//
//            availableRows.add(moRow);
//        }
//
//        return availableRows;
//    }

    public static int ImportDocuments(final SGuiSession session) throws SQLException {
        int cont = 0;
        com.mysql.jdbc.Statement statementDest = null;
        com.mysql.jdbc.Statement statementOri = null;
        statementDest = (com.mysql.jdbc.Statement) moConecctionDest.createStatement();
        statementOri = (com.mysql.jdbc.Statement) moConecctionOrigen.createStatement();
        ResultSet resulSetOri = null;
        ResultSet resulSetDest = null;
        String sqlOri;
        String sqlDes;
        String sqlTemp;
        String initial_date = "";
        String final_date = "";
        String id_year, id_doc;

        moConecctionDest.setAutoCommit(false);
        moConecctionOrigen.setAutoCommit(false);

        String erpCompany = "erp_universal";

        ArrayList<Object> listOfIds = new ArrayList<>();

        sqlTemp = "SELECT ts_siie_rep_1 FROM etla_com.c_cfg;";
        resulSetDest = statementDest.executeQuery(sqlTemp);

        if (resulSetDest.next()) {
            initial_date = (resulSetDest.getTimestamp("ts_siie_rep_1")).toString();
        }

        sqlTemp = "UPDATE etla_com.c_cfg SET ts_siie_rep_1 = ts_siie_rep_2;";
        statementDest.executeUpdate(sqlTemp);
        sqlTemp = "SELECT now() AS now FROM etla_com.c_cfg;";
        resulSetDest = statementDest.executeQuery(sqlTemp);

        if (resulSetDest.next()) {
            final_date = (resulSetDest.getTimestamp("now")).toString();
        } else {
            final_date = "2018/06/01 00:00:0";
        }

        sqlOri = "SELECT trn_dps.id_year, trn_dps.id_doc, trn_dps.ts_edit FROM " + erpCompany + ".trn_dps ";
//                + "WHERE NOT b_del AND fid_st_dps = " + SSmsConsts.ST_DPS_EMITTED + " AND trn_dps.dt >= '" + initial_date + "' AND (fid_cl_dps = " + SSmsConsts.DOC_TYPE_CN_NUM + " OR fid_cl_dps = " + SSmsConsts.DOC_TYPE_INV_NUM + ");";

        String sqlDesTemp;
        resulSetOri = statementOri.executeQuery(sqlOri);

        // Recorrer TODOS los registros del ORIGEN
        while (resulSetOri.next()) {
            id_year = resulSetOri.getString("id_year");
            id_doc = resulSetOri.getString("id_doc");
            listOfIds.add(id_year + "-" + id_doc);

            // Buscar cada uno de los IDS del ORIGEN en el DESTINO.
            sqlDesTemp = "SELECT s_erp_doc.erp_year_id, s_erp_doc.erp_doc_id, s_erp_doc.doc_upd FROM etla_com.s_erp_doc "
                    + "WHERE s_erp_doc.erp_year_id = " + id_year + " AND  s_erp_doc.erp_doc_id = " + id_doc;

            resulSetDest = statementDest.executeQuery(sqlDesTemp);

            if (resulSetDest.next()) {//HERE
                //Si los valores DATE son iguales, todo OK
                if (resulSetDest.getTimestamp("doc_upd").toString().equals(resulSetOri.getTimestamp("ts_edit").toString())) {
                    //TODO OK
                } else {// sino, si el DATE en el DESTINO es anterior al DATE del ORIGEN 
                    if (SLibTimeUtils.isSameDatetime(resulSetDest.getTimestamp("doc_upd"), resulSetOri.getTimestamp("ts_edit"))) {
                        needUpdate(session, id_year, id_doc, 2);
                        cont++;
                    }
                }
            } else {
                // If the origin row need to add in destiny.
                needInsert(id_year, id_doc);
                cont++;
            }
        }

        sqlDes = "SELECT s_erp_doc.id_erp_doc as id, s_erp_doc.erp_year_id, s_erp_doc.erp_doc_id FROM etla_com.s_erp_doc WHERE NOT B_DEL AND s_erp_doc.doc_date >= '" + initial_date + "';";

        resulSetDest = statementDest.executeQuery(sqlDes);
        ArrayList<Object> id_list_des = new ArrayList<>();

        while (resulSetDest.next()) {
            id_list_des.add(resulSetDest.getInt("erp_year_id") + "-" + resulSetDest.getInt("erp_doc_id"));
        }
        resulSetDest.first();
        // Split (id_yer - id_doc)
        String[] idSplit = null;
        for (int i = 0; i < id_list_des.size(); i++) {
            if (listOfIds.contains(id_list_des.get(i))) {

            } else {
                //System.out.println("Sobra este id: " + id_list_des.get(i));
                idSplit = id_list_des.get(i).toString().split("-");
                needBDel(resulSetDest.getString("id"), idSplit[0], idSplit[1]);
                cont++;
            }
        }
        sqlTemp = "UPDATE etla_com.c_cfg SET ts_siie_rep_2 = '" + final_date + "';";
        statementDest.executeUpdate(sqlTemp);
        moConecctionOrigen.commit();
        moConecctionDest.commit();
        return cont;
    }

    private static void needUpdate(final SGuiSession session, String id_year, String id_doc, int type) throws SQLException {
        switch (type) {
            case 1://Item
                break;
            case 2://Documento
                com.mysql.jdbc.Statement statementDestForUpdate = (com.mysql.jdbc.Statement) moConecctionDest.createStatement();
                com.mysql.jdbc.Statement statementOriForUpdate = (com.mysql.jdbc.Statement) moConecctionOrigen.createStatement();

                moConecctionDest.setAutoCommit(false);
                moConecctionOrigen.setAutoCommit(false);

                SDbErpDoc moRegistryDoc = new SDbErpDoc();

                String sqlAuxUpdate,
                sql = "";
                ResultSet resultSetValoresOrigen = null;

                sqlAuxUpdate = "SELECT "
                        + "dps.id_year, "
                        + "dps.id_doc, "
                        + "dps.fid_cl_dps, "
                        + "dps.fid_ct_dps, "
                        + "dps.dt, "
                        + "dps.num_ser, "
                        + "dps.num, "
                        + "dps.ts_edit, "
                        + "bp.id_bp, "
                        + "bp.bp, "
                        + "sum(ety.weight_gross) AS weight_gross, "
                        // + "dps.b_del, "
                        // + "dps.b_sys "
                        + "FROM TRN_DPS_ETY AS ety "
                        + "INNER JOIN TRN_DPS AS dps on ety.id_doc = dps.id_doc "
                        + "INNER JOIN ERP.BPSU_BP AS BP ON dps.fid_bp_r = bp.id_bp "
                        + "WHERE dps.id_year = " + id_year + " AND dps.id_doc = " + id_doc + " AND NOT dps.b_del AND NOT ety.b_del group by id_year;";

                resultSetValoresOrigen = statementOriForUpdate.executeQuery(sqlAuxUpdate);//;

                if (resultSetValoresOrigen.next()) {
                    try {
                        /**
                         * Registry
                         */
                        moRegistryDoc.setErpYearId(resultSetValoresOrigen.getInt("id_year"));
                        moRegistryDoc.setErpDocId(resultSetValoresOrigen.getInt("id_doc"));
                        moRegistryDoc.setDocClass(resultSetValoresOrigen.getString("fid_cl_dps"));
                        moRegistryDoc.setDocType(resultSetValoresOrigen.getString("fid_ct_dps"));
                        moRegistryDoc.setDocSeries(resultSetValoresOrigen.getString("num_ser"));
                        moRegistryDoc.setDocNumber(resultSetValoresOrigen.getString("num"));
                        moRegistryDoc.setDocDate(resultSetValoresOrigen.getDate("dt"));
                        moRegistryDoc.setBizPartnerId(resultSetValoresOrigen.getInt("id_bp"));
                        moRegistryDoc.setBizPartner(resultSetValoresOrigen.getString("bp"));
                        moRegistryDoc.setWeight(resultSetValoresOrigen.getDouble("weight_gross"));
                        moRegistryDoc.setDocUpdate(resultSetValoresOrigen.getTimestamp("ts_edit"));
                        moRegistryDoc.setClosed(false);
                        moRegistryDoc.setDeleted(false);
                        moRegistryDoc.setSystem(false);
                        //moRegistryDoc.setFkUserClosedId(sa.gui.util.SUtilConsts.USR_NA_ID);
                        moRegistryDoc.setFkUserInsertId(sa.gui.util.SUtilConsts.USR_NA_ID);
                        moRegistryDoc.setFkUserUpdateId(sa.gui.util.SUtilConsts.USR_NA_ID);
                        //moRegistryDoc.setTsUserClosed(null);// Now();
                        moRegistryDoc.setTsUserInsert(null);// Now();
                        moRegistryDoc.setTsUserUpdate(null);// Now();
                        moRegistryDoc.setRegistryNew(false);
                        moRegistryDoc.save(session);
                    } catch (Exception ex) {
                        Logger.getLogger(SSmsUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                int resultSet1 = statementDestForUpdate.executeUpdate(sql);
                moConecctionOrigen.commit();
                moConecctionDest.commit();
                moConecctionOrigen.setAutoCommit(true);
                moConecctionDest.setAutoCommit(true);
                if (resultSet1 == 0) {
                    System.out.println((char) 27 + "[31m" + "NO FUE POSIBLE ACTUALIZAR EL ID:" + moRegistryDoc.getErpYearId() + "" + moRegistryDoc.getErpDocId());
                }
                break;
            case 3://Boleto
                break;
            default:
                break;
        }
    }

    private static void needBDel(String id, String id_year, String id_doc) throws SQLException {
        com.mysql.jdbc.Statement statementDestForDel = (com.mysql.jdbc.Statement) moConecctionDest.createStatement();
        moConecctionDest.setAutoCommit(false);

        String sqlDel = "";
        sqlDel = "UPDATE etla_com.s_erp_doc SET b_del = 1 WHERE id_erp_doc = " + id + " AND erp_year_id = " + id_year + " AND erp_doc_id" + " = " + id_doc;
        boolean resultSet1 = statementDestForDel.execute(sqlDel);

        if (!resultSet1) {
//            System.out.println("SE HA CAMBIADO B_DEL DE MANERA CORRECTA PARA ID: " + id);
        } else {
            System.out.println((char) 27 + "[31m" + "NO SE HA CAMBIADO B_DEL DEL ID: " + id + " EN LA TABLA DE DESTINO");
        }

        // validacion las relaciones posiblemente creadas en la base de destino que hagan referencia al ID del registro elimanado //
        sqlDel = "UPDATE etla_com.s_wm_ticket_link SET b_del = 1 WHERE fk_erp_doc = " + id + ";";

        resultSet1 = statementDestForDel.execute(sqlDel);
        if (!resultSet1) {
//            System.out.println("SE HA CAMBIADO B_DEL DE MANERA CORRECTA PARA ID: " + id + " para los documentos vinculados donde este participa");
        } else {
            System.out.println((char) 27 + "[31m" + "NO SE HA CAMBIADO B_DEL DEL ID: " + id + " EN LA TABLA DE DESTINO");
        }
        moConecctionDest.commit();
    }

    public SSmsUtils(int type) {
        super(type);
    }

    private static void needInsert(String id_year, String id_doc) throws SQLException {
        com.mysql.jdbc.Statement statementDestForInsert = (com.mysql.jdbc.Statement) moConecctionDest.createStatement();
        com.mysql.jdbc.Statement statementOriForInsert = (com.mysql.jdbc.Statement) moConecctionOrigen.createStatement();
        moConecctionDest.setAutoCommit(false);
        moConecctionOrigen.setAutoCommit(false);
        String newQuery, sql;
        int nextId = 0;

        ResultSet rstNextId = statementDestForInsert.executeQuery("SELECT COALESCE(MAX(id_erp_doc), 0) + 1 FROM etla_com.s_erp_doc;");
        if (rstNextId.next()) {
            nextId = rstNextId.getInt(1);
        }

        newQuery = "SELECT "
                + "dps.id_year, "
                + "dps.id_doc, "
                + "dps.dt, "
                + "dps.num_ser, "
                + "dps.num, "
                + "dps.fid_ct_dps, "
                + "dps.fid_cl_dps, "
                + "dps.ts_edit, "
                + "bp.id_bp, "
                + "bp.bp, "
                + "sum(ety.weight_gross) AS weight_gross, "
                + "dps.b_del, "
                + "dps.b_sys "
                + "FROM TRN_DPS_ETY AS ety "
                + "INNER JOIN TRN_DPS AS dps on ety.id_doc = dps.id_doc "
                + "INNER JOIN ERP.BPSU_BP AS BP ON fid_bp_r = id_bp "
                + "WHERE dps.id_year = " + id_year + " AND dps.id_doc = " + id_doc + " AND NOT dps.b_del AND NOT ety.b_del group by id_year;";

        ResultSet resultSetValOriForInsert = statementOriForInsert.executeQuery(newQuery);
        // Variables for save the fields of the resultset
        int mnId_year = 0, mnId_doc = 0, mn_id_bp = 0;
        String msNum = "", msNum_Ser = "", msBp = "", mnFid_cl_dps = "", mnFid_ct_dps = "";
        double mdWeight = 0.0;
        boolean mbDel = false, mbSys = false;
        java.sql.Date mdDt = null;
        Timestamp mtTsEdit = null;
        SDbErpDoc moRegistryDoc = new SDbErpDoc();

        if (resultSetValOriForInsert.next()) {
            moRegistryDoc.setErpYearId(resultSetValOriForInsert.getInt("id_year"));
            moRegistryDoc.setErpDocId(resultSetValOriForInsert.getInt("id_doc"));
            moRegistryDoc.setDocClass(resultSetValOriForInsert.getString("fid_cl_dps"));
            moRegistryDoc.setDocType(resultSetValOriForInsert.getString("fid_ct_dps"));
            moRegistryDoc.setDocSeries(resultSetValOriForInsert.getString("num_ser"));
            moRegistryDoc.setDocNumber(resultSetValOriForInsert.getString("num"));
            moRegistryDoc.setDocDate(resultSetValOriForInsert.getDate("dt"));
            moRegistryDoc.setBizPartnerId(resultSetValOriForInsert.getInt("id_bp"));
            moRegistryDoc.setBizPartner(resultSetValOriForInsert.getString("bp"));
            moRegistryDoc.setWeight(resultSetValOriForInsert.getDouble("weight_gross"));
            moRegistryDoc.setDocUpdate(resultSetValOriForInsert.getTimestamp("ts_edit"));
            moRegistryDoc.setClosed(false);
            moRegistryDoc.setDeleted(false);
            moRegistryDoc.setSystem(false);
            //moRegistryDoc.setFkUserClosedId(sa.gui.util.SUtilConsts.USR_NA_ID);
            moRegistryDoc.setFkUserInsertId(sa.gui.util.SUtilConsts.USR_NA_ID);
            moRegistryDoc.setFkUserUpdateId(sa.gui.util.SUtilConsts.USR_NA_ID);
            //moRegistryDoc.setTsUserClosed(null);// Now();
            moRegistryDoc.setTsUserInsert(null);// Now();
            moRegistryDoc.setTsUserUpdate(null);// Now();
            moRegistryDoc.setRegistryNew(false);
            mnId_year = resultSetValOriForInsert.getInt("id_year");
            mnId_doc = resultSetValOriForInsert.getInt("id_doc");
            mdDt = resultSetValOriForInsert.getDate("dt");
            msNum_Ser = resultSetValOriForInsert.getString("num_ser");
            msNum = resultSetValOriForInsert.getString("num");
            mnFid_ct_dps = resultSetValOriForInsert.getString("fid_ct_dps");
            mnFid_cl_dps = resultSetValOriForInsert.getString("fid_cl_dps");
            mtTsEdit = resultSetValOriForInsert.getTimestamp("ts_edit");
            mn_id_bp = resultSetValOriForInsert.getInt("id_bp");
            msBp = resultSetValOriForInsert.getString("bp");
            mdWeight = resultSetValOriForInsert.getDouble("weight_gross");
            mbDel = resultSetValOriForInsert.getBoolean("b_del");
            mbSys = resultSetValOriForInsert.getBoolean("b_sys");

            switch (mnFid_ct_dps) {
                case "1"://INC
                    mnFid_ct_dps = "'INC'";
                    break;
                case "2": //EXP
                    mnFid_ct_dps = "'EXP'";
                    break;
                default:
                    break;
            }

            switch (mnFid_cl_dps) {
                case "3": //INV
                    mnFid_cl_dps = "'INV'";
                    break;
                case "5": //NC
                    mnFid_cl_dps = "'NC'";
                    break;
                default:
                    break;
            }
        }

        sql = "INSERT INTO etla_com.s_erp_doc( "
                + "id_erp_doc, "
                + "erp_year_id, "
                + "erp_doc_id, "
                + "doc_date, "
                + "doc_ser, "
                + "doc_num, "
                + "doc_type, "
                + "doc_class, "
                + "doc_upd, "
                + "biz_partner_id, "
                + "biz_partner, "
                + "weight, "
                + "b_del, "
                + "b_sys, "
                + "fk_usr_ins, "
                + "fk_usr_upd, "
                + "ts_usr_ins, "
                + "ts_usr_upd"
                + ") "
                + "VALUES( "
                + nextId + ", "
                + "'" + mnId_year + "', "
                + mnId_doc + ", "
                + "'" + mdDt + "', "
                + "'" + msNum_Ser + "', "
                + "'" + msNum + "', "
                + mnFid_cl_dps + ", "
                + mnFid_ct_dps + ", "
                + "'" + mtTsEdit + "', "
                + "'" + mn_id_bp + "', "
                + "'" + msBp + "', "
                + "" + mdWeight + ", "
                + (mbDel ? "1" : "0") + ", "
                + (mbSys ? "1" : "0") + ", 1, 1, now(), now()"
                + ")";

        boolean resultSet1 = statementDestForInsert.execute(sql);
        if (!resultSet1) {
//            System.out.println("SE HA INSERTADO DE MANERA CORRECTA EL ID: " + nextId);
        } else {
            System.out.println((char) 27 + "[31m" + "NO FUE POSIBLE INSERTAR EL ID:" + nextId);
        }
        moConecctionOrigen.commit();
        moConecctionDest.commit();
    }

//    public static ArrayList<SRowWmLinkRow> createWmLinkRowsForErpDocs(final SGuiSession session, final String docClass, final String docType) throws Exception {
//        return createWmLinkRows(session, SModConsts.S_ERP_DOC, docClass, docType, SLibConsts.UNDEFINED);
//    }
//
//    public static ArrayList<SRowWmLinkRow> createWmLinkRowsForWmTickets(final SGuiSession session, final int ticketType) throws Exception {
//        return createWmLinkRows(session, SModConsts.S_WM_TICKET, "", "", ticketType);
//    }
}

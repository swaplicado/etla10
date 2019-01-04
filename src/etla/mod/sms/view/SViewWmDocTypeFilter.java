/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.sms.db.SDbErpDoc;
import etla.mod.sms.db.SSmsConsts;
import etla.mod.sms.form.SFormLink;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Alfredo Pérez
 */
public class SViewWmDocTypeFilter extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    /**
     * mnDocType valid values declared in SModSysConsts: SX_INV_SAL... | SX_INV_PUR... | SX_CN_SAL... | SX_CN_PUR...
     */
    public static int mnDocType;
    /**
     * mnlinkType valid values declared in SModSysConsts: SX_REG_TO_LINK | SX_REG_LINKED | SX_REG_ALL
     */
    public static int mnLinkType;

    private JButton mjSendNext;
    private JButton mjSendPrev;
    private JButton mjClose;
    private SFormLink moFormLink;

    /**
     *
     * @param client
     * @param title
     * @param docType valid values declared in SModSysConsts: SX_INV_SAL... | SX_INV_PUR... | SX_CN_SAL... | SX_CN_PUR...
     * @param linkType valid values declared in SModSysConsts: SX_REG_TO_LINK | SX_REG_LINKED | SX_REG_ALL
     */
    public SViewWmDocTypeFilter(SGuiClient client, String title, int docType, int linkType) {
         super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_ERP_DOC, docType, title);
        mnDocType = docType;
        mnLinkType = linkType;
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        switch (mnLinkType) {
            case SModSysConsts.SX_REG_TO_LINK:
                //Crear vinculo
                mjSendNext = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_move_right.gif")), "Vincular con boleto...", (ActionListener) this);
                mjSendNext.setEnabled(true);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendNext);
                //Abrir
                mjSendPrev = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_doc_open.gif")), "Abrir boleto..", (ActionListener) this);
                mjSendPrev.setEnabled(true);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendPrev);
                break;
            case SModSysConsts.SX_REG_LINKED:
                //cerrar
                mjClose = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_doc_close.gif")), "Cerrado manual...", (ActionListener) this);
                mjClose.setEnabled(true);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjClose);
                break;
            default:
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sql2 = "";
        Object filter = null;
        /**
         * Aplicar un filtro para cada uno de los posibles casos de tipo de
         * docuemnto [INV | CN] y si es entrada o salida [EXP | INC]
         */
        switch (mnDocType) {
            case SModSysConsts.SX_INV_SAL:
                sql += (sql.isEmpty() ? "" : " AND ") + " doc_class = 'EXP' AND doc_type = 'INV' ";
                break;
            case SModSysConsts.SX_INV_PUR:
                sql += (sql.isEmpty() ? "" : " AND ") + " doc_class = 'INC' AND doc_type = 'INV' ";
                break;

            case SModSysConsts.SX_CN_SAL:
                sql += (sql.isEmpty() ? "" : " AND ") + " doc_class = 'EXP' AND doc_type = 'CN' ";
                break;

            case SModSysConsts.SX_CN_PUR:
                sql += (sql.isEmpty() ? "" : " AND ") + " doc_class = 'INC' AND doc_type = 'CN' ";
                break;
        }

        moPaneSettings = new SGridPaneSettings(1);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += " AND " + SGridUtils.getSqlFilterDate("d.doc_date", (SGuiDate) filter);
            sql += " AND " + "d.b_del = 0 ";
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += " AND " + SGridUtils.getSqlFilterDate("d.doc_date", (SGuiDate) filter);
            sql += " AND " + "d.b_del = 1 ";
        }
        
        switch (mnLinkType) {
            case SModSysConsts.SX_REG_TO_LINK:
                sql += " AND l.fk_erp_doc IS NULL AND NOT d.b_clo ";
                break;
            case SModSysConsts.SX_REG_LINKED:
                sql += " AND l.fk_erp_doc IS NOT NULL OR d.b_clo ";
                sql2 = "count(d.id_erp_doc) AS links, ";
                break;
            case SModSysConsts.SX_REG_ALL:
                sql += "AND (l.fk_erp_doc IS NOT NULL OR l.fk_erp_doc IS NULL) ";
                break;
            default:
        }
        
        msSql = "SELECT "
                + sql2
                + "d.id_erp_doc AS " + SDbConsts.FIELD_ID + "1, "
                + "d.erp_year_id AS " + SDbConsts.FIELD_CODE + ", "
                + "d.erp_doc_id  AS " + SDbConsts.FIELD_NAME + ", "
                //Posiblemente no son requeridos
                + "d.doc_class, "
                + "d.doc_type, "
                + "d.doc_ser, "
                + "d.doc_num, "
                // ↑
                + "d.doc_date, "
                + "d.biz_partner_id, "
                + "d.biz_partner, "
                + "d.weight, "
                + "d.doc_upd, "
                + "d.b_clo, "
                // ↓
                + "d.fk_usr_clo, "
                // ↑
                + "d.ts_usr_clo, "
                + "d.ts_usr_ins, "
                + "d.ts_usr_upd, "
                + "l.id_wm_ticket_link, "
                //↓
                + "sum(l.weight_link),"
                //↑
                + "l.weight_link, "
                + "l.notes, "

                + "l.b_appd, "
                + "l.b_aut_appd, "

                + "l.b_del, "
                + "l.b_sys, "

                + "l.fk_wm_ticket, "
                + "l.fk_erp_doc, "
                + "l.fk_shipt_n, "
                + "l.fk_wm_link_st, "
                + "l.fk_usr_link_st, "
                + "l.fk_usr_ins, "
                + "l.fk_usr_upd, "
                + "l.ts_usr_link_st, "
                + "l.ts_usr_ins, "
                + "l.ts_usr_upd, "
                    + "d.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                    + "d.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                    + "d.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                    + "d.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "d.fk_usr_clo AS f_usr_clo, "
                    + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                    + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "uc.name AS f_usr_clo, "
                    + "d.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                    + "d.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS f_usr_ins, "
                + "uu.name AS f_usr_upd "
                + "FROM "
                +  SModConsts.TablesMap.get(SModConsts.S_ERP_DOC) + " AS d "
                + "LEFT JOIN "
                + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET_LINK) + " AS l ON d.id_erp_doc = l.fk_erp_doc "
                + "INNER JOIN "
                + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON d.fk_usr_ins = ui.id_usr "
                + "INNER JOIN "
                + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON d.fk_usr_upd = uu.id_usr "
                + "INNER JOIN "
                + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uc ON d.fk_usr_clo = uc.id_usr "
                + "WHERE "
                + sql
                + "GROUP BY d.id_erp_doc "
                + "ORDER BY d.id_erp_doc;";
//validar SQL
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_ID + "1", "Id"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Año"));
        // TODOS LOS CAMPOS DE LA CONSULTA
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_NAME, "Doc ID"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_class", "Clase"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_type", "Tipo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_ser", "Serie"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_num", "Número"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "doc_date", "Fecha"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "biz_partner_id", "Id_asociado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "biz_partner", "Asociado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "weight", "Peso"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_upd", "Actualización"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_clo", "Cerrado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_usr_clo", "Cerrado por"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "d.ts_usr_clo", "f_cerrado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "d.ts_usr_ins", "f_insertado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "d.ts_usr_upd", "f_actualizado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "id_wm_ticket_link", "id vinculo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "sum(l.weight_link)", "Suma peso vinculado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "weight_link", "Peso del vinculo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "notes", "Notas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_appd", "Aprobado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_aut_appd", "Aprobado atm"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_del", "b_del"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_sys", "b_sys"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_wm_ticket", "fk_wm_ticket"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_erp_doc", "fk_erp_doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_shipt_n", "fk_shipt_n"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_wm_link_st", "fk_wm_link_st"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_usr_link_st", "fk_usr_link_st"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_usr_ins", "fk_usr_ins"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_usr_upd", "fk_usr_upd"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_link_st", "ts_usr_link_st"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_ins", "ts_usr_ins"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_upd", "ts_usr_upd"));
        
        //valores por defecto en las vistas
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            if (evt.getSource() instanceof JButton) {
                JButton button = (JButton) evt.getSource();

                if (button == mjSendNext) {
                    actionPerformedSendNext();
                }
                else if (button == mjClose) {
                    actionPerformedStatusAction(SSmsConsts.REGISTRY_CLOSE);
                }
                else if (button == mjSendPrev) {
                    actionPerformedStatusAction(SSmsConsts.REGISTRY_OPEN);
                }
            }
        }
    }

    private void actionPerformedSendNext() {
        if (getSelectedGridRow() == null){
            System.out.println("SE DEBE SELECCIONAR UN ROW");
        }
        else {
            try {
                // ALPHA: Validar el tipo de vinculo y mandar parametros para su modificacion.
                int[] id = getSelectedGridRow().getRowPrimaryKey();
                moFormLink = new SFormLink(miClient, "Vincular", SModConsts.S_ERP_DOC, id[0]);
                moFormLink.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(SViewWmDocTypeFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param typeAction close | open
     * @param type document | ticket
     * @param id id to apply action
     */
    private void actionPerformedStatusAction(int typeAction) {
        SDbErpDoc row = (SDbErpDoc) miClient.getSession().readRegistry(SModConsts.S_ERP_DOC, getSelectedGridRow().getRowPrimaryKey());
        row.setRegistryNew(false);
        try {
            switch (typeAction) {
                case SSmsConsts.REGISTRY_CLOSE:
                    row.setClosed(true);
                    break;
                case SSmsConsts.REGISTRY_OPEN:
                    row.setClosed(false);
                    break;
            }
            //Si aun no completa el peso, puede regresarlo al estatus anterior
            if (canOpen(row.getPkErpDocId(), row)) {
                row.save(miClient.getSession());
            } else {
                //NOTIFICAR AL USUARIO QUE EL PESO ESTA COMPLETO
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_UTILS_QTY_INVALID + ": PESO completo.");
            }
        } catch (Exception ex) {
            Logger.getLogger(SViewWmDocTypeFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static boolean canOpen(int id, SDbErpDoc rowDoc) {
        //Consulta a la BD, comparar pesos, si esta igual o excedido entonces, no permitir y regresar un false.
        return true;
    }
}

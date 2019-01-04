/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
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
public class SViewWmTicket extends SGridPaneView implements ActionListener{

    private static int mnLinkType;
    private static int mnTicType;

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjSendNext;
    private SFormLink moFormLink;

    /**
     * 
     * @param client
     * @param title
     * @param linkType TO LINK | LINKED //Agregar los comentarios igual que con los documentos para tener mayor consistencia de datos
     * @param ticType   IN | OUT
     */
    public SViewWmTicket (SGuiClient client, String title, int linkType, int ticType) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_WM_TICKET, SLibConsts.UNDEFINED, title);
        setLinkType(linkType);
        setTicType(ticType);
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        initComponentsCustom();
    }
    
    private void setLinkType( int linkType){
        mnLinkType = linkType;
    }

    private int getLinkType(){
        return mnLinkType;
    }
    
    private void setTicType( int ticType){
        mnTicType = ticType;
    }

    private int getTicType(){
        return mnTicType;
    }

    private void initComponentsCustom() {
//        setRowButtonsEnabled(true, true, true, false, true);
        setRowButtonsEnabled(false, false, false, false, false);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        mjSendNext = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_move_right.gif")), "Vincular con boleto...", (ActionListener) this);
        mjSendNext.setEnabled(true);
        if (getLinkType() == SModSysConsts.SX_REG_TO_LINK) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendNext);
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("tic.ticket_dt_arr ", (SGuiDate) filter);
            sql += " AND tic.b_del = 0 ";
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("tic.ticket_dt_arr ", (SGuiDate) filter);
            sql += " AND tic.b_del = 1 ";
        }

        //SModSysConsts.SS_WM_TICKET_TP_OUT | SModSysConsts.SS_WM_TICKET_TP_IN
        switch (getTicType()){
            case SModSysConsts.SS_WM_TICKET_TP_IN:
                sql += " AND tic.fk_wm_ticket_tp = 1";
                break;
            case SModSysConsts.SX_TIC_TO_DOC_IN_TO_INV_PUR:
                sql += " AND tic.fk_wm_ticket_tp = 1 AND doc.doc_class = 'INC' AND doc.doc_type = 'INV'";
                break;
            case SModSysConsts.SX_TIC_TO_DOC_IN_TO_CN_SAL:
                sql += " AND tic.fk_wm_ticket_tp = 1 AND doc.doc_class = 'EXP' AND doc.doc_type = 'CN'";
                break;
            case SModSysConsts.SX_DOC_TO_TIC_INV_PUR_TO_TIC_IN:
                sql += " AND tic.fk_wm_ticket_tp = 1 AND doc.doc_class = 'INC' AND doc.doc_type = 'INV'";
                break;
            case SModSysConsts.SX_DOC_TO_TIC_CN_SAL_TO_TIC_IN:
                sql += " AND tic.fk_wm_ticket_tp = 1 AND doc.doc_class = 'EXP' AND doc.doc_type = 'CN'";
                break;
            case SModSysConsts.SS_WM_TICKET_TP_OUT:
                sql += " AND tic.fk_wm_ticket_tp = 2";
                break;
            case SModSysConsts.SX_TIC_TO_DOC_OUT_TO_INV_SAL:
                sql += " AND tic.fk_wm_ticket_tp = 2 AND doc.doc_class = 'EXP' AND doc.doc_type = 'INV'";
                break;
            case SModSysConsts.SX_TIC_TO_DOC_OUT_TO_CN_PUR:
                sql += " AND tic.fk_wm_ticket_tp = 2 AND doc.doc_class = 'INC' AND doc.doc_type = 'CN'";
                break;
            case SModSysConsts.SX_DOC_TO_TIC_INV_SAL_TO_TIC_OUT:
                sql += " AND tic.fk_wm_ticket_tp = 2 AND doc.doc_class = 'EXP' AND doc.doc_type = 'INV'";
                break;
            case SModSysConsts.SX_DOC_TO_TIC_CN_PUR_TO_TIC_OUT:
                sql += " AND tic.fk_wm_ticket_tp = 2 AND doc.doc_class = 'INC' AND doc.doc_type = 'CN'";
                break;
        }
        //SModSysConsts.SX_REG_TO_LINK | SModSysConsts.SX_REG_LINKED
        if (getLinkType() != SModSysConsts.SX_REG_ALL){
            sql += " AND l.fk_wm_ticket IS  " + (getLinkType() == SModSysConsts.SX_REG_TO_LINK ? " NOT NULL " : " NULL ");
        }

        msSql = "SELECT tic.id_wm_ticket AS " + SDbConsts.FIELD_ID + "1, "
                + "tic.ticket_id AS " + SDbConsts.FIELD_CODE + ", " 
                + "tic.ticket_dt_arr AS " + SDbConsts.FIELD_NAME + ", "
                + "tic.ticket_dt_dep, "
                + "tic.company, "
                + "tic.driver_name, "
                + "tic.vehic_plate, "
                + "tic.weight_arr, "
                + "tic.weight_dep, "
                + "tic.weight, "
                + "tic.b_wm_arr, "
                + "tic.b_wm_dep, "
                + "tic.b_tared, "
                + "IF(fk_wm_ticket_tp = " + SModSysConsts.SS_WM_TICKET_TP_IN + " , 'ENTRADA', 'SALIDA') AS fk_wm_ticket_tp, "
                + "tic.fk_wm_item, "
                + "tic.fk_usr_tare, "
                + "tic.ts_usr_tare, "
                + "tic.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "tic.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "tic.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "tic.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "tic.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "tic.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " AS tic "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET_LINK) + " AS l ON tic.id_wm_ticket = l.fk_wm_ticket "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_WM_TICKET_TP) + " AS tp ON tp.id_wm_ticket_tp = tic.fk_wm_ticket_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_ERP_DOC) + " AS doc ON l.fk_erp_doc = doc.id_erp_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON tic.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON tic.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql) + " "
                + "GROUP BY tic.ticket_id "
                + "ORDER BY tic.ticket_id ";

    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<SGridColumnView>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_ID + "1", "Id"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Folio"));
        //Datos de la carga
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "company", "Compañia"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "driver_name", "Chofer"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "vehic_plate", "Placas"));
        //Datos pesada
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight_arr", "Peso llegada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight_dep", "Peso salida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight", "Peso neto"));
        //Datos fecha entrada/salida
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Entrada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ticket_dt_dep", "Salida"));
        //Datos importacion Revuelta
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_wm_arr", "Entrada isImport"));//XXX Verificar el nombre de la columna.
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_wm_dep", "Salida isImport"));//XXX verificar el nombre de la columna.
        //Datos producto E|S
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_wm_ticket_tp", "Tipo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fk_wm_item", "Ítem"));
        //valores defecto en las vistas
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
        //validar que se tenga un row seleccionado
        if (getSelectedGridRow() == null){
            System.out.println("SE DEBE SELECCIONAR UN ROW");
        }else{
            if (evt.getSource() instanceof JButton) {
                JButton button = (JButton) evt.getSource();
                if (button == mjSendNext) {
                    try {
                        int[] id = getSelectedGridRow().getRowPrimaryKey();
                        moFormLink = new SFormLink(miClient, "Vincular", SModConsts.S_WM_TICKET,id[0]); 
                        moFormLink.setVisible(true);
                    } catch (Exception ex) {
                        Logger.getLogger(SViewWmDocTypeFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

}

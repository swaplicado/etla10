/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import etla.mod.SModConsts;
import java.util.ArrayList;
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
public class SViewWmTicSinLink extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    public static int subType;

    public SViewWmTicSinLink (SGuiClient client, String title, int type) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_ERP_DOC, type, title);
        setSubtype(type);
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        initComponentsCustom();
    }

    private void setSubtype( int type){
        subType = type;
    }

    private int getSubtype(){
        return subType;
    }
    
    private int getType(){
        int returnValue = 0;
        switch (getSubtype()){
//            case SModSysConsts.SU_WM_TIC_IN_SIN_DOC:
//                returnValue = SModSysConsts.SS_WM_TICKET_TP_IN;
//            case SModSysConsts.SU_WM_TIC_OUT_SIN_DOC:
//                returnValue = SModSysConsts.SS_WM_TICKET_TP_OUT;
//            case SModSysConsts.SU_WM_TIC_ALL:
//                returnValue = SModSysConsts.SS_WM_TICKET_TP_ALL;
            default:
        }
        return returnValue;
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";

        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += " AND t.b_del = 0 ";
            sql += " AND " + SGridUtils.getSqlFilterDate("t.ticket_dt_arr", (SGuiDate) filter);
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += " AND " + SGridUtils.getSqlFilterDate("t.ticket_dt_arr", (SGuiDate) filter);
            sql += " AND  t.b_del = 1 ";
        }
        getType();
        sql += (sql.isEmpty() ? "" : "AND ") + "fk_wm_ticket_tp = '" +  (getType() == 3 ? " 1 OR fk_wm_ticket_tp = 2 " : getType()) + "' ";

        msSql = "SELECT " +
                "t.ticket_id AS " + SDbConsts.FIELD_ID + "1, " +
                "t.ticket_dt_arr AS " + SDbConsts.FIELD_CODE + ", " +
                "t.ticket_dt_dep AS " + SDbConsts.FIELD_NAME + ", " +
                "t.company, " +
                "t.weight_arr, " +
                "t.weight_dep, " +
                "t.weight, " +
                "t.b_tared, " +
                "t.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " +
                "t.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", " +
                "t.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " +
                "t.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " +
                "t.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " +
                "t.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "  +
                "FROM s_wm_ticket AS t " +
                "LEFT JOIN S_WM_TICKET_LINK AS l ON t.id_wm_ticket = l.fk_wm_ticket " +
                "INNER JOIN cu_usr AS ui ON t.fk_usr_ins = ui.id_usr " +
                "INNER JOIN cu_usr AS uu ON t.fk_usr_upd = uu.id_usr " +
                "INNER JOIN ss_wm_ticket_tp AS tp ON tp.id_wm_ticket_tp = t.fk_wm_ticket_tp " +
                "WHERE l.fk_wm_ticket IS NULL " + sql + " " +
                "ORDER BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_CODE + ";";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<SGridColumnView>();
        //columnas del tipo ID
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_ID + "1", "Id"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Doc_id"));
        //
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_NAME, "Typo Doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "company", "Peso neto doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight_arr", "Peso llegada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight_dep", "Peso salida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight", "Peso neto boleto"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_tared", "Tarado"));
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight", "Peso neto doc"));
        //valores defecto en todas las vistas
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

}

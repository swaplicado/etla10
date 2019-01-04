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
public class SViewWmDoc extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    public static int subType;

    public SViewWmDoc(SGuiClient client, String title, int type, int subtype) {
        super(client, SGridConsts.GRID_PANE_VIEW, type, subtype, title);
        setSubtype(subtype);
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        initComponentsCustom();
    }

    private void setSubtype(int type) {
        subType = type;
    }

    private int getSubtype() {
        return subType;
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);
        if (getSubtype() == 1) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
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
            sql += (sql.isEmpty() ? "" : " AND ") + " " + SGridUtils.getSqlFilterDate("doc.doc_date", (SGuiDate) filter) + " ";
            sql += (sql.isEmpty() ? "" : " AND ") + "doc.b_del = 0 ";
        } else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : " AND ") + SGridUtils.getSqlFilterDate("doc.doc_date", (SGuiDate) filter);
            sql += (sql.isEmpty() ? "" : " AND ") + "doc.b_del  = 1 ";
        }

        msSql = "SELECT "
                + "doc.id_erp_doc AS " + SDbConsts.FIELD_ID + "1, "
                + "doc.erp_year_id AS " + SDbConsts.FIELD_CODE + ", "
                + "doc.erp_doc_id  AS " + SDbConsts.FIELD_NAME + ", "
                + "doc.doc_ser, "
                + "doc.doc_type, "
                + "doc.doc_num, "
                + "doc.doc_date, "
                + "doc.weight, "
                + "doc.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "doc.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "doc.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "doc.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "doc.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "doc.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + " "
                + "FROM s_erp_doc AS doc "
                + "INNER JOIN cu_usr AS ui ON doc.fk_usr_ins = ui.id_usr "
                + "INNER JOIN cu_usr AS uu ON doc.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY doc.erp_year_id, doc.erp_doc_id;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_ID + "1", "Id"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Año"));
        //
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_NAME, "Doc ID"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_ser", "Serie"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_type", "Tipo de documento"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "doc_num", "Número de documento"));
        //
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "weight", "Peso neto doc"));
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

}

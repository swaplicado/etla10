/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import etla.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Alfredo Pérez
 */
public class SViewWmItem extends SGridPaneView {

    public SViewWmItem (SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SU_WM_ITEM, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(true, true, true, false, true);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "sh.b_del = 0 ";
        }

        msSql = "SELECT id_wm_item AS " + SDbConsts.FIELD_ID + "1, "
                + "prod_id AS " + SDbConsts.FIELD_CODE + ", "
                + "name AS " + SDbConsts.FIELD_NAME + ", "
                + "b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "IF(origin = 'N' , 'NACIONAL', 'EXTRANJERO') AS Origen, " //Verificar si con mayusculas o minusculas
                + "fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_ITEM) + " "
                + "ORDER BY name, id_wm_item";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Origen", "Origen"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_INS_ID, SGridConsts.COL_TITLE_USER_INS_ID));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_UPD_ID, SGridConsts.COL_TITLE_USER_UPD_ID));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}

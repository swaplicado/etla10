/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import erp.lib.SLibConstants;
import etla.mod.SModConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Daniel López
 */
public class SViewShipment extends SGridPaneView implements ActionListener{
    
    private JButton mjPrint;
    
    public SViewShipment (SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_SHIPT, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
        
        mjPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), SUtilConsts.TXT_PRINT, this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjPrint);
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(true, true, true, false, true);
    }
    
    private void actionPerformedPrint() {
        if (mjPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        miClient.getSession().printReport(SModConsts.SR_SHIPT, SLibConsts.UNDEFINED, null, null); // El último parámetro es el hashmap
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "sh.b_del = 0 ";
        }
        
        msSql = "SELECT st.name AS " + SDbConsts.FIELD_NAME + ", "
                + "sh.id_shipt AS " + SDbConsts.FIELD_ID + "1, "
                + "sh.number AS " + SDbConsts.FIELD_CODE + ", "
                + "sh.shipt_date AS " + SDbConsts.FIELD_DATE + ", "
                + "sh.b_ann, "
                + "sh.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "sh.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "sh.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "sh.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "sh.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "sh.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "sh.fk_usr_release, "
                + "sh.ts_usr_release, "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "ur.name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_SHIPT) + " AS sh "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "sh.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "sh.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ur ON "
                + "sh.fk_usr_release = ur.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_SHIPT_ST) + " AS st ON "
                + "sh.fk_shipt_st = st.id_shipt_st "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY sh.number, sh.id_shipt ";
    }
    
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<SGridColumnView>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_NAME, "Estatus"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "sh.b_ann", "Anulado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "ur.name", "Usr Libera"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "sh.ts_usr_release", "Usr TS Libera"));

        return columns;
    }
    
    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.AX_ETL);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjPrint) {
                actionPerformedPrint();
            }
        }
    }
}

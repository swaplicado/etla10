/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod;

import etla.mod.sms.db.SDbCargoType;
import etla.mod.sms.db.SDbComment;
import etla.mod.sms.db.SDbDestination;
import etla.mod.sms.db.SDbHandlingType;
import etla.mod.sms.db.SDbShipment;
import etla.mod.sms.db.SDbShipmentRow;
import etla.mod.sms.db.SDbShipmentType;
import etla.mod.sms.db.SDbShipper;
import etla.mod.sms.db.SDbVehicleType;
import etla.mod.sms.db.SDbErpDoc;
import etla.mod.sms.db.SDbWmItem;
import etla.mod.sms.db.SDbWmTicket;
import etla.mod.sms.form.SFormLink;
import etla.mod.sms.form.SFormShipment;
import etla.mod.sms.form.SFormShipper;
import etla.mod.sms.form.SFormWmDoc;
import etla.mod.sms.form.SFormWmItem;
import etla.mod.sms.form.SFormWmTicket;
import etla.mod.sms.view.SViewShipment;
import etla.mod.sms.view.SViewShipper;
import etla.mod.sms.view.SViewWmDocTypeFilter;
import etla.mod.sms.view.SViewWmItem;
import etla.mod.sms.view.SViewWmTicket;
import javax.swing.JMenu;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;

/**
 *
 * @author Daniel López, Alfredo Pérez
 */
public class SModModuleSms extends SGuiModule {

    private SFormShipper moFormShipper;
    private SFormShipment moFormShipment;
    private SFormWmItem moFormWmItem;
    private SFormWmTicket moFormWmTicket;
    private SFormWmDoc moFormWmDoc;
    private SFormLink moFormLink;

    public SModModuleSms(SGuiClient client) {
        super(client, SModConsts.MOD_SMS, SLibConsts.UNDEFINED);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.SS_SHIPT_ST:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_shipt_st = " + key[0] + " "; }
                };
                break;
            case SModConsts.SS_WEB_ROLE:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_web_role = " + key[0] + " "; }
                };
                break;
            case SModConsts.SU_SHIPT_TP:
                registry = new SDbShipmentType();
                break;
            case SModConsts.SU_CARGO_TP:
                registry = new SDbCargoType();
                break;
            case SModConsts.SU_HANDG_TP:
                registry = new SDbHandlingType();
                break;
            case SModConsts.SU_VEHIC_TP:
                registry = new SDbVehicleType();
                break;
            case SModConsts.SU_COMMENT:
                registry = new SDbComment();
                break;
            case SModConsts.SU_SHIPPER:
                registry = new SDbShipper();
                break;
            case SModConsts.SU_DESTIN:
                registry = new SDbDestination();
                break;
            case SModConsts.SU_WM_ITEM:
                registry = new SDbWmItem();
                break;
            case SModConsts.S_SHIPT:
                registry = new SDbShipment();
                break;
            case SModConsts.S_SHIPT_ROW:
                registry = new SDbShipmentRow();
                break;
            case SModConsts.S_EVIDENCE:
                break;
            case SModConsts.S_ERP_DOC:
                registry = new SDbErpDoc();
                break;
            case SModConsts.S_WM_TICKET:
                registry = new SDbWmTicket();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        String sql = "";
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.SS_SHIPT_ST:
            case SModConsts.SS_WEB_ROLE:
                break;
            case SModConsts.SU_SHIPT_TP:
                settings = new SGuiCatalogueSettings("Tipo embarque", 1);
                sql = "SELECT id_shipt_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_shipt_tp ";
                break;
            case SModConsts.SU_CARGO_TP:
                settings = new SGuiCatalogueSettings("Tipo carga", 1);
                sql = "SELECT id_cargo_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_cargo_tp ";
                break;
            case SModConsts.SU_HANDG_TP:
                settings = new SGuiCatalogueSettings("Tipo maniobra", 1);
                sql = "SELECT id_handg_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_handg_tp ";
                break;
            case SModConsts.SU_VEHIC_TP:
                settings = new SGuiCatalogueSettings("Tipo vehículo", 1);
                sql = "SELECT id_vehic_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_vehic_tp ";
                break;
            case SModConsts.SU_COMMENT:
                settings = new SGuiCatalogueSettings("Observaciones", 1);
                sql = "SELECT id_comment AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_comment ";
                break;
            case SModConsts.SU_SHIPPER:
                settings = new SGuiCatalogueSettings("Transportista", 1);
                sql = "SELECT id_shipper AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_shipper ";
                break;

            case SModSysConsts.SU_WM_ITEM_TP:
                settings = new SGuiCatalogueSettings("Tipo de item", 1);
                sql = "SELECT sort AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_wm_ticket_tp ";
                break;
            case SModConsts.SU_DESTIN:
                settings = new SGuiCatalogueSettings("Destino", 1);
                sql = "SELECT id_destin AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_destin ";
                break;
            case SModConsts.SU_WM_ITEM:
                settings = new SGuiCatalogueSettings("Ítems", 1);
                sql = "SELECT id_wm_item AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_wm_item ";
                break;
            case SModConsts.S_SHIPT:
            case SModConsts.S_SHIPT_ROW:
            case SModConsts.S_EVIDENCE:
                break;
            case SModConsts.S_ERP_DOC:
                settings = new SGuiCatalogueSettings("Documentos", 1);
                sql = "SELECT id_wm_item AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_wm_item ";
                break;
            case SModConsts.S_WM_TICKET:
                settings = new SGuiCatalogueSettings("Boletos", 1);
                sql = "SELECT id_wm_ticket AS " + SDbConsts.FIELD_ID + "1, ticket_id AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_wm_ticket ";
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);
        }

        return settings;
    }

    @Override
    public SGridPaneView getView(int type, int subtype, SGuiParams params) {
        SGridPaneView view = null;
        int linkType = params == null ? 0 : params.getType();
        String title = "";

        switch (type) {
            case SModConsts.S_SHIPT:
                switch (subtype) {
                    case SLibConsts.UNDEFINED:
                        view = new SViewShipment(miClient, subtype, "Embarques");
                        break;
                    case SModSysConsts.SS_SHIPT_ST_REL_TO:
                        view = new SViewShipment(miClient, subtype, "Embarques por liberar");
                        break;
                    case SModSysConsts.SS_SHIPT_ST_REL:
                        view = new SViewShipment(miClient, subtype, "Embarques liberados");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.SU_SHIPPER:
                view = new SViewShipper(miClient, "Transportistas");
                break;
            case SModConsts.SU_WM_ITEM:
                view = new SViewWmItem(miClient, "Ítems báscula");
                break;
            case SModConsts.S_ERP_DOC:
                switch (subtype) {
                    case SModSysConsts.SX_INV_SAL:
                        switch (linkType){
                            case SModSysConsts.SX_REG_TO_LINK:
                                title = "Facturas venta por vincular";
                                break;
                            case SModSysConsts.SX_REG_LINKED:
                                title = "Facturas de venta vinculadas";
                                break;
                            case SModSysConsts.SX_REG_ALL:
                                title = "Facturas venta";
                                break;
                            default:
                        }
                        break;
                    case SModSysConsts.SX_INV_PUR:
                         switch (linkType){
                            case SModSysConsts.SX_REG_TO_LINK:
                                title = "Facturas venta por vincular";
                                break;
                            case SModSysConsts.SX_REG_LINKED:
                                title = "Facturas de venta vinculadas";
                                break;
                            case SModSysConsts.SX_REG_ALL:
                                title = "Facturas venta";
                                break;
                            default:
                        }
                        break;
                    case SModSysConsts.SX_CN_SAL:
                        switch (linkType){
                            case SModSysConsts.SX_REG_TO_LINK:
                                title = "Notas de crédito de ventas por vincular";
                                break;
                            case SModSysConsts.SX_REG_LINKED:
                                title = "Notas de crédito de ventas vinculadas";
                                break;
                            case SModSysConsts.SX_REG_ALL:
                                title = "Notas de crédito ventas";
                                break;
                            default:
                        }
                        break;
                    case SModSysConsts.SX_CN_PUR:
                         switch (linkType){
                            case SModSysConsts.SX_REG_TO_LINK:
                                title = "Notas de crédito de compras por vincular";
                                break;
                            case SModSysConsts.SX_REG_LINKED:
                                title = "Notas de crédito de compras vinculadas";
                                break;
                            case SModSysConsts.SX_REG_ALL:
                                title = "Notas de crédito de compra";
                                break;
                            default:
                        }
                        break;
                        default:
                            break;
                }
                view = new SViewWmDocTypeFilter(miClient, title, subtype, linkType);
                break;
            case SModConsts.S_WM_TICKET:
                switch (subtype) {
                    case SModSysConsts.SX_REG_TO_LINK:
                        switch (linkType) {
                            case SModSysConsts.SS_WM_TICKET_TP_OUT:
                                view = new SViewWmTicket(miClient, "Boletos por vincular salida", subtype, linkType);
                                break;
                            case SModSysConsts.SS_WM_TICKET_TP_IN:
                                view = new SViewWmTicket(miClient, "Boletos por vincular entrada", subtype, linkType);
                                break;
                            default:
                        }
                        break;
                    case SModSysConsts.SX_REG_LINKED:
                        switch (linkType) {
                            case SModSysConsts.SS_WM_TICKET_TP_OUT:
                                view = new SViewWmTicket(miClient, "Boletos vinculados de salida", subtype, linkType);
                                break;
                            case SModSysConsts.SS_WM_TICKET_TP_IN:
                                view = new SViewWmTicket(miClient, "Boletos vinculados de entrada", subtype, linkType);
                                break;
                            case SModSysConsts.SX_TIC_TO_DOC_OUT_TO_INV_SAL:
                                view = new SViewWmTicket(miClient, "Boletos salida a facturas de ventas", subtype, linkType);
                                break;
                            case SModSysConsts.SX_TIC_TO_DOC_OUT_TO_CN_PUR:
                                view = new SViewWmTicket(miClient, "Boletos salida a notas de crédito de compras", subtype, linkType);
                                break;
                            case SModSysConsts.SX_TIC_TO_DOC_IN_TO_INV_PUR:
                                view = new SViewWmTicket(miClient, "Boletos de entrada a facturas de compras", subtype, linkType);
                                break;
                            case SModSysConsts.SX_TIC_TO_DOC_IN_TO_CN_SAL:
                                view = new SViewWmTicket(miClient, "Boletos de entrada a notas de crédito de ventas", subtype, linkType);
                                break;
                            default:
                        }
                        break;
                    case SModSysConsts.SX_REG_ALL:
                        switch (linkType) {
                            case SModSysConsts.SS_WM_TICKET_TP_OUT:
                                view = new SViewWmTicket(miClient, "Todos los boletos salida", subtype, linkType);
                                break;
                            case SModSysConsts.SS_WM_TICKET_TP_IN:
                                view = new SViewWmTicket(miClient, "Todos los boletos entrada", subtype, linkType);
                                break;
                            default:
                        }
                        break;
                    default:
                }
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiForm getForm(int type, int subtype, SGuiParams params) {
        SGuiForm form = null;

        switch (type) {
            case SModConsts.S_SHIPT:
                if (moFormShipment == null) {
                    moFormShipment = new SFormShipment(miClient, "Embarques");
                }
                form = moFormShipment;
                break;
            case SModConsts.SU_SHIPPER:
                if (moFormShipper == null) {
                    moFormShipper = new SFormShipper(miClient, "Transportista");
                }
                form = moFormShipper;
                break;
            case SModConsts.SU_WM_ITEM:
                if (moFormWmItem == null) {
                    moFormWmItem = new SFormWmItem(miClient, "Ítem báscula");
                }
                form = moFormWmItem;
                break;
            case SModConsts.S_ERP_DOC:
                if (moFormWmDoc == null) {
                    moFormWmDoc = new SFormWmDoc(miClient, "Documentos importados");
                }
                form = moFormWmTicket;
                break;
            case SModConsts.S_WM_TICKET:
                if (moFormWmTicket == null) {
                    moFormWmTicket = new SFormWmTicket(miClient, "Boletos Vinculados");
                }
                form = moFormWmTicket;
                break;
            case SModConsts.SX_WM_LINK:
                if (moFormLink == null) {
                    miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                form = moFormWmTicket;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        SGuiReport report = null;

        switch (type) {
            case SModConsts.SR_SHIPT:
                report = new SGuiReport("reps/shipt.jasper", "Orden de embarque");
                break;
        }

        return report;
    }
}

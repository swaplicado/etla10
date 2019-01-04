/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.form;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.sms.db.SDbErpDoc;
import etla.mod.sms.db.SDbWmTicket;
import etla.mod.sms.db.SDbWmTicketLink;
import etla.mod.sms.db.SRowWmLinkRow;
import etla.mod.sms.db.SSmsUtils2;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldKey;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Alfredo Pérez
 */
public class SFormLink extends SBeanForm implements ActionListener, ItemListener {

    private SDbErpDoc moRegistryErpDoc;
    private SDbWmTicket moRegistryTicket;
    private SDbWmTicketLink moDbWmTicketLink;
    private SGridPaneForm moGridAvailableRows;
    private SGridPaneForm moGridSelectedRows;

    private int mnRegistryTypeToLink; // SModConsts.S_ERP_DOC or SModConsts.S_WM_TICKET
    private int mnRegistryId;
    private ArrayList<SDbWmTicketLink> maRowsToLink;
    /**
     * Creates new form SFormShipmentOrder
     *
     * @param client
     * @param title
     * @param registryTypeToLink valid values declared in SModSysConsts:
     * S_ERP_DOC | S_WM_TICKET
     * @param registryId row
     */
    public SFormLink(final SGuiClient client, final String title, final int registryTypeToLink, final int registryId) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, registryTypeToLink, SLibConsts.UNDEFINED, title);

        mnRegistryTypeToLink = registryTypeToLink;
        mnRegistryId = registryId;
        maRowsToLink =  new ArrayList<>();
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpHeader = new javax.swing.JPanel();
        jpBody = new javax.swing.JPanel();
        jpRows = new javax.swing.JPanel();
        jpRowControls = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        kgLinked = new javax.swing.JLabel();
        jtfWeightToLink = new javax.swing.JTextField();
        jbRowAdd = new javax.swing.JButton();
        jbRowRemove = new javax.swing.JButton();
        jPanelInfo = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlRegistryTypeToLink = new javax.swing.JLabel();
        jtfRegistryTypeToLink = new javax.swing.JTextField();
        jlRegistrySubtypeToLink = new javax.swing.JLabel();
        jtfRegistrySubtypeToLink = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jtfFolio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtfBp = new javax.swing.JTextField();
        jpAvailableRows = new javax.swing.JPanel();
        jpSelectedRows = new javax.swing.JPanel();
        jPanelTotals = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jtfAvailableWeight = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jtfLinkedWeight = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jtfTotalWeight = new javax.swing.JTextField();
        jpFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpHeader.setLayout(new java.awt.GridLayout(1, 2));
        getContentPane().add(jpHeader, java.awt.BorderLayout.NORTH);

        jpBody.setLayout(new java.awt.BorderLayout());

        jpRows.setBorder(javax.swing.BorderFactory.createTitledBorder("Remisiones:"));
        jpRows.setLayout(new java.awt.BorderLayout(5, 6));

        jpRowControls.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        kgLinked.setText("Kg vincular");
        jPanel12.add(kgLinked);

        jtfWeightToLink.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfWeightToLink.setText("0.00");
        jPanel12.add(jtfWeightToLink);

        jbRowAdd.setText(">");
        jbRowAdd.setToolTipText("Agregar remisión");
        jbRowAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jbRowAdd);

        jbRowRemove.setText("<");
        jbRowRemove.setToolTipText("Remover remisón");
        jbRowRemove.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jbRowRemove);

        jpRowControls.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jpRows.add(jpRowControls, java.awt.BorderLayout.CENTER);

        jPanelInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos a vincular"));
        jPanelInfo.setLayout(new java.awt.GridLayout(2, 0, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRegistryTypeToLink.setText("<Tipo registro:>");
        jlRegistryTypeToLink.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlRegistryTypeToLink);

        jtfRegistryTypeToLink.setEditable(false);
        jtfRegistryTypeToLink.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(jtfRegistryTypeToLink);

        jlRegistrySubtypeToLink.setText("<Subtipo registro>:");
        jlRegistrySubtypeToLink.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlRegistrySubtypeToLink);

        jtfRegistrySubtypeToLink.setEditable(false);
        jtfRegistrySubtypeToLink.setFocusable(false);
        jtfRegistrySubtypeToLink.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(jtfRegistrySubtypeToLink);

        jPanelInfo.add(jPanel3);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel7.setText("Folio:");
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jLabel7);

        jtfFolio.setEditable(false);
        jtfFolio.setFocusable(false);
        jtfFolio.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfFolio);

        jLabel8.setText("Asoc. negocios:");
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jLabel8);

        jtfBp.setEditable(false);
        jtfBp.setFocusable(false);
        jtfBp.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel5.add(jtfBp);

        jPanelInfo.add(jPanel5);

        jpRows.add(jPanelInfo, java.awt.BorderLayout.PAGE_START);
        jPanelInfo.getAccessibleContext().setAccessibleName("Datos:");

        jpAvailableRows.setBorder(javax.swing.BorderFactory.createTitledBorder("Disponibles:"));
        jpAvailableRows.setPreferredSize(new java.awt.Dimension(475, 23));
        jpAvailableRows.setLayout(new java.awt.BorderLayout());
        jpRows.add(jpAvailableRows, java.awt.BorderLayout.WEST);

        jpSelectedRows.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccionados:"));
        jpSelectedRows.setPreferredSize(new java.awt.Dimension(475, 23));
        jpSelectedRows.setLayout(new java.awt.BorderLayout(0, 2));

        jPanelTotals.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del vinculo:"));
        jPanelTotals.setLayout(new java.awt.GridLayout(3, 0, 0, 2));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 2, 0));

        jLabel14.setText("Peso disponible kg: ");
        jLabel14.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jLabel14);

        jtfAvailableWeight.setEditable(false);
        jtfAvailableWeight.setText("xxx,xxx.xx");
        jtfAvailableWeight.setFocusable(false);
        jtfAvailableWeight.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jtfAvailableWeight);

        jPanelTotals.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 2, 0));

        jLabel13.setText("Peso vinculado kg;");
        jLabel13.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jLabel13);

        jtfLinkedWeight.setEditable(false);
        jtfLinkedWeight.setText("XXX");
        jtfLinkedWeight.setFocusable(false);
        jtfLinkedWeight.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jtfLinkedWeight);

        jPanelTotals.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 2, 0));

        jLabel12.setText("Peso total kg:");
        jLabel12.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jLabel12);

        jtfTotalWeight.setEditable(false);
        jtfTotalWeight.setText("XXX");
        jtfTotalWeight.setFocusable(false);
        jtfTotalWeight.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jtfTotalWeight);

        jPanelTotals.add(jPanel8);

        jpSelectedRows.add(jPanelTotals, java.awt.BorderLayout.PAGE_END);

        jpRows.add(jpSelectedRows, java.awt.BorderLayout.EAST);
        jpSelectedRows.getAccessibleContext().setAccessibleName("Seleccionadas:");

        jpBody.add(jpRows, java.awt.BorderLayout.CENTER);

        jpFooter.setLayout(new java.awt.GridLayout(1, 2));
        jpBody.add(jpFooter, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jpBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelInfo;
    private javax.swing.JPanel jPanelTotals;
    private javax.swing.JButton jbRowAdd;
    private javax.swing.JButton jbRowRemove;
    private javax.swing.JLabel jlRegistrySubtypeToLink;
    private javax.swing.JLabel jlRegistryTypeToLink;
    private javax.swing.JPanel jpAvailableRows;
    private javax.swing.JPanel jpBody;
    private javax.swing.JPanel jpFooter;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpRowControls;
    private javax.swing.JPanel jpRows;
    private javax.swing.JPanel jpSelectedRows;
    private javax.swing.JTextField jtfAvailableWeight;
    private javax.swing.JTextField jtfBp;
    private javax.swing.JTextField jtfFolio;
    private javax.swing.JTextField jtfLinkedWeight;
    private javax.swing.JTextField jtfRegistrySubtypeToLink;
    private javax.swing.JTextField jtfRegistryTypeToLink;
    private javax.swing.JTextField jtfTotalWeight;
    private javax.swing.JTextField jtfWeightToLink;
    private javax.swing.JLabel kgLinked;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);
        switch (mnRegistryTypeToLink) {
            case SModConsts.S_ERP_DOC:
                moRegistryErpDoc = (SDbErpDoc) miClient.getSession().readRegistry(SModConsts.S_ERP_DOC, new int[]{mnRegistryId});
                setInitialValues(SModConsts.S_ERP_DOC, mnRegistryId);
                jtfTotalWeight.setText(moRegistryErpDoc.getWeight() + "");
                break;
            case SModConsts.S_WM_TICKET:
                moRegistryTicket = (SDbWmTicket) miClient.getSession().readRegistry(SModConsts.S_WM_TICKET, new int[]{mnRegistryId});
                setInitialValues(SModConsts.S_WM_TICKET, mnRegistryId);
                jtfTotalWeight.setText(moRegistryTicket.getWeight() + "");
                break;
            default:
                break;
        }

        moFields.setFormButton(jbSave);
        addAllListeners();

        moGridAvailableRows = new SGridPaneForm(miClient, mnRegistryTypeToLink, mnRegistryTypeToLink == SModConsts.S_ERP_DOC ? SModConsts.SX_ERP_DOC : SModConsts.SX_WM_TICKET, (mnRegistryTypeToLink == SModConsts.S_ERP_DOC ? "Documentos " : "Boletos ") + "disponibles...", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                int col = 0;
                SGridColumnForm[] columns = null;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                switch (mnRegistryTypeToLink) {
                    case SModConsts.S_WM_TICKET:
                        columns = new SGridColumnForm[6];
                        //Columnas de boleto
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DATE_DATETIME, "Fecha");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Compañia");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Peso disponible kg");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Peso vinculado kg");
                        break;
                    case SModConsts.S_ERP_DOC:
                        columns = new SGridColumnForm[7];
                        //Columnas de un Ticket!
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_INT_8B, "Folio");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha llegada");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha salida");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Compañia");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Peso disponible kg");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Peso vinculado kg");
                        break;
                    default:
                }

                gridColumnsForm.addAll(Arrays.asList((SGridColumnForm[]) columns));

                return gridColumnsForm;
            }

            @Override
            public void actionMouseClicked() {
                actionPerformedAddRow();
            }
        };

        moGridAvailableRows.setForm(null);
        moGridAvailableRows.setPaneFormOwner(null);
        jpAvailableRows.add(moGridAvailableRows, BorderLayout.CENTER);

        moGridSelectedRows = new SGridPaneForm(miClient, SModConsts.S_WM_TICKET_LINK, SModConsts.SX_WM_TICKET_LINK,"Vinculos seleccionados...", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                int col = 0;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                SGridColumnForm[] columns = new SGridColumnForm[7];
                //Linked ROWS
                switch (mnRegistryTypeToLink) {
                    case SModConsts.S_WM_TICKET_LINK:
                    default:
                        columns = new SGridColumnForm[4];
                        //Columnas de boleto
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_INT_8B, "Folio");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Peso_vinculo");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_INT_8B, "Boleto");
                        columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_INT_8B, "Documento");
                    break;
                }
                gridColumnsForm.addAll(Arrays.asList((SGridColumnForm[]) columns));

                return gridColumnsForm;
            }

            @Override
            public void actionMouseClicked() {
                actionPerformedRemoveRow();
            }
        };

        moGridSelectedRows.setForm(null);
        moGridSelectedRows.setPaneFormOwner(null);
        moGridSelectedRows.clearGridRows();
        jpSelectedRows.add(moGridSelectedRows, BorderLayout.CENTER);

        mvFormGrids.add(moGridAvailableRows);
        mvFormGrids.add(moGridSelectedRows);

        moGridSelectedRows.clearGrid();
        actionPerformedShowRows();
    }

    public SDbWmTicketLink getMoDbWmTicketLink() {
        return moDbWmTicketLink;
    }

    public void setMoDbWmTicketLink(SDbWmTicketLink moDbWmTicketLink) {
        this.moDbWmTicketLink = moDbWmTicketLink;
    }

    public void save(final SGuiSession session) {
        try{
            for (int i =0; i <= maRowsToLink.size(); i++){
                maRowsToLink.get(i).save(miClient.getSession());
            }
        }catch (Exception e){
            System.out.println("Eorror en 'Save': " + e);
        }
    }
    private int idRowSelected = 0;
    /**
     *
     * @param type SModConsts.S_ERP_DOC | SModConsts.S_WM_TICKET
     * @param idLink Id of registry
     */
    private void setInitialValues(int type, int idLink) {
        idRowSelected = idLink;
        double aviableWeight = 0d, linkedWeight = 0d, totalWeight = 0d;
        ResultSet resultSet = null;
        String sql = "";
        String label = "";
        String folio = "";
        String tipo = "";
        String bp = "";

        sql = "SELECT "
            + "SUM(weight_link) AS wLink "
            + "FROM S_WM_TICKET_LINK AS l "
            + "INNER JOIN S_ERP_DOC AS d ON l.fk_erp_doc = d.id_erp_doc "
            + "INNER JOIN S_WM_TICKET AS t ON l.fk_wm_ticket = t.id_wm_ticket "
            + "WHERE NOT l.b_del AND " + (type == SModConsts.S_ERP_DOC ? "d.id_erp_doc" : "t.id_wm_ticket") + " = '" + idLink + "' "
            + "GROUP BY fk_wm_ticket, fk_erp_doc;";

        try {
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                aviableWeight = 0;
                linkedWeight = 0;
            } else {
                aviableWeight = totalWeight - linkedWeight;
                linkedWeight = resultSet.getDouble("wLink");
            }
        } catch (Exception e) {
            miClient.showMsgBoxError(e.toString());
        }

        switch (type) {
            case SModConsts.S_ERP_DOC:
                label = "Documento";
                tipo = moRegistryErpDoc.getAuxType();
                folio = moRegistryErpDoc.getDocSeriesNumber();
                bp = moRegistryErpDoc.getBizPartner();
                totalWeight = moRegistryErpDoc.getWeight();
                break;
            case SModConsts.S_WM_TICKET:
                label = "Boleto";
                tipo = moRegistryTicket.getFkWmTicketTypeId() == 1 ? "Entrada" : "Salida";
                folio = moRegistryTicket.getTicketId() + "";
                bp = moRegistryTicket.getCompany();
                totalWeight = moRegistryTicket.getWeight();
                break;
            default:
        }
        jtfLinkedWeight.setText("" + linkedWeight);
        jtfAvailableWeight.setText("" + aviableWeight);
        jtfRegistryTypeToLink.setText(label);

        jtfFolio.setText(folio);
        jtfBp.setText(bp);
        jtfRegistrySubtypeToLink.setText(tipo);
    }

    /**
     * 
     * @param weight
     * @param type Add = true Remove = false
     */
    private void computeWeight(double weight, boolean type) {
        if(type){
            jtfAvailableWeight.setText((Double.parseDouble(jtfAvailableWeight.getText()) - weight) + "");
            jtfLinkedWeight.setText((Double.parseDouble(jtfLinkedWeight.getText()) + weight) + "");
        }else{
            jtfAvailableWeight.setText((Double.parseDouble(jtfAvailableWeight.getText()) + weight) + "");
            jtfLinkedWeight.setText((Double.parseDouble(jtfLinkedWeight.getText()) - weight) + "");
        }
    }

    private void actionPerformedClearRows() {

    }

    private void convertRowToLinkRow(SDbWmTicket sdbTicket) {
        SDbWmTicketLink registry = new SDbWmTicketLink();
        registry.setWeightLinked(Double.parseDouble(jtfWeightToLink.getText()));
        registry.setNotes("");
        registry.setApproved(false);
        registry.setAutoApproved(false);
        registry.setDeleted(false);
        registry.setSystem(false);

        registry.setFkWmTicketId(sdbTicket.getPkWmTicketId());
        registry.setFkErpDocId(idRowSelected);
        registry.setFkShipmentId_n(1);
        registry.setFkWmLinkStatusId(1);
        registry.setFkUserLinkStatusId(1);
        registry.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());
        registry.setFkUserInsertId(miClient.getSession().getUser().getPkUserId());
        computeWeight(registry.getWeightLinked(), true);
        try {
            registry.save(miClient.getSession());
            //for ADD ROW TO SELECTED
            Vector vec = new Vector();
            maRowsToLink.add(registry);
            vec.add(registry);
            moGridSelectedRows.populateGrid(vec);
        } catch (Exception ex) {
            Logger.getLogger(SFormLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void convertRowToLinkRow(SDbErpDoc sdbDoc) {
        sdbDoc.getPkErpDocId();
        SDbWmTicketLink registry = new SDbWmTicketLink();
        registry.setWeightLinked(Double.parseDouble(jtfWeightToLink.getText()));
        registry.setNotes("");
        registry.setApproved(false);
        registry.setAutoApproved(false);
        registry.setDeleted(false);
        registry.setSystem(false);

        registry.setFkWmTicketId(idRowSelected);
        registry.setFkErpDocId(sdbDoc.getPkErpDocId());
        registry.setFkShipmentId_n(1);
        registry.setFkWmLinkStatusId(1);
        registry.setFkUserLinkStatusId(1);
        registry.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());
        registry.setFkUserInsertId(miClient.getSession().getUser().getPkUserId());
        maRowsToLink.add(registry);
//        moGridSelectedRows.populateGrid(new Vector<> (maRowsToLink));
        computeWeight(registry.getWeightLinked(), true);
        try {
             Vector vec = new Vector();
            for(int i = 0; i<=maRowsToLink.size(); i++){
                vec.add(maRowsToLink.get(i));
            }
            moGridSelectedRows.populateGrid(vec);
        } catch (Exception ex) {
            Logger.getLogger(SFormLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actionPerformedAddRow() {
        if (moGridAvailableRows.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
            moGridAvailableRows.getTable().requestFocus();
        } else {
            try {
                //CONVERTIR EL DocRow o TicRow SELECCIONADO A UN LINKROW
                switch (mnRegistryTypeToLink) {
                    case SModConsts.S_ERP_DOC:
                        convertRowToLinkRow((((SRowWmLinkRow) moGridAvailableRows.getSelectedGridRow()).getTicRow()));
                        break;
                    case SModConsts.S_WM_TICKET:
                        convertRowToLinkRow((((SRowWmLinkRow) moGridAvailableRows.getSelectedGridRow()).getDocRow()));
                        break;
                    default:
                }
                moGridSelectedRows.addGridRow((SGridRow) maRowsToLink);
                moGridSelectedRows.renderGridRows();

                int index = moGridAvailableRows.getTable().getSelectedRow();
                moGridAvailableRows.removeGridRow(index);
                moGridAvailableRows.renderGridRows();
                moGridAvailableRows.setSelectedGridRow(index < moGridAvailableRows.getModel().getRowCount() ? index : moGridAvailableRows.getModel().getRowCount() - 1);
                jbSave.setEnabled(true);
            } catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    private void actionPerformedRemoveRow() {
        if (moGridSelectedRows.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
        } else {
            try {
                // identify row to be removed:
                int index = moGridSelectedRows.getTable().getSelectedRow();

//                 check if row to be removed should be returned to available rows: ALPHA
                moGridAvailableRows.addGridRow(moGridSelectedRows.getSelectedGridRow());
                moGridAvailableRows.renderGridRows();
                moGridAvailableRows.setSelectedGridRow(moGridAvailableRows.getModel().getRowCount() - 1);

                moGridSelectedRows.removeGridRow(index);
                moGridSelectedRows.renderGridRows();
                moGridSelectedRows.setSelectedGridRow(index < moGridSelectedRows.getModel().getRowCount() ? index : moGridSelectedRows.getModel().getRowCount() - 1);
            } catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */
    @Override
    public void addAllListeners() {
        jbRowAdd.addActionListener(this);
        jbRowRemove.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbRowAdd.removeActionListener(this);
        jbRowRemove.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {

    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        if (registry instanceof SDbErpDoc) {
            moRegistryErpDoc = (SDbErpDoc) registry;
            if (moRegistryErpDoc.isRegistryNew()) {
                jtfRegistryKey.setText("");
            } else {
                jtfRegistryKey.setText(SLibUtils.textKey(moRegistryErpDoc.getPrimaryKey()));

            }
        } else {
            moRegistryTicket = (SDbWmTicket) registry;
            if (moRegistryTicket.isRegistryNew()) {
                jtfRegistryKey.setText("");
            } else {
                jtfRegistryKey.setText(SLibUtils.textKey(moRegistryTicket.getPrimaryKey()));
            }
        }

        moRegistryErpDoc = (SDbErpDoc) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistryErpDoc.isRegistryNew()) {
            jtfRegistryKey.setText("");
        } else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistryErpDoc.getPrimaryKey()));
        }

        moGridAvailableRows.populateGrid(new Vector<>());
        moGridSelectedRows.populateGrid(new Vector<>());

        moGridSelectedRows.clearGridRows();
        actionPerformedClearRows();
        setFormEditable(true);

        if (moRegistryErpDoc.isRegistryNew()) {
            jbSave.setEnabled(true);
        } else {

        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbErpDoc registry = moRegistryErpDoc.clone();

        if (registry.isRegistryNew()) {
        }

        //registry.setNumber(...);
        registry.getChildRows().clear();
        for (int i = 0; i < moGridSelectedRows.getTable().getRowCount(); i++) {
            registry.getChildRows().add(((SRowWmLinkRow) moGridSelectedRows.getGridRow(i)).getLinkRow());
        }

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) { 

        } else {
            try {
                throw new Exception("ERROR!!!");
            } catch (Exception ex) {
                Logger.getLogger(SFormLink.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton button = (JButton) evt.getSource();
            if (button == jbRowAdd) {
                if (validateAddRow()) {
                    actionPerformedAddRow();
                }
            } else if (button == jbRowRemove) {
                actionPerformedRemoveRow();
            }
        }
    }

    private boolean validateAddRow() {
        if (Double.parseDouble(jtfWeightToLink.getText()) <= 0) {
            jtfWeightToLink.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void actionPerformedShowRows() {
        try {
            switch (mnRegistryTypeToLink) {
                case SModConsts.S_ERP_DOC:
                    int docType = 0;
                    if ((moRegistryErpDoc.getDocType().equals(SDbErpDoc.TYPE_INV) && (moRegistryErpDoc.getDocClass().equals(SDbErpDoc.CLASS_INC)))
                            || (moRegistryErpDoc.getDocType().equals(SDbErpDoc.TYPE_CN) && (moRegistryErpDoc.getDocClass().equals(SDbErpDoc.CLASS_EXP)))) {
                        docType = SModSysConsts.SS_WM_TICKET_TP_OUT;
                    } else {
                        if ((moRegistryErpDoc.getDocType().equals(SDbErpDoc.TYPE_INV) && (moRegistryErpDoc.getDocClass().equals(SDbErpDoc.CLASS_EXP)))
                                || (moRegistryErpDoc.getDocType().equals(SDbErpDoc.TYPE_CN) && (moRegistryErpDoc.getDocClass().equals(SDbErpDoc.CLASS_INC)))) {
                            docType = SModSysConsts.SS_WM_TICKET_TP_IN;
                        }
                    }
                    moGridAvailableRows.populateGrid(new Vector<>(SSmsUtils2.createWmLinkRows(miClient.getSession(), mnRegistryTypeToLink, docType, 0)));
                    break;
                case SModConsts.S_WM_TICKET:
                    moGridAvailableRows.populateGrid(new Vector<>(SSmsUtils2.createWmLinkRows(miClient.getSession(), mnRegistryTypeToLink, 0, moRegistryTicket.getFkWmTicketTypeId())));
                    break;
                default:
            }
        } catch (Exception ex) {
            Logger.getLogger(SFormLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldKey) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldKey field = (SBeanFieldKey) e.getSource();
            }
        }
    }

    /**
     *
     * @param id
     * @param linkType DOCUMENT(SModConsts.S_ERP_DOC) OR
     * TICKET(SModConsts.S_WM_TICKET)
     * @return @return AvailableWeight for Documento or Ticket
     */
    private double getAvailableWeight(int id, int linkType) {
        SDbWmTicketLink registry = new SDbWmTicketLink();
        try {
            registry.read(miClient.getSession(), new int[]{id});
        } catch (Exception ex) {
            Logger.getLogger(SFormLink.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch (linkType) {
            case SModConsts.S_ERP_DOC:
                return registry.getAuxWeightAvailableDoc();
            case SModConsts.S_WM_TICKET:
                return registry.getAuxWeightAvailableDoc();
            default:
                return 0d;
        }
    }

    private SRowWmLinkRow createLinkRow(SGridRow selectedGridRow) throws Exception {
        SRowWmLinkRow linkRow = null;
        try {
            switch (mnRegistryTypeToLink) {
                case SModConsts.S_ERP_DOC:
                    linkRow = new SRowWmLinkRow((SDbErpDoc) selectedGridRow, SRowWmLinkRow.SUBTYPE_TO_LINK);
                    break;
                case SModConsts.S_WM_TICKET:
                    linkRow = new SRowWmLinkRow((SDbWmTicket) selectedGridRow, SRowWmLinkRow.SUBTYPE_TO_LINK);
                    break;
                default:

            }
        } catch (Exception e) {
            throw new Exception(e + " ");
        }
        return linkRow;
    }

    
}
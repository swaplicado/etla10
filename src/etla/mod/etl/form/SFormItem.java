/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.form;

import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.etl.db.SDbConfigAvista;
import etla.mod.etl.db.SDbItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Sergio Flores
 */
public class SFormItem extends SBeanForm implements ActionListener {
    
    private SDbItem moRegistry;
    
    /**
     * Creates new form SFormCustomer
     * @param client
     * @param title
     */
    public SFormItem(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.AU_ITM, SLibConsts.UNDEFINED, title);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        jtfCode = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlSrcCustomer = new javax.swing.JLabel();
        moKeySrcCustomer = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlSrcRequiredCurrency = new javax.swing.JLabel();
        moKeySrcRequiredCurrency = new sa.lib.gui.bean.SBeanFieldKey();
        jtfDefaultCurrency = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlSrcRequiredUnitOfMeasure = new javax.swing.JLabel();
        moKeySrcRequiredUnitOfMeasure = new sa.lib.gui.bean.SBeanFieldKey();
        jtfDefaultUnitOfMeasure = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jlDesItemId = new javax.swing.JLabel();
        moIntDesItemId = new sa.lib.gui.bean.SBeanFieldInteger();
        jbEditDesItemId = new javax.swing.JButton();
        jlSiie = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlCode);

        jtfCode.setEditable(false);
        jtfCode.setFocusable(false);
        jtfCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jtfCode);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlName);

        jtfName.setEditable(false);
        jtfName.setFocusable(false);
        jtfName.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(jtfName);

        jPanel2.add(jPanel4);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSrcCustomer.setText("Cliente:");
        jlSrcCustomer.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlSrcCustomer);

        moKeySrcCustomer.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel7.add(moKeySrcCustomer);

        jPanel2.add(jPanel7);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSrcRequiredCurrency.setText("Moneda requerida:");
        jlSrcRequiredCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlSrcRequiredCurrency);

        moKeySrcRequiredCurrency.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moKeySrcRequiredCurrency);

        jtfDefaultCurrency.setEditable(false);
        jtfDefaultCurrency.setToolTipText("Moneda predeterminada");
        jtfDefaultCurrency.setFocusable(false);
        jtfDefaultCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jtfDefaultCurrency);

        jPanel2.add(jPanel6);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSrcRequiredUnitOfMeasure.setText("Unidad requerida:");
        jlSrcRequiredUnitOfMeasure.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlSrcRequiredUnitOfMeasure);

        moKeySrcRequiredUnitOfMeasure.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel11.add(moKeySrcRequiredUnitOfMeasure);

        jtfDefaultUnitOfMeasure.setEditable(false);
        jtfDefaultUnitOfMeasure.setToolTipText("Unidad predeterminada");
        jtfDefaultUnitOfMeasure.setFocusable(false);
        jtfDefaultUnitOfMeasure.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jtfDefaultUnitOfMeasure);

        jPanel2.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDesItemId.setText("ID ítem:*");
        jlDesItemId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDesItemId);
        jPanel12.add(moIntDesItemId);

        jbEditDesItemId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_edit.gif"))); // NOI18N
        jbEditDesItemId.setToolTipText("Modificar");
        jbEditDesItemId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbEditDesItemId);

        jlSiie.setForeground(java.awt.Color.gray);
        jlSiie.setText("(Primary Key SIIE)");
        jlSiie.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlSiie);

        jPanel2.add(jPanel12);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout(5, 0));
        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbEditDesItemId;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlDesItemId;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlSiie;
    private javax.swing.JLabel jlSrcCustomer;
    private javax.swing.JLabel jlSrcRequiredCurrency;
    private javax.swing.JLabel jlSrcRequiredUnitOfMeasure;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfDefaultCurrency;
    private javax.swing.JTextField jtfDefaultUnitOfMeasure;
    private javax.swing.JTextField jtfName;
    private sa.lib.gui.bean.SBeanFieldInteger moIntDesItemId;
    private sa.lib.gui.bean.SBeanFieldKey moKeySrcCustomer;
    private sa.lib.gui.bean.SBeanFieldKey moKeySrcRequiredCurrency;
    private sa.lib.gui.bean.SBeanFieldKey moKeySrcRequiredUnitOfMeasure;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        SDbConfigAvista configAvista = ((SDbConfig) miClient.getSession().getConfigSystem()).getRegConfigAvista();
        
        SGuiUtils.setWindowBounds(this, 560, 350);
        
        moKeySrcCustomer.setKeySettings(miClient, SGuiUtils.getLabelName(jlSrcCustomer), false);
        moKeySrcRequiredCurrency.setKeySettings(miClient, SGuiUtils.getLabelName(jlSrcRequiredCurrency), false);
        moKeySrcRequiredUnitOfMeasure.setKeySettings(miClient, SGuiUtils.getLabelName(jlSrcRequiredUnitOfMeasure), false);
        moIntDesItemId.setIntegerSettings(SGuiUtils.getLabelName(jlDesItemId), SGuiConsts.GUI_TYPE_INT_RAW, true);
        
        moFields.addField(moKeySrcCustomer);
        moFields.addField(moKeySrcRequiredCurrency);
        moFields.addField(moKeySrcRequiredUnitOfMeasure);
        moFields.addField(moIntDesItemId);
        
        moFields.setFormButton(jbSave);
        
        jtfDefaultCurrency.setText((String) miClient.getSession().readField(SModConsts.AS_CUR, new int[] { configAvista.getFkSrcDefaultCurrencyId() }, SDbRegistry.FIELD_CODE));
        jtfDefaultUnitOfMeasure.setText((String) miClient.getSession().readField(SModConsts.AS_UOM, new int[] { configAvista.getFkSrcDefaultUnitOfMeasureId() }, SDbRegistry.FIELD_CODE));
        
        jtfDefaultCurrency.setCaretPosition(0);
        jtfDefaultUnitOfMeasure.setCaretPosition(0);
    }

    private void enableEditDesItemId(boolean enable) {
        moIntDesItemId.setEditable(enable);
        jbEditDesItemId.setEnabled(!enable);
    }
    
    private void actionEditDesItemId() {
        enableEditDesItemId(true);
        moIntDesItemId.requestFocus();
    }
    
    /*
     * Public methods
     */
    
    /*
     * Overriden methods
     */
    
    @Override
    public void addAllListeners() {
        jbEditDesItemId.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbEditDesItemId.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeySrcCustomer, SModConsts.AU_CUS, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeySrcRequiredCurrency, SModConsts.AS_CUR, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeySrcRequiredUnitOfMeasure, SModConsts.AS_UOM, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbItem) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        jtfCode.setText(moRegistry.getCode());
        jtfCode.setCaretPosition(0);
        jtfName.setText(moRegistry.getName());
        jtfName.setCaretPosition(0);
        moKeySrcCustomer.setValue(new int[] { moRegistry.getFkSrcCustomerId_n() });
        moKeySrcRequiredCurrency.setValue(new int[] { moRegistry.getFkSrcRequiredCurrencyId_n() });
        moKeySrcRequiredUnitOfMeasure.setValue(new int[] { moRegistry.getFkSrcRequiredUnitOfMeasureId_n() });
        moIntDesItemId.setValue(moRegistry.getDesItemId());

        setFormEditable(true);
        
        moKeySrcCustomer.setEnabled(false); // editable only admor
        enableEditDesItemId(false);
        
        if (moRegistry.isRegistryNew()) {
        }
        else {
        }
        
        addAllListeners();
    }

    @Override
    public SDbItem getRegistry() throws Exception {
        SDbItem registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setDesItemId(moIntDesItemId.getValue());
        registry.setSrcCustomerFk_n(moKeySrcCustomer.getSelectedIndex() <= 0 ? "" : (String) moKeySrcCustomer.getSelectedItem().getComplement());
        registry.setSrcRequiredCurrencyFk_n(moKeySrcRequiredCurrency.getSelectedIndex() <= 0 ? SLibConsts.UNDEFINED : (Integer) moKeySrcRequiredCurrency.getSelectedItem().getComplement());
        registry.setSrcRequiredUnitOfMeasureFk_n(moKeySrcRequiredUnitOfMeasure.getSelectedIndex() <= 0 ? "" : (String) moKeySrcRequiredUnitOfMeasure.getSelectedItem().getComplement());
        registry.setFkSrcCustomerId_n(moKeySrcCustomer.getSelectedIndex() <= 0 ? SLibConsts.UNDEFINED : moKeySrcCustomer.getValue()[0]);
        registry.setFkSrcRequiredCurrencyId_n(moKeySrcRequiredCurrency.getSelectedIndex() <= 0 ? SLibConsts.UNDEFINED : moKeySrcRequiredCurrency.getValue()[0]);
        registry.setFkSrcRequiredUnitOfMeasureId_n(moKeySrcRequiredUnitOfMeasure.getSelectedIndex() <= 0 ? SLibConsts.UNDEFINED : moKeySrcRequiredUnitOfMeasure.getValue()[0]);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbEditDesItemId) {
                actionEditDesItemId();
            }
        }
    }
}

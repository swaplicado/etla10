/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Alfredo Pérez
 */
public class SRowWmLinkRow implements SGridRow {

    /** Identifies if gonna be linked. */
    public static final int SUBTYPE_TO_LINK = 1;
    /** Identifies if its already linked. */
    public static final int SUBTYPE_LINKED = 2;

    protected int mnType;
    protected int mnSubtype;

    protected SDbErpDoc moDbWmDoc;
    protected SDbWmTicket moDbWmTicket;
    protected SDbWmTicketLink moDbWmTicketLink;
    protected SDbShipmentRow moLinkRow;
    protected double mdWeightLinked;
    protected double mdWeightJustLinked;

     public SDbShipmentRow getLinkRow() {
        return moLinkRow;
    }

    /**
     * 
     * @param type SModConsts: S_ERP_DOC |  S_WM_TICKET | S_WM_TICKET_LINK
     * @param subtype
     * @param wmDoc
     * @param wmTicket 
     */
    private SRowWmLinkRow(final int type, final int subtype, final SDbErpDoc wmDoc, final SDbWmTicket wmTicket) {
        mnType = type;
        mnSubtype = subtype;
        moDbWmDoc = wmDoc;
        moDbWmTicket = wmTicket;
        mdWeightLinked = 0;
        mdWeightJustLinked = 0;
    }

    /**
     * Creates a link grid row.
     * @param wmDoc Document to be linked or already linked.
     * @param subtype Subtype, either SUBTYPE_TO_LINK or SUBTYPE_LINKED.
     */
    public SRowWmLinkRow(final SDbErpDoc wmDoc, final int subtype) {
        this(SModConsts.S_ERP_DOC, subtype, wmDoc, null);
    }

    /**
     * Createsa a link grid row.
     * @param wmTicket Ticket to be linked or already linked.
     * @param subtype Subtype, either SUBTYPE_TO_LINK or SUBTYPE_LINKED.
     */
    public SRowWmLinkRow(final SDbWmTicket wmTicket, final int subtype) {
        this(SModConsts.S_WM_TICKET, subtype, null, wmTicket);
    }

    public SDbErpDoc getDocRow() { return moDbWmDoc; }
    public SDbWmTicket getTicRow() { return moDbWmTicket; }

    public void setWeightLinked(double d) { mdWeightLinked = d; }
    public void setWeightJustLinked(double d) { mdWeightJustLinked = d; }

    public int getType() { return mnType; }
    public int getSubtype() { return mnSubtype; }
    public double getWeightLinked() { return mdWeightLinked; }
    public double getWeightJustLinked() { return mdWeightJustLinked; }

    public SDbErpDoc getWmDoc() { return moDbWmDoc; }
    public SDbWmTicket getWmTicket() { return moDbWmTicket; }

    public double getWeightToLink() {
        double weight = 0;

        switch (getType()) {
            case SModConsts.S_ERP_DOC:
                weight = moDbWmDoc.getWeight();
                break;
            case SModConsts.S_WM_TICKET:
                weight = moDbWmTicket.getWeight();
                break;
            default:
        }

        return weight - mdWeightLinked - mdWeightJustLinked;
    }

    @Override
    public int[] getRowPrimaryKey() {
        int[] key = null;

        switch (mnType) {
            case SModConsts.S_ERP_DOC:
                key = moDbWmDoc.getPrimaryKey();
                break;
            case SModConsts.S_WM_TICKET:
                key = moDbWmTicket.getPrimaryKey();
                break;
            default:
        }

        return key;
    }

    @Override
    public String getRowCode() {
        String code = "";

        switch (mnType) {
            case SModConsts.S_ERP_DOC:
                code = moDbWmDoc.getCode();
                break;
            case SModConsts.S_WM_TICKET:
                code = moDbWmTicket.getCode();
                break;
            default:
        }

        return code;
    }

    @Override
    public String getRowName() {
        String name = "";

        switch (mnType) {
            case SModConsts.S_ERP_DOC:
                name = moDbWmDoc.getName();
                break;
            case SModConsts.S_WM_TICKET:
                name = moDbWmTicket.getName();
                break;
            default:
        }

        return name;
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        switch (mnType) {
            case SModConsts.S_WM_TICKET_LINK:
            switch(row) {
                case 0:
                    moDbWmTicketLink.getPkWmTicketLinkId();
                    break;
                case 1:
                    moDbWmTicketLink.getWeightLinked();
                    break;
                case 2:
                    moDbWmTicketLink.getFkWmTicketId();
                    break;
                case 3:
                    moDbWmTicketLink.getFkErpDocId();
                    break;
                default:
            }
            break;
            case SModConsts.S_ERP_DOC:
                switch(row) {
                    case 0:
                        value = moDbWmDoc.getAuxType();
                        break;
                    case 1:
                        value = moDbWmDoc.getDocSeriesNumber();
                        break;
                    case 2:
                        value = moDbWmDoc.getDocDate();
                        break;
                    case 3:
                        value = moDbWmDoc.getBizPartner();
                        break;
                    case 4:
                        value = moDbWmDoc.getWeight();
                        break;
                    case 5:
                        value = mdWeightLinked;
                        break;
                    default:
                }
                break;
            case SModConsts.S_WM_TICKET:
                switch(row) {
                    case 0:
                        value = moDbWmTicket.getFkWmTicketTypeId() == 1 ? "ENTRADA" : " SALIDA";
                        break;
                    case 1:
                        value = moDbWmTicket.getTicketId();
                        break;
                    case 2:
                        value = moDbWmTicket.getTicketDatetimeArrival();
                        break;
                    case 3:
                        value = moDbWmTicket.getTicketDatetimeDeparture();
                        break;
                    case 4:
                        value = moDbWmTicket.getCompany();
                        break;
                    case 5:
                        value = moDbWmTicket.getWeight();
                        break;
                    case 6:
                        value = mdWeightLinked;
                        break;

                    default:
                }  
            default:
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object o, int row) {
        switch (row){
            case 4:
                setWeightJustLinked((double) o);
                break;
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}

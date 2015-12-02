/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mmkt.data.SDataCustomerConfig;
import erp.mod.SModSysConsts;
import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SEtlProcessCatCustomers {
    
    public static void computeEtlCustomers(final SGuiSession session, final SEtlPackage etlPackage) throws Exception {
        int nBizPartnerId = 0;
        int nBizPartnerAliveId = 0;
        int nBizPartnerDeletedId = 0;
        int nBizPartnerBranchId = 0;
        int nSalesAgentId = 0;
        int nCustomerId = 0;
        int nAvistaCurrencyCustomerFk = 0;
        String sAvistaCountry = "";
        String sAvistaCountryFk = "";
        String sAvistaState = "";
        String sAvistaStateFk = "";
        String sAvistaUnitOfMeasureCustomerFk = "";
        int nSiieCurrencyFk = 0;
        boolean bIsBizPartnerPerson = false;
        boolean bIsBizPartnerCustomer = false;
        String sql = "";
        String[] sqlQueries = null;
        Statement statementEtl = session.getStatement().getConnection().createStatement();
        Statement statementSiie = etlPackage.ConnectionSiie.createStatement();
        Statement statementAvista = etlPackage.ConnectionAvista.createStatement();
        ResultSet resultSetEtl = null;
        ResultSet resultSetSiie = null;
        ResultSet resultSetAvista = null;
        SDataBizPartner dataBizPartner = null;
        SDataBizPartnerCategory dataBizPartnerCategory = null;
        SDataBizPartnerBranch dataBizPartnerBranch = null;
        SDataBizPartnerBranchAddress dataBizPartnerBranchAddress = null;
        SDataBizPartnerBranchContact dataBizPartnerBranchContact = null;
        SDataCustomerConfig dataBizPartnerCustomerConfig = null;
        SDataCustomerBranchConfig dataBizPartnerCustomerBranchConfig = null;
        SDbSysCurrency dbSysCurrencyCustomer = null;
        SDbSysCurrency dbSysCurrencyRequired = null;
        SDbSysUnitOfMeasure dbSysUnitOfMeasureCustomer = null;
        SDbSysUnitOfMeasure dbSysUnitOfMeasureRequired = null;
        SDbConfigAvista dbConfigAvista = ((SDbConfig) session.getConfigSystem()).getRegConfigAvista();
        SDbSalesAgent dbSalesAgent = null;
        SDbCustomer dbCustomer = null;
        SEtlCatalogs etlCatalogs = null;
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_CUS_STA);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        // Obtain sales agents list from Avista:
        
        sql = "SELECT DISTINCT c.SalesUserKey, u.UserId, u.FullName "
                + "FROM dbo.CustomerInvoices AS ci "
                + "INNER JOIN dbo.Customers AS c ON c.CustomerId=ci.CustomerId "
                + "INNER JOIN dbo.Users AS u ON u.UserKey=c.SalesUserKey "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + "ORDER BY c.SalesUserKey, u.UserId ";
        resultSetAvista = statementAvista.executeQuery(sql);
        while (resultSetAvista.next()) {
            /****************************************************************/
            if (SEtlConsts.SHOW_DEBUG_MSGS) {
                System.out.println(SEtlConsts.TXT_SAL_AGT + ": " + resultSetAvista.getString("FullName"));
            }
            /****************************************************************/
            
            // From Avista obtain sales agent:
            
            nSalesAgentId = 0;
            
            sql = "SELECT id_sal_agt "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_SAL_AGT) + " "
                    + "WHERE src_sal_agt_id='" + resultSetAvista.getString("SalesUserKey") + "' "
                    + "ORDER BY id_sal_agt ";
            resultSetEtl = statementEtl.executeQuery(sql);
            if (resultSetEtl.next()) {
                nSalesAgentId = resultSetEtl.getInt(1);
            }
            
            try {
                if (nSalesAgentId == 0) {
                    // Sales agent is new on ETL:

                    statementEtl.execute("START TRANSACTION");
                    
                    dbSalesAgent = new SDbSalesAgent(); // set on save
                    //dbSalesAgent.setPkSalesAgentId(...); // set on save
                    dbSalesAgent.setSrcSalesAgentId(resultSetAvista.getInt("SalesUserKey"));
                    //dbSalesAgent.setDesSalesAgentId(..); // user defined
                    dbSalesAgent.setCode(resultSetAvista.getString("UserId"));
                    dbSalesAgent.setName(resultSetAvista.getString("FullName"));
                    //dbSalesAgent.setFirstEtlInsert(...); // set on save
                    //dbSalesAgent.setLastEtlUpdate(...); // set on save
                    dbSalesAgent.setDeleted(false);
                    dbSalesAgent.setSystem(false);
                    dbSalesAgent.setFkLastEtlLogId(etlPackage.EtlLog.getPkEtlLogId());
                    //dbSalesAgent.setFkUserInsertId(...); // set on save
                    //dbSalesAgent.setFkUserUpdateId(...); // set on save
                    //dbSalesAgent.setTsUserInsert(...); // set on save
                    //dbSalesAgent.setTsUserUpdate(...); // set on save
                    
                    dbSalesAgent.save(session);
                    
                    statementEtl.execute("COMMIT");
                }
                else {
                    // Sales agent already exists on ETL:

                    //dbSalesAgent = new SDbSalesAgent();
                }
            }
            catch (Exception e) {
                statementEtl.execute("ROLLBACK");
                throw e;
            }
        }
        
        etlCatalogs = new SEtlCatalogs(session, true, false);
        
        // Obtain customers list from Avista:
        
        sql = "SELECT DISTINCT c.CustomerId, c.TaxId, c.CustomerNumber, c.CustomerName, c.ShortName, c.Active, c.DeletedFlag, "
                + "c.Address1, c.Address2, c.Address3, c.AddressInternalNumber, c.County AS Neighborhood, c.City, c.District AS County, "
                + "c.State, sc.StateDescription, c.Country, cc.CountryDescription, c.Zip, c.Phone, c.Fax, "
                + "c.AddressReference, c.PayTermCode, c.CreditLimit, c.CreditStatusCode, c.DefaultPricePerCode, c.CurrencyKey, c.SalesUserKey "
                + "FROM dbo.CustomerInvoices AS ci "
                + "INNER JOIN dbo.Customers AS c ON c.CustomerId=ci.CustomerId "
                + "LEFT OUTER JOIN dbo.StateCodes AS sc ON sc.StateCode=c.State "
                + "LEFT OUTER JOIN dbo.CountryCodes AS cc ON cc.CountryCode=c.Country "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + "ORDER BY c.CustomerId, c.TaxId ";
        resultSetAvista = statementAvista.executeQuery(sql);
        while (resultSetAvista.next()) {
            /****************************************************************/
            if (SEtlConsts.SHOW_DEBUG_MSGS) {
                System.out.println(SEtlConsts.TXT_CUS + ": " + resultSetAvista.getString("CustomerName"));
            }
            /****************************************************************/
            
            // Select customer's country:

            sAvistaCountryFk = resultSetAvista.getString("Country");
            sAvistaCountry = resultSetAvista.getString("CountryDescription");

            if (sAvistaCountryFk == null || sAvistaCountryFk.isEmpty()) {
                sAvistaCountryFk = dbConfigAvista.getSrcLocalCountryFk();
                sAvistaCountry = SEtlConsts.AvistaCountriesMap.get(sAvistaCountryFk);
                if (sAvistaCountry == null) {
                    throw new Exception(SEtlConsts.MSG_ERR_UNK_CTY + "'" + sAvistaCountryFk + "'.");
                }
            }
            sAvistaCountry = SLibUtils.textTrim(sAvistaCountry).toUpperCase();

            if (sAvistaCountryFk.compareTo(dbConfigAvista.getSrcLocalCountryFk()) != 0) {
                throw new Exception(SEtlConsts.MSG_ERR_UNK_CTY + "'" + sAvistaCountryFk + "'."); // by now, only local country allowed (i.e., MX)
            }

            // Select customer's state:
            
            sAvistaStateFk = resultSetAvista.getString("State");
            sAvistaState = resultSetAvista.getString("StateDescription");
            
            if (sAvistaStateFk == null || sAvistaStateFk.isEmpty()) {
                sAvistaStateFk = dbConfigAvista.getSrcLocalStateFk();
                sAvistaState = SEtlConsts.AvistaStatesMap.get(sAvistaStateFk);
                if (sAvistaState == null) {
                    throw new Exception(SEtlConsts.MSG_ERR_UNK_STA + "'" + sAvistaStateFk + "'.");
                }
            }
            sAvistaState = SLibUtils.textTrim(sAvistaState).toUpperCase();

            // Select customer's currency:

            nAvistaCurrencyCustomerFk = resultSetAvista.getInt("CurrencyKey");
            
            if (nAvistaCurrencyCustomerFk == 0) {
                dbSysCurrencyCustomer = null;
                dbSysCurrencyRequired = null;
            }
            else {
                dbSysCurrencyCustomer = etlCatalogs.getEtlCurrency(etlCatalogs.getEtlIdForCurrency(nAvistaCurrencyCustomerFk));
                dbSysCurrencyRequired = dbSysCurrencyCustomer.getPkCurrencyId() == dbConfigAvista.getFkSrcDefaultCurrencyId() ? null : dbSysCurrencyCustomer;
            }
            
            nSiieCurrencyFk = dbSysCurrencyCustomer == null || dbSysCurrencyCustomer.getDesCurrencyId() == dbConfigAvista.getDesLocalCurrencyFk() ? SLibConsts.UNDEFINED : dbSysCurrencyCustomer.getDesCurrencyId();

            // Select customer's unit of measure:

            sAvistaUnitOfMeasureCustomerFk = resultSetAvista.getString("DefaultPricePerCode") == null ? "" : resultSetAvista.getString("DefaultPricePerCode");
            
            if (sAvistaUnitOfMeasureCustomerFk.isEmpty()) {
                dbSysUnitOfMeasureCustomer = null;
                dbSysUnitOfMeasureRequired = null;
            }
            else {
                dbSysUnitOfMeasureCustomer = etlCatalogs.getEtlUnitOfMeasure(etlCatalogs.getEtlIdForUnitOfMeasure(sAvistaUnitOfMeasureCustomerFk));
                dbSysUnitOfMeasureRequired = dbSysUnitOfMeasureCustomer.getPkUnitOfMeasureId() == dbConfigAvista.getFkSrcDefaultUnitOfMeasureId() ? null : dbSysUnitOfMeasureCustomer;
            }
            
            // Select customer's sales agent:
            
            dbSalesAgent = etlCatalogs.getEtlSalesAgent(etlCatalogs.getEtlIdForSalesAgent(resultSetAvista.getInt("SalesUserKey")));
            if (dbSalesAgent != null && dbSalesAgent.getDesSalesAgentId() == 0) {
                throw new Exception(SEtlConsts.MSG_ERR_UNK_SAL_AGT + "'" + dbSalesAgent.getName() + "' (" + SEtlConsts.TXT_CUS + "='" + SLibUtils.textTrim(resultSetAvista.getString("CustomerName")) + "').");
            }

            // From SIIE, obtain oldest business partner, alive and deleted ones, both of them when possible:
            
            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_CUS_AUX_1);
            etlPackage.EtlLog.save(session);

            nBizPartnerAliveId = 0;
            nBizPartnerDeletedId = 0;
            bIsBizPartnerPerson = resultSetAvista.getString("TaxId").length() == SEtlConsts.RFC_LEN_PER || resultSetAvista.getString("TaxId").isEmpty();
            bIsBizPartnerCustomer = false;
            
            sqlQueries = new String[] {
                "SELECT id_bp, b_cus, b_del " // a) search by Customer ID
                    + "FROM erp.bpsu_bp "
                    + "WHERE ext_id='" + resultSetAvista.getString("CustomerId") + "' "
                    + "ORDER BY id_bp ",
                "SELECT id_bp, b_cus, b_del " // b) search by Tax ID
                    + "FROM erp.bpsu_bp "
                    + "WHERE fiscal_id='" + resultSetAvista.getString("TaxId") + "' "
                    + "ORDER BY id_bp "
            };
            
            queries:
            for (String query : sqlQueries) {
                resultSetSiie = statementSiie.executeQuery(query);
                while (resultSetSiie.next()) {
                    if (!resultSetSiie.getBoolean("b_del")) {
                        nBizPartnerAliveId = resultSetSiie.getInt("id_bp");
                        bIsBizPartnerCustomer = resultSetSiie.getBoolean("b_cus");
                        break queries;
                    }
                    else {
                        if (nBizPartnerDeletedId == 0) {
                            nBizPartnerDeletedId = resultSetSiie.getInt("id_bp");
                            bIsBizPartnerCustomer = resultSetSiie.getBoolean("b_cus");
                        }
                    }
                }
            }
            
            try {
                nBizPartnerId = nBizPartnerAliveId != 0 ? nBizPartnerAliveId : nBizPartnerDeletedId;

                if (nBizPartnerId == 0) {
                    // Business partner is new on SIIE:

                    statementSiie.execute("START TRANSACTION");

                    // Create business partner registry:

                    dataBizPartner = new SDataBizPartner();
                    //dataBizPartner.setPkBizPartnerId(...); // set on save
                    dataBizPartner.setBizPartner(SLibUtils.textTrim(resultSetAvista.getString("CustomerName")).replaceAll("'", "''"));
                    dataBizPartner.setBizPartnerCommercial(SLibUtils.textTrim(resultSetAvista.getString("ShortName")).replaceAll("'", "''"));
                    dataBizPartner.setLastname(!bIsBizPartnerPerson ? "" : SLibUtils.textTrim(resultSetAvista.getString("CustomerName")).replaceAll("'", "''"));
                    dataBizPartner.setFirstname("");
                    dataBizPartner.setFiscalId(SLibUtils.textTrim(resultSetAvista.getString("TaxId")));
                    dataBizPartner.setFiscalFrgId("");
                    dataBizPartner.setAlternativeId("");
                    dataBizPartner.setExternalId(resultSetAvista.getString("CustomerId")); // keystone for ETL processing!
                    dataBizPartner.setCodeBankSantander("");
                    dataBizPartner.setCodeBankBanBajio("");
                    dataBizPartner.setWeb("");
                    dataBizPartner.setIsCompany(false);
                    dataBizPartner.setIsSupplier(false);
                    dataBizPartner.setIsCustomer(true);
                    dataBizPartner.setIsCreditor(false);
                    dataBizPartner.setIsDebtor(false);
                    dataBizPartner.setIsAttributeBank(false);
                    dataBizPartner.setIsAttributeCarrier(false);
                    dataBizPartner.setIsAttributeEmployee(false);
                    dataBizPartner.setIsAttributeSalesAgent(false);
                    dataBizPartner.setIsAttributePartnerShareholder(false);
                    dataBizPartner.setIsAttributeRelatedParty(false);
                    dataBizPartner.setIsDeleted(false);
                    dataBizPartner.setFkBizPartnerIdentityTypeId(bIsBizPartnerPerson ? SModSysConsts.BPSS_TP_BP_IDY_PER : SModSysConsts.BPSS_TP_BP_IDY_ORG);
                    dataBizPartner.setFkTaxIdentityId(bIsBizPartnerPerson ? SModSysConsts.BPSS_TP_BP_IDY_PER : SModSysConsts.BPSS_TP_BP_IDY_ORG);
                    dataBizPartner.setFkFiscalBankId(SModSysConsts.FINS_FISCAL_BANK_NA);
                    dataBizPartner.setFkBizAreaId(SModSysConsts.BPSU_BA_DEF);
                    dataBizPartner.setFkUserNewId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartner.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartner.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartner.setUserNewTs(...);
                    //dataBizPartner.setUserEditTs(...);
                    //dataBizPartner.setUserDeleteTs(...);

                    // Create business partner category registry:

                    dataBizPartnerCategory = new SDataBizPartnerCategory();
                    //dataBizPartnerCategory.setPkBizPartnerId(...); // set by business partner
                    dataBizPartnerCategory.setPkBizPartnerCategoryId(SModSysConsts.BPSS_CT_BP_CUS);
                    dataBizPartnerCategory.setKey(SLibUtils.textTrim(resultSetAvista.getString("CustomerNumber")));
                    dataBizPartnerCategory.setCompanyKey("");
                    dataBizPartnerCategory.setCreditLimit(resultSetAvista.getDouble("CreditLimit"));
                    dataBizPartnerCategory.setDaysOfCredit(SLibUtils.parseInt(resultSetAvista.getString("PayTermCode")));
                    dataBizPartnerCategory.setDaysOfGrace(0);
                    dataBizPartnerCategory.setDateStart(SLibTimeUtils.getBeginOfYear(etlPackage.PeriodStart));
                    dataBizPartnerCategory.setDateEnd_n(null);
                    dataBizPartnerCategory.setIsCreditByUser(false);
                    dataBizPartnerCategory.setIsDeleted(false);
                    dataBizPartnerCategory.setFkBizPartnerCategoryId(SModSysConsts.BPSS_CT_BP_CUS);
                    dataBizPartnerCategory.setFkBizPartnerTypeId(SModSysConsts.BPSU_TP_BP_DEF);
                    dataBizPartnerCategory.setFkCreditTypeId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerCategory.setFkRiskTypeId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerCategory.setFkCfdAddendaTypeId(SModSysConsts.BPSS_TP_CFD_ADD_CFD_ADD_NA);
                    dataBizPartnerCategory.setFkLanguageId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerCategory.setFkCurrencyId_n(nSiieCurrencyFk);
                    dataBizPartnerCategory.setFkUserNewId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerCategory.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerCategory.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerCategory.setUserNewTs(...);
                    //dataBizPartnerCategory.setUserEditTs(...);
                    //dataBizPartnerCategory.setUserDeleteTs(...);

                    dataBizPartner.setDbmsCategorySettingsCo(null);
                    dataBizPartner.setDbmsCategorySettingsSup(null);
                    dataBizPartner.setDbmsCategorySettingsCus(dataBizPartnerCategory);
                    dataBizPartner.setDbmsCategorySettingsCdr(null);
                    dataBizPartner.setDbmsCategorySettingsDbr(null);

                    // Create business partner customer configuration:

                    dataBizPartnerCustomerConfig = new SDataCustomerConfig();
                    //dataBizPartnerCustomerConfig.setPkCustomerId(...); // set by business partner
                    dataBizPartnerCustomerConfig.setIsFreeDiscountDoc(false);
                    dataBizPartnerCustomerConfig.setIsFreeCommissions(false);
                    dataBizPartnerCustomerConfig.setIsDeleted(false);
                    dataBizPartnerCustomerConfig.setFkCustomerTypeId(SEtlConsts.SIIE_DEFAULT);
                    dataBizPartnerCustomerConfig.setFkMarketSegmentId(SEtlConsts.SIIE_DEFAULT);
                    dataBizPartnerCustomerConfig.setFkMarketSubsegmentId(SEtlConsts.SIIE_DEFAULT);
                    dataBizPartnerCustomerConfig.setFkDistributionChannelId(SEtlConsts.SIIE_DEFAULT);
                    dataBizPartnerCustomerConfig.setFkSalesAgentId_n(dbSalesAgent == null ? SLibConsts.UNDEFINED : dbSalesAgent.getDesSalesAgentId());
                    dataBizPartnerCustomerConfig.setFkSalesSupervisorId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerCustomerConfig.setFkUserNewId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerCustomerConfig.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerCustomerConfig.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerCustomerConfig.setUserNewTs(...);
                    //dataBizPartnerCustomerConfig.setUserEditTs(...);
                    //dataBizPartnerCustomerConfig.setUserDeleteTs(...);

                    dataBizPartner.setDbmsDataCustomerConfig(dataBizPartnerCustomerConfig);

                    // Create business partner branch:

                    dataBizPartnerBranch = new SDataBizPartnerBranch();
                    //dataBizPartnerBranch.setPkBizPartnerBranchId(...); // set on save
                    dataBizPartnerBranch.setBizPartnerBranch(SModSysConsts.TXT_HQ);
                    dataBizPartnerBranch.setCode("");
                    dataBizPartnerBranch.setIsAddressPrintable(true);
                    dataBizPartnerBranch.setIsDeleted(false);
                    //dataBizPartnerBranch.setFkBizPartnerId(...); // set by business partner
                    dataBizPartnerBranch.setFkBizPartnerBranchTypeId(SModSysConsts.BPSS_TP_BPB_HQ);
                    dataBizPartnerBranch.setFkTaxRegionId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerBranch.setFkAddressFormatTypeId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerBranch.setFkUserNewId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranch.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranch.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerBranch.setUserNewTs();
                    //dataBizPartnerBranch.setUserEditTs();
                    //dataBizPartnerBranch.setUserDeleteTs();

                    // Create business partner branch address:

                    dataBizPartnerBranchAddress = new SDataBizPartnerBranchAddress();
                    //dataBizPartnerBranchAddress.setPkBizPartnerBranchId(...); // set by business partner branch
                    //dataBizPartnerBranchAddress.setPkAddressId(...); // set on save
                    dataBizPartnerBranchAddress.setAddress(SModSysConsts.TXT_OFFICIAL);
                    dataBizPartnerBranchAddress.setStreet(SLibUtils.textTrim(resultSetAvista.getString("Address1")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setStreetNumberExt(SLibUtils.textTrim(resultSetAvista.getString("Address2")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setStreetNumberInt(SLibUtils.textTrim(resultSetAvista.getString("AddressInternalNumber")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setNeighborhood(SLibUtils.textTrim(resultSetAvista.getString("Neighborhood")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setReference(SLibUtils.textTrim(resultSetAvista.getString("AddressReference")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setLocality(SLibUtils.textTrim(resultSetAvista.getString("City")));
                    dataBizPartnerBranchAddress.setCounty(SLibUtils.textTrim(resultSetAvista.getString("County")));
                    dataBizPartnerBranchAddress.setState(sAvistaState);
                    dataBizPartnerBranchAddress.setZipCode(SLibUtils.textTrim(resultSetAvista.getString("Zip")));
                    dataBizPartnerBranchAddress.setPoBox("");
                    dataBizPartnerBranchAddress.setIsDefault(true);
                    dataBizPartnerBranchAddress.setIsDeleted(false);
                    dataBizPartnerBranchAddress.setFkAddressTypeId(SModSysConsts.BPSS_TP_ADD_OFF);
                    dataBizPartnerBranchAddress.setFkCountryId_n(SLibConsts.UNDEFINED); // by now, only local country allowed (i.e., MX)
                    dataBizPartnerBranchAddress.setFkUserNewId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranchAddress.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranchAddress.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerBranchAddress.setUserNewTs(...);
                    //dataBizPartnerBranchAddress.setUserEditTs(...);
                    //dataBizPartnerBranchAddress.setUserDeleteTs(...);
                    dataBizPartnerBranchAddress.setAuxCountrySysId(dbConfigAvista.getDesLocalCountryFk());

                    dataBizPartnerBranch.getDbmsBizPartnerBranchAddresses().add(dataBizPartnerBranchAddress);

                    // Create business partner branch address contact:

                    dataBizPartnerBranchContact = new SDataBizPartnerBranchContact();
                    //dataBizPartnerBranchContact.setPkBizPartnerBranchId(...); // set by business partner branch
                    //dataBizPartnerBranchContact.setPkContactId(); // set on save
                    dataBizPartnerBranchContact.setContact("");
                    dataBizPartnerBranchContact.setContactPrefix("");
                    dataBizPartnerBranchContact.setContactSuffix("");
                    dataBizPartnerBranchContact.setLastname("");
                    dataBizPartnerBranchContact.setFirstname("");
                    dataBizPartnerBranchContact.setCharge("");
                    dataBizPartnerBranchContact.setTelAreaCode01("");
                    dataBizPartnerBranchContact.setTelNumber01(SLibUtils.textTrim(resultSetAvista.getString("Phone")));
                    dataBizPartnerBranchContact.setTelExt01("");
                    dataBizPartnerBranchContact.setTelAreaCode02("");
                    dataBizPartnerBranchContact.setTelNumber02(SLibUtils.textTrim(resultSetAvista.getString("Fax")));
                    dataBizPartnerBranchContact.setTelExt02("");
                    dataBizPartnerBranchContact.setTelAreaCode03("");
                    dataBizPartnerBranchContact.setTelNumber03("");
                    dataBizPartnerBranchContact.setTelExt03("");
                    dataBizPartnerBranchContact.setNextelId01("");
                    dataBizPartnerBranchContact.setNextelId02("");
                    dataBizPartnerBranchContact.setEmail01("");
                    dataBizPartnerBranchContact.setEmail02("");
                    dataBizPartnerBranchContact.setSkype01("");
                    dataBizPartnerBranchContact.setSkype02("");
                    dataBizPartnerBranchContact.setIsDeleted(false);
                    dataBizPartnerBranchContact.setPkContactTypeId(SModSysConsts.BPSS_TP_CON_ADM);
                    dataBizPartnerBranchContact.setPkTelephoneType01Id(SLibUtils.textTrim(resultSetAvista.getString("Phone")).isEmpty() ? SModSysConsts.BPSS_TP_TEL_NA : SModSysConsts.BPSS_TP_TEL_TEL);
                    dataBizPartnerBranchContact.setPkTelephoneType02Id(SLibUtils.textTrim(resultSetAvista.getString("Fax")).isEmpty() ? SModSysConsts.BPSS_TP_TEL_NA : SModSysConsts.BPSS_TP_TEL_FAX);
                    dataBizPartnerBranchContact.setPkTelephoneType03Id(SModSysConsts.BPSS_TP_TEL_NA);
                    dataBizPartnerBranchContact.setFkUserNewId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranchContact.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranchContact.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerBranchContact.setUserNewTs(...);
                    //dataBizPartnerBranchContact.setUserEditTs(...);
                    //dataBizPartnerBranchContact.setUserDeleteTs(...);

                    dataBizPartnerBranch.getDbmsBizPartnerBranchContacts().add(dataBizPartnerBranchContact);

                    // Create business partner branch customer configuration:

                    dataBizPartnerCustomerBranchConfig = new SDataCustomerBranchConfig();
                    //dataBizPartnerCustomerBranchConfig.setPkCustomerBranchId(...);
                    dataBizPartnerCustomerBranchConfig.setIsDeleted(false);
                    dataBizPartnerCustomerBranchConfig.setFkSalesRouteId(SEtlConsts.SIIE_DEFAULT);
                    dataBizPartnerCustomerBranchConfig.setFkSalesAgentId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerCustomerBranchConfig.setFkSalesSupervisorId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerCustomerBranchConfig.setFkUserNewId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerCustomerBranchConfig.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerCustomerBranchConfig.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerCustomerBranchConfig.setUserNewTs(...);
                    //dataBizPartnerCustomerBranchConfig.setUserEditTs(...);
                    //dataBizPartnerCustomerBranchConfig.setUserDeleteTs(...);

                    dataBizPartnerBranch.getDbmsDataCustomerBranchConfig().add(dataBizPartnerCustomerBranchConfig);

                    dataBizPartner.getDbmsBizPartnerBranches().add(dataBizPartnerBranch);

                    // Save new business partner:

                    if (dataBizPartner.save(etlPackage.ConnectionSiie) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_INS + "'" + SLibUtils.textTrim(resultSetAvista.getString("CustomerName")) + "'.");
                    }
                    
                    statementSiie.execute("COMMIT");
                    
                    nBizPartnerId = dataBizPartner.getPkBizPartnerId();
                }
                else {
                    // Business partner already exists on SIIE:

                    dataBizPartner = new SDataBizPartner();
                    if (dataBizPartner.read(new int[] { nBizPartnerId }, statementSiie) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_QRY + "'" + SLibUtils.textTrim(resultSetAvista.getString("CustomerName")) + "'.");
                    }
                    
                    if (dataBizPartner.getExternalId().compareTo(resultSetAvista.getString("CustomerId")) != 0) {
                        
                        statementSiie.execute("START TRANSACTION");

                        dataBizPartner.setExternalId(resultSetAvista.getString("CustomerId"));
                        //dataBizPartner.setFkUserNewId(...); // by now, no trace of updating user required
                        //dataBizPartner.setFkUserEditId(...); // by now, no trace of updating user required
                        //dataBizPartner.setFkUserDeleteId(...); // by now, no trace of updating user required
                        
                        if (dataBizPartner.save(etlPackage.ConnectionSiie) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_UPD + "'" + SLibUtils.textTrim(resultSetAvista.getString("CustomerName")) + "'.");
                        }
                        
                        statementSiie.execute("COMMIT");
                    }
                }
                
                nBizPartnerBranchId = dataBizPartner.getDbmsHqBranch().getPkBizPartnerBranchId();
            }
            catch (Exception e) {
                statementSiie.execute("ROLLBACK");
                throw e;
            }
            
            // From Avista obtain customer:
            
            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_CUS_AUX_2);
            etlPackage.EtlLog.save(session);
            
            nCustomerId = 0;
            
            sql = "SELECT id_cus "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " "
                    + "WHERE src_cus_id='" + resultSetAvista.getString("CustomerId") + "' "
                    + "ORDER BY id_cus ";
            resultSetEtl = statementEtl.executeQuery(sql);
            if (resultSetEtl.next()) {
                nCustomerId = resultSetEtl.getInt(1);
            }
            
            try {
                if (nCustomerId == 0) {
                    // Customer is new on ETL:

                    statementEtl.execute("START TRANSACTION");
                    
                    dbCustomer = new SDbCustomer();
                    //dbCustomersetPkCustomerId(...); // set on save
                    dbCustomer.setSrcCustomerId(resultSetAvista.getString("CustomerId"));
                    dbCustomer.setDesCustomerId(nBizPartnerId); // user defined, but default value set
                    dbCustomer.setDesCustomerBranchId(nBizPartnerBranchId); // user defined, but default value set
                    dbCustomer.setCode(SLibUtils.textTrim(resultSetAvista.getString("CustomerNumber")));
                    dbCustomer.setName(SLibUtils.textTrim(resultSetAvista.getString("CustomerName")).replaceAll("'", "''"));
                    dbCustomer.setNameShort(SLibUtils.textTrim(resultSetAvista.getString("ShortName")).replaceAll("'", "''"));
                    dbCustomer.setTaxId(SLibUtils.textTrim(resultSetAvista.getString("TaxId")));
                    dbCustomer.setStreet(SLibUtils.textTrim(resultSetAvista.getString("Address1")).replaceAll("'", "''"));
                    dbCustomer.setNumberExt(SLibUtils.textTrim(resultSetAvista.getString("Address2")).replaceAll("'", "''"));
                    dbCustomer.setNumberInt(SLibUtils.textTrim(resultSetAvista.getString("AddressInternalNumber")).replaceAll("'", "''"));
                    dbCustomer.setNeighborhood(SLibUtils.textTrim(resultSetAvista.getString("Neighborhood")).replaceAll("'", "''"));
                    dbCustomer.setReference(SLibUtils.textTrim(resultSetAvista.getString("AddressReference")).replaceAll("'", "''"));
                    dbCustomer.setLocality(SLibUtils.textTrim(resultSetAvista.getString("City")));
                    dbCustomer.setCounty(SLibUtils.textTrim(resultSetAvista.getString("County")));
                    dbCustomer.setSrcStateFk(resultSetAvista.getString("State") == null ? "" : SLibUtils.textTrim(resultSetAvista.getString("State"))); // preserve original source value, even if null or not set
                    dbCustomer.setState(sAvistaState); // original or customized value
                    dbCustomer.setSrcCountryFk(resultSetAvista.getString("Country") == null ? "" : SLibUtils.textTrim(resultSetAvista.getString("Country"))); // preserve original source value, even if null or not set
                    dbCustomer.setCountry(sAvistaCountry); // original or customized value
                    dbCustomer.setZip(SLibUtils.textTrim(resultSetAvista.getString("Zip")));
                    dbCustomer.setPhone(SLibUtils.textTrim(resultSetAvista.getString("Phone")));
                    dbCustomer.setFax(SLibUtils.textTrim(resultSetAvista.getString("Fax")));
                    dbCustomer.setPayAccount(SEtlConsts.SIIE_PAY_ACC_UNDEF); // user defined, but default value set
                    dbCustomer.setCreditDays(SLibUtils.parseInt(resultSetAvista.getString("PayTermCode")));
                    dbCustomer.setCreditLimit(resultSetAvista.getDouble("CreditLimit"));
                    dbCustomer.setCreditStatusCode(SLibUtils.textTrim(resultSetAvista.getString("CreditStatusCode")));
                    dbCustomer.setPayTermCode(SLibUtils.textTrim(resultSetAvista.getString("PayTermCode")));
                    dbCustomer.setSrcCustomerCurrencyFk_n(nAvistaCurrencyCustomerFk);
                    dbCustomer.setSrcCustomerUnitOfMeasureFk_n(sAvistaUnitOfMeasureCustomerFk);
                    dbCustomer.setSrcCustomerSalesAgentFk_n(resultSetAvista.getInt("SalesUserKey"));
                    dbCustomer.setSrcRequiredCurrencyFk_n(dbSysCurrencyRequired == null ? SLibConsts.UNDEFINED : dbSysCurrencyRequired.getSrcCurrencyId()); // user defined, but default value set
                    dbCustomer.setSrcRequiredUnitOfMeasureFk_n(dbSysUnitOfMeasureRequired == null ? "" : dbSysUnitOfMeasureRequired.getSrcUnitOfMeasureId()); // user defined, but default value set
                    //dbCustomersetFirstEtlInsert(...); // set on save
                    //dbCustomersetLastEtlUpdate(...); // set on save
                    dbCustomer.setActive(resultSetAvista.getString("Active").compareTo(SEtlConsts.AVISTA_BOOL_Y) == 0);
                    dbCustomer.setDeleted(resultSetAvista.getString("DeletedFlag").compareTo(SEtlConsts.AVISTA_BOOL_Y) == 0);
                    dbCustomer.setSystem(false);
                    dbCustomer.setFkSrcCustomerCurrencyId_n(dbSysCurrencyCustomer == null ? SLibConsts.UNDEFINED : dbSysCurrencyCustomer.getPkCurrencyId());
                    dbCustomer.setFkSrcCustomerUnitOfMeasureId_n(dbSysUnitOfMeasureCustomer == null ? SLibConsts.UNDEFINED : dbSysUnitOfMeasureCustomer.getPkUnitOfMeasureId());
                    dbCustomer.setFkSrcCustomerSalesAgentId_n(dbSalesAgent == null ? SLibConsts.UNDEFINED : dbSalesAgent.getPkSalesAgentId());
                    dbCustomer.setFkSrcRequiredCurrencyId_n(dbSysCurrencyRequired == null ? SLibConsts.UNDEFINED : dbSysCurrencyRequired.getPkCurrencyId()); // user defined, but default value set
                    dbCustomer.setFkSrcRequiredUnitOfMeasureId_n(dbSysUnitOfMeasureRequired == null ? SLibConsts.UNDEFINED : dbSysUnitOfMeasureRequired.getPkUnitOfMeasureId()); // user defined, but default value set
                    dbCustomer.setFkDesRequiredPayMethodId_n(SLibConsts.UNDEFINED);
                    dbCustomer.setFkLastEtlLogId(etlPackage.EtlLog.getPkEtlLogId());
                    //dbCustomersetFkUserInsertId(...); // set on save
                    //dbCustomersetFkUserUpdateId(...); // set on save
                    //dbCustomersetTsUserInsert(...); // set on save
                    //dbCustomersetTsUserUpdate(...); // set on save
                    
                    dbCustomer.save(session);
                    
                    statementEtl.execute("COMMIT");
                }
                else {
                    // Customer already exists on ETL:

                    //customer = new SDbCustomer();
                }
            }
            catch (Exception e) {
                statementEtl.execute("ROLLBACK");
                throw e;
            }

            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_CUS_AUX_3);
            etlPackage.EtlLog.save(session);
        }
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_CUS_END);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        session.notifySuscriptors(SModConsts.AU_SAL_AGT);
        session.notifySuscriptors(SModConsts.AU_CUS);
    }
}

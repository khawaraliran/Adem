/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.globalqss.model;

/******************************************************************************
 * Entity Type = ECS_LCO
 * Overwrite Version 9249
 * #1 	ECS_LCO: 	recalcWithholdings() add more info, Invoice Line ID
 ******************************************************************************/

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocation;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MPriceList;
import org.compiere.model.MTax;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	LCO_MInvoice
 *
 *  @author Carlos Ruiz - globalqss - Quality Systems & Solutions - http://globalqss.com 
 *  @version  $Id: LCO_MInvoice.java 1009 2012-02-09 09:16:13Z suman $
 *  
 *  @author  PhaniKiran.Gutha
 *  @BugNo   1621
 *  @changes  in createWithholdingLines method  ,pass null to the calculate tax amount
 */
public class LCO_MInvoice extends MInvoice
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LCO_MInvoice.java 1009 2012-02-09 09:16:13Z suman $";
	/**
	 * 
	 */
	private static final long serialVersionUID = -924606040343895114L;

	public LCO_MInvoice(Properties ctx, int C_Invoice_ID, String trxName) {
		super(ctx, C_Invoice_ID, trxName);
	}

	//
	// TDS Calculation 
	//
	 
	public int recalcWithholdings(int paymentNatureId) {
		
		// Doc type 

		MDocType dt = new MDocType( getCtx(),getC_DocTypeTarget_ID(),get_TrxName());
		
		String genwh = dt.get_ValueAsString("GenerateWithholding");
		
		if ( genwh == null || genwh.equals("N") )
			return 0;

		int noins = 0;
		BigDecimal totwith = new BigDecimal("0");
		
		try {
		
			//
			// Delete previous records generated
			//
			
			String sqldel = "DELETE FROM LCO_InvoiceWithholding WHERE C_Invoice_ID = ?";
			
			PreparedStatement pstmtdel = DB.prepareStatement( sqldel,get_TrxName());
			pstmtdel.setInt(1, getC_Invoice_ID());
			
			int nodel = pstmtdel.executeUpdate();
			
			log.config("LCO_InvoiceWithholding deleted=" + nodel);
			pstmtdel.close();

			//
			// Fill variables normally needed
			//
			
			
			// BP variables
			
			MBPartner bp = new MBPartner( getCtx(),getC_BPartner_ID(),get_TrxName());
			
			Integer bp_isic_int = (Integer) bp.get_Value("LCO_ISIC_ID");
		
			int bp_isic_id = 0;
			
			if ( bp_isic_int != null )
				bp_isic_id = bp_isic_int.intValue();
			
			Integer bp_taxpayertype_int = (Integer) bp.get_Value("LCO_TaxPayerType_ID");
			int bp_taxpayertype_id = 0;
			
			if (bp_taxpayertype_int != null)
				bp_taxpayertype_id = bp_taxpayertype_int.intValue();
			
			MBPartnerLocation mbpl = new MBPartnerLocation( getCtx(),getC_BPartner_Location_ID(),get_TrxName() );
			
			MLocation bpl = MLocation.get( getCtx(),mbpl.getC_Location_ID(),get_TrxName());
			
			int bp_city_id = bpl.getC_City_ID();
			
			//
			// OrgInfo variables
			//
			
			MOrgInfo oi = MOrgInfo.get( getCtx(), getAD_Org_ID());
			Integer org_isic_int = (Integer) oi.get_Value("LCO_ISIC_ID");
			int org_isic_id = 0;
			
			if (org_isic_int != null)
				org_isic_id = org_isic_int.intValue();
			
			Integer org_taxpayertype_int = (Integer) oi.get_Value("LCO_TaxPayerType_ID");
			
			int org_taxpayertype_id = 0;
			
			if ( org_taxpayertype_int != null )
				org_taxpayertype_id = org_taxpayertype_int.intValue();
			
			MLocation ol = MLocation.get( getCtx(), 
										  oi.getC_Location_ID(),
										  get_TrxName());
			
			int org_city_id = ol.getC_City_ID();

			// Search withholding types (i.e. Payment Nature ) applicable depending on IsSOTrx

			String sqlt = " SELECT LCO_WithholdingType_ID "
						  + " FROM LCO_WithholdingType "
						  + " WHERE IsSOTrx = ? AND IsActive = 'Y' "
						  + " AND LCO_WithholdingType_ID = ? ";

			PreparedStatement pstmtt = DB.prepareStatement( sqlt, get_TrxName() );
			
			DB.setParameters( pstmtt, 
							  new Object[] { isSOTrx() ? "Y" : "N",
							  paymentNatureId });

			ResultSet rst = pstmtt.executeQuery();
			
			while (rst.next()) {
			
				//
				// For each applicable withholding Type ( i.e. Payment Nature && Deductee Type association)
				//
				
				X_LCO_WithholdingType wt = new X_LCO_WithholdingType( getCtx(),
																	  rst.getInt(1), 
																	  get_TrxName());
				X_LCO_WithholdingRuleConf wrc = null;
				
				log.info( "Withholding Type: " 
							+ wt.getLCO_WithholdingType_ID()
							+ "/" + wt.getName() );

				//
				// Look the conf fields i.e. Payment 
				//
				
				String sqlrc = " SELECT * "
								+ " FROM LCO_WithholdingRuleConf "
								+ " WHERE LCO_WithholdingType_ID = ? AND IsActive = 'Y'";
				
				PreparedStatement pstmtrc = DB.prepareStatement( sqlrc,
																 get_TrxName());
				
				pstmtrc.setInt(1, wt.getLCO_WithholdingType_ID());
				
				ResultSet rsrc = pstmtrc.executeQuery();
				
				if (rsrc.next()) {
				
					wrc = new X_LCO_WithholdingRuleConf( getCtx(), 
														 rsrc,
														 get_TrxName());
				} else {
					
					log.warning( " No LCO_WithholdingRuleConf for LCO_WithholdingType = "
								 + wt.getLCO_WithholdingType_ID());
					
					rsrc.close();
					pstmtrc.close();
					continue;
				}
				
				rsrc.close();
				pstmtrc.close();

				//
				// look for applicable rules according to config fields (rule) - i.e. TDS Rules
				//
				
				StringBuffer sqlr = new StringBuffer(
						
									" SELECT LCO_WithholdingRule_ID "
									+ "  FROM LCO_WithholdingRule "
									+ " WHERE LCO_WithholdingType_ID = ? "
									+ "   AND IsActive = 'Y' "
									+ "   AND ValidFrom <= ? " );
				
				if (wrc.isUseBPISIC())
					sqlr.append(" AND LCO_BP_ISIC_ID = ? ");
				
				if (wrc.isUseBPTaxPayerType())
					sqlr.append(" AND LCO_BP_TaxPayerType_ID = ? ");
				
				if (wrc.isUseOrgISIC())
					sqlr.append(" AND LCO_Org_ISIC_ID = ? ");
				
				if (wrc.isUseOrgTaxPayerType())
					sqlr.append(" AND LCO_Org_TaxPayerType_ID = ? ");
				
				if (wrc.isUseBPCity())
					sqlr.append(" AND LCO_BP_City_ID = ? ");
				
				if (wrc.isUseOrgCity())
					sqlr.append(" AND LCO_Org_City_ID = ? ");

				//
				// Add withholding categories of lines
				//
				
				if ( wrc.isUseWithholdingCategory() ) {

					//
					// look the conf fields
					//
					
					String sqlwcs = "SELECT DISTINCT COALESCE (p.LCO_WithholdingCategory_ID, COALESCE (c.LCO_WithholdingCategory_ID, 0)) "
							+ "  FROM C_InvoiceLine il "
							+ "  LEFT OUTER JOIN M_Product p ON (il.M_Product_ID = p.M_Product_ID) "
							+ "  LEFT OUTER JOIN C_Charge c ON (il.C_Charge_ID = c.C_Charge_ID) "
							+ "  WHERE C_Invoice_ID = ? AND il.IsActive='Y'";
					
					PreparedStatement pstmtwcs = DB.prepareStatement( sqlwcs,
																	  get_TrxName());
					pstmtwcs.setInt(1, getC_Invoice_ID());
					ResultSet rswcs = pstmtwcs.executeQuery();
					int i = 0;
					int wcid = 0;
					boolean addedlines = false;
					
					while ( rswcs.next() ) {
						
						wcid = rswcs.getInt(1);
					
						if (wcid > 0) {
							
							if (i == 0) {
								
								sqlr.append(" AND LCO_WithholdingCategory_ID IN (");
								addedlines = true;
								
							} else {
								
								sqlr.append(",");
							}
							
							sqlr.append(wcid);
							i++;
						}
					}
					if (addedlines)
						sqlr.append(") ");
					
					rswcs.close();
					pstmtwcs.close();
				}
				
				//
				// Add tax categories of lines
				//
				
				if (wrc.isUseProductTaxCategory()) {
					
					//
					// look the conf fields
					//
					
					String sqlwct = "SELECT DISTINCT COALESCE (p.C_TaxCategory_ID, COALESCE (c.C_TaxCategory_ID, 0)) "
							+ "  FROM C_InvoiceLine il "
							+ "  LEFT OUTER JOIN M_Product p ON (il.M_Product_ID = p.M_Product_ID) "
							+ "  LEFT OUTER JOIN C_Charge c ON (il.C_Charge_ID = c.C_Charge_ID) "
							+ "  WHERE C_Invoice_ID = ? AND il.IsActive='Y'";
					
					PreparedStatement pstmtwct = DB.prepareStatement(sqlwct,
							get_TrxName());
					
					pstmtwct.setInt(1, getC_Invoice_ID());
					ResultSet rswct = pstmtwct.executeQuery();
					
					int i = 0;
					int wcid = 0;
					boolean addedlines = false;
					
					while (rswct.next()) {
						
						wcid = rswct.getInt(1);
						
						if (wcid > 0) {
							
							if (i == 0) {
							
								sqlr.append(" AND C_TaxCategory_ID IN (");
								addedlines = true;
							} else {
								
								sqlr.append(",");
							}
							
							sqlr.append(wcid);
							i++;
						}
					}
					
					if (addedlines)
						sqlr.append(") ");
					
					rswct.close();
					pstmtwct.close();
				}

				PreparedStatement pstmtr = DB.prepareStatement( sqlr.toString(),
																get_TrxName());
				int idxpar = 1;
				pstmtr.setInt(idxpar, wt.getLCO_WithholdingType_ID());
				idxpar++;
				pstmtr.setTimestamp(idxpar, getDateInvoiced());
				
				if (wrc.isUseBPISIC()) {
				
					idxpar++;
					pstmtr.setInt(idxpar, bp_isic_id);
				}
				
				if (wrc.isUseBPTaxPayerType()) {
				
					idxpar++;
					pstmtr.setInt(idxpar, bp_taxpayertype_id);
				}
				
				if (wrc.isUseOrgISIC()) {
					
					idxpar++;
					pstmtr.setInt(idxpar, org_isic_id);
				}
				
				if (wrc.isUseOrgTaxPayerType()) {
				
					idxpar++;
					pstmtr.setInt(idxpar, org_taxpayertype_id);
				}
				
				if (wrc.isUseBPCity()) {
				
					idxpar++;
					pstmtr.setInt(idxpar, bp_city_id);
					
					if (bp_city_id <= 0)
						log.warning("Possible configuration error bp city is used but not set");
				}
				
				if (wrc.isUseOrgCity()) {
				
					idxpar++;
					pstmtr.setInt(idxpar, org_city_id);
					if (org_city_id <= 0)
						log.warning("Possible configuration error org city is used but not set");
				}

				ResultSet rsr = pstmtr.executeQuery();
				
				while (rsr.next()) {
				
					// for each applicable rule
					X_LCO_WithholdingRule wr = new X_LCO_WithholdingRule(
							getCtx(), rsr.getInt(1), get_TrxName());

					// bring record for withholding calculation
					X_LCO_WithholdingCalc wc = new X_LCO_WithholdingCalc(
							getCtx(), wr.getLCO_WithholdingCalc_ID(),
							get_TrxName());
					if (wc.getLCO_WithholdingCalc_ID() == 0) {
						log.severe("Rule without calc " + rsr.getInt(1));
						continue;
					}
					
					// bring record for tax
					
					MTax tax = new MTax(getCtx(), wc.getC_Tax_ID(),
							get_TrxName());

					log.info("WithholdingRule: "
							+ wr.getLCO_WithholdingRule_ID() + "/"
							+ wr.getName() + " BaseType:" + wc.getBaseType()
							+ " Calc: " + wc.getLCO_WithholdingCalc_ID() + "/"
							+ wc.getName() + " CalcOnInvoice:"
							+ wc.isCalcOnInvoice() + " Tax: "
							+ tax.getC_Tax_ID() + "/" + tax.getName());

					// calc base
					// apply rule to calc base
					BigDecimal base = null;
					// ECS_LCO #1
					BigDecimal c_invoiceline_id = null;
					// ESC_LCO

					if ( wc.getBaseType() == null ) {
						
						log.severe("Base Type null in calc record "
								+ wr.getLCO_WithholdingCalc_ID());
						
					} else if (wc.getBaseType().equals(
						
							X_LCO_WithholdingCalc.BASETYPE_Document)) {

						// base = getTotalLines();

						base = getGrandTotal();

					} else if (wc.getBaseType().equals(
							X_LCO_WithholdingCalc.BASETYPE_Line)) {

						String sqllca;

						if (wrc.isUseWithholdingCategory()
								&& wrc.isUseProductTaxCategory()) {

							// base = lines of the withholding category and tax
							// category
							sqllca =
							// ECS_LCO #1
							// "SELECT SUM (LineNetAmt) "
							// "SELECT SUM (LineNetAmt), C_InvoiceLine_ID "
							// /TODO make modification after demo
							// ****************************************/
							"SELECT SUM (linetotalamt), C_InvoiceLine_ID "
									// ECS_LCO
									+ "  FROM C_InvoiceLine il "
									+ " WHERE IsActive='Y' AND C_Invoice_ID = ? "
									+ "   AND (   EXISTS ( "
									+ "              SELECT 1 "
									+ "                FROM M_Product p "
									+ "               WHERE il.M_Product_ID = p.M_Product_ID "
									+ "                 AND p.C_TaxCategory_ID = ? "
									+ "                 AND p.LCO_WithholdingCategory_ID = ?) "
									+ "        OR EXISTS ( "
									+ "              SELECT 1 "
									+ "                FROM C_Charge c "
									+ "               WHERE il.C_Charge_ID = c.C_Charge_ID "
									+ "                 AND c.C_TaxCategory_ID = ? "
									+ "                 AND c.LCO_WithholdingCategory_ID = ?) "
									// ECS_LCO #1
									// "+ " ) "; "
									+ "       ) Group by C_InvoiceLine_ID ";
							// ECS_LCO
						} else if (wrc.isUseWithholdingCategory()) {
							// base = lines of the withholding category
							sqllca =
							// ECS_LCO #1
							// "SELECT SUM (LineNetAmt) "
							"SELECT SUM (linetotalamt), C_InvoiceLine_ID "
									// ECS_LCO
									+ "  FROM C_InvoiceLine il "
									+ " WHERE IsActive='Y' AND C_Invoice_ID = ? "
									+ "   AND (   EXISTS ( "
									+ "              SELECT 1 "
									+ "                FROM M_Product p "
									+ "               WHERE il.M_Product_ID = p.M_Product_ID "
									+ "                 AND p.LCO_WithholdingCategory_ID = ?) "
									+ "        OR EXISTS ( "
									+ "              SELECT 1 "
									+ "                FROM C_Charge c "
									+ "               WHERE il.C_Charge_ID = c.C_Charge_ID "
									+ "                 AND c.LCO_WithholdingCategory_ID = ?) "
									// ECS_LCO #1
									// "+ " ) "; "
									+ "       ) Group by C_InvoiceLine_ID ";
							// ECS_LCO
						} else if (wrc.isUseProductTaxCategory()) {
							// base = lines of the product tax category
							sqllca =
							// ECS_LCO #1
							// "SELECT SUM (LineNetAmt) "
							"SELECT SUM (linetotalamt), C_InvoiceLine_ID "
									// ECS_LCO
									+ "  FROM C_InvoiceLine il "
									+ " WHERE IsActive='Y' AND C_Invoice_ID = ? "
									+ "   AND (   EXISTS ( "
									+ "              SELECT 1 "
									+ "                FROM M_Product p "
									+ "               WHERE il.M_Product_ID = p.M_Product_ID "
									+ "                 AND p.C_TaxCategory_ID = ?) "
									+ "        OR EXISTS ( "
									+ "              SELECT 1 "
									+ "                FROM C_Charge c "
									+ "               WHERE il.C_Charge_ID = c.C_Charge_ID "
									+ "                 AND c.C_TaxCategory_ID = ?) "
									// ECS_LCO #1
									// "+ " ) "; "
									+ "       ) Group by C_InvoiceLine_ID ";
							// ECS_LCO
						} else {
							// base = all lines
							// sqllca =
							// "SELECT SUM (LineNetAmt) "
							// + "  FROM C_InvoiceLine il "
							// + " WHERE IsActive='Y' AND C_Invoice_ID = ? ";

							sqllca = "SELECT SUM (linetotalamt) "
									+ "  FROM C_InvoiceLine il "
									+ " WHERE IsActive='Y' AND C_Invoice_ID = ? ";
						}

						PreparedStatement pstmtlca = DB.prepareStatement(
								sqllca, get_TrxName());
						pstmtlca.setInt(1, getC_Invoice_ID());
						if (wrc.isUseWithholdingCategory()
								&& wrc.isUseProductTaxCategory()) {
							pstmtlca.setInt(2, wr.getC_TaxCategory_ID());
							pstmtlca.setInt(3,
									wr.getLCO_WithholdingCategory_ID());
							pstmtlca.setInt(4, wr.getC_TaxCategory_ID());
							pstmtlca.setInt(5,
									wr.getLCO_WithholdingCategory_ID());
						} else if (wrc.isUseWithholdingCategory()) {
							pstmtlca.setInt(2,
									wr.getLCO_WithholdingCategory_ID());
							pstmtlca.setInt(3,
									wr.getLCO_WithholdingCategory_ID());
						} else if (wrc.isUseProductTaxCategory()) {
							pstmtlca.setInt(2, wr.getC_TaxCategory_ID());
							pstmtlca.setInt(3, wr.getC_TaxCategory_ID());
						} else {
							; // nothing
						}
						ResultSet rslca = pstmtlca.executeQuery();
						// ECS_LCO #1
						/*
						 * if (rslca.next() base = rslca.getBigDecimal(1);
						 */
						while (rslca.next()) {
							base = rslca.getBigDecimal(1);
							c_invoiceline_id = rslca.getBigDecimal(2);

							log.info("Base: " + base + " Invoice Line: "
									+ c_invoiceline_id + " Thresholdmin:"
									+ wc.getThresholdmin());
							// if base between thresholdmin and thresholdmax
							// inclusive
							// if thresholdmax = 0 it is ignored
							if (base != null
									&& base.compareTo(Env.ZERO) != 0
									&& base.compareTo(wc.getThresholdmin()) >= 0
									&& (wc.getThresholdMax() == null
											|| wc.getThresholdMax().compareTo(
													Env.ZERO) == 0 || base
											.compareTo(wc.getThresholdMax()) <= 0)
									&& tax.getRate() != null
									&& tax.getRate().compareTo(Env.ZERO) != 0) {

								BigDecimal taxamt = createWithholdingLines(
										base, c_invoiceline_id, wc, tax, wr, wt);

								totwith = totwith.add(taxamt);
								noins++;
							}

							// Reset base to null, so that it will not be
							// repeated again
							base = null;
							// ECS_LCO
						}
						rslca.close();
						pstmtlca.close();

					} else if (wc.getBaseType().equals(
							X_LCO_WithholdingCalc.BASETYPE_Tax)) {
						// if specific tax
						if (wc.getC_BaseTax_ID() != 0) {
							// base = value of specific tax
							String sqlbst = "SELECT SUM(TaxAmt) "
									+ " FROM C_InvoiceTax "
									+ " WHERE IsActive='Y' AND C_Invoice_ID = ? "
									+ "   AND C_Tax_ID = ?";
							PreparedStatement pstmtbst = DB.prepareStatement(
									sqlbst, get_TrxName());
							pstmtbst.setInt(1, getC_Invoice_ID());
							pstmtbst.setInt(2, wc.getC_BaseTax_ID());
							ResultSet rsbst = pstmtbst.executeQuery();
							if (rsbst.next())
								base = rsbst.getBigDecimal(1);
							rsbst.close();
							pstmtbst.close();
						} else {
							// not specific tax
							// base = value of all taxes
							String sqlbsat = "SELECT SUM(TaxAmt) "
									+ " FROM C_InvoiceTax "
									+ " WHERE IsActive='Y' AND C_Invoice_ID = ? ";
							PreparedStatement pstmtbsat = DB.prepareStatement(
									sqlbsat, get_TrxName());
							pstmtbsat.setInt(1, getC_Invoice_ID());
							ResultSet rsbsat = pstmtbsat.executeQuery();
							if (rsbsat.next())
								base = rsbsat.getBigDecimal(1);
							rsbsat.close();
							pstmtbsat.close();
						}

					}
					log.info("Base: " + base + " Thresholdmin:"
							+ wc.getThresholdmin());

					// if base between thresholdmin and thresholdmax inclusive
					// if thresholdmax = 0 it is ignored
					if (base != null
							&& base.compareTo(Env.ZERO) != 0
							&& base.compareTo(wc.getThresholdmin()) >= 0
							&& (wc.getThresholdMax() == null
									|| wc.getThresholdMax().compareTo(Env.ZERO) == 0 || base
									.compareTo(wc.getThresholdMax()) <= 0)
							&& tax.getRate() != null
							&& tax.getRate().compareTo(Env.ZERO) != 0) {

						// ECS_LCO #1 move the logic as new method.
						BigDecimal taxamt = createWithholdingLines(base,
								c_invoiceline_id, wc, tax, wr, wt);
						// ECS_LCO
						totwith = totwith.add(taxamt);
						noins++;
					}
				} // while each applicable rule

			} // while type
			LCO_MInvoice.updateHeaderWithholding(getC_Invoice_ID(),
					get_TrxName());
			save();

			rst.close();
			pstmtt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			return -1;
		}

		return noins;
	}
	
	// ECS_LCO #1
	/**
	 *	Create new withholding record
	 *	@return
	 */
	private BigDecimal createWithholdingLines(BigDecimal base, BigDecimal c_invoiceline_id, X_LCO_WithholdingCalc wc, 
			MTax tax, X_LCO_WithholdingRule wr, X_LCO_WithholdingType wt)
	{
		// insert new withholding record
		// with: type, tax, base amt, percent, tax amt, trx date, acct date, rule
		MLCOInvoiceWithholding iwh = new MLCOInvoiceWithholding(getCtx(), 0, get_TrxName());
		iwh.setAD_Org_ID(getAD_Org_ID());
		iwh.setC_Invoice_ID(getC_Invoice_ID());
		// ECS_LCO #1
		iwh.set_ValueOfColumn("C_InvoiceLine_ID", c_invoiceline_id);
		// ECS_LCO
		iwh.setDateAcct(getDateAcct());
		iwh.setDateTrx(getDateInvoiced());
		iwh.setIsCalcOnPayment( ! wc.isCalcOnInvoice() );
		iwh.setIsTaxIncluded(false);
		iwh.setLCO_WithholdingRule_ID(wr.getLCO_WithholdingRule_ID());
		iwh.setLCO_WithholdingType_ID(wt.getLCO_WithholdingType_ID());
		iwh.setC_Tax_ID(tax.getC_Tax_ID());
		iwh.setPercent(tax.getRate());
		iwh.setProcessed(false);
		int stdPrecision = MPriceList.getStandardPrecision(getCtx(), getM_PriceList_ID());
		BigDecimal taxamt = tax.calculateTax(base, false, stdPrecision , null);
		if (wc.getAmountRefunded() != null &&
				wc.getAmountRefunded().compareTo(Env.ZERO) > 0) {
			taxamt = taxamt.subtract(wc.getAmountRefunded());
		}
		iwh.setTaxAmt(taxamt);
		iwh.setTaxBaseAmt(base);
		iwh.save();
		
		log.info("LCO_InvoiceWithholding saved:"+iwh.getTaxAmt());
		
		return taxamt;
	}	
	// ECS_LCO
	
	/**
	 *	Update Withholding in Header
	 *	@return true if header updated with withholding
	 */
	public static boolean updateHeaderWithholding(int C_Invoice_ID, String trxName)
	{
		//	Update Invoice Header
		String sql = 
			"UPDATE C_Invoice "
			+ " SET WithholdingAmt="
				+ "(SELECT COALESCE(SUM(TaxAmt),0) FROM LCO_InvoiceWithholding iw WHERE iw.IsActive = 'Y' " +
						"AND iw.IsCalcOnPayment = 'N' AND C_Invoice.C_Invoice_ID=iw.C_Invoice_ID) "
			+ "WHERE C_Invoice_ID=?";
		int no = DB.executeUpdate(sql, C_Invoice_ID, trxName);

		return no == 1;
	}	//	updateHeaderWithholding

	/*
	 * Set Withholding Amount without Logging (via direct SQL UPDATE)
	 */
	public static boolean setWithholdingAmtWithoutLogging(MInvoice inv, BigDecimal wamt) {
		DB.executeUpdate("UPDATE C_Invoice SET WithholdingAmt=? WHERE C_Invoice_ID=?", 
				new Object[] {wamt, inv.getC_Invoice_ID()}, 
				true, 
				inv.get_TrxName());
		return true;
	}
	
	// Get base withholding amount
	public static BigDecimal getInvoiceBaseWithholding(int C_Invoice_ID) throws SQLException
	{
		BigDecimal baseAmt = Env.ZERO;		
		String sql = "Select SUM(TaxAmt) as TotalTaxAmt FROM LCO_InvoiceWithholding "
				+" Where IsActive = 'Y' AND IsCalcOnPayment = 'Y' AND Processed = 'N' "
				+" AND C_Invoice_ID=?";
	
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Invoice_ID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) 
				baseAmt = rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : Env.ZERO;
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return baseAmt;
	}
	
	// Get base withholding amount
	public static BigDecimal getPaymentBaseWithholding(int C_PaymentAllocate_ID) throws SQLException
	{
		BigDecimal baseAmt = Env.ZERO;		
		String sql = "Select SUM(InvoiceAmt * (percent/100)) as TotalTaxAmt "
			+ " FROM LCO_PaymentWithholding "
			+ " WHERE IsActive = 'Y' AND IsCalcOnPayment = 'Y' AND Processed = 'N' "
			+ " AND C_PaymentAllocate_ID=?";
	
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_PaymentAllocate_ID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) 
				baseAmt = rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : Env.ZERO;
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return baseAmt;
	}
	
	// Get base withholding amount
	public static BigDecimal getInvoiceBaseTax(int C_Invoice_ID) throws SQLException
	{
		BigDecimal baseAmt = Env.ZERO;		
		String sql = "Select SUM(TaxAmt) as TotalTaxAmt FROM C_InvoiceLine "
				+" Where IsActive = 'Y' "
				+" AND C_Invoice_ID=?";
	
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Invoice_ID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) 
				baseAmt = rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : Env.ZERO;
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return baseAmt;
	}

	// Get PercentOpen (invoice amount)
	public static BigDecimal getPercentInvoiceOpenAmt(int C_Invoice_ID, int C_InvoicePaySchedule_ID) throws SQLException
	{
		BigDecimal InvoiceOpen = Env.ZERO;	
		BigDecimal GrandTotal = Env.ZERO;	
		BigDecimal PercentOpen = Env.ZERO;	
		String sql = "SELECT invoiceOpen(C_Invoice_ID, ?), GrandTotal as percent_open  "
			+ "FROM C_Invoice WHERE C_Invoice_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_InvoicePaySchedule_ID);
			pstmt.setInt(2, C_Invoice_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				InvoiceOpen = rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : Env.ZERO;	
				GrandTotal = rs.getBigDecimal(2) != null ? rs.getBigDecimal(2) : Env.ZERO;	
				if (!GrandTotal.equals(Env.ZERO))
				{
					PercentOpen = InvoiceOpen.divide(GrandTotal, 6, BigDecimal.ROUND_HALF_UP);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return PercentOpen;
	}
	
	
	// Get Invoice Open Amount
	public static BigDecimal getInvoiceOpenAmt (int C_Invoice_ID, int C_InvoicePaySchedule_ID) throws SQLException
	{
		BigDecimal InvoiceOpen = Env.ZERO;	
		String sql = "SELECT invoiceOpen(C_Invoice_ID, ?)  "
			+ "FROM C_Invoice WHERE C_Invoice_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_InvoicePaySchedule_ID);
			pstmt.setInt(2, C_Invoice_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				InvoiceOpen = rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : Env.ZERO;	
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return InvoiceOpen;
	}
	
	// ECS_LCO
	

}	//	LCO_MInvoice
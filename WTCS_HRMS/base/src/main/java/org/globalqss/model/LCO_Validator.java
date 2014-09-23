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
 * Overwrite Version 14299
 * #1 	KTU: 	No need to call validateWriteOffVsPaymentWithholdings()
 * 				as new WithholdingAmt field is in use.
 * #2	KTU:	As we are using WithholdingAmt, no need to substract write-off for
 * 				Withholding, just use it directly.
 * #3 	KTU:	When save Payment Allocate copy Withholding record from invoice.
 * #4 	KTU:	Account Posting use amount from Payment Withholding (not Invoice Withholding)
 * #5	KTU:	Ensure that, the amount of withholding after View Allocation is correct
 * 
 * Change History
 * 
 * 
 * Task		Date			Change ID			Author			Change
 * 	
 * 1700		16/01/2012		201201170654		Ranjit			At invoice level we are using the "calculateTax" method of MTax.java
 * 																& here directly calculating the value of tax as per the rate of tax,
 * 																so using the same method to calculate the tax
 * 
 ******************************************************************************/

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.acct.Doc;
import org.compiere.acct.DocTax;
import org.compiere.acct.Fact;
import org.compiere.acct.FactLine;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MInvoiceTax;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.MPriceList;
import org.compiere.model.MTax;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.ecosoft.model.MLCOPaymentWithholding;
import org.ecosoft.model.X_LCO_PaymentWithholding;
import org.globalqss.util.LCO_Utils;

/**
 * Validator or Localization Colombia (Withholdings)
 * 
 * @author Carlos Ruiz - globalqss - Quality Systems & Solutions -
 *         http://globalqss.com
 * @version $Id: LCO_Validator.java 1009 2012-02-09 09:16:13Z suman $
 * 
 * 
 * 
 * 
 * 
 * Bug NO 		Author			ChangeID			Description
 * ********************************************************************************
 * 1660			Ranjit			201112070145		When sales order generated & AR Invoice Indirect
 * 													document type - TDS option is YES then the message 
 * 													for failed to generate TDS is changed as user friendly
 * 
 */
public class LCO_Validator implements ModelValidator {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LCO_Validator.java 1009 2012-02-09 09:16:13Z suman $";
	/**
	 * Constructor. The class is instantiated when logging in and client is
	 * selected/known
	 */
	public LCO_Validator() {
		super();
	} // MyValidator

	/** Logger */
	private static CLogger log = CLogger.getCLogger(LCO_Validator.class);
	/** Client */
	private int m_AD_Client_ID = -1;

	/**
	 * Initialize Validation
	 * 
	 * @param engine
	 *            validation engine
	 * @param client
	 *            client
	 */
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing global validator: " + this.toString());
		}

		// Tables to be monitored
		engine.addModelChange(MInvoice.Table_Name, this);
		engine.addModelChange(MInvoiceLine.Table_Name, this);
		engine.addModelChange(MBPartner.Table_Name, this);
		engine.addModelChange(X_LCO_TaxIdType.Table_Name, this);
		engine.addModelChange(X_LCO_WithholdingCalc.Table_Name, this);
		engine.addModelChange(MPaymentAllocate.Table_Name, this);

		// Documents to be monitored
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addDocValidate(MPayment.Table_Name, this);
		engine.addDocValidate(MAllocationHdr.Table_Name, this);

	} // initialize

	/**
	 * Model Change of a monitored Table. Called after
	 * PO.beforeSave/PO.beforeDelete when you called addModelChange for the
	 * table
	 * 
	 * @param po
	 *            persistent object
	 * @param type
	 *            TYPE_
	 * @return error message or null
	 * @exception Exception
	 *                if the recipient wishes the change to be not accept.
	 */
	public String modelChange(PO po, int type) throws Exception {
		log.info(po.get_TableName() + " Type: " + type);
		String msg;

		if (po.get_TableName().equals(MInvoice.Table_Name)
				&& type == ModelValidator.TYPE_BEFORE_CHANGE) {
			msg = clearInvoiceWithholdingAmtFromInvoice((MInvoice) po);
			if (msg != null)
				return msg;
		}

		// when invoiceline is changed clear the withholding amount on invoice
		// in order to force a regeneration
		if (po.get_TableName().equals(MInvoiceLine.Table_Name)
				&& (type == ModelValidator.TYPE_BEFORE_NEW
						|| type == ModelValidator.TYPE_BEFORE_CHANGE || type == ModelValidator.TYPE_BEFORE_DELETE)) {
			msg = clearInvoiceWithholdingAmtFromInvoiceLine((MInvoiceLine) po,
					type);
			if (msg != null)
				return msg;
		}

		// Check Digit based on TaxID
		if (po.get_TableName().equals(MBPartner.Table_Name)
				&& (type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)) {
			MBPartner bpartner = (MBPartner) po;
			msg = mcheckTaxIdDigit(bpartner);
			if (msg != null)
				return msg;

			msg = mfillName(bpartner);
			if (msg != null)
				return msg;
		}

		if (po.get_TableName().equals(X_LCO_TaxIdType.Table_Name)
				&& (type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)) {
			X_LCO_TaxIdType taxidtype = (X_LCO_TaxIdType) po;
			if ((!taxidtype.isUseTaxIdDigit()) && taxidtype.isDigitChecked())
				taxidtype.setIsDigitChecked(false);
		}

		if (po.get_TableName().equals(X_LCO_WithholdingCalc.Table_Name)
				&& (type == ModelValidator.TYPE_BEFORE_CHANGE || type == ModelValidator.TYPE_BEFORE_NEW)) {
			X_LCO_WithholdingCalc lwc = (X_LCO_WithholdingCalc) po;
			if (lwc.isCalcOnInvoice() && lwc.isCalcOnPayment())
				lwc.setIsCalcOnPayment(false);
		}

		// Copy record Withholding Record from Invoice.
		if (po.get_TableName().equals(MPaymentAllocate.Table_Name)
				&& (type == ModelValidator.TYPE_AFTER_NEW
						|| type == ModelValidator.TYPE_AFTER_CHANGE
						|| type == ModelValidator.TYPE_BEFORE_CHANGE || type == ModelValidator.TYPE_BEFORE_DELETE)) {
			msg = updatePaymentWithholdings((MPaymentAllocate) po, type);
			if (msg != null)
				return msg;
		}

		return null;
	} // modelChange

	private String updatePaymentWithholdings(MPaymentAllocate pal, int type) {

		// New record, just copy over from invoice withholding
		if (type == ModelValidator.TYPE_AFTER_NEW
				|| (type == ModelValidator.TYPE_AFTER_CHANGE && pal
						.is_ValueChanged("C_Invoice_ID"))) {

			// Get inv_id
			int C_Invoice_ID = pal.getC_Invoice_ID();

			if (C_Invoice_ID > 0) {

				int pricelist_id = DB
						.getSQLValue(
								null,
								"SELECT M_PriceList_ID FROM C_Invoice WHERE C_Invoice_ID=?",
								C_Invoice_ID);
				int stdPrecision = MPriceList.getStandardPrecision(
						pal.getCtx(), pricelist_id);

				String sql = "SELECT LCO_InvoiceWithholding_ID "
						+ " FROM LCO_InvoiceWithholding "
						+ " WHERE C_Invoice_ID = ? AND IsActive = 'Y' "
						+ " ORDER BY LCO_InvoiceWithholding_ID";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(sql, pal.get_TrxName());
					pstmt.setInt(1, C_Invoice_ID);
					rs = pstmt.executeQuery();
					while (rs.next()) {

						MLCOInvoiceWithholding pwh = new MLCOInvoiceWithholding(
								pal.getCtx(), rs.getInt(1), pal.get_TrxName());
						X_LCO_PaymentWithholding newpwh = new X_LCO_PaymentWithholding(
								pal.getCtx(), 0, pal.get_TrxName());
						MPayment pay = new MPayment(pal.getCtx(),
								pal.getC_Payment_ID(), pal.get_TrxName());
						newpwh.setC_Invoice_ID(pwh.getC_Invoice_ID());
						newpwh.setC_InvoiceLine_ID(pwh.getC_InvoiceLine_ID());
						newpwh.setC_PaymentAllocate_ID(pal
								.getC_PaymentAllocate_ID());
						newpwh.setC_Tax_ID(pwh.getC_Tax_ID());
						newpwh.setDateAcct(pay.getDateAcct()); // Use date from
																// Payment
						newpwh.setDateTrx(pay.getDateTrx()); // Use date from
																// Payment
						newpwh.setIsActive(pwh.isActive());
						newpwh.setProcessed(pwh.isProcessed());
						newpwh.setIsCalcOnPayment(pwh.isCalcOnPayment());
						newpwh.setIsTaxIncluded(pwh.isTaxIncluded());
						newpwh.setLCO_WithholdingRule_ID(pwh
								.getLCO_WithholdingRule_ID());
						newpwh.setLCO_WithholdingType_ID(pwh
								.getLCO_WithholdingType_ID());
						newpwh.setPercent(pwh.getPercent());
						newpwh.setTaxAmt((pwh.getTaxAmt()).setScale(
								stdPrecision, BigDecimal.ROUND_HALF_UP));
						newpwh.setTaxBaseAmt((pwh.getTaxBaseAmt()).setScale(
								stdPrecision, BigDecimal.ROUND_HALF_UP));
						newpwh.setInvoiceAmt((pwh.getTaxBaseAmt()).setScale(
								stdPrecision, BigDecimal.ROUND_HALF_UP)); // Initial
																			// with
																			// Tax
																			// Base
																			// Amt

						if (!newpwh.save())
							return "Error saving LCO_PaymentWithholding modelChange";
					}
				} catch (Exception e) {
					log.log(Level.SEVERE, sql, e);
					return "Error creating LCO_PaymentWithholding when create new line";
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
			}
		}

		// New record or withholding amount changed (but same invoice), update
		// the PaymentWithholdings
		// 1) When new record, always update for the right percentage of payment
		// 2) When change the OverUnderAmt (which reflect percentage of payment)
		if (type == ModelValidator.TYPE_AFTER_NEW
				|| (type == ModelValidator.TYPE_AFTER_CHANGE && pal
						.is_ValueChanged("OverUnderAmt"))) {

			int pricelist_id = DB
					.getSQLValue(
							null,
							"SELECT M_PriceList_ID FROM C_Invoice WHERE C_Invoice_ID=?",
							pal.getC_Invoice_ID());
			int stdPrecision = MPriceList.getStandardPrecision(pal.getCtx(),
					pricelist_id);

			String sql = "SELECT LCO_PaymentWithholding_ID "
					+ "FROM LCO_PaymentWithholding "
					+ "WHERE C_PaymentAllocate_ID = ? " + "AND IsActive = 'Y' ";
			PreparedStatement pstmt = DB.prepareStatement(sql,
					pal.get_TrxName());
			ResultSet rs = null;

			try {

				// Get Percent Open of the current invoice
				BigDecimal percentOpen = LCO_MInvoice.getPercentInvoiceOpenAmt(
						pal.getC_Invoice_ID(), 0);
				// Get Percent payment

				BigDecimal invoiceAmt = pal.getInvoiceAmt();

				BigDecimal percent_payment = Env.ONE;

				if (null != invoiceAmt
						&& invoiceAmt.intValue() > Env.ZERO.intValue()) {

					percent_payment = (pal.getInvoiceAmt().subtract(pal
							.getOverUnderAmt())).divide(pal.getInvoiceAmt(), 6,
							BigDecimal.ROUND_HALF_UP);
				}

				pstmt.setInt(1, pal.getC_PaymentAllocate_ID());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					int pwhid = rs.getInt(1);
					X_LCO_PaymentWithholding pwh = new X_LCO_PaymentWithholding( // ECS_LCO
							pal.getCtx(), pwhid, pal.get_TrxName());

					// 1. Get Tax Percent
					MTax tax = new MTax(pal.getCtx(), pwh.getC_Tax_ID(),
							pal.get_TrxName());
					BigDecimal rate_percent = tax.getRate().divide(
							Env.ONEHUNDRED);

					// 2. Get Base Invoice Line Amt, only set when it first
					// created. next update, we can ignore as we use Amt from
					// itself.
					if (type == ModelValidator.TYPE_AFTER_NEW) {
						BigDecimal invAmt = pwh.getInvoiceAmt().multiply(
								percentOpen);
						pwh.setInvoiceAmt(invAmt.setScale(stdPrecision,
								BigDecimal.ROUND_HALF_UP));
					}

					// 3. Get Tax Amount = Tax Base Amount X Tax Rate X Percent
					// Payment (invoice amount)
					BigDecimal taxBaseAmt = pwh.getInvoiceAmt().multiply(
							percent_payment);
					
					//BigDecimal taxAmt = taxBaseAmt.multiply(rate_percent);
					
					// 201201170654 [Change ID]
					BigDecimal taxAmt = tax.calculateTax(taxBaseAmt, false, stdPrecision , null);

					// 4. update Payment Withholding line
					pwh.setTaxBaseAmt(taxBaseAmt.setScale(stdPrecision,
							BigDecimal.ROUND_HALF_UP));
					pwh.setTaxAmt(taxAmt.setScale(stdPrecision,
							BigDecimal.ROUND_HALF_UP));

					if (!pwh.save())
						return "Error saving LCO_PaymentWithholding modelChange"; // ECS_LCO
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return e.getLocalizedMessage();
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		}

		// Delete record, delete the PaymentWithholdings
		// 1) When delete the Payment Allocate record
		// 2) When about to change the Invoice ID
		if (type == ModelValidator.TYPE_BEFORE_DELETE
				|| (type == ModelValidator.TYPE_BEFORE_CHANGE && pal
						.is_ValueChanged("C_Invoice_ID"))) {

			String sqldel = "DELETE FROM LCO_PaymentWithholding "
					+ " WHERE C_PaymentAllocate_ID = ?";
			PreparedStatement pstmtdel = null;
			try {
				// Delete previous records generated
				pstmtdel = DB.prepareStatement(sqldel,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE, pal.get_TrxName());
				pstmtdel.setInt(1, pal.getC_PaymentAllocate_ID());
				int nodel = pstmtdel.executeUpdate();
				log.config("LCO_PaymentWithholding deleted=" + nodel);
			} catch (Exception e) {
				log.log(Level.SEVERE, sqldel, e);
				return "Error deleting LCO_PaymentWithholding ";
			} finally {
				DB.close(pstmtdel);
				pstmtdel = null;
			}
		}

		return null;
	}

	private String clearInvoiceWithholdingAmtFromInvoice(MInvoice inv) {
		// Clear invoice withholding amount

		if (inv.is_ValueChanged("AD_Org_ID")
				|| inv.is_ValueChanged(MInvoice.COLUMNNAME_C_BPartner_ID)
				|| inv.is_ValueChanged(MInvoice.COLUMNNAME_C_DocTypeTarget_ID)) {

			boolean thereAreCalc;
			try {
				thereAreCalc = thereAreCalc(inv);
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"Error looking for calc on invoice rules", e);
				return "Error looking for calc on invoice rules";
			}

			BigDecimal curWithholdingAmt = (BigDecimal) inv
					.get_Value("WithholdingAmt");
			if (thereAreCalc) {
				if (curWithholdingAmt != null) {
					inv.set_CustomColumn("WithholdingAmt", null);
				}
			} else {
				if (curWithholdingAmt == null) {
					inv.set_CustomColumn("WithholdingAmt", Env.ZERO);
				}
			}
		}

		return null;
	}

	private String clearInvoiceWithholdingAmtFromInvoiceLine(
			MInvoiceLine invline, int type) {

		if (type == ModelValidator.TYPE_BEFORE_NEW
				|| type == ModelValidator.TYPE_BEFORE_DELETE
				|| (type == ModelValidator.TYPE_BEFORE_CHANGE && (invline
						.is_ValueChanged("LineNetAmt")
						|| invline.is_ValueChanged("M_Product_ID")
						|| invline.is_ValueChanged("C_Charge_ID")
						|| invline.is_ValueChanged("IsActive") || invline
						.is_ValueChanged("C_Tax_ID")))) {
			// Clear invoice withholding amount
			MInvoice inv = invline.getParent();

			boolean thereAreCalc;
			try {
				thereAreCalc = thereAreCalc(inv);
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"Error looking for calc on invoice rules", e);
				return "Error looking for calc on invoice rules";
			}

			BigDecimal curWithholdingAmt = (BigDecimal) inv
					.get_Value("WithholdingAmt");
			if (thereAreCalc) {
				if (curWithholdingAmt != null) {
					if (!LCO_MInvoice
							.setWithholdingAmtWithoutLogging(inv, null))
						return "Error saving C_Invoice clearInvoiceWithholdingAmtFromInvoiceLine";
				}
			} else {
				if (curWithholdingAmt == null) {
					if (!LCO_MInvoice.setWithholdingAmtWithoutLogging(inv,
							Env.ZERO))
						return "Error saving C_Invoice clearInvoiceWithholdingAmtFromInvoiceLine";
				}
			}
		}

		return null;
	}

	private boolean thereAreCalc(MInvoice inv) throws SQLException {
		boolean thereAreCalc = false;
		String sqlwccoi = "SELECT 1 "
				+ "  FROM LCO_WithholdingType wt, LCO_WithholdingCalc wc "
				+ " WHERE wt.LCO_WithholdingType_ID = wc.LCO_WithholdingType_ID";
		PreparedStatement pstmtwccoi = DB.prepareStatement(sqlwccoi,
				inv.get_TrxName());
		ResultSet rswccoi = null;
		try {
			rswccoi = pstmtwccoi.executeQuery();
			if (rswccoi.next())
				thereAreCalc = true;
		} catch (SQLException e) {
			throw e;
		} finally {
			DB.close(rswccoi, pstmtwccoi);
			rswccoi = null;
			pstmtwccoi = null;
		}
		return thereAreCalc;
	}

	/**
	 * Validate Document. Called as first step of DocAction.prepareIt when you
	 * called addDocValidate for the table. Note that totals, etc. may not be
	 * correct.
	 * 
	 * @param po
	 *            persistent object
	 * @param timing
	 *            see TIMING_ constants
	 * @return error message or null
	 */
	public String docValidate(PO po, int timing) {
		log.info(po.get_TableName() + " Timing: " + timing);
		String msg;

		// before preparing a reversal invoice add the invoice withholding taxes
		if (po.get_TableName().equals(MInvoice.Table_Name)
				&& timing == TIMING_BEFORE_PREPARE) {
			MInvoice inv = (MInvoice) po;
			if (inv.isReversal()) {
				int invid = inv.getReversal_ID();

				if (invid > 0) {
					MInvoice invreverted = new MInvoice(inv.getCtx(), invid,
							inv.get_TrxName());
					String sql = "SELECT LCO_InvoiceWithholding_ID "
							+ " FROM LCO_InvoiceWithholding "
							+ " WHERE C_Invoice_ID = ? "
							+ " ORDER BY LCO_InvoiceWithholding_ID";
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(sql, inv.get_TrxName());
						pstmt.setInt(1, invreverted.getC_Invoice_ID());
						rs = pstmt.executeQuery();
						while (rs.next()) {
							MLCOInvoiceWithholding iwh = new MLCOInvoiceWithholding(
									inv.getCtx(), rs.getInt(1),
									inv.get_TrxName());
							MLCOInvoiceWithholding newiwh = new MLCOInvoiceWithholding(
									inv.getCtx(), 0, inv.get_TrxName());
							newiwh.setC_Invoice_ID(inv.getC_Invoice_ID());
							newiwh.setLCO_WithholdingType_ID(iwh
									.getLCO_WithholdingType_ID());
							newiwh.setPercent(iwh.getPercent());
							newiwh.setTaxAmt(iwh.getTaxAmt().negate());
							newiwh.setTaxBaseAmt(iwh.getTaxBaseAmt().negate());
							newiwh.setC_Tax_ID(iwh.getC_Tax_ID());
							if (!newiwh.save())
								return "Error saving LCO_InvoiceWithholding docValidate";
						}
					} catch (Exception e) {
						log.log(Level.SEVERE, sql, e);
						return "Error creating LCO_InvoiceWithholding for reversal invoice";
					} finally {
						DB.close(rs, pstmt);
						rs = null;
						pstmt = null;
					}
				} else {
					return "Can't get the number of the invoice reversed";
				}
			}
		}

		// before preparing invoice validate if withholdings has been generated
		if (po.get_TableName().equals(MInvoice.Table_Name)
				&& timing == TIMING_BEFORE_PREPARE) {
			MInvoice inv = (MInvoice) po;
			/* @TODO: Change this to IsReversal & Reversal_ID on 3.5 */
			if (inv.getDescription() != null
					&& inv.getDescription().contains("{->")
					&& inv.getDescription().endsWith(")")) {
				// don't validate this for autogenerated reversal invoices
			} else {
				if (inv.get_Value("WithholdingAmt") == null) {
					MDocType dt = new MDocType(inv.getCtx(),
							inv.getC_DocTypeTarget_ID(), inv.get_TrxName());
					String genwh = dt.get_ValueAsString("GenerateWithholding");
					if (genwh != null) {

						//
						// If the Business partner is applicable for the TDS
						// then
						// only calculate the TDS else No TDS calculation
						//

						boolean tdsAppliable = inv.getC_BPartner().istdsapplicable();
						
						if(tdsAppliable && genwh.equals("Y") && inv.getC_Order_ID() > 0) {
							
							return Msg.getMsg(inv.getCtx(),EagleConstants.TDS_NOTGENERATED_FORSALESORDER,new Object[] {dt.getName()});
						}

						if ( tdsAppliable &&  genwh.equals("Y") && inv.getC_Order_ID() <= 0) {

							// document type configured to compel generation of
							// withholdings

							return Msg.getMsg(inv.getCtx(),"LCO_WithholdingNotGenerated");
						}
					
						if (tdsAppliable && genwh.equals("A")) {

							// document type configured to generate withholdings
							// automatically
							
							LCO_MInvoice lcoinv = new  LCO_MInvoice(inv.getCtx(), inv.getC_Invoice_ID(),
									inv.get_TrxName());
							 
							 lcoinv.recalcWithholdings(dt.getLCO_WithholdingType_ID());
						}
					}
				}
			}
		}

		// after preparing invoice move invoice withholdings to taxes and recalc
		// grandtotal of invoice
		if (po.get_TableName().equals(MInvoice.Table_Name)
				&& timing == TIMING_BEFORE_COMPLETE) {
			msg = translateWithholdingToTaxes((MInvoice) po);
			if (msg != null)
				return msg;
		}

		// after completing the invoice fix the dates on withholdings and mark
		// the invoice withholdings as processed
		if (po.get_TableName().equals(MInvoice.Table_Name)
				&& timing == TIMING_AFTER_COMPLETE) {
			msg = completeInvoiceWithholding((MInvoice) po);
			if (msg != null)
				return msg;
		}

		// ECS_LCO #1 As Withholding field is in use, we do not need to put
		// Withholding on Write-off, so no need to validate
		// before completing the payment - validate that writeoff amount must be
		// greater than sum of payment withholdings
		/*
		 * if (po.get_TableName().equals(MPayment.Table_Name) && timing ==
		 * TIMING_BEFORE_COMPLETE) { msg =
		 * validateWriteOffVsPaymentWithholdings((MPayment) po); if (msg !=
		 * null) return msg; }
		 */
		// ECS_LCO

		// after completing the allocation - complete the payment withholdings
		if (po.get_TableName().equals(MAllocationHdr.Table_Name)
				&& timing == TIMING_AFTER_COMPLETE) {
			msg = completePaymentWithholdings((MAllocationHdr) po);
			if (msg != null)
				return msg;
		}

		// before posting the allocation - post the payment withholdings vs
		// writeoff amount
		if (po.get_TableName().equals(MAllocationHdr.Table_Name)
				&& timing == TIMING_BEFORE_POST) {
			msg = accountingForInvoiceWithholdingOnPayment((MAllocationHdr) po);
			if (msg != null)
				return msg;
		}

		// after completing the allocation - complete the payment withholdings
		if (po.get_TableName().equals(MAllocationHdr.Table_Name)
				&& (timing == TIMING_AFTER_VOID
						|| timing == TIMING_AFTER_REVERSECORRECT || timing == TIMING_AFTER_REVERSEACCRUAL)) {
			msg = reversePaymentWithholdings((MAllocationHdr) po);
			if (msg != null)
				return msg;
		}

		return null;
	} // docValidate

	private String validateWriteOffVsPaymentWithholdings(MPayment pay) {
		if (pay.getC_Invoice_ID() > 0) {
			// validate vs invoice of payment
			BigDecimal wo = pay.getWriteOffAmt();
			BigDecimal sumwhamt = Env.ZERO;
			sumwhamt = DB.getSQLValueBD(pay.get_TrxName(),
					"SELECT COALESCE (SUM (TaxAmt), 0) "
							+ "FROM LCO_InvoiceWithholding "
							+ "WHERE C_Invoice_ID = ? AND "
							+ "IsActive = 'Y' AND "
							+ "IsCalcOnPayment = 'Y' AND "
							+ "Processed = 'N' AND "
							+ "C_AllocationLine_ID IS NULL",
					pay.getC_Invoice_ID());
			if (sumwhamt == null)
				sumwhamt = Env.ZERO;
			if (wo.compareTo(sumwhamt) < 0 && sumwhamt.compareTo(Env.ZERO) != 0)
				return Msg.getMsg(pay.getCtx(),
						"LCO_WriteOffLowerThanWithholdings");
		} else {
			// validate every C_PaymentAllocate
			String sql = "SELECT C_PaymentAllocate_ID "
					+ "FROM C_PaymentAllocate " + "WHERE C_Payment_ID = ?";
			PreparedStatement pstmt = DB.prepareStatement(sql,
					pay.get_TrxName());
			ResultSet rs = null;
			try {
				pstmt.setInt(1, pay.getC_Payment_ID());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					int palid = rs.getInt(1);
					MPaymentAllocate pal = new MPaymentAllocate(pay.getCtx(),
							palid, pay.get_TrxName());
					BigDecimal wo = pal.getWriteOffAmt();
					BigDecimal sumwhamt = Env.ZERO;
					sumwhamt = DB.getSQLValueBD(pay.get_TrxName(),
							"SELECT COALESCE (SUM (TaxAmt), 0) "
									+ "FROM LCO_InvoiceWithholding "
									+ "WHERE C_Invoice_ID = ? AND "
									+ "IsActive = 'Y' AND "
									+ "IsCalcOnPayment = 'Y' AND "
									+ "Processed = 'N' AND "
									+ "C_AllocationLine_ID IS NULL",
							pal.getC_Invoice_ID());
					if (sumwhamt == null)
						sumwhamt = Env.ZERO;
					if (wo.compareTo(sumwhamt) < 0
							&& sumwhamt.compareTo(Env.ZERO) != 0)
						return Msg.getMsg(pay.getCtx(),
								"LCO_WriteOffLowerThanWithholdings");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return e.getLocalizedMessage();
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		}

		return null;
	}

	private String completePaymentWithholdings(MAllocationHdr ah) {
		MAllocationLine[] als = ah.getLines(true);
		for (int i = 0; i < als.length; i++) {

			MAllocationLine al = als[i];

			if (al.getC_Invoice_ID() > 0) {

				// Get payment / invoice object
				MPayment pay = new MPayment(ah.getCtx(), al.getC_Payment_ID(),
						ah.get_TrxName());
				MInvoice inv = new MInvoice(ah.getCtx(), al.getC_Invoice_ID(),
						ah.get_TrxName());

				// 1. Update PaymentWithholding
				String sql = "SELECT LCO_PaymentWithholding_ID FROM LCO_PaymentWithholding "
						+ "WHERE C_Invoice_ID = ? AND IsActive = 'Y' AND IsCalcOnPayment = 'Y' AND "
						+ "Processed = 'N' AND C_AllocationLine_ID IS NULL";
				PreparedStatement pstmt = DB.prepareStatement(sql,
						ah.get_TrxName());
				ResultSet rs = null;
				try {
					pstmt.setInt(1, al.getC_Invoice_ID());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						int iwhid = rs.getInt(1);
						MLCOPaymentWithholding iwh = new MLCOPaymentWithholding(
								ah.getCtx(), iwhid, ah.get_TrxName());
						iwh.setC_AllocationLine_ID(al.getC_AllocationLine_ID());
						iwh.setDateAcct(ah.getDateAcct());
						iwh.setDateTrx(ah.getDateTrx());
						iwh.setProcessed(true);
						if (!iwh.save())
							return "Error saving LCO_PaymentWithholding completePaymentWithholdings";
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return e.getLocalizedMessage();
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}

				// 2. Update WithholdingAmt in C_AllocationLine since the 360lts
				// did not do it yet.
				sql = "SELECT SUM(TaxAmt) AS WithholdingAmt"
						+ "	FROM LCO_PaymentWithholding WHERE C_AllocationLine_ID=?";
				pstmt = DB.prepareStatement(sql, ah.get_TrxName());
				rs = null;
				try {
					pstmt.setInt(1, al.getC_AllocationLine_ID());
					rs = pstmt.executeQuery();
					if (rs.next()) {

						BigDecimal WithholdingAmt = rs.getBigDecimal(1);
						WithholdingAmt = WithholdingAmt == null ? Env.ZERO
								: WithholdingAmt;

						if (!pay.isReceipt())
							WithholdingAmt = WithholdingAmt.negate();

						al.set_CustomColumn("WithholdingAmt", WithholdingAmt);

						if (!al.save())
							return "Error updating WithholdingAmt in Allocation Lines";
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return e.getLocalizedMessage();
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}

				// 3. Set Invoice.IsPaid()
				this.testAllocation(inv);
				if (!inv.save())
					return "Error saving IsPaid in completePaymentWithholdings";
			}
		}
		return null;
	}

	private String reversePaymentWithholdings(MAllocationHdr ah) {
		MAllocationLine[] als = ah.getLines(true);
		for (int i = 0; i < als.length; i++) {
			MAllocationLine al = als[i];
			if (al.getC_Invoice_ID() > 0) {
				String sql = "SELECT LCO_PaymentWithholding_ID "
						+ // ECS_LCO
						"FROM LCO_PaymentWithholding "
						+ "WHERE C_Invoice_ID = ? AND " + "IsActive = 'Y' AND "
						+ "IsCalcOnPayment = 'Y' AND " + "Processed = 'Y' AND "
						+ "C_AllocationLine_ID = ?";
				PreparedStatement pstmt = DB.prepareStatement(sql,
						ah.get_TrxName());
				ResultSet rs = null;
				try {
					pstmt.setInt(1, al.getC_Invoice_ID());
					pstmt.setInt(2, al.getC_AllocationLine_ID());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						int iwhid = rs.getInt(1);
						MLCOPaymentWithholding iwh = new MLCOPaymentWithholding( // ECS_LCO
								ah.getCtx(), iwhid, ah.get_TrxName());
						iwh.setC_AllocationLine_ID(0);
						iwh.setProcessed(false);
						if (!iwh.save())
							return "Error saving LCO_PaymentWithholding reversePaymentWithholdings"; // ECS_LCO
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return e.getLocalizedMessage();
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
			}
		}
		return null;
	}

	private String accountingForInvoiceWithholdingOnPayment(MAllocationHdr ah) {

		Doc doc = ah.getDoc();

		ArrayList<Fact> facts = doc.getFacts();
		// one fact per acctschema
		for (int i = 0; i < facts.size(); i++) {
			Fact fact = facts.get(i);
			MAcctSchema as = fact.getAcctSchema();

			MAllocationLine[] alloc_lines = ah.getLines(false);
			for (int j = 0; j < alloc_lines.length; j++) {
				BigDecimal tottax = new BigDecimal(0);

				MAllocationLine alloc_line = alloc_lines[j];
				doc.setC_BPartner_ID(alloc_line.getC_BPartner_ID());

				int inv_id = alloc_line.getC_Invoice_ID();
				if (inv_id <= 0)
					continue;
				MInvoice invoice = null;
				invoice = new MInvoice(ah.getCtx(),
						alloc_line.getC_Invoice_ID(), ah.get_TrxName());
				if (invoice == null)
					continue;
				String sql = "SELECT i.C_Tax_ID, NVL(SUM(i.TaxBaseAmt),0) AS TaxBaseAmt, NVL(SUM(i.TaxAmt),0) AS TaxAmt, t.Name, t.Rate, t.IsSalesTax "
						+ " FROM LCO_PaymentWithholding i, C_Tax t "
						+ " WHERE i.C_Invoice_ID = ? AND "
						+ "i.IsCalcOnPayment = 'Y' AND "
						+ "i.IsActive = 'Y' AND "
						+ "i.Processed = 'Y' AND "
						+ "i.C_AllocationLine_ID = ? AND "
						+ "i.C_Tax_ID = t.C_Tax_ID "
						+ "GROUP BY i.C_Tax_ID, t.Name, t.Rate, t.IsSalesTax";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(sql, ah.get_TrxName());
					pstmt.setInt(1, invoice.getC_Invoice_ID());
					pstmt.setInt(2, alloc_line.getC_AllocationLine_ID());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						int tax_ID = rs.getInt(1);
						BigDecimal taxBaseAmt = rs.getBigDecimal(2);
						BigDecimal amount = rs.getBigDecimal(3);
						String name = rs.getString(4);
						BigDecimal rate = rs.getBigDecimal(5);
						boolean salesTax = rs.getString(6).equals("Y") ? true
								: false;

						DocTax taxLine = new DocTax(tax_ID, name, rate,
								taxBaseAmt, amount, salesTax);

						if (amount != null && amount.signum() != 0) {
							FactLine tl = null;
							if (invoice.isSOTrx()) {
								tl = fact.createLine(null, taxLine.getAccount(
										DocTax.ACCTTYPE_TaxDue, as), as
										.getC_Currency_ID(), amount, null);
							} else {
								tl = fact.createLine(
										null,
										taxLine.getAccount(
												taxLine.getAPTaxType(), as),
										as.getC_Currency_ID(), null, amount);
							}
							if (tl != null)
								tl.setC_Tax_ID(taxLine.getC_Tax_ID());
							tottax = tottax.add(amount);
						}
					}
				} catch (Exception e) {
					log.log(Level.SEVERE, sql, e);
					return "Error posting C_InvoiceTax from LCO_PaymentWithholding";
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}

				// Write off DR
				if (Env.ZERO.compareTo(tottax) != 0) {
					// First try to find the WriteOff posting record
					FactLine[] factlines = fact.getLines();
					boolean foundflwriteoff = false;
					for (int ifl = 0; ifl < factlines.length; ifl++) {
						FactLine fl = factlines[ifl];
						// ECS_LCO #2
						// if
						// (fl.getAccount().equals(doc.getAccount(Doc.ACCTTYPE_WriteOff,
						// as))) {
						// ECS_LCO
						foundflwriteoff = true;
						// old balance = DB - CR
						BigDecimal balamt = fl.getAmtSourceDr().subtract(
								fl.getAmtSourceCr());
						// new balance = old balance +/- tottax
						BigDecimal newbalamt = Env.ZERO;
						if (invoice.isSOTrx())
							newbalamt = balamt.subtract(tottax);
						else
							newbalamt = balamt.add(tottax);
						if (Env.ZERO.compareTo(newbalamt) == 0) {
							// both zeros, remove the line
							fact.remove(fl);
						} else if (Env.ZERO.compareTo(newbalamt) > 0) {
							fl.setAmtAcct(fl.getC_Currency_ID(), Env.ZERO,
									newbalamt);
							fl.setAmtSource(fl.getC_Currency_ID(), Env.ZERO,
									newbalamt);
						} else {
							fl.setAmtAcct(fl.getC_Currency_ID(), newbalamt,
									Env.ZERO);
							fl.setAmtSource(fl.getC_Currency_ID(), newbalamt,
									Env.ZERO);
						}
						break;
						// ECS_LCO #2
						// }
						// ECS_LCO
					}
					// ECS_LCO #2 - No need to create new write off line as we
					// never deleted them
					/*
					 * if (! foundflwriteoff) { // Create a new line DocLine
					 * line = new DocLine(alloc_line, doc); FactLine fl = null;
					 * if (invoice.isSOTrx()) { fl = fact.createLine (line,
					 * doc.getAccount(Doc.ACCTTYPE_WriteOff, as),
					 * as.getC_Currency_ID(), null, tottax); } else { fl =
					 * fact.createLine (line,
					 * doc.getAccount(Doc.ACCTTYPE_WriteOff, as),
					 * as.getC_Currency_ID(), tottax, null); } if (fl != null)
					 * fl.setAD_Org_ID(ah.getAD_Org_ID()); }
					 */
					// ESC_LCO
				}
			}
		}
		return null;
	}

	private String completeInvoiceWithholding(MInvoice inv) {

		// Fill DateAcct and DateTrx with final dates from Invoice
		String upd_dates = "UPDATE LCO_InvoiceWithholding "
				+ "   SET DateAcct = "
				+ "          (SELECT DateAcct "
				+ "             FROM C_Invoice "
				+ "            WHERE C_Invoice.C_Invoice_ID = LCO_InvoiceWithholding.C_Invoice_ID), "
				+ "       DateTrx = "
				+ "          (SELECT DateInvoiced "
				+ "             FROM C_Invoice "
				+ "            WHERE C_Invoice.C_Invoice_ID = LCO_InvoiceWithholding.C_Invoice_ID) "
				+ " WHERE C_Invoice_ID = ? ";
		int noupddates = DB.executeUpdate(upd_dates, inv.getC_Invoice_ID(),
				inv.get_TrxName());
		if (noupddates == -1)
			return "Error updating dates on invoice withholding";

		// Set processed for isCalcOnInvoice records
		String upd_proc = "UPDATE LCO_InvoiceWithholding "
				+ "   SET Processed = 'Y' "
				+ " WHERE C_Invoice_ID = ? AND IsCalcOnPayment = 'N'";
		int noupdproc = DB.executeUpdate(upd_proc, inv.getC_Invoice_ID(),
				inv.get_TrxName());
		if (noupdproc == -1)
			return "Error updating processed on invoice withholding";

		return null;
	}

	private String translateWithholdingToTaxes(MInvoice inv) {
		BigDecimal sumit = new BigDecimal(0);

		MDocType dt = new MDocType(inv.getCtx(), inv.getC_DocTypeTarget_ID(),
				inv.get_TrxName());
		String genwh = dt.get_ValueAsString("GenerateWithholding");
		if (genwh == null || genwh.equals("N")) {
			// document configured to not manage withholdings - delete any
			String sqldel = "DELETE FROM LCO_InvoiceWithholding "
					+ " WHERE C_Invoice_ID = ?";
			PreparedStatement pstmtdel = null;
			try {
				// Delete previous records generated
				pstmtdel = DB.prepareStatement(sqldel,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE, inv.get_TrxName());
				pstmtdel.setInt(1, inv.getC_Invoice_ID());
				int nodel = pstmtdel.executeUpdate();
				log.config("LCO_InvoiceWithholding deleted=" + nodel);
			} catch (Exception e) {
				log.log(Level.SEVERE, sqldel, e);
				return "Error creating C_InvoiceTax from LCO_InvoiceWithholding -delete";
			} finally {
				DB.close(pstmtdel);
				pstmtdel = null;
			}
			inv.set_CustomColumn("WithholdingAmt", Env.ZERO);

		} else {
			// translate withholding to taxes
			String sql = "SELECT C_Tax_ID, NVL(SUM(TaxBaseAmt),0) AS TaxBaseAmt, NVL(SUM(TaxAmt),0) AS TaxAmt "
					+ " FROM LCO_InvoiceWithholding "
					+ " WHERE C_Invoice_ID = ? AND IsCalcOnPayment = 'N' AND IsActive = 'Y' "
					+ "GROUP BY C_Tax_ID";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, inv.get_TrxName());
				pstmt.setInt(1, inv.getC_Invoice_ID());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					MInvoiceTax it = new MInvoiceTax(inv.getCtx(), 0,
							inv.get_TrxName());
					it.setAD_Org_ID(inv.getAD_Org_ID());
					it.setC_Invoice_ID(inv.getC_Invoice_ID());
					it.setC_Tax_ID(rs.getInt(1));
					it.setTaxBaseAmt(rs.getBigDecimal(2));
					it.setTaxAmt(rs.getBigDecimal(3).negate());
					sumit = sumit.add(rs.getBigDecimal(3));
					if (!it.save())
						return "Error creating C_InvoiceTax from LCO_InvoiceWithholding - save InvoiceTax";
				}
				BigDecimal actualamt = (BigDecimal) inv
						.get_Value("WithholdingAmt");
				if (actualamt == null)
					actualamt = new BigDecimal(0);
				if (actualamt.compareTo(sumit) != 0 || sumit.signum() != 0) {
					inv.set_CustomColumn("WithholdingAmt", sumit);
					// Subtract to invoice grand total the value of withholdings
					BigDecimal gt = inv.getGrandTotal();
					inv.setGrandTotal(gt.subtract(sumit));
					inv.save(); // need to save here in order to let apply get
								// the right total
				}

				if (sumit.signum() != 0) {
					// GrandTotal changed! If there are payment schedule records
					// they need to be recalculated
					// subtract withholdings from the first installment
					BigDecimal toSubtract = sumit;
					for (MInvoicePaySchedule ips : MInvoicePaySchedule
							.getInvoicePaySchedule(inv.getCtx(),
									inv.getC_Invoice_ID(), 0, inv.get_TrxName())) {
						if (ips.getDueAmt().compareTo(toSubtract) >= 0) {
							ips.setDueAmt(ips.getDueAmt().subtract(toSubtract));
							toSubtract = Env.ZERO;
						} else {
							toSubtract = toSubtract.subtract(ips.getDueAmt());
							ips.setDueAmt(Env.ZERO);
						}
						if (!ips.save()) {
							return "Error saving Invoice Pay Schedule subtracting withholdings";
						}
						if (toSubtract.signum() <= 0)
							break;
					}
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, sql, e);
				return "Error creating C_InvoiceTax from LCO_InvoiceWithholding - select InvoiceTax";
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		}

		return null;
	}

	/**
	 * User Login. Called when preferences are set
	 * 
	 * @param AD_Org_ID
	 *            org
	 * @param AD_Role_ID
	 *            role
	 * @param AD_User_ID
	 *            user
	 * @return error message or null
	 */
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);
		return null;
	} // login

	/**
	 * Get Client to be monitored
	 * 
	 * @return AD_Client_ID client
	 */
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	} // getAD_Client_ID

	/**
	 * String Representation
	 * 
	 * @return info
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("LCO_Validator");
		return sb.toString();
	} // toString

	/**
	 * Check Digit based on TaxID.
	 */
	private String mcheckTaxIdDigit(MBPartner bpartner) {
		Integer taxidtype_I = (Integer) bpartner.get_Value("LCO_TaxIdType_ID");

		if (taxidtype_I == null) {
			// Returning error here has problems with Initial Client Setup and
			// other processes
			// that creates BPs
			// Mandatory must be delegated to UI (in AD_Field.ismandatory)
			// return Msg.getMsg(bpartner.getCtx(), "LCO_TaxIDTypeRequired");
			return null;
		}

		X_LCO_TaxIdType taxidtype = new X_LCO_TaxIdType(bpartner.getCtx(),
				taxidtype_I.intValue(), bpartner.get_TrxName());

		bpartner.set_ValueOfColumn("IsDetailedNames",
				taxidtype.isDetailedNames());
		bpartner.set_ValueOfColumn("IsUseTaxIdDigit",
				taxidtype.isUseTaxIdDigit());

		if (!taxidtype.isUseTaxIdDigit()) {
			bpartner.set_ValueOfColumn("TaxIdDigit", null);
			return null;
		}

		// Is Juridical
		String taxid = bpartner.getTaxID();
		if (taxid == null || taxid.trim().length() == 0)
			return Msg.getMsg(bpartner.getCtx(), "LCO_NoTaxID");

		int correctDigit = LCO_Utils.calculateDigitDian(taxid);
		if (correctDigit == -1) // Error on the Tax ID - possibly invalid
								// characters
			return Msg.getMsg(bpartner.getCtx(), "LCO_NotValidID");

		String taxIDDigit = (String) bpartner.get_Value("TaxIdDigit");
		if (taxidtype.isDigitChecked()) {
			if (taxIDDigit == null || taxIDDigit.trim().length() == 0)
				return Msg.getMsg(bpartner.getCtx(), "LCO_NoDigit"); // No Tax
																		// ID
																		// Digit
			int taxIDDigit_int;
			try {
				taxIDDigit_int = Integer.parseInt(taxIDDigit);
			} catch (NumberFormatException e) {
				return Msg.getMsg(bpartner.getCtx(), "LCO_NotANumber"); // Error
																		// on
																		// the
																		// check
																		// digit
			}
			if (correctDigit != taxIDDigit_int)
				return Msg.getMsg(bpartner.getCtx(), "LCO_VerifyCheckDigit");
		} else {
			bpartner.set_ValueOfColumn("TaxIdDigit",
					String.valueOf(correctDigit));
		}

		log.info(bpartner.toString());
		return null;
	} // mcheckTaxIdDigit

	/**
	 * Fill Name based on First and Last Names
	 * 
	 * @param bpartner
	 *            bpartner
	 * @return error message or null
	 */
	public String mfillName(MBPartner bpartner) {
		log.info("");
		boolean isDetailedNames = false;
		Boolean boolIsDetailedNames = (Boolean) bpartner
				.get_Value("IsDetailedNames");
		if (boolIsDetailedNames != null)
			isDetailedNames = boolIsDetailedNames.booleanValue();

		if (!isDetailedNames) {
			bpartner.set_ValueOfColumn("FirstName1", null);
			bpartner.set_ValueOfColumn("FirstName2", null);
			bpartner.set_ValueOfColumn("LastName1", null);
			bpartner.set_ValueOfColumn("LastName2", null);
			return null;
		}

		String filledName = null;

		if (bpartner.get_Value("FirstName1") == null
				|| ((String) bpartner.get_Value("FirstName1")).length() == 0)
			return Msg.getMsg(bpartner.getCtx(), "LCO_FirstName1Required");

		if (bpartner.get_Value("LastName1") == null
				|| ((String) bpartner.get_Value("LastName1")).length() == 0)
			return Msg.getMsg(bpartner.getCtx(), "LCO_LastName1Required");

		filledName = bpartner.get_ValueAsString("FirstName1").trim();
		if (bpartner.get_Value("FirstName2") != null)
			filledName = filledName + " "
					+ bpartner.get_ValueAsString("FirstName2").trim();

		if (filledName != null)
			// filledName = filledName + ", "; -- Separate first and last names
			// with comma
			filledName = filledName + " ";

		filledName = filledName
				+ bpartner.get_ValueAsString("LastName1").trim();
		if (bpartner.get_Value("LastName2") != null)
			filledName = filledName + " "
					+ bpartner.get_ValueAsString("LastName2").trim();

		bpartner.setName(filledName);
		return null;
	} // mfillName

	/**
	 * Test Allocation (and set paid flag) This method is the replication of
	 * MInvoice.testAllocation()
	 * 
	 * @return true if updated
	 */
	public boolean testAllocation(MInvoice inv) {
		boolean change = false;

		if (inv.isProcessed()) {
			BigDecimal alloc = this.getAllocatedAmt(inv); // absolute
			if (alloc == null)
				alloc = Env.ZERO;
			BigDecimal total = inv.getGrandTotal();
			if (!inv.isSOTrx())
				total = total.negate();
			if (inv.isCreditMemo())
				total = total.negate();
			boolean test = total.compareTo(alloc) == 0;
			change = test != inv.isPaid();
			if (change)
				inv.setIsPaid(test);
			log.fine("Paid=" + test + " (" + alloc + "=" + total + ")");
		}

		return change;
	} // testAllocation

	/**
	 * Get Allocated Amt in Invoice Currency This method is the replication of
	 * MInvoice.getAllocatedAmt()
	 * 
	 * @return pos/neg amount or null
	 */
	public BigDecimal getAllocatedAmt(MInvoice inv) {
		BigDecimal retValue = null;
		String sql = "SELECT SUM(currencyConvert(al.Amount+al.DiscountAmt+al.WriteOffAmt+al.WithholdingAmt," // KTU,
																												// add
																												// WithholdingAmt
				+ "ah.C_Currency_ID, i.C_Currency_ID,ah.DateTrx,COALESCE(i.C_ConversionType_ID,0), al.AD_Client_ID,al.AD_Org_ID)) "
				+ "FROM C_AllocationLine al"
				+ " INNER JOIN C_AllocationHdr ah ON (al.C_AllocationHdr_ID=ah.C_AllocationHdr_ID)"
				+ " INNER JOIN C_Invoice i ON (al.C_Invoice_ID=i.C_Invoice_ID) "
				+ "WHERE al.C_Invoice_ID=?"
				+ " AND ah.IsActive='Y' AND al.IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, inv.get_TrxName());
			pstmt.setInt(1, inv.getC_Invoice_ID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retValue = rs.getBigDecimal(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (SQLException e) {
			throw new DBException(e, sql);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		return retValue;
	} // getAllocatedAmt

} // LCO_Validator
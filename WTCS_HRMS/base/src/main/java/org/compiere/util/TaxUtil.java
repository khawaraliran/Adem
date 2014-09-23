/**
 * 
 */

package org.compiere.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.model.I_C_DocType;
import org.compiere.model.I_C_Tax;
import org.compiere.model.I_WTC_TaxIndicator;
import org.compiere.model.MDocType;
import org.compiere.model.MTax;
import org.compiere.model.MWTCChildTax;
import org.compiere.model.MWTCTaxIndicator;
import org.compiere.model.Query;

/**
 * @author PhaniKiran Gutha
 * 
 * 
 * @author  PhaniKiran Gutha
 * @BugNo   1621
 * @changes introduced new method to get the document id, with the name 
 ** @author PhaniKiran.Gutha
 ***************************************************************************************************************************
 *  Author     BugNo     ChangeID                  Description      
 *  Arunkumar  2443      [20120312:5:10]           Modified : containsTaxWithSource(MTax, List<MWTCChildTax>)
 *                                                            InActive tax will not be considered as child tax.
 *  Arunkumar  2443      [20120312:5:20]           Modified : getChildWithBaseTax(int, Properties, String)
 *                                                            InActive tax will not be considered as child tax.
 *  Arunkumar  2443      [20120316:12:03]          Modified : containsTaxWithSource(MTax, List<MWTCChildTax>) --  There is No Need To Check is Activ As We Are Getting Only Active Records as child taxes                                                                                                                                                                    
 */       
public class TaxUtil {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: TaxUtil.java 1158 2012-03-16 08:30:01Z arun $";
	
	

	public static List<MWTCChildTax> getChildWithBaseTax( int taxId , Properties ctx , String trxName ){

		String where = MWTCChildTax.COLUMNNAME_C_Tax_ID+" = " + taxId;
		
		List<MWTCChildTax>  childs = new Query( ctx  ,
												MWTCChildTax.Table_Name ,
												where,
												trxName )
										.setOrderBy( MWTCChildTax.COLUMNNAME_TaxOrder )
										.setOnlyActiveRecords( Boolean.TRUE )
										.list();
		
		ArrayList<MWTCChildTax> taxes = new ArrayList<MWTCChildTax>(); 

		for( MWTCChildTax child : childs){
            //2443
			//[20120312:5:20] , 20120316:12:03
			if(child.getChild_Tax().isActive()) {
				if( child.getTaxLevel().equalsIgnoreCase( MWTCChildTax.TAXLEVEL_BasePlusTax ) ){
						taxes.add( child );				
			    }
			}
			
		}

		return taxes;
	}

	public static boolean containsTaxWithSource( MTax tax, List<MWTCChildTax> childTaxs ){

		for( MWTCChildTax child : childTaxs ){
           if(child.isActive()) {
				MTax childTax = (MTax) child.getChild_Tax();
				//[2443]
				//[20120312:5:10]
				if(childTax != null && childTax.isActive()) {
					if( child.getChild_Tax_ID() == tax.get_ID() ){
						
						return Boolean.TRUE;
					}
				}
           }
		}

		return Boolean.FALSE ;

	}
	
	public static List<MTax> getTaxesOfIndicator ( int TaxIndicatorId , Properties ctx , String condition ,String trxName ){
		
		String where = I_C_Tax.COLUMNNAME_WTC_TaxIndicator_ID + " = " + TaxIndicatorId;
		
		if( condition != null && !condition.isEmpty() ){
			where = where +  " AND " + condition ;
		}
		
		return new Query( ctx , 
							I_C_Tax.Table_Name , 
							where , 
							trxName )
					.setOnlyActiveRecords( Boolean.TRUE )
					.list();
		
	}
	
	/**
	 * BugNo 1621
	 * @param clientId
	 * @param ctx
	 * @param name
	 * @return retun's the id of the document type with the name
	 */
	public static int getDocType( int clientId , Properties ctx , String name ){
		
		MDocType docType = new Query( ctx, 
										I_C_DocType.Table_Name, 
										I_C_DocType.COLUMNNAME_Name + " = ? ",
										null )
								.setParameters( name )
								.first();
		
		return docType.get_ID();
	}
	
	
	/**
	 * BugNo 1621
	 * @param clientId
	 * @param ctx
	 * @param Code  -- TaxIndicator Code
	 * @return retun's the id of the TaxIndicator Id with the Code
	 */
	public static int getTaxIndicator( int clientId , Properties ctx , String code ){
		
		MWTCTaxIndicator indicator = new Query( ctx, 
												I_WTC_TaxIndicator.Table_Name, 
												I_WTC_TaxIndicator.COLUMNNAME_Code + " = ? ",
										null )
								.setParameters( code )
								.first();
		
		return indicator.get_ID();
	}
}

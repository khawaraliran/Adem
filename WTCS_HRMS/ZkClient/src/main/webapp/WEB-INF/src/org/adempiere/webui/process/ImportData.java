package org.adempiere.webui.process;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;

import org.compiere.impexp.DataImportExport;
import org.compiere.model.MImporter;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * @author Arunkumar
 *
 */
public class ImportData extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: ImportData.java 1009 2012-02-09 09:16:13Z suman $";

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream(new File(path));
		MImporter impo = new MImporter(Env.getCtx(), importerID, get_TrxName());
		DataImportExport porter = new DataImportExport(impo, fis);
		List<StringBuffer> result = porter.importToDataBase(get_TrxName());
		for(StringBuffer r : result)
		{
			addLog(r.toString());
		}
		fis.close();
		fis = null;
		return "";
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	int importerID =0;
	String path =null;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equals("AD_Importer_ID"))
				importerID = ((BigDecimal) para[i].getParameter())
						.intValue();
			else if (name.equalsIgnoreCase("Filename"))
				path = (String) para[i].getParameter();
		}

	}

}
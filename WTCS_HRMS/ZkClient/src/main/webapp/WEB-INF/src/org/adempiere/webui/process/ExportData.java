package org.adempiere.webui.process;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;

import org.adempiere.pipo.CreateZipFile;
import org.compiere.impexp.DataImportExport;
import org.compiere.model.MImporter;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.wtc.util.DocumentUtil;
import org.compiere.util.Env;
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Filedownload;

/**
 * This process will  export the data from the tables
 * to a csv file and prepare's zip file for all the csv files
 * generated and popup a window at client side asking to download
 * This class will work only for web version
 * @author phani
 * ********************************************************************************************
 * Bug No   Author   ChangeID        Description        
 *   
 * 2012     Anitha  [20120202]    Handled The Exception
 * ********************************************************************************************* 
 */
public class ExportData extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: ExportData.java 1009 2012-02-09 09:16:13Z suman $";

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		MImporter impo = new MImporter(Env.getCtx(), exporterID, null);
		DataImportExport export = new DataImportExport(impo);
		File srcfiles = export.exportFromDB(get_TrxName());
		AMedia media = null;
		File DestFile =new File("Export"+impo.getName()+".zip");
       if(DestFile.exists())
    	   DestFile.delete();

        File destforlder = new File("Export"+Calendar.getInstance().getTimeInMillis());
        destforlder.mkdir();
        
//        destforlder.deleteOnExit();
        
        // Saves the file at the client side File System
       
//		for(File srcFile : srcfiles)
//		{
			DocumentUtil.copyFile(srcfiles, destforlder);
			
//		}
		
		CreateZipFile.zipFolder(destforlder, DestFile, "");
		media = new AMedia(DestFile,null,null);
		//[20120202]
		try{
		 Filedownload.save(media); 
		 for(File srcFile : destforlder.listFiles()) 
			{
				srcFile.delete();
			}
		 destforlder.delete();
		}catch (Exception e) {
			// TODO: handle exception
		}
//		 DestFile.delete();
		return "Data Exported To   "+DestFile.getName();
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	int exporterID =0;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equals("AD_Importer_ID"))
				exporterID = ((BigDecimal) para[i].getParameter()).intValue();
		}

	}

	
}

package org.wtc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.compiere.util.CLogger;



/****************************************************************************
 * @author Arunkumar
 *  
 **************************************************************************** 
 */


/**
 **********************************************************************************************************
 * @BugNo     @author     	@ChnageID		             @Description		
 *  2012      Arunkuamr		[20120130:4:00PM]			  Added A method Which will copy the Given In Put file To Destination Direcotory					
 */
public class DocumentUtil  {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: DocumentUtil.java 1009 2012-02-09 09:16:13Z suman $";
	private static CLogger logger = CLogger.getCLogger(DocumentUtil.class);

	/**
	 * This method copy the file to the specified directory
	 * 
	 * @param fromFile		: Path of the file with name 
	 * @param destDir		: Path of the destination directory along with name 
	 * @return				: File
	 * @throws IOException
	 */
	public static File copyFile(File fromFile, File destDir) throws IOException {
		File toFile = new File(destDir, fromFile.getName());
		FileInputStream fromStream = null;
		FileOutputStream toStream = null;

		// Copy the file, a buffer of bytes at a time.
		fromStream = new FileInputStream(fromFile);						
		toStream = new FileOutputStream(toFile);

		// To hold file contents
		byte[] buffer = new byte[4096];
		int bytes_read;

		while ((bytes_read = fromStream.read(buffer)) != -1) {
			toStream.write(buffer, 0, bytes_read);
		}

		fromStream.close();
		toStream.close();
		fromFile.delete();
		logger.finer("Copied file [" + fromFile + "] to [" + toFile + "]");

		return toFile;
	}
	
}


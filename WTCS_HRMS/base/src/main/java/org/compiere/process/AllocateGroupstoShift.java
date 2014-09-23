/**
 * @Description  This will allocatse the work groups for each shift
 */
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;


import org.compiere.model.MSysConfig;

import org.compiere.model.MWorkShift;
import org.compiere.model.Query;
import org.compiere.model.X_HR_Shift_Alloc;
import org.compiere.model.X_HR_Work_Group;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Msg;
import org.wtc.util.WTCTimeUtil;

/**
 *  @author Arunkumar
 *  @Date   
 */
public class AllocateGroupstoShift extends SvrProcess {

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	
	public List<X_HR_Work_Group> workGroupArray = null;
	public List<MWorkShift> workShiftswithOutFWGroup = null;
	
	public int successCount = 0;
	public int errorCount = 0;
	
	protected String doIt() throws Exception {
		
		String msg = null;
		if(getShiftAllocations(year) == null)
		{// no shift allocations are done for the year. go and allocate shifts for the year.
			PreparedStatement ps=null;
			ResultSet rs=null;
			
			String shiftGroupSql="SELECT HR_Shift_Group_ID from HR_Shift_Group";
			ps=DB.prepareStatement(shiftGroupSql, this.get_TrxName());
			
			try
			{
				rs=ps.executeQuery();
				
				while(rs.next())
				{
					workGroupArray = getWorkGroups(rs.getInt(1)); 
					
					
					
					if(startWorkGroup == null)
					{ 
						msg = AllocateShifts(workGroupArray,rs.getInt(1));
					}
					
				}
			}
			catch (Exception e) 
			{
				log.log(Level.SEVERE, "Can't Get ShiftGroup:");
			}
				
			
		}
		else
		{// shift allocations happened already. inform the user and exit from process.
			
			msg = Msg.getMsg(this.getCtx(), "SHIFT_ALLOCATION_EXISTS");
			
		}
		
		return msg;
	}

	// parameter year
	private Integer year = 0;
	
	// WorkGroup PO prepared using WorkGroupID passed as parameter
	private X_HR_Work_Group startWorkGroup = null;
	
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String parameterName = null;
		
		for(int i=0; i<para.length; i++)
		{
			parameterName = para[i].getParameterName();
			
			if (para[i].getParameter() == null); // if parameter is null do not execute next step
			
			else if(parameterName.equalsIgnoreCase("Year")) // if parameter is year store year value in variable year
			{
				year = para[i].getParameterAsInt();
			}
			
		}

	}// prepare

	/*
	 * query the HR_Shift_Alloc table for the shift allocation done for the year passed as argument.
	 * prepare sql query to retrive records from HR_Shift_Alloc Table and return the arrayList.
	 */
	private X_HR_Shift_Alloc[] getShiftAllocations(Integer year)
	{
		StringBuffer whereClause = new StringBuffer();
		whereClause.append("to_char(").append(X_HR_Shift_Alloc.COLUMNNAME_workdate).append(",'YYYY')= ?");
		Query query = new Query(this.getCtx(), X_HR_Shift_Alloc.Table_Name, whereClause.toString(), this.get_TrxName());
		query.setParameters(year.toString());
		List<X_HR_Shift_Alloc> list = query.list();
		X_HR_Shift_Alloc[] shiftAllocArray = new X_HR_Shift_Alloc[list.size()] ;
		return (list.size() > 0 ? list.toArray(shiftAllocArray) : null);
	}
	
	/*
	 * Query HR_WorkGroup table for workgroup whose isforshiftallow is set to y. in order by sequence number
	 * prepare database query accordingly and execute query return result.
	 */
	private List<X_HR_Work_Group> getWorkGroups(int shiftGroupID)
	{
		StringBuffer whereClause = new StringBuffer();
		whereClause.append(X_HR_Work_Group.COLUMNNAME_isforshiftalloc).append("='Y'");
		whereClause.append(" AND ");
		whereClause.append(X_HR_Work_Group.COLUMNNAME_HR_Shift_Group_ID);
		whereClause.append("= ");
		whereClause.append(shiftGroupID);
		Query query = new Query(this.getCtx(),X_HR_Work_Group.Table_Name,whereClause.toString(),this.get_TrxName());
		query.setOrderBy(X_HR_Work_Group.COLUMNNAME_SeqNo);
		List<X_HR_Work_Group> list = query.list();
		return list;
	}
	/*
	 * Query HR_Work_Shift table for workShifts which do not have fixed workGroups
	 * prepare database query accordingly and get po objects of the HR_Work_Shift table.
	 */
	private List<MWorkShift> getWorkShiftsWithOutFixedWorkGroup(int shiftGroup)
	{
		StringBuffer whereClause = new StringBuffer();
		whereClause.append(MWorkShift.COLUMNNAME_hasfixedworkgroup).append("='N'");
		whereClause.append(" and ");
		whereClause.append(MWorkShift.COLUMNNAME_HR_Shift_Group_ID);
		whereClause.append(" = ");
		whereClause.append(shiftGroup);
		Query query = new Query(this.getCtx(),MWorkShift.Table_Name,whereClause.toString(),this.get_TrxName());
		query.setOrderBy(MWorkShift.COLUMNNAME_SeqNo);
		List<MWorkShift> list = query.list();
		return list;
	}
	
	/*
	 *
	 * @param WorkGroupList
	 * @return
	 */
	private String AllocateShifts(List<X_HR_Work_Group> WorkGroupList,int shiftGroup)
	{
		//Boolean isShiftAllocRequired=true;
		
		workShiftswithOutFWGroup =  getWorkShiftsWithOutFixedWorkGroup(shiftGroup);
		
		/*
		if(WorkGroupList.size() == 1 && workShiftswithOutFWGroup.size()==1)
		{
			isShiftAllocRequired=false;
		}
		
		if(isShiftAllocRequired) //If There IS ONLY ONE WORKGroup AND WORKSHIT IN THIS SHIFT GROUP THEN WE DON't REQUIRE THIS SHIFTING.
		{*/
		
			if ( WorkGroupList.size() != workShiftswithOutFWGroup.size()){
				
				return Msg.getMsg(this.getCtx(), "WORK_GROUP_AND_SHIFT_MISMATCH");					
			}
			int noOfShiftChngs = MSysConfig.getIntValue(EagleConstants.VAR_NO_OF_SHIFT_CHANGES, EagleConstants.DEFAULT_SHIFT_CHANGES);
			
			HashMap<X_HR_Work_Group, MWorkShift> allocMap = createAllocationMap(WorkGroupList,workShiftswithOutFWGroup);
			//
			// Iterate throgh all the months in the year
			//
			for( int mon = 0; mon <12;mon ++){
				int daysInMonth = WTCTimeUtil.getDaysInMonth(year.intValue(),mon);
				
				int divisionInMonth = 1;
				int previousDivision = 1;
				// work group change when month changes and should not change for first month
				if(mon !=0){
				allocMap.clear();
				allocMap = updateAllocateMap();
				}
				//
				// Iterate through all the days in month
				//
				for (int days=1;days<=daysInMonth;days++){
					divisionInMonth = caluclateDivision(days,noOfShiftChngs);
					log.info("Day of the Month is");
					if ( previousDivision != divisionInMonth){
						//
						// New allocation map will be built, so clean the existing map
						//
						allocMap.clear();
						allocMap = updateAllocateMap();
					}
					
					Timestamp workDate = WTCTimeUtil.getTimeStamp( year.intValue(), mon,days,0,0,0,0);
					
					//
					// Create Object for Work Shift Allocation
					//
									
					Set<Entry<X_HR_Work_Group, MWorkShift>> entryset = allocMap.entrySet();
					Iterator iterator = entryset.iterator();
					
					while(iterator.hasNext())
					{
						Map.Entry<X_HR_Work_Group, MWorkShift> entryMap = (Entry<X_HR_Work_Group, MWorkShift> ) iterator.next();
						X_HR_Work_Group workGroup = entryMap.getKey();
						MWorkShift workShift = entryMap.getValue();
						X_HR_Shift_Alloc shiftAlloc = new X_HR_Shift_Alloc(this.getCtx(),0,this.get_TrxName());
	                    shiftAlloc.setHR_Work_Group_ID(workGroup.getHR_Work_Group_ID());
					    shiftAlloc.setHR_Work_Shift_ID(workShift.getHR_Work_Shift_ID());
						shiftAlloc.setworkdate(workDate);
						boolean created = shiftAlloc.save();
						if ( created == true){
							successCount++;
						}
						else{
						    errorCount++;	
						}
					}
					previousDivision = divisionInMonth;
								
				}
			}
		//}	
		
		String msg = Msg.getMsg(this.getCtx(),"SHIFT_ALLOCATION_PROCESS_MSG", new Object[] {new String(year.toString()),new Integer(successCount), new BigDecimal(errorCount)});
		return msg;
	}
	
	/*
	 * calucates the divisions of a month based on the day of the month and max no of division allowed in month
	 * @param dayOfMonth
	 * @param maxDivisionsPerMonth
	 * @return
	 */	
	
	private int caluclateDivision(int dayOfMonth, int maxDivisionsPerMonth)
	{
		int divisionDays = 30/maxDivisionsPerMonth;
		
		int division =0;
		/*
		 * If day of the month is less than the divisiondays then division will be wrong with approach given in SA
		 * Example : dayOfMonth is 4th and divisiondays is 5. then division=dayOfMonth%divisionDays will give division as 4 which is wrong
		 * so if dayOfMonth is less than divisiondays division will be 1 else division will be calculated
		 */
		if(dayOfMonth <= divisionDays)
			division = 1;
		else
			division= ((dayOfMonth-1)/divisionDays)+1;
		if(division > maxDivisionsPerMonth)
			division = maxDivisionsPerMonth;
		
		return division;
	}
	
	
	/**
	 *  update the AllocationMap which has workShift as key and workGroup as value
	 */
	
	private HashMap<X_HR_Work_Group,MWorkShift> updateAllocateMap()
	{	
		
		if ( workGroupArray.size()>1 ){
		workGroupArray = prepareWorkGroupList( workGroupArray.get(1),workGroupArray);
		}
		
		HashMap<X_HR_Work_Group,MWorkShift> newMap = createAllocationMap(workGroupArray,workShiftswithOutFWGroup);
		return newMap;
	}
	
	/*
	 * preapare New workGroup list from the startWorkGroup and Existing WorkGroup List
	 * bring startWorkgroup to the first place if the startworkgroup is in the workGroup list
	 * and inversing the order of the existing workGrouplist
	 */
	private List<X_HR_Work_Group> prepareWorkGroupList(X_HR_Work_Group startWorkGroup, List<X_HR_Work_Group> workGroupList)
	{
		if(workGroupList.size() == 1)
		{
			return workGroupList;
		}
		
		//TODO preapare workgroup list
		ArrayList<X_HR_Work_Group> newWorkGroupList = new ArrayList<X_HR_Work_Group>();
		Deque<X_HR_Work_Group> stack = new ArrayDeque<X_HR_Work_Group>();
		// TODO set processed current workGroup to false -- hold 
		boolean processedCurrentWorkGroup = false;
		// step-2
		for(X_HR_Work_Group currentWorkgroup : workGroupList)
		{
			if(currentWorkgroup.getHR_Work_Group_ID() == startWorkGroup.getHR_Work_Group_ID()) 
			{// check wether currentWorkGroup and startWorkGroup are same
				processedCurrentWorkGroup = true;
			}
			if(processedCurrentWorkGroup)
			{// if  current workGroup is processed add workGroup to newworkgrouplist
				newWorkGroupList.add(currentWorkgroup);
			}
			else
			{
				stack.push(currentWorkgroup);
			}
		}// end for
		
		// add all the workgroups in stack to newWorkGroupList from top
		for(X_HR_Work_Group wg : stack)
		{
			newWorkGroupList.add(wg);
		}
		stack = null;
		return newWorkGroupList;
	}
	
	private HashMap<X_HR_Work_Group, MWorkShift> createAllocationMap(List<X_HR_Work_Group> workGroupList,List<MWorkShift> workShiftList){
		HashMap<X_HR_Work_Group, MWorkShift> allocMap = new HashMap<X_HR_Work_Group, MWorkShift>();
	
		Iterator<X_HR_Work_Group> groupIterator = workGroupList.iterator();		
		Iterator<MWorkShift> listIterator = workShiftList.iterator();
		
		while(groupIterator.hasNext()&&listIterator.hasNext()){
		     allocMap.put(groupIterator.next(), listIterator.next());	
		}				
		return allocMap;
	}
}

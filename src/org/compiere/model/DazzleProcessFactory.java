package org.compiere.model;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;

import za.co.ntier.nbsm.NBSM_Proc_BankStatementMatcher;
import za.co.ntier.nbsm.NBSM_Proc_CreateMatcher;

/**
 * (c) 2015 nTier Software Services
 * @author Neil Gordon
 * Date: 13 Feb 2015
 * Description:	
 */
public class DazzleProcessFactory implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		if ("org.compiere.process.BankStatementMatcher".equals(className))
			return new NBSM_Proc_BankStatementMatcher();
		if ("za.co.ntier.nbsm.NBSM_Proc_CreateMatcher".equals(className))
			return new NBSM_Proc_CreateMatcher();
		return null;
	}
}

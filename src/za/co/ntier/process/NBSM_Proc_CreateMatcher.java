package za.co.ntier.process;

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.grid.WQuickEntry;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MMatchSetup;
import org.compiere.model.MWindow;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import za.co.ntier.common.NTierStringUtils;
import za.co.ntier.nbsm.NBSM_Common;

/**
 */
public class NBSM_Proc_CreateMatcher extends SvrProcess
{
	
	private MBankStatementLine m_bsLine;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		log.warning("NBSM_Proc_CreateMatcher - process");
		
		m_bsLine = new MBankStatementLine(getCtx(), getRecord_ID(), get_TrxName());
		if ( NTierStringUtils.isNullOrEmpty(m_bsLine.getDescription()) ) {
			throw new AdempiereException(String.format("Cannot create matcher for empty bank statement line description"));
		}
		
		// Work around for bug - Quick Entry not getting the correct default value
		Env.setContext(Env.getCtx(), "#C_BankAccount_ID", m_bsLine.getC_BankStatement().getC_BankAccount_ID());
		// Match text
		Env.setContext(Env.getCtx(), "#Description", m_bsLine.getDescription());
		if ( m_bsLine.getC_BPartner_ID() > 0 ) {
			Env.setContext(Env.getCtx(), "#C_BPartner_ID", m_bsLine.getC_BPartner_ID());
		} else {
			Env.setContext(Env.getCtx(), "#C_BPartner_ID", "");
		}
		if ( m_bsLine.getC_Charge_ID() > 0 ) {
			Env.setContext(Env.getCtx(), "#C_Charge_ID", m_bsLine.getC_Charge_ID());
		} else {
			Env.setContext(Env.getCtx(), "#C_Charge_ID", "");
		}
		if ( m_bsLine.get_ColumnIndex("IsTaxIncluded") >=0 ) {
			// Customized column
			Env.setContext(Env.getCtx(), "#IsTaxIncluded", m_bsLine.get_ValueAsBoolean("IsTaxIncluded"));
		}
		
		showQE();
		
		return "";

	}	//	doIt
	
	private void showQE() {
		
		// Get existing record by match text
		MMatchSetup matchSetup = MMatchSetup.getMatchSetup(
				getCtx(), get_TrxName(), 
				m_bsLine.getC_BankStatement().getC_BankAccount_ID(), 
				m_bsLine.getDescription());
		final int matchSetupID;
		if ( matchSetup == null ) {
			matchSetupID = 0;
		} else {
			matchSetupID = matchSetup.get_ID();
		}
		AEnv.executeAsyncDesktopTask(new Runnable() {
	    	@Override
			public void run() {
	    		String windowName = NBSM_Common.WINDOW_NAME_MATCHING_SETUP_QUICK_ENTRY;
	    		int windowID = MWindow.getWindow_ID(windowName);
	    		if ( windowID <= 0 ) {
	    			throw new AdempiereException(String.format("Could not find window '%s' required for Quick Entry - please apply 2pack", windowName));
	    		}
	    		final WQuickEntry vqe = new WQuickEntry (1, windowID);
	    		vqe.loadRecord ( matchSetupID );
	    		vqe.setVisible(true);
	    	    SessionManager.getAppDesktop().showWindow(vqe);
	    	}
	    });
		
	}
}
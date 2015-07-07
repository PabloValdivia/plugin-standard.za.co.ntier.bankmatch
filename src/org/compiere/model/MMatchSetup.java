package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.Env;

import za.co.ntier.common.NTierStringUtils;

/**
 * @author NCG
 * Date: 25 Jun 2015
 * Description:	
 */
public class MMatchSetup extends X_ZZ_NBSM_MatchSetup implements
		I_ZZ_NBSM_MatchSetup {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1706239969832026858L;

	public MMatchSetup(Properties ctx, int ZZ_NBSM_MatchSetup_ID,
			String trxName) {
		super(ctx, ZZ_NBSM_MatchSetup_ID, trxName);
	}

	public MMatchSetup(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
//	@Override
//	public void setZZ_NBSM_MatchText(String ZZ_NBSM_MatchText) {
//		if ( ZZ_NBSM_MatchText != null ) {
//			ZZ_NBSM_MatchText = ZZ_NBSM_MatchText.trim();
//		}
//		super.setZZ_NBSM_MatchText(ZZ_NBSM_MatchText);
//	}

	/**
	 * Get exact match
	 */
	public static MMatchSetup getMatchSetup(Properties ctx, String trxName, int C_BankAccount_ID, 
			String matchText) {
		if ( matchText == null ) {
			matchText = "";
		}
		if ( !matchText.startsWith("%") ) {
			matchText = "%" + matchText;
		}
		if ( !matchText.endsWith("%") ) {
			matchText = matchText + "%";
		}
		final String whereClause = " AD_Client_ID=? and c_bankaccount_id=? and trim(upper(zz_nbsm_matchtext)) like upper(?) ";
		MMatchSetup retValue = new Query(
				ctx, MMatchSetup.Table_Name, whereClause,
				trxName)
			.setParameters
				( Env.getAD_Client_ID(ctx), C_BankAccount_ID, matchText.trim())
			.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		boolean bpRequired = false;
		boolean ptRequired = false;
		
		if ( getZZ_NBSM_MatchText() != null ) {
			// Trim spaces
			setZZ_NBSM_MatchText(getZZ_NBSM_MatchText().trim());
		}
		if ( NTierStringUtils.isNullOrEmpty(getZZ_NBSM_MatchAction()) || 
				MMatchSetup.ZZ_NBSM_MATCHACTION____SelectTheMatchAction___.equals( 
				getZZ_NBSM_MatchAction() ) ) {
			log.saveError("Error", "Please specify the Match Action");
			return false;
		}
		if ( MMatchSetup.ZZ_NBSM_MATCHACTION_CopyReferenceValues.equals( 
				getZZ_NBSM_MatchAction() ) ) {
			if ( getC_BPartner_ID() == 0  && getC_Charge_ID() ==0 ) {
				log.saveError("Error", "Partner and/or charge is required");
				return false;
			}
		}
		if ( MMatchSetup.ZZ_NBSM_MATCHACTION_CreatePayment.equals( 
				getZZ_NBSM_MatchAction() ) ) {
			setC_Charge_ID(0);
			bpRequired = true;
			ptRequired = true;
		}
		if ( MMatchSetup.ZZ_NBSM_MATCHACTION_MatchOpenPaymentByAmount.equals( 
				getZZ_NBSM_MatchAction() ) ) {
			setC_Charge_ID(0);
			bpRequired = true;
			ptRequired = true;
		}
//		if ( getC_BPartner_ID() != 0  && getC_Charge_ID() !=0 ) {
//			log.saveError("Error", "Cannot specify both Business Partner and Charge");
//			return false;
//		}
		if ( bpRequired && getC_BPartner_ID() == 0 ) {
			log.saveError("Error", "Business Partner is required");
			return false;
		}
		if ( ptRequired && NTierStringUtils.isNullOrEmpty( getZZ_NBSM_PaymentType() ) ) {
			log.saveError("Error", "Payment type is required");
			return false;
		}
		return true;
	}
	
	/**
	 * toString method
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		try {
			sb.append("Match Setup: Line=").append(getLine());
			if ( ! NTierStringUtils.isNullOrEmpty( getZZ_NBSM_MatchText() )) {
				sb.append(", Match Text='").append( getZZ_NBSM_MatchText() ).append("'");
			}
			if ( ! NTierStringUtils.isNullOrEmpty( getDescription() )) {
				sb.append(", Description='").append( getDescription() ).append("'");
			}
//					.append("', ID=").append(get_ID());
			sb.append("] ");
			return sb.toString();
		} catch (Exception ex) {
			sb.append("] ");
			return sb.toString();
		}
	}

	
	
	
}

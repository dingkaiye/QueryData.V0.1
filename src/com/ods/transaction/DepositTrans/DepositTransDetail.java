package com.ods.transaction.DepositTrans;

import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.ods.db.DbDataLine;
import com.ods.db.DbQuery;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;
import com.ods.message.QueryMessager;
import com.ods.message.QueryResult;
import com.ods.transaction.ITransaction;
import com.ods.transaction.DepositTrans.Body.ReqBody;
import com.ods.ws.TxnBody;

public class DepositTransDetail implements ITransaction{
	Logger logger = OdsLog.getTxnLogger("DepositTransDetail");

	@Override
	public QueryMessager transaction(TxnBody txnBody, String SerialNo) throws TxnException, SQLException {
		
		logger.info("开始执行 " + SerialNo + "的查询");
		QueryResult queryResult = null;
		ReqBody reqbody = null;
		// 判断是否是所需body 
		if( (txnBody instanceof ReqBody) != true){
			throw  new TxnException(" 请求 Body 类型错误, 不是 DepositTransDetailIn 类型");
		}
		
		reqbody = (ReqBody) txnBody;
		
		// AcctId,StartDt,EndDt,PageBeginPos,PageShoeNum
		String AcctId =  reqbody.getAcctAcc() ;  //账号
		String StartDt =  reqbody.getStrDt() ;   // 开始日期
		String EndDt =  reqbody.getEndDt();   // 结束日期
		String pgBgn =  reqbody.getPgBgn();   //起始位置
		String pgShwNum =  reqbody.getPgShwNum() ;  //显示条数
		StringBuffer errorMsg = new StringBuffer();
		
		Integer PageBeginPos = 0;
		Integer PageShoeNum = 0;
		// 检查输入项  
		if( AcctId == null || "".equals(AcctId)) {
			errorMsg.append("账号不能为空,"); //
		}
		if( StartDt == null || "".equals(StartDt)) {
			errorMsg.append("起始日期不能为空,"); //
		}
		if( EndDt == null || "".equals(EndDt)) {
			errorMsg.append("结束日期不能为空,"); //
		}
		

		if (pgBgn != null && !"".equals(pgBgn)) {
			try {
				PageBeginPos = new Integer(pgBgn);
			} catch (Exception e) {
				errorMsg.append("起始日期输入错误,");
			}
		} else {
			logger.warn(SerialNo + " 起始位置为空,使用默认值 0 ");
			PageBeginPos = 0;
		}
		
		if (pgShwNum != null && !"".equals(pgShwNum)) {
			try {
				PageShoeNum = new Integer(pgShwNum);
			} catch (Exception e) {
				errorMsg.append("结束日期输入错误,");
			}

		} else {
			logger.warn(SerialNo + " 起始位置为空,使用默认值  10 ");
			PageShoeNum = 10;
		}
		
		
		if(errorMsg.length() != 0){
			logger.error(SerialNo + " 输入参数有误: " + errorMsg);
			throw new TxnException (errorMsg.toString());
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(" ACCTID    Acct, ");
		sql.append(" ACCTNAME  AcctOfNm, ");
		sql.append(" CURRID    Ccy, ");
		sql.append(" CERTTYPE  DocTp, ");
		sql.append(" CERTID    CrtfctNo, ");
		sql.append(" SEQID     EvntSrlNo, ");
		sql.append(" CARDID    CrdNum, ");
		sql.append(" ACCTNO    PsbkAcct, ");
		sql.append(" TRANSDT   TxnDt, ");
		sql.append(" AMT       TxnAmt, ");
		sql.append(" BAL       Balce, ");
		sql.append(" CTFLG     DoNotAcct, ");
		sql.append(" DCFLG     DbAndCr, ");
		sql.append(" REFLG     CorrSign, ");
		sql.append(" REMARKD   AbstRsm, ");
		sql.append(" OPPOACCT  AcctNo, ");
		sql.append(" TELLER    OprNum, ");
		sql.append(" TRANSORG  TdgNtw, ");
		sql.append(" TLRSEQNO  TelleSrlNo ");
		sql.append(" from TRANSDETAIL");
		sql.append(" where ACCTID = ? and TRANSDT >= ? and TRANSDT <= ? ");
		sql.append(" order by id, EvntSrlNo, ACCTID");
		String params[] = {AcctId, StartDt, EndDt};
		queryResult = DbQuery.excuteQuery(sql.toString(), params, PageBeginPos, PageBeginPos+PageShoeNum);
		
		ArrayList<DbDataLine> resultList = queryResult.getResultList();
		DbDataLine head = new DbDataLine();
		if( resultList.size() != 0 ){
			head = resultList.get(0);
		}
		QueryMessager Result = new QueryMessager(head, resultList);
		Result.resultHeadAdd("TotlNm", queryResult.getTotalRows()+""); 
		Result.resultHeadAdd("RtrnNm", resultList.size()+"");
		Result.resultHeadAdd("STRDT", StartDt+"");
		Result.resultHeadAdd("ENDDT", EndDt+"");
		logger.info("完成查询 " + SerialNo + "的查询");
		
		return Result;
	}

}

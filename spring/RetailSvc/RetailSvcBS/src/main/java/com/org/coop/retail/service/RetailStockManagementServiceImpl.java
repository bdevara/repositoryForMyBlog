package com.org.coop.retail.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.coop.canonical.beans.UIModel;
import com.org.coop.retail.servicehelper.RetailStockManagementServiceHelperImpl;

@Service
public class RetailStockManagementServiceImpl {

	private static final Logger log = Logger.getLogger(RetailStockManagementServiceImpl.class); 
	
	@Autowired
	private RetailStockManagementServiceHelperImpl retailStockManagementServiceHelperImpl;
	
	
	public UIModel getStockEntries(int branchId, int vendorId, int materialId, int pageNo, int recordsPerPage, String challanNo, String billNo) {
		return retailStockManagementServiceHelperImpl.getStockEntries(branchId, vendorId, materialId, pageNo, recordsPerPage, challanNo, billNo);
	}
	
	public UIModel saveStockEntries(UIModel uiModel) {
		uiModel = retailStockManagementServiceHelperImpl.saveStockEntries(uiModel);
		return uiModel;
	}
	
}

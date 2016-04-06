package com.org.coop.retail.servicehelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.coop.canonical.beans.BranchBean;
import com.org.coop.canonical.beans.RetailStockEntryBean;
import com.org.coop.canonical.beans.RetailStockReturnBean;
import com.org.coop.canonical.beans.UIModel;
import com.org.coop.retail.bs.mapper.RetailStockEntryMappingImpl;
import com.org.coop.retail.bs.mapper.RetailStockReturnMappingImpl;
import com.org.coop.retail.entities.StockEntry;
import com.org.coop.retail.entities.StockReturn;
import com.org.coop.retail.repositories.RetailStockEntryRepository;
import com.org.coop.retail.repositories.RetailStockReturnRepository;

@Service
public class RetailStockManagementServiceHelperImpl {

	private static final Logger log = Logger.getLogger(RetailStockManagementServiceHelperImpl.class); 
	
	@Autowired
	private RetailStockEntryRepository retailStockEntryRepository;
	
	@Autowired
	private RetailStockEntryMappingImpl retailStockEntryMappingImpl;
	
	@Autowired
	private RetailStockReturnRepository retailStockReturnRepository;
	
	@Autowired
	private RetailStockReturnMappingImpl retailStockReturnMappingImpl;
	
	@Transactional(value="retailTransactionManager")
	public UIModel getStockEntries(int branchId, int vendorId, int materialId, int pageNo, int recordsPerPage, String challanNo, String billNo) {
		UIModel uiModel = new UIModel();
		List<StockEntry> stocks = null;
		
		if(pageNo == 0) pageNo = 1;
		if(recordsPerPage == 0) recordsPerPage = 1000000;
		
		PageRequest pageRequest = new PageRequest(pageNo - 1, recordsPerPage, Sort.Direction.DESC, "entryDate");
		int recordCount = 0;
		if(branchId > 0) {
			if(vendorId > 0) {
				stocks = retailStockEntryRepository.findByVendorId(branchId, vendorId, pageRequest);
				recordCount = retailStockEntryRepository.countByVendorId(branchId, vendorId);
			} else if(materialId > 0) {
				stocks = retailStockEntryRepository.findByMaterialId(branchId, materialId, pageRequest);
				recordCount = retailStockEntryRepository.countByMaterialId(branchId, materialId);
			} else {
				stocks = retailStockEntryRepository.findByBranchId(branchId, pageRequest);
				recordCount = retailStockEntryRepository.countByBranchId(branchId);
			}
		}
		
		if(stocks != null && stocks.size() > 0) {
			uiModel.setRecordCount(recordCount);
			uiModel.setPageNo(pageNo);
			uiModel.setRecordsPerPage(recordsPerPage);
			
			List<RetailStockEntryBean> stocksBean = new ArrayList<RetailStockEntryBean>();
			for(StockEntry stock : stocks) {
				RetailStockEntryBean stockBean = new RetailStockEntryBean();
				retailStockEntryMappingImpl.mapBean(stock, stockBean);
				stocksBean.add(stockBean);
			}
			
			uiModel.setBranchBean(new BranchBean());
			uiModel.getBranchBean().setStockEntries(stocksBean);
		} else {
			if(log.isDebugEnabled()) {
				log.debug("No Stock-in records found for given input");
			}
		}
		return uiModel;
	}
	
	@Transactional(value="retailTransactionManager")
	public UIModel saveStockEntries(UIModel uiModel) {
		if(uiModel.getBranchBean().getStockEntries() != null && uiModel.getBranchBean().getStockEntries().size() > 0 ) {
			
			List<RetailStockEntryBean> stocksBean = uiModel.getBranchBean().getStockEntries();
			List<StockEntry> stocks = new ArrayList<StockEntry>();
			for(RetailStockEntryBean stockBean : stocksBean) {
				int stockId = stockBean.getStockId(); 
				StockEntry stock = null;
				if(stockId == 0) {
					if(log.isDebugEnabled()) {
						log.debug("New stock to be added for material: " + stockBean.getMaterialId());
					}
					stock = new StockEntry();
				} else {
					stock = retailStockEntryRepository.findOne(stockId);
					if(stock == null) {
						log.error("Incorrect stockId passed: " + stockId);
						uiModel.setErrorMsg("Incorrect stockId passed: " + stockId);
						return uiModel;
					}
				}
				
				//TODO: Need to add all validations here. Example delete stock is possible if the stock is not sold.
				// Modification of stock is possible if the sold stock is not greater than the modification qty
				retailStockEntryMappingImpl.mapBean(stockBean, stock);
				stocks.add(stock);
			}
			if(stocks.size() > 0) {
				retailStockEntryRepository.save(stocks);
				if(log.isDebugEnabled()) {
					log.debug("All stocks added");
				}
				
				// stockBean and stocks size has to be same
				if(stocksBean.size() == stocks.size()) {
					int i = 0;
					for(StockEntry stock : stocks) {
						RetailStockEntryBean stockBean = stocksBean.get(i++);
						retailStockEntryMappingImpl.mapBean(stock, stockBean);
					}
				}
			}
		}		
		return uiModel;
	}
	
	@Transactional(value="retailTransactionManager")
	public UIModel deleteStockEntries(UIModel uiModel) {
		if(uiModel.getBranchBean().getStockEntries() != null && uiModel.getBranchBean().getStockEntries().size() > 0 ) {
			
			List<RetailStockEntryBean> stocksBean = uiModel.getBranchBean().getStockEntries();
			List<StockEntry> stocks = new ArrayList<StockEntry>();
			for(RetailStockEntryBean stockBean : stocksBean) {
				int stockId = stockBean.getStockId(); 
				StockEntry stock = null;
				if(stockId == 0) {
					log.error("StockId can not be zero");
					uiModel.setErrorMsg("StockId can not be zero");
					return uiModel;
				} else {
					stock = retailStockEntryRepository.findOne(stockId);
					if(stock == null) {
						log.error("Incorrect stockId passed: " + stockId);
						uiModel.setErrorMsg("Incorrect stockId passed: " + stockId);
						return uiModel;
					}
				}
				
				// Rule 1: Stop stock deletion operation if the stock is already returned
				if(retailStockEntryRepository.checkIfAnyChildRecordExists(stockId) > 0) {
					log.error("Stock can not be deleted because it is partially/completely returned for stockId: " + stockId);
					uiModel.setErrorMsg("Stock can not be deleted because it is partially/completely returned for stockId: " + stockId);
					return uiModel;
					
				} else {
					retailStockEntryMappingImpl.mapBean(stockBean, stock);
					stocks.add(stock);
				}
			}
			if(stocks.size() > 0) {
				retailStockEntryRepository.save(stocks);
				if(log.isDebugEnabled()) {
					log.debug("All stocks updated with update user and updated date");
				}
				
				retailStockEntryRepository.delete(stocks);
				if(log.isDebugEnabled()) {
					log.debug("Selected stocks are deleted");
				}
			}
		}		
		return uiModel;
	}
	
	@Transactional(value="retailTransactionManager")
	public UIModel getStockReturns(int materialId, Date startDate, Date endDate, int pageNo, int recordsPerPage) {
		UIModel uiModel = new UIModel();
		List<StockReturn> stocks = null;
		
		if(pageNo == 0) pageNo = 1;
		if(recordsPerPage == 0) recordsPerPage = 1000000;
		
		PageRequest pageRequest = new PageRequest(pageNo - 1, recordsPerPage, Sort.Direction.DESC, "returnDate");
		int recordCount = 0;
		if(materialId > 0) {
			stocks = retailStockReturnRepository.findStocksByMaterialId(materialId, startDate, endDate, pageRequest);
			recordCount = retailStockReturnRepository.countStocksByMaterialId(materialId, startDate, endDate);
		} else {
			stocks = retailStockReturnRepository.findStocksByDateRange(startDate, endDate, pageRequest);
			recordCount = retailStockReturnRepository.countStocksByDateRange(startDate, endDate);
		}
		
		if(stocks != null && stocks.size() > 0) {
			uiModel.setRecordCount(recordCount);
			uiModel.setPageNo(pageNo);
			uiModel.setRecordsPerPage(recordsPerPage);
			
			List<RetailStockReturnBean> stocksBean = new ArrayList<RetailStockReturnBean>();
			for(StockReturn stock : stocks) {
				RetailStockReturnBean stockBean = new RetailStockReturnBean();
				retailStockReturnMappingImpl.mapBean(stock, stockBean);
				stocksBean.add(stockBean);
			}
			
			uiModel.setBranchBean(new BranchBean());
			uiModel.getBranchBean().setStockReturns(stocksBean);
			if(log.isDebugEnabled()) {
				log.debug("Retrieved Stock-return records for the given input");
			}
		} else {
			if(log.isDebugEnabled()) {
				log.debug("No Stock-return records found for given input");
			}
		}
		return uiModel;
	}
	
	@Transactional(value="retailTransactionManager")
	public UIModel saveStockReturns(UIModel uiModel) {
		if(uiModel.getBranchBean().getStockReturns() != null && uiModel.getBranchBean().getStockReturns().size() > 0 ) {
			
			List<RetailStockReturnBean> stocksBean = uiModel.getBranchBean().getStockReturns();
			List<StockReturn> stocks = new ArrayList<StockReturn>();
			for(RetailStockReturnBean stockBean : stocksBean) {
				int stockReturnId = stockBean.getStockReturnId();
				StockReturn stock = null;
				if(stockReturnId == 0) {
					if(log.isDebugEnabled()) {
						log.debug("New stock to be returned for stockId: " + stockBean.getStockId());
					}
					stock = new StockReturn();
				} else {
					stock = retailStockReturnRepository.findOne(stockReturnId);
					if(stock == null) {
						log.error("Incorrect stockReturnId passed: " + stockReturnId);
						uiModel.setErrorMsg("Incorrect stockReturnId passed: " + stockReturnId);
						return uiModel;
					}
				}
				
				//TODO: Need to add all validations here. Example delete stock is possible if the stock is not sold.
				// Modification of stock is possible if the sold stock is not greater than the modification qty
				retailStockReturnMappingImpl.mapBean(stockBean, stock);
				stocks.add(stock);
			}
			if(stocks.size() > 0) {
				retailStockReturnRepository.save(stocks);
				if(log.isDebugEnabled()) {
					log.debug("All stocks returned");
				}
				
				// stockBean and stocks size has to be same
				if(stocksBean.size() == stocks.size()) {
					int i = 0;
					for(StockReturn stock : stocks) {
						RetailStockReturnBean stockBean = stocksBean.get(i++);
						retailStockReturnMappingImpl.mapBean(stock, stockBean);
					}
				}
			}
		}		
		return uiModel;
	}
}
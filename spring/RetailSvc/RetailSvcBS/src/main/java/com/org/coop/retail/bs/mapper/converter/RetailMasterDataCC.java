package com.org.coop.retail.bs.mapper.converter;

import org.dozer.CustomConverter;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.coop.canonical.account.beans.GlMasterBean;
import com.org.coop.canonical.retail.beans.BankMasterBean;
import com.org.coop.retail.entities.BankMaster;
import com.org.coop.retail.entities.GlHeader;
import com.org.coop.retail.entities.GlMaster;
import com.org.coop.retail.entities.GlSubHeader;
import com.org.coop.retail.repositories.GlHeaderRepository;
import com.org.coop.retail.repositories.GlMasterRepository;

@Component("retailMasterDataCC")
public class RetailMasterDataCC extends DozerConverter<Object, Object> implements MapperAware, CustomConverter {
	@Autowired
	private GlMasterRepository glMasterRepository;
	
	@Autowired
	private GlHeaderRepository glHeaderRepository;
	
	public RetailMasterDataCC() {
		super(Object.class, Object.class);
	}
	
	public RetailMasterDataCC(Class prototypeA, Class prototypeB) {
		super(prototypeA, prototypeB);
	}
	
	public void setMapper(Mapper arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object convertFrom(Object src, Object dest) {
		if(src != null) {
			if("BANK_MASTER".equalsIgnoreCase(getParameter())) {
				BankMasterBean bankMasterBean = (BankMasterBean) src;
				BankMaster bankMaster = (BankMaster) dest;
				GlMaster glMaster = glMasterRepository.findOne(bankMasterBean.getGlMasCode());
				bankMaster.setGlMaster(glMaster);
			} if("GL_SUB_HEADER".equalsIgnoreCase(getParameter())) {
				GlMasterBean glMasterBean = (GlMasterBean) src;
				GlSubHeader glSubHeader = (GlSubHeader) dest;
				GlHeader glHeader = glHeaderRepository.findOne(glMasterBean.getGlHeaderCode());
				glSubHeader.setGlHeader(glHeader);
			}
		}
		return dest;
	}

	@Override
	public Object convertTo(Object src, Object dest) {
		return null;
	}
}

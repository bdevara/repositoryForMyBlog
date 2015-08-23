package com.ashish.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ashish.appConfig.AppConfig;
import com.ashish.beans.CreditCardBean;
import com.ashish.beans.DebitCardBean;
import com.ashish.business.CreditCardManagement;
import com.ashish.business.DebitCardManagement;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:**/applicationContext.xml"})
@ContextConfiguration(classes={AppConfig.class})
public class JPARepoTest {
	
	@Autowired
	private CreditCardManagement creditCardManagement;
	
	@Autowired
	private DebitCardManagement debitCardManagement;
	
	@Test
	public void addCreditcardUser() {
		CreditCardBean ccBean = new CreditCardBean();
		ccBean.setFirstName("Ashish");
		ccBean.setLastName("Mondal");
		ccBean.setUserName("ashismo");
		ccBean.setPaid(100);
		ccBean.setOutstanding(200);
		
		creditCardManagement.addCrCardCustomerData(ccBean);
	}
	
	@Test
	public void addDebitcardUser() {
		DebitCardBean dcBean = new DebitCardBean();
		dcBean.setFirstName("Ujan");
		dcBean.setLastName("Mondal");
		dcBean.setUserName("ujanmo");
		dcBean.setCurrentBal(500);
		dcBean.setTransferAmt(100);
		
		debitCardManagement.addDebitCardCustomerData(dcBean);
	}
	
	@Test
	public void payCreditCardBill() {
		
	}
}

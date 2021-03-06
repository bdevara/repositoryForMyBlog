package com.org.test.coop.master.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.coop.bs.config.DozerConfig;
import com.org.coop.bs.util.AdminSvcCommonUtil;
import com.org.coop.canonical.beans.UIModel;
import com.org.coop.canonical.retail.beans.RetailVendorBean;
import com.org.coop.retail.bs.config.RetailDozerConfig;
import com.org.coop.retail.entities.VendorMaster;
import com.org.coop.retail.servicehelper.RetailBranchSetupServiceHelperImpl;
import com.org.coop.society.data.admin.repositories.BranchMasterRepository;
import com.org.coop.society.data.transaction.config.DataAppConfig;
import com.org.test.coop.junit.JunitTestUtil;
import com.org.test.coop.society.data.transaction.config.TestDataAppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = "com.org.test.coop")
@EnableAutoConfiguration(exclude = { DataAppConfig.class, DozerConfig.class})
@ContextHierarchy({
	  @ContextConfiguration(classes={TestDataAppConfig.class, RetailDozerConfig.class})
})
@WebAppConfiguration
public class RetailVendorWSTest {
	private static final Logger logger = Logger.getLogger(RetailVendorWSTest.class);
	
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;
	
	private String addVendorJson = null;
	private String addBranchAsVendorJson = null;
	private String addBranchAsVendor1Json = null;
	private String updateVendorJson = null;
	private String addAnotherVendorJson = null;
	private String addAnotherVendorJson2 = null;
	
	private ObjectMapper om = null;
	
	@Before
	public void runBefore() throws Exception {
		try {
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			om.setDateFormat(df);
			addVendorJson = JunitTestUtil.getFileContent("inputJson/retail/branch/addVendor.json");
			addBranchAsVendorJson = JunitTestUtil.getFileContent("inputJson/retail/branch/addBranchAsVendor.json");
			addBranchAsVendor1Json = JunitTestUtil.getFileContent("inputJson/retail/branch/addBranchAsVendor1.json");
			updateVendorJson = JunitTestUtil.getFileContent("inputJson/retail/branch/updateVendor.json");
			addAnotherVendorJson = JunitTestUtil.getFileContent("inputJson/retail/branch/addAnotherVendor.json");
			addAnotherVendorJson2 = JunitTestUtil.getFileContent("inputJson/retail/branch/addAnotherVendor2.json");
		} catch (Exception e) {
			logger.error("Error while initializing: ", e);
			throw e;
		}
	}
	@Test
	public void testVendors() {
		addVendor();
		addAnotherVendor();
		addAnotherVendor2();
		updateVendor();
		getVendor();
		addBranchAsVendor();
		addBranchAsVendor1();
	}

	private void addBranchAsVendor() {
		try {
			assertNotNull(addVendorJson);
			MvcResult result = this.mockMvc.perform(post("/rest/saveVendor")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(addBranchAsVendorJson)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addBranchAsVendor.json");
			
		} catch(Exception e) {
			logger.error("Error while adding Branch as vendor", e);
			Assert.fail("Error while adding Branch as vendor");
		}
	}
	
	private void addBranchAsVendor1() {
		try {
			assertNotNull(addVendorJson);
			MvcResult result = this.mockMvc.perform(post("/rest/saveVendor")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(addBranchAsVendor1Json)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addBranchAsVendor1.json");
			
		} catch(Exception e) {
			logger.error("Error while adding Branch as vendor", e);
			Assert.fail("Error while adding Branch as vendor");
		}
	}
	
	private void addVendor() {
		try {
			assertNotNull(addVendorJson);
			MvcResult result = this.mockMvc.perform(post("/rest/saveVendor")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(addVendorJson)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addVendor.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailVendorBean vendorBean = uiModel.getBranchBean().getRetailVendors().get(0);
			assertEquals(1, vendorBean.getVendorId());
			assertEquals(1, vendorBean.getBranchId());
			assertEquals("CTS".toUpperCase(), vendorBean.getVendorName());
			assertEquals("Kolkata", vendorBean.getAddressLine1());
			assertEquals("712708", vendorBean.getPin());
			assertEquals("9830525559", vendorBean.getPhone1());
			assertEquals("ashish", vendorBean.getCreateUser());
		} catch(Exception e) {
			logger.error("Error while adding vendor", e);
			Assert.fail("Error while adding vendor");
		}
	}
	
	private void addAnotherVendor() {
		try {
			assertNotNull(addAnotherVendorJson);
			MvcResult result = this.mockMvc.perform(post("/rest/saveVendor")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(addAnotherVendorJson)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addAnotherVendor.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailVendorBean vendorBean = uiModel.getBranchBean().getRetailVendors().get(0);
			assertEquals(2, vendorBean.getVendorId());
			assertEquals(1, vendorBean.getBranchId());
			assertEquals("TCS".toUpperCase(), vendorBean.getVendorName());
			assertEquals("Kolkata", vendorBean.getAddressLine1());
			assertEquals("712708", vendorBean.getPin());
			assertEquals("9830525559", vendorBean.getPhone1());
			assertEquals("ashish", vendorBean.getCreateUser());
		} catch(Exception e) {
			logger.error("Error while adding vendor", e);
			Assert.fail("Error while adding vendor");
		}
	}
	private void addAnotherVendor2() {
		try {
			assertNotNull(addAnotherVendorJson2);
			MvcResult result = this.mockMvc.perform(post("/rest/saveVendor")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(addAnotherVendorJson2)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addAnotherVendor2.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailVendorBean vendorBean = uiModel.getBranchBean().getRetailVendors().get(0);
			assertEquals(3, vendorBean.getVendorId());
			assertEquals(1, vendorBean.getBranchId());
			assertEquals("TechMahindra".toUpperCase(), vendorBean.getVendorName());
			assertEquals("Kolkata", vendorBean.getAddressLine1());
			assertEquals("712708", vendorBean.getPin());
			assertEquals("9830525559", vendorBean.getPhone1());
			assertEquals("ashish", vendorBean.getCreateUser());
		} catch(Exception e) {
			logger.error("Error while adding vendor", e);
			Assert.fail("Error while adding vendor");
		}
	}
	
	private void updateVendor() {
		try {
			assertNotNull(updateVendorJson);
			MvcResult result = this.mockMvc.perform(post("/rest/saveVendor")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(updateVendorJson)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/updateVendor.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailVendorBean vendorBean = uiModel.getBranchBean().getRetailVendors().get(0);
			assertEquals(1, vendorBean.getVendorId());
			assertEquals(1, vendorBean.getBranchId());
			assertEquals("CTS".toUpperCase(), vendorBean.getVendorName());
			assertEquals("Kolkata", vendorBean.getAddressLine1());
			assertEquals("743502", vendorBean.getPin());
			assertEquals("9830525559", vendorBean.getPhone1());
			assertEquals("ashish", vendorBean.getCreateUser());
			assertEquals("ashish", vendorBean.getUpdateUser());
		} catch(Exception e) {
			logger.error("Error while updaing vendor", e);
			Assert.fail("Error while updaing vendor");
		}
	}
	
	private void getVendor() {
		try {
			MvcResult result = this.mockMvc.perform(get("/rest/getVendor?vendorId=1")
					 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
					).andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))
					.andReturn();
				
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/getVendor.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailVendorBean vendorBean = uiModel.getBranchBean().getRetailVendors().get(0);
			assertEquals(1, vendorBean.getVendorId());
			assertEquals(1, vendorBean.getBranchId());
			assertEquals("CTS", vendorBean.getVendorName());
			assertEquals("Kolkata", vendorBean.getAddressLine1());
			assertEquals("743502", vendorBean.getPin());
			assertEquals("9830525559", vendorBean.getPhone1());
			assertEquals("ashish", vendorBean.getCreateUser());
		} catch(Exception e) {
			logger.error("Error while retrieving vendor", e);
			Assert.fail("Error while retrieving vendor");
		}
	}
	
	
	private UIModel getUIModel(MvcResult result)
			throws UnsupportedEncodingException, IOException,
			JsonParseException, JsonMappingException {
		String json = result.getResponse().getContentAsString();
		UIModel createBranchBean = om.readValue(json, UIModel.class);
		return createBranchBean;
	}
	
	private UIModel getUIModel(MvcResult result, String path)
			throws UnsupportedEncodingException, IOException,
			JsonParseException, JsonMappingException {
		UIModel createBranchBean = getUIModel(result);
		JunitTestUtil.writeJSONToFile(createBranchBean, path);
		return createBranchBean;
	}
	
}

package com.org.test.coop.master.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.apache.log4j.Logger;
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
import com.org.coop.canonical.retail.beans.RetailRateChartBean;
import com.org.coop.retail.bs.config.RetailDozerConfig;
import com.org.coop.retail.entities.RetailRateChart;
import com.org.coop.retail.repositories.RetailRateChartRepository;
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
public class RetailRateChartWSTest {
	private static final Logger logger = Logger.getLogger(RetailRateChartWSTest.class);
	
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private RetailRateChartRepository retailRateChartRepository;
	
	@Autowired
	private AdminSvcCommonUtil adminSvcCommonUtil;
	
	private String addRetailRateChartJson = null;
	private String updateRetailRateChartJson = null;
	private String addAnotherRateChart = null;
	
	private ObjectMapper om = null;
	
	@Before
	public void runBefore() throws Exception {
		try {
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			om.setDateFormat(df);
			addRetailRateChartJson = JunitTestUtil.getFileContent("inputJson/retail/branch/addRateChart.json");
			addAnotherRateChart = JunitTestUtil.getFileContent("inputJson/retail/branch/addAnotherRateChart.json");
			updateRetailRateChartJson = JunitTestUtil.getFileContent("inputJson/retail/branch/updateRateChart.json");
			
		} catch (Exception e) {
			logger.error("Error while initializing: ", e);
			throw e;
		}
	}
	@Test
	public void testRateChart() {
		addRetailRate();
		addAnotherRateChart();
		updateTodaysRateChart();
		updateExistingRateChart();
		getRateChartByRateId();
		getRateChartByMaterialId();
	}

	private void addRetailRate() {
		try {
			assertNotNull(addRetailRateChartJson);
			MvcResult result = this.mockMvc.perform(post("/rest/saveRateChart")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(addRetailRateChartJson)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/updateRateChart.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailRateChartBean retailRateChartBean = uiModel.getBranchBean().getRetailRateCharts().get(0);
			assertEquals(1, retailRateChartBean.getRateId());
			assertEquals(2, retailRateChartBean.getBranchId());
			assertEquals(1, retailRateChartBean.getMaterialId());
			assertEquals(new BigDecimal("150.00"), retailRateChartBean.getUnitRate());
		} catch(Exception e) {
			logger.error("Error while adding rate chart", e);
		}
	}
	
	private void addAnotherRateChart() {
		try {
			assertNotNull(addAnotherRateChart);
			MvcResult result = this.mockMvc.perform(post("/rest/saveRateChart")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(addAnotherRateChart)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addAnotherRateChart.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailRateChartBean retailRateChartBean = uiModel.getBranchBean().getRetailRateCharts().get(0);
			assertEquals(2, retailRateChartBean.getRateId());
			assertEquals(2, retailRateChartBean.getBranchId());
			assertEquals(2, retailRateChartBean.getMaterialId());
			assertEquals(new BigDecimal("200.00"), retailRateChartBean.getUnitRate());
		} catch(Exception e) {
			logger.error("Error while adding rate chart", e);
		}
	}
	
	private void updateTodaysRateChart() {
		try {
			assertNotNull(updateRetailRateChartJson);
			RetailRateChart rrc = retailRateChartRepository.findOne(1);
			rrc.setStartDate(new java.sql.Date(new Date().getTime()));
			retailRateChartRepository.save(rrc);
			MvcResult result = this.mockMvc.perform(post("/rest/saveRateChart")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(updateRetailRateChartJson)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addVendor.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailRateChartBean retailRateChartBean = uiModel.getBranchBean().getRetailRateCharts().get(0);
			assertEquals(1, retailRateChartBean.getRateId());
			assertEquals(2, retailRateChartBean.getBranchId());
			assertEquals(1, retailRateChartBean.getMaterialId());
			assertEquals(new BigDecimal("200.00"), retailRateChartBean.getUnitRate());
		} catch(Exception e) {
			logger.error("Error while updaing retail rate chart", e);
		}
	}
	
	private void updateExistingRateChart() {
		try {
			assertNotNull(updateRetailRateChartJson);
			RetailRateChart rrc = retailRateChartRepository.findOne(1);
			rrc.setStartDate(new java.sql.Date(adminSvcCommonUtil.getDatesByOffsetDays(new Date(), -2).getTime()));
			retailRateChartRepository.save(rrc);
			
			MvcResult result = this.mockMvc.perform(post("/rest/saveRateChart")
				 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
				 .content(updateRetailRateChartJson)
				).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
			
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/addVendor.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailRateChartBean retailRateChartBean = uiModel.getBranchBean().getRetailRateCharts().get(0);
			assertEquals(3, retailRateChartBean.getRateId());
			assertEquals(2, retailRateChartBean.getBranchId());
			assertEquals(1, retailRateChartBean.getMaterialId());
			assertEquals(new BigDecimal("200.00"), retailRateChartBean.getUnitRate());
		} catch(Exception e) {
			logger.error("Error while updaing retail rate chart", e);
		}
	}
	
	private void getRateChartByRateId() {
		try {
			MvcResult result = this.mockMvc.perform(get("/rest/getRateChart?rateChartId=2")
					 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
					).andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))
					.andReturn();
				
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/getRateChart.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailRateChartBean retailRateChartBean = uiModel.getBranchBean().getRetailRateCharts().get(0);
			assertEquals(2, retailRateChartBean.getRateId());
			assertEquals(2, retailRateChartBean.getBranchId());
			assertEquals(2, retailRateChartBean.getMaterialId());
			assertEquals(new BigDecimal("200.00"), retailRateChartBean.getUnitRate());
		} catch(Exception e) {
			logger.error("Error while retrieving vendor", e);
		}
	}
	
	private void getRateChartByMaterialId() {
		try {
			MvcResult result = this.mockMvc.perform(get("/rest/getRateChart?materialId=1")
					 .contentType("application/json").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("ashish:ashish".getBytes()))
					).andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))
					.andReturn();
				
			UIModel uiModel = getUIModel(result, "outputJson/retail/branch/getRateChart.json");
			
			assertNull(uiModel.getErrorMsg());
			RetailRateChartBean retailRateChartBean = uiModel.getBranchBean().getRetailRateCharts().get(0);
			assertEquals(3, retailRateChartBean.getRateId());
			assertEquals(2, retailRateChartBean.getBranchId());
			assertEquals(1, retailRateChartBean.getMaterialId());
			assertEquals(new BigDecimal("200.00"), retailRateChartBean.getUnitRate());
		} catch(Exception e) {
			logger.error("Error while retrieving vendor", e);
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

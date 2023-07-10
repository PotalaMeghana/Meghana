package eStoreProduct.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eStoreProduct.DAO.admin.EmailConfigDAO;
import eStoreProduct.DAO.admin.OrderValueWiseShippingChargeDAO;
import eStoreProduct.DAO.admin.RegionDAO;
import eStoreProduct.DAO.admin.adminDAO;
import eStoreProduct.model.admin.entities.EmailConfigModel;
import eStoreProduct.model.admin.input.OrderValueWiseShippingChargesInput;
import eStoreProduct.model.admin.input.Regions;
import eStoreProduct.model.admin.output.OrderValueWiseShippingCharge;
import eStoreProduct.model.admin.output.RegionsOutput;

@Controller
public class adminSettingsController {
	adminDAO adao;
	EmailConfigDAO edao;
	RegionDAO redao;
	OrderValueWiseShippingChargeDAO owsc;
	private static final Logger logger = LoggerFactory.getLogger(adminReportController.class);

	@Autowired
	public adminSettingsController(adminDAO admindao, EmailConfigDAO edao, RegionDAO redao,
			OrderValueWiseShippingChargeDAO owsc) {

		adao = admindao;
		this.edao = edao;
		this.redao = redao;
		this.owsc = owsc;

	}

	// Add a region
	@RequestMapping(value = "/addRegion", method = RequestMethod.POST)
	@ResponseBody
	public String addRegion(@Validated Regions reg, Model model) {
		logger.info("adminSettingsController url: addRegion returns: DONE");
		redao.addRegion(reg);
		// Return response
		return "DONE";
	}

	// Remove a region
	@RequestMapping(value = "/remRegion", method = RequestMethod.POST)
	@ResponseBody
	public String addRegion(@RequestParam("regionId") String id, Model model) {
		logger.info("adminSettingsController url: remRegion returns: DONE");
		int Id = Integer.parseInt(id);
		redao.removeRegion(Id);
		// Return response
		return "DONE";
	}


	// Redirect to shipping configuration page
	@RequestMapping(value = "/ShippingRedirect", method = RequestMethod.GET)
	public String shippingRedirect(Model model) {
		logger.info("adminSettingsController url: ShippingRedirect returns: regions.jsp");
		List<RegionsOutput> regionList = redao.getRegions();
		model.addAttribute("regionList", regionList);
		// Call the view
		return "regions";
	}

	// Update email configuration
	@RequestMapping(value = "/EmailConfiguration", method = RequestMethod.POST)
	@ResponseBody
	public String emailConfiguration(@Validated EmailConfigModel ecm, Model model) {
		logger.info("adminSettingsController url: EmailConfiguration returns: done");
		edao.changeEmail(ecm);
		return "done";
	}

	// Return email configuration page
	@RequestMapping(value = "/EmailConfigurationPage", method = RequestMethod.GET)
	public String returnpage(Model model) {
		logger.info("adminSettingsController url: EmailConfigurationPage returns: emailConfig.jsp");
		return "emailConfig";
	}

	// Return order value-wise shipping charges page
	@RequestMapping(value = "/OrderValueWisePage", method = RequestMethod.GET)
	public String OrderValueWisePage(Model model) {
		logger.info("adminSettingsController url: OrderValueWisePage returns: OrderValueWisePage.jsp");
		List<OrderValueWiseShippingCharge> ocl = owsc.getAll();
		model.addAttribute("chargeList", ocl);
		// Call the view
		return "OrderValueWisePage";
	}

	// Update order value-wise shipping charge
	@RequestMapping(value = "/updateCharge", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateCharge(@Validated OrderValueWiseShippingChargesInput osw, Model model) {
		// Return response
		return owsc.updateCharge(osw);
	}

	// Add order value-wise shipping charge
	@RequestMapping(value = "/addCharge", method = RequestMethod.POST)
	@ResponseBody
	public boolean addCharge(@Validated OrderValueWiseShippingChargesInput osw, Model model) {
		// Return response
		return owsc.addCharge(osw);
	}

}
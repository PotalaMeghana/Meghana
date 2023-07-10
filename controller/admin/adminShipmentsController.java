package eStoreProduct.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eStoreProduct.DAO.admin.orderProductDAO;
import eStoreProduct.DAO.common.OrderDAO;
import eStoreProduct.model.admin.entities.orderModel;
import eStoreProduct.model.admin.input.orderProductInput;
import eStoreProduct.model.admin.output.orderProductsModel;

@Controller
public class adminShipmentsController {
	private static final Logger logger = LoggerFactory.getLogger(adminShipmentsController.class);
	private OrderDAO od;
	private orderModel om;
	private orderProductDAO opd;

	@Autowired
	adminShipmentsController(OrderDAO ord, orderModel omd, orderProductDAO orderproductdao) {
		od = ord;
		om = omd;
		opd = orderproductdao;
	}

	//displayed only the orders that are processed from the orders page and not all the ordersProducts in the order is not delivred or canclled .It is shipments progress page
	@GetMapping("/displayProcessedOrdersInShipments")
	public String showProcessedOrders(Model model) {
		logger.info("displayed only the orders that are processed from the orders page and not all the ordersProducts in the order is not delivred or canclled .It is shipments progress page");
		List<orderModel> orders = od.getAllOrders();
		model.addAttribute("orders", orders);
		return "shipmentProgressPage";
	}

	
	// Display processed order products to update their status
	@GetMapping("/displayProcessedOrderProductsToUpdateStatus")
	public String showProcessedOrderProducts(@RequestParam(value = "orderId") int o_id, Model model) {
		logger.info("adminShipmentsController url: displayProcessedOrderProductsToUpdateStatus returns: orderProductsListPage.jsp");
		List<orderProductsModel> orderproducts = opd.getOrderWiseOrderProducts(o_id);
		model.addAttribute("orderproducts", orderproducts);
		return "orderProductsListPage";
	}

	
	// Update the status of order products
	@PostMapping("/updateOrderProductStatus")
	public String updateStatusOrderProducts(@Validated orderProductInput opm1, Model model) {
		logger.info("adminShipmentsController url: updateOrderProductStatus returns: orderProductsListPage.jsp");
		opd.updateOrderProductStatus(opm1);
		List<orderProductsModel> orderproducts = opd.getOrderWiseOrderProducts(opm1.getOrdr_id());
		model.addAttribute("orderproducts", orderproducts);
		return "orderProductsListPage";
	}

	//displaying the orders that are shipped
	@GetMapping("/displayShippedOrders")
	public String showShippedOrders(Model model) {
		logger.info("displaying the orders that are shipped");
		List<orderModel> orders = od.getAllOrders();
		model.addAttribute("orders", orders);
		return "shipmentsShippedPage";
	}
}

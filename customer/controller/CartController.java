package eStoreProduct.customer.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eStoreProduct.BLL.FairandGStBLL;
import eStoreProduct.DAO.customer.ProductDAO1;
import eStoreProduct.DAO.customer.ServicableRegionDAO;
import eStoreProduct.DAO.customer.cartDAO;
import eStoreProduct.DAO.customer.customerDAO1;
import eStoreProduct.utility.ProductStockPriceForCust;
import eStoreProduct.model.customer.input.custCredModel;
import eStoreProduct.model.customer.input.cartModel;
@Controller
public class CartController {
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);

	ServicableRegionDAO sdao;
	static boolean flag = false;
	cartDAO cartimp;
	private final ProductDAO1 pdaoimp;
	public List<ProductStockPriceForCust> alist = new ArrayList<>();
	customerDAO1 cdao;
	FairandGStBLL BLL;

	@Autowired
	public CartController(cartDAO cartdao, ProductDAO1 productdao, customerDAO1 cdao, FairandGStBLL b,
			ServicableRegionDAO sdao) {
		cartimp = cartdao;
		pdaoimp = productdao;
		this.cdao = cdao;
		this.sdao = sdao;
		BLL = b;
	}

	// Method to add the product to the cart and send response back
	@GetMapping("/addToCart")
	@ResponseBody
	public String addToCart(@RequestParam(value = "productId", required = true) int productId, Model model,
			HttpSession session) throws NumberFormatException, SQLException {
		logger.info("eStoreProduct:CartController:adding product to cart");

		custCredModel cust1 = (custCredModel) session.getAttribute("customer");
		if (cust1 != null) {
			return cartimp.addToCart(productId, cust1.getCustId());
		} else {
			// If user is not logged in, add the product to a temporary list
			ProductStockPriceForCust product = pdaoimp.getProductById(productId);
			for (ProductStockPriceForCust p : alist) {
				if (p.getProd_id() == product.getProd_id()) {
					return "Already added to cart";
				}
			}
			product.setQuantity(1);
			alist.add(product);
			model.addAttribute("alist", alist);
			return "Added to cart";
		}
	}

	// Display the cart items and forward to the cart.jsp view
	@RequestMapping(value = "/cartDisplay", method = RequestMethod.GET)
	public String cartdisplay(Model model, HttpSession session) {
		logger.info("eStoreProduct:CartController:Displaying products from cart");

		custCredModel cust = (custCredModel) session.getAttribute("customer");
		if (cust != null) {
			// If user is logged in, retrieve products from the cart and calculate the cart cost
			List<ProductStockPriceForCust> products = cartimp.getCartProds(cust.getCustId());
			model.addAttribute("products", products);
			double cartcost = BLL.getCartCost(cust.getCustId());
			model.addAttribute("cartcost", cartcost);
			model.addAttribute("cust", cust);
			return "cart";
		} else {
			// If user is not logged in, display products from the temporary list and calculate the cart cost
			double cartcost = BLL.getCartCost(alist);
			model.addAttribute("cartcost", cartcost);
			model.addAttribute("alist", alist);
			return "cart";
		}
	}

	@RequestMapping(value = "/signOk", method = RequestMethod.GET)
	public ResponseEntity<String> getHomeFinal(@RequestParam("em") String email, @RequestParam("ps") String psd,
			Model model, HttpSession session) {
		System.out.println("customer in controller  " + psd);
		custCredModel cust = cdao.getCustomer(email, psd);
		if (cust != null) {
			flag = true;
			cdao.updatelastlogin(cust.getCustId());
			session.setAttribute("customer", cust);
			model.addAttribute("fl", flag);

			if (alist != null) {
				for (ProductStockPriceForCust psp : alist) {
					cartimp.addToCart(psp.getProd_id(), cust.getCustId());
				}
				List<ProductStockPriceForCust> products = cartimp.getCartProds(cust.getCustId());
				model.addAttribute("products", products);
				return ResponseEntity.ok("valid");
			}
		}
		return ResponseEntity.ok("invalid");
	}

	// Remove product from the cart
	@GetMapping("/removeFromCart")
	@ResponseBody
	public String removeFromCart(@RequestParam(value = "productId", required = true) int productId, Model model,
			HttpSession session) throws NumberFormatException, SQLException {
		logger.info("eStoreProduct:CartController:removing product from cart");

		custCredModel cust1 = (custCredModel) session.getAttribute("customer");
		if (cust1 != null) {
			// If user is logged in, remove the product from the cart
			cartimp.removeFromCart(productId, cust1.getCustId());
			return "Removed from cart";
		} else {
			// If user is not logged in, remove the product from the temporary list
			for (ProductStockPriceForCust p : alist) {
				if (p.getProd_id() == productId)
					alist.remove(p);
			}
			return "Removed from cart";
		}
	}

	// Update the quantity of a product in the cart
	@PostMapping("/updateQuantity")
	@ResponseBody
	public String updateQuantity(@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value = "quantity", required = true) int quantity, Model model, HttpSession session)
			throws NumberFormatException, SQLException {
		logger.info("eStoreProduct:CartController:updating the quantity of cart products");

		double cartcost = 0.0;
		custCredModel cust1 = (custCredModel) session.getAttribute("customer");
		if (cust1 != null) {
			// If user is logged in, update the quantity of the product in the cart and recalculate the cart cost
			cartModel cart = new cartModel(cust1.getCustId(), productId, quantity);
			cartimp.updateQty(cart);
			List<ProductStockPriceForCust> products = cartimp.getCartProds(cust1.getCustId());
			session.setAttribute("products", products);
			cartcost = (BLL.getCartCost(cust1.getCustId()));
			String ccost = String.valueOf(cartcost);
			return ccost;
		} else {
			// If user is not logged in, update the quantity of the product in the temporary list and recalculate the cart cost
			for (ProductStockPriceForCust product : alist) {
				if (product.getProd_id() == productId) {
					product.setQuantity(quantity);
				}
			}
			cartcost = (BLL.getCartCost(alist));
			return String.valueOf(cartcost);
		}
	}

	// Update the cost of cart products
	@PostMapping("/updateCostOnLoad")
	@ResponseBody
	public String updateCostOnLoad(Model model, HttpSession session) throws NumberFormatException, SQLException {
		logger.info("eStoreProduct:CartController:In cart, update the cost of cart items");

		double cartcost = 0.0;
		custCredModel cust1 = (custCredModel) session.getAttribute("customer");
		if (cust1 != null) {
			// If user is logged in, retrieve products from thecart and recalculate the cart cost
			List<ProductStockPriceForCust> products = cartimp.getCartProds(cust1.getCustId());
			session.setAttribute("products", products);
			cartcost = (BLL.getCartCost(cust1.getCustId()));
			String ccost = String.valueOf(cartcost);
			return ccost;
		} else {
			// If user is not logged in, recalculate the cart cost based on the products in the temporary list
			cartcost = (BLL.getCartCost(alist));
			return String.valueOf(cartcost);
		}
	}

	// Check the availability of products based on pincode
	@PostMapping("/checkPincodeValidity")
	@ResponseBody
	public String checkPincodeValidity(@RequestParam(value = "pincode", required = true) String pincode, Model model,
			HttpSession session) throws NumberFormatException, SQLException {
		logger.info("eStoreProduct:CartController:Checking the pincode Availability");

		return sdao.getValidityOfPincode(Integer.parseInt(pincode)) + "";
	}
}
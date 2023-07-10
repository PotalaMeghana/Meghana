package TestClasses;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eStoreProduct.DAO.ProdStockDAO;
import eStoreProduct.DAO.cartDAOImp;
import eStoreProduct.model.ServiceableRegion;
import eStoreProduct.model.cartModel;
import eStoreProduct.model.hsnModel;
import eStoreProduct.utility.ProductStockPrice;

public class CartDAOTestClass {

	@Mock
	private DataSource mockDataSource;

	@Mock
	private JdbcTemplate mockJdbcTemplate;

	@Mock
	private ProdStockDAO mockProdStockDAO;

	@InjectMocks
	private cartDAOImp cartDAO;

	private String insert_slam_cart = "INSERT INTO slam_cart (cust_id,prod_id,quantity) VALUES (?, ?,1)";
	private String delete_slam_cart = "DELETE FROM SLAM_CART WHERE cust_id=? AND prod_id=?";
	private String select_cart_products = "SELECT pd.*,sc.* FROM slam_Products pd, slam_cart sc WHERE sc.cust_id = ? AND sc.prod_id = pd.prod_id";
	private String update_qty = "update slam_cart set quantity=? where cust_id=? and prod_id=?";
	private String insert = "insert into slam_cart values(?,?,?);";
	private String select_checkcart_products = "SELECT pd.*,sc.* FROM slam_Products pd, slam_cart sc WHERE sc.cust_id = ? AND sc.prod_id = pd.prod_id and pd.prod_id=?";

	@BeforeClass
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		cartDAO = new cartDAOImp(mockDataSource, mockProdStockDAO);
		cartDAO.jdbcTemplate = mockJdbcTemplate;
	}

	@Test
	public void testAddToCartWhenProductNotInCart() {
		int productId = 1;
		int customerId = 100;
		List<ProductStockPrice> cproducts = new ArrayList<>();
		when(mockJdbcTemplate.query(eq(select_checkcart_products), any(RowMapper.class), eq(customerId), eq(productId)))
				.thenReturn(cproducts);
		when(mockJdbcTemplate.update(eq(insert_slam_cart), eq(customerId), eq(productId))).thenReturn(1);

		String result = cartDAO.addToCart(productId, customerId);

		verify(mockJdbcTemplate, atLeastOnce()).query(eq(select_checkcart_products), any(RowMapper.class), eq(customerId), eq(productId));
		verify(mockJdbcTemplate, atLeastOnce()).update(eq(insert_slam_cart), eq(customerId), eq(productId));
		assertEquals(result, "Added to cart");
	}

	@Test
	public void testAddToCartWhenProductAlreadyInCart() {
		int productId = 1;
		int customerId = 100;
		List<ProductStockPrice> cproducts = new ArrayList<>();
		ProductStockPrice productInCart = new ProductStockPrice();
		productInCart.setProd_id(productId);
		cproducts.add(productInCart);
		when(mockJdbcTemplate.query(eq(select_checkcart_products), any(RowMapper.class), eq(customerId), eq(productId)))
				.thenReturn(cproducts);

		String result = cartDAO.addToCart(productId, customerId);

		verify(mockJdbcTemplate, atLeastOnce()).query(eq(select_checkcart_products), any(RowMapper.class), eq(customerId), eq(productId));
		verify(mockJdbcTemplate, atLeastOnce()).update(eq(insert_slam_cart), eq(customerId), eq(productId));
		assertEquals(result, "Already added to cart");
	}

	@Test
	public void testAddToCartWhenInsertFails() {
		int productId = 1;
		int customerId = 100;
		List<ProductStockPrice> cproducts = new ArrayList<>();
		when(mockJdbcTemplate.query(eq(select_checkcart_products), any(RowMapper.class), eq(customerId), eq(productId)))
				.thenReturn(cproducts);
		when(mockJdbcTemplate.update(eq(insert_slam_cart), eq(customerId), eq(productId))).thenReturn(0);

		String result = cartDAO.addToCart(productId, customerId);

		verify(mockJdbcTemplate, atLeastOnce()).query(eq(select_checkcart_products), any(RowMapper.class), eq(customerId), eq(productId));
		verify(mockJdbcTemplate, atLeastOnce()).update(eq(insert_slam_cart), eq(customerId), eq(productId));
		assertEquals(result, "error");
	}

	@Test
	public void testRemoveFromCartWhenDeleteSucceeds() {
		int productId = 1;
		int customerId = 100;
		when(mockJdbcTemplate.update(eq(delete_slam_cart), eq(customerId), eq(productId))).thenReturn(1);

		int result = cartDAO.removeFromCart(productId, customerId);

		verify(mockJdbcTemplate, atLeastOnce()).update(eq(delete_slam_cart), eq(customerId), eq(productId));
		assertEquals(result, productId);
	}

	@Test
	public void testRemoveFromCartWhenDeleteFails() {
		int productId = 1;
		int customerId = 100;
		when(mockJdbcTemplate.update(eq(delete_slam_cart), eq(customerId), eq(productId))).thenReturn(0);

		int result = cartDAO.removeFromCart(productId, customerId);

		verify(mockJdbcTemplate, atLeastOnce()).update(eq(delete_slam_cart), eq(customerId), eq(productId));
		assertEquals(result, -1);
	}

	@Test
	public void testGetCartProdsWhenProductsExistInCart() {
		int customerId = 100;
		List<ProductStockPrice> expectedProducts = new ArrayList<>();
		when(mockJdbcTemplate.query(eq(select_cart_products), any(RowMapper.class), eq(customerId)))
				.thenReturn(expectedProducts);

		List<ProductStockPrice> result = cartDAO.getCartProds(customerId);

		verify(mockJdbcTemplate, atLeastOnce()).query(eq(select_cart_products), any(RowMapper.class), eq(customerId));
		assertEquals(result, expectedProducts);
	}

	@Test
	public void testGetCartProdsWhenNoProductsInCart() {
		int customerId = 100;
		when(mockJdbcTemplate.query(eq(select_cart_products), any(RowMapper.class), eq(customerId)))
				.thenReturn(Collections.emptyList());

		List<ProductStockPrice> result = cartDAO.getCartProds(customerId);

		verify(mockJdbcTemplate, atLeastOnce()).query(eq(select_cart_products), any(RowMapper.class), eq(customerId));
		assertTrue(result.isEmpty());
	}

	@Test
	public void testUpdateQtyWhenUpdateSucceeds() {
		cartModel cm = new cartModel();
		cm.setQty(5);
		cm.setCid(100);
		cm.setPid(1);
		when(mockJdbcTemplate.update(eq(update_qty), eq(cm.getQty()), eq(cm.getCid()), eq(cm.getPid()))).thenReturn(1);

		int result = cartDAO.updateQty(cm);

		verify(mockJdbcTemplate, atLeastOnce()).update(eq(update_qty), eq(cm.getQty()), eq(cm.getCid()), eq(cm.getPid()));
		assertEquals(result, 1);
	}

	@Test
	public void testUpdateQtyWhenUpdateFails() {
		cartModel cm = new cartModel();
		cm.setQty(5);
		cm.setCid(100);
		cm.setPid(1);
		when(mockJdbcTemplate.update(eq(update_qty), eq(cm.getQty()), eq(cm.getCid()), eq(cm.getPid()))).thenReturn(0);

		int result = cartDAO.updateQty(cm);

		verify(mockJdbcTemplate, atLeastOnce()).update(eq(update_qty), eq(cm.getQty()), eq(cm.getCid()), eq(cm.getPid()));
		assertEquals(result, -1);
	}

	@Test
	public void testUpdateinsertWhenUpdateSucceeds() {
		List<ProductStockPrice> products = new ArrayList<>();
		ProductStockPrice product1 = new ProductStockPrice();
		product1.setProd_id(1);
		product1.setQuantity(5);
		ProductStockPrice product2 = new ProductStockPrice();
		product2.setProd_id(2);
		product2.setQuantity(3);
		products.add(product1);
		products.add(product2);

		when(mockJdbcTemplate.update(eq(insert), eq(100), eq(1), eq(5))).thenReturn(1);
		when(mockJdbcTemplate.update(eq(insert), eq(100), eq(2), eq(3))).thenReturn(1);

		int result = cartDAO.updateinsert(products, 100);

		verify(mockJdbcTemplate, atLeastOnce()).update(eq(insert), eq(100), eq(1), eq(5));
		verify(mockJdbcTemplate, atLeastOnce()).update(eq(insert), eq(100), eq(2), eq(3));
		assertEquals(result, 1);
	}

	@Test
	public void testUpdateinsertWhenUpdateFails() {
		List<ProductStockPrice> products = new ArrayList<>();
		ProductStockPrice product1 = new ProductStockPrice();
		product1.setProd_id(1);
		product1.setQuantity(5);
		ProductStockPrice product2 = new ProductStockPrice();
		product2.setProd_id(2);
		product2.setQuantity(3);
		products.add(product1);
		products.add(product2);

		when(mockJdbcTemplate.update(eq(insert), eq(100), eq(1), eq(5))).thenReturn(0);

		int result = cartDAO.updateinsert(products, 100);

		verify(mockJdbcTemplate, atLeastOnce()).update(eq(insert), eq(100), eq(1), eq(5));
		verify(mockJdbcTemplate, atLeastOnce()).update(eq(insert), eq(100), eq(2), eq(3));
		assertEquals(result, 0);
	}

	@Test
	public void testGetHSNCodeByProductId() {
		int prodId = 1;
		hsnModel expectedHSNCode = new hsnModel();
		expectedHSNCode.setHsn_code(prodId);
		when(mockJdbcTemplate.queryForObject(any(String.class), any(Object[].class), any(RowMapper.class)))
				.thenReturn(expectedHSNCode);

		hsnModel result = cartDAO.getHSNCodeByProductId(prodId);

		verify(mockJdbcTemplate, atLeastOnce()).queryForObject(any(String.class), any(Object[].class), any(RowMapper.class));
		assertEquals(result, expectedHSNCode);
	}

	@Test
	public void testGetRegionByPincodeWhenRegionExists() {
		int pincode = 12345;
		ServiceableRegion expectedRegion = new ServiceableRegion();
		expectedRegion.setSrrgId(1);
		when(mockJdbcTemplate.query(any(String.class), any(RowMapper.class), eq(pincode)))
				.thenReturn(Collections.singletonList(expectedRegion));

		ServiceableRegion result = cartDAO.getRegionByPincode(pincode);

		verify(mockJdbcTemplate, atLeastOnce()).query(any(String.class), any(RowMapper.class), eq(pincode));
		assertEquals(result, expectedRegion);
	}

	@Test
	public void testGetRegionByPincodeWhenRegionDoesNotExist() {
		int pincode = 12345;
		when(mockJdbcTemplate.query(any(String.class), any(RowMapper.class), eq(pincode))).thenReturn(Collections.emptyList());

		ServiceableRegion result = cartDAO.getRegionByPincode(pincode);

		verify(mockJdbcTemplate, atLeastOnce()).query(any(String.class), any(RowMapper.class), eq(pincode));
		assertEquals(result, null);
	}
	

}

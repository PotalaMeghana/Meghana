package eStoreProduct.DAO.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import eStoreProduct.Exceptions.QuantityExceedsStockException;

@Component
public class StockUpdaterDAOImp implements StockUpdaterDAO {
	private final DataSourceTransactionManager transactionManager1;
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public StockUpdaterDAOImp(DataSourceTransactionManager transactionManager, JdbcTemplate jdbcTemplate) {
		this.transactionManager1 = transactionManager;
		this.jdbcTemplate = jdbcTemplate;
	}

	// Method to get the current stock of a product
	public int getProductStock(int productId) {
		// Retrieve the product stock from the database
		return jdbcTemplate.queryForObject("SELECT prod_stock FROM slam_productstock WHERE prod_id=?",
				new Object[] { productId }, Integer.class);
	}

	// Method to update the product stocks
	public void updateStocks(int prod_id, int qty) {
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		TransactionStatus transactionStatus = transactionManager1.getTransaction(transactionDefinition);
		System.out.println("   qty " + qty);

		try {
			// Calculate the new stock value
			int stock = getProductStock(prod_id) - qty;

			// Update the product stock in the database
			String update_stock = "UPDATE slam_productStock SET prod_stock=? WHERE prod_id=? ";
			jdbcTemplate.update(update_stock, stock, prod_id);

			// Perform any additional update operations here

			transactionManager1.commit(transactionStatus);
		} catch (Exception e) {
			transactionManager1.rollback(transactionStatus);
			// Handle the exception
		}
	}

	// Method to update the product quantity before checkout
	@Override
	public void updateQtyBeforeCheckOut(int prod_id, int qty) throws QuantityExceedsStockException {
		// Get the current stock of the product
		int prodstock = getProductStock(prod_id);

		if (getProductStock(prod_id) >= qty) {
			// Update the product stock after deducting the quantity
			String updateSt = "UPDATE slam_productstock SET prod_stock = ? WHERE prod_id = ?";
			jdbcTemplate.update(updateSt, (prodstock - qty), prod_id);
		} else {
			throw new QuantityExceedsStockException("Quantity exceeds product stock");
		}
	}

	// Method to update the product quantity after payment failure
	@Override
	public void updateQtyAfterPayFail(int prod_id, int qty) {
		// Get the current stock of the product
		int prodstock = getProductStock(prod_id);

		// Update the product stock by adding the quantity back
		String updateSt = "UPDATE slam_productstock SET prod_stock = ? WHERE prod_id = ?";
		jdbcTemplate.update(updateSt, (qty + prodstock), prod_id);
	}
}
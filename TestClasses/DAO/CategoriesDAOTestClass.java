package TestClasses;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eStoreProduct.DAO.CategoryDAOImp;
import eStoreProduct.model.Category;


public class CategoriesDAOTestClass {

	@Mock
	private DataSource mockDataSource;
	
	@Mock
	private JdbcTemplate mockJdbcTemplate;
	
	@Mock
	private RowMapper<Category> mockCategoryRowMapper;

	private CategoryDAOImp categoryDAO;
	private String SQL_GET_CATEGORIES = "SELECT prct_title FROM slam_productCategories";
	private String SQL_INSERT_CATEGORY = "insert into slam_productCAtegories(prct_id,prct_title,prct_desc) values(?,?,?)";
	private String SQL_GET_TOP_CATGID = "select prct_id from slam_productCAtegories order by prct_id desc limit 1";
	@BeforeClass
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		categoryDAO = new CategoryDAOImp(mockDataSource);
		categoryDAO.jdbcTemplate = mockJdbcTemplate;
	}
	@Test
	public void testAddNewCategory() {
	    // Example category data
	    Category category = new Category();
	    category.setPrct_title("Test Category");
	    category.setPrct_desc("Test Description");

		// Mock the behavior of the jdbcTemplate
	    when(mockJdbcTemplate.queryForObject(eq(SQL_GET_TOP_CATGID), eq(Integer.class))).thenReturn(1);
	    when(mockJdbcTemplate.update(eq(SQL_INSERT_CATEGORY), any(Integer.class), eq(category.getPrct_title()), eq(category.getPrct_desc()))).thenReturn(1);
	    

	    // Execute the addNewCategory method
	    boolean result = categoryDAO.addNewCategory(category);

	    // Verify the behavior
	    verify(mockJdbcTemplate).queryForObject(eq(SQL_GET_TOP_CATGID), eq(Integer.class));
	    verify(mockJdbcTemplate).update(eq(SQL_INSERT_CATEGORY), any(Integer.class), eq(category.getPrct_title()), eq(category.getPrct_desc()));
	    assertTrue(result);
	}

	
	@Test
	public void testGetAllCategories() {
		// TODO: Mock the necessary objects and define the expected behavior

		// Example test implementation
		List<Category> expectedCategories = createMockCategories();

		// Mock the behavior of the jdbcTemplate
		when(mockJdbcTemplate.query(eq(SQL_GET_CATEGORIES), any(RowMapper.class)))
				.thenReturn(expectedCategories);

		// Execute the getAllCategories method
		List<Category> actualCategories = categoryDAO.getAllCategories();

		// Verify the expected behavior
		verify(mockJdbcTemplate).query(eq(SQL_GET_CATEGORIES), any(RowMapper.class));
		assertEquals(actualCategories.size(), expectedCategories.size());
		// Additional assertions for category properties if necessary
	}
	
	// Helper method to create mock categories for testing
	private List<Category> createMockCategories() {
		// TODO: Create and return a list of mock categories
		
		List<Category> categories = new ArrayList<>();
		Category category1 = new Category();
//		category1.setPrct_id(1);
//		category1.setPrct_title("Category 1");
//		category1.setPrct_desc("Description 1");
		categories.add(category1);
		
		Category category2 = new Category();
//		category2.setPrct_id(2);
//		category2.setPrct_title("Category 2");
//		category2.setPrct_desc("Description 2");
		categories.add(category2);
		
		Category category3 = new Category();
//		category3.setPrct_id(3);
//		category3.setPrct_title("Category 1");
//		category3.setPrct_desc("Description 1");
		categories.add(category3);
		
		Category category4 = new Category();
//		category4.setPrct_id(4);
//		category4.setPrct_title("Category 2");
//		category4.setPrct_desc("Description 2");
		categories.add(category4);
		
		Category category5 = new Category();
//		category5.setPrct_id(5);
//		category5.setPrct_title("Category 1");
//		category5.setPrct_desc("Description 1");
		categories.add(category5);
		
		Category category6 = new Category();
//		category6.setPrct_id(6);
//		category6.setPrct_title("Category 2");
//		category6.setPrct_desc("Description 2");
		categories.add(category6);
		
		Category category7 = new Category();
//		category7.setPrct_id(7);
//		category7.setPrct_title("Category 2");
//		category7.setPrct_desc("Description 2");
		categories.add(category7);
		
		Category category8 = new Category();
//		category8.setPrct_id(8);
//		category8.setPrct_title("Category 2");
//		category8.setPrct_desc("Description 2");
		categories.add(category8);
		
		Category category9 = new Category();
//		category9.setPrct_id(9);
//		category9.setPrct_title("Category 2");
//		category9.setPrct_desc("Description 2");
		categories.add(category9);
		
		return categories;
	}
}

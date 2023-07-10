package eStoreProduct.TestClasses.admin.controller;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.admin.CategoryDAO;
import eStoreProduct.DAO.common.ProductDAO;
import eStoreProduct.controller.admin.adminMasterEntryController;
import eStoreProduct.DAO.admin.stockSummaryDAO;
import eStoreProduct.model.admin.input.Category;
import eStoreProduct.model.admin.input.Product;
import eStoreProduct.model.admin.output.stockSummaryModel;

public class AdminMasterEntryControllerTest {

    @Mock
    private stockSummaryDAO stockSummaryDao;

    @Mock
    private ProductDAO productDao;

    @Mock
    private CategoryDAO categoryDao;

    @InjectMocks
    private adminMasterEntryController controller;

    @Mock
    private Model model;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new adminMasterEntryController(stockSummaryDao, productDao, categoryDao);
    }

    @Test
    public void testShowEditableStocks() {
        List<stockSummaryModel> stocks1 = new ArrayList<>();
        when(stockSummaryDao.getStocks()).thenReturn(stocks1);

        String result = controller.showEditableStocks(model);

        verify(stockSummaryDao).getStocks();
        verify(model).addAttribute(eq("stocks1"), eq(stocks1));
        Assert.assertEquals(result, "editableStocks");
    }

    @Test
    public void testShowEditableProductsPrice() {
        List<stockSummaryModel> stocks1 = new ArrayList<>();
        when(stockSummaryDao.getStocks()).thenReturn(stocks1);

        String result = controller.showEditableProductsPrice(model);

        verify(stockSummaryDao).getStocks();
        verify(model).addAttribute(eq("stocks1"), eq(stocks1));
        Assert.assertEquals(result, "editablePrice");
    }

    @Test
    public void testShowUpdatedPrices() {
        stockSummaryModel ssm = new stockSummaryModel();

        String result = controller.showUpdatedPrices(ssm, model);

        verify(stockSummaryDao).updatePrice(eq(ssm.getId()), eq(ssm.getMrp()), eq(ssm.getPrice()));
        verify(stockSummaryDao).getStocks();
        verify(model).addAttribute(eq("stocks1"), anyList());
        Assert.assertEquals(result, "editablePrice");
    }

    @Test
    public void testShowUpdatedEditableStocks() {
        stockSummaryModel ssm = new stockSummaryModel();

        String result = controller.showUpdatedEditableStocks(ssm, model);

        verify(stockSummaryDao).updateStocks(eq(ssm.getId()), eq(ssm.getImageUrl()), eq(ssm.getDescription()),
                eq(ssm.getReorderLevel()), eq(ssm.getStock()), eq(ssm.getMrp()), eq(ssm.getPrice()));
        verify(stockSummaryDao).getStocks();
        verify(model).addAttribute(eq("stocks1"), anyList());
        Assert.assertEquals(result, "editableStocks");
    }

    @Test
    public void testAddNewProductInMasterEntry() {
        String result = controller.addNewProductInMasterEntry(model);

        Assert.assertEquals(result, "addNewProduct");
    }

    @Test
    public void testCreateProductNew() {
        Product prod = new Product();

        String result = controller.createProductNew(prod, model);

        verify(productDao).createProduct(eq(prod));
        Assert.assertEquals(result, "AddedProduct");
    }

    @Test
    public void testAddNewCategorytInMasterEntry() {
        String result = controller.addNewCategorytInMasterEntry(model);

        Assert.assertEquals(result, "addNewCategoryForm");
    }

    @Test
    public void testCreateNewCategory() {
        Category catg = new Category();
        BindingResult bindingResult = mock(BindingResult.class);

        String result = controller.createCategoryNew(catg, model);

        verify(categoryDao).addNewCategory(eq(catg));
        Assert.assertEquals(result, "AddedCategory");
    }

}

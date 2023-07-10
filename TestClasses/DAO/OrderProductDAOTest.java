package eStoreProduct.TestClasses.DAO;

import eStoreProduct.DAO.admin.orderProductDAOImp;
import eStoreProduct.model.admin.input.orderProductInput;
import eStoreProduct.model.admin.output.orderProductsModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class OrderProductDAOTest {
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private orderProductDAOImp orderProductDAO;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetOrderWiseOrderProducts_WithExistingOrderId_ReturnsOrderProductsList() {
        // Mock the query result
        int orderId = 123;
        List<orderProductsModel> expectedOrderProducts = new ArrayList<>();
        orderProductsModel orderProduct1 = new orderProductsModel();
        orderProductsModel orderProduct2 = new orderProductsModel();
        expectedOrderProducts.add(orderProduct1);
        expectedOrderProducts.add(orderProduct2);
        TypedQuery<orderProductsModel> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(orderProductsModel.class))).thenReturn(query);
        when(query.setParameter(eq("orderid"), eq(orderId))).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedOrderProducts);

        // Call the method under test
        List<orderProductsModel> actualOrderProducts = orderProductDAO.getOrderWiseOrderProducts(orderId);

        // Verify the interactions and assertions
        verify(entityManager).createQuery(anyString(), eq(orderProductsModel.class));
        verify(query).setParameter(eq("orderid"), eq(orderId));
        verify(query).getResultList();
        Assert.assertEquals(actualOrderProducts, expectedOrderProducts);
    }

    @Test
    public void testGetOrderWiseOrderProducts_WithException_ReturnsEmptyList() {
        // Mock the exception
        int orderId = 123;
        TypedQuery<orderProductsModel> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(orderProductsModel.class))).thenReturn(query);
        when(query.setParameter(eq("orderid"), eq(orderId))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Database connection failed"));

        // Call the method under test
        List<orderProductsModel> actualOrderProducts = orderProductDAO.getOrderWiseOrderProducts(orderId);

        // Verify the interactions and assertions
        verify(entityManager).createQuery(anyString(), eq(orderProductsModel.class));
        verify(query).setParameter(eq("orderid"), eq(orderId));
        verify(query).getResultList();
        Assert.assertTrue(actualOrderProducts.isEmpty());
    }

    @Test
    public void testUpdateOrderProductStatus_WithValidInput_ReturnsAffectedRowsCount() {
        // Mock the query result
        orderProductInput input = new orderProductInput();
        input.setShipment_status("Shipped");
        input.setOrdr_id(123);
        input.setProd_id(456);
        int expectedAffectedRows = 1;
        TypedQuery<Integer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(query);
        when(query.setParameter(eq("status"), eq("Shipped"))).thenReturn(query);
        when(query.setParameter(eq("oid"), eq(123))).thenReturn(query);
        when(query.setParameter(eq("pid"), eq(456))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(expectedAffectedRows);

        // Call the method under test
        int actualAffectedRows = orderProductDAO.updateOrderProductStatus(input);

        // Verify the interactions and assertions
        verify(entityManager).createQuery(anyString(), eq(Integer.class));
        verify(query).setParameter(eq("status"), eq("Shipped"));
        verify(query).setParameter(eq("oid"), eq(123));
        verify(query).setParameter(eq("pid"), eq(456));
        verify(query).executeUpdate();
        Assert.assertEquals(actualAffectedRows, expectedAffectedRows);
    }

    @Test
    public void testUpdateOrderProductStatus_WithException_ReturnsZeroAffectedRows() {
        // Mock the exception
        orderProductInput input = new orderProductInput();
        input.setShipment_status("Shipped");
        input.setOrdr_id(123);
        input.setProd_id(456);
        TypedQuery<Integer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(query);
        when(query.setParameter(eq("status"), eq("Shipped"))).thenReturn(query);
        when(query.setParameter(eq("oid"), eq(123))).thenReturn(query);
        when(query.setParameter(eq("pid"), eq(456))).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Database connection failed"));

        // Call the method under test
        int actualAffectedRows = orderProductDAO.updateOrderProductStatus(input);

        // Verify the interactions and assertions
        verify(entityManager).createQuery(anyString(), eq(Integer.class));
        verify(query).setParameter(eq("status"), eq("Shipped"));
        verify(query).setParameter(eq("oid"), eq(123));
        verify(query).setParameter(eq("pid"), eq(456));
        verify(query).executeUpdate();
        Assert.assertEquals(actualAffectedRows, 0);
    }
}

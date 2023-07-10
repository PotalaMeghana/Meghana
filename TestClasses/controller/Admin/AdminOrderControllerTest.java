package eStoreProduct.TestClasses.admin.controller;

import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.common.OrderDAO;
import eStoreProduct.controller.admin.adminOrderController;
import eStoreProduct.model.admin.entities.orderModel;

public class AdminOrderControllerTest {

    @Mock
    private OrderDAO orderDao;

    @Mock
    private orderModel orderModel;
    
    @InjectMocks
    private adminOrderController controller;

    @Mock
    private Model model;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new adminOrderController(orderDao, orderModel);
    }

    @Test
    public void testShowOrders() {
        List<orderModel> orders = new ArrayList<>();
        when(orderDao.getOrders(anyInt(), anyInt())).thenReturn(orders);
        when(orderDao.getTotalOrders()).thenReturn(10L);

        String result = controller.showOrders(model, 0);

        verify(orderDao).getOrders(0, 5);
        verify(orderDao).getTotalOrders();
        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute("token", "All");
        verify(model).addAttribute("page", 0);
        verify(model).addAttribute("totalPages", 2);
        Assert.assertEquals(result, "orderList");
    }

    @Test
    public void testShowOrdersForPagination() {
        List<orderModel> orders = new ArrayList<>();
        int page = 1;
        int pageSize = 5;
        int totalPages = 2;
        String token = "ProcessedPaginationToken";
        when(orderDao.getProcessedOnlyOrders(page, pageSize)).thenReturn(orders);
        when(orderDao.getTotalProcessedRecords()).thenReturn(10L);

        String result = controller.showOrdersForPagination(model, page, token);

        verify(orderDao).getProcessedOnlyOrders(page, pageSize);
        verify(orderDao).getTotalProcessedRecords();
        verify(model).addAttribute(eq("orders"), eq(orders));
        verify(model).addAttribute(eq("token"), eq("ProcessedPaginationToken"));
        verify(model).addAttribute(eq("page"), eq(page));
        verify(model).addAttribute(eq("totalPages"), eq(totalPages));
        Assert.assertEquals(result, "orderList");
    }

   /*
    @Test
    public void testLoadOrders() {
        List<orderModel> orders = new ArrayList<>();
        int page = 0;
        int pageSize = 5;
        String selectDateFilter = "daily";
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = currentDate.withHour(23).withMinute(59).withSecond(59);
        when(orderDao.loadOrdersByDate(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate))).thenReturn(orders);

        String result = controller.loadOrders(selectDateFilter, page, model);

        verify(orderDao).loadOrdersByDate(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        verify(model).addAttribute(eq("orders"), eq(orders));
        Assert.assertEquals(result, "filteredOrderList");
    }

*/
    @Test
    public void testUnprocessedOrders() {
        List<orderModel> orders = new ArrayList<>();
        int page = 0;
        int pageSize = 5;
        int totalPages = 2;
        when(orderDao.getUnprocessedOrders(page, pageSize)).thenReturn(orders);
        when(orderDao.getTotalUnprocessedOrders()).thenReturn(8L);

        String result = controller.unprocessedOrders(model, page);

        verify(orderDao).getUnprocessedOrders(page, pageSize);
        verify(orderDao).getTotalUnprocessedOrders();
        verify(model).addAttribute(eq("orders"), eq(orders));
        verify(model).addAttribute(eq("token"), eq("UnProcessedPaginationToken"));
        verify(model).addAttribute(eq("page"), eq(page));
        verify(model).addAttribute(eq("totalPages"), eq(totalPages));
        Assert.assertEquals(result, "orderList");
    }

    @Test
    public void testProcessedOnlyOrders() {
        List<orderModel> orders = new ArrayList<>();
        int page = 0;
        int pageSize = 5;
        int totalPages = 2;
        when(orderDao.getProcessedOnlyOrders(page, pageSize)).thenReturn(orders);
        when(orderDao.getTotalProcessedRecords()).thenReturn(10L);

        String result = controller.processedOnlyOrders(model, page);

        verify(orderDao).getProcessedOnlyOrders(page, pageSize);
        verify(orderDao).getTotalProcessedRecords();
        verify(model).addAttribute(eq("orders"), eq(orders));
        verify(model).addAttribute(eq("page"), eq(page));
        verify(model).addAttribute(eq("token"), eq("ProcessedPaginationToken"));
        verify(model).addAttribute(eq("totalPages"), eq(totalPages));
        Assert.assertEquals(result, "orderList");
    }

    @Test
    public void testProcessOrders() {
        long orderId = 1;
        int adminId = 1;
        int page = 0;
        int pageSize = 5;
        String token = "ProcessedPaginationToken";
        List<orderModel> orders = new ArrayList<>();
        when(orderDao.getProcessedOnlyOrders(page, pageSize)).thenReturn(orders);
        when(orderDao.getTotalProcessedRecords()).thenReturn(10L);

        String result = controller.processOrders(orderId, adminId, token, page, model);

        verify(orderDao).updateOrderProcessedBy(orderId, adminId);
        verify(orderDao).getProcessedOnlyOrders(page, pageSize);
        verify(orderDao).getTotalProcessedRecords();
        verify(model).addAttribute(eq("orders"), eq(orders));
        verify(model).addAttribute(eq("page"), eq(page));
        verify(model).addAttribute(eq("token"), eq("ProcessedPaginationToken"));
        verify(model).addAttribute(eq("totalPages"), anyInt());
        Assert.assertEquals(result, "orderList");
    }

}

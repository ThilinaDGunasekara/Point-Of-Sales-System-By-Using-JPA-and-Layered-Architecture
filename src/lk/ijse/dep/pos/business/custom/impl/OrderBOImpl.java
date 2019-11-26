package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.OrderBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOTypes;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.dao.custom.OrderDAO;
import lk.ijse.dep.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.db.JPAUtil;
import lk.ijse.dep.pos.dto.OrderDTO;
import lk.ijse.dep.pos.dto.OrderDTO2;
import lk.ijse.dep.pos.dto.OrderDetailDTO;
import lk.ijse.dep.pos.entity.*;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderBOImpl implements OrderBO {

    private OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER);
    private OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER_DETAIL);
    private ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOTypes.ITEM);
    private QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOTypes.QUERY);

    @Override
    public int getLastOrderId() throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        orderDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
        int lastOrderId = orderDAO.getLastOrderId();
        entityManager.getTransaction().commit();
        entityManager.close();
        return lastOrderId;
    }

    @Override
    public void placeOrder(OrderDTO order) throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        orderDAO.setEntitymanager(entityManager);
        orderDetailDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();

        int oId = order.getId();
        orderDAO.save(new Order(oId, new java.sql.Date(new Date().getTime()),entityManager.getReference(Customer.class,order.getCustomerId())));


        for (OrderDetailDTO orderDetail : order.getOrderDetails()) {
            orderDetailDAO.save(new OrderDetail(oId, orderDetail.getCode(),
                    orderDetail.getQty(), orderDetail.getUnitPrice()));


            Item item = itemDAO.find(orderDetail.getCode());
            item.setQtyOnHand(item.getQtyOnHand() - orderDetail.getQty());
            itemDAO.update(item);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

        @Override
        public List<OrderDTO2> getOrderInfo (String query) throws Exception {
            EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
            queryDAO.setEntitymanager(entityManager);
            entityManager.getTransaction().begin();

            List<CustomEntity> ordersInfo = queryDAO.getOrdersInfo(query+"%");
            entityManager.getTransaction().commit();
            entityManager.close();

            List<OrderDTO2> al = new ArrayList<>();

            for (CustomEntity customEntity : ordersInfo) {
                al.add(new OrderDTO2(customEntity.getOrderId(), customEntity.getOrderDate(), customEntity.getCustomerId(), customEntity.getCustomerName(), customEntity.getOrderTotal()));
            }

            return al;
        }
    }

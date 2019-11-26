package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.ItemBO;
import lk.ijse.dep.pos.business.exception.AlreadyExistsInOrderException;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOTypes;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep.pos.db.JPAUtil;
import lk.ijse.dep.pos.dto.ItemDTO;
import lk.ijse.dep.pos.entity.Item;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {

    private OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER_DETAIL);
    private ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOTypes.ITEM);

    @Override
    public void saveItem(ItemDTO item) throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
         itemDAO.save(new Item(item.getCode(),
                item.getDescription(), item.getUnitPrice(), item.getQtyOnHand()));
         entityManager.getTransaction().commit();
         entityManager.close();
    }

    @Override
    public void updateItem(ItemDTO item) throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
         itemDAO.save(new Item(item.getCode(),
                item.getDescription(), item.getUnitPrice(), item.getQtyOnHand()));
         entityManager.getTransaction().commit();
         entityManager.close();
    }

    @Override
    public void deleteItem(String itemCode) throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        orderDetailDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
        if (orderDetailDAO.existsByItemCode(itemCode)){
            throw new AlreadyExistsInOrderException("Item already exists in an order, hence unable to delete");
        }
         itemDAO.delete(itemCode);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<ItemDTO> findAllItems() throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
        List<Item> allItems = itemDAO.findAll();
        entityManager.getTransaction().commit();
        entityManager.close();
        List<ItemDTO> dtos = new ArrayList<>();
        for (Item item : allItems) {
            dtos.add(new ItemDTO(item.getCode(),
                    item.getDescription(),
                    item.getQtyOnHand(),
                    item.getUnitPrice()));
        }
        return dtos;
    }

    @Override
    public String getLastItemCode() throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
        String lastItemCode = itemDAO.getLastItemCode();
        entityManager.getTransaction().commit();
        entityManager.close();
        return lastItemCode;
    }

    @Override
    public ItemDTO findItem(String itemCode) throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
        Item item = itemDAO.find(itemCode);
        entityManager.getTransaction().commit();
        entityManager.close();
        return new ItemDTO(item.getCode(),
                item.getDescription(),
                item.getQtyOnHand(),
                item.getUnitPrice());
    }

    @Override
    public List<String> getAllItemCodes() throws Exception {
        EntityManager entityManager = JPAUtil.getEntityManager().createEntityManager();
        itemDAO.setEntitymanager(entityManager);
        entityManager.getTransaction().begin();
        List<Item> allItems = itemDAO.findAll();
        entityManager.getTransaction().commit();
        entityManager.close();
        List<String> codes = new ArrayList<>();
        for (Item item : allItems) {
            codes.add(item.getCode());
        }
        return codes;
    }
}

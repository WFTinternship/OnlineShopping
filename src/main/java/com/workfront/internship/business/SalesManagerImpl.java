package com.workfront.internship.business;

import com.workfront.internship.common.Basket;
import com.workfront.internship.common.Sale;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.LegacyDataSource;
import com.workfront.internship.dao.SaleDao;
import com.workfront.internship.dao.SaleDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Workfront on 7/25/2016.
 */
@Component
public class SalesManagerImpl implements SalesManager {

    @Autowired
    private LegacyDataSource dataSource;
    @Autowired
    private SaleDao saleDao;
    @Autowired
    BasketManager basketManager;



    public SalesManagerImpl(LegacyDataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        saleDao = new SaleDaoImpl(dataSource);
        basketManager = new BasketManagerImpl(dataSource);

    }
    @Override
    public List<Sale> getSales(User user) {
        if(user == null)
            throw new RuntimeException("invalid user");
        List<Sale> records = saleDao.getSales(user.getUserID());
        user.setRecords(records);
        return records;
    }

    @Override
    public int makeNewSale(Sale sale) {
        if(!validateSale(sale))
            throw new RuntimeException("invalid sale");
        int result = saleDao.insertSale(sale);
        return  result;
    }
    @Override
    public Sale getSaleBySaleID(int id){
        if(id <= 0)
            throw new RuntimeException("invalid id");
        Sale sale = saleDao.getSaleBySaleID(id);
        return sale;
    }
    @Override
    public Sale getSalesDetailedInfo(Sale sale){
        if(!validateSale(sale))
            throw new RuntimeException("invalid sale");
        Basket basket = basketManager.getBasket(sale.getBasket().getBasketID());
        sale.setBasket(basket);
        return sale;

    }
    @Override
    public void deleteSaleByUserID(int userId){
        if(userId <= 0)
            throw new RuntimeException("invalid id");
        saleDao.deletSaleByUserID(userId);
    }
    @Override
    public void deleteSaleBySaleID(int saleId){
        if(saleId <= 0)
            throw new RuntimeException("invalid id");
        saleDao.deleteSaleBySaleID(saleId);
    }

    @Override
    public List<Sale> getAllSales(){
        List<Sale> sales = saleDao.getAllSales();
        return sales;
    }
    private boolean validateSale(Sale sale){
        if(sale != null && sale.getAddressID() >0 && sale.getCreditCardID() >0 && sale.getDate() !=null &&
                sale.getUserID() > 0 && sale.getBasket() != null)
            return true;
        return false;
    }
}


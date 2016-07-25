package com.workfront.internship.business;

import com.workfront.internship.common.Sale;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.DataSource;
import com.workfront.internship.dao.SaleDao;
import com.workfront.internship.dao.SaleDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Workfront on 7/25/2016.
 */
public class SalesManagerImpl implements SalesManager {

    private DataSource dataSource;
    private SaleDao saleDao;



    public SalesManagerImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
        saleDao = new SaleDaoImpl(dataSource);

    }
    @Override
    public List<Sale> getSales(User user) {
        List<Sale> records = saleDao.getSales(user.getUserID());
        user.setRecords(records);
        return records;
    }

    @Override
    public void makeNewSale(Sale sale) {
        saleDao.insertSale(sale);
    }
    @Override
    public Sale getSaleBySaleID(int id){
        Sale sale = saleDao.getSaleBySaleID(id);
        return sale;
    }
    @Override
    public void deleteSaleByUserID(int userId){
        saleDao.deletSaleByUserID(userId);
    }
    @Override
    public void deleteSaleBySaleID(int saleId){
        saleDao.deleteSaleBySaleID(saleId);
    }

    @Override
    public List<Sale> getAllSales(){
        List<Sale> sales = saleDao.getAllSales();
        return sales;
    }
    @Override
    public void deleteAllSales(){
        saleDao.deleteAllSales();
    }
}

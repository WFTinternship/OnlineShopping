package com.workfront.internship.business;

import com.workfront.internship.common.Sale;
import com.workfront.internship.common.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 23.07.2016.
 */
public interface SalesManager {

    int makeNewSale(Sale sale);
    List<Sale> getSales(User user);
    Sale getSaleBySaleID(int id);
    Sale getSalesDetailedInfo(Sale sale);
    void deleteSaleByUserID(int userId);
    void deleteSaleBySaleID(int saleId);
  //void updateSale(Sale sale);
    List<Sale> getAllSales();


}

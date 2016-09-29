package com.workfront.internship.dao;

import com.workfront.internship.common.Sale;

import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface SaleDao {

    List<Sale> getSales(int userId);

    Sale getSaleBySaleID(int id);

    void deletSaleByUserID(int userId);

    void deleteSaleBySaleID(int saleId);

    int insertSale(Sale sale);

    void updateSaleStatus(int id, String status);

    List<Sale> getAllSales();

    void deleteAllSales();

}

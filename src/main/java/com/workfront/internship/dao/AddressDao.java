package com.workfront.internship.dao;

import com.workfront.internship.common.Address;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface AddressDao {
    List<Address> getShippingAddressByUserID(int ID);
    void deleteAddressesByUserID(int userId);
    void deleteAddressesByAddressID(int addressId);
    void deleteAddressesByAddressID(Connection connection, int addressId);
    int insertAddress(Address address);
    int insertAddress(Connection connection, Address address);
    void updateAddress(Address address);
    Address getAddressByID(int id);
    void deleteAllAddresses();
    List<Address> getAllAddresses();
}

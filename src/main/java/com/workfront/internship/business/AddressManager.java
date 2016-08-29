package com.workfront.internship.business;

import com.workfront.internship.common.Address;

import java.util.List;

/**
 * Created by Anna Asmangulyan on 8/29/2016.
 */
public interface AddressManager {
    List<Address> getShippingAddressByUserID(int ID);
    void deleteAddressesByUserID(int userId);
  //  void deleteAddressesByAddressID(int addressId);
    int insertAddress(Address address);
   void updateAddress(Address address);
   // Address getAddressByID(int id);
    void deleteAllAddresses();
   // List<Address> getAllAddresses();
}

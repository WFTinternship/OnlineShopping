package com.workfront.internship.business;

import com.workfront.internship.common.Address;
import com.workfront.internship.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Anna Asmangulyan on 8/29/2016.
 */
@Component
public class AddressManagerImpl implements AddressManager{
    @Autowired
    private AddressDao addressDao;
    @Override
    public List<Address> getShippingAddressByUserID(int ID){
        if(ID<=0)
            throw new RuntimeException("not a valid ID");
        return addressDao.getShippingAddressByUserID(ID);
    }
    @Override
    public void deleteAddressesByUserID(int userId){
        if(userId<=0)
            throw new RuntimeException("not a valid ID");
        addressDao.deleteAddressesByUserID(userId);

    }
    //  void deleteAddressesByAddressID(int addressId);
    @Override
    public int insertAddress(Address address){
        if(address==null)
            throw new RuntimeException("not a valid address");
        return addressDao.insertAddress(address);

    }
    //void updateAddress(Address address);
    // Address getAddressByID(int id);
    @Override
    public void deleteAllAddresses(){
        addressDao.deleteAllAddresses();

    }
    @Override
    public void updateAddress(Address address){
        if(address==null)
            throw new RuntimeException("not a valid address");
        addressDao.updateAddress(address);
    }
    // List<Address> getAllAddresses();
}

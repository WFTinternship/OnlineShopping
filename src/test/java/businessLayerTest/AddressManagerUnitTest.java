package businessLayerTest;

import com.workfront.internship.business.AddressManager;
import com.workfront.internship.business.AddressManagerImpl;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.MediaManagerImpl;
import com.workfront.internship.common.*;
import com.workfront.internship.dao.AddressDao;
import com.workfront.internship.dao.AddressDaoImpl;
import com.workfront.internship.dao.MediaDao;
import com.workfront.internship.dao.MediaDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Anna Asmangulyan on 8/29/2016.
 */
public class AddressManagerUnitTest {
    private User user;
    private Address address;
    private AddressManager addressManager;

    AddressDao addressDao;

    @Before
    public void setUP() throws IOException, SQLException {
        user = getTestUser();
        address = getTestAddress();
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        user.setShippingAddresses(addresses);
        addressManager = new AddressManagerImpl();

        addressDao = Mockito.mock(AddressDaoImpl.class);
        Whitebox.setInternalState(addressManager, "addressDao", addressDao);

    }
    @After
    public void tearDown() {
        user = null;
        address = null;
        addressManager = null;
        addressDao = null;

    }
    @Test
    public void getShippingAddressByUserID(){

        when(addressDao.getShippingAddressByUserID(user.getUserID())).thenReturn(user.getShippingAddresses());


        List<Address> addresses1 =addressManager.getShippingAddressByUserID(user.getUserID());

       assertEquals(addresses1.get(0), user.getShippingAddresses().get(0));

    }
    @Test(expected = RuntimeException.class)
    public void getMediaByProductID_invalidId(){
        addressManager.getShippingAddressByUserID(-1);
    }


    @Test
    public void insertAddress(){
        when(addressDao.insertAddress(address)).thenReturn(address.getAddressID());
        int result = addressManager.insertAddress(address);

        assertEquals("insertion failed!", result, address.getAddressID());
    }
    @Test(expected = RuntimeException.class)
    public void insertMedia_invalid_address(){
        addressManager.insertAddress(null);
    }
    @Test
    public void updateAddress(){

        addressManager.updateAddress(address);

        Mockito.verify(addressDao).updateAddress(address);
    }
    @Test(expected = RuntimeException.class)
    public void updateAddress_invalid_address(){
        addressManager.updateAddress(null);
    }

    @Test
    public void deleteAllAddresses(){

        addressManager.deleteAllAddresses();

        Mockito.verify(addressDao).deleteAllAddresses();

    }
    @Test(expected = RuntimeException.class)
    public void deleteAddressesByUserID_invalidId(){
        addressManager.deleteAddressesByUserID(-1);
    }
    @Test
    public void deleteAddressByUserID(){

        addressManager.deleteAddressesByUserID(user.getUserID());

        Mockito.verify(addressDao).deleteAddressesByUserID(user.getUserID());

    }


    private User getTestUser() {
        user = new User();
        user.setFirstname("Anahit").
                setLastname("galstyan").
                setUsername("anigal").
                setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").
                setConfirmationStatus(true).
                setAccessPrivilege("user").setUserID(1);

        return user;
    }
    private Address getTestAddress(){
        address = new Address();
        address.setUserID(user.getUserID()).setAddress("someaddress").setAddressID(2);
        return address;
    }
    private void doAssertion(Media media, Media media1){

        assertEquals(media.getMediaID(), media1.getMediaID());
        assertEquals(media.getMediaPath(), media1.getMediaPath());
        assertEquals(media.getProductID(), media1.getProductID());

    }
}

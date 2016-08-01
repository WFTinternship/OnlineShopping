package businessLayerTest;

import com.workfront.internship.business.EmailManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by Workfront on 7/29/2016.
 */
public class UserManagerImplTest {
    private User user;
    private UserManager userManager;
    DataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        user = getRandomUser();
        userManager = new UserManagerImpl(dataSource);

    }


    @After
    public void tearDown()  {
        //TODO implementation of deleteAllUsers in UserManager
        // userManager.deleteAllUsers();

    }

    @Test
    public void createAccount(){
        EmailManager emailManager = Mockito.mock(EmailManager.class);
        Whitebox.setInternalState(userManager, "emailManager", emailManager);
        when(emailManager.sendVerificationEmail(user)).thenReturn(true);
        String password = user.getPassword();
        userManager.createAccount(user);

        User actualUser = userManager.login(user.getUsername(), password);
        assertNotNull(actualUser);
    }

    private User getRandomUser() {
        User user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").
                setUsername("anigal").setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").setConfirmationStatus(true).
                setAccessPrivilege("user");
        Address address = new Address();
        address.setAddress("someAddress");
        List<Address> addressess = new ArrayList<>();
        addressess.add(address);
        user.setShippingAddresses(addressess);
        return user;
    }
}

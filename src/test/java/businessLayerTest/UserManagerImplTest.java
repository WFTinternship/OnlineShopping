package businessLayerTest;

import com.workfront.internship.business.EmailManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.LegacyDataSource;
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


public class UserManagerImplTest {
    private User user;
    private UserManager userManager;
    LegacyDataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = LegacyDataSource.getInstance();
        user = getTestUser();
        userManager = new UserManagerImpl();
        Whitebox.setInternalState(userManager, "dataSource", dataSource);
    }

    @After
    public void tearDown()  {
        userManager.deleteAccount(user.getUserID());
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
    private User getTestUser() {
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

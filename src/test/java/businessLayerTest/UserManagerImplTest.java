package businessLayerTest;

import com.workfront.internship.business.EmailManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;

import com.workfront.internship.dao.UserDao;
import com.workfront.internship.dao.UserDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


public class UserManagerImplTest {
    private User user;
    private UserManager userManager;
    private LegacyDataSource dataSource;
    private UserDao userDao;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = LegacyDataSource.getInstance();
        user = getTestUser();
        userManager = new UserManagerImpl();
        userDao = new UserDaoImpl();
        Whitebox.setInternalState(userDao, "dataSource", dataSource);
        Whitebox.setInternalState(userManager, "userDao", userDao);

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

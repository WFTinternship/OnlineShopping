package businessLayerTest;

import com.workfront.internship.business.EmailManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.common.Address;
import com.workfront.internship.common.User;

import com.workfront.internship.spring.TestConfiguration;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class UserManagerImplTest {

    private User user;

    @Autowired
    private UserManager userManager;

    @Before
    public void setUP() throws IOException, SQLException {

        user = getTestUser();
    }

    @After
    public void tearDown() {
        userManager.deleteAllUsers();
    }

    @Test
    public void createAccount() {
        EmailManager emailManager = Mockito.mock(EmailManager.class);
        Whitebox.setInternalState(userManager, "emailManager", emailManager);
        when(emailManager.sendVerificationEmail(user)).thenReturn(true);
        String password = user.getPassword();
        userManager.createAccount(user);

        User actualUser = userManager.login(user.getUsername(), password);
        assertNotNull("can not login", actualUser);
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

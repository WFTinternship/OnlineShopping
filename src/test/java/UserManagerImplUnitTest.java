import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImlp;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.DataSource;
import com.workfront.internship.dao.UserDao;
import com.workfront.internship.dao.UserDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Workfront on 7/27/2016.
 */
public class UserManagerImplUnitTest {
    private User user;
    private UserManager userManager;
    DataSource dataSource;

    @Before
    public void setUP() throws IOException, SQLException {
          dataSource = DataSource.getInstance();
          user = getRandomUser();
          userManager = new UserManagerImlp(dataSource);
    }


    @After
    public void tearDown(){

    }
    @Test
    public void createAccount_ValidUser_getHash() throws NoSuchAlgorithmException {

        String expectedPassword = userManager.getHash(user.getPassword());

        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        Whitebox.setInternalState(userManager, "userDao", userDao);

        userManager.createAccount(user);


        //  Mockito.verify(userDAO).addUser(testUser);
        String actualPassword = user.getPassword();
        assertEquals("Unable to hash password", actualPassword, expectedPassword);
    }
    @Test
    public void createAccount_ValidUser_validateUser() throws NoSuchAlgorithmException {

     //   String expectedPassword = userManager.getHash(user.getPassword());

        UserDao userDao = Mockito.mock(UserDaoImpl.class);
        user = Mockito.mock(User.class);
        Whitebox.setInternalState(userManager, "userDao", userDao);

        when(userManager.validateUser(user)).thenReturn(true);

        userManager.createAccount(user);


        Mockito.verify(userManager).validateUser(user);
       //  String actualPassword = user.getPassword();
       // assertEquals("Unable to hash password", actualPassword, expectedPassword);
    }



    private User getRandomUser() {
        User user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").setUsername("anigal").setPassword("anahitgal85").setEmail("galstyan@gmail.com").setConfirmationStatus(true).setAccessPrivilege("user");
        return user;
    }
}

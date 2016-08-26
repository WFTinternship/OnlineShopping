package businessLayerTest;

import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.dao.AddressDao;
import com.workfront.internship.dao.UserDao;
import com.workfront.internship.dao.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

import static org.mockito.Mockito.mock;


@Configuration
@ComponentScan(basePackages = "com.workfront")
@Profile("test")
public class ManagerTestConfig {

}

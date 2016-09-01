package controllerTest;

import com.workfront.internship.controller.HomePageController;
import com.workfront.internship.spring.TestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Anna Asmangulyan on 9/1/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class HomePageControllerIntegrationTest {
    @Autowired
    private HomePageController homePageController;

    private MockHttpServletRequest testRequest ;

}

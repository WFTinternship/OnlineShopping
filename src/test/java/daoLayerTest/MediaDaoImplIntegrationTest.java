package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;
import com.workfront.internship.spring.TestConfiguration;
import controllerTest.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class MediaDaoImplIntegrationTest {

    @Autowired
    MediaDao mediaDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryDao categoryDao;

    Media media;
    Product product;
    Category category;
    int lastInsertedIndex;

    @Before
    public void setUpDB() throws SQLException, IOException {

        category = TestHelper.getTestCategory();
        categoryDao.insertCategory(category);

        product = TestHelper.getTestProduct();
        product.setCategory(category);
        productDao.insertProduct(product);

        media = TestHelper.getTestMedia();
        media.setProductID(product.getProductID());
        lastInsertedIndex = mediaDao.insertMedia(media);
    }

    @After
    public void tearDown() {

        categoryDao.deleteAllCategories();
    }

    @Test
    public void deleteMediaByID() {

        mediaDao.deleteMediaByID(lastInsertedIndex);

        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);
        assertNull(media1);
    }

    @Test
    public void deleteMediaByProductID() {

        mediaDao.deleteMediaByProductID(media.getProductID());

        List<Media> medias = mediaDao.getMediaByProductID(media.getProductID());
        assertEquals(true, medias.isEmpty());
    }

    @Test
    public void deleteMediaByPath() {

        mediaDao.deleteMediaByPath(media.getMediaPath());
        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);
        assertNull(media1);
    }

    @Test
    public void insertMedia() {

        Media media2 = mediaDao.getMediaByMediaID(lastInsertedIndex);

        doAssertion(media2, media);

    }

    @Test
    public void getMediaByMediaID() {

        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);

        doAssertion(media, media1);

    }

    @Test
    public void getMediaByProductID() {

        //testing method...
        List<Media> medias1 = mediaDao.getMediaByProductID(media.getProductID());

        doAssertion(medias1.get(0), media);

    }


    @Test
    public void updateMedia() {

        media.setMediaPath("newMediaPath");

        mediaDao.updateMedia(media);

        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);

        doAssertion(media, media1);

    }


    @Test
    public void deleteAllMedias() {
        //testing method...
        mediaDao.deleteAllMedias();

        List<Media> medias = mediaDao.getAllMedias();

        assertEquals(true, medias.isEmpty());
    }

    @Test
    public void getAllMedias() {
        //testing method...
        List<Media> medias1 = mediaDao.getAllMedias();

        doAssertion(medias1.get(0), media);

    }


    private void doAssertion(Media media, Media media1) {

        assertEquals(media.getMediaID(), media1.getMediaID());
        assertEquals(media.getMediaPath(), media1.getMediaPath());
        assertEquals(media.getProductID(), media1.getProductID());

    }

}

package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;


public class TestMediaDaoImpl {
    LegacyDataSource dataSource;
    Media media = null;
    Product product = null;
    Category category = null;
    int lastInsertedIndex = 0;
    MediaDao mediaDao;
    ProductDao productDao;
    CategoryDao categoryDao;

    @Before
    public void setUpDB() throws SQLException, IOException{
        dataSource = LegacyDataSource.getInstance();
        mediaDao = new MediaDaoImpl();
        Whitebox.setInternalState(mediaDao, "dataSource", dataSource);
        productDao = new ProductDaoImpl();
        Whitebox.setInternalState(productDao, "dataSource", dataSource);
        categoryDao = new CategoryDaoImpl();
        Whitebox.setInternalState(categoryDao, "dataSource", dataSource);

        category = getRandomCategory();
        product = getRandomProduct();
        categoryDao.insertCategory(category);
        productDao.insertProduct(product);
        media = getRandomMedia();
        lastInsertedIndex = mediaDao.insertMedia(media);
    }

    @After
    public void tearDown() {

        mediaDao.deleteAllMedias();
        categoryDao.deleteAllCategories();
    }

    @Test
    public void deleteMediaByID(){

        mediaDao.deleteMediaByID(lastInsertedIndex);

        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);
        assertNull(media1);
    }
    @Test
    public void deleteMediaByProductID(){

        mediaDao.deleteMediaByProductID(media.getProductID());

        List<Media> medias = mediaDao.getMediaByProductID(media.getProductID());
        assertEquals(true, medias.isEmpty());
    }
    @Test
    public void deleteMediaByPath(){

        mediaDao.deleteMediaByPath(media.getMediaPath());
        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);
        assertNull(media1);
    }

    @Test
    public void insertMedia() {
        mediaDao.deleteAllMedias();
        Media media1 = getRandomMedia();
        int insertindex = mediaDao.insertMedia(media1);


        Media media2 = mediaDao.getMediaByMediaID(insertindex);

        doAssertion(media2, media1);

    }

    @Test
    public void getMediaByMediaID() {

        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);

        doAssertion(media, media1);

    }

    @Test
    public void getMediaByProductID(){
        mediaDao.deleteAllMedias();

        List<Media> medias = new ArrayList<>();


        Media media = getRandomMedia();
        mediaDao.insertMedia(media);
        medias.add(media);

        Media media1 = new Media();
        media1.setMediaPath("newMediaPath2").setProductID(media.getProductID());
        mediaDao.insertMedia(media1);
        medias.add(media1);

        List<Media> medias1 = mediaDao.getMediaByProductID(media.getProductID());

        for (int i = 0; i < medias1.size(); i++) {
            doAssertion(medias.get(i), medias1.get(i));
        }
    }


    @Test
    public void updateMedia() {


        media.setMediaPath("newMediaPath");

        mediaDao.updateMedia(media);

        Media media1 = mediaDao.getMediaByMediaID(lastInsertedIndex);

        doAssertion(media, media1);

    }


    @Test
    public void deleteAllMedias(){
        mediaDao.deleteAllMedias();

        List<Media> medias = mediaDao.getAllMedias();

        assertEquals(true, medias.isEmpty());
    }
    @Test
    public void getAllMedias(){

        mediaDao.deleteAllMedias();

        List<Media> medias = new ArrayList<>();


        Media media = getRandomMedia();
        mediaDao.insertMedia(media);
        medias.add(media);

        Media media1 = getRandomMedia();
        media1.setMediaPath("newMediaPath1");
        mediaDao.insertMedia(media1);
        medias.add(media1);

        List<Media> medias1 = mediaDao.getAllMedias();

        for (int i = 0; i < medias1.size(); i++) {
            doAssertion(medias.get(i), medias1.get(i));
        }
    }

    private Media getRandomMedia() {
        Media media = new Media();
        media.setMediaPath("randomPath").setProductID(product.getProductID());
        return media;
    }
    private Category getRandomCategory(){
        category = new Category();
        category.setName("randomCategory");
        return  category;
    }
    private Product getRandomProduct() {
        product = new Product();
        product.setName("randomName").setPrice(50).setDescription("blablabla").setShippingPrice(1).setQuantity(100).setCategory(category);
        return product;
    }
    private void doAssertion(Media media, Media media1){

        assertEquals(media.getMediaID(), media1.getMediaID());
        assertEquals(media.getMediaPath(), media1.getMediaPath());
        assertEquals(media.getProductID(), media1.getProductID());

    }

}

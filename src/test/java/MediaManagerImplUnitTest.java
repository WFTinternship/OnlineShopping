import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.MediaManagerImpl;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.DataSource;
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
 * Created by Administrator on 31.07.2016.
 */
public class MediaManagerImplUnitTest {
    private Media media;
    private Product product;
    private MediaManager mediaManager;
    DataSource dataSource;
    MediaDao mediaDao;

    @Before
    public void setUP() throws IOException, SQLException {
        product = getTestProduct();
        media = getTestMedia();
        mediaManager = new MediaManagerImpl(dataSource);
        mediaDao = Mockito.mock(MediaDaoImpl.class);
        Whitebox.setInternalState(mediaManager, "mediaDao", mediaDao);

    }
    @After
    public void tearDown() {

    }
    @Test
    public void getMediaBYProductID(){

        when(mediaDao.getMediaByProductID(product.getProductID())).thenReturn(product.getMedias());


        List<Media>medias1 =mediaManager.getMediaByProductID(product.getProductID());

        doAssertion(medias1.get(0), product.getMedias().get(0));
        doAssertion(medias1.get(1), product.getMedias().get(1));
    }
    @Test(expected = RuntimeException.class)
    public void getMediaByProductID_invalidId(){
        mediaManager.getMediaByProductID(-1);
    }

    @Test
    public void getMediaByID(){

        when(mediaDao.getMediaByMediaID(media.getMediaID())).thenReturn(media);


        Media media1 = mediaManager.getMediaByMediaID(media.getMediaID());

        doAssertion(media1,media);

    }
    @Test(expected = RuntimeException.class)
    public void getMediaByMediaID_invalidId(){
        mediaManager.getMediaByMediaID(-1);
    }
    @Test
    public void insertMedia(){
         when(mediaDao.insertMedia(media)).thenReturn(media.getMediaID());
         int result = mediaManager.insertMedia(media);

        assertEquals("insertion failed!", result, media.getMediaID());
    }
    @Test(expected = RuntimeException.class)
    public void insertMedia_invalid_media(){
        mediaManager.insertMedia(null);
    }
    @Test
    public void updateMedia(){

        mediaManager.updateMedia(media);

        Mockito.verify(mediaDao).updateMedia(media);
    }
    @Test(expected = RuntimeException.class)
    public void updateMedia_invalid_media(){
        mediaManager.updateMedia(null);
    }

    @Test
    public void deleteMediaByID(){

        mediaManager.deleteMediaByID(media.getMediaID());

        Mockito.verify(mediaDao).deleteMediaByID(media.getMediaID());

    }
    @Test(expected = RuntimeException.class)
    public void deleteMediaByID_invalidId(){
        mediaManager.deleteMediaByID(-1);
    }
    @Test
    public void deleteMediaByProductID(){

        mediaManager.deleteMediaByProductID(product.getProductID());

        Mockito.verify(mediaDao).deleteMediaByProductID(product.getProductID());

    }
    @Test(expected = RuntimeException.class)
    public void updateMedia_invalidId(){
        mediaManager.deleteMediaByProductID(-1);
    }

    private Product getTestProduct(){
        List<Media> medias = new ArrayList<>();
        Media media = getTestMedia();
        Media media1 = getTestMedia();
        medias.add(media);
        medias.add(media1);
        Category category = new Category();
        category.setCategoryID(1).setName("hat");
        Product product = new Product();
        product.setProductID(2).
                setName("baby hat").
                setPrice(50).
                setDescription("color:white").
                setShippingPrice(1).
                setQuantity(50).
                setCategory(category).
                setMedias(medias);
        return product;
    }
    private Media getTestMedia(){
        Random random = new Random();
        int x = random.nextInt(100000);
        Media media = new Media();
        media.setMediaID(x).setMediaPath("some path" + x).setProductID(2);
        return media;
    }
    private void doAssertion(Media media, Media media1){

        assertEquals(media.getMediaID(), media1.getMediaID());
        assertEquals(media.getMediaPath(), media1.getMediaPath());
        assertEquals(media.getProductID(), media1.getProductID());

    }
}

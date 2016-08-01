package businessLayerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Administrator on 30.07.2016.
 */
public class ProductManagerUnitTest {


    private Product product;
    private ProductManager productManager;
    DataSource dataSource;
    ProductDao productDao;
    MediaManager mediaManager;

    @Before
    public void setUP() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        product = getTestProduct();
        productManager = new ProductManagerImpl(dataSource);
        productDao = Mockito.mock(ProductDaoImpl.class);
        mediaManager = Mockito.mock(MediaManagerImpl.class);
        Whitebox.setInternalState(productManager, "productDao", productDao);
        Whitebox.setInternalState(productManager, "mediaManager", mediaManager);

    }

    @After

    public void tearDown() {

    }

    @Test
    public void createNewProduct_with_medias(){
        when(productDao.insertProduct(product)).thenReturn(20);

        int result = productManager.createNewProduct(product);

        verify(mediaManager, times(product.getMedias().size())).insertMedia(any(Media.class));
        assertEquals("insertion failed!", result, 20);

    }
    @Test
    public void createNewProduct_with_no_medias(){

        when(productDao.insertProduct(product)).thenReturn(20);
        List<Media> emptyMediaList = new ArrayList<>();
        product.setMedias(emptyMediaList);

        int result = productManager.createNewProduct(product);

        verify(mediaManager, never()).insertMedia(any(Media.class));
        assertEquals("insertion failed!", result, 20);

    }
    @Test(expected = RuntimeException.class)
    public void createNewProduct_invalid_entry(){
        productManager.createNewProduct(null);
    }
    @Test
    public void getProduct(){

        when(productDao.getProductByID(product.getProductID())).thenReturn(product);
        Product product1 = productManager.getProduct(product.getProductID());

        doAssertion(product, product1);

    }
    @Test
    public void updateProduct_add_media(){
        Product newProduct = getTestProduct();
        Media media = new Media();
        media.setProductID(newProduct.getProductID()).setMediaPath("newPath").setMediaID(3);
        newProduct.getMedias().add(media);
        System.out.println(product.getMedias().size() + "    " + newProduct.getMedias().size());
        when(mediaManager.getMediaByProductID(newProduct.getProductID())).thenReturn(product.getMedias());

        productManager.updateProduct(newProduct);

        verify(mediaManager).insertMedia(newProduct.getMedias().get(1));
        verify(mediaManager, never()).deleteMediaByID(any(Integer.class));


    }

    @Test
    public void updateProduct_remove_media(){
        Product newProduct = getTestProduct();
        List<Media> medias = new ArrayList<>();

        newProduct.setMedias(medias);
        System.out.println(product.getMedias().size() + "    " + newProduct.getMedias().size());
        when(mediaManager.getMediaByProductID(newProduct.getProductID())).thenReturn(product.getMedias());

        productManager.updateProduct(newProduct);

        verify(mediaManager, never()).insertMedia(any(Media.class));
        verify(mediaManager).deleteMediaByID(product.getMedias().get(0).getMediaID());


    }
    @Test(expected = RuntimeException.class)
    public void updateProduct(){
        productManager.updateProduct(null);
    }
    @Test
    public void deleteProduct(){

        productManager.deleteProduct(product.getProductID());

        verify(productDao).deleteProductByID(product.getProductID());

    }
    @Test
    public void getProductsByCategoryID(){
        List<Product> expectedProducts = new ArrayList<>();
        List<Product> actualProducts = new ArrayList<>();
        Product product1 = getTestProduct();
        product1.setName("girl hat");
        expectedProducts.add(product);
        expectedProducts.add(product1);
        when(productDao.getProdactsByCategoryID(product.getCategory().getCategoryID())).thenReturn(expectedProducts);

        actualProducts = productManager.getProdactsByCategoryID(product.getCategory().getCategoryID());

        doAssertion(actualProducts.get(0), expectedProducts.get(0));
        doAssertion(actualProducts.get(1), expectedProducts.get(1));

    }






    private Product getTestProduct(){
        List<Media> medias = new ArrayList<>();
        Media media = new Media();
        media.setMediaID(2).setMediaPath("somePath").setProductID(20);
        medias.add(media);
        Category category = new Category();
        category.setCategoryID(1).setName("hat");
        Product product = new Product();
        product.setName("baby hat").
                setPrice(50).
                setDescription("color:white").
                setShippingPrice(1).
                setQuantity(50).
                setCategory(category).
                setMedias(medias);
        return product;
    }
    private void doAssertion(Product product, Product product1){
        assertEquals(product.getProductID(), product1.getProductID());
        assertEquals(product.getName(), product1.getName());
        assertEquals(product.getPrice(), product1.getPrice());
        assertEquals(product.getShippingPrice(), product1.getShippingPrice());
        assertEquals(product.getQuantity(), product1.getQuantity());
        assertEquals(product.getCategory().getCategoryID(), product1.getCategory().getCategoryID());
        assertEquals(product.getDescription(), product1.getDescription());
    }


}
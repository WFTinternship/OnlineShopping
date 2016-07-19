import com.workfront.internship.common.Media;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class MediaDaoUnitTest {
    DataSource dataSource;
    MediaDao mediaDao;

    @Before
    public void beforeTest()throws IOException, SQLException{
        dataSource = Mockito.mock(DataSource.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        mediaDao = new MediaDaoImpl(dataSource);
    }
    @Test(expected = RuntimeException.class)
    public void insertMedia_dbError() {
        mediaDao.insertMedia(new Media());
    }
    @Test(expected = RuntimeException.class)
    public void getMediaByMediaID_dbError() {
        mediaDao.getMediaByMediaID(8);
    }
    @Test(expected = RuntimeException.class)
    public void getMediaByProductID_dbError() {
        mediaDao.getMediaByProductID(3);
    }
    @Test(expected = RuntimeException.class)
    public void getAllMedias_dbError() {
        mediaDao.getAllMedias();
    }
    @Test(expected = RuntimeException.class)
    public void updateMedia_dbError() {
        mediaDao.updateMedia(new Media());
    }
    @Test(expected = RuntimeException.class)
    public void deleteMediaByID_dbError() {
        mediaDao.deleteMediaByID(3);
    }
    @Test(expected = RuntimeException.class)
    public void deleteMediaByPath_dbError() {
        mediaDao.deleteMediaByPath("somePath");
    }
    @Test(expected = RuntimeException.class)
    public void deleteMediaByProductID_dbError() {
        mediaDao.deleteMediaByProductID(3);
    }
    @Test(expected = RuntimeException.class)
    public void deleteAllMedias_dbError() {
        mediaDao.deleteAllMedias();
    }
}

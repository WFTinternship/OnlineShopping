package com.workfront.internship.business;

import com.workfront.internship.dao.SizeDao;
import com.workfront.internship.common.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Anna Asmangulyan on 03.09.2016.
 */
@Component
public class SizeManagerImpl implements SizeManager {
    @Autowired
    private SizeDao sizeDao;

    @Override
    public List<Size> getSizesByCategoryId(int categoryId){
        return sizeDao.getSizesByCategoryId(categoryId);
    }

}

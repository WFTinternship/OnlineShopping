package com.workfront.internship.dao;
import com.workfront.internship.common.Size;

import java.util.List;

/**
 * Created by Anna Asmangulyan on 03.09.2016.
 */
public interface SizeDao {

   List<Size> getSizesByCategoryId(int categoryId);

   int insertSize(Size size);

}

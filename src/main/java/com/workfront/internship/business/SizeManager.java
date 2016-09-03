package com.workfront.internship.business;
import com.workfront.internship.common.Size;

import java.util.List;

/**
 * Created by Anna Asmangulyan on 03.09.2016.
 */
public interface SizeManager {
    List<Size> getSizesByCategoryId(int categoryId);
}

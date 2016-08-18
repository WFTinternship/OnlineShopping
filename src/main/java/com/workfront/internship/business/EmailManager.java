package com.workfront.internship.business;

import com.workfront.internship.common.User;
import org.springframework.stereotype.Component;

/**
 * Created by Workfront on 7/28/2016.
 */
@Component
public class EmailManager {
    public  boolean sendVerificationEmail(User user){
        return true;
    }
}

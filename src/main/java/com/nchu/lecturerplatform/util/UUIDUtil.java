package com.nchu.lecturerplatform.util;

import java.util.UUID;

public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}

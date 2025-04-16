package com.booksystem.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PathUtils {
    public static String getClassLoadRootPath() {
        String path = "";
        try {
            String prePath = URLDecoder.decode(PathUtils.class.getClassLoader().getResource("").getPath(),"utf-8").replace("/target/classes", "");
//            System.out.println("prePath:"+prePath)
//            /D:/booksystem/initialcode/BackEnd/books-manage-sys/
            String osName = System.getProperty("os.name");
//            讀取是甚麼作業系統
//            System.out.println("osName:"+osName);
            if (osName.toLowerCase().startsWith("mac")) {
                // 蘋果
                path = prePath.substring(0, prePath.length() - 1);
            } else if (osName.toLowerCase().startsWith("windows")) {
                // windows
                path = prePath.substring(1, prePath.length() - 1);
//                System.out.println("windows:"+path);
//                D:/booksystem/initialcode/BackEnd/books-manage-sys
            } else if(osName.toLowerCase().startsWith("linux") || osName.toLowerCase().startsWith("unix")) {
                // unix or linux
                path = prePath.substring(0, prePath.length() - 1);
            } else {
                path = prePath.substring(1, prePath.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }

}
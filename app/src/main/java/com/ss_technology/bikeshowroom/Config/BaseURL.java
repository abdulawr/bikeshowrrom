package com.ss_technology.bikeshowroom.Config;

public class BaseURL {

    static String base = "http://showroom.dbscompany.tech/";
   // static String base = "http://10.0.3.2";
    public static String Path()
    {
        return base+"api/";
    }
    public static String ImagePath(String dir){
        return base+"images/"+dir+"/";
    }

}

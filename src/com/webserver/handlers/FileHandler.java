package com.webserver.handlers;

import com.webserver.utils.ContentType;

import java.io.File;

/**
 * Created by Cosmin on 9/3/2016.
 */
public class FileHandler {

    public FileHandler(){

    }
    public static ContentType getContentTypeFromFileExtension(File f){
        String extension = f.getAbsolutePath().split("\\.")[1];
        ContentType content = null;
        switch(extension){
            case "txt":
                content = ContentType.TEXT;
                break;
            case "html":
                content = ContentType.HTML;
                break;
            case "js":
                content = ContentType.JS;
                break;
            case "css":
                content = ContentType.CSS;
                break;
            case "png":
                content = ContentType.PNG;
                break;
            case "jpeg":
                content = ContentType.JPEG;
                break;
            case "jpg":
                content = ContentType.JPG;
                break;
            default:
                content = ContentType.HTML;
                break;
        }
        return content;
    }
}

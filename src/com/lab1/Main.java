package com.lab1;

import com.lab1.downloaders.ImageDownloader;
import com.lab1.downloaders.UTMImageDownloader;
import com.lab1.handlers.SocketHandler;
import com.lab1.handlers.UTMSocketHandler;
import com.lab1.handlers.UniteSocketHandler;

public class Main
{

    public static void main(String[] args)
    {
//        SocketHandler utmSocketHandler = new UTMSocketHandler();
//        UTMImageDownloader utmImageDownloader = new UTMImageDownloader(utmSocketHandler);
//        utmImageDownloader.downloadAllImages("src/com/lab1/UtmGETResponse.txt");

        SocketHandler uniteSocketHandler = new UniteSocketHandler();
        ImageDownloader imageDownloader = new ImageDownloader(uniteSocketHandler);
        imageDownloader.downloadAllImages("src/com/lab1/UniteGETResponse.txt");

    }

    public static void print(String text)
    {
        System.out.println(text);
    }
}

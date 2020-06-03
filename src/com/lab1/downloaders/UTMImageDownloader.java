package com.lab1.downloaders;

import com.lab1.handlers.SocketHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UTMImageDownloader extends ImageDownloader
{
    public UTMImageDownloader(SocketHandler socketHandler)
    {
        super(socketHandler);
    }

    @Override
    public String getAllImagesPaths(String text)
    {
        String paths = "";
        String tmp = "";

        Pattern pattern = Pattern.compile("img [0-9a-zA-Z_ =':/.-]*(jpg|gif|png)",
                Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find())
        {
            tmp = tmp.concat(matcher.group(0) + "\n");
        }

        pattern = Pattern.compile("/wp-content[0-9a-zA-Z_ =':/.-]*(jpg|gif|png)",
                Pattern.MULTILINE);
        matcher = pattern.matcher(tmp);

        while (matcher.find())
        {
            paths = paths.concat(matcher.group(0) + "\n");
        }

        return paths;
    }

    @Override
    public void downloadAllImages(String filePath)
    {
        String[] paths = getAllImagesPaths(readFromFile(filePath))
                .split("\\r?\\n");

        for (String path : paths)
        {
            getPathsStack().push(path);
        }

        Thread thread1 = createDownloadThread("A");
        Thread thread2 = createDownloadThread("B");
        Thread thread3 = createDownloadThread("C");
        Thread thread4 = createDownloadThread("D");
        Thread thread5 = createDownloadThread("E");
        Thread thread6 = createDownloadThread("F");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();

    }
}

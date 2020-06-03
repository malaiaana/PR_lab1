package com.lab1.downloaders;

import com.lab1.handlers.SocketHandler;
import com.lab1.structures.MySocket;

import java.io.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageDownloader
{
    SocketHandler socketHandler;
    private Semaphore semaphore4;
    private Semaphore semGetRef;
    private Semaphore semCloseConnection;
    private Semaphore semGetSocket;
    private Stack<String> pathsStack;

    private class DownloadThread extends Thread
    {
        String name;

        public DownloadThread(String name)
        {
            this.name = name;
        }

        @Override
        public void run()
        {
            try
            {
                semaphore4.acquire();
                try
                {
                    while (!pathsStack.isEmpty())
                    {
                        semGetRef.acquire();
                        String path = pathsStack.pop();
                        semGetRef.release();
                        System.out.println(name + " : is downloading " + path);
                        downloadImage(path, "src/com/lab1" + path);
                    }
                }
                finally
                {
//                    calling release () after a successful acquire ()
                    System.out.println(name + " : releasing lock...");
                    semaphore4.release();
                    System.out.println(name + " : available Semaphore permits now: "
                            + semaphore4.availablePermits());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        public void downloadImage(String imageWebPath, String imagePath)
        {
            try
            {
                semGetSocket.acquire();
                int socketID = socketHandler.getSocketID(imageWebPath);
                MySocket socket = socketHandler.findSocket(socketID);
                semGetSocket.release();

                Pattern patternOfName = Pattern.compile("[-a-z_A-Z0-9]*(\\.jpg|\\.png)");
                Pattern patternOfPath = Pattern.compile("/wp-content/uploads/[0-9/]*");

                Matcher matcherOfName = patternOfName.matcher(imagePath);
                Matcher matcherOfPath = patternOfPath.matcher(imagePath);

                String fileName = ""; // should be downloaded
                String foldersPath = "";

                if (matcherOfName.find())
                {
                    fileName = fileName.concat(matcherOfName.group(0));
                }

                if (matcherOfPath.find())
                {
                    foldersPath = foldersPath.concat(matcherOfPath.group(0));
                }

                boolean dirCreated = new File("src/com/lab1" + foldersPath).mkdirs();
                // right - [-a-z_A-Z0-9]*(\.jpg|\.png)
                // left - \/wp-content\/uploads\/[0-9\/]*

                DataInputStream in = new DataInputStream(socket.getSocket().getInputStream());
                OutputStream out = new FileOutputStream(imagePath);

                int count, offset;
                byte[] buffer = new byte[2048];
                boolean eohFound = false;
                while ((count = in.read(buffer)) != -1)
                {
                    offset = 0;
                    if (!eohFound)
                    {
                        String string = new String(buffer, 0, count);
                        int indexOfEOH = string.indexOf("\r\n\r\n");
                        if (indexOfEOH != -1)
                        {
                            count = count - indexOfEOH - 4;
                            offset = indexOfEOH + 4;
                            eohFound = true;
                        }
                        else
                        {
                            count = 0;
                        }
                    }
                    out.write(buffer, offset, count);
                    out.flush();
                }
                in.close();
                out.close();
                semCloseConnection.acquire();
                socketHandler.closeConnection(socketID);
                semCloseConnection.release();
                System.out.println(imageWebPath + " is downloaded");
            }
            catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

    public ImageDownloader(SocketHandler socketHandler)
    {
        this.socketHandler = socketHandler;
        semaphore4 = new Semaphore(4);
        semGetRef = new Semaphore(1);
        semCloseConnection = new Semaphore(1);
        semGetSocket = new Semaphore(1);
        pathsStack = new Stack<>();
    }

    public String readFromFile(String filePath)
    {
        String result = "";
        try
        {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine())
            {
                result = result.concat(myReader.nextLine() + "\n");
            }
            myReader.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return result;
    }

    public String getImagePathByRef(String text, String referenceType)
    {
        String result = "";
        Pattern pattern = Pattern.compile(referenceType +
                "=\"/.*(png|gif|jpg)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find())
        {
            result = result.concat(matcher.group() + "\n");
        }
        return result;
    }

    public String getAllImagesPaths(String text)
    {
        String paths = "";
        String tmp = "";
        Pattern pattern = Pattern.compile("<img.*?src=\"(.*?)\"[^>]+>");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find())
        {
            tmp = tmp.concat(matcher.group(0) + "\n");
        }
        paths = paths.concat(getImagePathByRef(tmp, "src"));
        paths = paths.concat(getImagePathByRef(tmp, "lazy"));
        paths = paths.concat(getImagePathByRef(tmp, "narrow"));

        tmp = paths;
        paths = "";

        pattern = Pattern.compile("/.*");
        matcher = pattern.matcher(tmp);

        while (matcher.find())
        {
            paths = paths.concat((matcher.group(0) + "\n"));
        }

        return paths;
    }

    public void downloadAllImages(String filePath)
    {
        String[] paths = getAllImagesPaths(readFromFile(filePath))
                .split("\\r?\\n");

        for (String path : paths)
        {
            pathsStack.push(path);
        }

        DownloadThread downloadThread1 = new DownloadThread("A");
        DownloadThread downloadThread2 = new DownloadThread("B");
        DownloadThread downloadThread3 = new DownloadThread("C");
        DownloadThread downloadThread4 = new DownloadThread("D");
        DownloadThread downloadThread5 = new DownloadThread("E");
        DownloadThread downloadThread6 = new DownloadThread("F");

        downloadThread1.start();
        downloadThread2.start();
        downloadThread3.start();
        downloadThread4.start();
        downloadThread5.start();
        downloadThread6.start();

    }

    public DownloadThread createDownloadThread(String name)
    {
        return new DownloadThread(name);
    }

    public Stack<String> getPathsStack()
    {
        return pathsStack;
    }
}

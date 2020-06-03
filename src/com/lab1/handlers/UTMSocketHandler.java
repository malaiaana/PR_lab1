package com.lab1.handlers;

import com.lab1.structures.MySocket;
import com.lab1.structures.UTMSocket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class UTMSocketHandler extends SocketHandler
{
    public UTMSocketHandler()
    {
        super();
    }

    @Override
    public int getSocketID(String webPath)
    {
        SSLSocketFactory factory;
        SSLSocket socket;
        MySocket mySocket = new UTMSocket();
        BufferedReader in;
        PrintWriter out;
        try
        {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket("utm.md", 443);

            socket.startHandshake();

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())));

            out.println("GET " + webPath + " HTTP/1.1");
            out.println("HOST: utm.md");
            out.println("Accept-Language: en, ru, ro");
            out.println("Connection: keep-alive");
            out.println("Keep-Alive: 300");
            out.println("Cache-Control: no-cache");
            out.println("Accept-Charset: utf-8, iso-8859-1;q=0.5");
            out.println();

            out.flush();

            mySocket = new UTMSocket(socket, getNextSocketId(), in, out);
            setNextSocketId(getNextSocketId() + 1);

            addSocket(mySocket);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mySocket.getId();
    }
}

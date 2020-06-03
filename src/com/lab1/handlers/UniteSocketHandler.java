package com.lab1.handlers;

import com.lab1.structures.MySocket;
import com.lab1.structures.UniteSocket;

import java.io.*;

public class UniteSocketHandler extends SocketHandler
{
    public UniteSocketHandler()
    {
        super();
    }

    @Override
    public int getSocketID(String webPath)
    {
        BufferedReader in;
        PrintWriter out;
        MySocket mySocket = new UniteSocket();
        try
        {
            mySocket = new UniteSocket("unite.md", 80, getNextSocketId());
            in = new BufferedReader(
                    new InputStreamReader(mySocket.getSocket().getInputStream()));
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(mySocket.getSocket().getOutputStream())));
            mySocket.setIn(in);
            mySocket.setOut(out);
            setNextSocketId(getNextSocketId() + 1);

            out.println("GET " + webPath + " HTTP/1.1");
            out.println("HOST: unite.md");
            out.println("Accept-Language: en, ru, ro");
            out.println("Connection: keep-alive");
            out.println("Keep-Alive: 300");
            out.println("Cache-Control: no-cache");
            out.println("Accept-Charset: utf-8, iso-8859-1;q=0.5");
            out.println();

            out.flush();

            addSocket(mySocket);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return mySocket.getId();
    }
}

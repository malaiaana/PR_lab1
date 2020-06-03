package com.lab1.handlers;

import com.lab1.structures.MySocket;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class SocketHandler
{
    private ArrayList<MySocket> sockets;
    private int nextSocketId;

    public SocketHandler()
    {
        sockets = new ArrayList<>();
        nextSocketId = 0;
    }

    public abstract int getSocketID(String webPath);

    public String sendSocketRequest(int socketID)
    {
        MySocket socket = findSocket(socketID);
        String inputLine = null;
        String text = "";

        if (socket != null)
        {
            while (true)
            {
                try
                {
                    if ((inputLine = socket.getIn().readLine()) == null)
                    {
                        break;
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                text = text.concat(inputLine + "\n");
            }

            closeConnection(socket.getId());
        }
        return text;
    }

    protected void addSocket(MySocket socket)
    {
        this.sockets.add(socket);
    }

    public MySocket findSocket(int socketID)
    {
        MySocket foundSocket = null;
        for (MySocket socket : sockets)
        {
            if (socket.getId() == socketID)
            {
                foundSocket = socket;
                break;
            }
        }
        return foundSocket;
    }

    public void closeConnection(int socketId)
    {
        Iterator<MySocket> it = getSockets().iterator();
        while (it.hasNext())
        {
            MySocket socket = it.next();
            if (socket.getId() == socketId)
            {
                try
                {
                    socket.getIn().close();
                    socket.getOut().close();
                    socket.getSocket().close();
                    it.remove();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected int getNextSocketId()
    {
        return nextSocketId;
    }

    protected void setNextSocketId(int nextSocketId)
    {
        this.nextSocketId = nextSocketId;
    }

    protected ArrayList<MySocket> getSockets()
    {
        return sockets;
    }

    protected void setSockets(ArrayList<MySocket> sockets)
    {
        this.sockets = sockets;
    }
}

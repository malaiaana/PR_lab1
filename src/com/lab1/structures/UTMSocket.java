package com.lab1.structures;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UTMSocket implements MySocket
{

    private final SSLSocket socket;
    private final int id;
    private BufferedReader in;
    private PrintWriter out;

    public UTMSocket()
    {
        socket = null;
        id = -1;
        in = null;
        out = null;
    }

    public UTMSocket(SSLSocket socket, int id, BufferedReader in, PrintWriter out)
    {
        this.socket = socket;
        this.id = id;
        this.in = in;
        this.out = out;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public void setIn(BufferedReader in)
    {
        this.in = in;
    }

    @Override
    public BufferedReader getIn()
    {
        return in;
    }

    @Override
    public void setOut(PrintWriter out)
    {
        this.out = out;
    }

    @Override
    public PrintWriter getOut()
    {
        return out;
    }

    @Override
    public Socket getSocket()
    {
        return socket;
    }
}

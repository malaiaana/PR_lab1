package com.lab1.structures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class UniteSocket
        extends Socket
        implements MySocket
{
    private final int id;
    private BufferedReader in;
    private PrintWriter out;

    public UniteSocket()
    {
        id = -1;
        in = null;
        out = null;
    }

    public UniteSocket(String host, int port, int id) throws IOException
    {
        super(host, port);
        this.id = id;
    }

    public UniteSocket(Socket socket, int id, BufferedReader in, PrintWriter out) throws IOException
    {
        super(socket.getInetAddress(), socket.getPort());
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
    public BufferedReader getIn()
    {
        return in;
    }

    @Override
    public PrintWriter getOut()
    {
        return out;
    }

    @Override
    public Socket getSocket()
    {
        return this;
    }

    @Override
    public void setIn(BufferedReader in)
    {
        this.in = in;
    }

    @Override
    public void setOut(PrintWriter out)
    {
        this.out = out;
    }
}

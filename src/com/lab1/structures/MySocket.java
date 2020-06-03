package com.lab1.structures;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface MySocket
{
    int getId();

    void setIn(BufferedReader in);

    BufferedReader getIn();

    void setOut(PrintWriter out);

    PrintWriter getOut();

    Socket getSocket();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivy
 */
public class TcpListenerThread extends Thread {

    private boolean run;
    private TcpServer tcpServer;
    private ServerSocket serverSocket;
    private long ticks;

    public TcpListenerThread(TcpServer tcp) {
        ticks = 0L;
        tcpServer = tcp;
        serverSocket = tcp.getServerSocket();
    }

    @Override
    public void run() {
        if (run) {
            return;
        }
        run = true;
        System.out.println("[*] Running TCP server at 0.0.0.0:" + serverSocket.getLocalPort());
        while (run) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                // Give some time to complete the connection
                Thread.sleep(200L);
                if (socket == null || !socket.isConnected()) {
                    throw new Exception("Peer " + socket.getInetAddress().toString() + " was closed before the accept.");
                }
                
                TcpPeer peer = new TcpPeer(tcpServer, socket);
                if (!peer.startListener())
                    throw new Exception("Could not start listening on peer " + socket.getInetAddress().toString());
                
                tcpServer.enqueuePeer(peer);
            } catch (Exception ex) {
                Logger.getLogger(TcpListenerThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Give some time to remaining CPU to process remaining cycles
            try {
                ticks++;
                Thread.sleep(1L);
            } catch (InterruptedException e) {
            }
        }
    }

    public void kill() {
        run = false;
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException ex) {
            Logger.getLogger(TcpListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

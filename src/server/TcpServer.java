/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ivy
 */
public class TcpServer {

    private ServerSocket serverSocket;
    private TcpListenerThread listener;
    private AtomicInteger socketId;
    private Queue<TcpPeer> peers;

    public TcpServer() {
        socketId = new AtomicInteger(0);
        peers = new ConcurrentLinkedQueue<>();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    private int generateSocketId() {
        return socketId.addAndGet(1);
    }

    public boolean start(int port) throws Exception {
        if (isRunning()) {
            return false;
        }

        serverSocket = new ServerSocket(port);
        listener = new TcpListenerThread(this);
        listener.start();

        return true;
    }

    public boolean kill() throws Exception {
        if (serverSocket == null || listener == null) {
            return false;
        }

        listener.kill();
        serverSocket.close();
        return true;
    }

    public boolean isRunning() {
        return serverSocket != null && listener != null;
    }

    public boolean enqueuePeer(TcpPeer peer) throws Exception {
        boolean result = peers.add(peer);
        if (!result) {
            peer.kill();
            return false;
        }
        
        peer.setSocketId(generateSocketId());

        System.out.printf("[*] Peer %s:%d connected to TCP server at %s:%d successfully.\n",
                peer.getSocket().getInetAddress().getHostAddress(),
                peer.getSocket().getPort(),
                serverSocket.getInetAddress().getHostAddress(),
                serverSocket.getLocalPort()
        );

        return true;
    }
    
    public TcpPeer[] getPeers(){
        return (TcpPeer[])peers.toArray();
    }
    
    public TcpPeer findPeerByUuid(String uuid){
        for(TcpPeer peer : peers){
            if (peer.getSocketUuid().equals(uuid))
                return peer;
        }
        return null;
    }

}

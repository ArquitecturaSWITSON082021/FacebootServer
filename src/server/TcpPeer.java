/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import FacebootNet.Engine.AbstractPacket;
import ciphers.HashProvider;
import java.io.DataOutputStream;
import java.net.Socket;
import models.UserToken;

/**
 *
 * @author Ivy
 */
public class TcpPeer {

    private int id;
    private String uuid;
    private final TcpServer server;
    private TcpPeerThread thread;
    private final Socket socket;
    private long lastHeartbeat;
    private UserToken token;
    
    public TcpPeer(TcpServer server, Socket socket){
        this.server = server;
        this.socket = socket;
        this.lastHeartbeat = 0L;
        this.token = null;
    }
    
    public long getLastHeartbeat(){
        return lastHeartbeat;
    }
    
    public int getSocketId(){
        return this.id;
    }
    
    public String getSocketUuid(){
        return this.uuid;
    }
    
    public void setSocketId(int id){
        this.id = id;
        this.uuid = HashProvider.sha256.Encrypt(String.format("tcp_socket_%d_!#@#!@#", id));
    }
    
    public Socket getSocket(){
        return socket;
    }
    
    public boolean isListening(){
        return this.thread != null;
    }
    
    public boolean startListener(){
        if (isListening())
            return false;
        
        this.lastHeartbeat = managers.TimeManager.GetTickCount();
        this.thread = new TcpPeerThread(this);
        this.thread.start();
        
        return true;
    }
    
    public void authenticate(UserToken token){
        this.token = token;
    }
    
    public UserToken getToken(){
        return this.token;
    }
    
    public void write(byte[] packet) throws Exception{
        DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
        outstream.write(packet);
    }
    
    public void write(AbstractPacket packet) throws Exception{
        write(packet.Serialize());
    }
    
    public boolean isAuthenticated(){
        return this.token != null && this.token.getId() > 0;
    }
    
    public boolean kill() throws InterruptedException{
        if (!isListening())
            return false;
        
        return this.thread.kill();
    }
    
    

    
}

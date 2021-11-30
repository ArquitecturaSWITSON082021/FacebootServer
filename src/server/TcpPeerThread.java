/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import FacebootNet.Engine.PacketBuffer;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import router.Router;

/**
 *
 * @author Ivy
 */
public final class TcpPeerThread extends Thread {

    private TcpServer server;
    private Socket socket;
    private boolean isRunning;
    private PacketBuffer packet;
    private int packetSize;
    
    public TcpPeerThread(TcpServer server, Socket socket) {
        this.server = server;
        this.socket = socket;      
        this.packet = null;     
        this.packetSize = 0;
    }
    
    @Override
    public void run() {
        isRunning = true;
        BufferedInputStream instream = null;
        DataOutputStream outstream = null;
        try{
            instream = new BufferedInputStream(socket.getInputStream());
            outstream = new DataOutputStream(socket.getOutputStream());
        }catch(Exception e){}
        byte[] buff = new byte[1024];
        while(isRunning){
            int dwBytesRead = 0;
            try {
                Arrays.fill(buff, (byte)0);
                dwBytesRead = instream.read(buff, 0, buff.length);
            }catch(Exception e){
                continue;
            }
            
            if (dwBytesRead <= 0)
                    continue;
            
            try{
                
                
                packetSize += dwBytesRead;
                
                if (packet == null){
                    packet = new PacketBuffer(buff);
                }else{
                    packet.Write(buff, dwBytesRead);
                }
                
                if (packet != null && packetSize == packet.getSize()){
                        // execute packet
                        byte[] response = Router.Execute(packet, this.socket);
                        if (response != null){
                            outstream.write(response);
                        }
                        packet = null;
                        packetSize = 0;
                    }
                
                
                /*System.arraycopy(tempBuffer, 0, packetBuffer, currentOffset, dwBytesRead);
                currentOffset += dwBytesRead;
                System.out.println(packetBuffer); */
                
            } catch (Exception ex) {
                Logger.getLogger(TcpPeerThread.class.getName()).log(Level.SEVERE, null, ex);
                this.kill();
            }
            
            
        }
    }
    
    public boolean kill(){
        if (isRunning)
            return false;
        
        isRunning = false;
        try{
        Thread.sleep(200L);
        }catch(Exception e){}
        return true;
    }
    
}

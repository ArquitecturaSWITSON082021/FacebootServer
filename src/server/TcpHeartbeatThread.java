/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import managers.TimeManager;

/**
 *
 * @author Ivy
 */
public class TcpHeartbeatThread extends Thread {
    
    public TcpServer server;
    private boolean isRunning;
    
    public TcpHeartbeatThread(TcpServer server){
        this.server = server;
    }
    
    
    public void run(){
        isRunning = true;
        while(isRunning){
            try{
                TcpPeer[] peers = server.getPeers();
                for(TcpPeer peer : peers){
                    int diff = (int) (TimeManager.GetTickCount() - peer.getLastHeartbeat());
                    System.out.println(diff);
                }
            Thread.sleep(3000L);
            }catch(Exception e){}
        }
    }
    
    public void kill() throws InterruptedException{
        isRunning = false;
        Thread.sleep(200L);
    }
    
}

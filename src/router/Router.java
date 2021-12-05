/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import FacebootNet.Engine.PacketBuffer;
import java.net.Socket;
import server.TcpPeer;

/**
 *
 * @author Ivy
 */
public class Router {
    
    public static byte[] Execute(PacketBuffer packet, TcpPeer peer) throws Exception{
        String ipAddress = peer.getSocket().getInetAddress().getHostAddress();
        byte[] buf = packet.Serialize();
        System.out.printf("[*-%s] len=%d hex=%s\n", ipAddress, buf.length, FacebootNet.Utils.BytesToHex(buf));
        switch(packet.GetOpcode()){
            case FacebootNet.Engine.Opcodes.Hello:
                return controllers.SystemController.FetchServerStatus(buf);
            case FacebootNet.Engine.Opcodes.Login:
                return controllers.AuthController.DoLogin(peer, ipAddress, buf, false);
            case FacebootNet.Engine.Opcodes.DoRegister:
                return controllers.RegisterController.DoRegister(buf);
            case FacebootNet.Engine.Opcodes.DoPost:
                return controllers.PostsController.DoPost(peer, buf);
            case FacebootNet.Engine.Opcodes.FetchPosts:
                return controllers.PostsController.FetchPosts(buf);
            case FacebootNet.Engine.Opcodes.AttemptOauth:
                return controllers.AuthController.DoAttemptOauth(peer.getSocketUuid(), buf);
        }
        System.out.println("Unknown packet opcode: " + packet.GetOpcode());
        return null;
    }
}

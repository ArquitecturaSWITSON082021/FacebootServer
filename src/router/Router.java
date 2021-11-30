/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import FacebootNet.Engine.PacketBuffer;
import java.net.Socket;

/**
 *
 * @author Ivy
 */
public class Router {
    
    public static byte[] Execute(PacketBuffer packet, Socket socket) throws Exception{
        String ipAddress = socket.getInetAddress().getHostAddress();
        System.out.printf("[*-%s] hex=%s\n", ipAddress, FacebootNet.Utils.BytesToHex(packet.Serialize()));
        switch(packet.GetOpcode()){
            case FacebootNet.Engine.Opcodes.Hello:
                return controllers.SystemController.FetchServerStatus(packet);
            case FacebootNet.Engine.Opcodes.Login:
                return controllers.AuthController.DoLogin(packet, ipAddress);
            case FacebootNet.Engine.Opcodes.DoRegister:
                return controllers.RegisterController.DoRegister(packet);
        }
        System.out.println("Unknown packet opcode: " + packet.GetOpcode());
        return null;
    }
}

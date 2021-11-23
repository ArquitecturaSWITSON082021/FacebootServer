/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import FacebootNet.Engine.PacketBuffer;

/**
 *
 * @author Ivy
 */
public class Router {
    
    public static byte[] Execute(PacketBuffer packet) throws Exception{
        switch(packet.GetOpcode()){
            case FacebootNet.Engine.Opcodes.Hello:
                System.out.println("Got hello packet!");
                return controllers.SystemController.FetchServerStatus(packet);
            case FacebootNet.Engine.Opcodes.Login:
                System.out.println("Got login packet!");
                return null;
            case FacebootNet.Engine.Opcodes.DoRegister:
                System.out.println("Got register packet!");
                return null;
        }
        System.out.println("Unknown packet opcode: " + packet.GetOpcode());
        return null;
    }
}

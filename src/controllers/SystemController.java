/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Client.*;
import FacebootNet.Packets.Server.*;

/**
 *
 * @author Ivy
 */
public class SystemController {
    
    public static byte[] FetchServerStatus(byte[] packet) throws Exception{
        CHandshakePacket handshake = CHandshakePacket.Deserialize(packet);
        SHandshakePacket response = new SHandshakePacket(handshake.GetRequestIndex());
        response.ApplicationVersion = handshake.ApplicationVersion;
        response.IsAuthServiceRunning = true;
        response.IsChatMessageRunning = true;
        response.IsPostServiceRunning = true;
        return response.Serialize();
    }
}

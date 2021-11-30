/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import FacebootNet.Engine.ErrorCode;
import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Client.CDoPostPacket;
import FacebootNet.Packets.Client.CFetchPostsPacket;
import FacebootNet.Packets.Server.EPostStruct;
import FacebootNet.Packets.Server.SDoPostPacket;
import FacebootNet.Packets.Server.SFetchPostsPacket;
import java.io.File;
import java.util.Date;
import java.util.List;
import managers.AttachmentManager;
import models.Post;
import models.PostAttachment;
import models.User;
import server.TcpPeer;

/**
 *
 * @author Ivy
 */
public class PostsController {

    public static byte[] FetchPosts(byte[] buf) throws Exception {
        CFetchPostsPacket packet = CFetchPostsPacket.Deserialize(buf);
        SFetchPostsPacket response = new SFetchPostsPacket(packet.GetRequestIndex());
        List<Post> posts = dao.DaoProvider.Posts.Find();
        for(Post post : posts){
            User user = dao.DaoProvider.Users.FindFirstById(post.getUser().getId());
            List<PostAttachment> attachments = dao.DaoProvider.PostsAttachments.FindPostsAttachments(post);
            EPostStruct struct = new EPostStruct();
            struct.PostId = post.getId();
            struct.PostBody = post.getText();
            struct.PostTime = post.getCreatedAt().getTime();
            struct.TotalComments = 0;
            struct.TotalLikes = 0;
            struct.TotalReactions = 0;
            struct.UserId = user.getId();
            struct.UserName = user.getName() + user.getLastName();
            struct.HasAttachment = attachments != null && attachments.size() > 0;
            response.AddPost(struct);
        }
        
        return response.Serialize();
    }
    public static byte[] DoPost(TcpPeer peer, byte[] buf) throws Exception {
        CDoPostPacket packet = CDoPostPacket.Deserialize(buf);
        SDoPostPacket response = new SDoPostPacket(packet.GetRequestIndex());
        boolean hasPicture = packet.Picture != null && packet.Picture.length > 0;

        // Craft post row
        String uuid = ciphers.HashProvider.sha256.Encrypt(String.format(
                "()@!#)_post_%d_%d", new Date().getTime(), peer.getToken().getId()));

        Post post = dao.DaoProvider.Posts.Craft();
        post.setUuid(uuid);
        post.setText(packet.Contents);
        post.setUser(peer.getToken().getUser());
        post.setUserToken(peer.getToken());

        if (!post.Save()) {
            response.ErrorCode = ErrorCode.InternalServerError;
            return response.Serialize();
        }

        if (hasPicture) {
            if (AttachmentManager.WriteFile(uuid, packet.Picture)) {
                PostAttachment attachment = dao.DaoProvider.PostsAttachments.Craft();
                attachment.setUser(peer.getToken().getUser());
                attachment.setUserToken(peer.getToken());
                attachment.setPost(post);
                attachment.setAttachmentType(0);
                attachment.setFilename(packet.Filename);
                attachment.setFilepath(AttachmentManager.GetWorkingDirectory() + File.separatorChar + uuid);
                attachment.setFilesize(packet.Picture.length);
                if (!attachment.Save()) {
                    response.ErrorCode = ErrorCode.InternalServerError;
                    return response.Serialize();
                }
            }
        }

        return response.Serialize();
    }
}

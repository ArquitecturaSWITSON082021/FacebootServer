package models;
// Generated Nov 23, 2021 7:39:07 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PostReaction generated by hbm2java
 */
public class PostReaction  implements java.io.Serializable {


     private Integer id;
     private Post post;
     private User user;
     private UserToken userToken;
     private int reactionType;
     private Date updatedAt;
     private Date createdAt;
     private Date deletedAt;
     private Set userNotifications = new HashSet(0);

    public PostReaction() {
    }

	
    public PostReaction(Post post, User user, UserToken userToken, int reactionType) {
        this.post = post;
        this.user = user;
        this.userToken = userToken;
        this.reactionType = reactionType;
    }
    public PostReaction(Post post, User user, UserToken userToken, int reactionType, Date updatedAt, Date createdAt, Date deletedAt, Set userNotifications) {
       this.post = post;
       this.user = user;
       this.userToken = userToken;
       this.reactionType = reactionType;
       this.updatedAt = updatedAt;
       this.createdAt = createdAt;
       this.deletedAt = deletedAt;
       this.userNotifications = userNotifications;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Post getPost() {
        return this.post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public UserToken getUserToken() {
        return this.userToken;
    }
    
    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }
    public int getReactionType() {
        return this.reactionType;
    }
    
    public void setReactionType(int reactionType) {
        this.reactionType = reactionType;
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getDeletedAt() {
        return this.deletedAt;
    }
    
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
    public Set getUserNotifications() {
        return this.userNotifications;
    }
    
    public void setUserNotifications(Set userNotifications) {
        this.userNotifications = userNotifications;
    }




}



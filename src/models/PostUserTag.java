package models;
// Generated Nov 23, 2021 7:39:07 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * PostUserTag generated by hbm2java
 */
public class PostUserTag  implements java.io.Serializable {


     private Integer id;
     private Post post;
     private PostComment postComment;
     private User userByUserId;
     private User userByUserTagId;
     private UserToken userToken;
     private Date updatedAt;
     private Date createdAt;
     private Date deletedAt;

    public PostUserTag() {
    }

	
    public PostUserTag(Post post, PostComment postComment, User userByUserId, User userByUserTagId, UserToken userToken) {
        this.post = post;
        this.postComment = postComment;
        this.userByUserId = userByUserId;
        this.userByUserTagId = userByUserTagId;
        this.userToken = userToken;
    }
    public PostUserTag(Post post, PostComment postComment, User userByUserId, User userByUserTagId, UserToken userToken, Date updatedAt, Date createdAt, Date deletedAt) {
       this.post = post;
       this.postComment = postComment;
       this.userByUserId = userByUserId;
       this.userByUserTagId = userByUserTagId;
       this.userToken = userToken;
       this.updatedAt = updatedAt;
       this.createdAt = createdAt;
       this.deletedAt = deletedAt;
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
    public PostComment getPostComment() {
        return this.postComment;
    }
    
    public void setPostComment(PostComment postComment) {
        this.postComment = postComment;
    }
    public User getUserByUserId() {
        return this.userByUserId;
    }
    
    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }
    public User getUserByUserTagId() {
        return this.userByUserTagId;
    }
    
    public void setUserByUserTagId(User userByUserTagId) {
        this.userByUserTagId = userByUserTagId;
    }
    public UserToken getUserToken() {
        return this.userToken;
    }
    
    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
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




}



package models;
// Generated Nov 23, 2021 7:39:07 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * PostHashtag generated by hbm2java
 */
public class PostHashtag  implements java.io.Serializable {


     private Integer id;
     private Post post;
     private User user;
     private UserToken userToken;
     private String hashtag;
     private Date updatedAt;
     private Date createdAt;
     private Date deletedAt;

    public PostHashtag() {
    }

	
    public PostHashtag(Post post, User user, UserToken userToken, String hashtag) {
        this.post = post;
        this.user = user;
        this.userToken = userToken;
        this.hashtag = hashtag;
    }
    public PostHashtag(Post post, User user, UserToken userToken, String hashtag, Date updatedAt, Date createdAt, Date deletedAt) {
       this.post = post;
       this.user = user;
       this.userToken = userToken;
       this.hashtag = hashtag;
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
    public String getHashtag() {
        return this.hashtag;
    }
    
    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
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



package com.taotao.blog.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Taotao Ma
 */
@Entity
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String email;
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @ManyToOne
    private Post post;

    @OneToMany(mappedBy = "originalComment")
    private List<Comment> replyComments = new ArrayList<>();

    @ManyToOne
    private Comment originalComment;

    private boolean fromAdmin;

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Comment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }

    public Comment getOriginalComment() {
        return originalComment;
    }

    public void setOriginalComment(Comment originalComment) {
        this.originalComment = originalComment;
    }

    public boolean isFromAdmin() {
        return fromAdmin;
    }

    public void setFromAdmin(boolean fromAdmin) {
        this.fromAdmin = fromAdmin;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", content='" + content + '\'' +
                ", created=" + created +
                ", post=" + post +
                ", replyComments=" + replyComments +
                ", originalComment=" + originalComment +
                ", fromAdmin=" + fromAdmin +
                '}';
    }
}



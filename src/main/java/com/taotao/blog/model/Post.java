package com.taotao.blog.model;

import javax.persistence.*;
import java.util.*;

/**
 * @author Taotao Ma
 */
@Entity
@Table(name = "t_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    private boolean status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @ManyToOne
    private Topic topic;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Transient
    private String tagIds;

    private String description;

    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                ", topic=" + topic +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void init() {
        this.tagIds = tagsToIds(this.tags);
    }

    private String tagsToIds(List<Tag> tags) {
        if (tags.isEmpty()) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    builder.append(",");
                } else {
                    flag = true;
                }
                builder.append(tag.getId());
            }
            return builder.toString();
        }
    }

    public List<Long> getRelatedPostIds() {
        Set<Long> postIds = new HashSet<>();
        for (Post post : topic.getPosts()) {
            postIds.add(post.getId());
        }
        for (Tag tag : tags) {
            for (Post post : tag.getPosts()) {
                postIds.add(post.getId());
            }
        }
        postIds.remove(id);
        List<Long> ids = new ArrayList<>(postIds);
        ids.sort(Collections.reverseOrder());
        return ids;
    }
}

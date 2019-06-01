package com.taotao.blog.service;

import com.taotao.blog.dao.CommentRepository;
import com.taotao.blog.model.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Taotao Ma
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository repository;

    @Override
    public List<Comment> listCommentByPostId(Long postId) {
        List<Comment> comments = repository.findByPostIdAndOriginalCommentNull(postId, Sort.by("created"));
        return rebuildCommentView(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long originalCommentId = comment.getOriginalComment().getId();
        if (originalCommentId != -1) {
            comment.setOriginalComment(repository.findById(originalCommentId).orElse(null));
        } else {
            comment.setOriginalComment(null);
        }
        comment.setCreated(new Date());
        return repository.save(comment);
    }

    private List<Comment> rebuildCommentView(List<Comment> comments) {
        List<Comment> commentView = new ArrayList<>();
        for (Comment comment : comments) {
            // copy comment to rebuild
            Comment newOriginalComment = new Comment();
            BeanUtils.copyProperties(comment, newOriginalComment);

            // combine all replies into one list
            List<Comment> newReplyComments = new ArrayList<>();
            combineRepliesHelper(newOriginalComment, newReplyComments);
            newOriginalComment.setReplyComments(newReplyComments);

            // add to the comment view to return
            commentView.add(newOriginalComment);
        }
        return commentView;
    }

    private List<Comment> combineRepliesHelper(Comment comment, List<Comment> commentList) {
        List<Comment> replyComments = comment.getReplyComments();

        for (Comment replyComment : replyComments) {
            commentList.add(replyComment);
            commentList.addAll(combineRepliesHelper(replyComment, commentList));
        }

        return replyComments;
    }
}

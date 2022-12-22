package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Test
    void savaOrUpdate() {
        Comment comment = new Comment(5, "bravo");
        commentService.saveOrUpdate(comment);
        assertEquals(comment.getComment(), commentService.findById(comment.getId()).get().getComment());
    }

    @Test
    void delete() {
        List<Comment> comments1 = commentService.findAll();
        assertEquals(10, comments1.size());

        Optional<Comment> optionalComment = commentService.findById(12L);
        commentService.delete(optionalComment.get());
        List<Comment> comments2 = commentService.findAll();
        assertEquals(9, comments2.size());
    }

    @Test void findAll() {
        List<Comment> comments = commentService.findAll();
        Optional<Comment> optionalComment = commentService.findById(1L);
        assertEquals(comments.get(0).getCreationDate(), optionalComment.get().getCreationDate());
    }
}
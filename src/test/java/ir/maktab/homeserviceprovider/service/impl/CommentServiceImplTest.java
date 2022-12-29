package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.service.CommentService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Test
    @Order(1)
    void savaOrUpdate() {
        Comment comment = new Comment(5, "bravo");
        commentService.saveOrUpdate(comment);
        assertEquals(comment.getComment(), commentService.findById(comment.getId()).get().getComment());
    }

    @Test
    @Order(2)
    void delete() {
        Comment comment = new Comment(5, "bravo");
        commentService.saveOrUpdate(comment);
        List<Comment> comments1 = commentService.findAll();
        commentService.delete(comment);
        List<Comment> comments2 = commentService.findAll();
        assertEquals(comments1.size()-1, comments2.size());
    }
}
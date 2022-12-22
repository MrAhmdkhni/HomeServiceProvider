package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

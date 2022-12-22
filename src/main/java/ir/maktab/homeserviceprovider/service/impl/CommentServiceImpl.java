package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.repository.CommentRepository;
import ir.maktab.homeserviceprovider.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl
        extends BaseServiceImpl<Comment, Long, CommentRepository> implements CommentService {

    public CommentServiceImpl(CommentRepository repository) {
        super(repository);
    }
}

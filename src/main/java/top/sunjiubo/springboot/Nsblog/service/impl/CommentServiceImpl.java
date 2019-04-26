package top.sunjiubo.springboot.Nsblog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.sunjiubo.springboot.Nsblog.Repository.CommentRepository;
import top.sunjiubo.springboot.Nsblog.model.Comment;
import top.sunjiubo.springboot.Nsblog.service.CommentService;

import javax.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    /* (non-Javadoc)
     * @see com.waylau.spring.boot.blog.service.CommentService#removeComment(java.lang.Long)
     */
    @Override
    @Transactional
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

}

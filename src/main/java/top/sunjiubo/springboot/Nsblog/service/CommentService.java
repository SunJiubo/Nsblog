package top.sunjiubo.springboot.Nsblog.service;

import top.sunjiubo.springboot.Nsblog.model.Comment;

public interface CommentService {

    /**
     * 根据id获取评论
     * @param id
     * @return
     */
    Comment getCommentById(Long id);

    /**
     * 删除评论
     * @param id
     */
    void removeComment(Long id);
}

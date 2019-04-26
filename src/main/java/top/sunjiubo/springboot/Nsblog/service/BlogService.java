package top.sunjiubo.springboot.Nsblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.sunjiubo.springboot.Nsblog.model.Blog;
import top.sunjiubo.springboot.Nsblog.model.User;

public interface BlogService {
    /**
     * 保存博客
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除博客
     * @param id
     */
    void removeBlog(Long id);

    /**
     * 根据博客获取Blog
     * @param id
     * @return
     */
    Blog getBlogById(Long id);

    /**
     * 根据用户名进行分页查询
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable);

    /**
     * 根据用户名进行分页查询（最热）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleVoteAndSort(User user,String title,Pageable pageable);

    /**
     * 阅读量递增
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    Blog creatComment(Long blogId, String commentContent);

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     */
    void removeComment(Long blogId, Long commentId);
}

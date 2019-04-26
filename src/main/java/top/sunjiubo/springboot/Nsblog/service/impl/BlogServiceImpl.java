package top.sunjiubo.springboot.Nsblog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.sunjiubo.springboot.Nsblog.Repository.BlogRepository;
import top.sunjiubo.springboot.Nsblog.model.Blog;
import top.sunjiubo.springboot.Nsblog.model.Comment;
import top.sunjiubo.springboot.Nsblog.model.User;
import top.sunjiubo.springboot.Nsblog.model.Vote;
import top.sunjiubo.springboot.Nsblog.service.BlogService;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog saveBlog(Blog blog) {
        Blog returnBlog = blogRepository.save(blog);
        return returnBlog;
    }

    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        title = "%"+title+"%";
        String tags = title;
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user,tags,user,pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        System.out.println(blog);
//        blog.setReadSize(blog.getReadSize()+1);
//        this.saveBlog(blog);
    }

    @Override
    public Blog creatComment(Long blogId, String commentContent) {
        Blog originalBlog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user,commentContent);
        originalBlog.addComment(comment);
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeComment(commentId);
        this.saveBlog(originalBlog);
    }

    @Override
    public Blog creatVote(Long blogId) {
        Blog originaBlog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist = originaBlog.addVote(vote);
        if(isExist){
            throw  new IllegalArgumentException("该用户已经点过赞了");
        }
        return this.saveBlog(originaBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeVote(voteId);
        this.saveBlog(originalBlog);
    }
}

package top.sunjiubo.springboot.Nsblog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.sunjiubo.springboot.Nsblog.Repository.BlogRepository;
import top.sunjiubo.springboot.Nsblog.model.*;
import top.sunjiubo.springboot.Nsblog.model.es.EsBlog;
import top.sunjiubo.springboot.Nsblog.service.BlogService;
import top.sunjiubo.springboot.Nsblog.service.EsBlogService;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = (blog.getId()==null);
        EsBlog esBlog = null;

        Blog returnBlog = blogRepository.save(blog);
        if(isNew){
            esBlog = new EsBlog(returnBlog);
        }else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }

        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
        EsBlog esBlog= esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
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
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable){
        Page<Blog> blogs = blogRepository.findByCatalog(catalog,pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        System.out.println(blog);
        blog.setReadSize(blog.getReadSize()+1);
        this.saveBlog(blog);
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

package top.sunjiubo.springboot.Nsblog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.sunjiubo.springboot.Nsblog.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}

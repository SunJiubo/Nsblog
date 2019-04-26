package top.sunjiubo.springboot.Nsblog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.sunjiubo.springboot.Nsblog.model.Vote;

public interface VoteRepository extends JpaRepository<Vote,Long> {
}

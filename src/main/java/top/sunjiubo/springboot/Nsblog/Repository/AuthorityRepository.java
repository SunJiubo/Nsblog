package top.sunjiubo.springboot.Nsblog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.sunjiubo.springboot.Nsblog.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}

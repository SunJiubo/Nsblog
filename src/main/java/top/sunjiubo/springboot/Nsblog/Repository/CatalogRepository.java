package top.sunjiubo.springboot.Nsblog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.sunjiubo.springboot.Nsblog.model.Catalog;
import top.sunjiubo.springboot.Nsblog.model.User;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog,Long> {

    /**
     * 根据用户查询
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户和姓名查询
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user, String name);
}

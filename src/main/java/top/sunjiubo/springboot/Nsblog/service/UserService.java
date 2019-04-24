package top.sunjiubo.springboot.Nsblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.sunjiubo.springboot.Nsblog.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    /**
     * 保存用户
     * @param user
     * @return
     */
    User saveorUpdateUser(User user);

    /**
     * 注册用户
     * @param user
     * @return
     */
    User registerUser(User user);

    /**
     * 删除用户
     * @param id
     */
    void removeUser(Long id);

    /**
     * 删除列表中的用户
     * @param users
     */
    void removeUsersInBatch(List<User> users);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 获取用户列表
     * @return
     */
    List<User> listUsers();

    /**
     * 根据用户名进行分页模糊查询
     */
    Page<User> listUsersByNameLike(String name, Pageable pageable);
}

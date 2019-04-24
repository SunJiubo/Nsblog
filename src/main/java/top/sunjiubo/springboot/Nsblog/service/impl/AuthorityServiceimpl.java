package top.sunjiubo.springboot.Nsblog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.sunjiubo.springboot.Nsblog.dao.AuthorityRepository;
import top.sunjiubo.springboot.Nsblog.model.Authority;
import top.sunjiubo.springboot.Nsblog.service.AuthorityService;

@Service
public class AuthorityServiceimpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}

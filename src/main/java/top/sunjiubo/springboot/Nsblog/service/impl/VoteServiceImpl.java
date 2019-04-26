package top.sunjiubo.springboot.Nsblog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.sunjiubo.springboot.Nsblog.Repository.VoteRepository;
import top.sunjiubo.springboot.Nsblog.model.Blog;
import top.sunjiubo.springboot.Nsblog.model.Vote;
import top.sunjiubo.springboot.Nsblog.service.BlogService;
import top.sunjiubo.springboot.Nsblog.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}

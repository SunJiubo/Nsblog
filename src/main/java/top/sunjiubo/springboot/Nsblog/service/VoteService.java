package top.sunjiubo.springboot.Nsblog.service;

import top.sunjiubo.springboot.Nsblog.model.Vote;

public interface VoteService {

    /**
     * 根据Id获取vote
     * @param id
     * @return
     */
    Vote getVoteById(Long id);

    /**
     * 删除Vote
     * @param id
     */
    void removeVote(Long id);
}

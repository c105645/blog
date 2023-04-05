package com.ness.postservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.CommentVotes;

@Repository
public interface CommentVotesRepository extends JpaRepository<CommentVotes, Long>{

}

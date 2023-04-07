package com.ness.postservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.Comment;
import com.ness.postservice.entities.CommentVotes;

@Repository
public interface CommentVotesRepository extends JpaRepository<CommentVotes, Long>{

	@Query("select v from CommentVotes v where v.comment= ?1 and v.createdBy=?2")
	Optional<CommentVotes> findIfUserHasAlreadyVoted(Comment comment, String user);
}

package com.ness.postservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.Post;
import com.ness.postservice.entities.PostVotes;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Long> {
	
	@Query("select v from PostVotes v where v.post= ?1 and v.createdBy=?2")
	Optional<PostVotes> findIfUserHasAlreadyVoted(Post post, String user);

	@Query("select v from PostVotes v where v.post= ?1")
	List<PostVotes> findAllByPost(Post post);
	
	
	

}

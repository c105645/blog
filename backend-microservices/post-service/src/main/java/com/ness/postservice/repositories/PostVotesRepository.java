package com.ness.postservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.PostVotes;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Long> {

}

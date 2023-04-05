package com.ness.postservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.PostDetails;

@Repository
public interface PostDetailsRepository extends JpaRepository<PostDetails, Long>{

}

package com.ness.userprofileservice.repositories;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ness.userprofileservice.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	Optional<Category> findByName(String name);
	
	@Query("select c from Category c where lower(c.name) LIKE lower(concat('%', concat(?1, '%'))) or lower(c.description) LIKE lower(concat('%', concat(?1, '%')))")
	Slice<Category> findAllBySearchString(String searchString, Pageable firstPage);

}

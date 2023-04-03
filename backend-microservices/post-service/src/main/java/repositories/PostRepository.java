package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}

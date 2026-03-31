package com.social.application.Repositories;
import com.social.application.Models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LikeRepository extends JpaRepository<Like, Long>{
    long countByPost_Id(long postId);
    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);
}

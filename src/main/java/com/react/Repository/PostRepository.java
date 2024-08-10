package com.react.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.react.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

	@Query("SELECT p FROM Post p WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
	public List<Post> findPostsByUsersIdsDateDesc(List<Integer> userIds);
	
	public List<Post> findByUserId(Integer userId);
}

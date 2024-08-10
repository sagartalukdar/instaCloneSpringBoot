package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

}

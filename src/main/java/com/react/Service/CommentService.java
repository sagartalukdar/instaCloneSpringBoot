package com.react.Service;

import com.react.Exception.CommentException;
import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.model.Comment;
import com.react.model.User;

public interface CommentService {
	
	public Comment findCommentById(Integer commentId) throws CommentException;

	public Comment createComment(Comment comment,Integer userId,Integer postId) throws PostException, UserException;
	
	public Comment likeComment(User user,Integer commentId) throws CommentException;
	
	public Comment unlikeComment(User user,Integer commentId) throws CommentException;
}

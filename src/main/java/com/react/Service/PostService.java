package com.react.Service;

import java.util.List;

import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.model.Post;

public interface PostService {

	public Post findPostById(Integer postId) throws PostException;
	
	public Post createPost(Post post,Integer userId) throws UserException;
	
	public List<Post> findPostByUserIds(List<Integer> ids) throws PostException;
	
	public List<Post> findAllPostByUserId(Integer userId);
	
	public Post likePost(Integer userId,Integer postId) throws UserException, PostException;
	
	public Post unlikePost(Integer userId,Integer postId) throws UserException, PostException;
	
	public String savePost(Integer userId,Integer postId) throws UserException, PostException;
	
	public String unsavePost(Integer userId,Integer postId) throws UserException, PostException;
	
}

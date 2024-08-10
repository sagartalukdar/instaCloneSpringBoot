package com.react.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Dto.UserDto;
import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.Repository.PostRepository;
import com.react.Repository.UserRepository;
import com.react.model.Post;
import com.react.model.User;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Post findPostById(Integer postId) throws PostException {
		Optional<Post> opt=postRepository.findById(postId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new PostException("post not found with id: "+postId);
	}
	
	@Override
	public Post createPost(Post post, Integer userId) throws UserException {
		User user=userService.findUserById(userId);
	    
		UserDto userDto=new UserDto();
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setUsername(user.getUsername());
		
		Post createdPost=new Post();
		createdPost.setUser(userDto);
		createdPost.setCaption(post.getCaption());
		createdPost.setLocation(post.getLocation());
		createdPost.setCreatedAt(LocalDateTime.now());
		createdPost.setImage(post.getImage());
		
		return postRepository.save(createdPost);
	}

	@Override
	public List<Post> findPostByUserIds(List<Integer> ids) throws PostException {
		List<Post> userPosts=postRepository.findPostsByUsersIdsDateDesc(ids);
		if(userPosts.size()==0) {
			throw new PostException("no post available in any of following users");
		}
		return userPosts;
	}

	@Override
	public List<Post> findAllPostByUserId(Integer userId) {
		return postRepository.findByUserId(userId);
	}

	@Override
	public Post likePost(Integer userId, Integer postId) throws UserException, PostException {
		User user=userService.findUserById(userId);
		Post likePost=findPostById(postId);
		
		UserDto userDto=new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setUsername(user.getUsername());
		
		likePost.getLikedByUsers().add(userDto);
		return postRepository.save(likePost);		
		
	}


	@Override
	public Post unlikePost(Integer userId, Integer postId) throws UserException, PostException {
		User reqUser=userService.findUserById(userId);
		Post unlikePost=findPostById(postId);
		
		UserDto userDto=new UserDto();
		userDto.setId(reqUser.getId());
		userDto.setEmail(reqUser.getEmail());
		userDto.setImage(reqUser.getImage());
		userDto.setName(reqUser.getName());
		userDto.setUsername(reqUser.getUsername());
		
		unlikePost.getLikedByUsers().remove(userDto);
		return postRepository.save(unlikePost);
	}

	@Override
	public String savePost(Integer userId, Integer postId) throws UserException, PostException {
		User user=userService.findUserById(userId);
		Post post=findPostById(postId);
		
		if(!user.getSavedPosts().contains(post)) {
			user.getSavedPosts().add(post);
			userRepository.save(user);
			return "post saved";
		}
		return "error saving post";
	}

	@Override
	public String unsavePost(Integer userId, Integer postId) throws UserException, PostException {
		User user=userService.findUserById(userId);
		Post post=findPostById(postId);
		
		if(user.getSavedPosts().contains(post)) {
			user.getSavedPosts().remove(post);
			userRepository.save(user);
			return "post unsaved";
		}
		return "error unsaving post";
	}


}

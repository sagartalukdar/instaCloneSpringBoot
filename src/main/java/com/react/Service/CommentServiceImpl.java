package com.react.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Dto.UserDto;
import com.react.Exception.CommentException;
import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.Repository.CommentRepository;
import com.react.Repository.PostRepository;
import com.react.model.Comment;
import com.react.model.Post;
import com.react.model.User;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Override
	public Comment findCommentById(Integer commentId) throws CommentException {
		Optional<Comment>opt=commentRepository.findById(commentId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new CommentException("comment not found with: "+commentId);
	}

	@Override
	public Comment createComment(Comment comment, Integer userId, Integer postId) throws PostException, UserException {
		User user=userService.findUserById(userId);
		Post post=postService.findPostById(postId);
		
		Comment createdComment=new Comment();
		createdComment.setContent(comment.getContent());
		createdComment.setCreatedAt(LocalDateTime.now());
		
		UserDto userDto =new UserDto();
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setUsername(user.getUsername());
		
		createdComment.setUser(userDto);
		Comment savedComment= commentRepository.save(createdComment);
		
		post.getComments().add(savedComment);
		postRepository.save(post);
		return savedComment;		
	}

	@Override
	public Comment likeComment(User user, Integer commentId) throws CommentException{
		Comment comment=findCommentById(commentId);
		
		UserDto userDto=new UserDto();
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setUsername(user.getUsername());
		
		comment.getLikedByUsers().add(userDto);
		return commentRepository.save(comment);
	}

	@Override
	public Comment unlikeComment(User user, Integer commentId) throws CommentException {
		Comment comment=findCommentById(commentId);
		
		UserDto userDto=new UserDto();
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setUsername(user.getUsername());
		
		comment.getLikedByUsers().remove(userDto);
		return commentRepository.save(comment);
	}

}

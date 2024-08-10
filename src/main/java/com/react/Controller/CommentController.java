package com.react.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.CommentException;
import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.Service.CommentService;
import com.react.Service.UserService;
import com.react.model.Comment;
import com.react.model.User;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;

	@PostMapping("/create/{postId}")
	public ResponseEntity<Comment> createComment(@RequestHeader("Authorization")String jwt,@RequestBody Comment comment,@PathVariable("postId")Integer postId) throws UserException, PostException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<Comment>(commentService.createComment(comment, reqUser.getId(), postId),HttpStatus.CREATED);
	}
	
	
	@PutMapping("/like/{commentId}")
	public ResponseEntity<Comment> likeComment(@RequestHeader("Authorization")String jwt,@PathVariable("commentId")Integer commentId) throws UserException, CommentException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<Comment>(commentService.likeComment(reqUser, commentId),HttpStatus.OK);
	}
	
	@PutMapping("/unlike/{commentId}")
	public ResponseEntity<Comment> unlikeComment(@RequestHeader("Authorization")String jwt,@PathVariable("commentId")Integer commentId) throws UserException, CommentException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<Comment>(commentService.unlikeComment(reqUser, commentId),HttpStatus.OK);
	}
}

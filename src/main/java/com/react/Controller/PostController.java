package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.Service.PostService;
import com.react.Service.UserService;
import com.react.model.Post;
import com.react.model.User;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;

	@PostMapping("/create")
	public ResponseEntity<Post> createPost(@RequestHeader("Authorization")String jwt,@RequestBody Post post) throws UserException{
		User reqUser=userService.findUserByJwt(jwt);
		Post createdPost=postService.createPost(post, reqUser.getId());
		return new ResponseEntity<Post>(createdPost,HttpStatus.CREATED);
	}
	
	@GetMapping("/following/{idList}")
	public ResponseEntity<List<Post>> findHomePosts(@PathVariable("idList")List<Integer> ids) throws PostException{
		return new ResponseEntity<List<Post>>(postService.findPostByUserIds(ids),HttpStatus.OK);
	}
	
	@GetMapping("/userPosts/{userId}")
	public ResponseEntity<List<Post>> findAllPostByUserId(@PathVariable("userId")Integer userId){
		return new ResponseEntity<List<Post>>(postService.findAllPostByUserId(userId),HttpStatus.OK);
	}
	
	@PutMapping("/like/{postId}")
	public ResponseEntity<Post> likePost(@RequestHeader("Authorization")String jwt,@PathVariable("postId")Integer postId) throws UserException, PostException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<Post>(postService.likePost(reqUser.getId(), postId),HttpStatus.OK);
	}
	
	@PutMapping("/unlike/{postId}")
	public ResponseEntity<Post> unlikePost(@RequestHeader("Authorization")String jwt,@PathVariable("postId")Integer postId) throws UserException, PostException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<Post>(postService.unlikePost(reqUser.getId(), postId),HttpStatus.OK);
	}
	
	@PutMapping("/save/{postId}")
	public ResponseEntity<String> savePost(@RequestHeader("Authorization")String jwt,@PathVariable("postId")Integer postId) throws UserException, PostException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<String>(postService.savePost(reqUser.getId(), postId),HttpStatus.OK);
	}
	
	@PutMapping("/unsave/{postId}")
	public ResponseEntity<String> unSavePost(@RequestHeader("Authorization")String jwt,@PathVariable("postId")Integer postId) throws UserException, PostException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<String>(postService.unsavePost(reqUser.getId(), postId),HttpStatus.OK);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<Post> findPostByPostId(@PathVariable("postId")Integer postId) throws PostException{
		return new ResponseEntity<Post>(postService.findPostById(postId),HttpStatus.OK);
	}
}

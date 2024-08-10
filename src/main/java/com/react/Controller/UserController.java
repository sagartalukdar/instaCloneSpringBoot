package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.Response.MessageResponse;
import com.react.Service.UserService;
import com.react.model.Post;
import com.react.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/req")
	public ResponseEntity<User> getUserByJwt(@RequestHeader("Authorization")String jwt) throws UserException{
		return new ResponseEntity<User>(userService.findUserByJwt(jwt),HttpStatus.OK);
	}
	
	@PutMapping("/follow/{followUserId}")
	public ResponseEntity<MessageResponse> followUser(@RequestHeader("Authorization")String jwt,@PathVariable("followUserId")int followUserId) throws UserException{
		User reqUser=userService.findUserByJwt(jwt);
		User followUser=userService.findUserById(followUserId);
		
		String message=userService.followUser(reqUser.getId(), followUser.getId());
		return new ResponseEntity<MessageResponse>(new MessageResponse(message),HttpStatus.OK);
	}
	
	@PutMapping("/unfollow/{unFollowUserId}")
	public ResponseEntity<MessageResponse> unFollowUser(@RequestHeader("Authorization")String jwt,@PathVariable("unFollowUserId")int unFollowUserId) throws UserException{
		User reqUser=userService.findUserByJwt(jwt);
		User unFollowUser=userService.findUserById(unFollowUserId);
		
		String message=userService.unFollowUser(reqUser.getId(), unFollowUser.getId());
		return new ResponseEntity<MessageResponse>(new MessageResponse(message),HttpStatus.OK);
	}
	
	@PutMapping("/account/edit")
	public ResponseEntity<User> updateUser(@RequestHeader("Authorization")String jwt,@RequestBody User user) throws UserException{
		User reqUser=userService.findUserByJwt(jwt);
		return new ResponseEntity<User>(userService.updateUser(reqUser, user),HttpStatus.OK);
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<User> findUserByUsername(@PathVariable("username")String username)throws UserException{
		return new ResponseEntity<User>(userService.findByUsername(username),HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUsers(@RequestParam("q") String query) throws UserException{
      return new ResponseEntity<List<User>>(userService.searchUsers(query),HttpStatus.OK);
	}
	
	
}

package com.react.Service;

import java.util.List;

import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.model.Post;
import com.react.model.User;

public interface UserService {

	public User resgisterUser(User user) throws UserException;
	
	public User findUserById(Integer userId) throws UserException;
	
	public User findByEmail(String email) throws UserException;
	
	public User findByUsername(String username) throws UserException;
	
	public User findUserByJwt(String jwt) throws UserException;
	
	public String followUser(int reqUserId,int followUserId) throws UserException;
	
	public String unFollowUser(int reqUserId,int unFollowUserId) throws UserException;
	
	public User updateUser(User reqUser,User updatedUser) throws UserException;
	
	public User findUserByUsername(String username) throws UserException;
	
	public List<User> searchUsers(String query)throws UserException;

}

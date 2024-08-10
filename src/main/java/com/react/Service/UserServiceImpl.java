package com.react.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.react.Config.SecurityContext;
import com.react.Dto.UserDto;
import com.react.Exception.PostException;
import com.react.Exception.UserException;
import com.react.Repository.UserRepository;
import com.react.model.Post;
import com.react.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	
	@Override
	public User resgisterUser(User user) throws UserException {
		if(user.getEmail()==null || user.getPassword()==null
	      || user.getUsername()==null || user.getName()==null			
				) {
			throw new UserException("email,password,name,username cannot be empty. ");
		}
        Optional<User> isEmailExist=userRepository.findByEmail(user.getEmail());
        if(isEmailExist.isPresent()) {
        	throw new UserException("email is already used in another account");
        }
		Optional<User> isUsernameExist=userRepository.findByUsername(user.getUsername());
		if(isUsernameExist.isPresent()) {
			throw new UserException("username is already defined with another account");
		}
		User newUser=new User();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setName(user.getName());
		newUser.setUsername(user.getUsername());
		return userRepository.save(newUser);
	}
	
	public User findUserById(Integer userId) throws UserException {
		Optional<User> opt=userRepository.findById(userId);
		if(opt.isEmpty()) {
			throw new UserException("user not found with id: "+userId);
		}
		return opt.get();
	}

	@Override
	public User findByEmail(String email) throws UserException {
		Optional<User> opt=userRepository.findByEmail(email);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("user not found with email: "+email);
	}

	@Override
	public User findByUsername(String username) throws UserException {
		Optional<User> opt=userRepository.findByUsername(username);
		if(opt.isEmpty()) {
			throw new UserException("user not found with username: "+username);
		}
		return opt.get();		
	}

	@Override
	public User findUserByJwt(String jwt) throws UserException {
	    jwt=jwt.substring(7);
	    System.out.println(jwt);
	    Claims claims=Jwts.parserBuilder()
	    		       .setSigningKey(SecurityContext.JWT_KEY.getBytes())
	    		       .build()
	    		       .parseClaimsJws(jwt)
	    		       .getBody();
	    String email=(String) claims.get("username");
	    return findByEmail(email);
	}

	@Override
	public String followUser(int reqUserId, int followUserId) throws UserException {
		User reqUser=findUserById(reqUserId);
		User followUser=findUserById(followUserId);
		
		UserDto follower=new UserDto();
		follower.setId(reqUser.getId());
		follower.setEmail(reqUser.getEmail());
		follower.setImage(reqUser.getImage());
		follower.setName(reqUser.getName());
		follower.setUsername(reqUser.getUsername());
		
		followUser.getFollower().add(follower);
		
		UserDto following=new UserDto();
		following.setId(followUser.getId());
		following.setEmail(followUser.getEmail());
		following.setImage(followUser.getImage());
		following.setName(followUser.getName());
		following.setUsername(followUser.getUsername());
		
		reqUser.getFollowing().add(following);
		
		userRepository.save(followUser);
		userRepository.save(reqUser);
		
		return "you are following "+followUser.getUsername();
	}

	@Override
	public String unFollowUser(int reqUserId, int unFollowUserId) throws UserException {
        User requUser=findUserById(reqUserId);
        User unFollowUser=findUserById(unFollowUserId);
        
        UserDto follower=new UserDto();
        follower.setId(requUser.getId());
        follower.setEmail(requUser.getEmail());
        follower.setImage(requUser.getImage());
        follower.setName(requUser.getName());
        follower.setUsername(requUser.getUsername());
        
        unFollowUser.getFollower().remove(follower);
        
        UserDto following=new UserDto();
        following.setId(unFollowUser.getId());
        following.setEmail(unFollowUser.getEmail());
        following.setImage(unFollowUser.getImage());
        following.setName(unFollowUser.getName());
        following.setUsername(unFollowUser.getUsername());
        
        requUser.getFollowing().remove(following);
        
        userRepository.save(requUser);
        userRepository.save(unFollowUser);
        
        return "you are unFollow "+unFollowUser.getUsername();
	}

	@Override
	public User updateUser(User reqUser, User updatedUser) throws UserException {
		if(updatedUser.getEmail()!=null) {
			reqUser.setEmail(updatedUser.getEmail());
		}
		if(updatedUser.getBio()!=null) {
			reqUser.setBio(updatedUser.getBio());
		}
		if(updatedUser.getName()!=null) {
			reqUser.setName(updatedUser.getName());
		}
		if(updatedUser.getUsername()!=null) {
			reqUser.setUsername(updatedUser.getUsername());
		}
		if(updatedUser.getWebsite()!=null) {
			reqUser.setWebsite(updatedUser.getWebsite());
		}
		if(updatedUser.getMobile()!=null) {
			reqUser.setMobile(updatedUser.getMobile());
		}
		if(updatedUser.getGender()!=null) {
			reqUser.setGender(updatedUser.getGender());
		}
		if(updatedUser.getImage()!=null) {
			reqUser.setImage(updatedUser.getImage());
		}
		if(updatedUser.getId()==reqUser.getId()) {
		 return userRepository.save(reqUser);
		}
		throw new UserException("you can't update others profile");
	}

	@Override
	public User findUserByUsername(String username) throws UserException {
		Optional<User> opt=userRepository.findByUsername(username);
		if(opt.isEmpty()) {
			throw new UserException("user not found with username: "+username);
		}
		return opt.get();
	}

	@Override
	public List<User> searchUsers(String query)throws UserException {
		List<User> users=userRepository.searchUsers(query);
		if(users.size()==0) {
			throw new UserException("user not found with: "+query);
		}
		return users;
	}


}

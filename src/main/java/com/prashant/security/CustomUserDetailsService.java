package com.prashant.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prashant.entity.Role;
import com.prashant.entity.User;
import com.prashant.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
	User user=	userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).
		orElseThrow(()->new UsernameNotFoundException("User not found with username pr Email:"+usernameOrEmail));
	
		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	
private Collection<? extends GrantedAuthority>mapRolesToAuthorities(Set<Role>roles){
	return roles.stream().map(role ->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
}
}

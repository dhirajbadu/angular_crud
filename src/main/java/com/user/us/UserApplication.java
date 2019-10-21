package com.user.us;

import com.user.us.service.userInfo.UserInfo;
import com.user.us.service.userInfo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@ComponentScan({"com.user.us.service" , "com.user.us.controller"})
@SpringBootApplication
public class UserApplication {

	@Autowired
	private UserInfoRepository userInfoRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);

	}

	@PostConstruct
	private void createUser(){
		saveUser("Dhiraj" , "12345");
		saveUser("Benis" , "12345");
		saveUser("Kailas" , "12345");
	}

	private void saveUser(String name , String password){
		if (userInfoRepository.findByUsername(name) == null){
			userInfoRepository.save(new UserInfo(name.toLowerCase() , password));
		}
	}

}

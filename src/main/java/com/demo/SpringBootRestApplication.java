package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping(value = "/user")
public class SpringBootRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public @ResponseBody User create_user(@RequestBody User user){
		return userRepository.save(user);
	}

	@RequestMapping(value = "/get",method = RequestMethod.GET)
	public @ResponseBody User get_user(@RequestParam(value="id") long id){
		return userRepository.findOne(id);
	}

	@RequestMapping(value = "/delete",method = RequestMethod.DELETE)
	public @ResponseBody String delete_user(@RequestParam(value="id") long id){
		userRepository.delete(id);
		return "User Deleted.";
	}

	@RequestMapping(value = "/error")
	public String error() {
		return "Error handling";
	}
}

package com.user.us.controller;

import com.user.us.service.ResponseDto;
import com.user.us.service.userInfo.UserInfo;
import com.user.us.service.userInfo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserInfoRestController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping("/list")
    public List<UserInfo> list(){
        return userInfoRepository.findAll();
    }

    @PostMapping(value = "/delete" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> delete(@RequestBody UserInfo userInfo){
        ResponseDto responseDto = new ResponseDto();
        try {
            userInfoRepository.deleteById(userInfo.getId());
            responseDto.setMsg("user deleted successfully");
            responseDto.setDetails(userInfoRepository.findAll());
        }catch (Exception e){
            e.printStackTrace();
            responseDto.setMsg("unable to delete the user");
            responseDto.setDetails(userInfoRepository.findAll());
        }

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/edit" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> edit(@RequestBody UserInfo userInfo){
        ResponseDto responseDto = new ResponseDto();
        try {
            responseDto.setDetails(userInfoRepository.findById(userInfo.getId()));
            responseDto.setStatus("200");
        }catch (Exception e){
            e.printStackTrace();
            responseDto.setMsg("unable to delete the user");
            responseDto.setStatus("500");
        }

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/update" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> update(@RequestBody UserInfo currentUserInfo){
        ResponseDto responseDto = new ResponseDto();
        try {
            UserInfo previousUser = userInfoRepository.findById(currentUserInfo.getId()).get();
            previousUser.setUsername(currentUserInfo.getUsername());
            previousUser.setPassword(currentUserInfo.getPassword());
            UserInfo updatedUserInfo = userInfoRepository.save(previousUser);
            responseDto.setMsg("user successfully upadated");
            responseDto.setDetails(userInfoRepository.findAll());
            responseDto.setInfo(updatedUserInfo);
            responseDto.setStatus("200");
            return new ResponseEntity<ResponseDto>(responseDto , HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            responseDto.setMsg("internal server error");
            responseDto.setStatus("500");
            responseDto.setDetails(userInfoRepository.findAll());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/save" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> save(@RequestBody UserInfo userInfo){
        ResponseDto responseDto = new ResponseDto();
        try {
            UserInfo savedUser = userInfoRepository.save(userInfo);
            responseDto.setMsg("user successfully saved");
            responseDto.setDetails(savedUser);
            responseDto.setStatus("200");
            return new ResponseEntity<ResponseDto>(responseDto , HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            responseDto.setMsg("internal server error");
            responseDto.setStatus("500");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }
}

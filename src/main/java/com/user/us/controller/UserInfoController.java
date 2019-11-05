package com.user.us.controller;

import com.user.us.service.ImageUtls;
import com.user.us.service.report.ReportService;
import com.user.us.service.userInfo.UserInfo;
import com.user.us.service.userInfo.UserInfoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    public String index() {
        return "Wellcome to User module";
    }

    @GetMapping("/list")
    public List<UserInfo> list() {

        return userInfoRepository.findAll();
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String filePath = "/home/dbadu/Pictures/upload/" + fileName;

        ImageUtls.upload(file, file.getOriginalFilename(), filePath);

        return "redirect:/";
    }

}

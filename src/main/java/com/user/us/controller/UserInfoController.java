package com.user.us.controller;

import com.user.us.service.report.ReportService;
import com.user.us.service.userInfo.UserInfo;
import com.user.us.service.userInfo.UserInfoReport;
import com.user.us.service.userInfo.UserInfoRepository;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    public String index(){
        return "Wellcome to User module";
    }

    @GetMapping("/list")
    public List<UserInfo> list(){

        return userInfoRepository.findAll();
    }

    @GetMapping(value = "/report.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public HttpEntity<byte[]> getEmployeeReportPdf(final HttpServletResponse response) throws JRException, IOException, ClassNotFoundException {
        final UserInfoReport report = new UserInfoReport(userInfoRepository.findAll());
        final byte[] data = reportService.getReportPdf(report.getReport());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=employeeReport.pdf");
        header.setContentLength(data.length);

        return new HttpEntity<byte[]>(data, header);
    }


    @GetMapping(value = "/report.xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @ResponseBody
    public HttpEntity<byte[]> getEmployeeReportXlsx(final HttpServletResponse response) throws JRException, IOException, ClassNotFoundException {
        final UserInfoReport report = new UserInfoReport(userInfoRepository.findAll());
        final byte[] data = reportService.getReportXlsx(report.getReport());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=employeeReport.xlsx");
        header.setContentLength(data.length);

        return new HttpEntity<byte[]>(data, header);
    }

}

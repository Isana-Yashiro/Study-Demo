package com.qhit.controller;



import com.qhit.service.ExamPaperInfoService;
import com.qhit.service.StudentInfoService;
import com.qhit.service.SubjectInfoService;
import com.qhit.service.TeacherInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("admin")
public class AdminHomeHeadHandler {

    @Autowired
    private ExamPaperInfoService examPaperInfoService ;
    @Autowired
    private SubjectInfoService subjectInfoService;
    @Autowired
    private TeacherInfoService teacherInfoService;
    @Autowired
    private StudentInfoService studentInfoService;


    private Logger logger = Logger.getLogger(AdminHomeHeadHandler.class);


    @RequestMapping("/homeInfo")
    public void homeInfo(HttpServletResponse response) throws IOException {
        logger.info("加载后台首页相关数据");

        int examPaperTotal = examPaperInfoService.getExamPaperTotal();
        int subjectTotal = subjectInfoService.getSubjectTotal();
        int teacherTotal = teacherInfoService.getTeacherTotal();
        int studentTotal = studentInfoService.getStudentTotal();

        String json = "{\"examPaperTotal\":"+examPaperTotal+", " +
                "\"subjectTotal\":"+subjectTotal+", " +
                "\"teacherTotal\":"+teacherTotal+", " +
                "\"studentTotal\":"+studentTotal+"}";

        response.getWriter().print(json);
    }
}

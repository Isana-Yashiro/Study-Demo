package com.qhit.controller;


import com.google.gson.Gson;
import com.qhit.service.ClassInfoService;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/StudentExam")
public class StudentExamHandler {
    @Autowired
    ClassInfoService classInfoService;
    @Autowired
    private Gson gson;

    @RequestMapping("examCount")
    public void getStudentExamCount(Integer teacherid,HttpServletResponse response) throws IOException {
        if (teacherid == null) {
            response.getWriter().print("teacherid-null");
        }else {
           classInfoService.getClassbyTeacherId(teacherid);

        }

    }

}

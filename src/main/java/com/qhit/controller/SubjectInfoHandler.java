package com.qhit.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import com.qhit.pojo.CourseInfo;
import com.qhit.pojo.ExamPaperInfo;
import com.qhit.pojo.GradeInfo;
import com.qhit.pojo.SubjectInfo;
import com.qhit.service.*;
import com.qhit.tools.SubjectExcel;
import com.qhit.utils.MyUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("subject")
public class SubjectInfoHandler {

    @Autowired
    private SubjectInfoService subjectInfoService;
    @Autowired
    private CourseInfoService courseInfoService;
    @Autowired
    private GradeInfoService gradeInfoService;
    @Autowired
    private ExamPaperInfoService examPaperInfoService;
    @Autowired
    private ExamSubjectMiddleService examSubjectMiddleService;
    @Autowired
    private SubjectInfo subject;
    @Autowired
    private CourseInfo course;
    @Autowired
    private GradeInfo grade;
    @Autowired
    private ExamPaperInfo examPaper;

    private Logger logger = Logger.getLogger(SubjectInfoHandler.class);

    /*@RequestMapping("upload")
    public void upload(HttpServletRequest request, MultipartFile inputfile) {
        //上传文件处理
        String originalFilename = inputfile.getOriginalFilename();
        //获取本地路径
        Date date = new Date();
        String path = request.getServletContext().getRealPath("/") + "/upload/" +
                DateUtil.format(date, "yyyy/MM") + originalFilename;
        File file = new File(path);
        //文件从服务器内存传到磁盘
        try {
            inputfile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件从磁盘传到数据库
        List<SubjectInfo> byExcel = SubjectExcel.getByExcel(file);
        int i = subjectInfoService.addAll(byExcel);
    }
*/


    @RequestMapping("subjectList")
    public ModelAndView subjectList(Model model, Integer page, Map map) {
        int currPage = 1;
        if (page != null) {
            currPage = page;
        }
        int[] ints = PageUtil.transToStartEnd(currPage, 12);
        Map m = subjectInfoService.getAllWithPage(ints);
        m.put("pageNow", currPage);

        List<SubjectInfo> subjectList = subjectInfoService.getSubjectList(map);
        model.addAttribute("subjectList", m);
        return new ModelAndView("admin/subjects");
    }

    /**
     * 预添加试题信息
     *
     * @return
     */
    @RequestMapping("/preAddSubject")
    public ModelAndView preAddSubject() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/admin/subject-test");
        /** 获取年级集合 */
        List<GradeInfo> grades = gradeInfoService.getGrade();
        model.addObject("grades", grades);
        /** 获取科目集合 */
        List<CourseInfo> courses = courseInfoService.getCourseList();
        model.addObject("courses", courses);
        return model;
    }
    @RequestMapping("/addSubject")
    public @ResponseBody Map addSubject(Model model,SubjectInfo subjectInfo){
        HashMap map = new HashMap();
        int result=subjectInfoService.insertSubject(subjectInfo);
        if(result==0){
            map.put("state",1);
            map.put("msg","插入失败!");
        }else {
            map.put("state",0);
            map.put("msg","插入成功!");
        }
        return map;
    }

    @RequestMapping("/getSubjectById")
    public ModelAndView getSubjectById(Model model,Integer subjectId){
        SubjectInfo subjectInfo=subjectInfoService.getSubjectById(subjectId);
        List<GradeInfo> grades = gradeInfoService.getGrade();
        List<CourseInfo> courses=courseInfoService.getCourseList();
        model.addAttribute("subject",subjectInfo);
        model.addAttribute("courses",courses);
        model.addAttribute("grades", grades);
        return new ModelAndView("admin/subject-test1");
    }

    @RequestMapping("/updateSubject")
    public @ResponseBody Map updateSubject(Model model,SubjectInfo subjectInfo){
        HashMap map = new HashMap();
        int result=subjectInfoService.updateSubject(subjectInfo);
        if (result == 0) {
            map.put("state", 1);
            map.put("msg", "修改失败!");
        } else {
            map.put("state", 0);
            map.put("msg", "修改成功!");
        }
        return map;
    }

    @RequestMapping("/deleteSubject")
    public @ResponseBody
    Map deleteSubject(Model model, int subjectId){
        HashMap hashMap = new HashMap();
        int num = subjectInfoService.deleteSubject(subjectId);
        if(num==0){
            hashMap.put("state",1);
            hashMap.put("msg","删除失败！");
        }else{
            hashMap.put("state",0);
            hashMap.put("msg","删除成功！");
        }
        return hashMap;
    }

    /**
     * 将数据传递到上传页面
     *
     * @param mo
     * @return
     */
    @RequestMapping("preUploadSubject")
    public ModelAndView preUploadSubject(ModelAndView mo) {
//        获取所有试卷
        mo.addObject("examPapers", examPaperInfoService.getExamPaper());
//        获取所有年级
        mo.addObject("grades", gradeInfoService.getGrade());
//        获取所有科目
        mo.addObject("courses", courseInfoService.getCourseList());
        mo.setViewName("admin/importSubject");
        return mo;
    }


    @RequestMapping("uploadSubjectInfo")
    public String uploadSubjectInfo(MultipartFile inputFile, HttpServletRequest request,
                                    Integer division, Integer courseId,
                                    Integer gradeId, Integer examPaperId,
                                    Integer importOption,
                                    ExamPaperInfo examPaperInfo) throws IOException {
        HashMap<String, Integer> hashMap1 = new HashMap<>();
        InputStream inputStream = inputFile.getInputStream();
        hashMap1.put("status", 0);
        System.out.println("导入的类型为：" + importOption + "@#￥%……&");
        /*装取从excel表中获取的试题信息*/
        List<SubjectInfo> subjectList = null;
//        装取试题的ID
        ArrayList<Integer> subjectIds = new ArrayList<>();
        /*只导入试题*/
        if (importOption == 0) {
            System.out.println("-------------------------只导入试题-------------------");
            subjectList = MyUtils.upload(inputFile, request, division, courseId, gradeId);
            int i = subjectInfoService.insertManySubjectInfo(subjectList);
            System.out.println("测试只导入试题：：" + i);
            if (i > 0) {
                hashMap1.put("state", 0);
                System.out.println("成功将试题导入到数据库中。");
            }
        } else {
            subjectList = MyUtils.upload(inputFile, request, division, courseId, gradeId);
            System.out.println("@@@@@获取的试题数量：" + subjectList.size() + "@@@@@");
            for (SubjectInfo subjectInfo : subjectList
                    ) {
                /*循环添加试题，然后根据试题的多个条件，在获取刚添加的试题ID*/
                subjectInfoService.insertSubject(subjectInfo);
                /*获取刚添加的试题ID*/
                Integer subjectId = subjectInfo.getSubjectId();
                /*将获取 的试题ID放到试题ID集合中，用于接下来为试卷添加试题*/
                subjectIds.add(subjectId);
            }
            System.out.println("试题ID集合大小：" + subjectList.size() + "@@@@@");
            int totalScore = 0;
            for (SubjectInfo s : subjectList) {
                /*计算添加的试题的总分数*/
                totalScore += s.getSubjectScore();
            }
            for (int i = 0; i < subjectIds.size(); i++) {
                System.out.println("测试要导入的试题ID：" + subjectIds.get(i));
            }
            System.out.println("examPaperId::" + examPaperId);
            System.out.println("试卷：" + examPaperInfo);
            /*判断是将试题加入到已有试卷中，还是将试题加入到新创建试卷中*/
            if (importOption == 2) {
                System.out.println("-------------------------将试题导入到新建试卷-------------------");
                /*将试题加入到新建试卷*/
                examPaperInfo.setDivision(division);
                examPaperInfo.setGradeId(gradeId);
                examPaperInfo.setSubjectNum(subjectIds.size());
                examPaperInfo.setExamPaperScore(totalScore);
                /*将新建的试卷添加到数据库*/
                int i = MyUtils.addSubToExamPaper(examPaperInfo, examPaperInfoService, examSubjectMiddleService, subjectIds);
                if (i > 0) {
                    hashMap1.put("status", 1);
                    System.out.println("成功将试题导入到新建试卷" + examPaperInfo.getExamPaperName() + "之中");
                }
            } else {
                System.out.println("------------------------将试题导入到已有的试卷中-------------------");
                /* 将试题加入到已有的试卷中*/
                System.out.println("试卷编号：" + examPaperId);
                ExamPaperInfo oneExamPaperInfoById = examPaperInfoService.getOneExamPaperInfoById(examPaperId);
                System.out.println("获取的试卷信息；" + oneExamPaperInfoById);
                totalScore += oneExamPaperInfoById.getExamPaperScore();
                int subjectNum = oneExamPaperInfoById.getSubjectNum() + subjectIds.size();
                System.out.println(totalScore + "," + subjectNum);
                /*将试题添加到 试卷中*/
                HashMap hashMap = new HashMap();
                hashMap.put("examPaperId", examPaperId);
                hashMap.put("subList", subjectIds);
                int i = examSubjectMiddleService.isAddESM(hashMap);
                if (i > 0) {
                    int i1 = examPaperInfoService.updateExamPaperSubNumAndScore(examPaperId, subjectNum, totalScore);
                    if (i1 > 0) {
                        hashMap1.put("state", 0);
                        System.out.println("成功将试题加入到已有试卷：" + oneExamPaperInfoById.getExamPaperName() + "之中。");
                    }
                }
            }
        }
        return "redirect:subject/subjectList.action";
    }
}
package com.qhit.utils;

import com.qhit.pojo.ExamPaperInfo;
import com.qhit.pojo.SubjectInfo;

import com.qhit.service.ExamPaperInfoService;
import com.qhit.service.ExamSubjectMiddleService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyUtils {

    /**
     * 判断单元格是否为空
     *
     * @param row
     * @param num
     * @return
     */
    public static String getRowIsNull(Row row, int num) {
        Cell cell = row.getCell(num);
        if (cell == null || cell.equals("")) {
            return "";
        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue();
        }
    }

    /**
     * 读取excel中的试题信息，然后封装到对象中
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<SubjectInfo> getSubjectFromExcel(File file, Integer division, Integer courseId, Integer gradeId) throws IOException {
        ArrayList<SubjectInfo> subjectList = new ArrayList<>();
        SubjectInfo subjectInfo = null;
        try {
            InputStream is = new FileInputStream(file);
            XSSFWorkbook hwb = new XSSFWorkbook(is);
            XSSFSheet sheetAt = hwb.getSheetAt(0);
            for (int i = 0; i <= sheetAt.getLastRowNum(); i++) {
                XSSFRow row = sheetAt.getRow(i);
                if (row.getRowNum() == 0) {
                    continue;
                }
                subjectInfo = new SubjectInfo();
                subjectInfo.setDivision(division);
                subjectInfo.setGradeId(gradeId);
                subjectInfo.setCourseId(courseId);
                subjectInfo.setSubjectName(MyUtils.getRowIsNull(row, 0));
                subjectInfo.setOptionA(MyUtils.getRowIsNull(row, 1));
                subjectInfo.setOptionB(MyUtils.getRowIsNull(row, 2));
                subjectInfo.setOptionC(MyUtils.getRowIsNull(row, 3));
                subjectInfo.setOptionD(MyUtils.getRowIsNull(row, 4));
                subjectInfo.setRightResult(MyUtils.getRowIsNull(row, 5));
                subjectInfo.setSubjectScore(Integer.parseInt(MyUtils.getRowIsNull(row, 6)));
                String subEasy = MyUtils.getRowIsNull(row, 7);
                int subclassnum = 0;
                if (subEasy.equals("单选")) {
                    subclassnum = 0;
                } else if (subEasy.equals("多选")) {
                    subclassnum = 1;
                } else {
                    subclassnum = 2;
                }
                subjectInfo.setSubjectEasy(subclassnum);
                String subclass = MyUtils.getRowIsNull(row, 8);
                int subeasynum = 0;
                if (subclass.equals("简单")) {
                    subeasynum = 0;
                } else if (subclass.equals("普通")) {
                    subeasynum = 1;
                } else {
                    subeasynum = 2;
                }
                subjectInfo.setSubjectType(subeasynum);
                subjectList.add(subjectInfo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return subjectList;
    }


    /**
     * 获取从excle中读取的数据，并将结果返回出去
     *
     * @param inputFile
     * @param request
     * @param division
     * @param courseId
     * @param gradeId
     * @return
     */
    public static List<SubjectInfo> upload(MultipartFile inputFile,
                                           HttpServletRequest request,
                                           Integer division, Integer courseId, Integer gradeId) {
        List<SubjectInfo> subjectList = null;
        String fileName = inputFile.getOriginalFilename();
        System.out.println(division + "," + gradeId + "," + courseId);
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String path = "C:/Users/Lenovo/IdeaProjects/exam_010/src/main/webapp/upload/" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            inputFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*将试题信息从excel表中提取出来，封装到对象中*/
        try {
            subjectList = MyUtils.getSubjectFromExcel(file, division, gradeId, courseId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subjectList;
    }

    /**
     * 创建试卷，并为试卷添加试题
     *
     * @param examPaperInfo
     * @param examPaperInfoService
     * @param examSubjectMiddleService
     * @param subjectIds
     * @return
     */
    public static int addSubToExamPaper(ExamPaperInfo examPaperInfo, ExamPaperInfoService examPaperInfoService,
                                        ExamSubjectMiddleService examSubjectMiddleService, List<Integer> subjectIds) {
        System.out.println("新建试卷：" + examPaperInfo);
        int flag = 0;
        int i = examPaperInfoService.insertExamPaper(examPaperInfo);
        if (i > 0) {
            /*添加试卷成功后，获取新添加试卷的ID*/
            Integer examPaperId = examPaperInfo.getExamPaperId();
            System.out.println("新创建的试题编号：" + examPaperId);
            /*根据添加试卷的ID，为该试卷添加试题*/
            HashMap hashMap = new HashMap();
            hashMap.put("examPaperId", examPaperId);
            hashMap.put("subList", subjectIds);
            int i1 = examSubjectMiddleService.insertManySubToExamPaper(hashMap);
            if (i1 > 0) {
                flag = 1;
                System.out.println("为试卷导入试题成功");
            }
        }
        return flag;
    }
}

    /*public @ResponseBody
    Map importXLS(String file) {
        HashMap hashMap = new HashMap();
        ArrayList<SubjectInfo> subjectInfos = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheetAt = sheets.getSheetAt(0);
            for (Row row:sheetAt
                    ) {
                if(row.getRowNum()==0){
                    continue;
                }
                String subjectName=row.getCell(0).getStringCellValue();
                String optionA=row.getCell(1).getStringCellValue();
                String optionB=row.getCell(2).getStringCellValue();
                String optionC=row.getCell(3).getStringCellValue();
                String optionD=row.getCell(4).getStringCellValue();
                String rightResult=row.getCell(5).getStringCellValue();
                double subjectScore= row.getCell(6).getNumericCellValue();
                String subjectType=row.getCell(7).getStringCellValue();
                String subjectEasy=row.getCell(8).getStringCellValue();
                int subjectTypes=0;
                int subjectEasys=0;
                if(subjectType.equals("单选")){
                    subjectTypes=0;
                }
                if(subjectType.equals("多选")){
                    subjectTypes=1;
                }
                if(subjectType.equals("简答")){
                    subjectTypes=2;
                }
                if (subjectEasy.equals("简单")){
                    subjectEasys=0;
                }
                if (subjectEasy.equals("普通")){
                    subjectEasys=1;
                }
                if (subjectEasy.equals("困难")){
                    subjectEasys=2;
                }
                SubjectInfo subjectInfo = new SubjectInfo();
                subjectInfo.setSubjectName(subjectName);
                subjectInfo.setOptionA(optionA);
                subjectInfo.setOptionB(optionB);
                subjectInfo.setOptionC(optionC);
                subjectInfo.setOptionD(optionD);
                subjectInfo.setRightResult(rightResult);
                subjectInfo.setSubjectScore((int)subjectScore);
                subjectInfo.setSubjectType(subjectTypes);
                subjectInfo.setSubjectEasy(subjectEasys);
                subjectInfos.add(subjectInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return subjectInfos;
        int num = subjectInfoService.insertManySubjectInfo(subjectInfos);
        if(num==0){
            hashMap.put("state",1);
            hashMap.put("msg","导入失败！");
        }else{
            hashMap.put("state",0);
            hashMap.put("msg","导入成功！");
        }
        return hashMap;
    }*/
package com.qhit.controller;

import com.qhit.pojo.SubjectInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/subjectUpload")
public class SubjectInfoController {
    private List<SubjectInfo> importXLS() {
        ArrayList<SubjectInfo> subjectInfos = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream("C:/Users/Lenovo/IdeaProjects/exam_010/src/main/webapp/upload/sample.xlsx");
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
        return subjectInfos;
    }

    public static void main(String[] args) {
        SubjectInfoController subjectInfoController = new SubjectInfoController();
        subjectInfoController.importXLS();
    }
}

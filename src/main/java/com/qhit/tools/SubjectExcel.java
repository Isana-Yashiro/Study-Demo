package com.qhit.tools;

import com.qhit.pojo.SubjectInfo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectExcel {

    public static List<SubjectInfo> getByExcel(File file){
        ArrayList arrayList = new ArrayList();
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            for (int i = 0; i < sheet.getLastRowNum(); i++){
                XSSFRow row = sheet.getRow(i);
                SubjectInfo subjectInfo = new SubjectInfo();
                subjectInfo.setSubjectName(row.getCell(0).getStringCellValue());
                subjectInfo.setOptionA(row.getCell(1).getStringCellValue());


                arrayList.add(subjectInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}

package com.qhit.util;


import com.qhit.pojo.CourseInfo;
import com.qhit.pojo.GradeInfo;
import com.qhit.pojo.SubjectInfo;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;




public class SubjectImportUtil {

	public static List<SubjectInfo> getByExcle(File file) {
		ArrayList arrayList = new ArrayList();
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheets = xssfWorkbook.getSheetAt(0);
			for (int i = 0; i < sheets.getLastRowNum(); i++) {
				XSSFRow row = sheets.getRow(i);
				SubjectInfo subjectInfo = new SubjectInfo();
				subjectInfo.setSubjectname(row.getCell(0).getStringCellValue());
				subjectInfo.setOptiona(row.getCell(1).getStringCellValue());
				subjectInfo.setOptionb(row.getCell(2).getStringCellValue());
				subjectInfo.setOptionc(row.getCell(3).getStringCellValue());
				subjectInfo.setOptiond(row.getCell(4).getStringCellValue());
				subjectInfo.setRightresult(row.getCell(5).getStringCellValue());
				subjectInfo.setSubjectscore(Integer.parseInt(row.getCell(6).getStringCellValue()));
				subjectInfo.setSubjecttype(Integer.parseInt(row.getCell(7).getStringCellValue()));
				subjectInfo.setSubjecteasy(Integer.parseInt(row.getCell(8).getStringCellValue()));
				arrayList.add(subjectInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayList;
	}
}
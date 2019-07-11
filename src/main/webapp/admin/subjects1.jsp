<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="../common/include.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>试题信息</title>

 	<link href="${path }/css/bootstrap/bootstrap.min.css" rel="stylesheet" />
 	<link rel="stylesheet" type="text/css" href="${path }/js/zeroModal/zeroModal.css" />
</head>
<body>
	<!-- 试卷编号(针对手动添加试题到试卷) -->
	<span style="display: none;" id="examPaperId">${exampaperid }</span>
	<div style="text-align: center;">
		<table class="table table-striped table-hover table-condensed">
			<thead>
				<tr>
					<c:if test="${handAdd != null }">
						<th>
							已选:
							<span id="choosed" style="color: red;">${choosed }</span>
						</th>
					</c:if>
					<th>试题编号</th>
					<th>题目</th>
					<th>选项A</th>
					<th>选项B</th>
					<th>选项C</th>
					<th>选项D</th>
					<th>正确答案</th>
					<th>分值</th>
					<th>试题类型</th>
					<th>难易程度</th>
					<th>所属科目</th>
					<th>所属年级</th>
					<c:if test="${handAdd == null }">
						<th>操作
							&nbsp;
							<button type="button" class="btn btn-xs btn-info" onclick="toSubjectAdd()">添加</button>
						</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty subjects }">
						<c:forEach items="${subjects.result }" var="subject">
							<tr id="tr-${subject.subjectid }">
								<c:if test="${handAdd != null }">
									<td>
										<input type="checkbox" name="chooseSubject" id="${subject.subjectid }" />
									</td>
								</c:if>
								<td>${subject.subjectid }</td>
								<td>
									<c:if test="${fn:length(subject.subjectname) > 8 }">
										${fn:substring(subject.subjectname, 0, 8) }
										<a href="#" onclick="_iframe(1,'subject/${subject.subjectid }', 'subjects')">[...]</a>
									</c:if>
									<c:if test="${fn:length(subject.subjectname) <= 8 }">
										${subject.subjectname }
									</c:if>
								</td>
								<td>
									<c:if test="${fn:length(subject.optiona) > 5 }"> ${fn:substring(subject.optiona, 0, 5) } <sub><a href="#" onclick="_iframe(1,'subject/${subject.subjectid }', 'subjects')">[...]</a></sub> </c:if>
									<c:if test="${fn:length(subject.optiona) <= 5 }"> ${subject.optiona }</c:if>
								</td>
								<td>
									<c:if test="${fn:length(subject.optionb) > 5 }"> ${fn:substring(subject.optionb, 0, 5) } <sub><a href="#" onclick="_iframe(1,'subject/${subject.subjectid }', 'subjects')">[...]</a></sub> </c:if>
									<c:if test="${fn:length(subject.optionb) <= 5 }"> ${subject.optionb }</c:if>
								</td>
								<td>
									<c:if test="${fn:length(subject.optionc) > 5 }"> ${fn:substring(subject.optionc, 0, 5) } <sub><a href="#" onclick="_iframe(1,'subject/${subject.subjectid }', 'subjects')">[...]</a></sub> </c:if>
									<c:if test="${fn:length(subject.optionc) <= 5 }"> ${subject.optionc }</c:if>
								</td>
								<td>
									<c:if test="${fn:length(subject.optiond) > 5 }"> ${fn:substring(subject.optiond, 0, 5) } <sub><a href="#" onclick="_iframe(1,'subject/${subject.subjectid }', 'subjects')">[...]</a></sub> </c:if>
									<c:if test="${fn:length(subject.optiond) <= 5 }"> ${subject.optiond }</c:if>
								</td>
								<td>${subject.rightresult }</td>
								<td id="subjectScore">${subject.subjectscore }</td>
								<td>
									<c:if test="${subject.subjecttype == 0 }">单选</c:if>
									<c:if test="${subject.subjecttype == 1 }">多选</c:if>
									<c:if test="${subject.subjecttype == 2 }">简答</c:if>
								</td>
								<td>
									<c:if test="${subject.subjecteasy == 0 }">简单</c:if>
									<c:if test="${subject.subjecteasy == 1 }">普通</c:if>
									<c:if test="${subject.subjecteasy == 2 }">困难</c:if>
								</td>
								<td>${subject.course.coursename }</td>
								<td>${subject.grade.gradename }</td>
								<td>
									<c:if test="${handAdd == null }">
										<div class="btn-group">
											<button type="button" class="btn btn-info btn-sm" onclick="updateSubject(${subject.subjectid })">修改</button>
											<button type="button" class="btn btn-danger btn-sm" onclick="delSubject('${subject.subjectid }')">删除</button>
										</div>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</c:when>
				</c:choose>
			</tbody>
		</table>
		<form action="class" method="post">
			<input type="hidden" value="DELETE" name="_method" />
		</form>
		<div>
			<ul class="pagination">
				<li>
					<c:if test="${handAdd != null }">
						<button id="isAddHandle" type="button" class="btn btn-default">添加</button>
					</c:if>
				</li>
				<!-- 
					分页中，需要将 handAdd(是否为手动添加)、examPaperId(试卷编号) 一直传递，以保持查询条件
				 --> 
				
				<li><a href="${path}/examsubjectmiddle/selectSubjectAll.action?page=1&handAdd=${handAdd }&examPaperId=${exampaperid }">首页</a></li>
				<c:if test="${subjects.pageNow-1 > 0 }">
					<li><a href="${path}/examsubjectmiddle/selectSubjectAll.action?page=${subjects.pageNow-1 }&handAdd=${handAdd }&examPaperId=${exampaperid }">上一页</a></li>
				</c:if>
				<c:forEach begin="${subjects.pageNow }" end="${subjects.pageNow+4 }" var="subPage">
					<c:if test="${subPage-5 > 0 }">
						<li><a href="${path}/examsubjectmiddle/selectSubjectAll.action?page=${subPage-5 }&handAdd=${handAdd }&examPaperId=${exampaperid }">${subPage-5 }</a></li>
					</c:if>
				</c:forEach>
				<c:forEach begin="${subjects.pageNow }" end="${subjects.pageNow+5 }" step="1" var="pageNo">
					<!-- 当前页码小于总页数才能显示 -->
					<c:if test="${pageNo <= subjects.pageCount }">
						<c:if test="${subjects.pageNow == pageNo }">
							<li class="active"><a href="${path}/examsubjectmiddle/selectSubjectAll.action?page=${pageNo }&handAdd=${handAdd }&examPaperId=${exampaperid }">${pageNo }</a></li>
						</c:if>
						<c:if test="${subjects.pageNow != pageNo }">
							<li><a href="${path}/examsubjectmiddle/selectSubjectAll.action?page=${pageNo }&handAdd=${handAdd }&examPaperId=${exampaperid }" class="pageLink">${pageNo }</a></li>
						</c:if>
					</c:if>
				</c:forEach>
				<c:if test="${subjects.pageNow+1 <= subjects.pageCount }">
					<li><a href="${path}/examsubjectmiddle/selectSubjectAll.action?page=${subjects.pageNow+1 }&handAdd=${handAdd }&examPaperId=${exampaperid }">下一页</a></li>
				</c:if>
				<li><a href="${path}/examsubjectmiddle/selectSubjectAll.action?page=${subjects.pageCount }&handAdd=${handAdd }&examPaperId=${exampaperid }">尾页</a></li>
				<li>
					<a>${subjects.pageNow }/${subjects.pageCount }</a>
				</li>
				<li>
					<div style="width:-1%; height:100%;float:right;">
						<form action="${path}/examsubjectmiddle/selectSubjectAll.action?" id="scannerPageForm">
							<input id="scannerPage" type="text" name="page" handAdd="${handAdd }" examPaperId="${exampaperid }" style="width: 40px; height: 30px; border: 1px solid gray; border-radius: 4px;" />
							<input class="btn btn-default goPage" type="submit" value="Go" style="margin-left: -4px; height: 30px;" />
						</form>
					</div>
				</li>
			</ul>
		</div>
	</div>


	<!-- js引入 -->
    <script src="${path }/js/jquery.js"></script>
    <script src="${path }/js/kindeditor/kindeditor-min.js"></script>
    <script src="${path }/js/bootstrap/bootstrap.min.js"></script>
    <script src="${path }/js/zeroModal/zeroModal.min.js"></script>
	<script src="${path}/js/layer/layer.js"></script>
   	<script src="${path }/js/examPaper.js"></script>
   	<script src="${path }/js/Subject.js"></script>
   	<script type="text/javascript">

   		
   		$(function() {
   			//异步删除试题
  			$(".delSubject").click(function() {
  				var subjectId = $(this).attr("id");
  				$.ajax({
  					type: "POST",
  					data: "subjectId="+subjectId,
  					url: "delSubject",
  					success: function(data) {
  						if(data == 't') {
  							$("#tr-"+subjectId).remove();
  						} else {
  							alert("删除失败, 未知异常!");
  						}
  					},
  					error: function() {
						alert("删除失败, 未知异常!");  						
  					}
  				});
  			});
   		});
   	</script>
</body>
</html>
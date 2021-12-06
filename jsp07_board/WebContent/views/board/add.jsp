<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/includefile.jsp" %>
<%@ include file="../include/sessionCheck.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function addCheck() {
		var email =frmAdd.email.value;
		var subject =frmAdd.subject.value;
		var content =frmAdd.content.value;

	 	if(email==''){
			alert('이메일을 입력해 주세요');
			frmAdd.email.focus();
		}else if(subject==''){
			alert('제목을 입력해 주세요');
			frmAdd.subject.focus();
		}else {
			frmAdd.submit();
		} 
	}
</script>
</head>
<body>
	<%@ include file="../header.jsp" %>
	<h2>게시물등록</h2>
	<!-- enctype="application/x-www-form-urlencoded" : 단순문자열 전송(디폴트(기본값))-->
	<!-- enctype="multipart/form-data" : 파일을 전송 (method = "post") -->
	<form name=frmAdd action="${path}/board/add" method="post" enctype="multipart/form-data">
		<table border="1">
			<tr>
				<th>이메일</th>
				<td><input type="email" name="email" value="${sessionScope.email}" readonly></td>
			</tr>
			<tr>
				<th>제목</th>
				<td><input type="text" name="subject"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea rows="5" cols="25" name="content"></textarea></td>
			</tr>
			<tr>
				<th>파일</th>
				<td>
					<input type="file" name="file1"><br>
					<input type="file" name="file2"><br>
					<input type="file" name="file3"><br>
				</td>
				
			</tr>
			<tr>
				<td colspan="2" align="center">
					<button type="button" onclick="addCheck()">등록</button>
					<button type="reset">취소</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
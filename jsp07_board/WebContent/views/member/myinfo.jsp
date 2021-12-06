<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/includefile.jsp" %>
<%@ include file="../include/msg.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function goPopup(){
		// 주소검색을 수행할 팝업 페이지를 호출합니다.
		// 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
		var pop = window.open("${path}/views/member/jusoPopup.jsp","pop","width=570,height=420, scrollbars=yes, resizable=yes"); 
	}
	function jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2,engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn,detBdNmList,bdNm,bdKdcd,siNm,sggNm,emdNm,liNm,rn,udrtYn,buldMnnm,buldSlno,mtYn,lnbrMnnm,lnbrSlno,emdNo){
			// 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
			document.frmMyinfo.addr.value = roadAddrPart1; //도로명 주소
			document.frmMyinfo.addrdetail.value = addrDetail; //상세주소
			document.frmMyinfo.zipcode.value = zipNo; //우편번호
	}
	//수정체크
	function modifyCheck() {
		var email=frmMyinfo.email.value;
		var passwd =frmMyinfo.passwd.value;
		if(email==''){
			alert('이메일을 입력해 주세요');
			frmMyinfo.email.focus();
		}else if(passwd==''){
			alert('비밀번호를 입력해 주세요');
			frmMyinfo.passwd.focus();
		}else{
			frmMyinfo.submit();
		}
	}
	
</script>
</head>
<body>
	<%@ include file="../header.jsp"%>
	<h2>내정보</h2>
	<%-- <%=request.getRealPath("/") %> --%>
	<form name="frmMyinfo" action="${path}/member/modify" method="post" enctype="multipart/form-data">
		<table border="1">
			<tr>
				<th>이메일</th>
				<td><input type="email" name="email" value="${member.email}" readonly></td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td><input type="password" name="passwd"></td>
			</tr>
			<tr>
				<th>변경비밀번호</th>
				<td><input type="password" name="changepw"></td>
			</tr>
			<tr>
				<th>주소</th>
				<td>
					<input type="text" name="zipcode" size="5" value="${member.zipcode}">
					<button type="button" onclick="goPopup()">찾기</button>
					<hr>
					<input type="text" name="addr" size="30" value="${member.addr}"><br>
					<input type="text" name="addrdetail" size="30" value="${member.addrdetail}">
				</td>
			</tr>
			<tr>
				<th>사진</th>
				<td>
					<div>
				<!-- /saveImg : server.xml에서 Context 추가 -->
					<img alt="이미지를 등록해주세요" src="/saveImg/${member.filename}" width="150px">
					</div>
					<div>
					<a href="${path}/fin/filedown?filename=${member.filename}">다운로드</a>
					<hr>
					<input type="checkbox" name="filedel">삭제
					<input type="text" name="filename" value="${member.filename}" readonly><br> 
					</div>
					<hr>
					<input type="file" name="file">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<button type="button" onclick="modifyCheck()">수정</button>
					<button type="reset">취소</button>
				</td>
			</tr>		
		</table>
	</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./include/includefile.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	header{
		display: flex;
		justify-content: space-around;
	}
</style>

</head>
<body>
	<header>
		<div>
			<a href="${path}/views/home.jsp">
				<img alt="고양이로고" src="${path}/images/cat_logo.png" width="100"></a>
		 </div>
		<div> 고양이 유치원 </div>
		<div>
			<span><a href="${path}/member/myinfo">${sessionScope.email}</a></span>
			<c:if test="${empty sessionScope.email}">
			<!-- empty sessionScope.email : null체크 -->
				<a href="${path}/views/member/login.jsp">LOGIN</a> 
				<a href="${path}/views/member/add.jsp">JOIN</a>
			</c:if>
			<c:if test="${not empty sessionScope.email}">
				<a href="${path}/member/logout">LOGOUT</a> 
			</c:if>
		</div>
	</header>
	<hr>
	<nav>
		<a href="${path}/views/home.jsp">HOME</a>
		<a href="${path}/board/list">리스트</a>
		<a href="${path}/views/board/add.jsp">등록</a>
	</nav>
	<hr>
</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

</head>
<body>
	
<main>
	<div class="container-fluid bg-white fixed-area">
		<div class="row p-3">
			<div class="col-1">
				<a class="fs-5" href="${pageContext.request.contextPath}/"><i class="bi bi-chevron-left"></i></a>
			</div>
			<div class="col text-center" style="max-width: 500px;">
				<p class="text-center fs-5 fw-semibold">경고 !</p>
			</div>
			<div class="col-1">
			</div>
		</div>
	</div>
	
	<div class="container" style="padding-top: 90px;">
		<div class="body-container p-3">	
			
			<div class="d-grid pt-3">
				<p class="alert alert-warning text-center fs-6" >
					해당 정보를 접근 할 수 있는 권한 이 없습니다.
				</p>
			</div>
			
            <div class="d-grid">
                <button type="button" class="btn btn-lg btn-light" onclick="location.href='${pageContext.request.contextPath}/'">메인화면으로 이동 <i class="bi bi-arrow-counterclockwise"></i> </button>
            </div>
	                       			
		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>
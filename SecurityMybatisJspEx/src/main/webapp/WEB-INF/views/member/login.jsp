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
				<p class="text-center fs-5 fw-semibold">로그인</p>
			</div>
			<div class="col-1">
			</div>
		</div>
	</div>
	
	<div class="container" style="padding-top: 70px;">
		<div class="body-container">	
			
	        <div class="row">
	            <div class="col">
	                <div class="mt-2 p-5 pt-3 pb-1">
	                    <form name="loginForm" action="" method="post" class="row g-3">
	                    	<div>
	                    		<div class="fs-5 pb-1 fw-semibold">반갑습니다.</div>
	                    		<div class="fs-5 pb-2 fw-semibold">스프링 입니다.</div>
	                    	</div>
	                        
	                        <div>
	                            <label class="mb-1">아이디</label>
	                            <input type="text" name="userId" class="form-control" placeholder="아이디">
	                        </div>
	                        <div>
	                            <label class="mb-1">패스워드</label>
	                            <input type="password" name="userPwd" class="form-control" autocomplete="off" 
	                            	placeholder="패스워드">
	                        </div>
	                        <div>
	                        	<div class="row ps-3 pe-3">
		                            <div class="form-check col-auto">
		                                <input class="form-check-input" type="checkbox" id="autoLogin" checked>
		                                <label class="form-check-label" for="autoLogin"> 자동 로그인</label>
		                            </div>
		                            <div class="form-check col-auto">
		                                <input class="form-check-input" type="checkbox" id="rememberMe" checked>
		                                <label class="form-check-label" for="rememberMe"> 아이디 저장</label>
		                            </div>
	                            </div>
	                        </div>
	                        <div>
	                            <button type="button" class="btn btn-primary w-100" onclick="sendLogin();">&nbsp;Login&nbsp;<i class="bi bi-check2"></i></button>
	                        </div>
	                    </form>
	                    <hr class="mt-4">
	                    <div class="col-12">
	                        <p class="text-center mb-0">
	                        	<a href="#" class="text-decoration-none me-2">아이디 찾기</a>
	                        	<a href="#" class="text-decoration-none me-2">패스워드 찾기</a>
	                        	<a href="#" class="text-decoration-none">회원가입</a>
	                        </p>
	                    </div>
	                </div>
	
	                <div class="d-grid">
							<p class="form-control-plaintext text-center text-primary">${message}</p>
	                </div>
	
	            </div>
			</div>
			
		</div>
	</div>
</main>

<script type="text/javascript">
function sendLogin() {
    const f = document.loginForm;
	let str;
	
	str = f.userId.value.trim();
    if(!str) {
        f.userId.focus();
        return;
    }

    str = f.userPwd.value.trim();
    if(!str) {
        f.userPwd.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/member/login";
    f.submit();
}
</script>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>
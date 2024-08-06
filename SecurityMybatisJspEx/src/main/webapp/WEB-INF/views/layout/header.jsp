<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<style type="text/css">
	@import url(https://fonts.googleapis.com/earlyaccess/nanumgothiccoding.css);
	nav span{ font-family: 'Nanum Gothic Coding', 'Malgun Gothic', sans-serif; }
</style>

<div class="container-fluid p-0">
	<div>
		<div class="row">
			<div class="col-auto">
				<div class="p-2">
					<a href="${pageContext.request.contextPath}/"><i class="bi bi-app-indicator"></i></a>
				</div>
			</div>
			<div class="col">
				<div class="p-2 ps-6 fw-semibold text-center">
					SPRING
				</div>
			</div>
			<div class="col-auto">
				<div class="d-flex justify-content-end">
					<div class="p-2">
						<a href="#" title="장바구니"><i class="bi bi-cart"></i></a>
					</div>
					<div class="p-2">
						<a href="#" title="알림"><i class="bi bi-bell"></i></a>
					</div>
				</div>
			</div>	
		</div>
	
		<div class="row">
			<div class="col">
				<div class="input-group input-group-sm p-2">
					<input type="text" name="kwd" class="form-control" placeholder="검색어를 입력하세요" aria-describedby="basic-addon1">
					<span class="input-group-text" id="basic-addon1"><i class="bi bi-search"></i></span>
				</div>
			</div>
		</div>
	</div>

	<nav>
		<ul>
			<li>
				<span data-url="${pageContext.request.contextPath}/">홈</span>
			</li>
			<li>
				<span data-url="#">메뉴2</span>
			</li>
			<li>
				<span data-url="#">메뉴3</span>
			</li>
			<li>
				<span data-url="#">메뉴4</span>
			</li>
			<li>
				<span data-url="#">메뉴5</span>
			</li>
			<li>
				<span data-url="#">메뉴6</span>
			</li>
			<li>
				<span data-url="#">메뉴7</span>
			</li>
			<li>
				<span data-url="#">메뉴8</span>
			</li>
			<li>
				<span data-url="#">메뉴9</span>
			</li>
			<li>
				<span data-url="#">메뉴10</span>
			</li>
			<li>
				<span data-url="#">메뉴11</span>
			</li>
			<li>
				<span data-url="#">메뉴12</span>
			</li>
			<li>
				<span data-url="#">메뉴13</span>
			</li>
		</ul>
	</nav>
</div>

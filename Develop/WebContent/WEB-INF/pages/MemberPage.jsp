<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>會員資料</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link
	href="https://fonts.googleapis.com/css?family=Nunito+Sans:200,300,400,600,700,800,900"
	rel="stylesheet">
<link rel="stylesheet" href="css/open-iconic-bootstrap.min.css">
<link rel="stylesheet" href="css/animate.css">
<link rel="stylesheet" href="css/owl.carousel.min.css">
<link rel="stylesheet" href="css/owl.theme.default.min.css">
<link rel="stylesheet" href="css/magnific-popup.css">
<link rel="stylesheet" href="css/aos.css">
<link rel="stylesheet" href="css/ionicons.min.css">
<link rel="stylesheet" href="css/flaticon.css">
<link rel="stylesheet" href="css/icomoon.css">
<link rel="stylesheet" href="css/style.css">
<style>
.notice {
	color: #ff0000;
	font-size: small;
}

#memo {
	position: absolute;
	right: 0;
	color: #ff0000;
	font-size: small;
	text-align: right;
	padding-right: 50px;
}
</style>
</head>
<body data-spy="scroll" data-target=".site-navbar-target"
	data-offset="300">
	<nav
		class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light site-navbar-target"
		id="ftco-navbar">
		<div class="container">
			<a class="navbar-brand" href="index.html">Slim</a>
			<button class="navbar-toggler js-fh5co-nav-toggle fh5co-nav-toggle"
				type="button" data-toggle="collapse" data-target="#ftco-nav"
				aria-controls="ftco-nav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="oi oi-menu"></span> Menu
			</button>

			<div class="collapse navbar-collapse" id="ftco-nav">
				<ul class="navbar-nav nav ml-auto">
					<li class="nav-item"><a href="MemberAdminPages"
						class="nav-link"><span>Admin</span></a></li>
					<li class="nav-item"><a href="<c:url value='/loginPage'/>"
						class="nav-link"><span>Home</span></a></li>
					<li class="nav-item"><a href="index.html#programs-section"
						class="nav-link"><span>Programs</span></a></li>
					<li class="nav-item"><a href="index.html#services-section"
						class="nav-link"><span>Services</span></a></li>
					<li class="nav-item"><a href="index.html#schedule-section"
						class="nav-link"><span>Schedule</span></a></li>
					<li class="nav-item"><a href="index.html#about-section"
						class="nav-link"><span>About</span></a></li>
					<li class="nav-item"><a href="index.html#coaches-section"
						class="nav-link"><span>Coaches</span></a></li>
					<li class="nav-item"><a href="index.html#blog-section"
						class="nav-link"><span>Blog</span></a></li>
					<li class="nav-item"><a href="index.html#contact-section"
						class="nav-link"><span>Contact</span></a></li>
				</ul>
			</div>
		</div>
	</nav>
	<section class="hero-wrap hero-wrap-2"
		style="background-image: url('images/bg_3.jpg');"
		data-stellar-background-ratio="0.5">
		<div class="overlay"></div>
		<div class="container">
			<div
				class="row no-gutters slider-text align-items-end justify-content-center">
				<div class="col-md-9 ftco-animate pb-5 text-center">
					<h1 class="mb-3 bread">Our Stories</h1>
					<p class="breadcrumbs">
						<span class="mr-2"><a href="index.html">Home <i
								class="ion-ios-arrow-forward"></i></a></span> <span class="mr-2"><a
							href="blog.html">Blog <i class="ion-ios-arrow-forward"></i></a></span> <span>Blog
							Single <i class="ion-ios-arrow-forward"></i>
						</span>
					</p>
				</div>
			</div>
		</div>
	</section>
	<section class="ftco-counter img ftco-section ftco-no-pt ftco-no-pb"
		id="schedule-section">
		<div class="container">
			<div class="comment-form-wrap pt-5" style="padding: 20px;">
				<h3 class="mb-5">會員資料</h3>
				<form class="p-5 bg-light"
					style="position: relative; border: 1px solid;" method="post">
					<div class="form-group">
						<label for="memberAccount">會員帳號：</label> <label>${Account}</label>
						<img style="position: absolute; right: 1" height="175"
							src="getPhoto/<c:out value='${id}'/>"> <img
							style="position: absolute; right: 0" height="175"
							src="getPhoto2/<c:out value='${id}'/>">
					</div>
					<div class="form-group">
						<label>姓名：</label> <label for="memberAccount">${name}</label>
					</div>
					<div class="form-group">
						<label for="nickname">綽號：</label> <label>${nickname}</label>
					</div>
					<div class="form-group">
						<label for="gender">性別：</label> <label>${gender}</label>
					</div>
					<div class="form-group">
						<label for="memberEmail">Email：</label> <label>${email}</label>
					</div>
					<div class="form-group">
						<label for="birthdayDate">生日：</label> <label>${birthday}</label>
					</div>
					<div class="form-group">
						<label for="starSign">星座：</label> <label>${starSign}</label>
					</div>
					<div class="form-group">
						<label for="city">居住/生活城市：</label> <label>${city}</label>
					</div>
					<div class="form-group">
						<label for="bloodType">血型：</label> <label>${bloodtype}</label>
					</div>
					<div class="form-group">
						<label for="hobbies">興趣喜好：</label> <label>${hobbies}</label>
					</div>
					<div class="form-group">
						<label for="selfIntro">關於我：</label> <label>${selfinfo}</label>
					</div>
					<div class="form-group">
						<a href="<c:url value='MemberPage_show'/>">顯示</a> <a
							href="<c:url value='MemberPage_updatePic'/>">上傳照片</a> <a
							href="<c:url value='MemberPage_update'/>">修改</a><span>${errors.msg}</span>
						<br />
					</div>
				</form>
			</div>
		</div>
	</section>
	<%@ include file="footer.jsp"%>
	<script src="../js/ProductBrowserPage.js" type="text/javascript"></script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<title>OTP Page</title>
<meta charset="UTF-8">
<link rel="stylesheet" href="../cactus/vendor/css/bootstrap.min.css" />
<link rel="stylesheet" href="../cactus/assets/css/app.css" />
<script src="../cactus/vendor/js/jquery-2.0.3.min.js"></script>
<script src="../cactus/vendor/js/jquery.validate.min.js"></script>
</head>
    
<body class="background">
<div>
<section class="main">
	
    <form:form action="verifyOTP" method="post" name="userProfile" commandName="userProfile" class="form-2">
	<h1><span class="log-in"><spring:message code="OTP_RESEND"/></span></h1>
    	<input type="text" name="otp" maxlength="6" placeholder="OTP" autocomplete="off" required>
    	<form:errors path="otp" cssStyle="color: #ff0000;"/>
    	<input type="submit" style="float:none;"  value="Submit">
    </form:form>
</section>
</div>
</body>
</html>
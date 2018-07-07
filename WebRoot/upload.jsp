<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>文件上传的表单</h3>
	<!-- 
		注意:这里的文件上传的表单　必须要满足　以下的三个条件，缺一不可
		1. 表单的 提交方式, 必须是 post , 
		2. 表单的 enctype属性的值必须是 multipart/form-data (用来指定 表单的数据提交到服务器时 是怎样被描述的 , 是怎样带给服务器的 )
		3. 表单必须要有一个 input 标签,type属性的值是  type="file"
	 -->
	<font color="red">${message }</font>
	<form action="/day11_upload/upload2" method="post" enctype="multipart/form-data">
		上传人: <input type="text" name="name"><br/>
		请选择要上传的文件:<input type="file" name="file"><br/>
		<input type="submit" value="上传">
	</form>
</body>
</html>
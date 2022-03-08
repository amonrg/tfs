<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <form enctype="multipart/form-data" method="POST" action="upload">
            File: <input type="file" name="file" id="file" />
            <input type="submit" value="Upload" name="upload" id="upload" />
        </form>
        <br />
        <c:choose>
            <c:when test="${user_id == null}">
                <a href="login.html">Login</a>
                <a href="register.html">Register</a>
            </c:when>
            <c:otherwise>
                <a href="logout">Logout</a>
                <p>Username: ${username}</p>
            </c:otherwise>
        </c:choose>
    </body>
</html>

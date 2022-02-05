<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    </body>
</html>

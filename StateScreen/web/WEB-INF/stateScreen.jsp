<%-- 
    Document   : stateScreen
    Created on : Nov 2, 2013, 9:28:34 PM
    Author     : vis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>State Fund Request</title>
    </head>
    <body>
        <h1>State Fund Request</h1>

        <form action="stateScreen" method="post">
            <table>
                <tr>
                    <td>Amount Request: </td>
                    <td><input type="number" name="quantity"></td>
                </tr>
                <tr>
                    <td>State: </td>
                    <td><select name="State">
                    <c:forEach items="${states}" var="state">
                    <option value="${state.stateCode}">${state.stateName}</option>                         
                    </c:forEach>
                    </select></td>
                </tr>    
                <tr>
                    <td> Department: </td>
                    <td><select name="Department">
                    <c:forEach items="${departments}" var="department">
                    <option value="${department.departmentId}">${department.departmentName}</option>                         
                    </c:forEach>
                    </select></td>
                </tr>
            </table>
        
            <p><input type="submit" value="Request"></p>
        </form> 
    </body>
</html>

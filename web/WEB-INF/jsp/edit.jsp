<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume?action=${action}" enctype="application/x-www-form-urlencoded">

        <input type="hidden" name="uuid" value="${resume.uuid}">
        <table>
            <tr>
                <td>Имя:</td>
                <td><input type="text" name="fullName" size=50 value="${resume.fullName}">
                </td>
            </tr>
            <tr>
                <td>
                    <h3>Контакты:</h3>
                </td>
            </tr>
            <c:forEach var="type" items="<%=ContactType.values()%>">
                <tr>
                    <td>${type.title}</td>
                    <td><input type="text" name="${type.name()}" size=30
                               value="${resume.getContact(type)}"></td>
                </tr>
            </c:forEach>
            <tr>
                <td><h3>Секции :</h3></td>
            </tr>
            <c:forEach var="type" items="<%=SectionType.values()%>">
                <c:if test="${type.name().toString() != \"EDUCATION\" && type.name().toString() != \"EXPERIENCE\"}">
                    <tr>
                        <td>${type.title}</td>
                        <td>
                            <c:if test="${resume.getSection(type) ==null}">
                        <textarea name="${type.name()}"
                                  cols="200"></textarea>
                            </c:if>
                            <c:if test="${resume.getSection(type) !=null}">
                                <c:choose>
                                    <c:when test="${ type.toString() == \"PERSONAL\" || type.toString() == \"OBJECTIVE\" }"><textarea
                                            name="${type.name()}"
                                            cols="200">${resume.getSection(type)}</textarea></c:when>
                                    <c:when test="${ type.toString() == \"ACHIEVEMENT\" }"><textarea
                                            name="${type.name()}" cols="200" rows="15"><%=
                                    ((ListSection) resume.getSection(SectionType.ACHIEVEMENT))
                                            .getItems().stream().collect(Collectors.joining("\n"))%></textarea></c:when>
                                    <c:when test="${ type.toString() == \"QUALIFICATIONS\" }"><textarea
                                            name="${type.name()}" cols="200" rows="15"><%=
                                    ((ListSection) resume.getSection(SectionType.QUALIFICATIONS))
                                            .getItems().stream().collect(Collectors.joining("\n"))%></textarea></c:when>
                                </c:choose>
                            </c:if>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

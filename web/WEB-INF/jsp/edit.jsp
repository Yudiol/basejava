<%@ page import="java.util.stream.Collectors" %>
<%@ page import="com.urise.webapp.model.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
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
    <form method="post" action="resume?action=${action}"
          enctype="application/x-www-form-urlencoded">

        <input type="hidden" name="uuid" value="${resume.uuid}">
        <table>
            <tr>
                <td class="col1"><h3>Имя : </h3></td>
                <td class="col2"><input type="text" pattern="[А-яA-z0-9\s]+" required placeholder="ФИО" name="fullName"
                                        size=70 value="${resume.fullName}"></td>
            </tr>
            <tr>
                <td><h3>Контакты : </h3></td>
            </tr>
            <c:forEach var="type" items="<%=ContactType.values()%>">
                <tr>
                    <td class="col1"><h4>${type.title}</h4></td>
                    <td class="col2"><input type="text" name="${type.name()}" size=70
                                            value="${resume.getContact(type)}"></td>
                </tr>
            </c:forEach>
            <tr>
                <td><h2>Секции : </h2></td>
            </tr>
            <c:forEach var="type" items="<%=SectionType.values()%>">
                <tr class="section">
                    <td class="col1"><h3>${type.title}</h3></td>
                    <td class="col2">
                        <c:choose>
                        <c:when test="${ type.toString() == \"PERSONAL\" || type.toString() == \"OBJECTIVE\" }"><textarea
                            class="resizable"
                            name="${type.name()}" cols="200">${resume.getSection(type)}</textarea></c:when>

                        <c:when test="${ type.toString() == \"ACHIEVEMENT\" }"><textarea class="resizable"
                                                                                         name="${type.name()}"
                                                                                         cols="200"
                    ><%=((ListSection) resume.getSection(SectionType.ACHIEVEMENT)).getItems().stream().collect(Collectors.joining("\n"))%></textarea></c:when>
                        <c:when test="${ type.toString() == \"QUALIFICATIONS\" }"><textarea class="resizable"
                                                                                            name="${type.name()}"
                                                                                            cols="200"
                    ><%=((ListSection) resume.getSection(SectionType.QUALIFICATIONS)).getItems().stream().collect(Collectors.joining("\n"))%></textarea></c:when>
                        <c:when test="${ type.toString() == \"EDUCATION\" || type.toString() == \"EXPERIENCE\"}">
                        <c:choose>
                        <c:when test="${ type.toString() == \"EDUCATION\" }">
                            <c:set var="listOrganisations"
                                   value="<%=((OrganizationSection) resume.getSection(SectionType.EDUCATION)).getOrganizations()%>"/>
                        </c:when>
                        <c:when test="${ type.toString() == \"EXPERIENCE\" }">
                            <c:set var="listOrganisations"
                                   value="<%=((OrganizationSection) resume.getSection(SectionType.EXPERIENCE)).getOrganizations()%>"/>
                        </c:when>
                        </c:choose>
                                <%int count = 0;%>
                        <div>
                            <input type="checkbox" data-name="${type}" data-organisation="<%=count%>" class="checkbox"
                                   id="checkbox${type}">
                            <label for="${type.name()}" style="font-weight: 700;">Нет ${type.title}</label>
                        </div>
                        <c:forEach
                                items="${listOrganisations}"
                                var="organisation"
                                varStatus="status">
                            <jsp:useBean id="organisation" type="com.urise.webapp.model.Organization"/>
                                <%int counterPeriods = 0;%>
                        <div id="o-${type}<%=count%>">
                            <p> Organisation : <input type="text"
                                                      name="title${type.name()}${status.index}"
                                                      value="${organisation.homePage.name}"
                                                      size=70></p>
                            <p> Site : <input type="text" name="site${type.name()}${status.index}"
                                              value="${organisation.homePage.url}"
                                              size=70></p>
                        </div>
                        <c:forEach items="${organisation.posts}" var="periods" varStatus="period">
                            <jsp:useBean id="periods" type="com.urise.webapp.model.Period"/>
                        <div id="p-${type}<%=count%><%=counterPeriods%>">
                            <p> Start : <input id="start" type="date"
                                               value="${periods.startDate}"
                                               required name="startDate${type.name()}${status.index}${period.index}">
                                End : <input type="date" value="${periods.endDate}"
                                             required name="endDate${type.name()}${status.index}${period.index}"></p>
                            <p> Position : <input size=70 required type="text" value="${periods.title}"
                                                  name="titlePeriod${type.name()}${status.index}${period.index}">
                            </p>
                            <div> Description : <textarea required class="resizable"
                                                          name="description${type.name()}${status.index}${period.index}"
                                                          id="description${type.name()}${status.index}${period.index}"
                                                          cols="200">${periods.description}</textarea>
                            </div>
                            <c:choose>
                                <c:when test="${period.index  != 0}">
                                    <button type="button" class="removePeriod" data-name="${type}"
                                            data-period="<%=counterPeriods%>" data-organisation="<%=count%>">Удалить
                                        период
                                    </button>
                                </c:when>
                            </c:choose>
                        </div>
                                <%counterPeriods++;%>
                        </c:forEach>
                        <div id="b-p-${type}<%=count%>">
                            <button type="button" data-organisation="<%=count%>" class="periods"
                                    data-period="<%=counterPeriods%>" data-name="${type.name()}">Добавить период
                            </button>
                            <input type="hidden" id="counterPeriods${type.name()}${status.index}"
                                   name="counterPeriods${type.name()}${status.index}"
                                   value=<%=counterPeriods%>>
                        </div>
                        <div id="b-o-r-${type}<%=count%>">
                            <button data-organisation="<%=count%>" type="button" class="removeOrganisation"
                                    data-name="${type.name()}"
                            >Удалить организацию
                            </button>
                            <%count++;%>
                        </div>
                        </c:forEach>
                        <div id="button-addOrganisation-${type.name()}">
                            <button data-organisation="<%=count%>" type="button" class="organisations"
                                    id="${type.name()}"
                                    data-name="${type.name()}">Добавить организацию
                            </button>
                            <input id="count${type}" type="hidden" name="count${type}" value=<%=count%>></div>
                        <input id="firstOrganisation${type}" type="hidden" name="firstOrganisation${type}"
                               value="0"></div>
                        </c:when>
                        </c:choose>
                </tr>
            </c:forEach>
        </table>

        <script src="..\js\file.js"></script>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

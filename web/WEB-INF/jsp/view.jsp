<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.OrganizationSection" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <h3>${contacts}</h3>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <h3>${sections}</h3>
    <c:forEach var="type" items="<%=SectionType.values()%>">
        <c:if test='${resume.getSection(type) != null }'>
            <h3>${type.title}</h3>
            <c:choose>
                <c:when test="${ type.toString() == \"PERSONAL\" || type.toString() == \"OBJECTIVE\" }">${resume.getSection(type)}</c:when>
                <c:when test="${ type.toString() == \"ACHIEVEMENT\" ||  type.toString() == \"QUALIFICATIONS\"}"><c:forEach
                        var="item" items="${resume.getSection(type).getItems()}"><p>${item}</p></c:forEach></c:when>
                <c:when test="${type.toString() == \"EDUCATION\" || type.toString() == \"EXPERIENCE\"}">
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
                    <c:forEach var="organisation" items="${listOrganisations}">
                        <jsp:useBean id="organisation" type="com.urise.webapp.model.Organization"/>
                        <div>
                            <c:choose>
                                <c:when test="${organisation.homePage.url == ''}">
                                    <p> Организация : ${organisation.homePage.name}</p>
                                </c:when>
                                <c:when test="${organisation.homePage.url != ''}">
                                    <p> Организация : <a
                                            href="${organisation.homePage.url}"> ${organisation.homePage.name}</a>
                                    </p>
                                </c:when>
                            </c:choose>
                        </div>
                        <c:forEach var="post" items="${organisation.posts}">
                            <div>
                                <p style="margin-left: 50px;"> С - ${post.startDate == "4000-01-01"?"":post.startDate}
                                    по
                                    - ${ post.startDate == "4000-01-01"?"":(post.endDate=="3000-01-01"?"настоящее время":post.endDate)} </p>
                                <p style="margin-left: 50px;"> Позиция : ${post.title}</p>
                                <p style="margin-left: 50px;"> Описание : ${post.description}</p>
                            </div>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:if>
    </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
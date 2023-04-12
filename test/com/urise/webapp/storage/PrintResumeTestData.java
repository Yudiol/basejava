package com.urise.webapp.storage;

import com.urise.webapp.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintResumeTestData {
    public static void main(String[] args) {


        Resume resume = new Resume("Григорий Кислин");

        getContact(resume);
        resume.setSection(SectionType.ACHIEVEMENT, getList(listAchievement()));
        resume.setSection(SectionType.QUALIFICATIONS, getList(listQualification()));
        resume.setSection(SectionType.OBJECTIVE,
                new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.setSection(SectionType.PERSONAL,
                new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        resume.setSection(SectionType.EXPERIENCE, getOrganization(listOrganization()));
        resume.setSection(SectionType.EDUCATION, getOrganization(listEducation()));


        System.out.println(resume.getFullName());

        for (ContactType type : ContactType.values()) {
            System.out.println(type.getTitle() + " " + resume.getContact(type));
        }

        System.out.println();

        printText(resume, SectionType.OBJECTIVE);
        printText(resume, SectionType.PERSONAL);
        printList(resume, SectionType.ACHIEVEMENT);
        printList(resume, SectionType.QUALIFICATIONS);
        printOrganization(resume, SectionType.EXPERIENCE);
        printOrganization(resume, SectionType.EDUCATION);

    }

    public static void printText(Resume resume, SectionType type) {
        System.out.println(type.getTitle() + "\n" + ((TextSection) resume.getSection(type)).getContent() + "\n");
    }

    public static void printList(Resume resume, SectionType type) {
        System.out.println();
        System.out.println(type.getTitle() + "\n");
        ((ListSection) resume.getSection(type)).getItems().stream()
                .forEach(e -> System.out.println((char) 176 + " " + e));

    }

    public static void printOrganization(Resume resume, SectionType type) {
        System.out.println("\n" + type.getTitle());
        ((OrganizationSection) resume.getSection(type)).getOrganizations().stream()
                .peek(e -> System.out.println("\n\t\t\t\t\t" + e.getHomePage().getName())).flatMap(el -> el.getPosts().stream()).forEach(
                        element -> System.out.println(element.getStartDate().format(DateTimeFormatter.ofPattern("MM/yyyy")) + " - " +
                                element.getEndDate().format(DateTimeFormatter.ofPattern("MM/yyyy")) + "   " + element.getTitle()
                                + "\n\t\t\t\t\t" + element.getDescription()));
    }

    public static void getContact(Resume resume) {
        resume.setContact(ContactType.MOBILE, "+7(921) 855-0482");
        resume.setContact(ContactType.SKYPE, "skype:grigory.kislin");
        resume.setContact(ContactType.MAIL, "gkislin@yandex.ru");
        resume.setContact(ContactType.LINKEDIN, "Профиль LinkedIn");
        resume.setContact(ContactType.GITHUB, "https://github.com/gkislin");
        resume.setContact(ContactType.STATCKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        resume.setContact(ContactType.HOME_PAGE, "http://gkislin.ru/");
    }

    public static ListSection getList(List<String> list) {
        return new ListSection(list);
    }

    public static OrganizationSection getOrganization(List<Organization> list) {
        return new OrganizationSection(list);
    }




    public static List<String> listAchievement() {
        List<String> achievementList = new ArrayList<>();
        achievementList.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк " +
                "на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие " +
                "в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет ");
        achievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный " +
                "maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". " +
                "Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.");
        achievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция " +
                "с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, " +
                "Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. " +
                "Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        achievementList.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, " +
                "GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievementList.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура," +
                " JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. " +
                "Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        achievementList.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, " +
                "Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        return achievementList;
    }

    public static List<String> listQualification() {
        List<String> qualificationList = new ArrayList<>();
        qualificationList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualificationList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualificationList.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB");
        qualificationList.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        qualificationList.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        qualificationList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, " +
                "Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports," +
                " Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        qualificationList.add("Python: Django.");
        qualificationList.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualificationList.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualificationList.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, " +
                "MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        qualificationList.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        qualificationList.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        qualificationList.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
        qualificationList.add("Родной русский, английский \"upper intermediate\"");
        return qualificationList;
    }

    public static List<Organization> listOrganization() {
        List<Organization> organizations = new ArrayList<>();
        organizations.add(new Organization("Java Online Projects", "https://javaops.ru/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2013, 10, 1), LocalDate.now(),
                        "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.")))));
        organizations.add(new Organization("Wrike", "https://www.wrike.com/vv/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2014, 10, 01), LocalDate.of(2016, 01, 01),
                        "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления " +
                        "проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). " +
                        "Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.")))));
        organizations.add(new Organization("RIT Center", "https://www.wrike.com/vv/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2012, 04, 01), LocalDate.of(2014, 10, 01),
                        "Java архитектор", "Организация процесса разработки системы ERP для разных окружений: " +
                        "релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы " +
                        "(pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C " +
                        "(WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование " +
                        "из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, " +
                        "OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python")))));
        organizations.add(new Organization("Luxoft(Deutsche Bank)", "https://www.wrike.com/vv/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2010, 12, 01), LocalDate.of(2012, 04, 01),
                        "Ведущий программист", "Участие в проекте Deutsche Bank CRM(WebLogic, Hibernate, Spring, Spring MVC, " +
                        "SmartGWT, GWT, Jasper, Oracle).Реализация клиентской и серверной части CRM.Реализация RIA - " +
                        "приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга." +
                        "JPA, Spring, Spring - MVC, GWT, ExtGWT(GXT), Highstock, Commet, HTML5.")))));
        organizations.add(new Organization("Yota", "https://www.wrike.com/vv/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2008, 06, 01), LocalDate.of(2010, 12, 01),
                        "Ведущий программист", "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" " +
                        "(GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, " +
                        "статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)")))));
        organizations.add(new Organization("Enkata", "https://www.wrike.com/vv/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2007, 03, 01), LocalDate.of(2008, 06, 01),
                        "Разработчик ПО", "Реализация клиентской (Eclipse RCP) и серверной " +
                        "(JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining).")))));
        organizations.add(new Organization("Siemens AG", "https://www.wrike.com/vv/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2005, 01, 01), LocalDate.of(2007, 02, 01),
                        "Разработчик ПО", "Разработка информационной модели, проектирование интерфейсов, " +
                        "реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).")))));
        organizations.add(new Organization("Alcatel", "https://www.wrike.com/vv/",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(1997, 9, 01), LocalDate.of(2005, 01, 01),
                        "Инженер по аппаратному и программному тестированию", "Тестирование, отладка, внедрение ПО цифровой телефонной " +
                        "станции Alcatel 1000 S12 (CHILL, ASM).")))));
        return organizations;
    }


    public static List<Organization> listEducation() {
        List<Organization> list = new ArrayList<>();
        list.add(new Organization("Coursera", "https://www.coursera.org/learn/scala-functional-programming",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2013, 03, 01), LocalDate.of(2013, 05, 01),
                        "Functional Programming Principles in Scala' by Martin Odersky", "")))));
        list.add(new Organization("Luxoft", "https://www.coursera.org/learn/scala-functional-programming",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2011, 03, 01), LocalDate.of(2011, 04, 01),
                        "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", "")))));
        list.add(new Organization("Siemens AG", "https://www.coursera.org/learn/scala-functional-programming",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(2005, 01, 01), LocalDate.of(2005, 04, 01),
                        "3 месяца обучения мобильным IN сетям (Берлин)", "")))));
        list.add(new Organization("Alcatel", "https://www.coursera.org/learn/scala-functional-programming",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(1997, 9, 01), LocalDate.of(1998, 03, 01),
                        "6 месяцев обучения цифровым телефонным сетям (Москва)", "")))));
        list.add(new Organization("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "https://www.coursera.org/learn/scala-functional-programming",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(1993, 9, 01), LocalDate.of(1996, 07, 01),
                        "Аспирантура (программист С, С++)", ""),new Period(
                        LocalDate.of(1987, 9, 01), LocalDate.of(1993, 07, 01),
                        "Инженер (программист Fortran, C)", "")))));
        list.add(new Organization("Заочная физико-техническая школа при МФТИ", "https://www.coursera.org/learn/scala-functional-programming",
                new ArrayList<>(Arrays.asList(new Period(
                        LocalDate.of(1984, 9, 01), LocalDate.of(1987, 06, 01),
                        "Закончил с отличием", "")))));
        return list;
    }


}

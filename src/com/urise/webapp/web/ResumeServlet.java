package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {

    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName").trim();
        Resume r = new Resume(uuid, fullName);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.setContact(type, value.trim());
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value;
            int firstOrganisation = 0;
            if (Objects.equals(type.toString(), "EXPERIENCE") || Objects.equals(type.toString(), "EDUCATION")) {
                firstOrganisation = Integer.parseInt(request.getParameter("firstOrganisation" + type));
                value = request.getParameter("title" + type + firstOrganisation);
            } else {
                value = request.getParameter(type.name());
            }
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        r.setSection(type, new TextSection(value.trim()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.setSection(type, new ListSection(Arrays.stream(value.split("\n"))
                                .map(String::trim)
                                .collect(Collectors.toList())));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        int count = Integer.parseInt(request.getParameter("count" + type));
                        List<Organization> organization = new ArrayList<>();
                        for (int i = firstOrganisation; i < count; i++) {
                            String title = request.getParameter("title" + type.name() + i);
                            if (Objects.equals(title, "")) {
                                continue;
                            }
                            String site = request.getParameter("site" + type.name() + i);
                            int counterPeriods = request.getParameter("counterPeriods" + type.name() + i) == null ? 0 : Integer.parseInt(request.getParameter("counterPeriods" + type.name() + i));
                            List<Period> periods = new ArrayList<>();
                            for (int k = 0; k < counterPeriods; k++) {
                                String startDate = request.getParameter("startDate" + type.name() + i + k);
                                String endDate = request.getParameter("endDate" + type.name() + i + k);
                                if (Objects.equals(startDate, null) || Objects.equals(endDate, null)) {
                                    continue;
                                }
                                if (Objects.equals(startDate, "")) {
                                    startDate = "4000-01-01";
                                }
                                if (Objects.equals(endDate, "")) {
                                    endDate = "4000-01-01";
                                }
                                String titlePeriod = request.getParameter("titlePeriod" + type.name() + i + k);
                                if (Objects.equals(titlePeriod, "")) {
                                    titlePeriod = " ";
                                }
                                String description = request.getParameter("description" + type.name() + i + k);
                                if (Objects.equals(description, "")) {
                                    description = " ";
                                }
                                periods.add(new Period(convertStringToLocalDate(startDate),
                                        convertStringToLocalDate(endDate), titlePeriod, description));
                            }
                            organization.add(new Organization(title, site, periods));

                            r.setSection(type, new OrganizationSection(organization));
                        }
                        break;
                }
            } else {
                r.getSections().remove(type);
            }
        }
        if (action.equals("new")) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                setSectionNotNull(r);
                break;
            case "new":
                r = new Resume("");
                request.setAttribute("action", "new");
                action = "edit";
                setSectionNotNull(r);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("sections", r.getSections().size() == 0 ? "" : "Секции :");
        request.setAttribute("contacts", r.getContacts().size() == 0 ? "" : "Контакты :");
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    private LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);

    }

    private void setListSection(SectionType section, Resume resume) {
        if (resume.getSection(section) == null) {
            resume.setSection(section, new ListSection(new ArrayList<String>(Collections.singleton(""))));
        }
    }

    private void setOrganisationSection(SectionType section, Resume resume) {
        if (resume.getSection(section) == null) {
            List<Organization> organizationList = new ArrayList<>();
            List<Period> periodList = new ArrayList<>();
            periodList.add(new Period());
            organizationList.add(new Organization("", "", periodList));
            resume.setSection(section, new OrganizationSection(organizationList));
        }
    }

    private void setSectionNotNull(Resume r) {
        setListSection(SectionType.ACHIEVEMENT, r);
        setListSection(SectionType.QUALIFICATIONS, r);
        setOrganisationSection(SectionType.EDUCATION, r);
        setOrganisationSection(SectionType.EXPERIENCE, r);
    }
}
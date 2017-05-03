package org.tju.HFDemo.core.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shaohan.yin on 03/05/2017.
 */
public class StudentInfo {
    private String id;
    private String name;
    private String university;
    private String degree;
    private Date start;
    private Date end;
    private List<String> educationQualifications = new LinkedList<>();
    private List<InternInfo> internInfos = new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<String> getEducationQualifications() {
        return educationQualifications;
    }

    public void setEducationQualifications(List<String> educationQualifications) {
        this.educationQualifications = educationQualifications;
    }

    public void addEducationQualifications(String educationQualification) {
        this.educationQualifications.add(educationQualification);
    }

    public void removeEducationQualification(String educationQualification) {
        this.educationQualifications.remove(educationQualification);
    }

    public List<InternInfo> getInternInfos() {
        return internInfos;
    }

    public void setInternInfos(List<InternInfo> internInfos) {
        this.internInfos = internInfos;
    }

    public void addInternInfos(InternInfo internInfo) {
        this.internInfos.add(internInfo);
    }

    public void removeInternInfos(InternInfo internInfo) {
        this.internInfos.remove(internInfo);
    }

}

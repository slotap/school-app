package io.github.slotap.school.model;

import java.util.Set;

public class StudentDto {
    private long id;
    private String firstname;
    private String lastname;
    private int age;
    private String email;
    private String degreeCourse;
    private Set<Teacher> teacherSet;

    public StudentDto(long id, String firstname, String lastname, int age, String email, String degreeCourse, Set<Teacher> teacherSet) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.degreeCourse = degreeCourse;
        this.teacherSet = teacherSet;
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    public Set<Teacher> getTeacherSet() {
        return teacherSet;
    }
}

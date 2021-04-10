package io.github.slotap.school.model;

import java.util.Set;

public class StudentDto {
    private final long id;
    private final String firstname;
    private final String lastname;
    private final int age;
    private final String email;
    private final String degreeCourse;
    private final Set<Teacher> teacherSet;

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

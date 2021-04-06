package io.github.slotap.school.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student extends SchoolMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String degreeCourse;

    public Student(){}

    public Student(String firstname, String lastname, int age, String email, String degreeCourse) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.degreeCourse = degreeCourse;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    private void setDegreeCourse(String degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age && degreeCourse.equals(student.degreeCourse) && firstname.equals(student.firstname) && lastname.equals(student.lastname) && email.equals(student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, degreeCourse, firstname, lastname, email, age);
    }
}

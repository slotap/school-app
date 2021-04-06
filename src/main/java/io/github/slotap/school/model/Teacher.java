package io.github.slotap.school.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher extends SchoolMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String teachingSubject;

    public Teacher(){}

    public Teacher(String firstname, String lastname, int age, String email, String subject) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.teachingSubject = subject;
    }

    public String getTeachingSubject() {
        return teachingSubject;
    }

    private void setTeachingSubject(String subject) {
        this.teachingSubject = subject;
    }

    public long getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id && age == teacher.age && firstname.equals(teacher.firstname) && lastname.equals(teacher.lastname) && teachingSubject.equals(teacher.teachingSubject) && email.equals(teacher.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, teachingSubject,firstname,lastname, age, email);
    }
}

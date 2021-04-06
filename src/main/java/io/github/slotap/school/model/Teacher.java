package io.github.slotap.school.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher extends SchoolMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String subject;

    public Teacher(){}

    public Teacher(String firstname, String lastname, int age, String email, String subject) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    private void setSubject(String subject) {
        this.subject = subject;
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
        return id == teacher.id && age == teacher.age && firstname.equals(teacher.firstname) && lastname.equals(teacher.lastname) && subject.equals(teacher.subject) && email.equals(teacher.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, subject,firstname,lastname, age, email);
    }
}

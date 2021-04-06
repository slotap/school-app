package io.github.slotap.school.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class SchoolMember {
    protected String firstname;
    protected String lastname;
    protected int age;
    protected String email;

    public String getFirstname() {
        return firstname;
    }

    protected void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    protected void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    protected void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }
}

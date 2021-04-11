package io.github.slotap.school.service;

import io.github.slotap.school.dto.StudentDto;
import io.github.slotap.school.dto.TeacherDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DbStudentServiceTest {
    @Autowired
    private StudentMemberService studentService;
    @Autowired
    private TeacherMemberService teacherService;

    @Test
    void testSaveStudentToDb() {
        //Given
        StudentDto student = new StudentDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");

        //When
        StudentDto savedStudent = studentService.saveMember(student);
        long id = savedStudent.getId();

        //Then
        assertTrue(studentService.getById(id).isPresent());

        //CleanUp
        studentService.deleteMember(id);
    }

    @Test
    void testDeleteStudentFromDbWithoutDeletingTeacher() {
        //Given
        StudentDto student= new StudentDto("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        TeacherDto teacher = new TeacherDto("Andrzej","Wajda",40,"aWajda@Gmail.com","Historia");
        student.getTeachers().add(teacher);
        StudentDto savedStudent = studentService.saveMember(student);
        TeacherDto savedTeacher = teacherService.saveMember(teacher);

        //When
        studentService.deleteMember(savedStudent.getId());

        //Then
        assertTrue(studentService.getById(savedStudent.getId()).isEmpty());
        assertTrue(teacherService.getById(savedTeacher.getId()).isPresent());

        //CleanUp
        teacherService.deleteMember(savedTeacher.getId());
    }

    @Test
    void testGetStudentFromDBandReturnDto(){
        //Given
        StudentDto student = new StudentDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.saveMember(student);
        long id = savedStudent.getId();

        //When
        Optional<StudentDto> studentDto = studentService.getById(id);

        //Then
        assertEquals(student.getFirstname(), studentDto.get().getFirstname());
        assertEquals(student.getLastname(), studentDto.get().getLastname());
        assertEquals(student.getAge(), studentDto.get().getAge());
        assertEquals(student.getEmail(), studentDto.get().getEmail());
        assertEquals(student.getDegreeCourse(), studentDto.get().getDegreeCourse());

        //CleanUp
        studentService.deleteMember(id);
    }

    @Test
    void testFindStudentByName(){
        //Given
        StudentDto student = new StudentDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.saveMember(student);
        long id = savedStudent.getId();

        //When
        List<StudentDto> studentDtoList = studentService.getByLastnameFirstname("Kowalski","Jan");

        //Then
        assertNotEquals(0, studentDtoList.size());

        //CleanUp
        studentService.deleteMember(id);
    }

    @Test
    void testFindStudentByNameIfFirstnameEmpty(){
        //Given
        StudentDto student = new StudentDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.saveMember(student);
        long id = savedStudent.getId();

        //When
        List<StudentDto> studentDtoList = studentService.getByLastnameFirstname("Kowalski","");

        //Then
        assertNotEquals(0, studentDtoList.size());

        //CleanUp
        studentService.deleteMember(id);
    }

    @Test
    void testFindStudentByNameIfLastnameEmpty(){
        //Given
        StudentDto student = new StudentDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.saveMember(student);
        long id = savedStudent.getId();

        //When
        List<StudentDto> studentDtoList = studentService.getByLastnameFirstname("","Jan");

        //Then
        assertNotEquals(0, studentDtoList.size());

        //CleanUp
        studentService.deleteMember(id);
    }
}

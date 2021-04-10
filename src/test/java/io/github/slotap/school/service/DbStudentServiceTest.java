package io.github.slotap.school.service;

import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DbStudentServiceTest {
    @Autowired
    private DbStudentService studentService;
    @Autowired
    private DbTeacherService teacherService;

    @Test
    void testSaveStudentToDb() {
        //Given
        Student student = new Student("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");

        //When
        StudentDto savedStudent = studentService.save(student);
        long id = savedStudent.getId();

        //Then
        assertTrue(studentService.getData(id).isPresent());

        //CleanUp
        studentService.delete(id);
    }

    @Test
    void testDeleteStudentFromDbWithoutDeletingTeacher() {
        //Given
        Student student= new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        Teacher teacher = new Teacher("Andrzej","Wajda",40,"aWajda@Gmail.com","Historia");
        student.getTeachers().add(teacher);
        StudentDto savedStudent = studentService.save(student);
        TeacherDto savedTeacher = teacherService.save(teacher);

        //When
        studentService.delete(savedStudent.getId());

        //Then
        assertTrue(studentService.getData(savedStudent.getId()).isEmpty());
        assertTrue(teacherService.getData(savedTeacher.getId()).isPresent());

        //CleanUp
        teacherService.delete(savedTeacher.getId());
    }

    @Test
    void testGetStudentFromDBandReturnDto(){
        //Given
        Student student = new Student("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.save(student);
        long id = savedStudent.getId();

        //When
        Optional<StudentDto> studentDto = studentService.getDtoData(id);

        //Then
        assertEquals(student.getFirstname(), studentDto.get().getFirstname());
        assertEquals(student.getLastname(), studentDto.get().getLastname());
        assertEquals(student.getAge(), studentDto.get().getAge());
        assertEquals(student.getEmail(), studentDto.get().getEmail());
        assertEquals(student.getDegreeCourse(), studentDto.get().getDegreeCourse());

        //CleanUp
        studentService.delete(id);
    }

    @Test
    void testFindStudentByName(){
        //Given
        Student student = new Student("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.save(student);
        long id = savedStudent.getId();

        //When
        List<StudentDto> studentDtoList = studentService.findByName("Kowalski","Jan");

        //Then
        assertNotEquals(0, studentDtoList.size());

        //CleanUp
        studentService.delete(id);
    }

    @Test
    void testFindStudentByNameIfFirstnameEmpty(){
        //Given
        Student student = new Student("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.save(student);
        long id = savedStudent.getId();

        //When
        List<StudentDto> studentDtoList = studentService.findByName("Kowalski","");

        //Then
        assertNotEquals(0, studentDtoList.size());

        //CleanUp
        studentService.delete(id);
    }

    @Test
    void testFindStudentByNameIfLastnameEmpty(){
        //Given
        Student student = new Student("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        StudentDto savedStudent = studentService.save(student);
        long id = savedStudent.getId();

        //When
        List<StudentDto> studentDtoList = studentService.findByName("","Jan");

        //Then
        assertNotEquals(0, studentDtoList.size());

        //CleanUp
        studentService.delete(id);
    }
}
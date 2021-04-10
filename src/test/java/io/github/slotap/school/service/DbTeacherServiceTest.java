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
class DbTeacherServiceTest {
    @Autowired
    private DbStudentService studentService;
    @Autowired
    private DbTeacherService teacherService;

    @Test
    void testSaveTeacherToDb() {
        //Given
        Teacher teacher = new Teacher("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");

        //When
        TeacherDto savedTeacher = teacherService.save(teacher);
        long id = savedTeacher.getId();

        //Then
        assertTrue(teacherService.getData(id).isPresent());

        //CleanUp
        teacherService.delete(id);
    }

    @Test
    void testDeleteTeacherFromDbWithoutDeletingStudent() {
        //Given
        Student student= new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        Teacher teacher = new Teacher("Andrzej","Wajda",40,"aWajda@Gmail.com","Historia");
        teacher.getStudents().add(student);
        TeacherDto savedTeacher = teacherService.save(teacher);
        StudentDto savedStudent = studentService.save(student);

        //When
        teacherService.delete(savedTeacher.getId());

        //Then
        assertTrue(teacherService.getData(savedTeacher.getId()).isEmpty());
        assertTrue(studentService.getData(savedStudent.getId()).isPresent());

        //CleanUp
        studentService.delete(savedTeacher.getId());
    }

    @Test
    void testGetTeacherFromDBandReturnDto(){
        //Given
        Teacher teacher = new Teacher("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.save(teacher);
        long id = savedTeacher.getId();

        //When
        Optional<TeacherDto> teacherDto = teacherService.getDtoData(id);

        //Then
        assertEquals(teacher.getFirstname(), teacherDto.get().getFirstname());
        assertEquals(teacher.getLastname(), teacherDto.get().getLastname());
        assertEquals(teacher.getAge(), teacherDto.get().getAge());
        assertEquals(teacher.getEmail(), teacherDto.get().getEmail());
        assertEquals(teacher.getTeachingSubject(), teacherDto.get().getTeachingSubject());

        //CleanUp
        studentService.delete(id);
    }

    @Test
    void testFindTeacherByName(){
        //Given
        Teacher teacher = new Teacher("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.save(teacher);
        long id = savedTeacher.getId();

        //When
        List<TeacherDto> teacherDtoList = teacherService.findByName("Kowalski","Jan");

        //Then
        assertNotEquals(0, teacherDtoList.size());

        //CleanUp
        teacherService.delete(id);
    }

    @Test
    void testFindTeacherByNameIfFirstnameEmpty(){
        //Given
        Teacher teacher = new Teacher("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.save(teacher);
        long id = savedTeacher.getId();

        //When
        List<TeacherDto> teacherDtoList = teacherService.findByName("Kowalski","");

        //Then
        assertNotEquals(0, teacherDtoList.size());

        //CleanUp
        teacherService.delete(id);
    }

    @Test
    void testFindStudentByNameIfLastnameEmpty(){
        //Given
        Teacher teacher = new Teacher("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.save(teacher);
        long id = savedTeacher.getId();

        //When
        List<TeacherDto> teacherDtoList = teacherService.findByName("","Jan");

        //Then
        assertNotEquals(0, teacherDtoList.size());

        //CleanUp
        teacherService.delete(id);
    }
}
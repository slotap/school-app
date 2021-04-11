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
class DbTeacherServiceTest {
    @Autowired
    private StudentMemberService studentService;
    @Autowired
    private TeacherMemberService teacherService;

    @Test
    void testSaveTeacherToDb() {
        //Given
        TeacherDto teacher = new TeacherDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");

        //When
        TeacherDto savedTeacher = teacherService.saveMember(teacher);
        long id = savedTeacher.getId();

        //Then
        assertTrue(teacherService.getById(id).isPresent());

        //CleanUp
        teacherService.deleteMember(id);
    }

    @Test
    void testDeleteTeacherFromDbWithoutDeletingStudent() {
        //Given
        StudentDto student= new StudentDto("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        TeacherDto teacher = new TeacherDto("Andrzej","Wajda",40,"aWajda@Gmail.com","Historia");
        teacher.getStudents().add(student);
        TeacherDto savedTeacher = teacherService.saveMember(teacher);
        StudentDto savedStudent = studentService.saveMember(student);

        //When
        teacherService.deleteMember(savedTeacher.getId());

        //Then
        assertTrue(teacherService.getById(savedTeacher.getId()).isEmpty());
        assertTrue(studentService.getById(savedStudent.getId()).isPresent());

        //CleanUp
        studentService.deleteMember(savedTeacher.getId());
    }

    @Test
    void testGetTeacherFromDBandReturnDto(){
        //Given
        TeacherDto teacher = new TeacherDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.saveMember(teacher);
        long id = savedTeacher.getId();

        //When
        Optional<TeacherDto> teacherDto = teacherService.getById(id);

        //Then
        assertEquals(teacher.getFirstname(), teacherDto.get().getFirstname());
        assertEquals(teacher.getLastname(), teacherDto.get().getLastname());
        assertEquals(teacher.getAge(), teacherDto.get().getAge());
        assertEquals(teacher.getEmail(), teacherDto.get().getEmail());
        assertEquals(teacher.getTeachingSubject(), teacherDto.get().getTeachingSubject());

        //CleanUp
        studentService.deleteMember(id);
    }

    @Test
    void testFindTeacherByName(){
        //Given
        TeacherDto teacher = new TeacherDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.saveMember(teacher);
        long id = savedTeacher.getId();

        //When
        List<TeacherDto> teacherDtoList = teacherService.getByLastnameFirstname("Kowalski","Jan");

        //Then
        assertNotEquals(0, teacherDtoList.size());

        //CleanUp
        teacherService.deleteMember(id);
    }

    @Test
    void testFindTeacherByNameIfFirstnameEmpty(){
        //Given
        TeacherDto teacher = new TeacherDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.saveMember(teacher);
        long id = savedTeacher.getId();

        //When
        List<TeacherDto> teacherDtoList = teacherService.getByLastnameFirstname("Kowalski","");

        //Then
        assertNotEquals(0, teacherDtoList.size());

        //CleanUp
        teacherService.deleteMember(id);
    }

    @Test
    void testFindStudentByNameIfLastnameEmpty(){
        //Given
        TeacherDto teacher = new TeacherDto("Jan", "Kowalski", 22, "jkowalksi@Gmail.com", "Politologia");
        TeacherDto savedTeacher = teacherService.saveMember(teacher);
        long id = savedTeacher.getId();

        //When
        List<TeacherDto> teacherDtoList = teacherService.getByLastnameFirstname("","Jan");

        //Then
        assertNotEquals(0, teacherDtoList.size());

        //CleanUp
        teacherService.deleteMember(id);
    }
}

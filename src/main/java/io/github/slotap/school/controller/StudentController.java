package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import io.github.slotap.school.service.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final DbService dbService;
    private final StudentMapper studentMapper;

    public StudentController(DbService dbService, StudentMapper studentMapper) {
        this.dbService = dbService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    ResponseEntity<List<StudentDto>> getAllStudents(Pageable page){
        logger.info("Fetching all students - custom pageable");
        List<Student> studentList = dbService.getAll(page).getContent();
        return ResponseEntity.ok(studentMapper.mapToStudentDtoList(studentList));
    }
}

package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import io.github.slotap.school.service.DbStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final DbStudentService dbService;
    private final StudentMapper studentMapper;

    public StudentController(DbStudentService dbService, StudentMapper studentMapper) {
        this.dbService = dbService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents(Pageable page){
        logger.info("Fetching all students - custom pageable");
        List<Student> studentList = dbService.getAll(page).getContent();
        return ResponseEntity.ok(studentMapper.mapToStudentDtoList(studentList));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody @Valid Student createStudent){
        logger.info("Creating new Student");
        Student result = dbService.createStudent(createStudent);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent (@PathVariable long id, @RequestBody @Valid Student toUpdate ){
        logger.info("Updating Student");
        if(!dbService.existById(id)){
            return ResponseEntity.notFound().build();
        }
        dbService.getStudent(id).ifPresent( student -> student.updateStudent(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id){
        logger.info("Deleting Student");
        if(!dbService.existById(id)){
            return ResponseEntity.notFound().build();
        }
        dbService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }


}

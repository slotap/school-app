package io.github.slotap.school.controller;

import io.github.slotap.school.dto.StudentDto;
import io.github.slotap.school.service.StudentMemberService;
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
    private final StudentMemberService studentService;

    public StudentController(StudentMemberService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents(Pageable page) {
        logger.info("Fetching all students - custom pageable");
        List<StudentDto> studentList = studentService.getAllMembers(page);
        return ResponseEntity.ok(studentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable long id) {
        logger.info("Fetching a student");
        return studentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/teachers")
    public ResponseEntity<?> getTeachersForStudentId(@PathVariable long id) {
        logger.info("Filtering all teachers assigned to a selected student");
        return studentService.getById(id)
                .map(StudentDto::getTeachers)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDto>> findStudentsByLastnameAndFirstname(@RequestParam(name = "lastname") String lastName, @RequestParam(name = "firstname") String firstName) {
        logger.info("Looking for searched Students");
        List<StudentDto> resultList = studentService.getByLastnameFirstname(lastName, firstName);
        return ResponseEntity.ok(resultList);
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid StudentDto newStudent) {
        logger.info("Creating new Student");
        StudentDto result = studentService.saveMember(newStudent);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable long id, @RequestBody @Valid StudentDto toUpdate) {
        logger.info("Updating Student");
        return studentService.updateMember(id, toUpdate)
                .map(updatedStudent -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id) {
        logger.info("Deleting Student");
        if (!studentService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}

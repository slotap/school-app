package io.github.slotap.school.controller;

import io.github.slotap.school.dto.TeacherDto;
import io.github.slotap.school.service.TeacherMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final TeacherMemberService teacherService;

    public TeacherController(TeacherMemberService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    ResponseEntity<List<TeacherDto>> getAllTeachers(Pageable page) {
        logger.info("Fetching all teachers - custom pageable");
        List<TeacherDto> teacherList = teacherService.getAllMembers(page);
        return ResponseEntity.ok(teacherList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacher(@PathVariable long id) {
        logger.info("Fetching a teacher");
        return teacherService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<?> getStudentsForTeacherId(@PathVariable long id) {
        logger.info("Filtering all students assigned to a selected teacher");
        return teacherService.getById(id)
                .map(TeacherDto::getStudents)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherDto>> findTeachersByLastnameFirstname(@RequestParam(name = "lastname") String lastName, @RequestParam(name = "firstname") String firstName) {
        logger.info("Looking for searched Teachers");
        List<TeacherDto> resultList = teacherService.getByLastnameFirstname(lastName, firstName);
        return ResponseEntity.ok(resultList);
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody @Valid TeacherDto newTeacher) {
        logger.info("Creating new Teacher");
        TeacherDto result = teacherService.saveMember(newTeacher);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable long id, @RequestBody @Valid TeacherDto toUpdate) {
        logger.info("Updating Teacher");
        return teacherService.updateMember(id, toUpdate)
                .map(updatedTeacher -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable long id) {
        logger.info("Deleting Teacher");
        if (!teacherService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        teacherService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}

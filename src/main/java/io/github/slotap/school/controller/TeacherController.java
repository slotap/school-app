package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.TeacherMapper;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import io.github.slotap.school.service.DbTeacherService;
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
    private final DbTeacherService dbService;
    private final TeacherMapper teacherMapper;

    public TeacherController(DbTeacherService dbService, TeacherMapper teacherMapper) {
        this.dbService = dbService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    ResponseEntity<List<TeacherDto>> getAllTeachers(Pageable page) {
        logger.info("Fetching all teachers - custom pageable");
        List<Teacher> teacherList = dbService.getAll(page).getContent();
        return ResponseEntity.ok(teacherMapper.mapToTeacherDtoList(teacherList));
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody @Valid Teacher newTeacher){
        logger.info("Creating new Teacher");
        Teacher result = dbService.saveTeacher(newTeacher);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(teacherMapper.mapToTeacherDto(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher (@PathVariable long id, @RequestBody @Valid Teacher toUpdate ){
        logger.info("Updating Teacher");
        if(!dbService.existById(id)){
            return ResponseEntity.notFound().build();
        }
        dbService.getTeacher(id).ifPresent(teacher ->{
            teacher.updateTeacher(toUpdate);
            dbService.saveTeacher(teacher);
        });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable long id) {
        logger.info("Deleting Teacher");
        if (!dbService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        dbService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}

package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.TeacherMapper;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import io.github.slotap.school.service.DbTeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

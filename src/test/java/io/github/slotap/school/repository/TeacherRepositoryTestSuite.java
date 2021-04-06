package io.github.slotap.school.repository;

import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherRepositoryTestSuite {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveManyToMany(){
        //Given
        Student janKowalski = new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        Student annaNowak = new Student("Anna","Nowak",20,"anowak@Gmail.com","Historia");
        Student krzysztofIbisz = new Student("Krzysztof","Ibisz",25,"kibisz@Gmail.com","Psychologia");

        Teacher janMikrut = new Teacher("Jan","Mikrut",35,"jmikrut@Gmail.com","Politologia");
        Teacher andrzejWajda = new Teacher("Andrzej","Wajda",40,"aWajda@Gmail.com","Historia");

        andrzejWajda.getStudents().add(janKowalski);
        andrzejWajda.getStudents().add(annaNowak);
        janMikrut.getStudents().add(janKowalski);
        janMikrut.getStudents().add(krzysztofIbisz);

        janKowalski.getTeachers().add(andrzejWajda);
        janKowalski.getTeachers().add(janMikrut);
        annaNowak.getTeachers().add(andrzejWajda);
        krzysztofIbisz.getTeachers().add(janMikrut);

        //When
        teacherRepository.save(janMikrut);
        long janMikrutId = janMikrut.getId();
        teacherRepository.save(andrzejWajda);
        long aWajdaId = andrzejWajda.getId();

        //Then
        assertNotEquals(0, janMikrutId);
        assertNotEquals(0, aWajdaId);

        //CleanUp
      try{
            teacherRepository.deleteById(janMikrutId);
            teacherRepository.deleteById(aWajdaId);
            studentRepository.deleteById(janKowalski.getId());
            studentRepository.deleteById(annaNowak.getId());
            studentRepository.deleteById(krzysztofIbisz.getId());
        }catch (Exception e){
          e.printStackTrace();
        }
    }
}

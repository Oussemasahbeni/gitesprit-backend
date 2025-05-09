package com.esprit.gitesprit.academic.domain.service;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.academic.domain.port.input.AcademicYearUseCases;
import com.esprit.gitesprit.academic.domain.port.input.ClassroomUseCases;
import com.esprit.gitesprit.academic.domain.port.input.SubjectUseCases;
import com.esprit.gitesprit.academic.domain.port.output.Classrooms;
import com.esprit.gitesprit.academic.domain.port.output.Subjects;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class ClassroomService implements ClassroomUseCases {

    private final Classrooms classrooms;
    private final SubjectUseCases subjectUseCases;
    private final Subjects subjects;
    private final AcademicYearUseCases academicYearUseCases;

    @Override
    public Classroom create(Classroom classroom, UUID academicYearId) {
        AcademicYear academicYear = academicYearUseCases.findById(academicYearId);
        classroom.setAcademicYear(academicYear);
        return classrooms.create(classroom);
    }

    @Override
    public Classroom update(Classroom classroom) {
        findById(classroom.getId());
        return classrooms.update(classroom);
    }

    @Override
    public Classroom assignSubject(UUID classroomId, Subject subject) {
        Classroom classroom = findById(classroomId);
        if (classroom.getSubjects().contains(subject)) {
            return classroom;
        }
        subject.setClassroom(classroom);
        classroom.addSubject(subject);
        classrooms.update(classroom);
        return classroom;
    }

    @Override
    public Classroom removeSubject(UUID classroomId, UUID subjectId) {
        Classroom classroom = findById(classroomId);
        Subject subject = subjectUseCases.findById(subjectId);
        if (!checkSubjectInClassroom(classroom, subjectId)) {
            return classroom;
        }
        classroom.removeSubject(subject);
        subject.setClassroom(null);
        subjects.update(subject);
        return classroom;
    }

    @Override
    public Classroom findById(UUID id) {
        return classrooms.findById(id).orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.CLASS_NOT_FOUND));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id);
        classrooms.deleteById(id);
    }

    @Override
    public void deleteAll() {
        classrooms.deleteAll();
    }

    @Override
    public List<Classroom> findAll() {
        return classrooms.findAll();
    }

    @Override
    public Page<Classroom> findAllPaginated(String search, Pageable pageable) {
        return classrooms.findAllPaginated(search, pageable);
    }

    private boolean checkSubjectInClassroom(Classroom classroom, UUID subjectId) {
        return classroom.getSubjects().stream().anyMatch(subject -> subject.getId().equals(subjectId));
    }
}
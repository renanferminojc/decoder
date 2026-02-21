package com.ead.course.repositories.lesson;

import com.ead.course.models.LessonModule;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<LessonModule, UUID> {}

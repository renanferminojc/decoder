package com.ead.course.repositories.course;

import com.ead.course.models.CourseModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseModel, UUID> {}

package kaamsetu.com.repository;

import kaamsetu.com.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication>findByJob_Id(Long jobId);
    boolean existsByJob_IdAndStudent_Id(Long jobId, Long StudentId);
}

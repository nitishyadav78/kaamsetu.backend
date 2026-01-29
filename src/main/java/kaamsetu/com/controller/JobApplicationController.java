package kaamsetu.com.controller;

import kaamsetu.com.DTO.JobApplicationRequest;
import kaamsetu.com.model.*;
import kaamsetu.com.repository.JobApplicationRepository;
import kaamsetu.com.repository.JobRepository;
import kaamsetu.com.repository.UserRepository;
import org.apache.catalina.core.AprLifecycleListener;
import org.hibernate.metamodel.mapping.MappingModelCreationLogging_$logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JobApplicationController {

    private final JobApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    public JobApplicationController(JobApplicationRepository applicationRepository, JobRepository jobRepository, UserRepository userRepository)
    {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/applications")
    public ResponseEntity<?>applyForJobs(@RequestBody JobApplicationRequest req)
    {
        Job job = jobRepository.findById(req.getJobId()).orElse(null);
        if(job == null)
        {
            return ResponseEntity.badRequest().body("Invalid Job Id");
        }

        User student = userRepository.findById(req.getStudentId()).orElse(null);
        if(student == null)
        {
            return ResponseEntity.badRequest().body("Invalid Student");
        }

        if(applicationRepository.existsByJob_IdAndStudent_Id(req.getJobId(), req.getStudentId()))
        {
            return ResponseEntity.badRequest().body("Already Apply for this job");
        }
        if(job.getStatus() != JobStatus.OPEN)
        {
            return ResponseEntity.badRequest().body("Job is not open for application");
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .student(student)
                .status(ApplicationStatus.PENDING)
                .coverMessage(req.getMessage())
                .build();

        JobApplication saved = applicationRepository.save(application);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<List<JobApplication>> getApplicationsForJob(@PathVariable Long jobId)
    {
        List<JobApplication> apps = applicationRepository.findByJob_Id(jobId);


        return ResponseEntity.ok(apps);
    }
    @PutMapping("/applications/{applicationId}/accept")
    public ResponseEntity<?>acceptApplication(@PathVariable Long applicationId)
    {
            JobApplication application = applicationRepository.findById(applicationId).orElse(null);
            if(application == null)
            {
                return ResponseEntity.badRequest().body("Invalid application id");
            }
            if(application.getStatus() != ApplicationStatus.PENDING)
            {
                return ResponseEntity.badRequest().body("Application is already processed");
            }
            Job job = application.getJob();
            if(job == null)
            {
                return ResponseEntity.badRequest().body("Associate job not found");
            }
            if(job.getStatus() != JobStatus.OPEN)
            {
                return ResponseEntity.badRequest().body("Job is not accepting for accepting Order");
            }
        application.setStatus(ApplicationStatus.ACCEPTED);
            job.setStatus(JobStatus.ASSIGNED);
            job.setAssignedTo(application.getStudent());

            List<JobApplication> allApps = applicationRepository.findByJob_Id(job.getId());
            for(JobApplication app : allApps)
            {
                if(!app.getId().equals(application.getId()) && app.getStatus() == ApplicationStatus.PENDING)
                {
                    app.setStatus(ApplicationStatus.REJECTED);
                }
            }

            jobRepository.save(job);
            applicationRepository.saveAll(allApps);
            return ResponseEntity.ok("Application Accepted and job assigned to student");

    }
    // - Marks this single application REJECTED
    @PutMapping("/application/{applicationId}/reject")
    public ResponseEntity<?>rejectApplication(@PathVariable Long ApplicationId)
    {
        JobApplication application = applicationRepository.findById(ApplicationId).orElse(null);
        if(application == null)
        {
            return ResponseEntity.badRequest().body("Invalid application id");
        }
        if(application.getStatus() != ApplicationStatus.PENDING)
        {
            return ResponseEntity.badRequest().body("Application is already proccessd");
        }
        application.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(application);
        return ResponseEntity.ok("Application rejected");
    }
}

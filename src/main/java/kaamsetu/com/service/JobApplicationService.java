package kaamsetu.com.service;

import kaamsetu.com.model.*;
import kaamsetu.com.repository.JobApplicationRepository;
import kaamsetu.com.repository.JobRepository;
import kaamsetu.com.repository.UserRepository;

public class JobApplicationService {
    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    public JobApplicationService(JobApplicationRepository applicationRepository,JobRepository jobRepository, UserRepository userRepository)
    {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }
    //Student Apply karega
    public String applyForJob(Long jobId, Long studentId, String messages)
    {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("job not found"));

        User student = userRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if(applicationRepository.existsByJob_IdAndStudent_Id(jobId, studentId))
        {
            return "You have already applied for this job";
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .student(student)
                .status(ApplicationStatus.PENDING)
                .coverMessage(messages)
                .build();

        applicationRepository.save(application);
        return "Applied Successfully";
    }

    public String acceptApplication(Long applicationId)
    {
        JobApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));

        app.setStatus(ApplicationStatus.ACCEPTED);

        Job job = app.getJob();
        job.setStatus(JobStatus.ASSIGNED);

        job.setAssignedTo(app.getStudent());

        applicationRepository.save(app);
        jobRepository.save(job);

        return  "Application Accepted and Job Assigned";
    }
}

package kaamsetu.com.service;

import kaamsetu.com.DTO.JobCreateRequest;
import kaamsetu.com.DTO.JobSubmitRequest;
import kaamsetu.com.model.Job;
import kaamsetu.com.model.JobStatus;
import kaamsetu.com.model.User;
import kaamsetu.com.repository.JobRepository;
import kaamsetu.com.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository)
    {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }
    public Job createJob(JobCreateRequest req)
    {
        User client = userRepository.findById(req.getClientId())
                .orElseThrow( () -> new RuntimeException("Invalid ClientId, user not found"));

        Job job = Job.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory())
                .budget(req.getBudget())
                .deadline(req.getDeadline())
                .client(client)
                .status(JobStatus.OPEN)
                .build();
        return  jobRepository.save(job);
    }
    //Sare jobs ki list
    public List<Job> getAllJobs()
    {
        return jobRepository.findAll();
    }
    //Only open jobs ki list
    public List<Job> getOpenJobs()
    {
        return jobRepository.findByStatus(JobStatus.OPEN);
    }
    //Job Submit karne ka logic
    public String submitJob(Long jobId, JobSubmitRequest req)
    {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Invalid Job Id"));

        if(job.getStatus() != JobStatus.ASSIGNED)
        {
            return "Job is not Assigned";
        }
        User student = userRepository.findById(req.getStudentId())
                .orElseThrow(() -> new RuntimeException("Invalid Student"));

        // Pehle check karein ki job assigned hai bhi ya nahi
        if (job.getAssignedTo() == null) {
            return "This job has not been assigned to anyone yet";
        }

// Objects.equals use karna best practice hai null-safety ke liye
        if (!job.getAssignedTo().getId().equals(student.getId())) {
            return "You are not assigned to this job";
        }

        job.setSubmissionMessage(req.getMessages());
        job.setSubmissionLink(req.getLink());
        job.setSubmittedAt(LocalDateTime.now());
        job.setStatus(JobStatus.SUBMITTED);

        jobRepository.save(job);
        return  "Job Submitted Successfully";
    }

}

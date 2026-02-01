package kaamsetu.com.controller;

import kaamsetu.com.DTO.JobCreateRequest;
import kaamsetu.com.DTO.JobSubmitRequest;
import kaamsetu.com.model.Job;
import kaamsetu.com.model.JobStatus;
import kaamsetu.com.model.User;
import kaamsetu.com.repository.JobRepository;
import kaamsetu.com.repository.UserRepository;
import kaamsetu.com.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService)
    {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobCreateRequest req)
    {
        try
        {
            return  ResponseEntity.ok(jobService.createJob(req));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<Job>> listJobs()
    {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
    @GetMapping("/open")
    public ResponseEntity<List<Job>> listOpenJobs()
    {
      return ResponseEntity.ok(jobService.getOpenJobs());
    }
    @PutMapping("/{jobId}/submit")
    public ResponseEntity<?>submitJob(@PathVariable Long jobId, @RequestBody JobSubmitRequest req)
    {
        String result = jobService.submitJob(jobId, req);
        if(result.contains("SuccessFully"))
        {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);


        // phle mera controller directly Repository se baat krta hai lekin ab hamne
        /*Job job = jobRepository.findById(jobId).orElse(null);
        //Job Exist or Not
        if(job == null)
        {
            return ResponseEntity.badRequest().body("InValid Job Id");
        }
        //Job Assigned Id
        if(job.getStatus() != JobStatus.ASSIGNED)
        {
            return ResponseEntity.badRequest().body("Job Is Not Assigned");
        }

        //job Student valid or not

        User student = userRepository.findById(req.getStudentId()).orElse(null);
        if(student == null)
        {
            return ResponseEntity.badRequest().body("Invalid Student");
        }


        if(job.getAssignedTo().getId() != student.getId())
        {
            return ResponseEntity.badRequest().body("You are not assigned to this job");
        }

        job.setSubmissionMessage(req.getMessages());
        job.setSubmissionLink(req.getLink());
        job.setSubmittedAt(LocalDateTime.now());

        job.setStatus(JobStatus.SUBMITTED);
        jobRepository.save(job);

        return ResponseEntity.ok("Job Submitted Successfully");

         */

    }
}

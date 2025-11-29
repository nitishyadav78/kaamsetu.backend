package kaamsetu.com.controller;

import kaamsetu.com.DTO.JobCreateRequest;
import kaamsetu.com.model.Job;
import kaamsetu.com.model.JobStatus;
import kaamsetu.com.model.User;
import kaamsetu.com.repository.JobRepository;
import kaamsetu.com.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobRepository jobRepository;

    private final UserRepository userRepository;



    public JobController(JobRepository jobRepository, UserRepository userRepository)
    {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobCreateRequest req)
    {
        Optional<User>clientOpt = userRepository.findById(req.getClientId());
        if(clientOpt.isEmpty())
        {
            return ResponseEntity.badRequest().body("Invalid ClientId, user not found");

        }

        User client = clientOpt.get();

        Job job = Job.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory())
                .budget(req.getBudget())
                .deadline(req.getDeadline())
                .client(client)
                .status(JobStatus.OPEN)
                .build();

        Job saved = jobRepository.save(job);
        return ResponseEntity.ok(saved);
    }


    @GetMapping
    public ResponseEntity<List<Job>> listJobs()
    {
        List<Job> jobs = jobRepository.findAll();
        return  ResponseEntity.ok(jobs);
    }
    @GetMapping("/open")
    public ResponseEntity<List<Job>> listOpenJobs()
    {
        List<Job> openJobs = jobRepository.findByStatus(JobStatus.OPEN);
        return ResponseEntity.ok(openJobs);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?>getJob(@PathVariable Long id)
    {
        return jobRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

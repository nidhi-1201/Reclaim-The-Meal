package com.ReclaimTheMeal;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulingTasks {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    
    @Scheduled(cron = "0 0/5 * * * ?")
    public void deleteTimedOutPosts() {
        List<Post> posts = postRepository.selectByDateTimeEquals(new Date());
        if(posts.size() != 0){
            for (Post post : posts) {
                User user = post.getUser();
                if(user != null){
                    try{
                        emailSenderService.sendEmail("reclaimthemeal@gmail.com", user.getEmail(), "Sending email using Spring Booot", "Hey, How are you!! Sending an email for testing purpose!!");
                        postRepository.deleteByDateTimeEquals(new Date());
                    }
                    catch(MailException e) {
                        throw e;
                    }
                }
            }
        }
    }
}

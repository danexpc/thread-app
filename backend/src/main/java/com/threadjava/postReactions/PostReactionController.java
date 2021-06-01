package com.threadjava.postReactions;

import com.threadjava.postReactions.dto.ReceivedPostReactionDto;
import com.threadjava.postReactions.dto.ResponsePostReactionDto;
import com.threadjava.postReactions.model.PostReaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static com.threadjava.auth.TokenService.getUserId;

@RestController
@RequestMapping("/api/postreaction")
public class PostReactionController {
    @Autowired
    private PostReactionService postsService;
    @Autowired
    private SimpMessagingTemplate template;

    @PutMapping
    public Optional<ResponsePostReactionDto> setReaction(@RequestBody ReceivedPostReactionDto postReaction){
        postReaction.setUserId(getUserId());
        var reaction = postsService.setReaction(postReaction);

        if (reaction.isPresent() && reaction.get().getUserId() != getUserId()) {
            // notify a user if someone (not himself) liked his post
            template.convertAndSend("/topic/like", "Your post was liked!");
        }
        return reaction;
    }

    @GetMapping("/{postId}")
    public Optional<ResponsePostReactionDto> getReaction(@PathVariable UUID postId) {
        return postsService.getReaction(postId, getUserId());
    }
}

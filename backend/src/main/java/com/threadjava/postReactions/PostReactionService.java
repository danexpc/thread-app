package com.threadjava.postReactions;

import com.threadjava.postReactions.dto.ReceivedPostReactionDto;
import com.threadjava.postReactions.dto.ResponsePostReactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PostReactionService {
    @Autowired
    private PostReactionsRepository postReactionsRepository;

    public Optional<ResponsePostReactionDto> setReaction(ReceivedPostReactionDto postReactionDto) {

        var reaction = postReactionsRepository.getPostReaction(postReactionDto.getUserId(), postReactionDto.getPostId());

        if (reaction.isPresent()) {
            var react = reaction.get();
            if (react.getIsLike() == postReactionDto.getIsLike()) {
                postReactionsRepository.deleteById(react.getId());
                return Optional.empty();
            } else {
                react.setIsLike(postReactionDto.getIsLike());
                var result = postReactionsRepository.save(react);
                return Optional.of(PostReactionMapper.MAPPER.reactionToPostReactionDto(result));
            }
        } else {
            var postReaction = PostReactionMapper.MAPPER.dtoToPostReaction(postReactionDto);
            var result = postReactionsRepository.save(postReaction);
            return Optional.of(PostReactionMapper.MAPPER.reactionToPostReactionDto(result));
        }
    }

    public Optional<ResponsePostReactionDto> getReaction(UUID postId, UUID userId) {
        var reaction = postReactionsRepository.getPostReaction(userId, postId);

        return reaction.map(PostReactionMapper.MAPPER::reactionToPostReactionDto);
    }
}

package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Comment;
import com.laclippers.los.entity.Post;
import com.laclippers.los.entity.User;
import com.laclippers.los.repository.CommentRepository;
import com.laclippers.los.repository.PostRepository;
import com.laclippers.los.repository.UserRepository;
import com.laclippers.los.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/posts")
    public Result<List<Post>> listPosts() {
        return Result.ok(postRepository.findAllByOrderByCreatedAtDesc());
    }

    @GetMapping("/posts/user/{userId}")
    public Result<List<Post>> postsByUser(@PathVariable Long userId) {
        return Result.ok(postRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @PostMapping("/posts")
    public Result<Post> createPost(@RequestBody Post post) {
        Long userId = AuthUtil.currentUserId();
        String username;
        if (userId == null) {
            username = "游客" + (int) (Math.random() * 1000);
        } else {
            username = userRepository.findById(userId).map(User::getUsername).orElse("球迷");
            post.setUserId(userId);
        }
        post.setId(null);
        post.setUsername(username);
        post.setLikes(0);
        post.setComments(0);
        post.setViews(0);
        post.setTime("刚刚");
        post.setCreatedAt(LocalDateTime.now());
        if (post.getTag() == null) post.setTag("讨论");
        if (post.getTagClass() == null) post.setTagClass("discussion");
        return Result.ok(postRepository.save(post));
    }

    @PostMapping("/posts/{id}/like")
    public Result<Integer> like(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "帖子不存在"));
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
        return Result.ok(post.getLikes());
    }

    @PostMapping("/posts/{id}/views")
    public Result<Integer> views(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "帖子不存在"));
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        return Result.ok(post.getViews());
    }

    @GetMapping("/posts/{id}/comments")
    public Result<List<Comment>> comments(@PathVariable Long id) {
        return Result.ok(commentRepository.findByPostId(id));
    }

    @PostMapping("/posts/{id}/comments")
    public Result<Comment> addComment(@PathVariable Long id, @RequestBody Comment comment) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "帖子不存在"));
        Long userId = AuthUtil.currentUserId();
        String username;
        if (userId == null) {
            username = "游客";
        } else {
            username = userRepository.findById(userId).map(User::getUsername).orElse("球迷");
        }
        comment.setId(null);
        comment.setPostId(id);
        comment.setUsername(username);
        comment.setCreatedAt(LocalDateTime.now());
        post.setComments(post.getComments() + 1);
        postRepository.save(post);
        return Result.ok(commentRepository.save(comment));
    }
}
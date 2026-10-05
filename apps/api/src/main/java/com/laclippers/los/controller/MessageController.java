package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Message;
import com.laclippers.los.entity.User;
import com.laclippers.los.repository.MessageRepository;
import com.laclippers.los.repository.UserRepository;
import com.laclippers.los.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/conversations")
    public Result<List<Map<String, Object>>> conversations() {
        Long me = requireUser();
        List<Message> all = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtAsc(me, me);

        Map<Long, List<Message>> grouped = new LinkedHashMap<>();
        for (Message m : all) {
            Long partner = m.getSenderId().equals(me) ? m.getReceiverId() : m.getSenderId();
            grouped.computeIfAbsent(partner, k -> new ArrayList<>()).add(m);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, List<Message>> e : grouped.entrySet()) {
            Long partnerId = e.getKey();
            List<Message> msgs = e.getValue();
            Message last = msgs.get(msgs.size() - 1);
            long unread = msgs.stream()
                    .filter(m -> m.getReceiverId().equals(me) && !Boolean.TRUE.equals(m.getIsRead()))
                    .count();
            User u = userRepository.findById(partnerId).orElse(null);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("userId", partnerId);
            item.put("username", u != null ? u.getUsername() : "球迷");
            item.put("avatar", u != null ? u.getAvatar() : null);
            item.put("lastMessage", last.getContent());
            item.put("lastTime", last.getCreatedAt());
            item.put("unread", unread);
            result.add(item);
        }
        result.sort((a, b) -> ((LocalDateTime) b.get("lastTime")).compareTo((LocalDateTime) a.get("lastTime")));
        return Result.ok(result);
    }

    @GetMapping("/{userId}")
    public Result<List<Message>> thread(@PathVariable Long userId) {
        Long me = requireUser();
        List<Message> msgs = messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtAsc(me, me).stream()
                .filter(m -> m.getSenderId().equals(userId) || m.getReceiverId().equals(userId))
                .collect(Collectors.toList());
        for (Message m : msgs) {
            if (m.getReceiverId().equals(me) && !Boolean.TRUE.equals(m.getIsRead())) {
                m.setIsRead(true);
                messageRepository.save(m);
            }
        }
        return Result.ok(msgs);
    }

    @PostMapping
    public Result<Message> send(@RequestBody Message req) {
        Long me = requireUser();
        if (req.getReceiverId() == null) {
            throw new BusinessException(400, "缺少接收人");
        }
        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            throw new BusinessException(400, "消息内容不能为空");
        }
        if (req.getReceiverId().equals(me)) {
            throw new BusinessException(400, "不能给自己发私信");
        }
        Message m = new Message();
        m.setSenderId(me);
        m.setReceiverId(req.getReceiverId());
        m.setContent(req.getContent().trim());
        m.setIsRead(false);
        m.setCreatedAt(LocalDateTime.now());
        return Result.ok(messageRepository.save(m));
    }

    private Long requireUser() {
        Long userId = AuthUtil.currentUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}
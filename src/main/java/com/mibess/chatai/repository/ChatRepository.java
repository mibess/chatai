package com.mibess.chatai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mibess.chatai.domain.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<List<Chat>> findByChatId(String chatId);

}

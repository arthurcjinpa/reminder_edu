package com.taskmanager.taskappmongo.telegram.repository;

import com.taskmanager.taskappmongo.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserProfile, String> {
     @Query("{'chatId': ?0}")
     Optional<String> findIdByChatId(long chatId);
}

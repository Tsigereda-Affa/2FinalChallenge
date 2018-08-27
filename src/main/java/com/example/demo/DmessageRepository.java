package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface DmessageRepository extends CrudRepository<Dmessage, Long>{
    ArrayList<Dmessage> findByusername(String username);
//    ArrayList<Dmessage> findAllBySendby(User sendby);
//    ArrayList<Dmessage> findAllBySendto(User sendto);
}

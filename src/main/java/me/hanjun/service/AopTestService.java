package me.hanjun.service;


import me.hanjun.domain.Entity;
import me.hanjun.repository.MyRepository;
import me.hanjun.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AopTestService {

    @Autowired
    private MyRepository myRepository;

    @Transactional
    public void publicMethod() {
        System.out.println("Public method started");
        saveEntity(); // private 메서드 호출
        System.out.println("Public method finished");
    }

    private void saveEntity() {
        System.out.println("Saving entity");
        Entity entity = new Entity();
        myRepository.save(entity); // 트랜잭션 내에서 실행


    }


}

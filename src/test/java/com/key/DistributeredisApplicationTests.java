package com.key;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeredisApplicationTests {

    @Test
    public void contextLoads() {

    }

    @Test
    public void redisconnectTest() throws JsonProcessingException {
//        for(int i = 0 ; i<50; i++){
//            new RedisUtils().setObject("test201"+i,"q12312sss32");
//        }

        DDs s = new DDs();
        s.setName("zhangsan");
        s.setNumber(1);

        String body = new ObjectMapper().writeValueAsString(s);
        System.out.println(body);
    }



}

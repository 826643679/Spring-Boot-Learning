package com.example.redismq;

import com.example.dao.RedisDao;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class RedismqApplicationTests {

    @Test
    void contextLoads() {
    }

    public static Logger logger= LoggerFactory.getLogger(RedismqApplicationTests.class);

    @Autowired
    RedisDao redisDao;
    @Test
    public void testRedis(){
        redisDao.setKey("name","luhao");
        redisDao.setKey("age","18");
        logger.info(redisDao.getValue("name"));
        logger.info(redisDao.getValue("age"));
    }

}

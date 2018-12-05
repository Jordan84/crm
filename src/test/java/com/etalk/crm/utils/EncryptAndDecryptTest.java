package com.etalk.crm.utils;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EncryptAndDecryptTest {

    @Test
    public void encrypt() {
        List<Map<String, Object>> dataList = new ArrayList<>();
//		Map<String,Object> map=new HashMap<>();
//		map.put("loginId", 7009);
//		map.put("loginName", "s0005");
//		map.put("roleId", 3);
//		map.put("storesId", 1);
//		map.put("tokenString", EncryptAndDecrypt.encrypt(JSON.toJSONString(map)));
//		map.put("pictures", "public/img/head/s0005_7009.jpg");
//		map.remove("loginId");
//		map.remove("roleId");
//		map.remove("storesId");
//		dataList.add(map);
//
//		Map<String,Object> map1=new HashMap<>();
//		map1.put("loginId", 8775);
//		map1.put("loginName", "s00051");
//		map1.put("roleId", 3);
//		map1.put("storesId", 1);
//		map1.put("tokenString", EncryptAndDecrypt.encrypt(JSON.toJSONString(map1)));
//		map1.put("pictures", "public/img/head/s00051_8775.jpg");
//		map1.remove("loginId");
//		map1.remove("roleId");
//		map1.remove("storesId");
//		dataList.add(map1);

        Map map2 = new HashMap();
        map2.put("loginId", 64927);
        map2.put("loginName", "etalk_Terwer");
        map2.put("roleId", 3);
        map2.put("storeId", 43);
        dataList.add(map2);

        System.out.println(EncryptAndDecrypt.encrypt(JSON.toJSONString(dataList)));
        System.out.println(EncryptAndDecrypt.decrypt(EncryptAndDecrypt.encrypt(JSON.toJSONString(dataList))));
    }

    @Test
    public void decrypt() {
        System.out.println(EncryptAndDecrypt.decrypt("4_N0K4YT88bOaa5Tbr89X51-U79_u_XH0CN1DujmZkArMn489cemHlNIHN82hOeve0okB-QukLkpYPHt5MuuPA"));
    }

    @Test
    public void MD5() {
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("loginId", 74355);
        map.put("loginName", "stestweixin");
        map.put("roleId", 3);
        map.put("storeId", 1);
        System.out.println(EncryptAndDecrypt.encrypt(JSON.toJSONString(map)));
        System.out.println(EncryptAndDecrypt.decrypt(EncryptAndDecrypt.encrypt(JSON.toJSONString(map))));
    }
}
package com.tools;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Specialist;

/**
 * Created by Admin on 14.02.2019.
 */
public class Constant {

    private static final String INSTANCE_NAME = "mainInstance";
    private static final String MAP_NAME = "userMap";

    public static void createInstanceAndMap(){
        Config config = new Config();
        config.setInstanceName(INSTANCE_NAME);
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(MAP_NAME);
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }

    public static void fillMap(Specialist specialist, String login, String password, IMap map) {
        String key = new Encryptor().genRandString();
        String vector = new Encryptor().genRandString();
        map.put("key", key);
        map.put("vector", vector);
        map.put("login", new Encryptor().encrypt(key, vector, login));
        map.put("password", new Encryptor().encrypt(key, vector, password));
        map.put("id", specialist.getId());
        map.put("name", specialist.getName());
        map.put("surname", specialist.getSurname());
        map.put("pageIndex", "1");
    }

    public static String[] getAuth(){
        IMap map = Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME).getMap(MAP_NAME);
        String[] auth = new String[2];
        map.set("testN", "testname");
        map.set("testPass", "testpass");
        auth[0] = map.get("testN").toString();
        auth[1] = map.get("testPass").toString();
        return auth;
    }
}

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

    public static HazelcastInstance getInstance(){
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME);
    }

    public static IMap getMap(){
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME).getMap(MAP_NAME);
    }

    public static void fillMap(Specialist specialist, String login, String password) {
        String key = new Encryptor().genRandString();
        String vector = new Encryptor().genRandString();
        getMap().put("key", key);
        getMap().put("vector", vector);
        getMap().put("login", new Encryptor().encrypt(key, vector, login));
        getMap().put("password", new Encryptor().encrypt(key, vector, password));
        getMap().put("id", specialist.getId());
        getMap().put("name", specialist.getName());
        getMap().put("surname", specialist.getSurname());
        getMap().put("pageIndex", "1");
    }

    public static String[] getAuth(){
        String[] auth = new String[2];
        auth[0] = new Encryptor().decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("login").toString());
        auth[1] = new Encryptor().decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("password").toString());
        return auth;
    }
}

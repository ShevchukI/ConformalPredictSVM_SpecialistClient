package com.tools;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.DataSet;
import com.models.SpecialistEntity;

public class HazelCastMap {
    private static final String INSTANCE_NAME = "mainSpecialistInstance";
    private static final String SPECIALIST_MAP_NAME = "specialist";
    private static final String USER_MAP_NAME = "authorizationSpecialist";
    private static final String DATASET_MAP_NAME = "dataSetSpecialist";
    private static final String KEY_MAP_NAME = "keySpecialist";
    private static final String MISCELLANEOUS_MAP_NAME = "miscSpecialist";

    private static HazelcastInstance hazelcastInstance;

    public static void createInstanceAndMap() {
        Config config = new Config();
        config.setInstanceName(INSTANCE_NAME);
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(1488);
        config.addMapConfig(createMapWithName(USER_MAP_NAME));
        config.addMapConfig(createMapWithName(DATASET_MAP_NAME));
        config.addMapConfig(createMapWithName(SPECIALIST_MAP_NAME));
        config.addMapConfig(createMapWithName(KEY_MAP_NAME));
        config.addMapConfig(createMapWithName(MISCELLANEOUS_MAP_NAME));
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }

    private static MapConfig createMapWithName(String mapName) {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(mapName);
        return mapConfig;
    }

//    public static void fillMap(SpecialistEntity specialistEntity, String login, String password) {
//        String key = new Encryptor().genRandString();
//        String vector = new Encryptor().genRandString();
//        getMapByName(KEY_MAP_NAME).put("key", key);
//        getMapByName(KEY_MAP_NAME).put("vector", vector);
//        getMapByName(USER_MAP_NAME).put("login", new Encryptor().encrypt(key, vector, login));
//        getMapByName(USER_MAP_NAME).put("password", new Encryptor().encrypt(key, vector, password));
//        getMapByName(USER_MAP_NAME).put("id", specialistEntity.getId());
//        getMapByName(USER_MAP_NAME).put("name", specialistEntity.getName());
//        getMapByName(USER_MAP_NAME).put("surname", specialistEntity.getSurname());
//        getMapByName(MISCELLANEOUS_MAP_NAME).put("pageIndexAllDataSet", "1");
//        getMapByName(MISCELLANEOUS_MAP_NAME).put("pageIndexMyDataSet", "1");
//        getMapByName(MISCELLANEOUS_MAP_NAME).put("pageIndexAllConfiguration", "1");
//        getMapByName(MISCELLANEOUS_MAP_NAME).put("pageIndexMyConfiguration", "1");
//    }

//    public static void fillMap(SpecialistEntity specialistEntity, String[] authorization) {
//        String key = new Encryptor().genRandString();
//        String vector = new Encryptor().genRandString();
//        getMapByName(KEY_MAP_NAME).put(Constant.KEY, key);
//        getMapByName(KEY_MAP_NAME).put(Constant.VECTOR, vector);
//        getMapByName(USER_MAP_NAME).put(Constant.LOGIN, Encryptor.encrypt(key, vector, authorization[0]));
//        getMapByName(USER_MAP_NAME).put(Constant.PASSWORD, Encryptor.encrypt(key, vector, authorization[1]));
//
//        getSpecialistMap().put(1, specialistEntity);
//
//        getMapByName(MISCELLANEOUS_MAP_NAME).put(Constant.PAGE_INDEX_ALL_DATASET, 1);
//        getMapByName(MISCELLANEOUS_MAP_NAME).put(Constant.PAGE_INDEX_MY_DATASET, 1);
//        getMapByName(MISCELLANEOUS_MAP_NAME).put(Constant.PAGE_INDEX_ALL_MODEL, 1);
//        getMapByName(MISCELLANEOUS_MAP_NAME).put(Constant.PAGE_INDEX_MY_MODEL, 1);
//    }

    public static HazelcastInstance getInstance() {
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME);
    }

    public static IMap getMapByName(String mapName) {
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME).getMap(mapName);
    }



    public static void clearInstance() {
        getMapByName(USER_MAP_NAME).clear();
        getMapByName(DATASET_MAP_NAME).clear();
        getMapByName(KEY_MAP_NAME).clear();
        getMapByName(SPECIALIST_MAP_NAME).clear();
        getMapByName(MISCELLANEOUS_MAP_NAME).clear();
    }

//    public static String getDataSetMapName() {
//        return DATASET_MAP_NAME;
//    }

    public static String getUserMapName() {
        return USER_MAP_NAME;
    }

    public static String getKeyMapName() {
        return KEY_MAP_NAME;
    }

    public static String getMiscellaneousMapName() {
        return MISCELLANEOUS_MAP_NAME;
    }

    public static IMap<Integer, SpecialistEntity> getSpecialistMap() {
        return hazelcastInstance.getMap(SPECIALIST_MAP_NAME);
    }


    public static IMap<String, Integer> getMiscellaneousMap() {
        return hazelcastInstance.getMap(MISCELLANEOUS_MAP_NAME);
    }


//    public static void changePassword(String password){
//        HazelCastMap.getMapByName(HazelCastMap.getUserMapName()).put(Constant.PASSWORD,
//                Encryptor.encrypt(HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(Constant.KEY).toString(),
//                HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(Constant.VECTOR).toString(),
//                password));
//    }

//    public static void changeUserInformation(SpecialistEntity specialistEntity){
//        getSpecialistMap().put(1, specialistEntity);
//    }

    public static IMap<Integer, DataSet> getDataSetMap(){
        return hazelcastInstance.getMap(DATASET_MAP_NAME);
    }

}

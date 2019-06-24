package com.tools;

import com.models.DataSet;
import com.models.SpecialistEntity;

import java.util.HashMap;
import java.util.Map;

public class GlobalMap {
    private static GlobalMap ourInstance = new GlobalMap();

    private static Map<String, String> userMap;
    private static Map<Integer, SpecialistEntity> specialistMap;
    private static Map<Integer, DataSet> dataSetMap;
    private static Map<String, String> keyMap;
    private static Map<String, String> miscMap;

    public static GlobalMap getInstance() {
        return ourInstance;
    }

    public static void fillMap(String[] authorization){
        String key = new Encryptor().genRandString();
        String vector = new Encryptor().genRandString();
        getKeyMap().put(Constant.KEY, key);
        getKeyMap().put(Constant.VECTOR, vector);
        getUserMap().put(Constant.LOGIN, Encryptor.encrypt(key, vector, authorization[0]));
        getUserMap().put(Constant.PASSWORD, Encryptor.encrypt(key, vector, authorization[1]));
        getMiscMap().put(Constant.PAGE_INDEX_ALL_DATASET, "1");
        getMiscMap().put(Constant.PAGE_INDEX_MY_DATASET, "1");
        getMiscMap().put(Constant.PAGE_INDEX_ALL_MODEL, "1");
        getMiscMap().put(Constant.PAGE_INDEX_MY_MODEL, "1");
    }

    public static Map<String, String> getUserMap() {
        return userMap;
    }

    public static Map<Integer, SpecialistEntity> getSpecialistMap() {
        return specialistMap;
    }

    public static Map<Integer, DataSet> getDataSetMap() {
        return dataSetMap;
    }

    public static Map<String, String> getKeyMap() {
        return keyMap;
    }

    public static Map<String, String> getMiscMap() {
        return miscMap;
    }

    private GlobalMap() {
        userMap = new HashMap<>();
        specialistMap = new HashMap<>();
        dataSetMap = new HashMap<>();
        keyMap = new HashMap<>();
        miscMap = new HashMap<>();
    }
}

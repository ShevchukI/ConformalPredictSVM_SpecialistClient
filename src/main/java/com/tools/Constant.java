package com.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.SVMParameter;
import com.models.Specialist;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Admin on 14.02.2019.
 */
public class Constant {

    private static final String INSTANCE_NAME = "mainInstance";
    private static final String USER_MAP_NAME = "user";
    private static final String DATASET_MAP_NAME = "dataset";
    private static final String KEY_MAP_NAME = "key";
    private static final String MISCELLANEOUS_MAP_NAME = "misc";

    private static final int SVM_DEGREE = 3;
    private static final double SVM_GAMMA = 1;
    private static final double SVM_C = 1;
    private static final double SVM_NU = 0.5;
    private static final double SVM_EPS = 0.001;



    public static void createInstanceAndMap() {
        Config config = new Config();
        config.setInstanceName(INSTANCE_NAME);
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(1488);
        config.addMapConfig(createMapWithName(USER_MAP_NAME));
        config.addMapConfig(createMapWithName(DATASET_MAP_NAME));
        config.addMapConfig(createMapWithName(KEY_MAP_NAME));
        config.addMapConfig(createMapWithName(MISCELLANEOUS_MAP_NAME));
//        MapConfig mapConfig = new MapConfig();
//        mapConfig.setName(USER_MAP_NAME);
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }

    public static HazelcastInstance getInstance() {
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME);
    }

//    public static IMap getMap() {
//        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME).getMap(USER_MAP_NAME);
//    }

    public static IMap getMapByName(String mapName){
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME).getMap(mapName);
    }

    private static MapConfig createMapWithName(String mapName){
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(mapName);
        return mapConfig;
    }

    public static void fillMap(Specialist specialist, String login, String password) {
        String key = new Encryptor().genRandString();
        String vector = new Encryptor().genRandString();
        getMapByName("key").put("key", key);
        getMapByName("key").put("vector", vector);
        getMapByName("user").put("login", new Encryptor().encrypt(key, vector, login));
        getMapByName("user").put("password", new Encryptor().encrypt(key, vector, password));
        getMapByName("user").put("id", specialist.getId());
        getMapByName("user").put("name", specialist.getName());
        getMapByName("user").put("surname", specialist.getSurname());
        getMapByName("misc").put("pageIndexAllDataset", "1");
        getMapByName("misc").put("pageIndexMyDataset", "1");
        getMapByName("misc").put("pageIndexAllConfiguration", "1");
        getMapByName("misc").put("pageIndexMyConfiguration", "1");

//        getMap().put("key", key);
//        getMap().put("vector", vector);
//        getMap().put("login", new Encryptor().encrypt(key, vector, login));
//        getMap().put("password", new Encryptor().encrypt(key, vector, password));
//        getMap().put("id", specialist.getId());
//        getMap().put("name", specialist.getName());
//        getMap().put("surname", specialist.getSurname());
//        getMap().put("pageIndex", "1");
    }

    public static String[] getAuth() {
        String[] auth = new String[2];
        auth[0] = new Encryptor().decrypt(getMapByName("key").get("key").toString(),
                getMapByName("key").get("vector").toString(), getMapByName("user").get("login").toString());
        auth[1] = new Encryptor().decrypt(getMapByName("key").get("key").toString(),
                getMapByName("key").get("vector").toString(), getMapByName("user").get("password").toString());
        return auth;
    }

    public static ArrayList<SVMParameter> fillKernelType(HttpResponse response) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        DataInputStream dataInputStream = new DataInputStream(response.getEntity().getContent());
        String line;
        while ((line = dataInputStream.readLine()) != null) {
            stringBuilder.append(line);
        }
        dataInputStream.close();
        String json = stringBuilder.toString();
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<SVMParameter>>() {
        }.getType();
        ArrayList<SVMParameter> allTypes = gson.fromJson(json, founderListType);
        ArrayList<SVMParameter> SVCkernelTypes = new ArrayList<>();
        for(SVMParameter svmParameter: allTypes){
            if(svmParameter.getName().equals("LINEAR")
                    || svmParameter.getName().equals("POLY")
                    || svmParameter.getName().equals("RBF")
                    || svmParameter.getName().equals("SIGMOID")){
                SVCkernelTypes.add(svmParameter);
            }
        }
        Collections.sort(SVCkernelTypes, SVMParamNameComparator);
        return SVCkernelTypes;
    }

    public static Comparator<SVMParameter> SVMParamNameComparator = new Comparator<SVMParameter> (){

        @Override
        public int compare(SVMParameter param1, SVMParameter param2) {
            String parameterName1 = param1.getName().toUpperCase();
            String parameterName2 = param2.getName().toUpperCase();
            return parameterName1.compareTo(parameterName2);
        }
    };

    public static int getCountSplitString(String string, String delimeter){
        return string.split(delimeter).length;
    }

    public static int getSvmDegree() {
        return SVM_DEGREE;
    }

    public static double getSvmGamma(int columnCount) {
        return SVM_GAMMA/columnCount;
    }

    public static double getSvmC() {
        return SVM_C;
    }

    public static double getSvmNu() {
        return SVM_NU;
    }

    public static double getSvmEps() {
        return SVM_EPS;
    }

    public static double formatterSliderValueToDouble(double text, String pattern){
        DecimalFormat formatter = new DecimalFormat(pattern);
        String string = formatter.format(text);
        return Double.parseDouble(string.replace(",", "."));
    }

    public static String responseToString(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
//        return  EntityUtils.toString(response.getEntity());
        return  content;
    }
}

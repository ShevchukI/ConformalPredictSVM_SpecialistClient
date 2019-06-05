package com.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.config.MapConfig;
import com.models.Predict;
import com.models.SVMParameter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.*;


public class Constant {

    //Hazelcast
    public final static String KEY = "key";
    public final static String VECTOR = "vector";
    public final static String LOGIN = "login";
    public final static String PASSWORD = "password";
    public final static String PAGE_INDEX_ALL_DATASET = "pageIndexAllDataSet";
    public final static String PAGE_INDEX_MY_DATASET = "pageIndexMyDataSet";
    public final static String PAGE_INDEX_ALL_MODEL = "pageIndexAllModel";
    public final static String PAGE_INDEX_MY_MODEL = "pageIndexMyModel";



//    private static final String INSTANCE_NAME = "mainSpecialistInstance";
//    private static final String USER_MAP_NAME = "specialist";
//    private static final String DATASET_MAP_NAME = "dataSet";
//    private static final String KEY_MAP_NAME = "key";
//    private static final String MISCELLANEOUS_MAP_NAME = "misc";

    //Icons
    private final static String SIGN_IN_ICON = "/img/icons/signIn.png";
    private final static String APPLICATION_ICON = "img/icons/icon.png";
    private final static String ADD_ICON = "img/icons/add.png";
    private final static String CANCEL_ICON = "img/icons/cancel.png";
    private final static String DELETE_ICON = "img/icons/delete.png";
    private final static String INFO_ICON = "img/icons/info.png";
    private final static String OK_ICON = "img/icons/ok.png";
    private final static String RETURN_ICON = "img/icons/return.png";
    private final static String RUN_ICON = "img/icons/run.png";
    private final static String SEARCH_ICON = "img/icons/search.png";
    private final static String EDIT_ICON = "img/icons/edit.png";


    private static final int SVM_DEGREE = 3;
    private static final double SVM_GAMMA = 1;
    private static final double SVM_C = 1;
    private static final double SVM_NU = 0.5;
    private static final double SVM_EPS = 0.001;

    private static final int OBJECT_ON_PAGE = 30;

    private final static String LOCALHOST_URL = "http://localhost:8888";
    //    private final static String LOCALHOST_URL = "http://111810cd.ngrok.io";
//    private final static String LOCALHOST_URL = "http://495cad62.ngrok.io/";
    private final static String DOCTOR_URL = "/doctor-system/specialist";
    private final static String URL = LOCALHOST_URL + DOCTOR_URL;

    protected static String getLocalhostUrl() {
        return LOCALHOST_URL;
    }

    public static String getUrl(){
        return URL;
    }

    public static HttpResponse crudEntity(HttpEntity httpEntity, HttpPost post, HttpGet get, HttpPut put, HttpDelete delete) {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((Constant.getAuth()[0] + ":" + Constant.getAuth()[1]).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpRequestBase request = null;
        if (post != null) {

            post.setHeader("Content-Type", "application/json");
            if(httpEntity!=null) {
                post.setEntity(httpEntity);
            }
            request = post;

        } else if (put != null) {

            put.setHeader("Content-Type", "application/json");
            if(httpEntity!=null) {
                put.setEntity(httpEntity);
            }
            request = put;

        } else if (get != null) {
            request = get;
        } else if (delete != null) {
            delete.setHeader("Content-Type", "application/json");
            request = delete;
        }
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static boolean checkStatusCode(int statusCode) {
        switch (statusCode) {
            case 200:
                return true;
            case 201:
                return true;
            case 401:
                Constant.getAlert("Unauthorized: login or password incorrect!",
                        "Error code: " + statusCode, Alert.AlertType.ERROR);
                return false;
            case 404:
                Constant.getAlert("Error!", String.valueOf(statusCode), Alert.AlertType.ERROR);
                return false;
            case 423:
                Constant.getAlert(null, "Not allow!", Alert.AlertType.ERROR);
                return false;
            case 504:
                Constant.getAlert("Connection to the server is missing!",
                        "Error code: " + statusCode, Alert.AlertType.ERROR);
                return false;
            default:
                Constant.getAlert(null,
                        "Error code: " + statusCode, Alert.AlertType.ERROR);
                return false;
        }
    }

//    public static void createInstanceAndMap() {
//        Config config = new Config();
//        config.setInstanceName(INSTANCE_NAME);
//        NetworkConfig networkConfig = config.getNetworkConfig();
//        networkConfig.setPort(1488);
//        config.addMapConfig(createMapWithName(USER_MAP_NAME));
//        config.addMapConfig(createMapWithName(DATASET_MAP_NAME));
//        config.addMapConfig(createMapWithName(KEY_MAP_NAME));
//        config.addMapConfig(createMapWithName(MISCELLANEOUS_MAP_NAME));
//        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
//    }

//    public static HazelcastInstance getInstance() {
//        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME);
//    }

//    public static IMap getMapByName(String mapName) {
//        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME).getMap(mapName);
//    }

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

    public static String[] getAuth() {
        String[] auth = new String[2];
        String login = Encryptor.decrypt(HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(KEY).toString(),
                HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(VECTOR).toString(), HazelCastMap.getMapByName(HazelCastMap.getUserMapName()).get(LOGIN).toString());
        String password = Encryptor.decrypt(HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(KEY).toString(),
                HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(VECTOR).toString(), HazelCastMap.getMapByName(HazelCastMap.getUserMapName()).get(PASSWORD).toString());
        auth[0] = login;
        auth[1] = password;
        return auth;
    }

//    public static String[] getAuth() {
//        String[] auth = new String[2];
//        auth[0] = new Encryptor().decrypt(getMapByName(KEY_MAP_NAME).get("key").toString(),
//                getMapByName(KEY_MAP_NAME).get("vector").toString(), getMapByName(USER_MAP_NAME).get("login").toString());
//        auth[1] = new Encryptor().decrypt(getMapByName("key").get("key").toString(),
//                getMapByName(KEY_MAP_NAME).get("vector").toString(), getMapByName(USER_MAP_NAME).get("password").toString());
//        return auth;
//    }

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
        for (SVMParameter svmParameter : allTypes) {
            if (svmParameter.getName().equals("LINEAR")
                    || svmParameter.getName().equals("POLY")
                    || svmParameter.getName().equals("RBF")
                    || svmParameter.getName().equals("SIGMOID")) {
                SVCkernelTypes.add(svmParameter);
            }
        }
        Collections.sort(SVCkernelTypes, SVMParamNameComparator);
        return SVCkernelTypes;
    }

    public static Comparator<SVMParameter> SVMParamNameComparator = new Comparator<SVMParameter>() {

        @Override
        public int compare(SVMParameter param1, SVMParameter param2) {
            String parameterName1 = param1.getName().toUpperCase();
            String parameterName2 = param2.getName().toUpperCase();
            return parameterName1.compareTo(parameterName2);
        }
    };

    public static int getCountSplitString(String string, String delimeter) {
        return string.split(delimeter).length;
    }

    public static int getSvmDegree() {
        return SVM_DEGREE;
    }

    public static double getSvmGamma(int columnCount) {
        return SVM_GAMMA / columnCount;
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

    public static double formatterSliderValueToDouble(double text, String pattern) {
        DecimalFormat formatter = new DecimalFormat(pattern);
        String string = formatter.format(text);
        return Double.parseDouble(string.replace(",", "."));
    }

    public static String responseToString(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
//        return  EntityUtils.toString(response.getEntity());
        return content;
    }

    public static ArrayList<Predict> getPredictListFromJson(HttpResponse response) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        DataInputStream dataInputStream = new DataInputStream(response.getEntity().getContent());
        String line;
        while ((line = dataInputStream.readLine()) != null) {
            stringBuilder.append(line);
        }
        dataInputStream.close();
        String json = stringBuilder.toString();
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<Predict>>() {
        }.getType();
        ArrayList<Predict> predicts = gson.fromJson(json, founderListType);
        return predicts;
    }

    public static void printTableAndMatrix(String outputFileName, ArrayList<Predict> predicts) throws IOException {
        SettingsExcel settingsExcel = new SettingsExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Result");
        XSSFCellStyle styleStandart = settingsExcel.createStyleForCellStandart(workbook);

        String[] columnsTitle = {"Id", "Real class", "Predict class", "Confidence", "Credibility",
                "pPositive", "pNegative", "Alpha positive", "Alpha negative", "Parameters"};
        settingsExcel.createCellForTitle(sheet, columnsTitle);
        Row row;
        Cell cell;

        settingsExcel.createMainCell(sheet, predicts);

        int indentRow = predicts.size() + 2;

        row = sheet.createRow(indentRow);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Real: 1\nPredict: 1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Real: 1\nPredict: -1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Real: -1\nPredict: -1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Real: -1\nPredict: 1");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Empty");
        cell.setCellStyle(styleStandart);

        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Uncertain predictions");
        cell.setCellStyle(styleStandart);

        double[] significance = {0.01, 0.05, 0.1, 0.15, 0.2};

        int column = 6;
        int[] matrix = new int[column];

        for (int i = 0; i < significance.length; i++) {
            for (int j = 0; j < column; j++) {
                matrix[j] = 0;
            }
            for (int k = 0; k < predicts.size(); k++) {
                if ((predicts.get(k).getPPositive() < significance[i]
                        && predicts.get(k).getPNegative() < significance[i])) {
                    matrix[4] = matrix[4] + 1;
                } else if (predicts.get(k).getPPositive() >= significance[i]
                        && predicts.get(k).getPNegative() >= significance[i]) {
                    matrix[5] = matrix[5] + 1;
                } else if (predicts.get(k).getRealClass() == 1 && predicts.get(k).getPredictClass() == 1) {
                    matrix[0] = matrix[0] + 1;
                } else if (predicts.get(k).getRealClass() == 1 && predicts.get(k).getPredictClass() == -1) {
                    matrix[1] = matrix[1] + 1;
                } else if (predicts.get(k).getRealClass() == -1 && predicts.get(k).getPredictClass() == -1) {
                    matrix[2] = matrix[2] + 1;
                } else if (predicts.get(k).getRealClass() == -1 && predicts.get(k).getPredictClass() == 1) {
                    matrix[3] = matrix[3] + 1;
                }
//                } else if (predicts.get(k).getRealClass() == -1 && predicts.get(k).getPredictClass() == 1) {
//                    matrix[1] = matrix[1] + 1;
//                } else if (predicts.get(k).getRealClass() == 1 && predicts.get(k).getPredictClass() == -1) {
//                    matrix[2] = matrix[2] + 1;
//                } else if (predicts.get(k).getRealClass() == -1 && predicts.get(k).getPredictClass() == -1) {
//                    matrix[3] = matrix[3] + 1;
//                }

            }
            settingsExcel.createCellRowMatrixRegionPrediction(sheet, matrix, significance[i], indentRow + i);
        }

//        File file = null;
//        try {
        File file = new File(outputFileName + ".xlsx");
        file.getParentFile().mkdirs();
//        } catch (NullPointerException e) {
//            getAlert(null, "Invalid directory", Alert.AlertType.ERROR);
//        }
        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        outFile.close();
    }

    public static void getAlert(String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean questionOkCancel(String questionText) {
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, questionText, ok, cancel);
        questionOfCancellation.setHeaderText(null);
        Optional<ButtonType> result = questionOfCancellation.showAndWait();
        if (result.orElse(cancel) == ok) {
            return true;
        } else {
            return false;
        }
    }

//    public static String getInstanceName() {
//        return INSTANCE_NAME;
//    }
//
//    public static String getUserMapName() {
//        return USER_MAP_NAME;
//    }
//
//    public static String getDataSetMapName() {
//        return DATASET_MAP_NAME;
//    }
//
//    public static String getKeyMapName() {
//        return KEY_MAP_NAME;
//    }
//
//    public static String getMiscellaneousMapName() {
//        return MISCELLANEOUS_MAP_NAME;
//    }

    public static int getObjectOnPage() {
        return OBJECT_ON_PAGE;
    }

    public static ImageView signInIcon() {
        return new ImageView(SIGN_IN_ICON);
    }

    public static ImageView applicationIcon() {
        return new ImageView(APPLICATION_ICON);
    }

    public static ImageView addIcon() {
        return new ImageView(ADD_ICON);
    }

    public static ImageView cancelIcon() {
        return new ImageView(CANCEL_ICON);
    }

    public static ImageView deleteIcon() {
        return new ImageView(DELETE_ICON);
    }

    public static ImageView infoIcon() {
        return new ImageView(INFO_ICON);
    }

    public static ImageView okIcon() {
        return new ImageView(OK_ICON);
    }

    public static ImageView returnIcon() {
        return new ImageView(RETURN_ICON);
    }

    public static ImageView runIcon() {
        return new ImageView(RUN_ICON);
    }

    public static ImageView searchIcon() {
        return new ImageView(SEARCH_ICON);
    }

    public static ImageView editIcon() {
        return new ImageView(EDIT_ICON);
    }
}

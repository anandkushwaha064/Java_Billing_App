package org.billing.utility;

import org.billing.pagepojo.ProductPojo;
import org.billing.pagepojo.ScrapPojo;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class JsonUtils {

    public List<ProductPojo> getProductsPojo(String jsonString) {
        List<ProductPojo> data = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            JSONArray jsonArray = (JSONArray) jsonObject.get("products");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    ProductPojo productPojo = new ProductPojo();

                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    productPojo.setId((String) jsonObject1.get("id"));
                    productPojo.setProductName((String) jsonObject1.get("name"));
                    productPojo.setQuantity((String) jsonObject1.get("quantity"));
                    productPojo.setQuantityType((String) jsonObject1.get("quantityType"));
                    productPojo.setGst((String) jsonObject1.get("gst"));
                    productPojo.setRate((String) jsonObject1.get("rate"));
                    productPojo.setTotal((String) jsonObject1.get("total"));
                    data.add(productPojo);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return data;
    }

    public List<ScrapPojo> getScrapsPojo(String jsonString) {
        List<ScrapPojo> data = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            JSONArray jsonArray = (JSONArray) jsonObject.get("scraps");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    ScrapPojo scrapsPojo = new ScrapPojo();

                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    scrapsPojo.setId((String) jsonObject1.get("id"));
                    scrapsPojo.setProductName((String) jsonObject1.get("name"));
                    scrapsPojo.setQuantity((String) jsonObject1.get("quantity"));
                    scrapsPojo.setQuantityType((String) jsonObject1.get("quantityType"));
                    scrapsPojo.setRate((String) jsonObject1.get("rate"));
                    scrapsPojo.setTotal((String) jsonObject1.get("total"));
                    data.add(scrapsPojo);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return data;
    }
}


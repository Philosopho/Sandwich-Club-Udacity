package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = null;

        try {
            JSONObject jsonObject = new JSONObject(json);

            String placeOfOrigin = jsonObject.getString("placeOfOrigin");
            String description = jsonObject.getString("description");
            String imageSrc = jsonObject.getString("image");
            JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");

            JSONObject jsonObjectName = jsonObject.getJSONObject("name");
            String mainName = jsonObjectName.getString("mainName");
            JSONArray alsoKnownAsArray = jsonObjectName.getJSONArray("alsoKnownAs");


            List<String> alsoKnownAsList = new ArrayList<>();
            List<String> ingredientsList = new ArrayList<>();

            if(alsoKnownAsArray != null) { populateListFromArray(alsoKnownAsList, alsoKnownAsArray); }

            if(ingredientsArray != null) { populateListFromArray(ingredientsList, ingredientsArray); }

            sandwich = new Sandwich(
                    mainName,
                    alsoKnownAsList,
                    placeOfOrigin,
                    description,
                    imageSrc,
                    ingredientsList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }

    private static void populateListFromArray(List<String> list, JSONArray array) throws JSONException {
        int length = array.length();

        for(int i = 0; i < length; i++) {
            list.add(array.getString(i));
        }
    }
}

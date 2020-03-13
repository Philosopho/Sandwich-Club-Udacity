package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView imageSrcOriginTextView;
    private TextView alsoKnownAsTextView;
    private TextView ingredientsTextView;
    private TextView descriptionTextView;
    private TextView descriptionLabelTextView;
    private TextView alsoKnownAsLabelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        imageSrcOriginTextView = findViewById(R.id.origin_tv);
        alsoKnownAsTextView = findViewById(R.id.also_known_tv);
        ingredientsTextView = findViewById(R.id.ingredients_tv);
        descriptionTextView = findViewById(R.id.description_tv);
        descriptionLabelTextView = findViewById(R.id.description_label_tv);
        alsoKnownAsLabelTextView = findViewById(R.id.also_known_label);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        List<String> akaStrings = sandwich.getAlsoKnownAs();
        List<String> ingredients = sandwich.getIngredients();
        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        String description = sandwich.getDescription();

        if(description == null || description.equals("")){
            toggleDescriptionVisibility(View.GONE);
        }
        else {
            toggleDescriptionVisibility(View.VISIBLE);
            descriptionTextView.setText(description);
        }

        if(!placeOfOrigin.equals("")) {
            imageSrcOriginTextView.setText(placeOfOrigin);
        }

        if(akaStrings != null && !akaStrings.isEmpty()) {
            toggleAlsoKnownAsVisibility(View.VISIBLE);
            alsoKnownAsTextView.setText(buildStringsFromList(akaStrings));
        }
        else {
            toggleAlsoKnownAsVisibility(View.GONE);
        }

        ingredientsTextView.setText(buildStringsFromList(ingredients));

    }

    private void toggleDescriptionVisibility(int visibility) {
        descriptionTextView.setVisibility(visibility);
        descriptionLabelTextView.setVisibility(visibility);
    }

    private void toggleAlsoKnownAsVisibility(int visibility){
        alsoKnownAsLabelTextView.setVisibility(visibility);
        alsoKnownAsTextView.setVisibility(visibility);
    }

    private String buildStringsFromList(List<String> list) {
        StringBuilder builder = new StringBuilder();
        boolean numbered = list.size() > 1;
        int number = 0;

        for(String string : list) {
            number++;
            if(number == 1) {
                addString(builder, number, string, numbered);
            }
            else {
                builder.append("\n");
                addString(builder, number, string, numbered);
            }
        }

        return builder.toString();
    }

    private void addString(StringBuilder builder,
                           int number,
                           String string,
                           boolean numbered){
        if (numbered) {
            builder.append(number);
            builder.append(". ");
        }
        builder.append(string);
    }
}

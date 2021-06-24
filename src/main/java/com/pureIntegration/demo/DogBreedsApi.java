package com.pureIntegration.demo;

import com.pureIntegration.demo.model.DogBreed;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/dogBreeds")
public class DogBreedsApi
{
    @GetMapping
    public JSONObject getDogBreeds() throws IOException {
        JSONObject response = new JSONObject();
        // make http call to the URL given in Step 1
        final String step1Url = "https://raw.githubusercontent.com/mlenze/CodingExcercise-Java/main/apidata.json";
        JSONObject inputJson = readJsonFromUrl(step1Url);

        // parse and sort json based on keys which are dog breeds
        Map<String, List<String>> dogBreedsForApi1 = new TreeMap<>();

        for (String dogBreed : inputJson.keySet())
        {
            Object value = inputJson.get(dogBreed);
            dogBreedsForApi1.computeIfAbsent(dogBreed, k -> new ArrayList<>());

            if (value == null)
            {
                dogBreedsForApi1.put(dogBreed, List.of(""));
            }
            else if (value instanceof JSONArray)
            {
                JSONArray jsonArray = ((JSONArray) value);

                if (jsonArray.length()> 0 )
                {
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        dogBreedsForApi1.get(dogBreed).add((String)jsonArray.get(i));
                    }
                }

            }

        }

        // return in format as in step 2
        response.put("message", dogBreedsForApi1);
        response.put("status", "success");

        return response;
    }

    private  String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }
}

package linc.com.alarmclockforprogrammers.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil<T> {

    //todo make this class static

    private Gson gson;

    public JsonUtil(Gson gson) {
        this.gson = gson;
    }

    public String listToJson(List<T> list) {
        return gson.toJson(list);
    }

    public List<T> jsonToList(String json) {
        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        return gson.fromJson(json, listType);
    }

}

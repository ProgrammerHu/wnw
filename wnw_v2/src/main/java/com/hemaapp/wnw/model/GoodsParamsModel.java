package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 商品参数数据模型
 * Created by HuHu on 2016-09-07.
 */
public class GoodsParamsModel extends XtomObject implements Serializable {
    private static final long serialVersionUID = -1796406234156285365L;
    private String param_id;
    private String param_name;
    private ArrayList<ParamsChild> children = new ArrayList<>();

    public GoodsParamsModel(String param_id, String param_name, ArrayList<ParamsChild> children) {
        this.param_id = param_id;
        this.param_name = param_name;
        this.children = children;
    }

    public GoodsParamsModel(JSONObject jsonObject) {
        try {
            param_id = get(jsonObject, "id");
            param_name = get(jsonObject, "name");
            if (!jsonObject.isNull("childItems") && !isNull(jsonObject.getString("childItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("childItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    children.add(new ParamsChild(jsonArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addChild(ParamsChild child) {
        children.add(child);
    }

    public void setParam_id(String param_id) {
        this.param_id = param_id;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getParam_id() {
        return param_id;
    }

    public String getParam_name() {
        return param_name;
    }

    public ArrayList<ParamsChild> getChildren() {
        return children;
    }

    public static class ParamsChild extends XtomObject implements Serializable {
        private static final long serialVersionUID = 5829246811724726088L;
        private String id;
        private String name;

        public ParamsChild(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public ParamsChild(JSONObject jsonObject) {
            try {
                id = get(jsonObject, "attr_id");
                name = get(jsonObject, "name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

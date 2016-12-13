package com.hemaapp.luna_demo.util;

import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * Created by HuHu on 2016/5/15.
 */
public class MapLineUtil extends XtomObject {
    private AMap aMap;
    private int color;
    private TextView textView;
    private ArrayList<Polyline> paths = new ArrayList<>();
    private ArrayList<LatLng> points = new ArrayList<>();

    private LatLng beforeLatLng;

    public MapLineUtil(AMap aMap, TextView textView, int color) {
        this.aMap = aMap;
        this.textView = textView;
        this.color = color;
    }


    public void addPath(LatLng point) {
        if (point == null) {
            return;
        }
//        PolylineOptions polylineOptions = new PolylineOptions().addAll(points).width(5).color(color);
        if (beforeLatLng == null) {
            beforeLatLng = point;
            return;
        }
        PolylineOptions polylineOptions = new PolylineOptions().add(beforeLatLng, point).width(5).color(color);
        Polyline polyline = aMap.addPolyline(polylineOptions);
        beforeLatLng = point;
//        points.add(point);
//        paths.add(polyline);
    }
}

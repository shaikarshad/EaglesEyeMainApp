package com.base.ours.eagleseyemainapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USR on 4/24/2017.
 */

public class NearestDistanceHelper {
    public static Map<String, List<String>> bsdistDetails;
    public List<String> distEncoded;
    public static final int DISTANCE_IND = 0;
    public static final int ENCODED_STRING_IND = 1;

    public void addDetls(String busNo, String distance, String encodedString) {
        if (bsdistDetails == null) {
            bsdistDetails = new HashMap<>();
            distEncoded = new ArrayList<>();
        } else {
            distEncoded = new ArrayList<>();
        }
        distEncoded.add(DISTANCE_IND, distance);
        distEncoded.add(ENCODED_STRING_IND, encodedString);
        bsdistDetails.put(busNo, distEncoded);
        Log.d("checking map values", "at line 30" + busNo);
    }

    public List<String> getMaxDetls() {
        int maxDist = 0;
        String maxDistEncStr = "";
        String maxbsNo = "";
        List<String> distEncStrDetls = new ArrayList<>();
        for (String bsNo : this.bsdistDetails.keySet()) {
            List<String> tempDistEncoded = new ArrayList<>(this.bsdistDetails.get(bsNo));
            int tempDist = Integer.valueOf(tempDistEncoded.get(DISTANCE_IND));
            if (tempDist > maxDist) {
                maxDist = tempDist;
                maxDistEncStr = tempDistEncoded.get(ENCODED_STRING_IND);
                maxbsNo = bsNo;
            }
        }
        distEncStrDetls.add(maxbsNo);
        distEncStrDetls.add(maxDistEncStr);
        return distEncStrDetls;
    }
}

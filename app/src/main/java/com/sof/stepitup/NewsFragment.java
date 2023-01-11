package com.sof.stepitup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;


public class NewsFragment extends Fragment {
    public static final String ip = "145.52.154.211";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        String[] field = new String[1];
        field[0] = "message";
        String[] data = new String[1];
        data[0] = "getNewsLetter"; //random string

        PutData putData = new PutData("http://" + ip + "/stepitup/GetRankingInfo.php", "GET", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                try {
                    String result = putData.getResult();
                    JSONArray users = new JSONArray(result);

                    for (int i = 0; i < users.length(); i++) {
//                        usernameId
                        JSONArray user = users.getJSONArray(i);
//                        String currentUsername = user.getString(0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return root;
    }
}
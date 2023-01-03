package com.sof.stepitup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class RankFragment extends Fragment {
    public static final String ip = "192.168.2.12";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rank, container, false);
//        SessionManager sessionManager = new SessionManager(root.getContext());
//        TextView rankText = root.findViewById(R.id.message_rank_fragment);

        TableLayout tableLayout = root.findViewById(R.id.table);

        String[] field = new String[1];
        field[0] = "message";
        String[] data = new String[1];
        data[0] = "rankingDataGet"; //random string

        PutData putData = new PutData("http://" + ip + "/stepitup/GetRankingInfo.php", "GET", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
//                rankText.setText("helpme");
                try {
                    String result = putData.getResult();
                    JSONArray users = new JSONArray(result);

                    for (int i = 0; i < users.length(); i++) {
//                        usernameId
                        JSONArray user = users.getJSONArray(i);
                        String currentUsername = user.getString(0);
                        int steps = user.getInt(1);
                        TableRow row = new TableRow(root.getContext());

                        TextView username = new TextView(root.getContext());
                        username.setText(currentUsername);
                        username.setTextColor(Color.BLACK);
                        username.setPadding(0,0,150,0);
                        row.addView(username);

                        TextView stepTxt = new TextView(root.getContext());
                        stepTxt.setText(""+steps);
                        stepTxt.setTextColor(Color.BLACK);
                        row.addView(stepTxt);

                        tableLayout.addView(row);
//                        rankText.setText(currentUsername);
//                        Toast.makeText(root.getContext(), currentUsername + currentStepsForUser, Toast.LENGTH_SHORT).show();
                    }

//                    int userSteps = user.getInt("stappen");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getParentFragmentManager()
                                .beginTransaction()
                                .detach(RankFragment.this)
                                .commit();
                        getParentFragmentManager()
                                .beginTransaction()
                                .attach(RankFragment.this)
                                .commit();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        return root;
    }
}
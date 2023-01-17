package com.sof.stepitup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {
    public static final String ip = "145.52.155.202";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        SessionManager sessionManager = new SessionManager(root.getContext(),"userSession");
//        TextView welcome = root.findViewById(R.id.welcome_txt);
        TextView steps = root.findViewById(R.id.user_steps);
        TextView date = root.findViewById(R.id.text_today);
        TextView distance = root.findViewById(R.id.text_walking_distance);
        TextView calories = root.findViewById(R.id.calories);
        ProgressBar progressBar = root.findViewById(R.id.pBar);
//        zet username in welkom
//        String welcomeText = getString(R.string.welkom, sessionManager.getUserInfo()[1]);
//        welcome.setText(welcomeText);
        //query voor user data
        String[] field = new String[1];
        field[0] = "user_ID";
        String[] data = new String[1];
        data[0] = sessionManager.getUserInfo()[0].toString();

        PutData putData = new PutData("http://" + ip + "/stepitup/GetLatestUserInfo.php", "GET", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                try {
                    String result = putData.getResult();
                    JSONObject user = new JSONObject(result);
                    int userSteps = user.getInt("stappen");
                    sessionManager.updatePoints(user.getInt("punten"));
                    String dbSteps = getString(R.string.steps, userSteps);
                    steps.setText(dbSteps);

                    String totalCal = String.format("%.1f", userSteps*0.4);
                    String cal = getString(R.string.cal, totalCal); //elke stap is .4 cal
                    calories.setText(cal);

                    String totalKm = String.format("%.3f", userSteps/1200.0);
                    String km = getString(R.string.kilometers,totalKm);//1200 ongeveer voor 1 km
                    distance.setText(km);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    String dateFormatted = sdf.format(new Date());
                    String today = getString(R.string.date, dateFormatted);
                    date.setText(today);

                    progressBar.setProgress(userSteps);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(
                new  SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getParentFragmentManager()
                                .beginTransaction()
                                .detach(HomeFragment.this)
                                .commit();
                        getParentFragmentManager()
                                .beginTransaction()
                                .attach(HomeFragment.this)
                                .commit();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        return root;
    }
}
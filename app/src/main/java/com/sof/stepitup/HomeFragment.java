package com.sof.stepitup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {
    public static final String ip = "192.168.2.12";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        SessionManager sessionManager = new SessionManager(root.getContext());
        TextView welcome = root.findViewById(R.id.welcome_txt);
        TextView steps = root.findViewById(R.id.steps);

//        zet username in welkom
        String welcomeText = getString(R.string.welkom, sessionManager.getUserInfo()[1]);
        welcome.setText(welcomeText);

        //gewoon logout
        Button logout = (Button) root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
//                finish();
            }
        });
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

                    String a = getString(R.string.steps, userSteps);
                    steps.setText(a);
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
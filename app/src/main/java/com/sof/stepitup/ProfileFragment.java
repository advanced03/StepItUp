package com.sof.stepitup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ProfileFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView list;
    private SessionManager sessionManager;
    String[] options = new String[]{"Aankopen", "Log uit"};
    public static final String ip = "192.168.137.1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        sessionManager = new SessionManager(root.getContext(),"userSession");

        list = root.findViewById(R.id.lvProfile);

        ArrayAdapter<String> profileAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1, options);
        list.setAdapter(profileAdapter);
        list.setOnItemClickListener(this);

        TextView stepsTxt = root.findViewById(R.id.stappen);
        TextView usernameTxt = root.findViewById(R.id.username);
        TextView emailTxt = root.findViewById(R.id.email);
        TextView pointsTxt = root.findViewById(R.id.punten);
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
                    double userPoints = user.getDouble("punten");
                    String userEmail = user.getString("email");
                    String username = user.getString("gebruikersnaam");

                    sessionManager.updatePoints(user.getDouble("punten"));
                    String steps = getString(R.string.steps, userSteps);
                    stepsTxt.setText(steps);
                    System.out.println(userPoints);
                    String points = getString(R.string.punten, String.format("%.3f", userPoints));
                    pointsTxt.setText(points);

                    String email = getString(R.string.email, userEmail);
                    emailTxt.setText(email);

                    String name = getString(R.string.username, username);
                    usernameTxt.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swipeProfileRefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new  SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getParentFragmentManager()
                                .beginTransaction()
                                .detach(ProfileFragment.this)
                                .commit();
                        getParentFragmentManager()
                                .beginTransaction()
                                .attach(ProfileFragment.this)
                                .commit();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (options[position]) {
            case "Aankopen":
                ((MainNavigation) getActivity()).replaceFragments();
                break;
            case "Log uit":
                sessionManager.logoutUser();
                break;
        }
    }
}
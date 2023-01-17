package com.sof.stepitup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

public class PurchasesFragment extends Fragment {
    public static final String ip = "192.168.137.1";
    CardView card;
    LinearLayout layoutInsideCardVertical;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_purchases, container, false);
        LinearLayout l = root.findViewById(R.id.baseLinearLayout);
        SessionManager sessionManager = new SessionManager(root.getContext(), "userSession");
        String[] field = new String[1];
        field[0] = "userId";
        String[] data = new String[1];
        data[0] = sessionManager.getUserInfo()[0].toString(); //random string

        PutData putData = new PutData("http://" + ip + "/stepitup/GetPurchases.php", "GET", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                try {
                    String result = putData.getResult();
                    System.out.println(result);
                    if (!result.equals("nothing found")) {
                        JSONArray purchases = new JSONArray(result);
                        int prevId = 0;

                        for (int i = 0; i < purchases.length(); i++) {
                            LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            LinearLayout.LayoutParams marginParamsMatchParent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                            JSONArray purchase = purchases.getJSONArray(i);

                            int id = purchase.getInt(0);
                            String productName = purchase.getString(1);
                            int amount = purchase.getInt(2);
                            String date = purchase.getString(3);

                            marginParams.setMargins(30,20,30,10);

                            if (prevId != id) {
                                card = new CardView(root.getContext());
                                layoutInsideCardVertical = new LinearLayout(root.getContext());
                                layoutInsideCardVertical.setOrientation(LinearLayout.VERTICAL);

                                TextView dateTxt = new TextView(root.getContext());
                                dateTxt.setLayoutParams(marginParams);
                                dateTxt.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body1);
                                dateTxt.setTextColor(Color.parseColor("#666666"));
                                dateTxt.setGravity(Gravity.CENTER);
                                dateTxt.setText(date);

                                layoutInsideCardVertical.addView(dateTxt);
                                marginParamsMatchParent.setMargins(0,0,0,30); // 1
                                card.setLayoutParams(marginParamsMatchParent);
                                card.setElevation(30);
                                card.setRadius(20);
                            }

                            TextView titleTxt = new TextView(root.getContext());
                            titleTxt.setLayoutParams(marginParams);
                            titleTxt.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title);
                            titleTxt.setText(productName);

                            TextView amountTxt = new TextView(root.getContext());
                            amountTxt.setLayoutParams(marginParams);
                            amountTxt.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
                            amountTxt.setTextColor(Color.parseColor("#666666"));
                            amountTxt.setText("hoeveelheid: "+amount);

                            layoutInsideCardVertical.addView(titleTxt);
                            layoutInsideCardVertical.addView(amountTxt);
                            if (prevId != id) {
                                card.addView(layoutInsideCardVertical);
                                l.addView(card);

                            }
                            prevId = id;
                        }
                    }
                    else {
                        TextView emptyText = new TextView(root.getContext());
                        emptyText.setText("Er zijn geen aankopen...");
                        emptyText.setGravity(Gravity.CENTER);
                        emptyText.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title);
                        l.addView(emptyText);
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        return root;
    }
}
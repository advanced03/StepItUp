package com.sof.stepitup;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingcartFragment extends Fragment implements View.OnClickListener{
    public static final String ip = "192.168.137.1";
    private SessionManager sessionManager;
    private SessionManager userSessionManager;
    private int points;
    private int total;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shoppingcart, container, false);
        LinearLayout l = root.findViewById(R.id.baseLinearLayout);

        sessionManager = new SessionManager(root.getContext(), "cartSession");
        userSessionManager = new SessionManager(root.getContext(), "userSession");
        points = (int) userSessionManager.getUserInfo()[4];

        ArrayList<HashMap<String, Object>> cart = sessionManager.getCart();

        if (!cart.isEmpty()) {
            total = 0;
            for (HashMap<String, Object> item :cart){
                LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams marginParamsMatchParent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                LinearLayout layoutInsideCardVertical = new LinearLayout(root.getContext());
                layoutInsideCardVertical.setOrientation(LinearLayout.VERTICAL);

                LinearLayout layoutInsideCardHorizontal = new LinearLayout(root.getContext());
                layoutInsideCardHorizontal.setOrientation(LinearLayout.HORIZONTAL);

                int itemId = (int) (double) item.get("id");
                total += (int)(double) item.get("amount") * (int)(double) item.get("price");
                String itemName = (String) item.get("name");
                int itemPrice = (int)(double) item.get("price");
                int itemAmount = (int)(double) item.get("amount");

                CardView card = new CardView(root.getContext());

                marginParamsMatchParent.setMargins(0,0,0,30); // 1
                card.setLayoutParams(marginParamsMatchParent);
                card.setElevation(10);
                card.setRadius(20);

                marginParams.setMargins(30,30,30,10);
                TextView title = new TextView(root.getContext());
                title.setLayoutParams(marginParams);
                title.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title);
                title.setTextColor(Color.parseColor("#666666"));
                title.setText(itemName);

                TextView price = new TextView(root.getContext());
                price.setLayoutParams(marginParams);
                price.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
                price.setTextColor(Color.parseColor("#666666"));
                price.setText("Punten: "+itemPrice);

                TextView totalTxt = new TextView(root.getContext());
                totalTxt.setLayoutParams(marginParams);
                totalTxt.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
                totalTxt.setTextColor(Color.parseColor("#666666"));
                totalTxt.setText("Totaal: " +(itemPrice*itemAmount));

                Button add = new Button(root.getContext());
                add.setText("+");
                add.setTag(R.id.cart_action, "add");
                add.setTag(R.id.item_id, itemId);
                add.setGravity(Gravity.CENTER);
                add.setMinWidth(0);
                add.setMinHeight(0);
                add.setPadding(0,0,0,0);
                add.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
                add.setOnClickListener(this);

                TextView count = new TextView(root.getContext());
                count.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
                count.setGravity(Gravity.CENTER);
                count.setTypeface(null, Typeface.BOLD);
                count.setText(""+itemAmount);

                Button remove = new Button(root.getContext());
                remove.setTag(R.id.cart_action, "remove");
                remove.setTag(R.id.item_id, itemId);
                remove.setPadding(0, 0, 0, 0);
                remove.setMinWidth(0);
                remove.setMinHeight(0);
                remove.setText("-");
                remove.setGravity(Gravity.CENTER);
                remove.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
                remove.setOnClickListener(this);

                layoutInsideCardHorizontal.addView(add);
                layoutInsideCardHorizontal.addView(count);
                layoutInsideCardHorizontal.addView(remove);

                layoutInsideCardVertical.addView(title);
                layoutInsideCardVertical.addView(price);
                layoutInsideCardVertical.addView(totalTxt);
                layoutInsideCardVertical.addView(layoutInsideCardHorizontal);

                card.addView(layoutInsideCardVertical);

                l.addView(card);
            }
            LinearLayout.LayoutParams bottom = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            bottom.gravity = Gravity.CENTER;
            TextView cartTotalTxt = new TextView(root.getContext());
            cartTotalTxt.setGravity(Gravity.END);
            cartTotalTxt.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
            cartTotalTxt.setText("Totaal: "+total);

            TextView userPoints = new TextView(root.getContext());
            userPoints.setGravity(Gravity.END);
            userPoints.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
            userPoints.setText("Jouw punten: "+points);

            Button buyButton = new Button(root.getContext());
            buyButton.setLayoutParams(bottom);
            buyButton.setTag(R.id.cart_action, "buy");
            buyButton.setText("Koop Nu!");
            buyButton.setAllCaps(false);
            buyButton.setOnClickListener(this);

            Button resetButton = new Button(root.getContext());
            resetButton.setLayoutParams(bottom);
            resetButton.setTag(R.id.cart_action, "empty");
            resetButton.setText("Winkelwagen legen");
            resetButton.setAllCaps(false);
            resetButton.setOnClickListener(this);

            l.addView(cartTotalTxt);
            l.addView(userPoints);
            l.addView(buyButton);
            l.addView(resetButton);
        }
        else {
            TextView emptyText = new TextView(root.getContext());
            emptyText.setText("Er zitten geen producten in de winkelwagen!");
            emptyText.setGravity(Gravity.CENTER);
            emptyText.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title);
            l.addView(emptyText);
        }
        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag(R.id.cart_action) != null && v.getTag(R.id.item_id) != null) {
            String action = (String) v.getTag(R.id.cart_action);
            int id = (int) v.getTag(R.id.item_id);
            sessionManager.changeCart(action,id);
            refreshFragment();
        }
        else if(v.getTag(R.id.cart_action) == "buy") {
            //doe hier koop actie code
            int leftoverPoints = points - total;
            if (leftoverPoints <= 0) {
                Toast.makeText(v.getContext(), "Te weinig punten....", Toast.LENGTH_SHORT).show();
            }
            else {
                String [] field = {"userId","cartArrayString","newPointBalance"};
                String [] data = {Integer.toString((int) userSessionManager.getUserInfo()[0]), sessionManager.getStringifiedCart(),Integer.toString(leftoverPoints)};
                PutData putData = new PutData("http://" + ip + "/stepitup/checkout.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Success!")) {
                            userSessionManager.updatePoints(leftoverPoints);
                            sessionManager.resetCart();
                            refreshFragment();
                        } else {
                            System.out.println(result);
                        }
                    }
                }
            }
        }
        else if (v.getTag(R.id.cart_action) == "empty") {
            sessionManager.resetCart();
            refreshFragment();
        }
    }

    public void refreshFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .detach(ShoppingcartFragment.this)
                .commit();
        getParentFragmentManager()
                .beginTransaction()
                .attach(ShoppingcartFragment.this)
                .commit();
    }
}
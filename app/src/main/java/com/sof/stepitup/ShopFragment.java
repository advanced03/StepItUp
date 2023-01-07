package com.sof.stepitup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


public class ShopFragment extends Fragment implements View.OnClickListener {
    public static final String ip = "192.168.2.12";
    private SessionManager sessionManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        sessionManager = new SessionManager(root.getContext());

        TableLayout tableLayout = root.findViewById(R.id.table);

        String[] field = new String[1];
        field[0] = "message";
        String[] data = new String[1];
        data[0] = "getAllProducts"; //random string

        PutData putData = new PutData("http://" + ip + "/stepitup/GetAllProducts.php", "GET", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                try {
                    String result = putData.getResult();
                    JSONArray products = new JSONArray(result);

                    TableRow rowTxt = new TableRow(root.getContext());

                    TextView nameTxt = new TextView(root.getContext());
                    nameTxt.setText("Product");
                    nameTxt.setGravity(Gravity.CENTER);
                    nameTxt.setPadding(5,5,5,5);

                    TextView descriptionTxt = new TextView(root.getContext());
                    descriptionTxt.setText("Omschrijving");
                    descriptionTxt.setGravity(Gravity.CENTER);
                    descriptionTxt.setPadding(5,5,5,5);

                    TextView categoryTxt = new TextView(root.getContext());
                    categoryTxt.setText("Categorie");
                    categoryTxt.setGravity(Gravity.CENTER);
                    categoryTxt.setPadding(5,5,5,5);

                    TextView priceTxt = new TextView(root.getContext());
                    priceTxt.setText("Prijs");
                    priceTxt.setGravity(Gravity.CENTER);
                    priceTxt.setPadding(5,5,5,5);

                    TextView cartTxt = new TextView(root.getContext());
                    cartTxt.setText("Winkelwagen");
                    cartTxt.setGravity(Gravity.CENTER);
                    cartTxt.setPadding(5,5,5,5);

                    rowTxt.addView(nameTxt);
                    rowTxt.addView(descriptionTxt);
                    rowTxt.addView(categoryTxt);
                    rowTxt.addView(priceTxt);
                    rowTxt.addView(cartTxt);
                    tableLayout.addView(rowTxt);

                    for (int i = 0; i < products.length(); i++) {

                        JSONArray product = products.getJSONArray(i);
                        int currentProductId = product.getInt(0);
                        String currentProductName = product.getString(1);
                        String currentProductDescription = product.getString(2);
                        if (currentProductDescription.equals("null")) {
                            currentProductDescription = "";
                        }
                        String currentProductCategory = product.getString(3);
                        int currentProductPrice = product.getInt(4);

                        TableRow row = new TableRow(root.getContext());

                        TextView name = new TextView(root.getContext());
                        name.setText(currentProductName);
                        name.setGravity(Gravity.CENTER);
                        name.setPadding(5,5,5,5);

                        TextView description= new TextView(root.getContext());
                        description.setText(currentProductDescription);
                        description.setGravity(Gravity.CENTER);
                        description.setPadding(5,5,5,5);

                        TextView category = new TextView(root.getContext());
                        category.setText(currentProductCategory);
                        category.setGravity(Gravity.CENTER);
                        category.setPadding(5,5,5,5);

                        TextView price = new TextView(root.getContext());
                        price.setText(currentProductPrice + " punten");
                        price.setGravity(Gravity.CENTER);
                        price.setPadding(5,5,5,5);

                        Button cart = new Button(root.getContext());
                        cart.setText("Voeg toe");
                        cart.setId(currentProductId);
                        cart.setTag(R.id.name_of_product, currentProductName);
                        cart.setTag(R.id.price_of_product, currentProductPrice);
                        cart.setOnClickListener(this);
                        cart.setGravity(Gravity.CENTER);
                        cart.setPadding(20,20,20,20);

                        row.addView(name);
                        row.addView(description);
                        row.addView(category);
                        row.addView(price);
                        row.addView(cart);
                        tableLayout.addView(row);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return root;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Object productPrice = v.getTag(R.id.price_of_product);
        Object productName = v.getTag(R.id.name_of_product);
        HashMap<String, Object> addedProduct = new HashMap<>();
        addedProduct.put("id",id);
        addedProduct.put("name",productName);
        addedProduct.put("price",productPrice);
        addedProduct.put("amount",1);
        sessionManager.addToCart(addedProduct);
    }
}
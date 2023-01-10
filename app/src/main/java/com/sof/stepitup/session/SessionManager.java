package com.sof.stepitup.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import com.sof.stepitup.Login;
import com.sof.stepitup.ShoppingcartFragment;

import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    Gson gson;

    int Private_mode = 0;
    //PREF_NAME kan volgens mij alles zijn
    public static final String IS_LOGGED = "isLoggedIn";

    public static final String CART = "cart";
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String ROLE = "role";
//    public static final String STEPS = "steps";

    public SessionManager (Context context, String sessionName) {
        this.context = context;
        pref = context.getSharedPreferences(sessionName, Private_mode);
        editor = pref.edit();
        gson = new Gson();
    }

    public void createLoginSession(int userId, String username, String email, String role) {
        editor.putInt(USER_ID, userId);
//        editor.putInt(STEPS, steps);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.putString(ROLE, role);
        editor.putBoolean(IS_LOGGED, true);

        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED, false);
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);;

        context.startActivity(i);
    }

    public Object[] getUserInfo() {
        int id = pref.getInt(USER_ID, 0);
        String username = pref.getString(USERNAME, "NO_USERNAME_FOUND");
        String email = pref.getString(EMAIL, "NO_EMAIL_FOUND");
        String role = pref.getString(ROLE, "NO_ROLE_FOUND");

        return new Object[]{id,username,email,role};
    }

    public void addToCart(HashMap<String, Object> chosenProduct) {

        String cartString = pref.getString(CART,"[]");
        Type type = new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType();
//        TypeReference ArrayList typeRef = new TypeReference<HashMap<String,Object>>() {};
        boolean inCart = false;
        ArrayList<HashMap<String, Object>> currentCart = gson.fromJson(cartString, type);
//        ArrayList<HashMap<String, Object>> currentCart = objectMapper.readValue(cartString, typeRef);
        //if cart iets heeft
//        objectMapper.writeValueAsString();
        if (!currentCart.isEmpty()) {

            //check of product al in cart zit
            for (HashMap<String, Object> product : currentCart) {
                int chosen = (int) chosenProduct.get("id");
                double productInCart = (double) product.get("id");

                if (chosen == productInCart) {
                    product.put("amount", (double) product.get("amount") + 1);
                    inCart = true;
                    break;
                }
            }
        }

        if (!inCart) {
            currentCart.add(chosenProduct);
        }

        String newCart = gson.toJson(currentCart);
        editor.putString(CART,newCart);
        editor.commit();
//        Intent i = new Intent(context, ShoppingcartFragment.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

//        context.startActivity(i);
//        editor.putString(CART, username);
    }
    public ArrayList<HashMap<String, Object>> getCart() {

        String cartString = pref.getString(CART,"[]");
        Type type = new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType();
        ArrayList<HashMap<String, Object>> currentCart = gson.fromJson(cartString, type);
        return currentCart;
    }

    public void changeCart(String action, int id) {

        String cartString = pref.getString(CART,"[]");
        Type type = new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType();
        ArrayList<HashMap<String, Object>> currentCart = gson.fromJson(cartString, type);
        for (HashMap<String, Object> product : currentCart) {
            int chosen = id;
            double productInCart = (double) product.get("id");

            if (action.equals("add")) {
                if (chosen == productInCart) {
                    System.out.println("being added RIGHT NOW");
                    product.put("amount", (double) product.get("amount") + 1);
                    break;
                }
            }
            else if (action.equals("remove")){
                System.out.println("REMOVING 1 RIGHT NOW");
                product.put("amount", (double) product.get("amount") - 1);
                if ((double)product.get("amount") <= 0) {
                    System.out.println("REMOVING IT RIGHT NOW");
                    currentCart.remove(product);
                }
                break;
            }
        }
        String newCart = gson.toJson(currentCart);
        editor.putString(CART,newCart);
        editor.commit();
    }
}

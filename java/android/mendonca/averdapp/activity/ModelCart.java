package android.mendonca.averdapp.activity;

import java.util.ArrayList;

/**
 * Created by johnson on 28-05-2018.
 */
public class ModelCart {
    private ArrayList<ItemDetails> cartItems = new ArrayList<ItemDetails>();

    public ItemDetails getProducts(int position){

        return cartItems.get(position);


    }

    public void setProducts(ItemDetails Products){

        cartItems.add(Products);

    }


    public int getCartsize(){

        return cartItems.size();

    }

    public boolean CheckProductInCart(ItemDetails aproduct){


        return cartItems.contains(aproduct);


    }
}

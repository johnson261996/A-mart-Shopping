package android.mendonca.averdapp.activity;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by johnson on 28-05-2018.
 */

public class ItemDetails {

    private ArrayList<String> productName;
    private ArrayList<String> productPrice;

    public ItemDetails( ArrayList<String> productName,ArrayList<String> productPrice){

        this.productName = productName;
        this.productPrice = productPrice;

    }

    public String getProductName(int position){

        return productName.get(position);

    }

    public String getProductPrice(int position){

        return productPrice.get(position);

    }
}

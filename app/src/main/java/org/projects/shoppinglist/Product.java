package org.projects.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;
import android.content.Intent;

/**
 * Created by heunex on 10/05/16.
 */
public class Product implements Parcelable {
     int quantity;
     String name;
     String amount;

    public Product() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }




    @Override
    public String toString() {
        if (quantity == 0){
            return quantity + " " + name;
        } else {
            return quantity+" "+name+" " +amount;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(name);
        dest.writeString(amount);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(int quantity, String name, String amount) {
        this.quantity = quantity;
        this.name = name;
        this.amount = amount;
    }


    // "De-parcel object
    public Product(Parcel in) {
        quantity = in.readInt();
        name = in.readString();
        amount = in.readString();
    }
}





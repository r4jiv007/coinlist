package com.digital.coinlist.domain.entity;

import android.os.Parcel;
import com.digital.coinlist.ui.main.adapter.Selectable;

public class CoinItem implements Selectable, android.os.Parcelable {

    private String symbol;

    private String name;

    private String id;

    public CoinItem(String symbol, String name, String id) {
        this.symbol = symbol;
        this.name = name;
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String displayName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.symbol);
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public void readFromParcel(Parcel source) {
        this.symbol = source.readString();
        this.name = source.readString();
        this.id = source.readString();
    }

    protected CoinItem(Parcel in) {
        this.symbol = in.readString();
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Creator<CoinItem> CREATOR = new Creator<CoinItem>() {
        @Override
        public CoinItem createFromParcel(Parcel source) {
            return new CoinItem(source);
        }

        @Override
        public CoinItem[] newArray(int size) {
            return new CoinItem[size];
        }
    };
}

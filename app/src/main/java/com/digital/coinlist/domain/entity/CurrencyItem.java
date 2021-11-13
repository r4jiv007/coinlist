package com.digital.coinlist.domain.entity;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.digital.coinlist.ui.main.adapter.Selectable;

public class CurrencyItem implements Selectable, android.os.Parcelable {

    private  String currency;

    public CurrencyItem(String currency) {
        this.currency = currency;
    }

    @Override
    public String displayName() {
        return currency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.currency);
    }

    public void readFromParcel(Parcel source) {
        this.currency = source.readString();
    }

    protected CurrencyItem(Parcel in) {
        this.currency = in.readString();
    }

    public static final Creator<CurrencyItem> CREATOR = new Creator<CurrencyItem>() {
        @Override
        public CurrencyItem createFromParcel(Parcel source) {
            return new CurrencyItem(source);
        }

        @Override
        public CurrencyItem[] newArray(int size) {
            return new CurrencyItem[size];
        }
    };
}

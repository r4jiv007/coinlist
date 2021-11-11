package com.digital.coinlist.data.network.entity;

import com.google.gson.annotations.SerializedName;

public class CoinListItem{

	@SerializedName("symbol")
	private String symbol;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	public String getSymbol(){
		return symbol;
	}

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}
}
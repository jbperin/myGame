package com.jibepe.labo3d;

import java.util.Observable;

import android.util.Log;

public class Force extends Observable {
	private String TAG = new String ("FORCE");
	private static Force mInstance = null;
	private double strength;
	private double previousStrength;
	
	public double getStrength() {
		return strength;
	}
	
	public void setStrength(double strength) {
		this.strength = strength;
		if (Math.abs(this.strength - previousStrength) >= 0.01)
		{
			previousStrength = strength;
			Log.d(TAG, "Strength = "+this.strength);
			this.setChanged();
			this.notifyObservers();
		}
	}
	public static Force getInstance(){
		if (mInstance == null) {
			mInstance = new Force();
		}
		return (mInstance);
	}
	private Force() {
		strength = 0.0;
	}

}

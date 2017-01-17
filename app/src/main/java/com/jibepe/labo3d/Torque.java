package com.jibepe.labo3d;

import java.util.Observable;

import android.util.Log;

public class Torque extends Observable {

	private String TAG = new String ("TORQUE");
	private static Torque mInstance = null;
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
	public static Torque getInstance(){
		if (mInstance == null) {
			mInstance = new Torque();
		}
		return (mInstance);
	}
	private Torque() {
		strength = 0.0;
	}

}

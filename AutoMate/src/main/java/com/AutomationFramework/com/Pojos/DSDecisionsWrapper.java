package com.AutomationFramework.com.Pojos;

import java.util.ArrayList;
import java.util.List;

public class DSDecisionsWrapper {
	
	String model_year;
	String make;
	String model;
	String trim;	
	String fuel;
	
	String uvc;
	
	List<FeaturesConfig> features_configs=new ArrayList<FeaturesConfig>();

	public String getModel_year() {
		return model_year;
	}

	public void setModel_year(String model_year) {
		this.model_year = model_year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTrim() {
		return trim;
	}

	public void setTrim(String trim) {
		this.trim = trim;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getUvc() {
		return uvc;
	}

	public void setUvc(String uvc) {
		this.uvc = uvc;
	}

	public List<FeaturesConfig> getFeatures_configs() {
		return features_configs;
	}

	public void setFeatures_configs(List<FeaturesConfig> features_configs) {
		this.features_configs = features_configs;
	}
	
	
	

}

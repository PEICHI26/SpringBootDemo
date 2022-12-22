package com.mongoddemo.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {

	private Map<String, RateData> rates;

	private int code;

	@Getter
	@Setter
	public static class RateData {
		private double rate;
		private long timestamp;
	}
}

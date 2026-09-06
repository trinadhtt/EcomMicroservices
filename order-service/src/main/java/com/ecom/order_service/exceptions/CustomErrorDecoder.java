package com.ecom.order_service.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder{

	@Override
	public Exception decode(String s, Response response) {
		if(response.status() == 404) {
			return new RuntimeException("Product Not Found");
		}
		return new RuntimeException("Generic Error:" +response);
	}

}

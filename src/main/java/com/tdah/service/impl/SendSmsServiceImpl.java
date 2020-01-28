package com.tdah.service.impl;

import org.springframework.stereotype.Service;

import com.tdah.service.ISendSmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SendSmsServiceImpl implements ISendSmsService {

	public static final String ACCOUNT_SID = "ACbbe9a5de7b158d95bb51b8970de4802e";
	public static final String AUTH_TOKEN = "4e6ac9e3dc4919b4174c28b3b72ddf61";

	@Override
	public void enviarSms(String codigo, String numeroTelefono) {

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		String mensaje = "Su código de verificación para recuperar su clave es: " + codigo;
		Message message = Message.creator(new PhoneNumber("+51" + numeroTelefono), new PhoneNumber("+12486557343"), mensaje).create();
	}

}

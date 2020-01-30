package com.tdah.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.ITiwilioDAO;
import com.tdah.model.TiwilioAccout;
import com.tdah.service.ISendSmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SendSmsServiceImpl implements ISendSmsService {

	@Autowired
	ITiwilioDAO twilioDao;

	@Override
	public void enviarSms(String codigo, String numeroTelefono) {
		TiwilioAccout twilio = twilioDao.findById(1).get();
		
		String ACCOUNT_SID = twilio.getAccountSid();
		String AUTH_TOKEN = twilio.getAuthToken();
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		String mensaje = "Su código de verificación para recuperar su clave es: " + codigo;
		Message message = Message.creator(new PhoneNumber("+51" + numeroTelefono), new PhoneNumber(twilio.getPhoneNumber()), mensaje).create();
	}

}

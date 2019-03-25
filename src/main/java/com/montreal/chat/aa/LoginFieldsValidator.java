package com.montreal.chat.aa;

import java.util.regex.Pattern;

import com.montreal.chat.view.dto.ChatUserDTO;

public class LoginFieldsValidator {
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

	public static boolean validate(ChatUserDTO userDto) {
		return isCPF(userDto.getCpf()) && isValidEmail(userDto.getEmail()) && userDto.getName() != null && userDto.getName().length() > 3;
	}

	private static boolean isValidEmail(String email) {
		if (email == null || email.length() == 0) {
			return false;
		}

		return pattern.matcher(email).matches();
	}

	private static boolean isCPF(String cpf) {
		if(cpf == null || cpf == "") {
			return false;
		}
		cpf = cpf.replace(".", "").replace("-", "").trim();

		if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222")
				|| cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
				|| cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
				|| cpf.equals("99999999999") || (cpf.length() != 11)) {
			return (false);
		}

		char dig10, dig11;
		int sm, i, r, num, peso;

		sm = 0;
		peso = 10;
		for (i = 0; i < 9; i++) {
			num = (int) (cpf.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}

		r = 11 - (sm % 11);
		if ((r == 10) || (r == 11))
			dig10 = '0';
		else
			dig10 = (char) (r + 48);

		sm = 0;
		peso = 11;
		for (i = 0; i < 10; i++) {
			num = (int) (cpf.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso - 1;
		}

		r = 11 - (sm % 11);
		if ((r == 10) || (r == 11)) {
			dig11 = '0';
		} else {
			dig11 = (char) (r + 48);
		}

		if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
			return (true);
		}
		return (false);
	}
}

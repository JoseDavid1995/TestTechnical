package com.prueba.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.demo.core.entity.Phone;
import com.prueba.demo.core.entity.User;
import com.prueba.demo.repository.PhoneRepository;
import com.prueba.demo.repository.UserRepository;
import com.prueba.demo.service.DemoService;
import com.prueba.demo.support.dto.Answer;
import com.prueba.demo.support.dto.DtoResponse;
import com.prueba.demo.support.dto.JwtUtil;
import com.prueba.demo.support.dto.UserDetail;
import com.prueba.demo.support.dto.UserDto;
import com.prueba.demo.support.dto.UserDto.PhoneDto;

@Service
public class DemoServiceImpl implements DemoService {
	private static final Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	UserRepository userRepository;

	@Autowired
	PhoneRepository phoneRepository;

	@Autowired
	JwtUtil jwtUtil;

	@Override
	public Answer<?> addUser(UserDto dto) throws Exception {

		DtoResponse respuesta = null;
		try {

			if (dto.getId() == null || dto.getId().equals("")) {
				List<User> validarCorreo = userRepository.findByEmail(dto.getEmail());

				if (validarCorreo != null && !validarCorreo.isEmpty()) {
					return new Answer<>(false, null, "El email ya fue registrado anteriormente");
				}
				UUID uuid = null;
				uuid = UUID.randomUUID();
				String idUsuarioGenerado = uuid.toString();
				dto.setId(idUsuarioGenerado);

				User usuario = User.builder()
						.id(dto.getId())
						.name(dto.getName())
						.email(dto.getEmail())
						.password(dto.getPassword())
						.active(dto.getActive())
						.dateCreate(LocalDateTime.now())
						.dateModify(dto.getDateModify())
						.dateLastLogin(LocalDateTime.now())
						.build();

				User usuarioSave = userRepository.save(usuario);

				Phone telefono;
				String idTelefonoGenerado = "";

				if (dto.getPhones() != null && !dto.getPhones().isEmpty()) {
					for (PhoneDto phone : dto.getPhones()) {
						// telefono = new Phone();
						idTelefonoGenerado = "";

						uuid = UUID.randomUUID();
						idTelefonoGenerado = uuid.toString();
						phone.setId(idTelefonoGenerado);

						telefono = Phone.builder()
								.id(phone.getId())
								.idUser(usuarioSave.getId())
								.number(phone.getNumber())
								.cityCode(phone.getCityCode())
								.countryCode(phone.getCountryCode())
								.active(phone.getActive())
								.dateCreate(usuarioSave.getDateCreate())
								.dateModify(usuarioSave.getDateModify())
								.dateLastLogin(usuarioSave.getDateLastLogin())
								.build();

						phoneRepository.save(telefono);
					}
				}

				UserDetail detail = UserDetail.builder()
						.email(dto.getEmail())
						.name(dto.getName())
						.id(usuarioSave.getId())
						.build();

				String token = jwtUtil.createToken(usuarioSave.getId());

				DtoResponse response = new DtoResponse(usuarioSave.getId(),
						formatLocalDate(usuarioSave.getDateCreate()),
						usuarioSave.getActive(),
						usuarioSave.getDateModify() != null ? (formatLocalDate(usuarioSave.getDateModify())) : null,
						formatLocalDate(usuarioSave.getDateLastLogin()), token);

				respuesta = response;
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return new Answer<>(true, respuesta,
				respuesta != null ? "Se registró correctamente" : "Hubo un error al registrar al usuario");
	}

	@Override
	public Answer<?> updateUser(String idUser, UserDto body) throws Exception {

		DtoResponse respuesta = null;
		try {
			if (idUser != null) {
				List<User> validarCorreo = userRepository.findByEmail(body.getEmail());
				Boolean mismoUsuario = false;
				for (User usuario : validarCorreo) {
					if (body.getId().equals(usuario.getId())) {
						mismoUsuario = true;
					}
				}
				if (!mismoUsuario && validarCorreo != null && !validarCorreo.isEmpty()) {
					return new Answer<>(false, null, "El email ya fue registrado anteriormente");
				}

				UUID uuid = null;

				Optional<User> usuario = userRepository.findById(body.getId());
				User user = usuario.get();

				user = User.builder()
						.id(body.getId())
						.name(body.getName())
						.email(body.getEmail())
						.password(body.getPassword())
						.active(body.getActive())
						.dateModify(LocalDateTime.now())
						.build();
				User usuarioSave = userRepository.save(user);

				Phone phone;
				String idPhoneGenerate = "";

				if (body.getPhones() != null && !body.getPhones().isEmpty()) {
					for (PhoneDto tel : body.getPhones()) {

						idPhoneGenerate = "";
						if (tel.getId() == null || tel.getId().equals("")) {
							uuid = UUID.randomUUID();
							idPhoneGenerate = uuid.toString();
							tel.setId(idPhoneGenerate);
						}

						phone = Phone.builder()
								.id(tel.getId())
								.idUser(usuarioSave.getId())
								.number(tel.getNumber())
								.cityCode(tel.getCityCode())
								.countryCode(tel.getCountryCode())
								.active(tel.getActive())
								.dateCreate(usuarioSave.getDateCreate())
								.dateModify(usuarioSave.getDateModify())
								.dateLastLogin(usuarioSave.getDateLastLogin())
								.build();
						phoneRepository.save(phone);
					}
				}

				String token = jwtUtil.createToken(usuarioSave.getId());

				DtoResponse response = new DtoResponse(usuarioSave.getId(),
						formatLocalDate(usuarioSave.getDateCreate()),
						usuarioSave.getActive(), formatLocalDate(usuarioSave.getDateModify()),
						formatLocalDate(usuarioSave.getDateLastLogin()), token);

				respuesta = response;
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return new Answer<>(true, respuesta,
				respuesta != null ? "Se actualizó correctamente" : "Hubo un error al actualizar el registro");
	}

	@Override
	public Answer<?> getListUser() throws Exception {

		List<User> listUser = userRepository.findAll();
		List<Phone> listPhone = new ArrayList<>();

		List<UserDto> listUserDto = new ArrayList<>();
		UserDto userDto;

		List<UserDto.PhoneDto> listPhoneDto = new ArrayList<>();
		UserDto.PhoneDto phoneDto;

		for (User user : listUser) {
			listPhone = new ArrayList<>();

			listPhoneDto = new ArrayList<>();

			userDto = UserDto.builder()
					.id(user.getId())
					.name(user.getName())
					.email(user.getEmail())
					.password(user.getPassword())
					.active(user.getActive())
					.dateCreate(user.getDateCreate())
					.dateModify(user.getDateModify())
					.dateLastLogin(user.getDateLastLogin())
					.build();

			listPhone = phoneRepository.findByIdUser(user.getId());

			for (Phone telefono : listPhone) {
				phoneDto = PhoneDto.builder()
						.id(telefono.getId())
						.idUser(telefono.getIdUser())
						.number(telefono.getNumber())
						.cityCode(telefono.getCityCode())
						.countryCode(telefono.getCountryCode())
						.active(telefono.getActive())
						.dateCreate(telefono.getDateCreate())
						.dateModify(telefono.getDateModify())
						.dateLastLogin(telefono.getDateLastLogin())
						.build();

				listPhoneDto.add(phoneDto);
			}
			userDto.setPhones(listPhoneDto);
			listUserDto.add(userDto);
		}

		return new Answer<>(true, listUserDto);
	}

	@Override
	public Answer<?> login(UserDto dto) throws Exception {
		Optional<User> usuario = userRepository.findById(dto.getId());
		if (!usuario.isPresent()) {
			return new Answer<>(false, null, "el usuario no existe");
		}

		User user = usuario.get();
		user.setDateLastLogin(LocalDateTime.now());
		User userUpdate = userRepository.save(user);

		String token = jwtUtil.createToken(userUpdate.getId());

		DtoResponse response = new DtoResponse(userUpdate.getId(), formatLocalDate(userUpdate.getDateLastLogin()),
				token);
		 
		return new Answer<>(true, response, "Se inició sesión correctamente");
	}

	private static String formatLocalDate(LocalDateTime localDateTime) {

		String fechaFormateada = "";

		if (localDateTime != null) {
			fechaFormateada = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.GERMANY).format(localDateTime);
		}

		return fechaFormateada;
	}

}
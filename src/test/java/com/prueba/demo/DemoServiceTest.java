package com.prueba.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.prueba.demo.core.entity.User;
import com.prueba.demo.repository.PhoneRepository;
import com.prueba.demo.repository.UserRepository;
import com.prueba.demo.service.impl.DemoServiceImpl;
import com.prueba.demo.support.dto.UserDto;
import com.prueba.demo.support.dto.Answer;
import com.prueba.demo.support.dto.JwtUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoServiceTest {

    @Mock
    private UserRepository usuarioRepository;

    @Mock
    private PhoneRepository telefonoRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private DemoServiceImpl demoService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetListUser() throws Exception {

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(new User()));

        Answer<?> respuesta = demoService.getListUser();

        assertThat(respuesta.isSuccess(), is(true));
        assertNotNull(respuesta.getDato());
    }

    @Test
    public void testAddUser() throws Exception {

        UserDto usuarioDto = UserDto.builder()
                .email("registrar@gmail.com")
                .name("Nombre prueba")
                .password("PassowrdPass1!")
                .phones(new ArrayList<>())
                .build();

        when(usuarioRepository.findByEmail(usuarioDto.getEmail())).thenReturn(Arrays.asList());
        when(usuarioRepository.save(any(User.class))).thenReturn(new User());

        when(jwtUtil.createToken(anyString())).thenReturn("");

        Answer<?> respuesta = demoService.addUser(usuarioDto);

        assertThat(respuesta.isSuccess(), is(true));
        assertNotNull(respuesta.getMessage(), is("Se registró correctamente"));
    }

    @Test
    public void testUpdateUser() throws Exception {

        User usuarioExistente = User.builder()
                .id("1")
                .email("existente@gmail.com")
                .name("Nombre Existente")
                .password("PasswordExistente1!")
                .build();

        UserDto usuarioDto = UserDto.builder()
                .id("1")
                .email("actualizar@gmail.com")
                .name("Nombre prueba")
                .password("PasswordPass2!")
                .phones(new ArrayList<>())
                .build();

        when(usuarioRepository.findByEmail(usuarioDto.getEmail())).thenReturn(Arrays.asList(usuarioExistente));
        when(usuarioRepository.findById(usuarioDto.getId())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(User.class))).thenReturn(new User());
        when(jwtUtil.createToken(anyString())).thenReturn("");

        Answer<?> respuesta = demoService.updateUser(usuarioDto.getId(), usuarioDto);

        assertThat(respuesta.isSuccess(), is(true));
        assertNotNull(respuesta.getMessage(), is("Se actualizó correctamente"));
    }

}
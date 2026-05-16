package com.example.pinchaapp.network;

import com.example.pinchaapp.dto.AuthResponseDto;
import com.example.pinchaapp.dto.LoginDto;
import com.example.pinchaapp.dto.RegistroDto;
import com.example.pinchaapp.dto.RespuestaDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface ApiService {

    // Auth
    @POST("api/auth/login")
    Call<RespuestaDto<AuthResponseDto>> login(@Body LoginDto body);

    @POST("api/auth/registro")
    Call<RespuestaDto<Object>> registro(@Body RegistroDto body);
}

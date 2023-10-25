package com.waff1e.waffle.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.waff1e.waffle.auth.data.DefaultAuthRepository
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.auth.network.AuthService
import com.waff1e.waffle.waffles.data.DefaultWafflesRepository
import com.waff1e.waffle.waffles.data.WafflesRepository
import com.waff1e.waffle.waffles.network.WafflesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://39c2e677-1701-431e-9097-dfe2414db875.mock.pstmn.io"

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        // TODO("jwt 인터셉터 추가 필요")
        return OkHttpClient.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit,
            ) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter =
                    retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

                override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }

        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authService: AuthService): AuthRepository {
        return DefaultAuthRepository(authService)
    }

    @Singleton
    @Provides
    fun provideWafflesService(retrofit: Retrofit): WafflesService {
        return retrofit.create(WafflesService::class.java)
    }

    @Singleton
    @Provides
    fun provideWafflesRepository(wafflesService: WafflesService): WafflesRepository {
        return DefaultWafflesRepository(wafflesService)
    }
}
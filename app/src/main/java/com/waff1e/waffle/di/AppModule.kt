package com.waff1e.waffle.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.waff1e.waffle.data.DefaultWaffleRepository
import com.waff1e.waffle.data.WaffleRepository
import com.waff1e.waffle.network.WaffleService
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
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://87a26294-cb63-4261-8e72-ad987fd83ee3.mock.pstmn.io"

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
    fun provideWaffleService(retrofit: Retrofit): WaffleService {
        return retrofit.create(WaffleService::class.java)
    }

    @Singleton
    @Provides
    fun provideWaffleRepository(waffleService: WaffleService): WaffleRepository {
        return DefaultWaffleRepository(waffleService)
    }
}
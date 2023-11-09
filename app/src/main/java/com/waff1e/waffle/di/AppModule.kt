package com.waff1e.waffle.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.waff1e.waffle.auth.data.DefaultAuthRepository
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.auth.network.AuthService
import com.waff1e.waffle.member.data.DefaultMemberRepository
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.network.MemberService
import com.waff1e.waffle.waffle.data.DefaultWaffleRepository
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.network.WaffleService
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
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.lang.reflect.Member
import java.lang.reflect.Type
import javax.inject.Singleton

const val LIMIT = 20
const val DOUBLE_CLICK_DELAY = 1000L
// 8-14자리 영문
const val PASSWORD_RULE = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]!@#$%^&*]{8,14}\$"
const val NICKNAME_MAX_LENGTH = 20
const val NAME_MAX_LENGTH = 50
const val DEBOUNCE_TIME = 350L

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://f005fffd-9a26-4a99-bcbb-24171bfba8ee.mock.pstmn.io"

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
    fun provideWafflesService(retrofit: Retrofit): WaffleService {
        return retrofit.create(WaffleService::class.java)
    }

    @Singleton
    @Provides
    fun provideWafflesRepository(waffleService: WaffleService): WaffleRepository {
        return DefaultWaffleRepository(waffleService)
    }

    @Singleton
    @Provides
    fun provideMemberService(retrofit: Retrofit): MemberService {
        return retrofit.create(MemberService::class.java)
    }

    @Singleton
    @Provides
    fun provideMemberRepository(memberService: MemberService): MemberRepository {
        return DefaultMemberRepository(memberService)
    }
}
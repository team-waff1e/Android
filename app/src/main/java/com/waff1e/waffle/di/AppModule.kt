package com.waff1e.waffle.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.waff1e.waffle.auth.data.DefaultAuthRepository
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.auth.network.AuthService
import com.waff1e.waffle.comment.data.CommentRepository
import com.waff1e.waffle.comment.data.DefaultCommentRepository
import com.waff1e.waffle.comment.network.CommentService
import com.waff1e.waffle.member.data.DefaultMemberRepository
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.network.MemberService
import com.waff1e.waffle.utils.LoginUser
import com.waff1e.waffle.waffle.data.DefaultWaffleRepository
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.network.WaffleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import java.lang.reflect.Type
import javax.inject.Inject
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
    fun provideLoginUserPreference(@ApplicationContext context: Context): LoginUserPreferenceModule = LoginUserPreferenceModule(context)

    @Singleton
    @Provides
    fun provideOkhttpClient(interceptor: WaffleInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor) // TODO. JSESSIONID 인터셉터
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

    @Singleton
    @Provides
    fun provideCommentService(retrofit: Retrofit): CommentService {
        return retrofit.create(CommentService::class.java)
    }

    @Singleton
    @Provides
    fun provideCommentRepository(commentService: CommentService): CommentRepository {
        return DefaultCommentRepository(commentService)
    }

    @Singleton
    class WaffleInterceptor @Inject constructor(
        private  val loginUserPreference: LoginUserPreferenceModule
    ): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val pref = runBlocking {
                loginUserPreference.jsessionidFlow.first()
            }

            val newRequest = request().newBuilder()
                .apply {
                    if (pref != null) addHeader(JSESSIONID, pref)
                }
                .build()

            proceed(newRequest)
        }
    }
}
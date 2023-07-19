package me.varoa.studentprofiles.core.di

import android.content.Context
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import logcat.logcat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {
    companion object {
        private const val DEFAULT_TIMEOUT = 2L
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor { message ->
                    logcat("API") { message }
                }.setLevel(HttpLoggingInterceptor.Level.BASIC),
            )
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
            .build()
    }

    @Singleton
    @Provides
    fun provideCoilLoader(
        @ApplicationContext context: Context,
        client: OkHttpClient,
    ) = ImageLoader.Builder(context)
        .okHttpClient(client)
        .crossfade(true)
        .build()
}

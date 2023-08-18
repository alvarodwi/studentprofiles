package me.varoa.studentprofiles.core.di

import android.content.Context
import coil.ImageLoader
import logcat.logcat
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val REMOTE_MODULE =
    module {
        val defaultTimeout = 2L

        fun provideOkHttpClient(): OkHttpClient {
            val hostUrl = "schale.gg"
            val hostSslPin =
                listOf(
                    "sha256/uW3dqS3J0/Lb456cj4bLKeaSviwu838+IjZ1BNN+z5g=",
                )

            val certificatePinner =
                CertificatePinner.Builder().also { builder ->
                    hostSslPin.forEach { pin -> builder.add(hostUrl, pin) }
                }.build()

            return OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor { message ->
                        logcat("API") { message }
                    }.setLevel(HttpLoggingInterceptor.Level.BASIC),
                )
                .connectTimeout(defaultTimeout, TimeUnit.MINUTES)
                .readTimeout(defaultTimeout, TimeUnit.MINUTES)
                .certificatePinner(certificatePinner)
                .build()
        }

        single { provideOkHttpClient() }

        fun provideCoilLoader(
            context: Context,
            client: OkHttpClient,
        ) = ImageLoader.Builder(context)
            .okHttpClient(client)
            .crossfade(true)
            .build()

        single { provideCoilLoader(androidContext(), get()) }
    }

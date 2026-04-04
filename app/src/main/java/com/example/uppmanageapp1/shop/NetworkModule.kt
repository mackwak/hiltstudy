package com.example.uppmanageapp1.shop

import com.example.uppmanageapp1.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

interface ShopifyService {
    @GET("admin/api/2024-01/products.json")
    suspend fun getProducts(): Response<ProductsResponse>
}


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-Shopify-Access-Token", BuildConfig.SHOPIFY_ACCESS_TOKEN)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideShopifyService(client: OkHttpClient): ShopifyService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Replace with actual shop URL or dynamic logic
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // use Gson converter
            .build()
            .create(ShopifyService::class.java)
    }
}
package com.hidden.artify.di

import android.content.Context
import com.hidden.artify.data.preferences.ArtPreferenceManager
import com.hidden.artify.data.preferences.ArtSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providePreferences(context: Context): ArtSharedPreferences =
        ArtPreferenceManager(context)

}
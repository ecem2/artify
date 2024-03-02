package com.hidden.artify.di

import com.hidden.artify.data.preferences.ArtPreferenceManager
import com.hidden.artify.data.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceModule {

    @Binds
    abstract fun providePreferences(preferences: ArtPreferenceManager): Preferences

}
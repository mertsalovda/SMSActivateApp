package ru.mertsalovda.smsactivateapp.di

import dagger.Component
import ru.mertsalovda.smsactivateapp.ui.activateflow.PayViewMode
import ru.mertsalovda.smsactivateapp.ui.activateflow.activate.ActivateViewMode
import ru.mertsalovda.smsactivateapp.ui.auth.AuthFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {

    companion object {
        fun create(): AppComponent {
            return DaggerAppComponent.builder().appModule(AppModule()).build()
        }
    }

    fun inject(activateViewMode: ActivateViewMode)
    fun inject(payViewMode: PayViewMode)
    fun inject(fragment: AuthFragment)
}
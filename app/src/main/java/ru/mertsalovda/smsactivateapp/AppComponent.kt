package ru.mertsalovda.smsactivateapp

import dagger.Component
import ru.mertsalovda.smsactivateapp.ui.activateflow.ActivateViewMode

@Component(
    modules = []
)
interface AppComponent {

    companion object {
        fun create(): AppComponent {
            return DaggerAppComponent.builder().build()
        }
    }

    fun inject(activateViewMode: ActivateViewMode)
}
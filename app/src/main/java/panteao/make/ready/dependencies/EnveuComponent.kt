package panteao.make.ready.dependencies

import panteao.make.ready.dependencies.modules.UserPreferencesModule
import dagger.Component
import panteao.make.ready.PanteaoApplication
import panteao.make.ready.activities.homeactivity.ui.HomeActivity
import panteao.make.ready.activities.splash.ui.ActivitySplash
import javax.inject.Singleton


@Singleton
@Component(modules = [UserPreferencesModule::class])
interface EnveuComponent {
    fun inject (panteaoApplication: PanteaoApplication)
    fun inject(splash: ActivitySplash)
    fun inject(homeActivity: HomeActivity)

}
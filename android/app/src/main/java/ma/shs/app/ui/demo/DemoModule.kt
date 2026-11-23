package ma.shs.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ma.shs.app.ui.demo.DemoRepository
import ma.shs.app.ui.demo.FakeDemoRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DemoModule {
    @Binds
    abstract fun bindDemoRepository(impl: FakeDemoRepository): DemoRepository
}
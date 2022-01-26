package com.example.android.hilt.di

import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


/*
*
* Can we add this function to the DatabaseModule class we created before, or do we need a new module? There are multiple reasons why we should create a new module:
* 1. For better organization, a module's name should convey the type of information it provides.
*    For example, it wouldn't make sense to include navigation bindings in a module named DatabaseModule.
* 2. The DatabaseModule module is installed in the SingletonComponent, so the bindings are available in the application container.
*    Our new navigation information (i.e. AppNavigator) needs information specific to the activity becauseAppNavigatorImpl has an Activity as a dependency. Therefore, it must be installed in the Activity container instead of the Application container, since that's where information about the Activity is available.
* 3. Hilt Modules cannot contain both non-static and abstract binding methods, so you cannot place @Binds and @Provides annotations in the same class
* */
@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {

    /*
    * @Binds must annotate an abstract function (since it's abstract, it doesn't contain any code and the class needs to be abstract too).
    * The return type of the abstract function is the interface we want to provide an implementation for (i.e. AppNavigator).
    * The implementation is specified by adding a unique parameter with the interface implementation type (i.e. AppNavigatorImpl).
    * */
    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}
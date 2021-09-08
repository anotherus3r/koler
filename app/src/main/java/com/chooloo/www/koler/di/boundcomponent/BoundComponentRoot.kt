package com.chooloo.www.koler.di.boundcomponent

import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.koler.di.component.ComponentRoot
import com.chooloo.www.koler.interactor.dialog.DialogInteractor
import com.chooloo.www.koler.interactor.navigation.NavigationInteractor
import com.chooloo.www.koler.interactor.permission.PermissionInteractor
import com.chooloo.www.koler.interactor.proximity.ProximityInteractor
import com.chooloo.www.koler.interactor.screen.ScreenInteractor
import com.chooloo.www.koler.interactor.sim.SimInteractor
import io.reactivex.disposables.CompositeDisposable

interface BoundComponentRoot : ComponentRoot {
    val lifecycleOwner: LifecycleOwner
    val disposables: CompositeDisposable

    val simInteractor: SimInteractor
    val dialogInteractor: DialogInteractor
    val screenInteractor: ScreenInteractor
    val proximityInteractor: ProximityInteractor
    val permissionInteractor: PermissionInteractor
    val navigationInteractor: NavigationInteractor
}
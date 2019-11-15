package io.reactivex.bus

import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.Subject

class RxBusEvent(var subject: Subject<Any>, var disposable: DisposableObserver<*>)
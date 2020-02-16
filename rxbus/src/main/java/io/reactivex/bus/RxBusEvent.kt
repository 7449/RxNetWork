package io.reactivex.bus

import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.subjects.Subject

class RxBusEvent(val subject: Subject<Any>, val disposable: DisposableObserver<*>)
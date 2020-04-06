package io.reactivex.bus

import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.Subject


class RxBusEvent(val subject: Subject<Any>, val disposable: DisposableObserver<*>)
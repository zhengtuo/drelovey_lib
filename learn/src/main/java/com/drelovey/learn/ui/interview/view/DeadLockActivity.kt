package com.drelovey.learn.ui.interview.view

import android.graphics.Point
import com.alibaba.android.arouter.facade.annotation.Route
import com.drelovey.common.base.activity.BaseActivity
import com.drelovey.common.base.viewmodel.EmptyViewModel
import com.drelovey.learn.R
import com.drelovey.learn.data.model.Account
import com.drelovey.learn.data.model.Dispatcher
import com.drelovey.learn.data.model.Taxi
import com.drelovey.learn.databinding.ActivityDeadlockBinding
import com.drelovey.provider.router.RouterPath
import dagger.hilt.android.AndroidEntryPoint

@Route(path = RouterPath.PATH_INTERVIEW_DEADLOCK)
@AndroidEntryPoint
class DeadLockActivity :
    BaseActivity<ActivityDeadlockBinding, EmptyViewModel>(R.layout.activity_deadlock) {

    // 加时赛锁
    val tieLock = Any()

    var thread1: Thread? = null
    var thread2: Thread? = null

    var accountA: Account? = null
    var accountB: Account? = null

    var dispatcher: Dispatcher? = null
    var taxi: Taxi? = null

    override fun initialization() {
        binding {
            lifecycleOwner = this@DeadLockActivity
        }

        initDeadLock()

        binding.btStatic.setOnClickListener {
            thread1 = Thread {
                while (true) {
                    a()
                    if (thread1 != null && thread1!!.isInterrupted) {
                        break
                    }
                }
            }.apply {
                start()
            }

            thread2 = Thread {
                while (true) {
                    b()
                    if (thread2 != null && thread2!!.isInterrupted) {
                        break
                    }
                }
            }.apply {
                start()
            }
        }

        binding.btDynamic.setOnClickListener {
            thread1 = Thread {
                while (true) {
                    transefMoney2(accountA!!, accountB!!, 5.0)
                    if (thread1 != null && thread1!!.isInterrupted) {
                        break
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        break
                    }
                }
            }.apply {
                start()
            }

            thread2 = Thread {
                while (true) {
                    transefMoney2(accountB!!, accountA!!, 5.0)
                    if (thread2 != null && thread2!!.isInterrupted) {
                        break
                    }
                    try {
                        Thread.sleep(1200)
                    } catch (e: Exception) {
                        break
                    }
                }
            }.apply {
                start()
            }
        }

        binding.btObject.setOnClickListener {
            dispatcher = Dispatcher()
            taxi = Taxi(dispatcher!!)
            thread1 = Thread {
                while (true) {
                    taxi?.setLocation(Point(1, 2))
                    if (thread2 != null && thread2!!.isInterrupted) {
                        break
                    }
//                    try {
//                        Thread.sleep(1000)
//                    } catch (e: Exception) {
//                        break
//                    }
                }

            }.apply {
                start()
            }

            thread2 = Thread {
                while (true) {
                    dispatcher?.getImage()
                    if (thread2 != null && thread2!!.isInterrupted) {
                        break
                    }
//                    try {
//                        Thread.sleep(1200)
//                    } catch (e: Exception) {
//                        break
//                    }
                }
            }.apply {
                start()
            }
        }
    }

    private fun initDeadLock() {

        condition()

        //在JAVA编程中,有3种典型的死锁类型：
        //静态的锁顺序死锁,动态的锁顺序死锁,协作对象之间发生的死锁。

        staticStateDeadLock()
        dynamicStateDeadLock()
    }

    fun condition() {
        //死锁产生的条件
        //一般来说,要出现死锁问题需要满足以下条件：
        //1.互斥条件：一个资源每次只能被一个线程使用。
        //2.请求与保持条件：一个线程因请求资源而阻塞时，对已获得的资源保持不放。
        //3.不剥夺条件：线程已获得的资源,在未使用完之前,不能强行剥夺。
        //4.循环等待条件：若干线程之间形成一种头尾相接的循环等待资源关系。
    }

    //静态的锁顺序死锁
    private fun staticStateDeadLock() {
        //a和b两个方法都需要获得A锁和B锁。
        //一个线程执行a方法且已经获得了A锁,在等待B锁;另一个线程执行了b方法且已经获得了B锁,在等待A锁。这种状态,就是发生了静态的锁顺序死锁。
        //a()
        //b()
    }

    //可能发生静态锁顺序死锁的代码
    private val lockA: Any = Any()
    private val lockB: Any = Any()

    private fun a() {
        synchronized(lockA) {
            synchronized(lockB) {
                println("function a")
            }
        }
    }

    private fun b() {
        synchronized(lockB) {
            synchronized(lockA) {
                println("function b")
            }
        }
    }

    //解决静态的锁顺序死锁的方法就是：所有需要多个锁的线程，都要以相同的顺序来获得锁。
    private fun a2() {
        synchronized(lockA) {
            synchronized(lockB) {
                println("function a")
            }
        }
    }

    private fun b2() {
        synchronized(lockA) {
            synchronized(lockB) {
                println("function b")
            }
        }
    }

    //动态的锁顺序死锁
    private fun dynamicStateDeadLock() {
        //动态的锁顺序死锁是指两个线程调用同一个方法时,传入的参数颠倒造成的死锁。
        //如下代码,一个线程调用了transferMoney方法并传入参数accountA,accountB;
        //另一个线程调用了transferMoney方法并传入参数accountA,accountB;另一个线程调用了transferMoney方法并传入参数accountB,accountA。
        //此时就可能发生在静态的锁顺序死锁中存在的问题,即：第一个线程获得了accountA锁并等待accountB锁,第二个线程获得了accountB锁并等待accountA锁。

        accountA = Account()
        accountB = Account()
    }

    private fun transefMoney(fromAccount: Account, toAccount: Account, amount: Double) {
        synchronized(fromAccount) {
            synchronized(toAccount) {
                if (fromAccount < amount) {
                    throw Exception()
                } else {
                    fromAccount.minus(amount)
                    toAccount.add(amount)
                }
                println("function transefMoney" + fromAccount.moneycount)
            }
        }
    }

    fun transefMoney2(fromAccount: Account, toAccount: Account, amount: Double) {
        class Helper {
            fun tranfer() {
                if (fromAccount < amount) {
                    throw Exception()
                } else {
                    fromAccount.minus(amount)
                    toAccount.add(amount)
                }
                println("function transefMoney" + fromAccount.moneycount)
            }
        }

        val fromHashCode = System.identityHashCode(fromAccount)
        val toHashCode = System.identityHashCode(toAccount)

        if (fromHashCode < toHashCode) {
            synchronized(fromAccount) {
                synchronized(toAccount) {
                    Helper().tranfer()
                }
            }
        } else if (fromHashCode > toHashCode) {
            synchronized(toAccount) {
                synchronized(fromAccount) {
                    Helper().tranfer()
                }
            }
        } else {
            synchronized(tieLock) {
                synchronized(fromAccount) {
                    synchronized(toAccount) {
                        Helper().tranfer()
                    }
                }
            }
        }
    }

    //协作对象之间发生的死锁
    private fun cooperationObjectDeadLock() {
        //有时,死锁并不会那么明显,比如两个相互协作的类之间的死锁,比如下面的代码：一个线程调用了Taxi对象的setLocation方法,另一个线程调用了Dispatcher对象的getImage方法。
        //此时可能会发生,第一个线程持有Taxi对象锁并等待Dispatcher对象锁,另一个线程持有Dispatcher对象并等待Taxi对象锁。
    }

    override fun onDestroy() {
        super.onDestroy()
        thread1?.interrupt()
        thread2?.interrupt()
    }
}
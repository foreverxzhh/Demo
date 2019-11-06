package com.hua.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int SIZE = 10000;
    private long mTime;
    private TextView mLog;
    private TextView mTv1, mTv2, mTv3;
    private TextView mTh1, mTh2, mTime1, mTime2;
    private Button mStart1, mStart2;
    private static Handler sHandler;
    private ScheduledThreadPoolExecutor mThreadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //1、ScheduledThreadPool 示范
        mLog = findViewById(R.id.log);
        mLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTv1 = findViewById(R.id.tv1);
        mTv2 = findViewById(R.id.tv2);
        mTv3 = findViewById(R.id.tv3);
        sHandler = new Handler(getMainLooper());
        mTime = System.currentTimeMillis();
        mThreadPoolExecutor = new ScheduledThreadPoolExecutor(3);
        //一旦启用已延迟的任务就执行它，但是有关何时启用，启用后何时执行则没有任何实时保证。按照提交的先进先出 (FIFO) 顺序来启用那些被安排在同一执行时间的任务。
        //特别是，因为它作为一个使用 corePoolSize 线程和一个无界队列的固定大小的池，所以调整 maximumPoolSize 没有什么效果。
        //threadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);//shutdown是否影响延迟任务
        //threadPoolExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);//shutdown是否影响定期任务
        //threadPoolExecutor.execute(getRunnable(10));//无参数
        //threadPoolExecutor.submit(getRunnable(10));//返回参数
        for (int i = 0; i < 10; i++) {
            //threadPoolExecutor.schedule(getRunnable(10), 1, TimeUnit.SECONDS);//延迟执行
            mThreadPoolExecutor.scheduleAtFixedRate(getRunnable(i), 2, 10, TimeUnit.SECONDS);//定期执行，开始时间
            //threadPoolExecutor.scheduleWithFixedDelay(getRunnable(i), 1, 10, TimeUnit.SECONDS);//定期执行，结束时间
        }
        //threadPoolExecutor.shutdown();//结束，等待当前执行任务完成
        //threadPoolExecutor.shutdownNow();//立即结束

        //2、使用线程池和不使用线程池的区别
        mStart1 = findViewById(R.id.start1);
        mStart2 = findViewById(R.id.start2);
        mTh1 = findViewById(R.id.th1);
        mTh2 = findViewById(R.id.th2);
        mTime1 = findViewById(R.id.time1);
        mTime2 = findViewById(R.id.time2);
        mStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useThreadPool(SIZE);
            }
        });
        mStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unUseThreadPool(SIZE);
            }
        });
    }

    private void useThreadPool(int count) {
        long startTime = System.currentTimeMillis();
        final List<Integer> list = new LinkedList<>();
        final Random random = new Random();

        /*
         * corePoolSize - 池中所保存的线程数，包括空闲线程。
         * maximumPoolSize - 池中允许的最大线程数。
         * keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
         * unit - keepAliveTime 参数的时间单位。
         * workQueue - 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(count));
        for (int i = 0; i < count; i++) {

            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    list.add(random.nextInt());
                }
            });

        }

        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final long delta = (System.currentTimeMillis() - startTime);
        Log.d("hua_demo", "测试使用线程池:");
        Log.d("hua_demo", "" + delta);
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                mTh1.setBackgroundResource(android.R.color.holo_green_light);
                mTh1.setText("Finish!");
                mTime1.setText(delta + "");
            }
        });
    }

    public void unUseThreadPool(int count) {
        long startTime = System.currentTimeMillis();
        final List<Integer> l = new LinkedList<>();
        final Random random = new Random();

        for (int i = 0; i < count; i++) {

            Thread thread = new Thread() {
                public void run() {
                    l.add(random.nextInt());
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        final long delta = (System.currentTimeMillis() - startTime);
        Log.d("hua_demo", "测试未使用线程池:");
        Log.d("hua_demo", "" + delta);
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                mTh2.setBackgroundResource(android.R.color.holo_green_light);
                mTh2.setText("Finish!");
                mTime2.setText(delta + "");
            }
        });
    }

    /**
     * 2019-11-06 10:53:17.281 887-887/com.hua.demo D/hua_demo: 测试使用线程池:
     * 2019-11-06 10:53:17.281 887-887/com.hua.demo D/hua_demo: 29
     * 2019-11-06 10:53:26.252 887-887/com.hua.demo D/hua_demo: 测试未使用线程池:
     * 2019-11-06 10:53:26.252 887-887/com.hua.demo D/hua_demo: 1389
     */

    private Runnable getRunnable(final int i) {
        return new Runnable() {
            @Override
            public void run() {
                if (Thread.currentThread().getName().contains("thread-1")) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTv1.setText("Task " + i);
                            mTv1.setBackgroundResource(android.R.color.holo_red_light);
                        }
                    });
                } else if(Thread.currentThread().getName().contains("thread-2")) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTv2.setText("Task " + i);
                            mTv2.setBackgroundResource(android.R.color.holo_green_light);
                        }
                    });
                } else if(Thread.currentThread().getName().contains("thread-3")) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTv3.setText("Task " + i);
                            mTv3.setBackgroundResource(android.R.color.holo_blue_light);
                        }
                    });
                }

                final String start = (System.currentTimeMillis() - mTime)/1000 + "/" + "Task: " + i + " start; Name: "+Thread.currentThread().getName() + "\n";
                //Log.d("hua_demo", start);
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLog.append(start);
                        int offset = mLog.getLineCount() * mLog.getLineHeight();
                        if (offset > mLog.getHeight()) mLog.scrollTo(0, offset - mLog.getHeight());
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final String end = (System.currentTimeMillis() - mTime)/1000 + "/" + "Task: " + i + "  end ; Name: "+Thread.currentThread().getName() + "\n";
                //Log.d("hua_demo", end);
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLog.append(end);
                        int offset = mLog.getLineCount() * mLog.getLineHeight();
                        if (offset > mLog.getHeight()) mLog.scrollTo(0, offset - mLog.getHeight());
                    }
                });

                if (Thread.currentThread().getName().contains("thread-1")) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTv1.setText("");
                            mTv1.setBackgroundResource(android.R.color.holo_red_dark);
                        }
                    });
                } else if(Thread.currentThread().getName().contains("thread-2")) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTv2.setText("");
                            mTv2.setBackgroundResource(android.R.color.holo_green_dark);
                        }
                    });
                } else if(Thread.currentThread().getName().contains("thread-3")) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTv3.setText("");
                            mTv3.setBackgroundResource(android.R.color.holo_blue_dark);
                        }
                    });
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        mThreadPoolExecutor.shutdownNow();
    }
}
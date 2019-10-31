package com.hua.demo;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(3);
        //特别是，因为它作为一个使用 corePoolSize 线程和一个无界队列的固定大小的池，所以调整 maximumPoolSize 没有什么效果。

        //threadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);//shutdown是否影响延迟任务

        //threadPoolExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);//shutdown是否影响定期任务

        //threadPoolExecutor.execute(getRunnable(10));//无参数

        //threadPoolExecutor.submit(getRunnable(10));//返回参数

        for (int i = 0; i < 3; i++) {
            //threadPoolExecutor.schedule(getRunnable(10), 1, TimeUnit.SECONDS);//延迟执行

            threadPoolExecutor.scheduleAtFixedRate(getRunnable(i), 1, 10, TimeUnit.SECONDS);//定期执行，开始
            /*
            2019-10-31 10:43:10.879 12773-12803/com.hua.demo D/hua_demo: running:0; Thread: 12803; Name: pool-1-thread-1
            2019-10-31 10:43:10.879 12773-12805/com.hua.demo D/hua_demo: running:1; Thread: 12805; Name: pool-1-thread-3
            2019-10-31 10:43:10.879 12773-12804/com.hua.demo D/hua_demo: running:2; Thread: 12804; Name: pool-1-thread-2
            2019-10-31 10:43:13.880 12773-12805/com.hua.demo D/hua_demo: end:1; Thread: 12805; Name: pool-1-thread-3
            2019-10-31 10:43:13.880 12773-12803/com.hua.demo D/hua_demo: end:0; Thread: 12803; Name: pool-1-thread-1
            2019-10-31 10:43:13.880 12773-12804/com.hua.demo D/hua_demo: end:2; Thread: 12804; Name: pool-1-thread-2
            2019-10-31 10:43:20.879 12773-12803/com.hua.demo D/hua_demo: running:0; Thread: 12803; Name: pool-1-thread-1
            2019-10-31 10:43:20.879 12773-12804/com.hua.demo D/hua_demo: running:1; Thread: 12804; Name: pool-1-thread-2
            2019-10-31 10:43:20.879 12773-12805/com.hua.demo D/hua_demo: running:2; Thread: 12805; Name: pool-1-thread-3
            2019-10-31 10:43:23.880 12773-12803/com.hua.demo D/hua_demo: end:0; Thread: 12803; Name: pool-1-thread-1
            2019-10-31 10:43:23.880 12773-12804/com.hua.demo D/hua_demo: end:1; Thread: 12804; Name: pool-1-thread-2
            2019-10-31 10:43:23.880 12773-12805/com.hua.demo D/hua_demo: end:2; Thread: 12805; Name: pool-1-thread-3
             */

            //threadPoolExecutor.scheduleWithFixedDelay(getRunnable(i), 1, 10, TimeUnit.SECONDS);//定期执行，结束
            /*
            2019-10-31 10:44:26.025 12895-12924/com.hua.demo D/hua_demo: running:0; Thread: 12924; Name: pool-1-thread-1
            2019-10-31 10:44:26.027 12895-12925/com.hua.demo D/hua_demo: running:1; Thread: 12925; Name: pool-1-thread-2
            2019-10-31 10:44:26.027 12895-12926/com.hua.demo D/hua_demo: running:2; Thread: 12926; Name: pool-1-thread-3
            2019-10-31 10:44:29.025 12895-12924/com.hua.demo D/hua_demo: end:0; Thread: 12924; Name: pool-1-thread-1
            2019-10-31 10:44:29.028 12895-12925/com.hua.demo D/hua_demo: end:1; Thread: 12925; Name: pool-1-thread-2
            2019-10-31 10:44:29.028 12895-12926/com.hua.demo D/hua_demo: end:2; Thread: 12926; Name: pool-1-thread-3
            2019-10-31 10:44:39.026 12895-12924/com.hua.demo D/hua_demo: running:0; Thread: 12924; Name: pool-1-thread-1
            2019-10-31 10:44:39.028 12895-12925/com.hua.demo D/hua_demo: running:1; Thread: 12925; Name: pool-1-thread-2
            2019-10-31 10:44:39.028 12895-12926/com.hua.demo D/hua_demo: running:2; Thread: 12926; Name: pool-1-thread-3
            2019-10-31 10:44:42.027 12895-12924/com.hua.demo D/hua_demo: end:0; Thread: 12924; Name: pool-1-thread-1
            2019-10-31 10:44:42.029 12895-12925/com.hua.demo D/hua_demo: end:1; Thread: 12925; Name: pool-1-thread-2
            2019-10-31 10:44:42.029 12895-12926/com.hua.demo D/hua_demo: end:2; Thread: 12926; Name: pool-1-thread-3

             */
        }

        //threadPoolExecutor.shutdown();//结束，等待当前执行任务完成

        //threadPoolExecutor.shutdownNow();//立即结束
    }

    private Runnable getRunnable(final int i) {
        return new Runnable() {
            @Override
            public void run() {
                Log.d("hua_demo", "running:" + i + "; Thread: "+android.os.Process.myTid()+"; Name: "+Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("hua_demo", "end:" + i + "; Thread: "+android.os.Process.myTid()+"; Name: "+Thread.currentThread().getName());
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

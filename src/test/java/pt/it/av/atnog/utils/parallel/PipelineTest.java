package pt.it.av.atnog.utils.parallel;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class PipelineTest {
    static List<Integer> input = new ArrayList<>();

    @BeforeClass
    public static void init() {
        input.add(0);
        input.add(1);
        input.add(2);
        input.add(3);
        input.add(4);
        input.add(5);
        input.add(6);
        input.add(7);
        input.add(8);
        input.add(9);
    }

    @Test(timeout = 3000)
    public void test_one_task() {
        Pipeline pipeline = new Pipeline();
        BlockingQueue<Object> sink = pipeline.sink(), source = pipeline.source();

        pipeline.addLast((Object o, List<Object> l) -> {
            Integer i = (Integer) o;
            l.add(i + 10);
        });

        pipeline.start();

        for (Integer i : input)
            sink.add(i);

        int idx = 0;
        try {
            pipeline.join();
            boolean done = false;
            while (!source.isEmpty()) {
                Object ob = source.take();
                if ((ob instanceof Stop))
                    done = true;
                else {
                    Integer i = (Integer) ob;
                    assertTrue(i == input.get(idx++) + 10);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(timeout = 3000)
    public void test_two_tasks() {
        Pipeline pipeline = new Pipeline();
        BlockingQueue<Object> sink = pipeline.sink(), source = pipeline.source();

        pipeline.addLast((Object o, List<Object> l) -> {
            Integer i = (Integer) o;
            l.add(i + 10);
        });

        pipeline.addLast((Object o, List<Object> l) -> {
            Integer i = (Integer) o;
            l.add(i * 2);
        });

        pipeline.start();

        for (Integer i : input)
            sink.add(i);

        int idx = 0;
        try {
            pipeline.join();
            boolean done = false;
            while (!source.isEmpty()) {
                Object ob = source.take();
                if ((ob instanceof Stop))
                    done = true;
                else {
                    Integer i = (Integer) ob;
                    assertTrue(i == (input.get(idx++) + 10) * 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(timeout = 3000)
    public void test_three_tasks() {
        Pipeline pipeline = new Pipeline();
        BlockingQueue<Object> sink = pipeline.sink(), source = pipeline.source();

        pipeline.addLast((Object o, List<Object> l) -> {
            Integer i = (Integer) o;
            l.add(i + 10);
        });

        pipeline.addLast((Object o, List<Object> l) -> {
            Integer i = (Integer) o;
            l.add(i * 2);
        });

        pipeline.addLast((Object o, List<Object> l) -> {
            Integer i = (Integer) o;
            l.add(i / 5);
        });

        pipeline.start();

        for (Integer i : input)
            sink.add(i);

        int idx = 0;
        try {
            pipeline.join();
            boolean done = false;
            while (!source.isEmpty()) {
                Object ob = source.take();
                if ((ob instanceof Stop))
                    done = true;
                else {
                    Integer i = (Integer) ob;
                    assertTrue(i == ((input.get(idx++) + 10) * 2) / 5);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package co.mindie.cindy.worker.queue;

import java.util.List;

/**
 * Created by simoncorsin on 29/09/14.
 */
public interface WorkProcessor<T> {

	void processItems(List<T> items);

}

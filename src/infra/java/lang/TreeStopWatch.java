package lang;

import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * Inspirado em http://gustlik.wordpress.com/2008/06/20/hierarchical-stopwatch-measuring-code-performance/
 */
public class TreeStopWatch {

	private Stack<Event> eventStack = new Stack<Event>();
	private List<Event> events = Lists.newArrayList();
	private int depth = 0;

	public TreeStopWatch startEvent(String eventName) {
		events.add(eventStack.push(new Event(depth, eventName)));
		depth++;
		return this;
	}

	public TreeStopWatch stopEvent() {
		depth--;
		eventStack.pop().stop();
		return this;
	}

	public long getTotalDuration() {
		return events.get(0).getDuration();
	}

	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		if (!eventStack.isEmpty()) {
			sb.append("WARNING: same events have not been marked as closed, ");
			sb.append("the summary might contain wrong data...\n");
		}
		for (Event event : events) {
			sb.append(StringUtils.repeat('\t', event.getDepth())).append(event.getName()).append(" : ").append(event.getDuration()).append(" ms\n");
		}
		return sb.toString();
	}

	public static class Event {
		private int depth;
		private String name;
		private long startTime;
		private long duration;

		public Event(int depth, String name) {
			this.depth = depth;
			this.name = name;
			startTime = System.currentTimeMillis();
		}

		public int getDepth() {
			return depth;
		}

		public long getDuration() {
			return duration;
		}

		public String getName() {
			return name;
		}

		public Event stop() {
			duration = System.currentTimeMillis() - startTime;
			return this;
		}

	}

}

package com.n26.utility;

import java.math.BigDecimal;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

@Component
public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {

	private long count;
	private BigDecimal sum = BigDecimal.ZERO;
	private BigDecimal min;
	private BigDecimal max;

	public BigDecimalSummaryStatistics() {
		count = 0;
		sum = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP);
		min = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP);
		max = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public void clear() {
		count = 0;
		sum = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP);
		min = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP);
		max = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	@Override
	public synchronized void accept(BigDecimal value) {
		if (count == 0) {
			count++;
			sum = value;
			min = value;
			max = value;
			return;
		}
		++count;
		sum = sum.add(value);
		min = min.compareTo(value) > 0 ? value : min;
		max = max.compareTo(value) < 0 ? value : max;
	}

	public void combine(BigDecimalSummaryStatistics other) {
		count += other.count;
		sum = sum.add(other.sum);
		min = min.min(other.min);
		max = max.max(other.max);
	}

	public BigDecimalSummaryStatistics merge(BigDecimalSummaryStatistics other) {
		if (other.count > 0) {
			if (count == 0) {
				count = other.count;
				sum = other.sum;
				min = other.min;
				max = other.max;
			} else {
				sum = sum.add(other.sum);
				if (min.compareTo(other.min) > 0)
					min = other.min;
				if (max.compareTo(other.max) < 0)
					max = other.max;
				count += other.count;
			}
		}
		return this;
	}

	public final long getCount() {
		return count;
	}

	public final BigDecimal getSum() {
		return sum;
	}

	public final BigDecimal getMin() {
		return min;
	}

	public final BigDecimal getMax() {
		return max;
	}

	public final BigDecimal getAverage() {
		return getCount() > 0 ? getSum().divide(new BigDecimal(getCount()), 2, BigDecimal.ROUND_HALF_UP)
				: new BigDecimal(0);
	}

	@Override
	public String toString() {
		return String.format("%s{count=%d, sum=%f, min=%f, average=%f, max=%f}", this.getClass().getSimpleName(),
				getCount(), getSum(), getMin(), getAverage(), getMax());
	}

}
